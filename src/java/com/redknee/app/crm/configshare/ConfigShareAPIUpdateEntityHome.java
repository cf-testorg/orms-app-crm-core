package com.redknee.app.crm.configshare;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.redknee.framework.xhome.beans.Identifiable;
import com.redknee.framework.xhome.beans.XBeans;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.HomeOperationEnum;
import com.redknee.framework.xhome.home.HomeProxy;
import com.redknee.framework.xhome.support.IdentitySupport;


/**
 * 
 * 
 */
public class ConfigShareAPIUpdateEntityHome extends HomeProxy
{

    private static final long serialVersionUID = 514145204568893797L;
    public static final String CONFIG_SHARE_API_UPDATE_ENTITY = "CONFIG_SHARE_API_UPDATE_ENTITY";

    public ConfigShareAPIUpdateEntityHome(final Context ctx, final Home delegate)
    {
        super(ctx, delegate);
    }


    @Override
    public Object create(final Context context, final Object object) throws HomeException
    {
        final Class<? extends Object> clazz = object.getClass();
        ConfigShareAPIUpdateEntityInfo info = new ConfigShareAPIUpdateEntityInfo(clazz, HomeOperationEnum.CREATE);
        Context subCtx = context.createSubContext();
        subCtx.put(CONFIG_SHARE_API_UPDATE_ENTITY, info);
        Object result = super.create(subCtx, object);
        return result;
    }


    @Override
    public Object store(final Context context, final Object object) throws HomeException
    {
        final Class<? extends Object> clazz = object.getClass();
        final String ID = getIdentifier(context,object, clazz);
        ConfigShareAPIUpdateEntityInfo info = new ConfigShareAPIUpdateEntityInfo(clazz, HomeOperationEnum.STORE, ID);
        Context subCtx = context.createSubContext();
        subCtx.put(CONFIG_SHARE_API_UPDATE_ENTITY, info);
        Object result = super.store(subCtx, object);
        return result;
    }


    @Override
    public void remove(final Context context, final Object object) throws HomeException
    {
        final Class<? extends Object> clazz = object.getClass();
        final String ID = getIdentifier(context, object, clazz);
        ConfigShareAPIUpdateEntityInfo info = new ConfigShareAPIUpdateEntityInfo(clazz,
                HomeOperationEnum.REMOVE, ID);
        Context subCtx = context.createSubContext();
        subCtx.put(CONFIG_SHARE_API_UPDATE_ENTITY, info);
        super.remove(subCtx, object);
    }


    private String getIdentifier(final Context ctx, final Object bean, final Class beanClass)
    {
        String ID = null;
        if (bean instanceof Identifiable)
        {
            final Object beanId = ((Identifiable)bean).ID();
            
            final IdentitySupport idSupport = (IdentitySupport) XBeans.getInstanceOf(ctx, beanClass, IdentitySupport.class);
            if (idSupport != null)
            {
                ID = idSupport.toStringID(beanId);
            }
            else
            {
                ID = String.valueOf(beanId);
            }

        }
        return ID;
    }
    
    
 
    
    


}
