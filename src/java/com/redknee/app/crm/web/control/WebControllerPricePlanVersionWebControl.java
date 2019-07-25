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


import com.redknee.app.crm.bean.PricePlanVersion;


/**
 * Provides a custom WebController for the versions of a price plan that set up
 * the action links and buttons properly.
 *
 * @author gary.anderson@redknee.com
 */
public
class WebControllerPricePlanVersionWebControl
    extends WebControllerWebControl57
{
    /**
     * Creates a new WebController. test
     */
    public WebControllerPricePlanVersionWebControl()
    {
        super(PricePlanVersion.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String createSQLClause(Object key)
    {
        return "id = " + key;
    }

} // class
