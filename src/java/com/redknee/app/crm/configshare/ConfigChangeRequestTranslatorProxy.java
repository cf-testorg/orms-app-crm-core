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

import java.util.Collection;

import com.redknee.framework.xhome.beans.AbstractBean;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.context.ContextAwareSupport;


/**
 * 
 *
 * @author aaron.gourley@redknee.com
 * @since 
 */
public class ConfigChangeRequestTranslatorProxy extends ContextAwareSupport implements ConfigChangeRequestTranslator
{
    public ConfigChangeRequestTranslatorProxy(ConfigChangeRequestTranslator delegate)
    {
        this(null, delegate);
    }
    
    public ConfigChangeRequestTranslatorProxy(Context ctx, ConfigChangeRequestTranslator delegate)
    {
        setContext(ctx);
        setDelegate(delegate);
    }
    
    /**
     * {@inheritDoc}
     */
    public Collection<? extends AbstractBean> translate(Context ctx, ConfigChangeRequest request) throws ConfigChangeRequestTranslationException
    {
        return getDelegate(ctx).translate(ctx, request);
    }


    /** Inserts a ConfigChangeRequestTranslatorProxy before the current delegate. **/
    public void addProxy(Context ctx, ConfigChangeRequestTranslatorProxy proxy)
    {
        proxy.setDelegate(getDelegate(ctx));

        setDelegate(proxy);  
    }


    /** Inserts a ConfigChangeRequestTranslatorProxy before the last non ConfigChangeRequestTranslatorProxy. **/
    public void appendProxy(Context ctx, ConfigChangeRequestTranslatorProxy proxy)
    {
        if ( getDelegate(ctx) instanceof ConfigChangeRequestTranslatorProxy )
        {
            ConfigChangeRequestTranslatorProxy delegate = (ConfigChangeRequestTranslatorProxy) getDelegate(ctx);

            delegate.appendProxy(ctx, proxy);
        }
        else
        {
            addProxy(ctx, proxy);  
        }
    }


    /** Find the first instance of cls in the Decorator chain. **/ 
    public ConfigChangeRequestTranslator findDecorator(Class cls)
    {
        if ( cls.isInstance(this) )
        {
            return this;
        }
        ConfigChangeRequestTranslator delegate = getDelegate(getContext());
        if ( delegate instanceof ConfigChangeRequestTranslatorProxy )
        {
            return ((ConfigChangeRequestTranslatorProxy) delegate).findDecorator(cls);  
        }

        return cls.isInstance(delegate) ? delegate : null;
    }


    public boolean hasDecorator(Class cls)
    {
        return findDecorator(cls) != null;
    }

    public void setDelegate(ConfigChangeRequestTranslator delegate)
    {
        delegate_ = delegate;
    }

    public ConfigChangeRequestTranslator getDelegate(Context ctx)
    {
        return delegate_;
    }

    protected ConfigChangeRequestTranslator delegate_;
}
