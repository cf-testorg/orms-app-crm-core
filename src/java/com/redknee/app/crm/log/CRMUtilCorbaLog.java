package com.redknee.app.crm.log;

import com.redknee.util.corba.Log;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xlog.log.*; 


public class CRMUtilCorbaLog implements Log
{
        public CRMUtilCorbaLog(Context ctx, String name) {
		ctx_ = ctx; 
		name_ = name; 
	}


	
	public void debug(String msg)
	{
           new DebugLogMsg(CRMUtilCorbaLog.class.getName(), name_ + " " + msg, null).log(ctx_);
	}

	public void debug(String msg, Throwable t)
	{
          new DebugLogMsg(CRMUtilCorbaLog.class.getName(), name_ + " " + msg, t).log(ctx_);

        }

	public boolean isDebugEnabled()
	{
          return  LogSupport.isDebugEnabled(ctx_);
	}

	public void error(String msg)
	{ 
          new MajorLogMsg(CRMUtilCorbaLog.class.getName(), name_ + " " + msg, null).log(ctx_);
	}

	public void error(String msg, Throwable t)
	{          
	  new MajorLogMsg(CRMUtilCorbaLog.class.getName(), name_ + " " + msg, t).log(ctx_);
 
	}


	Context ctx_; 
	String name_; 
}
