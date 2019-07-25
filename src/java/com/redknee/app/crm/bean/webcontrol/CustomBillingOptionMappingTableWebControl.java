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

import com.redknee.app.crm.bean.BillingOptionMappingTableWebControl;


/**
 * 
 *
 * @author aaron.gourley@redknee.com
 * @since 
 */
public class CustomBillingOptionMappingTableWebControl extends BillingOptionMappingTableWebControl
{
    @Override
    public WebControl getCallTypeWebControl()
    {
        return CustomBillingOptionMappingWebControl.CUSTOM_CALL_TYPE_WC;
    }


    @Override
    public WebControl getZoneIdentifierWebControl()
    {
        return CustomBillingOptionMappingWebControl.CUSTOM_ZONE_IDENTIFIER_WC;
    }


    @Override
    public WebControl getUsageTypeWebControl()
    {
        return CustomBillingOptionMappingWebControl.CUSTOM_USAGE_TYPE_WC;
    }


    @Override
    public WebControl getTaxAuthorityWebControl()
    {
        return CustomBillingOptionMappingWebControl.CUSTOM_TAX_AUTHORITY_WC;
    }


    @Override
    public WebControl getTaxAuthority2WebControl()
    {
        return CustomBillingOptionMappingWebControl.CUSTOM_TAX_AUTHORITY_2_WC;
    }


    @Override
    public WebControl getBillingCategoryWebControl()
    {
        return CustomBillingOptionMappingWebControl.CUSTOM_BILLING_CATEGORY_WC;
    }
}
