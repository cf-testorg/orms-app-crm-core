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

/**
 * Immutable 2D index
 * @author sbanerjee
 *
 * @param <T> Must be an immutable scalar (e.g. Integer, Long, etc).
 */
public final class Index2D<T>
{
    private final T primaryIndex;
    private final T secondaryIndex;
    /**
     * @param primaryIndex
     * @param secondaryIndex
     */
    public Index2D(T primaryIndex, T secondaryIndex)
    {
        if(primaryIndex==null || secondaryIndex==null)
            throw new NullPointerException("Both the component indices must be non-null.");
        
        this.primaryIndex = primaryIndex;
        this.secondaryIndex = secondaryIndex;
    }
    /**
     * @return the primaryIndex
     */
    public final T getPrimaryIndex()
    {
        return this.primaryIndex;
    }
    /**
     * @return the secondaryIndex
     */
    public final T getSecondaryIndex()
    {
        return this.secondaryIndex;
    }
    
    @Override
    public int hashCode()
    {
        return (int) (primaryIndex.hashCode() ^ secondaryIndex.hashCode());
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if (this==obj)
            return true;
        
        if(obj instanceof Index2D)
            return (this.primaryIndex.equals(((Index2D)obj).primaryIndex) 
                    && this.secondaryIndex == ((Index2D)obj).secondaryIndex);
        
        return false;
    }
}