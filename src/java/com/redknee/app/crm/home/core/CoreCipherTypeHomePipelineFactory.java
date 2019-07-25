package com.redknee.app.crm.home.core;

import java.io.IOException;
import java.rmi.RemoteException;

import com.redknee.app.crm.bean.CipherType;
import com.redknee.app.crm.home.PipelineFactory;
import com.redknee.app.crm.support.StorageSupportHelper;
import com.redknee.framework.core.platform.CoreSupport;
import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;

public class CoreCipherTypeHomePipelineFactory implements PipelineFactory
{
	public CoreCipherTypeHomePipelineFactory()
	{
		super();
	}

	@Override
	public Home createPipeline(Context ctx, Context serverCtx)
	    throws RemoteException, HomeException, IOException, AgentException
	{
		Home home = CoreSupport.bindHome(ctx, CipherType.class); 
		    
		
		return home;
	}

}