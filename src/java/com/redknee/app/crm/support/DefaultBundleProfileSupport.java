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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.redknee.framework.license.LicenseMgr;
import com.redknee.framework.xhome.beans.xi.PropertyInfo;
import com.redknee.framework.xhome.context.Context;

import com.redknee.app.crm.bundle.BundleTypeEnum;
import com.redknee.app.crm.bundle.UnitTypeEnum;
import com.redknee.app.crm.bundle.license.BMLicenseConstants;
import com.redknee.product.bundle.manager.api.v21.BundleProfileApiXInfo;
import com.redknee.product.bundle.manager.provision.v5_0.bundle.AbsoluteTime;
import com.redknee.product.bundle.manager.provision.v5_0.bundle.ActivationScheme;
import com.redknee.product.bundle.manager.provision.v5_0.bundle.BundleProfile;
import com.redknee.product.bundle.manager.provision.v5_0.bundle.DurationType;
import com.redknee.product.bundle.manager.provision.v5_0.bundle.ExpiryScheme;
import com.redknee.product.bundle.manager.provision.v5_0.bundle.GroupChargingScheme;
import com.redknee.product.bundle.manager.provision.v5_0.bundle.QuotaScheme;
import com.redknee.product.bundle.manager.provision.v5_0.bundle.RecurrenceScheme;
import com.redknee.product.bundle.manager.provision.v5_0.bundle.Validity;
import com.redknee.product.bundle.manager.provision.common.param.Parameter;
import com.redknee.product.bundle.manager.provision.common.param.ParameterValueType;
import com.redknee.product.bundle.manager.provision.common.type.YearDateTime;

public class DefaultBundleProfileSupport implements BundleProfileSupport
{
    protected static BundleProfileSupport instance_ = null;
    public static BundleProfileSupport instance()
    {
        if (instance_ == null)
        {
            instance_ = new DefaultBundleProfileSupport();
        }
        return instance_;
    }

    protected DefaultBundleProfileSupport()
    {
    }


    /**
     * {@inheritDoc}
     */
    public PropertyInfo getBundleProfileIdXInfo(Context ctx)
    {
        PropertyInfo id = BundleProfileApiXInfo.BUNDLE_ID;
        
        LicenseMgr manager = (LicenseMgr) ctx.get(LicenseMgr.class);
        if (manager.isLicensed(ctx, BMLicenseConstants.BUNDLE_MANAGER_27))
        {
            id = BundleProfileApiXInfo.BUNDLE_ID;
        }
        
        return id;
    }


    /**
     * {@inheritDoc}
     */
    public PropertyInfo getBundleProfileNameXInfo(Context ctx)
    {
        PropertyInfo name = BundleProfileApiXInfo.NAME;
        
        LicenseMgr manager = (LicenseMgr) ctx.get(LicenseMgr.class);
        if (manager.isLicensed(ctx, BMLicenseConstants.BUNDLE_MANAGER_27))
        {
            name = BundleProfileApiXInfo.NAME;
        }
        
        return name;
    }


    /**
     * {@inheritDoc}
     */
    public PropertyInfo getBundleCategoryTypeXInfo(Context ctx)
    {
        PropertyInfo type = BundleProfileApiXInfo.BUNDLE_CATEGORY_ID;
        
        LicenseMgr manager = (LicenseMgr) ctx.get(LicenseMgr.class);
        if (manager.isLicensed(ctx, BMLicenseConstants.BUNDLE_MANAGER_27))
        {
            type = BundleProfileApiXInfo.BUNDLE_CATEGORY_ID;
        }
        
        return type;
    }


    /**
     * {@inheritDoc}
     */
    public PropertyInfo getBundleProfileSPIDXInfo(Context ctx)
    {
        PropertyInfo spid = BundleProfileApiXInfo.SPID;
        
        LicenseMgr manager = (LicenseMgr) ctx.get(LicenseMgr.class);
        if (manager.isLicensed(ctx, BMLicenseConstants.BUNDLE_MANAGER_27))
        {
            spid = BundleProfileApiXInfo.SPID;
        }
        
        return spid;
    }
    

    /**
     * {@inheritDoc}
     */
    public Set getUnitTypes(Context ctx, short enumIndex)
    {
        HashSet set = new HashSet();

        LicenseMgr manager = (LicenseMgr) ctx.get(LicenseMgr.class);
        if (manager.isLicensed(ctx, BMLicenseConstants.BUNDLE_MANAGER_27))
        {
            BundleTypeEnum bundleType = BundleTypeEnum.get(enumIndex);

            Iterator it = UnitTypeEnum.COLLECTION.iterator();
            while (it.hasNext())
            {
                UnitTypeEnum type = (UnitTypeEnum) it.next();
                if (type.getBundleType().equals(bundleType))
                {
                    set.add(type);
                }
            }
        }
        else
        {
            com.redknee.product.bundle.manager.api.v21.BundleTypeEnum bundleType = 
                com.redknee.product.bundle.manager.api.v21.BundleTypeEnum.get(enumIndex);

            Iterator it = com.redknee.product.bundle.manager.api.v21.UnitTypeEnum.COLLECTION.iterator();
            while (it.hasNext())
            {
                com.redknee.product.bundle.manager.api.v21.UnitTypeEnum type = 
                    (com.redknee.product.bundle.manager.api.v21.UnitTypeEnum) it.next();
                if (type.getBundleType().equals(bundleType))
                {
                    set.add(type);
                }
            }
        }
        return set;
    }
    

