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

import com.redknee.framework.xhome.xenum.AbstractEnum;


/**
 * This interface should be implemented by extensions that are valid for only a sub-set of auxiliary service types.
 * @author Marcio Marques
 * @since 9.1
 *
 */
public interface TypeDependentExtension
{
    /**
     * Returns whether or not extension is valid for a given type.
     * @param subscriberType Subscriber type
     * @return Yes if valid, no otherwise.
     */
    public boolean isValidForType(AbstractEnum enumType);

}
