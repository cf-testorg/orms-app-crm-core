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
package com.redknee.app.crm.move.processor.factory;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xlog.log.LogSupport;
import com.redknee.framework.xlog.log.PMLogMsg;

import com.redknee.app.crm.move.MoveConstants;
import com.redknee.app.crm.move.MoveProcessor;
import com.redknee.app.crm.move.MoveRequest;
import com.redknee.app.crm.move.processor.DependencyMoveProcessor;
import com.redknee.app.crm.move.processor.LoggingMoveProcessor;
import com.redknee.app.crm.move.processor.NullMoveProcessor;
import com.redknee.app.crm.move.processor.PMMoveProcessor;
import com.redknee.app.crm.move.processor.StartTimeRecordingProcessor;


/**
 * This is the core version of the move processor factory.  It will likely need to be over-ridden
 * by the application using the feature.
 *
 * @author Aaron Gourley
 * @since 8.1
 */
public class DefaultMoveProcessorFactory implements MoveProcessorFactory
{
    private static MoveProcessorFactory instance_ = null;
    public static MoveProcessorFactory instance()
    {
        if (instance_ == null)
        {
            instance_ = new DefaultMoveProcessorFactory();
        }
        return instance_;
    }
    
    protected DefaultMoveProcessorFactory()
    {
    }

    public final <MR extends MoveRequest> MoveProcessor<MR> getInstance(Context ctx, MR request)
    {
        if (request == null)
        {
            return NullMoveProcessor.instance();
        }
        
        MoveProcessor<MR> processor = getRequestSpecificInstance(ctx, request);

        if (processor != null)
        {
            processor = new LoggingMoveProcessor<MR>(processor, new PMMoveProcessor<MR>(processor));
            
            if (!ctx.has(MoveConstants.MOVE_START_TIME_CTX_KEY))
            {
                processor = new StartTimeRecordingProcessor<MR>(processor);
            }
        }

        return processor;
    }

    /**
     * This method is designed to be overridden by the application that is aware of MoveRequest types
     * specific to its feature set.
     * 
     * The extending class should not wrap its processor pipeline with anything that applies
     * to all pipelines.  The default getInstance() method does this.
     */
    protected <MR extends MoveRequest> MoveProcessor<MR> getRequestSpecificInstance(Context ctx, MR request)
    {
        PMLogMsg pm = new PMLogMsg(DefaultMoveProcessorFactory.class.getName(), "DefaultProcessorCreation");

        MoveProcessor<MR> processor = null;

        // All other move requests will simply delegate to their dependencies
        // (e.g. CompoundMoveRequest delegates to its contained requests) 
        processor = new DependencyMoveProcessor<MR>(request);
        pm.log(ctx);
        return processor;
    }
}

