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

import com.redknee.app.crm.bean.DeploymentTypeEnum;


/**
 * Provides support for the enumeration of the types of ways in which CRM may be
 * deployed.
 *
 * @author gary.anderson@redknee.com
 */
public interface DeploymentTypeSupport extends Support
{
    /**
     * Defines the key used to look-up type cnfiguration in the System
     * properties.
     */
    public static final String SYSTEM_PROPERTY_KEY = "com.redknee.app.crm.deploymenttype";

    public static final String IS_BAS_CTX_KEY = "is_bas";

    public static final String IS_ECARE_CTX_KEY = "is_ecare";

    public static final String IS_INVOICE_SERVER_CTX_KEY = "is_invoice_server";

    /**
     * Provides a method to set-up the given context to indicate the type of CRM
     * deployment.  This method is intended for calling at application start-up.
     * This method is offerred outside of the Install.execute() because packages
     * that depend upon AppCrm may need to perform this initialization before
     * Install.execute() can be called.
     *
     * @param context The applicatino context in which to perform the
     * initialization.
     *
     * @exception IllegalStateException Thrown if the system configuration is
     * set inappropriately.
     *
     * @see com.redknee.app.crm.bean.DeploymentTypeEnum#getTypeForSystemProperty
     */
    public void initializeDeploymentType(final Context context);


    /**
     * Indicates whether or not this version of CRM is deployed as a BAS node.
     *
     * @param context The operating context.
     * @return True if this version of CRM is deployed as a BAS node; false otherwise.
     */
    public boolean isBas(final Context context);


    /**
     * Indicates whether or not this version of CRM is deployed as an E-Care
     * node.
     *
     * @param context The operating context.
     * @return True if this version of CRM is deployed as an E-Care node; false otherwise.
     */
    public boolean isEcare(final Context context);


    /**
     * Indicates whether or not this version of CRM is deployed as an E-Care
     * node.
     *
     * @param context The operating context.
     * @return True if this version of CRM is deployed as an E-Care node; false otherwise.
     */
    public boolean isSingle(final Context context);


    /**
     * Indicates whether or not this is a CRM Invoice Server deployment.
     *
     * @param context The operating context.
     * @return True if this is an CRM Invoice Server deployment; false otherwise.
     */
    public boolean isInvoiceServer(final Context context);

    /**
     * Provides a method, primarily for the start-up routine, to determine a
     * deployment type based on a configured system property.
     *
     * @return The DeploymentTypeEnum of the current system.  In the absence of
     * any configuration, the type is assumed to be stand-alone.
     *
     * @exception IllegalStateException Thrown if the configuration indicates an
     * unsupported type.
     */
    public DeploymentTypeEnum getTypeForSystemProperty();

} // class
