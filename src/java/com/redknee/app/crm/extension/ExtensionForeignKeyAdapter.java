/*
 * This code is a protected work and subject to domestic and international
 * copyright law(s). A complete listing of authors of this work is readily 
 * available. Additionally, source code is, by its very nature, confidential 
 * information and inextricably contains trade secrets and other information 
 * proprietary, valuable and sensitive to Redknee, no unauthorised use, 
 * disclosure, manipulation or otherwise is permitted, and may only be used 
 * in accordance with the terms of the licence agreement entered into with 
 * Redknee Inc. and/or its subsidiaries.
 *
 * Copyright (c) Redknee Inc. and its subsidiaries. All Rights Reserved.
 */
package com.redknee.app.crm.extension;

import java.util.Collection;

import com.redknee.framework.xhome.beans.xi.PropertyInfo;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Adapter;
import com.redknee.framework.xhome.home.HomeException;



/**
 * This adapter copies the original bean's key into the extension's foreign key field.
 * 
 * It only works when the parent bean has a "simple" key (1 property).  Otherwise, it
 * does nothing.
 *
 * @author Aaron Gourley
 * @since 8.2
 */
public class ExtensionForeignKeyAdapter implements Adapter
{
    protected final PropertyInfo extKeyProp_;
    
    public ExtensionForeignKeyAdapter(PropertyInfo extensionForeignKeyProp)
    {
        extKeyProp_ = extensionForeignKeyProp;
    }

    /**
     * @{inheritDoc}
     */
    public Object adapt(Context ctx, Object obj) throws HomeException
    {
        return obj;
    }


    /**
     * @{inheritDoc}
     */
    public Object unAdapt(Context ctx, Object obj) throws HomeException
    {
        if (obj instanceof ExtensionAware)
        {
            ExtensionAware parentBean = (ExtensionAware)obj;
            
            PropertyInfo extProp = parentBean.getExtensionHolderProperty();
            if (extProp != null)
            {
                PropertyInfo beanKeyProp = extProp.getXInfo().getID();
                if (beanKeyProp != null)
                {
                    Object key = beanKeyProp.get(obj);
                    
                    Collection<Extension> extensions = parentBean.getExtensions();
                    if (extensions != null)
                    {
                        for( Extension extension : extensions )
                        {
                            if( extension != null )
                            {
                                extKeyProp_.set(extension, key);
                            }
                        }   
                    }
                }
            }
        }
        return obj;
    }

}
