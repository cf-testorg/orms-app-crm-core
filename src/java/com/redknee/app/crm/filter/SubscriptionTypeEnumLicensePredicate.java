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
package com.redknee.app.crm.filter;

import java.util.HashMap;
import java.util.Map;

import com.redknee.framework.license.LicenseMgr;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.filter.Predicate;
import com.redknee.framework.xlog.log.MajorLogMsg;

import com.redknee.app.crm.CoreCrmLicenseConstants;
import com.redknee.app.crm.bean.account.SubscriptionTypeEnum;


/**
 * Provides a predicate to determine if a given SubscriptionTypeEnum is licensed.
 *
 * @author gary.anderson@redknee.com
 */
public class SubscriptionTypeEnumLicensePredicate
    implements Predicate
{
    /**
     * {@inheritDoc}
     */
    public boolean f(final Context context, final Object obj)
    {
        if (obj == null || !(obj instanceof SubscriptionTypeEnum))
        {
            return false;
        }
        
        final SubscriptionTypeEnum type = (SubscriptionTypeEnum)obj;
        if(SubscriptionTypeEnum.OPERATOR == type)
        {
            // operator is not subject to license.
            return true;
        }
        final String licenseName = LICENSE_NAMES.get(type);

        final boolean licensed;
        if (licenseName == null)
        {
            new MajorLogMsg(this, "Failed to find licence for type: " + type, null).log(context);
            licensed = false;
        }
        else
        {
            final LicenseMgr manager = (LicenseMgr)context.get(LicenseMgr.class);
            licensed = manager.isLicensed(context, licenseName);
        }

        return licensed;
    }


    /**
     * Provides a Map of enumeration value to license names.
     */
    static final Map<SubscriptionTypeEnum, String> LICENSE_NAMES;
    static
    {
        // The unit-tests should detect if a new enumeration value is added
        // without updating this map.
        LICENSE_NAMES = new HashMap<SubscriptionTypeEnum, String>();
        LICENSE_NAMES.put(SubscriptionTypeEnum.AIRTIME, CoreCrmLicenseConstants.AIRTIME_LICENSE_KEY);
        LICENSE_NAMES.put(SubscriptionTypeEnum.MOBILE_WALLET, CoreCrmLicenseConstants.MOBILE_WALLET_KEY);
        LICENSE_NAMES.put(SubscriptionTypeEnum.NETWORK_WALLET, CoreCrmLicenseConstants.NETWORK_WALLET_KEY);
        LICENSE_NAMES.put(SubscriptionTypeEnum.PREPAID_CALLING_CARD, CoreCrmLicenseConstants.PREPAID_CALLING_CARD_LICENSE_KEY);
        LICENSE_NAMES.put(SubscriptionTypeEnum.WIRE_LINE, CoreCrmLicenseConstants.WIRE_LINE_LICENSE_KEY);
        LICENSE_NAMES.put(SubscriptionTypeEnum.BROADBAND, CoreCrmLicenseConstants.BROADBAND_LICENSE_KEY);
    }

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;

}
