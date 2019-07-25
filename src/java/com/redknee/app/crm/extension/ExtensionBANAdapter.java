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
package com.redknee.app.crm.extension;

import java.util.Collection;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Adapter;
import com.redknee.framework.xhome.home.HomeException;

import com.redknee.app.crm.account.BANAware;


/**
 * This adapter sets the BAN property of all extensions in the bean.
 *
 * @author Aaron Gourley
 * @since 8.2
 */
public class ExtensionBANAdapter implements Adapter
{
    /**
     * @{inheritDoc}
     */
    public Object adapt(Context ctx, Object obj) throws HomeException
    {
        if( obj instanceof BANAware
                && obj instanceof ExtensionAware)
        {   
            Collection<Extension> extensions = ((ExtensionAware)obj).getExtensions();
            if( extensions != null )
            {
                for( Extension extension : extensions )
                {
                    if( extension instanceof BANAware )
                    {
                        ((BANAware)extension).setBAN(((BANAware)obj).getBAN());
                    }
                }
            }
        }
        return obj;
    }


    /**
     * @{inheritDoc}
     */
    public Object unAdapt(Context ctx, Object obj) throws HomeException
    {
        return obj;
    }

}
