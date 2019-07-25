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


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.redknee.framework.xhome.beans.xi.PropertyInfo;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.elang.False;
import com.redknee.framework.xhome.elang.In;
import com.redknee.framework.xhome.elang.True;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.support.MapSupport;
import com.redknee.framework.xlog.log.DebugLogMsg;
import com.redknee.framework.xlog.log.InfoLogMsg;
import com.redknee.framework.xlog.log.MinorLogMsg;

import com.redknee.app.crm.bean.KeyConfiguration;
import com.redknee.app.crm.bean.KeyConfigurationXInfo;
import com.redknee.app.crm.bean.KeyValueFeatureEnum;
import com.redknee.app.crm.calculator.UserInputValueCalculator;
import com.redknee.app.crm.calculator.ValueCalculator;
import com.redknee.app.crm.calculator.ValueCalculatorProxy;


/**
 * @author aaron.gourley@redknee.com
 * @since 8.2
 */
public class DefaultKeyValueSupport implements KeyValueSupport
{
    protected static KeyValueSupport instance_ = null;
    public static KeyValueSupport instance()
    {
        if (instance_ == null)
        {
            instance_ = new DefaultKeyValueSupport();
        }
        return instance_;
    }

    protected DefaultKeyValueSupport()
    {
    }

    public Collection<KeyConfiguration> getGlobalKeys(Context ctx, boolean forKeyValueReplacement)
    {
        return getConfiguredKeys(ctx, forKeyValueReplacement, KeyValueFeatureEnum.GENERIC);
    }

    public Collection<KeyConfiguration> getEmailTemplateKeys(Context ctx, boolean forKeyValueReplacement)
    {
        return getConfiguredKeys(ctx, forKeyValueReplacement, KeyValueFeatureEnum.INVOICE_EMAIL);
    }

    public Collection<KeyConfiguration> getStateChangeEmailTemplateKeys(Context ctx, boolean forKeyValueReplacement)
    {
        return getConfiguredKeys(ctx, forKeyValueReplacement, KeyValueFeatureEnum.STATE_CHANGE_EMAIL);
    }

    public Collection<KeyConfiguration> getServiceSuspensionEmailTemplateKeys(Context ctx, boolean forKeyValueReplacement)
    {
        return getConfiguredKeys(ctx, forKeyValueReplacement, KeyValueFeatureEnum.SERVICE_SUSPENSION_EMAIL);
    }

    public Collection<KeyConfiguration> getServiceUnsuspensionEmailTemplateKeys(Context ctx, boolean forKeyValueReplacement)
    {
        return getConfiguredKeys(ctx, forKeyValueReplacement, KeyValueFeatureEnum.SERVICE_UNSUSPENSION_EMAIL);
    }
    
    public Collection<KeyConfiguration> getRecurringRechargePreWarnEmailTemplateKeys(Context ctx, boolean forKeyValueReplacement)
    {
        return getConfiguredKeys(ctx, forKeyValueReplacement, KeyValueFeatureEnum.RECURRING_RECHARGE_PREWARN_EMAIL);
    }

    public Collection<KeyConfiguration> getExpiryExtensionEmailTemplateKeys(Context ctx, boolean forKeyValueReplacement)
    {
        return getConfiguredKeys(ctx, forKeyValueReplacement, KeyValueFeatureEnum.EXPIRY_EXTENSION_EMAIL);
    }

    public Collection<KeyConfiguration> getPrepaidRechargeEmailTemplateKeys(Context ctx, boolean forKeyValueReplacement)
    {
        return getConfiguredKeys(ctx, forKeyValueReplacement, KeyValueFeatureEnum.PREPAID_RECHARGE_EMAIL);
    }

    public Collection<KeyConfiguration> getPreExpiryEmailTemplateKeys(Context ctx, boolean forKeyValueReplacement)
    {
        return getConfiguredKeys(ctx, forKeyValueReplacement, KeyValueFeatureEnum.PRE_EXPIRY_EMAIL);
    }

