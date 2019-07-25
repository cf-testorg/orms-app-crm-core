package com.redknee.app.crm.configshare;

import java.util.ArrayList;
import java.util.Collection;

import com.redknee.framework.xhome.beans.AbstractBean;
import com.redknee.framework.xhome.beans.XBeans;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.support.IdentitySupport;
import com.redknee.framework.xlog.log.DebugLogMsg;
import com.redknee.framework.xlog.log.LogSupport;
import com.redknee.framework.xlog.log.MinorLogMsg;

import com.redknee.app.crm.support.ConfigChangeRequestSupport;
import com.redknee.app.crm.support.ConfigChangeRequestSupportHelper;
import com.redknee.app.crm.support.HomeSupportHelper;


public class DefaultConfigChangeRequestTranslator implements ConfigChangeRequestTranslator
{
    private static ConfigChangeRequestTranslator instance_ = null;
    public static ConfigChangeRequestTranslator instance()
    {
        if (instance_ == null)
        {
            instance_ = new DefaultConfigChangeRequestTranslator();
        }
        return instance_;
    }

    protected DefaultConfigChangeRequestTranslator()
    {
        this(null);
    }


    public DefaultConfigChangeRequestTranslator(Class<? extends AbstractBean> internalBeanClass)
    {
        setInternalBeanClass(internalBeanClass);
    }


    public Collection<? extends AbstractBean> translate(Context ctx, ConfigChangeRequest request) throws ConfigChangeRequestTranslationException
    {
        if (LogSupport.isDebugEnabled(ctx))
        {
            new DebugLogMsg(this, "Running DefaultConfigChangeREquestTranslator, assuming that all shared properties have the same field names in external and local applications.", null).log(ctx);
        }
        Collection<AbstractBean> list = new ArrayList<AbstractBean>();
        try
        {
            Class<? extends AbstractBean> beanClass = getBeanClass(ctx, request);
            AbstractBean createNewBean = null;
            if (request.getTypeOfUpdate() != -1)
            {
                // This is a home update
                IdentitySupport identitySupport = (IdentitySupport) XBeans.getInstanceOf(ctx, beanClass,
                        IdentitySupport.class);
                if (identitySupport != null)
                {
                    final Object beanID = identitySupport.fromStringID(request.getBeanId());
                    try
                    {
                        createNewBean = HomeSupportHelper.get(ctx).findBean(ctx, beanClass, beanID);
                        if (createNewBean != null && createNewBean.isFrozen())
                        {
                            // Sometimes TotalCachingHome purposely return frozen beans.  We must clone them
                            // in order to update them.
                            AbstractBean frozenBean = createNewBean;
                            createNewBean = null;
                            createNewBean = (AbstractBean) frozenBean.clone();
                        }
                    }
                    catch (Exception e)
                    {
                        new MinorLogMsg(this, "Error retrieving bean [" + request.getBeanClass() + "] with ID ["
                                + beanID + "].  Attempting to create new instance...", e).log(ctx);
                    }
                    if (createNewBean == null)
                    {
                        createNewBean = (AbstractBean) XBeans.instantiate(beanClass, ctx);
                        identitySupport.setID(createNewBean, beanID);
                    }
                }
            }
            else
            {
                // This is a bean update
                createNewBean = (AbstractBean) ctx.get(beanClass);
            }

            if (createNewBean != null)
            {
                Collection<IndividualUpdate> allProperties = request.getUpdateRequest().values();
                if (allProperties != null)
                {
                    final ConfigChangeRequestSupport configChangeRequestSupport = ConfigChangeRequestSupportHelper.get(ctx);
                    for (IndividualUpdate update : allProperties)
                    {
                        configChangeRequestSupport.setPropertyInfo(ctx, update, createNewBean);
                    }
                }
                // Add to list after all properties have been mapped. This will avoid
                // partial updates succeeding.
                list.add(createNewBean);
            }
            else
            {
                throw new ConfigChangeRequestTranslationException("Unable to find bean to update " + beanClass + " with Id " + request.getBeanId());
            }
        }
        catch (ClassNotFoundException classEx)
        {
            throw new ConfigChangeRequestTranslationException("Unable to translate request for bean class [" + request.getBeanClass() + "].  Class not found.", classEx);
        }
        catch (Throwable t)
        {
            throw new ConfigChangeRequestTranslationException("Unable to translate request for bean class [" + request.getBeanClass() + "].  Unexpected error occurred.", t);
        }
        return list;
    }


    protected Class<? extends AbstractBean> getBeanClass(Context ctx, ConfigChangeRequest request) throws ClassNotFoundException
    {
        if (internalBeanClass_ == null)
        {
            if (LogSupport.isDebugEnabled(ctx))
            {
                new DebugLogMsg(this, "Assuming that external bean class ["
                        + request.getBeanClass() + " ] is same in the local application", null).log(ctx);
            }
            return (Class<AbstractBean>) Class.forName(request.getBeanClass());
        }
        else
        {
            if (LogSupport.isDebugEnabled(ctx))
            {
                new DebugLogMsg(this, "Mapping external bean class ["
                        + request.getBeanClass() + " ] to internal bean class [" + internalBeanClass_.getName() + "]", null).log(ctx);
            }
            return internalBeanClass_;
        }
    }

    public void setInternalBeanClass(Class<? extends AbstractBean> internalBeanClass)
    {
        this.internalBeanClass_ = internalBeanClass;
    }

    private Class<? extends AbstractBean> internalBeanClass_;
}
