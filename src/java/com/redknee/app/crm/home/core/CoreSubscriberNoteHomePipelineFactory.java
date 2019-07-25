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
 * Copyright  Redknee Inc. and its subsidiaries. All Rights Reserved. 
 */
package com.redknee.app.crm.home.core;


import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Comparator;

import com.redknee.app.crm.bean.Note;
import com.redknee.app.crm.bean.NoteOwnerTypeEnum;
import com.redknee.app.crm.bean.SubscriberNote;
import com.redknee.app.crm.bean.SubscriberNoteHome;
import com.redknee.app.crm.support.ConfigChangeRequestSupportHelper;
import com.redknee.app.crm.xhome.adapter.SubscriberNoteTranslationAdapter;
import com.redknee.framework.xhome.beans.ReverseComparator;
import com.redknee.framework.xhome.beans.SafetyUtil;
import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.AdapterHome;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.NullHome;
import com.redknee.framework.xhome.home.SortingHome;


/**
 * 
 * @author Aaron Gourley
 * @since 7.7
 * Ported from 7.7
 */
public class CoreSubscriberNoteHomePipelineFactory extends CoreNoteHomePipelineFactory
{
    public CoreSubscriberNoteHomePipelineFactory()
    {
        super("SUBSCRIBERNOTE", NoteOwnerTypeEnum.SUBSCRIPTION);
    }

    /* (non-Javadoc)
     * @see com.redknee.app.crm.home.PipelineFactory#createPipeline(com.redknee.framework.xhome.context.Context, com.redknee.framework.xhome.context.Context)
     */
    @Override
    public Home createPipeline(Context ctx, Context serverCtx) throws RemoteException, HomeException, IOException,
            AgentException
    {
        Home home  = new AdapterHome(super.createPipeline(ctx, serverCtx), new SubscriberNoteTranslationAdapter());
        return new SortingHome(ctx, home , new ReverseComparator(new Comparator<Note>()
        {
            @Override
            public int compare(Note note1, Note note2)
            {
                return SafetyUtil.safeCompare(note1.getLastModified(), note2.getLastModified());
            }
        }));
    }
    
    

    
}