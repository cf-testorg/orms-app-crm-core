package com.redknee.app.crm.support;

import com.redknee.app.crm.bean.ServiceTypeEnum;
import com.redknee.app.crm.bean.service.ExternalAppMapping;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.context.ContextSupport;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;


public class ExternalAppMappingSupportHelper extends SupportHelper
{
    private ExternalAppMappingSupportHelper()
    {
    }
    
    /**
     * @deprecated Use contextualized version of method
     */
    public static ExternalAppMappingSupport get()
    {
        return get(ExternalAppMappingSupport.class, DefaultExternalAppMappingSupport.instance());
    }
    
    public static ExternalAppMappingSupport get(Context ctx)
    {
        ExternalAppMappingSupport instance = get(ctx, ExternalAppMappingSupport.class, DefaultExternalAppMappingSupport.instance());
        return instance;
    }
    
    public static ExternalAppMappingSupport set(Context ctx, ExternalAppMappingSupport instance)
    {
        return register(ctx, ExternalAppMappingSupport.class, instance);
    }
}
