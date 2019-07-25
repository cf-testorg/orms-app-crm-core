package com.redknee.app.crm.web.control;

import java.util.Date;
import java.io.PrintWriter;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.webcontrol.ProxyWebControl;
import com.redknee.framework.xhome.webcontrol.WebControl;

public class HideMaxDateWebControl
   extends ProxyWebControl
{
   public static final Date maxDate_ = new Date(Long.MAX_VALUE);

   public HideMaxDateWebControl(WebControl delegate)
   {
      super(delegate);
   }

   public void toWeb(Context ctx, PrintWriter out, String name, Object obj)
   {
      if (obj instanceof Date)
      {
         Date date = (Date)obj;
         // do not display really really large dates
         if (maxDate_.equals(date))
         {
            return;
         }
      }
      super.toWeb(ctx, out, name, obj);
   }
}
