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
package com.redknee.app.crm.client;

import static com.redknee.app.crm.client.CorbaClientTrapIdDef.BUNDLE_CATEGORY_PROV_SVC_DOWN;
import static com.redknee.app.crm.client.CorbaClientTrapIdDef.BUNDLE_CATEGORY_PROV_SVC_UP;

import com.redknee.app.crm.support.BundleProfileSupportHelper;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xlog.log.DebugLogMsg;
import com.redknee.framework.xlog.log.InfoLogMsg;
import com.redknee.framework.xlog.log.LogSupport;
import com.redknee.framework.xlog.log.MinorLogMsg;
import com.redknee.product.bundle.manager.provision.common.param.Parameter;
import com.redknee.product.bundle.manager.provision.v5_0.category.Category;
import com.redknee.product.bundle.manager.provision.v5_0.category.CategoryProvision;
import com.redknee.product.bundle.manager.provision.v5_0.category.CategoryReturnParam;

/**
 * Provides an interface for communicating with Bundle Manager bundle category corba services
 * 
 * Support clustered corba client
 * Refactored reusable elements to an abstract class AbstractCrmClient<T>
 * @author rchen
 * @since June 29, 2009 
 * 
 */
public class BMBundleCategoryCorbaClient extends AbstractCrmClient<CategoryProvision> implements BundleCategoryProvisionClient 
{

    /**
     * Name of the CORBA client.
     */
    private static final String CLIENT_NAME = "BundleCategoryProvisionClient";

    private static final Class<CategoryProvision> SERVICE_TYPE = CategoryProvision.class;

    /**
     * Service description.
     */
    private static final String SERVICE_DESCRIPTION = "CORBA client for Bundle Category Provisioning services";

    public BMBundleCategoryCorbaClient(final Context ctx)
    {
        // TODO change the trap IDs
        super(ctx, CLIENT_NAME, SERVICE_DESCRIPTION, SERVICE_TYPE,
        BUNDLE_CATEGORY_PROV_SVC_DOWN, BUNDLE_CATEGORY_PROV_SVC_UP);
    }
    
    public short createCategory(Category category)
    {
        Parameter[] inParamSet = new Parameter[0];
        return createCategory(category, inParamSet);
    }


    private short createCategory(Category category, Parameter[] inParamSet)
    {
        short retInt = -1;
        CategoryProvision service = getService();
        if (service == null)
        {
            new InfoLogMsg(this, "createCategory(): unable to get service", null).log(getContext());
            return retInt;
        }
        try
        {
            if (LogSupport.isDebugEnabled(getContext()))
            {
                StringBuilder msg = new StringBuilder();
                msg.append("Attempting to createCategory() on bundle manager: ");
                msg.append("Category[");
                msg.append(category);
                msg.append("], inParamSet[");
                msg.append(BundleProfileSupportHelper.get(getContext()).paramSetToString(inParamSet));
                msg.append("]");
                LogSupport.debug(getContext(), this, msg.toString());
            }
            CategoryReturnParam result = service.createCategory(category, inParamSet);
            if (LogSupport.isDebugEnabled(getContext()))
            {
                new DebugLogMsg(this, "createCategory(): Result code = " + result.resultCode, null).log(getContext());
            }
            retInt = result.resultCode;
        }
        catch (org.omg.CORBA.COMM_FAILURE commFail)
        {
            retInt = COMMUNICATION_FAILURE;
        }
        catch(Throwable t)
        {
            retInt = COMMUNICATION_FAILURE;
        }
        return retInt;
    }


    public short updateCategory(int spId, int categoryId, Category category)
    {
        Parameter[] inParamSet = new Parameter[0];
        return updateCategory(spId, categoryId, category, inParamSet);
    }


