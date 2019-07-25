;; Bind key configuration home
(let ((ctx (getContext ctx "core"))) (let ((ctx (getContext ctx "AppCrmCore"))) (bindHome ctx com.redknee.app.crm.bean.KeyConfigurationHome.class com.redknee.app.crm.bean.KeyConfiguration.class)))

;; Global Key Configurations (available for use by all features)
(let ((ctx (getContext ctx "core"))) (let ((ctx (getContext ctx "AppCrm"))) (homeCreateOrStore (.get ctx com.redknee.app.crm.bean.KeyConfigurationHome.class) (let ((b (com.redknee.app.crm.bean.KeyConfiguration.))) (setBeanProperty b "Feature" (com.redknee.app.crm.bean.KeyValueFeatureEnum.get 0s))(setBeanProperty b "Key" "$SERVICE_NAME$")(setBeanProperty b "ValueCalculator" (let ((b (com.redknee.app.crm.calculator.ServiceFee2ValueCalculator.))) (setBeanProperty b "Property" com.redknee.app.crm.bean.ServiceFee2XInfo.SERVICE_NAME$) b)) b))))
(let ((ctx (getContext ctx "core"))) (let ((ctx (getContext ctx "AppCrm"))) (homeCreateOrStore (.get ctx com.redknee.app.crm.bean.KeyConfigurationHome.class) (let ((b (com.redknee.app.crm.bean.KeyConfiguration.))) (setBeanProperty b "Feature" (com.redknee.app.crm.bean.KeyValueFeatureEnum.get 0s))(setBeanProperty b "Key" "$SERVICE_ID$")(setBeanProperty b "ValueCalculator" (let ((b (com.redknee.app.crm.calculator.ServiceValueCalculator.))) (setBeanProperty b "Property" com.redknee.app.crm.bean.ServiceXInfo.ID$) b)) b))))
(let ((ctx (getContext ctx "core"))) (let ((ctx (getContext ctx "AppCrm"))) (homeCreateOrStore (.get ctx com.redknee.app.crm.bean.KeyConfigurationHome.class) (let ((b (com.redknee.app.crm.bean.KeyConfiguration.))) (setBeanProperty b "Feature" (com.redknee.app.crm.bean.KeyValueFeatureEnum.get 0s))(setBeanProperty b "Key" "$SERVICE_PERIOD$")(setBeanProperty b "ValueCalculator" (let ((b (com.redknee.app.crm.calculator.LanguageFormattingValueCalculator.))) (setBeanProperty b "Delegate" (let ((b (com.redknee.app.crm.calculator.ServiceFee2ValueCalculator.))) (setBeanProperty b "Property" com.redknee.app.crm.bean.ServiceFee2XInfo.SERVICE_PERIOD$) b)) b)) b))))
(let ((ctx (getContext ctx "core"))) (let ((ctx (getContext ctx "AppCrm"))) (homeCreateOrStore (.get ctx com.redknee.app.crm.bean.KeyConfigurationHome.class) (let ((b (com.redknee.app.crm.bean.KeyConfiguration.))) (setBeanProperty b "Feature" (com.redknee.app.crm.bean.KeyValueFeatureEnum.get 0s))(setBeanProperty b "Key" "$SERVICE_FEE$")(setBeanProperty b "ValueCalculator" (let ((b (com.redknee.app.crm.calculator.CurrencyFormattingValueCalculator.))) (setBeanProperty b "Delegate" (let ((b (com.redknee.app.crm.calculator.ServiceFee2ValueCalculator.))) (setBeanProperty b "Property" com.redknee.app.crm.bean.ServiceFee2XInfo.FEE$) b))(setBeanProperty b "AbsoluteValue" #f) b)) b))))
(let ((ctx (getContext ctx "core"))) (let ((ctx (getContext ctx "AppCrm"))) (homeCreateOrStore (.get ctx com.redknee.app.crm.bean.KeyConfigurationHome.class) (let ((b (com.redknee.app.crm.bean.KeyConfiguration.))) (setBeanProperty b "Feature" (com.redknee.app.crm.bean.KeyValueFeatureEnum.get 0s))(setBeanProperty b "Key" "$AUXILIARY_SERVICE_NAME$")(setBeanProperty b "ValueCalculator" (let ((b (com.redknee.app.crm.calculator.AuxiliaryServiceValueCalculator.))) (setBeanProperty b "Property" com.redknee.app.crm.bean.AuxiliaryServiceXInfo.NAME$) b)) b))))
(let ((ctx (getContext ctx "core"))) (let ((ctx (getContext ctx "AppCrm"))) (homeCreateOrStore (.get ctx com.redknee.app.crm.bean.KeyConfigurationHome.class) (let ((b (com.redknee.app.crm.bean.KeyConfiguration.))) (setBeanProperty b "Feature" (com.redknee.app.crm.bean.KeyValueFeatureEnum.get 0s))(setBeanProperty b "Key" "$AUXILIARY_SERVICE_ID$")(setBeanProperty b "ValueCalculator" (let ((b (com.redknee.app.crm.calculator.AuxiliaryServiceValueCalculator.))) (setBeanProperty b "Property" com.redknee.app.crm.bean.AuxiliaryServiceXInfo.IDENTIFIER$) b)) b))))
(let ((ctx (getContext ctx "core"))) (let ((ctx (getContext ctx "AppCrm"))) (homeCreateOrStore (.get ctx com.redknee.app.crm.bean.KeyConfigurationHome.class) (let ((b (com.redknee.app.crm.bean.KeyConfiguration.))) (setBeanProperty b "Feature" (com.redknee.app.crm.bean.KeyValueFeatureEnum.get 0s))(setBeanProperty b "Key" "$AUXILIARY_SERVICE_PERIOD$")(setBeanProperty b "ValueCalculator" (let ((b (com.redknee.app.crm.calculator.LanguageFormattingValueCalculator.))) (setBeanProperty b "Delegate" (let ((b (com.redknee.app.crm.calculator.AuxiliaryServiceValueCalculator.))) (setBeanProperty b "Property" com.redknee.app.crm.bean.AuxiliaryServiceXInfo.CHARGING_MODE_TYPE$) b)) b)) b))))
(let ((ctx (getContext ctx "core"))) (let ((ctx (getContext ctx "AppCrm"))) (homeCreateOrStore (.get ctx com.redknee.app.crm.bean.KeyConfigurationHome.class) (let ((b (com.redknee.app.crm.bean.KeyConfiguration.))) (setBeanProperty b "Feature" (com.redknee.app.crm.bean.KeyValueFeatureEnum.get 0s))(setBeanProperty b "Key" "$AUXILIARY_SERVICE_FEE$")(setBeanProperty b "ValueCalculator" (let ((b (com.redknee.app.crm.calculator.CurrencyFormattingValueCalculator.))) (setBeanProperty b "Delegate" (let ((b (com.redknee.app.crm.calculator.AuxiliaryServiceValueCalculator.))) (setBeanProperty b "Property" com.redknee.app.crm.bean.AuxiliaryServiceXInfo.CHARGE$) b))(setBeanProperty b "AbsoluteValue" #f) b)) b))))
(let ((ctx (getContext ctx "core"))) (let ((ctx (getContext ctx "AppCrm"))) (homeCreateOrStore (.get ctx com.redknee.app.crm.bean.KeyConfigurationHome.class) (let ((b (com.redknee.app.crm.bean.KeyConfiguration.))) (setBeanProperty b "Feature" (com.redknee.app.crm.bean.KeyValueFeatureEnum.get 0s))(setBeanProperty b "Key" "$BUNDLE_NAME$")(setBeanProperty b "ValueCalculator" (let ((b (com.redknee.app.crm.calculator.BundleProfilePropertyValueCalculator.))) (setBeanProperty b "Property" com.redknee.app.crm.bundle.BundleProfileXInfo.NAME$) b)) b))))
(let ((ctx (getContext ctx "core"))) (let ((ctx (getContext ctx "AppCrm"))) (homeCreateOrStore (.get ctx com.redknee.app.crm.bean.KeyConfigurationHome.class) (let ((b (com.redknee.app.crm.bean.KeyConfiguration.))) (setBeanProperty b "Feature" (com.redknee.app.crm.bean.KeyValueFeatureEnum.get 0s))(setBeanProperty b "Key" "$BUNDLE_ID$")(setBeanProperty b "ValueCalculator" (let ((b (com.redknee.app.crm.calculator.BundleProfilePropertyValueCalculator.))) (setBeanProperty b "Property" com.redknee.app.crm.bundle.BundleProfileXInfo.BUNDLE_ID$) b)) b))))
(let ((ctx (getContext ctx "core"))) (let ((ctx (getContext ctx "AppCrm"))) (homeCreateOrStore (.get ctx com.redknee.app.crm.bean.KeyConfigurationHome.class) (let ((b (com.redknee.app.crm.bean.KeyConfiguration.))) (setBeanProperty b "Feature" (com.redknee.app.crm.bean.KeyValueFeatureEnum.get 0s))(setBeanProperty b "Key" "$BUNDLE_PERIOD$")(setBeanProperty b "ValueCalculator" (let ((b (com.redknee.app.crm.calculator.LanguageFormattingValueCalculator.))) (setBeanProperty b "Delegate" (let ((b (com.redknee.app.crm.calculator.BundleFeePropertyValueCalculator.))) (setBeanProperty b "Property" com.redknee.app.crm.bundle.BundleFeeXInfo.SERVICE_PERIOD$) b)) b)) b))))
(let ((ctx (getContext ctx "core"))) (let ((ctx (getContext ctx "AppCrm"))) (homeCreateOrStore (.get ctx com.redknee.app.crm.bean.KeyConfigurationHome.class) (let ((b (com.redknee.app.crm.bean.KeyConfiguration.))) (setBeanProperty b "Feature" (com.redknee.app.crm.bean.KeyValueFeatureEnum.get 0s))(setBeanProperty b "Key" "$BUNDLE_FEE$")(setBeanProperty b "ValueCalculator" (let ((b (com.redknee.app.crm.calculator.CurrencyFormattingValueCalculator.))) (setBeanProperty b "Delegate" (let ((b (com.redknee.app.crm.calculator.BundleFeePropertyValueCalculator.))) (setBeanProperty b "Property" com.redknee.app.crm.bundle.BundleFeeXInfo.FEE$) b))(setBeanProperty b "AbsoluteValue" #f) b)) b))))
(let ((ctx (getContext ctx "core"))) (let ((ctx (getContext ctx "AppCrm"))) (homeCreateOrStore (.get ctx com.redknee.app.crm.bean.KeyConfigurationHome.class) (let ((b (com.redknee.app.crm.bean.KeyConfiguration.))) (setBeanProperty b "Feature" (com.redknee.app.crm.bean.KeyValueFeatureEnum.get 0s))(setBeanProperty b "Key" "$PACKAGE_NAME$")(setBeanProperty b "ValueCalculator" (let ((b (com.redknee.app.crm.calculator.ServicePackagePropertyValueCalculator.))) (setBeanProperty b "Property" com.redknee.app.crm.bean.ServicePackageXInfo.NAME$) b)) b))))
(let ((ctx (getContext ctx "core"))) (let ((ctx (getContext ctx "AppCrm"))) (homeCreateOrStore (.get ctx com.redknee.app.crm.bean.KeyConfigurationHome.class) (let ((b (com.redknee.app.crm.bean.KeyConfiguration.))) (setBeanProperty b "Feature" (com.redknee.app.crm.bean.KeyValueFeatureEnum.get 0s))(setBeanProperty b "Key" "$PACKAGE_ID$")(setBeanProperty b "ValueCalculator" (let ((b (com.redknee.app.crm.calculator.ServicePackagePropertyValueCalculator.))) (setBeanProperty b "Property" com.redknee.app.crm.bean.ServicePackageXInfo.ID$) b)) b))))
(let ((ctx (getContext ctx "core"))) (let ((ctx (getContext ctx "AppCrm"))) (homeCreateOrStore (.get ctx com.redknee.app.crm.bean.KeyConfigurationHome.class) (let ((b (com.redknee.app.crm.bean.KeyConfiguration.))) (setBeanProperty b "Feature" (com.redknee.app.crm.bean.KeyValueFeatureEnum.get 0s))(setBeanProperty b "Key" "$PACKAGE_PERIOD$")(setBeanProperty b "ValueCalculator" (let ((b (com.redknee.app.crm.calculator.LanguageFormattingValueCalculator.))) (setBeanProperty b "Delegate" (let ((b (com.redknee.app.crm.calculator.ServicePackageFeePropertyValueCalculator.))) (setBeanProperty b "Property" com.redknee.app.crm.bean.ServicePackageFeeXInfo.SERVICE_PERIOD$) b)) b)) b))))
(let ((ctx (getContext ctx "core"))) (let ((ctx (getContext ctx "AppCrm"))) (homeCreateOrStore (.get ctx com.redknee.app.crm.bean.KeyConfigurationHome.class) (let ((b (com.redknee.app.crm.bean.KeyConfiguration.))) (setBeanProperty b "Feature" (com.redknee.app.crm.bean.KeyValueFeatureEnum.get 0s))(setBeanProperty b "Key" "$PACKAGE_FEE$")(setBeanProperty b "ValueCalculator" (let ((b (com.redknee.app.crm.calculator.CurrencyFormattingValueCalculator.))) (setBeanProperty b "Delegate" (let ((b (com.redknee.app.crm.calculator.ServicePackageFeePropertyValueCalculator.))) (setBeanProperty b "Property" com.redknee.app.crm.bean.ServicePackageFeeXInfo.FEE$) b))(setBeanProperty b "AbsoluteValue" #f) b)) b))))
(let ((ctx (getContext ctx "core"))) (let ((ctx (getContext ctx "AppCrm"))) (homeCreateOrStore (.get ctx com.redknee.app.crm.bean.KeyConfigurationHome.class) (let ((b (com.redknee.app.crm.bean.KeyConfiguration.))) (setBeanProperty b "Feature" (com.redknee.app.crm.bean.KeyValueFeatureEnum.get 0s))(setBeanProperty b "Key" "$SERVICES$")(setBeanProperty b "ValueCalculator" (let ((b (com.redknee.app.crm.calculator.CollectionFormattingValueCalculator.))) (setBeanProperty b "Delegate" (let ((b (com.redknee.app.crm.calculator.CollectionValueCalculator.))) (setBeanProperty b "Delegate" (let ((b (com.redknee.app.crm.calculator.ServiceFee2ValueCalculator.))) (setBeanProperty b "Property" com.redknee.app.crm.bean.ServiceFee2XInfo.SERVICE_ID$) b)) (setBeanProperty b "TemplateType" 0) b))(setBeanProperty b "Separator" "")(setBeanProperty b "SeparatorAfterLastElement" #t) b)) b))))
(let ((ctx (getContext ctx "core"))) (let ((ctx (getContext ctx "AppCrm"))) (homeCreateOrStore (.get ctx com.redknee.app.crm.bean.KeyConfigurationHome.class) (let ((b (com.redknee.app.crm.bean.KeyConfiguration.))) (setBeanProperty b "Feature" (com.redknee.app.crm.bean.KeyValueFeatureEnum.get 0s))(setBeanProperty b "Key" "$AUXILIARYSERVICES$")(setBeanProperty b "ValueCalculator" (let ((b (com.redknee.app.crm.calculator.CollectionFormattingValueCalculator.))) (setBeanProperty b "Delegate" (let ((b (com.redknee.app.crm.calculator.CollectionValueCalculator.))) (setBeanProperty b "Delegate" (let ((b (com.redknee.app.crm.calculator.AuxiliaryServiceValueCalculator.))) (setBeanProperty b "Property" com.redknee.app.crm.bean.AuxiliaryServiceXInfo.IDENTIFIER$) b))(setBeanProperty b "TemplateType" 1) b))(setBeanProperty b "Separator" "")(setBeanProperty b "SeparatorAfterLastElement" #t) b)) b))))
(let ((ctx (getContext ctx "core"))) (let ((ctx (getContext ctx "AppCrm"))) (homeCreateOrStore (.get ctx com.redknee.app.crm.bean.KeyConfigurationHome.class) (let ((b (com.redknee.app.crm.bean.KeyConfiguration.))) (setBeanProperty b "Feature" (com.redknee.app.crm.bean.KeyValueFeatureEnum.get 0s))(setBeanProperty b "Key" "$BUNDLES$")(setBeanProperty b "ValueCalculator" (let ((b (com.redknee.app.crm.calculator.CollectionFormattingValueCalculator.))) (setBeanProperty b "Delegate" (let ((b (com.redknee.app.crm.calculator.CollectionValueCalculator.))) (setBeanProperty b "Delegate" (let ((b (com.redknee.app.crm.calculator.BundleFeePropertyValueCalculator.))) (setBeanProperty b "Property" com.redknee.app.crm.bundle.BundleFeeXInfo.ID$) b)) (setBeanProperty b "TemplateType" 2) b))(setBeanProperty b "Separator" "")(setBeanProperty b "SeparatorAfterLastElement" #t) b)) b))))
(let ((ctx (getContext ctx "core"))) (let ((ctx (getContext ctx "AppCrm"))) (homeCreateOrStore (.get ctx com.redknee.app.crm.bean.KeyConfigurationHome.class) (let ((b (com.redknee.app.crm.bean.KeyConfiguration.))) (setBeanProperty b "Feature" (com.redknee.app.crm.bean.KeyValueFeatureEnum.get 0s))(setBeanProperty b "Key" "$PACKAGES$")(setBeanProperty b "ValueCalculator" (let ((b (com.redknee.app.crm.calculator.CollectionFormattingValueCalculator.))) (setBeanProperty b "Delegate" (let ((b (com.redknee.app.crm.calculator.CollectionValueCalculator.))) (setBeanProperty b "Delegate" (let ((b (com.redknee.app.crm.calculator.ServicePackageFeePropertyValueCalculator.))) (setBeanProperty b "Property" com.redknee.app.crm.bean.ServicePackageFeeXInfo.PACKAGE_ID$) b)) (setBeanProperty b "TemplateType" 3) b))(setBeanProperty b "Separator" "")(setBeanProperty b "SeparatorAfterLastElement" #t) b)) b))))
(let ((ctx (getContext ctx "core"))) (let ((ctx (getContext ctx "AppCrm"))) (homeCreateOrStore (.get ctx com.redknee.app.crm.bean.KeyConfigurationHome.class) (let ((b (com.redknee.app.crm.bean.KeyConfiguration.))) (setBeanProperty b "Feature" (com.redknee.app.crm.bean.KeyValueFeatureEnum.get 0s))(setBeanProperty b "Key" "$SERVICE_NM$")(setBeanProperty b "ValueCalculator" (let ((b (com.redknee.app.crm.calculator.ServiceValueCalculator.))) (setBeanProperty b "Property" com.redknee.app.crm.bean.ServiceXInfo.NAME$) b)) b))))


;; Credit Notice Configurations
(let ((ctx (getContext ctx "core"))) (let ((ctx (getContext ctx "AppCrm"))) (homeCreateOrStore (.get ctx com.redknee.app.crm.bean.KeyConfigurationHome.class) (let ((b (com.redknee.app.crm.bean.KeyConfiguration.))) (setBeanProperty b "Feature" (com.redknee.app.crm.bean.KeyValueFeatureEnum.get 0s))(setBeanProperty b "Key" "_noticeDate")(setBeanProperty b "ValueCalculator" (let ((b (com.redknee.app.crm.calculator.DateFormattingValueCalculator.))) (setBeanProperty b "Delegate" (let ((b (com.redknee.app.crm.calculator.TimestampValueCalculator.)))  b))(setBeanProperty b "Format" "dd MMM, yyyy") b)) b))))
(let ((ctx (getContext ctx "core"))) (let ((ctx (getContext ctx "AppCrm"))) (homeCreateOrStore (.get ctx com.redknee.app.crm.bean.KeyConfigurationHome.class) (let ((b (com.redknee.app.crm.bean.KeyConfiguration.))) (setBeanProperty b "Feature" (com.redknee.app.crm.bean.KeyValueFeatureEnum.get 0s))(setBeanProperty b "Key" "_yearSuffix")(setBeanProperty b "ValueCalculator" (let ((b (com.redknee.app.crm.calculator.DateFormattingValueCalculator.))) (setBeanProperty b "Delegate" (let ((b (com.redknee.app.crm.calculator.TimestampValueCalculator.)))  b))(setBeanProperty b "Format" "yy") b)) b))))
