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
import com.redknee.app.crm.extension.FinalExtension;
import com.redknee.app.crm.extension.MandatoryExtension;
import com.redknee.app.crm.extension.TypeDependentExtension;
import com.redknee.app.crm.extension.auxiliaryservice.NGRCOptInAuxSvcExtensionHome;
import com.redknee.app.crm.extension.auxiliaryservice.NGRCOptInAuxSvcExtensionXInfo;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.xenum.AbstractEnum;

/**
 * Implements the NGRC Opt-In auxiliary service extension.
 * @author Marcio Marques
 * @since 9.1.2
 *
 */
public class NGRCOptInAuxSvcExtension extends com.redknee.app.crm.extension.auxiliaryservice.NGRCOptInAuxSvcExtension implements FinalExtension,
        MandatoryExtension, TypeDependentExtension
{

    @Override
    public boolean isValidForType(AbstractEnum auxiliaryServiceType)
    {
        return NGRCOptInAuxSvcExtension.isExtensionValidForType(auxiliaryServiceType);
    }
    
    public static boolean isExtensionValidForType(AbstractEnum auxiliaryServiceType)
    {
        return AuxiliaryServiceTypeEnum.NGRC_OPTIN.equals(auxiliaryServiceType);
    }

    public static NGRCOptInAuxSvcExtension getNGRCOptInAuxSvcExtension(Context ctx, long auxiliaryServiceID) throws HomeException
    {
        Home home = (Home) ctx.get(NGRCOptInAuxSvcExtensionHome.class);
        return (NGRCOptInAuxSvcExtension) home.find(ctx, new EQ(NGRCOptInAuxSvcExtensionXInfo.AUXILIARY_SERVICE_ID, auxiliaryServiceID));
    }


}
