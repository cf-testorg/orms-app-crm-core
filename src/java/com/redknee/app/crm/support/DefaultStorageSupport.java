package com.redknee.app.crm.support;

import java.lang.reflect.Constructor;
import java.rmi.RemoteException;

import com.redknee.framework.application.RemoteApplication;
import com.redknee.framework.core.home.PMHome;
import com.redknee.framework.core.platform.CoreSupport;
import com.redknee.framework.xhome.beans.SafetyUtil;
import com.redknee.framework.xhome.beans.XBeans;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.CacheConfig;
import com.redknee.framework.xhome.home.CacheConfigHome;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.RMIHomeClient;
import com.redknee.framework.xhome.home.RMIHomeServer;
import com.redknee.framework.xhome.home.TestSerializabilityHome;
import com.redknee.framework.xhome.home.XMLHome;
import com.redknee.framework.xhome.xdb.AbstractXDBHome;
import com.redknee.framework.xhome.xdb.XDBHome;
import com.redknee.framework.xlog.log.InfoLogMsg;
import com.redknee.framework.xlog.log.MajorLogMsg;

import com.redknee.app.crm.CoreCrmConstants;
import com.redknee.app.crm.xhome.home.SetPrincipalHome;
import com.redknee.app.crm.xhome.home.SetPrincipalRmiServerHome;


/** 
 * Support for createing either XML or XDBHomes.
 */
public class DefaultStorageSupport implements StorageSupport
{
    protected static StorageSupport instance_ = null;
    public static StorageSupport instance()
    {
        if (instance_ == null)
        {
            instance_ = new DefaultStorageSupport();
        }
        return instance_;
    }

    protected DefaultStorageSupport()
    {
    }

    /**
     * {@inheritDoc}
     */
    public Home createHome(Context ctx, Class cls, String tableName)
    {
        return createHome(ctx, cls, tableName, true);
    }

    @Override
    public Home createHome(Context ctx, Class cls, String tableName, boolean addXDBHomeToContext)
    {
        try
        {
            new InfoLogMsg(DefaultStorageSupport.class, "Attempting to instantiate home for \"" + cls + "\".", null).log(ctx);

            if ( ctx.getBoolean("UseXMLHomes", false) )
            {
                Class       cl  = XBeans.getClass(ctx, cls, XMLHome.class);
                Constructor con = cl.getConstructor(new Class[]    { String.class });

                return new PMHome((Home) con.newInstance(new Object[]    { CoreSupport.getFile(ctx, cls.getName()) + ".xml" }), ctx, cls.getName());
            }

            Class       cl   = XBeans.getClass(ctx, cls, XDBHome.class);
            Home        home = (Home) ctx.get(cl);
            if (home == null
                    || !(home instanceof AbstractXDBHome)
                    || !SafetyUtil.safeEquals(tableName, ((AbstractXDBHome)home).getTableName()))
            {
                Constructor con  = cl.getConstructor(new Class[] { Context.class,String.class });
                home = (Home) con.newInstance(new Object[] { ctx, tableName });

                if (addXDBHomeToContext)
                {
                    ctx.put(cl, home);
                }
            }

            return home;
        }
        catch (Throwable t)
        {
            new MajorLogMsg(DefaultStorageSupport.class.getName(), "Failed to instantiate home for \"" + cls + "\".", t)
            .log(ctx);

            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    public CacheConfig getCacheConfig(Context ctx, Object key)
    {
        CacheConfig cfg = null;
        Home home = (Home)ctx.get(CacheConfigHome.class);

        try
        {
            cfg = (CacheConfig)home.find(ctx, key.toString());

            if (cfg == null)
            {
                cfg = new CacheConfig();
                cfg.setKey(key.toString());
                home.create(ctx, cfg);
            }
        }
        catch (HomeException hEx)
        {
            new MajorLogMsg(DefaultStorageSupport.class, "fail to look up cache size for "+key, hEx).log(ctx);
            // return a default configuration
            cfg = new CacheConfig();
            cfg.setKey(key.toString());
        }
        return cfg;
    }

    /**
     * {@inheritDoc}
     */
    public void createRmiService(Context ctx, Context serverCtx, Home serverHome, Class homeClass, String basHost, int basPort ) throws RemoteException
    {
        if (DeploymentTypeSupportHelper.get(ctx).isBas(ctx) || DeploymentTypeSupportHelper.get(ctx).isSingle(ctx))
        {
            RMIHomeServer server = 
                new RMIHomeServer(
                        serverCtx,
                        new PMHome(ctx, homeClass.getName() + ".rmiserver", serverHome),
                        homeClass.getName()); 
            server.register();
        }
        else if ( DeploymentTypeSupportHelper.get(ctx).isEcare(ctx))
        {
            // override the default home with RMIHomeClient
            ctx.put(homeClass,
                    new TestSerializabilityHome(ctx,
                            new PMHome(ctx, homeClass.getName() + ".rmiclient",
                                    new RMIHomeClient(
                                            ctx,
                                            basHost,
                                            basPort,
                                            homeClass.getName()))));

        }
    }

    /**
     * {@inheritDoc}
     */
    public void createRmiService(Context ctx, Context serverCtx, Class homeClass, String basHost, int basPort ) throws RemoteException
    {
        if ( DeploymentTypeSupportHelper.get(ctx).isBas(ctx) || DeploymentTypeSupportHelper.get(ctx).isSingle(ctx))
        {
            Home serverHome=(Home) ctx.get(homeClass);

            RMIHomeServer server = 
                new RMIHomeServer(
                        serverCtx,
                        new PMHome(ctx, homeClass.getName() + ".rmiserver", serverHome),
                        homeClass.getName()); 
            server.register();
        }
        else if ( DeploymentTypeSupportHelper.get(ctx).isEcare(ctx))
        {
            // override the default home with RMIHomeClient
            ctx.put(homeClass,
                    new TestSerializabilityHome(ctx,
                            new PMHome(ctx, homeClass.getName() + ".rmiclient",
                                    new RMIHomeClient(
                                            ctx,
                                            basHost,
                                            basPort,
                                            homeClass.getName()))));

        }
    }

    /**
     * {@inheritDoc}
     */
    public RemoteApplication retrieveRemoteBASAppConfig(Context ctx)
    {
        return (RemoteApplication)ctx.get(CoreCrmConstants.BAS_APPNAME);
    }

    /**
     * {@inheritDoc}
     */
    public void createPrincipalSettingHomes(Context ctx, Class homeClass)
    {
        if ( DeploymentTypeSupportHelper.get(ctx).isBas(ctx) || DeploymentTypeSupportHelper.get(ctx).isSingle(ctx))
        {
            Home serverHome = (Home) ctx.get(homeClass);
            serverHome = new SetPrincipalRmiServerHome(serverHome);
            ctx.put(homeClass, serverHome);
        }
        else if ( DeploymentTypeSupportHelper.get(ctx).isEcare(ctx))
        {
            //isEcare
            Home clientHome = (Home) ctx.get(homeClass);
            clientHome = new SetPrincipalHome(clientHome);
            ctx.put(homeClass, clientHome);
        }
    }
}
