package com.redknee.app.crm.bean.core;


import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.context.ContextAware;


public class PrepaidCallingCard extends com.redknee.app.crm.bean.calldetail.PrepaidCallingCard implements ContextAware
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

     /**
    * {@inheritDoc}
    */
    public Context getContext()
    {
        return context_;
    }
    
    
    /**
    * {@inheritDoc}
    */
    public void setContext(final Context context)
    {
        context_ = context;
    }
    
    private transient Context context_;


}
