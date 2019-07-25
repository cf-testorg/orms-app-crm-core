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
package com.redknee.app.crm.move.support;

import com.redknee.app.crm.move.MoveProcessor;
import com.redknee.app.crm.move.MoveRequest;
import com.redknee.app.crm.move.processor.factory.DefaultMoveProcessorFactory;
import com.redknee.app.crm.move.processor.factory.MoveProcessorFactory;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.webcontrol.HTMLExceptionListener;


/**
 * Provides convenience methods to get move processors.
 *
 * @author Aaron Gourley
 * @since 8.1
 */
public class MoveProcessorSupport
{
    public static <MR extends MoveRequest> MoveProcessor<MR> getMoveProcessor(Context ctx, MR request)
    {
        MoveProcessorFactory factory = (MoveProcessorFactory) ctx.get(
                MoveProcessorFactory.class, 
                DefaultMoveProcessorFactory.instance());
        
        return factory.getInstance(ctx, request);
    }
    
    public static void copyHTMLExceptionListenerExceptions(Context fromCtx, Context toCtx)
    {
        HTMLExceptionListener from = (HTMLExceptionListener) fromCtx.get(HTMLExceptionListener.class);
        HTMLExceptionListener to = (HTMLExceptionListener) toCtx.get(HTMLExceptionListener.class);
        if (to == null)
        {
            toCtx.put(HTMLExceptionListener.class, from);
        }
        else if (from != null)
        {
            for (Object e : from.getExceptions())
            {
                to.thrown((Throwable) e);
            }
        }
    }
    
}
