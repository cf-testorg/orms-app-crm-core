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
package com.redknee.app.crm.bean.webcontrol;

import com.redknee.framework.xhome.webcontrol.FinalWebControl;
import com.redknee.framework.xhome.webcontrol.WebControl;
import com.redknee.framework.xhome.xenum.Enum;
import com.redknee.framework.xhome.xenum.EnumCollection;

import com.redknee.app.crm.web.control.LicenceAwareEnumWebControl;
import com.redknee.app.crm.xenum.LicenseAwareEnumProxy;

import com.redknee.app.crm.bundle.ActivationTypeEnum;
import com.redknee.app.crm.bundle.BundleProfileWebControl;
import com.redknee.app.crm.bundle.ExpiryTypeEnum;
import com.redknee.app.crm.bundle.GroupChargingTypeEnum;
import com.redknee.app.crm.bundle.QuotaTypeEnum;
import com.redknee.app.crm.bundle.RecurrenceTypeEnum;
import com.redknee.app.crm.bundle.license.BMLicenseConstants;
import com.redknee.app.crm.bundle.web.QuotaSchemeWebControl;
import com.redknee.app.crm.bundle.web.SegmentWebControl;


/**
 * 
 * 
 * @author aaron.gourley@redknee.com
 * @since
 */
public class CustomBundleProfileWebControl extends BundleProfileWebControl
{

    /**
     * {@inheritDoc}
     */
    @Override
    public WebControl getGLCodeWebControl()
    {
        return CustomAdjustmentInfoWebControl.CUSTOM_GL_CODE_WC;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public WebControl getActivationSchemeWebControl()
    {
        return CUSTOM_ACTIVATION_SCHEME_WC;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public WebControl getGroupChargingSchemeWebControl()
    {
        return CUSTOM_GROUP_CHARGING_SCHEME_WC;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public WebControl getInitialBalanceLimitWebControl()
    {
        return CUSTOM_INITIAL_BALANCE_LIMIT_WC;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public WebControl getQuotaSchemeWebControl()
    {
        return CUSTOM_QUOTA_SCHEME_WC;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public WebControl getSegmentWebControl()
    {
        return CUSTOM_SEGMENT_WC;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public WebControl getExpirySchemeWebControl()
    {
        return CUSTOM_EXPIRY_SCHEME_WC;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public WebControl getRecurrenceSchemeWebControl()
    {
        return CUSTOM_RECURRENCE_SCHEME_WC;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public WebControl getRolloverMaxWebControl()
    {
        return CUSTOM_ROLLOVER_MAX_WC;
    }

    public static final WebControl CUSTOM_SEGMENT_WC = new FinalWebControl(new SegmentWebControl(true));
    public static final WebControl CUSTOM_QUOTA_SCHEME_WC = new FinalWebControl(new QuotaSchemeWebControl(new Enum[]
        {QuotaTypeEnum.UNLIMITED_QUOTA, QuotaTypeEnum.FIXED_QUOTA, QuotaTypeEnum.MOVING_QUOTA}, new Enum[]
        {QuotaTypeEnum.FIXED_QUOTA, QuotaTypeEnum.MOVING_QUOTA}, new Enum[]
        {QuotaTypeEnum.MOVING_QUOTA}, true));
	public static final WebControl CUSTOM_INITIAL_BALANCE_LIMIT_WC =
	    new com.redknee.app.crm.bundle.web.MapUnitWebControl(
            22);
    public static final WebControl CUSTOM_GROUP_CHARGING_SCHEME_WC = new LicenceAwareEnumWebControl(new EnumCollection(
            new Enum[]
                {
                        GroupChargingTypeEnum.GROUP_NOT_SUPPORTED,
                        new LicenseAwareEnumProxy(GroupChargingTypeEnum.MEMBER_BUNDLE,
                                BMLicenseConstants.GROUP_CHARGING_SCHEME_GROUP_SUPPORT),
                        new LicenseAwareEnumProxy(GroupChargingTypeEnum.GROUP_BUNDLE,
                                BMLicenseConstants.GROUP_CHARGING_SCHEME_GROUP_SUPPORT)}), true);
	public static final WebControl CUSTOM_ROLLOVER_MAX_WC =
	    new com.redknee.app.crm.bundle.web.MapUnitWebControl(22);
    public static final WebControl CUSTOM_ACTIVATION_SCHEME_WC = new FinalWebControl(new LicenceAwareEnumWebControl(
            new EnumCollection(new Enum[]
                {
                        new LicenseAwareEnumProxy(ActivationTypeEnum.ACTIVATE_ON_PROVISION,
                                BMLicenseConstants.ACTIVATION_SCHEME_ACTIVE_ON_PROVISION),
                        new LicenseAwareEnumProxy(ActivationTypeEnum.SCHEDULED_ACTIVATION,
                                BMLicenseConstants.ACTIVATION_SCHEME_SCHEDULED_ACTIVATION),
                        new LicenseAwareEnumProxy(ActivationTypeEnum.FIRST_CALL_ACTIVATION,
                                BMLicenseConstants.ACTIVATION_SCHEME_FIRST_CALL_ACTIVATION)}), true));
    public static final WebControl CUSTOM_EXPIRY_SCHEME_WC = new FinalWebControl(new LicenceAwareEnumWebControl(
            new EnumCollection(new Enum[]
                {
                        new LicenseAwareEnumProxy(ExpiryTypeEnum.NEVER_EXPIRE,
                                BMLicenseConstants.EXPIRY_SCHEME_NEVER_EXPIRE),
                        new LicenseAwareEnumProxy(ExpiryTypeEnum.EXPIRE_AND_DELAY_PURGE,
                                BMLicenseConstants.EXPIRY_SCHEME_EXPIRE_AND_DELAY_PURGE)}), true));
    public static final WebControl CUSTOM_RECURRENCE_SCHEME_WC = new FinalWebControl(new LicenceAwareEnumWebControl(
            new EnumCollection(new Enum[]
                {
                        new LicenseAwareEnumProxy(RecurrenceTypeEnum.RECUR_CYCLE_FIXED_DATETIME,
                                BMLicenseConstants.RECUR_SCHEME_RECURRING_FIXED),
                        new LicenseAwareEnumProxy(RecurrenceTypeEnum.RECUR_CYCLE_FIXED_INTERVAL,
                                BMLicenseConstants.RECUR_SCHEME_RECURRING_INTERVAL),
                        new LicenseAwareEnumProxy(RecurrenceTypeEnum.ONE_OFF_FIXED_DATE_RANGE,
                                BMLicenseConstants.RECUR_SCHEME_ONEOFF_FIXED),
                        new LicenseAwareEnumProxy(RecurrenceTypeEnum.ONE_OFF_FIXED_INTERVAL,
                                BMLicenseConstants.RECUR_SCHEME_ONEOFF_INTERVAL)}), true));
}
