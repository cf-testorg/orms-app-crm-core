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
import com.redknee.framework.xhome.beans.xi.PropertyInfo;
import com.redknee.framework.xhome.context.Context;


/**
 * A support class that constructs and provides access to "bean loader maps"
 * that can be used to dig through Contexts and Homes using foriegn key relationships
 * to intelligently find stuff that we are looking for.
 * 
 * TODO: Make the predefined maps GUI configurable instead of static and enhance to support:
 * 
 * - Loading beans contained within Collections/Maps within beans
 *   - i.e. to get a PoolExtension from within an Account
 *   
 * - Loading beans with multi-part keys (?)
 *
 * @author aaron.gourley@redknee.com
 * @since 
 */
public interface BeanLoaderSupport extends Support
{
    public void setBeanLoaderMap(Context ctx, Map<Class, Collection<PropertyInfo>> map);

    public Map<Class, Collection<PropertyInfo>> getBeanLoaderMap(Context ctx);

    public boolean hasBeanLoaderMap(Context ctx);
    
    
    /**
     * This method will do its best to populate the context with desired values.
     * 
     * It will use the following approaches:
     *  1. Check to see if the dependent context key is already available.
     *  2. Use the class->property map to retrieve a bean if the calculator needs a bean class context key.
     *  3. Recursively "dig" to get a specific value and put it in the context if found.
     *  
     * @param ctx Context to load missing beans into
     * @param desiredContextKeys Collection of Class or PropertyInfo objects that are being used as context keys.
     * @param beanLoaderMap Map used to recursively retrieve the desired beans.
     * @return
     */
    public void prepareContext(
            Context ctx, 
            Collection<Object> desiredContextKeys, 
            Map<Class, Collection<PropertyInfo>> beanLoaderMap);
    
    public void prepareContext(
            Context ctx, 
            Collection<Object> desiredContextKeys);
    
    public void prepareContext(
            Context ctx, 
            Object... desiredContextKeys);

    public <T extends AbstractBean> T getBean(Context ctx, Class<T> ctxKey);

    public <T extends AbstractBean> Map<Class, Collection<PropertyInfo>> getBeanLoaderMap(Context ctx, Class<T> baseType);

    /**
     * Method to merge multiple bean loader maps together into one comprehensive map.
     * 
     * @param dest Bean loader map to merge into.
     * @param source One or more maps to copy values from.
     */
    public void mergeBeanLoaderMaps(Map<Class, Collection<PropertyInfo>> dest, Map<Class, Collection<PropertyInfo>>... source);
}
