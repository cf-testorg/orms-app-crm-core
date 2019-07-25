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
package com.redknee.app.crm.delivery.email;

import java.util.Map;


/**
 * Deprecating this bean and all of its properties.  Generic KeyConfiguration feature
 * should be used instead.
 *
 * @author aaron.gourley@redknee.com
 * @since 8.2
 * 
 * @deprecated
 */
@Deprecated
public class KeywordConfiguration extends AbstractKeywordConfiguration
{

    @Deprecated
    public KeywordConfiguration()
    {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Deprecated
    public Map getSubstitutionConfig()
    {
        return super.getSubstitutionConfig();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Deprecated
    public void setSubstitutionConfig(Map substitutionConfig) throws IllegalArgumentException
    {
        super.setSubstitutionConfig(substitutionConfig);
    }

}
