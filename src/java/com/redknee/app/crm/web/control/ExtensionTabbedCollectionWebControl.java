package com.redknee.app.crm.web.control;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.True;
import com.redknee.framework.xhome.webcontrol.WebControl;

import com.redknee.app.crm.extension.ExtensionHolder;
import com.redknee.app.crm.extension.FinalExtension;
import com.redknee.util.snippet.webcontrol.TabbedCollectionWebControl;


public class ExtensionTabbedCollectionWebControl extends TabbedCollectionWebControl
{
    public ExtensionTabbedCollectionWebControl(Class beanType)
    {
        super(beanType);
        setAllowListChangePredicate(True.instance());
    }

    public ExtensionTabbedCollectionWebControl(Context ctx, Class beanType)
    {
        super(ctx, beanType);
        setAllowListChangePredicate(True.instance());
    }

    public ExtensionTabbedCollectionWebControl(WebControl delegate, Class beanType)
    {
        super(delegate, beanType);
        setAllowListChangePredicate(True.instance());
    }

    public ExtensionTabbedCollectionWebControl(WebControl delegate)
    {
        super(delegate);
        setAllowListChangePredicate(True.instance());
    }
    @Override
    public boolean isEnabled(Context ctx, boolean isNew, Object bean)
    {
        boolean result = true;
        if (!isNew
                && bean instanceof ExtensionHolder)
        {
            ExtensionHolder holder = (ExtensionHolder) bean;
            if (holder.getExtension() instanceof FinalExtension)
            {
                return false;
            }
        }
        return result;
    }

    
}
