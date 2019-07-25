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

import java.util.Collection;
import java.util.Date;

import com.redknee.app.crm.bean.Note;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.HomeException;


public interface CoreNoteSupport<T extends Note> extends Support
{

    public Collection<Note> getInvoiceNotesFromSubscriberNotes(final Context ctx, final String subId)
            throws HomeException;


    public Collection<Note> getInvoiceNotesFromSubscriberNotes(final Context ctx, final String subId,
            final String language, final Date startDate, final Date endDate) throws HomeException;


    public Collection<Note> getNotesFromSubscriberNotes(final Context ctx, final Object where, final String language) throws HomeException;
}
