/*
 * This code is a protected work and subject to domestic and international
 * copyright law(s).  A complete listing of authors of this work is readily
 * available.  Additionally, source code is, by its very nature, confidential
 * information and inextricably contains trade secrets and other information
 * proprietary, valuable and sensitive to Redknee.  No unauthorized use,
 * disclosure, manipulation or otherwise is permitted, and may only be used
 * in accordance with the terms of the license agreement entered into with
 * Redknee Inc. and/or its subsidiaries.
 *
 * Copyright (c) Redknee Inc. and its subsidiaries. All Rights Reserved.
 */
package com.redknee.app.crm.bean.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.redknee.app.crm.bean.AuxiliaryServiceStateEnum;
import com.redknee.app.crm.bean.AuxiliaryServiceTypeEnum;
import com.redknee.app.crm.bean.AuxiliaryServiceXInfo;
import com.redknee.app.crm.bean.CallingGroupTypeEnum;
import com.redknee.app.crm.bean.ServiceBase;
import com.redknee.app.crm.bean.TypeAware;
import com.redknee.app.crm.exception.RethrowExceptionListener;
import com.redknee.app.crm.extension.Extension;
import com.redknee.app.crm.extension.ExtensionAware;
import com.redknee.app.crm.extension.ExtensionHolder;
import com.redknee.app.crm.extension.ExtensionLoadingAdapter;
import com.redknee.app.crm.extension.TypeDependentExtension;
import com.redknee.app.crm.extension.auxiliaryservice.AuxiliaryServiceExtension;
import com.redknee.app.crm.extension.auxiliaryservice.AuxiliaryServiceExtensionXInfo;
import com.redknee.app.crm.extension.auxiliaryservice.TFAAuxSvcExtension;
import com.redknee.app.crm.extension.auxiliaryservice.core.CallingGroupAuxSvcExtension;
import com.redknee.app.crm.extension.auxiliaryservice.core.GroupChargingAuxSvcExtension;
import com.redknee.app.crm.extension.auxiliaryservice.core.HomeZoneAuxSvcExtension;
import com.redknee.app.crm.extension.auxiliaryservice.core.MultiSimAuxSvcExtension;
import com.redknee.app.crm.extension.auxiliaryservice.core.ProvisionableAuxSvcExtension;
import com.redknee.app.crm.extension.auxiliaryservice.core.VPNAuxSvcExtension;
import com.redknee.app.crm.extension.auxiliaryservice.core.VoicemailAuxSvcExtension;
import com.redknee.app.crm.state.FinalStateAware;
import com.redknee.app.crm.support.EnumStateSupportHelper;
import com.redknee.app.crm.support.ExtensionSupportHelper;
import com.redknee.app.crm.xhome.beans.xi.NonModelPropertyInfo;
import com.redknee.framework.xhome.beans.IllegalPropertyArgumentException;
import com.redknee.framework.xhome.beans.Validatable;
import com.redknee.framework.xhome.beans.XBeans;
import com.redknee.framework.xhome.beans.XCloneable;
import com.redknee.framework.xhome.beans.xi.PropertyInfo;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.context.ContextLocator;
import com.redknee.framework.xhome.home.HomeOperationEnum;
import com.redknee.framework.xhome.msp.SpidAware;
import com.redknee.framework.xhome.util.enabled.EnabledAware;
import com.redknee.framework.xhome.xenum.AbstractEnum;
import com.redknee.framework.xhome.xenum.Enum;
import com.redknee.framework.xlog.log.LogSupport;
import com.redknee.framework.xlog.log.MajorLogMsg;

/**
 * 
 *
 * @author aaron.gourley@redknee.com
 * @since 
 */
public class AuxiliaryService extends com.redknee.app.crm.bean.AuxiliaryService implements FinalStateAware, EnabledAware, Validatable, ExtensionAware, TypeAware, ServiceBase
{

    /**
     * Set of auxiliary service types that have group charge functionality.
     * When adding auxiliary service type to group chargeable set, add implementation to get the leader for
     * a subscriber in getGroupLeaderForCharging() method.
     */
    public final static Set<AuxiliaryServiceTypeEnum> GROUP_CHARGEABLE_SERVICES =
        Collections.unmodifiableSet(
                new HashSet<AuxiliaryServiceTypeEnum>(
                        Arrays.asList(
                                AuxiliaryServiceTypeEnum.Vpn)));

