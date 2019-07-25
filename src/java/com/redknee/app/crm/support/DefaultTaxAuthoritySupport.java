/*
 * This code is a protected work and subject to domestic and international copyright
 * law(s). A complete listing of authors of this work is readily available. Additionally,
 * source code is, by its very nature, confidential information and inextricably contains
 * trade secrets and other information proprietary, valuable and sensitive to Redknee. No
 * unauthorized use, disclosure, manipulation or otherwise is permitted, and may only be
 * used in accordance with the terms of the license agreement entered into with Redknee
 * Inc. and/or its subsidiaries.
 * 
 * Copyright (c) Redknee Inc. and its subsidiaries. All Rights Reserved.
 */
package com.redknee.app.crm.support;

import java.util.List;

import com.redknee.app.crm.bean.TaxAuthority;
import com.redknee.app.crm.bean.TaxAuthorityHome;
import com.redknee.app.crm.bean.TaxAuthorityXInfo;
import com.redknee.app.crm.bean.TaxComponent;
import com.redknee.app.crm.bean.TaxExemptionInclusion;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.And;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xlog.log.LogSupport;


/**
 * @author bhupendra.pandey@redknee.com
 * 
 */
public class DefaultTaxAuthoritySupport implements TaxAuthoritySupport
{
    protected static TaxAuthoritySupport instance_ = null;
    public static TaxAuthoritySupport instance()
    {
        if (instance_ == null)
        {
            instance_ = new DefaultTaxAuthoritySupport();
        }
        return instance_;
    }

    protected DefaultTaxAuthoritySupport()
    {
    }

    /**
     * Determine whether "Enable Enhanced Tax Exemption/Inclusion" is enabled for the
     * corresponding spid.
     * 
     * @param ctx
     *            The operating context.
     * @param spid
     *            The given service provider id.
     */
    public boolean isTEICEnabled(final Context ctx, final int spid)
    {
        return false;
    }


    /**
     * Retrive the TEIC object corresponding to the supplied id and spid.
     * 
     * @param ctx
     *            The operating context.
     * @param teicId
     *            The TEIC id whose corresponding TEIC object is to be retrieved.
     * @param spid
     *            The service provider id whose corresponding TEIC object is to be
     *            retrieved.
     * @return The matching TEIC object
     */
    public TaxExemptionInclusion getTEICById(Context ctx, long teicId, int spid)
    {
        TaxExemptionInclusion teic = null;
        try
        {
            teic = (TaxExemptionInclusion) HomeSupportHelper.get(ctx)
                    .findBean(ctx, TaxExemptionInclusion.class, teicId);
        }
        catch (HomeException he)
        {
            LogSupport.minor(ctx, DefaultTaxAuthoritySupport.class, "Error encountered while finding the TaxExemptionInclusion with id "
                    + teicId, he);
        }
        return teic;
    }


    /**
     * Construct the Tax Authority description based on the pattern 'TaxAuthority's Name
     * -<TaxRate>%' - for legacy tax authority. 'TaxAuthority's Name -
     * {Name1-<TaxRate1>%Name2-<TaxRate2>%..NameN-<TaxRateN>% ' - for multiple tax
     * percentages.
     * 
     * @param ctx
     *            The operating context
     * @param ta
     *            The tax authority whose description string is to be constructed.
     * @return The constructed description string.
     */
    public String getDescription(final Context ctx, final TaxAuthority ta)
    {
        final StringBuilder sb = new StringBuilder(ta.getTaxAuthName());
        sb.append(" - ");
        final List<TaxComponent> listOfTaxRates = ta.getTaxComponents();
        sb.append("{");
        sb.append(ta.getInvoiceShortCode());
        sb.append("-");
        sb.append(ta.getTaxRate());
        sb.append("%");
        for (final TaxComponent trs : listOfTaxRates)
        {
            sb.append(trs.getTaxCode());
            sb.append("-");
            sb.append(trs.getTaxRate());
            sb.append("%");
        }
        sb.append("}");
        return sb.toString();
    }


    /**
     * Retrieve the matching TaxAuthority object for the supplied spid,id.
     * 
     * @param ctx
     *            The operating context
     * @param id
     *            The tax authority identifier whose TA object is to be retrieved.
     * @param spid
     *            The service provider identifier whose TA object is to be retrieved.
     * @return The matching TaxAuthority object
     */
    public TaxAuthority getTaxAuthorityById(final Context ctx, final int id, final int spid)
    {
        TaxAuthority ta = null;
        try
        {
            final Home home = (Home) ctx.get(TaxAuthorityHome.class);
            final And condition = new And();
            condition.add(new EQ(TaxAuthorityXInfo.TAX_ID, id));
            condition.add(new EQ(TaxAuthorityXInfo.SPID, spid));
            ta = (TaxAuthority) home.find(ctx, condition);
        }
        catch (final HomeException he)
        {
            LogSupport.minor(ctx, DefaultTaxAuthoritySupport.class, "Encountered an error-", he);
        }
        return ta;
    }


    /**
     * Retrieve the TaxExemptionInclusion's invoice description corresponding to the supplied id and spid.
     * 
     * @param ctx
     *            The operating context.
     * @param teicId
     *            The TaxExemptionInclusion id whose corresponding TaxExemptionInclusion's invoice description is to be
     *            retrieved.
     * @param spid
     *            The service provider id whose corresponding TaxExemptionInclusion' invoice description is
     *            to be retrieved.
     * @return The matching TaxExemptionInclusion object's invoice description
     */
    public String getTEICinvoiceDesc(Context ctx, long teicId, int spid)
    {
        String desc = "";
        if (isTEICEnabled(ctx, spid))
        {
            final TaxExemptionInclusion teic = getTEICById(ctx, teicId, spid);
            if (teic != null)
            {
                desc = teic.getInvoiceDesc();
            }
        }
        return desc;
    }


    /**
     * Retrive the total tax percentage configured corresponding to the supplied
     * taxAuthority.
     * 
     * @param ctx
     * @param taxAuth
     * @param spid
     * @return
     */
    public double getTotalTaxPercentage(final Context ctx, final TaxAuthority taxAuth, final int spid)
    {
        double taxRate = taxAuth.getTaxRate();
        final List<TaxComponent> listOfTaxRates = taxAuth.getTaxComponents();
        for (final TaxComponent trs : listOfTaxRates)
        {
            taxRate += trs.getTaxRate();
        }
        return taxRate;
    }


    /**
     * 
     * @param taxAuth
     * @param name
     * @return
     */
    public double getTaxRateByName(TaxAuthority taxAuth, String name)
    {
        if (taxAuth.getTaxCode().equals(name))
        {
            return taxAuth.getTaxRate();
        }
        for (Object obj : taxAuth.getTaxComponents())
        {
            TaxComponent taxComp = (TaxComponent) obj;
            if (taxComp.getTaxCode().equals(name))
            {
                return taxComp.getTaxRate();
            }
        }
        return 0;
    }

    /**
     * Default tax to be applied if Tax authority specified doesn't exist.
     */
    private static final int DEFAULT_TAX = 0;
    /**
     * The divisor to be considered.
     */
    private static final double DIVISOR = 100.0;
    public final int PRECISION_PLACES = 16;
}
