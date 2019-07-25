/*
 * Copyright (c) 2007, REDKNEE.com. All Rights Reserved.
 * 
 * This software is the confidential and proprietary information of REDKNEE.com.
 * ("Confidential Information"). You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement you entered
 * into with REDKNEE.com.
 * 
 * REDKNEE.COM MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, OR NON-INFRINGEMENT.
 * REDKNEE.COM SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF
 * USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */
package com.redknee.app.crm.bundle;

import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;

import com.redknee.framework.xhome.beans.SafetyUtil;
import com.redknee.product.bundle.manager.provision.common.type.UnitType;
import com.redknee.product.bundle.manager.provision.common.type.YearDateTime;
import com.redknee.product.bundle.manager.provision.v5_0.bundle.AbsoluteTime;
import com.redknee.product.bundle.manager.provision.v5_0.bundle.ActivationScheme;
import com.redknee.product.bundle.manager.provision.v5_0.bundle.BundleCategoryAssociation;
import com.redknee.product.bundle.manager.provision.v5_0.bundle.BundleProfile;
import com.redknee.product.bundle.manager.provision.v5_0.bundle.DurationType;
import com.redknee.product.bundle.manager.provision.v5_0.bundle.ExpiryScheme;
import com.redknee.product.bundle.manager.provision.v5_0.bundle.GroupChargingScheme;
import com.redknee.product.bundle.manager.provision.v5_0.bundle.QuotaScheme;
import com.redknee.product.bundle.manager.provision.v5_0.bundle.RecurrenceScheme;
import com.redknee.product.bundle.manager.provision.v5_0.bundle.Validity;


/**
 * @author bpandey
 * 
 */
public class BundleProfileAdapter
{

