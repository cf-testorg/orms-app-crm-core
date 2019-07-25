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

package com.redknee.app.crm.bundle.driver;

import com.redknee.app.crm.client.ConnectionStatus;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.context.ContextSupport;
import com.redknee.framework.xhome.home.CurriedHome;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeProxy;
import com.redknee.product.bundle.manager.api.BundleService;
import com.redknee.product.bundle.manager.api.BundleServiceProxy;

/**
 * Bundle Manager Driver Proxy.
 *
 * @author kevin.greer@redknee.com
 */
public class BMDriverProxy extends AbstractBMDriverProxy
{

    protected transient Context context_ = new ContextSupport();

    public BMDriverProxy()
    {
    }

    /**
     * {@inheritDoc}
     */
    public void setDelegate(final BMDriver delegate)
    {
        super.setDelegate(delegate);

        delegate.setContext(getContext());
    }

    /**
     * {@inheritDoc}
     */
    public Context getContext()
    {
        return context_;
    }

    /**
     * {@inheritDoc}
     */
    public void setContext(final Context context)
    {
        context_ = context;

        try
        {
            getDelegate().setContext(context);
        }
        catch (NullPointerException e)
        {
        }
    }

    // Homes

    // I return the Homes as HomeProxies which take their delegate
    // from the BMDriverProxy so that the Homes are always up-to-date
    // and the BMDriver can be updated dynamically.

    /**
     * {@inheritDoc}
     */
    public Home bundleProfileApiHome()
    {
        return new HomeProxy()
        {
            public Home getDelegate(final Context ctx)
            {
                return new CurriedHome(ctx, (BMDriverProxy.this).getDelegate().bundleProfileApiHome());
            }

            public String toString()
            {
                return getDelegate(null).toString();
            }
        };
    }

    /**
     * {@inheritDoc}
     */
    public Home bundleCategoryApiHome()
    {
        return new HomeProxy()
        {
            public Home getDelegate(final Context ctx)
            {
                return new CurriedHome(ctx, (Home) (BMDriverProxy.this).getDelegate().bundleCategoryApiHome());
            }

            public String toString()
            {
                return getDelegate(null).toString();
            }
        };
    }

    /**
     * {@inheritDoc}
     */
    public Home bundleProfileApiHomev21()
    {
        return new HomeProxy()
        {
            public Home getDelegate(final Context ctx)
            {
                return new CurriedHome(ctx, (BMDriverProxy.this).getDelegate().bundleProfileApiHomev21());
            }

            public String toString()
            {
                return getDelegate(null).toString();
            }
        };
    }

    /**
     * {@inheritDoc}
     */
    public Home bundleCategoryApiHomev21()
    {
        return new HomeProxy()
        {
            public Home getDelegate(final Context ctx)
            {
                return new CurriedHome(ctx, (Home) (BMDriverProxy.this).getDelegate().bundleCategoryApiHomev21());
            }

            public String toString()
            {
                return getDelegate(null).toString();
            }
        };
    }

    /**
     * {@inheritDoc}
     */
    public Home subscriberBucketApiHome()
    {
        return new HomeProxy()
        {
            public Home getDelegate(final Context ctx)
            {
                return new CurriedHome(ctx, (Home) (BMDriverProxy.this).getDelegate().subscriberBucketApiHome());
            }

            public String toString()
            {
                return getDelegate(null).toString();
            }
        };
    }

    /**
     * {@inheritDoc}
     */
    public Home subscriberBucketRetrievalApiHome()
    {
        return new HomeProxy()
        {
            public Home getDelegate(final Context ctx)
            {
                return new CurriedHome(ctx,
                        (Home) (BMDriverProxy.this).getDelegate().subscriberBucketRetrievalApiHome());
            }

            public String toString()
            {
                return getDelegate(null).toString();
            }
        };
    }

    /**
     * {@inheritDoc}
     */
    public Home balanceApplicationHome()
    {
        return new HomeProxy()
        {
            public Home getDelegate(final Context ctx)
            {
                return new CurriedHome(ctx, (Home) (BMDriverProxy.this).getDelegate().balanceApplicationHome());
            }

            public String toString()
            {
                return getDelegate(null).toString();
            }
        };
    }


    /**
     * {@inheritDoc}
     */
    public Home subscriberBucketApiHomev21()
    {
        return new HomeProxy()
        {
            public Home getDelegate(final Context ctx)
            {
                return new CurriedHome(ctx, (Home) (BMDriverProxy.this).getDelegate().subscriberBucketApiHomev21());
            }

            public String toString()
            {
                return getDelegate(null).toString();
            }
        };
    }

    /**
     * {@inheritDoc}
     */
    public Home subscriberBucketRetrievalApiHomev21()
    {
        return new HomeProxy()
        {
            public Home getDelegate(final Context ctx)
            {
                return new CurriedHome(ctx,
                        (Home) (BMDriverProxy.this).getDelegate().subscriberBucketRetrievalApiHomev21());
            }

            public String toString()
            {
                return getDelegate(null).toString();
            }
        };
    }

    /**
     * add subscriberBucketRetrievalApiHomev26 to reflect the changes
     * @author karen lin
     */
    public Home subscriberBucketRetrievalApiHomev26(){
    	 return new HomeProxy()
         {
             public Home getDelegate(final Context ctx)
             {
                 return new CurriedHome(ctx,
                         (Home) (BMDriverProxy.this).getDelegate().subscriberBucketRetrievalApiHomev26());
             }

             public String toString()
             {
                 return getDelegate(null).toString();
             }
         };
    }


    // Services

    /**
     * {@inheritDoc}
     */
    public BundleService bundleService()
    {
        return new BundleServiceProxy()
        {
            public BundleService getDelegate()
            {
                return (BMDriverProxy.this).getDelegate().bundleService();
            }
        };
    }

    /**
     * {@inheritDoc}
     */

    /**
     * {@inheritDoc}
     */
    public com.redknee.product.bundle.manager.api.v21.BundleService bundleServicev21()
    {
        return new com.redknee.product.bundle.manager.api.v21.BundleServiceProxy()
        {
            public com.redknee.product.bundle.manager.api.v21.BundleService getDelegate()
            {
                return (BMDriverProxy.this).getDelegate().bundleServicev21();
            }
        };
    }


    /**
     * {@inheritDoc}
     */
    public String toString()
    {
        return getClass().getName() + "(" + getDelegate() + ")";
    }

    public void injectBundleManagerHomes(Context ctx)
    {
        getDelegate().injectBundleManagerHomes(ctx);
	}

    
    /**
     * @{inheritDoc}
     */
     public void install(Context ctx)
     {
         getDelegate().install(ctx);
     }

    /**
     * @{inheritDoc}
     */
     public void uninstall(Context ctx)
     {
         getDelegate().uninstall(ctx);
     }

    /* (non-Javadoc)
     * @see com.redknee.app.crm.client.ExternalService#getServiceDescription()
     */
    public String getDescription()
    {
        return getDelegate().getDescription();
    }

    /* (non-Javadoc)
     * @see com.redknee.app.crm.client.ExternalService#getServiceName()
     */
    public String getName()
    {
        return getDelegate().getName();
    }

    /* (non-Javadoc)
     * @see com.redknee.app.crm.client.ExternalService#isServiceAlive()
     */
    public boolean isAlive()
    {
        return getDelegate().isAlive();
    }

    @Override
    public ConnectionStatus[] getConnectionStatus()
    {
        return getDelegate().getConnectionStatus();
    }

    @Override
    public String getServiceStatus()
    {
        return getDelegate().getServiceStatus();
    }

}


