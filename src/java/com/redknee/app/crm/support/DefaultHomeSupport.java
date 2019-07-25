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
package com.redknee.app.crm.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.redknee.framework.xhome.beans.AbstractBean;
import com.redknee.framework.xhome.beans.BeanAwareFacet;
import com.redknee.framework.xhome.beans.Identifiable;
import com.redknee.framework.xhome.beans.XBeans;
import com.redknee.framework.xhome.beans.xi.PropertyInfo;
import com.redknee.framework.xhome.beans.xi.XInfo;
import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.And;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.elang.Limit;
import com.redknee.framework.xhome.elang.LimitPredicate;
import com.redknee.framework.xhome.elang.True;
import com.redknee.framework.xhome.filter.Predicate;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.HomeInternalException;
import com.redknee.framework.xhome.home.HomeProxy;
import com.redknee.framework.xhome.home.OrderByHome;
import com.redknee.framework.xhome.visitor.AbortVisitException;
import com.redknee.framework.xhome.visitor.AvgVisitor;
import com.redknee.framework.xhome.visitor.CountingVisitor;
import com.redknee.framework.xhome.visitor.FunctionVisitor;
import com.redknee.framework.xhome.visitor.ListBuildingVisitor;
import com.redknee.framework.xhome.visitor.MaxVisitor;
import com.redknee.framework.xhome.visitor.MinVisitor;
import com.redknee.framework.xhome.visitor.PredicateVisitor;
import com.redknee.framework.xhome.visitor.SumVisitor;
import com.redknee.framework.xhome.visitor.Visitor;
import com.redknee.framework.xhome.visitor.Visitors;
import com.redknee.framework.xhome.xdb.Count;
import com.redknee.framework.xhome.xdb.Max;
import com.redknee.framework.xhome.xdb.Min;
import com.redknee.framework.xhome.xdb.Sum;
import com.redknee.framework.xhome.xdb.XDBHome;
import com.redknee.framework.xhome.xdb.XStatement;
import com.redknee.framework.xlog.log.DebugLogMsg;
import com.redknee.framework.xlog.log.LogSupport;
import com.redknee.framework.xlog.log.MajorLogMsg;

import com.redknee.app.crm.xhome.visitor.CloneIfFrozenVisitor;
import com.redknee.app.crm.xhome.visitor.FreezingVisitor;


/**
 * @author Aaron Gourley
 * @since 8.2
 */
public class DefaultHomeSupport implements HomeSupport
{
    protected static HomeSupport instance_ = null;
    public static HomeSupport instance()
    {
        if (instance_ == null)
        {
            instance_ = new DefaultHomeSupport();
        }
        return instance_;
    }

    protected DefaultHomeSupport()
    {
    }
    
    /**
     * {@inheritDoc}
     */
    public <T extends AbstractBean> T createBean(Context ctx, T bean) throws HomeInternalException, HomeException
    {
        final Class beanType = bean.getClass();
        return (T) getHome(ctx, beanType).create(ctx, bean);
    }


    /**
     * {@inheritDoc}
     */
    public <T extends AbstractBean> T storeBean(Context ctx, T bean) throws HomeInternalException, HomeException
    {
        final Class beanType = bean.getClass();
        return (T) getHome(ctx, beanType).store(ctx, bean);
    }


    /**
     * {@inheritDoc}
     */
    public <T extends AbstractBean> void removeBean(Context ctx, T bean) throws HomeInternalException, HomeException
    {
        final Class beanType = bean.getClass();
        getHome(ctx, beanType).remove(ctx, bean);
    }

    
    /**
     * {@inheritDoc}
     */
    public <T extends AbstractBean> T findBean(Context ctx, Class<T> beanType, Object where) throws HomeInternalException, HomeException
    {
        return (T) getHome(ctx, beanType).find(ctx, where);
    }


    /**
     * {@inheritDoc}
     */
    public <T extends AbstractBean> Collection<T> getBeans(Context ctx, Class<T> beanType) throws HomeInternalException, HomeException
    {
        return getBeans(ctx, beanType, True.instance());
    }


    /**
     * {@inheritDoc}
     */
    public <T extends AbstractBean> Collection<T> getReadOnlyBeans(Context ctx, Class<T> beanType) throws HomeInternalException, HomeException
    {
        return getReadOnlyBeans(ctx, beanType, True.instance());
    }


