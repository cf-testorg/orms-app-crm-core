package com.redknee.app.crm.core.agent;

import com.redknee.framework.core.platform.CoreSupport;
import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.context.ContextAgent;
import com.redknee.framework.xlog.log.LogSupport;

import com.redknee.app.crm.bean.tcbsrp.*;
import com.redknee.app.crm.tcbsrp.*;

/**
 * 
 * This install class takes care of all bean/home installs for generating
 * TCB SRP files. This is separated out so that any BSS app should 
 * be able to install this by calling this agent.
 * 
 * @author ameya.bhurke@redknee.com
 *
 */
public class TcbSrpInstall extends CoreSupport implements ContextAgent {

	public void execute(Context ctx) throws AgentException {
		
		LogSupport.info(ctx, TcbSrpInstall.class, " :: begin");
		
		CoreSupport.bindBean(ctx, TcbSrpFileConfig.class);
		ctx.put(SRPFileExtensionCounter.class , SRPFileExtensionCounter.instance(ctx));
		
		ctx.put(TcbSrpHome.class, new TcbSrpFileBufferedHome(ctx));
		
		LogSupport.info(ctx, TcbSrpInstall.class, " :: end");
	}

}
