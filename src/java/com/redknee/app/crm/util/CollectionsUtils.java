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
package com.redknee.app.crm.util;

import java.util.Collection;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.filter.Predicate;
import com.redknee.framework.xhome.visitor.AbortVisitException;

/**
 * 
 * A more type-safe way of filtering collections (avoiding unnecessary casts).
 * @author sbanerjee
 *
 */
public class CollectionsUtils
{
    /**
     * 
     * @param <BEAN>
     * @param ctx
     * @param dest
     * @param source
     * @param p
     */
    public static <BEAN> void filter(Context ctx, Collection<BEAN> dest, Collection<BEAN> source, TypedPredicate<BEAN> p)
    {
        if(source==null || source.size()<1)
        {
            dest.clear();
            return;
        }
        
        dest.clear();
        for(BEAN sourceBean : source)
        {
            try
            {
                if(p.f(ctx, sourceBean))
                    dest.add(sourceBean);
            } 
            catch (AbortVisitException e)
            {
                break;
            }
        }
    }
    
    /**
     * So we could use framework's Predicate structures
     * @param <BEAN>
     * @param ctx
     * @param dest
     * @param source
     * @param p
     */
    public static <BEAN> void filter(Context ctx, Collection<BEAN> dest, Collection<BEAN> source, final Predicate p)
    {
        CollectionsUtils.<BEAN>filter(ctx, dest, source, new TypedPredicate<BEAN>()
            {
                @Override
                public boolean f(Context ctx, BEAN obj) throws AbortVisitException
                {
                    return p.f(ctx, obj);
                }
            }
        );
    }
}
