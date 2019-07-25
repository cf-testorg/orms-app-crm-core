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

import com.redknee.framework.xhome.webcontrol.WebControl;

import com.redknee.app.crm.delivery.email.CRMEmailTemplateTableWebControl;


/**
 * 
 *
 * @author aaron.gourley@redknee.com
 * @since 
 */
public class CustomCRMEmailTemplateTableWebControl extends CRMEmailTemplateTableWebControl
{

    /**
     * {@inheritDoc}
     */
    @Override
    public WebControl getPreviousStateWebControl()
    {
        return CustomCRMEmailTemplateWebControl.CUSTOM_PREVIOUS_STATE_WC;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public WebControl getNewStateWebControl()
    {
        return CustomCRMEmailTemplateWebControl.CUSTOM_NEW_STATE_WC;
    }
}
