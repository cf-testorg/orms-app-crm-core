package com.redknee.app.crm.home.core;

import java.io.IOException;
import java.rmi.RemoteException;

import com.redknee.app.crm.bean.CipherKey;
import com.redknee.app.crm.bean.CipherKeyTransientHome;
import com.redknee.app.crm.home.PipelineFactory;
import com.redknee.app.crm.support.StorageSupportHelper;
import com.redknee.app.crm.util.cipher.CustomizeCipherKeyHome;
import com.redknee.app.crm.xhome.home.TotalCachingHome;
import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;

public class CoreCipherKeyHomePipelineFactory implements PipelineFactory
{
	public CoreCipherKeyHomePipelineFactory()
	{
		super();
	}

	@Override
	public Home createPipeline(Context ctx, Context serverCtx)
	    throws RemoteException, HomeException, IOException, AgentException
	{
		Home home =
		    StorageSupportHelper.get(ctx).createHome(ctx,
		        CipherKey.class, "CIPHERKEY");		 
		home =  new TotalCachingHome( ctx, new CipherKeyTransientHome(ctx),home);

		home = new CustomizeCipherKeyHome(home);
		
		return home;
	}

}
