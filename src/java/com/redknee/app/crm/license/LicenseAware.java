package com.redknee.app.crm.license;

import com.redknee.framework.xhome.context.Context;

public interface LicenseAware
{
	public boolean isLicensed(Context ctx);
}
