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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.redknee.framework.xhome.beans.ExceptionListener;
import com.redknee.framework.xhome.beans.SafetyUtil;
import com.redknee.framework.xhome.beans.XBeans;
import com.redknee.framework.xhome.beans.xi.PropertyInfo;
import com.redknee.framework.xhome.beans.xi.XInfo;
import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.And;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.elang.NARY;
import com.redknee.framework.xhome.elang.Value;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.HomeProxy;
import com.redknee.framework.xhome.support.IdentitySupport;
import com.redknee.framework.xhome.support.MapSupport;
import com.redknee.framework.xhome.visitor.AbstractValueVisitor;
import com.redknee.framework.xhome.visitor.FindVisitor;
import com.redknee.framework.xhome.visitor.ListBuildingVisitor;
import com.redknee.framework.xhome.visitor.RemoveAllVisitor;
import com.redknee.framework.xhome.visitor.Visitor;
import com.redknee.framework.xhome.visitor.Visitors;
import com.redknee.framework.xhome.xdb.XProjection;
import com.redknee.framework.xlog.log.DebugLogMsg;
import com.redknee.framework.xlog.log.LogSupport;
import com.redknee.framework.xlog.log.MinorLogMsg;

import com.redknee.app.crm.bean.KeyValueEntry;
import com.redknee.app.crm.bean.KeyValueEntryID;
import com.redknee.app.crm.bean.KeyValueEntryXInfo;
import com.redknee.app.crm.support.HomeSupportHelper;


/**
 * This home will flatten multiple key/value pair extension and their entries into a single home.
 *
 * @author aaron.gourley@redknee.com
 * @since 8.2
 */
public class KeyValueEntryFlatteningHome extends HomeProxy
{
    final private PropertyInfo keyValueMap_;
    final private PropertyInfo key_;
    final private PropertyInfo value_;


