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
import com.redknee.framework.xhome.xenum.AbstractEnum;
import com.redknee.framework.xlog.log.MajorLogMsg;

import com.redknee.app.crm.CoreCrmLicenseConstants;
import com.redknee.app.crm.technology.TechnologyEnum;

/**
 * Based on the license installed in a map it will decide if this enum item will be displayed or not
 * @author arturo.medina@redknee.com
 *
 */
public class TechnologyLicenseEnumPredicateEvaluator extends
        AbstractPredicateEvaluator implements EnumPredicateEvaluator
{

    /**
     * 
     * @param delegate
     */
    public TechnologyLicenseEnumPredicateEvaluator(
            EnumPredicateEvaluator delegate)
    {
        super(delegate);
    }

    /**
     * {@inheritDoc}
     */
    public boolean evaluate(Context ctx, AbstractEnum value)
    {
        if (value == null || !(value instanceof TechnologyEnum))
        {
            return false;
        }

        final TechnologyEnum type = (TechnologyEnum)value;
        final String licenseName = LICENSES.get(type);

        final boolean licensed;
        if (licenseName == null)
        {
            new MajorLogMsg(this, "Failed to find licence for type: " + type, null).log(ctx);
            licensed = false;
        }
        else
        {
            final LicenseMgr manager = (LicenseMgr)ctx.get(LicenseMgr.class);
            licensed = manager.isLicensed(ctx, licenseName);
        }

        return (licensed || TechnologyEnum.NO_TECH.equals(type)) && delegate(ctx, value);
    }

    static Map<TechnologyEnum, String> LICENSES;
    static
    {
        LICENSES = new HashMap<TechnologyEnum, String>();
        LICENSES.put(TechnologyEnum.GSM, CoreCrmLicenseConstants.GSM_LICENSE_KEY);
        LICENSES.put(TechnologyEnum.CDMA, CoreCrmLicenseConstants.TDMA_CDMA_LICENSE_KEY);
        LICENSES.put(TechnologyEnum.TDMA, CoreCrmLicenseConstants.TDMA_CDMA_LICENSE_KEY);
        LICENSES.put(TechnologyEnum.VSAT_PSTN, CoreCrmLicenseConstants.VSAT_PSTN_LICENSE_KEY);
    }
}
