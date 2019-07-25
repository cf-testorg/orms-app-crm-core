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
package com.redknee.app.crm.home.validator;

import com.redknee.framework.xhome.beans.CompoundIllegalStateException;
import com.redknee.framework.xhome.beans.Validator;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.HomeOperationEnum;
import com.redknee.framework.xhome.home.HomeProxy;

/**
 * Validator home where validation is performed only for removal.
 * @author Marcio Marques
 * @since 8.5
 *
 */
public class RemovalValidatingHome
extends HomeProxy
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    protected Validator validator_;


    public Validator getValidator()
    {
        return validator_;
    }


    public void setValidator(Validator validator)
    {
        validator_ = validator;
    }


    public RemovalValidatingHome(Validator validator, Home delegate)
    {
        setDelegate(delegate);
        setValidator(validator);
    }


    public void remove(Context ctx, Object obj) throws HomeException
    {
        Context ctx1 = ctx.createSubContext();
        ctx1.put(HomeOperationEnum.class, HomeOperationEnum.CREATE);
        validate(ctx1, obj, getValidator());
        getDelegate().remove(ctx, obj);
    }


    protected void validate(Context ctx, Object obj, Validator validator) throws HomeException
    {
        try
        {
            validator.validate(ctx, obj);
        }
        catch (IllegalStateException e)
        {
            if (e instanceof CompoundIllegalStateException)
            {
                throw new HomeException(e.getMessage(), e);
            }
            else
            {
                throw new HomeException(e.getMessage(), new CompoundIllegalStateException(e));
            }
        }
    }
}
