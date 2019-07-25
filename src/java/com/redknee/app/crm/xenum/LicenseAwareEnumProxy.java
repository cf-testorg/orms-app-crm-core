package com.redknee.app.crm.xenum;

import com.redknee.framework.license.LicenseMgr;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.xenum.Enum;
import com.redknee.app.crm.license.LicenseAware;


public class LicenseAwareEnumProxy extends EnumProxy implements LicenseAware
{

    private String licenseName_;


    public LicenseAwareEnumProxy(Enum delegate, String licenseName)
    {
        super(delegate);
        licenseName_ = licenseName;
    }


    public boolean isLicensed(Context ctx)
    {
        LicenseMgr manager = (LicenseMgr) ctx.get(LicenseMgr.class);
        // To ensure that all the licenses are met we delegate down the license chain
        return manager.isLicensed(ctx, licenseName_)
                && ((getDelegate() instanceof LicenseAware) ? ((LicenseAware) getDelegate()).isLicensed(ctx) : true);
    }


    public String toString()
    {
        return getDelegate().toString();
    }


    public int hashCode()
    {
        return getIndex();
    }


    public boolean equals(Object o)
    {
        if (o == this)
            return true;
        if (o == null)
            return false;
        if ( !(o instanceof Enum))
        {
            return false;
        }
        return ((Enum) o).getIndex() == getIndex();
    }
}
