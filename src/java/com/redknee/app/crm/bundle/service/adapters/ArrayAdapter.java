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

/**
 * A more type-safe way to adapt arrays. Arrays to Collection migration can
 * be done using JDK API (Arrays.** and/or Collections.**)
 * 
 * @author sbanerjee
 * TODO Move to a better location so other projects can use this
 */
public class ArrayAdapter<FROM_TYPE, TO_TYPE>
{
    /**
     * 
     * @author sbanerjee
     * TODO Move to a better location so other projects can use this
     * @param <FROM_TYPE>
     * @param <TO_TYPE>
     */
    public static interface ComponentAdapter<FROM_TYPE, TO_TYPE>
    {
        public TO_TYPE newInstance();
        public TO_TYPE[] newArray(int length);
        public void adapt(FROM_TYPE fromComponent, TO_TYPE toComponent);
    }
    
    private ComponentAdapter<FROM_TYPE, TO_TYPE> compAdapter;
    
    public ArrayAdapter(ComponentAdapter<FROM_TYPE, TO_TYPE> compAdapter)
    {
        this.compAdapter = compAdapter;
    }
    
    public TO_TYPE[] adapt(FROM_TYPE[] fromArray)
    {
        if(fromArray == null)
            return null;
        
        TO_TYPE[] ret = this.compAdapter.newArray(fromArray.length);
        for(int i=0 ; i<fromArray.length ; i++)
        {
            ret[i] = this.compAdapter.newInstance();
            this.compAdapter.adapt(fromArray[i], ret[i]);
        }
        return ret;
    }
    
    public TO_TYPE[] adaptNonNull(FROM_TYPE[] fromArray)
    {
        if(fromArray == null)
            return this.compAdapter.newArray(0);
        
        TO_TYPE[] ret = this.compAdapter.newArray(fromArray.length);
        for(int i=0 ; i<fromArray.length ; i++)
        {
            ret[i] = this.compAdapter.newInstance();
            this.compAdapter.adapt(fromArray[i], ret[i]);
        }
        return ret;
    }
}