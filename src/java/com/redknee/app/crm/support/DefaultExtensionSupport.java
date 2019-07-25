/*
 * This code is a protected work and subject to domestic and international
 * copyright law(s). A complete listing of authors of this work is readily 
 * available. Additionally, source code is, by its very nature, confidential 
 * information and inextricably contains trade secrets and other information 
 * proprietary, valuable and sensitive to Redknee, no unauthorised use, 
 * disclosure, manipulation or otherwise is permitted, and may only be used 
 * in accordance with the terms of the licence agreement entered into with 
 * Redknee Inc. and/or its subsidiaries.
 *
 * Copyright (c) Redknee Inc. and its subsidiaries. All Rights Reserved.
 */
package com.redknee.app.crm.support;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.redknee.app.crm.bean.TypeAware;
import com.redknee.app.crm.extension.AdvancedFeatures;
import com.redknee.app.crm.extension.Extension;
import com.redknee.app.crm.extension.ExtensionAware;
import com.redknee.app.crm.extension.ExtensionHolder;
import com.redknee.app.crm.extension.MandatoryExtension;
import com.redknee.app.crm.extension.SubCategoryExtension;
import com.redknee.app.crm.extension.TypeDependentExtension;
import com.redknee.framework.xhome.beans.FacetMgr;
import com.redknee.framework.xhome.beans.XBeans;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.entity.EntityInfo;
import com.redknee.framework.xhome.entity.EntityInfoHome;
import com.redknee.framework.xhome.entity.EntityInfoXInfo;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.language.MessageMgr;
import com.redknee.framework.xhome.xenum.AbstractEnum;
import com.redknee.framework.xlog.log.DebugLogMsg;
import com.redknee.framework.xlog.log.InfoLogMsg;
import com.redknee.framework.xlog.log.LogSupport;
import com.redknee.framework.xlog.log.MajorLogMsg;


/**
 * 
 *
 * @author Aaron Gourley
 * @since 
 */
public class DefaultExtensionSupport implements ExtensionSupport
{
    protected static ExtensionSupport instance_ = null;
    public static ExtensionSupport instance()
    {
        if (instance_ == null)
        {
            instance_ = new DefaultExtensionSupport();
        }
        return instance_;
    }

    protected DefaultExtensionSupport()
    {
    }
    
    public boolean isExtensionLicensed(Context context, Class extensionCls)
    {
        boolean result = true;
        Map<Class, String> extensionLicenseMap = (Map<Class, String>)context.get(EXTENSION_LICENSE_KEYS_CTX_KEY);
        
        if( extensionLicenseMap == null )
        {
            extensionLicenseMap = new HashMap<Class, String>();
            context.put(EXTENSION_LICENSE_KEYS_CTX_KEY, extensionLicenseMap);
        }
        
        String licenseKey = extensionLicenseMap.get(extensionCls);
        
        if (licenseKey != null && !LicensingSupportHelper.get(context).isLicensed(context, licenseKey))
        {
            result = false;
        }
        else if (SubCategoryExtension.class.isAssignableFrom(extensionCls))
        {
            try
            {
                SubCategoryExtension subCategoryExtension = (SubCategoryExtension) XBeans.instantiate(extensionCls, context);
                result = isExtensionLicensed(context, subCategoryExtension.getExtensionCategoryClass());
            }
            catch (Throwable t)
            {
                LogSupport.minor(context, this, "Unable to check license for extension " + extensionCls.getName() +". Unable to instantiate extension to check category's license: " + t.getMessage(), t);
            }
        }
        
        return result;
    }
    
    public void registerExtension(Context ctx, Class extensionCls)
    {        
        registerExtension(ctx, extensionCls, XBeans.getClass(ctx, extensionCls, Home.class));
    }
    
    public void registerExtension(Context ctx, Class extensionCls, String licenseKey)
    {        
        registerExtension(ctx, extensionCls, XBeans.getClass(ctx, extensionCls, Home.class), licenseKey);
    }

    public void registerExtension(Context ctx, Class extensionCls, Object homeKey)
    {
        registerExtension(ctx, extensionCls, homeKey, null);
    }
    
