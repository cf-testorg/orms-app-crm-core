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
package com.redknee.app.crm.extension.auxiliaryservice.core;

import com.redknee.app.crm.CoreCrmLicenseConstants;
import com.redknee.app.crm.bean.AuxiliaryServiceTypeEnum;
import com.redknee.app.crm.bean.core.AuxiliaryService;
import com.redknee.app.crm.extension.DependencyValidatableExtension;
import com.redknee.app.crm.extension.MandatoryExtension;
import com.redknee.app.crm.extension.TypeDependentExtension;
import com.redknee.app.crm.extension.auxiliaryservice.MultiSimAuxSvcExtensionHome;
import com.redknee.app.crm.extension.auxiliaryservice.MultiSimAuxSvcExtensionXInfo;
import com.redknee.app.crm.license.LicenseAware;
import com.redknee.app.crm.support.ExtensionSupportHelper;
import com.redknee.app.crm.support.HomeSupportHelper;
import com.redknee.app.crm.support.LicensingSupportHelper;
import com.redknee.framework.xhome.beans.CompoundIllegalStateException;
import com.redknee.framework.xhome.beans.IllegalPropertyArgumentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.HomeOperationEnum;
import com.redknee.framework.xhome.xenum.AbstractEnum;
import com.redknee.framework.xlog.log.MinorLogMsg;

/**
 * Implements the Multi-SIM auxiliary service extension.
 * @author Marcio Marques
 * @since 9.1.2
 *
 */
public class MultiSimAuxSvcExtension extends com.redknee.app.crm.extension.auxiliaryservice.MultiSimAuxSvcExtension implements MandatoryExtension, LicenseAware, TypeDependentExtension, DependencyValidatableExtension
{
    @Override
    public boolean isLicensed(Context ctx)
    {
        return LicensingSupportHelper.get(ctx).isLicensed(ctx, CoreCrmLicenseConstants.MULTI_SIM_LICENSE);
    }

    @Override
    public boolean isValidForType(AbstractEnum auxiliaryServiceType)
    {
        return isExtensionValidForType(auxiliaryServiceType);
    }
    
    public static boolean isExtensionValidForType(AbstractEnum auxiliaryServiceType)
    {
        return AuxiliaryServiceTypeEnum.MultiSIM.equals(auxiliaryServiceType);
    }

    public static MultiSimAuxSvcExtension getMultiSimAuxSvcExtension(Context ctx, long auxiliaryServiceID) throws HomeException
    {
        Home home = (Home) ctx.get(MultiSimAuxSvcExtensionHome.class);
        return (MultiSimAuxSvcExtension) home.find(ctx, new EQ(MultiSimAuxSvcExtensionXInfo.AUXILIARY_SERVICE_ID, auxiliaryServiceID));
    }

    @Override
    public void validateDependency(Context ctx) throws IllegalStateException
    {
        CompoundIllegalStateException cise = new CompoundIllegalStateException();
        
        if (HomeOperationEnum.STORE.equals(ctx.get(HomeOperationEnum.class, HomeOperationEnum.STORE)))
        {
                MultiSimAuxSvcExtension existingExtension = null;
                try
                {
                    existingExtension = HomeSupportHelper.get(ctx).findBean(ctx, MultiSimAuxSvcExtension.class, new EQ(MultiSimAuxSvcExtensionXInfo.AUXILIARY_SERVICE_ID, this.getAuxiliaryServiceId()));
                }
                catch (HomeException e)
                {
                    new MinorLogMsg(this, "Error retrieving MultiSimAuxSvcExtension with ID=" + this.getAuxiliaryServiceId(), e).log(ctx);
                }
                
                if (existingExtension != null)
                {
                    if (this.getMaxNumSIMs() < existingExtension.getMaxNumSIMs())
                    {
                        cise.thrown(new IllegalPropertyArgumentException(MultiSimAuxSvcExtensionXInfo.MAX_NUM_SIMS, "New maximum number of SIMs must be greater or equals old maximum number of SIMs (" + this.getMaxNumSIMs() + ")"));
                    }
                }
          }
        cise.throwAll();
    }
    
}
