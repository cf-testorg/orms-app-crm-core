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
package com.redknee.app.crm.taxation;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.And;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xlog.log.EntryLogMsg;
import com.redknee.framework.xlog.log.LogSupport;

import com.redknee.app.crm.bean.CT23Rule;
import com.redknee.app.crm.bean.CT23RuleDefaultValueConfig;
import com.redknee.app.crm.bean.CT23RuleHome;
import com.redknee.app.crm.bean.CT23RuleXInfo;
import com.redknee.app.crm.bean.ErIndexToProvinceConfig;
import com.redknee.app.crm.bean.PrefixTypeEnum;
import com.redknee.app.crm.bean.calldetail.CallDetail;
import com.redknee.app.crm.bean.core.Msisdn;
import com.redknee.app.crm.collection.LocationZoneToProvinceMapper;
import com.redknee.app.crm.collection.MscPrefixToProvinceMapper;
import com.redknee.app.crm.collection.MsisdnPrefixToProvinceMapper;
import com.redknee.app.crm.collection.PrefixToProvinceMapper;
import com.redknee.app.crm.support.HomeSupportHelper;
/**
 * @author bdhavalshankh
 * @since 9.5
 */
public class CT23RuleImpl implements TaxationMethod
{
    public CT23RuleImpl()
    {
    }
    
    @Override
    public int getTaxAuthority(Context ctx) throws HomeException
    {
        CallDetail cdr = (CallDetail) ctx.get(CallDetail.class);
        String msisdnValue = (String) ctx.get(Msisdn.class);
        
        
        if(cdr == null  && msisdnValue == null)
        {
            throw new IllegalArgumentException("CallDetail or Msisdn object not found in context."); 
        }
        
        String cdrHomeProv = "";
        String cdrOrigProv = ""; 
        String cdrTermProv = "";
        
        String homeProv = ""; 
        String origProv = "";
        String termProv = "";
        
        CT23RuleDefaultValueConfig defaultConfig = (CT23RuleDefaultValueConfig) ctx.get(CT23RuleDefaultValueConfig.class);
        
        if(msisdnValue != null)
        {
            /* This block will execute while invoicing only
             */
            PrefixToProvinceMapper msisdnMapper = (PrefixToProvinceMapper) ctx.get(MsisdnPrefixToProvinceMapper.class);
            homeProv = msisdnMapper.getProvince(ctx, msisdnValue);
            origProv = defaultConfig.getOrigProvDefault();
            termProv = defaultConfig.getTermProvDefault();
            if(homeProv == null)
            {
                StringBuilder sb = new StringBuilder();
                sb.append("No prefix to province mapping found for[HomeProvince = ");
                sb.append(homeProv);
                sb.append("].");
                new EntryLogMsg(15113, this, "", "", new String[]{sb.toString()}, null).log(ctx);
                
                throw new HomeException(sb.toString());
            }
        }
        else if(cdr != null)
        {
            cdrHomeProv = cdr.getHomeProv();
            cdrOrigProv = cdr.getOrigProv();
            cdrTermProv = cdr.getTermProv();
            
            PrefixToProvinceMapper homeMapper = null;
            PrefixToProvinceMapper origMapper = null;
            PrefixToProvinceMapper termMapper = null;
                    
            if(cdr.getChargedParty()!= null)
            {
                ErIndexToProvinceConfig provinceConfig = ProvincePrefixMappingSupport.getErToProvincesConfig(ctx, cdr, cdr.getChargedParty());
                if(provinceConfig != null)
                {
                    homeMapper = getMapperForPrefixType(ctx, provinceConfig.getErIndexToProvinceType().getHomePrefixType());
                    origMapper = getMapperForPrefixType(ctx, provinceConfig.getErIndexToProvinceType().getOrigPrefixType());
                    termMapper = getMapperForPrefixType(ctx, provinceConfig.getErIndexToProvinceType().getTermPrefixType());
                }
                else
                {
                    LogSupport.major(ctx, this, "No ER index to Province Configuration found for the cdr");
                }
            }
            
            homeProv = homeMapper.getProvince(ctx, cdrHomeProv); 
            origProv = origMapper.getProvince(ctx, cdrOrigProv);
            termProv  = termMapper.getProvince(ctx, cdrTermProv);
            
            if(homeProv == null || homeProv.trim().equals(""))
            {
                StringBuilder sb = new StringBuilder();
                sb.append("No prefix to province mapping found for[HomeProvince = ");
                sb.append(cdrHomeProv);
                sb.append("].");
                new EntryLogMsg(15113, this, "", "", new String[]{sb.toString()}, null).log(ctx);
                
                throw new HomeException(sb.toString());
            }
            
            if(origProv == null || origProv.trim().equals(""))
            {
                if(cdrOrigProv == null || cdrOrigProv.trim().equals("") || cdrOrigProv.trim().equals("-1"))
                {
                    origProv = defaultConfig.getOrigProvDefault();
                }
                else
                {
                    origProv = defaultConfig.getForeignMscDefault();
                }
            }
            
            if(termProv == null || termProv.trim().equals(""))
            {
                if(cdrTermProv == null || cdrTermProv.trim().equals("") || cdrTermProv.trim().equals("-1"))
                {
                    termProv = defaultConfig.getTermProvDefault();
                }
                else
                {
                    termProv = defaultConfig.getForeignMscDefault();
                }
            }
            
            final Home home = (Home) ctx.get(CT23RuleHome.class);
            if (home == null)
            {
                throw new IllegalStateException("CT23RuleHome not found in context.");
            }
    
        }
        LogSupport.info(ctx, this, "setTaxAuthority: homeProv=" + homeProv + ", origProv=" + origProv + ", termProv=" + termProv);
        return getTaxAuthByProvince(ctx, homeProv, origProv, termProv); 
    }

