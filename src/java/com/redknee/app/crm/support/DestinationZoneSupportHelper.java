package com.redknee.app.crm.support;

import com.redknee.framework.xhome.context.Context;


public class DestinationZoneSupportHelper extends SupportHelper
{
    private DestinationZoneSupportHelper()
    {
    }
    
    /**
     * @deprecated Use contextualized version of method
     */
    public static DestinationZoneSupport get()
    {
        return get(DestinationZoneSupport.class, DefaultDestinationZoneSupport.instance());
    }
    
    public static DestinationZoneSupport get(Context ctx)
    {
        DestinationZoneSupport instance = get(ctx, DestinationZoneSupport.class, DefaultDestinationZoneSupport.instance());
        return instance;
    }
    
    public static DestinationZoneSupport set(Context ctx, DestinationZoneSupport instance)
    {
        return register(ctx, DestinationZoneSupport.class, instance);
    }
}
