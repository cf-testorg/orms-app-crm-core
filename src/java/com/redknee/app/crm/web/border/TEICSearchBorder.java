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
package com.redknee.app.crm.web.border;

import com.redknee.app.crm.bean.TaxExemptionInclusion;
import com.redknee.app.crm.bean.TaxExemptionInclusionXInfo;
import com.redknee.app.crm.bean.search.TaxExemptionInclusionSearchWebControl;
import com.redknee.app.crm.bean.search.TaxExemptionInclusionSearchXInfo;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.web.search.SearchBorder;
import com.redknee.framework.xhome.web.search.SelectSearchAgent;
import com.redknee.framework.xhome.web.search.WildcardSelectSearchAgent;


/**
 * @author bhupendra.pandey@redknee.com
 * 
 *         Search Border for TEIC by Id,Name and Spid
 */
public class TEICSearchBorder extends SearchBorder
{

    public TEICSearchBorder(Context ctx)
    {
        super(ctx, TaxExemptionInclusion.class, new TaxExemptionInclusionSearchWebControl());
        // ID
        addAgent(new SelectSearchAgent(TaxExemptionInclusionXInfo.ID, TaxExemptionInclusionSearchXInfo.TEIC_ID, false)
                .addIgnore(Long.valueOf(-1)));
        // SPID
        addAgent(new SelectSearchAgent(TaxExemptionInclusionXInfo.SPID, TaxExemptionInclusionSearchXInfo.SPID, false)
                .addIgnore(Integer.valueOf(-1)));
        // Name
        addAgent(new WildcardSelectSearchAgent(TaxExemptionInclusionXInfo.NAME, TaxExemptionInclusionSearchXInfo.NAME,
                true));
    }
}
