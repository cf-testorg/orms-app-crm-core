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
package com.redknee.app.crm.web.control;

import java.io.PrintWriter;
import javax.servlet.ServletRequest;

import com.redknee.framework.xhome.beans.xi.PropertyInfo;
import com.redknee.framework.xhome.beans.xi.PropertyInfoAware;
import com.redknee.framework.xhome.beans.xi.XInfo;
import com.redknee.framework.xhome.beans.xi.XInfoAware;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.webcontrol.AbstractWebControl;


/**
 * This web control will filter PropertyInfo in the drop down based on the given bean XInfo
 * 
 * @author aaron.gourley@redknee.com
 * @since 8.6
 */
public class DynamicPropertyInfoSelectWebControl extends EnhancedPropertyInfoSelectWebControl 
{
    public DynamicPropertyInfoSelectWebControl(XInfo defaultXInfo)
    {
        super(defaultXInfo);
    }

    public DynamicPropertyInfoSelectWebControl(XInfo defaultXInfo, boolean autoPreview)
    {
        super(defaultXInfo, autoPreview);
    }

    public DynamicPropertyInfoSelectWebControl(XInfo defaultXInfo, boolean autoPreview, boolean hideReadOnlyAndHiddenProperties, boolean sort, boolean includePropertyName,
            boolean usePropertyNameInFromWeb)
    {
        super(defaultXInfo, autoPreview, hideReadOnlyAndHiddenProperties, sort, includePropertyName, usePropertyNameInFromWeb);
    }


    /**
     * Dynamically retrieve the XInfo from XInfoAware or PropertyInfoAware parent beans.
     * 
     * @param ctx Operating context
     * @return XInfo that the parent bean wants this web control to use.
     */
    public XInfo getXInfo(Context ctx)
    {
        Object object = ctx.get(AbstractWebControl.BEAN);
        if (object instanceof XInfoAware)
        {
            return ((XInfoAware) object).getXInfo(ctx);
        }
        else if (object instanceof PropertyInfoAware)
        {
            PropertyInfo propertyInfo = ((PropertyInfoAware) object).getPropertyInfo();
            if (propertyInfo != null)
            {
                return propertyInfo.getXInfo();
            }
        }
        return super.getXInfo();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public XInfo getXInfo()
    {
        throw new UnsupportedOperationException("getXInfo() not supported by " + this.getClass().getName() + ".  Use context aware version of method.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void toWeb(Context ctx, PrintWriter out, String name, Object obj)
    {
        // The super-class does not call getXInfo(Context), so delegate to one that does.
        new DynamicXInfoDelegateWebControl(ctx).toWeb(ctx, out, name, obj);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object fromWeb(Context ctx, ServletRequest req, String name)
    {
        // The super-class does not call getXInfo(Context), so delegate to one that does.
        return new DynamicXInfoDelegateWebControl(ctx).fromWeb(ctx, req, name);
    }
    
    /**
     * This inner class is a copy of 'this' in every way except it dynamically sets the XInfo from the context.
     * 
     * @author aaron.gourley@redknee.com
     * @since 8.6
     */
    private class DynamicXInfoDelegateWebControl extends EnhancedPropertyInfoSelectWebControl
    {
        public DynamicXInfoDelegateWebControl(Context ctx)
        {
            super(
                    DynamicPropertyInfoSelectWebControl.this.getXInfo(ctx),
                    DynamicPropertyInfoSelectWebControl.this.autoPreview_,
                    DynamicPropertyInfoSelectWebControl.this.hideReadOnlyAndHiddenProperties_,
                    DynamicPropertyInfoSelectWebControl.this.sort_,
                    DynamicPropertyInfoSelectWebControl.this.includePropertyName_,
                    DynamicPropertyInfoSelectWebControl.this.usePropertyNameInFromWeb_);
            
            coll_ = DynamicPropertyInfoSelectWebControl.this.coll_;
            noSelectionMsg_ = DynamicPropertyInfoSelectWebControl.this.noSelectionMsg_;
            optional_ = DynamicPropertyInfoSelectWebControl.this.optional_;
            optionalValue_ = DynamicPropertyInfoSelectWebControl.this.optionalValue_;
            selectFilter_ = DynamicPropertyInfoSelectWebControl.this.selectFilter_;
            listSize_ = DynamicPropertyInfoSelectWebControl.this.listSize_;
        }
    }
}
