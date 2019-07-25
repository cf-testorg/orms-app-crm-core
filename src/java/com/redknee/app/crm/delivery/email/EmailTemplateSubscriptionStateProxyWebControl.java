/*
 * This code is a protected work and subject to domestic and international
 * copyright law(s). A complete listing of authors of this work is readily
 * available. Additionally, source code is, by its very nature, confidential
 * information and inextricably contains trade secrets and other information
 * proprietary, valuable and sensitive to Redknee. No unauthorized use,
 * disclosure, manipulation or otherwise is permitted, and may only be used in
 * accordance with the terms of the license agreement entered into with Redknee
 * Inc. and/or its subsidiaries.
 * 
 * Copyright (c) Redknee Inc. and its subsidiaries. All Rights Reserved.
 */

package com.redknee.app.crm.delivery.email;

import com.redknee.app.crm.delivery.email.CRMEmailTemplate;
import com.redknee.app.crm.delivery.email.EmailTemplateTypeEnum;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.webcontrol.AbstractWebControl;
import com.redknee.framework.xhome.webcontrol.ProxyWebControl;
import com.redknee.framework.xhome.webcontrol.WebControl;

/**
 * Web control for subscription state. Displays "--N/A--" unless the template
 * type is subscription state change.
 * 
 * @author cindy.wong@redknee.com
 * @since 8.3
 */
public class EmailTemplateSubscriptionStateProxyWebControl extends
        ProxyWebControl
{

    public EmailTemplateSubscriptionStateProxyWebControl(WebControl delegate)
    {
        super(delegate);
    }

    public void toWeb(Context ctx, java.io.PrintWriter p1, String p2, Object p3)
    {
        CRMEmailTemplate template = (CRMEmailTemplate) ctx
                .get(AbstractWebControl.BEAN);
        if (EmailTemplateTypeEnum.STATE_CHANGE_INDEX != template
                .getTemplateType())
        {
            p1.print("--N/A--");
        }
        else
        {
            super.toWeb(ctx, p1, p2, p3);
        }
    }
}
