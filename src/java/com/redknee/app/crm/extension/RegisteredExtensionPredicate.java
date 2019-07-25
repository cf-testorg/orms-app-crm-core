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

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.entity.EntityInfo;
import com.redknee.framework.xhome.filter.Predicate;
import com.redknee.framework.xhome.visitor.AbortVisitException;

import com.redknee.app.crm.support.ExtensionSupportHelper;


/**
 * 
 *
 * @author Aaron Gourley
 * @since 7.4.16
 */
public class RegisteredExtensionPredicate implements Predicate
{
    private static RegisteredExtensionPredicate instance_ = null;
    
    public static RegisteredExtensionPredicate instance()
    {
        if( instance_ == null )
        {
            instance_ = new RegisteredExtensionPredicate();
        }
        return instance_;
    }

    /**
     * @{inheritDoc}
     */
    public boolean f(Context context, Object obj) throws AbortVisitException
    {
        Class extensionCls = null;
        if( obj instanceof EntityInfo )
        {
            try
            {
                extensionCls = Class.forName(((EntityInfo)obj).getClassName());
            }
            catch (ClassNotFoundException e)
            {
                // Eat it.
            }
        }
        else if( obj instanceof Class )
        {
            extensionCls = (Class)obj;
        }
        else if( obj != null )
        {
            extensionCls = obj.getClass();
        }
        return ExtensionSupportHelper.get(context).isExtensionRegistered(context, extensionCls);
    }

}
