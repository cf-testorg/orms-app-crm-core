/*
 * Created on Oct 24, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.redknee.app.crm.tps.pipe;

/**
 * @author Larry Xia
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public interface TPSPipeConstant {
	
	public final static String TPS_PIPE_ERROR_OUTPUT_STREAM = "TPS_PIPE_ERROR_OUTPUT_STREAM";
	public final static String TPS_PIPE_ERROR_FILE = "TPS_PIPE_ERROR_FILE";
	public final static String TPS_PIPE_ERROR_FILE_HEADER = "TPS_PIPE_ERROR_FILE_HEADER";
	public final static String TPS_PIPE_TPS_FILE = "TPS_PIPE_TPS_FILE";
	
	public final static String TPS_PIPE_ACCOUNT_TOTAL_OWING = "TPS_PIPE_ACCOUNT_TOTAL_OWING";
	public final static String TPS_PIPE_SUBSCRIBER_TOTAL_OWING = "TPS_PIPE_TOTAL_OWING";
	public final static String TPS_PIPE_ACCOUNT_TAX_OWING = "TPS_PIPE_ACCOUNT_TAX_OWING";
	public final static String TPS_PIPE_SUBSCRIBER_TAX_OWING = "TPS_PIPE_SUBSCRIBER_TAX_OWING";
	public final static String TPS_PIPE_ACCOUNT_TAX_AMOUNT = "TPS_PIPE_ACCOUNT_TAX_AMOUNT";
	public final static String TPS_PIPE_SUBSCRIBER_TAX_AMOUNT = "TPS_PIPE_SUBSCRIBER_TAX_AMOUNT";
	public final static String TPS_PIPE_ACCOUNT_TOTAL_CHARGE = "TPS_PIPE_TOTAL_CHARGE";
	public final static String TPS_PIPE_SUBSCRIBER_TOTAL_CHARGE = "TPS_PIPE_TOTAL_CHARGE";
	public final static String TPS_PIPE_SUBSCRIBER_BALANCE= "TPS_PIPE_SUBSCRIBER_BALANCE";
	public final static String TPS_PIPE_ACCOUNT_BALANCE= "TPS_PIPE_ACCOUNT_BALANCE";
	public final static String TPS_PIPE_ACCOUNT_TOTAL_USAGE= "TPS_PIPE_ACCOUNT_TOTAL_USAGE";
	public final static String TPS_PIPE_SUBSCRIBER_TOTAL_USAGE= "TPS_PIPE_SUBSCRIBER_TOTAL_USAGE";
	
	public final static String INVOICE_INVOICE_DATE = "INVOICE_INVOICE_DATE"; 
	public final static String ACCOUNT_INVOICE_DATE = "ACCOUNT_INVOICE_DATE"; 
    public final static String TPS_PIPE_RESULT_CODE= "TPS_PIPE_RESULT_CODE";
	public final static String TPS_UPS_RESULT_CODE= "TPS_UPS_RESULT_CODE";

	public static final String ADJUSTMENT_TYPE_BILL = "Payments"; 
	public static final String ADJUSTMENT_TYPE_ADEP = "Deposit Made"; 
	public static final String ADJUSTMENT_TYPE_RDEP = "Roaming Deposit"; 

 	public static final String DUNNING_ACCOUNT_SET = "DUNNING_ACCOUNT_SET"; 
 	
  	public final static String PIPELINE_TPS_KEY = "PIPELINE_TPS_KEY";
	public final static String PIPELINE_SUBSCRIBER_DEPOSIT_KEY = "PIPELINE_SUBSCRIBER_DEPOSIT_KEY";
	public final static String PIPELINE_SUBSCRIBER_BILL_KEY = "PIPELINE_SUBSCRIBER_BILL_KEY";
	public final static String PIPELINE_ACCOUNT_DEPOSIT_KEY = "PIPELINE_ACCOUNT_DEPOSIT_KEY";
	public final static String PIPELINE_ACCOUNT_BILL_KEY = "PIPELINE_ACCOUNT_BILL_KEY";


	public static final int FAIL_TO_FIND_MSISDN_PREFIX = 1;
	public static final int INVALID_MSISDN_LENGTH = 2;
	public static final int FAIL_TO_FIND_ADJUST_TYPE = 3;
	public static final int FAIL_TO_FILL_IN_TRASACTION_RECORD = 4;
	public static final int FAIL_TO_FIND_SUB = 5;
	public static final int FAIL_TO_FIND_ACCOUNT = 6;
	public static final int FAIL_TO_FIND_TAX_RATE = 7;
	public static final int FAIL_TO_FIND_SERVICE_PROVIDER = 8;
	public static final int FAIL_TO_CREATE_TRANSACTION = 9;
	public static final int FAIL_TO_QUERY_TRANSACTION_TABLE = 10;
	public static final int FAIL_TO_CAL_TOTAL_CHARGES = 11;
	public static final int FAIL_TO_CAL_TOTAL_USAGE = 12;
	public static final int FAIL_TO_RESET_SUB = 13;
	public static final int PREPAID_SUB_REJECTED = 14;
	public static final int UNSUPPORTED_GROUP_PAYMENT = 19;
	public static final int FAIL_DUPLICATE_TRANSACTION = 15; 
	public static final int FAIL_UPDATE_SUSCRIBER_TABLE = 16;
	public static final int FAIL_UPDATE_CREDIT_LIMIT = 17;
	public static final int MULTIPLE_SUBCRIBER_EXCEPTION = 18;
	public static final int INVALID_TPS_ENTRY_NO_BAN_NO_MSISDN = 20; 
	public static final int PAYMENT_EXCEPTION_RECORD_FOUND = 21;
 
	public static final int RESULT_CODE_SUCCESS = 0; 
	public static final int RESULT_CODE_DATABASE_NA = 3003; 
	public static final int RESULT_CODE_MOBILE_NA = 3004; 
	public static final int RESULT_CODE_UPS_FAILS = 3008; 
	public static final int RESULT_CODE_GENERAL = 9999; 
	public final static int RESULT_CODE_UPS_RESULT_NOT_APPLY = -1; 

	public static  final double CREDIT_LIMIT_FACTOR = 1.5; 
}
