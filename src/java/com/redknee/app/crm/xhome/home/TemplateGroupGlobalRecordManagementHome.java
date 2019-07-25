package com.redknee.app.crm.xhome.home;

import com.redknee.framework.core.platform.CoreSupport;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.And;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.HomeInternalException;
import com.redknee.framework.xhome.home.HomeProxy;
import com.redknee.framework.xlog.log.DebugLogMsg;
import com.redknee.framework.xlog.log.LogSupport;

import com.redknee.app.crm.notification.template.NotificationTemplateGroup;
import com.redknee.app.crm.notification.template.TemplateGroupGlobalRecord;
import com.redknee.app.crm.notification.template.TemplateGroupGlobalRecordXInfo;
import com.redknee.app.crm.support.HomeSupportHelper;

/**
 * This home creates global records for local NotificationTemplateGroup records.
 *
 * @author aaron.gourley@redknee.com
 * @since 8.8/9.0
 */
public class TemplateGroupGlobalRecordManagementHome extends HomeProxy
{

    public TemplateGroupGlobalRecordManagementHome(Context ctx, Home delegate)
    {
        super(ctx, delegate);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object create(Context ctx, Object obj) throws HomeException, HomeInternalException
    {
        ensureGlobalRecordExists(ctx, obj);
        return super.create(ctx, obj);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object store(Context ctx, Object obj) throws HomeException, HomeInternalException
    {
        Object result = super.store(ctx, obj);
        ensureGlobalRecordExists(ctx, obj);
        return result;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(Context ctx, Object obj) throws HomeException, HomeInternalException
    {
        if (obj instanceof NotificationTemplateGroup)
        {
            String appName = CoreSupport.getApplication(ctx).getName();
            String name = ((NotificationTemplateGroup) obj).getName();
            int spid = ((NotificationTemplateGroup) obj).getSpid();

            And filter = new And();
            filter.add(new EQ(TemplateGroupGlobalRecordXInfo.APP_NAME, appName));
            filter.add(new EQ(TemplateGroupGlobalRecordXInfo.NAME, name));
            filter.add(new EQ(TemplateGroupGlobalRecordXInfo.SPID, spid));

            Home recordHome = HomeSupportHelper.get(ctx).getHome(ctx, TemplateGroupGlobalRecord.class);
            recordHome.removeAll(ctx, filter);
        }
        super.remove(ctx, obj);
    }


    protected void ensureGlobalRecordExists(Context ctx, Object obj)
    {
        if (obj instanceof NotificationTemplateGroup)
        {
            String appName = CoreSupport.getApplication(ctx).getName();
            String name = ((NotificationTemplateGroup) obj).getName();
            int spid = ((NotificationTemplateGroup) obj).getSpid();
            
            try
            {
                And filter = new And();
                filter.add(new EQ(TemplateGroupGlobalRecordXInfo.APP_NAME, appName));
                filter.add(new EQ(TemplateGroupGlobalRecordXInfo.NAME, name));
                filter.add(new EQ(TemplateGroupGlobalRecordXInfo.SPID, spid));
                
                TemplateGroupGlobalRecord record = HomeSupportHelper.get(ctx).findBean(ctx, TemplateGroupGlobalRecord.class, filter);
                if (record == null)
                {
                    record = new TemplateGroupGlobalRecord();
                    record.setAppName(appName);
                    record.setName(name);
                    record.setSpid(spid);
                    HomeSupportHelper.get(ctx).createBean(ctx, record);
                }
            }
            catch (HomeException e)
            {
                if (LogSupport.isDebugEnabled(ctx))
                {
                    new DebugLogMsg(this, "Error creating global notification template group record with [appName=" + appName + ",name=" + name + ",spid=" + spid + "]", e).log(ctx);
                }
            }
        }
    }
}