    /**
     * {@inheritDoc}
     */
    public <T extends AbstractBean> Collection<T> getBeans(Context ctx, Class<T> beanType, Object where) throws HomeInternalException, HomeException
    {
        return getBeans(ctx, beanType, where, -1);
    }


    /**
     * {@inheritDoc}
     */
    public <T extends AbstractBean> Collection<T> getReadOnlyBeans(Context ctx, Class<T> beanType, Object where) throws HomeInternalException, HomeException
    {
        return getReadOnlyBeans(ctx, beanType, where, -1);
    }


    /**
     * {@inheritDoc}
     */
    public <T extends AbstractBean> Collection<T> getBeans(Context ctx, Class<T> beanType, Object where, int limit) throws HomeInternalException, HomeException
    {
        return getBeans(ctx, beanType, where, limit, true, new PropertyInfo[]{});
    }


    /**
     * {@inheritDoc}
     */
    public <T extends AbstractBean> Collection<T> getReadOnlyBeans(Context ctx, Class<T> beanType, Object where, int limit) throws HomeInternalException, HomeException
    {
        return getReadOnlyBeans(ctx, beanType, where, limit, true, new PropertyInfo[]{});
    }


    /**
     * {@inheritDoc}
     */
    public <T extends AbstractBean> Collection<T> getBeans(Context ctx, Class<T> beanType, Object where, boolean orderAscending) throws HomeInternalException, HomeException
    {
        return getBeans(ctx, beanType, where, -1, orderAscending);
    }


    /**
     * {@inheritDoc}
     */
    public <T extends AbstractBean> Collection<T> getReadOnlyBeans(Context ctx, Class<T> beanType, Object where, boolean orderAscending) throws HomeInternalException, HomeException
    {
        return getReadOnlyBeans(ctx, beanType, where, -1, orderAscending);
    }


    /**
     * {@inheritDoc}
     */
    public <T extends AbstractBean> Collection<T> getBeans(Context ctx, Class<T> beanType, Object where, int limit, boolean orderAscending) throws HomeInternalException, HomeException
    {
        XInfo xinfo = (XInfo) XBeans.getInstanceOf(ctx, beanType, XInfo.class);
        if (xinfo != null && xinfo.getID()!=null)
        {
            return getBeans(ctx, beanType, where, limit, orderAscending, new PropertyInfo[]{ xinfo.getID() });
        }
        else
        {
            return getBeans(ctx, beanType, where, limit, orderAscending, new PropertyInfo[]{});   
        }
    }


    /**
     * {@inheritDoc}
     */
    public <T extends AbstractBean> Collection<T> getReadOnlyBeans(Context ctx, Class<T> beanType, Object where, int limit, boolean orderAscending) throws HomeInternalException, HomeException
    {
        XInfo xinfo = (XInfo) XBeans.getInstanceOf(ctx, beanType, XInfo.class);
        if (xinfo != null && xinfo.getID()!=null)
        {
            return getReadOnlyBeans(ctx, beanType, where, limit, orderAscending, new PropertyInfo[]{ xinfo.getID() });
        }
        else
        {
            return getReadOnlyBeans(ctx, beanType, where, limit, orderAscending, new PropertyInfo[]{});   
        }
    }


    /**
     * {@inheritDoc}
     */
    public <T extends AbstractBean> Collection<T> getBeans(Context ctx, Class<T> beanType, Object where, boolean orderAscending, PropertyInfo... orderBy) throws HomeInternalException, HomeException
    {
        return getBeans(ctx, beanType, where, -1, orderAscending, orderBy);
    }


    /**
     * {@inheritDoc}
     */
    public <T extends AbstractBean> Collection<T> getReadOnlyBeans(Context ctx, Class<T> beanType, Object where, boolean orderAscending, PropertyInfo... orderBy) throws HomeInternalException, HomeException
    {
        return getReadOnlyBeans(ctx, beanType, where, -1, orderAscending, orderBy);
    }


    /**
     * {@inheritDoc}
     */
    public <T extends AbstractBean> Collection<T> getBeans(Context ctx, Class<T> beanType, Object where, int limit, boolean orderAscending, PropertyInfo... orderBy) throws HomeInternalException, HomeException
    {
        return getBeans(ctx, beanType, where, limit, false, orderAscending, orderBy);
    }


