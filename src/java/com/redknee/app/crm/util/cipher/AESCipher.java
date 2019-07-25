package com.redknee.app.crm.util.cipher;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.redknee.app.crm.bean.CipherKey;

public class AESCipher
implements CrmCipher
{

	@Override
	public String decode(String source)
	throws Exception
	{
		if (!isReady)
		{
			init(this.key);
		}
		

		byte[] encBytes = org.apache.commons.codec.binary.Base64.decodeBase64(source.getBytes("ISO-8859-1"));
	    byte[] uncrypted = decoder.doFinal(encBytes);
		return new  String(uncrypted);
	}

	@Override
	public String encode(String source)
	throws Exception
	{
		if (!isReady)
		{
			init(this.key);
		}
	    byte[] encrypted = encoder.doFinal(source.getBytes());
		
		return new String(org.apache.commons.codec.binary.Base64.encodeBase64(encrypted));
	}

	@Override
	public void init(CipherKey key) 
	throws Exception
	{
		this.key = key; 
		SecretKeySpec skeySpec = new SecretKeySpec(key.getKeyPrivate().getBytes(), CIPHER_AES);
		encoder = Cipher.getInstance(CIPHER_AES);
		encoder.init(Cipher.ENCRYPT_MODE, skeySpec);
		decoder = Cipher.getInstance(CIPHER_AES);
		decoder.init(Cipher.DECRYPT_MODE, skeySpec);
		isReady = true; 		
	}
	
	


	@Override
	public CipherKey generateKey(CipherKey key) 
	throws Exception 
	{
	       KeyGenerator kgen = KeyGenerator.getInstance(CIPHER_AES);
	       kgen.init(key.getKeyLength()); 
	       SecretKey skey = kgen.generateKey(); 
	       byte[] raw = skey.getEncoded();
	       key.setKeyPrivate( new String(raw));
	       return key; 
	}




	Cipher encoder;
	Cipher decoder;
	CipherKey key; 
	boolean isReady = false; 
	
	static final public String CIPHER_AES = "AES"; 
}
