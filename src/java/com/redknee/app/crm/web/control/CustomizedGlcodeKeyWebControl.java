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

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.msp.SpidAware;
import com.redknee.framework.xhome.webcontrol.AbstractWebControl;
import com.redknee.framework.xhome.webcontrol.KeyWebControlOptionalValue;
import com.redknee.framework.xhome.webcontrol.WebControl;

import com.redknee.app.crm.bean.GLCodeMappingHome;
import com.redknee.app.crm.bean.GLCodeMappingID;
import com.redknee.app.crm.bean.GLCodeMappingIdentitySupport;
import com.redknee.app.crm.bean.GLCodeMappingKeyWebControl;
import com.redknee.app.crm.bean.GLCodeMappingXInfo;

public class CustomizedGlcodeKeyWebControl implements WebControl
{
    private final WebControl delegate_;
    private static final Object OPTIONAL_KEY = new GLCodeMappingID(-1, "default");
    private static final String OVERRIDE_KEY = GLCodeMappingIdentitySupport.instance().toStringID(OPTIONAL_KEY);
    // TODO when Framework bug is fixed use OPTIONAL_KEY directly
    private static final Object KEY_CONTROL = new KeyWebControlOptionalValue("--", OVERRIDE_KEY);

    /**
     * Constructor with no parameters.
     * Default is autoPreview false as for other Web Controls.
     * If you need autorefresh call the other constructor in model files.
     */
    public CustomizedGlcodeKeyWebControl()
    {
        this(false);
    }

    public CustomizedGlcodeKeyWebControl(final boolean autoPreview)
    {
        delegate_ = new GLCodeMappingKeyWebControl(1, autoPreview, KEY_CONTROL);
    }

    ////////////////////////////////////////////////////////////////// Impl WebControl

    public void fromWeb(final Context ctx, final Object p1, final javax.servlet.ServletRequest p2, final String p3)
    {
        delegate_.fromWeb(ctx, p1, p2, p3);
    }

    public Object fromWeb(final Context ctx, final javax.servlet.ServletRequest p1, final String p2)
    {
        return ((GLCodeMappingID) delegate_.fromWeb(ctx, p1, p2)).getGlCode();
    }

    public void toWeb(final Context ctx, final java.io.PrintWriter p1, final String p2, final Object p3)
    {
        final SpidAware spid = (SpidAware) ctx.get(AbstractWebControl.BEAN);

        final Context subctx = ctx.createSubContext();
        Home glcodeHome = (Home) ctx.get(GLCodeMappingHome.class);

        // TODO we should be able to rely on MSP support 
        glcodeHome = glcodeHome.where(ctx, new EQ(GLCodeMappingXInfo.SPID, Integer.valueOf(spid.getSpid())));
        subctx.put(GLCodeMappingHome.class, glcodeHome);
        delegate_.toWeb(subctx, p1, p2, new GLCodeMappingID(spid.getSpid(), (String) p3));
    }
}
