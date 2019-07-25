/*
 * This code is a protected work and subject to domestic and international
 * copyright law(s). A complete listing of authors of this work is readily
 * available. Additionally, source code is, by its very nature, confidential
 * information and inextricably contains trade secrets and other information
 * proprietary, valuable and sensitive to Redknee. No unauthorized use,
 * disclosure, manipulation or otherwise is permitted, and may only be used in
 * accordance with the terms of the license agreement entered into with Redknee
 * Inc. and/or its subsidiaries.
 * 
 * Copyright (c) Redknee Inc. and its subsidiaries. All Rights Reserved.
 */
package com.redknee.app.crm.notification;

import java.util.Properties;

import com.redknee.framework.msg.Email;
import com.redknee.framework.msg.EmailTemplate;
import com.redknee.framework.msg.Msg;
import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.context.ContextAgent;
import com.redknee.framework.xlog.log.PMLogMsg;

public class EmailExecutionContextAgent implements ContextAgent
{
    private static EmailExecutionContextAgent instance_ = null;

    public static EmailExecutionContextAgent instance()
    {
        if (instance_ == null)
        {
            instance_ = new EmailExecutionContextAgent();
        }
        return instance_;
    }

    /**
     * {@inheritDoc}
     */
    public void execute(Context ctx) throws AgentException
    {
        PMLogMsg pm = new PMLogMsg(EmailExecutionContextAgent.class.getSimpleName(), "execute()");
        try
        {
            Msg message = (Msg) ctx.get(Msg.class);
            EmailTemplate template = (EmailTemplate) ctx.get(EmailTemplate.class);
            Properties properties = (Properties) ctx.get(Properties.class);
            Email.sendEmailFromTemplate(ctx, message, template.getName(), properties);
        }
        finally
        {
            pm.log(ctx);
        }
    }

    private EmailExecutionContextAgent()
    {
    }
}
