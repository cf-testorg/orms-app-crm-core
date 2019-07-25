/*
 * This code is a protected work and subject to domestic and international copyright
 * law(s). A complete listing of authors of this work is readily available. Additionally,
 * source code is, by its very nature, confidential information and inextricably contains
 * trade secrets and other information proprietary, valuable and sensitive to Redknee. No
 * unauthorized use, disclosure, manipulation or otherwise is permitted, and may only be
 * used in accordance with the terms of the license agreement entered into with Redknee
 * Inc. and/or its subsidiaries.
 * 
 * Copyright (c) Redknee Inc. and its subsidiaries. All Rights Reserved.
 */
package com.redknee.app.crm.support.messages;

import java.util.Collection;

import com.redknee.app.crm.bean.SpidLangMsgConfig;
import com.redknee.app.crm.bean.SpidLangMsgConfigXInfo;
import com.redknee.framework.xhome.beans.ID;
import com.redknee.framework.xhome.beans.XBeans;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.context.ContextAware;
import com.redknee.framework.xhome.elang.And;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.filter.Predicate;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.HomeInternalException;
import com.redknee.framework.xlog.log.InfoLogMsg;
import com.redknee.framework.xlog.log.MajorLogMsg;
import com.redknee.framework.xlog.log.MinorLogMsg;


/**
 * 
 * @author ssimar
 * 
 * @param <MESSSAGE>
 *            - Is type that extends SpidLangMessage A generic handle Multi-Language
 *            messages The code and logic is same for all message-types, just differs in
 *            the Bean Type and Identity All message bean types are children of
 *            SpidLangMessage All BeanID types are children of the Bean type itself.
 *            Please do not be tempted to use E-Langs for lookups on messageID. We want
 *            maximum cache hits Xbens.Identity based look-ups are leverage the caching on
 *            Homes. Do not change to e-Lang there.
 */
public class MessageConfigurationSupport<MESSSAGE extends SpidLangMsgConfig, MESSAGEID extends ID> implements ContextAware
{

    
    /**
     * author: simar.singh@redknee.com
     * 
     * The BeanID class extends the Bean Class but it does not have a default
     * constructor I are better-off passing the BeanID instance as a reference to Bean
     * type rather than getting the BeanID and then getting the custom constructor to
     * instantiate an ID. All we need is the BeanID type instance; we can clone it from the
     * one passed. 
     */
    public MessageConfigurationSupport(final Context ctx, final Object homeKey, final Class<MESSSAGE> beanClass, final Class<MESSAGEID> idBeanClass, final MESSAGEID defualtMessageID)
    {
        ctx_ = ctx;
        beanClass_ = beanClass;
        idBeanClass_= idBeanClass;
        homeKey_ = homeKey;
        DEFAULT_MESSAGE_ID = defualtMessageID;
    }


    /**
     * Xbeans.Identity based look-ups leverage the caching. Do not change to e-Lang
     * 
     * @param spid
     * @return - <MESSAGE> for the Spid and Default Language
     * @throws HomeInternalException
     * @throws HomeException
     */
    public MESSSAGE getMessageConfiguration(final int spid) throws HomeInternalException, HomeException
    {
        return getMessageConfiguration(getContext(), spid, DEFAULT_LANGUAGE);
    }


    /**
     * Xbeans.Identity based look-ups leverage the caching. Do not change to e-Lang
     * 
     * @param spid
     * @param language
     * @return <MESSAGE> keyed with SPID and Language
     * @throws HomeInternalException
     * @throws HomeException
     */
    public MESSSAGE getMessageConfiguration(final int spid, final String language) throws HomeInternalException,
            HomeException
    {
        return getMessageConfiguration(getContext(), spid, language);
    }


    /**
     * Xbeans.Identity based look-ups leverage the caching. Do not change to e-Lang
     * 
     * @param context
     * @param spid
     * @param language
     * @return <MESSAGE> for the Suscriber's SPID and Language (Default Language if
     *         Subscrber's language in unknown)
     * @throws HomeInternalException
     * @throws HomeException
     */
    public MESSSAGE getMessageConfiguration(Context context, final int spid, final String language)
            throws HomeInternalException, HomeException
    {
        return (MESSSAGE) (getMessageConfiguration(context, getHomeKey(), getMessageID(spid, language)));
    }
    
    /**
     * Xbeans.Identity based look-ups leverage the caching. Do not change to e-Lang
     * @param context
     * @param messageID
     * @return <MESSAGE> for the Suscriber's SPID and Language (Default Language if
     *         Subscrber's language in unknown)
     * @throws HomeInternalException
     * @throws HomeException
     */
    public MESSSAGE getMessageConfiguration(final Context context, final MESSAGEID messageID)
            throws HomeInternalException, HomeException
    {
        return getMessageConfiguration(context, getHomeKey(), messageID);
    }


