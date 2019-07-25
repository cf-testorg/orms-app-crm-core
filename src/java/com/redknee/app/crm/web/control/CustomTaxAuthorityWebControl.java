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
package com.redknee.app.crm.web.control;

import com.redknee.app.crm.bean.TaxAuthorityWebControl;
import com.redknee.app.crm.web.control.CustomizedTaxationMethodInfoKeyWebControl;
import com.redknee.framework.xhome.webcontrol.WebControl;


public class CustomTaxAuthorityWebControl extends TaxAuthorityWebControl
{
    /**
     * @author bdhavalshankh
     * @since 9.5
     */

    
    @Override
    public WebControl getTaxationMethodWebControl()
    {
        return CUSTOM_TAXATION_METHOD_WC;
    }
    
    public static final WebControl CUSTOM_TAXATION_METHOD_WC = new CustomizedTaxationMethodInfoKeyWebControl();
}
