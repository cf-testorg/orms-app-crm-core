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
package com.redknee.app.crm.core.agent;

import java.util.Arrays;
import java.util.Iterator;

import com.redknee.framework.core.platform.CoreSupport;
import com.redknee.framework.xhome.beans.XBeans;
import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.context.ContextAgent;
import com.redknee.framework.xhome.elang.False;
import com.redknee.framework.xhome.elang.Or;
import com.redknee.framework.xhome.elang.PermissionCheck;
import com.redknee.framework.xhome.elang.True;
import com.redknee.framework.xhome.filter.Predicate;
import com.redknee.framework.xhome.home.CurriedHome;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.HomeProxy;
import com.redknee.framework.xhome.home.JournalHome;
import com.redknee.framework.xhome.home.NotifyingHome;
import com.redknee.framework.xhome.home.XMLHome;
import com.redknee.framework.xhome.language.MessageMgr;
import com.redknee.framework.xhome.menu.Action;
import com.redknee.framework.xhome.menu.FieldMode;
import com.redknee.framework.xhome.menu.XMenu;
import com.redknee.framework.xhome.menu.XMenuBeanConfig;
import com.redknee.framework.xhome.menu.XMenuHome;
import com.redknee.framework.xhome.menu.XMenuMenuConfig;
import com.redknee.framework.xhome.menu.XMenuTransientHome;
import com.redknee.framework.xhome.menu.XMenuWebControllerConfig;
import com.redknee.framework.xhome.support.MapSupport;
import com.redknee.framework.xhome.visitor.AbortVisitException;
import com.redknee.framework.xhome.visitor.AdapterVisitor;
import com.redknee.framework.xhome.visitor.Visitor;
import com.redknee.framework.xhome.web.action.DeleteAction;
import com.redknee.framework.xhome.web.action.EditAction;
import com.redknee.framework.xhome.web.action.ViewAction;
import com.redknee.framework.xhome.web.search.SearchBorder;
import com.redknee.framework.xhome.web.xmenu.service.DefaultXMenuService;
import com.redknee.framework.xhome.web.xmenu.service.XMenuService;
import com.redknee.framework.xlog.log.InfoLogMsg;

import com.redknee.app.crm.configshare.BeanClassMappingHome;
import com.redknee.app.crm.configshare.BeanClassMappingXMenuAdapter;
import com.redknee.app.crm.notification.NotificationThreadPool;
import com.redknee.app.crm.notification.NotificationTypeEnum;
import com.redknee.app.crm.notification.NotificationTypeSchedule;
import com.redknee.app.crm.notification.delivery.BinaryDeliveryService;
import com.redknee.app.crm.notification.delivery.EmailDeliveryService;
import com.redknee.app.crm.notification.delivery.SmsDeliveryService;
import com.redknee.app.crm.notification.liaison.NotificationLiaison;
import com.redknee.app.crm.support.NotificationSupport;
import com.redknee.app.crm.support.NotificationSupportHelper;
import com.redknee.app.crm.xhome.home.TransientXMenuMergeHome;


/**
 * This class installs transient XMenus
 *
 * @author aaron.gourley@redknee.com
 * @since 8.6
 */
public class TransientXMenuInstall implements ContextAgent
{
    private static final TransientXMenuCreationVisitor MENU_CREATOR = new TransientXMenuCreationVisitor();
    public static final String TRANSIENT_XMENU_HOME = "CoreTransientXMenuHome";

    /**
     * {@inheritDoc}
     */
    public void execute(Context ctx) throws AgentException
    {
        Home transientXMenuHome = new XMenuTransientHome(ctx);
        XMenuService xMenuService = (XMenuService) ctx.get(XMenuService.class);
        if (xMenuService instanceof DefaultXMenuService)
        {
            transientXMenuHome = new NotifyingHome(transientXMenuHome);
        }
        ctx.put(TRANSIENT_XMENU_HOME, transientXMenuHome);
        if (!overrideXMenuHome(ctx, transientXMenuHome))
        {
            return;
        }
        
        if (transientXMenuHome instanceof NotifyingHome)
        {
            ((NotifyingHome) transientXMenuHome).addHomeChangeListener(new com.redknee.framework.xhome.web.xmenu.Install().new XMenuListener((DefaultXMenuService) xMenuService));
        }

        createConfigSharigMenus(ctx);
        createAdvancedNotificationMenus(ctx);
    }

