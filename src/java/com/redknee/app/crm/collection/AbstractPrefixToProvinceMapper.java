package com.redknee.app.crm.collection;

import java.util.Collection;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.redknee.app.crm.bean.ProvincePrefix;
import com.redknee.app.crm.bean.ProvincePrefixHome;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeChangeEvent;
import com.redknee.framework.xhome.home.HomeChangeListener;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.HomeOperationEnum;
import com.redknee.framework.xhome.util.collection.trie.AsciiPrefixMap;
import com.redknee.framework.xhome.util.collection.trie.PrefixMap;
import com.redknee.framework.xlog.log.LogSupport;


abstract public class AbstractPrefixToProvinceMapper implements PrefixToProvinceMapper, HomeChangeListener
{

    private PrefixMap cache_ = new PrefixMap();
    
    private boolean needUpdatingCache = true;
    
    private final ReentrantReadWriteLock lock_ = new ReentrantReadWriteLock();

    Context ctx_;
    
    public void setContext(Context ctx)
    {
        ctx_ = ctx;
    }
    
    public AbstractPrefixToProvinceMapper(Context ctx)
    {
        validate(ctx);
        setContext(ctx);
        registerForHomeNotifications(ctx);
    }

    abstract protected void registerForHomeNotifications(Context ctx);
    
    @Override
    public String getProvince(Context ctx, String identifier)
    {
        ProvincePrefix province = (ProvincePrefix) cache_.lookup(identifier);
        if (province != null)
        {
            return province.getCode();
        }
        return "";
    }


    @Override
    public void homeChange(HomeChangeEvent evt)
    {
        HomeOperationEnum op = evt.getOperation();
        if (op == HomeOperationEnum.CREATE || op == HomeOperationEnum.STORE || op == HomeOperationEnum.REMOVE)
        {
            invalidate();
        }
    }


    /**
     * Deletes the PrefixMap because it is no longer valid.
     */
    private void invalidate()
    {
        lock_.writeLock().lock();
        {
            needUpdatingCache = true;
        }
        lock_.writeLock().unlock();
    }


    public PrefixMap validate(Context ctx)
    {
        lock_.readLock().lock();
        try
        {
            if (!needUpdatingCache)
            {
                return cache_;
            }
        }
        finally
        {
            lock_.readLock().unlock();
        }
        lock_.writeLock().lock();
        
        try
        {
            if (!needUpdatingCache)
            {
                return cache_;
            }
            cache_ = new AsciiPrefixMap();
            
            try
            {
                Home provinceHome = (Home) ctx.get(ProvincePrefixHome.class);
                for (ProvincePrefix province : (Collection<ProvincePrefix>) provinceHome.selectAll(ctx))
                {
                    populatePrefixMap(ctx, province, cache_);
                }
            }
            catch (HomeException e)
            {
                LogSupport.minor(ctx, this, "INTERNAL ERROR", e);
            }
            needUpdatingCache = false;
            return cache_;
        }
        finally
        {
            lock_.writeLock().unlock();
        }
    }

    abstract protected void populatePrefixMap(Context ctx, ProvincePrefix province, PrefixMap prefixMap);
    
}