    public final static Set<AuxiliaryServiceTypeEnum> HLR_PROVISIONABLE_SERVICES =
        Collections.unmodifiableSet(
                new HashSet<AuxiliaryServiceTypeEnum>(
                        Arrays.asList(
                                AuxiliaryServiceTypeEnum.Provisionable,
                                AuxiliaryServiceTypeEnum.Vpn,
                                AuxiliaryServiceTypeEnum.AdditionalMsisdn,
                                AuxiliaryServiceTypeEnum.PRBT,
                                AuxiliaryServiceTypeEnum.MultiSIM,
                                AuxiliaryServiceTypeEnum.NGRC_OPTIN)));
    
    public final static Set<AuxiliaryServiceTypeEnum> TFA_CHARGEABLE_SERVICES =
            Collections.unmodifiableSet(
                    new HashSet<AuxiliaryServiceTypeEnum>(
                            Arrays.asList(
                                    AuxiliaryServiceTypeEnum.TFA)));

    public static final List<AuxiliaryServiceStateEnum> FINAL_STATES =
        Arrays.asList(
                AuxiliaryServiceStateEnum.CLOSED
                );


    public boolean isEnabled()
    {
        return this.getState() == AuxiliaryServiceStateEnum.ACTIVE;
    }

    public void setEnabled(final boolean flag)
    {
        if (!isInFinalState())
        {
            if (flag)
            {
                this.setState(AuxiliaryServiceStateEnum.ACTIVE);
            }
            else
            {
                this.setState(AuxiliaryServiceStateEnum.DEPRECATED);
            }
        }
    }


    /**
     * {@inheritDoc}
     */
    public AbstractEnum getAbstractState()
    {
        return getState();
    }


    /**
     * {@inheritDoc}
     */
    public void setAbstractState(final AbstractEnum state)
    {
        setState((AuxiliaryServiceStateEnum) state);
    }

    /**
     * {@inheritDoc}
     */
    public Collection<? extends Enum> getFinalStates()
    {
        return FINAL_STATES;
    }


    /**
     * {@inheritDoc}
     */
    public <T extends Enum> boolean isFinalState(T state)
    {
        Collection<? extends Enum> finalStates = getFinalStates();
        return EnumStateSupportHelper.get().isOneOfStates(state, finalStates);
    }


    /**
     * {@inheritDoc}
     */
    public boolean isInFinalState()
    {
        Collection<? extends Enum> finalStates = getFinalStates();
        return EnumStateSupportHelper.get().isOneOfStates(this, finalStates);
    }

