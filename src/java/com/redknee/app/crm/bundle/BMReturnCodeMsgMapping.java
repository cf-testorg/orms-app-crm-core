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
package com.redknee.app.crm.bundle;

import java.util.HashMap;
import java.util.Map;

/**
 * HashMap to store all the message returned by BM application during
 * Bundle profile creation process.
 *
 * @author marcio.marques@redknee.com
 */
public final class BMReturnCodeMsgMapping
{
    private static final String UNKNOWN_ERROR_MSG = "Unknown error code";
    
    private static final Map<Integer,String> errorMsgMap_ = new HashMap<Integer,String>();
    private static final Map<Integer,String> bucketErrorMsgMap_ = new HashMap<Integer,String>();
    
    // com.redknee.product.bundle.manager.provision.bundle.error.ErroCode
    // Commonly Used Errors
    private static final String SUBSCRIBER_NOT_FOUND = "Subscriber not found";
    private static final String SQL_ERROR = "SQL error occurred";
    private static final String INTERNAL_ERROR = "Internal error occurred";
    private static final String BAD_PARAMETER = "Bad parameter informed";

    // invalid input parameters format
    private static final String  MAX_OPS_EXCEEDED = "Maximum number of operators exceeded";

    // Bundle Profile Provisioning Errors
    private static final String INVALID_SPID = "Invalid SPID informed";
    private static final String BUNDLE_IN_USE = "Bundle is in use";
    private static final String CATEGORY_NOT_FOUND = "Category not found";
    private static final String BUNDLE_NOT_FOUND = "Bundle not found";
    private static final String INVALID_UNIT_TYPE = "Invalid unit type informed";
    private static final String CATEGORY_ALREADY_EXIST = "Category already exists";
    private static final String CROSS_SERVICE_REQUIRED = "Cross service required";

    // when category unit type is CORSS_UNIT, all bundles must enableWeighedUnit
    private static final String CROSS_SERVICE_DISALLOWED = "Cross service not allowed";
    private static final String START_PAST_EXPIRY = "Start date greater than end date";

    // when start time is greater when expiry time
    private static final String VALIDITY_REQUIRED = "Number of units required";
    private static final String VALIDITY_DISALLOWED = "Number of units not allowed";
    private static final String START_TIME_REQUIRED = "Start date required";
    private static final String START_TIME_DISALLOWED = "Start date not allowed";
    private static final String EXPIRY_TIME_REQUIRED = "End date required";
    private static final String EXPIRY_TIME_DISALLOWED = "End date not allowed";
    private static final String UNLIMITED_QUOTA_NEVER_EXPIRY_DISALLOWED = "Never expiry not allowed for Unlimited Quota";
    private static final String UNIT_CURRENCY_COEXIST_DISALLOWED = "Unit currency coexist not allowed";
    private static final String SCHEDULED_ACTIVATION_DISSALLOWED = "Scheduled activation not allowed";
    private static final String MOVING_QUOTA_INVALID_RECURRENCE_SCHEME = "Invalid recurrence scheme for Moving Quota";
    private static final String MOVING_QUOTA_INVALID_GROUP_CHARGING_SCHEME = "Invalid group charging scheme for Moving Quota";
    private static final String MOVING_QUOTA_INVALID_CHARGING_SCHEME = "Invalid charging scheme for Moving Quota";
    private static final String INVALID_UNIT_TYPE_ASSIGNMENT = "Unit type is invalid for this type of bundle";
    private static final String EMPTY_CATEGORY_SEQUENCE = "Empty category sequence";
    private static final String SEQUENCE_ALREADY_EXIST = "Sequence already exists";
    private static final String SEQUENCE_NOT_FOUND = "Sequence not found";
    private static final String INVALID_SERVICE_CLASS = "Invalid service class";
    private static final String CATEGORY_IN_USE = "Category in use";
    

    
    // com.redknee.product.bundle.manager.provision.bucket.error.ErroCode
    
    // Generic Errors
    private static final String BUCKET_OPERATION_NOT_SUPPORTED = "Operation not supported";

