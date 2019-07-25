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

import com.redknee.app.crm.bean.core.AuxiliaryService;
import com.redknee.app.crm.extension.DependencyValidatableExtension;
import com.redknee.app.crm.extension.MandatoryExtension;
import com.redknee.app.crm.extension.TypeDependentExtension;
import com.redknee.app.crm.extension.auxiliaryservice.GroupChargingAuxSvcExtensionHome;
import com.redknee.app.crm.extension.auxiliaryservice.GroupChargingAuxSvcExtensionXInfo;
import com.redknee.framework.xhome.beans.CompoundIllegalStateException;
import com.redknee.framework.xhome.beans.IllegalPropertyArgumentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.HomeOperationEnum;
import com.redknee.framework.xhome.xenum.AbstractEnum;

/**
 * Implements the Group Charging auxiliary service extension.
 * @author Marcio Marques
 * @since 9.1.2
 *
 */
public class GroupChargingAuxSvcExtension extends com.redknee.app.crm.extension.auxiliaryservice.GroupChargingAuxSvcExtension implements
        MandatoryExtension, TypeDependentExtension, DependencyValidatableExtension
{

    public void validateDependency(Context ctx) throws IllegalStateException
    {
        CompoundIllegalStateException cise = new CompoundIllegalStateException();
        boolean isCreateCall = HomeOperationEnum.CREATE.equals(ctx.get(HomeOperationEnum.class));

        if (isCreateCall)
        {

            if (this.getGroupGLCode() == GroupChargingAuxSvcExtension.DEFAULT_GROUPGLCODE)
            {
                cise.thrown(new IllegalPropertyArgumentException(GroupChargingAuxSvcExtensionXInfo.GROUP_GLCODE,
                        "Group GL Code is required."));
            }
        }
        
        cise.throwAll();
    }


    @Override
    public boolean isValidForType(AbstractEnum auxiliaryServiceType)
    {
        return isExtensionValidForType(auxiliaryServiceType);
    }
    
    public static boolean isExtensionValidForType(AbstractEnum auxiliaryServiceType)
    {
        return AuxiliaryService.GROUP_CHARGEABLE_SERVICES.contains(auxiliaryServiceType);
    }

    public static GroupChargingAuxSvcExtension getGroupChargingAuxSvcExtension(Context ctx, long auxiliaryServiceID) throws HomeException
    {
        Home home = (Home) ctx.get(GroupChargingAuxSvcExtensionHome.class);
        return (GroupChargingAuxSvcExtension) home.find(ctx, new EQ(GroupChargingAuxSvcExtensionXInfo.AUXILIARY_SERVICE_ID, auxiliaryServiceID));
    }
}
