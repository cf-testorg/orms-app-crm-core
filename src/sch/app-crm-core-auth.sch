(let ((ctx (getContext ctx "core"))) (bindBean ctx com.redknee.framework.auth.AuthConfig.class com.redknee.framework.auth.AuthConfig.class))
(let ((ctx (getContext ctx "core"))) (let ((b (.get ctx com.redknee.framework.auth.AuthConfig.class))) (setBeanProperty b "accountExpiry" #t) b))
(let ((ctx (getContext ctx "core"))) (let ((b (.get ctx com.redknee.framework.auth.AuthConfig.class))) (setBeanProperty b "multiLingualSupport" #t) b))
(let ((ctx (getContext ctx "core"))) (let ((b (.get ctx com.redknee.framework.auth.AuthConfig.class))) (setBeanProperty b "MSPEnabled" #t) b))
(let ((ctx (getContext ctx "core"))) (let ((b (.get ctx com.redknee.framework.auth.AuthConfig.class))) (setBeanProperty b "showCompany" #f) b))
(let ((ctx (getContext ctx "core"))) (let ((b (.get ctx com.redknee.framework.auth.AuthConfig.class))) (setBeanProperty b "showDepartment" #f) b))
(let ((ctx (getContext ctx "core"))) (let ((b (.get ctx com.redknee.framework.auth.AuthConfig.class))) (setBeanProperty b "showPhone" #f) b))
(let ((ctx (getContext ctx "core"))) (let ((b (.get ctx com.redknee.framework.auth.AuthConfig.class))) (setBeanProperty b "showMobile" #f) b))
(let ((ctx (getContext ctx "core"))) (let ((b (.get ctx com.redknee.framework.auth.AuthConfig.class))) (setBeanProperty b "showNote" #f) b))
(let ((ctx (getContext ctx "core"))) (let ((b (.get ctx com.redknee.framework.auth.AuthConfig.class))) (setBeanProperty b "clusteringEnabled" #t) b))


;; allow rkadm loggin
(let ((b (.get ctx com.redknee.framework.auth.AuthConfig.class))) (setBeanProperty b "rkadmEnabled" #t) (setBeanProperty b "challengeRoot" #f) b)

;; set the spid to 1 for rkadm
(let ((ctx (getContext ctx "core"))) (homeCreateOrStore (.get ctx com.redknee.framework.xhome.auth.bean.UserHome.class) (let ((b (com.redknee.framework.xhome.auth.bean.User.))) (setBeanProperty b "LastModified" (DateUtil.parse "2007.12.21 17:31:00:000 -0500"))(setBeanProperty b "PasswordLastModified" (DateUtil.parse "2007.12.21 17:31:00:000 -0500"))(setBeanProperty b "Id" "rkadm")(setBeanProperty b "Password" "15a5d575d6")(setBeanProperty b "Group" "Redknee Admin")(setBeanProperty b "Spid" 1)(setBeanProperty b "Extension" #null)(setBeanProperty b "StartDate" (DateUtil.parse "2006.12.02 00:00:00:000 -0500"))(setBeanProperty b "EndDate" (DateUtil.parse "")) b)))

;; Allow root enabled access
(let ((b (.get ctx com.redknee.framework.auth.AuthConfig.class))) (setBeanProperty b "rootEnabled" #t) b)

;; Update the Password Expiry to 99999 days so that the rkadm user doesn't get locked out
(let ((b (.get ctx com.redknee.framework.auth.AuthConfig.class))) (setBeanProperty b "passwordExpiry" 99999) b)
