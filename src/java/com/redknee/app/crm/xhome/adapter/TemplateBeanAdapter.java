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
package com.redknee.app.crm.xhome.adapter;

import com.redknee.framework.xhome.beans.AbstractBean;
import com.redknee.framework.xhome.beans.XCloneable;
import com.redknee.framework.xhome.context.ConstantContextFactory;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xlog.log.MinorLogMsg;


/**
 * This class adapts a bean class to/from another bean class.  The target bean class is
 * instantiated by cloning a template instance.
 * 
 * Example of use:
 *   
 *    If one bean (X) contains a subset of another bean (Y)'s properties, then X can be
 *    used to initialize the values of the properties that it shares with Y.  The rest
 *    of the properties of Y can come from the template instance.
 *
 * @author aaron.gourley@redknee.com
 * @since 8.6
 */
public class TemplateBeanAdapter<UNADAPT_TYPE extends AbstractBean, ADAPT_TYPE extends AbstractBean> extends FactoryBeanAdapter<UNADAPT_TYPE, ADAPT_TYPE>
{
    private static final long serialVersionUID = 1L;
    
    public TemplateBeanAdapter(Class<UNADAPT_TYPE> unAdaptClass, Class<ADAPT_TYPE> adaptClass, UNADAPT_TYPE unAdaptTemplate, ADAPT_TYPE adaptTemplate)
    {
        super(unAdaptClass, adaptClass, new TemplateContextFactory(unAdaptTemplate), new TemplateContextFactory(adaptTemplate));
    }
    
    private static final class TemplateContextFactory extends ConstantContextFactory
    {
        private TemplateContextFactory(Object value)
        {
            super(value);
        }

        @Override
        public Object create(Context ctx)
        {
            Object result = super.create(ctx);
            if (result instanceof XCloneable)
            {
                try
                {
                    result = ((XCloneable)result).clone();
                }
                catch (CloneNotSupportedException e)
                {
                    new MinorLogMsg(this, "Error cloning " + result.getClass().getSimpleName() + ".  Unable to adapt bean from template.", e).log(ctx);
                    result = null;
                }
            }
            return result;
        }
    }
}
