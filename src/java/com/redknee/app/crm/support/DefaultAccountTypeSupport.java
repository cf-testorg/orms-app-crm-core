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

import com.redknee.app.crm.bean.core.AccountCategory;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.True;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xlog.log.DebugLogMsg;
import com.redknee.framework.xlog.log.MinorLogMsg;

/**
 * @author jchen
 */
public class DefaultAccountTypeSupport<T extends AccountCategory> implements
    AccountTypeSupport<T>
{
	protected static AccountTypeSupport<AccountCategory> instance_ = null;
	protected final Class<T> cls_;
    
    public static AccountTypeSupport<AccountCategory> instance()
    {
        if (instance_ == null)
        {
			instance_ =
			    new DefaultAccountTypeSupport<AccountCategory>(
			        AccountCategory.class);
        }
        return instance_;
    }

	protected DefaultAccountTypeSupport(Class<T> cls)
    {
		cls_ = cls;
    }

	/**
	 * @param ctx
	 * @param type
	 * @return
	 */
    @Override
	public T getTypedAccountType(Context ctx, long type)
    {
		T objType = null;
        try
        {
			objType = HomeSupportHelper.get(ctx).findBean(ctx, cls_, type);
        }
        catch (final HomeException exception)
        {
            String msg = "Error retrieving account type with ID " + type;
            new DebugLogMsg(this, msg, exception).log(ctx);
            new MinorLogMsg(this, msg + ": " + exception.getMessage(), null).log(ctx);
            objType =  null;
        }

        return objType;
    }

   /**
    *
    * @param useFirstBeanDefault if set to true and the Account Type with the input key type is
    * not found, return the first Account Type in the AccountTypeHome instead of null
    */
	@Override
	public T
	    getTypedAccountType(Context ctx, long type, boolean useFirstBeanDefault)
   {
		T result = getTypedAccountType(ctx, type);
      if (result == null
              && useFirstBeanDefault)
      {
         // return the first item
         try
         {
				result =
				    HomeSupportHelper.get(ctx).findBean(ctx, cls_,
				        True.instance());
         }
         catch (HomeException hEx)
         {
            // eat it. There's nothing much I can do
         }
      }
      return result;
   }
   
	/**
	 * @param ctx
	 * @return
	 * @see com.redknee.app.crm.support.AccountTypeSupport#getAccountTypeClass(com.redknee.framework.xhome.context.Context)
	 */
	@Override
	public Class<T> getAccountTypeClass(Context ctx)
	{
		return cls_;
	}

}
