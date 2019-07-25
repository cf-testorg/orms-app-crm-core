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
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.HomeInternalException;

import com.redknee.app.crm.bean.GLCodeMapping;


/**
 * Support methods for GLCode access.
 *
 * @author arturo.medina@redknee.com
 */
public interface GLCodeSupport extends Support
{
    /**
     * Returns all the GL codes of a service provider.
     *
     * @param ctx
     *            The operating context.
     * @param spid
     *            Service provider identifier.
     * @return A collection of all GL codes of a service provider.
     * @throws HomeException
     *             Thrown if there are problems retrieving the GL codes.
     * @throws HomeInternalException
     *             Thrown if there are irrecoverable problems retrieving the GL codes.
     */
    public Collection<GLCodeMapping> getGLCodes(final Context ctx, final int spid) throws HomeInternalException, HomeException;


    /**
     * Returns a GL code by its description and service provider.
     *
     * @param ctx
     *            The operating context.
     * @param desc
     *            GL code description.
     * @param spid
     *            Service provider identifier.
     * @return A GL code of the provided service provider and with the provided
     *         description.
     * @throws HomeException
     *             Thrown if there are problems looking up the GL code.
     */
    public GLCodeMapping getGLCodeFromDescriptionBySpid(final Context ctx, final String desc, final int spid) throws HomeException;

}