    public void registerExtension(Context ctx, Class extensionCls, Object homeKey, String licenseKey)
    {
        Map<Class, Object> extensionHomeMap = (Map<Class, Object>)ctx.get(EXTENSION_HOME_KEYS_CTX_KEY);
        Map<Class, String> extensionLicenseMap = (Map<Class, String>)ctx.get(EXTENSION_LICENSE_KEYS_CTX_KEY);

        if( extensionHomeMap == null )
        {
            extensionHomeMap = new HashMap<Class, Object>();
            ctx.put(EXTENSION_HOME_KEYS_CTX_KEY, extensionHomeMap);
        }
        
        if( extensionLicenseMap == null )
        {
            extensionLicenseMap = new HashMap<Class, String>();
            ctx.put(EXTENSION_LICENSE_KEYS_CTX_KEY, extensionLicenseMap);
        }

        if( extensionCls == null || !Extension.class.isAssignableFrom(extensionCls) )
        {
            return;
        }
        
        extensionHomeMap.put(extensionCls, homeKey);
        
        extensionLicenseMap.put(extensionCls, licenseKey);        
    }
    
    public void unRegisterExtension(Context ctx, Class extensionCls)
    {        
        unRegisterExtension(ctx, extensionCls, XBeans.getClass(ctx, extensionCls, Home.class));
    }
    
    public void unRegisterExtension(Context ctx, Class extensionCls, String licenseKey)
    {        
        unRegisterExtension(ctx, extensionCls, XBeans.getClass(ctx, extensionCls, Home.class), licenseKey);
    }

    public void unRegisterExtension(Context ctx, Class extensionCls, Object homeKey)
    {
        unRegisterExtension(ctx, extensionCls, homeKey, null);
    }
    
    public void unRegisterExtension(Context ctx, Class extensionCls, Object homeKey, String licenseKey)
    {
        Map<Class, Object> extensionHomeMap = (Map<Class, Object>)ctx.get(EXTENSION_HOME_KEYS_CTX_KEY);
        Map<Class, String> extensionLicenseMap = (Map<Class, String>)ctx.get(EXTENSION_LICENSE_KEYS_CTX_KEY);

        if( extensionHomeMap == null )
        {
            extensionHomeMap = new HashMap<Class, Object>();
            ctx.put(EXTENSION_HOME_KEYS_CTX_KEY, extensionHomeMap);
        }
        
        if( extensionLicenseMap == null )
        {
            extensionLicenseMap = new HashMap<Class, String>();
            ctx.put(EXTENSION_LICENSE_KEYS_CTX_KEY, extensionLicenseMap);
        }

        if( extensionCls == null || !Extension.class.isAssignableFrom(extensionCls) )
        {
            return;
        }
        
        extensionHomeMap.remove(extensionCls);
        
        extensionLicenseMap.remove(extensionCls);        
    }
    

    public ExtensionAware getParentBean(Context ctx)
    {
        Object parentBean = ctx.get(EXTENSION_PARENT_BEAN_CTX_KEY);
        if (parentBean instanceof ExtensionAware)
        {
            return (ExtensionAware) parentBean;   
        }
        return null;
    }
    
    public void setParentBean(Context ctx, Object bean)
    {
        if (bean instanceof AdvancedFeatures)
        {
            ctx.put(EXTENSION_PARENT_BEAN_CTX_KEY, ((AdvancedFeatures) bean).getParentBean());   
        }
        else if (bean instanceof ExtensionAware)
        {
            ctx.put(EXTENSION_PARENT_BEAN_CTX_KEY, bean);   
        }
    }
    
