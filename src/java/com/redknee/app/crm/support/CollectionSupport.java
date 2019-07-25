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
import java.util.Comparator;
import java.util.Map;

import com.redknee.framework.xhome.beans.Function;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.filter.Predicate;
import com.redknee.framework.xhome.home.Adapter;
import com.redknee.framework.xhome.home.HomeException;


/**
 * A set of utilitiy functions to use with Java utility Collections, Framework
 * {@link Predicate Predicates}, and {@link Function Functions}.
 *
 * @author gary.anderson@redknee.com
 */
public interface CollectionSupport extends Support
{
    /**
     * Performs a logical AND on the results of the give predicate on the
     * objects in the given collection.  Returns true if application of the
     * predicate to all objects in the collection returns true.  Returns false
     * as soon as the first application of the predate to an object returns
     * false.
     *
     * @param objects The collection of objects on which to operate.
     * @param predicate The predicate to apply to the objects in the
     * collection.
     *
     * @exception IllegalArgumentException Thrown if either the objects
     * parameter or the predicate parameter is null.
     */
    public boolean and(Context ctx,final Collection objects, final Predicate predicate);


    /**
     * Finds the first object in the given collection that returns true for the
     * given matcher.
     *
     * @param objects The collection of objects on which to operate.
     * @param matcher The matcher to apply to the objects in the collection.
     *
     * @exception IllegalArgumentException Thrown if either the objects
     * parameter or the matcher parameter is null.
     */
    public <T> T findFirst(Context ctx,final Collection<T> objects, final Predicate matcher);


    /**
     * Finds all the objects in the given collection that return true for the
     * given matcher, and adds them to the given matches collection.
     *
     * @param objects The collection of objects on which to operate.
     * @param matcher The matcher to apply to the objects in the collection.
     * @param matches [OUT] The objects in the given collection that match.
     *
     * @exception IllegalArgumentException Thrown if either the objects
     * parameter, the matcher parameter, or the matches parameter is null.
     */
    public <T> void findAll(Context ctx,
        final Collection<T> objects,
        final Predicate matcher,
        final Collection<T> matches);


    public Object[] adaptCollectionToArray(final Context ctx, final Collection collection, final Adapter adapter) throws HomeException;


    public Collection adaptCollection(final Context ctx, final Collection collection, final Adapter adapter) throws HomeException;
    
    public <T> T[] adaptCollection(final Context ctx, final Collection collection, final Adapter adapter,
            final T[] targetArray) throws HomeException;


    public <T> Collection<T> adaptCollection(final Context ctx, final Collection collection, final Adapter adapter,
        final Class<T> targetType) throws HomeException;


    /**
     * Finds all the objects in the given collection that return true for the
     * given matcher, and adds them to the given matches collection.
     *
     * @param objects The collection of objects on which to operate.
     * @param matcher The matcher to apply to the objects in the collection.
     * @param return The objects in the given collection that match.
     *
     * @exception IllegalArgumentException Thrown if any of the objects
     * parameter, the matcher parameter, or the matches parameter is null.
     */
    public <T> Collection<T> findAll(Context ctx,
        final Collection<T> objects,
        final Predicate matcher);


    /**
     * Applies the given operation to each object in the collection until the
     * operation operates on all objects in the collection, or until the
     * operation returns false, whichever comes first.
     *
     * @param objects The collection of objects on which to operate.
     * @param operation The operation to apply to the objects in the
     * collection.
     *
     * @exception IllegalArgumentException Thrown if either the objects
     * parameter or the operation parameter is null.
     */
    public void forEach(Context ctx,final Collection objects, final Predicate operation);


    /**
     * Maps the given keys to the values produced by the given valueMaker.  That
     * is, for each key:
     *
     * <pre>map.put(key, valueMaker.f(key);</pre>
     *
     * @param keys A collection of keys.
     * @param valueMaker Creates the value for each key.
     * @param map [OUT] The Map into which the key-value pairs are placed.
     *
     * @exception IllegalArgumentException Thrown if any of the keys parameter,
     * the valueMaker parameter, or the map parameter is null.
     */
    public <T> void mapKeys(Context ctx,
        final Collection<T> keys,
        final Function valueMaker,
        final Map<T, Object> map);


