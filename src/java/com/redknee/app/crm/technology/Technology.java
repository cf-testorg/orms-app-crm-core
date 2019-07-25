/*
 * Created on Nov 10, 2005
 */
package com.redknee.app.crm.technology;

import com.redknee.framework.xhome.context.Context;

/**
 * @author rattapattu
 */
public class Technology
{

    public final static String BEAN_TECHNOLOGY_CPROPERTY = "TECHNOLOGY";
    
    /**
     * @param ctx
     * @param techEnum
     */
    
    public static void setBeanTechnology(Context ctx, TechnologyEnum techEnum)
    {
        ctx.put(BEAN_TECHNOLOGY_CPROPERTY, techEnum);
    }

    public static TechnologyEnum getBeanTechnology(Context ctx)
    {
       return (TechnologyEnum) ctx.get(BEAN_TECHNOLOGY_CPROPERTY);
    }

    public static TechnologyEnum getTechnology(Context ctx)
    {
       return (TechnologyEnum) ctx.get(BEAN_TECHNOLOGY_CPROPERTY, TechnologyEnum.GSM);
    }    
}
