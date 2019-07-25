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

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.redknee.framework.xhome.auth.bean.User;
import com.redknee.framework.xhome.beans.SafetyUtil;
import com.redknee.framework.xhome.beans.XBeans;
import com.redknee.framework.xhome.beans.xi.PropertyInfo;
import com.redknee.framework.xhome.beans.xi.XInfo;
import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.home.HomeOperationEnum;
import com.redknee.framework.xhome.msp.SpidAware;
import com.redknee.framework.xhome.visitor.AbortVisitException;
import com.redknee.framework.xhome.visitor.Visitor;
import com.redknee.framework.xlog.log.DebugLogMsg;
import com.redknee.framework.xlog.log.InfoLogMsg;
import com.redknee.framework.xlog.log.LogSupport;
import com.redknee.framework.xlog.log.MinorLogMsg;

import com.redknee.app.crm.support.ConfigChangeRequestSupportHelper;
import com.redknee.app.crm.support.HomeSupportHelper;


/**
 * Visitor used for sharing and synchronizing configuration that is registered for sharing.
 *
 * @author aaron.gourley@redknee.com
 * @since 8.4
 */
public class ConfigSharingVisitor implements Visitor
{
    private static final Object NULL_PLACEHOLDER = new Object();
    public static final short DEFAULT_NULL_HOME_OPERATION = -1;
    protected PropertyInfo property_ = null;
    protected HomeOperationEnum homeOperation_ = null;
    
    protected Object oldValue_ = null;

    public ConfigSharingVisitor()
    {
    }
    
    public ConfigSharingVisitor(PropertyInfo property, Object oldValue)
    {
        property_ = property;
        oldValue_ = oldValue;
    }
    
    public ConfigSharingVisitor(HomeOperationEnum operation, Object oldBean)
    {
        homeOperation_ = operation;
        oldValue_ = oldBean;
    }

    /**
     * {@inheritDoc}
     */
    public void visit(Context ctx, Object bean) throws AgentException, AbortVisitException
    {
        ConfigChangeRequest request = null;
        try
        {
            if (homeOperation_ != null)
            {
                request = createConfigChangeRequest(ctx, homeOperation_, oldValue_, bean);
            }
            else if (property_ != null)
            {
                request = createConfigChangeRequest(ctx, property_, oldValue_, bean);
            }
            else
            {
                request = createConfigChangeRequest(ctx, NULL_PLACEHOLDER, bean);
            }
        }
        catch (Throwable t)
        {
            StringBuilder msg = new StringBuilder("Error occurred while creating ");
            if (homeOperation_ != null)
            {
                msg.append("home [");
                msg.append(homeOperation_);
                msg.append("]");
            }
            else if (property_ != null)
            {
                msg.append("bean [");
                msg.append(property_.getBeanClass().getName());
                msg.append(".");
                msg.append(property_.getName());
                msg.append("]");
            }
            else
            {
                msg.append("sync-up");
            }
            msg.append(" configuration change request for bean [");
            msg.append(String.valueOf(bean));
            msg.append("]");
            throw new AgentException(msg.toString(), t);
        }

        if (request != null
                && (HomeOperationEnum.REMOVE.equals(homeOperation_)
                        || request.getUpdateRequest().size() > 0))
        {
            try
            {
                ConfigChangeRequest result = HomeSupportHelper.get(ctx).createBean(ctx, request);
                new InfoLogMsg(this, "Successfully submitted configuration change request [" + result + "]", null).log(ctx);
            }
            catch (Throwable t)
            {
                throw new AgentException("Error occurred while submitting configuration change request [" + request + "]", t);
            }
        }
        else
        {
            new DebugLogMsg(this, "Request contains no changes to propogate.  No change request sent.", null).log(ctx);
        }
    }



