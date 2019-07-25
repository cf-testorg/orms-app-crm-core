/*
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
package com.redknee.app.crm.web.control;

import java.io.PrintWriter;
import java.util.List;

import com.redknee.framework.xhome.beans.FacetMgr;
import com.redknee.framework.xhome.beans.xi.PropertyInfo;
import com.redknee.framework.xhome.beans.xi.XInfo;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.language.DummyMessageMgrSPI;
import com.redknee.framework.xhome.language.Lang;
import com.redknee.framework.xhome.language.MessageMgrSPI;
import com.redknee.framework.xhome.language.MessageMgrSPIProxy;
import com.redknee.framework.xhome.webcontrol.AbstractWebControl;
import com.redknee.framework.xhome.webcontrol.ProxyWebControl;
import com.redknee.framework.xhome.webcontrol.ViewModeEnum;
import com.redknee.framework.xhome.webcontrol.WebControl;

import com.redknee.app.crm.extension.Extension;
import com.redknee.app.crm.extension.ExtensionHolder;


/**
 * This web control clears the title of the extension and optionally hides fields of extensions.
 * 
 * The extension title should be shown by the containing web control (e.g. TabbedCollectionWebControl).
 *
 * @author Aaron Gourley
 * @since 7.4.16 
 */
public class ExtensionViewCustomizationWebControl extends ProxyWebControl
{
    private final PropertyInfo[] hiddenProperties_;
    
    public ExtensionViewCustomizationWebControl(WebControl delegate, PropertyInfo... hiddenProperties)
    {
        super(delegate);
        hiddenProperties_ = hiddenProperties;
    }

    @Override
    public void toWeb(Context ctx, PrintWriter out, String name, Object obj)
    {
        Context sCtx = ctx.createSubContext();
        
        Extension extension = null;
        if( obj instanceof Extension )
        {
            extension = (Extension)obj;
        }
        else if( obj instanceof ExtensionHolder )
        {
            extension = ((ExtensionHolder)obj).getExtension();
        } 

        if( extension != null )
        {
            // Clear the title.
            final String headerMessageKey = extension.getClass().getSimpleName() + ".Label";
            MessageMgrSPI spi = (MessageMgrSPI)sCtx.get(MessageMgrSPI.class, DummyMessageMgrSPI.instance());
            sCtx.put(MessageMgrSPI.class, new MessageMgrSPIProxy(spi) {
                @Override
                public String get(Context mCtx, String key, Class module, Lang lang, String defaultValue, Object[] args)
                {
                    if( headerMessageKey.equals(key) )
                    {
                        return "";
                    }
                    return super.get(mCtx, key, module, lang, defaultValue, args);
                }
            });
            
            if (hiddenProperties_ != null && hiddenProperties_.length > 0)
            {
                FacetMgr fmgr = (FacetMgr)sCtx.get(FacetMgr.class);
                XInfo xinfo = (XInfo)fmgr.getInstanceOf(sCtx, extension.getClass(), XInfo.class);
                List<PropertyInfo> properties = xinfo.getProperties(sCtx);
                if (properties != null && properties.size() > 0)
                {
                    for (PropertyInfo hiddenProp : hiddenProperties_)
                    {
                        for( PropertyInfo prop : properties )
                        {
                            if (hiddenProp.getName().equalsIgnoreCase(prop.getName()))
                            {
                                AbstractWebControl.setMode(sCtx, prop, ViewModeEnum.NONE);
                                break;
                            }
                        }
                    }
                }
            }
        }

        super.toWeb(sCtx, out, name, obj);
    }

}
