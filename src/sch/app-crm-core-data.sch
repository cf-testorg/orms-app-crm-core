;;binding spid lang home(s)
(let ((ctx (getContext ctx "core"))) (bindHome ctx com.redknee.app.crm.bean.SpidLangHome.class com.redknee.app.crm.bean.SpidLang.class))
(let ((ctx (getContext ctx "core"))) (homeCreateOrStore (.get ctx com.redknee.app.crm.bean.SpidLangHome.class) (let ((b (com.redknee.app.crm.bean.SpidLang.))) (setBeanProperty b "Id" 1)(setBeanProperty b "Spid" 1)(setBeanProperty b "Language" "en") b)))

;;install a dummy home for CRMSpidLang that redirects to SpidLang for backwards compatibility
(let ((ctx (getContext ctx "core"))) (.put ctx com.redknee.app.crm.bean.CRMSpidLangHome.class (let ((b (com.redknee.app.crm.xhome.home.ContextRedirectingHome. ctx com.redknee.app.crm.bean.SpidLangHome.class))) b)))

;;binding taxauthority home
(let ((ctx (getContext ctx "core"))) (bindHome ctx com.redknee.app.crm.bean.TaxAuthorityHome.class com.redknee.app.crm.bean.TaxAuthority.class))
;;default Tax Authority
(let ((ctx (getContext ctx "core"))) (homeCreateOrStore (.get ctx com.redknee.app.crm.bean.TaxAuthorityHome.class) (let ((b (com.redknee.app.crm.bean.TaxAuthority.))) (setBeanProperty b "TaxId" 1)(setBeanProperty b "Spid" 1)(setBeanProperty b "TaxAuthName" "Tax Auth Name") b)))

;;bindign dealer code home
(let ((ctx (getContext ctx "core"))) (bindHome ctx com.redknee.app.crm.bean.DealerCodeHome.class com.redknee.app.crm.bean.DealerCode.class))
;; default dealer code
(let ((ctx (getContext ctx "core"))) (homeCreateOrStore (.get ctx com.redknee.app.crm.bean.DealerCodeHome.class) (let ((b (com.redknee.app.crm.bean.DealerCode.))) (setBeanProperty b "Code" "Dealer Code")(setBeanProperty b "Spid" 1)(setBeanProperty b "Desc" "Dealer Code Description") b)))

;;binding billcycle home
(let ((ctx (getContext ctx "core"))) (bindHome ctx com.redknee.app.crm.bean.BillCycleHome.class com.redknee.app.crm.bean.BillCycle.class))
;;default billcycle
(let ((ctx (getContext ctx "core"))) (homeCreateOrStore (.get ctx com.redknee.app.crm.bean.BillCycleHome.class) (let ((b (com.redknee.app.crm.bean.BillCycle.))) (setBeanProperty b "BillCycleID" 1)(setBeanProperty b "Spid" 1)(setBeanProperty b "Description" "Default Bill Cycle ID  ") b)))

;; insert predefined DiscountClasses
(let ((ctx (getContext ctx "core"))) (let ((ctx (getContext ctx "Application"))) (bindHome ctx com.redknee.app.crm.bean.DiscountClassHome.class com.redknee.app.crm.bean.DiscountClass.class)))
(let ((ctx (getContext ctx "core"))) (let ((ctx (getContext ctx "Application"))) (homeCreateOrStore (.get ctx com.redknee.app.crm.bean.DiscountClassHome.class) (let ((b (com.redknee.app.crm.bean.DiscountClass.))) (setBeanProperty b "Name" "Default Discount Class") b))))

;; Subscription Level
(let ((ctx (getContext ctx "core"))) (let ((ctx (getContext ctx "Application"))) (bindHome ctx com.redknee.app.crm.bean.priceplan.SubscriptionLevelHome.class com.redknee.app.crm.bean.priceplan.SubscriptionLevel.class)))

;; Email Template
(let ((ctx (getContext ctx "core"))) (let ((ctx (getContext ctx "Application"))) (bindHome ctx com.redknee.app.crm.delivery.email.CRMEmailTemplateHome.class com.redknee.app.crm.delivery.email.CRMEmailTemplate.class)))

