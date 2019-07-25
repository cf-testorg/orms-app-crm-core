package com.redknee.app.crm.home;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.redknee.app.crm.support.HomeSupportHelper;
import com.redknee.framework.xhome.beans.Identifiable;
import com.redknee.framework.xhome.beans.xi.PropertyInfo;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.HomeProxy;


/**
 * A HomeProxy that fails the remove request if the listed dependencies exist.
 * Dependencies from beans/home that reference the primary-key of the home being wrapped
 * will be checked
 * 
 * @author simar.singh@redknee.com
 * 
 * @param <BEAN>
 *            - Any Identifiable/Home bean
 */
public class DependencyCheckOnRemveHome<BEAN extends Identifiable> extends HomeProxy
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;


    /**
     * 
     * @param dependency
     *            - Referencing property of dependent bean
     * @param home
     *            - Hope on which dependencies are being checked for.
     * @param dependencies
     *            - Additional referencing property of dependent bean
     */
    public DependencyCheckOnRemveHome(PropertyInfo dependency, Home home, PropertyInfo... dependencies)
    {
        super(home);
        Set<PropertyInfo> dependencySet = new HashSet<PropertyInfo>(Arrays.asList(dependencies));
        dependencySet.add(dependency);
        dependencies_ = Collections.unmodifiableSet(dependencySet);
    }


    @SuppressWarnings("unchecked")
    public void remove(Context ctx, Object obj) throws HomeException
    {
        if (obj instanceof Identifiable)
        {
            failIfHasDependencies(ctx, (BEAN) obj);
        }
        getDelegate(ctx).remove(obj);
    }


    /**
     * 
     * @param ctx
     * @param bean
     * @throws HomeException
     *             - if the bean has dependencies
     */
    @SuppressWarnings("unchecked")
    private void failIfHasDependencies(Context ctx, BEAN bean) throws HomeException
    {
        for (PropertyInfo dependentProperty : dependencies_)
        {
            if (null != HomeSupportHelper.get(ctx).findBean(ctx, dependentProperty.getBeanClass(),
                    new EQ(dependentProperty, bean.ID())))
            {
                throw new HomeException("Remove request failed because of dependencies from ["
                        + dependentProperty.getBeanClass().getSimpleName() + " (" + dependentProperty.getLabel(ctx)
                        + ") ]");
            }
        }
    }

    private final Set<PropertyInfo> dependencies_;
}