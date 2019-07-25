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
import com.redknee.app.crm.bean.TFAAuxiliaryServiceExtensionTypeEnum;
import com.redknee.app.crm.extension.DependencyValidatableExtension;
import com.redknee.app.crm.extension.FinalExtension;
import com.redknee.app.crm.extension.MandatoryExtension;
import com.redknee.app.crm.extension.TypeDependentExtension;
import com.redknee.app.crm.extension.auxiliaryservice.TFAAuxSvcExtensionHome;
import com.redknee.app.crm.extension.auxiliaryservice.TFAAuxSvcExtensionXInfo;
import com.redknee.framework.xhome.beans.CompoundIllegalStateException;
import com.redknee.framework.xhome.beans.IllegalPropertyArgumentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.xenum.AbstractEnum;
import com.redknee.framework.xhome.xenum.EnumCollection;

/**
 * Implements the TFA auxiliary service extension.
 * @author Bhagyashree
 * @since 9.4.0
 *
 */
public class TFAAuxSvcExtension extends com.redknee.app.crm.extension.auxiliaryservice.TFAAuxSvcExtension implements FinalExtension, MandatoryExtension, TypeDependentExtension, DependencyValidatableExtension
{ 
	 @Override
	    public boolean isValidForType(AbstractEnum auxiliaryServiceType)
	    {
	        return isExtensionValidForType(auxiliaryServiceType);
	    }
	    
	    public static boolean isExtensionValidForType(AbstractEnum auxiliaryServiceType)
	    {
	        return AuxiliaryServiceTypeEnum.TFA.equals(auxiliaryServiceType);
	    }

	    public static TFAAuxSvcExtension getTfaServiceExtention(Context ctx, long auxiliaryServiceID) throws HomeException
	    {
	        Home home = (Home) ctx.get(TFAAuxSvcExtensionHome.class);
	        return (TFAAuxSvcExtension) home.find(ctx, new EQ(TFAAuxSvcExtensionXInfo.AUXILIARY_SERVICE_ID, auxiliaryServiceID));
	    }

		@Override
		public void validateDependency(Context ctx)
				throws IllegalStateException {
			// TODO Auto-generated method stub
			
		}
}