    /**
     * @{inheritDoc}
     */
    public void validate(final Context context) throws IllegalStateException
    {
        if (!isHLRProvisionable() && (AuxiliaryServiceTypeEnum.Download_Servers != this.getType()))
        {
            // Make sure Basic, Homezone, Voicemail and Promotion Aux Service doesn't set any HLR Commands.
            ProvisionableAuxSvcExtension provisionableAuxSvcExtension = ExtensionSupportHelper.get(context)
                    .getExtension(context, this, ProvisionableAuxSvcExtension.class);
            if (provisionableAuxSvcExtension != null)
            {
                throw new IllegalStateException(this.getType().getDescription()
                        + " service doesn't allow HLR Provisioning!");
            }
        }
        
        boolean isCreateCall = HomeOperationEnum.CREATE.equals(context.get(HomeOperationEnum.class));

        final RethrowExceptionListener el = new RethrowExceptionListener();

        if (isCreateCall && AuxiliaryService.DEFAULT_GLCODE.equals(this.getGLCode()))
        {
            el.thrown(new IllegalPropertyArgumentException(AuxiliaryServiceXInfo.GLCODE,
                    "GL Code is required."));
        }
        if (isCreateCall && this.isGroupChargable())
        {
            GroupChargingAuxSvcExtension groupChargingAuxSvcExtension = ExtensionSupportHelper.get(context)
                    .getExtension(context, this, GroupChargingAuxSvcExtension.class);
            if (groupChargingAuxSvcExtension == null)
            {
                el.thrown(new IllegalStateException("Group Charging extension required"));
            }
        }

        if (this.getType().getIndex() == AuxiliaryServiceTypeEnum.HomeZone_INDEX)
        {
            HomeZoneAuxSvcExtension homezoneAuxSvcExtension = ExtensionSupportHelper.get(context).getExtension(context, this, HomeZoneAuxSvcExtension.class);
            if (homezoneAuxSvcExtension==null)
            {
                el.thrown(new IllegalStateException("Home Zone extension required"));
            }
        }
        else if (this.getType().getIndex() == AuxiliaryServiceTypeEnum.Vpn_INDEX)
        {
            VPNAuxSvcExtension vpnAuxSvcExtension = ExtensionSupportHelper.get(context).getExtension(context, this, VPNAuxSvcExtension.class);
            if (vpnAuxSvcExtension==null)
            {
                el.thrown(new IllegalStateException("VPN extension required"));
            }
        }
        else if (this.getType().getIndex() == AuxiliaryServiceTypeEnum.MultiSIM_INDEX)
        {
            MultiSimAuxSvcExtension multiSimAuxSvcExtension = ExtensionSupportHelper.get(context).getExtension(context, this, MultiSimAuxSvcExtension.class);
            if (multiSimAuxSvcExtension==null)
            {
                el.thrown(new IllegalStateException("Multi-SIM (Charging) extension required"));
            }
        }
        else if (this.getType().getIndex() == AuxiliaryServiceTypeEnum.Voicemail_INDEX)
        {
            VoicemailAuxSvcExtension voicemailAuxSvcExtension = ExtensionSupportHelper.get(context).getExtension(context, this, VoicemailAuxSvcExtension.class);
            if (voicemailAuxSvcExtension==null)
            {
                el.thrown(new IllegalStateException("Voicemail extension required."));
            }
        }
        else if (this.getType().getIndex() == AuxiliaryServiceTypeEnum.TFA_INDEX)
        {
            TFAAuxSvcExtension tfaAuxSvcExtension = ExtensionSupportHelper.get(context).getExtension(context, this, TFAAuxSvcExtension.class);
            if (tfaAuxSvcExtension==null)
            {
                el.thrown(new IllegalStateException("TFA extension required."));
            }
        }


        el.throwAllAsCompoundException();
    }
    

    public boolean isGroupChargable()
    {
        return isOfType(GROUP_CHARGEABLE_SERVICES);
    }

    public boolean isHLRProvisionable()
    {
        return isOfType(HLR_PROVISIONABLE_SERVICES);
    }

    /**
     * @param type List of possible enumerated subscription type values.
     * @return True if this subscription's type matches the input type
     */
    private boolean isOfType(Collection<AuxiliaryServiceTypeEnum> types)
    {
        if (types != null)
        {
            return types.contains(getType());
        }
        else
        {
            return false;
        }
    }

    public PropertyInfo getExtensionHolderProperty()
    {
        return EXTENSIONS_PROPERTY;
    }

    public Collection<Extension> getExtensions()
    {
        return getExtensions(ContextLocator.locate());
    }
    public Collection<Extension> getExtensions(Context ctx)
    {
        Collection<ExtensionHolder> holders = (Collection<ExtensionHolder>) getExtensionHolderProperty().get(this);
        return ExtensionSupportHelper.get(ctx).unwrapExtensions(holders);
    }


    public Collection<Class> getExtensionTypes()
    {
        final Context ctx = ContextLocator.locate();
        return getExtensionTypes(ctx, this.getType());
    }


    public static Collection<Class> getExtensionTypes(final Context ctx, AuxiliaryServiceTypeEnum type)
    {
        Collection<Class> desiredExtTypes = auxServiceExtTypes_.get(type);
        if (desiredExtTypes == null)
        {
            Set<Class<AuxiliaryServiceExtension>> extClasses = ExtensionSupportHelper.get(ctx).getRegisteredExtensions(
                    ctx, AuxiliaryServiceExtension.class);
            desiredExtTypes = new ArrayList<Class>();
            for (Class<AuxiliaryServiceExtension> ext : extClasses)
            {
                try
                {
                    if (TypeDependentExtension.class.isAssignableFrom(ext))
                    {
                        TypeDependentExtension typeDependentExt = (TypeDependentExtension) XBeans.instantiate(ext, ctx);
                        if (typeDependentExt.isValidForType(type))
                        {
                            desiredExtTypes.add(ext);
                        }
                    }
                    else
                    {
                        desiredExtTypes.add(ext);
                    }
                }
                catch (Exception ex)
                {
                    new MajorLogMsg(AuxiliaryService.class, " Unable to instantiate ext class " + ext, ex).log(ctx);
                }
            }
            auxServiceExtTypes_.put(type, desiredExtTypes);
        }
        return desiredExtTypes;
    }
    