    /**
     * {@inheritDoc}
     */
    public String bucketTemplateToString(BundleProfile bucketTemplate)
    {
        String value =
                "[bundleid="
                + bucketTemplate.bundleId
                + "] [groupBundleId="
                + bucketTemplate.groupBundleId
                + "] [bundlePrice="
                + bucketTemplate.bundlePrice
                + "] [initialBalanceLimit="
                + bucketTemplate.initialBalanceLimit
                + "] [startDate="
                + printYearDateTime(bucketTemplate.startDate)
                + "] [expiryDate="
                + printYearDateTime(bucketTemplate.expiryDate)
                + "] [expiryPercent="
                + bucketTemplate.expiryPercent
                + "] [isCurrency="
                + bucketTemplate.isCurrency
                + "] [reportOnBillCycle="
                + bucketTemplate.reportOnBillingCycle
                + "] [reportOnBucketPurge="
                + bucketTemplate.reportOnBucketPurge
                + "] [reportOnBucketRemoval="
                + bucketTemplate.reportOnBucketRemoval
                + "] [validity="
                + printValidity(bucketTemplate.validity)
                + "] [RecurrenceValidity="
                + printValidity(bucketTemplate.recurrenceValidity)
                + "] [RelativeStartDatevalidity="
                + printValidity(bucketTemplate.relativeStartValidity)
                + "] [RelativeStartDateValidityTime="
                + printAbsoluteTime(bucketTemplate.relativeStartTime)
                + "] [RecurrenceValidityStartTime="
                + printAbsoluteTime(bucketTemplate.recurIntervalTime)
                + "] [name="
                + bucketTemplate.name
                + "] [enablePromotionProvision="
                + bucketTemplate.enablePromotionProvision
                + "] [enableWeightUnit="
                + bucketTemplate.enableWeightedUnit
                + "] [rolloverMax="
                + bucketTemplate.rolloverMax
                + "] [rolloverPercent="
                + bucketTemplate.rolloverPercent
                + "] [spid="
                + bucketTemplate.spid
                + "] [groupBundleId="
                + bucketTemplate.groupBundleId
                + "] [groupChargingScheme="
                + printGroupChargingScheme(bucketTemplate.groupChargingScheme)
                + "] [chargingPriority="
                + bucketTemplate.chargePriority
                + "] [Currency="
                + bucketTemplate.currencyType
                + "] [quotaScheme="
                + printQuotaScheme(bucketTemplate.quotaScheme)
                + "] [recurrenceScheme="
                + printRecurrenceScheme(bucketTemplate.recurrenceScheme)
                + "] [activationScheme="
                + printActivationScheme(bucketTemplate.activationScheme)
                + "] [expiryScheme="
                + printExpiryScheme(bucketTemplate.expiryScheme)
                + "]";
        return value;
    }

    private String printYearDateTime(YearDateTime time)
    {
        if(time == null)
        {
            return "null";
        }
        else
        {
            return "[Year:" + time.year + " Month:" + time.month + " Day:" + time.day + " Hour:" + time.hour + " Minutes:" + time.minute + "]";
        }
    }
    
    
    private String printValidity(Validity validity)
    {
        if(validity == null)
        {
            return "null";
        }
        else
        {
            return "[Validity:" + validity.validity + " Interval:" + printUnitType(validity.unit) + " IsPresent:" + validity.isPresent + "]";
        }
    }
    
    private String printUnitType(DurationType unit)
    {
        if(unit == null)
        {
            return "null";
        }
        else
        {
            switch(unit.value())
            {
                case DurationType._DAY:
                    return "DAY";
                case DurationType._MONTH:
                    return "MONTH";
                default:
                    return "Invalid Duration Type";
            }
        }
    }

    private String printAbsoluteTime(AbsoluteTime time)
    {
        if(time == null)
        {
            return "null";
        }
        else
        {
            return "[Hour:" + time.startHour + " Minute:" + time.startMinute + "]";
        }
    }
    
    private String printGroupChargingScheme(GroupChargingScheme scheme)
    {
        if(scheme == null)
        {
            return "null";
        }
        else
        {
            switch(scheme.value())
            {
                case GroupChargingScheme._GROUP_BUNDLE:
                    return "GROUP_BUNDLE";
                case GroupChargingScheme._MEMBER_BUNDLE:
                    return "MEMBER_BUNDLE";
                case GroupChargingScheme._GROUP_NOT_SUPPORTED:
                    return "GROUP_NOT_SUPPORTED";
                default:
                    return "Invalid GroupChargingScheme";
            }
        }
    }
    
