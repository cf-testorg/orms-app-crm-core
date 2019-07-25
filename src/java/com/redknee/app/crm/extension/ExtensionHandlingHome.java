/*
 * This code is a protected work and subject to domestic and international copyright
 * law(s). A complete listing of authors of this work is readily available. Additionally,
 * source code is, by its very nature, confidential information and inextricably contains
 * trade secrets and other information proprietary, valuable and sensitive to Redknee. No
 * unauthorized use, disclosure, manipulation or otherwise is permitted, and may only be
 * used in accordance with the terms of the license agreement entered into with Redknee
 * Inc. and/or its subsidiaries.
 * 
 * Copyright (c) Redknee Inc. and its subsidiaries. All Rights Reserved.
 */
package com.redknee.app.crm.extension;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.redknee.app.crm.state.FinalStateAware;
import com.redknee.app.crm.support.ExtensionSupportHelper;
import com.redknee.app.crm.support.HomeSupportHelper;
import com.redknee.framework.xhome.beans.CompoundIllegalStateException;
import com.redknee.framework.xhome.beans.ExceptionListener;
import com.redknee.framework.xhome.beans.Identifiable;
import com.redknee.framework.xhome.beans.IllegalPropertyArgumentException;
import com.redknee.framework.xhome.beans.SafetyUtil;
import com.redknee.framework.xhome.beans.XBeans;
import com.redknee.framework.xhome.beans.xi.PropertyInfo;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.context.ContextAware;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.home.Adapter;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.HomeOperationEnum;
import com.redknee.framework.xhome.home.HomeProxy;
import com.redknee.framework.xhome.webcontrol.OutputWebControl;
import com.redknee.framework.xlog.log.InfoLogMsg;


/**
 * This class deals with provisioning/deprovisioning extension changes to parent beans.
 * Extension data is stored in its own home, so this class finds the appropriate home for
 * each extension and proxies the create/store/remove request appropriately.
 * 
 * Note: This home will only work properly for parent beans with "simple" keys (i.e. 1
 * property).
 * 
 * @author aaron.gourley@redknee.com
 * @since 8.2
 */
public class ExtensionHandlingHome<T extends Extension> extends HomeProxy
{

    protected final PropertyInfo extKeyProp_;
    protected final Adapter foreignKeyAdapter_;
    protected final Class<T> extType_;
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String BYPASS_UPDATE_TO_EXTENSION = "bypassExtensionUpdate";


    public ExtensionHandlingHome(final Context ctx, Class<T> preferredType, PropertyInfo extensionForeignKeyProp,
            final Home delegate)
    {
        this(ctx, preferredType, extensionForeignKeyProp, new ExtensionForeignKeyAdapter(extensionForeignKeyProp),
                delegate);
    }


    public ExtensionHandlingHome(final Context ctx, Class<T> preferredType, PropertyInfo extensionForeignKeyProp,
            Adapter foreignKeyAdapter, final Home delegate)
    {
        super(ctx, delegate);
        extKeyProp_ = extensionForeignKeyProp;
        foreignKeyAdapter_ = foreignKeyAdapter;
        extType_ = preferredType;
    }


    @Override
    public Object create(final Context ctx, final Object obj) throws HomeException
    {
        Context subCtx = ctx.createSubContext();
        subCtx.put(HomeOperationEnum.class, HomeOperationEnum.CREATE);
        validateDependencyValidatableExtensions(subCtx, (ExtensionAware) obj);
        Object returnObj = super.create(subCtx, obj);
        if (foreignKeyAdapter_ != null)
        {
            // Must set the key value of the extensions after the bean is created
            // because the key may have been set during the parent bean's create call.
            returnObj = foreignKeyAdapter_.unAdapt(subCtx, returnObj);
        }
        if (returnObj instanceof ExtensionAware && !subCtx.has(BYPASS_UPDATE_TO_EXTENSION))
        {
            createAllExtensions(subCtx, (ExtensionAware) returnObj);
        }
        return returnObj;
    }


