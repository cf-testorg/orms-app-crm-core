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
import com.redknee.app.crm.bean.SystemNoteSubTypeEnum;
import com.redknee.app.crm.bean.SystemNoteTypeEnum;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;


public interface NoteSupport<T extends Note> extends Support
{

    public Collection<Note> getInvoiceNotesFromSubscriberNotes(final Context ctx, final String subId)
            throws HomeException;


    public Collection<Note> getInvoiceNotesFromSubscriberNotes(final Context ctx, final String subId,
            final String language, final Date startDate, final Date endDate) throws HomeException;


    public Collection<Note> getNotesFromSubscriberNotes(final Context ctx, final Object where, final String language)
            throws HomeException;


    public void addAccountNote(final Context ctx, final String ban, String notemsg, final SystemNoteTypeEnum notetype,
            final SystemNoteSubTypeEnum notesubtype) throws HomeException;


    public void addNote(final Context ctx, final Home home, final String id, String notemsg, final String notetype,
            final String notesubtype) throws HomeException;


    public void addSubscriberNote(final Context ctx, final String subId, String notemsg,
            final SystemNoteTypeEnum notetype, final SystemNoteSubTypeEnum notesubtype) throws HomeException;


    public Collection<Note> getInvoiceNotes(final Context ctx, Home home, final String profileId, final Date startDate,
            final Date endDate) throws HomeException;


    public Collection<Note> getSubscriberInvoiceNotes(final Context ctx, final String profileId, final Date startDate,
            final Date endDate) throws HomeException;


    public Collection<Note> getAccountInvoiceNotes(final Context ctx, final String profileId, final Date startDate,
            final Date endDate) throws HomeException;
}