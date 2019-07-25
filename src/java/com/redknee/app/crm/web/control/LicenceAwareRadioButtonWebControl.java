package com.redknee.app.crm.web.control;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletRequest;

import com.redknee.app.crm.license.LicenseAware;
import com.redknee.app.crm.xenum.EnumProxy;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.webcontrol.RadioButtonWebControl;
import com.redknee.framework.xhome.xenum.Enum;
import com.redknee.framework.xhome.xenum.EnumCollection;

public class LicenceAwareRadioButtonWebControl extends RadioButtonWebControl
{

    public LicenceAwareRadioButtonWebControl(EnumCollection enumc)
    {
        super(enumc);
    }


    public LicenceAwareRadioButtonWebControl(EnumCollection _enum, String before, String after)
    {
        super(_enum, before, after);
    }


    public LicenceAwareRadioButtonWebControl(EnumCollection _enum, boolean autoPreview, String before, String after)
    {
        super(_enum, autoPreview, before, after);
    }


    public LicenceAwareRadioButtonWebControl(EnumCollection enumc, boolean autoPreview)
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
        Object enumeration = null;
        if ( req.getParameter(name) == null )
        {
           throw new NullPointerException("Null Enum Value");
        }
        else
        {
            enumeration =  getCompleteEnumCollection(ctx).getByIndex(new Short(req.getParameter(name)).shortValue());
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