    public void createAdvancedNotificationMenus(Context ctx)
    {
        XMenu menu = createMenuOfTypeMenu(ctx, 
                "appCrmCoreNotificationLiaison", "appCrmCoreNotification", 
                "Liaisons");
        menu.setVisible(false);
        menu.setDisplayOrder(1);
        MENU_CREATOR.create(ctx, menu);
        
        menu = createMenuOfTypeMenu(ctx, 
                "appCrmCoreNotificationGenerator", "appCrmCoreNotification", 
                "Message Generators");
        menu.setVisible(false);
        menu.setDisplayOrder(2);
        MENU_CREATOR.create(ctx, menu);
        
        createNotificationTypeMenus(ctx);

        menu = createMenuOfTypeBean(ctx, 
                "appCrmCoreNotificationEmailDelivery", "appCrmCoreNotificationDelivery", 
                "E-Mail (Advanced)", 
                EmailDeliveryService.class,
                "",
                true,
                com.redknee.framework.xhome.elang.False.instance());
        menu.setDisplayOrder(2);
        MENU_CREATOR.create(ctx, menu);

        menu = createMenuOfTypeBean(ctx, 
                "appCrmCoreNotificationSmsDelivery", "appCrmCoreNotificationDelivery", 
                "SMS (Advanced)", 
                SmsDeliveryService.class,
                "",
                true,
                com.redknee.framework.xhome.elang.False.instance());
        menu.setDisplayOrder(4);
        MENU_CREATOR.create(ctx, menu);

        menu = createMenuOfTypeBean(ctx, 
                "appCrmCoreNotificationBinaryDelivery", "appCrmCoreNotificationDelivery", 
                "Binary Data (Advanced)", 
                BinaryDeliveryService.class,
                "",
                true,
                com.redknee.framework.xhome.elang.False.instance());
        menu.setDisplayOrder(6);
        MENU_CREATOR.create(ctx, menu);

        menu = createMenuOfTypeHome(ctx, 
                "appCrmCoreNotificationTypeSchedules", "appCrmCoreNotification", 
                "Notification Schedule Assignments", 
                NotificationTypeSchedule.class,
                "",
                /* View */      True.instance(),
                /* New */       False.instance(),
                /* Copy */      False.instance(),
                /* Update */    True.instance(),
                /* Delete */    False.instance());
        menu.setDisplayOrder(8);
        MENU_CREATOR.create(ctx, menu);

        menu = createMenuOfTypeHome(ctx, 
                "appCrmCoreNotificationThreadPools", "appCrmCoreNotification", 
                "Thread Pools", 
                NotificationThreadPool.class,
                "",
                /* View */      True.instance(),
                /* New */       False.instance(),
                /* Copy */      False.instance(),
                /* Update */    True.instance(),
                /* Delete */    False.instance());
        menu.setDisplayOrder(9);
        MENU_CREATOR.create(ctx, menu);
    }

    protected XMenu createMenuOfTypeMenu(Context ctx, String key, String parentKey, String label)
    {
        XMenu menu = createBasicMenu(ctx, key, parentKey, label);

        menu.setType("Menu");
        XMenuMenuConfig menuConfig = new XMenuMenuConfig();
        menuConfig.setLabel(label.substring(0, Math.min(label.length(), XMenuMenuConfig.LABEL_WIDTH)));
        menu.setConfig(menuConfig);
        
        return menu;
    }
    
    protected XMenu createMenuOfTypeBean(
            Context ctx, 
            String key, String parentKey, String label,
            Class beanClass, String customCtxKey,
            boolean enableHelp, Predicate updatePredicate)
    {
        XMenu menu = createBasicMenu(ctx, key, parentKey, label);

        XMenuBeanConfig config = new XMenuBeanConfig();
        config.setBeanClass(beanClass == null ? null : beanClass.getName());
        config.setSpecialBeanName(customCtxKey);
        config.setHelpEnabled(enableHelp);
        config.setUpdatePredicate(updatePredicate);

        menu.setType("Bean");
        menu.setConfig(config);
        
        return menu;
    }
    
