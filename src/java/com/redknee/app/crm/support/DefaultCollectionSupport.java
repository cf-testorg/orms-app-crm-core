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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import com.redknee.framework.xhome.beans.Function;
import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.True;
import com.redknee.framework.xhome.filter.Predicate;
import com.redknee.framework.xhome.home.Adapter;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.visitor.AdapterVisitor;
import com.redknee.framework.xhome.visitor.ListBuildingVisitor;
import com.redknee.framework.xhome.visitor.Visitors;


/**
 * @author gary.anderson@redknee.com
 */
public class DefaultCollectionSupport implements CollectionSupport
{
    protected static CollectionSupport instance_ = null;
    public static CollectionSupport instance()
    {
        if (instance_ == null)
        {
            instance_ = new DefaultCollectionSupport();
        }
        return instance_;
    }

    protected DefaultCollectionSupport()
    {
    }
	
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
    public boolean and(Context ctx,final Collection objects, final Predicate predicate)
    {
        if (objects == null)
        {
            throw new IllegalArgumentException("The objects parameter is null.");
        }

        if (predicate == null)
        {
            throw new IllegalArgumentException("The predicate parameter is null.");
        }

        final Iterator iObject = objects.iterator();
        while (iObject.hasNext())
        {
            final Object object = iObject.next();
            final boolean keepGoing = predicate.f(ctx,object);
            if (!keepGoing)
            {
                return false;
            }
        }

        return true;
    }


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
    public <T> T findFirst(Context ctx,final Collection<T> objects, final Predicate matcher)
    {
        if (objects == null)
        {
            throw new IllegalArgumentException("The objects parameter is null.");
        }

        if (matcher == null)
        {
            throw new IllegalArgumentException("The matcher parameter is null.");
        }

        for (T object : objects)
        {
            if (matcher.f(ctx,object))
            {
                return object;
            }
        }

        return null;
    }


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
        final Collection<T> matches)
    {
        if (objects == null)
        {
            throw new IllegalArgumentException("The objects parameter is null.");
        }

        if (matcher == null)
        {
            throw new IllegalArgumentException("The matcher parameter is null.");
        }

        if (matches == null)
        {
            throw new IllegalArgumentException("The matches parameter is null.");
        }

        for (T object : objects)
        {
            if (matcher.f(ctx,object))
            {
                matches.add(object);
            }
        }
    }


    public Object[] adaptCollectionToArray(final Context ctx, final Collection collection, final Adapter adapter) throws HomeException
    {
        return adaptCollection(ctx, collection, adapter, new Object[]{});
    }


    public Collection adaptCollection(final Context ctx, final Collection collection, final Adapter adapter) throws HomeException
    {
        return adaptCollection(ctx, collection, adapter, Object.class);
    }
    
    public <T> T[] adaptCollection(final Context ctx, final Collection collection, final Adapter adapter,
            final T[] targetArray) throws HomeException
        {
            return adaptCollection(ctx, collection, adapter, targetArray.getClass().getComponentType()).toArray(targetArray);
        }


    public <T> Collection<T> adaptCollection(final Context ctx, final Collection collection, final Adapter adapter,
        final Class<T> targetType) throws HomeException
    {
        ListBuildingVisitor results = new ListBuildingVisitor();
        
        try
        {
            Visitors.forEach(ctx, collection, new AdapterVisitor(adapter, results));
            for(Object result : results)
            {
                if (result != null
                        && !targetType.isInstance(result))
                {
                    throw new HomeException("Failed to adapt CRM data type to API data type.  Required '"
                            + targetType.getName() + "' but got '" + result.getClass().getName() + "'");
                }
            }
        }
        catch (AgentException e)
        {
            throw new HomeException(e);
        }
        
        return results;
    }


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
        final Predicate matcher)
    {
        final ArrayList<T> matches = new ArrayList<T>();
        findAll(ctx,objects, matcher, matches);
        return matches;
    }


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
    public void forEach(Context ctx,final Collection objects, final Predicate operation)
    {
        if (objects == null)
        {
            throw new IllegalArgumentException("The objects parameter is null.");
        }

        if (operation == null)
        {
            throw new IllegalArgumentException("The operation parameter is null.");
        }

        and(ctx,objects, operation);
    }


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
        final Map<T, Object> map)
    {
        if (keys == null)
        {
            throw new IllegalArgumentException("The keys parameter is null.");
        }

        if (valueMaker == null)
        {
            throw new IllegalArgumentException("The valueMaker parameter is null.");
        }

        if (map == null)
        {
            throw new IllegalArgumentException("The map parameter is null.");
        }

        forEach(ctx,keys,
            new Predicate()
            {
                public boolean f(Context _ctx,final Object key)
                {
                    map.put((T)key, valueMaker.f(_ctx,key));
                    return true;
                }
            });
    }


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
        final Function valueMaker)
    {
        final TreeMap map = new TreeMap();
        mapKeys(ctx,keys, valueMaker, map);
        return map;
    }


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
        final Map<Object, T> map)
    {
        if (values == null)
        {
            throw new IllegalArgumentException("The values parameter is null.");
        }

        if (keyMaker == null)
        {
            throw new IllegalArgumentException("The keyMaker parameter is null.");
        }

        if (map == null)
        {
            throw new IllegalArgumentException("The map parameter is null.");
        }

        forEach(ctx,values,
            new Predicate()
            {
                public boolean f(Context _ctx,final Object value)
                {
                    map.put(keyMaker.f(_ctx,value), (T) value);
                    return true;
                }
            });
    }


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
        final Function keyMaker)
    {
        final TreeMap map = new TreeMap();
        mapValues(ctx,values, keyMaker, map);
        return map;
    }


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
    public boolean or(Context ctx,final Collection objects, final Predicate predicate)
    {
        if (objects == null)
        {
            throw new IllegalArgumentException("The objects parameter is null.");
        }

        if (predicate == null)
        {
            throw new IllegalArgumentException("The predicate parameter is null.");
        }

        final Iterator iObject = objects.iterator();
        while (iObject.hasNext())
        {
            final Object object = iObject.next();
            final boolean result = predicate.f(ctx,object);
            if (result)
            {
                return true;
            }
        }

        return false;
    }


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
        final Collection results)
    {
        if (objects == null)
        {
            throw new IllegalArgumentException("The objects parameter is null.");
        }

        if (function == null)
        {
            throw new IllegalArgumentException("The function parameter is null.");
        }

        if (results == null)
        {
            throw new IllegalArgumentException("The results parameter is null.");
        }

        processIf(ctx, objects, function, True.instance(), results);
    }


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
        final Function function)
    {
        final ArrayList results = new ArrayList(objects.size());
        process(ctx, objects, function, results);
        return results;
    }


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
        final Collection results)
    {
        if (objects == null)
        {
            throw new IllegalArgumentException("The objects parameter is null.");
        }

        if (function == null)
        {
            throw new IllegalArgumentException("The function parameter is null.");
        }

        if (predicate == null)
        {
            throw new IllegalArgumentException("The predicate parameter is null.");
        }

        if (results == null)
        {
            throw new IllegalArgumentException("The results parameter is null.");
        }

        forEach(ctx,
            objects,
            new Predicate()
            {
                public boolean f(Context _ctx,final Object object)
                {
                    if (predicate.f(_ctx,object))
                    {
                        results.add(function.f(_ctx,object));
                    }

                    return true;
                }
            });
    }


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
        final Predicate predicate)
    {
        final ArrayList results = new ArrayList();
        processIf(ctx, objects, function, predicate, results);
        return results;
    }
    
    public Comparator getLongComparator()
    {
        return new Comparator()
        {
            public int compare(Object o1, Object o2)
            {
                Long val1 = (Long)o1;
                Long val2 = (Long)o2;
                
                if(val1.longValue() > val2.longValue())
                {
                    return 1;
                }
                else if(val1.longValue() < val2.longValue())
                {
                    return -1;
                }
                else
                {
                    return 0;
                }
            }
        };
    }
    
} // class
