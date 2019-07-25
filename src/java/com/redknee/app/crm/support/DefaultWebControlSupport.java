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
import com.redknee.framework.xhome.webcontrol.AbstractWebControl;
import com.redknee.framework.xhome.webcontrol.OutputWebControl;
import com.redknee.framework.xhome.webcontrol.ViewModeEnum;

/**
 * @author jchen
 */
public class DefaultWebControlSupport implements WebControlSupport
{
    protected static WebControlSupport instance_ = null;
    public static WebControlSupport instance()
    {
        if (instance_ == null)
        {
            instance_ = new DefaultWebControlSupport();
        }
        return instance_;
    }

    protected DefaultWebControlSupport()
    {
    }
    
    /**
     * @deprecated Deprecated in 7.5.  Use (@link #isPropertyVisible(Context, PropertyInfo))
     */
	@Deprecated
    public boolean isPropertyVisible(Context ctx, String property)
	{
		return !ViewModeEnum.NONE.equals(AbstractWebControl.getMode(ctx, property));
	}
	
    public boolean isPropertyVisible(Context ctx, PropertyInfo property)
    {
        return !ViewModeEnum.NONE.equals(AbstractWebControl.getMode(ctx, property));
    }
	
    /**
     * @deprecated Deprecated in 7.5.  Use (@link #hideProperty(Context, PropertyInfo))
     */
	@Deprecated
    public void hideProperty(Context ctx, String property)
	{
		AbstractWebControl.setMode(ctx, property, ViewModeEnum.NONE);
	}
	
    public void hideProperty(Context ctx, PropertyInfo property)
    {
        AbstractWebControl.setMode(ctx, property, ViewModeEnum.NONE);
    }
	
    /**
     * @deprecated Deprecated in 7.5.  Use (@link #hideProperties(Context, PropertyInfo[]))
     */
	@Deprecated
    public void hideProperties(Context ctx, String[] properties)
	{
		for (int i = 0; i < properties.length; i++)
		{
			hideProperty(ctx, properties[i]);
		}
	}

    public void hideProperties(Context ctx, PropertyInfo[] properties)
    {
        for (int i = 0; i < properties.length; i++)
        {
            hideProperty(ctx, properties[i]);
        }
    }
    
    /**
     * @deprecated Deprecated in 7.5.  Use (@link #setPropertyReadOnly(Context, PropertyInfo))
     */
	@Deprecated
    public void setPropertyReadOnly(Context ctx, String property)
	{
		if (isPropertyVisible(ctx, property))
		{
			AbstractWebControl.setMode(ctx, property,  ViewModeEnum.READ_ONLY);
		}
	}
	
    public void setPropertyReadOnly(Context ctx, PropertyInfo property)
    {
        if (isPropertyVisible(ctx, property))
        {
            AbstractWebControl.setMode(ctx, property,  ViewModeEnum.READ_ONLY);
        }
    }
    
    /**
     * @deprecated Deprecated in 7.5.  Use (@link #setPropertiesReadOnly(Context, PropertyInfo[]))
     */
	@Deprecated
    public void setPropertiesReadOnly(Context ctx, String[] properties)
	{
		for (int i = 0; i < properties.length; i++)
		{
			setPropertyReadOnly(ctx, properties[i]);
		}
	}

    public void setPropertiesReadOnly(Context ctx, PropertyInfo[] properties)
    {
        for (int i = 0; i < properties.length; i++)
        {
            setPropertyReadOnly(ctx, properties[i]);
        }
    }
    
    public void setPropertyReadWrite(Context ctx, PropertyInfo property)
    {
        if (isPropertyVisible(ctx, property))
        {
            AbstractWebControl.setMode(ctx, property,  ViewModeEnum.READ_WRITE);
        }
    }

    public void setPropertiesReadWrite(Context ctx, PropertyInfo[] properties)
    {
        for (int i = 0; i < properties.length; i++)
        {
            setPropertyReadWrite(ctx, properties[i]);
        }
    }
	
	public boolean isCreateMode(Context ctx)
	{
		return ctx.getInt("MODE", OutputWebControl.DISPLAY_MODE) == OutputWebControl.CREATE_MODE;
	}

}
