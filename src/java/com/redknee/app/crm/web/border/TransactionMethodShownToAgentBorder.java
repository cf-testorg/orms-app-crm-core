package com.redknee.app.crm.web.border;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.redknee.app.crm.bean.TransactionMethodHome;
import com.redknee.app.crm.bean.TransactionMethodXInfo;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.web.border.Border;
import com.redknee.framework.xhome.webcontrol.RequestServicer;

public class TransactionMethodShownToAgentBorder 
implements Border
{

	   public void service(
	            Context             ctx,
	      final HttpServletRequest  req,
	            HttpServletResponse res,
	            RequestServicer     delegate)
	      throws ServletException, IOException
	   {
	      ctx = ctx.createSubContext();
	      Home home = (Home)ctx.get(TransactionMethodHome.class);
	      
	      home = home.where(ctx, new EQ(TransactionMethodXInfo.SHOWN_TO_AGENT, new Boolean(true))); 
	      ctx.put(TransactionMethodHome.class, home );

	      delegate.service(ctx, req, res);
	   }
}