    @Override
    public Object store(final Context ctx, final Object obj) throws HomeException
    {
        Context subCtx = ctx.createSubContext();
        subCtx.put(HomeOperationEnum.class, HomeOperationEnum.STORE);
        validateDependencyValidatableExtensions(subCtx, (ExtensionAware) obj);
        final Object returnObj = super.store(subCtx, obj);
        if (returnObj instanceof ExtensionAware && !subCtx.has(BYPASS_UPDATE_TO_EXTENSION))
        {
            updateAllExtensions(subCtx, (ExtensionAware) returnObj);
        }
        return returnObj;
    }


    @Override
    public void remove(final Context ctx, final Object obj) throws HomeException
    {
        Context subCtx = ctx.createSubContext();
        subCtx.put(HomeOperationEnum.class, HomeOperationEnum.REMOVE);
        validateDependencyValidatableExtensions(subCtx, (ExtensionAware) obj);
        if (obj instanceof Identifiable && !subCtx.has(BYPASS_UPDATE_TO_EXTENSION))
        {
            removeAllExtensions(subCtx, (Identifiable) obj);
        }
        super.remove(subCtx, obj);
    }


    private void validateDependencyValidatableExtensions(Context ctx, ExtensionAware parentBean) throws HomeException
    {
        PropertyInfo extProp = parentBean.getExtensionHolderProperty();
        if (extProp != null)
        {
            final List<ExtensionHolder> extensions = (List<ExtensionHolder>) extProp.get(parentBean);
            if (extensions.size() > 0)
            {
                final Context sCtx = ctx.createSubContext();
                sCtx.put(BYPASS_UPDATE_TO_EXTENSION, Boolean.TRUE);
                ExtensionSupportHelper.get(sCtx).setParentBean(sCtx, parentBean);
                CompoundIllegalStateException cise = new CompoundIllegalStateException();
                for (ExtensionHolder extensionHolder : extensions)
                {
                    Extension extension = extensionHolder.getExtension();
                    
                    if (!ExtensionSupportHelper.get(ctx).isExtensionLicensed(ctx, extension.getClass()))
                    {
                        cise.thrown(new IllegalStateException("Extension " + extension.getClass().getName() + " is not supported."));
                    }
                    else if (extension instanceof DependencyValidatableExtension)
                    {
                        try
                        {
                            ((DependencyValidatableExtension) extension).validateDependency(sCtx);
                        }
                        catch (IllegalStateException e)
                        {
                            cise.thrown(e);
                        }
                    }
                }
                try
                {
                    cise.throwAll();
                }
                catch (Throwable t)
                {
                    throw new HomeException(t.getMessage(), t);
                }
            }
        }
    }


    private void createAllExtensions(Context ctx, ExtensionAware parentBean) throws HomeException
    {
        PropertyInfo extProp = parentBean.getExtensionHolderProperty();
        if (extProp != null)
        {
            final List<ExtensionHolder> extensions = (List<ExtensionHolder>) extProp.get(parentBean);
            if (extensions.size() > 0)
            {
                final Context sCtx = ctx.createSubContext();
                sCtx.put(BYPASS_UPDATE_TO_EXTENSION, Boolean.TRUE);
                ExtensionSupportHelper.get(sCtx).setParentBean(sCtx, parentBean);
                
                ExceptionListener el = (ExceptionListener) ctx.get(ExceptionListener.class);
                for (ExtensionHolder extensionHolder : extensions)
                {
                    Extension extension = extensionHolder.getExtension();
                    if (extension instanceof ContextAware)
                    {
                        ((ContextAware) extension).setContext(ctx);
                    }
                    final Home home = ExtensionSupportHelper.get(sCtx).getExtensionHome(sCtx, extension);
                    if (home != null)
                    {
                        try
                        {
                            extension = (Extension) home.create(sCtx, extension);
                            extensionHolder.setExtension(extension);
                        }
                        catch (HomeException e)
                        {
                            if (el != null)
                            {
                                if (e.getCause() instanceof CompoundIllegalStateException)
                                {
                                   ((CompoundIllegalStateException)e.getCause()).rethrow(el);
                                }
                                else if (e.getCause() instanceof IllegalPropertyArgumentException)
                                {
                                    el.thrown(e.getCause());
                                }
                                else
                                {
                                    el.thrown(e);
                                }
                            }
                            else
                            {
                                throw e;
                            }
                        }
                    }
                    else if (el != null)
                    {
                        el.thrown(new IllegalArgumentException("Extension not supported: " + (extension != null ? extension.getClass().getName() : null)));
                    }
                }
            }
        }
    }


