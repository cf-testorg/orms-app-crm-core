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

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.NullHome;
import com.redknee.framework.xhome.home.partition.NodeInfo;
import com.redknee.framework.xlog.log.InfoLogMsg;

import com.redknee.util.partitioning.config.PartitionEntity;
import com.redknee.util.partitioning.config.PartitionEntityHome;
import com.redknee.util.partitioning.config.PartitionTypeEnum;
import com.redknee.util.partitioning.partition.factory.ContextNodeHomeFactory;
import com.redknee.util.partitioning.partition.factory.DefaultNodeHomeFactory;
import com.redknee.util.partitioning.partition.factory.InvalidOperationNodeHomeFactory;
import com.redknee.util.partitioning.partition.factory.NodeHomeFactory;
import com.redknee.util.partitioning.partition.factory.RmiChannelOrNodeHomeFactory;


/**
 * 
 *
 * @author aaron.gourley@redknee.com
 * @since 
 */
public class DefaultConfigShareNodeHomeFactory extends DefaultNodeHomeFactory
{
    public DefaultConfigShareNodeHomeFactory()
    {
        super();
    }

    public DefaultConfigShareNodeHomeFactory(NodeHomeFactory localHomeDelegate)
    {
        super(localHomeDelegate);
    }

    public DefaultConfigShareNodeHomeFactory(Context ctx)
    {
        super(ctx, ContextNodeHomeFactory.instance());
    }

    public DefaultConfigShareNodeHomeFactory(Context ctx, NodeHomeFactory delegate)
    {
        super(ctx, delegate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Home createHome(Context ctx, Class beanClass, NodeInfo node)
    {
        Home partitionHome = (Home)ctx.get(PartitionEntityHome.class);
        if( partitionHome != null )
        {
            try
            {
                PartitionEntity partition = (PartitionEntity)partitionHome.find(ctx, node.getName());
                if( partition != null
                        && PartitionTypeEnum.REMOTE.equals(partition.getPartitionType()) )
                {
                    new InfoLogMsg(this, "Creating channelled 'or' home for partition: " + node.getName() + ", bean class: " + beanClass.getName(), null).log(ctx);
                    return new RmiChannelOrNodeHomeFactory(
                            ctx,
                            getDelegate(ctx), 
                            InvalidOperationNodeHomeFactory.instance()).createHome(ctx, beanClass, node);
                }
                else
                {
                    return super.createHome(ctx, beanClass, node);
                }
            }
            catch( HomeException he )
            {

            }
        }
        return NullHome.instance();
    }

}
