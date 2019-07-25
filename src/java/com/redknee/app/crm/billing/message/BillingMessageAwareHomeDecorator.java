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

import com.redknee.framework.xhome.beans.CompoundValidator;
import com.redknee.framework.xhome.beans.Validator;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.HomeProxy;
import com.redknee.framework.xhome.home.ValidatingHome;

import com.redknee.util.snippet.log.Logger;

/**
 * Install the Homes needed for Billing Message support by the Bean.
 *
 * @author victor.stratan@redknee.com
 */
public class BillingMessageAwareHomeDecorator
{
    public Home decorateHome(final Context ctx, Home home)
    {
        /* Note on order of operations:
         * Validate before storing in to the BillingMessageHome.
         */

        if (home instanceof HomeProxy)
        {
            // Remove any existing BillingMessageSaveHome from the pipeline.
            // This can happen if the multiple callers try to register the same pipeline.
            for (Home temp = home; temp instanceof HomeProxy; temp = ((HomeProxy)temp).getDelegate(ctx))
            {
                HomeProxy proxy = (HomeProxy) temp;
                Home delegate = proxy.getDelegate(ctx);
                if (delegate instanceof BillingMessageSaveHome)
                {
                    BillingMessageSaveHome existingSharingHome = (BillingMessageSaveHome) delegate;
                    proxy.setDelegate(existingSharingHome.getDelegate(ctx));
                    existingSharingHome.setDelegate(null);
                }
            }

            final Home validatingHome = ((HomeProxy)home).findDecorator(ValidatingHome.class);
            if (validatingHome == null)
            {
                // Add the decorators on top.
                home = new BillingMessageSaveHome(home);
                home = new ValidatingHome(BillingMessageAwareValidator.instance(), home);
            }
            else
            {
                final ValidatingHome theValidatingHome = (ValidatingHome)validatingHome;

                final Validator createValidator = theValidatingHome.getCreateValidator();
                final Validator storeValidator = theValidatingHome.getStoreValidator();

                final CompoundValidator validator;
                if (createValidator instanceof CompoundValidator)
                {
                    validator = (CompoundValidator) createValidator;
                }
                else
                {
                    validator = new CompoundValidator();
                    validator.add(createValidator);
                }

                validator.add(BillingMessageAwareValidator.instance());

                if (storeValidator != createValidator)
                {
                    // yes, this is an instance check, not an equals() check
                    final CompoundValidator secondValidator;
                    if (createValidator instanceof CompoundValidator)
                    {
                        secondValidator = (CompoundValidator) storeValidator;
                    }
                    else
                    {
                        secondValidator = new CompoundValidator();
                        secondValidator.add(storeValidator);
                    }

                    validator.add(BillingMessageAwareValidator.instance());
                }

                // Add save home decorator after validation.
                theValidatingHome.addProxy(ctx, new BillingMessageSaveHome(null));
            }
        }
        else
        {
            //this is a fall back that should not happen.
            final String msg = "FIX THIS: Incorrect parameter type. Should be HomeProxy!";
            Logger.debug(ctx, this, msg, new HomeException(msg));
            home = new BillingMessageSaveHome(home);
            home = new ValidatingHome(BillingMessageAwareValidator.instance(), home);
        }

        return home;
    }
}
