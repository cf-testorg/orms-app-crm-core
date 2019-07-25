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

import com.redknee.app.crm.bundle.exception.BundleManagerException;
import com.redknee.app.crm.bundle.exception.CategoryAlreadyExistException;
import com.redknee.app.crm.bundle.exception.CategoryNotExistException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.app.crm.bundle.*;

/**
 * General API methods for handling Categories in Bundle manager
 * @author arturo.medina@redknee.com
 *
 */
public interface BundleManagerCategory
{
    /**
     * Creates a category in Bundle Manager
     * @param ctx the operating context
     * @param category the CRM Bundle Category to insert
     * @throws CategoryAlreadyExistException 
     * @throws BundleManagerException 
     */
    public void createCategory(Context ctx , BundleCategory category) 
        throws CategoryAlreadyExistException, BundleManagerException;
    
    /**
     * Updates a category in BM
     * @param ctx the operating context
     * @param category the CRM Bundle Category to insert
     * @throws CategoryNotExistException 
     * @throws BundleManagerException 
     */
    public void updateCategory(Context ctx , BundleCategory category) 
        throws CategoryNotExistException, BundleManagerException;
    
    /**
     * Removes a category in Bundle Manager 
     * @param ctx the operating context
     * @param categoryId the category Id to remove from BM
     * @throws CategoryNotExistException 
     * @throws BundleManagerException 
     */
    public void removeCategory(Context ctx , long categoryId)
        throws CategoryNotExistException, BundleManagerException;
    
    /**
     * Retrieves from Bundle Manager a Bundle Category and translates the information into a BundleCategory CRM object
     * @param ctx the operating context
     * @param categoryId the category Id to retrieve from BM
     * @return
     * @throws CategoryNotExistException 
     * @throws BundleManagerException 
     */
    public BundleCategory getCategory(Context ctx, long categoryId)
        throws CategoryNotExistException, BundleManagerException;
    
}
