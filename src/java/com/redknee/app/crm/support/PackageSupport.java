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
package com.redknee.app.crm.support;

import com.redknee.app.crm.bean.core.GSMPackage;
import com.redknee.app.crm.bean.core.TDMAPackage;
import com.redknee.app.crm.bean.core.VSATPackage;
import com.redknee.app.crm.numbermgn.GenericPackage;
import com.redknee.app.crm.technology.TechnologyEnum;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.HomeInternalException;


/**
 * @author jchen
 * This support class is not a good choice for the model. New Package Types (GSM, VSAT etc) get added and the support for them may be missed.
 * To reduce the chances of missing support of new Package Types, making this support class implement Package-Processor.
 */
public interface PackageSupport extends Support
{
    /**
     * Gets the package of the given technology type and identifer.
     *
     * @param context The operating context.
     * @param technology The type of card technology.
     * @param identifier The identifier of the package.
     */
    public GenericPackage getPackage(
        final Context context,
        final TechnologyEnum technology,
        final String identifier, int spid)
        throws HomeException;
    
    public TDMAPackage getTDMAPackage(Context ctx, final String number, final int spid)
        throws HomeException;
    
    public VSATPackage getVSATPackage(Context ctx, final String number)
        throws HomeException;
    
    public GSMPackage getGSMPackage(Context ctx, final String number)
    throws HomeException;
    
    public void setPackageState(final Context ctx, final String number,TechnologyEnum tech, final int newState, int spid) throws HomeException, HomeInternalException;

    /**
     * Retrieves the IMSI or MIN for the informed package based on the informed technology.
     * @param ctx Context object.
     * @param packageId Package identifier.
     * @param technology Technology.
     * @return Subscriber IMSI or MIN.
     * @throws HomeException
     */
    public String retrieveIMSIorMIN(Context ctx, String packageId, TechnologyEnum technology, int spid) throws HomeException;

    /**
     * Some parts of the code, like the Subscriber.IMSI field, treat the GSM
     * IMSI as interchangable with the TDMA/CDMA MIN field.  This method
     * abstracts the specifics of searching the card package tables for an entry
     * that matches IMSI or MIN.
     *
     * @param context The operating context.
     * @param technology The car package technology.
     * @param imsiOrMin The IMSI or MIN on which to find.
     * @param spid The SPID along with The IMSI or MIN on which to find.
     * @return The package that matches the IMSI or MIN; or null if no such card
     * package is found.
     *
     * @exception HomeException Thrown if there are problems accessing the Home
     * data in the context.
     */
    public GenericPackage lookupPackageForIMSIOrMIN(
        final Context context,
        final TechnologyEnum technology,
        final String imsiOrMin, 
        final int spid)
        throws HomeException;
    
    /**
     * @param ctx
     * @param technologyType
     * @return
     */
    public Home returnPackageHomeBasedOnTechnology(Context ctx , TechnologyEnum technologyType);
    
}
