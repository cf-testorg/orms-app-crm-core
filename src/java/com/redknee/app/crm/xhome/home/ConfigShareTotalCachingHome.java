package com.redknee.app.crm.xhome.home;

import com.redknee.app.crm.configshare.ConfigSharingHome;
import com.redknee.app.crm.support.ConfigChangeRequestSupportHelper;
import com.redknee.framework.xhome.cluster.RMIClusteredHome;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.AbstractClassAwareHome;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.HomeInternalException;
import com.redknee.framework.xhome.home.HomeProxy;
import com.redknee.framework.xhome.visitor.Visitor;
import com.redknee.framework.xlog.log.MajorLogMsg;


/**
 * A combination of one <i>local</i> Home and a collection of (generally) <i>remote</i>
 * Homes.
 * 
 * Reads are performed on the local Home while updates are performed on all homes. Updates
 * can be clustered and config sharing used to push changes to other applications that are
 * using this home. This is to ensure that no local caches become stale.
 * 
 * This class is thread safe. It doesn't synchronize the Home operations but it does
 * synchronize when delegating to the cluster to help prevent non-deterministic
 * interleaving of dependent operations.
 **/
public class ConfigShareTotalCachingHome extends TotalCachingHome
{

    public ConfigShareTotalCachingHome(Context ctx, Home local, Home delegate)
    {
        this(ctx, local, delegate, ctx.getBoolean(BYPASS_CLUSTERING_CTX_KEY, false));
    }


    public ConfigShareTotalCachingHome(Context ctx, Home local, Home delegate, boolean bypassClustering)
    {
        super(ctx, delegate);
        initConfigShareCache(ctx, local, delegate);
        if (!bypassClustering)
        {
            Object beanClass = null;
            try
            {
                beanClass = local.cmd(AbstractClassAwareHome.CLASS_CMD);
                if (!(beanClass instanceof Class))
                {
                    beanClass = delegate.cmd(AbstractClassAwareHome.CLASS_CMD);
                }
            }
            catch (HomeException e)
            {
                new MajorLogMsg(this, "Error looking up bean class for cache " + local.getClass().getName()
                        + ".  This total cache will not be clustered.", e).log(ctx);
            }
            if (beanClass instanceof Class)
            {
                // Must cluster the cache because it is updated via config sharing
                try
                {
                    local = new RMIClusteredHome(ctx, beanClass + ".TotalCache", local);
                }
                catch (Exception e)
                {
                    new MajorLogMsg(this, "Error creating cluster up bean class for cache "
                            + local.getClass().getName() + ".  This total cache will not be clustered.", e).log(ctx);
                }
                local = ConfigChangeRequestSupportHelper.get(ctx).registerHomeForConfigSharing(ctx, local,
                        (Class) beanClass);
            }
            else
            {
                new MajorLogMsg(this, "Cache " + local.getClass().getName()
                        + " returned an invalid bean class.  This total cache will not be clustered.", null).log(ctx);
            }
        }
        cache_ = local;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Visitor forEach(Context ctx, Visitor visitor, Object where) throws HomeException, HomeInternalException
    {
        return getCacheHome().forEach(ctx, visitor, where);
    }


    public Home removeFirstCacheHomeDecorator()
    {
        if (cache_ instanceof HomeProxy)
        {
            cache_ = ((HomeProxy) cache_).getDelegate();
        }
        return cache_;
    }


    private void initConfigShareCache(Context ctx, Home local, Home delegate)
    {
        Home configShareFreeCache = local;
        try
        {
            if (configShareFreeCache instanceof HomeProxy)
            {
                Home configShareHome = ((HomeProxy) configShareFreeCache).findDecorator(ConfigSharingHome.class);
                if (configShareHome instanceof ConfigSharingHome)
                {
                    configShareFreeCache = ((ConfigSharingHome) configShareHome).getDelegate(ctx);
                }
            }
            super.initCache(ctx, local, delegate);
        }
        catch (UnsupportedOperationException ex)
        {
            new MajorLogMsg(this, "failed to load beans to cache", ex).log(ctx);
        }
    }
}
