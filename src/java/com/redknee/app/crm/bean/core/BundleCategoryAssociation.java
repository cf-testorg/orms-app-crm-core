package com.redknee.app.crm.bean.core;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.context.ContextAware;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xlog.log.LogSupport;

import com.redknee.app.crm.bundle.BundleCategory;
import com.redknee.app.crm.bundle.exception.BundleManagerException;
import com.redknee.app.crm.bundle.exception.CategoryNotExistException;
import com.redknee.app.crm.bundle.service.CRMBundleCategory;

/**
 * Helper methods for BundleCategoryAssociation.
 *
 * @author Candy Wong
 **/
public class BundleCategoryAssociation extends com.redknee.app.crm.bundle.BundleCategoryAssociation implements ContextAware
{
    @Override
    public int getType()
    {
        if ( getContext() == null || getCategoryId()<=0 )
        {
           return super.getType();
        }
        else
        {
            try
            {
               BundleCategory category = getCategory(getContext());
    
               if ( category != null )
               {
                  return category.getUnitType().getBundleType().getIndex();
               }
            }
            catch (Throwable t)
            {
            }
    
            return super.getType();
        }
    }
    
    public BundleCategory getCategory(Context ctx)
    throws HomeException
 {
      CRMBundleCategory service = (CRMBundleCategory) ctx.get(CRMBundleCategory.class);
      BundleCategory cat = null;

      if (service != null)
      {
          try
          {
              cat = service.getCategory(ctx, getCategoryId());
          }
          catch (CategoryNotExistException e)
          {
              if (LogSupport.isDebugEnabled(ctx))
              {
                  String message = "CategoryNotExistException while getting the category " + getCategoryId();
                  LogSupport.debug(ctx, this, message, e);
              }
          }
          catch (BundleManagerException e)
          {
              if (LogSupport.isDebugEnabled(ctx))
              {
                  String message = "BundleManagerException while getting the category " + getCategoryId();
                  LogSupport.debug(ctx, this, message, e);
              }
          }
      }
      else
      {
          String message = "Bundle Category Service doesn't exists in the Context " + getCategoryId();
          LogSupport.crit(ctx, this, message);
      }

      return cat;
 }
    

    
    public Context getContext()
    {
       return ctx_;
    }


    public void setContext(Context ctx)
    {
       ctx_ = ctx;
    }

    static final long serialVersionUID = 1L;
    private transient Context ctx_;

}
