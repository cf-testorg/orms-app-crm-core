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
package com.redknee.app.crm.support;

import java.util.Set;

import com.redknee.framework.xhome.beans.xi.PropertyInfo;
import com.redknee.framework.xhome.context.Context;

import com.redknee.product.bundle.manager.provision.v5_0.bundle.BundleProfile;
import com.redknee.product.bundle.manager.provision.common.param.Parameter;

public interface BundleProfileSupport extends Support
{

    /**
     * depending on the License it will return the right Property Info
     * @param ctx
     * @return
     */
    public PropertyInfo getBundleProfileIdXInfo(Context ctx);

    
    /**
     * depending on the License it will return the right Property Info
     * @param ctx
     * @return
     */
    public PropertyInfo getBundleProfileNameXInfo(Context ctx);

    
    /**
     * depending on the License it will return the right Property Info
     * @param ctx
     * @return
     */
    public PropertyInfo getBundleCategoryTypeXInfo(Context ctx);

    
    /**
     * depending on the License it will return the right Property Info
     * @param ctx
     * @return
     */
    public PropertyInfo getBundleProfileSPIDXInfo(Context ctx);
    
    /**
     * maps the bundle type to unit types
     * @param enumIndex
     * @return
     */
    public Set getUnitTypes(Context ctx, short enumIndex);
    
    /**************************************************************************
     * Creates a string output of the BundleProfile2 object
     * @param bucketTemplate the bundle profile to do the toString for
     *************************************************************************/
    public String bucketTemplateToString(BundleProfile bucketTemplate);

    /**
     * Output ParamSets in the following format:
     * 
     * ARRAYLENGTH|PARAM1.ID|PARAM1.TYPE|PARAM1.VALUE|PARAM2.ID|PARAM2.TYPE|PARAM2.VALUE|...
     * 
     */
    public String paramSetToString(final Parameter[] inParamSet);

    public StringBuilder paramSetToString(final StringBuilder strBldr, final Parameter[] inParamSet);

    public String paramSetArrayToString(final Parameter[][] inParamSet);

    public StringBuilder paramSetArrayToString(final StringBuilder strBldr, final Parameter[][] inParamSet);

}
