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

import java.util.HashMap;
import java.util.Map;

import com.redknee.framework.xhome.beans.SafetyUtil;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeChangeEvent;
import com.redknee.framework.xhome.home.HomeChangeListener;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.HomeOperationEnum;
import com.redknee.framework.xhome.home.HomeProxy;
import com.redknee.framework.xhome.home.NotifyingHome;
import com.redknee.framework.xhome.home.NotifyingHomeItem;
import com.redknee.framework.xhome.pipe.Throttle;

import com.redknee.app.crm.delivery.email.CRMEmailConfig;


/**
 * 
 *
 * @author aaron.gourley@redknee.com
 * @since 8.2
 */
public class DefaultEmailSupport implements EmailSupport
{
    private static final Map<Integer, Throttle> smtpThrottleMap_ = new HashMap<Integer, Throttle>();

    protected static EmailSupport instance_ = null;
    public static EmailSupport instance()
    {
        if (instance_ == null)
        {
            instance_ = new DefaultEmailSupport();
        }
        return instance_;
    }

    protected DefaultEmailSupport()
    {
    }
    
    /**
     * {@inheritDoc}
     */
    public Throttle getSMTPThrottle(Context ctx, int spid)
    {
        try
        {
            return getSMTPThrottle(HomeSupportHelper.get(ctx).findBean(ctx, CRMEmailConfig.class, Integer.valueOf(spid)));
        }
        catch (HomeException e)
        {
            return null;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public Throttle getSMTPThrottle(CRMEmailConfig config)
    {
        Throttle throttle = smtpThrottleMap_.get(config.getSpid());
        if (throttle == null)
        {
            throttle = new Throttle(config.getThrottleInfo());
            smtpThrottleMap_.put(config.getSpid(), throttle);
        }
        
        if (!SafetyUtil.safeEquals(config.getThrottleInfo(), throttle.getThrottleInfo()))
        {
            throttle.setThrottleInfo(config.getThrottleInfo());
        }
        
        return throttle;
    }
    
    /**
     * {@inheritDoc}
     */
    public void installEmailThrottle(Home emailConfigHome)
    {
        NotifyingHome home = null;
        if (emailConfigHome instanceof HomeProxy)
        {
            home = (NotifyingHome) ((HomeProxy)emailConfigHome).findDecorator(NotifyingHome.class);
        }
        if (home != null)
        {
            home.addHomeChangeListener(new CRMEmailConfigThrottleHomeChangeListener());
        }
    }
    
    public static class CRMEmailConfigThrottleHomeChangeListener implements HomeChangeListener
    {
        /**
         * {@inheritDoc}
         */
        public void homeChange(HomeChangeEvent evt)
        {
            if (evt.getOperation() == HomeOperationEnum.STORE
                    || evt.getOperation() == HomeOperationEnum.CREATE)
            {
                final Object src = evt.getSource();
                
                final Object newObj;
                if (src instanceof NotifyingHomeItem)
                {
                    newObj = ((NotifyingHomeItem)src).getNewObject();
                }
                else
                {
                    newObj = src;
                }
                
                if (newObj instanceof CRMEmailConfig)
                {
                    CRMEmailConfig config = (CRMEmailConfig) newObj;
                    
                    Throttle throttle = smtpThrottleMap_.get(config.getSpid());
                    if (throttle == null)
                    {
                        smtpThrottleMap_.put(config.getSpid(), new Throttle(config.getThrottleInfo()));
                    }
                    else if (!SafetyUtil.safeEquals(config.getThrottleInfo(), throttle.getThrottleInfo()))
                    {
                        throttle.setThrottleInfo(config.getThrottleInfo());
                    }
                }
            }
            else if (evt.getOperation() == HomeOperationEnum.REMOVE)
            {
                Object oldObj = null;
                
                Object src = evt.getSource();
                if (src instanceof NotifyingHomeItem)
                {
                    oldObj = ((NotifyingHomeItem)src).getOldObject();
                }
                else
                {
                    oldObj = src;
                }
                
                if (oldObj instanceof CRMEmailConfig)
                {
                    smtpThrottleMap_.remove(((CRMEmailConfig)oldObj).getSpid());
                }
            }
            else if (evt.getOperation() == HomeOperationEnum.REMOVE_ALL)
            {
                smtpThrottleMap_.clear();
            }
        }
    }
}
