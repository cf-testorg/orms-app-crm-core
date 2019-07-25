/*
 * SupportedUnitTypesWebControl.java
 *
 * Author : victor.stratan@redknee.com
 * Date: Sep 6, 2006
 *
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
package com.redknee.app.crm.bundle.web;

import com.redknee.framework.xhome.webcontrol.*;
import com.redknee.framework.xhome.xenum.Enum;
import com.redknee.framework.xhome.xenum.EnumCollection;
import com.redknee.app.crm.bundle.UnitTypeEnum;

/**
 * Show only supported Unit Types in the selection dropdown
 *
 * @author victor.stratan@redknee.com
 */
public class SupportedUnitTypesWebControl extends ProxyWebControl
{
    public SupportedUnitTypesWebControl(boolean autoPreview)
    {
        Enum[] units = new Enum[]{UnitTypeEnum.VOLUME_SECONDS, UnitTypeEnum.VOLUME_BYTES,
                UnitTypeEnum.EVENT_SMS_MMS,UnitTypeEnum.EVENT_GENERIC, UnitTypeEnum.SECONDARY_BALANCE};
        EnumCollection enumCollection = new EnumCollection(units);
        EnumWebControl wc = new EnumWebControl(enumCollection, autoPreview);

        setDelegate(wc);
    }
}