    private short updateCategory(int spId, int categoryId, Category category, Parameter[] inParamSet)
    {
        short retInt = -1;
        CategoryProvision service = getService();
        if (service == null)
        {
            new InfoLogMsg(this, "updateCategory(): unable to get service", null).log(getContext());
            return retInt;
        }
        try
        {
            if (LogSupport.isDebugEnabled(getContext()))
            {
                StringBuilder msg = new StringBuilder();
                msg.append("Attempting to updateCategory() on bundle manager: ");
                msg.append("Category[");
                msg.append(category);
                msg.append("], inParamSet[");
                msg.append(BundleProfileSupportHelper.get(getContext()).paramSetToString(inParamSet));
                msg.append("]");
                LogSupport.debug(getContext(), this, msg.toString());
            }
            CategoryReturnParam result = service.updateCategory(categoryId, category, inParamSet);
            if (LogSupport.isDebugEnabled(getContext()))
            {
                new DebugLogMsg(this, "updateCategory(): Result code = " + result.resultCode, null).log(getContext());
            }
            retInt = result.resultCode;
        }
        catch (org.omg.CORBA.COMM_FAILURE commFail)
        {
            retInt = COMMUNICATION_FAILURE;
        }
        catch(Throwable t)
        {
            retInt = COMMUNICATION_FAILURE;
        }
        return retInt;
    }


    public short removeCategory(int spId, int categoryId)
    {
        Parameter[] inParamSet = new Parameter[0];
        return removeCategory(spId, categoryId, inParamSet);
    }


    private short removeCategory(int spId, int categoryId, Parameter[] inParamSet)
    {
        short retInt = -1;
        CategoryProvision service = getService();
        if (service == null)
        {
            new InfoLogMsg(this, "removeCategory(): unable to get service", null).log(getContext());
            return retInt;
        }
        try
        {
            if (LogSupport.isDebugEnabled(getContext()))
            {
                StringBuilder msg = new StringBuilder();
                msg.append("Attempting to removeCategory() on bundle manager: ");
                msg.append("Category[ID=");
                msg.append(categoryId);
                msg.append(", spid=");
                msg.append(spId);
                msg.append("], inParamSet[");
                msg.append(BundleProfileSupportHelper.get(getContext()).paramSetToString(inParamSet));
                msg.append("]");
                LogSupport.debug(getContext(), this, msg.toString());
            }
            CategoryReturnParam result = service.removeBundleCategory(spId, categoryId, inParamSet);
            if (LogSupport.isDebugEnabled(getContext()))
            {
                new DebugLogMsg(this, "removeCategory(): Result code = " + result.resultCode, null).log(getContext());
            }
            retInt = result.resultCode;
        }
        catch (org.omg.CORBA.COMM_FAILURE commFail)
        {
            retInt = COMMUNICATION_FAILURE;
        }
        catch(Throwable t)
        {
            retInt = COMMUNICATION_FAILURE;
        }
        return retInt;
    }


    public Category getBundleCategory(int spId, int categoryId)
    {
        Parameter[] inParamSet = new Parameter[0];
        return getBundleCategory(spId, categoryId, inParamSet);
    }


    private Category getBundleCategory(int spId, int categoryId, Parameter[] inParamSet)
    {
        Category retObj = null;
        CategoryProvision service = getService();
        if (service == null)
        {
            new InfoLogMsg(this, "getCategoryById(): unable to get service", null).log(getContext());
            return retObj;
        }
        try
        {
            if (LogSupport.isDebugEnabled(getContext()))
            {
                StringBuilder msg = new StringBuilder();
                msg.append("Attempting to getBundleCategory() on bundle manager: ");
                msg.append("Category[ID=");
                msg.append(categoryId);
                msg.append(", spid=");
                msg.append(spId);
                msg.append("], inParamSet[");
                msg.append(BundleProfileSupportHelper.get(getContext()).paramSetToString(inParamSet));
                msg.append("]");
                LogSupport.debug(getContext(), this, msg.toString());
            }
            CategoryReturnParam result = service.getBundleCategory(spId, categoryId, inParamSet);
            if (LogSupport.isDebugEnabled(getContext()))
            {
                new DebugLogMsg(this, "getBundleCategory(): Result code = " + result.resultCode, null).log(getContext());
            }
            if (result.resultCode == 0)
            {
                retObj = result.outBundleCategory;
            }
        }
        catch(Throwable t)
        {
            new MinorLogMsg(this, "CORBA communication failure.", t).log(getContext());
        }
        return retObj;
    }
    
    public String getServiceDescription()
    {
        return "CORBA client for Bundle Manager bundle category services.";
    }
    

    protected CategoryProvision categoryService_;
}
