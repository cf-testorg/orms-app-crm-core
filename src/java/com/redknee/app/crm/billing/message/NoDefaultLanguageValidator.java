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
package com.redknee.app.crm.billing.message;

import com.redknee.app.crm.CoreCrmLicenseConstants;
import com.redknee.app.crm.bean.SpidLangMsgConfig;
import com.redknee.app.crm.support.LicensingSupportHelper;

import com.redknee.framework.xhome.beans.Validator;
import com.redknee.framework.xhome.context.Context;

/**
 * This Validator is used on SpidLangMsgConfig beans.
 * 
 * This validator will place a limitation on the decorated SpidLangMsgConfig bean
 * and not allow it to configure any DEFAULT_LANGUAGE records.
 * @author angie.li@redknee.com
 *
 */
public class NoDefaultLanguageValidator implements Validator 
{

    public void validate(Context ctx, Object obj)
            throws IllegalStateException 
    {
        final boolean multiLicense = LicensingSupportHelper.get(ctx).isLicensed(ctx, CoreCrmLicenseConstants.MULTI_LANGUAGE);
        SpidLangMsgConfig config = (SpidLangMsgConfig) obj;
        if (multiLicense && config.getLanguage().equals(SpidLangMsgConfig.DEFAULT_LANGUAGE))
        {
            throw new IllegalStateException("Do not configure using a DEFAULT language.  Choose a specific language.");
        }
    }

}
