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
package com.redknee.app.crm.delivery.email;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.support.MapSupport;
import com.redknee.framework.xlog.log.MinorLogMsg;

import com.redknee.app.crm.bean.KeyConfiguration;
import com.redknee.app.crm.bean.KeyValueFeatureEnum;
import com.redknee.app.crm.calculator.ValueCalculator;
import com.redknee.app.crm.support.EmailKeywordConfigurationSupportHelper;
import com.redknee.app.crm.support.HomeSupportHelper;
import com.redknee.app.crm.support.KeyValueSupportHelper;


/**
 * This class can be used to migrate Email configuration from 8.2.0 to 8.2.2's
 * generic key configuration table.
 *
 * @author aaron.gourley@redknee.com
 * @since 8.2
 */
public class EmailKeywordConfigurationMigrationTool
{   
    public EmailKeywordConfigurationMigrationTool()
    {
        super();
    }

    /**
     * {@inheritDoc}
     */
    public void migrate(Context ctx)
    {
        KeywordConfiguration emailKeywordConfig = (KeywordConfiguration) ctx.get(KeywordConfiguration.class);

        Map<String, EmailConstant> oldConstants = emailKeywordConfig.getSubstitutionConfig();
        if (oldConstants != null && oldConstants.size() > 0)
        {
            oldConstants = new HashMap<String, EmailConstant>(oldConstants);
            
            Collection<KeyConfiguration> emailKeys = KeyValueSupportHelper.get(ctx).getEmailTemplateKeys(ctx, false);
            if (emailKeys != null)
            {
                final List<KeyConfiguration> emailKeyList;
                if (emailKeys instanceof List)
                {
                    emailKeyList = (List<KeyConfiguration>) emailKeys;
                }
                else
                {
                    emailKeyList = new ArrayList<KeyConfiguration>(emailKeys);
                }

                Map<String, KeyConfiguration> existingKeyMap = MapSupport.fromList(emailKeyList);
                if (emailKeys != null)
                {
                    for (Iterator<Map.Entry<String, EmailConstant>> iterator = oldConstants.entrySet().iterator(); iterator.hasNext(); )
                    {
                        Map.Entry<String, EmailConstant> entry = iterator.next();

                        EmailConstant emailConstant = entry.getValue();
                        if (emailConstant == null)
                        {
                            iterator.remove();
                            continue;
                        }

                        ValueCalculator valueCalculator = EmailKeywordConfigurationSupportHelper.get(ctx).getNonDeprecatedValueCalculator(emailConstant.getValueCalculator());
                        if (valueCalculator == null)
                        {
                            iterator.remove();
                            continue;
                        }

                        try
                        {
                            if (existingKeyMap.containsKey(entry.getKey()))
                            {
                                KeyConfiguration existingConfig = existingKeyMap.get(entry.getKey());

                                if (KeyValueFeatureEnum.INVOICE_EMAIL.equals(existingConfig.getFeature()))
                                {
                                    // Update calculator
                                    KeyConfiguration newConfig = new KeyConfiguration();
                                    newConfig.setKey(emailConstant.getKeyword());
                                    newConfig.setFeature(KeyValueFeatureEnum.INVOICE_EMAIL);
                                    newConfig.setValueCalculator(valueCalculator);
                                    HomeSupportHelper.get(ctx).storeBean(ctx, newConfig);
                                }
                                else if (!(valueCalculator.equals(existingConfig.getValueCalculator())
                                            && KeyValueFeatureEnum.GENERIC.equals(existingConfig.getFeature())))
                                {
                                    throw new Exception("Invoice email keyword " + emailConstant.getKeyword()
                                            + "' exists for another feature (" + existingConfig.getFeature() + ")");
                                }
                            }
                            else
                            {
                                // Create calculator
                                KeyConfiguration newConfig = new KeyConfiguration();
                                newConfig.setKey(emailConstant.getKeyword());
                                newConfig.setFeature(KeyValueFeatureEnum.INVOICE_EMAIL);
                                newConfig.setValueCalculator(valueCalculator);
                                HomeSupportHelper.get(ctx).createBean(ctx, newConfig);
                            }

                            iterator.remove();
                        }
                        catch (Exception e)
                        {
                            // Don't remove from map.  Let admin correct error then try migration again later.
                            new MinorLogMsg(this, "Error propogating calculator update for keyword '" + emailConstant.getKeyword() + "' to primary key/value configuration.", e).log(ctx);
                        }
                    }
                }
            }

            // Fire property change to update map in journal
            emailKeywordConfig.setSubstitutionConfig(oldConstants);
        }
    }

}
