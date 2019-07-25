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
package com.redknee.app.crm.support;

import java.util.Collection;
import java.util.Map;

import com.redknee.framework.xhome.beans.AbstractBean;
import com.redknee.framework.xhome.beans.Identifiable;
import com.redknee.framework.xhome.beans.xi.PropertyInfo;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.HomeInternalException;


/**
 * Interface containing a series of methods that are meant to perform common
 * Home operations conveniently and efficiently.
 *
 * @author Aaron Gourley
 * @since 8.2
 */
public interface HomeSupport extends Support
{

    /**
     * Creates a bean in the coresponding home and returns a strongly typed result bean.
     * 
     * @param <T>
     * @param ctx Operating context.
     * @param bean The bean we are creating.
     *
     * @return Bean identified by the given filter criteria.
     *
     * @throws HomeInternalException, HomeException If an error occurs during Home operation execution.
     */
    public <T extends AbstractBean> T createBean(Context ctx, T bean) throws HomeInternalException, HomeException;

    /**
     * Stores a bean in the coresponding home and returns a strongly typed result bean.
     *
     * @param <T>
     * @param ctx Operating context.
     * @param bean The bean we are storing.
     *
     * @return Bean identified by the given filter criteria.
     *
     * @throws HomeInternalException, HomeException If an error occurs during Home operation execution.
     */
    public <T extends AbstractBean> T storeBean(Context ctx, T bean) throws HomeInternalException, HomeException;

    /**
     * Removes a bean in the coresponding home.
     *
     * @param <T>
     * @param ctx Operating context.
     * @param bean The bean we are removing.
     *
     * @return Bean identified by the given filter criteria.
     *
     * @throws HomeInternalException, HomeException If an error occurs during Home operation execution.
     */
    public <T extends AbstractBean> void removeBean(Context ctx, T bean) throws HomeInternalException, HomeException;

    /**
     * Retrieves a strongly typed bean matching filter criteria from the given
     * bean's home.
     * 
     * @param <T>
     * @param ctx Operating context.
     * @param beanType Type of bean we are looking for.
     * @param where Filter criteria
     * 
     * @return Bean identified by the given filter criteria.
     * 
     * @throws HomeInternalException, HomeException If an error occurs during Home operation execution.
     */
    public <T extends AbstractBean> T findBean(Context ctx, Class<T> beanType, Object where) throws HomeInternalException, HomeException;


    /**
     * Retrieves a parameterized collection of beans matching filter criteria
     * from the given bean's home.
     * 
     * @param <T>
     * @param ctx Operating context
     * @param beanType Type of bean that we are looking for.
     * 
     * @return Parameterized collection of all beans of the desired type.
     * 
     * @throws HomeInternalException, HomeException If an error occurs during Home operation execution.
     */
    public <T extends AbstractBean> Collection<T> getBeans(Context ctx, Class<T> beanType) throws HomeInternalException, HomeException;


    /**
     * Retrieves a parameterized collection of beans matching filter criteria
     * from the given bean's home.
     * 
     * @param <T>
     * @param ctx Operating context
     * @param beanType Type of bean that we are looking for.
     * @param where Filter criteria.
     * 
     * @return Parameterized collection of beans of the desired type that match
     *          the filter criteria.
     * 
     * @throws HomeInternalException, HomeException If an error occurs during Home operation execution.
     */
    public <T extends AbstractBean> Collection<T> getBeans(Context ctx, Class<T> beanType, Object where) throws HomeInternalException, HomeException;


    /**
     * Retrieves a parameterized collection of a limited number of beans matching
     * filter criteria from the given bean's home.
     * 
     * @param <T>
     * @param ctx Operating context
     * @param beanType Type of bean that we are looking for.
     * @param where Filter criteria.
     * @param limit Maximum number of beans to retrieve
     * 
     * @return Parameterized collection of beans of the desired type that match
     *          the filter criteria.
     * 
     * @throws HomeInternalException, HomeException If an error occurs during Home operation execution.
     */
    public <T extends AbstractBean> Collection<T> getBeans(Context ctx, Class<T> beanType, Object where, int limit) throws HomeInternalException, HomeException;


