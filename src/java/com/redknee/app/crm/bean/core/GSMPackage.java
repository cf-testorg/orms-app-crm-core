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

import com.redknee.app.crm.numbermgn.GenericPackage;
import com.redknee.app.crm.numbermgn.PackageProcessingException;
import com.redknee.app.crm.numbermgn.PackageProcessor;
import com.redknee.app.crm.resource.ResourceDeviceDefaultPackage;
import com.redknee.app.crm.resource.ResourceDeviceDefaultPackageXInfo;
import com.redknee.app.crm.support.HomeSupportHelper;
import com.redknee.framework.xhome.beans.ExceptionListener;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.context.ContextLocator;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xlog.log.MinorLogMsg;


/**
 * 
 * 
 * @author aaron.gourley@redknee.com
 * @since
 */
public class GSMPackage extends com.redknee.app.crm.bean.GSMPackage implements GenericPackage
{

    @Override
    public String getDefaultResourceID()
    {
        if (null == defaultResourceID_ || defaultResourceID_.length() < 1)
        {
            Context ctx = ContextLocator.locate();
            try
            {
                defaultResourceID_ = getDefaultResourceID(ctx);   
            }
            catch (Throwable t)
            {
                String errorMessage = "Error in fetching Default-Resource-ID [" + defaultResourceID_
                        + "] from Pacakge-Resourece-Device realationship home.";
                new MinorLogMsg(this, errorMessage, t).log(ctx);
                {
                    final ExceptionListener exceptionListner = (ExceptionListener) ctx.get(ExceptionListener.class);
                    exceptionListner.thrown(new IllegalStateException(errorMessage, t));
                }
                // NOP
            }
        }
        return ((null == defaultResourceID_) ? ("") : (defaultResourceID_));
    }

    public String getDefaultResourceID(Context ctx) throws HomeException
    {
        if(null==ctx)
        {
            return null;
        }
        ResourceDeviceDefaultPackage association = 
                    HomeSupportHelper.get(ctx).findBean(ctx,ResourceDeviceDefaultPackage.class,
                             new EQ(ResourceDeviceDefaultPackageXInfo.PACK_ID,getPackId()));
        if(null == association)
        {
            return null;
        }
        else
        {
            return association.getResourceID();
        }
    }

    public Object delegatePackageProcessing(
        final Context context,
        final PackageProcessor processor)
        throws PackageProcessingException
    {
        return processor.processPackage(context, this);
    }
}
