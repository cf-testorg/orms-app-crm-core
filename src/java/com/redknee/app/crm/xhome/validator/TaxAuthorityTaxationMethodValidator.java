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

import com.redknee.app.crm.bean.TaxAuthority;
import com.redknee.app.crm.bean.TaxAuthorityTypeEnum;
import com.redknee.app.crm.bean.TaxAuthorityXInfo;
import com.redknee.app.crm.bean.TaxationMethodInfo;
import com.redknee.app.crm.bean.TaxationMethodInfoHome;
import com.redknee.framework.xhome.beans.CompoundIllegalStateException;
import com.redknee.framework.xhome.beans.IllegalPropertyArgumentException;
import com.redknee.framework.xhome.beans.Validator;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xlog.log.LogSupport;


/**
 * 
 *
 * @author bdhavalshankh
 * @since 9.5
 */
public class TaxAuthorityTaxationMethodValidator implements Validator
{
    private static Validator instance_;
    protected TaxAuthorityTaxationMethodValidator()
    {
    }

    public static Validator instance()
    {
        if (instance_ == null)
        {
            instance_ = new TaxAuthorityTaxationMethodValidator();
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

            if(taxAuth.getTaxAuthName() ==  null || taxAuth.getTaxAuthName().trim().equals(""))
            {
                cise.thrown(new IllegalPropertyArgumentException(TaxAuthorityXInfo.TAX_AUTH_NAME, "Tax Authority Name can not be empty"));
            }
            
           if(taxAuth.getTaxAuthorityType().getIndex() == TaxAuthorityTypeEnum.EXTERNAL_INDEX && taxAuth.getTaxationMethod() == -1)
           {
               cise.thrown(new IllegalArgumentException("Taxation Method Can Not Be Empty For External Type Of Tax Authority Type."));
           }
           
            try
            {
                Home home = (Home) ctx.get(TaxationMethodInfoHome.class);
                TaxationMethodInfo methodInfo  = null;
                methodInfo = (TaxationMethodInfo) home.find(ctx, taxAuth.getTaxationMethod());
                if(methodInfo != null && taxAuth.getSpid() != methodInfo.getSpid())
                {
                    cise.thrown(new IllegalArgumentException("SPID for Taxation Method and TaxAuthority can not be different "));
                }
            }
            catch (HomeException e)
            {
                LogSupport.info(ctx, this, "Error while getting TaxationMethodInfo");
            }
           
        }

        cise.throwAll();
    }
}