    /**
     * {@inheritDoc}
     */
    public <T extends AbstractBean> Collection<T> getReadOnlyBeans(Context ctx, Class<T> beanType, Object where, int limit, boolean orderAscending, PropertyInfo... orderBy) throws HomeInternalException, HomeException
    {
        return getBeans(ctx, beanType, where, limit, true, orderAscending, orderBy);
    }


    /**
     * {@inheritDoc}
     */
    public <T extends AbstractBean> Collection<T> getBeans(Context ctx, Class<T> beanType, Object where, int limit, boolean readOnly, boolean orderAscending, PropertyInfo... orderBy) throws HomeInternalException, HomeException
    {
        ListBuildingVisitor listBuildingVisitor = new ListBuildingVisitor();
        Visitor visitor = listBuildingVisitor;

        if (!readOnly)
        {
            visitor = new CloneIfFrozenVisitor(visitor);
        }
        else
        {
            visitor = new FreezingVisitor(visitor);
        }

        if (limit > 0)
        {
            if (orderBy != null && orderBy.length > 0)
            {
                // Note: Limit ELang is not used by itself here because it conflicts with ORDER BY in Oracle
                LimitPredicate limitPredicate = new LimitPredicate();
                limitPredicate.setBean(new Limit(limit));

                visitor = new PredicateVisitor(limitPredicate, new CustomListBuildingVisitor(visitor));

                if (!hasBeans(ctx, beanType, where))
                {
                    // Skip the forEach performed below.  It will do a full select and sort the list.
                    // In this case, we know it will find nothing anyways...
                    return new ArrayList<T>();
                }
            }
            else
            {
                And and = new And();
                and.add(wrapKeyWithEQ(ctx, beanType, where));
                and.add(new Limit(limit));
                where = and;
            }
        }

        ctx = getWhereContext(ctx, beanType, where, orderAscending, orderBy);

        Home home = getHome(ctx, beanType);

        Visitor resultVisitor = home.forEach(ctx, visitor);
        resultVisitor = Visitors.find(resultVisitor, Collection.class);
        if (!(resultVisitor instanceof Collection))
        {
            resultVisitor = Visitors.find(resultVisitor, CustomListBuildingVisitor.class);
            if (resultVisitor instanceof CustomListBuildingVisitor)
            {
                resultVisitor = ((CustomListBuildingVisitor) resultVisitor).delegate_;
                resultVisitor = Visitors.find(resultVisitor, Collection.class);
            }
            if (!(resultVisitor instanceof Collection))
            {
                resultVisitor = listBuildingVisitor;
            }
        }
        if (resultVisitor instanceof Collection)
        {
            return (Collection) resultVisitor;
        }
        return new ArrayList<T>();
    }

    
    /**
     * {@inheritDoc}
     */
    public <T extends AbstractBean & Identifiable> Map<Object, T> getMappedBeans(Context ctx, Class<T> beanType, Object where, int limit) throws HomeInternalException, HomeException
    {
        final Map<Object, T> mappedBeans = new HashMap<Object, T>();
        if (limit > 0)
        {
            And and = new And();
            and.add(wrapKeyWithEQ(ctx, beanType, where));
            and.add(new Limit(limit));
            getHome(ctx, beanType).forEach(ctx, new Visitor()
            {
             
                private final long serialVersionUID = 1L;
                @Override
                public void visit(Context ctx, Object obj) throws AgentException, AbortVisitException
                {
                    T bean = (T) obj;
                    mappedBeans.put(bean.ID(), bean);
                }
            }, and);
        }
        return mappedBeans;
    }

    
    /**
     * {@inheritDoc}
     */
    public Context getWhereContext(Context parentCtx, Class<? extends AbstractBean> beanType, Object where)
    {
        return getWhereContext(parentCtx, beanType, where, true, new PropertyInfo[]{});
    }


    /**
     * {@inheritDoc}
     */
    public Context getWhereContext(Context parentCtx, Class<? extends AbstractBean> beanType, Object where, boolean orderAscending)
    {
        XInfo xinfo = (XInfo) XBeans.getInstanceOf(parentCtx, beanType, XInfo.class);
        if (xinfo != null)
        {
            return getWhereContext(parentCtx, beanType, where, orderAscending, new PropertyInfo[]{ xinfo.getID() });
        }
        else
        {
            return getWhereContext(parentCtx, beanType, where, orderAscending, new PropertyInfo[]{});   
        }
    }


