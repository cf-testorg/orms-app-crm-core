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
package com.redknee.app.crm.bean.core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.redknee.framework.xhome.beans.AbstractBean;
import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.In;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.visitor.AbortVisitException;
import com.redknee.framework.xhome.visitor.Visitor;
import com.redknee.framework.xhome.visitor.Visitors;
import com.redknee.framework.xlog.log.DebugLogMsg;
import com.redknee.framework.xlog.log.LogSupport;
import com.redknee.framework.xlog.log.MinorLogMsg;

import com.redknee.app.crm.bean.ChargingLevelEnum;
import com.redknee.app.crm.bean.ServicePackageFee;
import com.redknee.app.crm.bean.ServicePackageHome;
import com.redknee.app.crm.bean.ServicePackageXInfo;
import com.redknee.app.crm.bean.ServicePreferenceEnum;
import com.redknee.app.crm.xhome.adapter.StaticContextBeanAdapter;


/**
 * 
 *
 * @author aaron.gourley@redknee.com
 * @since 
 */
public class ServicePackageVersion extends com.redknee.app.crm.bean.ServicePackageVersion
{
    /**
     * Visit all BundleFees in this SPV and in its Packages.
     */
    public Visitor forEachBundle(Context ctx, Visitor visitor)
      throws AgentException
    {
        final Visitor visitor2 = Visitors.forEach(ctx, getBundleFees(), visitor);

        try
        {
            Set packageIDs = getPackageFees().keySet();
            return ((Home) ctx.get(ServicePackageHome.class)).where(ctx,
                // Set obtained from keySet() is not serializable, so put values in a new set
                new In(ServicePackageXInfo.ID, new HashSet(packageIDs))).forEach(new Visitor()
                {
                    public void visit(Context ctx, Object obj)
                    {
                        ServicePackage pack=(ServicePackage) obj;
                        ServicePackageVersion version = pack.getCurrentVersion(ctx);
                        try
                        {
                            version.forEachBundle(ctx,visitor2);
                        }
                        catch (AgentException e)
                        {
                            if(LogSupport.isDebugEnabled(ctx))
                            {
                                new DebugLogMsg(this,e.getMessage(),e).log(ctx);
                            }
                        }
                    }
                });
        }
        catch (HomeException e)
        {
            throw new AgentException(e);
        }
    }

    /**
     * Visit all ServiceFees in this SPV and in its Packages.
     */
    public Visitor forEachService(Context ctx, Visitor visitor,ServicePackageFee packFee)
      throws AgentException
    {
        try
        {
            Home packHome=(Home) ctx.get(ServicePackageHome.class);
            ServicePackage parentPack=(ServicePackage) packHome.find(ctx, Integer.valueOf(getId()));

            visitor = Visitors.forEach(ctx, copyExisting(ctx,getServiceFees(),parentPack,packFee), visitor);

            for(Iterator i=getPackageFees().values().iterator();i.hasNext();)
            {
                ServicePackageFee fee=(ServicePackageFee) i.next();
                ServicePackage pack=(ServicePackage) packHome.find(ctx, Integer.valueOf(fee.getPackageId()));

                if(pack!=null)
                {
                    ServicePackageVersion version = pack.getCurrentVersion(ctx);

                    if (version != null)
                    {
                        version.forEachService(ctx, visitor, fee);
                    }
                }
                else
                {
                    if(LogSupport.isDebugEnabled(ctx))
                    {
                        new DebugLogMsg(this,"Cannot find package with id: "+fee.getPackageId(),null).log(ctx);
                    }
                }
            }
        }
        catch (HomeException e)
        {
            throw new AgentException(e);
        }

        return visitor;
    }

