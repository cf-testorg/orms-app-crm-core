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
package com.redknee.app.crm.xhome.validator;

import java.util.Collection;

import com.redknee.framework.xhome.beans.CompoundIllegalStateException;
import com.redknee.framework.xhome.beans.IllegalPropertyArgumentException;
import com.redknee.framework.xhome.beans.Validator;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.And;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xlog.log.MinorLogMsg;

import com.redknee.app.crm.bean.TaxAuthority;
import com.redknee.app.crm.bean.TaxAuthorityXInfo;
import com.redknee.app.crm.support.HomeSupportHelper;


/**
 * 
 *
 * @author aaron.gourley@redknee.com
 * @since 
 */
public class TaxAuthorityShortCodeValidator implements Validator
{
    private static Validator instance_;
    protected TaxAuthorityShortCodeValidator()
    {
    }

    public static Validator instance()
    {
        if (instance_ == null)
        {
            instance_ = new TaxAuthorityShortCodeValidator();
        }
        return instance_;
    }

    /**
     * {@inheritDoc}
     */
    public void validate(Context ctx, Object bean) throws IllegalStateException
    {
        CompoundIllegalStateException cise = new CompoundIllegalStateException();

        if (bean instanceof TaxAuthority)
        {
            TaxAuthority taxAuth = (TaxAuthority) bean;

            /*
             * TT 10052549040 - allow entries with more spaces as shortcode. XSLT must filter out 
             * the TAs with short-code as 'SPACE' before trying to match and list the entries.
             */
            if(taxAuth.getInvoiceShortCode()==null || taxAuth.getInvoiceShortCode().trim().length()<1)
                return;
            
            Collection taxAuthority=null;
            try
            {
                And filter = new And();
                filter.add(new EQ(TaxAuthorityXInfo.SPID, taxAuth.getSpid()));
                filter.add(new EQ(TaxAuthorityXInfo.INVOICE_SHORT_CODE, taxAuth.getInvoiceShortCode()));
                if (HomeSupportHelper.get(ctx).hasBeans(ctx, TaxAuthority.class, 2, filter))
                {
                    cise.thrown(new IllegalPropertyArgumentException(TaxAuthorityXInfo.INVOICE_SHORT_CODE, "Duplicate Invoice Short Code :"+ taxAuth.getInvoiceShortCode()));
                }
            }
            catch (HomeException e)
            {
                final String msg = "Error occurred checking for duplicate tax short code.";
                new MinorLogMsg(this, msg, e).log(ctx);
                cise.thrown(new IllegalPropertyArgumentException(TaxAuthorityXInfo.INVOICE_SHORT_CODE, msg));
            }
        }

        cise.throwAll();
    }

}
