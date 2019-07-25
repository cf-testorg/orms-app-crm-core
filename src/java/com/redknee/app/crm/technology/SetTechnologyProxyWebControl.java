/*
 *  SetTechnologyProxyWebControl.java
 *
 *  Author : rattapattu
 *  Date   : Oct 12, 2005
 *
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
package com.redknee.app.crm.technology;

import java.io.PrintWriter;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.webcontrol.ProxyWebControl;
import com.redknee.framework.xhome.webcontrol.WebControl;

/**
 * Calls the Technology.setTechnology() method with the technology of the current bean if is TechnologyAware.
 *
 * This based on frameworks MSP support
 * 
 * @author  rattapattu
 **/
public class SetTechnologyProxyWebControl
   extends ProxyWebControl
{

   public SetTechnologyProxyWebControl(WebControl delegate)
   {
      super(delegate);
   }

   
   public void toWeb(Context ctx, PrintWriter out, String name, Object obj)
   {
      if ( obj instanceof TechnologyAware )
      {
         TechnologyEnum techEnum = ((TechnologyAware) obj).getTechnology();
         
         if ( techEnum != null )
         {
            Technology.setBeanTechnology(ctx, techEnum);
         }
      }
      
      getDelegate(ctx).toWeb(wrapContext(ctx), out, name, obj);
   }

}


