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
package com.redknee.app.crm.xhome.home;

import java.util.Collection;

import com.redknee.framework.xhome.beans.Identifiable;
import com.redknee.framework.xhome.beans.SafetyUtil;
import com.redknee.framework.xhome.beans.XBeans;
import com.redknee.framework.xhome.beans.xi.PropertyInfo;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.AdapterHome;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.support.IdentitySupport;

import com.redknee.app.crm.xhome.adapter.TransientFieldResetAdapter;


/**
 * This home resets transient properties to default values before creating or storing.
 * It can perform a reset on all transient fields (default), or a specified list of
 * them for advanced use (e.g. optimization in special cases).
 * 
 * It is useful when the data layer does not play by the same rules as the databsae
 * homes (i.e. AbstractTransientHome).
 * 
 * It returns the object that was passed in so that the unmodified bean is available
 * for post-processing for create/store operations (i.e. we do not want to clear
 * s-transient data for the return call-flow, just for future retrievals).
 * 
 * @author aaron.gourley@redknee.com
 * @since 8.2
 */

public class TransientFieldResettingHome extends AdapterHome
{

    public TransientFieldResettingHome(Context ctx, Home delegate)
    {
        super(ctx, new TransientFieldResetAdapter(), delegate);
    }

    public TransientFieldResettingHome(Context ctx, Home delegate, PropertyInfo... propertiesToReset)
    {
        super(ctx, new TransientFieldResetAdapter(propertiesToReset), delegate);
    }

    public TransientFieldResettingHome(Context ctx, Home delegate, Collection<PropertyInfo> propertiesToReset)
    {
        super(ctx, new TransientFieldResetAdapter(propertiesToReset), delegate);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object create(Context ctx, Object obj) throws HomeException
    {
        final Object returnObj = super.create(ctx, obj);

        // XDBHome (and potentially other homes) may set the ID upon record creation.
        // In such cases, populate the ID of the passed in bean in preparation for
        // returning it.
        //
        // In general, the TransientFieldResettingHome should be placed as close as
        // possible to the low-level persistent home to prevent other fields from
        // being affected in a similar way.
        updateID(ctx, obj, returnObj);
        
        // Return the original object so that the returning home pipeline sees the original bean
        // with transient fields still intact.
        return obj;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object store(Context ctx, Object obj) throws HomeException
    {
        final Object returnObj = super.store(ctx, obj);

        // If for some reason the ID was changed by a low-level home (e.g. XDBHome),
        // populate the ID of the passed in bean in preparation for returning it.
        //
        // In general, the TransientFieldResettingHome should be placed as close as
        // possible to the low-level persistent home to prevent other fields from
        // being affected in a similar way.
        updateID(ctx, obj, returnObj);

        // Return the original object so that the returning home pipeline sees the original bean
        // with transient fields still intact.
        return obj;
    }

    
    private void updateID(Context ctx, Object obj, final Object returnObj)
    {
        if (returnObj instanceof Identifiable && obj instanceof Identifiable)
        {
            Identifiable idAwareReturnObj = (Identifiable) returnObj;
            Identifiable idAwareObj = (Identifiable) obj;
            
            final Object returnId = idAwareReturnObj.ID();
            final Object id = idAwareObj.ID();
            if (!SafetyUtil.safeEquals(id, returnId))
            {
                IdentitySupport idSupport = (IdentitySupport) XBeans.getInstanceOf(ctx, obj.getClass(), IdentitySupport.class);
                if (idSupport.isKey(returnId))
                {
                    idSupport.setID(obj, returnId);
                }
            }
        }
    }
}
