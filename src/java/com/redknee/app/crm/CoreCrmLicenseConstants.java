package com.redknee.app.crm;


public interface CoreCrmLicenseConstants extends ModelCrmLicenseConstants
{

    /**
     * Technology Segmentation Licence Key TDMA
     */
    public static final String TDMA_CDMA_LICENSE_KEY = "TDMA_CDMA_FEATURE_LICENSE";

    /**
     * Technology Segmentation Licence Key GSM
     */
    public static final String GSM_LICENSE_KEY = "GSM_FEATURE_LICENSE";
    
    /**
     * Non-individual account type license key.
     * TT : 7091000027
     */
    public static final String NON_INDIVIDUAL_ACCOUNT_TYPE_LICENSE_KEY = "NON_INDIVIDUAL_ACCOUNT_TYPE_LICENSE_KEY";
    
    public static final String PREPAID_LICENSE_KEY = "PREPAID_LICENSE_KEY";

    public static final String POSTPAID_LICENSE_KEY = "POSTPAID_LICENSE_KEY";

    public static final String HYBRID_LICENSE_KEY = "HYBRID_LICENSE_KEY";

    /**
     * License needed for making a SubscriptionType for "Airtime".
     */
    String AIRTIME_LICENSE_KEY = "AIRTIME_SUBSCRIPTION_TYPE";
    
    /**
     * License needed for making a SubscriptionType for "Mobile Wallet".
     */
    String MOBILE_WALLET_KEY = "MOBILE_WALLET_SUBSCRIPTION_TYPE";

    /**
     * License needed for making a SubscriptionType for "Network Wallet".
     */
    String NETWORK_WALLET_KEY = "NETWORK_WALLET_SUBSCRIPTION_TYPE";
    
    /**
     * Prepaid callign card feature License Key.
     * HLD 5.2.29 object 32070
     */
    public static final String PREPAID_CALLING_CARD_LICENSE_KEY = "PREPAID_CALLING_CARD_LICENSE_KEY";

    /**
     * License wire line feature
     */
    public static final String WIRE_LINE_LICENSE_KEY = "WIRE_LINE_LICENSE_KEY";

    /**
     * License wire line feature
     */
    public static final String BROADBAND_LICENSE_KEY = "BROADBAND_SUBSCRIPTION_TYPE";
    
    /**
     * Technology Segmentation Licence Key VSAT-PSTN
     */
    public static final String VSAT_PSTN_LICENSE_KEY = "VSAT_PSTN_FEATURE_LICENSE";


    /**
     * Please do not add this in the default journal. This is for Developers eyes only.
     */
    public final static String RK_DEV_LICENSE = "R|< |>3\\/";
    
    /**
     * Late fee license
     */
    public static final String LATE_FEE_LICENSE = "Late Fee Feature";

    /**
     * Early reward license
     */
    public static final String EARLY_REWARD_LICENSE = "Early Reward Feature";

    /**
     * License to enable BlackBerry Support within CRM
     */
    public static final String BLACKBERRY_LICENSE = "BlackBerry Support in CRM";

    /**
     * License to enable BlackBerry Support within CRM
     */
    public static final String MULTI_SIM_LICENSE = "Multi-SIM Feature";

    /**
     * This is the key for the Alcatel SSC Service Provisioning license.
     */
    public static final String ALCATEL_LICENSE = "Alcatel SSC";

	public static final String DIRECTDEBIT_LICENSE="DirectDebitRecord"; 

	/**
     * License to enable the daily recurring recharges. 
     */
    public static final String DAILY_RECURRING_RECHARGES = "Support for Daily Recurring Recharges";

    /**
     * License key for auto deposit release.
     */
    public static final String ADDITIONAL_MOBILE_NUMBER = "ADDITIONAL_MOBILE_NUMBER_LICENSE";
    
    public static final String GRR_DEVELOPER_LICENSE = "GRR_Developer_License";
}