    /**
     * Clones the map of ServiceFees and sets the mandatory flag from the service package and the service package fee.
     * This might have to be changed because we might not want the cltc and the dispCltc from the service package fee.
     * [PaulSperneac]
     *
     * @param ctx
     * @param serviceFees
     * @param pack the package from which the copy was called
     * @param packFee the package fee for that package. if the fee is null it means that the package is the fake
     * one from the PricePlanVersion
     * @return
     */
    private Map copyExisting(Context ctx,Map serviceFees,ServicePackage pack,ServicePackageFee packFee)
    {
        Map ret=new HashMap();

        for(Iterator i=serviceFees.values().iterator();i.hasNext();)
        {
            try
            {
                com.redknee.app.crm.bean.ServiceFee2 fee = (com.redknee.app.crm.bean.ServiceFee2) ((AbstractBean) i.next()).clone();

                if(packFee!=null)
                {
                    if (packFee.isMandatory())
                    {
                        fee.setServicePreference(ServicePreferenceEnum.MANDATORY);
                    }
                    else
                    {
                        fee.setServicePreference(ServicePreferenceEnum.OPTIONAL);
                    }

                    fee.setCltcDisabled(packFee.getCltcDisabled());
                    fee.setDispCLTC(packFee.getDispCLTC());

                    // if it is in package mode, make the individual fees 0. the charge is going to
                    // be taken from the package reccur recharge field
                    if (pack != null)
                    {
                        fee.setSource("Package: ");
                        if (pack.getChargingLevel() == ChargingLevelEnum.PACKAGE)
                        {
                            fee.setFee(0);
                        }
                    }
                }

                ret.put(Long.valueOf(fee.getServiceId()),fee);
            }
            catch (CloneNotSupportedException e)
            {
                if(LogSupport.isDebugEnabled(ctx))
                {
                    new DebugLogMsg(this,e.getMessage(),e).log(ctx);
                }
            }
        }

        return ret;
    }

    /**
     * This will return the list of all service fees from the current version and the service packages in this version
     *
     * @param ctx
     * @return
     */
    public Map<Long, ServiceFee2> getServiceFees(Context ctx)
    {
        final Map<Long, ServiceFee2> fees=new HashMap<Long, ServiceFee2>();
        
        try
        {
            forEachService(ctx,new Visitor()
                {
                    public void visit(Context vCtx, Object obj) throws AgentException, AbortVisitException
                    {
                        com.redknee.app.crm.bean.ServiceFee2 modelFee=(com.redknee.app.crm.bean.ServiceFee2) obj;
                        
                        try
                        {
                        	Context appctx = (Context)vCtx.get("app");
                        	ServiceFee2 fee = (ServiceFee2) new StaticContextBeanAdapter<com.redknee.app.crm.bean.ServiceFee2, ServiceFee2>(
                                    com.redknee.app.crm.bean.ServiceFee2.class, 
                                    ServiceFee2.class, appctx).adapt(vCtx, modelFee);
                            
                            fees.put(Long.valueOf(fee.getServiceId()),fee);
                        }
                        catch (HomeException e)
                        {
                            new MinorLogMsg(this, "Error populating service fee map: " + e.getMessage(), e).log(vCtx);
                        }
                    }
                },null);
        }
        catch (AgentException e)
        {
            if(LogSupport.isDebugEnabled(ctx))
            {
                new DebugLogMsg(this,e.getMessage(),e).log(ctx);
            }
        }

        return fees;
    }

    /**
     * This will return the list of all bundle fees from the current version and the service packages in this version
     *
     * @param ctx
     * @return
     */
    public Map<Long, BundleFee> getBundleFees(Context ctx)
    {
        final Map<Long, BundleFee> fees=new HashMap<Long, BundleFee>();

        try
        {
            forEachBundle(ctx,new Visitor()
                {
                    public void visit(Context vCtx, Object obj) throws AgentException, AbortVisitException
                    {
                        com.redknee.app.crm.bundle.BundleFee modelFee=(com.redknee.app.crm.bundle.BundleFee) obj;
                        
                        try
                        {
                        	Context appctx = (Context)vCtx.get("app");
                        	BundleFee fee = (BundleFee) new StaticContextBeanAdapter<com.redknee.app.crm.bundle.BundleFee, BundleFee>(
                                    com.redknee.app.crm.bundle.BundleFee.class, 
                                    BundleFee.class, appctx).adapt(vCtx, modelFee);

                            fees.put(Long.valueOf(fee.getId()),fee);
                        }
                        catch (HomeException e)
                        {
                            new MinorLogMsg(this, "Error populating service fee map: " + e.getMessage(), e).log(vCtx);
                        }
                    }
                });
        }
        catch (AgentException e)
        {
            if(LogSupport.isDebugEnabled(ctx))
            {
                new DebugLogMsg(this,e.getMessage(),e).log(ctx);
            }
        }

        return fees;
    }
}
