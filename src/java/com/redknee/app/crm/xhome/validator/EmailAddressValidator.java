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
package com.redknee.app.crm.xhome.validator;

import javax.mail.internet.InternetAddress;

import com.redknee.framework.xhome.beans.IllegalPropertyArgumentException;
import com.redknee.framework.xhome.beans.Validator;
import com.redknee.framework.xhome.beans.xi.PropertyInfo;
import com.redknee.framework.xhome.context.Context;

import com.redknee.app.crm.exception.RethrowExceptionListener;


/**
 * A generic email address validator
 *
 * @author aaron.gourley@redknee.com
 * @since 8.6
 */
public class EmailAddressValidator implements Validator
{
    public EmailAddressValidator(PropertyInfo property)
    {
        property_ = property;
        if (property_ == null)
        {
            throw new NullPointerException("Email property can not be null.");
        }
        if (!String.class.isAssignableFrom(property_.getType()))
        {
            throw new IllegalArgumentException("Email property must be of type '" + String.class.getName() + "'.");
        }
    }

    /**
     * {@inheritDoc}
     */
    public void validate(Context ctx, Object obj) throws IllegalStateException
    {
        RethrowExceptionListener el = new RethrowExceptionListener();
        
        try
        {
            String address = (String) property_.get(obj);
            if (address != null && address.length() > 0)
            {
                new InternetAddress(address).validate();
            }
        }
        catch (javax.mail.internet.AddressException e)
        {
            el.thrown(new IllegalPropertyArgumentException(property_, "Invalid E-Mail Address"));
        }
        
        el.throwAllAsCompoundException();
    }

    protected PropertyInfo property_ = null;
}
