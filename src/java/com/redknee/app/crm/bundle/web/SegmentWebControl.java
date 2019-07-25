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
package com.redknee.app.crm.bundle.web;

import java.io.PrintWriter;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.webcontrol.AbstractWebControl;
import com.redknee.framework.xhome.webcontrol.ProxyWebControl;
import com.redknee.framework.xhome.webcontrol.RadioButtonWebControl;
import com.redknee.framework.xhome.webcontrol.WebControl;
import com.redknee.framework.xhome.xenum.Enum;
import com.redknee.framework.xhome.xenum.EnumCollection;

import com.redknee.app.crm.bean.core.BundleProfile;
import com.redknee.app.crm.bundle.BundleSegmentEnum;
import com.redknee.app.crm.bundle.BundleTypeEnum;

/**
 * Select SegmentTypeEnum subset based on the bundle profile category.
 *
 * @author victor.stratan@redknee.com
 */
public class SegmentWebControl extends ProxyWebControl
{
    public SegmentWebControl(final boolean autoPreview)
    {
        final Enum[] enumSimple = new Enum[]{BundleSegmentEnum.PREPAID, BundleSegmentEnum.POSTPAID};
        final Enum[] enumPoints = new Enum[]{BundleSegmentEnum.HYBRID};

        final EnumCollection simpleCollection = new EnumCollection(enumSimple);
        final EnumCollection pointsCollection = new EnumCollection(enumPoints);

        simpleControl_ = new RadioButtonWebControl(simpleCollection, autoPreview);
        pointsControl_ = new RadioButtonWebControl(pointsCollection, autoPreview);
        completeControl_ = new RadioButtonWebControl(BundleSegmentEnum.COLLECTION, autoPreview);

        setDelegate(simpleControl_);
    }

    /**
     * Switch between different sets of segmentation Enum based on the bundle type.
     * {@inheritDoc}
     */
    public void toWeb(final Context ctx, final PrintWriter p1, final String p2, final Object p3)
    {
        final int mode = ctx.getInt("MODE", DISPLAY_MODE);
        if (mode != DISPLAY_MODE)
        {
            final BundleProfile bean = (BundleProfile) ctx.get(AbstractWebControl.BEAN);

            final WebControl current = getDelegate();
            if (bean.getType() == BundleTypeEnum.POINTS_INDEX)
            {
                if (current != pointsControl_)
                {
                    setDelegate(pointsControl_);
                    // there is another set just like this in the model file
                    bean.setSegment(BundleSegmentEnum.HYBRID);
                }
            }
            else
            {
                if (current != simpleControl_)
                {
                    setDelegate(simpleControl_);
                    bean.setSegment(BundleSegmentEnum.PREPAID);
                }
            }
        }
        else
        {
            setDelegate(completeControl_);
        }

        super.toWeb(ctx, p1, p2, p3);
    }

    private final RadioButtonWebControl simpleControl_;
    private final RadioButtonWebControl pointsControl_;
    private final RadioButtonWebControl completeControl_;
}
