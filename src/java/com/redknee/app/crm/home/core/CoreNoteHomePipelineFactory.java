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
package com.redknee.app.crm.home.core;

import java.io.IOException;
import java.rmi.RemoteException;

import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.LastModifiedAwareHome;
import com.redknee.framework.xhome.home.NoSelectAllHome;

import com.redknee.app.crm.bean.Note;
import com.redknee.app.crm.bean.NoteOwnerTypeEnum;
import com.redknee.app.crm.bean.NoteXDBHome;
import com.redknee.app.crm.home.NoteConfigShareAndHome;
import com.redknee.app.crm.home.NoteOwnerTypeSettingHome;
import com.redknee.app.crm.home.PipelineFactory;
import com.redknee.app.crm.support.ConfigChangeRequestSupportHelper;
import com.redknee.app.crm.xhome.home.UserAgentHome;


/**
 * 
 * @author Aaron Gourley
 * @since 7.7
 */
public class CoreNoteHomePipelineFactory implements PipelineFactory
{
    public CoreNoteHomePipelineFactory()
    {
    }
    
    public CoreNoteHomePipelineFactory(String tableName)
    {
        tableName_ = tableName;
    }
    
	public CoreNoteHomePipelineFactory(String tableName,
	    NoteOwnerTypeEnum ownerType)
	{
		tableName_ = tableName;
		ownerType_ = ownerType;
	}

    /* (non-Javadoc)
     * @see com.redknee.app.crm.home.PipelineFactory#createPipeline(com.redknee.framework.xhome.context.Context, com.redknee.framework.xhome.context.Context)
     */
    @Override
    public Home createPipeline(Context ctx, Context serverCtx) throws RemoteException, HomeException, IOException,
            AgentException
    {
		Home home = new NoteXDBHome(ctx, tableName_);
		home = new NoteOwnerTypeSettingHome(ctx, home, ownerType_);
		home = new UserAgentHome(ctx, home);
		home = new LastModifiedAwareHome(home);
		home = new NoSelectAllHome(home);
		home = new NoteConfigShareAndHome(ctx, home, ownerType_);
		return home;
    }

    protected String tableName_ = NoteXDBHome.DEFAULT_TABLE_NAME;
	protected NoteOwnerTypeEnum ownerType_ = NoteOwnerTypeEnum.ACCOUNT;
}