    public List<ExtensionHolder> getExtensionHolders()
    {
        return auxiliaryServiceExtensions_;
    }
    
    /**
     * Lazy loading extensions. {@inheritDoc}
     */
    public List<ExtensionHolder> getAuxiliaryServiceExtensions()
    {
        synchronized (this)
        {
            if (getExtensionHolders() == null)
            {
                final Context ctx = ContextLocator.locate();
                try
                {
                    /*
                     * To avoid deadlock, use a credit category
                     * "with extensions loaded" along with extension loading
                     * adapter.
                     */
                    AuxiliaryService auxServiceCopy = (AuxiliaryService) this.clone();
                    auxServiceCopy.setAuxiliaryServiceExtensions(new ArrayList());
                    
                    auxServiceCopy = (AuxiliaryService) new ExtensionLoadingAdapter<AuxiliaryServiceExtension>(
                            AuxiliaryServiceExtension.class,
                            AuxiliaryServiceExtensionXInfo.AUXILIARY_SERVICE_ID,this.getExtensionTypes().toArray(new Class[0]) )
                            .adapt(ctx, auxServiceCopy);

                    this.setAuxiliaryServiceExtensions(auxServiceCopy
                            .getAuxiliaryServiceExtensions());
                }
                catch (Exception e)
                {
                    LogSupport.minor(ctx, this,
                            "Exception occurred loading extensions. Extensions NOT loaded.");
                    LogSupport.debug(ctx, this,
                            "Exception occurred loading extensions. Extensions NOT loaded.", e);
                }
            }
            else
            {
                for (ExtensionHolder holder : getExtensionHolders())
                {
                    if (holder.getExtension() instanceof SpidAware && this.getSpid()>0)
                    {
                        ((SpidAware) holder.getExtension()).setSpid(this.getSpid());
                    }
                    
                }
            }
        }

        return getExtensionHolders();
    }

    
    public void setAuxiliaryServiceExtensions(List<ExtensionHolder> auxiliaryServiceExtensions)
    {
        auxiliaryServiceExtensions_ = auxiliaryServiceExtensions;
    }

    /**
     * Adding cloning functionality to clone added fields.
     * 
     * @return the clone object
     * @throws CloneNotSupportedException
     *             should not be thrown
     */
    @Override
    public Object clone() throws CloneNotSupportedException
    {
        AuxiliaryService clone = (AuxiliaryService) super.clone();
        return cloneAuxiliaryServiceExtensionList(clone);
    }

    private AuxiliaryService cloneAuxiliaryServiceExtensionList(
            final AuxiliaryService clone) throws CloneNotSupportedException
    {
        if ( auxiliaryServiceExtensions_ != null)
        {
            final List extentionList = new ArrayList(auxiliaryServiceExtensions_.size());
            clone.setAuxiliaryServiceExtensions(extentionList);
            for (final Iterator it = auxiliaryServiceExtensions_.iterator(); it
                    .hasNext();)
            {
                extentionList.add(safeClone((XCloneable) it.next()));
            }
        }
        return clone;
    }

    @Override
    public int getTypeEnumIndex()
    {
        if (getType()!=null)
        {
            return getType().getIndex();
        }
        else
        {
            return -1;
        }
    }

    @Override
    public AbstractEnum getEnumType()
    {
        return getType();
    }
    
