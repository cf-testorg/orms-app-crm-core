package com.redknee.app.crm.web.control;

import java.io.PrintWriter;

import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collection;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.webcontrol.*;
import com.redknee.framework.xhome.home.*;

import com.redknee.framework.xlog.log.*;

import com.redknee.app.crm.bean.*;


/**
 * @author lzou
 * @date   Dec 15, 2003
 *
 * Used in Subscriber/Account Note screen.  
 */
public class NoteSubTypeWebControl implements KeyWebControl
{
    public static int    size_          = 1;
    
    protected final static WebControl webControl_  = new TextFieldWebControl(20);    

    /**
     * 
     */
    public NoteSubTypeWebControl()
    {
        super();
    }
    /* (non-Javadoc)
     * @see com.redknee.framework.xhome.webcontrol.InputWebControl#fromWeb(com.redknee.framework.xhome.context.Context, javax.servlet.ServletRequest, java.lang.String)
     */
    public Object fromWeb(Context ctx, ServletRequest req, String name)
        throws NullPointerException
    {
         return webControl_.fromWeb(ctx, req, name);
    }
    /* (non-Javadoc)
     * @see com.redknee.framework.xhome.webcontrol.InputWebControl#fromWeb(com.redknee.framework.xhome.context.Context, java.lang.Object, javax.servlet.ServletRequest, java.lang.String)
     */
    public void fromWeb(
        Context ctx,
        Object obj,
        ServletRequest req,
        String name)
    {
        webControl_.fromWeb(ctx, obj, req, name);
    }
    /* (non-Javadoc)
     * @see com.redknee.framework.xhome.webcontrol.OutputWebControl#toWeb(com.redknee.framework.xhome.context.Context, java.io.PrintWriter, java.lang.String, java.lang.Object)
     */
    public void toWeb(
        Context ctx,
        PrintWriter out,
        String name,
        Object obj)
    {
        int mode = ctx.getInt("MODE", DISPLAY_MODE);       
        
        Collection reasons = new ArrayList();
        NoteType noteType  = null;
        String type        = null;
        String reason      = "";
        NoteTyped   note   = (NoteTyped)ctx.get(AbstractWebControl.BEAN);
        Home noteTypeHome  = (Home) ctx.get(NoteTypeHome.class);
        
        if ( obj != null )  
        {         
            reason  = obj.toString();
        }
                
        type = note.getNoteType();
        
        if ( type == null || type.equals("") || type.length()== 0)
        {
              type =  "Suspended"; // in Note.xml, this has been set to be default type
        }
                
        if ( type != null ) 
        {   
            try
            {
                  noteType = (NoteType)noteTypeHome.find(ctx,type);                  
            }
            catch(Exception e)
            {
                  new MinorLogMsg(this, "Can't find NoteType " + type + " in NoteTypeHome", e).log(ctx);
            }
        }
        
        if ( noteType != null )
        {
            //    reasons for account == subtypes for subscriber
            reasons = noteType.getReasons();
        }
        
        switch (mode)
        {
             case EDIT_MODE:
             case CREATE_MODE:
                out.println("<select name=\"" + name + "\" size=\"" + size_ + "\">");
            
                for ( Iterator i = reasons.iterator() ; i.hasNext() ; )
                {
                   Reason r = (Reason)i.next();

                   out.print("<option value=\""+ r.getReasoncode()+"\"");
                   if ( r.getReasoncode().equals(reason) )
                   {
                      out.print(" selected=\"selected\"");
                   }
                  
                   out.println(">" + r.getDesc() + "</option>");
                }
                out.println("</select>");
             break;

             case DISPLAY_MODE:
             default:  // no subtype, obj == null
                out.print(( obj== null) ? "":obj .toString());
        }       
    }
}