    /**
     * @param ctx
     * @param homeProv
     * @param origProv
     * @param termProv
     * @return Tax Authority
     * @throws HomeException
     */
    private int getTaxAuthByProvince(Context ctx, String homeProv, String origProv, String termProv)
            throws HomeException
    {
        And filter = new And();
        filter.add(new EQ(CT23RuleXInfo.HOME_PROV, homeProv));
        filter.add(new EQ(CT23RuleXInfo.ORIG_PROV, origProv));
        filter.add(new EQ(CT23RuleXInfo.TERM_PROV, termProv));
        
        final CT23Rule ct23Rule = HomeSupportHelper.get(ctx).findBean(ctx, CT23Rule.class, filter);
        if (ct23Rule == null)
        {
            StringBuilder sb = new StringBuilder();
            sb.append("No Tax Rule found for [HomeProvince = ");
            sb.append(homeProv);
            sb.append(", OrigProv = ");
            sb.append(origProv);
            sb.append(", TermProv = ");
            sb.append(termProv);
            sb.append("].");
            new EntryLogMsg(15113, this, "", "", new String[]{sb.toString()}, null).log(ctx);
            
            throw new HomeException(sb.toString());
        }
        else
        {
           return ct23Rule.getTaxAuthorityId();
        }
    }

    /**
     * @param ctx
     * @param prefixTypeEnum
     */
    private PrefixToProvinceMapper getMapperForPrefixType(Context ctx, PrefixTypeEnum prefixTypeEnum)
    {
        PrefixToProvinceMapper msisdnMapper = (PrefixToProvinceMapper) ctx.get(MsisdnPrefixToProvinceMapper.class);
        if(msisdnMapper == null)
        {
            throw new IllegalStateException("MsisdnPrefixToProvinceMapper object not found in context."); 
        }
        
        PrefixToProvinceMapper mscMapper = (PrefixToProvinceMapper) ctx.get(MscPrefixToProvinceMapper.class);
        if(mscMapper == null)
        {
            throw new IllegalStateException("MscPrefixToProvinceMapper object not found in context."); 
        }
        
        PrefixToProvinceMapper locationZoneMapper = (PrefixToProvinceMapper) ctx.get(LocationZoneToProvinceMapper.class);
        if(locationZoneMapper == null)
        {
            throw new IllegalStateException("locationZoneMapperPrefixToProvinceMapper object not found in context."); 
        }
        
        if(prefixTypeEnum.getIndex() == PrefixTypeEnum.MSISDN_INDEX)
        {
            return msisdnMapper;
        }
        else if(prefixTypeEnum.getIndex() == PrefixTypeEnum.MSC_INDEX)
        {
            return mscMapper;
        }
        else if(prefixTypeEnum.getIndex() == PrefixTypeEnum.LOCATION_ZONE_INDEX)
        {
            return locationZoneMapper;
        }
        return null;
        
    }

}