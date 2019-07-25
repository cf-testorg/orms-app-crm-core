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
 * This calculator runs user defined Shell script that returns a value.
 *
 * @author asim.mahmood@redknee.com
 * @since 9.2
 */
public class ShellValueCalculator extends AbstractShellValueCalculator
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;


    public ShellValueCalculator()
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
        PMLogMsg pm = new PMLogMsg("ValueCalculator", this.getClass().getName(), this.getScript());
        Object result = null;
        try
        {
            result = ScriptExecutorFactory.instance().create(this.getScriptType()).retrieveObject(ctx, this.getScript(), String.class.getName());
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
