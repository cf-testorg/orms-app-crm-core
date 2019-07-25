/**
 * 
 */
package com.redknee.app.crm.home;

import java.io.IOException;
import java.rmi.RemoteException;

import com.redknee.app.crm.bean.OverPaymentRun;
import com.redknee.app.crm.bean.OverPaymentRunXDBHome;
import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.NoSelectAllHome;

/**
 * @author alok.sohani
 * @since 9.7.2
 */
public class OverPaymentRunHomePipelineFactory implements PipelineFactory{

		@Override
		public Home createPipeline(Context ctx, Context serverCtx)
				throws RemoteException, HomeException, IOException, AgentException {
			
			Home home = new OverPaymentRunXDBHome(ctx, "OVERPAYMENTRUN");
			
			home = new NoSelectAllHome(home);		
			
			return home;
		}

}
