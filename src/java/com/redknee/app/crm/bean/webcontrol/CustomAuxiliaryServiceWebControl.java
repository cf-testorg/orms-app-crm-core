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

import com.redknee.framework.xhome.webcontrol.FinalWebControl;
import com.redknee.framework.xhome.webcontrol.WebControl;

import com.redknee.app.crm.bean.AuxiliaryService;
import com.redknee.app.crm.bean.AuxiliaryServiceWebControl;
import com.redknee.app.crm.technology.TechnologyOnlyEnumWebControl;
import com.redknee.app.crm.web.control.CustomizedGlcodeKeyWebControl;


/**
 * 
 *
 * @author aaron.gourley@redknee.com
 * @since 
 */
public class CustomAuxiliaryServiceWebControl extends AuxiliaryServiceWebControl
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
    public WebControl getChargingModeTypeWebControl()
    {
        return CUSTOM_CHARGING_MODE_TYPE_WC;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebControl getGLCodeWebControl()
    {
        return CUSTOM_GL_CODE_WC;
    }


    public static final WebControl CUSTOM_TECHNOLOGY_WC = new FinalWebControl(new TechnologyOnlyEnumWebControl().setHasAny(true));

    public static final WebControl CUSTOM_GL_CODE_WC = new com.redknee.framework.xhome.msp.SetSpidProxyWebControl(new CustomizedGlcodeKeyWebControl());
    public static final WebControl CUSTOM_CHARGING_MODE_TYPE_WC = new com.redknee.app.crm.web.control.ServicePeriodEnumWebControl(true);

}
