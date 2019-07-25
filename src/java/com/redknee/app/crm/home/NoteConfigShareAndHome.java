/*
 * 
 * Copyright (c) Redknee, 2002 - all rights reserved
 */
package com.redknee.app.crm.home;

import com.redknee.app.crm.bean.AccountNote;
import com.redknee.app.crm.bean.AccountNoteHome;
import com.redknee.app.crm.bean.Note;
import com.redknee.app.crm.bean.NoteOwnerTypeEnum;
import com.redknee.app.crm.bean.SubscriberNote;
import com.redknee.app.crm.bean.SubscriberNoteHome;
import com.redknee.framework.xhome.beans.XBeans;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.HomeProxy;
import com.redknee.framework.xlog.log.MinorLogMsg;


/**
 * This allows Notes to be configshared by one home and one home will do the storge
 * support
 * 
 * @author ksivasubramaniam
 * 
 */
public class NoteConfigShareAndHome extends HomeProxy
{

    /** Subsequent delegates after the first. **/
    private final NoteOwnerTypeEnum ownerType_;


    public NoteConfigShareAndHome(Context ctx, Home local, NoteOwnerTypeEnum ownerType)
    {
        setContext(ctx);
        setDelegate(local);
        ownerType_ = ownerType;
    }


    public Object create(Context ctx, Object obj) throws HomeException
    {
        Object ret = getDelegate().create(ctx, obj);
        
        if(((Note) ret).getAutoPush())
        {
        
        Home home = getOwnerConfigShareHome(ctx);
        if (home != null)
        {
            try
            {
                Object newObj = getOwnerObject(ctx, (Note) ret);
                home.create(newObj);
            }
            catch (HomeException homeEx)
            {
                new MinorLogMsg(this, " Unable to config share " + ret, homeEx).log(ctx);
            }
        }
        }
        return ret;
    }


    public void remove(Context ctx, Object obj) throws HomeException
    {
        getDelegate().remove(ctx, obj);
        Home home = getOwnerConfigShareHome(ctx);
        if (home != null)
        {
            try
            {
                Object newObj = getOwnerObject(ctx, (Note) obj);
                home.remove(newObj);
            }
            catch (HomeException homeEx)
            {
                new MinorLogMsg(this, " Unable to config share " + obj, homeEx).log(ctx);
            }
        }
    }


    public void removeAll(Context ctx, Object obj) throws HomeException
    {
        getDelegate().removeAll(ctx, obj);
        Home home = getOwnerConfigShareHome(ctx);
        if (home != null)
        {
            try
            {
                // Object newObj = getOwnerObject(ctx, (Note) obj);
                home.removeAll(obj);
            }
            catch (Exception homeEx)
            {
                new MinorLogMsg(this, " Unable to config share " + obj, homeEx).log(ctx);
            }
        }
    }


    public Object store(Context ctx, Object obj) throws HomeException
    {
        Object ret = getDelegate().store(ctx, obj);
        Home home = getOwnerConfigShareHome(ctx);
        if (home != null)
        {
            try
            {
                Object newObj = getOwnerObject(ctx, (Note) ret);
                home.store(newObj);
            }
            catch (HomeException homeEx)
            {
                new MinorLogMsg(this, " Unable to config share " + ret, homeEx).log(ctx);
            }
        }
        return ret;
    }


    private Object getOwnerObject(final Context ctx, final Note note)
    {
        if (ownerType_ == NoteOwnerTypeEnum.ACCOUNT)
        {
            AccountNote accountNote = new AccountNote();
            XBeans.copy(ctx, note, accountNote);
            accountNote.setBan(note.getIdIdentifier());
            return accountNote;
        }
        else if (ownerType_ == NoteOwnerTypeEnum.SUBSCRIPTION)
        {
            SubscriberNote subNote = new SubscriberNote();
            XBeans.copy(ctx, note, subNote);
            return subNote;
        }
        return null;
    }


    private Home getOwnerConfigShareHome(final Context ctx)
    {
        Home home = null;
        if (ownerType_ == NoteOwnerTypeEnum.ACCOUNT)
        {
            home = (Home) ctx.get(AccountNoteHome.class);
        }
        else if (ownerType_ == NoteOwnerTypeEnum.SUBSCRIPTION)
        {
            home = (Home) ctx.get(SubscriberNoteHome.class);
        }
        return home;
    }
}