    /**
     * Retrieves a sorted, parameterized collection of beans matching filter criteria
     * from the given bean's home.
     * 
     * @param <T>
     * @param ctx Operating context
     * @param beanType Type of bean that we are looking for.
     * @param where Filter criteria.
     * @param orderAscending Flag indicating whether or not to sort in ascending or descending order (by primary key).
     * 
     * @return Sorted, parameterized collection of beans of the desired type that match the filter criteria.
     * 
     * @throws HomeInternalException, HomeException If an error occurs during Home operation execution.
     */
    public <T extends AbstractBean> Collection<T> getBeans(Context ctx, Class<T> beanType, Object where, boolean orderAscending) throws HomeInternalException, HomeException;


    /**
     * Retrieves a sorted, parameterized collection of a limited number of beans
     * matching filter criteria from the given bean's home.
     * 
     * @param <T>
     * @param ctx Operating context
     * @param beanType Type of bean that we are looking for.
     * @param where Filter criteria.
     * @param limit Maximum number of beans to retrieve
     * @param orderAscending Flag indicating whether or not to sort in ascending or descending order (by primary key).
     * 
     * @return Sorted, parameterized collection of beans of the desired type that match the filter criteria.
     * 
     * @throws HomeInternalException, HomeException If an error occurs during Home operation execution.
     */
    public <T extends AbstractBean> Collection<T> getBeans(Context ctx, Class<T> beanType, Object where, int limit, boolean orderAscending) throws HomeInternalException, HomeException;


    /**
     * Retrieves a sorted, parameterized collection of beans matching filter criteria
     * from the given bean's home.
     * 
     * @param <T>
     * @param ctx Operating context
     * @param beanType Type of bean that we are looking for.
     * @param where Filter criteria.
     * @param orderAscending Flag indicating whether or not to sort in ascending or descending order.
     * @param orderBy Fields to sort on.
     * 
     * @return Sorted, parameterized collection of beans of the desired type that match the filter criteria.
     * 
     * @throws HomeInternalException, HomeException If an error occurs during Home operation execution.
     */
    public <T extends AbstractBean> Collection<T> getBeans(Context ctx, Class<T> beanType, Object where, boolean orderAscending, PropertyInfo... orderBy) throws HomeInternalException, HomeException;


    /**
     * Retrieves a sorted, parameterized collection of a limited number of beans
     * matching filter criteria from the given bean's home.
     * 
     * @param <T>
     * @param ctx Operating context
     * @param beanType Type of bean that we are looking for.
     * @param where Filter criteria.
     * @param limit Maximum number of beans to retrieve
     * @param orderAscending Flag indicating whether or not to sort in ascending or descending order.
     * @param orderBy Fields to sort on.
     * 
     * @return Sorted, parameterized collection of beans of the desired type that match the filter criteria.
     * 
     * @throws HomeInternalException, HomeException If an error occurs during Home operation execution.
     */
    public <T extends AbstractBean> Collection<T> getBeans(Context ctx, Class<T> beanType, Object where, int limit, boolean orderAscending, PropertyInfo... orderBy) throws HomeInternalException, HomeException;


    /**
     * Retrieves a parameterized collection of beans matching filter criteria
     * from the given bean's home.  The resulting beans will be frozen.
     * 
     * @param <T>
     * @param ctx Operating context
     * @param beanType Type of bean that we are looking for.
     * 
     * @return Parameterized collection of all beans of the desired type.
     * 
     * @throws HomeInternalException, HomeException If an error occurs during Home operation execution.
     */
    public <T extends AbstractBean> Collection<T> getReadOnlyBeans(Context ctx, Class<T> beanType) throws HomeInternalException, HomeException;