    protected XMenu createMenuOfTypeHome(
            Context ctx, 
            String key, String parentKey, String label, 
            Class beanClass, String customCtxKey,
            Predicate viewPredicate,
            Predicate newPredicate,
            Predicate copyPredicate,
            Predicate updatePredicate,
            Predicate deletePredicate,
            FieldMode... fieldModes)
    {
        XMenu menu = createBasicMenu(ctx, key, parentKey, label);

        XMenuWebControllerConfig config = new XMenuWebControllerConfig();
        config.setRequiredClass(beanClass == null ? null : beanClass.getName());
        config.setSpecialHomeName(customCtxKey);
        
        config.setCopyPredicate(copyPredicate);
        config.setDeletePredicate(deletePredicate);
        config.setNewPredicate(newPredicate);
        config.setUpdatePredicate(updatePredicate);
        config.setSavePredicate(newPredicate);
        config.setPreviewPredicate(new Or().add(newPredicate).add(copyPredicate).add(updatePredicate));
        
        if (fieldModes != null)
        {
            config.setFieldModes(MapSupport.fromList(Arrays.asList(fieldModes)));
        }
        
        Class searchBorder = XBeans.getClass(ctx, beanClass, SearchBorder.class);
        if (searchBorder != null
                && SearchBorder.class.isAssignableFrom(searchBorder))
        {
            config.setSummaryBorder(
                    "try { return com.redknee.framework.xhome.beans.XBeans.instantiate("
                    + searchBorder.getName()
                    + ".class, ctx); } catch (Throwable t) { return com.redknee.framework.xhome.webcontrol.SimpleSearchBorder.instance(); }");
        }
        
        Action viewActionHolder = new Action();
        ViewAction viewAction = new ViewAction();
        viewAction.setPredicate(viewPredicate);
        viewActionHolder.setWebAction(viewAction);

        Action editActionHolder = new Action();
        EditAction editAction = new EditAction();
        editAction.setPredicate(updatePredicate);
        editActionHolder.setWebAction(editAction);

        Action deleteActionHolder = new Action();
        DeleteAction deleteAction = new DeleteAction();
        deleteAction.setPredicate(deletePredicate);
        deleteActionHolder.setWebAction(deleteAction);
        
        config.setActions(Arrays.asList(new Action[] {viewActionHolder, editActionHolder, deleteActionHolder}));
        
        menu.setType("WebController");
        menu.setConfig(config);
        
        return menu;
    }

    protected XMenu createBasicMenu(Context ctx, String key, String parentKey, String label)
    {
        XMenu menu = null;
        try
        {
            menu = (XMenu) XBeans.instantiate(XMenu.class, ctx);
        }
        catch (Exception e)
        {
            menu = new XMenu();
        }
        
        menu.setKey(key);
        menu.setParentKey(parentKey);
        menu.setLabel(label.substring(0, Math.min(label.length(), XMenu.LABEL_WIDTH)));
        
        return menu;
    }

    public void createNotificationTypeMenus(Context ctx)
    {
        String typeLiaisonMenuKey = CoreSupport.getApplication(ctx).getName() + "TypeSpecificLiaisons";
        String typeLiaisonMenuLabel = new MessageMgr(ctx, this).get("ConfigShare.TypeSpecificLiaisonMenuLabel", "Notification Type Liaisons");

        XMenu parent = createMenuOfTypeMenu(ctx, typeLiaisonMenuKey, "appCrmCoreNotificationLiaison",  typeLiaisonMenuLabel);
        
        try
        {
            // Create parent menu
            MENU_CREATOR.create(ctx, parent);

            Iterator<NotificationTypeEnum> iter = NotificationSupportHelper.get(ctx).getSupportedNotificationTypes(ctx).iterator();
            while (iter.hasNext())
            {
                NotificationTypeEnum notificationType = iter.next();

                String ctxKey = NotificationSupport.NOTIFICATION_TYPE_LIAISON_CTX_KEY_PREFIX + notificationType;
                NotificationLiaison liaison = (NotificationLiaison) ctx.get(ctxKey);
                if (liaison != null)
                {
                    String childLabel = notificationType.getDescription(ctx);

                    String key = CoreSupport.getApplication(ctx).getName() + "Type" + notificationType.getIndex() + "Liaison";
                    XMenu menu = createMenuOfTypeBean(ctx, 
                            key, parent.getKey(), childLabel, 
                            liaison.getClass(), 
                            ctxKey,
                            true, False.instance());
                    MENU_CREATOR.create(ctx, menu);
                }
            }
        }
        catch (Exception e)
        {
            new InfoLogMsg(this, "Error creating menu(s) for type specific notification liaisons", e).log(ctx);
        }
    }

