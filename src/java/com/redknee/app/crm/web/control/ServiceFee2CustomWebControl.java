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
package com.redknee.app.crm.web.control;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletRequest;

import com.redknee.framework.xhome.beans.CompoundIllegalStateException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xlog.log.MajorLogMsg;

import com.redknee.app.crm.bean.ServicePeriodEnum;
import com.redknee.app.crm.bean.Service;
import com.redknee.app.crm.bean.ServiceFee2TableWebControl;
import com.redknee.app.crm.bean.ServiceHome;
import com.redknee.app.crm.bean.ServicePeriodEnum;
import com.redknee.app.crm.bean.ServiceTypeEnum;
import com.redknee.app.crm.bean.core.ServiceFee2;


/**
 * Verifies the services are voice masil or not.
 * @author prasanna.kulkarni@redknee.com
 */
public class ServiceFee2CustomWebControl extends ServiceFee2TableWebControl
{
    /**
     * {@inheritDoc}
     */
    public Object fromWeb(final Context ctx, final ServletRequest req,
            final String name)
    {
        ServiceFee2 svcFeeBean = null;
        Service svc = null;
        final List selectedSvcs = new ArrayList();
        super.fromWeb(ctx, selectedSvcs, req, name);

        // TODO this is the wrong place for this check. This should be done in
        // an Validator
        boolean vmPresentFlag = false;
        for (int i = 0; i < selectedSvcs.size(); i++)
        {
            svcFeeBean = (ServiceFee2) selectedSvcs.get(i);
            if (svcFeeBean != null)
            {
                svc = getRelatedSvc(ctx, svcFeeBean.getServiceId());
                svcFeeBean.setServicePeriod(svc.getChargeScheme());
                svcFeeBean.setRecurrenceInterval(svc.getRecurrenceInterval());

                if (svc != null && svc.getType() == ServiceTypeEnum.VOICEMAIL)
                {
                    if (vmPresentFlag)
                    {
                        // TODO specify in the error message which 2 services
                        // conflict
                        throw new IllegalStateException(
                                "Only one voicemail service is allowed, please select only one");
                    }
                    vmPresentFlag = true;
                }
            }
        }

        return selectedSvcs;
    }

    /**
     * Returns the service relateed to the particular service ID.
     * @param ctx the operating context to get the homes
     * @param svcId the service id to retrieve
     * @return the service with the id = svcId
     */
    public Service getRelatedSvc(final Context ctx, final long svcId)
    {
        Service svc = null;
        final Home svcHome = (Home) ctx.get(ServiceHome.class);

        try
        {
            svc = (Service) svcHome.find(ctx, Long.valueOf(svcId));
        }
        catch (HomeException he)
        {
            new MajorLogMsg(this, "The service with serviceID:" + svcId
                    + " not found", he).log(ctx);
        }
        return svc;
    }
}
