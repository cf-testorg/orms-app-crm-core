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
package com.redknee.app.crm.support;

import com.redknee.framework.xhome.context.Context;


/**
 * Checks if a License is enabled.
 * @author larry.xia@redknee.com
 */
public interface LicensingSupport extends Support
{
    /**
     * Checks if a License provided in the licenseKey parameter is enabled.
     * Very handy when you have to check one license on time.
     *
     * @param ctx the current context
     * @param licenseKey license key to check
     * @return true is license is enabled
     */
    public boolean isLicensed(final Context ctx, final String licenseKey);
}
