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
package com.redknee.app.crm.move;

import java.util.ArrayList;
import java.util.List;

import com.redknee.framework.xhome.beans.XBeans;
import com.redknee.framework.xhome.beans.xi.PropertyInfo;
import com.redknee.framework.xhome.beans.xi.XInfo;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xlog.log.PMLogMsg;

import com.redknee.app.crm.extension.MovableExtension;
import com.redknee.app.crm.move.processor.ReadOnlyMoveProcessor;
import com.redknee.app.crm.move.request.CompoundMoveRequest;
import com.redknee.app.crm.move.support.MoveProcessorSupport;


/**
 * This class provides a point of entry for the move process.  Given a request,
 * it will execute relevant validation and/or execute the move operation.
 *
 * @author Aaron Gourley
 * @since 8.1
 */
public class MoveManager
{
    public void validate(Context ctx, MoveRequest request) throws IllegalStateException
    {
        String pmModule = this.getClass().getName();
        String pmPrefix = "move(" + request.getClass().getSimpleName() + ")";
        
        PMLogMsg pm = new PMLogMsg(pmModule, pmPrefix);
        PMLogMsg pmErrValidation = new PMLogMsg(pmModule, pmPrefix + "-ValidationFailure");
        try
        {
            trim(ctx, request);
            
            MoveProcessor<MoveRequest> processor = MoveProcessorSupport.getMoveProcessor(ctx, request);
            if (processor == null)
            {
                throw new IllegalStateException("Could not retrieve a move processor for move request " + request);
            }

            try
            {
                Context moveCtx = processor.setUp(ctx);
                if (!moveCtx.has(MoveManager.class))
                {
                    moveCtx.put(MoveManager.class, this);
                }
                try
                {
                    new ReadOnlyMoveProcessor<MoveRequest>(processor, "Executing validation only.").validate(moveCtx);
                }
                finally
                {
                    processor.tearDown(moveCtx);
                }
            }
            catch (MoveException e)
            {
                throw new IllegalStateException("Unexpected error occurred during validation of request " + request, e);
            }
            pm.log(ctx);
        }
        catch (IllegalStateException ise)
        {
            pmErrValidation.log(ctx);
            throw ise;
        }
    }
    
    public MoveRequest move(Context ctx, MoveRequest request) throws IllegalStateException, MoveException
    {
        String pmModule = this.getClass().getName();
        String pmPrefix = "move(" + request.getClass().getSimpleName() + ")";
        
        PMLogMsg pm = new PMLogMsg(pmModule, pmPrefix);
        PMLogMsg pmErrValidation = new PMLogMsg(pmModule, pmPrefix + "-ValidationFailure");
        PMLogMsg pmErrMove = new PMLogMsg(pmModule, pmPrefix + "-MoveFailure");
        
        MoveRequest result = request;
        try
        {
            trim(ctx, request);

            MoveProcessor<MoveRequest> processor = MoveProcessorSupport.getMoveProcessor(ctx, request);
            if (processor == null)
            {
                throw new MoveException(request, "Could not retrieve a move processor.");
            }

            Context moveCtx = processor.setUp(ctx);
            moveCtx.put(MovableExtension.MOVE_IN_PROGRESS_CTX_KEY, true);
            
            if (!moveCtx.has(MoveManager.class))
            {
                moveCtx.put(MoveManager.class, this);
            }
            try
            {
                new ReadOnlyMoveProcessor<MoveRequest>(processor, "Executing validation only.").validate(moveCtx);
                if (!request.hasErrors(moveCtx))
                {
                    processor.move(moveCtx);

                    result = processor.getRequest();
                    if (result.hasErrors(moveCtx))
                    {
                        throw new MoveException(result, "Request failed with " + result.getErrors(moveCtx).size() + " error(s).");
                    }
                }
            }
            finally
            {
                processor.tearDown(moveCtx);
                processedRequests_.add(0, processor.getRequest());
            }

            pm.log(ctx);
        }
        catch (IllegalStateException ise)
        {
            pmErrValidation.log(ctx);
            throw ise;
        }
        catch (MoveException me)
        {
            pmErrMove.log(ctx);
            throw me;
        }
        
        return result;
    }


    /**
     * Trim leading and trailing spaces from all string values in the input request.
     * 
     * @param <MR>
     * @param ctx
     * @param request
     */
    private static <MR extends MoveRequest> void trim(Context ctx, MR request)
    {
        Object xinfoObj = XBeans.getInstanceOf(ctx, request, XInfo.class);
        if (xinfoObj != null && xinfoObj instanceof XInfo)
        {
            XInfo xinfo = (XInfo)xinfoObj;
            for(Object propertyObj : xinfo.getProperties(ctx))
            {
                if (propertyObj instanceof PropertyInfo)
                {
                    PropertyInfo property = (PropertyInfo) propertyObj;
                    if (property.getType() != null
                            && String.class.getName().equals(property.getType().getName()))
                    {
                        String val = (String)property.get(request);
                        property.set(request, val.trim());
                    }
                }
            }
        }
    }

    
    /**
     * @return 
     */
    public CompoundMoveRequest getProcessedRequests()
    {
        return new CompoundMoveRequest(processedRequests_.toArray(new MoveRequest[]{}));
    }
    
    protected List<MoveRequest> processedRequests_ = new ArrayList<MoveRequest>();
}
