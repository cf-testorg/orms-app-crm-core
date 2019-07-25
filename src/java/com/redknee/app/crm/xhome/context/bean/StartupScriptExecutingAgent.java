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
package com.redknee.app.crm.xhome.context.bean;

import java.util.Collection;

import com.redknee.app.crm.bean.StartupScriptHolder;
import com.redknee.app.crm.bean.StartupScripts;
import com.redknee.app.crm.support.HomeSupportHelper;
import com.redknee.framework.core.bean.Script;
import com.redknee.framework.core.scripting.ScriptExecutor;
import com.redknee.framework.core.scripting.support.ScriptExecutorFactory;
import com.redknee.framework.xhome.beans.AbstractBean;
import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.beans.XDeepCloneable;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.context.ContextAgent;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xlog.log.DebugLogMsg;
import com.redknee.framework.xlog.log.InfoLogMsg;
import com.redknee.framework.xlog.log.LogSupport;
import com.redknee.framework.xlog.log.MajorLogMsg;



/**
 * A context agent that runs the pre/post startup scripts.
 *
 * @author aaron.gourley@redknee.com
 * @since 8.5
 */
public class StartupScriptExecutingAgent extends AbstractBean implements ContextAgent, XDeepCloneable
{
    /**
     * {@inheritDoc}
     */
    public void execute(Context ctx) throws AgentException
    {
        final String prePostLogPrefix = preStartup_ ? "pre-" : "post-";
        StartupScripts scriptWrapper = (StartupScripts) ctx.get(StartupScripts.class);
        if (scriptWrapper != null)
        {
            Collection<StartupScriptHolder> scripts = null;
            if (preStartup_)
            {
                scripts = scriptWrapper.getPreScripts();
            }
            else
            {
                scripts = scriptWrapper.getPostScripts();
            }

            for (StartupScriptHolder script : scripts)
            {
                try
                {
                    Script scriptBean = null;
                    try
                    {
                        new InfoLogMsg(this, "Loading " + prePostLogPrefix + "startup script '" + script.getScript() + "'...", null).log(ctx);
                        scriptBean = HomeSupportHelper.get(ctx).findBean(ctx, Script.class, script.getScript());
                        new InfoLogMsg(this, prePostLogPrefix + "startup script '" + script.getScript() + "' loaded successfully.", null).log(ctx);
                    }
                    catch (HomeException e)
                    {
                        new MajorLogMsg(this, "Error loading " + prePostLogPrefix + "startup script '" + script.getScript() + "': " + e.getMessage(), e).log(ctx);
                    }

                    if (scriptBean != null)
                    {
                        new InfoLogMsg(this, "Initializing " + scriptBean.getLang() + " engine for " + prePostLogPrefix + "startup script '" + script.getScript() + "'...", null).log(ctx);
                        ScriptExecutor scriptExecutor = ScriptExecutorFactory.create(scriptBean.getLang());
                        if (scriptExecutor != null)
                        {
                            new InfoLogMsg(this, scriptBean.getLang() + " engine for " + prePostLogPrefix + "startup script '" + script.getScript() + "' initialized successfully.", null).log(ctx);
                            String result = null;
                            try
                            {
                                new InfoLogMsg(this, "Executing " + prePostLogPrefix + "startup script '" + script.getScript() + "'...", null).log(ctx);
                                result = scriptExecutor.execute(ctx, scriptBean.getScript());
                                new InfoLogMsg(this, prePostLogPrefix + "startup script '" + script.getScript() + "' executed successfully.  See DEBUG logs for script output.", null).log(ctx);
                            }
                            catch (Exception e)
                            {
                                new MajorLogMsg(this, "Error executing " + prePostLogPrefix + "startup script: " + e.getMessage(), e).log(ctx);
                            }
                            if (LogSupport.isDebugEnabled(ctx) && result != null && result.trim().length() > 0)
                            {
                                new DebugLogMsg(this, scriptBean.getLang() + " script '" + scriptBean.getScript() + "' return result: " + result, null).log(ctx);
                            }
                        }
                        else
                        {
                            new MajorLogMsg(this, scriptBean.getLang() + " engine for " + prePostLogPrefix + "startup script '" + script.getScript() + "' could not be initialized!  " + scriptBean.getLang() + " not supported.", null).log(ctx);
                        }
                    }
                    else
                    {
                        new MajorLogMsg(this, (preStartup_ ? "Pre-" : "Post-") + "startup script '" + script.getScript() + "' not found.  Skipping execution...", null).log(ctx);
                    }
                }
                catch (Throwable t)
                {
                    // Catch all here.  We don't want any potentially buggy custom start-up scripts to screw up other startup scripts.
                    new MajorLogMsg(this, "Unexpected error with " + prePostLogPrefix + "startup script!", t).log(ctx);
                }
            }
        }
    }


    public void setPreStartup(boolean preStartup)
    {
        preStartup_ = preStartup;
    }
    
    protected boolean preStartup_ = false;

    @Override
    public boolean transientEquals(Object o)
    {
        return persistentEquals(o);
    }


    @Override
    public boolean persistentEquals(Object o)
    {
        return (o instanceof StartupScriptExecutingAgent) && 
                (preStartup_ == ((StartupScriptExecutingAgent) o).preStartup_);
    }
}
