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
package com.redknee.app.crm.filter;

import java.util.HashSet;
import java.util.Set;

import com.redknee.framework.xhome.filter.Predicate;
import com.redknee.framework.xlog.log.MinorLogMsg;

/**
 * An abstract predicate to be used with the FilteredMultiSelectWebControl. 
 * ALWAYS implement both constructors.
 * @author Marcio Marques
 * @since 8.5
 *
 */
public abstract class FilteredMultiSelectPredicate implements Predicate
{
    private int filterId_;
    
    private Set<Integer> selectedIds_;

    public FilteredMultiSelectPredicate(String filterId, String selectedIds)
    {
        try
        {
            filterId_ = Integer.parseInt(filterId);
        }
        catch (Throwable t)
        {
            new MinorLogMsg(this, "Unable to parse filter identifier " + filterId + ": " + t.getMessage(), t);
            filterId_ = -1;
        }
        
        selectedIds_ = new HashSet<Integer>();
        for (String id : selectedIds.split(","))
        {
            if (!id.isEmpty())
            {
                try
                {
                    selectedIds_.add(Integer.parseInt(id));
                }
                catch (Throwable t)
                {
                    new MinorLogMsg(this, "Unable to parse selected identifier " + id + ": " + t.getMessage(), t);
                }
            }
        }
    }

    public FilteredMultiSelectPredicate(String filterId)
    {
        try
        {
            filterId_ = Integer.parseInt(filterId);
        }
        catch (Throwable t)
        {
            new MinorLogMsg(this, "Unable to parse filter identifier " + filterId + ": " + t.getMessage(), t);
            filterId_ = -1;
        }
        
        selectedIds_ = new HashSet<Integer>();
    }
    
    public int getFilterId()
    {
        return filterId_;
    }

    
    public Set<Integer> getSelectedIds()
    {
        return selectedIds_;
    }

    
    
}