    // Commonly Used Errors
    private static final String BUCKET_SUBSCRIBER_NOT_FOUND = "Subscriber not found";
    private static final String BUCKET_SQL_ERROR = "SQL error";
    private static final String BUCKET_INTERNAL_ERROR = "Internal error";
    private static final String BUCKET_BAD_PARAMETER = "Bad parameter informed";

    // invalid input parameters format
    private static final String BUCKET_MAX_OPS_EXCEEDED = "Maximum number of operators exceeded";

    // Bunde Profile Provisiong Errors
    private static final String BUCKET_INVALID_SPID = "Invalid SPID";
    private static final String BUCKET_BUNDLE_NOT_FOUND = "Bundle not found";
    private static final String BUCKET_MEMBER_GROUP_VIOLATION = "Member group violation";
    private static final String BUCKET_BUCKET_NOT_FOUND = "Bucket not found";
    private static final String BUCKET_MAX_AMT_EXCEEDED = "Maximum amount exceeded";

    // when switching bundle
    private static final String BUCKET_MULTIPLE_BUNDLE_DISALLOWED = "Multiple bundles not allowed";
    private static final String BUCKET_EXPIRY_TIME_REQUIRED = "Expiry date required";

    // when bundle is UNLIMITED_QUOTA, expiry time is required
    private static final String BUCKET_EXPIRY_TIME_DISALLOWED = "Expiry date not allowed";

    // when bundle is NEVER_EXPIRED, expiry time cannot be provisoined
    private static final String BUCKET_BILLING_DATE_REQUIRED = "Billing date required";

    // when bundle is filxed billing cycle, billing date is required
    private static final String BUCKET_BILLING_DATE_DISALLOWED = "Billing date not allowed";

    // when bundle is not filxed billing cycle, billing date cannot be provisoined
    private static final String BUCKET_SWITCH_BUNDLE_DISALLOWED = "Switch bundles operation not allowed";

    // when attempt to switch bundle that is not fixed billing cycle or when they are not the same category
    private static final String BUCKET_BUCKET_ALREADY_EXIST = "Bucket already exists";
    private static final String BUCKET_TOPUP_BUCKET_ALREADY_EXISTS = "Topup bucket already exists";
    private static final String BUCKET_UNIT_CURRENCY_COEXIST_DISALLOWED = "Unit currency coexist not allowed";
    private static final String BUCKET_CATEGORY_NOT_FOUND = "Category not found";    
    
