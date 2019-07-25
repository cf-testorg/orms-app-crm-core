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
package com.redknee.app.crm.delivery.email;

import com.redknee.app.crm.support.EmailSupportHelper;
import com.redknee.framework.msg.EmailConfig;
import com.redknee.framework.msg.Msg;
import com.redknee.framework.msg.MsgBox;
import com.redknee.framework.msg.MsgBoxProxy;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.pipe.Throttle;
import com.redknee.framework.xlog.log.MajorLogMsg;
import com.redknee.framework.xlog.log.MinorLogMsg;


/**
 * 
 *
 * @author aaron.gourley@redknee.com
 * @since 8.2
 */
public class CRMEmailConfigMsgBoxProxy extends MsgBoxProxy
{

    public CRMEmailConfigMsgBoxProxy(MsgBox delegate)
    {
        super(delegate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void send(Context ctx, Msg msg)
    {
        CRMEmailConfig config=(CRMEmailConfig) ctx.get(CRMEmailConfig.class);
        if(config==null)
        {
            new MajorLogMsg(this,"Cannot find CRM SMTP server config.  Request to SMTP server will not be throttled and global configuration will be used (if available).",null).log(ctx);
        }
        else
        {
            ctx = ctx.createSubContext().put(EmailConfig.class, config.getSmtpConfig());

            Throttle throttle = EmailSupportHelper.get(ctx).getSMTPThrottle(config);
            if (throttle.getThrottleInfo().isEnabled())
            {
                throttle.sleep();
            }   
        }
        try
        {
            super.send(ctx, msg);
        }
        catch (Exception aEx)
        {
            new MinorLogMsg(this,"Unable to send msg " + msg,aEx).log(ctx);
        }
    }

}
