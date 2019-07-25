package com.redknee.app.crm.xenum;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.xenum.Enum;
import com.redknee.framework.xhome.xenum.EnumCollection;

public class EnumProxy implements Enum
{

	private Enum delegate_;
	
	public EnumProxy(Enum delegate)
	{
		delegate_ = delegate;
	}

	public int compareTo(Object obj)
	{
		return delegate_.compareTo(obj);
	}

	public EnumCollection getCollection()
	{
		return delegate_.getCollection();
	}

	public String getDescription()
	{
		return delegate_.getDescription();
	}

	public String getDescription(Context ctx)
	{
		return delegate_.getDescription(ctx);
	}

	public short getIndex()
	{
		return delegate_.getIndex();
	}

	public Enum getDelegate()
	{
		return delegate_;
	}

	public void setDelegate(Enum delegate)
	{
		this.delegate_ = delegate_;
	}

    public String getName()
    {
        return delegate_.getName();
    }

	public static Object getChild(Object enumeration)
	{
		Object retEnum = enumeration;
		while (retEnum instanceof EnumProxy)
		{
			retEnum = ((EnumProxy)retEnum).getDelegate();
		}
		return retEnum;
	}

}