    /**
     * Retrieves a parameterized collection of beans matching filter criteria
     * from the given bean's home.  The resulting beans will be frozen.
     * 
     * @param <T>
     * @param ctx Operating context
     * @param beanType Type of bean that we are looking for.
     * @param where Filter criteria.
     * 
     * @return Parameterized collection of beans of the desired type that match
     *          the filter criteria.
     * 
     * @throws HomeInternalException, HomeException If an error occurs during Home operation execution.
     */
    public <T extends AbstractBean> Collection<T> getReadOnlyBeans(Context ctx, Class<T> beanType, Object where) throws HomeInternalException, HomeException;


    /**
     * Retrieves a parameterized collection of a limited number of beans matching
     * filter criteria from the given bean's home.  The resulting beans will be frozen.
     * 
     * @param <T>
     * @param ctx Operating context
     * @param beanType Type of bean that we are looking for.
     * @param where Filter criteria.
     * @param limit Maximum number of beans to retrieve
     * 
     * @return Parameterized collection of beans of the desired type that match
     *          the filter criteria.
     * 
     * @throws HomeInternalException, HomeException If an error occurs during Home operation execution.
     */
    public <T extends AbstractBean> Collection<T> getReadOnlyBeans(Context ctx, Class<T> beanType, Object where, int limit) throws HomeInternalException, HomeException;


    /**
     * Retrieves a sorted, parameterized collection of beans matching filter criteria
     * from the given bean's home.  The resulting beans will be frozen.
     * 
     * @param <T>
     * @param ctx Operating context
     * @param beanType Type of bean that we are looking for.
     * @param where Filter criteria.
     * @param orderAscending Flag indicating whether or not to sort in ascending or descending order (by primary key).
     * 
     * @return Sorted, parameterized collection of beans of the desired type that match the filter criteria.
     * 
     * @throws HomeInternalException, HomeException If an error occurs during Home operation execution.
     */
    public <T extends AbstractBean> Collection<T> getReadOnlyBeans(Context ctx, Class<T> beanType, Object where, boolean orderAscending) throws HomeInternalException, HomeException;


    /**
     * Retrieves a sorted, parameterized collection of a limited number of beans
     * matching filter criteria from the given bean's home.  The resulting beans
     * will be frozen.
     * 
     * @param <T>
     * @param ctx Operating context
     * @param beanType Type of bean that we are looking for.
     * @param where Filter criteria.
     * @param limit Maximum number of beans to retrieve
     * @param orderAscending Flag indicating whether or not to sort in ascending or descending order (by primary key).
     * 
     * @return Sorted, parameterized collection of beans of the desired type that match the filter criteria.
     * 
     * @throws HomeInternalException, HomeException If an error occurs during Home operation execution.
     */
    public <T extends AbstractBean> Collection<T> getReadOnlyBeans(Context ctx, Class<T> beanType, Object where, int limit, boolean orderAscending) throws HomeInternalException, HomeException;


    /**
     * Retrieves a sorted, parameterized collection of beans matching filter criteria
     * from the given bean's home.  The resulting beans will be frozen.
     * 
     * @param <T>
     * @param ctx Operating context
     * @param beanType Type of bean that we are looking for.
     * @param where Filter criteria.
     * @param orderAscending Flag indicating whether or not to sort in ascending or descending order.
     * @param orderBy Fields to sort on.
     * 
     * @return Sorted, parameterized collection of beans of the desired type that match the filter criteria.
     * 
     * @throws HomeInternalException, HomeException If an error occurs during Home operation execution.
     */
    public <T extends AbstractBean> Collection<T> getReadOnlyBeans(Context ctx, Class<T> beanType, Object where, boolean orderAscending, PropertyInfo... orderBy) throws HomeInternalException, HomeException;


    /**
     * Retrieves a sorted, parameterized collection of a limited number of beans
     * matching filter criteria from the given bean's home.  The resulting beans
     * will be frozen.
     * 
     * @param <T>
     * @param ctx Operating context
     * @param beanType Type of bean that we are looking for.
     * @param where Filter criteria.
     * @param limit Maximum number of beans to retrieve
     * @param orderAscending Flag indicating whether or not to sort in ascending or descending order.
     * @param orderBy Fields to sort on.
     * 
     * @return Sorted, parameterized collection of beans of the desired type that match the filter criteria.
     * 
     * @throws HomeInternalException, HomeException If an error occurs during Home operation execution.
     */
    public <T extends AbstractBean> Collection<T> getReadOnlyBeans(Context ctx, Class<T> beanType, Object where, int limit, boolean orderAscending, PropertyInfo... orderBy) throws HomeInternalException, HomeException;