    /**
     * Maps the given keys to the values produced by the given valueMaker.  That
     * is, for each key:
     *
     * <pre>map.put(key, valueMaker.f(key))</pre>
     *
     * @param keys A collection of keys.
     * @param valueMaker Creates the value for each key.
     * @return The Map into which the key-value pairs are placed.
     *
     * @exception IllegalArgumentException Thrown if any of the keys parameter,
     * the valueMaker parameter, or the map parameter is null.
     */
    public <T> Map<T, Object> mapKeys(Context ctx,
        final Collection<T> keys,
        final Function valueMaker);


    /**
     * Maps the keys produced by the given keyMaker to the given values.  That
     * is, for each value:
     *
     * <pre>map.put(keyMaker.f(value), value)</pre>
     *
     * @param values A collection of values.
     * @param keyMaker Creates the key for each value.
     * @param map [OUT] The Map into which the key-value pairs are placed.
     *
     * @exception IllegalArgumentException Thrown if any of the values parameter,
     * the keyMaker parameter, or the map parameter is null.
     */
    public <T> void mapValues(Context ctx,
        final Collection<T> values,
        final Function keyMaker,
        final Map<Object, T> map);


    /**
     * Maps the keys produced by the given keyMaker to the given values.  That
     * is, for each value:
     *
     * <pre>map.put(keyMaker.f(value), value)</pre>
     *
     * @param values A collection of values.
     * @param keyMaker Creates the key for each value.
     * @return The Map into which the key-value pairs are placed.
     *
     * @exception IllegalArgumentException Thrown if any of the values parameter,
     * the keyMaker parameter, or the map parameter is null.
     */
    public <T> Map<Object,T> mapValues(Context ctx,
        final Collection<T> values,
        final Function keyMaker);


    /**
     * Performs a logical OR on the results of the give predicate on the objects
     * in the given collection.  Returns true as soon as the first application
     * of the predate to an object returns true.  Returns false if application
     * of the predicate to all objects in the collection returns false.
     *
     * @param objects The collection of objects on which to operate.
     * @param predicate The predicate to apply to the objects in the
     * collection.
     *
     * @exception IllegalArgumentException Thrown if either the objects
     * parameter or the predicate parameter is null.
     */
    public boolean or(Context ctx,final Collection objects, final Predicate predicate);


    /**
     * Processes each of the objects in the given objects collection, using the
     * given function, and places the results in the given results collection.
     *
     * @param objects The collection of objects to process.
     * @param function The function used to process the given objects.
     * @param results [OUT] The collection into which the results are placed.
     *
     * @exception IllegalArgumentException Throw if any of the objects
     * parameter, the function parameter, or the results parameter are null.
     */
    public void process(Context ctx,
        final Collection objects,
        final Function function,
        final Collection results);


    /**
     * Processes each of the objects in the given objects collection, using the
     * given function, and places the results in the given results collection.
     *
     * @param objects The collection of objects to process.
     * @param function The function used to process the given objects.
     * @return The collection into which the results are placed.
     *
     * @exception IllegalArgumentException Throw if any of the objects
     * parameter, the function parameter, or the results parameter are null.
     */
    public Collection process(Context ctx,
        final Collection objects,
        final Function function);


    /**
     * Processes each of the objects in the given objects collection, using the
     * given function, and places the results in the given results collection.
     *
     * @param objects The collection of objects to process.
     * @param function The function used to process the given objects.
     * @param predicate The predicate used to determine whether or not to
     * process an object.
     * @param results [OUT] The collection into which the results are placed.
     *
     * @exception IllegalArgumentException Throw if any of the objects
     * parameter, the function parameter, or the results parameter are null.
     */
    public void processIf(Context ctx,
        final Collection objects,
        final Function function,
        final Predicate predicate,
        final Collection results);


    /**
     * Processes each of the objects in the given objects collection, using the
     * given function, and places the results in the given results collection.
     *
     * @param objects The collection of objects to process.
     * @param function The function used to process the given objects.
     * @param predicate The predicate used to determine whether or not to
     * process an object.
     * @return The collection into which the results are placed.
     *
     * @exception IllegalArgumentException Throw if any of the objects
     * parameter, the function parameter, or the results parameter are null.
     */
    public Collection processIf(Context ctx,
        final Collection objects,
        final Function function,
        final Predicate predicate);
    
    public Comparator getLongComparator();
} // class
