package com.redknee.app.crm.notification;

import com.redknee.framework.core.platform.CoreSupport;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.HomeInternalException;
import com.redknee.framework.xhome.home.HomeProxy;

/**
 * This proxy sets the application name in scheduled notification beans.
 *
 * @author aaron.gourley@redknee.com
 * @since 8.8/9.0
 */
public class AppNameSettingScheduledNotificationHome extends HomeProxy
{
    public AppNameSettingScheduledNotificationHome(Home delegate)
    {
        super(delegate);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object create(Context ctx, Object obj) throws HomeException, HomeInternalException
    {
        if (obj instanceof ScheduledNotification)
        {
            ((ScheduledNotification) obj).setAppName(CoreSupport.getApplication(ctx).getName());
        }
        return super.create(ctx, obj);
    }
}