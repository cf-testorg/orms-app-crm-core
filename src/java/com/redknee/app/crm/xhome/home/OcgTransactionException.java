package com.redknee.app.crm.xhome.home;

import com.redknee.framework.xhome.home.HomeException;

/**
 * @author ltse
 *
 * This exception is thrown by OcgForwardTransactionHome when the transaction
 * sending to ocg fails.
 */
public class OcgTransactionException extends HomeException
{

	private int errorCode_ = 0;
	
	public OcgTransactionException(String msg, int errorCode)
	{
		super(msg);
		setErrorCode(errorCode);
	}

	/**
	 * Returns the errorCode.
	 * @return int
	 */
	public int getErrorCode() {
		return errorCode_;
	}

	/**
	 * Sets the errorCode.
	 * @param errorCode The errorCode to set
	 */
	public void setErrorCode(int errorCode) {
		this.errorCode_ = errorCode;
	}

}
