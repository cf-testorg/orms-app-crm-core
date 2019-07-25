package com.redknee.app.crm.exception.codemapping;

import java.util.HashMap;
import java.util.Map;

/**
 * Translate some of the most common S2100 Error Codes in to Human Readable text. For
 * those error codes not supported, return "OCG error". Can be extended to include
 * more error codes as is necessary.
 *
 */
public class S2100ReturnCodeMsgMapping
{
    /**
     * This prevents instantiation of this class.
     */
    private S2100ReturnCodeMsgMapping()
    {
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
            return UNKNOWN_ERROR_MSG + "(" + errCode + ").";
        }
    }    

    private static final Map<Integer,String> errorMsgMap_ = new HashMap<Integer,String>();

    private static final String SUCCESS_NO_BALANCE = "Subscription's ABM profile has no balance.";
    private static final String WRONG_PIN = "The entered PIN is invalid.";
    private static final String WRONG_PIN_LEN = "The entered PIN length is invalid.";
    private static final String INCORRECT_SP = "The informed SPID is incorrect.";
    private static final String UNKNOWN_ERROR = "Unknown error.";
    private static final String RECORD_NOT_FOUND = "Subscription's ABM profile was not found.";
    private static final String SQL_ERROR = "Database error occurred in ABM.";
    private static final String INTERNAL_ERROR = "Internal error occurred in ABM.";
    private static final String OPS_NOT_ALLOWED = "Operation not allowed in ABM.";
    private static final String MAX_OPS_EXCEEDED = "Maximum number of operations exceeded in ABM.";
    private static final String MAX_AMT_EXCEEDED = "Maximum amount exceeded in ABM.";
    private static final String NOT_ENOUGH_BAL = "Subscription's ABM profile does not have enough balance.";
    private static final String BAL_EXPIRED = "Subscription's ABM profile is expired. Please check subscription expiry date.";
    private static final String SCP_NOT_ALLOWED = "User does not have SCP ID configured in OCG. Please review OCG Charging User configuration.";
    private static final String SCP_NOT_AVAILABLE = "SCP is not available in ABM.";
    private static final String BAD_DATA = "Bad data encountered in ABM.";
    private static final String INVALID_SCPID = "User SCP ID is invalid. Please review OCG Charging User configuration.";
    private static final String INVALID_CURRENCY = "Invalid currency was used.";
    private static final String INVALID_CHARGE_AMOUNT = "Invalid charge amount.";
    private static final String ILLEGAL_PROFILE_STATE = "Subscription's ABM profile is in an illegal state.";
    private static final String UNKNOWN_DATASERVICE = "Unknown data service.";
    private static final String TRANSACTION_TIMEDOUT = "Transaction to OCG/ABM timed out.";
    private static final String RETRIES_FAILED = "Retries failed in ABM.";
    private static final String FAIR_USAGE_LIMIT_EXCEEDED = "Fair usage limit exceeded.";
    private static final String DUPLICATE_TRANSID = "Duplicated transaction identifier.";
    private static final String TRANSACTION_IN_PROGRESS = "Transaction in progress.";
    private static final String OD_MAX_BAL_ALLOWED_EXCEEDED = "Overdraft maximum balance allowed exceeded.";
    private static final String OD_CREDIT_LIMIT_EXCEEDED = "Overdraft credit limit exceeded.";
    private static final String INVALID_FOREIGN_CURRENCY = "Invalid foreign currency was used.";
    private static final String SERVICE_NOT_AVAILABLE = "Service is not available.";
    private static final String WrongVoucherState = "Voucher is in an invalid state.";
    private static final String UNKNOWN_SCP = "Unknown SCP.";
    private static final String LOW_BALANCE = "Low balance.";
    private static final String OAM_POLLER_DOWN = "OAM poller is down.";
    private static final String BALANCE_STALE = "Balance is stale.";
    private static final String INVALID_ACCT_NUM = "Invalid BAN.";
    private static final String INVALID_MSISDN = "Invalid MSISDN.";
    private static final String INVALID_AMT = "Invalid MSISDN.";
    private static final String UNKNOWN_STATE = "Subscription's ABM profile is in an unknown state.";
    private static final String IO_ERROR = "IO error in ABM.";
    private static final String INVALID_ADJ_CODE = "Invalid adjustment code.";
    private static final String BALANCE_INSUFFICIENT = "Insufficient balance.";
    private static final String BALANCE_QUERY_FAILED = "Balance query failed.";
    private static final String BALANCE_DEDUCT_FAILED = "Balance deduction failed.";
    private static final String INVALID_SUBSCRIPTION_TYPE = "Invalid subscription type.";
    
 
    static
    {
        errorMsgMap_.put(Integer.valueOf(com.redknee.product.s2100.ErrorCode.SUCCESS_NO_BALANCE), SUCCESS_NO_BALANCE);
        errorMsgMap_.put(Integer.valueOf(com.redknee.product.s2100.ErrorCode.WRONG_PIN), WRONG_PIN);
        errorMsgMap_.put(Integer.valueOf(com.redknee.product.s2100.ErrorCode.WRONG_PIN_LEN), WRONG_PIN_LEN);
        errorMsgMap_.put(Integer.valueOf(com.redknee.product.s2100.ErrorCode.INCORRECT_SP), INCORRECT_SP);
        errorMsgMap_.put(Integer.valueOf(com.redknee.product.s2100.ErrorCode.UNKNOWN_ERROR), UNKNOWN_ERROR);
        errorMsgMap_.put(Integer.valueOf(com.redknee.product.s2100.ErrorCode.RECORD_NOT_FOUND), RECORD_NOT_FOUND);
        errorMsgMap_.put(Integer.valueOf(com.redknee.product.s2100.ErrorCode.SQL_ERROR), SQL_ERROR);
        errorMsgMap_.put(Integer.valueOf(com.redknee.product.s2100.ErrorCode.INTERNAL_ERROR), INTERNAL_ERROR);
        errorMsgMap_.put(Integer.valueOf(com.redknee.product.s2100.ErrorCode.OPS_NOT_ALLOWED), OPS_NOT_ALLOWED);
        errorMsgMap_.put(Integer.valueOf(com.redknee.product.s2100.ErrorCode.MAX_OPS_EXCEEDED), MAX_OPS_EXCEEDED);
        errorMsgMap_.put(Integer.valueOf(com.redknee.product.s2100.ErrorCode.MAX_AMT_EXCEEDED), MAX_AMT_EXCEEDED);
        errorMsgMap_.put(Integer.valueOf(com.redknee.product.s2100.ErrorCode.NOT_ENOUGH_BAL), NOT_ENOUGH_BAL);
        errorMsgMap_.put(Integer.valueOf(com.redknee.product.s2100.ErrorCode.BAL_EXPIRED), BAL_EXPIRED);
        errorMsgMap_.put(Integer.valueOf(com.redknee.product.s2100.ErrorCode.SCP_NOT_ALLOWED), SCP_NOT_ALLOWED);
        errorMsgMap_.put(Integer.valueOf(com.redknee.product.s2100.ErrorCode.SCP_NOT_AVAILABLE), SCP_NOT_AVAILABLE);
        errorMsgMap_.put(Integer.valueOf(com.redknee.product.s2100.ErrorCode.BAD_DATA), BAD_DATA);
        errorMsgMap_.put(Integer.valueOf(com.redknee.product.s2100.ErrorCode.INVALID_SCPID), INVALID_SCPID);
        errorMsgMap_.put(Integer.valueOf(com.redknee.product.s2100.ErrorCode.INVALID_CURRENCY), INVALID_CURRENCY);
        errorMsgMap_.put(Integer.valueOf(com.redknee.product.s2100.ErrorCode.INVALID_CHARGE_AMOUNT), INVALID_CHARGE_AMOUNT);
        errorMsgMap_.put(Integer.valueOf(com.redknee.product.s2100.ErrorCode.ILLEGAL_PROFILE_STATE), ILLEGAL_PROFILE_STATE);
        errorMsgMap_.put(Integer.valueOf(com.redknee.product.s2100.ErrorCode.UNKNOWN_DATASERVICE), UNKNOWN_DATASERVICE);
        errorMsgMap_.put(Integer.valueOf(com.redknee.product.s2100.ErrorCode.TRANSACTION_TIMEDOUT), TRANSACTION_TIMEDOUT);
        errorMsgMap_.put(Integer.valueOf(com.redknee.product.s2100.ErrorCode.RETRIES_FAILED), RETRIES_FAILED);
        errorMsgMap_.put(Integer.valueOf(com.redknee.product.s2100.ErrorCode.FAIR_USAGE_LIMIT_EXCEEDED), FAIR_USAGE_LIMIT_EXCEEDED);
        errorMsgMap_.put(Integer.valueOf(com.redknee.product.s2100.ErrorCode.DUPLICATE_TRANSID), DUPLICATE_TRANSID);
        errorMsgMap_.put(Integer.valueOf(com.redknee.product.s2100.ErrorCode.TRANSACTION_IN_PROGRESS), TRANSACTION_IN_PROGRESS);
        errorMsgMap_.put(Integer.valueOf(com.redknee.product.s2100.ErrorCode.OD_MAX_BAL_ALLOWED_EXCEEDED), OD_MAX_BAL_ALLOWED_EXCEEDED);
        errorMsgMap_.put(Integer.valueOf(com.redknee.product.s2100.ErrorCode.OD_CREDIT_LIMIT_EXCEEDED), OD_CREDIT_LIMIT_EXCEEDED);
        errorMsgMap_.put(Integer.valueOf(com.redknee.product.s2100.ErrorCode.INVALID_FOREIGN_CURRENCY), INVALID_FOREIGN_CURRENCY);
        errorMsgMap_.put(Integer.valueOf(com.redknee.product.s2100.ErrorCode.SERVICE_NOT_AVAILABLE), SERVICE_NOT_AVAILABLE);
        errorMsgMap_.put(Integer.valueOf(com.redknee.product.s2100.ErrorCode.WrongVoucherState), WrongVoucherState);
        errorMsgMap_.put(Integer.valueOf(com.redknee.product.s2100.ErrorCode.UNKNOWN_SCP), UNKNOWN_SCP);
        errorMsgMap_.put(Integer.valueOf(com.redknee.product.s2100.ErrorCode.LOW_BALANCE), LOW_BALANCE);
        errorMsgMap_.put(Integer.valueOf(com.redknee.product.s2100.ErrorCode.OAM_POLLER_DOWN), OAM_POLLER_DOWN);
        errorMsgMap_.put(Integer.valueOf(com.redknee.product.s2100.ErrorCode.BALANCE_STALE), BALANCE_STALE);
        errorMsgMap_.put(Integer.valueOf(com.redknee.product.s2100.ErrorCode.INVALID_ACCT_NUM), INVALID_ACCT_NUM);
        errorMsgMap_.put(Integer.valueOf(com.redknee.product.s2100.ErrorCode.INVALID_MSISDN), INVALID_MSISDN);
        errorMsgMap_.put(Integer.valueOf(com.redknee.product.s2100.ErrorCode.INVALID_AMT), INVALID_AMT);
        errorMsgMap_.put(Integer.valueOf(com.redknee.product.s2100.ErrorCode.UNKNOWN_STATE), UNKNOWN_STATE);
        errorMsgMap_.put(Integer.valueOf(com.redknee.product.s2100.ErrorCode.IO_ERROR), IO_ERROR);
        errorMsgMap_.put(Integer.valueOf(com.redknee.product.s2100.ErrorCode.INVALID_ADJ_CODE), INVALID_ADJ_CODE);
        errorMsgMap_.put(Integer.valueOf(com.redknee.product.s2100.ErrorCode.BALANCE_INSUFFICIENT), BALANCE_INSUFFICIENT);
        errorMsgMap_.put(Integer.valueOf(com.redknee.product.s2100.ErrorCode.BALANCE_QUERY_FAILED), BALANCE_QUERY_FAILED);
        errorMsgMap_.put(Integer.valueOf(com.redknee.product.s2100.ErrorCode.BALANCE_DEDUCT_FAILED), BALANCE_DEDUCT_FAILED);
        errorMsgMap_.put(Integer.valueOf(com.redknee.product.s2100.ErrorCode.INVALID_SUBSCRIPTION_TYPE), INVALID_SUBSCRIPTION_TYPE);
    }
    

    
 

    private static final String UNKNOWN_ERROR_MSG = "OCG error";
}