    /**
     * Xbeans.Identity based look-ups leverage the caching. Do not change to e-Lang
     * 
     * @param ctx
     * @param homeKey
     * @param messageID
     * @return <MESSAGE> for the Suscriber's SPID and Language (Default Language if
     *         Subscrber's language in unknown)
     * @throws HomeInternalException
     * @throws HomeException
     */
    protected MESSSAGE getMessageConfiguration(final Context ctx, final Object homeKey, final MESSAGEID messageID)
            throws HomeInternalException, HomeException
    {
        final Home messageHome = ((Home) ctx.get(homeKey));
        MESSSAGE message;
        
        if (messageID !=null )

        {
            message = (MESSSAGE) messageHome.find(ctx, messageID);
            if (message != null)
            {
                /*
                 * return the exact match found for (spid,language)
                 */
                return (MESSSAGE) message;
            }
        }

        
        message = XBeans.instantiate(beanClass_, getContext());
        MESSSAGE frozenMessage = message;
        DEFAULT_MESSAGE_ID.set(message);
        message.setLanguage(DEFAULT_LANGUAGE);
        message = (MESSSAGE) messageHome.find(ctx, message);
        if (message == null)
        {
            new MinorLogMsg(this, "Default-Language SMS Notification of type ID [" + messageID.getClass().getName()
                    + "] is missing for SPID: " + frozenMessage.getSpid(), null).log(ctx);
            return null;
        }
        else if (!message.isActive())
        {
            new InfoLogMsg(this, "SMS Notification of type [" + message.getClass().getName()
                    + "] is disabled for SPID: " + message.getSpid() + ", Language: " + message.getLanguage(), null)
                    .log(ctx);
            return null;
        }
        else
        {
            return (MESSSAGE) message;
        }
    }


    public MESSAGEID getDefaultMessageID()
    {
        MESSSAGE message = XBeans.instantiate(beanClass_, getContext());
        DEFAULT_MESSAGE_ID.set(message);
        return (MESSAGEID) message.ID();
    }


    public MESSAGEID getMessageID(int spid, String language)
    {
        MESSSAGE message = XBeans.instantiate(beanClass_, getContext());
        
        final MESSAGEID messageID = getDefaultMessageID();

        messageID.set(message);
        
        message.setSpid(spid);
        if (language != null && language.length() > 0)
        {
            message.setLanguage(language);
        }
        else
        {
            // language is null/blank on error and not supported cases
            message.setLanguage(DEFAULT_LANGUAGE);
        }
        
        messageID.get(message);
        
        return messageID;
    }

    /**
     * Return the collection of MESSSAGE elements stored in the system for the given SPID
     * @param ctx
     * @param homeKey
     * @param spid
     * @return
     * @throws HomeException
     */
    public Collection<MESSSAGE> getAllMessageConfiguration(Context ctx, final int spid)
    throws HomeException
    {
    	return getAllMessageConfiguration(ctx, spid, null);
    }

    /**
     * Return the collection of MESSSAGE elements stored in the system for the given SPID and filter
     * Force to at least select by SPID
     * @param ctx
     * @param homeKey
     * @param spid
     * @return
     * @throws HomeException
     */
    public Collection<MESSSAGE> getAllMessageConfiguration(Context ctx, final int spid, final Predicate filter)
    throws HomeException
    {
        Object homeKey = getHomeKey();
        final Home messageHome = ((Home) ctx.get(homeKey));
        
        And predicate = new And();
        predicate.add(new EQ(SpidLangMsgConfigXInfo.SPID, spid));
        if (filter != null)
        {
        	predicate.add(filter);
        }
        if (messageHome == null)
        {
            StringBuilder msg = new StringBuilder();
            msg.append("Error occurred while retrieving all Messages for SPID ");
            msg.append(spid);
            if(filter != null)
            {
            	msg.append(" and filter ");
            	msg.append(filter.toString());
            }
            msg.append(". MessageHome is null in the Context! ");
            if (homeKey instanceof Class)
            {
                msg.append(" for Class Key= ");
                msg.append(((Class)homeKey).getSimpleName());
            }
            new MajorLogMsg(this, msg.toString(), null).log(ctx);
            throw new HomeException(msg.toString());
        }
        return messageHome.where(ctx, predicate).selectAll();
    }
    
    /**
     * Create the new record in the System.
     * @param ctx
     * @param record
     * @return
     * @throws HomeException
     */
    public Object createMessageConfiguration(Context ctx, MESSSAGE record)
    throws HomeException
    {
        final Home messageHome = ((Home) ctx.get(getHomeKey()));
        return messageHome.create(ctx, record);
    }
    
    /**
     * Store the record in the System.
     * @param ctx
     * @param record
     * @return
     * @throws HomeException
     */
    public Object updateMessageConfiguration(Context ctx, MESSSAGE record)
    throws HomeException
    {
        final Home messageHome = ((Home) ctx.get(getHomeKey()));
        return messageHome.store(ctx, record);
    }
    
    /**
     * Remove the record from the system.
     * @param ctx
     * @param record
     * @throws HomeException
     */
    public void removeMessageConfiguration(Context ctx, MESSSAGE record)
    throws HomeException
    {
        final Home messageHome = ((Home) ctx.get(getHomeKey()));
        messageHome.remove(ctx, record);
    }

    /*
     * @Override(non-Javadoc)
     * 
     * @see com.redknee.framework.xhome.context.ContextAware#getContext()
     */
    public Context getContext()
    {
        return ctx_;
    }


    /*
     * @Override(non-Javadoc)
     * 
     * @see
     * com.redknee.framework.xhome.context.ContextAware#setContext(com.redknee.framework
     * .xhome.context.Context)
     */
    public void setContext(Context ctx)
    {
        ctx_ = ctx;
    }


    public Object getHomeKey()
    {
        return homeKey_;
    }


    public void setHomeKey(Object homeKey)
    {
        this.homeKey_ = homeKey;
    }

    private Context ctx_;
    private Object homeKey_;
    Class<MESSSAGE> beanClass_;
    Class<MESSAGEID> idBeanClass_;
    private static String DEFAULT_LANGUAGE = SpidLangMsgConfig.DEFAULT_LANGUAGE;
    public final MESSAGEID DEFAULT_MESSAGE_ID;
}
