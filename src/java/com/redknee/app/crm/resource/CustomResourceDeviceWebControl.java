/*
 * This code is a protected work and subject to domestic and international copyright
 * law(s). A complete listing of authors of this work is readily available. Additionally,
 * source code is, by its very nature, confidential information and inextricably contains
 * trade secrets and other information proprietary, valuable and sensitive to Redknee. No
 * unauthorized use, disclosure, manipulation or otherwise is permitted, and may only be
 * used in accordance with the terms of the license agreement entered into with Redknee
 * Inc. and/or its subsidiaries.
 * 
 * Copyright (c) Redknee Inc. and its subsidiaries. All Rights Reserved.
 */
package com.redknee.app.crm.resource;

import com.redknee.framework.xhome.webcontrol.HiddenWebControl;
import com.redknee.framework.xhome.webcontrol.ReadOnlyWebControl;
import com.redknee.framework.xhome.webcontrol.WebControl;

import com.redknee.app.crm.resource.ResourceDeviceWebControl;
import com.redknee.app.crm.technology.TechnologyOnlyEnumWebControl;
import com.redknee.app.crm.web.control.SetTechnologyFromGroupWebControl;

/**
 * 
 * 
 * @author aaron.gourley@redknee.com
 * @since
 */
public class CustomResourceDeviceWebControl extends ResourceDeviceWebControl
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
    public WebControl getStateWebControl()
    {
        return CUSTOM_STATE_WC;
    }

    public static final WebControl CUSTOM_TECHNOLOGY_WC = new HiddenWebControl(new ReadOnlyWebControl(
            new com.redknee.framework.xhome.msp.SetSpidProxyWebControl(new SetTechnologyFromGroupWebControl(
                    new TechnologyOnlyEnumWebControl(false, true, true)))));
    public static final WebControl CUSTOM_STATE_WC = new ResourceDeviceStateEnumIndexWebControl();
}
