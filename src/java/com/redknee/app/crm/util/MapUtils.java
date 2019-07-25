package com.redknee.app.crm.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.filter.Predicate;
import com.redknee.framework.xhome.visitor.AbortVisitException;

/**
 * 
 * @author sbanerjee
 *
 */
public class MapUtils
{
    /**
     * 
     * @param <INDEX>
     * @param <OBJ>
     * @param hierarchicalMap
     * @return
     */
    public static <INDEX, OBJ> Map<Index2D<INDEX>, OBJ> as2DMap(Map<INDEX, Map<INDEX, OBJ>> hierarchicalMap) 
    {
        Map<Index2D<INDEX>, OBJ> ret = new HashMap<Index2D<INDEX>, OBJ>();
        
        for(INDEX outer : hierarchicalMap.keySet())
            for(Map.Entry<INDEX,OBJ> inner : hierarchicalMap.get(outer).entrySet())
                ret.put(new Index2D<INDEX>(outer, inner.getKey()), inner.getValue());
        
        return ret;
    }
    
    /**
     * 
     * @param <INDEX>
     * @param <OBJ>
     * @param twoDMap
     * @return
     */
    public static <INDEX, OBJ> Map<INDEX, Map<INDEX, OBJ>> asNestedMap(Map<Index2D<INDEX>, OBJ> twoDMap)
    {
        Map<INDEX, Map<INDEX, OBJ>> ret = new HashMap<INDEX, Map<INDEX,OBJ>>();
        
        for(Entry<Index2D<INDEX>, OBJ> entry : twoDMap.entrySet())
        {
            INDEX i1 = entry.getKey().getPrimaryIndex();
            INDEX i2 = entry.getKey().getSecondaryIndex();
            OBJ val = entry.getValue();
            
            Map<INDEX,OBJ> inner = ret.get(i1);
            if(inner == null)
            {
                inner = new HashMap<INDEX, OBJ>();
                ret.put(i1, inner);
            }
            
            inner.put(i2, val);
        }
        
        return ret;
    }
    
    /**
     * 
     * @param <K>
     * @param <V>
     * @param ctx
     * @param dest
     * @param source
     * @param p
     */
    public static <K, V> void filter(Context ctx, Map<K, V> dest, Map<K, V> source, TypedPredicate<Map.Entry<K, V>> p)
    {
        if(source==null || source.size()<1)
        {
            dest.clear();
            return;
        }
        
        dest.clear();
        for(Map.Entry<K, V> sourceEntry : source.entrySet())
        {
            try
            {
                if(p.f(ctx, sourceEntry))
                    dest.put(sourceEntry.getKey(), sourceEntry.getValue());
            } 
            catch (AbortVisitException e)
            {
                break;
            }
        }
    }
    
    /**
     * So we could use framework's Predicate structures.
     * @param <K>
     * @param <V>
     * @param ctx
     * @param dest
     * @param source
     * @param p
     */
    public static <K, V> void filter(Context ctx, Map<K, V> dest, Map<K, V> source, final Predicate p)
    {
        MapUtils.filter(ctx, dest, source, new TypedPredicate<Map.Entry<K, V>>()
                        {
                            @Override
                            public boolean f(Context ctx, Entry<K, V> obj)
                                    throws AbortVisitException
                            {
                                return p.f(ctx, obj);
                            }
                        }
                );
    }
}