    public String getExtensionName(Context ctx, Class cls)
    {
        if( !isExtensionClass(cls) )
        {
            return "N/A";
        }
        
        MessageMgr msgs = new MessageMgr(ctx, cls);
        
        String name = msgs.get(cls.getName() + ".extensionName");
        
        if( name == null )
        {
            Home entityHome = (Home)ctx.get(EntityInfoHome.class);
            if( entityHome != null )
            {
                try
                {
                    EntityInfo info = (EntityInfo)entityHome.find(ctx, new EQ(EntityInfoXInfo.CLASS_NAME, cls.getName()));
                    if( info != null )
                    {
                        name = info.getName();
                    }
                }
                catch (HomeException e)
                {
                    // Eat it.  Who cares.
                }
            }
        }
        
        if( name == null )
        {
            // Insert spaces into camel-case class name and remove 'Extension' at end of class name by default
            StringBuilder titleCaseName = new StringBuilder(cls.getSimpleName());
            titleCaseName.replace(0, 1, String.valueOf(Character.toUpperCase(titleCaseName.substring(0, 1).charAt(0))));
            String extensionSuffix = "Extension";
            if( extensionSuffix.equalsIgnoreCase(titleCaseName.substring(titleCaseName.length()-extensionSuffix.length(), titleCaseName.length())) )
            {
                titleCaseName.delete(titleCaseName.length()-extensionSuffix.length(), titleCaseName.length());
            }
            
            StringBuilder buf = new StringBuilder();
            for (char c : titleCaseName.toString().toCharArray()) {
                if (buf.length() > 0 && Character.isUpperCase(c)) {
                    buf.append(' ');
                }
                buf.append(c);
            }
            
            name = buf.toString();
        }
        
        return name;
    }

    public String getExtensionDescription(Context ctx, Class cls)
    {
        MessageMgr msgs = new MessageMgr(ctx, cls);
        
        String name = msgs.get(cls.getName() + ".extensionDescription", "");
        
        return name;
    }
    
    public Object getExtensionHomeKey(Context ctx, Class extension)
    {
        Map<Class, Object> extensionHomeMap = (Map<Class, Object>)ctx.get(EXTENSION_HOME_KEYS_CTX_KEY);
        if( extensionHomeMap == null )
        {
            extensionHomeMap = new HashMap<Class, Object>();
            ctx.put(EXTENSION_HOME_KEYS_CTX_KEY, extensionHomeMap);
        }
        
        if (isExtensionLicensed(ctx, extension))
        {
            Object key =  extensionHomeMap.get(extension);
            if (key == null)
            {
                key = XBeans.getClass(ctx, extension, Home.class);
            }
            return key;
        }
        else
        {
            new InfoLogMsg(this, "Extension of type " + (extension != null ? extension.getName() : null) + " not available.  Returning null home key...", null).log(ctx);
            return null;
        }
    }
    
    public Collection getAllExtensionHomeKeys(Context ctx, Class preferredType)
    {
        Map<Class, Object> extensionHomeMap = (Map<Class, Object>)ctx.get(EXTENSION_HOME_KEYS_CTX_KEY);
        if( extensionHomeMap == null )
        {
            extensionHomeMap = new HashMap<Class, Object>();
            ctx.put(EXTENSION_HOME_KEYS_CTX_KEY, extensionHomeMap);
        }
        
        Collection results = new ArrayList();
        
        for (Class extensionClass : extensionHomeMap.keySet())
        {
            if (preferredType.isAssignableFrom(extensionClass)
                    && isExtensionLicensed(ctx, extensionClass))
            {
                results.add(getExtensionHomeKey(ctx, extensionClass));
            }
        }
        
        return results;
    }
    
    public boolean isExtensionRegistered(Context ctx, Class extensionCls)
    {
        return getRegisteredExtensions(ctx, extensionCls).contains(extensionCls);
    }
    
    public <T extends Extension, S extends T> Set<Class<S>> getRegisteredExtensions(Context ctx, Class<T> preferredType)
    {
        if( preferredType == null || !Extension.class.isAssignableFrom(preferredType) )
        {
            return new HashSet<Class<S>>();
        }
        
        Map<Class, Object> extensionHomeMap = (Map<Class, Object>)ctx.get(EXTENSION_HOME_KEYS_CTX_KEY);
        if( extensionHomeMap == null )
        {
            return new HashSet<Class<S>>();
        }
        
        Set<Class<S>> result = new HashSet<Class<S>>();
        
        for (Class extensionType : extensionHomeMap.keySet())
        {
            if (preferredType.isAssignableFrom(extensionType)
                    && isExtensionLicensed(ctx, extensionType))
            {
                result.add(extensionType);
            }
        }
        
        return result;
    }

