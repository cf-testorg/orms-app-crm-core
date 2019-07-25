package com.redknee.app.crm.home.province;

import java.io.IOException;
import java.rmi.RemoteException;

import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.AuditJournalHome;
import com.redknee.framework.xhome.home.CachingHome;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.NotifyingHome;
import com.redknee.framework.xhome.home.SortingHome;

import com.redknee.app.crm.bean.IdentifierEnum;
import com.redknee.app.crm.bean.ProvincePrefix;
import com.redknee.app.crm.bean.ProvincePrefixTransientHome;
import com.redknee.app.crm.collection.AbstractPrefixToProvinceMapper;
import com.redknee.app.crm.collection.LocationZoneToProvinceMapper;
import com.redknee.app.crm.collection.MscPrefixToProvinceMapper;
import com.redknee.app.crm.collection.MsisdnPrefixToProvinceMapper;
import com.redknee.app.crm.home.PipelineFactory;
import com.redknee.app.crm.sequenceId.IdentifierSettingHome;
import com.redknee.app.crm.support.StorageSupportHelper;


public class ProvincePrefixHomePipelineFactory implements PipelineFactory
{

    @Override
    public Home createPipeline(Context ctx, Context serverCtx) throws RemoteException, HomeException, IOException,
            AgentException
    {
        Home home = StorageSupportHelper.get(ctx).createHome(ctx, ProvincePrefix.class, "PROVINCE");
        home=new CachingHome(ctx, ProvincePrefix.class, new ProvincePrefixTransientHome(ctx), home);
        home = new ProvinceCodeDuplicateCheckHome(ctx, home);
        home = new ProvincePrefixCacheUpdateHome(ctx, home)
        {

            AbstractPrefixToProvinceMapper getMsisdnProvinceMapper(Context ctx)
            {
                return (MsisdnPrefixToProvinceMapper) ctx.get(MsisdnPrefixToProvinceMapper.class);
            }

            AbstractPrefixToProvinceMapper getMscProvinceMapper(Context ctx)
            {
                return (MscPrefixToProvinceMapper) ctx.get(MscPrefixToProvinceMapper.class);
            }

            AbstractPrefixToProvinceMapper getLocationZoneProvinceMapper(Context ctx)
            {
                return (LocationZoneToProvinceMapper) ctx.get(LocationZoneToProvinceMapper.class);
            }
 
        };
        
        home=new SortingHome(ctx, home);
        home=new NotifyingHome(home);
        home=new AuditJournalHome(ctx, home);
        home=new RemoveProvinceValidatorHome(home);
        home=new IdentifierSettingHome(ctx, home, IdentifierEnum.PROVINCE_PREFIX_ID, null);

        return home;
    }
}
