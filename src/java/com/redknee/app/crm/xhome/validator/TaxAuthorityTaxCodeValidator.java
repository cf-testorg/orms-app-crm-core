/*
 * This code is a protected work and subject to domestic and international copyright
 * law(s). A complete listing of authors of this work is readily available. Additionally,
 * source code is, by its very nature, confidential information and inextricably contains
 * trade secrets and other information proprietary, valuable and sensitive to Redknee. No
 * unauthorized use, disclosure, manipulation or otherwise is permitted, and may only be
 * used in accordance with the terms of the license agreement entered into with Redknee
 * Inc. and/or its subsidiaries.
 * 
 * Copyright (c) Redknee Inc. and its subsidiaries. All Rights Reserved.
 */
package com.redknee.app.crm.xhome.validator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.redknee.app.crm.bean.TaxAuthority;
import com.redknee.app.crm.bean.TaxAuthorityXInfo;
import com.redknee.app.crm.bean.TaxComponent;
import com.redknee.framework.xhome.beans.CompoundIllegalStateException;
import com.redknee.framework.xhome.beans.IllegalPropertyArgumentException;
import com.redknee.framework.xhome.beans.Validator;
import com.redknee.framework.xhome.context.Context;


/**
 * 
 * 
 * @author bhupendra.pandey@redknee.com
 * @since
 */
public class TaxAuthorityTaxCodeValidator implements Validator
{

    private static Validator instance_;


    protected TaxAuthorityTaxCodeValidator()
    {
    }


    public static Validator instance()
    {
        if (instance_ == null)
        {
            instance_ = new TaxAuthorityTaxCodeValidator();
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
            List<TaxComponent> taxComponentList = taxAuth.getTaxComponents();
            Set<String> taxCodeSet = new HashSet<String>();
            for (Object obj : taxAuth.getTaxComponents())
            {
                TaxComponent taxComponent = (TaxComponent) obj;
                if (taxComponent.getTaxCode().equals(taxAuth.getTaxCode()))
                {
                    cise
                            .thrown(new IllegalPropertyArgumentException(TaxAuthorityXInfo.TAX_CODE,
                                    "Duplicate Tax Code."));
                }
                if (taxCodeSet.contains(taxComponent.getTaxCode()))
                {
                    cise.thrown(new IllegalPropertyArgumentException(TaxAuthorityXInfo.TAX_COMPONENTS,
                            "Duplicate Tax Code."));
                }
                taxCodeSet.add(taxComponent.getTaxCode());
            }
        }
        cise.throwAll();
    }
}
