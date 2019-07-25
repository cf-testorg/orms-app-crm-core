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
package com.redknee.app.crm.core.agent;

import com.redknee.framework.core.platform.CoreSupport;
import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.context.ContextAgent;
import java.util.Collection;
import com.redknee.app.crm.bean.CurrencyPrecision;
import com.redknee.app.crm.bean.TaxationMethodInfo;
import com.redknee.app.crm.bean.TaxationMethodInfoHome;
import com.redknee.app.crm.client.AppOcgClient;
import com.redknee.app.crm.collection.MscPrefixToProvinceMapper;
import com.redknee.app.crm.collection.MsisdnPrefixToProvinceMapper;
import com.redknee.app.crm.collection.LocationZoneToProvinceMapper;
import com.redknee.app.crm.smsc.SmscConnectionMgr;
import com.redknee.app.crm.support.ConfigChangeRequestSupport;
import com.redknee.app.crm.support.ConfigChangeRequestSupportHelper;
import com.redknee.app.crm.support.SystemStatusSupportHelper;
import com.redknee.app.crm.xdb.XDBExternalService;
import com.redknee.app.urcs.client.agent.ConnectionStatusHub;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xlog.log.MajorLogMsg;
import com.redknee.util.partitioning.xhome.AbstractConfigurablePartitionHome;
import com.redknee.util.snippet.log.Logger;


/**
 * 
 *
 * @author aaron.gourley@redknee.com
 * @since 
 */
public class ServiceInstall extends CoreSupport implements ContextAgent
{
    public static final Class<AppOcgClient> APP_OCG_CLIENT_KEY = AppOcgClient.class;

    /**
     * {@inheritDoc}
     */
    public void execute(Context ctx) throws AgentException
    {
        // Register default XDB External Service
        XDBExternalService xdbExternalService = new XDBExternalService(ctx);
        ctx.put(XDBExternalService.class, xdbExternalService);
        SystemStatusSupportHelper.get(ctx).registerServiceStatus(xdbExternalService);

        // We don't want finds to fail actual call flows due to connection errors for config sharing feature.
        // This is only because we don't expect find() to ever find anything anyways, since we aren't storing anything...
        AbstractConfigurablePartitionHome.disableHomeInternalExceptionOnFindErrors(ctx);

        // Install a required URCS dependency for AbstractCrmClient use (i.e. RemoteServiceStatusImpl)
        ctx.put(ConnectionStatusHub.class, new ConnectionStatusHub());

        CurrencyPrecision precision = (CurrencyPrecision) ctx.get(CurrencyPrecision.class);
        ConfigChangeRequestSupport configSharingSupport = ConfigChangeRequestSupportHelper.get(ctx);
        configSharingSupport.registerBeanForConfigSharing(ctx, precision);
        
        ctx.put(SmscConnectionMgr.class, new SmscConnectionMgr(ctx));

        // Install the BM Homes and service, move from ModelProductBundleManager
        new com.redknee.app.crm.bundle.driver.Install().execute(ctx);

        installURCSClients(ctx);
        
        ctx.put(MscPrefixToProvinceMapper.class, new MscPrefixToProvinceMapper(ctx));
        ctx.put(MsisdnPrefixToProvinceMapper.class, new MsisdnPrefixToProvinceMapper(ctx));
        ctx.put(LocationZoneToProvinceMapper.class, new LocationZoneToProvinceMapper(ctx));
        
        installTaxationImplementations(ctx);
    }

    /**
     * @param ctx
     */
    private void installURCSClients(Context ctx)
    {
        Logger.info(ctx, this, "Installing core URCS clients...", null);

        final AppOcgClient clientAppOcg = new AppOcgClient(ctx);
        ctx.put(APP_OCG_CLIENT_KEY, clientAppOcg);
    }
    
    private void installTaxationImplementations(Context ctx)
    {
        try
        {
            Home home = (Home)ctx.get(TaxationMethodInfoHome.class);
            Collection<TaxationMethodInfo> list = home.selectAll();
            for (TaxationMethodInfo taxationMethodInfo : list)
            {
                ctx.put(taxationMethodInfo.getAdapter(), Class.forName(taxationMethodInfo.getAdapter()).getConstructor(new Class[]
                        {}).newInstance(new Object[]{}));
            }

        }
        catch(HomeException he)
        {
            new MajorLogMsg(this, "Failed to install one or more TaxationMethodInfo." , he).log(ctx);
        }
        catch (Throwable e) 
        {
            new MajorLogMsg(this, "Failed to install one or more TaxationMethodInfo." , e).log(ctx);
        }
    }

}
