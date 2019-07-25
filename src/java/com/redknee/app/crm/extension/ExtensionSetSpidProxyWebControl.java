package com.redknee.app.crm.extension;

import java.io.PrintWriter;
import java.util.Iterator;

import com.redknee.app.crm.support.ExtensionSupportHelper;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.msp.MSP;
import com.redknee.framework.xhome.msp.Spid;
import com.redknee.framework.xhome.msp.SpidAware;
import com.redknee.framework.xhome.msp.SpidHome;
import com.redknee.framework.xhome.webcontrol.ProxyWebControl;
import com.redknee.framework.xhome.webcontrol.WebControl;


public class ExtensionSetSpidProxyWebControl
extends ProxyWebControl
{

   public ExtensionSetSpidProxyWebControl(WebControl delegate)
   {
      super(delegate);
   }

   
   public void toWeb(Context ctx, PrintWriter out, String name, Object obj)
   {
      Object bean = ExtensionSupportHelper.get(ctx).getParentBean(ctx);
      
      // Don't reset the spid if it has already been set
      if ( MSP.getBeanSpid(ctx) == null && bean instanceof SpidAware )
      {
         int spid = ((SpidAware) bean).getSpid();

         // If spid is default then try using the first available spid 
         // from the SpidHome
         if ( spid == 0 )
         {
            try
            {
               Home home = (Home) ctx.get(SpidHome.class);
               
               // Note that we use selectAll() instead of find()
               // eventhough we only want one to ensure that we
               // get the "first" one.
               Iterator i = home.selectAll().iterator();
               
               if ( i.hasNext() )
               {
                  spid = ((Spid) i.next()).getId();  
               }
            }
            catch (HomeException e)
            {
            }
         }
         
         if ( spid != 0 )
         {
            ctx = ctx.createSubContext();
            MSP.setBeanSpid(ctx, spid);
         }
         /* else (spid == 0) then it should be an error because no spid's are availabe. */
      }
      
      getDelegate(ctx).toWeb(wrapContext(ctx), out, name, obj);
   }


}
