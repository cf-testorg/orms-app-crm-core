/*
 * This code is a protected work and subject to domestic and international
 * copyright law(s). A complete listing of authors of this work is readily 
 * available. Additionally, source code is, by its very nature, confidential 
 * information and inextricably contains trade secrets and other information 
 * proprietary, valuable and sensitive to Redknee, no unauthorised use, 
 * disclosure, manipulation or otherwise is permitted, and may only be used 
 * in accordance with the terms of the licence agreement entered into with 
 * Redknee Inc. and/or its subsidiaries.
 *
 * Copyright (c) Redknee Inc. and its subsidiaries. All Rights Reserved.
 */
package com.redknee.app.crm.move;

import com.redknee.framework.xhome.beans.CompoundIllegalStateException;



/**
 * An exception wrapper which is used to give specific error codes . 
 *
 * @author bdhavalshankh
 * @since 9.5.1
 */
public class CompoundMoveIllegalSateException extends CompoundIllegalStateException
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String DEFAULT_ERROR_CODE = "0001";
    public static final String ACCOUNT_NOT_RESPONSIBLE = "0002";
    public static final String ACCOUNT_NOT_INDIVIDUAL = "0003";
    public static final String ACCOUNT_NOT_POSTPAID = "0004";
    public static final String ACCOUNT_UNDER_HIERARCHY = "0005";
    public static final String NO_ACTIVE_SUBSCRIPTION = "0006";
    public static final String PAYMENT_PLAN_ENROLLED = "0007";
    public static final String REQUEST_ALREADY_PROCESSING = "0008";
    public static final String ACCOUNT_NOT_ACTIVE = "0009";

    
    
    private String errorMessage = "";
    private String errorCode  = "0001";
    
   
    public CompoundMoveIllegalSateException(String errorCode, String errorMessage)
    {
        super(errorMessage);
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }


    public CompoundMoveIllegalSateException()
    {
        this("CompoundMoveIllegalSateException");
    }
    
    public CompoundMoveIllegalSateException(String txt)
    {
       super(txt);
    }

 
    public String getErrorMessage()
    {
        return errorMessage;
    }


    
    public void setErrorMessage(String errorMessage)
    {
        this.errorMessage = errorMessage;
    }


    public String getErrorCode()
    {
        return errorCode;
    }
    
    public void setErrorCode(String errorCode)
    {
        this.errorCode = errorCode;
    }
    
    public void thrown(String errorCode, String errorMessage, Throwable t)
    {
        setErrorCode(errorCode);
        setErrorMessage(errorMessage);
        super.thrown(t);
        throw this;
    }
}
