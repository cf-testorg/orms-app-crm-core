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

package com.redknee.app.crm.technology;

import java.io.PrintWriter;

import com.redknee.framework.license.LicenseMgr;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.webcontrol.EnumIndexWebControl;
import com.redknee.framework.xhome.webcontrol.EnumWebControl;
import com.redknee.framework.xhome.webcontrol.ProxyWebControl;
import com.redknee.framework.xhome.webcontrol.WebControl;
import com.redknee.framework.xhome.xenum.Enum;
import com.redknee.framework.xhome.xenum.EnumCollection;
import com.redknee.framework.xlog.log.DebugLogMsg;
import com.redknee.framework.xlog.log.LogSupport;
import com.redknee.framework.xlog.log.MajorLogMsg;

import com.redknee.app.crm.CoreCrmLicenseConstants;
import com.redknee.util.snippet.webcontrol.SafeEnumCollection;

/**
 * Provides the technology choices based on enabled Licenses.
 *
 * @author victor.stratan@redknee.com
 */
public class TechnologyOnlyEnumWebControl extends ProxyWebControl
{

    private static final EnumCollection NO_ANY_COL = new SafeEnumCollection(new Enum[]
            {
                    TechnologyEnum.GSM,
                    TechnologyEnum.TDMA,
                    TechnologyEnum.CDMA,
                    TechnologyEnum.VSAT_PSTN,
                    TechnologyEnum.NO_TECH
            });

    private static final EnumCollection GSM_TDMA_COLLECTION = new SafeEnumCollection(new Enum[]
            {
                    TechnologyEnum.GSM,
                    TechnologyEnum.TDMA,
                    TechnologyEnum.CDMA,
                    TechnologyEnum.NO_TECH
            });

    private static final EnumCollection GSM_TDMA_COLLECTION_WITH_ANY = new SafeEnumCollection(new Enum[]
            {
                    TechnologyEnum.GSM,
                    TechnologyEnum.TDMA,
                    TechnologyEnum.CDMA,
                    TechnologyEnum.ANY,
                    TechnologyEnum.NO_TECH
            });

    private static final EnumCollection GSM_VSAT_COLLECTION = new SafeEnumCollection(new Enum[]
            {
                    TechnologyEnum.GSM,
                    TechnologyEnum.VSAT_PSTN,
                    TechnologyEnum.NO_TECH
            });

    private static final EnumCollection GSM_VSAT_COLLECTION_WITH_ANY = new SafeEnumCollection(new Enum[]
            {
                    TechnologyEnum.GSM,
                    TechnologyEnum.ANY,
                    TechnologyEnum.VSAT_PSTN,
                    TechnologyEnum.NO_TECH
            });

    private static final EnumCollection TDMA_VSAT_COLLECTION = new SafeEnumCollection(new Enum[]
            {
                    TechnologyEnum.TDMA,
                    TechnologyEnum.CDMA,
                    TechnologyEnum.VSAT_PSTN,
                    TechnologyEnum.NO_TECH
            });

    private static final EnumCollection TDMA_VSAT_COLLECTION_WITH_ANY = new SafeEnumCollection(new Enum[]
            {
                    TechnologyEnum.TDMA,
                    TechnologyEnum.CDMA,
                    TechnologyEnum.ANY,
                    TechnologyEnum.VSAT_PSTN,
                    TechnologyEnum.NO_TECH
            });

    private static final EnumCollection GSM_COLLECTION = new SafeEnumCollection(new Enum[]
            {
                    TechnologyEnum.GSM,
                    TechnologyEnum.NO_TECH
            });

    private static final EnumCollection GSM_COLLECTION_WITH_ANY = new SafeEnumCollection(new Enum[]
            {
                    TechnologyEnum.GSM,
                    TechnologyEnum.ANY,
                    TechnologyEnum.NO_TECH
            });

    private static final EnumCollection TDMA_COLLECTION = new SafeEnumCollection(new Enum[]
            {
                    TechnologyEnum.TDMA,
                    TechnologyEnum.CDMA,
                    TechnologyEnum.NO_TECH
            });

