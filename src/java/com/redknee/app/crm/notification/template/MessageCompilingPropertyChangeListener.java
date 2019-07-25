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
package com.redknee.app.crm.notification.template;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.context.ContextLocator;

public class MessageCompilingPropertyChangeListener implements PropertyChangeListener
{
    private final CompilableNotificationTemplate template_;

    public MessageCompilingPropertyChangeListener(CompilableNotificationTemplate template)
    {
        template_ = template;
    }

    public void propertyChange(PropertyChangeEvent evt)
    {
        Context ctx = ContextLocator.locate();
        if (template_ != null)
        {
            template_.resetCompiledTemplate(ctx);
            template_.compile(ctx);
        }
    }
}