    public List<ExtensionHolder> wrapExtensions(Context ctx, Collection<? extends Extension> extensions)
    {
        List<ExtensionHolder> extensionHolders = new ArrayList<ExtensionHolder>();

        final FacetMgr fMgr = (FacetMgr) ctx.get(FacetMgr.class);
        
        for( Extension extension : extensions )
        {
            ExtensionHolder holder = (ExtensionHolder) fMgr.getInstanceOf(ctx, extension.getClass(), ExtensionHolder.class);
            if (holder == null)
            {
                new MajorLogMsg(this, "No extension holder configured in the facet manager for extension of type " + extension.getClass().getName(), null).log(ctx);
            }
            else
            {
                holder.setExtension(extension);
                extensionHolders.add(holder);   
            }
        }
        
        return extensionHolders;
    }

    public List<ExtensionHolder> wrapExtensions(Context ctx, Extension... extensions)
    {
        return wrapExtensions(ctx, Arrays.asList(extensions));
    }

    public Collection<Extension> unwrapExtensions(Collection<ExtensionHolder> holders)
    {
        Collection<Extension> extensions = new ArrayList<Extension>();
        
        if (holders != null)
        {
            for (ExtensionHolder holder : holders)
            {
                if (holder != null)
                {
                    extensions.add(holder.getExtension());
                }
            }   
        }
        
        return extensions;
    }

    public Collection<Extension> unwrapExtensions(ExtensionHolder... holders)
    {
        return unwrapExtensions(Arrays.asList(holders));
    }

    /**
     * Get the appropriate Home from the Context for the given extension.
     * 
     * @param ctx The operating context
     * @param extension Extension for which we need a Home
     * @return The Extension's Home
     */
    public Home getExtensionHome(Context ctx, Extension extension)
    {
        if (extension != null)
        {
            return getExtensionHome(ctx, extension.getClass());
        }
        return null;
    }

    /**
     * Get the appropriate Home from the Context for the given extension type.
     * 
     * @param ctx The operating context
     * @param extensionClass Type of Extension for which we need a Home
     * @return The Extension's Home
     */
    public Home getExtensionHome(Context ctx, Class extensionClass)
    {
        Object extensionHomeKey = getExtensionHomeKey(ctx, extensionClass);
        if (extensionHomeKey != null)
        {
            return (Home)ctx.get(extensionHomeKey);
        }
        return null;
    }

    /**
     * Get a collection of all Homes for all registerd extensions.
     * 
     * @param ctx The operating context
     * @return All Extension Homes
     */
    public Collection<Home> getExtensionHomes(Context ctx, Class preferredType)
    {
        Collection<Home> homes = new ArrayList<Home>();
        
        Collection homeKeys = getAllExtensionHomeKeys(ctx, preferredType);
        for( Object homeKey : homeKeys )
        {
            if (homeKey != null)
            {
                Home home = (Home)ctx.get(homeKey);
                if( home != null )
                {
                    homes.add(home);   
                }
            }
        }
        
        return homes;
    }
    
    /**
     * Get a map of the existing extension IDs for each of an account's extensions.
     * 
     * @param ctx The operating context
     * @param ban The BAN of the account for which to query the current list of extensions
     * @return Extension class to list of existing extensions for the given BAN 
     */
    public <T extends Extension, S extends T> Map<Class<S>, List<S>> getExistingExtensionMap(Context ctx, Class<T> preferredType, Object predicate)
    {
        List<S> results = new ArrayList<S>();
        
        Set<Class<S>> extensionTypes = getRegisteredExtensions(ctx, preferredType);
        for( Class<S> extensionCls : extensionTypes )
        {
            results.addAll(getExtensions(ctx, extensionCls, predicate));
        }
        
        Map<Class<S>, List<S>> oldExtensionMap = new HashMap<Class<S>, List<S>>();
        for( S extension : results )
        {
            List<S> extensionList = oldExtensionMap.get(extension.getClass());
            if( extensionList == null )
            {
                extensionList = new ArrayList<S>();
                oldExtensionMap.put((Class<S>)extension.getClass(), extensionList);
            }
            extensionList.add(extension);
        }
        return oldExtensionMap;
    }