    private static final EnumCollection TDMA_COLLECTION_WITH_ANY = new SafeEnumCollection(new Enum[]
            {
                    TechnologyEnum.TDMA,
                    TechnologyEnum.CDMA,
                    TechnologyEnum.ANY,
                    TechnologyEnum.NO_TECH
            });

    private static final EnumCollection VSAT_COLLECTION = new SafeEnumCollection(new Enum[]
            {
                    TechnologyEnum.VSAT_PSTN,
                    TechnologyEnum.NO_TECH
            });

    private static final EnumCollection VSAT_WITH_ANY = new SafeEnumCollection(new Enum[]
            {
                    TechnologyEnum.ANY,
                    TechnologyEnum.VSAT_PSTN,
                    TechnologyEnum.NO_TECH
            });
    
    private static final EnumCollection EMPTY_COLLECTION = new SafeEnumCollection(new Enum[]{TechnologyEnum.NO_TECH});

    private static final EnumCollection ONLY_ANY_COLLECTION = new SafeEnumCollection(new Enum[]
            {
                    TechnologyEnum.ANY,
                    TechnologyEnum.NO_TECH
            });

    private static final EnumCollection[] ALL = {NO_ANY_COL, TechnologyEnum.COLLECTION};

    private static final EnumCollection[] GSM_TDMA = {GSM_TDMA_COLLECTION, GSM_TDMA_COLLECTION_WITH_ANY};

    private static final EnumCollection[] GSM_VSAT = {GSM_VSAT_COLLECTION, GSM_VSAT_COLLECTION_WITH_ANY};

    private static final EnumCollection[] TDMA_VSAT = {TDMA_VSAT_COLLECTION, TDMA_VSAT_COLLECTION_WITH_ANY};

    private static final EnumCollection[] GSM = {GSM_COLLECTION, GSM_COLLECTION_WITH_ANY};

    private static final EnumCollection[] TDMA = {TDMA_COLLECTION, TDMA_COLLECTION_WITH_ANY};

    private static final EnumCollection[] VSAT = {VSAT_COLLECTION, VSAT_WITH_ANY};

    private static final EnumCollection[] NONE = {EMPTY_COLLECTION, ONLY_ANY_COLLECTION};

    public TechnologyOnlyEnumWebControl()
    {
        this(false);
    }

    public TechnologyOnlyEnumWebControl(final boolean autoPreview)
    {
        this(autoPreview, false);
    }

    public TechnologyOnlyEnumWebControl(final boolean autoPreview, final boolean hasAny)
    {
        this(autoPreview, hasAny, false);
    }

    public TechnologyOnlyEnumWebControl(final boolean autoPreview, final boolean hasAny, final boolean isIndex)
    {
        this.autoPreview_ = autoPreview;
        this.setHasAny(hasAny);
        this.setIndexWebControl(isIndex);

        /* TT:7041847523
         * Adding default delegate so that it doesn't crash when NEW is clicked after CRM reboot
         */
        this.setDelegate(buildWebControl(ALL[hasAny_], autoPreview_));
    }

    public TechnologyOnlyEnumWebControl setHasAny(final boolean hasAny)
    {
        this.hasAny_ = hasAny ? 1 : 0;
        return this;
    }

    public TechnologyOnlyEnumWebControl setIndexWebControl(final boolean isIndex)
    {
        this.isIndex_ = isIndex;
        return this;
    }

    private WebControl buildWebControl(final EnumCollection enumc, final boolean autoPreview)
    {
        if (this.isIndex_)
        {
            return new EnumIndexWebControl(enumc, autoPreview);
        }
        else
        {
            return new EnumWebControl(enumc, autoPreview);
        }
    }

