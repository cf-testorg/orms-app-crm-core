package com.redknee.app.crm.util.cipher;

import com.redknee.app.crm.bean.CipherKey;

public class RedkneeFWCipher
implements CrmCipher
{

	@Override
	public String decode(String source) throws Exception 
	{
		return cipher.decode(source);
	}

	@Override
	public String encode(String source) throws Exception 
	{
		return cipher.encode(source);
	}

	@Override
	public void init(CipherKey key) throws Exception 
	{
		
	}

	@Override
	public CipherKey generateKey(CipherKey key) 
	throws Exception 
	{
		return key;
	}

	public final static com.redknee.framework.auth.cipher.SimpleCipher cipher = com.redknee.framework.auth.cipher.SimpleCipher.instance();
	
}
