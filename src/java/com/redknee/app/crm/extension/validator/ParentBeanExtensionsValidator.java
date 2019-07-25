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
package com.redknee.app.crm.extension.validator;

import java.util.Arrays;
import java.util.Collection;

import com.redknee.framework.xhome.beans.Validatable;
import com.redknee.framework.xhome.beans.Validator;
import com.redknee.framework.xhome.context.Context;

import com.redknee.app.crm.extension.Extension;
import com.redknee.app.crm.extension.ExtensionAware;
import com.redknee.app.crm.support.ExtensionSupportHelper;


/**
 * Validates extensions that must be validated before creating or updating the parent bean.
 *
 * @author aaron.gourley@redknee.com
 * @since 8.2
 */
public class ParentBeanExtensionsValidator implements Validator
{
    public ParentBeanExtensionsValidator()
    {
        this(Extension.class);
    }
    
    public ParentBeanExtensionsValidator(Class<? extends Extension>... extensions)
    {
        extensionsToValidate_ = Arrays.asList(extensions);
    }
    
    /**
     * {@inheritDoc}
     */
    public void validate(Context ctx, Object obj) throws IllegalStateException
    {
        if (obj instanceof ExtensionAware)
        {
            Collection<Extension> extensions = ((ExtensionAware)obj).getExtensions();
            for (Extension extension : extensions)
            {
                if( extension instanceof Validatable )
                {
                    for (Class<? extends Extension> extCls : extensionsToValidate_)
                    {
                        if (extCls.isAssignableFrom(extension.getClass()))
                        {
                            // Put the parent bean in the context because the validatable extension might need it.
                            Context sCtx = ctx.createSubContext();
                            ExtensionSupportHelper.get(sCtx).setParentBean(sCtx, obj);

                            // Execute the extension's internal validation
                            ((Validatable)extension).validate(sCtx);
                            break;
                        }
                    }
                }                
            }
        }
    }

    protected final Collection<Class<? extends Extension>> extensionsToValidate_;
}
