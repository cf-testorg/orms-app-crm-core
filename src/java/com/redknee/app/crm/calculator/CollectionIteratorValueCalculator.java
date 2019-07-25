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
package com.redknee.app.crm.calculator;

import java.util.ArrayList;
import java.util.Collection;

import com.redknee.framework.core.scripting.support.ScriptExecutorFactory;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xlog.log.PMLogMsg;


/**
 * This calculator iterates through a collection.
 * 
 * For each object in the collection, the object instance is
 * put in the context against a configurable key (refer method
 * {@link CollectionIteratorValueCalculator#getBeanClass()} ).
 * The delegate is then called. It's the job of the delegate to
 * individually process every object.
 *
 * @author ameya.bhurke@redknee.com
 * @since 9.7.1
 */
public class CollectionIteratorValueCalculator extends AbstractCollectionIteratorValueCalculator
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;


    public CollectionIteratorValueCalculator()
    {
        super();
    }

    @Override
    public Collection<Object> getDependentContextKeys(Context ctx)
    {
        return new ArrayList<Object>();
    }

    @Override
    public Object getValueAdvanced(Context ctx)
    {
        PMLogMsg pm = new PMLogMsg("ValueCalculator", this.getClass().getName(), "");
        Object result = null;
        try
        {
        	// The below line will execute a shell script. That
        	// script should put a Collection of objects against
        	// java.util.Collection key in the context.
           super.getValueAdvanced(ctx);
           
           Collection objectCollection = (Collection)ctx.get(java.util.Collection.class);
           
           if(objectCollection == null) 
           {
        	   return "";
           }
           
           StringBuilder resultBuilder = new StringBuilder();
           Class key = Class.forName(getBeanClass());
           
           for(Object object : objectCollection)
           {
        	   ctx.put(key, object);
        	   resultBuilder.append(getDelegate().getValueAdvanced(ctx));
           }
           
           result = resultBuilder.toString();
        }
        catch(Exception e)
        {
            throw new IllegalStateException(e);
        }
        finally
        {
            pm.log(ctx);
        }
        
        return result;
    }
}