    /**
     * {@inheritDoc}
     */
    public Context getWhereContext(Context parentCtx, Class<? extends AbstractBean> beanType, Object where, boolean orderAscending, PropertyInfo... orderBy)
    {
        Home home = null;
        
        try
        {
            home = getHome(parentCtx, beanType);
        }
        catch (HomeException e)
        {
            new MajorLogMsg(this, e.getMessage(), null).log(parentCtx);
            if (LogSupport.isDebugEnabled(parentCtx))
            {
                new DebugLogMsg(this, e.getMessage(), e).log(parentCtx);   
            }
        }

        Context whereCtx = parentCtx.createSubContext();
        where = wrapKeyWithEQ(whereCtx, beanType, where);
        if (home != null)
        {
            home = home.where(parentCtx, where);
            boolean sortOnForEach = true;
            try
            {
				Object obj = home.cmd(parentCtx, com.redknee.framework.xhome.xdb.AbstractXDBHome.TABLE_NAME);
				if (obj != null && obj instanceof String && !obj.equals(TABLE_NAME))
				{
					sortOnForEach = false;
				}				
			}
            catch (Exception e)
            {
            	if (LogSupport.isDebugEnabled(parentCtx))
                {
                    new DebugLogMsg(this, "Exception while looking up xdb home decorator", e).log(parentCtx);   
                }
			}            
            if (home != null)
            {
                if (orderBy != null && orderBy.length > 0)
                {
                    home = new OrderByHome(parentCtx, home);                    
                    ((OrderByHome)home).setSortOnForEach(sortOnForEach);
                    for (PropertyInfo property : orderBy)
                    {
                        ((OrderByHome)home).addOrderBy(property, orderAscending);
                    }
                }
            }
            whereCtx.put(getHomeClass(parentCtx, beanType), home);
        }
        return whereCtx;
    }


    /**
     * {@inheritDoc}
     */
    public long getBeanCount(Context ctx, Class cls, Object where) throws HomeInternalException, HomeException
    {
        where = wrapKeyWithEQ(ctx, cls, where);
        return getBeanCount(ctx, getHome(ctx, cls), where);
    }


    /**
     * {@inheritDoc}
     */
    public long getBeanCount(Context ctx, Home home, Object where) throws HomeInternalException, HomeException
    {
        long count = 0l;
        if (home != null)
        {
            home = home.where(ctx, where);
            if (home != null)
            {
                boolean projectionFailed = false;
                
                Object value = null;
                try
                {
                    value = home.cmd(ctx, new Count());   
                }
                catch (HomeException he)
                {
                    // Ignore.  Try to find count value using visitor below.
                    if (LogSupport.isDebugEnabled(ctx))
                    {
                        new DebugLogMsg(this, "Count projection failed on " + home.getClass().getSimpleName(), he).log(ctx);
                    }
                    projectionFailed = true;
                }
                
                if (value instanceof Number)
                {
                    count = ((Number)value).longValue();
                }
                else if (projectionFailed || !isDatabaseHome(home))
                {
                    if (LogSupport.isDebugEnabled(ctx))
                    {
                        new DebugLogMsg(this, "Attempting to determine bean count on " + home.getClass().getSimpleName() + " using forEach with CountingVisitor...", null).log(ctx);
                    }
                    
                    CountingVisitor v = new CountingVisitor();
                    Visitor resultVisitor = home.forEach(ctx, v, where);
                    resultVisitor = Visitors.find(resultVisitor, CountingVisitor.class);
                    if (!(resultVisitor instanceof CountingVisitor))
                    {
                        resultVisitor = Visitors.find(v, CountingVisitor.class);
                    }
                    if (resultVisitor instanceof CountingVisitor)
                    {
                        count = ((CountingVisitor) resultVisitor).getCount();
                    }
                }
            }
        }
        return count;
    }

    
    /**
     * {@inheritDoc}
     */
    public boolean hasBeans(Context ctx, Class cls, Object where) throws HomeInternalException, HomeException
    {
        return hasBeans(ctx, cls, 1, where);
    }


    /**
     * {@inheritDoc}
     */
    public boolean hasBeans(Context ctx, Home home, Object where) throws HomeInternalException, HomeException
    {
        return hasBeans(ctx, home, 1, where);
    }


    /**
     * {@inheritDoc}
     */
    public boolean hasBeans(Context ctx, Class cls, int count, Object where) throws HomeInternalException, HomeException
    {
        where = wrapKeyWithEQ(ctx, cls, where);
        return hasBeans(ctx, getHome(ctx, cls), count, where);
    }


