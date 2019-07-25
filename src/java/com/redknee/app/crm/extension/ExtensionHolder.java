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
package com.redknee.app.crm.extension;



/**
 * Abstract extension holder class to implement comparable.
 *
 * @author aaron.gourley@redknee.com
 * @since 8.2
 */
public class ExtensionHolder extends AbstractExtensionHolder
{   
    public final int compareTo(Object other)
    {
        if( other == null
                || !(other instanceof ExtensionHolder)
                || !(this.getExtension() instanceof Comparable)
                || !(((ExtensionHolder)other).getExtension() instanceof Comparable))
        {
            return 1;
        }
        return com.redknee.framework.xhome.beans.XBeans.compare(
                (Comparable) this.getExtension(), 
                (Comparable) ((ExtensionHolder)other).getExtension());
    }
}
