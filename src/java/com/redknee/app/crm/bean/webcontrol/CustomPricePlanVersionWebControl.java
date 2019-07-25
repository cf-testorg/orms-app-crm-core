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
package com.redknee.app.crm.bean.webcontrol;

import com.redknee.framework.xhome.webcontrol.WebControl;

import com.redknee.app.crm.bean.PricePlanVersionWebControl;


/**
 * 
 *
 * @author aaron.gourley@redknee.com
 * @since 
 */
public class CustomPricePlanVersionWebControl extends PricePlanVersionWebControl
{
    /**
     * {@inheritDoc}
     */
    @Override
    public WebControl getServicePackageVersionWebControl()
    {
        return CUSTOM_SERVICE_PACKAGE_VERSION_WC;
    }

    public static final WebControl CUSTOM_SERVICE_PACKAGE_VERSION_WC = new com.redknee.app.crm.web.control.PricePlanVersionCustomServicePackageWebControl();
}
