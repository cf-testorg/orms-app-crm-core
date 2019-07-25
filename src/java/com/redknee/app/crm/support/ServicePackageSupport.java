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

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;

import com.redknee.app.crm.bean.core.ServicePackage;
import com.redknee.app.crm.bean.core.ServicePackageVersion;


/**
 * Support class for service packages.
 *
 * @author larry.xia@redknee.com
 */
public interface ServicePackageSupport extends Support
{
    /**
     * Returns a service package.
     *
     * @param ctx
     *            The operating context.
     * @param id
     *            Service package identifier.
     * @return The identified service package, or null if none exists.
     * @throws HomeException
     *             Thrown if there are problems looking up the service package.
     */
    public ServicePackage getServicePackage(final Context ctx, final int id) throws HomeException;


    /**
     * Returns the requested service package version.
     *
     * @param context
     *            The operating context.
     * @param id
     *            Service package identifier.
     * @param version
     *            Version of the service package to look up.
     * @return The requested service package version.
     * @throws HomeException
     *             Thrown if there are problems looking up the service package version.
     */
    public ServicePackageVersion getServicePackageVersion(final Context context, final int id, final int version)
        throws HomeException;


    /**
     * Returns the current version of a service package.
     *
     * @param ctx
     *            The operating context.
     * @param packageId
     *            Service package identifier.
     * @return The current version if the provided service package.
     * @throws HomeException
     *             Thrown if there are problems looking up the current version of the
     *             identified service package.
     */
    public ServicePackageVersion getCurrentVersion(final Context ctx, final int packageId) throws HomeException;


    /**
     * Returns the service package home.
     *
     * @param context
     *            The operating context.
     * @return Service package home.
     * @throws HomeException
     *             Thrown if service package home is not found.
     */
    public Home getServicePackageHome(final Context context) throws HomeException;


    /**
     * Returns the service package version home.
     *
     * @param context
     *            The operating context.
     * @return Service package version home.
     * @throws HomeException
     *             Thrown if service package home is not found.
     */
    public Home getServicePackageVersionHome(final Context context) throws HomeException;
}