    private void removeAllExtensions(Context ctx, Identifiable parentBean) throws HomeException
    {
        final List<T> extensions = ExtensionSupportHelper.get(ctx).getAllExtensions(ctx, extType_,
                new EQ(extKeyProp_, parentBean.ID()));
        if (extensions.size() > 0)
        {
            final Context sCtx = ctx.createSubContext();
            sCtx.put(BYPASS_UPDATE_TO_EXTENSION, Boolean.TRUE);
            ExtensionSupportHelper.get(sCtx).setParentBean(sCtx, parentBean);
            for (T extension : extensions)
            {
                if (extension instanceof ContextAware)
                {
                    ((ContextAware) extension).setContext(ctx);
                }
                final Home home = ExtensionSupportHelper.get(sCtx).getExtensionHome(sCtx, extension);
                if (home != null)
                {
                    home.remove(sCtx, extension);
                }
            }
        }
    }


    private void updateAllExtensions(Context ctx, ExtensionAware parentBean) throws HomeException
    {
        if (parentBean instanceof Identifiable)
        {
            PropertyInfo extProp = parentBean.getExtensionHolderProperty();
            if (extProp != null)
            {
                // Get a list of the existing extensions. We'll need this to determine
                // which
                // extensions were added, modified, and removed from the list.
                final Map<Class<T>, List<T>> oldExtensions = ExtensionSupportHelper.get(ctx).getExistingExtensionMap(
                        ctx, extType_, new EQ(extKeyProp_, ((Identifiable) parentBean).ID()));
                // Update the extension data for the current extension list
                final List<ExtensionHolder> newExtensions = (List<ExtensionHolder>) extProp.get(parentBean);
                if (newExtensions.size() > 0 || oldExtensions.size() > 0)
                {
                    boolean isInFinalState = false;
                    if (parentBean instanceof FinalStateAware)
                    {
                        FinalStateAware finalStateAware = (FinalStateAware) parentBean;
                        isInFinalState = finalStateAware.getFinalStates().contains(finalStateAware.getAbstractState());
                    }

                    ExceptionListener el = (ExceptionListener) ctx.get(ExceptionListener.class);
                    
                    final Context sCtx = ctx.createSubContext();
                    sCtx.put(BYPASS_UPDATE_TO_EXTENSION, Boolean.TRUE);
                    ExtensionSupportHelper.get(sCtx).setParentBean(sCtx, parentBean);
                    for (ExtensionHolder extensionHolder : newExtensions)
                    {
                        try
                        {
                            Extension extension = extensionHolder.getExtension();
                            if (extension instanceof ContextAware)
                            {
                                ((ContextAware) extension).setContext(ctx);
                            }
                            Class<? extends Extension> extensionType = extension.getClass();
                            final Home home = ExtensionSupportHelper.get(sCtx).getExtensionHome(sCtx, extension);
                            if (home != null)
                            {
                                final Object extensionId = extension.ID();
                                if (!isCreateMode(sCtx, extensionHolder))
                                {
                                    final List<T> oldExtensionList = oldExtensions.get(extensionType);
                                    if (oldExtensionList != null && oldExtensionList.size() > 0)
                                    {
                                        final Iterator<T> iterator = oldExtensionList.iterator();
                                        while (iterator.hasNext())
                                        {
                                            T oldExtension = iterator.next();
                                            if (SafetyUtil.safeEquals(extensionId, oldExtension.ID()))
                                            {
                                                // Remove from old extensoins list
                                                // iterator first. We don't want the
                                                // extension to get removed if an
                                                // exception occurs during validation.
                                                iterator.remove();

                                                // Skip the extension update if the extension didn't change
                                                if (!SafetyUtil.safeEquals(extension, oldExtension))
                                                {
                                                    // Update the extension unless it's
                                                    // deactivable and parent bean is going
                                                    // into final state. In this case, call
                                                    // the deactivate method
                                                    if (!(isInFinalState && extension instanceof DeactivableExtension))
                                                    {
                                                        extension = (Extension) home.store(sCtx, extension);
                                                        extensionHolder.setExtension(extension);
                                                    }
                                                    else
                                                    {
                                                        try
                                                        {
                                                            ((DeactivableExtension) extension).deactivate(ctx);
                                                        }
                                                        catch (ExtensionInstallationException e)
                                                        {
                                                            throw new HomeException("Unable to deactivate extension '" + extension.getName(ctx) + "': " + e.getMessage(), e);
                                                        }
                                                    }
                                                }

                                                // Break from while loop because there will not be
                                                // more than one existing extension with the same ID
                                                break;
                                            }
                                        }
                                    }
                                }
                                else
                                {
                                    // This is a new extension added to the bean on this
                                    // update
                                    // Add the extension unless it's deactivable and parent bean is going into final state
                                    if (!(isInFinalState && extension instanceof DeactivableExtension))
                                    {
                                        extension = (Extension) home.create(sCtx, extension);
                                        extensionHolder.setExtension(extension);
                                    }
                                }
                            }
                            else if (el != null)
                            {
                                el.thrown(new IllegalArgumentException("Unable to create unsupported extension: " + (extension != null ? extension.getClass().getName() : null)));
                            }
                        }
                        catch (HomeException e)
                        {
                            if (el != null)
                            {
                                if (e.getCause() instanceof CompoundIllegalStateException)
                                {
                                   ((CompoundIllegalStateException)e.getCause()).rethrow(el);
                                }
                                else if (e.getCause() instanceof IllegalPropertyArgumentException)
                                {
                                    el.thrown(e.getCause());
                                }
                                else
                                {
                                    el.thrown(e);
                                }
                            }
                            else
                            {
                                throw e;
                            }
                        }
                    }
                    // Now we have to remove the deselected extensions
                    for (Map.Entry<Class<T>, List<T>> entry : oldExtensions.entrySet())
                    {
                        Class<T> extensionClass = entry.getKey();
                        final Home home = ExtensionSupportHelper.get(sCtx).getExtensionHome(sCtx, extensionClass);
                        if (home != null)
                        {
                            for (T oldExtension : entry.getValue())
                            {
                                try
                                {
                                    home.remove(sCtx, oldExtension);
                                }
                                catch (HomeException e)
                                {
                                    if (el != null)
                                    {
                                        el.thrown(e);
                                    }
                                    else
                                    {
                                        throw e;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    private boolean isCreateMode(Context ctx, ExtensionHolder extensionHolder)
    {
        Extension extension = extensionHolder.getExtension();
        Class<? extends Extension> extensionType = extension.getClass();
        boolean isCreateMode = SafetyUtil.safeEquals(ctx.get("MODE", OutputWebControl.EDIT_MODE), OutputWebControl.CREATE_MODE);
        if (!isCreateMode)
        {
            try
            {
                Class homeClass = XBeans.getClass(ctx, extensionType, Home.class);
                Home home = ExtensionSupportHelper.get(ctx).getExtensionHome(ctx, extension);
                if (home != null 
                        && homeClass != null 
                        && !ctx.has(homeClass))
                {
                    // Put home class in context so that HomeSupport works
                    ctx = ctx.createSubContext().put(homeClass, home);
                }
                // Call the Class-aware method so that the ID gets wrapped with proper
                // EQ()
                isCreateMode = !HomeSupportHelper.get(ctx).hasBeans(ctx, extensionType, extension.ID());
            }
            catch (HomeException e)
            {
                new InfoLogMsg(this, "Error occured verifying that we are really in create mode for "
                        + extension.getName(ctx) + " extension: " + extension.ID(), e).log(ctx);
            }
        }
        return isCreateMode;
    }
}
