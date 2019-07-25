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

import com.redknee.app.crm.bean.AuxiliaryServiceTypeEnum;
import com.redknee.app.crm.extension.MandatoryExtension;
import com.redknee.app.crm.extension.TypeDependentExtension;
import com.redknee.app.crm.extension.auxiliaryservice.AddMsisdnAuxSvcExtensionHome;
import com.redknee.app.crm.extension.auxiliaryservice.CallingGroupAuxSvcExtensionXInfo;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.xenum.AbstractEnum;

/**
 * Implements the Calling Group auxiliary service extension.
 * @author Marcio Marques
 * @since 9.1.2
 *
 */
public class CallingGroupAuxSvcExtension extends com.redknee.app.crm.extension.auxiliaryservice.CallingGroupAuxSvcExtension implements MandatoryExtension, TypeDependentExtension
{
    @Override
    public boolean isValidForType(AbstractEnum auxiliaryServiceType)
    {
        return isExtensionValidForType(auxiliaryServiceType);
    }
    
    public static boolean isExtensionValidForType(AbstractEnum auxiliaryServiceType)
    {
        return AuxiliaryServiceTypeEnum.CallingGroup.equals(auxiliaryServiceType);
    }

    public static CallingGroupAuxSvcExtension getCallingGroupAuxSvcExtension(Context ctx, long auxiliaryServiceID) throws HomeException
    {
        Home home = (Home) ctx.get(AddMsisdnAuxSvcExtensionHome.class);
        return (CallingGroupAuxSvcExtension) home.find(ctx, new EQ(CallingGroupAuxSvcExtensionXInfo.AUXILIARY_SERVICE_ID, auxiliaryServiceID));
    }

    /**
     * This key is used to explicitly enable or disable this home.
     * SubscriberAuxiliaryServices can be removed as a result of a change to the
     * CUG, so there no need to update the CUG in that case.
     */
    public final static String ENABLED =
            CallingGroupAuxSvcExtension.class.getName() + ".ACTIVATION";
    
}
