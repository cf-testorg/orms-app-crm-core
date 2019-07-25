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

import java.util.Collection;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.And;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;

import com.redknee.app.crm.bean.GLCodeMapping;
import com.redknee.app.crm.bean.GLCodeMappingHome;
import com.redknee.app.crm.bean.GLCodeMappingXInfo;


/**
 * Support methods for GLCode access.
 *
 * @author arturo.medina@redknee.com
 */
public final class DefaultGLCodeSupport implements GLCodeSupport
{
    protected static GLCodeSupport instance_ = null;
    public static GLCodeSupport instance()
    {
        if (instance_ == null)
        {
            instance_ = new DefaultGLCodeSupport();
        }
        return instance_;
    }

    protected DefaultGLCodeSupport()
    {
    }


    /**
     * {@inheritDoc}
     */
    public Collection<GLCodeMapping> getGLCodes(final Context ctx, final int spid) throws HomeException
    {
        Collection<GLCodeMapping> glCodes = null;
        final Home glHome = (Home) ctx.get(GLCodeMappingHome.class);

        if (glHome != null)
        {
            glCodes = glHome.select(ctx, new EQ(GLCodeMappingXInfo.SPID, spid));
        }

        return glCodes;
    }

    /**
     * {@inheritDoc}
     */
    public GLCodeMapping getGLCodeFromDescriptionBySpid(final Context ctx, final String desc, final int spid)
        throws HomeException
    {
        final Home glCodeHome = (Home) ctx.get(GLCodeMappingHome.class);
        return (GLCodeMapping) glCodeHome.find(ctx, new And().add(new EQ(GLCodeMappingXInfo.GL_CODE, desc)).add(
            new EQ(GLCodeMappingXInfo.SPID, Integer.valueOf(spid))));
    }

}
