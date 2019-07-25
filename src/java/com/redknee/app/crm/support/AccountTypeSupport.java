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

import com.redknee.app.crm.bean.AccountCategory;
import com.redknee.framework.xhome.context.Context;

/**
 * @author jchen
 */
public interface AccountTypeSupport<T extends AccountCategory> extends Support
{
	public T getTypedAccountType(Context ctx, long type);

   /**
    *
    * @param useFirstBeanDefault if set to true and the Account Type with the input key type is
    * not found, return the first Account Type in the AccountTypeHome instead of null
    */
	public T getTypedAccountType(Context ctx, long type,
	    boolean useFirstBeanDefault);
   
	public Class<T> getAccountTypeClass(Context ctx);
}
