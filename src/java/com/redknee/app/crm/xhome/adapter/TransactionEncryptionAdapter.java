/*
 *  TransactionEncryptionAdapterHome.java
 *
 *  Author : Gary Anderson
 *  Date   : 2003-12-10
 *
 *  Copyright (c) Redknee, 2003
 *  - all rights reserved
 */
package com.redknee.app.crm.xhome.adapter;

import com.redknee.framework.auth.cipher.Cipher;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Adapter;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.auth.cipher.SimpleCipher;

import com.redknee.app.crm.bean.Transaction;


/**
 * Originally created to prevent credit card numbers from being stored in the
 * database in plaintext.
 *
 * @author gary.anderson@redknee.com
 */
public class TransactionEncryptionAdapter
    implements Adapter
{
    public TransactionEncryptionAdapter(final Context context)
    {
        cipher_ = new SimpleCipher(context);
    }

    /**
     * @see com.redknee.framework.xhome.home.Adapter#adapt(com.redknee.framework.xhome.context.Context, java.lang.Object)
     */
    public Object adapt(Context ctx,final Object obj)
        throws HomeException
    {
        final Transaction transaction = (Transaction)obj;

        if (transaction == null || transaction.getCreditCardNumber() == null)
        {
            return transaction;
        }
        
        final String decodedNumber =
            cipher_.decode(transaction.getCreditCardNumber());

        final String pattern = "(.*)(....)";

        final String center = decodedNumber.replaceAll(pattern, "$1");
        final String stars = center.replaceAll(".", "*");

        final String obfuscated = decodedNumber.replaceAll(pattern, stars + "$2");
        
        transaction.setCreditCardNumber(obfuscated);

        return transaction;
    }

    /**
     * @see com.redknee.framework.xhome.home.Adapter#unAdapt(com.redknee.framework.xhome.context.Context, java.lang.Object)
     */
    public Object unAdapt(Context ctx,final Object obj)
        throws HomeException
    {
        final Transaction transaction = (Transaction)obj;
        
        if (transaction == null || transaction.getCreditCardNumber() == null)
        {
            return transaction;
        }
        
        final String encodedNumber =
            cipher_.encode(transaction.getCreditCardNumber());

        // TODO - 2003-12-10 - Should obfuscate the decoded number?
        
        transaction.setCreditCardNumber(encodedNumber);

        return transaction;
    }

    
    /**
     * The Cipher used to encrypt/decrypt values.
     */
    protected final Cipher cipher_;
    
    
} // class