    /**
     * Retrieves collection of a limited number of beans mapped as (ID->bean)
     * matching filter criteria from the given bean's home.
     * 
     * @param <T>
     * @param ctx Operating context
     * @param beanType Type of bean that we are looking for.
     * @param where Filter criteria.
     * @param limit Maximum number of beans to retrieve
     * 
     * @return A Map of (ID->bean) of the desired type that match the filter criteria.
     * 
     * @throws HomeInternalException, HomeException If an error occurs during Home operation execution.
     */
    public <T extends AbstractBean & Identifiable> Map<Object, T> getMappedBeans(Context ctx, Class<T> beanType, Object where, int limit) throws HomeInternalException, HomeException;

    

    /**
     * Retrieves a sub-context where the bean's home is matches the given filter criteria.
     * 
     * @param <T>
     * @param parentCtx Operating context
     * @param beanType Type of bean that we want to filter in the context.
     * @param where Filter criteria.
     * 
     * @return Sub-context containing the bean's home wrapped to match the filter criteria.
     * 
     * @throws HomeInternalException, HomeException If an error occurs during Home operation execution.
     */
    public Context getWhereContext(Context parentCtx, Class<? extends AbstractBean> beanType, Object where);


    /**
     * Retrieves a sub-context where the bean's home is sorted and matches the given filter criteria.
     * 
     * @param <T>
     * @param parentCtx Operating context
     * @param beanType Type of bean that we want to filter in the context.
     * @param where Filter criteria.
     * @param orderAscending Flag indicating whether or not to sort in ascending or descending order.
     * 
     * @return Sub-context containing the bean's home wrapped to sort output and match the filter criteria.
     * 
     * @throws HomeInternalException, HomeException If an error occurs during Home operation execution.
     */
    public Context getWhereContext(Context parentCtx, Class<? extends AbstractBean> beanType, Object where, boolean orderAscending);


    /**
     * Retrieves a sub-context where the bean's home is sorted and matches the given filter criteria.
     * 
     * @param <T>
     * @param parentCtx Operating context
     * @param beanType Type of bean that we want to filter in the context.
     * @param where Filter criteria.
     * @param orderAscending Flag indicating whether or not to sort in ascending or descending order.
     * @param orderBy Fields to sort on.
     * 
     * @return Sub-context containing the bean's home wrapped to sort output and match the filter criteria.
     * 
     * @throws HomeInternalException, HomeException If an error occurs during Home operation execution.
     */
    public Context getWhereContext(Context parentCtx, Class<? extends AbstractBean> beanType, Object where, boolean orderAscending, PropertyInfo... orderBy);


    /**
     * Efficiently calculates the number of beans matching filter criteria
     * from the given bean's home.
     * 
     * @param ctx Operating context
     * @param cls Bean class or Home class.
     * @param where Filter criteria.
     * 
     * @return Number of beans that match the filter criteria.
     * 
     * @throws HomeInternalException, HomeException If an error occurs during Home operation execution.
     */
    public long getBeanCount(Context ctx, Class cls, Object where) throws HomeInternalException, HomeException;


    /**
     * Efficiently calculates the number of beans matching filter criteria
     * from the given home.
     * 
     * @param ctx Operating context
     * @param home Home to get the beans from.
     * @param where Filter criteria.
     * 
     * @return Number of beans that match the filter criteria.
     * 
     * @throws HomeInternalException, HomeException If an error occurs during Home operation execution.
     */
    public long getBeanCount(Context ctx, Home home, Object where) throws HomeInternalException, HomeException;

