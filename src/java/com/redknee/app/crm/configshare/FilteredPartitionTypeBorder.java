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
package com.redknee.app.crm.configshare;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.web.border.Border;
import com.redknee.framework.xhome.webcontrol.RequestServicer;

import com.redknee.app.crm.support.HomeSupportHelper;
import com.redknee.util.partitioning.config.PartitionEntity;
import com.redknee.util.partitioning.config.PartitionEntityXInfo;
import com.redknee.util.partitioning.config.PartitionTypeEnum;


/**
 * 
 *
 * @author aaron.gourley@redknee.com
 * @since 
 */
public class FilteredPartitionTypeBorder implements Border
{
    public FilteredPartitionTypeBorder(PartitionTypeEnum type)
    {
        type_ = type;
    }
    
    /**
     * {@inheritDoc}
     */
    public void service(Context ctx, HttpServletRequest req, HttpServletResponse res, RequestServicer delegate)
            throws ServletException, IOException
    {
        Context sCtx = HomeSupportHelper.get(ctx).getWhereContext(
                ctx, 
                PartitionEntity.class, 
                new EQ(PartitionEntityXInfo.PARTITION_TYPE, type_), 
                true, PartitionEntityXInfo.PARTITION_ID);
        
        delegate.service(sCtx, req, res);
    }

    protected final PartitionTypeEnum type_;
}
