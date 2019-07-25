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

import java.util.Map;

import com.redknee.framework.xhome.context.Context;

import com.redknee.app.crm.bean.AlcatelSSCProperty;
import com.redknee.app.crm.bean.KeyValueFeatureEnum;


/**
 * Support class for stuff shared by the 3 Alcatel SSC extensions.
 *
 * @author aaron.gourley@redknee.com
 * @since 8.2
 */
public interface AlcatelSSCSupport extends Support
{
    /**
     * This method ensures that the given key/value map contains all of the required
     * user-input keys for the given feature level.
     */
    public void initializeMap(Context ctx, KeyValueFeatureEnum feature, Map<String, AlcatelSSCProperty> keyValuePairs);
}
