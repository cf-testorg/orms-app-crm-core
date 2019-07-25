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
package com.redknee.app.crm.home;

import com.redknee.framework.msg.EmailConfig;
import com.redknee.framework.msg.EmailConfigXInfo;
import com.redknee.framework.xhome.beans.CompoundIllegalStateException;
import com.redknee.framework.xhome.beans.IllegalPropertyArgumentException;
import com.redknee.framework.xhome.beans.MissingRequireValueException;
import com.redknee.framework.xhome.beans.Validator;
import com.redknee.framework.xhome.context.Context;

import com.redknee.app.crm.delivery.email.CRMEmailConfig;
import com.redknee.app.crm.xhome.validator.EmailAddressValidator;

/**
 * Validates the reply to address for the CRM email config.
 * @author marcio.marques@redknee.com
 *
 */
public class EmailConfigReplyToAddressValidator implements Validator
{
    private static final EmailAddressValidator EMAIL_SYNTAX_VALIDATOR = new EmailAddressValidator(EmailConfigXInfo.REPLY_TO_ADDRESS);

    /**
     * Creates a new EmailConfigReplyToAddressValidator.
     */
    private EmailConfigReplyToAddressValidator()
    {
    }


    /**
     * Gets the singleton instance of this class.
     *
     * @return The singleton instance of this class.
     */
    public static EmailConfigReplyToAddressValidator instance()
    {
        return instance_;
    }
    
    /**
     * {@inheritDoc}
     */
    public void validate(final Context ctx, final Object obj)
    {
        final CompoundIllegalStateException el = new CompoundIllegalStateException();
        EmailConfig emailConfig = null;
        if (obj instanceof CRMEmailConfig)
        {
            final CRMEmailConfig crmEmailConfig = (CRMEmailConfig) obj;
            emailConfig = crmEmailConfig.getSmtpConfig();
        }
        else if (obj instanceof EmailConfig)
        {
            emailConfig = (EmailConfig) obj;
        }
        
        if (emailConfig != null)
        {
            String replyToAddress = emailConfig.getReplyToAddress();
            if (replyToAddress == null || replyToAddress.trim().length() == 0)
            {
                el.thrown(new MissingRequireValueException(EmailConfigXInfo.REPLY_TO_ADDRESS));
            }
            else
            {
                try
                {
                    EMAIL_SYNTAX_VALIDATOR.validate(ctx, emailConfig);
                }
                catch (IllegalStateException e)
                {
                    el.thrown(e);
                }
                catch (Exception e)
                {
                    el.thrown(new IllegalPropertyArgumentException(EmailConfigXInfo.REPLY_TO_ADDRESS,
                            "Invalid Reply To Address"));
                }
            }
        }
        
        el.throwAll();
    }

    private static final EmailConfigReplyToAddressValidator instance_ = new EmailConfigReplyToAddressValidator();
    
}
