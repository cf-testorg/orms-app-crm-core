/*
 * Created on 2005-1-12
 */
package com.redknee.app.crm.support;

import java.util.Collection;
import java.util.Comparator;
import java.util.Date;

import com.redknee.app.crm.CoreCrmConstants;
import com.redknee.app.crm.bean.AbstractNote;
import com.redknee.app.crm.bean.Note;
import com.redknee.app.crm.bean.NoteHome;
import com.redknee.app.crm.bean.NoteXInfo;
import com.redknee.app.crm.bean.SystemNoteSubTypeEnum;
import com.redknee.app.crm.bean.SystemNoteTypeEnum;
import com.redknee.framework.xhome.auth.bean.User;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.And;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.elang.GTE;
import com.redknee.framework.xhome.elang.LT;
import com.redknee.framework.xhome.elang.LTE;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.SortingHome;
import com.redknee.framework.xhome.language.Lang;
import com.redknee.framework.xhome.language.LangXInfo;


/**
 * 
 * @author ksivasubramaniam
 *
 */
public class DefaultNoteSupport implements NoteSupport
{
    protected static NoteSupport instance_ = null;
    public static NoteSupport instance()
    {
        if (instance_ == null)
        {
            instance_ = new DefaultNoteSupport();
        }
        return instance_;
    }

    protected DefaultNoteSupport()
    {
    }
    
    
    

    @Override
    public Collection getInvoiceNotesFromSubscriberNotes(final Context ctx, final String subId) throws HomeException
    {
        Home home = (Home) ctx.get(CoreCrmConstants.SUBSCRIBER_NOTE_HOME);
        
        Collection notes = home.select(ctx, new EQ(NoteXInfo.ID_IDENTIFIER, subId));
        return notes;
    }

    @Override
    public Collection getInvoiceNotesFromSubscriberNotes(final Context ctx,final  String subId,final  String language, final Date startDate,
            Date endDate) throws HomeException
    {
        Lang lang = getLanguage(ctx, language);
        ctx.put(Lang.class, lang);
        Home home = (Home) ctx.get(CoreCrmConstants.SUBSCRIBER_NOTE_HOME);
        And where = new And();
        where.add(new EQ(NoteXInfo.ID_IDENTIFIER, subId));
        where.add(new GTE(NoteXInfo.CREATED, startDate));
        where.add(new LTE(NoteXInfo.CREATED, endDate));
        Collection notes = home.where(ctx, where).selectAll(ctx);   
        return notes;
    }

    @Override
    public Collection getNotesFromSubscriberNotes(final Context ctx,final  Object where, final String language) throws HomeException
    {
        Lang lang = getLanguage(ctx, language);
        ctx.put(Lang.class, lang);
        Home home = (Home) ctx.get(CoreCrmConstants.SUBSCRIBER_NOTE_HOME);
        Collection notes = home.where(ctx, where).selectAll(ctx);        
        return notes;
    }
    
    private Lang getLanguage(final Context ctx, final String language) throws HomeException
    {
        Lang lang = HomeSupportHelper.get(ctx).findBean(ctx, Lang.class, new EQ(LangXInfo.CODE, language));
        return lang;
    }
    
    public void addSubscriberNote(final Context ctx, final String subId, String notemsg,
            final SystemNoteTypeEnum notetype, final SystemNoteSubTypeEnum notesubtype) throws HomeException
    {
        final Home home = (Home) ctx.get(NoteHome.class);

        addNote(ctx, home, subId, notemsg, notetype.getDescription(), notesubtype.getDescription());
    }

    public void addNote(final Context ctx, final Home home, final String id, String notemsg,
            final String notetype, final String notesubtype) throws HomeException
    {
        final Note note = new Note();
        note.setIdIdentifier(id);
        note.setAgent(getAgent(ctx));
        note.setCreated(new Date());
        note.setType(notetype);
        note.setSubType(notesubtype);
        if (notemsg == null)
        {
            notemsg = "";
        }
        else if (notemsg.length() > AbstractNote.NOTE_WIDTH)
        {
            notemsg = notemsg.substring(0, AbstractNote.NOTE_WIDTH);
        }
        note.setNote(notemsg);
        home.create(ctx, note);
    }

    public void addAccountNote(final Context ctx, final String ban, String notemsg,
            final SystemNoteTypeEnum notetype, final SystemNoteSubTypeEnum notesubtype) throws HomeException
    {
        final Home home = (Home) ctx.get(CoreCrmConstants.ACCOUNT_NOTE_HOME);

        addNote(ctx, home, ban, notemsg, notetype.getDescription(), notesubtype.getDescription());
    }
    
    /**
     * Get Notes for the specified Account profile, logged in the given date range, for display on Invoice.
     * @param ctx
     * @param profileId - Account or Subscriber identifier
     * @param startDate - start of date range
     * @param endDate - end of date range
     * @return
     */
    public Collection<Note> getAccountInvoiceNotes(final Context ctx, 
            final String profileId, 
            final Date startDate, 
            final Date endDate)
        throws HomeException
    {
        final Home home = (Home) ctx.get(CoreCrmConstants.ACCOUNT_NOTE_HOME);
        return getInvoiceNotes(ctx, home, profileId, startDate, endDate);
    }
    
    /**
     * Get Notes for the specified Subscriber profile, logged in the given date range, for display on Invoice.
     * @param ctx
     * @param profileId - Account or Subscriber identifier
     * @param startDate - start of date range
     * @param endDate - end of date range
     * @return
     */
    public Collection<Note> getSubscriberInvoiceNotes(final Context ctx, 
            final String profileId, 
            final Date startDate, 
            final Date endDate)
        throws HomeException
    {
        final Home home = (Home) ctx.get(NoteHome.class);
        return getInvoiceNotes(ctx, home, profileId, startDate, endDate);
    }
    
    /**
     * Get Notes for the specified profile, logged in the given date range, for display on Invoice. 
     * @param ctx
     * @param home - Note Home
     * @param profileId - Account or Subscriber identifier
     * @param startDate - start of date range
     * @param endDate - end of date range
     * @return
     * @throws HomeException
     */
    public Collection<Note> getInvoiceNotes(final Context ctx, 
            Home home, 
            final String profileId, 
            final Date startDate, 
            final Date endDate)
        throws HomeException
    {
        And predicate = new And();
        predicate.add(new EQ(NoteXInfo.ID_IDENTIFIER, profileId));
        predicate.add(new GTE(NoteXInfo.CREATED, startDate));
        predicate.add(new LT(NoteXInfo.CREATED, endDate));
        predicate.add(new EQ(NoteXInfo.SHOW_ON_INVOICE, true));
        
        //Sort results by the date the notes were posted
        home = new SortingHome(home, new Comparator<Note>()
        {
            public int compare(Note note1, Note note2)
            {
                return note1.getCreated().compareTo(note2.getCreated()); 
            }
        });
        return home.where(ctx, predicate).selectAll();
    }
    
    public  String getAgent(final Context ctx)
    {
        final User principal = (User) ctx.get(java.security.Principal.class, new User());
        return (principal.getId().trim().equals("") ? CoreCrmConstants.SYSTEM_AGENT : principal.getId());
    }
    
    
}
