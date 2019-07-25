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


/**
 * Base extension implementation
 *
 * @author Aaron Gourley
 * @since 7.4.16
 */
public class NullExtension implements Extension
{
    private static Extension instance_ = null;
    public static Extension instance()
    {
        if( instance_ == null )
        {
            instance_ = new NullExtension();
        }
        return instance_;
    }

    /**
     * @{inheritDoc}
     */
    public String getDescription(Context ctx)
    {
        return "";
    }


    /**
     * @{inheritDoc}
     */
    public String getName(Context ctx)
    {
        return "";
    }


    /**
     * @{inheritDoc}
     */
    public String getSummary(Context ctx)
    {
        return "";
    }
    

    /**
     * {@inheritDoc}
     */
    public Object ID()
    {
        return "";
    }

    public boolean isCategory()
    {
        return false;
    }
    
    public int getMaxChildren()
    {
        return 0;
    }

    public Class<? extends Extension> getExtensionCategoryClass()
    {
        return null;
    }
}
