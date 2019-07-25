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
package com.redknee.app.crm.log;

import com.redknee.framework.xhome.beans.AbstractBean;
import com.redknee.framework.xhome.msp.SpidAware;
import com.redknee.framework.xlog.er.ER;


/**
 * Provides a simple abstract class for supporting model-based ER definition.
 * 
 * @author gary.anderson@redknee.com
 */
public abstract class AbstractER extends AbstractBean implements ER, SpidAware
{
    /**
     * {@inheritDoc}
     */
    public int getSpid()
    {
        return spid_;
    }


    /**
     * {@inheritDoc}
     */
    public long getTimestamp()
    {
        return timestamp_;
    }


    /**
     * {@inheritDoc}
     */
    public void setSpid(final int spid)
    {
        spid_ = spid;
    }


    /**
     * {@inheritDoc}
     */
    public void setTimestamp(final long timestamp)
    {
        timestamp_ = timestamp;
    }


    /**
     * The service provider ID.
     */
    protected int spid_;


    /**
     * The time of generation of the ER (set automatically by ERLogMsg).
     */
    protected long timestamp_;


    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;

}