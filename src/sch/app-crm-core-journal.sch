;; By default we will keep 30 rollover files.  Each file will have default max size of ~1GB and will be rolled over each day.
;; This means that in the worst case we have somewhere between:
;; - 30 days of failed operations
;; - 1 day with 30 GB worth of failures in the audit journal
(let ((ctx (getContext ctx "core"))) 
  (let ((ctx (getContext ctx "app"))) 
    (bindBean 
      ctx 
      "FailedOperationsAuditJournalConfig"
      com.redknee.framework.xhome.journal.JournalConfig.class)))


(let ((ctx (getContext ctx "core"))) (let ((ctx (getContext ctx "app"))) (let ((b (.get ctx "FailedOperationsAuditJournalConfig"))) (setBeanProperty b "fileName" "FailedOperations.log") b)))
(let ((ctx (getContext ctx "core"))) (let ((ctx (getContext ctx "app"))) (let ((b (.get ctx "FailedOperationsAuditJournalConfig"))) (setBeanProperty b "path" "../audit/") b)))
(let ((ctx (getContext ctx "core"))) (let ((ctx (getContext ctx "app"))) (let ((b (.get ctx "FailedOperationsAuditJournalConfig"))) (setBeanProperty b "maxBackupIndex" 30) b)))