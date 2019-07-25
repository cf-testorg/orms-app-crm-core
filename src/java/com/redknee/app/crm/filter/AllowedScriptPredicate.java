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
package com.redknee.app.crm.filter;

import com.redknee.framework.core.bean.ScriptLanguageEnum;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.filter.Predicate;


/**
 * A predicate used to allow only Beanshell,Javashell,Groovy as the script language choice.
 * @author abaid
 * 
 */
public class AllowedScriptPredicate implements Predicate
{

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.redknee.framework.xhome.filter.Predicate#f(com.redknee.framework.xhome.context
     * .Context, java.lang.Object)
     */
    public boolean f(final Context ctx, final Object obj)
    {
        final ScriptLanguageEnum lang = (ScriptLanguageEnum) obj;
        boolean allowed = false;
        if (lang.getIndex() == ScriptLanguageEnum.BSH_INDEX
                || lang.getIndex() == ScriptLanguageEnum.JSH_INDEX)
        {
            allowed = true;
        }
        return allowed;
    }
}
