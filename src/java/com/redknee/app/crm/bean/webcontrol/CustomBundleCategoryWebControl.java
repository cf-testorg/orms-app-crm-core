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

import com.redknee.framework.xhome.webcontrol.FinalWebControl;
import com.redknee.framework.xhome.webcontrol.WebControl;

import com.redknee.app.crm.bundle.BundleCategoryWebControl;
import com.redknee.app.crm.bundle.web.SupportedUnitTypesWebControl;


/**
 * 
 *
 * @author aaron.gourley@redknee.com
 * @since 
 */
public class CustomBundleCategoryWebControl extends BundleCategoryWebControl
{
    /**
     * {@inheritDoc}
     */
    @Override
    public WebControl getUnitTypeWebControl()
    {
        return CUSTOM_UNIT_TYPE_WC;
    }

    public static final WebControl CUSTOM_UNIT_TYPE_WC = new FinalWebControl(new SupportedUnitTypesWebControl(false));
}
