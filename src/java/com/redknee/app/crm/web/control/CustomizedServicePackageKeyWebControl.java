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
package com.redknee.app.crm.web.control;

import java.io.PrintWriter;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.And;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.support.IdentitySupport;
import com.redknee.framework.xhome.webcontrol.AbstractKeyWebControl;
import com.redknee.framework.xhome.webcontrol.AbstractWebControl;
import com.redknee.framework.xhome.webcontrol.WebControl;

import com.redknee.app.crm.bean.PricePlan;
import com.redknee.app.crm.bean.ServicePackageFee;
import com.redknee.app.crm.bean.ServicePackageHome;
import com.redknee.app.crm.bean.ServicePackageXInfo;
import com.redknee.app.crm.bean.core.ServicePackage;
import com.redknee.app.crm.support.HomeSupportHelper;

/**
 * @author jke
 */
public class CustomizedServicePackageKeyWebControl  extends AbstractKeyWebControl
{

        private AbstractKeyWebControl delegate_ ;
        
        public CustomizedServicePackageKeyWebControl(AbstractKeyWebControl delegate)
        {
            setDelegate(delegate);
        }
        
        public CustomizedServicePackageKeyWebControl(AbstractKeyWebControl delegate, final boolean autoPreview)
        {
        	super(autoPreview);
            setDelegate(delegate);
        }

        @Override
        public void toWeb(Context ctx, PrintWriter out, String name, final Object obj)
        {
            super.toWeb(wrapContext(ctx), out, name, obj);
        }

        /**
         * @return Returns the delegate.
         */
        @Override
        public WebControl getDelegate()
        {
            return delegate_;
        }
        /**
         * @param delegate The delegate to set.
         */
        public void setDelegate(AbstractKeyWebControl delegate)
        {
            this.delegate_ = delegate;
        }

        /* (non-Javadoc)
         * @see com.redknee.framework.xhome.webcontrol.AbstractKeyWebControl#getDesc(com.redknee.framework.xhome.context.Context, java.lang.Object)
         */
        @Override
        public String getDesc(Context ctx, Object bean)
        {
            return delegate_.getDesc(ctx,bean);
        }

        /* (non-Javadoc)
         * @see com.redknee.framework.xhome.webcontrol.AbstractKeyWebControl#getIdentitySupport()
         */
        @Override
        public IdentitySupport getIdentitySupport()
        {
            return delegate_.getIdentitySupport();
        }

        /* (non-Javadoc)
         * @see com.redknee.framework.xhome.webcontrol.AbstractKeyWebControl#getHomeKey()
         */
        @Override
        public Object getHomeKey()
        {
            return delegate_.getHomeKey();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Context wrapContext(final Context ctx)
        {
            final ServicePackageFee spf = (ServicePackageFee)ctx.get(AbstractWebControl.BEAN);
            
            PricePlan pp = (PricePlan) ctx.get(PricePlan.class);
            
            if (pp == null)
            {
                return ctx;
            }

            final And filter = new And();
            filter.add(new EQ(ServicePackageXInfo.TYPE, pp.getPricePlanType()));
            filter.add(new EQ(ServicePackageXInfo.SPID, pp.getSpid()));
            
            return HomeSupportHelper.get(ctx).getWhereContext(ctx, ServicePackage.class, filter);
        }
}
