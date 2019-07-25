package com.redknee.app.crm;



public interface CoreCrmConstants extends ModelCrmConstants
{
    /** The plain transaction home key for discount transactions*/
    public static final String DISCOUNT_PLAIN_TXN_HOME = "DiscountPlainTxnHome";
    /**
     * The context key for the full transaction home. This is a hack to fix payment plan
     * loan reversal not being generated when an account on payment plan is dunned due to
     * payment.
     */
    public static final String FULL_TRANSACTION_HOME = "Full Transaction Home";

    /** OM name */
    public static final String OM_CRM_PROV_ERROR = "ECare_Prov_Error";

    /** OM name */
    public static final String OM_CRM_UNPROV_ERROR = "ECare_UnProv_Error";

    // TODO: add the FS brief description of the OM in the javadoc for the name

    public static final String BAS_APPNAME = "AppCrmBas";
    public static final int BAS_PORT = 9264;
    
    public static final long POOL_PP_ID_START = 0x4000000000000000L;
    
    public static final int MIN_BILL_CYCLE_DAY = 1;
    public static final int MAX_BILL_CYCLE_DAY = 28;
    public static final int SPECIAL_BILL_CYCLE_DAY = -1;
    public static final int AUTO_BILL_CYCLE_START_ID = 9000;
    
    public static final String READ_ONLY_HOME_ADJUSTMENT_TYPE = "ADJUSTMENT_TYPE_READ_ONLY_HOME";
    public static final String SYSTEM_HOME_ADJUSTMENT_TYPE = "ADJUSTMENT_TYPE_SYSTEM_HOME";


    /** Key in the context under which the SubscriberNote home is found */
    public static final String SUBSCRIBER_NOTE_HOME = "SubscriberNoteHome";

    /** key in the context under which the AccountNoteHome is found */
    public static final String ACCOUNT_NOTE_HOME = "AccountNoteHome";
    
    public static final int INVALID_ADJUSTMENT = -999;
    
    public static final String SYSTEM_AGENT = "System";
    
    public final static long NONE_BUNDLE_ID = -1l;
    
    public final static long SELF_BUNDLE_ID = -2l;
}
