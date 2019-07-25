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
package com.redknee.app.crm.home;

import com.redknee.framework.xhome.beans.xi.PropertyInfo;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.And;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;

import com.redknee.app.crm.bean.GLCodeMapping;
import com.redknee.app.crm.bean.GLCodeMappingHome;
import com.redknee.app.crm.bean.GLCodeMappingXInfo;
import com.redknee.app.crm.xhome.home.SimpleBeanERHome;

/**
 * HLD section 8.1.1.83 has GL Code Management ER specification.
 *
 * @author angie.li@redknee.com
 */
public
class GLCodeERLogHome
    extends SimpleBeanERHome
{
    public GLCodeERLogHome(final Home delegate)
    {
        super(delegate, IDENTIFIER, CLASS, TITLE, FIELDS);
    }

    private static final int IDENTIFIER = 1116;
    private static final int CLASS = 700;
    private static final String TITLE = "GL Code Management";

    private static final PropertyInfo[] FIELDS =
    {
        GLCodeMappingXInfo.SPID,
        GLCodeMappingXInfo.GL_CODE,
        GLCodeMappingXInfo.DEBIT_GL_CODE,
        GLCodeMappingXInfo.CREDIT_GL_CODE,
        GLCodeMappingXInfo.DESCRIPTION,
        GLCodeMappingXInfo.GL_ACCOUNT_NUMBER,
        GLCodeMappingXInfo.GL_DEPARTMENT_ID,
        GLCodeMappingXInfo.GL_PRODUCT_CODE,
        GLCodeMappingXInfo.STATE
    };

    protected Object getOriginal(final Context context, final Object object) throws HomeException
    {
        final GLCodeMapping newBean = (GLCodeMapping)object;

        final Home home = (Home)context.get(GLCodeMappingHome.class);

        final And criteria = new And();
        criteria.add(new EQ(GLCodeMappingXInfo.SPID, newBean.getSpid()));
        criteria.add(new EQ(GLCodeMappingXInfo.GL_CODE, newBean.getGlCode()));

        return home.find(context, criteria);
    }
    
}
