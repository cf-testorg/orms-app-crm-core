package com.redknee.app.crm.web.control;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletRequest;

import com.redknee.app.crm.license.LicenseAware;
import com.redknee.app.crm.xenum.EnumProxy;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.language.MessageMgr;
import com.redknee.framework.xhome.webcontrol.EnumWebControl;
import com.redknee.framework.xhome.xenum.Enum;
import com.redknee.framework.xhome.xenum.EnumCollection;

public class LicenceAwareEnumWebControl extends EnumWebControl
{

	public LicenceAwareEnumWebControl(EnumCollection enumc)
	{
		super(enumc);
	}
	
	public LicenceAwareEnumWebControl(EnumCollection enumc, int size)
	{
		super(enumc, size);
	}
	
	public LicenceAwareEnumWebControl(EnumCollection enumc, boolean autoPreview)
	{
		super(enumc, autoPreview);
	}
	
    public EnumCollection getCompleteEnumCollection(Context ctx)
    {
        return super.getEnumCollection(ctx);
    }

    public EnumCollection getEnumCollection(Context ctx)
    {
        EnumCollection collection = getCompleteEnumCollection(ctx);
        int  mode = ctx.getInt("MODE", DISPLAY_MODE);
        
        if (mode != DISPLAY_MODE)
        {
            List<Enum> result = new ArrayList<Enum>();
            for ( Iterator it = collection.iterator() ; it.hasNext() ; )
            {
                Enum bean = (Enum) it.next();
                if (!(bean instanceof LicenseAware && !((LicenseAware)bean).isLicensed(ctx)))
                {
                    result.add(bean);
                }
            }
            return new EnumCollection(result.toArray(new Enum[result.size()]));
        }
        else
        {
            return collection;
        }
    }


    public Object fromWeb(Context ctx, ServletRequest req, String name) throws NullPointerException
    {
        if (req.getParameter(name) == null)
        {
            throw new NullPointerException("Null com.redknee.framework.xhome.xenum.Enum Value");
        }
        else
        {
            // returns an com.redknee.framework.xhome.xenum.Enum object containing the index and description
            Object enumeration = getEnumCollection(ctx).getByIndex(new Short(req.getParameter(name)).shortValue());
            if (enumeration instanceof EnumProxy)
            {
                return EnumProxy.getChild(enumeration);
            }
            else
            {
                return enumeration;
            }
        }
    }
}
