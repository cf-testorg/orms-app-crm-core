package com.redknee.app.crm.support;

import java.rmi.RemoteException;

import com.redknee.framework.application.RemoteApplication;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.CacheConfig;
import com.redknee.framework.xhome.home.Home;


/**
 * Support for creating either XML or XDBHomes.
 */
public interface StorageSupport extends Support
{

    /**
     * Temporary Solution. Will fix once XMLHome and XDBHomes have empty constructors. put ctx.put("UseXMLHomes", true);
     * in Application.script to use XMLHomes instead
     * 
     * @param ctx the context where the home is inserted
     * @param cls the class of the bean that we want the home for
     * @return the home
     */
    public Home createHome(Context ctx, Class cls, String tableName);

    public Home createHome(Context ctx, Class cls, String tableName, boolean addXDBHomeToContext);


    public CacheConfig getCacheConfig(Context ctx, Object key);


    public void createRmiService(Context ctx, Context serverCtx, Home serverHome, Class homeClass, String basHost,
            int basPort) throws RemoteException;


    /**
     * Creates an RMI server and client home for a home already in the context.
     * 
     * @param ctx
     * @param serverCtx
     * @param homeClass
     * @param basHost
     * @param basPort
     * @throws RemoteException
     */
    public void createRmiService(Context ctx, Context serverCtx, Class homeClass, String basHost, int basPort)
            throws RemoteException;


    public RemoteApplication retrieveRemoteBASAppConfig(Context ctx);


    /**
     * Decorate using the SetPrincipalRmiServerHome on the BAS node (and Single node),
     * and the SetPrincipalHome on the E-care node.
     * 
     * @param ctx
     * @param homeClass
     */
    public void createPrincipalSettingHomes(Context ctx, Class homeClass);
}
