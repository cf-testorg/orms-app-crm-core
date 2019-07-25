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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.servlet.ServletRequest;

import com.redknee.framework.xhome.beans.SafetyUtil;
import com.redknee.framework.xhome.beans.xi.PropertyInfo;
import com.redknee.framework.xhome.beans.xi.PropertyInfoSelectWebControl;
import com.redknee.framework.xhome.beans.xi.XInfo;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.webcontrol.SelectWebControl;


/**
 * This web control will filter PropertyInfo in the drop down based on the given bean XInfo
 * 
 * @author aaron.gourley@redknee.com
 */
public class EnhancedPropertyInfoSelectWebControl extends PropertyInfoSelectWebControl 
{
    public EnhancedPropertyInfoSelectWebControl(XInfo xInfo)
    {
        super(xInfo);
    }
    
    public EnhancedPropertyInfoSelectWebControl(XInfo xInfo, boolean autoPreview)
    {
        super(xInfo, autoPreview);
    }
    
    public EnhancedPropertyInfoSelectWebControl(XInfo xInfo, boolean autoPreview, boolean hideReadOnlyAndHiddenProperties, boolean sort, boolean includePropertyName, boolean usePropertyNameInFromWeb)
    {
        super(xInfo, autoPreview);
        hideReadOnlyAndHiddenProperties_ = hideReadOnlyAndHiddenProperties;
        sort_ = sort;
        includePropertyName_ = includePropertyName;
        usePropertyNameInFromWeb_ = usePropertyNameInFromWeb;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SelectWebControl setCollection(Collection coll)
    {
        if (hideReadOnlyAndHiddenProperties_)
        {
            Collection newColl = new ArrayList();
            for (Object obj : coll)
            {
                if (obj instanceof PropertyInfo)
                {
                    PropertyInfo prop = (PropertyInfo) obj;
                    if (!prop.getAttributes().containsKey("HIDDEN")
                            && !prop.getAttributes().containsKey("READ-ONLY"))
                    {
                        newColl.add(obj);
                    }
                }
            }
            coll = newColl;
        }
        if (sort_)
        {
            if (!(coll instanceof ArrayList))
            {
                coll = new ArrayList(coll);
            }
            Collections.sort((List)coll);
        }
        return super.setCollection(coll);
    }

    /**
     * Override the Description with the name of the bean property as well as the label.
     */
    @Override
    public String getDesc(Context ctx, Object obj)
    {
        String desc = super.getDesc(ctx, obj);
        
        desc = desc.replaceAll("\\&nbsp;", " ");
        
        if (obj instanceof PropertyInfo)
        {
            PropertyInfo p = (PropertyInfo) obj;

            if (desc == null || desc.trim().length() == 0)
            {
                desc = p.getName();
            }
            
            if (includePropertyName_
                    && !SafetyUtil.safeEquals(p.getName(), desc))
            {
                desc = p.getName() + " (" + desc + ")";
            }
        }
        
        return desc;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Object fromWeb(Context ctx, ServletRequest req, String name)
    {
        Object ret = super.fromWeb(ctx, req, name);
        if (usePropertyNameInFromWeb_
                && ret instanceof PropertyInfo)
        {
            ret = ((PropertyInfo)ret).getName();
        }
        return ret;
    }

    protected boolean hideReadOnlyAndHiddenProperties_ = false;
    protected boolean sort_ = true;
    protected boolean includePropertyName_ = true;
    protected boolean usePropertyNameInFromWeb_ = false;
}