    public Collection<KeyConfiguration> getTransferDisputeEmailTemplateKeys(Context ctx, boolean forKeyValueReplacement)
    {
        return getConfiguredKeys(ctx, forKeyValueReplacement, KeyValueFeatureEnum.TRANSFER_DISPUTE_EMAIL);
    }

    public Collection<KeyConfiguration> getAlcatelSSCCRMSpidKeys(Context ctx, boolean forKeyValueReplacement)
    {
        return getConfiguredKeys(ctx, forKeyValueReplacement, KeyValueFeatureEnum.ALCATEL_SSC_SPID);
    }

    public Collection<KeyConfiguration> getAlcatelSSCServiceKeys(Context ctx, boolean forKeyValueReplacement)
    {
        return getConfiguredKeys(ctx, forKeyValueReplacement, 
                KeyValueFeatureEnum.ALCATEL_SSC_SERVICE, 
                KeyValueFeatureEnum.ALCATEL_SSC_SPID);
    }

    public Collection<KeyConfiguration> getAlcatelSSCSubscriptionKeys(Context ctx, boolean forKeyValueReplacement)
    {
        return getConfiguredKeys(ctx, forKeyValueReplacement, 
                KeyValueFeatureEnum.ALCATEL_SSC_SUBSCRIPTION, 
                KeyValueFeatureEnum.ALCATEL_SSC_SERVICE, 
                KeyValueFeatureEnum.ALCATEL_SSC_SPID);
    }

    public Map<String, Object> getGlobalKeyValueMap(Context ctx)
    {
        return getKeyValueMapUsingDefaultBeanLoader(ctx,
                KeyValueFeatureEnum.GENERIC);
    }

    public Map<String, Object> getEmailTemplateKeyValueMap(Context ctx)
    {
        return getKeyValueMapUsingDefaultBeanLoader(ctx, 
                KeyValueFeatureEnum.INVOICE_EMAIL, 
                KeyValueFeatureEnum.GENERIC);
    }

    public Map<String, Object> getAlcatelSSCCCRMSpidKeyValueMap(Context ctx)
    {
        return getKeyValueMapUsingDefaultBeanLoader(ctx, 
                KeyValueFeatureEnum.ALCATEL_SSC_SPID, 
                KeyValueFeatureEnum.GENERIC);
    }

    public Map<String, Object> getAlcatelSSCServiceKeyValueMap(Context ctx)
    {
        return getKeyValueMapUsingDefaultBeanLoader(ctx, 
                KeyValueFeatureEnum.ALCATEL_SSC_SERVICE, 
                KeyValueFeatureEnum.ALCATEL_SSC_SPID, 
                KeyValueFeatureEnum.GENERIC);
    }

    public Map<String, Object> getAlcatelSSCSubscriptionKeyValueMap(Context ctx)
    {
        return getKeyValueMapUsingDefaultBeanLoader(ctx, 
                KeyValueFeatureEnum.ALCATEL_SSC_SUBSCRIPTION, 
                KeyValueFeatureEnum.ALCATEL_SSC_SERVICE, 
                KeyValueFeatureEnum.ALCATEL_SSC_SPID, 
                KeyValueFeatureEnum.GENERIC);
    }

    public Map<String, Object> getStateChangeEmailTemplateKeyValueMap(Context ctx)
    {
        return getKeyValueMapUsingDefaultBeanLoader(ctx,
                KeyValueFeatureEnum.STATE_CHANGE_EMAIL,
                KeyValueFeatureEnum.GENERIC);
    }

    public Map<String, Object> getServiceSuspensionEmailTemplateKeyValueMap(Context ctx)
    {
        return getKeyValueMapUsingDefaultBeanLoader(ctx,
                KeyValueFeatureEnum.SERVICE_SUSPENSION_EMAIL,
                KeyValueFeatureEnum.GENERIC);
    }

