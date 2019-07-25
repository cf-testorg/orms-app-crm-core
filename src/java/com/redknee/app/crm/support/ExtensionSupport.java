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
package com.redknee.app.crm.support;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.redknee.app.crm.extension.AbstractExtension;
import com.redknee.app.crm.extension.Extension;
import com.redknee.app.crm.extension.ExtensionAware;
import com.redknee.app.crm.extension.ExtensionHolder;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;


/**
 * 
 *
 * @author Aaron Gourley
 * @since 
 */
public interface ExtensionSupport extends Support
{
    public static final String EXTENSION_LICENSE_KEYS_CTX_KEY = "Extension.RegisteredExtensions.Licenses";
    public static final String EXTENSION_HOME_KEYS_CTX_KEY = "Extension.RegisteredExtensions";
    public static final String EXTENSION_PARENT_BEAN_CTX_KEY = AbstractExtension.EXTENSION_PARENT_BEAN_CTX_KEY;

    public void registerExtension(Context ctx, Class extensionCls);
    
    public void registerExtension(Context ctx, Class extensionCls, Object homeKey);
    
    public void registerExtension(Context ctx, Class extensionCls, String licenseKey);
    
    public void registerExtension(Context ctx, Class extensionCls, Object homeKey, String licenseKey);

    public void unRegisterExtension(Context ctx, Class extensionCls);
    
    public void unRegisterExtension(Context ctx, Class extensionCls, Object homeKey);
    
    public void unRegisterExtension(Context ctx, Class extensionCls, String licenseKey);
    
    public void unRegisterExtension(Context ctx, Class extensionCls, Object homeKey, String licenseKey);

    public ExtensionAware getParentBean(Context ctx);
    
    public boolean isExtensionLicensed(Context context, Class extensionCls);
    
    public void setParentBean(Context ctx, Object bean);
    
    public String getExtensionName(Context ctx, Class cls);

    public String getExtensionDescription(Context ctx, Class cls);
    
    public Object getExtensionHomeKey(Context ctx, Class extension);
    
    public Collection getAllExtensionHomeKeys(Context ctx, Class preferredType);
    
    public boolean isExtensionRegistered(Context ctx, Class extensionCls);
    
    public <T extends Extension, S extends T> Set<Class<S>> getRegisteredExtensions(Context ctx, Class<T> preferredType);

    public List<ExtensionHolder> wrapExtensions(Context ctx, Collection<? extends Extension> extensions);

    public List<ExtensionHolder> wrapExtensions(Context ctx, Extension... extensions);

    public Collection<Extension> unwrapExtensions(Collection<ExtensionHolder> holders);

    public Collection<Extension> unwrapExtensions(ExtensionHolder... holders);

    /**
     * Get the appropriate Home from the Context for the given extension.
     * 
     * @param ctx The operating context
     * @param extension Extension for which we need a Home
     * @return The Extension's Home
     */
    public Home getExtensionHome(Context ctx, Extension extension);

    /**
     * Get the appropriate Home from the Context for the given extension type.
     * 
     * @param ctx The operating context
     * @param extensionClass Type of Extension for which we need a Home
     * @return The Extension's Home
     */
    public Home getExtensionHome(Context ctx, Class extensionClass);

    /**
     * Get a collection of all Homes for all registerd extensions.
     * 
     * @param ctx The operating context
     * @return All Extension Homes
     */
    public Collection<Home> getExtensionHomes(Context ctx, Class preferredType);
    
    /**
     * Get a map of the existing extension IDs for each of an account's extensions.
     * 
     * @param ctx The operating context
     * @param ban The BAN of the account for which to query the current list of extensions
     * @return Extension class to list of existing extensions for the given BAN 
     */
    public <T extends Extension, S extends T> Map<Class<S>, List<S>> getExistingExtensionMap(Context ctx, Class<T> preferredType, Object predicate);

    public <T extends Extension, S extends T> List<T> getAllExtensions(Context ctx, Class<T> preferredType, Object predicate, Class... preferredInterfaces);
    
    public <T extends Extension> List<T> getExtensions(final Context ctx, Class<T> extensionType, Object predicate);
    
    public <T extends Extension> T getExtension(Context ctx, ExtensionAware extensionAware, Class<T> beanType);

}
