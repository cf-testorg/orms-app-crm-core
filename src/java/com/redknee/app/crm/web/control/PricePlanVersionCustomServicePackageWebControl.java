/*
 * Created on Sep 30, 2005
 *
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
package com.redknee.app.crm.web.control;

import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletRequest;

import com.redknee.app.crm.bean.PricePlanSubTypeEnum;
import com.redknee.app.crm.bean.ServiceFee2XInfo;
import com.redknee.app.crm.bean.ServiceHome;
import com.redknee.app.crm.bean.ServicePackageFee;
import com.redknee.app.crm.bean.ServicePackageFeeXInfo;
import com.redknee.app.crm.bean.ServicePackageHome;
import com.redknee.app.crm.bean.ServicePackageVersionWebControl;
import com.redknee.app.crm.bean.ServicePackageVersionXInfo;
import com.redknee.app.crm.bean.ServicePackageXInfo;
import com.redknee.app.crm.bean.ServiceXInfo;
import com.redknee.app.crm.bean.SubscriberTypeEnum;
import com.redknee.app.crm.bean.core.PricePlan;
import com.redknee.app.crm.bean.core.PricePlanVersion;
import com.redknee.app.crm.bean.core.ServicePackage;
import com.redknee.app.crm.bean.core.ServicePackageVersion;
import com.redknee.app.crm.bundle.BundleFeeXInfo;
import com.redknee.app.crm.bundle.BundleProfileHome;
import com.redknee.app.crm.bundle.BundleProfileXInfo;
import com.redknee.app.crm.bundle.BundleSegmentEnum;
import com.redknee.app.crm.bundle.FlexTypeEnum;
import com.redknee.app.crm.support.WebControlSupport;
import com.redknee.app.crm.support.WebControlSupportHelper;
import com.redknee.framework.xhome.beans.xi.PropertyInfo;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.And;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.elang.Not;
import com.redknee.framework.xhome.elang.Or;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.LRUSelectCachingHome;
import com.redknee.framework.xhome.web.renderer.DefaultDetailRenderer;
import com.redknee.framework.xhome.web.renderer.DetailRenderer;
import com.redknee.framework.xlog.log.DebugLogMsg;
import com.redknee.framework.xlog.log.LogSupport;

/**
 *
 * Custom web control for ServicePackage when used inside PricePlanVersion. This hides a series of fields from
 * inside ServicePackage.
 *
 * @author psperneac
 *
 */
public class PricePlanVersionCustomServicePackageWebControl extends ServicePackageVersionWebControl
{

    public PricePlanVersionCustomServicePackageWebControl()
    {
        super();
    }

    /**
     * @see com.redknee.app.crm.bean.ServicePackageWebControl#toWeb(com.redknee.framework.xhome.context.Context, java.io.PrintWriter, java.lang.String, java.lang.Object)
     */
    @Override
    public void toWeb(Context ctx, PrintWriter out, String name, Object obj)
    {
        ctx=ctx.createSubContext();
        
        WebControlSupport webControlSupport = WebControlSupportHelper.get(ctx);
        webControlSupport.hideProperties(ctx, new PropertyInfo[]
                                                               {
                ServicePackageXInfo.ID,
                ServicePackageXInfo.TYPE,
                ServicePackageXInfo.NAME,
                ServicePackageXInfo.SPID,
                ServicePackageXInfo.CHARGING_LEVEL,
                ServicePackageXInfo.CHARGING_MODE,
                ServicePackageXInfo.RECURRING_RECHARGE,
                ServicePackageXInfo.ADJUSTMENT_GLCODE,
                ServicePackageXInfo.ADJUSTMENT_TYPE_DESCRIPTION,
                ServicePackageXInfo.ADJUSTMENT_INVOICE_DESCRIPTION,
                ServicePackageXInfo.TAX_AUTHORITY,
                ServicePackageXInfo.TOTAL_CHARGE,
                BundleFeeXInfo.START_DATE,
                BundleFeeXInfo.END_DATE,
                BundleFeeXInfo.PAYMENT_NUM,
                BundleFeeXInfo.NEXT_RECURRING_CHARGE_DATE,
                ServiceFee2XInfo.CLTC_DISABLED,
                ServicePackageFeeXInfo.CLTC_DISABLED,
                ServicePackageVersionXInfo.VERSION,
                ServicePackageVersionXInfo.ACTIVATION,
                ServicePackageVersionXInfo.ACTIVATE_DATE,
                                                               }
        );
        
        PricePlan pp = getPricePlan(ctx);
        if (pp!=null)
        {
        	if(!(pp.getPricePlanSubType().equals(PricePlanSubTypeEnum.PICKNPAY)))
        	{
        		webControlSupport.hideProperties(ctx, new PropertyInfo[]
        				{
        					ServiceFee2XInfo.APPLY_WITHIN_MRC_GROUP,
        					BundleFeeXInfo.APPLY_WITHIN_MRC_GROUP
        				}
        		);
        	}
        }

//        webControlSupport.setPropertiesReadOnly(ctx, new PropertyInfo[]
//                {
//				BundleFeeXInfo.SERVICE_PERIOD,
//				ServiceFee2XInfo.SERVICE_PERIOD
//                }
//        );
        
        ctx.put("ACTIONS",false);

        ctx.put(DetailRenderer.class, new DefaultDetailRenderer(ctx)
            {
               @Override
            public void Table(Context ctx, PrintWriter out, String title)
               {
                  TREnd(ctx, out);
               }

               @Override
            public void TableEnd(Context ctx, PrintWriter out, String footer)
               {
                  TR(ctx, out,"");
               }
            });

        updatePackageFees(ctx,(ServicePackageVersion)obj);
        
        Context subCtx = filterAndCacheBundles(ctx);
        subCtx=filterServices(subCtx); 
        filterPackages(subCtx);
       
        super.toWeb(subCtx, out, name, obj);
    }
    
