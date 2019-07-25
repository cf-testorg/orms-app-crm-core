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

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.webcontrol.AbstractWebControl;
import com.redknee.framework.xhome.webcontrol.ProxyWebControl;
import com.redknee.framework.xhome.webcontrol.WebControl;

/**
 * This decorator is to be used for search beans that need <msp/> functionality.
 *
 * @author victor.stratan@redknee.com
 */
public class SearchBeanToContextWebControl extends ProxyWebControl
{
    /**
     * @param delegate
     */
    public SearchBeanToContextWebControl(WebControl delegate)
    {
        super(delegate);
        
    }

    public void toWeb(Context ctx, PrintWriter p1, String p2, Object bean)
    {
        Context subCtx = ctx.createSubContext();
        ctx.get(AbstractWebControl.BEAN, bean);
        super.toWeb(subCtx, p1, p2, bean);
    }
}
