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
 * Copyright &copy; Redknee Inc. and its subsidiaries. All Rights Reserved.
 *
 */
package com.redknee.app.crm.home;

import com.redknee.framework.xhome.beans.Function;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xlog.log.LogSupport;

import com.redknee.app.crm.bean.Note;
import com.redknee.app.crm.bean.NoteOwnerTypeEnum;

/**
 * @author cindy.wong@redknee.com
 * @since 2011-03-04
 */
public class NoteOwnerTypeSettingFunction implements Function
{

	/**
	 * Serial version UID.
	 */
	private static final long serialVersionUID = 1L;

	public NoteOwnerTypeSettingFunction(NoteOwnerTypeEnum ownerType)
	{
		this.ownerType_ = ownerType;
	}

	@Override
	public Object f(Context ctx, Object obj)
	{
		if (obj instanceof Note)
		{
			Note note = (Note) obj;
			
			Note noteclone = null;
	 		try 
	 		{
	 			noteclone = (Note) note.clone();
	 		} 
	 		catch (Exception ex) 
	 		{
	             LogSupport.minor(ctx,this,"Could not clone the Note " +obj , ex);
	             return note;
	 		}
	 		
	 		noteclone.setOwnerType(ownerType_);
	 		return noteclone;
		}
		return obj;
	}

	private final NoteOwnerTypeEnum ownerType_;
}
