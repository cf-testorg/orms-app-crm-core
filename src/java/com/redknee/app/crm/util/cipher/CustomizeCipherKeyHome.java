package com.redknee.app.crm.util.cipher;

import com.redknee.app.crm.bean.CipherKey;
import com.redknee.app.crm.bean.CipherType;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.HomeInternalException;
import com.redknee.framework.xhome.home.HomeProxy;
import com.redknee.framework.xlog.log.MinorLogMsg;

public class CustomizeCipherKeyHome
extends HomeProxy
{
    public CustomizeCipherKeyHome(final Home delegate)
    {
        super(delegate);
    }


	@Override
	public Object create(Context ctx, Object obj) throws HomeException,
			HomeInternalException 
	{
		CipherKey key = (CipherKey) obj; 
		
		Home cipherTypeHome = (Home) ctx.get(com.redknee.app.crm.bean.CipherTypeHome.class);

		CipherType type = (CipherType) cipherTypeHome.find(ctx, new Integer(key.getKeyType())); 
		try 
		{
	
			CrmCipher cipher = ( CrmCipher) Class.forName( type.getCipherClassName()).newInstance() ;

			key = cipher.generateKey(key); 
		} catch(Exception e)
		{
			new MinorLogMsg(this, "fail to create cipher key", e).log(ctx);
			throw new HomeException("fail to create Cipher Key for spid " + key.getSpid());
		}
		return super.create(ctx, key); 
		
	}

	@Override
	public void drop(Context ctx) throws HomeException, HomeInternalException,
	UnsupportedOperationException 
	{

		throw new UnsupportedOperationException("Operation is forbidden"); 
	}

	@Override
	public void remove(Context ctx, Object obj) throws HomeException,
			HomeInternalException {
		Home spidHome = (Home) ctx.get(com.redknee.framework.xhome.msp.SpidHome.class);
		CipherKey key = (CipherKey)obj;
			
		if (spidHome.find(new Integer(key.getSpid())) != null)
		{
			throw new UnsupportedOperationException("Operation is forbidden");
		}
			
			super.remove(ctx, obj);

	}

	@Override
	public void removeAll(Context ctx, Object where) throws HomeException,
			HomeInternalException, UnsupportedOperationException
	{
		throw new UnsupportedOperationException("Operation is forbidden");
	}

	@Override
	/**
	 * do not allow to update key
	 */
	public Object store(Context ctx, Object obj) throws HomeException,
	HomeInternalException 
	{

		throw new UnsupportedOperationException("Operation is forbidden, please contact with Redknee GTAC for a MOP if change is a must");
	}

}