    protected ConfigChangeRequest createConfigChangeRequest(
            Context ctx, 
            HomeOperationEnum operation, 
            Object oldBean, 
            Object newBean)
    {
        ConfigChangeRequest request = ConfigChangeRequestSupportHelper.get(ctx).createConfigChangeRequest(ctx, newBean, operation.getIndex());
        request.setTypeOfUpdate(operation.getIndex());
        
        final Map<Object, IndividualUpdate> updateRequest = new HashMap<Object, IndividualUpdate>();
        
        XInfo xinfo = (XInfo) XBeans.getInstanceOf(ctx, newBean, XInfo.class);
        List<PropertyInfo> properties = xinfo.getProperties(ctx);
        for (PropertyInfo prop : properties)
        {
            if (!prop.isMobile() || !prop.isPersistent())
            {
                continue;
            }
            
            final Object oldValue;
            if (oldBean != null
                    && prop.getBeanClass().isAssignableFrom(oldBean.getClass()))
            {
                oldValue = prop.get(oldBean); 
            }
            else
            {
                oldValue = NULL_PLACEHOLDER;
            }
            
            final Object newValue;
            if (newBean != null
                    && prop.getBeanClass().isAssignableFrom(newBean.getClass()))
            {
                newValue = prop.get(newBean);
            }
            else
            {
                newValue = NULL_PLACEHOLDER;
            }
            addUpdateToMap(ctx, updateRequest, prop, true, oldValue, newValue);
        }
        
        if(newBean instanceof SpidAware)
        {
        	request.setSpid(((SpidAware) newBean).getSpid());
        }
        
        request.setUpdateRequest(updateRequest);
        
        return request;
    }

    
    protected ConfigChangeRequest createConfigChangeRequest(
            Context ctx, 
            final PropertyInfo prop,
            Object oldValue,
            Object newBean)
    {
        if (!prop.isMobile() || !prop.isPersistent())
        {
            return null;
        }
        ConfigChangeRequest request = ConfigChangeRequestSupportHelper.get(ctx).createConfigChangeRequest(ctx, newBean, DEFAULT_NULL_HOME_OPERATION);
        request.setTypeOfUpdate(DEFAULT_NULL_HOME_OPERATION);
        
        Map<Object, IndividualUpdate> updates = new HashMap<Object, IndividualUpdate>();
        Object newValue = prop.get(newBean);
        addUpdateToMap(ctx, updates, prop, true, oldValue, newValue);
        request.setUpdateRequest(updates);
        
        if(newBean instanceof SpidAware)
        {
        	request.setSpid(((SpidAware) newBean).getSpid());
        }
        
        return request;
    }
    

    protected ConfigChangeRequest createConfigChangeRequest(Context ctx, Object oldObj, Object newObj)
    {
        ConfigChangeRequest request = ConfigChangeRequestSupportHelper.get(ctx).createConfigChangeRequest(ctx, newObj, HomeOperationEnum.CREATE_INDEX);
        
        boolean isHomeUpdate = true;

        for (Class beanClass = newObj.getClass();
                beanClass != null;
                beanClass = beanClass.getSuperclass())
        {
            try
            {
                SharedBean sharedBean = HomeSupportHelper.get(ctx).findBean(ctx, 
                        SharedBean.class, 
                        new EQ(SharedBeanXInfo.BEAN_CLASS_NAME, beanClass.getName()));
                if (sharedBean != null)
                {
                    isHomeUpdate = sharedBean.isShareHome();
                    break;
                }
            }
            catch (Exception e)
            {
                if (LogSupport.isDebugEnabled(ctx))
                {
                    new DebugLogMsg(this, e.getClass().getSimpleName() + " occurred in " + this.getClass().getSimpleName() + ".createConfigChangeRequest(): " + e.getMessage(), e).log(ctx);
                }
            }
        }
        
        if (isHomeUpdate)
        {
            request.setTypeOfUpdate(HomeOperationEnum.CREATE_INDEX);
        }
        else
        {
            request.setTypeOfUpdate(DEFAULT_NULL_HOME_OPERATION);
            request.setModifiedBy(getModifiedBy(ctx));
        }
        
        getXInfosAndCreateChangeRequest(ctx, newObj, oldObj, request);
        
        if(newObj instanceof SpidAware)
        {
        	request.setSpid(((SpidAware) newObj).getSpid());
        }
        
        return request;
    }

    
    private String getModifiedBy(final Context ctx)
    {
        String modified = "system";
        User user = (User) ctx.get(Principal.class);
        if (user != null)
        {
            modified = user.getId();
        }
        return modified;
    }

