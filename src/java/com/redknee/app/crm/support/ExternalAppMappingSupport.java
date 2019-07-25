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

import com.redknee.app.crm.bean.ServiceTypeEnum;
import com.redknee.app.crm.bean.service.ExternalAppMapping;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;

/**
 * Utility class for ExternalAppMapping
 * 
 * @author angie.li@redknee.com
 */
public interface ExternalAppMappingSupport extends Support
{
    public long getExternalApplicationId(final Context ctx, final ServiceTypeEnum serviceType) throws HomeException;


    public ExternalAppMapping getExternalAppMapping(final Context ctx, final ServiceTypeEnum serviceType)
            throws HomeException;


    public void addExternalAppMappingBeans(final Context ctx);


    public void addExternalAppMappingBeans(final Context ctx, final Home home);


    public void createExternalAppMapping(final Context ctx, final Home home, final ServiceTypeEnum type,
            final String handler);

}
