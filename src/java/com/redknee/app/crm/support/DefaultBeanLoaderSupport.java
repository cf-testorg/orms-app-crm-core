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

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import com.redknee.framework.xhome.beans.AbstractBean;
import com.redknee.framework.xhome.beans.XBeans;
import com.redknee.framework.xhome.beans.xi.PropertyInfo;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.HomeInternalException;
import com.redknee.framework.xhome.msp.Spid;
import com.redknee.framework.xhome.support.IdentitySupport;
import com.redknee.framework.xhome.webcontrol.AbstractWebControl;
import com.redknee.framework.xlog.log.DebugLogMsg;
import com.redknee.framework.xlog.log.InfoLogMsg;
import com.redknee.framework.xlog.log.MinorLogMsg;

import com.redknee.app.crm.bean.GLCodeMapping;
import com.redknee.app.crm.bean.ReasonCode;
import com.redknee.app.crm.bean.Service;
import com.redknee.app.crm.bean.ServiceXInfo;
import com.redknee.app.crm.bean.TransactionMethod;
import com.redknee.app.crm.bean.TransactionXInfo;
import com.redknee.app.crm.bean.core.Transaction;
import com.redknee.app.crm.bean.payment.PaymentAgent;
import com.redknee.app.crm.bean.service.xml.XMLProvisioningServiceType;
import com.redknee.app.crm.extension.service.AlcatelSSCServiceExtension;
import com.redknee.app.crm.extension.service.AlcatelSSCServiceExtensionXInfo;



/**
 * A support class that constructs and provides access to "bean loader maps"
 * that can be used to dig through Contexts and Homes using foriegn key relationships
 * to intelligently find stuff that we are looking for.
 * 
 * TODO: Make the predefined maps GUI configurable instead of static and enhance to support:
 * 
 * - Loading beans contained within Collections/Maps within beans
 *   - i.e. to get a PoolExtension from within an Account
 *   
 * - Loading beans with multi-part keys (?)
 *
 * @author aaron.gourley@redknee.com
 * @since 8.2 
 */
public class DefaultBeanLoaderSupport implements BeanLoaderSupport
{   
    protected static final Object BEAN_LOADER_MAP_CTX_KEY = "KeyValueSupport.BeanLoaderMap";

    protected static BeanLoaderSupport instance_ = null;
    public static BeanLoaderSupport instance()
    {
        if (instance_ == null)
        {
            instance_ = new DefaultBeanLoaderSupport();
        }
        return instance_;
    }

    protected DefaultBeanLoaderSupport()
    {
    }
    
    public void setBeanLoaderMap(Context ctx, Map<Class, Collection<PropertyInfo>> map)
    {
        ctx.put(BEAN_LOADER_MAP_CTX_KEY, map);
    }

    public Map<Class, Collection<PropertyInfo>> getBeanLoaderMap(Context ctx)
    {
        return (Map<Class, Collection<PropertyInfo>>) ctx.get(BEAN_LOADER_MAP_CTX_KEY);
    }

    
    public boolean hasBeanLoaderMap(Context ctx)
    {
        return ctx.has(BEAN_LOADER_MAP_CTX_KEY);
    }
    
    
    /**
     * This method will do its best to populate the context with desired values.
     * 
     * It will use the following approaches:
     *  1. Check to see if the dependent context key is already available.
     *  2. Use the class->property map to retrieve a bean if the calculator needs a bean class context key.
     *  3. Recursively "dig" to get a specific value and put it in the context if found.
     *  
     * @param ctx Context to load missing beans into
     * @param desiredContextKeys Collection of Class or PropertyInfo objects that are being used as context keys.
     * @param beanLoaderMap Map used to recursively retrieve the desired beans.
     * @return
     */
    public void prepareContext(
            Context ctx, 
            Collection<Object> desiredContextKeys, 
            Map<Class, Collection<PropertyInfo>> beanLoaderMap)
    {
        if (ctx == null
                || desiredContextKeys == null
                || desiredContextKeys.size() == 0)
        {
            return;
        }
        
        // Failed lookup tracker prevents subsequent lookups for beans that couldn't
        // be loaded the first time (no reason they'll load the second time).
        Set<Object> failedLookups = new HashSet<Object>();
        for (Object ctxKey : desiredContextKeys)
        {
            if (ctxKey != null)
            {
                Object newCtxValue = getValueForContext(ctx, ctxKey, beanLoaderMap, failedLookups);
                if (newCtxValue != null)
                {
                    ctx.put(ctxKey, newCtxValue);   
                }
                else
                {
                    new DebugLogMsg(this, "Unable to load bean for desired key=" + ctxKey, null).log(ctx);
                }
            }
        }
    }
    
