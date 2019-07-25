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

import com.redknee.framework.xhome.context.Context;

import com.redknee.app.crm.bean.TaxAuthority;
import com.redknee.app.crm.bean.TaxExemptionInclusion;


/**
 * @author bhupendra.pandey@redknee.com
 * 
 */
public interface TaxAuthoritySupport extends Support
{

    /**
     * Determine whether "Enable Enhanced Tax Exemption/Inclusion" is enabled for the
     * corresponding spid.
     * 
     * @param ctx
     *            The operating context.
     * @param spid
     *            The given service provider id.
     */
    public boolean isTEICEnabled(final Context ctx, final int spid);


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
    public TaxExemptionInclusion getTEICById(Context ctx, long teicId, int spid);


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
    public String getDescription(final Context ctx, final TaxAuthority ta);


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
    public TaxAuthority getTaxAuthorityById(final Context ctx, final int id, final int spid);


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
    public String getTEICinvoiceDesc(Context ctx, long teicId, int spid);


    /**
     * Retrive the total tax percentage configured corresponding to the supplied
     * taxAuthority.
     * 
     * @param ctx
     * @param taxAuth
     * @param spid
     * @return
     */
    public double getTotalTaxPercentage(final Context ctx, final TaxAuthority taxAuth, final int spid);


    /**
     * 
     * @param taxAuth
     * @param name
     * @return
     */
    public double getTaxRateByName(TaxAuthority taxAuth, String name);
}