    public <T extends Extension, S extends T> List<T> getAllExtensions(Context ctx, Class<T> preferredType, Object predicate, Class... preferredInterfaces)
    {
        List<T> results = new ArrayList<T>();
        
        Set<Class<S>> extensionTypes = getRegisteredExtensions(ctx, preferredType);
        for( Class<S> extensionCls : extensionTypes )
        {
            if (preferredInterfaces!=null && preferredInterfaces.length>0)
            {
                for (Class preferredInterface : preferredInterfaces)
                {
                    if (preferredInterface.isAssignableFrom(extensionCls))
                    {
                        results.addAll(getExtensions(ctx, extensionCls, predicate));
                        break;
                    }
                }
            }
            else
            {
                results.addAll(getExtensions(ctx, extensionCls, predicate));
            }
        }
        
        return results;
    }
    
    public <T extends Extension> List<T> getExtensions(final Context ctx, Class<T> extensionType, Object predicate)
    {
        List<T> extensions = new ArrayList<T>();
        
        Home home = getExtensionHome(ctx, extensionType);
        if (home != null)
        {
            try
            {
                Collection<T> c = home.select(ctx, predicate);
                if( c != null )
                {
                    extensions.addAll(c);   
                }

            }
            catch (UnsupportedOperationException e)
            {
                new DebugLogMsg(this, UnsupportedOperationException.class.getSimpleName() + " occurred in " + this.getClass().getSimpleName() + ".getExtension(): " + e.getMessage(), e).log(ctx);
                new MajorLogMsg(this, "Error loading extensions. See debug logs for details.", null).log(ctx);
            }
            catch (HomeException e)
            {
                new DebugLogMsg(this, HomeException.class.getSimpleName() + " occurred in " + this.getClass().getSimpleName() + ".getExtension(): " + e.getMessage(), e).log(ctx);
                new MajorLogMsg(this, "Error loading extensions. See debug logs for details.", null).log(ctx);
            }
        }
        
        return extensions;
    }
    
    private boolean isExtensionClass(Class cls)
    {
        Class[] interfaces = cls.getInterfaces();
        if( interfaces != null && interfaces.length > 0 )
        {
            for( Class ifc : interfaces )
            {
                if( Extension.class.getName().equals(ifc.getName()) )
                {
                    return true;
                }
            }
        }
        
        Class superclass = cls.getSuperclass();
        if( superclass != null )
        {
            return isExtensionClass(superclass);
        }
        
        return false;
    }
    
    private <T extends Extension> boolean isExtensionValidForType(TypeAware typeAware, Class<T> beanType) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException
    {
        if (beanType.isAssignableFrom(TypeDependentExtension.class))
        {
            try
            {
                Method method = beanType.getMethod("isExtensionValidForType", AbstractEnum.class);
                if (method==null)
                {
                    throw new NoSuchMethodException();
                }
                else
                {
                    return (Boolean) method.invoke(null, typeAware.getEnumType());
                }
            } 
            catch (NoSuchMethodException exception)
            {
                TypeDependentExtension extension = (TypeDependentExtension) beanType.getConstructor().newInstance();
                return extension.isValidForType(typeAware.getEnumType());
            }
        }
        else
        {
            return false;
        }
    }
    
    public <T extends Extension> T getExtension(Context ctx, ExtensionAware extensionAware, Class<T> beanType)
    {
        T result = null;

        try
        {
            if (!beanType.isAssignableFrom(TypeDependentExtension.class)
                    || (extensionAware instanceof TypeAware && isExtensionValidForType((TypeAware) extensionAware, beanType)))
            {
                Set<Class> extensionTypeSet = new HashSet<Class>();
    
                Collection<Extension> extensions = extensionAware.getExtensions();
                boolean found = false;
                if (extensions != null)
                {
                    
                    for (Extension extension : extensions)
                    {
                        if (extension!=null && beanType.isAssignableFrom(extension.getClass()))
                        {
                            result = (T) extension;
                            break;
                        }
                    }    
                }
            }
        }
        catch (Throwable t)
        {
            LogSupport.minor(ctx, DefaultExtensionSupport.class, "Unable to retrieve extension of type " + beanType.getSimpleName() + ": " + t.getMessage(), t);
            
        }
        
        return result;
    }
    
}
