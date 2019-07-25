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
package com.redknee.app.crm.bean.webcontrol;

import java.io.PrintWriter;

import com.redknee.app.crm.bean.PricePlanWebControl;
import com.redknee.app.crm.bean.PricePlanXInfo;
import com.redknee.app.crm.bean.core.PricePlan;
import com.redknee.app.crm.bean.core.SubscriptionType;
import com.redknee.app.crm.billing.message.GenericBillingMessageTableWebControl;
import com.redknee.app.crm.technology.TechnologyOnlyEnumWebControl;
import com.redknee.app.crm.web.control.HiddenCurrencyWebControl;
import com.redknee.framework.xhome.beans.ExceptionListener;
import com.redknee.framework.xhome.beans.IllegalPropertyArgumentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.relationship.RelationshipWebControl;
import com.redknee.framework.xhome.webcontrol.FinalWebControl;
import com.redknee.framework.xhome.webcontrol.HiddenWebControl;
import com.redknee.framework.xhome.webcontrol.ReadOnlyWebControl;
import com.redknee.framework.xhome.webcontrol.ViewModeEnum;
import com.redknee.framework.xhome.webcontrol.WebControl;


/**
 * 
 *
 * @author aaron.gourley@redknee.com
 * @since 
 */
public class CustomPricePlanWebControl extends PricePlanWebControl
{

    /**
     * {@inheritDoc}
     */
    @Override
    public WebControl getCurrentVersionChargeWebControl()
    {
        return CUSTOM_CURRENT_VERSION_CHARGE_WC;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public WebControl getTechnologyWebControl()
    {
        return CUSTOM_TECHNOLOGY_WC;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public WebControl getBillingMessagesWebControl()
    {
        return CUSTOM_BILLING_MESSAGES_WC;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void toWeb(Context ctx, PrintWriter out, String name, Object obj)
    {
        PricePlan bean = (PricePlan) obj;
        
        Context subCtx = ctx.createSubContext();

        // hide un-related price-plan properties
        final SubscriptionType subscriptionTypeObj;
        try
        {
            subscriptionTypeObj = bean.getSubscriptionType(ctx);
            if(null != subscriptionTypeObj)
            {
                if (!subscriptionTypeObj.isWireless())
                {
                    setMode(subCtx,PricePlanXInfo.SMSRATE_PLAN, ViewModeEnum.NONE);
                    setMode(subCtx,PricePlanXInfo.DATA_RATE_PLAN, ViewModeEnum.NONE);
                }
                if (!subscriptionTypeObj.isVoice())
                {
                    setMode(subCtx,PricePlanXInfo.VOICE_RATE_PLAN, ViewModeEnum.NONE);
                }
                if (!subscriptionTypeObj.isMessaging())
                {
                    setMode(subCtx,PricePlanXInfo.SMSRATE_PLAN, ViewModeEnum.NONE);
                }
                if (!subscriptionTypeObj.isData())
                {
                    setMode(subCtx,PricePlanXInfo.DATA_RATE_PLAN, ViewModeEnum.NONE);
                }
            }
            else
            {
                final ExceptionListener el = (ExceptionListener) ctx.get(ExceptionListener.class);
                if (el != null)
                {
                    el.thrown(new IllegalPropertyArgumentException(PricePlanXInfo.SUBSCRIPTION_TYPE, "Subscription Type Configuration for Subscription-Type-ID [" + bean.getSubscriptionType() + "] was not found."));
                }
            }

        }
        catch(Throwable t)
        {
            final ExceptionListener el = (ExceptionListener) ctx.get(ExceptionListener.class);
            if (el != null)
            {
                el.thrown(new IllegalPropertyArgumentException(PricePlanXInfo.SUBSCRIPTION_TYPE, t));
            }
        }

        super.toWeb(ctx, out, name, obj);
    }



//    public static final WebControl CUSTOM_VERSIONS_WC = 
  //      new RelationshipWebControl(PricePlanXInfo.VERSIONS);

  //      new com.redknee.framework.xhome.msp.SetSpidProxyWebControl(new WebControllerPricePlanVersionWebControl().setXmenuKey("appCRMPricePlanVersion"));
    public static final WebControl CUSTOM_BILLING_MESSAGES_WC = new GenericBillingMessageTableWebControl();
    public static final WebControl CUSTOM_CURRENT_VERSION_CHARGE_WC = new HiddenWebControl(new ReadOnlyWebControl(new HiddenCurrencyWebControl(false)));

    public static final WebControl CUSTOM_TECHNOLOGY_WC = new FinalWebControl(new TechnologyOnlyEnumWebControl());
}
