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
package com.redknee.app.crm.web.border;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.redknee.framework.xhome.beans.XBeans;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.context.ContextAwareSupport;
import com.redknee.framework.xhome.context.ContextFactory;
import com.redknee.framework.xhome.elang.Not;
import com.redknee.framework.xhome.web.border.Border;
import com.redknee.framework.xhome.webcontrol.RequestServicer;

import com.redknee.app.crm.bean.KeyConfiguration;
import com.redknee.app.crm.bean.KeyConfigurationXInfo;
import com.redknee.app.crm.bean.KeyValueFeatureEnum;
import com.redknee.app.crm.bean.search.KeyConfigurationSearch;
import com.redknee.app.crm.bean.search.KeyConfigurationSearchXInfo;
import com.redknee.app.crm.filter.IsGenericKeyConfigurationPredicate;
import com.redknee.app.crm.support.HomeSupportHelper;
import com.redknee.app.crm.support.WebControlSupportHelper;


/**
 * Border to automatically apply a feature to the search criteria
 *
 * @author aaron.gourley@redknee.com
 * @since 8.8/9.0
 */
public class KeyConfigurationStaticFeatureBorder implements Border
{
    private final KeyValueFeatureEnum feature_;
    private final boolean hideGeneric_;

    public KeyConfigurationStaticFeatureBorder(KeyValueFeatureEnum feature)
    {
        this(feature, false);
    }
    
    public KeyConfigurationStaticFeatureBorder(KeyValueFeatureEnum feature, boolean hideGeneric)
    {
        feature_ = feature;
        hideGeneric_ = hideGeneric;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void service(final Context parentCtx, HttpServletRequest req, HttpServletResponse res, RequestServicer delegate)
            throws ServletException, IOException
    {
        Context ctx = parentCtx.createSubContext();
        
        WebControlSupportHelper.get(ctx).hideProperty(ctx, KeyConfigurationSearchXInfo.FEATURE);
        WebControlSupportHelper.get(ctx).hideProperty(ctx, KeyConfigurationXInfo.FEATURE);

        XBeans.putBeanFactory(ctx, KeyConfiguration.class, new StaticFeatureKeyConfigurationFactory(parentCtx, feature_));
        XBeans.putBeanFactory(ctx, KeyConfigurationSearch.class, new StaticFeatureKeyConfigurationSearchFactory(parentCtx, feature_));
        
        if (hideGeneric_)
        {
            ctx = HomeSupportHelper.get(ctx).getWhereContext(ctx, KeyConfiguration.class, new Not(new IsGenericKeyConfigurationPredicate()));
        }
        
        delegate.service(ctx, req, res);
    }

    private static final class StaticFeatureKeyConfigurationFactory extends ContextAwareSupport implements ContextFactory
    {
        private KeyValueFeatureEnum feature_;
        
        private StaticFeatureKeyConfigurationFactory(Context parentCtx, KeyValueFeatureEnum feature)
        {
            setContext(parentCtx);
            feature_ = feature;
        }


        public Object create(Context fCtx)
        {
            KeyConfiguration bean;
            try
            {
                // Instantiate using the parent context to avoid factory loop
                bean = (KeyConfiguration) XBeans.instantiate(KeyConfiguration.class, getContext());
            }
            catch (Exception e)
            {
                bean = new KeyConfiguration();
            }
            
            bean.setFeature(feature_);
            
            return bean;
        }
    }
    
    private static final class StaticFeatureKeyConfigurationSearchFactory extends ContextAwareSupport implements ContextFactory
    {
        private KeyValueFeatureEnum feature_;
        
        private StaticFeatureKeyConfigurationSearchFactory(Context parentCtx, KeyValueFeatureEnum feature)
        {
            setContext(parentCtx);
            feature_ = feature;
        }


        public Object create(Context fCtx)
        {
            KeyConfigurationSearch form;
            try
            {
                // Instantiate using the parent context to avoid factory loop
                form = (KeyConfigurationSearch) XBeans.instantiate(KeyConfigurationSearch.class, getContext());
            }
            catch (Exception e)
            {
                form = new KeyConfigurationSearch();
            }
            
            form.setFeature(feature_);
            
            return form;
        }
    }
}
