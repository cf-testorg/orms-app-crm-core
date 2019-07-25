package com.redknee.app.crm.extension.auxiliaryservice.core;

import com.redknee.app.crm.bean.AuxiliaryServiceTypeEnum;
import com.redknee.app.crm.extension.FinalExtension;
import com.redknee.app.crm.extension.MandatoryExtension;
import com.redknee.app.crm.extension.TypeDependentExtension;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.xenum.AbstractEnum;
import com.redknee.app.crm.extension.auxiliaryservice.*; 

public class PromOptOutAuxSvcExtension 
extends com.redknee.app.crm.extension.auxiliaryservice.PromOptOutAuxSvcExtension
implements FinalExtension,MandatoryExtension, TypeDependentExtension
{
    @Override
    public boolean isValidForType(AbstractEnum auxiliaryServiceType)
    {
        return isExtensionValidForType(auxiliaryServiceType);
    }
    
    public static boolean isExtensionValidForType(AbstractEnum auxiliaryServiceType)
    {
        return AuxiliaryServiceTypeEnum.PROMOTION_SMS_OPT_OUT.equals(auxiliaryServiceType);
    }

    public static PromOptOutAuxSvcExtension getPromOptOutAuxSvcExtension(Context ctx, long auxiliaryServiceID) throws HomeException
    {
        Home home = (Home) ctx.get(PromOptOutAuxSvcExtensionHome.class);
        return (PromOptOutAuxSvcExtension) home.find(ctx, new EQ(PromOptOutAuxSvcExtensionXInfo.AUXILIARY_SERVICE_ID, auxiliaryServiceID));
    }
}