    /**
     * {@inheritDoc}
     */
    public boolean hasBeans(Context ctx, Home home, int count, Object where) throws HomeInternalException, HomeException
    {
        // Can't use Limit here because some homes do the limit before applying the criteria
        // (i.e. will only look at the first 'count' records even if those records do not match the given criteria).
        // See generic transient home for example, which will end prematurely due to an AbortVisitException thrown
        // by the LimitPredicate.
        /*
         *  And newFilter = new And();
         *  newFilter.add(new Limit(count));
         *  newFilter.add(where);
         */
        return ((count > 0 && getBeanCount(ctx, home, where) >= count)
                || (count == 0 && !hasBeans(ctx, home, where)));
    }


    /**
     * {@inheritDoc}
     */
    public Object max(Context ctx, PropertyInfo property, Object where) throws HomeInternalException, HomeException
    {
        Home home = getHome(ctx, property.getXInfo().getBeanClass());
        return max(ctx, property, home, where);
    }


    /**
     * {@inheritDoc}
     */
    public Object max(Context ctx, PropertyInfo property, Home home, Object where) throws HomeInternalException, HomeException
    {
        where = wrapKeyWithEQ(ctx, property.getBeanClass(), where);

        Object max = null;

        boolean projectionFailed = false;
        
        Object result = null;
        try
        {
            result = home.cmd(ctx, new Max(property,where));
        }
        catch (HomeException he)
        {
            // Ignore.  Try to find max value using visitor below.
            if (LogSupport.isDebugEnabled(ctx))
            {
                new DebugLogMsg(this, "Max projection failed on " + home.getClass().getSimpleName(), he).log(ctx);
            }
            projectionFailed = true;
        }

        if (result instanceof Number)
        {
            // Example: XDB returns a number when a Max projection is passed in a CMD 
            max = ((Number)result).doubleValue();
        }
        else if (projectionFailed || !isDatabaseHome(home))
        {
            if (LogSupport.isDebugEnabled(ctx))
            {
                new DebugLogMsg(this, "Attempting to determine max value on " + home.getClass().getSimpleName() + " using forEach with MaxVisitor...", null).log(ctx);
            }
            
            // Example: Some home's will not handle the Max projection, and will return it.
            // In such cases, we need to use a max visitor which is admittedly not as efficient.
            FunctionVisitor fv = new FunctionVisitor(property, new MaxVisitor());
            Visitor resultVisitor = home.forEach(ctx, fv, where);
            resultVisitor = Visitors.find(resultVisitor, MaxVisitor.class);
            if (!(resultVisitor instanceof MaxVisitor))
            {
                resultVisitor = Visitors.find(fv, MaxVisitor.class);
            }
            if (resultVisitor instanceof MaxVisitor)
            {
                max = ((MaxVisitor) resultVisitor).getValue();
            }
        }

        return max;
    }


    /**
     * {@inheritDoc}
     */
    public Object min(Context ctx, PropertyInfo property, Object where) throws HomeInternalException, HomeException
    {
        Home home = getHome(ctx, property.getXInfo().getBeanClass());
        return min(ctx, property, home, where);
    }


    /**
     * {@inheritDoc}
     */
    public Object min(Context ctx, PropertyInfo property, Home home, Object where) throws HomeInternalException, HomeException
    {
        where = wrapKeyWithEQ(ctx, property.getBeanClass(), where);

        Object min = null;

        boolean projectionFailed = false;
        
        Object result = null;
        try
        {
            result = home.cmd(ctx, new Min(property,where));
        }
        catch (HomeException he)
        {
            // Ignore.  Try to find min value using visitor below.
            if (LogSupport.isDebugEnabled(ctx))
            {
                new DebugLogMsg(this, "Min projection failed on " + home.getClass().getSimpleName(), he).log(ctx);
            }
            projectionFailed = true;
        }

        if (result instanceof Number)
        {
            // Example: XDB returns a number when a Min projection is passed in a CMD 
            min = ((Number)result).doubleValue();
        }
        else if (projectionFailed || !isDatabaseHome(home))
        {
            if (LogSupport.isDebugEnabled(ctx))
            {
                new DebugLogMsg(this, "Attempting to determine min value on " + home.getClass().getSimpleName() + " using forEach with MinVisitor...", null).log(ctx);
            }
            
            // Example: Some home's will not handle the Min projection, and will return it.
            // In such cases, we need to use a min visitor which is admittedly not as efficient.
            FunctionVisitor fv = new FunctionVisitor(property, new MinVisitor());
            Visitor resultVisitor = home.forEach(ctx, fv, where);
            resultVisitor = Visitors.find(resultVisitor, MinVisitor.class);
            if (!(resultVisitor instanceof MinVisitor))
            {
                resultVisitor = Visitors.find(fv, MinVisitor.class);
            }
            if (resultVisitor instanceof MinVisitor)
            {
                min = ((MinVisitor) resultVisitor).getValue();
            }
        }
        
        return min;
    }


