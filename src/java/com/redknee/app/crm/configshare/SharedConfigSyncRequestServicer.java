/*
 * This code is a protected work and subject to domestic and international
 * copyright law(s).  A complete listing of authors of this work is readily
 * available.  Additionally, source code is, by its very nature, confidential
 * information and inextricably contains trade secrets and other information
 * proprietary, valuable and sensitive to Redknee.  No unauthorized use,
 * disclosure, manipulation or otherwise is permitted, and may only be used
 * in accordance with the terms of the license agreement entered into with
 * Redknee Inc. and/or its subsidiaries.
 *
 * Copyright (c) Redknee Inc. and its subsidiaries. All Rights Reserved.
 */
package com.redknee.app.crm.configshare;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.redknee.framework.xhome.beans.ExceptionListener;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.language.MessageMgr;
import com.redknee.framework.xhome.web.renderer.ButtonRenderer;
import com.redknee.framework.xhome.web.renderer.DefaultButtonRenderer;
import com.redknee.framework.xhome.web.renderer.DefaultFormRenderer;
import com.redknee.framework.xhome.web.renderer.FormRenderer;
import com.redknee.framework.xhome.webcontrol.BeanWebController;
import com.redknee.framework.xhome.webcontrol.HTMLExceptionListener;
import com.redknee.framework.xhome.webcontrol.OutputWebControl;
import com.redknee.framework.xhome.webcontrol.RequestServicer;
import com.redknee.framework.xhome.webcontrol.WebControl;
import com.redknee.framework.xlog.log.MajorLogMsg;
import com.redknee.framework.xlog.log.MinorLogMsg;


/**
 * 
 *
 * @author aaron.gourley@redknee.com
 * @since 8.4
 */
public class SharedConfigSyncRequestServicer implements RequestServicer
{
    public static final String MSG_KEY_SYNC_BUTTON_NAME                          = "SharedConfigSyncForm.SyncButtonName";   
    public static final String MSG_KEY_SYNC_REQUEST_SUCCESS                      = "SharedConfigSyncForm.success";
    public static final String MSG_KEY_SYNC_REQUEST_FAILURE                      = "SharedConfigSyncForm.failure";
    public static final String MSG_KEY_BUTTON_NAME_CTX_KEY                       = "Sync_Key_Button_Name_Ctx_Key";

    public static final String MSG_KEY_INCOMPLETE                                = "SyncRequest.incomplete";
    
    /**
     * {@inheritDoc}
     */
    public void service(Context ctx, HttpServletRequest req, HttpServletResponse res) throws ServletException,
            IOException
    {
        final PrintWriter out = res.getWriter();

        HttpSession session = req.getSession();
        
        final Context sCtx = ctx.createSubContext();
        
        final ButtonRenderer buttonRenderer =
                (ButtonRenderer) sCtx.get(
                        ButtonRenderer.class,
                        DefaultButtonRenderer.instance());

        MessageMgr manager = new MessageMgr(sCtx, this);

        HTMLExceptionListener exceptions = (HTMLExceptionListener) ctx.get(HTMLExceptionListener.class);
        if (exceptions == null)
        {
            exceptions = new HTMLExceptionListener(manager);
            sCtx.put(HTMLExceptionListener.class, exceptions);
        }
        sCtx.put(ExceptionListener.class, exceptions);

        SharedConfigSyncForm request = new SharedConfigSyncForm();
        final WebControl wc = new SharedConfigSyncFormWebControl();
        wc.fromWeb(sCtx, request, req, "");

        String msg = "";
        String syncButtonText = manager.get(MSG_KEY_SYNC_BUTTON_NAME, "Execute");
        if (!exceptions.hasErrors())
        {
            try
            {   
                if (!sessionSet.add(session.getId()))
                {
                    String engMsg = "The previous synchronization request isn't complete yet, please try again later.";
                    new MinorLogMsg(this, engMsg, null).log(sCtx);
                    throw new HomeException(manager.get(MSG_KEY_INCOMPLETE, engMsg));
                }
                
                if (buttonRenderer.isButton(sCtx, syncButtonText))
                {
                    SharedConfigSyncProcessor processor = new SharedConfigSyncProcessor();
                    processor.process(sCtx, request);
                    
                    msg = manager.get(MSG_KEY_SYNC_REQUEST_SUCCESS, 
                            "Sync operation submitted successfully.",
                            new String[] {});
                }
            }
            catch (final SharedConfigSyncException exception)
            {
                msg = manager.get(MSG_KEY_SYNC_REQUEST_FAILURE, 
                        "The operation failed for request: {0}",
                        new String[] {request.toString()});
                new MajorLogMsg(this, "Problem occured submitting the sync operation.", exception).log(sCtx);
                exceptions.thrown(exception);
            }
            catch (final Throwable throwable)
            {
                new MajorLogMsg(this, "Unexpected problem occured while submitting the sync operation.", throwable).log(sCtx);
                exceptions.thrown(throwable);
            }
            finally
            {
                sessionSet.remove(session.getId());
            }
        }

        // Use create mode so that dynamic bean web controls allow edit on primary key (i.e. final) fields
        sCtx.put("MODE", OutputWebControl.CREATE_MODE);

        final FormRenderer formRenderer =
                (FormRenderer) sCtx.get(FormRenderer.class, DefaultFormRenderer.instance());

        formRenderer.Form(out, sCtx);

        out.println("<input type=\"hidden\" name=\"PreviewButtonSrc\" />");// value=\"none\" onclick=\"form.submit()\" 
        
        out.print("<table>");
        if (msg != null && msg.trim().length() > 0)
        {
            boolean showRedText = exceptions.hasErrors();
            
            out.println("<tr><td>&nbsp;</td></tr><tr><td align=\"center\"><b style=\"color:" + (showRedText ? "red" : "green") + ";\">");
            out.print(msg);
            out.println("</b></td></tr><tr><td>&nbsp;</td></tr>");
        }

        out.print("<tr><td>");

        if (exceptions.hasErrors())
        {
            exceptions.toWeb(sCtx, out, "", request);
            out.print("</td></tr><tr><td>&nbsp;</td></tr><tr><td>");
        }
        
        wc.toWeb(sCtx, out, "", request);

        out.print("</td></tr><tr><th align=\"right\">");

        buttonRenderer.inputButton(out, sCtx, this.getClass(), "Preview", false);
        
        buttonRenderer.inputButton(out, sCtx, this.getClass(), syncButtonText, false);
        
        BeanWebController.outputHelpLink(sCtx, req, res, out, buttonRenderer);

        out.print("</th></tr></table>");

        formRenderer.FormEnd(out);
    }

    static Set<String> sessionSet = Collections.synchronizedSet(new HashSet<String>());
}
