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
package com.redknee.app.crm.web.control;

import java.io.PrintWriter;
import java.text.DecimalFormat;

import javax.servlet.ServletRequest;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.webcontrol.AlignmentEnum;
import com.redknee.framework.xhome.webcontrol.BooleanWebControl;
import com.redknee.framework.xhome.webcontrol.LongWebControl;
import com.redknee.framework.xhome.webcontrol.WebControl;

public class OffsetLongWebControl extends LongWebControl
{
    public static final BooleanWebControl DEFAULT_BOOLEAN_WC = new BooleanWebControl("Before", "After");

    public OffsetLongWebControl(int width)
    {
        super(width);
    }


    public OffsetLongWebControl(int width, int base)
    {
        super(width, base);
    }


    public OffsetLongWebControl(int width, long min, long max)
    {
        super(width, min, max);
    }


    public OffsetLongWebControl(int width, String format)
    {
        super(width, format);
    }
    
    
    public OffsetLongWebControl(int width, AlignmentEnum align)
    {
        super(width, align);
    }


    public OffsetLongWebControl(int width, DecimalFormat format)
    {
        super(width, format);
    }


    public OffsetLongWebControl(int width, DecimalFormat format, long min, long max)
    {
        super(width, format, min, max);
    }


    public OffsetLongWebControl(int width, DecimalFormat format, int base, long min, long max, AlignmentEnum align,
            int maxLength)
    {
        super(width, format, base, min, max, align, maxLength);
    }


    public OffsetLongWebControl(int width, int base, AlignmentEnum align)
    {
        super(width, base, align);
    }


    public OffsetLongWebControl(int width, int base, int maxLength)
    {
        super(width, base, maxLength);
    }


    @Override
    public void toWeb(Context ctx, PrintWriter out, String name, Object obj)
    {
        if (obj instanceof Number)
        {
            long offset = ((Number) obj).longValue();
            boolean isBefore = (offset <= 0);
            offset = Math.abs(offset);
            
            super.toWeb(ctx, out, name, offset);
            if (unitsLabel_ != null && unitsLabel_.trim().length() > 0)
            {
                out.print("&nbsp;" + unitsLabel_ + "&nbsp;");
            }
            getBooleanWebControlDelegate().toWeb(ctx, out, name + ".Before", isBefore);
        }
        else
        {
            super.toWeb(ctx, out, name, obj);
        }
    }


    @Override
    public Object fromWeb(Context ctx, ServletRequest req, String name)
    {
        boolean isBefore = ((Boolean) getBooleanWebControlDelegate().fromWeb(ctx, req, name + ".Before")).booleanValue();
        long offset = ((Number) super.fromWeb(ctx, req, name)).longValue();
        if (isBefore)
        {
            offset *= -1;
        }
        
        return Long.valueOf(offset);
    }
    
    public OffsetLongWebControl setBooleanWebControlDelegate(WebControl delegate)
    {
        booleanWc_ = delegate;
        return this;
    }
    
    public OffsetLongWebControl setUnitsLabel(String units)
    {
        unitsLabel_ = units;
        return this;
    }
    
    public WebControl getBooleanWebControlDelegate()
    {
        return booleanWc_;
    }


    protected String unitsLabel_;
    protected WebControl booleanWc_ = DEFAULT_BOOLEAN_WC;
}