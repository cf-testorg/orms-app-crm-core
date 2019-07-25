package com.redknee.app.crm.filter;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.filter.Predicate;
import com.redknee.framework.xhome.visitor.AbortVisitException;
import com.redknee.framework.xhome.webcontrol.OutputWebControl;

import com.redknee.app.crm.extension.ExtensionHolder;
import com.redknee.app.crm.extension.FinalExtension;


public class FinalExtensionPredicate implements Predicate
{

    @Override
    public boolean f(Context ctx, Object obj) throws AbortVisitException
    {
        boolean result = false;
        if (obj instanceof FinalExtension
                || ((obj instanceof ExtensionHolder) && ((ExtensionHolder)obj).getExtension() instanceof FinalExtension))
        {
            result = OutputWebControl.CREATE_MODE != ctx.getInt("MODE", OutputWebControl.DISPLAY_MODE);
        }
        return result;
    }
}