    private String printQuotaScheme(QuotaScheme scheme)
    {
        if(scheme == null)
        {
            return "null";
        }
        else
        {
            switch(scheme.value())
            {
                case QuotaScheme._FIXED_QUOTA:
                    return "FIXED_QUOTA";
                case QuotaScheme._MOVING_QUOTA:
                    return "MOVING_QUOTA";
                case QuotaScheme._UNLIMITED_QUOTA:
                    return "UNLIMITED_QUOTA";
                default:
                    return "Invalid QuotaScheme";
            }
        }
    }

    private String printRecurrenceScheme(RecurrenceScheme scheme) {

        if(scheme == null)
        {
            return "null";
        }
        else
        {
            switch(scheme.value())
            {
                case RecurrenceScheme._ONE_OFF_FIXED_DATE_RANGE:
                    return "ONE_OFF_FIXED_DATE_RANGE";
                case RecurrenceScheme._ONE_OFF_FIXED_INTERVAL:
                    return "ONE_OFF_FIXED_INTERVAL";
                case RecurrenceScheme._RECUR_CYCLE_FIXED_DATETIME:
                    return "RECUR_CYCLE_FIXED_DATETIME";
                case RecurrenceScheme._RECUR_CYCLE_FIXED_INTERVAL:
                    return "RECUR_CYCLE_FIXED_INTERVAL";
                default:
                    return "Invalid RecurrenceScheme";
            }
        }
    }
    
    private String printActivationScheme(ActivationScheme scheme)
    {
        if(scheme == null)
        {
            return "null";
        }
        else
        {
            switch(scheme.value())
            {
                case ActivationScheme._ACTIVATE_ON_PROVISION:
                    return "ACTIVATE_ON_PROVISION";
                case ActivationScheme._SCHEDULED_ACTIVATION:
                    return "SCHEDULED_ACTIVATION";
                default:
                    return "Invalid ActivationScheme";
            }
        }
    }
    
    private String printExpiryScheme(ExpiryScheme scheme)
    {
        if(scheme == null)
        {
            return "null";
        }
        else
        {
            switch(scheme.value())
            {
                case ExpiryScheme._EXPIRE_AND_DELAY_PURGE:
                    return "EXPIRE_AND_DELAY_PURGE";
                case ExpiryScheme._NEVER_EXPIRE:
                    return "NEVER_EXPIRE";
                default:
                    return "Invalid RecurrenceScheme";
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public String paramSetToString(final Parameter[] inParamSet)
    {
        if (inParamSet == null || inParamSet.length == 0)
        {
            return "";
        }

        final StringBuilder strBldr = new StringBuilder();
        paramSetToString(strBldr, inParamSet);
        return strBldr.toString();
    }
    
    /**
     * {@inheritDoc}
     */
    public StringBuilder paramSetToString(final StringBuilder strBldr, final Parameter[] inParamSet)
    {
        final String delimiter = "|";
        
        if (inParamSet == null)
        {
            return strBldr;
        }

        strBldr.append(inParamSet.length);
        strBldr.append(delimiter);

        for (int i = 0; i < inParamSet.length; i++)
        {
            Parameter param = inParamSet[i];

            // Output ParamID
            strBldr.append(param.parameterID);
            strBldr.append(delimiter);

            // Output Parameter Type
            ParameterValueType pvt = param.value.discriminator();
            strBldr.append(pvt.value());
            strBldr.append(delimiter);

            // Output Parameter itself
            switch (pvt.value())
            {
                case ParameterValueType._PARAM_BOOLEAN :
                    strBldr.append(param.value.booleanValue());
                    break;

                case ParameterValueType._PARAM_BYTE_STREAM :
                    strBldr.append(Arrays.toString(param.value.byteArrayValue()));
                    break;

                case ParameterValueType._PARAM_DOUBLE :
                    strBldr.append(param.value.doubleValue());
                    break;

                case ParameterValueType._PARAM_FLOAT :
                    strBldr.append(param.value.floatValue());
                    break;

                case ParameterValueType._PARAM_INT :
                    strBldr.append(param.value.intValue());
                    break;

                case ParameterValueType._PARAM_LONG :
                    strBldr.append(param.value.longValue());
                    break;

                case ParameterValueType._PARAM_STRING :
                    strBldr.append(param.value.stringValue());
                    break;
                    
                case ParameterValueType._PARAM_SHORT :
                    strBldr.append(param.value.shortValue());
                    break;  
            }

            // Terminating delimiter
            strBldr.append(delimiter);
        }

        return strBldr;
    }

    /**
     * {@inheritDoc}
     */
    public String paramSetArrayToString(final Parameter[][] inParamSet)
    {
        if (inParamSet == null || inParamSet.length == 0)
        {
            return "";
        }

        final StringBuilder strBldr = new StringBuilder();
        paramSetArrayToString(strBldr, inParamSet);
        return strBldr.toString();
    }

    /**
     * {@inheritDoc}
     */
    public StringBuilder paramSetArrayToString(final StringBuilder strBldr, final Parameter[][] inParamSet)
    {
        final String rowDelimiter = ", ";
        for (int i = 0; i < inParamSet.length; i++)
        {
            paramSetToString(strBldr, inParamSet[i]);
            strBldr.append(rowDelimiter);
        }

        return strBldr;
    }

}