    @Override
    public void toWeb(final Context ctx, final PrintWriter out, final String name, final Object obj)
    {
        // TODO 2007-10-18 redesign using EnumWebControl.getEnumCollection() look for other similar implementations
        // TODO redesign so that the Web Controls are not recreated every time, less garbage - less GC

        final LicenseMgr lMgr = (LicenseMgr) ctx.get(LicenseMgr.class);
        if (lMgr != null && lMgr.isLicensed(ctx, CoreCrmLicenseConstants.GSM_LICENSE_KEY)
                && lMgr.isLicensed(ctx, CoreCrmLicenseConstants.TDMA_CDMA_LICENSE_KEY)
                && lMgr.isLicensed(ctx, CoreCrmLicenseConstants.VSAT_PSTN_LICENSE_KEY))
        {
            this.setDelegate(buildWebControl(ALL[hasAny_], autoPreview_));

            if (LogSupport.isDebugEnabled(ctx))
            {
                new DebugLogMsg("License for CDMA, TDMA and GSM Technologies are Active", null, null).log(ctx);
            }
        }
        else if (lMgr != null && lMgr.isLicensed(ctx, CoreCrmLicenseConstants.GSM_LICENSE_KEY)
                && lMgr.isLicensed(ctx, CoreCrmLicenseConstants.TDMA_CDMA_LICENSE_KEY))
        {
            this.setDelegate(buildWebControl(GSM_TDMA[hasAny_], autoPreview_));

            if (LogSupport.isDebugEnabled(ctx))
            {
                new DebugLogMsg("License for GSM Technology is Active", null, null).log(ctx);
            }
        }
        else if (lMgr != null && lMgr.isLicensed(ctx, CoreCrmLicenseConstants.GSM_LICENSE_KEY)
                && lMgr.isLicensed(ctx, CoreCrmLicenseConstants.VSAT_PSTN_LICENSE_KEY))
        {
            this.setDelegate(buildWebControl(GSM_VSAT[hasAny_], autoPreview_));

            if (LogSupport.isDebugEnabled(ctx))
            {
                new DebugLogMsg("License for GSM Technology is Active", null, null).log(ctx);
            }
        }
        else if (lMgr != null && lMgr.isLicensed(ctx, CoreCrmLicenseConstants.TDMA_CDMA_LICENSE_KEY)
                && lMgr.isLicensed(ctx, CoreCrmLicenseConstants.VSAT_PSTN_LICENSE_KEY))
        {
            this.setDelegate(buildWebControl(TDMA_VSAT[hasAny_], autoPreview_));

            if (LogSupport.isDebugEnabled(ctx))
            {
                new DebugLogMsg("License for GSM Technology is Active", null, null).log(ctx);
            }
        }
        else if (lMgr != null && lMgr.isLicensed(ctx, CoreCrmLicenseConstants.GSM_LICENSE_KEY))
        {
            this.setDelegate(buildWebControl(GSM[hasAny_], autoPreview_));

            if (LogSupport.isDebugEnabled(ctx))
            {
                new DebugLogMsg("License for GSM Technology is Active", null, null).log(ctx);
            }
        }
        else if (lMgr != null && lMgr.isLicensed(ctx, CoreCrmLicenseConstants.TDMA_CDMA_LICENSE_KEY))
        {
            this.setDelegate(buildWebControl(TDMA[hasAny_], autoPreview_));

            if (LogSupport.isDebugEnabled(ctx))
            {
                new DebugLogMsg("License for CDMA and TDMA Technologies are Active", null, null).log(ctx);
            }
        }
        else if (lMgr != null && lMgr.isLicensed(ctx, CoreCrmLicenseConstants.VSAT_PSTN_LICENSE_KEY))
        {
            this.setDelegate(buildWebControl(VSAT[hasAny_], autoPreview_));

            if (LogSupport.isDebugEnabled(ctx))
            {
                new DebugLogMsg("License for CDMA and TDMA Technologies are Active", null, null).log(ctx);
            }
        }
        else
        {
            this.setDelegate(buildWebControl(NONE[hasAny_], autoPreview_));

            new MajorLogMsg(this, "No license defined for GSM/TDMA/CDMA, either one of them should be enabled",
                    null).log(ctx);
        }

        super.toWeb(ctx, out, name, obj);
    }

    private boolean autoPreview_;
    private int hasAny_;
    private boolean isIndex_;
}
