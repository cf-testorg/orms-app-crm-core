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

package com.redknee.app.crm.bean.core;

import com.redknee.app.crm.bean.SubscriptionTypeAware;
import com.redknee.app.crm.util.cipher.CrmCipher;
import com.redknee.app.crm.util.cipher.CrmEncryptingException;
import com.redknee.app.crm.util.cipher.Encrypted;
import com.redknee.framework.xhome.context.Context;


public class Transaction 
extends com.redknee.app.crm.bean.Transaction 
implements SubscriptionTypeAware, Encrypted 
{
    /**
     * Needed for proper serialization between different JVM versions.
     */
    private static final long serialVersionUID = 1L;


    public Transaction()
    {
        
    }


    /**
     * Looks-up and returns the Subscriber for the current MSISDN.
     *
     * @param ctx
     *            The operating context.
     * @return The subscriber if one exists for the MSISDN; null otherwise.
     * @exception HomeException
     *                Thrown if there is a problem with the MsisdnHome or the
     *                SubscriberHome.
     */
  //WARNING KUMARAN (FEB. 25, 2010) due AppCrmCore Migration:  This funciton has been removed temp    

//    public Subscriber getSubscriber(final Context ctx) throws HomeException
//    {
//        if (ctx == null)
//        {
//            throw new IllegalArgumentException("Could not find object.  Context parameter is null.");
//        }
        
//        return SubscriberSupport.lookupSubscriberForMSISDN(ctx, getMSISDN(), getSubscriptionTypeId(), getTransDate());
//    }

    /** a flag to identify internal split transaction. */
    private transient boolean internal_ = false;


    /**
     * Set to true for internal generated transactions.
     */
    public void setInternal()
    {
        this.internal_ = true;
    }


    /**
     * Query if this is an internal transaction.
     *
     * @return Whether this is an internal transaction.
     */
    public boolean isInternal()
    {
        return this.internal_;
    }


    /**
     * Looks-up and returns the Account for the current Account Number (WITHOUT looking at
     * the current MSISDN).
     *
     * @param ctx
     *            The operating context.
     * @return The account if one exists for the account number; null otherwise.
     * @exception HomeException
     *                Thrown if there is a problem with the AccountHome.
     */
  
//WARNING KUMARAN (FEB. 25, 2010) due AppCrmCore Migration:  This funciton has been removed temp    
//    public Account getAccount(final Context ctx) throws HomeException
//    {
//        return AccountSupport.getAccount(ctx, getAcctNum());
//    }

    /**
     * Returns the SubscriptionType bean for this transaction.
     * 
     * @param ctx IN The operating context.
     * @return The SubscriptionType if one exists for the set identifier; null otherwise.
     * @throws Exception Thrown if there is a problem with the SubscriptionType Home.
     */
    @Override
    public SubscriptionType getSubscriptionType(final Context ctx)
    {
        return SubscriptionType.getSubscriptionType(ctx, getSubscriptionType());
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public long getSubscriptionType()
    {
        return this.getSubscriptionTypeId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSubscriptionType(long subscriptionTypeId)
    {
        this.setSubscriptionTypeId(subscriptionTypeId);
    }
    
    
    
	synchronized public void encrypt(CrmCipher cipher) throws CrmEncryptingException
	{
		try 
		{
			if(this.getCreditCardNumber()!=null && this.getCreditCardNumber().trim().length() > 0
					&& !this.getCreditCardNumber().startsWith(Encrypted.ENCRYPTED_MASK_PREFIX) )
			{
				this.setEncodedCreditCardNumber(cipher.encode(this.getCreditCardNumber()));
				this.setDecodedCreditCard(this.getCreditCardNumber()); 
				this.setCreditCardNumber( Encrypted.ENCRYPTED_MASK_PREFIX + this.decodedCreditCard.trim().substring(
						this.decodedCreditCard.trim().length() -4));
			}
		} catch (Exception e)
		{
			CrmEncryptingException ex =  new CrmEncryptingException();
			ex.setStackTrace(e.getStackTrace());
			throw ex; 
		}
			
	}
	
	synchronized public void decrypt(CrmCipher cipher) throws CrmEncryptingException
	{
		try 
		{
			if(this.getEncodedCreditCardNumber()!=null  && this.getEncodedCreditCardNumber().trim().length() > 0 )
			{
				this.setDecodedCreditCard(cipher.decode(this.getEncodedCreditCardNumber()));
				this.setCreditCardNumber( Encrypted.ENCRYPTED_MASK_PREFIX + this.decodedCreditCard.trim().substring(
					this.decodedCreditCard.trim().length() -4));
			
			} 
		} catch (Exception e)
		{
			CrmEncryptingException ex =  new CrmEncryptingException();
			ex.setStackTrace(e.getStackTrace());
			throw ex; 
		}
	
	}
    
	public String getDecodedCreditCard() {
		return decodedCreditCard;
	}


	private void setDecodedCreditCard(String decodedCreditCard) {
		this.decodedCreditCard = decodedCreditCard;
	}

	private String decodedCreditCard = ""; 
    
	/**
	 * TT#12051459060 
	 * Only used for Postpaid
	 */
	private boolean allowCreditLimitOverride = true;
    
    /**
     * Only to be used for Postpaid. The client is supposed to be aware of
     * whether the subscription is Postpaid or not, and he must ensure this
     * method is called only for Postpaid.
     */
    public boolean isAllowCreditLimitOverride()
    {
        return this.allowCreditLimitOverride;
    }

    /**
     * Only to be used for Postpaid. The client is supposed to be aware of
     * whether the subscription is Postpaid or not, and he must ensure this
     * method is called only for Postpaid.
     * @return
     */
    public boolean getAllowCreditLimitOverride()
    {
        return this.isAllowCreditLimitOverride();
    }

    /**
     * Only to be used for Postpaid
     * @param allowCreditLimitOverride the allowCreditLimitOverride to set
     */
    public void setAllowCreditLimitOverride(boolean allowCreditLimitOverride)
    {
        this.allowCreditLimitOverride = allowCreditLimitOverride;
    }


    public static final long UNKOWN_CHARGE = Long.MAX_VALUE;
    public static final long UNKOWN_REFUND = Long.MIN_VALUE;
    
}