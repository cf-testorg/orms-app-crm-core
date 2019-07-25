package com.redknee.app.crm.xhome.home;

import java.util.Collection;

import com.redknee.framework.xhome.cluster.RMIClusteredHome;
import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.AbstractClassAwareHome;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.HomeInternalException;
import com.redknee.framework.xhome.home.HomeOperationEnum;
import com.redknee.framework.xhome.home.HomeProxy;
import com.redknee.framework.xhome.visitor.AbortVisitException;
import com.redknee.framework.xhome.visitor.Visitor;
import com.redknee.framework.xlog.log.MajorLogMsg;


/**
 * A combination of one <i>local</i> Home and a collection of (generally) <i>remote</i>
 * Homes.
 * 
 * Reads are performed on the local Home while updates are performed on all homes. No
 * role-back, store-and-forward behaviour, or transactional support is provided by this
 * class as that is the responsibility of the delgate Homes. If you require this ability
 * then use the RMIClusteredHome instead.
 * 
 * This class is thread safe. It doesn't synchronize the Home operations but it does
 * synchronize when delegating to the cluster to help prevent non-deterministic
 * interleaving of dependent operations.
 **/
public class TotalCachingHome extends HomeProxy
{

    public static final String BYPASS_CLUSTERING_CTX_KEY = "TOTAL_CLUSTERING_BYPASS_CLUSTERING";

    /** Subsequent delegates after the first. **/
    protected Home cache_;


    protected TotalCachingHome(Context ctx, Home delegate)
    {
        setContext(ctx);
        setDelegate(delegate);
    }

    public TotalCachingHome(Context ctx, Home local, Home delegate)
    {
        this(ctx, local,delegate,ctx.getBoolean(BYPASS_CLUSTERING_CTX_KEY, false));
    }
    
    public TotalCachingHome(Context ctx, Home local, Home delegate,boolean bypassClustering)
    {
        setContext(ctx);
        setDelegate(delegate);
        cache_ = local;
        initCache(ctx, local, delegate);
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
                try
                {
                    local = new RMIClusteredHome(ctx, beanClass + ".TotalCache", local);
                }
                catch (Exception e)
                {
                    new MajorLogMsg(this, "Error creating cluster up bean class for cache "
                            + local.getClass().getName() + ".  This total cache will not be clustered.", e).log(ctx);
                }
            }
            else
            {
                new MajorLogMsg(this, "Cache " + local.getClass().getName()
                        + " returned an invalid bean class.  This total cache will not be clustered.", null).log(ctx);
            }
        }
    }


    public Object create(Context ctx, Object obj) throws HomeException
    {
        Object ret = getDelegate().create(ctx, obj);
        synchronized (this)
        {
            getCacheHome().create(ctx, ret);
        }
        return ret;
    }


    public void remove(Context ctx, Object obj) throws HomeException
    {
        getDelegate().remove(ctx, obj);
        synchronized (this)
        {
            getCacheHome().remove(ctx, obj);
        }
    }


    public void removeAll(Context ctx, Object obj) throws HomeException
    {
        getDelegate().removeAll(ctx, obj);
        synchronized (this)
        {
            getCacheHome().removeAll(ctx, obj);
        }
    }


    public Object store(Context ctx, Object obj) throws HomeException
    {
        Object ret = getDelegate().store(ctx, obj);
        synchronized (this)
        {
            getCacheHome().store(ctx, ret);
        }
        return ret;
    }


    public Object find(Context ctx, Object key) throws HomeException
    {
        return getCacheHome().find(ctx, key);
    }


    @Override
    public Collection select(Context ctx, Object where) throws HomeException, UnsupportedOperationException
    {
        return getCacheHome().select(ctx, where);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Visitor forEach(Context ctx, Visitor visitor, Object where) throws HomeException, HomeInternalException
    {
        return getCacheHome().forEach(ctx, visitor, where);
    }


    public Object cmd(Context ctx, Object command) throws HomeException, HomeInternalException
    {
        if (command instanceof TotalCachingHomeCmd)
        {
            TotalCachingHomeCmd cmd = (TotalCachingHomeCmd) command;
            switch (cmd.getOperation().getIndex())
            {
            case HomeOperationEnum.CREATE_INDEX:
                getCacheHome().create(ctx, cmd.getBean());
                break;
            case HomeOperationEnum.STORE_INDEX:
                getCacheHome().store(ctx, cmd.getBean());
                break;
            case HomeOperationEnum.REMOVE_INDEX:
                getCacheHome().remove(ctx, cmd.getBean());
                break;
            case HomeOperationEnum.REMOVE_ALL_INDEX:
                getCacheHome().removeAll(ctx, cmd.getBean());
                break;
            case HomeOperationEnum.FIND_INDEX:
                getCacheHome().find(ctx, cmd.getBean());
                break;
            case HomeOperationEnum.SELECT_INDEX:
                getCacheHome().select(ctx, cmd.getBean());
                break;
            default:
                throw new HomeException("Operation not supported");
            }
            return Boolean.TRUE;
        }
        return getDelegate(ctx).cmd(ctx, command);
    }


    public Home getCacheHome()
    {
        return cache_;
    }


    protected void initCache(Context ctx, Home local, Home delegate)
    {
        try
        {
            final Home destHome = local;
            delegate.forEach(ctx, new Visitor()
            {

                public void visit(Context ctx, Object obj) throws AgentException, AbortVisitException
                {
                    try
                    {
                        destHome.create(ctx, obj);
                    }
                    catch (HomeException e)
                    {
                        new MajorLogMsg(this, "failed to load bean to cache", e).log(ctx);
                    }
                }
            });
        }
        catch (UnsupportedOperationException ex)
        {
            new MajorLogMsg(this, "failed to load beans to cache", ex).log(ctx);
        }
        catch (HomeException e)
        {
            new MajorLogMsg(this, "failed to load beans to cache", e).log(ctx);
        }
    }

    public static final String PURGE_CACHE = "TotalCachingHome.purgeCache";
}
