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
package com.redknee.app.crm.support;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.Permission;
import java.security.Principal;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletResponse;

import com.redknee.app.crm.bean.SpidBillingMessage;
import com.redknee.app.crm.bean.SpidBillingMessageID;
import com.redknee.app.crm.billing.message.BillingMessageHomePipelineFactory;
import com.redknee.app.crm.support.messages.MessageConfigurationSupport;
import com.redknee.framework.auth.AuthMgr;
import com.redknee.framework.core.bean.Script;
import com.redknee.framework.core.scripting.ScriptExecutor;
import com.redknee.framework.core.scripting.support.ScriptExecutorFactory;
import com.redknee.framework.xhome.auth.SimplePermission;
import com.redknee.framework.xhome.auth.bean.User;
import com.redknee.framework.xhome.beans.AbstractBean;
import com.redknee.framework.xhome.beans.ExceptionListener;
import com.redknee.framework.xhome.beans.IllegalPropertyArgumentException;
import com.redknee.framework.xhome.beans.XBeans;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.context.ContextLocator;
import com.redknee.framework.xhome.language.Lang;
import com.redknee.framework.xhome.language.MessageMgr;
import com.redknee.framework.xhome.language.MessageMgrAware;
import com.redknee.framework.xhome.language.MessageMgrSPI;
import com.redknee.framework.xhome.language.MessageMgrSPIProxy;
import com.redknee.framework.xhome.web.renderer.DefaultTableRenderer;
import com.redknee.framework.xhome.web.renderer.TableRenderer;
import com.redknee.framework.xhome.webcontrol.HTMLExceptionListener;
import com.redknee.framework.xlog.log.DebugLogMsg;
import com.redknee.framework.xlog.log.InfoLogMsg;
import com.redknee.framework.xlog.log.LogSupport;
import com.redknee.framework.xlog.log.MajorLogMsg;
import com.redknee.util.snippet.log.Logger;

/**
 * @author victor.stratan@redknee.com
 */
public final class DefaultFrameworkSupport implements FrameworkSupport
{
    protected static FrameworkSupport instance_ = null;
    public static FrameworkSupport instance()
    {
        if (instance_ == null)
        {
            instance_ = new DefaultFrameworkSupport();
        }
        return instance_;
    }

    protected DefaultFrameworkSupport()
    {
    }

    /**
     * {@inheritDoc}
     */
    public void notifyExceptionListener(final Context ctx, final Throwable th)
    {
        ExceptionListener listener = (ExceptionListener) ctx.get(HTMLExceptionListener.class);
        ExceptionListener otherListener = (ExceptionListener) ctx.get(ExceptionListener.class);
        
        if (listener != null)
        {
            listener.thrown(th);
        }
        else if (otherListener != null && !otherListener.equals(listener))
        {
            otherListener.thrown(th);
        }
        else if (otherListener == null)
        {
            Logger.info(ctx, this, "Missing Exception Listener");
            Logger.minor(ctx, this, th.getMessage(), th);
        }
        
    }

    /**
     * {@inheritDoc}
     */
    public void initExceptionListener(final Context ctx, final Object caller)
    {
        ExceptionListener listener = (ExceptionListener) ctx.get(ExceptionListener.class);

        if (listener == null || !(listener instanceof HTMLExceptionListener))
        {
            listener = new HTMLExceptionListener(new MessageMgr(ctx, caller));
            ctx.put(ExceptionListener.class, listener);
        }

        ExceptionListener el = (ExceptionListener) ctx.get(HTMLExceptionListener.class);
        if (el == null)
        {
            ctx.put(HTMLExceptionListener.class, listener);
        }
    }


    /**
     * {@inheritDoc}
     */
    public void printCapturedExceptions(final Context ctx)
    {
        // print the warnings to the screen if the screen is available
        final HTMLExceptionListener el = (HTMLExceptionListener) ctx.get(HTMLExceptionListener.class);
        final PrintWriter out = getWriter(ctx);
        if (el != null && el.hasErrors() && out != null)
        {
            out.println("<center><font color=\"red\"><b>Error during update operation:</b></font></center>");
            el.toWeb(ctx, out, null, null);
        }
    }

