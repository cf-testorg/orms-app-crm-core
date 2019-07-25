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
import com.redknee.app.crm.bean.core.AuxiliaryService;
import com.redknee.app.crm.extension.MandatoryExtension;
import com.redknee.app.crm.extension.TypeDependentExtension;
import com.redknee.app.crm.extension.auxiliaryservice.ProvisionableAuxSvcExtensionHome;
import com.redknee.app.crm.extension.auxiliaryservice.ProvisionableAuxSvcExtensionXInfo;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.xenum.AbstractEnum;

/**
 * Implements the Provisionable auxiliary service extension.
 * @author Marcio Marques
 * @since 9.1.2
 *
 */
public class ProvisionableAuxSvcExtension extends com.redknee.app.crm.extension.auxiliaryservice.ProvisionableAuxSvcExtension implements MandatoryExtension, TypeDependentExtension
{
    @Override
    public boolean isValidForType(AbstractEnum auxiliaryServiceType)
    {
        return isExtensionValidForType(auxiliaryServiceType);
    }
    
    public static boolean isExtensionValidForType(AbstractEnum auxiliaryServiceType)
    {
        return AuxiliaryService.HLR_PROVISIONABLE_SERVICES.contains(auxiliaryServiceType) ||
        		AuxiliaryServiceTypeEnum.Download_Servers.equals(auxiliaryServiceType)|| 
        		AuxiliaryServiceTypeEnum.SERVICE_PROVISIONING_GATEWAY.equals(auxiliaryServiceType);
    }

    public static ProvisionableAuxSvcExtension getProvisionableAuxSvcExtension(Context ctx, long auxiliaryServiceID) throws HomeException
    {
        Home home = (Home) ctx.get(ProvisionableAuxSvcExtensionHome.class);
        return (ProvisionableAuxSvcExtension) home.find(ctx, new EQ(ProvisionableAuxSvcExtensionXInfo.AUXILIARY_SERVICE_ID, auxiliaryServiceID));
    }
    
}
