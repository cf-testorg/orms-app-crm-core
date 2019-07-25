/*
 * This code is a protected work and subject to domestic and international
 * copyright law(s). A complete listing of authors of this work is readily 
 * available. Additionally, source code is, by its very nature, confidential 
 * information and inextricably contains trade secrets and other information 
 * proprietary, valuable and sensitive to Redknee, no unauthorised use, 
 * disclosure, manipulation or otherwise is permitted, and may only be used 
 * in accordance with the terms of the licence agreement entered into with 
 * Redknee Inc. and/or its subsidiaries.
 *
 * Copyright (c) Redknee Inc. and its subsidiaries. All Rights Reserved.
 */
package com.redknee.app.crm.web.control;

import java.io.PrintWriter;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.webcontrol.ProxyWebControl;
import com.redknee.framework.xhome.webcontrol.WebControl;

import com.redknee.app.crm.extension.Extension;
import com.redknee.app.crm.extension.ExtensionHolder;


/**
 * Outputs an extension's description before or after the web control output.
 *
 * @author Aaron Gourley
 * @since 7.4.16
 */
public class ExtensionDescriptionWebControl extends ProxyWebControl
{
    protected final boolean placeAtEnd_;

    public ExtensionDescriptionWebControl(WebControl delegate)
    {
        this(delegate, false);
    }

    public ExtensionDescriptionWebControl(WebControl delegate, boolean placeAtEnd)
    {
        super(delegate);
        placeAtEnd_ = placeAtEnd;
    }

    @Override
    public void toWeb(Context ctx, PrintWriter out, String name, Object obj)
    {
        Extension extension = null;
        if( obj instanceof Extension )
        {
            extension = (Extension)obj;
        }
        else if( obj instanceof ExtensionHolder )
        {
            extension = ((ExtensionHolder)obj).getExtension();
        } 

        if( extension != null )
        {
            if( !placeAtEnd_ )
            {
                out.println("<p align=\"left\">" + extension.getDescription(ctx) + "</p>");
            }
            super.toWeb(ctx, out, name, obj);
            if( placeAtEnd_ )
            {
                out.println("<p align=\"left\">" + extension.getDescription(ctx) + "</p>");
            }
        }
        else
        {
            super.toWeb(ctx, out, name, obj);
        }
    }
}
