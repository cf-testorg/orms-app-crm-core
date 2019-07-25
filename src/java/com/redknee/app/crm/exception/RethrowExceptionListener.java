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
package com.redknee.app.crm.exception;

import java.util.Iterator;

import com.redknee.framework.xhome.beans.DefaultExceptionListener;
import com.redknee.framework.xhome.beans.ExceptionListener;
import com.redknee.framework.xhome.beans.CompoundIllegalStateException;

/**
 * Copies thrown exceptions to the supplied ExceptionListener.
 * Added to avoid creating a CompoundIllegalStateException when there are no exceptions.
 * Using this class will make the successful calls faster, by not instanciating expansive exceptions without need.
 * But the failed calls will be slower, because of the need to instantiate this class.
 * On average, because we have many Validators that potentially generate unneeded exceptions, using this class will
 * improve performance.
 *
 * @author victor.stratan@redknee.com
 */
public class RethrowExceptionListener extends DefaultExceptionListener
{
    /**
     * Copies thrown exceptions to the supplied ExceptionListener.
     *
     * @param el the exception listener to pass the exceptions to.
     */
    public void rethrow(final ExceptionListener el)
    {
        for (Iterator i = list_.iterator(); i.hasNext();)
        {
            el.thrown((Throwable) i.next());
        }
    }

    public void throwAllAsCompoundException()
    {
        if (this.numOfErrors() > 0)
        {
            final CompoundIllegalStateException el = new CompoundIllegalStateException();
            this.rethrow(el);
            el.throwAll();
        }
    }
}