    public void printCapturedExceptionsAsWarnings(final Context ctx, final Object caller)
    {
        // print the warnings to the screen if the screen is available
        final HTMLExceptionListener el = (HTMLExceptionListener) ctx.get(HTMLExceptionListener.class);
        final PrintWriter out = getWriter(ctx);
        if (el != null && el.hasErrors() && out != null)
        {
            Set<String> propertiesToLinkMap = new TreeSet<String>();
            MessageMgr mmgr = new MessageMgr(ctx, caller);
            TableRenderer tr = (TableRenderer) ctx.get(TableRenderer.class, DefaultTableRenderer.instance());
            
            out.println("<table><tr><td>");
            tr.Table(ctx, out, "Warning(s)");
            
            int j = 0;
            for ( Iterator<Throwable> i = el.getExceptions().iterator() ; i.hasNext() ; j++ )
            {
                Throwable t = i.next();
                tr.TR(ctx, out, null, j);
                tr.TD(ctx, out);
                if (t instanceof IllegalPropertyArgumentException)
                {
                    final IllegalPropertyArgumentException ipe = (IllegalPropertyArgumentException) t;
                    out.print("<a href=\"#");
                    out.print(ipe.getPropertyName());
                    out.print("\">");
                    out.print(mmgr.get(ipe.getPropertyName() + ".Label", ipe.getPropertyLabel()));
                    out.print("</a>");
                    if (!propertiesToLinkMap.contains(ipe.getPropertyName()))
                    {
                        propertiesToLinkMap.add(ipe.getPropertyName());
                    }
                    tr.TDEnd(ctx, out);
                    tr.TD(ctx, out);
                    MessageMgrAware mmgrAware = (MessageMgrAware) XBeans.getInstanceOf(ctx, ipe, MessageMgrAware.class);
                    if (mmgrAware != null)
                    {
                        out.print(mmgrAware.toString(ctx, mmgr));
                    }
                    else
                    {
                        out.print(ipe.getMessageText());
                    }
                }
                else
                {
                    out.println("&nbsp;");
                    tr.TDEnd(ctx, out);
                    tr.TD(ctx, out);
                    out.print(t.getMessage());
                }
                tr.TDEnd(ctx, out);
                tr.TREnd(ctx, out);
                i.remove();
            }
            
            tr.TableEnd(ctx, out);
            out.println("</td></tr></table>");

            for (Iterator<String> i = propertiesToLinkMap.iterator(); i.hasNext(); j++)
            {
                final String property = i.next();
                ((Context) ctx.get("..")).put(MessageMgrSPI.class, new MessageMgrSPIProxy((MessageMgrSPI) ctx
                        .get(MessageMgrSPI.class))
                {

                    @Override
                    public String get(Context ctx, String key, Class module, Lang lang, String defaultValue,
                            Object[] args)
                    {
                        return (property + ".Label").equals(key)
                                ? "<a name=\"" + property + "\"/><font color=\"red\">"
                                        + getDelegate().get(ctx, key, module, lang, defaultValue, args) + "</font>"
                                : getDelegate().get(ctx, key, module, lang, defaultValue, args);
                    }
                });
                mmgr.setSPI((MessageMgrSPI) ctx.get(MessageMgrSPI.class));
            }

        }
    }


    /**
     * {@inheritDoc}
     */
    public PrintWriter getWriter(final Context ctx)
    {
        final HttpServletResponse res = (HttpServletResponse) ctx.get(HttpServletResponse.class);
        PrintWriter out = null;
        if (res != null)
        {
            try
            {
                out = res.getWriter();
            }
            catch (final IOException ioEx)
            {
                // do nothing
            }
        }

        return out;
    }

    /**
     * {@inheritDoc}
     */
    public TableRenderer getTableRenderer(final Context ctx)
    {
        final TableRenderer renderer = (TableRenderer) ctx.get(TableRenderer.class, DefaultTableRenderer.instance());

        return renderer;
    }

    /**
     * {@inheritDoc}
     */
    public MessageMgr getMessageMgr(final Context ctx, final Object caller)
    {
        final MessageMgr manager = new MessageMgr(ctx, caller);

        return manager;
    }

    /**
     * {@inheritDoc}
     */
    public User getUserOrNull(final Context ctx)
    {
        User user = (User) ctx.get(Principal.class);
        return user;
    }

    /**
     * {@inheritDoc}
     */
    public String getCurrentUserID(final Context ctx)
    {
        User user = getUserOrNull(ctx);
        return (user == null) ? "" : user.getId();
    }

