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
package com.redknee.app.crm.move.dependency;

import com.redknee.app.crm.move.MoveDependencyManager;


/**
 * The usual null style implementation of the MoveDependencyManager interface.
 *
 * @author Aaron Gourley
 * @since 8.1
 */
public class NullMoveDependencyManager extends EmptyMoveDependencyManager
{
    private static MoveDependencyManager instance_ = null;
    public static MoveDependencyManager instance()
    {
        if (instance_ == null)
        {
            instance_ = new NullMoveDependencyManager();
        }
        return instance_;
    }
    
    protected NullMoveDependencyManager()
    {
        super(null);
    }
}