    /**
     * {@inheritDoc}
     */
    public Number average(Context ctx, final PropertyInfo property, Object where) throws HomeInternalException, HomeException
    {
        Home home = getHome(ctx, property.getXInfo().getBeanClass());
        return average(ctx, property, home, where);
    }

    
    /**
     * {@inheritDoc}
     */
    public Number average(Context ctx, PropertyInfo property, final Home home, Object where) throws HomeInternalException, HomeException
    {
        where = wrapKeyWithEQ(ctx, property.getBeanClass(), where);

        Number avg = 0;

        FunctionVisitor fv = new FunctionVisitor(property, new AvgVisitor());
        Visitor resultVisitor = home.forEach(ctx, fv, where);
        resultVisitor = Visitors.find(resultVisitor, AvgVisitor.class);
        if (!(resultVisitor instanceof AvgVisitor))
        {
            resultVisitor = Visitors.find(fv, AvgVisitor.class);
        }
        if (resultVisitor instanceof AvgVisitor)
        {
            avg = (Number)((AvgVisitor) resultVisitor).getValue();
        }

        return avg;
    }


    /**
     * {@inheritDoc}
     */
    public Number sum(Context ctx, final PropertyInfo property, Object where) throws HomeInternalException, HomeException
    {
        Home home = getHome(ctx, property.getXInfo().getBeanClass());
        return sum(ctx, property, home, where);
    }


    /**
     * {@inheritDoc}
     */
    public Number sum(Context ctx, PropertyInfo property, final Home home, Object where) throws HomeInternalException, HomeException
    {
        where = wrapKeyWithEQ(ctx, property.getBeanClass(), where);

        Number sum = 0;

        boolean projectionFailed = false;

        Object result = new Object();

        try
        {
            result = home.cmd(ctx, new Sum(property, where));
        }
        catch (HomeException he)
        {
            //Do nothing
            if (LogSupport.isDebugEnabled(ctx))
            {
                new DebugLogMsg(this, "Sum projection failed on " + home.getClass().getSimpleName(), he).log(ctx);
            }
            projectionFailed = true;
        }

        if (result instanceof Number)
        {
            // Example: XDB returns a number when a Sum projection is passed in a CMD 
            sum = (Number)result;
        }
        else if (projectionFailed || !isDatabaseHome(home))
        {
            if (LogSupport.isDebugEnabled(ctx))
            {
                new DebugLogMsg(this, "Attempting to determine sum value on " + home.getClass().getSimpleName() + " using forEach with SumVisitor...", null).log(ctx);
            }
            
            // Example: Some home's will not handle the Sum projection, and will return it.
            // In such cases, we need to use a sum visitor which is admittedly not as efficient.
            FunctionVisitor fv = new FunctionVisitor(property, new SumVisitor());
            Visitor resultVisitor = home.forEach(ctx, fv, where);
            resultVisitor = Visitors.find(resultVisitor, SumVisitor.class);
            if (!(resultVisitor instanceof SumVisitor))
            {
                resultVisitor = Visitors.find(fv, SumVisitor.class);
            }
            if (resultVisitor instanceof SumVisitor)
            {
                sum = (Number)((SumVisitor) resultVisitor).getValue();
            }
        }

        return sum;
    }


