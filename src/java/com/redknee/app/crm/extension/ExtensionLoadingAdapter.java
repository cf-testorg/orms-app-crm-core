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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.redknee.framework.xhome.beans.Identifiable;
import com.redknee.framework.xhome.beans.xi.PropertyInfo;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.home.Adapter;
import com.redknee.framework.xhome.home.HomeException;

import com.redknee.app.crm.support.ExtensionSupportHelper;


/**
 * This adapter populates the transient extension field in the parent bean.
 *
 * @author Aaron Gourley
 * @since 8.2
 */
public class ExtensionLoadingAdapter<T extends Extension> implements Adapter
{
    protected final PropertyInfo extKeyProp_;
    protected final Class<T> extType_;
    protected final Class[] preferedInterfaces_;
    
    public ExtensionLoadingAdapter(Class<T> preferredType, PropertyInfo extensionForeignKeyProp)
    {
        this(preferredType, extensionForeignKeyProp, null);
    }
    
    public ExtensionLoadingAdapter(Class<T> preferredType, PropertyInfo extensionForeignKeyProp, Class[] interfaces)
    {
        extKeyProp_ = extensionForeignKeyProp;
        extType_ = preferredType;
        preferedInterfaces_ = interfaces;
    }

    /**
     * @{inheritDoc}
     */
    public Object adapt(Context ctx, Object obj) throws HomeException
    {
        if (obj instanceof ExtensionAware)
        {
            ExtensionAware parentBean = (ExtensionAware) obj;
            
            Collection<Extension> extensions = parentBean.getExtensions();
            List<T> dbExtensions = null;
            if (obj instanceof Identifiable)
            {
                dbExtensions = ExtensionSupportHelper.get(ctx).getAllExtensions(
                        ctx, 
                        extType_, 
                        new EQ(extKeyProp_, ((Identifiable)obj).ID()), preferedInterfaces_);
            }

            if( extensions != null && extensions.size() > 0
                    || (dbExtensions != null && dbExtensions.size() > 0) )
            {
                Map<String, Extension> extensionMap = new HashMap<String, Extension>();
                if (extensions != null)
                {
                    for( Extension extension : extensions )
                    {
                        if (extension != null)
                        {
                            extensionMap.put(extension.getClass().getSimpleName()+":"+extension.ID().toString(), extension);
                        }
                    }   
                }
                if (dbExtensions != null)
                {
                    for( T dbExtension : dbExtensions )
                    {
                        extensionMap.put(dbExtension.getClass().getSimpleName()+":"+dbExtension.ID().toString(), dbExtension);
                    }
                }
                List<Extension> newExtensions = new ArrayList<Extension>();
                newExtensions.addAll(extensionMap.values());
                parentBean.getExtensionHolderProperty().set(obj, ExtensionSupportHelper.get(ctx).wrapExtensions(ctx, newExtensions));
            }
            return obj;
        }
        if( obj instanceof Collection )
        {
            Collection newObj = new ArrayList();
            for( Object bean : (Collection)obj )
            {
                newObj.add(adapt(ctx, bean));
            }
            return newObj;
        }
        return obj;
    }

    /**
     * @{inheritDoc}
     */
    public Object unAdapt(Context ctx, Object obj) throws HomeException
    {
        return obj;
    }
}
