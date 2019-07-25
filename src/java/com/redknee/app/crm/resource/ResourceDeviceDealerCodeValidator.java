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
package com.redknee.app.crm.resource;

import com.redknee.framework.xhome.beans.IllegalPropertyArgumentException;
import com.redknee.framework.xhome.beans.Validator;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.HomeOperationEnum;

import com.redknee.app.crm.bean.DealerCode;
import com.redknee.app.crm.bean.DealerCodeXInfo;
import com.redknee.app.crm.exception.RethrowExceptionListener;
import com.redknee.app.crm.support.HomeSupportHelper;
import com.redknee.app.crm.bean.core.ResourceDevice;

/**
 * 
 *
 * @author victor.stratan@redknee.com
 */
public class ResourceDeviceDealerCodeValidator implements Validator
{
    /**
     *
     * {@inheritDoc}
     */
    public void validate(final Context ctx, final Object obj) throws IllegalStateException
    {
        final ResourceDevice resource = (ResourceDevice) obj;
        final RethrowExceptionListener exceptions = new RethrowExceptionListener();

        boolean doValidate = false;
        final HomeOperationEnum operation = (HomeOperationEnum) ctx.get(HomeOperationEnum.class);
        if (HomeOperationEnum.STORE.equals(operation))
        {
            final ResourceDevice oldResource = ResourceDevice.getResourceDevice(ctx, resource.getResourceID());
            if (oldResource != null)
            {
                doValidate = !oldResource.getDealerCode().equals(resource.getDealerCode());
            }
            else
            {
                exceptions.thrown(new IllegalPropertyArgumentException(ResourceDeviceXInfo.RESOURCE_ID,
                        "Unable to find Resource Device with ID " + resource.getResourceID()));
            }
        }
        else
        {
            // CREATE operation
            doValidate = true;
        }
        
        if (doValidate)
        {
            try
            {
                final EQ where = new EQ(DealerCodeXInfo.CODE, resource.getDealerCode());
                final DealerCode dealer = HomeSupportHelper.get(ctx).findBean(ctx, DealerCode.class, where);
                if (dealer == null)
                {
                    exceptions.thrown(new IllegalPropertyArgumentException(ResourceDeviceXInfo.DEALER_CODE,
                            "Unable to find Dealer Code " + resource.getDealerCode()));
                }
            }
            catch (HomeException e)
            {
                final Exception exception = new IllegalPropertyArgumentException(ResourceDeviceXInfo.DEALER_CODE,
                        "Unable to find Dealer Code " + resource.getDealerCode());
                exception.initCause(e);
                exceptions.thrown(exception);
            }
        }

        exceptions.throwAllAsCompoundException();
    }
}
