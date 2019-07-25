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

/*
 * @author jchen
 * Created on Jan 10, 2006
 */
package com.redknee.app.crm.sequenceId;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.HomeProxy;

import com.redknee.app.crm.bean.IdentifierSequence;
import com.redknee.app.crm.bean.IdentifierSequenceHome;

/**
 * @author jchen
 *
 * The purpose of this class is to make getNextIdentifier moving into home class,
 * so it can take advantage of home, RMI service for cluster safe
 */
public class IdentifierSequenceIncrementHome extends HomeProxy 
{

    public IdentifierSequenceIncrementHome(Home deletgate)
    {
        super(deletgate);
    }
    
    
    /* 
     * @return next 
     * (non-Javadoc)
     * @see com.redknee.framework.xhome.home.HomeSPI#cmd(com.redknee.framework.xhome.context.Context, java.lang.Object)
     */
    @Override
    public Object cmd(Context ctx, Object cmd) throws HomeException
    {
        Object ret = null;
        if (cmd instanceof IncrementIdentifierCmd)
        {
            IncrementIdentifierCmd incrementIdentifierCmd = (IncrementIdentifierCmd)cmd;
            ret = doCmd(ctx, incrementIdentifierCmd);            
        }
        else
        {
            ret = super.cmd(ctx, cmd);
        }
        return ret;
        
    }
    
    /**
     * 
     * @param ctx
     * @param incrementIdentifierCmd
     * @return update and return next available sequence identifier
     * @throws HomeException
     */
    private Object doCmd(Context ctx, IncrementIdentifierCmd incrementIdentifierCmd) throws HomeException
    {
        Object ret;
        Home seqHome = (Home)ctx.get(IdentifierSequenceHome.class);
        long newSeq = incrementIdentifier(ctx, seqHome, incrementIdentifierCmd.identifierSequenceName,
                incrementIdentifierCmd.notifier);
        ret = Long.valueOf(newSeq);
        return ret;
    }



    long incrementIdentifier(Context ctx, Home seqHome, String identifierSequenceName, RollOverNotofiable notifier) throws HomeException 
    {
        long nextIdentifier=0;
		synchronized(IdentifierSequenceIncrementHome.class)
		{
			IdentifierSequence seq=(IdentifierSequence)seqHome.find(ctx,identifierSequenceName);
			
			if(seq==null)
			{
				seq=new IdentifierSequence();
				seq.setIdentifier(identifierSequenceName);
				
				seq=(IdentifierSequence) seqHome.create(ctx,seq);
			}

			nextIdentifier=seq.aquireNextIdentifier(notifier);
			seqHome.store(ctx,seq);
		}
        return nextIdentifier;
    }
}
