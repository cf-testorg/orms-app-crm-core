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
package com.redknee.app.crm.support;


import java.util.Collection;
import java.util.Map;

import com.redknee.framework.xhome.context.Context;

import com.redknee.app.crm.bean.KeyConfiguration;
import com.redknee.app.crm.bean.KeyValueFeatureEnum;


/**
 *  This class provides methods to retrieve configured keys for classes/features as well as
 *  compute key/value pairs for such classes/features.
 *
 * @author aaron.gourley@redknee.com
 * @since 8.2
 */
public interface KeyValueSupport extends Support
{   
    public Collection<KeyConfiguration> getGlobalKeys(Context ctx, boolean forKeyValueReplacement);
    
    public Collection<KeyConfiguration> getEmailTemplateKeys(Context ctx, boolean forKeyValueReplacement);

    public Collection<KeyConfiguration> getStateChangeEmailTemplateKeys(Context ctx, boolean forKeyValueReplacement);
    
    public Collection<KeyConfiguration> getServiceSuspensionEmailTemplateKeys(Context ctx, boolean forKeyValueReplacement);
    
    public Collection<KeyConfiguration> getServiceUnsuspensionEmailTemplateKeys(Context ctx, boolean forKeyValueReplacement);

    public Collection<KeyConfiguration> getRecurringRechargePreWarnEmailTemplateKeys(Context ctx, boolean forKeyValueReplacement);

    public Collection<KeyConfiguration> getExpiryExtensionEmailTemplateKeys(Context ctx, boolean forKeyValueReplacement);

    public Collection<KeyConfiguration> getPrepaidRechargeEmailTemplateKeys(Context ctx, boolean forKeyValueReplacement);

    public Collection<KeyConfiguration> getPreExpiryEmailTemplateKeys(Context ctx, boolean forKeyValueReplacement);

    public Collection<KeyConfiguration> getTransferDisputeEmailTemplateKeys(Context ctx, boolean forKeyValueReplacement);
    
    public Collection<KeyConfiguration> getAlcatelSSCCRMSpidKeys(Context ctx, boolean forKeyValueReplacement);
    
    public Collection<KeyConfiguration> getAlcatelSSCServiceKeys(Context ctx, boolean forKeyValueReplacement);
    
    public Collection<KeyConfiguration> getAlcatelSSCSubscriptionKeys(Context ctx, boolean forKeyValueReplacement);
    
    public Map<String, Object> getGlobalKeyValueMap(Context ctx);
    
    public Map<String, Object> getEmailTemplateKeyValueMap(Context ctx);

    public Map<String, Object> getStateChangeEmailTemplateKeyValueMap(Context ctx);

    public Map<String, Object> getServiceSuspensionEmailTemplateKeyValueMap(Context ctx);

    public Map<String, Object> getServiceUnsuspensionEmailTemplateKeyValueMap(Context ctx);

    public Map<String, Object> getRecurringRechargePreWarnEmailTemplateKeyValueMap(Context ctx);

    public Map<String, Object> getExpiryExtensionEmailTemplateKeyValueMap(Context ctx);
    
    public Map<String, Object> getPrepaidRechargeEmailTemplateKeyValueMap(Context ctx);

    public Map<String, Object> getPreExpiryEmailTemplateKeyValueMap(Context ctx);

    public Map<String, Object> getTransferDisputeEmailTemplateKeyValueMap(Context ctx);
    
    public Map<String, Object> getAlcatelSSCCCRMSpidKeyValueMap(Context ctx);
    
    public Map<String, Object> getAlcatelSSCServiceKeyValueMap(Context ctx);
    
    public Map<String, Object> getAlcatelSSCSubscriptionKeyValueMap(Context ctx);
    
    public Collection<KeyConfiguration> getConfiguredKeys(Context ctx, boolean forKeyValueReplacement, KeyValueFeatureEnum... features);
    
    public Map<String, Object> getCalculatedKeyValueMap(Context ctx, KeyValueFeatureEnum... features);

    public String getUserInputValue(Context ctx, KeyValueFeatureEnum feature, String key);
    
    public String getUserInputValue(Context ctx, KeyValueFeatureEnum feature, KeyConfiguration keyConf);
}