    private void getXInfosAndCreateChangeRequest(Context ctx, Object newObj, Object oldObj, ConfigChangeRequest request)
    {
        final Map<Object, IndividualUpdate> updateRequest = new HashMap<Object, IndividualUpdate>();
        XInfo xinfo = (XInfo) XBeans.getInstanceOf(ctx, newObj, XInfo.class);
        List<PropertyInfo> properties = xinfo.getProperties(ctx);
        for (PropertyInfo prop : properties)
        {
            if (!prop.isMobile() || !prop.isPersistent())
            {
                continue;
            }
            
            final Object oldValue;
            if (oldObj != null && prop.getBeanClass().isAssignableFrom(oldObj.getClass()))
            {
                oldValue = prop.get(oldObj);
            }
            else
            {
                oldValue = NULL_PLACEHOLDER;
            }
            final Object newValue;
            if (newObj != null && prop.getBeanClass().isAssignableFrom(newObj.getClass()))
            {
                newValue = prop.get(newObj);
            }
            else
            {
                newValue = NULL_PLACEHOLDER;
            }
            addUpdateToMap(ctx, updateRequest, prop, false, oldValue, newValue);
        }
        request.setUpdateRequest(updateRequest);
    }


    private void addUpdateToMap(final Context ctx, final Map<Object, IndividualUpdate> updateRequest,
            final PropertyInfo prop, final boolean deltaUpdate,
            final Object oldValue, final Object newValue)
    {
        if (!deltaUpdate || !SafetyUtil.safeEquals(oldValue, newValue))
        {
            try
            {
                IndividualUpdate update = new IndividualUpdate();
                update.setFieldName(prop.getName());
                
                boolean shouldShare = true;
                boolean simpleToString = isSimpleStringConversion(prop);
                
                Object dummyBean = null;
                
                if (oldValue != NULL_PLACEHOLDER)
                {
                    String oldStringValue = null;
                    if (simpleToString)
                    {
                        oldStringValue = String.valueOf(oldValue);
                    }
                    else
                    {
                        if (dummyBean == null)
                        {
                            dummyBean = XBeans.instantiate(prop.getXInfo().getBeanClass(), ctx);
                        }
                        
                        try
                        {
                            prop.set(dummyBean, oldValue);
                            oldStringValue = prop.toString(dummyBean);
                        }
                        catch (IllegalArgumentException e)
                        {
                            if (LogSupport.isDebugEnabled(ctx))
                            {
                                new DebugLogMsg(this, 
                                        "Error setting old value of " + prop.getXInfo().getName() + "." + prop.getName()
                                        + " to value [" + oldValue
                                        + "].  Old value will not be sent in config sharing request.", e)
                                        .log(ctx);
                            }
                        }
                    }
                    update.setOldValue(oldStringValue);
                }
                if (newValue != NULL_PLACEHOLDER)
                {
                    String newStringValue = null;
                    if (simpleToString)
                    {
                        newStringValue = String.valueOf(newValue);
                    }
                    else
                    {
                        if (dummyBean == null)
                        {
                            dummyBean = XBeans.instantiate(prop.getXInfo().getBeanClass(), ctx);
                        }
                        
                        try
                        {
                            prop.set(dummyBean, newValue);
                            newStringValue = prop.toString(dummyBean);
                        }
                        catch (IllegalArgumentException e)
                        {
                            shouldShare = false;
                            new MinorLogMsg(this, 
                                    "Error setting new value of " + prop.getXInfo().getName() + "." + prop.getName()
                                    + " to value [" + newValue
                                    + "].  Property will not be sent in config sharing request.", e)
                            .log(ctx);
                        }
                    }
                    update.setNewValue(newStringValue);
                }
                
                if (shouldShare)
                {
                    updateRequest.put(update.ID(), update);
                }
            }
            catch (Exception e)
            {
                new MinorLogMsg(this, e.getClass().getSimpleName() + " occurred in " + this.getClass().getSimpleName()
                        + ".addUpdateToMap().  Property " + prop.getXInfo().getName() + "." + prop.getName()
                        + " will not be sent in config sharing request.", e)
                        .log(ctx);
            }
        }
    }

    private boolean isSimpleStringConversion(final PropertyInfo prop)
    {
        Class propertyType = prop.getType();
        
        boolean result = String.class.isAssignableFrom(propertyType);
        
        if (!result
                // Booleans are rendered as 'y' or 'n' by FW, which differs from 'true', 'false', or 'null' for Boolean.toString()
                && !Boolean.class.isAssignableFrom(propertyType))
        {
            if (!propertyType.isPrimitive())
            {
                propertyType = XBeans.getPrimativeType(propertyType);
            }
            if (propertyType != null
                    && !Boolean.TYPE.isAssignableFrom(propertyType))
            {
                result = propertyType.isPrimitive();
            }
        }
        
        return result;
    }
}
