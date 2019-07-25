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
package com.redknee.app.crm.xhome.home;

import java.util.Stack;


import com.redknee.framework.xhome.beans.Identifiable;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.AuditJournalHome;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.HomeInternalException;
import com.redknee.framework.xhome.home.HomeOperationEnum;
import com.redknee.framework.xhome.home.HomeProxy;
import com.redknee.framework.xhome.home.NullHome;
import com.redknee.framework.xhome.journal.AuditFileJournal;
import com.redknee.framework.xhome.journal.FileJournal;
import com.redknee.framework.xhome.journal.Journal;
import com.redknee.framework.xhome.journal.JournalConfig;
import com.redknee.framework.xhome.journal.JournalException;
import com.redknee.framework.xhome.journal.RollingFileJournal;
import com.redknee.framework.xhome.journal.SynchronizedJournal;
import com.redknee.framework.xhome.journal.ZipJournal;
import com.redknee.framework.xhome.journal.event.CommentEvent;
import com.redknee.framework.xhome.journal.event.JournalEvent;
import com.redknee.framework.xhome.journal.event.NullJournalEvent;
import com.redknee.framework.xhome.journal.event.TimestampJournalEvent;
import com.redknee.framework.xlog.log.CritLogMsg;
import com.redknee.framework.xlog.log.MinorLogMsg;

/**
 * This home records all failed operations to a special journal for auditing purposes.  The journal can
 * be configured using the key "FailedOperations" in:
 * 
 *  Core -> Runtime -> Journal -> Files
 *  Core -> Runtime -> Journal -> Rolling
 *
 * Exception information will be output to the log along with the bean that resulted in the failure.
 *
 * @author aaron.gourley@redknee.com
 * @since 8.8/9.0
 */
public class FailureAuditingJournalHome extends HomeProxy
{
    protected static final String FAILED_OPERATIONS_AUDIT_JOURNAL = "FailedOperationsAuditJournal";
    
    public static final String FAILED_OPERATIONS_AUDIT_JOURNAL_CONFIG = "FailedOperationsAuditJournalConfig";
    //Context ctx_;
    protected JournalConfig cfg_;

    public FailureAuditingJournalHome(Context ctx, Home delegate)
    {
        this(ctx, null, delegate);
    }
    
    public FailureAuditingJournalHome(Context ctx, Object retryHomeKey, Home delegate)
    {
        super(ctx, delegate);
        Context ctx_ = ctx.createSubContext();
        super.setContext(ctx_);
         cfg_= getJournalFileConfig(ctx);
        if(cfg_==null)
        {
        	new CritLogMsg(this,"Can't find JournalConfig under key:"+FAILED_OPERATIONS_AUDIT_JOURNAL_CONFIG,null).log(ctx);
        	//throw (new Exception("Can't find JournalConfig under key:"+FAILED_OPERATIONS_AUDIT_JOURNAL_CONFIG));
        }
        else
        {
        	
        	ctx_.put(JournalConfig.class, cfg_);
        }
        failureHome_ = new AuditJournalHome(ctx_, retryHomeKey, NullHome.instance(), FAILED_OPERATIONS_AUDIT_JOURNAL);

    }