    public Map<String, Object> getServiceUnsuspensionEmailTemplateKeyValueMap(Context ctx)
    {
        return getKeyValueMapUsingDefaultBeanLoader(ctx,
                KeyValueFeatureEnum.SERVICE_UNSUSPENSION_EMAIL,
                KeyValueFeatureEnum.GENERIC);
    }

    public Map<String, Object> getRecurringRechargePreWarnEmailTemplateKeyValueMap(Context ctx)
    {
        return getKeyValueMapUsingDefaultBeanLoader(ctx,
                KeyValueFeatureEnum.RECURRING_RECHARGE_PREWARN_EMAIL,
                KeyValueFeatureEnum.GENERIC);
    }

    public Map<String, Object> getExpiryExtensionEmailTemplateKeyValueMap(Context ctx)
    {
        return getKeyValueMapUsingDefaultBeanLoader(ctx,
                KeyValueFeatureEnum.EXPIRY_EXTENSION_EMAIL,
                KeyValueFeatureEnum.GENERIC);
    }

    public Map<String, Object> getPrepaidRechargeEmailTemplateKeyValueMap(Context ctx)
    {
        return getKeyValueMapUsingDefaultBeanLoader(ctx,
                KeyValueFeatureEnum.PREPAID_RECHARGE_EMAIL,
                KeyValueFeatureEnum.GENERIC);
    }

    public Map<String, Object> getPreExpiryEmailTemplateKeyValueMap(Context ctx)
    {
        return getKeyValueMapUsingDefaultBeanLoader(ctx,
                KeyValueFeatureEnum.PRE_EXPIRY_EMAIL,
                KeyValueFeatureEnum.GENERIC);
    }

    public Map<String, Object> getTransferDisputeEmailTemplateKeyValueMap(Context ctx)
    {
        return getKeyValueMapUsingDefaultBeanLoader(ctx,
                KeyValueFeatureEnum.TRANSFER_DISPUTE_EMAIL,
                KeyValueFeatureEnum.GENERIC);
    }

    public Collection<KeyConfiguration> getConfiguredKeys(Context ctx, boolean forKeyValueReplacement, KeyValueFeatureEnum... features)
    {
        Collection<KeyConfiguration> result = new ArrayList<KeyConfiguration>();

        Object filter = True.instance();
        if (features != null)
        {
            if (features.length > 1)
            {
                filter = new In();
                ((In)filter).setArg(KeyConfigurationXInfo.FEATURE);
                for (KeyValueFeatureEnum feature : features)
                {
                    if (feature != null)
                    {
                        ((In)filter).add(feature);   
                    }   
                }
            }
            else if (features.length == 1)
            {
                filter = new EQ(KeyConfigurationXInfo.FEATURE, features[0]);
            }
            else
            {
                filter = False.instance();
            }
        }

        Collection<KeyConfiguration> keyConf = null;
        try
        {
            keyConf = HomeSupportHelper.get(ctx).getBeans(ctx, KeyConfiguration.class, filter);
        }
        catch (HomeException e)
        {
            new MinorLogMsg(this, "Error retrieving email constant keys for " + Arrays.toString(features), e).log(ctx);
        }


        if (keyConf != null)
        {
            if (!forKeyValueReplacement)
            {
                result = keyConf;
            }
            else
            {
                // sort by key length so longest is replaced first, else common short strings corrupt longer strings
                final List<KeyConfiguration> keyList;
                if (keyConf instanceof List)
                {
                    keyList = (List<KeyConfiguration>) keyConf;
                }
                else
                {
                    keyList = new ArrayList<KeyConfiguration>(keyConf);
                }

                Map<String, KeyConfiguration> substitutionMap = MapSupport.fromList(keyList);
                List<String> keys = new ArrayList<String>(substitutionMap.keySet());
                Collections.sort(keys, new StringLengthComparator());

                for (String key : keys)
                {
                    result.add(substitutionMap.get(key));
                }
            }
        }

        return result;
    }


