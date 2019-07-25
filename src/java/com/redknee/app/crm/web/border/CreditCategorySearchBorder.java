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
package com.redknee.app.crm.web.border;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.web.search.FindSearchAgent;
import com.redknee.framework.xhome.web.search.SearchBorder;
import com.redknee.framework.xhome.web.search.WildcardSelectSearchAgent;

import com.redknee.app.crm.bean.core.CreditCategory;
import com.redknee.app.crm.bean.CreditCategoryXInfo;
import com.redknee.app.crm.bean.search.CreditCategorySearchWebControl;
import com.redknee.app.crm.bean.search.CreditCategorySearchXInfo;

/**
 * @author rattapattu
 */
public class CreditCategorySearchBorder extends SearchBorder
{
public CreditCategorySearchBorder(final Context ctx)
    {
        super(ctx, CreditCategory.class, new CreditCategorySearchWebControl());       

        
    // Credit Category ID
    addAgent(new FindSearchAgent(CreditCategoryXInfo.CODE, CreditCategorySearchXInfo.CODE).addIgnore((Integer.valueOf(-1)))

     );
    
    // desc
    addAgent(new WildcardSelectSearchAgent(CreditCategoryXInfo.DESC, CreditCategorySearchXInfo.DESC, true));

  }

}