    public static JournalConfig getJournalFileConfig(Context ctx)
    {
    	return (JournalConfig) ctx.get(FAILED_OPERATIONS_AUDIT_JOURNAL_CONFIG);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object create(Context ctx, Object obj) throws HomeException, HomeInternalException
    {
        try
        {
            return super.create(ctx, obj);
        }
        catch (HomeException e)
        {
        	addFailureToJournal(ctx, HomeOperationEnum.CREATE, obj, e);
            throw e;
        }
        catch (RuntimeException e)
        {
        	addFailureToJournal(ctx, HomeOperationEnum.CREATE, obj, e);
            throw e;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object store(Context ctx, Object obj) throws HomeException, HomeInternalException
    {
        try
        {
            return super.store(ctx, obj);
        }
        catch (HomeException e)
        {
        	addFailureToJournal(ctx, HomeOperationEnum.STORE, obj, e);
            throw e;
        }
        catch (RuntimeException e)
        {
        	addFailureToJournal(ctx, HomeOperationEnum.STORE, obj, e);
            throw e;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(Context ctx, Object obj) throws HomeException, HomeInternalException
    {
        try
        {
            super.remove(ctx, obj);
        }
        catch (HomeException e)
        {
        	addFailureToJournal(ctx, HomeOperationEnum.REMOVE, obj, e);
            throw e;
        }
        catch (RuntimeException e)
        {
        	addFailureToJournal(ctx, HomeOperationEnum.REMOVE, obj, e);
            throw e;
        }
    }

    public void addFailureToJournal(Context ctx, HomeOperationEnum op, Object obj, Exception e) throws HomeException,
            HomeInternalException
    {
        JournalEvent logEvent = null;

        boolean auditEnabled = false;

        Journal journal = (Journal) ctx.get(failureHome_.getAuditJournalKey());
        if (journal!=null)
        {
        	auditEnabled = cfg_.getAuditEnabled();
//          JournalConfig config = (JournalConfig) ctx.get(JournalConfig.class);
//          auditEnabled = (config != null && config.getAuditEnabled());

            /*
            try
            {
                AuditEnabledCmd cmd = new AuditEnabledCmd();
                cmd.setParameter(obj);
           //     Object ret = journal.cmd(ctx, cmd);
                if(ret instanceof Boolean)
                {
                    auditEnabled  = ((Boolean)ret).booleanValue();
                }
            }
            catch (JournalException je)
            {
            }
*/
        }

        if(auditEnabled)
        {
            Stack<JournalEvent> eventStack = new Stack<JournalEvent>();

            eventStack.push(new CommentEvent("OPERATION", op.getDescription(ctx), NullJournalEvent.instance()));
            eventStack.push(new CommentEvent("CLASS", obj.getClass().getName(), NullJournalEvent.instance()));
            if (obj instanceof Identifiable)
            {
                eventStack.push(new CommentEvent("   ID", String.valueOf(((Identifiable) obj).ID()), NullJournalEvent.instance()));
            }

            eventStack.push(new CommentEvent("ERROR", e.getClass().getName() + ": " + e.getMessage(), NullJournalEvent.instance()));
            StackTraceElement[] stackTrace = e.getStackTrace();
            if (stackTrace != null && stackTrace.length > 0)
            {
                for (StackTraceElement stack : stackTrace)
                {
                    eventStack.push(new CommentEvent("FRAME", "\t" + stack, NullJournalEvent.instance()));
                }
            }
            Throwable cause = e;
            for (cause = cause.getCause(); cause != null; cause = cause.getCause())
            {
                eventStack.push(new CommentEvent("CAUSE", cause.getClass().getName() + ": " + cause.getMessage(), NullJournalEvent.instance()));
                stackTrace = cause.getStackTrace();
                if (stackTrace != null && stackTrace.length > 0)
                {
                    for (StackTraceElement stack : stackTrace)
                    {
                        eventStack.push(new CommentEvent("FRAME", "\t" + stack, NullJournalEvent.instance()));
                    }
                }
            }
            eventStack.push(new CommentEvent("REQUEST", "See following line...", NullJournalEvent.instance()));

            JournalEvent prev = eventStack.pop();
            while(!eventStack.isEmpty())
            {
                JournalEvent current = eventStack.pop();
                if (current instanceof CommentEvent)
                {
                    current.setEvent(prev);
                }
                prev = current;
            }

            logEvent = new TimestampJournalEvent(new FileJournal().new FileJournalAudit(), prev);

            synchronized (FAILED_OPERATIONS_AUDIT_JOURNAL)
            {
                if (journal != null
                        && logEvent != null)
                {
                    try
                    {
                        journal.add(ctx, logEvent);
                    }
                    catch (JournalException je)
                    {
                        new MinorLogMsg(this, "Error outputting error log to audit journal: " + e.getMessage(), je).log(ctx);
                    }
                }
                failureHome_.create(ctx, obj);
            }
        }
    }
    
    protected final AuditJournalHome failureHome_;

    public static void initJournal(Context ctx)
    {
        if (!ctx.has(FailureAuditingJournalHome.FAILED_OPERATIONS_AUDIT_JOURNAL))
        {
        	JournalConfig cfg = getJournalFileConfig(ctx);
        	if(cfg==null)
        	{
            	new CritLogMsg(FailureAuditingJournalHome.class,"Can't find JournalConfig under key:"+FAILED_OPERATIONS_AUDIT_JOURNAL_CONFIG,null).log(ctx);
        		return;
        	}
        	  // TODO: Look at how to have different FileJournal configurations.
            Journal journal = new ZipJournal(ctx, 
                    new RollingFileJournal( 
                    		new AuditFileJournal(cfg.getFileName())));
//                          AuditFileJournal.create(ctx, "FailedOperations", false, true)));

            try
            {
            	 journal.cmd(ctx, Journal.OPEN);
            }
            catch (JournalException e)
            {
                // can't really happen, it's just openning a file
                // it doesn't do the loading part.
                new MinorLogMsg(FailureAuditingJournalHome.class, e.getMessage(), e).log(ctx);
            }
            ctx.put(FAILED_OPERATIONS_AUDIT_JOURNAL, new SynchronizedJournal(journal));
        }
    }
}