    /**
     * 
     * Create menus intended to facilitate manual auditing of config sharing success/failure operations
     * The installed menus will provide data views of all incoming shared beans configured in the system.
     * 
     * @param ctx
     * @param xmenuHome
     * @param home
     */
    protected void createConfigSharigMenus(Context ctx)
    {
        Home home = (Home)ctx.get(BeanClassMappingHome.class);
        if (home == null)
        {
            new InfoLogMsg(this, "Unable to create menus for shared beans that are being received by "
                    + CoreSupport.getApplication(ctx).getName()
                    + ".  No BeanClassMappingHome installed in context.", null).log(ctx);
            return;
        }
        
        
        String dataViewMenuKey = CoreSupport.getApplication(ctx).getName() + "ConfigShareDataView";
        String dataViewMenuLabel = new MessageMgr(ctx, this).get("ConfigShare.DataViewMenuLabel", "Audit Views");

        XMenu parent = createMenuOfTypeMenu(ctx, dataViewMenuKey, "Partitioning", dataViewMenuLabel);
        parent.setMenuPredicate(new PermissionCheck(CoreSupport.RK_ADMIN_PERMISSION));

        try
        {
            // Create parent menu for all data views
            MENU_CREATOR.create(ctx, parent);

            // Create XMenus for each of the bean/class mappings
            home.forEach(ctx, 
                    new AdapterVisitor(
                            new BeanClassMappingXMenuAdapter(dataViewMenuKey), 
                            MENU_CREATOR));
        }
        catch (HomeException e)
        {
            new InfoLogMsg(this, "Error creating menu(s) for shared beans that are being received by " + CoreSupport.getApplication(ctx).getName(), e).log(ctx);
        }
    }

    protected boolean overrideXMenuHome(Context ctx, Home xmenuHome)
    {
        Home originalXMenuHome = (Home) ctx.get(XMenuHome.class);
        if (originalXMenuHome instanceof HomeProxy)
        {
            HomeProxy proxy = (HomeProxy) originalXMenuHome;
            HomeProxy previousProxy = null;
            while (proxy.getDelegate(ctx) instanceof HomeProxy
                    && !(proxy.getDelegate(ctx) instanceof JournalHome || proxy.getDelegate(ctx) instanceof XMLHome))
            {
                previousProxy = proxy;
                proxy = (HomeProxy) proxy.getDelegate(ctx);
                if (proxy instanceof CurriedHome
                        && (proxy.getDelegate(ctx) instanceof JournalHome || proxy.getDelegate(ctx) instanceof XMLHome))
                {
                    Home delegate = proxy.getDelegate(ctx);
                    previousProxy.setDelegate(delegate);
                    proxy = previousProxy;
                }
            }
            
            // Merged view of persistent and transient homes
            Home delegate = proxy.getDelegate(ctx);
            delegate = new TransientXMenuMergeHome(ctx, delegate, xmenuHome);
            proxy.setDelegate(delegate);
        }
        else
        {
            Context coreCtx = ctx;
            for (; coreCtx != null && !"core".equals(coreCtx.getName()); coreCtx = (Context) coreCtx.get(".."))
            {
                // NOP, use for loop to dig
            }

            if (coreCtx == null)
            {
                new InfoLogMsg(this, "Unable to create menus for shared beans that are being received by "
                        + CoreSupport.getApplication(ctx).getName()
                        + ".  Unable to locate core context to override XMenuHome.", null).log(ctx);
                return false;
            }
            
            coreCtx.put(XMenuHome.class, 
                            new TransientXMenuMergeHome(
                                    ctx, 
                                    originalXMenuHome, 
                                    xmenuHome));
        }

        return true;
    }

    private static final class TransientXMenuCreationVisitor implements Visitor
    {
        public void create(Context ctx, XMenu menu)
        {
            try
            {
                visit(ctx, menu);
            }
            catch (Exception e)
            {
            }
        }

        public void visit(Context ctx, Object obj) throws AgentException, AbortVisitException
        {
            if (obj instanceof XMenu)
            {
                try
                {
                    ((Home) ctx.get(TRANSIENT_XMENU_HOME)).create(ctx, obj);
                }
                catch (HomeException e)
                {
                    new InfoLogMsg(this, "Error creating transient XMenu: " + ((XMenu)obj).getKey(), e).log(ctx);
                }
            }
        }
    }
}
