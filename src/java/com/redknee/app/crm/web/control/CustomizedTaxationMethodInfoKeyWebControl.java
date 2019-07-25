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

import com.redknee.app.crm.bean.TaxationMethodInfoHome;
import com.redknee.app.crm.bean.TaxationMethodInfoKeyWebControl;
import com.redknee.app.crm.bean.TaxationMethodInfoXInfo;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.msp.SpidAware;
import com.redknee.framework.xhome.webcontrol.AbstractWebControl;

public class CustomizedTaxationMethodInfoKeyWebControl extends TaxationMethodInfoKeyWebControl
{
    /**
     * Constructor with no parameters.
     * Default is autoPreview false as for other Web Controls.
     * If you need autorefresh call the other constructor in model files.
     */
    public CustomizedTaxationMethodInfoKeyWebControl()
    {
        this(false);
    }

    public CustomizedTaxationMethodInfoKeyWebControl(final boolean autoPreview)
    {
        super(autoPreview);
    }

    ////////////////////////////////////////////////////////////////// Impl WebControl

    public void toWeb(final Context ctx, final java.io.PrintWriter out, final String name, final Object obj)
    {
        final SpidAware spid = (SpidAware) ctx.get(AbstractWebControl.BEAN);

        final Context subctx = ctx.createSubContext();
        Home home = (Home) ctx.get(TaxationMethodInfoHome.class);

        home = home.where(ctx, new EQ(TaxationMethodInfoXInfo.SPID, Integer.valueOf(spid.getSpid())));
        subctx.put(getHomeKey(), home);
        super.toWeb(subctx, out, name, obj);
        
    }
}