    public KeyValueEntryFlatteningHome(
            Context ctx,
            Home keyValueEntryHome,
            PropertyInfo keyValueMapProperty, 
            PropertyInfo keyProperty, 
            PropertyInfo valueProperty)
    {
        super(ctx, keyValueEntryHome);
        
        keyValueMap_ = keyValueMapProperty;
        key_ = keyProperty;
        value_ = valueProperty;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object create(Context ctx, Object obj) throws HomeException
    {
        Class beanClass = getBeanClass();
        
        Map<String, String> keyValueMap = getKeyValueMap(ctx, obj);
        for (Map.Entry<String, String> entry : keyValueMap.entrySet())
        {
            if (entry == null)
            {
                continue;
            }

            String key = entry.getKey();
            if (key != null && key.length() > 0)
            {
                KeyValueEntry newBean = new KeyValueEntry();
                newBean.setBeanClass(beanClass.getName());
                newBean.setBeanIdentifier(getExtensionIDString(ctx, obj));
                newBean.setKey(key);
                newBean.setValue(entry.getValue());
                super.create(ctx, newBean);
            }
        }
        return obj;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object store(Context ctx, Object obj) throws HomeException
    {
        Class beanClass = getBeanClass();
        
        String idString = getExtensionIDString(ctx, obj);
        if (idString != null)
        {
            And filter = new And();
            filter.add(new EQ(KeyValueEntryXInfo.BEAN_CLASS, beanClass.getName()));
            filter.add(new EQ(KeyValueEntryXInfo.BEAN_IDENTIFIER, idString));
            
            Map<KeyValueEntryID, KeyValueEntry> existingPairMap = MapSupport.fromList((List)super.select(ctx, filter));
            Map<String, String> keyValueMap = getKeyValueMap(ctx, obj);
            for (Map.Entry<String, String> entry : keyValueMap.entrySet())
            {
                if (entry == null)
                {
                    continue;
                }
                
                final String key = entry.getKey();
                if (key != null && key.length() > 0)
                {
                    KeyValueEntryID pair = new KeyValueEntryID(beanClass.getName(), idString, key);
                    try
                    {
                        if (existingPairMap.containsKey(pair))
                        {
                            KeyValueEntry updateBean = existingPairMap.remove(pair);
                            if (!SafetyUtil.safeEquals(updateBean.getValue(), entry.getValue()))
                            {
                                updateBean.setValue(entry.getValue());
                                super.store(ctx, updateBean);   
                            }
                        }
                        else
                        {
                            KeyValueEntry newBean = new KeyValueEntry();
                            newBean.setBeanClass(pair.getBeanClass());
                            newBean.setBeanIdentifier(pair.getBeanIdentifier());
                            newBean.setKey(pair.getKey());
                            newBean.setValue(entry.getValue());
                            super.create(ctx, newBean);
                        }
                    }
                    catch (HomeException e)
                    {
                        ExceptionListener el = (ExceptionListener) ctx.get(ExceptionListener.class);
                        if (el != null)
                        {
                            el.thrown(e);
                        }
                        else
                        {
                            new MinorLogMsg(this, "Error storing key/value pair [ID=" + pair + "]", e).log(ctx);
                        }
                    }
                }
            }

            for (KeyValueEntry toRemove : existingPairMap.values())
            {
                try
                {
                    super.remove(ctx, toRemove);
                }
                catch (HomeException e)
                {
                    ExceptionListener el = (ExceptionListener) ctx.get(ExceptionListener.class);
                    if (el != null)
                    {
                        el.thrown(e);
                    }
                    else
                    {
                        new MinorLogMsg(this, "Error removing key/value pair [" + toRemove + "]", e).log(ctx);
                    }
                }
            }
        }
        
        return obj;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(Context ctx, Object obj) throws HomeException
    {
        Class beanClass = getBeanClass();

        Map<String, String> keyValueMap = getKeyValueMap(ctx, obj);
        for (Map.Entry<String, String> keyValue : keyValueMap.entrySet())
        {
            KeyValueEntry pair = new KeyValueEntry();
            pair.setBeanClass(beanClass.getName());
            pair.setBeanIdentifier(getExtensionIDString(ctx, obj));
            pair.setKey(keyValue.getKey());
            pair.setValue(keyValue.getValue());
            try
            {
                super.remove(ctx, pair);   
            }
            catch (HomeException e)
            {
                ExceptionListener el = (ExceptionListener) ctx.get(ExceptionListener.class);
                if (el != null)
                {
                    el.thrown(e);
                }
                else
                {
                    new MinorLogMsg(this, "Error removing key/value pair [" + pair + "]", e).log(ctx);
                }
            }
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void removeAll(Context ctx, Object where) throws HomeException
    {
        forEach(ctx, new RemoveAllVisitor(this), where);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Collection select(Context ctx, Object where) throws HomeException
    {
        Visitor result = forEach(ctx, new ListBuildingVisitor(), where);
        result = Visitors.find(result, Collection.class);
        if (result instanceof Collection)
        {
            return (Collection)result;
        }
        return new ArrayList();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object find(Context ctx, Object where) throws HomeException
    {
        Object findVisitorWhere = HomeSupportHelper.get(ctx).wrapKeyWithEQ(ctx, this.getBeanClass(), where);
        Visitor result = forEach(ctx, new FindVisitor(ctx, findVisitorWhere), where);
        result = Visitors.find(result, AbstractValueVisitor.class);
        if (result instanceof AbstractValueVisitor)
        {
            return ((AbstractValueVisitor)result).getValue();
        }
        return null;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Visitor forEach(Context ctx, Visitor visitor, Object where) throws HomeException
    {
        Collection results = null;
        try
        {
            results = this.getRealBeans(ctx, where);
        }
        catch (Exception e)
        {
            throw new HomeException("Error occurred retrieving key/value pairs: " + e.getMessage(), e);
        }
        
        if (results != null)
        {
            try
            {
                return Visitors.forEach(ctx, results, visitor, where);
            }
            catch (AgentException e)
            {
                throw new HomeException("Error occurred retrieving key/value pairs: " + e.getMessage(), e);
            }   
        }
        
        return visitor;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object cmd(Context ctx, Object arg) throws HomeException
    {
        if (arg instanceof XProjection)
        {
            throw new HomeException("Projection commands are not supported by " + this.getClass().getSimpleName());
        }
        
        try
        {
            if (LogSupport.isDebugEnabled(ctx))
            {
                new DebugLogMsg(this, "cmd() called received by " + this.getClass().getSimpleName()
                        + ".  Passing cmd on to delegate for processing, although it may fail...", null).log(ctx);
            }
            
            Object result = super.cmd(ctx, arg);

            if (LogSupport.isDebugEnabled(ctx))
            {
                new DebugLogMsg(this, "cmd() call completed without error on " + this.getClass().getSimpleName(), null).log(ctx);
            }
            return result;
        }
        catch (HomeException e)
        {
            if (LogSupport.isDebugEnabled(ctx))
            {
                new DebugLogMsg(this, "cmd() failed on " + this.getClass().getSimpleName()
                    + ".  This is probably due to a schema difference between the 2 home pipelines involved.  Returning original cmd argument so the call doesn't crash and burn...", e).log(ctx);
            }
            return arg;
        }
    }
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isKey(Context ctx, Object key) throws HomeException
    {
        Class beanClass = getBeanClass();
        IdentitySupport idSupport = (IdentitySupport) XBeans.getInstanceOf(ctx, beanClass, IdentitySupport.class);
        if (idSupport != null)
        {
            return idSupport.isKey(key);
        }
        return false;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void drop(Context ctx) throws HomeException
    {
        throw new UnsupportedOperationException("Not allowed to drop the master key/value table from a " + KeyValueEntryFlatteningHome.class.getSimpleName());
    }


    protected Class getBeanClass()
    {
        Class beanClass = null;
        if (keyValueMap_ != null)
        {
            beanClass = keyValueMap_.getBeanClass();
        }
        else if (key_ != null)
        {
            beanClass = key_.getBeanClass();
        }
        return beanClass;
    }


    protected Collection<Object> getRealBeans(Context ctx, Object obj) throws HomeException, IOException, InstantiationException
    {
        Class beanClass = getBeanClass();

        And filter = new And();
        filter.add(new EQ(KeyValueEntryXInfo.BEAN_CLASS, beanClass.getName()));

        final String extensionIDString = getExtensionIDString(ctx, obj);
        if (extensionIDString != null)
        {
            filter.add(new EQ(KeyValueEntryXInfo.BEAN_IDENTIFIER, extensionIDString));   
        }
        
        Collection<KeyValueEntry> pairs = super.select(ctx, filter);
        
        Map<Object, Object> beanMap = new HashMap<Object, Object>();
        IdentitySupport idSupport = (IdentitySupport) XBeans.getInstanceOf(ctx, beanClass, IdentitySupport.class);
        for (KeyValueEntry pair : pairs)
        {
            if (!beanMap.containsKey(pair.getBeanIdentifier()))
            {
                Object newBean = XBeans.instantiate(beanClass, ctx);
                if (idSupport != null)
                {
                    idSupport.setID(newBean, idSupport.fromStringID(pair.getBeanIdentifier()));
                }
                beanMap.put(pair.getBeanIdentifier(), newBean);
            }

            Object bean = beanMap.get(pair.getBeanIdentifier());
            try
            {
                if (keyValueMap_ != null)
                {
                    Map newPairs = (Map) keyValueMap_.get(bean);
                    if (newPairs == null)
                    {
                        newPairs = new HashMap<Object, Object>();
                    }
                    Object newPair = XBeans.instantiate(key_.getBeanClass(), ctx);
                    key_.set(newPair, key_.fromString(pair.getKey()));
                    value_.set(newPair, value_.fromString(pair.getValue()));
                    newPairs.put(XBeans.getIdentifier(newPair), newPair);
                    keyValueMap_.set(bean, newPairs);
                }
                else if (key_ != null && pairs != null && pairs.size() > 0)
                {
                    key_.set(bean, key_.fromString(pair.getKey()));
                    value_.set(bean, value_.fromString(pair.getValue()));
                    break;
                }
            }
            catch (ClassCastException e)
            {
                throw new HomeException("Invalid object passed to " + KeyValueEntryFlatteningHome.class.getSimpleName() + ": " + e.getMessage(), e);
            }
        }
        
        return beanMap.values();
    }


    protected String getExtensionIDString(Context ctx, Object obj)
    {
        Class beanClass = getBeanClass();
        
        Object extensionID = null;
        if (obj instanceof NARY)
        {
            for (Value value : (Collection<Value>) ((NARY) obj).getList())
            {
                String extensionIDString = getExtensionIDString(ctx, value.getArg1());
                if (extensionIDString != null)
                {
                    return extensionIDString;
                }
            }
        }
        else if (obj instanceof EQ)
        {
            EQ eq = (EQ) obj;
            
            PropertyInfo prop = null;
            Object value = null;
            
            Object arg1 = eq.getArg1();
            Object arg2 = eq.getArg2();
            if (arg1 instanceof PropertyInfo)
            {
                prop = (PropertyInfo) arg1;
                value = arg2;
            }
            else if (arg2 instanceof PropertyInfo)
            {
                prop = (PropertyInfo) arg2;
                value = arg1;
            }
            if (prop != null)
            {
                XInfo xInfo = prop.getXInfo();
                if (xInfo != null)
                {
                    PropertyInfo idProperty = xInfo.getID();
                    if (SafetyUtil.safeEquals(idProperty.getSQLName(), prop.getSQLName())
                            && idProperty.getType().isInstance(value))
                    {
                        extensionID = value;
                    }
                }
            }
        }
        else
        {
            extensionID = XBeans.getIdentifier(obj);
        }
        
        IdentitySupport idSupport = (IdentitySupport) XBeans.getInstanceOf(ctx, beanClass, IdentitySupport.class);
        if (idSupport != null)
        {
            if (idSupport.isKey(obj))
            {
                extensionID = obj;
            }   
        }

        final String result;
        
        if (extensionID != null)
        {
            if (idSupport != null)
            {
                extensionID = idSupport.toStringID(extensionID);   
            }
            result = String.valueOf(extensionID);
        }
        else
        {
            result = null;
        }
        
        return result;
    }


    protected Map<String, String> getKeyValueMap(Context ctx, Object obj) throws HomeException
    {
        Map<String, String> keyValueMap = new HashMap<String, String>();
        
        Object keyValueObj = obj;
        if (keyValueMap_ != null)
        {
            try
            {
                keyValueObj = keyValueMap_.get(obj);   
            }
            catch (ClassCastException e)
            {
                throw new HomeException("Invalid object passed to " + KeyValueEntryFlatteningHome.class.getSimpleName() + ": " + e.getMessage(), e);
            }
        }
        
        if (!(keyValueObj instanceof Map))
        {
            addKeyValuePairToMap(ctx, keyValueMap, keyValueObj);
        }
        else
        {
            Map beansKeyValueMap = (Map) keyValueObj;
            for (Object bean : beansKeyValueMap.values())
            {
                addKeyValuePairToMap(ctx, keyValueMap, bean);
            }
        }
        
        return keyValueMap;
    }


    protected void addKeyValuePairToMap(Context ctx, Map<String, String> map, Object bean) throws HomeException
    {
        Object key = null;
        Object value = null;
        try
        {
            key = key_.get(bean);
            value = value_.get(bean);
        }
        catch (ClassCastException e)
        {
            throw new HomeException("Invalid object passed to " + KeyValueEntryFlatteningHome.class.getSimpleName() + ": " + e.getMessage(), e);
        }

        IdentitySupport idSupport = (IdentitySupport) XBeans.getInstanceOf(ctx, bean.getClass(), IdentitySupport.class);
        if (idSupport != null)
        {
            key = idSupport.toStringID(key);    
        }

        map.put(String.valueOf(key), String.valueOf(value));
    }
}
