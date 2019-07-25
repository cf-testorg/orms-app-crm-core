/**
    FreezingVisitor
 
    @author: Kevin Greer
    Date   : Jan 7, 2010
 
    Copyright (c) Redknee, 2010
        - all rights reserved
 **/

package com.redknee.app.crm.xhome.visitor;

import com.redknee.framework.xhome.beans.Freezable;
import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.visitor.AbortVisitException;
import com.redknee.framework.xhome.visitor.Visitor;
import com.redknee.framework.xhome.visitor.VisitorProxy;


/**
 * Visitor Decorator which freezes vistees before they are visited.
 * 
 * @deprecated Use FW 6's FreezingVisitor if available in your application.
 **/
@Deprecated
public class FreezingVisitor extends VisitorProxy
{
    public FreezingVisitor(Visitor delegate)
    {
        super(delegate);
    }


    @Override
    public void visit(Context ctx, Object bean) throws AgentException, AbortVisitException
    {
        getDelegate().visit(ctx, freeze(bean));
    }


    public Object freeze(Object bean)
    {
        if (bean instanceof Freezable)
        {
            ((Freezable) bean).freeze();
        }
        return bean;
    }

}