    /**
     * Efficiently determines whether or not there are beans matching the
     * filter criteria.
     * 
     * @param ctx Operating context
     * @param cls Bean class or Home class.
     * @param where Filter criteria.
     * 
     * @return True iff there are beans matching the filter criteria in the
     *          given bean's home.
     * 
     * @throws HomeInternalException, HomeException If an error occurs during Home operation execution.
     */
    public boolean hasBeans(Context ctx, Class cls, Object where) throws HomeInternalException, HomeException;


    /**
     * Efficiently determines whether or not there are beans matching the
     * filter criteria in the given home.
     * 
     * @param ctx Operating context
     * @param home Home to look for the beans in.
     * @param where Filter criteria.
     * 
     * @return True iff there are beans matching the filter criteria in the
     *          given home.
     * 
     * @throws HomeInternalException, HomeException If an error occurs during Home operation execution.
     */
    public boolean hasBeans(Context ctx, Home home, Object where) throws HomeInternalException, HomeException;


    /**
     * Efficiently determines whether or not there are at least the given
     * number of beans matching the filter criteria.
     * 
     * @param ctx Operating context
     * @param cls Bean class or Home class.
     * @param count How many beans are we looking for?
     * @param where Filter criteria.
     * 
     * @return True iff there are beans matching the filter criteria in the
     *          given bean's home.
     * 
     * @throws HomeInternalException, HomeException If an error occurs during Home operation execution.
     */
    public boolean hasBeans(Context ctx, Class cls, int count, Object where) throws HomeInternalException, HomeException;


    /**
     * Efficiently determines whether or not there are at least the given
     * number of beans matching the filter criteria.
     * 
     * @param ctx Operating context
     * @param home Home to look for the beans in.
     * @param count How many beans are we looking for?  If 0, returns true iff there are no beans (i.e. "Does this home have 0 beans?  Yes!").
     * @param where Filter criteria.
     * 
     * @return True iff there are beans matching the filter criteria in the
     *          given home.
     * 
     * @throws HomeInternalException, HomeException If an error occurs during Home operation execution.
     */
    public boolean hasBeans(Context ctx, Home home, int count, Object where) throws HomeInternalException, HomeException;


    /**
     * Efficiently computes the maximum value of the given property for all beans matching
     * the filter criteria in the given property's bean's home.  This operation is more
     * efficient for XDB homes than other Homes, but works for any type of Home.
     * 
     * @param ctx Operating context
     * @param property Bean property to compute the Maximum of.  The property must be of type Comparable.
     * @param where Filter criteria.
     * 
     * @return Maximum value of the given property across all matching beans.
     * 
     * @throws HomeInternalException, HomeException If an error occurs during Home operation execution.
     */
    public Object max(Context ctx, PropertyInfo property, Object where) throws HomeInternalException, HomeException;


    /**
     * Efficiently computes the maximum value of the given property for all beans matching
     * the filter criteria in the given home.  This operation is more efficient for XDB homes
     * than other Homes, but works for any type of Home.
     * 
     * @param ctx Operating context
     * @param property Bean property to compute the Maximum of.  The property must be of type Comparable.
     * @param home Home to perform calculations on.
     * @param where Filter criteria.
     * 
     * @return Maximum value of the given property across all matching beans.
     * 
     * @throws HomeInternalException, HomeException If an error occurs during Home operation execution.
     */
    public Object max(Context ctx, PropertyInfo property, Home home, Object where) throws HomeInternalException, HomeException;


    /**
     * Efficiently computes the minimum value of the given property for all beans matching
     * the filter criteria in the given property's bean's home.  This operation is more
     * efficient for XDB homes than other Homes, but works for any type of Home.
     * 
     * @param ctx Operating context
     * @param property Bean property to compute the Minimum of.  The property must be of type Comparable.
     * @param where Filter criteria.
     * 
     * @return Minimum value of the given property across all matching beans.
     * 
     * @throws HomeInternalException, HomeException If an error occurs during Home operation execution.
     */
    public Object min(Context ctx, PropertyInfo property, Object where) throws HomeInternalException, HomeException;


