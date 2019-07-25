/*
 * This code is a protected work and subject to domestic and international
 * copyright law(s).  A complete listing of authors of this work is readily
 * available.  Additionally, source code is, by its very nature, confidential
 * information and inextricably contains trade secrets and other information
 * proprietary, valuable and sensitive to Redknee.  No unauthorized use,
 * disclosure, manipulation or otherwise is permitted, and may only be used
 * in accordance with the terms of the license agreement entered into with
 * Redknee Inc. and/or its subsidiaries.
 *
 * Copyright (c) Redknee Inc. and its subsidiaries. All Rights Reserved.
 */
package com.redknee.app.crm.support;

import com.redknee.framework.xhome.beans.xi.PropertyInfo;
import com.redknee.framework.xhome.context.Context;

/**
 * @author jchen
 */
public interface WebControlSupport extends Support
{
    /**
     * @deprecated Deprecated in 7.5.  Use (@link #isPropertyVisible(Context, PropertyInfo))
     */
	@Deprecated
    public boolean isPropertyVisible(Context ctx, String property);
	
    public boolean isPropertyVisible(Context ctx, PropertyInfo property);
	
    /**
     * @deprecated Deprecated in 7.5.  Use (@link #hideProperty(Context, PropertyInfo))
     */
	@Deprecated
    public void hideProperty(Context ctx, String property);
	
    public void hideProperty(Context ctx, PropertyInfo property);
	
    /**
     * @deprecated Deprecated in 7.5.  Use (@link #hideProperties(Context, PropertyInfo[]))
     */
	@Deprecated
    public void hideProperties(Context ctx, String[] properties);

    public void hideProperties(Context ctx, PropertyInfo[] properties);
    
    /**
     * @deprecated Deprecated in 7.5.  Use (@link #setPropertyReadOnly(Context, PropertyInfo))
     */
	@Deprecated
    public void setPropertyReadOnly(Context ctx, String property);
	
    public void setPropertyReadOnly(Context ctx, PropertyInfo property);
    
    /**
     * @deprecated Deprecated in 7.5.  Use (@link #setPropertiesReadOnly(Context, PropertyInfo[]))
     */
	@Deprecated
    public void setPropertiesReadOnly(Context ctx, String[] properties);

    public void setPropertiesReadOnly(Context ctx, PropertyInfo[] properties);
    
    public void setPropertyReadWrite(Context ctx, PropertyInfo property);

    public void setPropertiesReadWrite(Context ctx, PropertyInfo[] properties);
	
	public boolean isCreateMode(Context ctx);

}
