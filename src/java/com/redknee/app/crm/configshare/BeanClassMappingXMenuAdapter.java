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
package com.redknee.app.crm.configshare;

import java.util.Arrays;

import com.redknee.framework.core.platform.CoreSupport;
import com.redknee.framework.xhome.beans.XBeans;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.False;
import com.redknee.framework.xhome.elang.PermissionCheck;
import com.redknee.framework.xhome.home.Adapter;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.menu.Action;
import com.redknee.framework.xhome.menu.HelpTypeEnum;
import com.redknee.framework.xhome.menu.XMenu;
import com.redknee.framework.xhome.menu.XMenuBeanConfig;
import com.redknee.framework.xhome.menu.XMenuTextConfig;
import com.redknee.framework.xhome.menu.XMenuWebControllerConfig;
import com.redknee.framework.xhome.web.action.ViewAction;
import com.redknee.framework.xhome.web.search.SearchBorder;

import com.redknee.app.crm.bean.core.BeanClassMapping;
import com.redknee.app.crm.support.HomeSupportHelper;


/**
 * This adapter converts BeanClassMapping instances to XMenus
 *
 * @author aaron.gourley@redknee.com
 * @since 8.6
 */
public class BeanClassMappingXMenuAdapter implements Adapter
{
    public BeanClassMappingXMenuAdapter(String parentKey)
    {
        parentKey_ = parentKey;
    }
    
    /**
     * {@inheritDoc}
     */
    public Object adapt(Context ctx, Object obj) throws HomeException
    {
        XMenu menu = null;
        
        if (obj instanceof BeanClassMapping)
        {
            // Adapt BeanClassMapping to XMenu
            BeanClassMapping mapping = (BeanClassMapping) obj;

            Class beanClass = getLocalBeanClass(ctx, mapping);
            final String localBeanName;
            if (beanClass != null)
            {
                localBeanName = beanClass.getName();
            }
            else
            {
                localBeanName = mapping.getExternalBeanName();
            }

            try
            {
                menu = (XMenu) XBeans.instantiate(XMenu.class, ctx);
            }
            catch (Exception e)
            {
                menu = new XMenu();
            }

            String safeClassName = getSafeClassName(localBeanName);
            menu.setKey(CoreSupport.getApplication(ctx).getName() + "ConfigShare" + safeClassName);
            menu.setLabel(safeClassName);
            
            menu.setMenuPredicate(new PermissionCheck(CoreSupport.RK_ADMIN_PERMISSION));

            menu.setParentKey(parentKey_);
            menu.setHelpType(HelpTypeEnum.TEXT);
            menu.setHelp("<font size=\"2\"><b>Data view for config sharing data.</b><br/><br/>Source = <b>" + mapping.getExternalBeanName() + "</b><br/>Destination = <b>" + localBeanName + "</b></font>");
            
            // Menu Type strings come from XMenuDriverHome, but we are only using FW default ones so they are just hard-coded
            // Specifically, we'll use 'Bean' and 'WebController' for working ones, and 'Text' for menus that we couldn't generate properly.
            
            if (beanClass == null)
            {
                configureErrorMenu(ctx, menu, mapping, "Local class could not be found.");
            }
            else
            {
                Home home = null;
                try
                {
                    home = HomeSupportHelper.get(ctx).getHome(ctx, beanClass);
                }
                catch (HomeException e)
                {
                    // NOP
                }
                if (home != null)
                {
                    configureHomeMenu(ctx, menu, beanClass);
                }
                else if (ctx.has(beanClass))
                {
                    configureBeanMenu(ctx, menu, beanClass);
                }
                else
                {
                    configureErrorMenu(ctx, menu, mapping, "Local data could not be found.  No home or bean installed in context.");
                }
            }
        }

        return menu;
    }

    protected void configureBeanMenu(Context ctx, XMenu menu, Class beanClass)
    {
        XMenuBeanConfig config = new XMenuBeanConfig();
        config.setBeanClass(beanClass.getName());
        config.setHelpEnabled(true);
        config.setPreviewEnabled(false);
        config.setUpdatePredicate(False.instance());

        menu.setType("Bean");
        menu.setConfig(config);
    }

    protected void configureHomeMenu(Context ctx, XMenu menu, Class beanClass)
    {
        XMenuWebControllerConfig config = new XMenuWebControllerConfig();
        config.setRequiredClass(beanClass.getName());

        config.setCopyPredicate(False.instance());
        config.setDeletePredicate(False.instance());
        config.setNewPredicate(False.instance());
        config.setUpdatePredicate(False.instance());
        config.setSavePredicate(False.instance());
        config.setPreviewPredicate(False.instance());

        Class searchBorder = XBeans.getClass(ctx, beanClass, SearchBorder.class);
        if (searchBorder != null)
        {
            config.setSummaryBorder(
                    "try { return com.redknee.framework.xhome.beans.XBeans.instantiate("
                    + searchBorder.getName()
                    + ".class, ctx); } catch (Throwable t) { return com.redknee.framework.xhome.webcontrol.SimpleSearchBorder.instance(); }");
        }
        
        Action viewAction = new Action();
        viewAction.setWebAction(new ViewAction());
        config.setActions(Arrays.asList(new Action[] {viewAction}));
        
        menu.setType("WebController");
        menu.setConfig(config);
    }


    protected void configureErrorMenu(Context ctx, XMenu menu, BeanClassMapping mapping, String customMessage)
    {
        menu.setType("Text");
        
        XMenuTextConfig xmenuTextConfig = new XMenuTextConfig();
        xmenuTextConfig.setText("<font color=\"red\"><b>Unable to generate menu definition for " + mapping.getExternalBeanName() + "</b></font><br/><br/>"
                                    + customMessage);
        menu.setConfig(xmenuTextConfig);
    }


    protected Class getLocalBeanClass(Context ctx, BeanClassMapping mapping)
    {
        Class beanClass = null;

        String externalBeanName = mapping.getExternalBeanName();
        
        ConfigChangeRequestTranslator translator = mapping.getTranslator(ctx);
        if (translator instanceof DefaultConfigChangeRequestTranslator)
        {
            DefaultConfigChangeRequestTranslator defaultTranslator = (DefaultConfigChangeRequestTranslator) translator;
            
            ConfigChangeRequest request = null;
            try
            {
                request = (ConfigChangeRequest) XBeans.instantiate(ConfigChangeRequest.class, ctx);
            }
            catch (Exception e)
            {
                request = new ConfigChangeRequest();
            }
            
            request.setBeanClass(externalBeanName);
            
            try
            {
                beanClass = defaultTranslator.getBeanClass(ctx, request);
            }
            catch (Throwable t)
            {
                // NOP
            }
        }

        if (beanClass == null)
        {
            try
            {
                beanClass = Class.forName(externalBeanName);
            }
            catch (Throwable t)
            {
                // NOP
            }
        }
        
        return beanClass;
    }


    private String getSafeClassName(String beanClassName)
    {
        String result = null;
        
        if (beanClassName != null)
        {
            int i = beanClassName.lastIndexOf(".")+1;
            if (i > 0 && i < beanClassName.length())
            {
                result = beanClassName.substring(i);
            }
            else
            {
                result = beanClassName;
            }
        }
        
        if (result == null || result.trim().length() == 0)
        {
            result = "Null";
        }
        
        return result;
    }


    /**
     * {@inheritDoc}
     */
    public Object unAdapt(Context ctx, Object obj) throws HomeException
    {
        throw new UnsupportedOperationException("XMenu to BeanClassMapping conversion not supported.");
    }

    protected String parentKey_;
}
