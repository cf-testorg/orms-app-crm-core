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
package com.redknee.app.crm.bundle.service;

import java.util.Collection;

import com.redknee.app.crm.bundle.exception.BundleManagerException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;


/**
 * Business logic to operate some CRM specific business logic into BM
 * @author arturo.medina@redknee.com
 *
 */
public interface CRMBundleCategory extends BundleManagerCategory
{
    /**
     * Retrieves a set of categories based on the desired Unit type
     * @param ctx
     * @param type
     * @return
     * @throws BundleManagerException 
     */
    public Home getCategoriesByUnitType(Context ctx, int type)
        throws BundleManagerException;
    
    /**
     * Retrieves a set of categories based on the desired Unit type
     * @param ctx
     * @param type
     * @return
     * @throws BundleManagerException 
     */
    public Home getCategoriesByUnitTypeRange(Context ctx, Collection type)
        throws BundleManagerException;

}
