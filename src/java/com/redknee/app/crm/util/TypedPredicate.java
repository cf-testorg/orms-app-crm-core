package com.redknee.app.crm.util;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.visitor.AbortVisitException;


public interface TypedPredicate<BEAN>
{

   /**
    * @exception AbortVisitException called to abort a Visit.
    **/
   public boolean f(Context ctx, BEAN obj)
      throws AbortVisitException;
}