    /**
     * This method is private because it is a little too in-depth regarding
     * choice of bean loader for other classes to worry about.
     */
    private Map<String, Object> getKeyValueMapUsingDefaultBeanLoader(Context ctx, KeyValueFeatureEnum... feature)
    {
        ctx = ctx.createSubContext();
        if (feature != null && feature.length > 0)
        {
            BeanLoaderSupportHelper.get(ctx).setBeanLoaderMap(ctx, getBeanLoaderMapForFeature(ctx, feature[0]));
        }
        return getCalculatedKeyValueMap(ctx, feature);
    }


    public Map<String, Object> getCalculatedKeyValueMap(Context ctx, KeyValueFeatureEnum... features)
    {
        // Create a sub-context to hold all of the beans retrieved by the bean loader
        ctx = ctx.createSubContext();

        // sort by key length so longest is replaced first, else common short strings corrupt longer strings
        Map<String, Object> keyValueMap = new TreeMap<String, Object>(new StringLengthComparator());

        Collection<KeyConfiguration> keys = getConfiguredKeys(ctx, false, features);
        for (KeyConfiguration key : keys)
        {
            ValueCalculator valueCalculator = key.getValueCalculator();

            Object value = null;
            if (valueCalculator != null)
            {
                // Put the key configuration bean in the context for use by calculators
                // that might need to know more about the feature that is is being used with.
                ctx.put(KeyConfiguration.class, key);
                value = valueCalculator.getValue(ctx);   
            }

            keyValueMap.put(key.getKey(), value);
        }

        return keyValueMap;
    }

    public String getUserInputValue(Context ctx, KeyValueFeatureEnum feature, String key)
    {
        KeyConfiguration keyConf = null;
        try
        {
            keyConf = HomeSupportHelper.get(ctx).findBean(ctx, KeyConfiguration.class, key);
        }
        catch (HomeException e)
        {
            String msg = "Error looking up key configuration entry for key=" + key;
            new InfoLogMsg(this, msg + ": " + e.getMessage(), null).log(ctx);
            new DebugLogMsg(this, msg, e).log(ctx);
        }

        return getUserInputValue(ctx, feature, keyConf);
    }

    public String getUserInputValue(Context ctx, KeyValueFeatureEnum feature, KeyConfiguration keyConf)
    {
        String result = "";

        if (keyConf != null)
        {
            ValueCalculator calc = keyConf.getValueCalculator();
            if (calc instanceof ValueCalculatorProxy)
            {
                calc = ((ValueCalculatorProxy)calc).findDecorator(UserInputValueCalculator.class);
            }
            if (calc instanceof UserInputValueCalculator)
            {
                Context sCtx = ctx.createSubContext();
                sCtx.put(KeyConfiguration.class, keyConf);

                BeanLoaderSupportHelper.get(sCtx).setBeanLoaderMap(sCtx, getBeanLoaderMapForFeature(ctx, feature));

                result = String.valueOf(((UserInputValueCalculator)calc).getValue(sCtx));
            }
        }

        return result;
    }


    /**
     * This method is protected because it alters the input context (not a sub-context)
     */
    protected Map<Class, Collection<PropertyInfo>> getBeanLoaderMapForFeature(
            Context ctx,
            KeyValueFeatureEnum feature)
            {
        // This method should be over-ridden as required by the applications that implement the features
        return BeanLoaderSupportHelper.get(ctx).getBeanLoaderMap(ctx);
            }

    private final class StringLengthComparator implements Comparator<String>
    {

        public int compare(String key1, String key2)
        {
            if (key1 == null)
            {
                return -1;
            }
            if (key2 == null)
            {
                return 1;
            }
            int result = Integer.valueOf(key2.length()).compareTo(Integer.valueOf(key1.length()));
            if (result == 0)
            {
                // TreeMap will blow over keys of equal length to other keys, so we must
                // fall back to default compare if keys are same length.
                result = key1.compareTo(key2);
            }
            return result;
        }
    }
}
