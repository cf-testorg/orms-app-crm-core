package com.redknee.app.crm.util.cipher;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.redknee.app.crm.bean.CipherKey;
import com.redknee.app.crm.bean.CipherType;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.msp.Spid;
import com.redknee.framework.xlog.log.CritLogMsg;
import com.redknee.framework.xlog.log.MajorLogMsg;

public class CrmCipherFactory 
{
	private CrmCipherFactory(Context ctx)
	{
		initialize( ctx); 
	}
	
	static public CrmCipherFactory getInstance(Context ctx)
	{
		if (factory_ == null)
		{
			factory_ = new CrmCipherFactory(ctx);
		}
		
		return factory_; 
		
	}
	
	private void initialize(Context ctx)
	{
		Home spidHome = (Home) ctx.get(com.redknee.framework.xhome.msp.SpidHome.class);
		
		try
		{
			Collection<Spid> spids = spidHome.selectAll(ctx);
		
			for(Spid spid : spids)
			{
				try
				{
					createCipher(ctx, spid.getId());
				} catch (Exception e)
				{
					new MajorLogMsg(this, "fail to initilize CrmCipher for spid " + spid.getId(), e).log(ctx);
				}	
			}
		} catch (Exception e)
		{
			new CritLogMsg(this, "fail to initilize CrmCiphers", e).log(ctx);
		}
		
		
		
	}
	
	private CrmCipher createCipher(Context ctx, int spid)
	throws Exception
	{
		
			Home home = (Home) ctx.get(com.redknee.app.crm.bean.CipherKeyHome.class);
			CipherKey key = (CipherKey) home.find(new Integer(spid));
			
			if ( key != null)
			{	
				Home cipherTypeHome = (Home) ctx.get(com.redknee.app.crm.bean.CipherTypeHome.class);
			
				CipherType type = (CipherType) cipherTypeHome.find(ctx, new Integer(key.getKeyType())); 
				
				CrmCipher ret = ( CrmCipher) Class.forName( type.getCipherClassName()).newInstance() ;

				ret.init(key);
				
				CrmCipherFactory.ciphers_.put(new Integer(spid), ret);
				
				return ret; 
				
				
			} else
			{
				throw new Exception("find to find cipher key configuration for spid " + spid); 
			}

 
	}
	
	
	
	public CrmCipher getCipher(Context ctx, int spid)
	throws Exception
	{
		if ( CrmCipherFactory.ciphers_.get(new Integer(spid)) == null)
		{
			return this.createCipher(ctx, spid); 
		}
		
		return CrmCipherFactory.ciphers_.get(new Integer(spid)); 
	}
	
	
	
	static CrmCipherFactory factory_; 
	static Map<Integer, CrmCipher>  ciphers_ = new HashMap<Integer, CrmCipher>(); 
}
