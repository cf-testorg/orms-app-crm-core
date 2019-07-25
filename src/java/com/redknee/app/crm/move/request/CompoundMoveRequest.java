/*
 * This code is a protected work and subject to domestic and international
 * copyright law(s). A complete listing of authors of this work is readily 
 * available. Additionally, source code is, by its very nature, confidential 
 * information and inextricably contains trade secrets and other information 
 * proprietary, valuable and sensitive to Redknee, no unauthorised use, 
 * disclosure, manipulation or otherwise is permitted, and may only be used 
 * in accordance with the terms of the licence agreement entered into with 
 * Redknee Inc. and/or its subsidiaries.
 *
 * Copyright (c) Redknee Inc. and its subsidiaries. All Rights Reserved.
 */
package com.redknee.app.crm.move.request;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.language.MessageMgr;

import com.redknee.app.crm.move.MoveRequest;
import com.redknee.app.crm.move.MoveWarningException;


/**
 * Container for 0 or more other MoveRequest objects.
 *
 * @author Aaron Gourley
 * @since 8.1
 */
public class CompoundMoveRequest extends AbstractCompoundMoveRequest
{

    public CompoundMoveRequest()
    {
    }
    
    public CompoundMoveRequest(MoveRequest... requests)
    {
        add(requests);
    }

    /**
     * @param requests
     */
    public void add(MoveRequest... requests)
    {
        getRequests().addAll(Arrays.asList(requests));
    }

    /**
     * @{inheritDoc}
     */
    public String getSuccessMessage(Context ctx)
    {
        MessageMgr mmgr = new MessageMgr(ctx, this);
        return mmgr.get(CompoundMoveRequest.class.getSimpleName() + ".success", 
                "Bulk move request containing {0} individual move requests completed successfully.", 
                new String[] {
                        String.valueOf((getRequests() != null ? getRequests().size() : 0))
                    });
    }

    /**
     * @{inheritDoc}
     */
    public void reportError(Context ctx, Throwable error)
    {
        errors_.add(error);
    }

    /**
     * @{inheritDoc}
     */
    public void reportWarning(Context ctx, MoveWarningException warning)
    {
        warnings_.add(warning);
    }

    /**
     * @{inheritDoc}
     */
    public boolean hasErrors(Context ctx)
    {
        List<MoveRequest> requests = getRequests();
        for (MoveRequest request : requests)
        {
            if (request.hasErrors(ctx))
            {
                return true;
            }
        }
        return errors_ != null && errors_.size() > 0;
    }

    /**
     * @{inheritDoc}
     */
    public boolean hasWarnings(Context ctx)
    {
        List<MoveRequest> requests = getRequests();
        for (MoveRequest request : requests)
        {
            if (request.hasWarnings(ctx))
            {
                return true;
            }
        }
        return warnings_ != null && warnings_.size() > 0;
    }

    /**
     * @{inheritDoc}
     */
    public Set<Throwable> getErrors(Context ctx)
    {
        Set<Throwable> errors = new HashSet<Throwable>();
        errors.addAll(errors_);
        
        List<MoveRequest> requests = getRequests();
        for (MoveRequest request : requests)
        {
            errors.addAll(request.getErrors(ctx));
        }
        return Collections.unmodifiableSet(errors);
    }

    /**
     * @{inheritDoc}
     */
    public Set<MoveWarningException> getWarnings(Context ctx)
    {
        Set<MoveWarningException> warnings = new HashSet<MoveWarningException>();
        warnings.addAll(warnings_);
        
        List<MoveRequest> requests = getRequests();
        for (MoveRequest request : requests)
        {
            warnings.addAll(request.getWarnings(ctx));
        }
        return Collections.unmodifiableSet(warnings);
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public Object ID()
    {
        if (id_ == null || !id_.equals(getRequests().toString()))
        {
            id_ = getRequests().toString();
        }
        return id_;
    }
    
    @Override
    public String toString()
    {
       return getClass().getName() + "(" + getRequests() + ")";
    }
    
    private String id_ = null;
    
    protected Set<Throwable> errors_ = new HashSet<Throwable>();
    protected Set<MoveWarningException> warnings_ = new HashSet<MoveWarningException>();
}
