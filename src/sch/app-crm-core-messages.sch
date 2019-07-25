;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Service Extension Name & Description 
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Key/Value Alcatel SSC
(let ((ctx (getContext ctx "core"))) (homeCreateOrStore (.get ctx com.redknee.framework.xhome.language.XMessageHome.class) (let ((b (com.redknee.framework.xhome.language.XMessage.))) (setBeanProperty b "Key" "com.redknee.app.crm.extension.service.core.AlcatelSSCServiceExtension.extensionName")(setBeanProperty b "Text" "Alcatel SSC") b)))
(let ((ctx (getContext ctx "core"))) (homeCreateOrStore (.get ctx com.redknee.framework.xhome.language.XMessageHome.class) (let ((b (com.redknee.framework.xhome.language.XMessage.))) (setBeanProperty b "Key" "com.redknee.app.crm.extension.service.core.AlcatelSSCServiceExtension.extensionDescription") b)))

;; BlackBerry
(let ((ctx (getContext ctx "core"))) (homeCreateOrStore (.get ctx com.redknee.framework.xhome.language.XMessageHome.class) (let ((b (com.redknee.framework.xhome.language.XMessage.))) (setBeanProperty b "Key" "com.redknee.app.crm.extension.service.BlackberryServiceExtension.extensionName")(setBeanProperty b "Text" "Blackberry Services") b)))
(let ((ctx (getContext ctx "core"))) (homeCreateOrStore (.get ctx com.redknee.framework.xhome.language.XMessageHome.class) (let ((b (com.redknee.framework.xhome.language.XMessage.))) (setBeanProperty b "Key" "com.redknee.app.crm.extension.service.BlackberryServiceExtension.extensionDescription") b)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Credit Category Extension Name & Description 
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Late Fee
(let ((ctx (getContext ctx "core"))) (homeCreateOrStore (.get ctx com.redknee.framework.xhome.language.XMessageHome.class) (let ((b (com.redknee.framework.xhome.language.XMessage.))) (setBeanProperty b "Key" "com.redknee.app.crm.extension.creditcategory.core.LateFeeCreditCategoryExtension.extensionName")(setBeanProperty b "Text" "Late Fee") b)))
(let ((ctx (getContext ctx "core"))) (homeCreateOrStore (.get ctx com.redknee.framework.xhome.language.XMessageHome.class) (let ((b (com.redknee.framework.xhome.language.XMessage.))) (setBeanProperty b "Key" "com.redknee.app.crm.extension.creditcategory.core.LateFeeCreditCategoryExtension.extensionDescription") b)))

;; Early Reward
(let ((ctx (getContext ctx "core"))) (homeCreateOrStore (.get ctx com.redknee.framework.xhome.language.XMessageHome.class) (let ((b (com.redknee.framework.xhome.language.XMessage.))) (setBeanProperty b "Key" "com.redknee.app.crm.extension.creditcategory.core.EarlyRewardCreditCategoryExtension.extensionName")(setBeanProperty b "Text" "Early Reward") b)))
(let ((ctx (getContext ctx "core"))) (homeCreateOrStore (.get ctx com.redknee.framework.xhome.language.XMessageHome.class) (let ((b (com.redknee.framework.xhome.language.XMessage.))) (setBeanProperty b "Key" "com.redknee.app.crm.extension.creditcategory.core.EarlyRewardCreditCategoryExtension.extensionDescription") b)))
