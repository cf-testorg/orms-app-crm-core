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
package com.redknee.app.crm.extension;

import com.redknee.framework.xhome.context.Context;

/**
 * Represents and extension in which something must be done upon parent deactivation.
 * @author Marcio Marques
 * @since 8.5
 * 
 */
public interface DeactivableExtension
{
    /**
     * Action performed during parent deactivation
     * @param ctx
     * @throws ExtensionInstallationException
     */
    public void deactivate(Context ctx) throws ExtensionInstallationException;
    
}
