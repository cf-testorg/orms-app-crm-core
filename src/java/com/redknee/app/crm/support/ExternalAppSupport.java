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

import com.redknee.app.crm.bean.ServiceBase;
import com.redknee.app.crm.bean.externalapp.ExternalAppEnum;
import com.redknee.framework.xhome.context.Context;


/**
 * 
 *
 * @author Marcio Marques
 * @since 9.1.3
 */
public interface ExternalAppSupport extends Support
{
    public static int NO_CONNECTION = -1;
    public static int REMOTE_EXCEPTION = -2;
    public static int UNKNOWN = -3;
    public static int COMMUNICATION_FAILURE = 301;
    
    public static int BSS_DATABASE_FAILURE_SUBSCRIPTION_RETRIEVAL = 10;
    
    public static int BSS_DATABASE_FAILURE_ACCOUNT_RETRIEVAL = 20;
    public static int BSS_DATABASE_FAILURE_ACCOUNT_UPDATE = 21;
    
    public static int BSS_DATABASE_FAILURE_MSISDN_RETRIEVAL = 30;
    public static int BSS_DATABASE_FAILURE_MSISDN_UPDATE = 31;
    public static int BSS_DATABASE_FAILURE_MSISDN_CLAIM = 32;
    public static int BSS_DATABASE_FAILURE_MSISDN_RELEASE = 33;
    public static int BSS_DATABASE_FAILURE_MSISDN_ASSOCIATION = 34;
    public static int BSS_DATABASE_FAILURE_MSISDN_DEASSOCIATION = 35;
    public static int BSS_DATABASE_FAILURE_MSISDN_HISTORY_UPDATE = 36;

    public static int BSS_DATABASE_FAILURE_PACKAGE_RETRIEVAL = 40;
    public static int BSS_DATABASE_FAILURE_PACKAGE_UPDATE_IN_USE = 41;
    public static int BSS_DATABASE_FAILURE_PACKAGE_UPDATE_HELD = 42;
    public static int BSS_DATABASE_FAILURE_PACKAGE_NOT_IN_USE_VALIDATION = 41;
    public static int BSS_DATABASE_FAILURE_IMSI_HISTORY_UPDATE = 42;

    public static int BSS_DATABASE_FAILURE_AUXILIARY_SERVICE_RETRIEVAL = 50;
    public static int BSS_DATABASE_FAILURE_AUXILIARY_SERVICE_CREATION = 51;
    public static int BSS_DATABASE_FAILURE_AUXILIARY_SERVICE_UPDATE = 52;
    public static int BSS_DATABASE_FAILURE_AUXILIARY_SERVICE_REMOVAL = 53;

    public static int BSS_DATABASE_FAILURE_SUBSCRIPTION_HOMEZONE_RETRIEVAL = 60;
    public static int BSS_DATABASE_FAILURE_SUBSCRIPTION_HOMEZONE_CREATION = 61;
    public static int BSS_DATABASE_FAILURE_SUBSCRIPTION_HOMEZONE_UPDATE = 62;
    public static int BSS_DATABASE_FAILURE_SUBSCRIPTION_HOMEZONE_REMOVAL = 63;
    
    public static int BSS_DATABASE_FAILURE_SUBSCRIPTION_HOMEZONE_COUNT_RETRIEVAL = 65;
    public static int BSS_DATABASE_FAILURE_SUBSCRIPTION_HOMEZONE_COUNT_UPDATE = 66;

    public static int BSS_DATABASE_FAILURE_EXTENSION_REMOVAL = 70;

    public static int BSS_DATABASE_FAILURE_CUSTOMER_SWAP_RETRIEVAL = 80;
    public static int BSS_DATABASE_FAILURE_FEATURE_MODIFICATION_RETRIEVAL = 81;
    
    public int getResultCode(Context ctx, ExternalAppEnum externalApp);
    
    public String getErrorCodeMessage(Context ctx, ExternalAppEnum externalApp, int errorCode);
    
    public String getProvisionErrorMessage(Context ctx, ExternalAppEnum externalApp, int errorCode, ServiceBase service);

    public String getUnprovisionErrorMessage(Context ctx, ExternalAppEnum externalApp, int errorCode, ServiceBase service);

    public String getSuspensionErrorMessage(Context ctx, ExternalAppEnum externalApp, int errorCode, ServiceBase service);

    public String getResumeErrorMessage(Context ctx, ExternalAppEnum externalApp, int errorCode, ServiceBase service);
    
    public void addExternalAppResultCodes(Context ctx);
    
    public void addExternalAppErrorCodeMessages(Context ctx);
}
