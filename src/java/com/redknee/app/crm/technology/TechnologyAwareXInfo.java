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
package com.redknee.app.crm.technology;

import java.util.Arrays;
import java.util.List;

import com.redknee.framework.xhome.beans.xi.AbstractPropertyInfo;
import com.redknee.framework.xhome.beans.xi.PropertyInfo;
import com.redknee.framework.xhome.beans.xi.XInfo;
import com.redknee.framework.xhome.context.Context;

/**
 * @author rattapattu
 */
public class TechnologyAwareXInfo implements XInfo
{
    static Context ctx_ = null;
    protected final static TechnologyAwareXInfo instance_ = new TechnologyAwareXInfo();

    protected TechnologyAwareXInfo()
    {
    }

    public static TechnologyAwareXInfo instance(Context ctx)
    {
        ctx_ = ctx;
        return instance_;
    }

    public String getName()
    {
        return "TechnologyAware";
    }

    public Class getBeanClass()
    {
        return TechnologyAware.class;
    }

    public final static short TECHNOLOGY_INDEX = 0;

    public static PropertyInfo TECHNOLOGY = new AbstractPropertyInfo("Technology")
    {
        public String getName()
        {
            return "Technology";
        }

        public String getLabel()
        {
            return "Technology";
        }
       
        public Class getType() { return TechnologyEnum.class; }

        public Object get(Object obj)
        {
            return ((TechnologyAware) obj).getTechnology();
        }

        public void set(Object obj, Object value) throws IllegalArgumentException
        {
            ((TechnologyAware) obj).setTechnology((TechnologyEnum) value);
        }

        public List getCollection()
        {
            return TechnologyAwareXInfo.instance(ctx_).getProperties(ctx_);
        }
    };

    public PropertyInfo getID()
    {
        return null;
    }

    public final static List PROPERTIES = Arrays.asList(new Object[]
            {
                TECHNOLOGY
            });

    public List getProperties(Context ctx)
    {
        return PROPERTIES;
    }

    public String getDescription(Context ctx, Object obj)
    {
        return "TechnologyAware" + obj.getClass().getSimpleName();
    }

    public String getHelp(Context ctx)
    {
        return "Technology Type: GSM/CDMA";
    }

    public String getLabel(Context ctx)
    {
        return "Technology";
    }

    public List getDefaultTableProperties(Context ctx)
    {
        return new java.util.ArrayList();
    }

    @Override
    public List<PropertyInfo> getPersistentProperties(Context ctx)
    {
        return PROPERTIES;
    }

    @Override
    public List<PropertyInfo> getMobileProperties(Context ctx)
    {
        return null;
    }

    @Override
    public List<PropertyInfo> getTabbedProperties(Context ctx)
    {
        return PROPERTIES;
    }

    @Override
    public PropertyInfo getPropertyInfo(Context ctx, String propertyName)
    {
        return TECHNOLOGY;
    }

    @Override
    public boolean hasTabs()
    {
        return false;
    }

}