;; Credit Category
(let ((ctx (getContext ctx "core"))) (let ((ctx (getContext ctx "Application"))) (bindHome ctx com.redknee.app.crm.bean.CreditCategoryHome.class com.redknee.app.crm.bean.CreditCategory.class)))
(let ((ctx (getContext ctx "core"))) (let ((ctx (getContext ctx "Application"))) (homeCreateOrStore (.get ctx com.redknee.app.crm.bean.CreditCategoryHome.class) (let ((b (com.redknee.app.crm.bean.CreditCategory.))) (.setCode b 0)(.setDesc b "VIP")(.setSpid b 1) b))))
(let ((ctx (getContext ctx "core"))) (let ((ctx (getContext ctx "Application"))) (homeCreateOrStore (.get ctx com.redknee.app.crm.bean.CreditCategoryHome.class) (let ((b (com.redknee.app.crm.bean.CreditCategory.))) (.setCode b 1)(.setDesc b "Staff")(.setSpid b 1) b))))
(let ((ctx (getContext ctx "core"))) (let ((ctx (getContext ctx "Application"))) (homeCreateOrStore (.get ctx com.redknee.app.crm.bean.CreditCategoryHome.class) (let ((b (com.redknee.app.crm.bean.CreditCategory.))) (.setCode b 2)(.setDesc b "Individual")(.setSpid b 1) b))))
(let ((ctx (getContext ctx "core"))) (let ((ctx (getContext ctx "Application"))) (homeCreateOrStore (.get ctx com.redknee.app.crm.bean.CreditCategoryHome.class) (let ((b (com.redknee.app.crm.bean.CreditCategory.))) (.setCode b 4)(.setDesc b "Government")(.setSpid b 1) b))))
(let ((ctx (getContext ctx "core"))) (let ((ctx (getContext ctx "Application"))) (homeCreateOrStore (.get ctx com.redknee.app.crm.bean.CreditCategoryHome.class) (let ((b (com.redknee.app.crm.bean.CreditCategory.))) (.setCode b 8)(.setDesc b "Major Account")(.setSpid b 1) b))))
(let ((ctx (getContext ctx "core"))) (let ((ctx (getContext ctx "Application"))) (homeCreateOrStore (.get ctx com.redknee.app.crm.bean.CreditCategoryHome.class) (let ((b (com.redknee.app.crm.bean.CreditCategory.))) (.setCode b 11)(.setDesc b "Debt Collection")(.setSpid b 1) b))))
(let ((ctx (getContext ctx "core"))) (let ((ctx (getContext ctx "Application"))) (homeCreateOrStore (.get ctx com.redknee.app.crm.bean.CreditCategoryHome.class) (let ((b (com.redknee.app.crm.bean.CreditCategory.))) (.setCode b 12)(.setDesc b "Foreign Nationals")(.setSpid b 1) b))))
(let ((ctx (getContext ctx "core"))) (let ((ctx (getContext ctx "Application"))) (homeCreateOrStore (.get ctx com.redknee.app.crm.bean.CreditCategoryHome.class) (let ((b (com.redknee.app.crm.bean.CreditCategory.))) (.setCode b 13)(.setDesc b "None")(.setSpid b 1) b))))

