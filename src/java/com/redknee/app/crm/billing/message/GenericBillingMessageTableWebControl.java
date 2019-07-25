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
package com.redknee.app.crm.billing.message;

import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletRequest;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.msp.SpidHome;
import com.redknee.framework.xhome.msp.SpidXInfo;
import com.redknee.framework.xhome.webcontrol.AbstractWebControl;
import com.redknee.framework.xhome.webcontrol.OutputWebControl;
import com.redknee.framework.xhome.webcontrol.ViewModeEnum;

import com.redknee.app.crm.CoreCrmLicenseConstants;
import com.redknee.app.crm.bean.BillingMessage;
import com.redknee.app.crm.bean.BillingMessageTableWebControl;
import com.redknee.app.crm.bean.BillingMessageXInfo;
import com.redknee.app.crm.support.LicensingSupportHelper;

/**
 * Webcontrol that will display the BillingMessage records in a editable table 
 * format.
 * 
 * The adaption from the appropriate BillingMessageHome to the Bean using this web-control is handled in
 * the bean's pipeline. In this web control we will just control the displayable fields and suppressing 
 * the actions
 * 
 * @author angie.li@redknee.com
 *
 * @param <MESSSAGE>
 */
public class GenericBillingMessageTableWebControl extends
    BillingMessageTableWebControl 
{
    
    @Override
    public Object fromWeb(Context ctx, ServletRequest req, String name)
            throws NullPointerException 
    {
        return super.fromWeb(ctx, req, name);
    }

    @Override
    public void fromWeb(Context ctx, Object obj, ServletRequest req,
            String name) 
    {
        /* Since SpidLangMessage beans have SPID and Language as Primary keys, the fromWeb() will 
         * only retrieve values in CREATE MODE.  We want to always retrieve the values using this 
         * web control
         */
        Context subCtx = ctx.createSubContext();
        subCtx.put("MODE", OutputWebControl.CREATE_MODE);
        super.fromWeb(subCtx, obj, req, name);
    }

    @Override
    public void toWeb(Context ctx, PrintWriter out, String name, Object obj)
    {
        ArrayList<BillingMessage> list = (ArrayList<BillingMessage>)obj;
        BillingMessageAware bean = (BillingMessageAware) ctx.get(AbstractWebControl.BEAN);
        
        int spid = -1;
        // filters list of languages to set of languages configured for spid
        if (bean != null)
        {
            spid = bean.getSpid();
        }
        
        /* During create Process, the bean's SPID value may change.  This should drive us to discard any 
         * BillingMessage records in our list that have a different SPID. 
         * Instead of going that route, we'll instead force the SPID change upon the records in the list*/ 
        for(BillingMessage record: list)
        {
            record.setSpid(spid);
        }

        final boolean multiLicense = LicensingSupportHelper.get(ctx).isLicensed(ctx, CoreCrmLicenseConstants.MULTI_LANGUAGE);

        Context subCtx = ctx.createSubContext();
        if (list.size() > 0 && !multiLicense)
        {
            // no need for blank rows if no multi-language and one row already present
            subCtx.put(NUM_OF_BLANKS, -1);
        }
        else
        {
            subCtx.put(NUM_OF_BLANKS, 1);
        }

        //list.add(new BillingMessage(spid, "DEFAULT_LANGUAGE", true, "", bean.getIdentifier()));
        subCtx.put("ACTIONS", false);
        subCtx.put("MODE", OutputWebControl.CREATE_MODE);

        if (!multiLicense)
        {
            setMode(ctx, BillingMessageXInfo.LANGUAGE, ViewModeEnum.NONE);
        }
        setMode(subCtx, BillingMessageXInfo.ACTIVE, ViewModeEnum.NONE);

        if (spid > 0)
        {
            final Home oldSpidHome = (Home) subCtx.get(SpidHome.class);
            final EQ where = new EQ(SpidXInfo.ID, Integer.valueOf(spid));
            final Home decoratedHome = oldSpidHome.where(subCtx, where);
            subCtx.put(SpidHome.class, decoratedHome);
        }

        super.toWeb(subCtx, out, name, list);
    }
}
