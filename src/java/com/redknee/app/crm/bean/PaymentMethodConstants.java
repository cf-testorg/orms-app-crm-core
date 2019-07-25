package com.redknee.app.crm.bean;

public interface PaymentMethodConstants 
{
	public static final int PAYMENT_METHOD_CASH = 1; 
	public static final int PAYMENT_METHOD_CHEQUE = 2;
	public static final int PAYMENT_METHOD_DEBIT_CARD = 3;
	public static final int PAYMENT_METHOD_TELEBANK = 4;
	public static final int PAYMENT_METHOD_CREDIT_CARD = 5;
	public static final int PAYMENT_METHOD_INVOICE = 9;
	public static final int PAYMENT_METHOD_TRANSFER = 10;
	public static final int PAYMENT_METHOD_LOYALTY_VOUCHER = 1001;
	public static final int PAYMENT_METHOD_BANK_LOAN = 1002;

	public static final int PAYMENT_METHOD_STATE_VALID=0;
	public static final int PAYMENT_METHOD_STATE_PRE_EXPIRY=1;
	public static final int PAYMENT_METHOD_STATE_EXPIRED=2;
}
