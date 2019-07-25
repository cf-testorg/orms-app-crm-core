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

import java.util.Collection;

import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.HomeInternalException;
import com.redknee.framework.xhome.home.HomeProxy;
import com.redknee.framework.xhome.visitor.FunctionVisitor;
import com.redknee.framework.xhome.visitor.ListBuildingVisitor;
import com.redknee.framework.xhome.visitor.Visitor;
import com.redknee.framework.xhome.visitor.Visitors;
import com.redknee.framework.xlog.log.LogSupport;

import com.redknee.app.crm.bean.NoteOwnerTypeEnum;

/**
 * Sets the note owner type when retrieving the note. This field is really only
 * needed by config sharing, but could be useful elsewhere.
 * 
 * @author cindy.wong@redknee.com
 * @since 2011-03-04
 */
public class NoteOwnerTypeSettingHome extends HomeProxy
{
	/**
	 * Serial version UID.
	 */
	private static final long serialVersionUID = 1L;

	public NoteOwnerTypeSettingHome(Context ctx, Home delegate,
	    NoteOwnerTypeEnum ownerType)
	{
		super(ctx, delegate);
		this.function_ = new NoteOwnerTypeSettingFunction(ownerType);
	}

	@Override
	public Object find(Context ctx, Object obj) throws HomeException,
	    HomeInternalException
	{
		return this.function_.f(ctx, super.find(ctx, obj));
	}

	@Override
	public Collection select(Context ctx, Object obj) throws HomeException,
	    HomeInternalException
	{
		ListBuildingVisitor result = new ListBuildingVisitor();
		FunctionVisitor functionVisitor =
		    new FunctionVisitor(this.function_, result);
		try
		{
			Visitors.forEach(ctx, super.select(ctx, obj), functionVisitor);
		}
		catch (AgentException exception)
		{
			LogSupport.info(ctx, this,
			    "Exception caught while setting note owner", exception);
			throw new HomeException(exception);
		}
		return result;
	}

	@Override
	public Visitor forEach(Context ctx, Visitor visitor, Object where)
	    throws HomeException, HomeInternalException
	{
		Visitor result = null;
		FunctionVisitor functionVisitor =
		    new FunctionVisitor(this.function_, visitor);
		functionVisitor =
		    (FunctionVisitor) super.forEach(ctx, functionVisitor, where);
		if (functionVisitor != null)
		{
			result = functionVisitor.getDelegate();
		}
		return result;
	}

	private final NoteOwnerTypeSettingFunction function_;
}