    public void prepareContext(
            Context ctx, 
            Collection<Object> desiredContextKeys)
    {
        prepareContext(ctx, desiredContextKeys, getBeanLoaderMap(ctx));
    }
    
    public void prepareContext(
            Context ctx, 
            Object... desiredContextKeys)
    {
        prepareContext(ctx, Arrays.asList(desiredContextKeys));
    }

    public <T extends AbstractBean> T getBean(Context ctx, Class<T> ctxKey)
    {
        return (T) getValueForContext(ctx.createSubContext(), ctxKey, getBeanLoaderMap(ctx), new HashSet<Object>());
    }

    private Object getValueForContext(Context ctx, Object ctxKey, Map<Class, Collection<PropertyInfo>> beanLoaderMap, Set<Object> failedLookups)
    {
        // First see if the bean is already placed in the context under some other common key
        Object result = getBeanFromContextKey(ctx, ctxKey);
        if (result == null
                && !failedLookups.contains(ctxKey))
        {
            if (ctxKey instanceof Class
                    && AbstractBean.class.isAssignableFrom((Class)ctxKey))
            {
                boolean retryAllowed = false;
                
                if (beanLoaderMap != null)
                {
                    // By convention, we normally store beans under this type of context key.
                    Collection<PropertyInfo> propertyInfoList = beanLoaderMap.get(ctxKey);
                    if (propertyInfoList != null)
                    {
                        for (PropertyInfo propertyInfo : propertyInfoList)
                        {
                            if (propertyInfo != null
                                    && /* Prevent infinite loop */ (ctx.has(propertyInfo) || !ctxKey.equals(propertyInfo.getBeanClass())))
                            {
                                try
                                {
                                    result = getBeanUsingPropertyInfo(ctx, (Class<AbstractBean>) ctxKey, propertyInfo, beanLoaderMap, failedLookups);
                                    if (result != null)
                                    {
                                        break;
                                    }
                                }
                                catch (HomeInternalException e)
                                {
                                    // Don't add to failed lookups (HomeInternalException is retryable)
                                    retryAllowed = true;
                                }
                            }
                        }
                    }   
                }
                
                if (result == null
                        && !retryAllowed)
                {
                    failedLookups.add(ctxKey);   
                }
            }
            else if (ctxKey instanceof PropertyInfo)
            {
                // We can support putting property values in the context for calculators
                // That want to look up values in the context using the PropertyInfo
                PropertyInfo prop = (PropertyInfo) ctxKey;
                Object bean = getValueForContext(ctx, prop.getBeanClass(), beanLoaderMap, failedLookups);
                if (bean != null)
                {
                    if (!ctx.has(prop.getBeanClass()))
                    {
                        // Put the bean in the context to avoid repeatedly getting the bean when
                        // calculators need many PropertyInfo's from the same bean.
                        ctx.put(prop.getBeanClass(), bean);   
                    }
                    result = prop.get(bean);
                }
            }
        }
        return result;
    }

    
    private Object getBeanUsingPropertyInfo(
            Context ctx, 
            Class<AbstractBean> beanClass, 
            PropertyInfo propertyInfo,
            Map<Class, Collection<PropertyInfo>> beanLoaderMap, 
            Set<Object> failedLookups) throws HomeInternalException
    {
        Object result = null;
        
        // Try to look up the bean
        Object propertyValue = getValueForContext(ctx, propertyInfo, beanLoaderMap, failedLookups);
        if (propertyValue != null)
        {
            if (beanClass.isAssignableFrom(propertyValue.getClass()))
            {
                result = propertyValue;
            }
            else
            {
                IdentitySupport idSupport = (IdentitySupport) XBeans.getInstanceOf(ctx, beanClass, IdentitySupport.class);
                if (idSupport == null || idSupport.isKey(propertyValue))
                {
                    try
                    {
                        result = HomeSupportHelper.get(ctx).findBean(ctx, beanClass, propertyValue);
                        if (result == null)
                        {
                            new DebugLogMsg(this, 
                                    beanClass.getSimpleName() + " not found with ID=" + propertyValue, null).log(ctx);
                        }
                    }
                    catch (HomeInternalException e)
                    {
                        new MinorLogMsg(this, 
                                "Error retrieving " + beanClass.getSimpleName() + " with ID=" + propertyValue, e).log(ctx);
                        throw e;
                    }
                    catch (HomeException e)
                    {
                        new MinorLogMsg(this, 
                                "Error retrieving " + beanClass.getSimpleName() + " with ID=" + propertyValue, e).log(ctx);
                    }   
                }
                else
                {
                    new InfoLogMsg(this, 
                            "Unable to retrieve " + beanClass.getSimpleName()
                            + " using " + propertyInfo.getXInfo().getName()
                            + "." + propertyInfo.getName()
                            + "=" + propertyValue
                            + ".  Value type does not match property type.", null).log(ctx);
                }
            }
        }
        //else: Don't add to failed lookups because it could have been because of a HomeInternalException
        
        return result;
    }

