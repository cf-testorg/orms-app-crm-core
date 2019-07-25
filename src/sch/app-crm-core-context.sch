;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; CONTEXT ALIAS INSTALLATION
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Create "aliases" for all CRM apps.  This will make journal loads referencing application specific contexts identical.
(let ((ctx (getContext ctx "core"))) (let ((sCtx (.createSubContext ctx "app")))
    (.put ctx "app" sCtx)
    (.put ctx "Application" sCtx)
    (.put ctx "Application Context" sCtx)
    (.put ctx "AppCrmCore" sCtx)
    (.put ctx "AppCrm" sCtx)
    (.put ctx "AppCrmInvoice" sCtx)
    (.put ctx "AppCrmMediation" sCtx)
    (.put ctx "AppCrmProvision" sCtx)
 sCtx))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; PRE INSTALL
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Define run levels for applications to use to auto-install stuff before application startup runs
(define AppPreInstall runLevel5)

(r 'addItem AppPreInstall 
    (r 'createItemByJavashell ctx
        "import com.redknee.framework.xhome.context.*;
         import com.redknee.framework.xhome.home.*;
         
         Context appCtx = (Context) ctx.get(\"app\");
         com.redknee.app.crm.support.DeploymentTypeSupportHelper.get(appCtx).initializeDeploymentType(appCtx);
         com.redknee.util.snippet.log.Logger.install(appCtx);
         new com.redknee.util.partitioning.agent.StorageInstall().execute(appCtx);
         new com.redknee.app.crm.core.agent.Install().execute(appCtx);
         " ))


;; Remove the NotifyingHome from the CacheConfigHome.
;; This will disable updates to cache configurations without a restart.
;; This should be removed when TT9120120042 is resolved with a permanent FW fix.
(r 'addItem AppPreInstall 
    (r 'createItemByJavashell ctx
        "import com.redknee.framework.xhome.context.*;
         import com.redknee.framework.xhome.home.*;

         /* Dig for the \"core\" context */
         while (ctx != null && !\"core\".equals(ctx.getName()))
         {
             ctx = (Context) ctx.get(\"..\");
         }
        
         if (ctx != null)
         {
             /* Remove NotifyingHome from the front of the cache config pipeline */
             Home home = (Home) ctx.get(CacheConfigHome.class);
             if (home instanceof NotifyingHome
                     || home instanceof CurriedHome)
             {
                 while (home instanceof NotifyingHome
                         || home instanceof CurriedHome)
                 {
                     home = ((HomeProxy)home).getDelegate(ctx);
                 }
                 ctx.put(CacheConfigHome.class, home);
             }
        
             /* Remove all other NotifyingHomes from the cache config pipeline */
             while (home instanceof HomeProxy)
             {
                 Home tempHome = ((HomeProxy)home).getDelegate(ctx);
                 while (tempHome instanceof NotifyingHome
                         || tempHome instanceof CurriedHome)
                 {
                     tempHome = ((HomeProxy)tempHome).getDelegate(ctx);
                 }
                 ((HomeProxy)home).setDelegate(tempHome);
                 home = ((HomeProxy)home).getDelegate(ctx);
             }
         }"))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; POST INSTALL
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Define run levels for applications to use to auto-install stuff after application startup runs
(define AppPostInstall runLevel9)

(r 'addItem AppPostInstall 
    (r 'createItemByJavashell ctx
        "import com.redknee.framework.xhome.context.*;
         import com.redknee.framework.xhome.home.*;
         
         Context appCtx = (Context) ctx.get(\"app\");
		 new com.redknee.app.urcs.client.agent.Install().execute(appCtx);
         new com.redknee.util.partitioning.agent.ServiceInstall().execute(appCtx);
         new com.redknee.app.crm.core.agent.TransientXMenuInstall().execute(appCtx);
         " ))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; CUSTOMIZABLE PRE/POST INSTALL
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Register special context agent capable of running startup scripts
(let ((ctx (getContext ctx "core"))) (homeCreateOrStore (.get ctx com.redknee.framework.xhome.entity.EntityInfoHome.class) (let ((b (com.redknee.framework.xhome.entity.EntityInfo.))) (setBeanProperty b "Name" "Pre-Startup Script Executor")(setBeanProperty b "ClassName" "com.redknee.app.crm.xhome.context.bean.PreStartupScriptExecutingAgent")(setBeanProperty b "Roles" (let ((c (java.util.ArrayList.)))  c)) b)))
(let ((ctx (getContext ctx "core"))) (homeCreateOrStore (.get ctx com.redknee.framework.xhome.entity.EntityInfoHome.class) (let ((b (com.redknee.framework.xhome.entity.EntityInfo.))) (setBeanProperty b "Name" "Startup Script Executor")(setBeanProperty b "ClassName" "com.redknee.app.crm.xhome.context.bean.StartupScriptExecutingAgent")(setBeanProperty b "Roles" (let ((c (java.util.ArrayList.)))  c)) b)))

;; Create special run levels that can be used for deployment specific customizations
(define CustomAppPreStart runLevel5)
(define CustomAppPostStart runLevel9)

(r 'addItem CustomAppPreStart 
    (r 'createItemByJavashell ctx
        "import com.redknee.framework.xhome.context.*;
         import com.redknee.framework.xhome.home.*;
         
         Context appCtx = (Context) ctx.get(\"app\");
         new com.redknee.app.crm.xhome.context.bean.PreStartupScriptExecutingAgent().execute(appCtx);
		" ))

(r 'addItem CustomAppPostStart 
    (r 'createItemByJavashell ctx
        "import com.redknee.framework.xhome.context.*;
         import com.redknee.framework.xhome.home.*;
         
         Context appCtx = (Context) ctx.get(\"app\");
         new com.redknee.app.crm.xhome.context.bean.StartupScriptExecutingAgent().execute(appCtx);
		" ))
		
;; Bind startup script configuration bean
(let ((ctx (getContext ctx "core"))) (let ((ctx (getContext ctx "Application"))) (bindBean ctx com.redknee.app.crm.bean.StartupScripts.class com.redknee.app.crm.bean.StartupScripts.class)))

;; Install menu for startup script GUI access
(let ((ctx (getContext ctx "core"))) (homeCreateOrStore (.get ctx com.redknee.framework.xhome.menu.XMenuHome.class) (let ((b (com.redknee.framework.xhome.menu.XMenu.))) (setBeanProperty b "Key" "AppCrmCoreStartupScripts")(setBeanProperty b "ParentKey" "Application")(setBeanProperty b "Label" "Custom Startup Scripts")(setBeanProperty b "Type" "Bean")(setBeanProperty b "MenuPredicate" (let ((b (com.redknee.framework.xhome.elang.PermissionCheck.))) (setBeanProperty b "Permission" "redknee.admin") b))(setBeanProperty b "Borders" (let ((c (java.util.ArrayList.)))  c))(setBeanProperty b "CustomizationScript" "")(setBeanProperty b "Config" (let ((b (com.redknee.framework.xhome.menu.XMenuBeanConfig.))) (setBeanProperty b "BeanClass" "com.redknee.app.crm.bean.StartupScripts")(setBeanProperty b "UpdatePredicate" (let ((b (com.redknee.framework.xhome.elang.True.instance)))  b))(setBeanProperty b "BeanWebControl" "") b)) b)))
