package com.redknee.app.crm.web.control;

import java.io.PrintWriter;

import com.redknee.framework.xhome.webcontrol.TextFieldWebControl;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.web.agent.WebAgents;

public class PreviewTextFieldWebControl
   extends TextFieldWebControl
{
   public PreviewTextFieldWebControl()
   {
      super();
   }

   public PreviewTextFieldWebControl(int size)
   {
      super(size);
   }

   public PreviewTextFieldWebControl(int size, int maxLength)
   {
      super(size, maxLength);
   }

   protected void outputCustomAttributes(Context ctx, PrintWriter out)
   {
      out.print(" onChange=\"autoPreview('" + WebAgents.getDomain(ctx) + "', event)\"");
   }
}
