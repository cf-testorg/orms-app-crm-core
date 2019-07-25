package com.redknee.app.crm.web.control;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.webcontrol.*;
import java.io.PrintWriter;
import javax.servlet.ServletRequest;


public class NullHandlingTextAreaWebControl
   extends TextAreaWebControl

{
  //Default Constructor sets the number of cols and rows
  public NullHandlingTextAreaWebControl()
  {
      super();
  }
  
  
  public NullHandlingTextAreaWebControl(int cols, int rows)
  {
      super(cols, rows);
  }

  
  public NullHandlingTextAreaWebControl(int cols, int rows, boolean scrollMode)
  {
      super(cols, rows, scrollMode);
  }
  
  
  public void toWeb(Context ctx, PrintWriter out, String name, Object obj)
  {
      if ( obj == null )
      {
           obj = "";
      }

      super.toWeb(ctx, out, name, obj);
  }
}
