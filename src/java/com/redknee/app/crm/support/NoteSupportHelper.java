/*
 * This code is a protected work and subject to domestic and international copyright
 * law(s). A complete listing of authors of this work is readily available. Additionally,
 * source code is, by its very nature, confidential information and inextricably contains
 * trade secrets and other information proprietary, valuable and sensitive to Redknee. No
 * unauthorized use, disclosure, manipulation or otherwise is permitted, and may only be
 * used in accordance with the terms of the license agreement entered into with Redknee
 * Inc. and/or its subsidiaries.
 * 
 * Copyright (c) Redknee Inc. and its subsidiaries. All Rights Reserved.
 */
package com.redknee.app.crm.support;

import com.redknee.framework.xhome.context.Context;


/**
 * This class eases installation/retrieval of the NoteSupport interface
 * 
 */
public final class NoteSupportHelper extends SupportHelper
{

    private NoteSupportHelper()
    {
    }


    /**
     * @deprecated Use contextualized version of method
     */
    public static NoteSupport get()
    {
        return get(NoteSupport.class, DefaultNoteSupport.instance());
    }


    public static NoteSupport get(Context ctx)
    {
        NoteSupport instance = get(ctx, NoteSupport.class, DefaultNoteSupport.instance());
        return instance;
    }


    public static NoteSupport set(Context ctx, NoteSupport instance)
    {
        return register(ctx, NoteSupport.class, instance);
    }
} // class
