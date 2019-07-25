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
package com.redknee.app.crm.bean.webcontrol;

import com.redknee.framework.xhome.relationship.RelationshipWebControl;
import com.redknee.framework.xhome.webcontrol.FinalWebControl;
import com.redknee.framework.xhome.webcontrol.WebControl;

import com.redknee.app.crm.bean.ServicePackageWebControl;
import com.redknee.app.crm.technology.TechnologyOnlyEnumWebControl;
import com.redknee.app.crm.web.control.CustomizedGlcodeKeyWebControl;
import com.redknee.app.crm.web.control.ServicePackageVersionWebControllerWebControl;


/**
 * 
 *
 * @author aaron.gourley@redknee.com
 * @since 
 */
public class CustomServicePackageWebControl extends ServicePackageWebControl
{
    /**
     * {@inheritDoc}
     */
    @Override
    public WebControl getTechnologyWebControl()
    {
        return CUSTOM_TECHNOLOGY_WC;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebControl getAdjustmentGLCodeWebControl()
    {
        return CUSTOM_ADJUSTMENT_GL_CODE_WC;
    }
    /**
     * {@inheritDoc}
     */

/*    public RelationshipWebControl getVersionsWebControl()
    {
        return (RelationshipWebControl)CUSTOM_VERSIONS_WC;
    }*/

    public static final WebControl CUSTOM_TECHNOLOGY_WC = new FinalWebControl(new TechnologyOnlyEnumWebControl().setHasAny(true));
    public static final WebControl CUSTOM_ADJUSTMENT_GL_CODE_WC = new com.redknee.framework.xhome.msp.SetSpidProxyWebControl(new CustomizedGlcodeKeyWebControl());
    public static final WebControl CUSTOM_VERSIONS_WC = new com.redknee.framework.xhome.msp.SetSpidProxyWebControl(new ServicePackageVersionWebControllerWebControl().setXmenuKey("appCRMServicePackageVersion"));
}
