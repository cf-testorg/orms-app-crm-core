package com.redknee.app.crm.util.cipher;

import com.redknee.app.crm.bean.CipherKey;

public interface CrmCipher 
{
	public void init(CipherKey key) throws Exception; 
	public String encode(String source) throws Exception; 
	public String decode(String source) throws Exception;
	public CipherKey generateKey(CipherKey key) throws Exception; 
}
