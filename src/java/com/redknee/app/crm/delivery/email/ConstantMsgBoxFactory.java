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

import com.redknee.framework.msg.MsgBox;
import com.redknee.framework.xhome.context.Context;


/**
 * This factory returns the MsgBox instance provided in the constructor.
 *
 * @author aaron.gourley@redknee.com
 * @since 8.8/9.0
 */
public class ConstantMsgBoxFactory implements MsgBoxFactory
{
    public ConstantMsgBoxFactory(MsgBox box)
    {
        value_ = box;
    }

    /**
     * {@inheritDoc}
     */
    public Object create(Context ctx)
    {
        return getMessageBox(ctx);
    }
    
    /**
     * {@inheritDoc}
     */
    public MsgBox getMessageBox(Context ctx)
    {
        return value_;
    }

    public void setMsgBox(MsgBox box)
    {
        value_ = box;
    }

    protected MsgBox value_;
}
