/*
 * This code is a protected work and subject to domestic and international
 * copyright law(s). A complete listing of authors of this work is readily 
 * available. Additionally, source code is, by its very nature, confidential 
 * information and inextricably contains trade secrets and other information 
 * proprietary, valuable and sensitive to Redknee, no unauthorised use, 
 * disclosure, manipulation or otherwise is permitted, and may only be used 
 * in accordance with the terms of the licence agreement entered into with 
 * Redknee Inc. and/or its subsidiaries.
 *
 * Copyright (c) Redknee Inc. and its subsidiaries. All Rights Reserved.
 */
package com.redknee.app.crm.web.function;

import com.redknee.framework.xhome.beans.Function;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.language.MessageMgr;

import com.redknee.app.crm.extension.Extension;
import com.redknee.app.crm.extension.ExtensionHolder;


/**
 * 
 *
 * @author Aaron Gourley
 * @since 7.4.16
 */
public class ExtensionHolderGenericTableFunction implements Function
{
    protected static final Function instance_ = new ExtensionHolderGenericTableFunction();

    protected ExtensionHolderGenericTableFunction()
    {
    }

    public static Function instance()
    {
        return instance_;
    }

    public Object f(Context ctx, Object obj)
    {
        if (obj instanceof ExtensionHolder)
        {
            ExtensionHolder holder = (ExtensionHolder) obj;
            Extension extension = holder.getExtension();
            if( extension != null )
            {
                return extension.getName(ctx);   
            }
        }
        return new MessageMgr(ctx, this).get("Extension.DefaultName", "Select an extension...");
    }
}