    /**
     * {@inheritDoc}
     */
    public Object wrapKeyWithEQ(Context ctx, Class cls, Object where)
    {
        if (where != null
                && !(where instanceof XStatement
                        || where instanceof Predicate
                        || where instanceof Context))
        {
            if (cls.isInstance(where))
            {
                Object bean = where;
                where = XBeans.getInstanceOf(ctx, where, XStatement.class);
                if (where instanceof BeanAwareFacet)
                {
                    ((BeanAwareFacet) where).setBean(bean);
                }
            }
            else
            {
                Object xinfoObj = XBeans.getInstanceOf(ctx, cls, XInfo.class);
                if (xinfoObj instanceof XInfo)
                {
                    XInfo xinfo = (XInfo) xinfoObj;
                    if (xinfo.getID() != null)
                    {
                        Class type = null;
                        try
                        {
                            type = xinfo.getID().getType();
                        }
                        catch (Exception e)
                        {
                            // Exception will be thrown for the ID beans which do not have getType method implemented.
                            // In such case BSS should use exact where which is passed in this method.
                        }
                        if (type != null  && !( type.isAssignableFrom(com.redknee.framework.xhome.beans.xi.CompoundPropertyInfo.class)))
                        {
                            Object key = null;
                            if (type.isInstance(where))
                            {
                                key = where;   
                            }
                            else if (where instanceof Number)
                            {
                                // Wrap keys with EQs containing correct data type
                                Number number = (Number) where;
                                if (type.isAssignableFrom(Long.class))
                                {
                                    key = number.longValue();
                                }
                                else if (type.isAssignableFrom(Integer.class))
                                {
                                    key = number.intValue();
                                }
                                else if (type.isAssignableFrom(Short.class))
                                {
                                    key = number.shortValue();
                                }
                                else if (type.isAssignableFrom(Double.class))
                                {
                                    key = number.doubleValue();
                                }
                                else if (type.isAssignableFrom(Float.class))
                                {
                                    key = number.floatValue();
                                }
                                else if (type.isAssignableFrom(Byte.class))
                                {
                                    key = number.byteValue();
                                }
                                else if (type.isAssignableFrom(Boolean.class))
                                {
                                    if (number.longValue() == 0)
                                    {
                                        key = false;
                                    }
                                    else if (number.longValue() == 1)
                                    {
                                        key = true;
                                    }
                                }
                            }
                            if (key != null)
                            {
                                where = new EQ(xinfo.getID(), key);
                            }
                        }
                    }
                }
            }
        }
        return where;
    }


    /**
     * {@inheritDoc}
     */
    public Home getHome(Context ctx, Class cls) throws HomeException
    {
        if (ctx == null)
        {
            throw new HomeException("Unable to process " + cls.getSimpleName() + " home operation.  Context not set.");   
        }
        
        final Class homeClass;
        homeClass = getHomeClass(ctx, cls);
        return getHome(ctx, (Object) homeClass);
    }

    
    /**
     * {@inheritDoc}
     */
    public Home getHome(Context ctx, Object contextKey) throws HomeException
    {
        if (ctx == null)
        {
            throw new HomeException("Unable to retrieve home for context key " + contextKey + ".  Context not set.");   
        }

        if (contextKey == null)
        {
            throw new HomeException("Unable to retrieve home for null context key.");
        }

        final Object home = ctx.get(contextKey);
        if (home == null || !(home instanceof Home))
        {
            throw new HomeException("No " + contextKey + " installed in context!");
        }

        return (Home) home;
    }

    protected boolean isDatabaseHome(Home home)
    {
        return home instanceof XDBHome
                || (home instanceof HomeProxy
                        && ((HomeProxy)home).hasDecorator(XDBHome.class));
    }

    /**
     * Retrieves home class corresponding to the given class.
     * 
     * @param ctx Operating context
     * @param cls Bean class or Home class.
     * 
     * @return Home class
     */
    private Class getHomeClass(Context ctx, Class cls)
    {
        final Class homeClass;
        if (Home.class.isAssignableFrom(cls))
        {
            homeClass = cls;
        }
        else
        {
            homeClass = XBeans.getClass(ctx, cls, Home.class);
        }
        return homeClass;
    }

    /**
     * A list building visitor that doesn't implement the java.util.List
     * interface.  This is required to propery do sorted-limit queries
     * on some types of homes (i.e. Journal/Transient homes). 
     *
     * @author aaron.gourley@redknee.com
     * @since 8.2
     */
    private class CustomListBuildingVisitor<T> implements Visitor
    {
        public CustomListBuildingVisitor(Visitor delegate)
        {
            delegate_ = delegate;
        }

        /**
         * {@inheritDoc}
         */
        public void visit(Context ctx, Object obj) throws AgentException, AbortVisitException
        {
            delegate_.visit(ctx, obj);
        }

        private Visitor delegate_;
    }
    
    public static String TABLE_NAME = "TABLE NAME";
}
