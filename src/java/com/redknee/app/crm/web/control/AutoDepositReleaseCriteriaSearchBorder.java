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
package com.redknee.app.crm.web.control;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.web.search.FindSearchAgent;
import com.redknee.framework.xhome.web.search.SearchBorder;
import com.redknee.framework.xhome.web.search.WildcardSelectSearchAgent;

import com.redknee.app.crm.bean.AutoDepositReleaseCriteria;
import com.redknee.app.crm.bean.search.AutoDepositReleaseCriteriaSearch;
import com.redknee.app.crm.bean.search.AutoDepositReleaseCriteriaSearchWebControl;
import com.redknee.app.crm.bean.search.AutoDepositReleaseCriteriaSearchXInfo;
import com.redknee.app.crm.bean.AutoDepositReleaseCriteriaXInfo;

/**
 * A custom search border for Auto Deposit Release Criteria.
 *
 * @author cindy.wong@redknee.com
 */
public class AutoDepositReleaseCriteriaSearchBorder extends SearchBorder
{
    /**
     * Creates a new AutoDepositReleaseCriteriaSearchBorder.
     *
     * @param ctx The operation context.
     */
    public AutoDepositReleaseCriteriaSearchBorder(final Context ctx)
    {
        super(ctx, AutoDepositReleaseCriteria.class, new AutoDepositReleaseCriteriaSearchWebControl());

        // identifier
        addAgent(new FindSearchAgent(AutoDepositReleaseCriteriaXInfo.IDENTIFIER,
            AutoDepositReleaseCriteriaSearchXInfo.IDENTIFIER).addIgnore(Integer.valueOf(-1)));

        // description
        addAgent(new WildcardSelectSearchAgent(AutoDepositReleaseCriteriaXInfo.DESCRIPTION,
            AutoDepositReleaseCriteriaSearchXInfo.DESCRIPTION, true));
    }
}