;; Insert predefined transaction methods.
(let ((ctx (getContext ctx "core"))) (let ((ctx (getContext ctx "Application"))) (bindHome ctx com.redknee.app.crm.bean.TransactionMethodHome.class com.redknee.app.crm.bean.TransactionMethod.class)))
(let ((ctx (getContext ctx "core"))) (let ((ctx (getContext ctx "Application"))) (homeCreateOrStore (.get ctx com.redknee.app.crm.bean.TransactionMethodHome.class) (let ((b (com.redknee.app.crm.bean.TransactionMethod.))) (.setIdentifier b 1L)(.setName b "Cash")(.setDescription b "Payment by cash.") b))))
(let ((ctx (getContext ctx "core"))) (let ((ctx (getContext ctx "Application"))) (homeCreateOrStore (.get ctx com.redknee.app.crm.bean.TransactionMethodHome.class) (let ((b (com.redknee.app.crm.bean.TransactionMethod.))) (setBeanProperty b "Identifier" 2L)(setBeanProperty b "Name" "Cheque")(setBeanProperty b "Description" "Payment by cheque.")(setBeanProperty b "BankTransitUsed" #t)(setBeanProperty b "BankAccountUsed" #t) b))))
(let ((ctx (getContext ctx "core"))) (let ((ctx (getContext ctx "Application"))) (homeCreateOrStore (.get ctx com.redknee.app.crm.bean.TransactionMethodHome.class) (let ((b (com.redknee.app.crm.bean.TransactionMethod.))) (setBeanProperty b "Identifier" 3L)(setBeanProperty b "Name" "Debit Card")(setBeanProperty b "Description" "Payment by debit card.")(setBeanProperty b "IdentifierUsed" #t) b))))
(let ((ctx (getContext ctx "core"))) (let ((ctx (getContext ctx "Application"))) (homeCreateOrStore (.get ctx com.redknee.app.crm.bean.TransactionMethodHome.class) (let ((b (com.redknee.app.crm.bean.TransactionMethod.))) (.setIdentifier b 4L)(.setName b "Telebank")(.setDescription b "Payment by Telebank.") b))))
(let ((ctx (getContext ctx "core"))) (let ((ctx (getContext ctx "Application"))) (homeCreateOrStore (.get ctx com.redknee.app.crm.bean.TransactionMethodHome.class) (let ((b (com.redknee.app.crm.bean.TransactionMethod.))) (setBeanProperty b "Identifier" 5L)(setBeanProperty b "Name" "Credit Card")(setBeanProperty b "Description" "Payment by Credit Card.")(setBeanProperty b "IdentifierUsed" #t)(setBeanProperty b "DateUsed" #t)(setBeanProperty b "NameUsed" #t) b))))
(let ((ctx (getContext ctx "core"))) (let ((ctx (getContext ctx "Application"))) (homeCreateOrStore (.get ctx com.redknee.app.crm.bean.TransactionMethodHome.class) (let ((b (com.redknee.app.crm.bean.TransactionMethod.))) (setBeanProperty b "Identifier" 6L)(setBeanProperty b "Name" "VISA")(setBeanProperty b "Description" "Payment by VISA.")(setBeanProperty b "IdentifierUsed" #t)(setBeanProperty b "DateUsed" #t)(setBeanProperty b "NameUsed" #t) b))))
(let ((ctx (getContext ctx "core"))) (let ((ctx (getContext ctx "Application"))) (homeCreateOrStore (.get ctx com.redknee.app.crm.bean.TransactionMethodHome.class) (let ((b (com.redknee.app.crm.bean.TransactionMethod.))) (setBeanProperty b "Identifier" 5L)(setBeanProperty b "Name" "Credit Card")(setBeanProperty b "Description" "Payment by Credit Card.")(setBeanProperty b "IdentifierUsed" #t)(setBeanProperty b "DateUsed" #t)(setBeanProperty b "NameUsed" #t) b))))
(let ((ctx (getContext ctx "core"))) (let ((ctx (getContext ctx "Application"))) (homeCreateOrStore (.get ctx com.redknee.app.crm.bean.TransactionMethodHome.class) (let ((b (com.redknee.app.crm.bean.TransactionMethod.))) (setBeanProperty b "Identifier" 8L)(setBeanProperty b "Name" "AMEX")(setBeanProperty b "Description" "Payment by American Express.")(setBeanProperty b "IdentifierUsed" #t)(setBeanProperty b "DateUsed" #t)(setBeanProperty b "NameUsed" #t) b))))
(let ((ctx (getContext ctx "core"))) (let ((ctx (getContext ctx "Application"))) (homeCreateOrStore (.get ctx com.redknee.app.crm.bean.TransactionMethodHome.class) (let ((b (com.redknee.app.crm.bean.TransactionMethod.))) (setBeanProperty b "Identifier" 9L)(setBeanProperty b "Name" "Invoice")(setBeanProperty b "Description" "Payment by invoice") b))))
(let ((ctx (getContext ctx "core"))) (let ((ctx (getContext ctx "Application"))) (homeCreateOrStore (.get ctx com.redknee.app.crm.bean.TransactionMethodHome.class) (let ((b (com.redknee.app.crm.bean.TransactionMethod.))) (setBeanProperty b "Identifier" 10L)(setBeanProperty b "Name" "Transfer")(setBeanProperty b "Description" "Payment by transfer") b))))

;; Account Categories
(let ((ctx (getContext ctx "core"))) (let ((ctx (getContext ctx "Application"))) (bindHome ctx com.redknee.app.crm.bean.AccountCategoryHome.class com.redknee.app.crm.bean.AccountCategory.class)))
(let ((ctx (getContext ctx "core"))) (let ((ctx (getContext ctx "Application"))) (homeCreateOrStore (.get ctx com.redknee.app.crm.bean.AccountCategoryHome.class) (let ((b (com.redknee.app.crm.bean.AccountCategory.))) (.setIdentifier b 1L)(.setSpid b 1)(.setName b "Personal")(.setDescription b "Personal") (setBeanProperty b "CustomerType" (com.redknee.app.crm.bean.CustomerTypeEnum.get 3s)) b))))
(let ((ctx (getContext ctx "core"))) (let ((ctx (getContext ctx "Application"))) (homeCreateOrStore (.get ctx com.redknee.app.crm.bean.AccountCategoryHome.class) (let ((b (com.redknee.app.crm.bean.AccountCategory.))) (.setIdentifier b 2L)(.setSpid b 1)(.setName b "Corporate")(.setDescription b "Corporate") (setBeanProperty b "CustomerType" (com.redknee.app.crm.bean.CustomerTypeEnum.get 0s)) b))))
(let ((ctx (getContext ctx "core"))) (let ((ctx (getContext ctx "Application"))) (homeCreateOrStore (.get ctx com.redknee.app.crm.bean.AccountCategoryHome.class) (let ((b (com.redknee.app.crm.bean.AccountCategory.))) (.setIdentifier b 3L)(.setSpid b 1)(.setName b "Government")(.setDescription b "Government") (setBeanProperty b "CustomerType" (com.redknee.app.crm.bean.CustomerTypeEnum.get 1s)) b))))

;; Invoice Delivery Options
(let ((ctx (getContext ctx "core"))) (let ((ctx (getContext ctx "Application"))) (bindHome ctx com.redknee.app.crm.invoice.delivery.InvoiceDeliveryOptionHome.class com.redknee.app.crm.invoice.delivery.InvoiceDeliveryOption.class)))
(let ((ctx (getContext ctx "core"))) (let ((ctx (getContext ctx "Application"))) (homeCreateOrStore (.get ctx com.redknee.app.crm.invoice.delivery.InvoiceDeliveryOptionHome.class) (let ((b (com.redknee.app.crm.invoice.delivery.InvoiceDeliveryOption.))) (setBeanProperty b "Id" 0L)(setBeanProperty b "DeliveryType" (SetSupport.fromString (com.redknee.app.crm.invoice.delivery.DeliveryTypeEnumIdentitySupport.instance) "1"))(setBeanProperty b "DisplayName" "Post")(setBeanProperty b "SubFolderName" "post") b))))
(let ((ctx (getContext ctx "core"))) (let ((ctx (getContext ctx "Application"))) (homeCreateOrStore (.get ctx com.redknee.app.crm.invoice.delivery.InvoiceDeliveryOptionHome.class) (let ((b (com.redknee.app.crm.invoice.delivery.InvoiceDeliveryOption.))) (setBeanProperty b "Id" 1L)(setBeanProperty b "DeliveryType" (SetSupport.fromString (com.redknee.app.crm.invoice.delivery.DeliveryTypeEnumIdentitySupport.instance) "0"))(setBeanProperty b "DisplayName" "E-mail")(setBeanProperty b "SubFolderName" "not-post") b))))
(let ((ctx (getContext ctx "core"))) (let ((ctx (getContext ctx "Application"))) (homeCreateOrStore (.get ctx com.redknee.app.crm.invoice.delivery.InvoiceDeliveryOptionHome.class) (let ((b (com.redknee.app.crm.invoice.delivery.InvoiceDeliveryOption.))) (setBeanProperty b "Id" 2L)(setBeanProperty b "DeliveryType" (SetSupport.fromString (com.redknee.app.crm.invoice.delivery.DeliveryTypeEnumIdentitySupport.instance) "0,1"))(setBeanProperty b "DisplayName" "Post and E-mail")(setBeanProperty b "SubFolderName" "post") b))))
(let ((ctx (getContext ctx "core"))) (let ((ctx (getContext ctx "Application"))) (homeCreateOrStore (.get ctx com.redknee.app.crm.invoice.delivery.InvoiceDeliveryOptionHome.class) (let ((b (com.redknee.app.crm.invoice.delivery.InvoiceDeliveryOption.))) (setBeanProperty b "Id" 3L)(setBeanProperty b "DeliveryType" (SetSupport.fromString (com.redknee.app.crm.invoice.delivery.DeliveryTypeEnumIdentitySupport.instance) ""))(setBeanProperty b "DisplayName" "None")(setBeanProperty b "SubFolderName" "not-post")(setBeanProperty b "NonResponsibleDefault" #t) b))))


;; cipher type 
(let ((ctx (getContext ctx "core"))) (let ((ctx (getContext ctx "Application"))) (bindHome ctx com.redknee.app.crm.bean.CipherTypeHome.class com.redknee.app.crm.bean.CipherType.class)))
(let ((ctx (getContext ctx "core"))) (let ((ctx (getContext ctx "Application"))) (homeCreateOrStore (.get ctx com.redknee.app.crm.bean.CipherTypeHome.class) (let ((b (com.redknee.app.crm.bean.CipherType.))) (setBeanProperty b "CipherName" "AES 128")(setBeanProperty b "CipherClassName" "com.redknee.app.crm.util.cipher.AESCipher") b))))
(let ((ctx (getContext ctx "core"))) (let ((ctx (getContext ctx "Application"))) (homeCreateOrStore (.get ctx com.redknee.app.crm.bean.CipherTypeHome.class) (let ((b (com.redknee.app.crm.bean.CipherType.))) (setBeanProperty b "Id" 1)(setBeanProperty b "CipherName" "Redknee Simple ")(setBeanProperty b "CipherClassName" "com.redknee.app.crm.util.cipher.RedkneeFWCipher") b))))

;; DDR Writer type
(let ((ctx (getContext ctx "core"))) (let ((ctx (getContext ctx "Application"))) (bindHome ctx com.redknee.app.crm.bean.bank.DDROutputWriterTypeHome.class com.redknee.app.crm.bean.bank.DDROutputWriterType.class)))
(let ((ctx (getContext ctx "core"))) (let ((ctx (getContext ctx "Application"))) (homeCreateOrStore (.get ctx com.redknee.app.crm.bean.bank.DDROutputWriterTypeHome.class) (let ((b (com.redknee.app.crm.bean.bank.DDROutputWriterType.))) (setBeanProperty b "Name" "Default")(setBeanProperty b "DDRWriterClassName" "com.redknee.app.crm.bas.directDebit.DoryDirectDebitOutputWriter") b))))

;; SAP Doc Header
(let ((ctx (getContext ctx "core"))) (let ((ctx (getContext ctx "Application"))) (bindHome ctx com.redknee.app.crm.bean.SAPDocHeaderHome.class com.redknee.app.crm.bean.SAPDocHeader.class)))
(let ((ctx (getContext ctx "core"))) (let ((ctx (getContext ctx "Application"))) (homeCreateOrStore (.get ctx com.redknee.app.crm.bean.SAPDocHeaderHome.class) (let ((b (com.redknee.app.crm.bean.SAPDocHeader.))) (setBeanProperty b "DocHeader" "Invoice")(setBeanProperty b "Description" "invoice") b))))
(let ((ctx (getContext ctx "core"))) (let ((ctx (getContext ctx "Application"))) (homeCreateOrStore (.get ctx com.redknee.app.crm.bean.SAPDocHeaderHome.class) (let ((b (com.redknee.app.crm.bean.SAPDocHeader.))) (setBeanProperty b "DocHeader" "CreditNT")(setBeanProperty b "Description" "credit nt") b))))
(let ((ctx (getContext ctx "core"))) (let ((ctx (getContext ctx "Application"))) (homeCreateOrStore (.get ctx com.redknee.app.crm.bean.SAPDocHeaderHome.class) (let ((b (com.redknee.app.crm.bean.SAPDocHeader.))) (setBeanProperty b "DocHeader" "TRANSFER BETWEEN ACCOUNT")(setBeanProperty b "Description" "TRANSFER BETWEEN ACCOUNT") b))))
(let ((ctx (getContext ctx "core"))) (let ((ctx (getContext ctx "Application"))) (homeCreateOrStore (.get ctx com.redknee.app.crm.bean.SAPDocHeaderHome.class) (let ((b (com.redknee.app.crm.bean.SAPDocHeader.))) (setBeanProperty b "DocHeader" "EARNED REVENUE")(setBeanProperty b "Description" "EARNED REVENUE") b))))
(let ((ctx (getContext ctx "core"))) (let ((ctx (getContext ctx "Application"))) (homeCreateOrStore (.get ctx com.redknee.app.crm.bean.SAPDocHeaderHome.class) (let ((b (com.redknee.app.crm.bean.SAPDocHeader.))) (setBeanProperty b "DocHeader" "BOOKED REVENUE POSTING")(setBeanProperty b "Description" "BOOKED REVENUE POSTING") b))))
(let ((ctx (getContext ctx "core"))) (let ((ctx (getContext ctx "Application"))) (homeCreateOrStore (.get ctx com.redknee.app.crm.bean.SAPDocHeaderHome.class) (let ((b (com.redknee.app.crm.bean.SAPDocHeader.))) (setBeanProperty b "DocHeader" "TAP-In Journal Transact")(setBeanProperty b "Description" "TAP-In Journal Transact") b))))
