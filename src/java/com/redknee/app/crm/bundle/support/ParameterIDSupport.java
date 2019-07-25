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
package com.redknee.app.crm.bundle.support;

import com.redknee.app.crm.support.Support;
import com.redknee.product.bundle.manager.provision.common.param.Parameter;
import com.redknee.product.bundle.manager.provision.common.param.ParameterValue;

/**
 * Support class for dealing with Parameter ID on corba calls with Bundle Manager
 *
 */
public interface ParameterIDSupport extends Support
{
    public ParameterValue getParam(Parameter[] paramSet, short parameterId);
}
