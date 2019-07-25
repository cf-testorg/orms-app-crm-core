package com.redknee.app.crm.web.control;

import java.io.PrintWriter;

import com.redknee.framework.xhome.webcontrol.LongWebControl;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.web.agent.WebAgents;

public class PreviewLongWebControl
   extends LongWebControl
{
   public PreviewLongWebControl()
   {
      this(20);
   }

   public PreviewLongWebControl(int width)
   {
      super(width);
   }

   public PreviewLongWebControl(int width, int base)
   {
      super(width, base);
   }

   public PreviewLongWebControl(int width, int base, int maxLength)
   {
      super(width, base, maxLength);
   }

   protected void outputCustomAttributes(Context ctx, PrintWriter out)
   {
      out.print(" onBlur=\"autoPreview('" + WebAgents.getDomain(ctx) + "', event)\"");
   }
}