    public boolean isBirthdayPlan(Context ctx)
    {
        boolean result = false;
        if (this.getType() == AuxiliaryServiceTypeEnum.CallingGroup)
        {
            CallingGroupTypeEnum callingGroupType = CallingGroupAuxSvcExtension.DEFAULT_CALLINGGROUPTYPE;
            CallingGroupAuxSvcExtension callingGroupAuxSvcExtension = ExtensionSupportHelper.get(ctx).getExtension(ctx, this, CallingGroupAuxSvcExtension.class);
            if (callingGroupAuxSvcExtension!=null)
            {
                callingGroupType = callingGroupAuxSvcExtension.getCallingGroupType();
            }
            else 
            {
                LogSupport.minor(ctx, this,
                        "Unable to find required extension of type '" + CallingGroupAuxSvcExtension.class.getSimpleName()
                                + "' for auxiliary service " + this.getIdentifier());
            }
            result = CallingGroupTypeEnum.BP.equals(callingGroupType);
        }
        return result;
    }
    
    public boolean isPLP(Context ctx)
    {
        boolean result = false;
        if (this.getType() == AuxiliaryServiceTypeEnum.CallingGroup)
        {
            CallingGroupTypeEnum callingGroupType = CallingGroupAuxSvcExtension.DEFAULT_CALLINGGROUPTYPE;
            CallingGroupAuxSvcExtension callingGroupAuxSvcExtension = ExtensionSupportHelper.get(ctx).getExtension(ctx, this, CallingGroupAuxSvcExtension.class);
            if (callingGroupAuxSvcExtension!=null)
            {
                callingGroupType = callingGroupAuxSvcExtension.getCallingGroupType();
            }
            else 
            {
                LogSupport.minor(ctx, this,
                        "Unable to find required extension of type '" + CallingGroupAuxSvcExtension.class.getSimpleName()
                                + "' for auxiliary service " + this.getIdentifier());
            }
            result = CallingGroupTypeEnum.PLP.equals(callingGroupType);
        }
        return result;
    }

    public boolean isCUG(Context ctx)
    {
        boolean result = false;
        if (this.getType() == AuxiliaryServiceTypeEnum.CallingGroup)
        {
            CallingGroupTypeEnum callingGroupType = CallingGroupAuxSvcExtension.DEFAULT_CALLINGGROUPTYPE;
            CallingGroupAuxSvcExtension callingGroupAuxSvcExtension = ExtensionSupportHelper.get(ctx).getExtension(ctx, this, CallingGroupAuxSvcExtension.class);
            if (callingGroupAuxSvcExtension!=null)
            {
                callingGroupType = callingGroupAuxSvcExtension.getCallingGroupType();
            }
            else 
            {
                LogSupport.minor(ctx, this,
                        "Unable to find required extension of type '" + CallingGroupAuxSvcExtension.class.getSimpleName()
                                + "' for auxiliary service " + this.getIdentifier());
            }
            result = CallingGroupTypeEnum.CUG.equals(callingGroupType);
        }
        return result;
    }
    
    public boolean isPrivateCUG(Context ctx)
    {
        boolean result = false;
        if (this.getType() == AuxiliaryServiceTypeEnum.CallingGroup)
        {
            CallingGroupTypeEnum callingGroupType = CallingGroupAuxSvcExtension.DEFAULT_CALLINGGROUPTYPE;
            CallingGroupAuxSvcExtension callingGroupAuxSvcExtension = ExtensionSupportHelper.get(ctx).getExtension(ctx, this, CallingGroupAuxSvcExtension.class);
            if (callingGroupAuxSvcExtension!=null)
            {
                callingGroupType = callingGroupAuxSvcExtension.getCallingGroupType();
            }
            else 
            {
                LogSupport.minor(ctx, this,
                        "Unable to find required extension of type '" + CallingGroupAuxSvcExtension.class.getSimpleName()
                                + "' for auxiliary service " + this.getIdentifier());
            }
            result = CallingGroupTypeEnum.PCUG.equals(callingGroupType);
        }
        return result;
    }
    
    


    /**
     * The operating context.
     */
    protected transient Context context_;

    public static final String AUXILIARY_SERVICE_EXTENSIONS_FIELD_NAME = "auxiliaryServiceExtensions_";
    public static final PropertyInfo EXTENSIONS_PROPERTY = new NonModelPropertyInfo(AuxiliaryService.class, AUXILIARY_SERVICE_EXTENSIONS_FIELD_NAME, AuxiliaryServiceXInfo.instance());
    protected List<ExtensionHolder> auxiliaryServiceExtensions_ = null;
    public static Map<AbstractEnum, Collection<Class>> auxServiceExtTypes_ = new HashMap<AbstractEnum, Collection<Class>>();
    
    
    
}
