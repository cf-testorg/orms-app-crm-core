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
package com.redknee.app.crm.extension;

import com.redknee.app.crm.bean.SubscriberTypeEnum;

/**
 * This interface should be implemented by extensions that are valid for only a sub-set of subscriber types.
 * @author Marcio Marques
 * @since 8.5
 *
 */
public interface SubscriberTypeDependentExtension
{
    /**
     * Returns whether or not extension is valid for a given subscriber type.
     * @param subscriberType Subscriber type
     * @return Yes if valid, no otherwise.
     */
    public boolean isValidForSubscriberType(SubscriberTypeEnum subscriberType);
}
