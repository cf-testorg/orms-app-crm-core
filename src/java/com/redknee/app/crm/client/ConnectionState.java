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
package com.redknee.app.crm.client;

/**
 * Provides connection states of clients.
 *
 * @author victor.stratan@redknee.com
 */
public enum ConnectionState
{
    /**
     * The client has not yet been initialized; has never been used.
     */
    UNINITIALIZED {@Override public String toString(){return "Uninitialized";}},

    /**
     * The client's connection is up, so far as it can tell.
     */
    UP            {@Override public String toString(){return "Alive";}},

    /**
     * The client's connection is down, and needs to be reinitialized.
     */
    DOWN          {@Override public String toString(){return "Dead";}}
}
