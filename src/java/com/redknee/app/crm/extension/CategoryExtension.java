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

/**
 * Categories extensions should implement this interface.
 * @author Marcio Marques
 * @since 8.5
 *
 */
public interface CategoryExtension
{
    /**
     * Returns the maximum number of children a category can have.
     * @return Maximum number of children categories.
     */
    public int getMaxChildren();
}
