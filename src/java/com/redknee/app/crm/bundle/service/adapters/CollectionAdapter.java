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
package com.redknee.app.crm.bundle.service.adapters;

import java.util.Collection;

/**
 * A more type-safe way to adapt Collection. Arrays to Collection migration 
 * and vice-versa can be done using JDK API (Arrays.** and/or Collections.**)
 * 
 * TODO Move to a better location so other projects can use this
 * @author sbanerjee
 *
 * @param <FROM_TYPE>
 * @param <TO_TYPE>
 * @param <FROM_COLLECTION_TYPE>
 * @param <TO_COLLECTION_TYPE>
 */
public class CollectionAdapter<FROM_TYPE, TO_TYPE, 
    FROM_COLLECTION_TYPE extends Collection<FROM_TYPE>, 
    TO_COLLECTION_TYPE extends Collection<TO_TYPE>>
{
    /**
     * TODO Move to a better location so other projects can use this
     * @author sbanerjee
     *
     * @param <FROM_TYPE>
     * @param <TO_TYPE>
     * @param <FROM_COLLECTION_TYPE>
     * @param <TO_COLLECTION_TYPE>
     */
    public static interface ComponentAdapter<FROM_TYPE, TO_TYPE, 
        FROM_COLLECTION_TYPE, TO_COLLECTION_TYPE>
    {
        public TO_TYPE newInstance();
        
        public TO_COLLECTION_TYPE newCollection(int length);
        
        /**
         * Responsibility of the caller to instantiate the parameters fromComponent and toComponent.
         * @param fromComponent
         * @param toComponent
         */
        public void adapt(FROM_TYPE fromComponent, TO_TYPE toComponent);
    }
    
    private ComponentAdapter<FROM_TYPE, TO_TYPE, FROM_COLLECTION_TYPE, TO_COLLECTION_TYPE> compAdapter;
    
    public CollectionAdapter(ComponentAdapter<FROM_TYPE, TO_TYPE, FROM_COLLECTION_TYPE, TO_COLLECTION_TYPE> compAdapter)
    {
        this.compAdapter = compAdapter;
    }
    
    public TO_COLLECTION_TYPE adapt(FROM_COLLECTION_TYPE fromCollection)
    {
        if(fromCollection == null)
            return null;
        
        TO_COLLECTION_TYPE ret = this.compAdapter.newCollection(fromCollection.size());
        
        for(FROM_TYPE from : fromCollection)
        {
            TO_TYPE to = this.compAdapter.newInstance();
            this.compAdapter.adapt(from, to);
            ret.add(to);
        }
        
        return ret;
    }
    
    public TO_COLLECTION_TYPE adaptNonNull(FROM_COLLECTION_TYPE fromCollection)
    {
        if(fromCollection == null)
            return this.compAdapter.newCollection(0);
        
        TO_COLLECTION_TYPE ret = this.compAdapter.newCollection(fromCollection.size());
        
        for(FROM_TYPE from : fromCollection)
        {
            TO_TYPE to = this.compAdapter.newInstance();
            this.compAdapter.adapt(from, to);
            ret.add(to);
        }
        
        return ret;
    }
}