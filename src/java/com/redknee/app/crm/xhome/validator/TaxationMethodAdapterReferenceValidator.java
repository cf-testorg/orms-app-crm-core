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

import java.lang.reflect.Type;

import com.redknee.app.crm.bean.TaxationMethodInfo;
import com.redknee.app.crm.taxation.TaxationMethod;
import com.redknee.framework.xhome.beans.CompoundIllegalStateException;
import com.redknee.framework.xhome.beans.Validator;
import com.redknee.framework.xhome.context.Context;


public class TaxationMethodAdapterReferenceValidator implements Validator
{
    /**
     * @author bdhavalshankh
     * @since 9.5
     */

    private static Validator instance_;


    protected TaxationMethodAdapterReferenceValidator()
    {
    }


    public static Validator instance()
    {
        if (instance_ == null)
        {
            instance_ = new TaxationMethodAdapterReferenceValidator();
        }
        return instance_;
    }


    /**
     * {@inheritDoc}
     */
    public void validate(Context ctx, Object bean) throws IllegalStateException
    {
        CompoundIllegalStateException cise = new CompoundIllegalStateException();

        if (bean instanceof TaxationMethodInfo)
        {
            TaxationMethodInfo taxMethod = (TaxationMethodInfo) bean;
            if(taxMethod.getAdapter() != null) 
            {
                Class cls = null;
                boolean isImplemented = false;
                try
                {
                    String ref = taxMethod.getAdapter();//"com.redknee.app.crm.mediation.taxation.CT23RuleImpl";
                    cls = Class.forName(ref);
                    
                    if(cls != null)
                    {
                        Type[] implementedInterfaces =  cls.getGenericInterfaces();
                        if (implementedInterfaces.length == 0)
                        {
                            cise.thrown(new IllegalArgumentException("Adapter Class supplied must implement interface - TaxationMethod. No other classes are accepted."));
                        }
                        else
                        {
                            for (Type intf : implementedInterfaces)
                            {
                                if(intf.equals(TaxationMethod.class))
                                {
                                    isImplemented = true; 
                                    break;
                                 }
                            }
                            if(isImplemented == false)    
                            {
                                cise.thrown(new IllegalArgumentException("Adapter Class supplied must implement interface - TaxationMethod. No other classes are accepted."));
                            }
                          }
                     }
                }
                catch (ClassNotFoundException e)
                {
                    cise.thrown(new IllegalArgumentException("Adapter not present"));
                }
            }
        }
        cise.throwAll();
    }
}
