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

import java.io.PrintWriter;
import java.security.Permission;
import java.security.Principal;

import com.redknee.app.crm.support.messages.MessageConfigurationSupport;
import com.redknee.framework.auth.AuthMgr;
import com.redknee.framework.core.bean.Script;
import com.redknee.framework.xhome.auth.SimplePermission;
import com.redknee.framework.xhome.auth.bean.User;
import com.redknee.framework.xhome.beans.AbstractBean;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.language.MessageMgr;
import com.redknee.framework.xhome.web.renderer.TableRenderer;

/**
 * Support methods for Framework features. The idea is to not copy paste the same code 200 times.
 *
 * @author victor.stratan@redknee.com
 */
public interface FrameworkSupport extends Support
{
    public void notifyExceptionListener(final Context ctx, final Throwable th);

    public void initExceptionListener(final Context ctx, final Object caller);


    /**
     * Prints captured exceptions on the web page.
     *
     * @param ctx The operating context.
     */
    public void printCapturedExceptions(final Context ctx);

    /**
     * Prints captured exceptions on the web page.
     *
     * @param ctx The operating context.
     */
    public void printCapturedExceptionsAsWarnings(final Context ctx, final Object caller);

    /**
     * Returns the print writer of this context.
     *
     * @param ctx The operating context.
     * @return The print writer of this context.
     */
    public PrintWriter getWriter(final Context ctx);

    /**
     * Returns the Table Renderer of this context.
     *
     * @param ctx The operating context.
     * @return The table renderer of this context.
     */
    public TableRenderer getTableRenderer(final Context ctx);

    /**
     * Returns the Message Manager of this context.
     *
     * @param ctx The operating context.
     * @return The message manager of this context.
     */
    public MessageMgr getMessageMgr(final Context ctx, final Object caller);

    /**
     * Returns the current GUI user or null.
     *
     * @param ctx the operating context
     * @return the user object or null
     */
    public User getUserOrNull(final Context ctx);

    /**
     * Returns the ID of the current GUI user or empty string if user is not available.
     *
     * @param ctx the operating context
     * @return the user ID or empty string
     */
    public String getCurrentUserID(final Context ctx);

    /**
     * This method will help avoid catching the CloneNotSupportedException everywhere you clone beans which is
     * useless when the bean extends AbstractBean.
     *
     * @param bean the bean to clone
     * @return the cloned bean
     */
    
    /**
     * To support FW 5_7 and FW 6 compatiblity
     * @return This method will used so that for FW 5_7, it will return Context.NULL
     * For FW 6.0, it will return null
     */
    public Object getFWSupportedContextNull();
    
    /**
     * To support FW 5_7 and FW 6 compatiblity
     * This method will used so that for FW 5_7, it will return EmptyContext.instanc()
     * For FW 6.0, it will return new ContextSupport()
     */
    public Context getEmptyContext();
    
    /**
     * To support FW 5_7 and FW 6 compatiblity
     * @param value => which boolean predicate should be return
     * @return
     * This method will used so that for FW 5_7, it will return new True() or new False()
     * For FW 6.0, it will return False.instance() or True.instance()
     */
    public com.redknee.framework.xhome.filter.Predicate getTrueOrFalseFWPredicate(boolean value); 
    
    
    
    /**
     * To support FW 5_7 and FW 6 compatiblity
     * @param ctx
     * @return MessageConfigurationSupport based on correct key used for appropriate FW
     */
    public MessageConfigurationSupport getConfigurationSupport(Context ctx);
    
    
    public <T extends AbstractBean> T doClone(final T bean);
    
    
    /**
     * 
     * @param <T>
     * @param ctx
     * @param beanClass
     * @return
     */
    public <T extends AbstractBean> T instantiateBean(final Context ctx, final Class<T> beanClass);

    /**
     * 
     * @param ctx
     * @param script
     * @return
     */
    public Object getObjectFromScript(Context ctx, Script script);
    
    /**
     * 
     * @param <T>
     * @param ctx
     * @param objectType
     * @param script
     * @return
     */
    public <T> T getObjectFromScript(Context ctx, Class<T> objectType, Script script);
    
    /**
     * 
     * @param ctx
     * @param script
     * @param scriptOutput
     * @return
     */
    public Object getObjectFromScript(Context ctx, Script script, StringBuilder scriptOutput);
    /**
     * 
     * @param <T>
     * @param ctx
     * @param objectType
     * @param script
     * @param scriptOutput
     * @return
     */
    public <T> T getObjectFromScript(Context ctx, Class<T> objectType, Script script, StringBuilder scriptOutput);

    
    
    /**
     * 
     * @param ctx
     * @param permission
     * @return true if current session user has the permission
     */
    public boolean hasPermission(Context ctx, String permission);
    
    /**
     * 
     * @param ctx
     * @param permission
     * @return true if current session user has the permission
     */
    public boolean hasPermission(Context ctx, Permission permission);

}