    private Object getBeanFromContextKey(Context ctx, Object ctxKey)
    {
        Object result = null;

        if (ctxKey instanceof Class
                && AbstractBean.class.isAssignableFrom((Class)ctxKey))
        {
            Class cls = (Class) ctxKey;
            if (result == null)
            {
                // Check for bean placed in Context by extension feature
                Object extParentBean = ExtensionSupportHelper.get(ctx).getParentBean(ctx);
                if (extParentBean != null
                        && cls.isAssignableFrom(extParentBean.getClass()))
                {
                    result = extParentBean;
                }
            }
            
            if (result == null)
            {
                // Check for bean placed in Context by web control
                try
                {
                    for (Context tempCtx = ctx;
                            tempCtx != null;
                            tempCtx = (Context) tempCtx.get(".."))
                    {
                        Object wcBean = tempCtx.get(AbstractWebControl.BEAN);
                        if (wcBean == null)
                        {
                            break;
                        }
                        if (cls.isAssignableFrom(wcBean.getClass()))
                        {
                            result = wcBean;
                            break;
                        }
                    }
                }
                catch (Exception e)
                {
                    // Ignore
                }   
            }
        }
        
        if (result == null)
        {
            result = ctx.get(ctxKey);
        }
        
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public <T extends AbstractBean> Map<Class, Collection<PropertyInfo>> getBeanLoaderMap(Context ctx, Class<T> baseType)
    {
        // Should be extended to add custom functionality for different base types
        Map<Class, Collection<PropertyInfo>> map = new HashMap<Class, Collection<PropertyInfo>>();

        if (Service.class.isAssignableFrom(baseType))
        {
            map = getServiceBasedMap();
        }
        else if (AlcatelSSCServiceExtension.class.isAssignableFrom(baseType))
        {
            map = getAlcatelSSCServiceBasedMap();
        }
        else if (Transaction.class.isAssignableFrom(baseType))
        {
            map = getTransactionBasedMap();
        }
        else
        {
            map = getBeanLoaderMap(ctx);
        }
        
        return map;
    }


    /**
     * {@inheritDoc}
     */
    public void mergeBeanLoaderMaps(Map<Class, Collection<PropertyInfo>> dest, Map<Class, Collection<PropertyInfo>>... source)
    {
        if (dest != null && source != null && source.length > 0)
        {
            for (Map<Class, Collection<PropertyInfo>> sourceMap : source)
            {
                if (sourceMap != null && sourceMap.size() > 0)
                {
                    for (Map.Entry<Class, Collection<PropertyInfo>> entry : sourceMap.entrySet())
                    {
                        addAllBeanLoaderMapEntries(dest, entry.getKey(), entry.getValue());
                    }   
                }
            }
        }
    }

    public Map<Class, Collection<PropertyInfo>> getServiceBasedMap()
    {
        synchronized(SERVICE_LOCK)
        {
            if (SERVICE_BASED_MAP == null)
            {
                // Add service related lookup hierarchy
                SERVICE_BASED_MAP = new HashMap<Class, Collection<PropertyInfo>>();
                addBeanLoaderMapEntry(SERVICE_BASED_MAP, Spid.class, ServiceXInfo.SPID);
                addBeanLoaderMapEntry(SERVICE_BASED_MAP, com.redknee.app.crm.bean.account.SubscriptionType.class, ServiceXInfo.SUBSCRIPTION_TYPE);
                addBeanLoaderMapEntry(SERVICE_BASED_MAP, com.redknee.app.crm.bean.core.SubscriptionType.class, ServiceXInfo.SUBSCRIPTION_TYPE);
                addBeanLoaderMapEntry(SERVICE_BASED_MAP, XMLProvisioningServiceType.class, ServiceXInfo.XML_PROV_SVC_TYPE);
                addBeanLoaderMapEntry(SERVICE_BASED_MAP, com.redknee.app.crm.bean.AdjustmentType.class, ServiceXInfo.ADJUSTMENT_TYPE);
                addBeanLoaderMapEntry(SERVICE_BASED_MAP, com.redknee.app.crm.bean.core.AdjustmentType.class, ServiceXInfo.ADJUSTMENT_TYPE);
                SERVICE_BASED_MAP = getUnmodifiableBeanLoaderMap(SERVICE_BASED_MAP);
            }
        }
        return SERVICE_BASED_MAP;
    }

    public Map<Class, Collection<PropertyInfo>> getAlcatelSSCServiceBasedMap()
    {
        synchronized(ALCATEL_SSC_EXTENSION_SERVICE_LOCK)
        {
            if (ALCATEL_SSC_EXTENSION_SERVICE_BASED_MAP == null)
            {
                // Add Alcatel SSC Service related lookup hierarchy
                ALCATEL_SSC_EXTENSION_SERVICE_BASED_MAP = new HashMap<Class, Collection<PropertyInfo>>();
                addBeanLoaderMapEntry(ALCATEL_SSC_EXTENSION_SERVICE_BASED_MAP, Spid.class, AlcatelSSCServiceExtensionXInfo.SPID);
                addBeanLoaderMapEntry(ALCATEL_SSC_EXTENSION_SERVICE_BASED_MAP, com.redknee.app.crm.bean.Service.class, AlcatelSSCServiceExtensionXInfo.SERVICE_ID);
                addBeanLoaderMapEntry(ALCATEL_SSC_EXTENSION_SERVICE_BASED_MAP, com.redknee.app.crm.bean.core.Service.class, AlcatelSSCServiceExtensionXInfo.SERVICE_ID);
                mergeBeanLoaderMaps(ALCATEL_SSC_EXTENSION_SERVICE_BASED_MAP, getServiceBasedMap());
                ALCATEL_SSC_EXTENSION_SERVICE_BASED_MAP = getUnmodifiableBeanLoaderMap(ALCATEL_SSC_EXTENSION_SERVICE_BASED_MAP);
            }
        }
        return ALCATEL_SSC_EXTENSION_SERVICE_BASED_MAP;
    }

    public Map<Class, Collection<PropertyInfo>> getTransactionBasedMap()
    {
        synchronized(TRANSACTION_LOCK)
        {
            if (TRANSACTION_BASED_MAP == null)
            {
                TRANSACTION_BASED_MAP = new HashMap<Class, Collection<PropertyInfo>>();
                addBeanLoaderMapEntry(TRANSACTION_BASED_MAP, Spid.class, TransactionXInfo.SPID);
                addBeanLoaderMapEntry(TRANSACTION_BASED_MAP, com.redknee.app.crm.bean.AdjustmentType.class, TransactionXInfo.ADJUSTMENT_TYPE);
                addBeanLoaderMapEntry(TRANSACTION_BASED_MAP, com.redknee.app.crm.bean.core.AdjustmentType.class, TransactionXInfo.ADJUSTMENT_TYPE);
                addBeanLoaderMapEntry(TRANSACTION_BASED_MAP, GLCodeMapping.class, TransactionXInfo.GLCODE);
                addBeanLoaderMapEntry(TRANSACTION_BASED_MAP, com.redknee.app.crm.bean.Msisdn.class, TransactionXInfo.MSISDN);
                addBeanLoaderMapEntry(TRANSACTION_BASED_MAP, com.redknee.app.crm.bean.core.Msisdn.class, TransactionXInfo.MSISDN);
                addBeanLoaderMapEntry(TRANSACTION_BASED_MAP, PaymentAgent.class, TransactionXInfo.PAYMENT_AGENCY);
                addBeanLoaderMapEntry(TRANSACTION_BASED_MAP, ReasonCode.class, TransactionXInfo.REASON_CODE);
                addBeanLoaderMapEntry(TRANSACTION_BASED_MAP, com.redknee.app.crm.bean.account.SubscriptionType.class, TransactionXInfo.SUBSCRIPTION_TYPE_ID);
                addBeanLoaderMapEntry(TRANSACTION_BASED_MAP, com.redknee.app.crm.bean.core.SubscriptionType.class, TransactionXInfo.SUBSCRIPTION_TYPE_ID);
                addBeanLoaderMapEntry(TRANSACTION_BASED_MAP, TransactionMethod.class, TransactionXInfo.TRANSACTION_METHOD);
                TRANSACTION_BASED_MAP = getUnmodifiableBeanLoaderMap(TRANSACTION_BASED_MAP);
            }
        }
        return TRANSACTION_BASED_MAP;
    }
    
    protected Map<Class, Collection<PropertyInfo>> getUnmodifiableBeanLoaderMap(Map<Class, Collection<PropertyInfo>> map)
    {
        Map<Class, Collection<PropertyInfo>> result = new HashMap<Class, Collection<PropertyInfo>>();
        for (Map.Entry<Class, Collection<PropertyInfo>> entry : map.entrySet())
        {
            result.put(entry.getKey(), Collections.unmodifiableCollection(entry.getValue()));
        }
        return Collections.unmodifiableMap(result);
    }


    protected void addBeanLoaderMapEntry(Map<Class, Collection<PropertyInfo>> beanLoaderMap, Class cls, PropertyInfo... properties)
    {
        addAllBeanLoaderMapEntries(beanLoaderMap, cls, Arrays.asList(properties));
    }


    protected void addAllBeanLoaderMapEntries(Map<Class, Collection<PropertyInfo>> beanLoaderMap, Class cls, Collection<PropertyInfo> properties)
    {
        if (beanLoaderMap != null)
        {
            Collection<PropertyInfo> props = beanLoaderMap.get(cls);
            if (props == null)
            {
                // use linked hash set to maintain insertion order
                // (assumption is that we prefer to use properties added earlier to ones added later)
                props = new LinkedHashSet<PropertyInfo>();
                beanLoaderMap.put(cls, props);
            }
            props.addAll(properties);   
        }
    }

    private static Object SERVICE_LOCK = new Object();
    private static Map<Class, Collection<PropertyInfo>> SERVICE_BASED_MAP = null;

    private static Object ALCATEL_SSC_EXTENSION_SERVICE_LOCK = new Object();
    private static Map<Class, Collection<PropertyInfo>> ALCATEL_SSC_EXTENSION_SERVICE_BASED_MAP = null;

    private static Object TRANSACTION_LOCK = new Object();
    private static Map<Class, Collection<PropertyInfo>> TRANSACTION_BASED_MAP = null;
}
