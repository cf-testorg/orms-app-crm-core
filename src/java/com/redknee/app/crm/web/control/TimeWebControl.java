package com.redknee.app.crm.web.control;

/**
 *  Title: TimeWebControl Description: In Edit mode displays as a text field. In
 *  Display mode displays a formated time Copyright: Copyright (c) 2002 Company:
 *  Redknee
 *
 *@author     Lily Zou
 *@version    1.0
 */

import java.io.PrintWriter;
import java.text.DecimalFormat;

import javax.servlet.ServletRequest;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.util.time.Time;
import com.redknee.framework.xhome.webcontrol.PrimitiveWebControl;
import com.redknee.framework.xhome.webcontrol.WebControl;


public class TimeWebControl
   extends PrimitiveWebControl
{

   private static  WebControl  instance__      = new TimeWebControl();
   private static  int         DEFAULT_LENGTH  = 20;

   final static  DecimalFormat  HOUR_FORMAT    = new DecimalFormat("##");
   final static  DecimalFormat  MINUTE_FORMAT  = new DecimalFormat("00");

   private TimeWebControl() { }


   public static WebControl instance()
   {
      return instance__;
   }

   
   public void toWeb(Context ctx, PrintWriter out, String name, Object obj)
   {
      int   mode  = ctx.getInt("MODE", DISPLAY_MODE);

      Time  time  = (Time) obj;

      switch (mode)
      {
         case CREATE_MODE:
         case EDIT_MODE:
            out.print("<INPUT TYPE=\"text\" NAME=\"" + name + "\" SIZE=\"" + DEFAULT_LENGTH + "\" VALUE=\"");

            if (obj != null)
            {
               out.print(time.toString());
            }
            out.print("\">");

            break;
         case DISPLAY_MODE:
         default:
            if (obj != null)
            {
               out.print(toString(time));
            }
            else
            {
               out.print("&nbsp;");
            }
      }
   }

   public String toString(Time time)
   {
      StringBuilder buf = new StringBuilder();
      
      append(time, buf);
      
      return buf.toString();
   }
   
   
   public void append(Time time, StringBuilder buf)
   {
      int s = time.getSeconds();
      synchronized (HOUR_FORMAT ){
      	buf.append(HOUR_FORMAT.format(time.getHours()));
      }
      buf.append(":");
      synchronized (MINUTE_FORMAT ){
      	buf.append(MINUTE_FORMAT.format(time.getMinutes()));   
      	buf.append(":");
       	buf.append(MINUTE_FORMAT.format(s));
      }
    }  
   

   public Object fromWeb(Context ctx, ServletRequest req, String name)
      throws IllegalArgumentException
   {
      if (req.getParameter(name) == null)
      {
         return null;
      }

      return new Time(req.getParameter(name));
   }

}

