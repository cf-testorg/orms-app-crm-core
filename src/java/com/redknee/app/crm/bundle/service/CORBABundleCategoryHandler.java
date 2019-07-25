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
import java.util.HashSet;
import java.util.Set;

import com.redknee.app.crm.bundle.BundleCategory;
import com.redknee.app.crm.bundle.BundleCategoryHome;
import com.redknee.app.crm.bundle.BundleCategoryXInfo;
import com.redknee.app.crm.bundle.exception.BundleManagerException;
import com.redknee.app.crm.bundle.exception.CategoryAlreadyExistException;
import com.redknee.app.crm.bundle.exception.CategoryNotExistException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.elang.In;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xlog.log.LogSupport;
import com.redknee.framework.xlog.log.MinorLogMsg;

/**
 * Handles all services for categories in CRM from the CORBA perspective
 * @author 
 *
 */
public class CORBABundleCategoryHandler implements CRMBundleCategory
{

    /**
     * {@inheritDoc}
     */
    public Home getCategoriesByUnitType(Context ctx, int type)
            throws BundleManagerException
    {
            Home categoryHome = getBundleCategoryHome(ctx);
            categoryHome = categoryHome.where(ctx, new EQ(BundleCategoryXInfo.UNIT_TYPE, Integer.valueOf(type)));
            return categoryHome;    
    }

    /**
     * {@inheritDoc}
     */
   public Home getCategoriesByUnitTypeRange(Context ctx, Collection type)
            throws BundleManagerException
    {
          Set typeSet = new HashSet(type);
          Home categoryHome = getBundleCategoryHome(ctx);
          categoryHome = categoryHome.where(ctx, new In(BundleCategoryXInfo.UNIT_TYPE, typeSet));
          return categoryHome;
    }

   /**
    * {@inheritDoc}
    */
    public void createCategory(Context ctx, BundleCategory category)
            throws CategoryAlreadyExistException, BundleManagerException
    {
        Home categoryHome = getBundleCategoryHome(ctx);
        try
        {
            BundleCategory bundleCategory = null;
            try
            {
                bundleCategory = getCategory(ctx, category.getCategoryId());
            }
            catch (CategoryNotExistException e)
            {                
            }
            
            if (bundleCategory == null)
            {
                categoryHome.create(ctx, category);
            }
            else
            {
                throw new CategoryAlreadyExistException("Category ID " + category.getCategoryId());
            }
            

        }
        catch (HomeException e)
        {
            String msg = "Home exception while creating category id " + category.getCategoryId() + " on bundle manager: "
                    + e.getMessage();
            LogSupport.major(ctx, this, msg);
            throw new BundleManagerException(msg, e);
        }
    }

    /**
     * {@inheritDoc}
     */
   public BundleCategory getCategory(Context ctx, long categoryId)
            throws CategoryNotExistException, BundleManagerException
    {
       //EQ eq = new EQ(BundleCategoryXInfo.CATEGORY_ID, Long.valueOf(categoryId));

       Home categoryHome = getBundleCategoryHome(ctx);
       BundleCategory result = null;
       try
       {
    	   // find method works perfectly well with primary keys. no need to use elang.
           result = (BundleCategory) categoryHome.find(ctx, (int) categoryId);
       }
       catch (HomeException e)
       {
           String msg = "Home exception while getting bundle category with category ID" + categoryId + " on bundle manager: "
                   + e.getMessage();
           LogSupport.major(ctx, this, msg);
           throw new BundleManagerException(msg, e);
       }
       if (result == null)
       {
           throw new CategoryNotExistException("Bundle category id " + categoryId + " not found.");
       }         
       return result;
    }

   /**
    * {@inheritDoc}
    */
    public void removeCategory(Context ctx, long categoryId)
            throws CategoryNotExistException, BundleManagerException
    {
        Home categoryHome = getBundleCategoryHome(ctx);
        try
        {
           BundleCategory category = null;
           category = getCategory(ctx, categoryId);
           categoryHome.remove(category);
        }
        catch (HomeException e)
        {
            new MinorLogMsg(this, "Error encountered trying to remove category " + categoryId, e).log(ctx);
            throw new BundleManagerException(e);           
        }
    }

    /**
     * {@inheritDoc}
     */
    public void updateCategory(Context ctx, BundleCategory category)
            throws CategoryNotExistException, BundleManagerException
    {
        Home categoryHome = getBundleCategoryHome(ctx);
        try
        {   
            BundleCategory oldCategory = null;
            oldCategory = getCategory(ctx, category.getCategoryId());
            categoryHome.store(ctx, category);
        }
        catch (HomeException e)
        {
            String msg = "Home exception while updating bundle category with category ID"
                    + category.getCategoryId() + " on bundle manager: "
                    + e.getMessage();
            LogSupport.major(ctx, this, msg);
            throw new BundleManagerException(msg, e);
        }
    }

    private Home getBundleCategoryHome(Context ctx)
    {
        return (Home) ctx.get(BundleCategoryHome.class);
    }
}