    private PricePlan getPricePlan(Context ctx)
    {
        PricePlanVersion ppv = (PricePlanVersion) ctx.get(BEAN);
        if(ppv != null)
        {
            PricePlan pp = null;
            try
            {
                pp = ppv.getPricePlan(ctx);
            }
            catch (Exception e)
            {
                LogSupport.minor(ctx, this, "Unable to retrieve price plan: " + e.getMessage(), e);
            }
            return pp;
        }
        return null;
    }
    
    private Context filterAndCacheBundles(Context ctx)
    {
        Context subCtx = ctx;
        PricePlan pp = getPricePlan(ctx);
        if (pp!=null)
        {
            subCtx = ctx.createSubContext();
            Home home = (Home) ctx.get(BundleProfileHome.class);
            And and = new And();
            Or filter = new Or();
            filter.add(new EQ(BundleProfileXInfo.SEGMENT, BundleSegmentEnum.HYBRID));
            if (SubscriberTypeEnum.PREPAID.equals(pp.getPricePlanType()))
            {
                filter.add(new EQ(BundleProfileXInfo.SEGMENT, BundleSegmentEnum.PREPAID));
            }
            else
            {
                filter.add(new EQ(BundleProfileXInfo.SEGMENT, BundleSegmentEnum.POSTPAID));
            }
            And flexFilter = new And().add(new EQ(BundleProfileXInfo.FLEX_TYPE, FlexTypeEnum.SECONDARY)).add(
                    new EQ(BundleProfileXInfo.FLEX, true));
            and.add(new EQ(BundleProfileXInfo.SPID, pp.getSpid()));
            and.add(filter).add( new Not(flexFilter) );
            home = home.where(subCtx, and);
            subCtx.put(BundleProfileHome.class, new LRUSelectCachingHome(subCtx, BundleProfileHome.class.getName(),home)) ;
        }
    return subCtx;
    }
    
    
    private Context filterServices(Context ctx)
    {        
        PricePlan pp = getPricePlan(ctx);
        if (pp!=null)
        {
            And and = new And();
            and.add(new EQ(ServiceXInfo.SPID, pp.getSpid()));
            and.add(new EQ(ServiceXInfo.TECHNOLOGY, pp.getTechnology()));
            Home home = (Home) ctx.get(ServiceHome.class);
            home = home.where(ctx, and);
            ctx.put(ServiceHome.class, new LRUSelectCachingHome(ctx, ServiceHome.class.getName(),home)) ;            
        }
        return ctx;
    }
    
    private Context filterPackages(Context ctx)
    {        
        PricePlan pp = getPricePlan(ctx);
        if (pp!=null)
        {
            And and = new And();
            and.add(new EQ(ServicePackageXInfo.SPID, pp.getSpid()));
            and.add(new EQ(ServicePackageXInfo.TYPE, pp.getPricePlanType()));
            Home home = (Home) ctx.get(ServicePackageHome.class);
            home = home.where(ctx, and);
            ctx.put(ServicePackageHome.class, new LRUSelectCachingHome(ctx, ServicePackageHome.class.getName(),home)) ;            
        }
        return ctx;
    }
    
    private void updatePackageFees(Context ctx, ServicePackageVersion pack)
    {
        Map packages=pack.getPackageFees();

        Home home=(Home) ctx.get(ServicePackageHome.class);

        if(packages!=null && packages.size()>0)
        {
            for(Iterator i=packages.keySet().iterator();i.hasNext();)
            {
                try
                {
                    ServicePackageFee fee=(ServicePackageFee) packages.get(i.next());
                    ServicePackage p=(ServicePackage) home.find(ctx, Integer.valueOf(fee.getPackageId()));
                    if(p!=null)
                    {
                        p.updateTotalCharge(ctx);
                        fee.setFee(p.getTotalCharge());
                    }
                    else
                    {
                        if(LogSupport.isDebugEnabled(ctx))
                        {
                            new DebugLogMsg(this,"Cannot find package with id: "+fee.getPackageId(),null).log(ctx);
                        }
                    }
                }
                catch(HomeException e)
                {
                    if(LogSupport.isDebugEnabled(ctx))
                    {
                        new DebugLogMsg(this,e.getMessage(),e).log(ctx);
                    }
                }
            }
        }
    }
    
	@Override
	public void fromWeb(Context ctx, Object obj, ServletRequest req, String name)
    {
		super.fromWeb(ctx, obj, req, name);
	}
    
}
