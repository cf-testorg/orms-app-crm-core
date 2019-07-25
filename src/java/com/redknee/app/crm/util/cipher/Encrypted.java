package com.redknee.app.crm.util.cipher;


public interface Encrypted 
{
	public void encrypt(CrmCipher cipher) throws CrmEncryptingException;
	
	public void decrypt(CrmCipher cipher) throws CrmEncryptingException; 
	
      public static final String ENCRYPTED_MASK_PREFIX = "*******"; 
}
