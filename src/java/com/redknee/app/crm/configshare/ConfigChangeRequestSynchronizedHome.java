package com.redknee.app.crm.configshare;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.HomeInternalException;
import com.redknee.framework.xhome.home.HomeProxy;

import com.redknee.app.crm.configshare.ConfigChangeRequest;

/**
 * This home synchronizes requests to the same bean class having the same ID to prevent
 * concurrent updates related to two changes on the same bean(s).
 *
 * @author aaron.gourley@redknee.com
 * @since 8.8/9.0
 */
public class ConfigChangeRequestSynchronizedHome extends HomeProxy
{
    public ConfigChangeRequestSynchronizedHome(Context ctx, Home delegate)
    {
        super(ctx, delegate);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object create(Context ctx, Object obj) throws HomeException, HomeInternalException
    {
        ConfigChangeRequest request = (ConfigChangeRequest) obj;

        String keyString = request.getBeanClass() + "_" + request.getBeanId();
        
        Object lock; 
        synchronized (LOCK_MAP_LOCK)
        {
            WeakReference<String> lockID = lockKeyMap_.get(keyString);
            if (lockID == null)
            {
                lockID = new WeakReference<String>(keyString);
                lockKeyMap_.put(keyString, lockID);
            }

            // Hold a strong reference to the key so that it doesn't get GC'd
            String key = lockID.get();
            
            lock = lockMap_.get(key);
            if (lock == null)
            {
                lock = new Object();
                lockMap_.put(key, lock);
                if (!lockKeyMap_.containsKey(key))
                {
                    lockKeyMap_.put(key, lockID);
                }
            }
        }
        
        synchronized (lock)
        {
            return super.create(ctx, obj);
        }
    }
    
    protected final Object LOCK_MAP_LOCK = new Object();
    protected Map<String, WeakReference<String>> lockKeyMap_ = new WeakHashMap<String, WeakReference<String>>();
    protected Map<String, Object> lockMap_ = new WeakHashMap<String, Object>();
}