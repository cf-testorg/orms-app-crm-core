package com.redknee.app.crm.util.cipher;

import com.redknee.framework.xhome.beans.XBeans;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Adapter;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.msp.SpidAware;
import com.redknee.framework.xlog.log.MinorLogMsg;

public class SpidAwareEncryptingAdapter 
implements Adapter
{

	@Override
	public Object adapt(Context ctx, Object obj) throws HomeException
	{
		 if (obj instanceof SpidAware && obj instanceof Encrypted)
		 {
			 SpidAware spid = (SpidAware) obj; 
			 Object identifier = XBeans.getIdentifier(spid);
			 
			 try
			 {
				 CrmCipher cipher = CrmCipherFactory.getInstance(ctx).getCipher(ctx, spid.getSpid());
				 if (cipher != null)
				 {
					 ((Encrypted) obj).decrypt(cipher); 
				 } else 
				 {
					 throw new HomeException("Fail to find cipher for spid" + spid.getSpid());
				 }
			 } catch (HomeException e)
			 {
				 throw e; 
			 } 
			 catch(Throwable t)
			 {
			     StringBuilder sb = new StringBuilder();
			     sb.append("Unable to decrypt data for bean '");
			     sb.append(obj.getClass().getSimpleName());
			     sb.append("'");
			     if (identifier!=null)
			     {
			         sb.append(" with identifier '");
			         sb.append(identifier);
			         sb.append("'");
			     }
				 new MinorLogMsg(this, sb.toString(), t).log(ctx);
				 throw new HomeException(sb.toString(),t);  
			 }
			 
		 }
	        
		 return obj; 
	}

	@Override
	public Object unAdapt(Context ctx, Object obj) throws HomeException 
	{
		 if (obj instanceof SpidAware && obj instanceof Encrypted)
		 {
			 SpidAware spid = (SpidAware) obj; 
			 
			 try
			 {
				 CrmCipher cipher = CrmCipherFactory.getInstance(ctx).getCipher(ctx, spid.getSpid());
				 if (cipher != null)
				 {
					 ((Encrypted) obj).encrypt(cipher); 
				 } else 
				 {
					 throw new HomeException("Fail to find cipher for spid" + spid.getSpid());
				 }
			 } catch (HomeException e)
			 {
				 throw e; 
			 } catch(Throwable t)
			 {
				 new MinorLogMsg(this, "fail to encrypt data", t).log(ctx);
				 throw new HomeException("fail to encrypt data", t);  
			 }
			 
		 }
	        
		 return obj; 
	}

}