    /**
     * Efficiently computes the minimum value of the given property for all beans matching
     * the filter criteria in the given home.  This operation is more efficient for XDB homes
     * than other Homes, but works for any type of Home.
     * 
     * @param ctx Operating context
     * @param property Bean property to compute the Minimum of.  The property must be of type Comparable.
     * @param home Home to perform calculations on.
     * @param where Filter criteria.
     * 
     * @return Minimum value of the given property across all matching beans.
     * 
     * @throws HomeInternalException, HomeException If an error occurs during Home operation execution.
     */
    public Object min(Context ctx, PropertyInfo property, Home home, Object where) throws HomeInternalException, HomeException;


    /**
     * Efficiently computes the average value of the given property for all beans matching
     * the filter criteria in the given property's bean's home.  This operation is more
     * efficient for XDB homes than other Homes, but works for any type of Home.
     * 
     * @param ctx Operating context
     * @param property Bean property to compute the Average of.  The property must be of numeric type.
     * @param where Filter criteria.
     * 
     * @return Average value of the given property across all matching beans.
     * 
     * @throws HomeInternalException, HomeException If an error occurs during Home operation execution.
     */
    public Number average(Context ctx, final PropertyInfo property, Object where) throws HomeInternalException, HomeException;

    /**
     * Efficiently computes the average value of the given property for all beans matching
     * the filter criteria in the given home.  This operation is more efficient for XDB homes
     * than other Homes, but works for any type of Home.
     * 
     * @param ctx Operating context
     * @param property Bean property to compute the Average of.  The property must be of numeric type.
     * @param home Home to perform calculations on.
     * @param where Filter criteria.
     * 
     * @return Average value of the given property across all matching beans.
     * 
     * @throws HomeInternalException, HomeException If an error occurs during Home operation execution.
     */
    public Number average(Context ctx, PropertyInfo property, final Home home, Object where) throws HomeInternalException, HomeException;


    /**
     * Efficiently computes the sum of all values of the given property for all beans matching
     * the filter criteria in the given property's bean's home.  This operation is more efficient
     * for XDB homes than other Homes, but works for any type of Home.
     * 
     * @param ctx Operating context
     * @param property Bean property to compute the Sum of.  The property must be of numeric type.
     * @param where Filter criteria.
     * 
     * @return Total value of the given property across all matching beans.
     * 
     * @throws HomeInternalException, HomeException If an error occurs during Home operation execution.
     */
    public Number sum(Context ctx, final PropertyInfo property, Object where) throws HomeInternalException, HomeException;


    /**
     * Efficiently computes the sum of all values of the given property for all beans matching
     * the filter criteria in the given home.  This operation is more efficient for XDB homes
     * than other Homes, but works for any type of Home.
     * 
     * @param ctx Operating context
     * @param property Bean property to compute the Sum of.  The property must be of numeric type.
     * @param home Home to perform calculations on.
     * @param where Filter criteria.
     * 
     * @return Total value of the given property across all matching beans.
     * 
     * @throws HomeInternalException, HomeException If an error occurs during Home operation execution.
     */
    public Number sum(Context ctx, PropertyInfo property, final Home home, Object where) throws HomeInternalException, HomeException;


    public Object wrapKeyWithEQ(Context ctx, Class cls, Object where);


    /**
     * Retrieves a non-null instance of the given bean's Home.
     * 
     * @param ctx Operating context
     * @param cls Bean class or Home class.
     * 
     * @return Non-null instance of Home stored under the given bean's default key.
     * 
     * @throws HomeException If no Home is installed in the context under the default key for the bean.
     */
    public Home getHome(Context ctx, Class cls) throws HomeException;


    /**
     * Retrieves a non-null instance of the desired Home.
     * 
     * @param ctx Operating context
     * @param contextKey Key under which the desired Home is stored in the Context.
     * 
     * @return Non-null instance of Home stored under the given key.
     * 
     * @throws HomeException If no Home is installed in the context under the given key.
     */
    public Home getHome(Context ctx, Object contextKey) throws HomeException;
}
