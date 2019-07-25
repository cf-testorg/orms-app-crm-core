/*
 * Created on Nov 21, 2005

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
package com.redknee.app.crm.technology;

import java.io.PrintWriter;

import com.redknee.framework.license.LicenseMgr;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.webcontrol.EnumWebControl;
import com.redknee.framework.xhome.xenum.EnumCollection;
import com.redknee.framework.xlog.log.DebugLogMsg;
import com.redknee.framework.xlog.log.LogSupport;
import com.redknee.framework.xlog.log.MajorLogMsg;

import com.redknee.app.crm.CoreCrmLicenseConstants;
import com.redknee.app.crm.xhome.CustomEnumCollection;

/**
 * @author skushwaha
 */
public class TechnologyFilterWebControl extends EnumWebControl
{

    private final static EnumCollection NO_ANY_COL = new CustomEnumCollection(
                    TechnologyEnum.GSM,
                    TechnologyEnum.TDMA,
                    TechnologyEnum.CDMA);

    private final static EnumCollection GSM_COLLECTION = new CustomEnumCollection(TechnologyEnum.GSM);

    private final static EnumCollection GSM_COLLECTION_WITH_ANY = new CustomEnumCollection(
                    TechnologyEnum.GSM,
                    TechnologyEnum.ANY);

    private final static EnumCollection TDMA_COLLECTION = new CustomEnumCollection(
                    TechnologyEnum.TDMA,
                    TechnologyEnum.CDMA);

    private final static EnumCollection TDMA_COLLECTION_WITH_ANY = new CustomEnumCollection(
                    TechnologyEnum.TDMA,
                    TechnologyEnum.CDMA,
                    TechnologyEnum.ANY);

    private final static EnumCollection[] ALL = {NO_ANY_COL, TechnologyEnum.COLLECTION};

    private final static EnumCollection[] GSM = {GSM_COLLECTION, GSM_COLLECTION_WITH_ANY};

    private final static EnumCollection[] TDMA = {TDMA_COLLECTION, TDMA_COLLECTION_WITH_ANY};

    public TechnologyFilterWebControl(final EnumCollection enumeration, final boolean autoPreview)
    {
    	super(enumeration,autoPreview);
    }

    public TechnologyFilterWebControl setHasAny(final boolean hasAny)
    {
        this.hasAny_ = hasAny ? 1 : 0;
        return this;
    }

    @Override
    public void toWeb(final Context ctx, final PrintWriter out, final String name, final Object obj)
    {
        // TODO redesign so that the Web Controls are not recreated every time, less garbage - less GC

        final LicenseMgr lMgr = (LicenseMgr) ctx.get(LicenseMgr.class);
        if (lMgr != null && lMgr.isLicensed(ctx, CoreCrmLicenseConstants.TDMA_CDMA_LICENSE_KEY)
                && lMgr.isLicensed(ctx, CoreCrmLicenseConstants.GSM_LICENSE_KEY))
        {
            //this.setDelegate(new EnumWebControl(ALL[hasAny_], autoPreview_));
        	new EnumWebControl(ALL[hasAny_], autoPreview_).toWeb(ctx, out, name, obj);
            if (LogSupport.isDebugEnabled(ctx))
            {
                new DebugLogMsg("License for CDMA, TDMA and GSM Technologies are Active", null, null).log(ctx);
            }
        }
        else if (lMgr != null && lMgr.isLicensed(ctx, CoreCrmLicenseConstants.GSM_LICENSE_KEY))
        {
            new EnumWebControl(GSM[hasAny_], autoPreview_).toWeb(ctx, out, name, obj);

            if (LogSupport.isDebugEnabled(ctx))
            {
                new DebugLogMsg("License for GSM Technology is Active", null, null).log(ctx);
            }
        }
        else if (lMgr != null && lMgr.isLicensed(ctx, CoreCrmLicenseConstants.TDMA_CDMA_LICENSE_KEY))
        {
            new EnumWebControl(TDMA[hasAny_], autoPreview_).toWeb(ctx, out, name, obj);

            if (LogSupport.isDebugEnabled(ctx))
            {
                new DebugLogMsg("License for CDMA and TDMA Technologies are Active", null, null).log(ctx);
            }
        }
        else
        {
            new MajorLogMsg(this, "No license defined for GSM/TDMA/CDMA, either one of them should be enabled",
                    null).log(ctx);
        }

    }

    private boolean autoPreview_;
    private int hasAny_;
}
