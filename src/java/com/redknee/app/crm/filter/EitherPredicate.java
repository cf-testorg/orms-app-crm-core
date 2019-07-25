package com.redknee.app.crm.filter;

import java.sql.SQLException;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.filter.Predicate;
import com.redknee.framework.xhome.visitor.AbortVisitException;
import com.redknee.framework.xhome.xdb.SimpleXStatement;
import com.redknee.framework.xhome.xdb.XPreparedStatement;
import com.redknee.framework.xhome.xdb.XStatement;


public class EitherPredicate implements XStatement,Predicate
{
    
    public EitherPredicate()
    {
        
    }
    public EitherPredicate( final Predicate predicate, final String xstmt)
    {
        xStatement_ = new SimpleXStatement(xstmt);
        predicate_ = predicate;
        
    }

    public EitherPredicate( final Predicate predicate, final XStatement xstmt)
    {
        xStatement_ = xstmt;
        predicate_ = predicate;
        
    }

    @Override
    public boolean f(Context ctx, Object obj) throws AbortVisitException
    {
        return predicate_.f(ctx, obj);
    }

    @Override
    public String createStatement(Context ctx)
    {
        return xStatement_.createStatement(ctx);
    }

    @Override
    public void set(Context ctx, XPreparedStatement ps) throws SQLException
    {
        xStatement_.set(ctx, ps);
    }
    private XStatement xStatement_= null;
    private Predicate predicate_ = null;
}