    /**
     * {@inheritDoc}
     */
    public <T extends AbstractBean> T doClone(final T bean)
    {
        try
        {
            return (T) bean.clone();
        }
        catch (CloneNotSupportedException e)
        {
            // this should never happen
            final String msg = "CRITICAL ERROR: AbstractBean is expected to override clone!";
            Logger.crit(ContextLocator.locate(), this, msg, e);
            throw new UnsupportedOperationException(msg, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public <T extends AbstractBean> T instantiateBean(final Context ctx, final Class<T> beanClass)
    {
        T bean = null;
        try
        {
            bean = (T) XBeans.instantiate(beanClass, ctx);
        }
        catch (final Exception e)
        {
            Logger.minor(ctx, this, "Unable to Framework instantiate " + beanClass.getName(), e);
        }

        return bean;
    }

    public Object getObjectFromScript(Context ctx, Script script)
    {
        return getObjectFromScript(ctx, script, null);
    }

    public Object getObjectFromScript(Context ctx, Script script, StringBuilder scriptOutput)
    {
        return getObjectFromScript(ctx, Object.class, script, scriptOutput);
    }
    
    public <T> T getObjectFromScript(Context ctx, Class<T> objectType, Script script)
    {
        return getObjectFromScript(ctx, objectType, script, null);
    }
    
    public <T> T getObjectFromScript(Context ctx, Class<T> objectType, Script script, StringBuilder scriptOutput)
    {
        T result = null;
        
        ctx = ctx.createSubContext();
        
        String msg = "Invalid script configuration.";
        
        if (script != null)
        {
            if (LogSupport.isDebugEnabled(ctx))
            {
                new DebugLogMsg(this, "Initializing " + script.getLang() + " engine for script '" + script.getScript() + "'...", null).log(ctx);
            }
            ScriptExecutor scriptExecutor = ScriptExecutorFactory.create(script.getLang());
            if (scriptExecutor instanceof ScriptExecutor)
            {
                ScriptExecutor shellExecutor = (ScriptExecutor) scriptExecutor;

                if (LogSupport.isDebugEnabled(ctx))
                {
                    new DebugLogMsg(this, script.getLang() + " engine for  script '" + script.getScript() + "' initialized successfully.", null).log(ctx);
                }
                
                try
                {
                    if (LogSupport.isDebugEnabled(ctx))
                    {
                        new DebugLogMsg(this, "Executing  script '" + script.getScript() + "'...", null).log(ctx);
                    }
                    msg = shellExecutor.execute(ctx, script.getScript());
                    new InfoLogMsg(this, "Script '" + script.getScript() + "' executed successfully.  See DEBUG logs for script output.", null).log(ctx);

                    if (LogSupport.isDebugEnabled(ctx))
                    {
                        new DebugLogMsg(this, script.getLang() + " script '" + script.getScript() + "' output: " + msg, null).log(ctx);
                    }
                    
                    Object resultObject = shellExecutor.retrieveObject(ctx, script.getName());
                    if (objectType.isInstance(resultObject))
                    {
                        result = (T) resultObject;
                    }
                    else
                    {
                        String resultObjectType = (resultObject == null ? "null" : resultObject.getClass().getName());
                        msg = "Script returned invalid result.  " + objectType.getName() + " implementation expected, but got: " + resultObjectType;
                        new MajorLogMsg(this, msg, null).log(ctx);
                    }
                }
                catch (Exception e)
                {
                    msg = "Error executing script: " + e.getMessage();
                    new MajorLogMsg(this, msg, e).log(ctx);
                }
            }
            else
            {
                new MajorLogMsg(this, script.getLang() + " engine for script '" + script.getScript() + "' could not be initialized!  " + script.getLang() + " not supported.", null).log(ctx);
            }
        }
        
        if (scriptOutput != null)
        {
            scriptOutput.append(msg);
        }
        
        return result;
    }
    
    /**
     * {@inheritDoc}
     */
    public Object getFWSupportedContextNull()
    {
        return null;
    }
    
    /**
     * {@inheritDoc}
     */
    public Context getEmptyContext()
    {
    	return new com.redknee.framework.xhome.context.ContextSupport();
    }
    
    /**
     * {@inheritDoc}
     */
    public com.redknee.framework.xhome.filter.Predicate getTrueOrFalseFWPredicate(boolean value)
    {
        if (value)
        {
        	return com.redknee.framework.xhome.elang.True.instance();
        }
        else
        {
        	return com.redknee.framework.xhome.elang.False.instance();
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public MessageConfigurationSupport getConfigurationSupport(Context ctx)
    {
    	 MessageConfigurationSupport<SpidBillingMessage, SpidBillingMessageID> support = (MessageConfigurationSupport<SpidBillingMessage, SpidBillingMessageID>) ctx
                 .get(BillingMessageHomePipelineFactory.getBillingMessageConfigurationKey(SpidBillingMessage.class));
    	 
    	 return support;
    }


    public boolean hasPermission(Context ctx, String permission)
    {
        return hasPermission(ctx, new SimplePermission(permission));
    }
    
    public boolean hasPermission(Context ctx, Permission permission)
    {
        if ( permission == null)
        {
            return false; 
        }
    
        final AuthMgr auth =
            (AuthMgr)ctx.get(AuthMgr.class);
        
        final Principal principal = (Principal)ctx.get(Principal.class);
  

        return (auth != null
            && principal != null
            && auth.check(principal, permission)); 
    }

}
