package com.redknee.app.crm.bundle.web;

import java.io.PrintWriter;


import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.webcontrol.AbstractWebControl;
import com.redknee.framework.xhome.webcontrol.EnumWebControl;
import com.redknee.framework.xhome.webcontrol.ProxyWebControl;
import com.redknee.framework.xhome.webcontrol.WebControl;
import com.redknee.framework.xhome.xenum.Enum;
import com.redknee.framework.xhome.xenum.EnumCollection;

import com.redknee.app.crm.bean.core.BundleProfile;
import com.redknee.app.crm.bundle.BundleSegmentEnum;
import com.redknee.app.crm.bundle.BundleTypeEnum;
import com.redknee.app.crm.bundle.QuotaTypeEnum;

/**
 * Select QuotaTypeEnum subset based on the bundle profile segment
 * type: postpaid/prepaid.
 *
 * @author victor.stratan@redknee.com
 */
public class QuotaSchemeWebControl extends ProxyWebControl
{

    /**
     * Accepts the enums to display in the control
     * @param enumPrepaid
     * @param enumPostpaid
     * @param enumHybrid
     * @param autoPreview
     */
    public QuotaSchemeWebControl(Enum[] enumPrepaid, Enum[] enumPostpaid, Enum[] enumHybrid, boolean autoPreview)
    {
        EnumCollection quotaSchemePrepaidCollection = new EnumCollection(enumPrepaid);
        EnumCollection quotaSchemePostpaidCollection = new EnumCollection(enumPostpaid);
        EnumCollection quotaSchemeHybridCollection = new EnumCollection(enumHybrid);

        prepaidControl_ = new EnumWebControl(quotaSchemePrepaidCollection, autoPreview);
        postpaidControl_ = new EnumWebControl(quotaSchemePostpaidCollection, autoPreview);
        hybridControl_ = new EnumWebControl(quotaSchemeHybridCollection, autoPreview);

        setDelegate(prepaidControl_);
    }

    /**
     * {@inheritDoc}
     */
    public void toWeb(Context ctx, PrintWriter p1, String p2, Object p3)
    {
        BundleProfile bean = (BundleProfile) ctx.get(AbstractWebControl.BEAN);

        final WebControl current = getDelegate();
        if (bean.getSegment() == BundleSegmentEnum.PREPAID)
        {
            if (current != prepaidControl_)
            {
                setDelegate(prepaidControl_);
                bean.setQuotaScheme(QuotaTypeEnum.FIXED_QUOTA);
            }
        }
        else
        {
            if (current != postpaidControl_)
            {
                setDelegate(postpaidControl_);
                bean.setQuotaScheme(QuotaTypeEnum.FIXED_QUOTA);
            }
        }

        super.toWeb(ctx, p1, p2, p3);
    }

    final EnumWebControl prepaidControl_;
    final EnumWebControl postpaidControl_;
    final EnumWebControl hybridControl_;
}