    static
    {
        bucketErrorMsgMap_.put(Integer.valueOf(com.redknee.product.bundle.manager.provision.v5_0.bucket.error.ErrorCode.OPERATION_NOT_SUPPORTED), BUCKET_OPERATION_NOT_SUPPORTED);
        bucketErrorMsgMap_.put(Integer.valueOf(com.redknee.product.bundle.manager.provision.v5_0.bucket.error.ErrorCode.SUBSCRIBER_NOT_FOUND), BUCKET_SUBSCRIBER_NOT_FOUND);
        bucketErrorMsgMap_.put(Integer.valueOf(com.redknee.product.bundle.manager.provision.v5_0.bucket.error.ErrorCode.SQL_ERROR), BUCKET_SQL_ERROR);
        bucketErrorMsgMap_.put(Integer.valueOf(com.redknee.product.bundle.manager.provision.v5_0.bucket.error.ErrorCode.INTERNAL_ERROR), BUCKET_INTERNAL_ERROR);
        bucketErrorMsgMap_.put(Integer.valueOf(com.redknee.product.bundle.manager.provision.v5_0.bucket.error.ErrorCode.BAD_PARAMETER), BUCKET_BAD_PARAMETER);
        bucketErrorMsgMap_.put(Integer.valueOf(com.redknee.product.bundle.manager.provision.v5_0.bucket.error.ErrorCode.MAX_OPS_EXCEEDED), BUCKET_MAX_OPS_EXCEEDED);
        
        bucketErrorMsgMap_.put(Integer.valueOf(com.redknee.product.bundle.manager.provision.v5_0.bucket.error.ErrorCode.INVALID_SPID), BUCKET_INVALID_SPID);
        bucketErrorMsgMap_.put(Integer.valueOf(com.redknee.product.bundle.manager.provision.v5_0.bucket.error.ErrorCode.BUNDLE_NOT_FOUND), BUCKET_BUNDLE_NOT_FOUND);
        bucketErrorMsgMap_.put(Integer.valueOf(com.redknee.product.bundle.manager.provision.v5_0.bucket.error.ErrorCode.MEMBER_GROUP_VIOLATION), BUCKET_MEMBER_GROUP_VIOLATION);
        bucketErrorMsgMap_.put(Integer.valueOf(com.redknee.product.bundle.manager.provision.v5_0.bucket.error.ErrorCode.BUCKET_NOT_FOUND), BUCKET_BUCKET_NOT_FOUND);
        bucketErrorMsgMap_.put(Integer.valueOf(com.redknee.product.bundle.manager.provision.v5_0.bucket.error.ErrorCode.MAX_AMT_EXCEEDED), BUCKET_MAX_AMT_EXCEEDED);

        bucketErrorMsgMap_.put(Integer.valueOf(com.redknee.product.bundle.manager.provision.v5_0.bucket.error.ErrorCode.MULTIPLE_BUNDLE_DISALLOWED), BUCKET_MULTIPLE_BUNDLE_DISALLOWED);
        bucketErrorMsgMap_.put(Integer.valueOf(com.redknee.product.bundle.manager.provision.v5_0.bucket.error.ErrorCode.EXPIRY_TIME_REQUIRED), BUCKET_EXPIRY_TIME_REQUIRED);

        bucketErrorMsgMap_.put(Integer.valueOf(com.redknee.product.bundle.manager.provision.v5_0.bucket.error.ErrorCode.EXPIRY_TIME_DISALLOWED), BUCKET_EXPIRY_TIME_DISALLOWED);

        bucketErrorMsgMap_.put(Integer.valueOf(com.redknee.product.bundle.manager.provision.v5_0.bucket.error.ErrorCode.BILLING_DATE_REQUIRED), BUCKET_BILLING_DATE_REQUIRED);

        bucketErrorMsgMap_.put(Integer.valueOf(com.redknee.product.bundle.manager.provision.v5_0.bucket.error.ErrorCode.BILLING_DATE_DISALLOWED), BUCKET_BILLING_DATE_DISALLOWED);

        bucketErrorMsgMap_.put(Integer.valueOf(com.redknee.product.bundle.manager.provision.v5_0.bucket.error.ErrorCode.SWITCH_BUNDLE_DISALLOWED), BUCKET_SWITCH_BUNDLE_DISALLOWED);

        bucketErrorMsgMap_.put(Integer.valueOf(com.redknee.product.bundle.manager.provision.v5_0.bucket.error.ErrorCode.BUCKET_ALREADY_EXIST), BUCKET_BUCKET_ALREADY_EXIST);
        bucketErrorMsgMap_.put(Integer.valueOf(com.redknee.product.bundle.manager.provision.v5_0.bucket.error.ErrorCode.TOPUP_BUCKET_ALREADY_EXISTS), BUCKET_TOPUP_BUCKET_ALREADY_EXISTS);
        bucketErrorMsgMap_.put(Integer.valueOf(com.redknee.product.bundle.manager.provision.v5_0.bucket.error.ErrorCode.UNIT_CURRENCY_COEXIST_DISALLOWED), BUCKET_UNIT_CURRENCY_COEXIST_DISALLOWED);
        bucketErrorMsgMap_.put(Integer.valueOf(com.redknee.product.bundle.manager.provision.v5_0.bucket.error.ErrorCode.CATEGORY_NOT_FOUND), BUCKET_CATEGORY_NOT_FOUND);

        errorMsgMap_.put(Integer.valueOf(com.redknee.product.bundle.manager.provision.v5_0.bundle.error.ErrorCode.SUBSCRIBER_NOT_FOUND), SUBSCRIBER_NOT_FOUND);
        errorMsgMap_.put(Integer.valueOf(com.redknee.product.bundle.manager.provision.v5_0.bundle.error.ErrorCode.SQL_ERROR), SQL_ERROR);
        errorMsgMap_.put(Integer.valueOf(com.redknee.product.bundle.manager.provision.v5_0.bundle.error.ErrorCode.INTERNAL_ERROR), INTERNAL_ERROR);
        errorMsgMap_.put(Integer.valueOf(com.redknee.product.bundle.manager.provision.v5_0.bundle.error.ErrorCode.BAD_PARAMETER), BAD_PARAMETER);

        errorMsgMap_.put(Integer.valueOf(com.redknee.product.bundle.manager.provision.v5_0.bundle.error.ErrorCode.MAX_OPS_EXCEEDED), MAX_OPS_EXCEEDED);

        errorMsgMap_.put(Integer.valueOf(com.redknee.product.bundle.manager.provision.v5_0.bundle.error.ErrorCode.INVALID_SPID), INVALID_SPID);
        errorMsgMap_.put(Integer.valueOf(com.redknee.product.bundle.manager.provision.v5_0.bundle.error.ErrorCode.BUNDLE_IN_USE), BUNDLE_IN_USE);
        errorMsgMap_.put(Integer.valueOf(com.redknee.product.bundle.manager.provision.v5_0.bundle.error.ErrorCode.CATEGORY_NOT_FOUND), CATEGORY_NOT_FOUND);
        errorMsgMap_.put(Integer.valueOf(com.redknee.product.bundle.manager.provision.v5_0.bundle.error.ErrorCode.BUNDLE_NOT_FOUND), BUNDLE_NOT_FOUND);
        errorMsgMap_.put(Integer.valueOf(com.redknee.product.bundle.manager.provision.v5_0.bundle.error.ErrorCode.INVALID_UNIT_TYPE), INVALID_UNIT_TYPE);
        errorMsgMap_.put(Integer.valueOf(com.redknee.product.bundle.manager.provision.v5_0.bundle.error.ErrorCode.CATEGORY_ALREADY_EXIST), CATEGORY_ALREADY_EXIST);
        errorMsgMap_.put(Integer.valueOf(com.redknee.product.bundle.manager.provision.v5_0.bundle.error.ErrorCode.CROSS_SERVICE_REQUIRED), CROSS_SERVICE_REQUIRED);
        
        errorMsgMap_.put(Integer.valueOf(com.redknee.product.bundle.manager.provision.v5_0.bundle.error.ErrorCode.CROSS_SERVICE_DISALLOWED), CROSS_SERVICE_DISALLOWED);
        errorMsgMap_.put(Integer.valueOf(com.redknee.product.bundle.manager.provision.v5_0.bundle.error.ErrorCode.START_PAST_EXPIRY), START_PAST_EXPIRY);

        errorMsgMap_.put(Integer.valueOf(com.redknee.product.bundle.manager.provision.v5_0.bundle.error.ErrorCode.VALIDITY_REQUIRED), VALIDITY_REQUIRED);
        errorMsgMap_.put(Integer.valueOf(com.redknee.product.bundle.manager.provision.v5_0.bundle.error.ErrorCode.VALIDITY_DISALLOWED), VALIDITY_DISALLOWED);
        errorMsgMap_.put(Integer.valueOf(com.redknee.product.bundle.manager.provision.v5_0.bundle.error.ErrorCode.START_TIME_REQUIRED), START_TIME_REQUIRED);
        errorMsgMap_.put(Integer.valueOf(com.redknee.product.bundle.manager.provision.v5_0.bundle.error.ErrorCode.START_TIME_DISALLOWED), START_TIME_DISALLOWED);
        errorMsgMap_.put(Integer.valueOf(com.redknee.product.bundle.manager.provision.v5_0.bundle.error.ErrorCode.EXPIRY_TIME_REQUIRED), EXPIRY_TIME_REQUIRED);
        errorMsgMap_.put(Integer.valueOf(com.redknee.product.bundle.manager.provision.v5_0.bundle.error.ErrorCode.EXPIRY_TIME_DISALLOWED), EXPIRY_TIME_DISALLOWED);
        errorMsgMap_.put(Integer.valueOf(com.redknee.product.bundle.manager.provision.v5_0.bundle.error.ErrorCode.UNLIMITED_QUOTA_NEVER_EXPIRY_DISALLOWED), UNLIMITED_QUOTA_NEVER_EXPIRY_DISALLOWED);
        errorMsgMap_.put(Integer.valueOf(com.redknee.product.bundle.manager.provision.v5_0.bundle.error.ErrorCode.UNIT_CURRENCY_COEXIST_DISALLOWED), UNIT_CURRENCY_COEXIST_DISALLOWED);
        errorMsgMap_.put(Integer.valueOf(com.redknee.product.bundle.manager.provision.v5_0.bundle.error.ErrorCode.SCHEDULED_ACTIVATION_DISSALLOWED), SCHEDULED_ACTIVATION_DISSALLOWED);
        errorMsgMap_.put(Integer.valueOf(com.redknee.product.bundle.manager.provision.v5_0.bundle.error.ErrorCode.MOVING_QUOTA_INVALID_RECURRENCE_SCHEME), MOVING_QUOTA_INVALID_RECURRENCE_SCHEME);
        errorMsgMap_.put(Integer.valueOf(com.redknee.product.bundle.manager.provision.v5_0.bundle.error.ErrorCode.MOVING_QUOTA_INVALID_GROUP_CHARGING_SCHEME), MOVING_QUOTA_INVALID_GROUP_CHARGING_SCHEME);
        errorMsgMap_.put(Integer.valueOf(com.redknee.product.bundle.manager.provision.v5_0.bundle.error.ErrorCode.MOVING_QUOTA_INVALID_CHARGING_SCHEME), MOVING_QUOTA_INVALID_CHARGING_SCHEME);
        errorMsgMap_.put(Integer.valueOf(com.redknee.product.bundle.manager.provision.v5_0.bundle.error.ErrorCode.INVALID_UNIT_TYPE_ASSIGNMENT), INVALID_UNIT_TYPE_ASSIGNMENT);
        errorMsgMap_.put(Integer.valueOf(com.redknee.product.bundle.manager.provision.v5_0.bundle.error.ErrorCode.EMPTY_CATEGORY_SEQUENCE), EMPTY_CATEGORY_SEQUENCE);
        errorMsgMap_.put(Integer.valueOf(com.redknee.product.bundle.manager.provision.v5_0.bundle.error.ErrorCode.SEQUENCE_ALREADY_EXIST), SEQUENCE_ALREADY_EXIST);
        errorMsgMap_.put(Integer.valueOf(com.redknee.product.bundle.manager.provision.v5_0.bundle.error.ErrorCode.SEQUENCE_NOT_FOUND), SEQUENCE_NOT_FOUND);
        errorMsgMap_.put(Integer.valueOf(com.redknee.product.bundle.manager.provision.v5_0.bundle.error.ErrorCode.INVALID_SERVICE_CLASS), INVALID_SERVICE_CLASS);
        errorMsgMap_.put(Integer.valueOf(com.redknee.product.bundle.manager.provision.v5_0.bundle.error.ErrorCode.CATEGORY_IN_USE), CATEGORY_IN_USE);

    
    }

    /**
     * Returns the predefined error message default
     * associated with an error code.
     * @param ctx the operating context
     * @param errCode the error code for which to return the message
     * @return the error message
     */
    public static String getMessage(final int errCode)
    {
        String msg = errorMsgMap_.get(Integer.valueOf(errCode));

        if (msg != null)
        {
            return msg;
        }
        else
        {
            return UNKNOWN_ERROR_MSG;
        }
    }
    
    /**
     * Returns the predefined error message default
     * associated with an error code.
     * @param ctx the operating context
     * @param errCode the error code for which to return the message
     * @return the error message
     */
    public static String getBucketMessage(final int errCode)
    {
        String msg = bucketErrorMsgMap_.get(Integer.valueOf(errCode));

        if (msg != null)
        {
            return msg;
        }
        else
        {
            return UNKNOWN_ERROR_MSG;
        }
    }

    /**
     * This prevents instantiation of this class.
     */
    private BMReturnCodeMsgMapping()
    {
    }
}