    /**
     * Adapts BundleProfile to BM bundle profile
     * 
     * @param bundleProfile
     * @return
     */
    public static BundleProfile adaptBundleProfile(com.redknee.app.crm.bean.core.BundleProfile bundleProfile)
    {
        BundleProfile adaptedBundleProfile;
        adaptedBundleProfile = new BundleProfile();
        adaptedBundleProfile.bundleId = bundleProfile.getBundleId();
        adaptedBundleProfile.groupBundleId = bundleProfile.getGroupBundleId();
        adaptedBundleProfile.name = bundleProfile.getName();
        // overwriting these values for now since the CRM-BM view doesn't have these data
        adaptedBundleProfile.bundlePrice = (bundleProfile.isFlex() && FlexTypeEnum.SECONDARY.equals(bundleProfile
                .getFlexType())) ? bundleProfile.getAuxiliaryServiceCharge() : 0;
        adaptedBundleProfile.currencyType = "CAD";
        adaptedBundleProfile.spid = bundleProfile.getSpid();
        adaptedBundleProfile.categoryIds = new BundleCategoryAssociation[bundleProfile.getBundleCategoryIds().size()];
        final Iterator<Map.Entry<?, com.redknee.app.crm.bean.core.BundleCategoryAssociation>> iter;
        iter = bundleProfile.getBundleCategoryIds().entrySet().iterator();
        int i = 0;
        boolean isDataBundle = false;
        while (iter.hasNext())
        {
            com.redknee.app.crm.bean.core.BundleCategoryAssociation association = iter.next().getValue();
            adaptedBundleProfile.categoryIds[i] = new BundleCategoryAssociation();
            adaptedBundleProfile.categoryIds[i].bundlecategoryId = association.getCategoryId();
            if (bundleProfile.isCurrency())
            {
                adaptedBundleProfile.categoryIds[i].unitType = UnitType.from_int(UnitTypeEnum.CURRENCY_INDEX);
            }
            else if (association.getType() == UnitTypeEnum.VOLUME_BYTES_INDEX)
            {
                isDataBundle = true;
                adaptedBundleProfile.categoryIds[i].unitType = UnitType.VOLUME_BYTES;
            }
            else
            {
                adaptedBundleProfile.categoryIds[i].unitType = UnitType.from_int(association.getType());
            }
            adaptedBundleProfile.categoryIds[i].perUnit = 1;
            adaptedBundleProfile.categoryIds[i].rate = (int) association.getRate();
            i++;
        }
        if (bundleProfile.isSingleService() && isDataBundle)
        {
            adaptedBundleProfile.initialBalanceLimit = (long) (bundleProfile.getInitialBalanceLimit());
            adaptedBundleProfile.rolloverMax = (long) (bundleProfile.getRolloverMax());
        }
        else
        {
            adaptedBundleProfile.initialBalanceLimit = bundleProfile.getInitialBalanceLimit();
            adaptedBundleProfile.rolloverMax = bundleProfile.getRolloverMax();
        }
        adaptedBundleProfile.quotaScheme = QuotaScheme.from_int(bundleProfile.getQuotaScheme().getIndex());
        adaptedBundleProfile.chargePriority = bundleProfile.getChargingPriority();
        adaptedBundleProfile.groupChargingScheme = GroupChargingScheme.from_int(bundleProfile.getGroupChargingScheme()
                .getIndex());
        adaptedBundleProfile.rolloverPercent = bundleProfile.getRolloverPercent();
        adaptedBundleProfile.expiryPercent = bundleProfile.getExpiryPercent();
        adaptedBundleProfile.isCurrency = bundleProfile.isCurrency();
        // TT# : 12060603064. For Cross Service Bundle, pass enableWeightedUnit = true,
        // otherwise pass enableWeightedUnit = false
        // Passing enableWeightedUnit = true, helps URCS determine its Cross Service
        // Bundle.
        if (bundleProfile.getType() == BundleTypeEnum.CROSS_SERVICE_INDEX)
        {
            adaptedBundleProfile.enableWeightedUnit = true;
        }
        else
        {
            adaptedBundleProfile.enableWeightedUnit = false;
        }
        adaptedBundleProfile.rolloverMaxPercent = bundleProfile.getRolloverMaxPercentage();
        adaptedBundleProfile.enablePromotionProvision = bundleProfile.getEnablePromotionProvision();
        adaptedBundleProfile.recurrenceScheme = RecurrenceScheme.from_int(bundleProfile.getRecurrenceScheme()
                .getIndex());
        adaptedBundleProfile.activationScheme = ActivationScheme.from_int(bundleProfile.getActivationScheme()
                .getIndex());
        adaptedBundleProfile.expiryScheme = ExpiryScheme.from_int(bundleProfile.getExpiryScheme().getIndex());
        int validity = bundleProfile.getValidity();
        adaptedBundleProfile.validity = new Validity();
        adaptedBundleProfile.validity.validity = validity;
        int bundleInterval = DurationTypeEnum.DAY_INDEX;
        boolean isNotBCD = bundleProfile.getInterval() != DurationTypeEnum.BCD_INDEX;
        if (isNotBCD)
        {
            bundleInterval = bundleProfile.getInterval();
        }
        adaptedBundleProfile.validity.unit = DurationType.from_int(bundleInterval);
        boolean oneOffFixedInterval = SafetyUtil.safeEquals(bundleProfile.getRecurrenceScheme(),
                RecurrenceTypeEnum.ONE_OFF_FIXED_INTERVAL);
        boolean expireAndDelayPurge = SafetyUtil.safeEquals(bundleProfile.getExpiryScheme(),
                ExpiryTypeEnum.EXPIRE_AND_DELAY_PURGE);
        if (bundleProfile.getRecurrenceScheme().isInterval() && (oneOffFixedInterval || expireAndDelayPurge)
                && validity > 0 && isNotBCD)
        {
        	adaptedBundleProfile.validity.isPresent = true;
        }
      	int relativeValidity = bundleProfile.getRelativeStartValidity();
        adaptedBundleProfile.relativeStartValidity = new Validity();
        adaptedBundleProfile.relativeStartValidity.validity = validity;
        adaptedBundleProfile.relativeStartValidity.unit = DurationType.from_int((bundleProfile
                .getRelativeStartInterval()));
        if (bundleProfile.getRecurrenceScheme().isInterval() && relativeValidity > 0)
        {
            adaptedBundleProfile.relativeStartValidity.isPresent = true;
        }
        int recurringValidity = bundleProfile.getRecurringStartValidity();
        adaptedBundleProfile.recurrenceValidity = new Validity();
        adaptedBundleProfile.recurrenceValidity.validity = recurringValidity;
        adaptedBundleProfile.recurrenceValidity.unit = DurationType
                .from_int((bundleProfile.getRecurringStartInterval()));
        if (bundleProfile.getRecurrenceScheme().isInterval()
                && SafetyUtil.safeEquals(bundleProfile.getRecurrenceScheme(),
                        RecurrenceTypeEnum.RECUR_CYCLE_FIXED_INTERVAL) && recurringValidity > 0)
        {
            adaptedBundleProfile.recurrenceValidity.isPresent = true;
        }
        YearDateTime startDate = new YearDateTime();
        if (bundleProfile.getStartDate() != null)
        {
            Calendar bundleStartDate = Calendar.getInstance();
            bundleStartDate.setTime(bundleProfile.getStartDate());
            startDate.year = bundleStartDate.get(Calendar.YEAR);
            startDate.month = bundleStartDate.get(Calendar.MONTH) + 1;
            startDate.day = bundleStartDate.get(Calendar.DAY_OF_MONTH);
            startDate.hour = bundleStartDate.get(Calendar.HOUR_OF_DAY);
            startDate.minute = bundleStartDate.get(Calendar.MINUTE);
            startDate.isPresent = true;
        }
        else
        {
            startDate.isPresent = false;
        }
        adaptedBundleProfile.startDate = startDate;
        adaptedBundleProfile.expiryScheme = ExpiryScheme.from_int(bundleProfile.getExpiryScheme().getIndex());
        YearDateTime expiryDate = new YearDateTime();
        if (bundleProfile.getEndDate() != null)
        {
            Calendar bundleExpiryDate = Calendar.getInstance();
            bundleExpiryDate.setTime(bundleProfile.getEndDate());
            expiryDate.year = bundleExpiryDate.get(Calendar.YEAR);
            expiryDate.month = bundleExpiryDate.get(Calendar.MONTH) + 1;
            expiryDate.day = bundleExpiryDate.get(Calendar.DAY_OF_MONTH);
            expiryDate.hour = bundleExpiryDate.get(Calendar.HOUR_OF_DAY);
            expiryDate.minute = bundleExpiryDate.get(Calendar.MINUTE);
            expiryDate.isPresent = true;
        }
        else
        {
            expiryDate.isPresent = false;
        }
        adaptedBundleProfile.expiryDate = expiryDate;
        adaptedBundleProfile.reportOnBillingCycle = true;
        adaptedBundleProfile.reportOnBucketRemoval = true;
        adaptedBundleProfile.reportOnBucketPurge = true;
        adaptedBundleProfile.recurIntervalTime = new AbsoluteTime();
        adaptedBundleProfile.recurIntervalTime.startHour = bundleProfile.getRecurringStartHour();
        adaptedBundleProfile.recurIntervalTime.startMinute = bundleProfile.getRecurringStartMinutes();
        adaptedBundleProfile.recurIntervalTime.isPresent = true;
        adaptedBundleProfile.relativeStartTime = new AbsoluteTime();
        adaptedBundleProfile.relativeStartTime.startHour = bundleProfile.getRelativeStartHour();
        adaptedBundleProfile.relativeStartTime.startMinute = bundleProfile.getRelativeStartMinutes();
        adaptedBundleProfile.relativeStartTime.isPresent = true;
        return adaptedBundleProfile;
    }
}
