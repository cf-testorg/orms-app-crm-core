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

import java.util.Collection;

import com.redknee.app.crm.bean.SupplementaryData;
import com.redknee.app.crm.bean.SupplementaryDataEntityEnum;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.HomeException;

/**
 * A set of utility method for use with SupplementaryData objects.
 *
 * @author Marcio Marques
 * @since 9.1.3
 */
public interface SupplementaryDataSupport extends Support
{

    /**
     * Retrieve all supplementary data objects for a given entity identified by the given identifier
     * @param context
     * @param entity
     * @param identifier
     * @return
     * @throws HomeException
     */
    public Collection<SupplementaryData> getSupplementaryData(Context context, SupplementaryDataEntityEnum entity,
            String identifier) throws HomeException;

   
    /**
     * Retrieve a given supplementary data objects for a given entity identified by the given identifier
     * @param context
     * @param entity
     * @param identifier
     * @param key
     * @return
     * @throws HomeException
     */
    public SupplementaryData getSupplementaryData(Context context, SupplementaryDataEntityEnum entity,
            String identifier, String key) throws HomeException;


    /**
     * Add or update a supplementary data object to a given entity identified by the given identifier
     * @param context
     * @param entity
     * @param identifier
     * @param key
     * @param value
     * @throws HomeException
     */
    public void addOrUpdateSupplementaryData(Context context, SupplementaryDataEntityEnum entity, String identifier,
            String key, String value) throws HomeException;


    /**
     * Remove  a supplementary data object from a given entity identified by a given identifier
     * @param context
     * @param entity
     * @param identifier
     * @param key
     * @throws HomeException
     */
    public void removeSupplementaryData(Context context, SupplementaryDataEntityEnum entity, String identifier,
            String key) throws HomeException;


    /**
     * Add or update a supplementary data object
     * @param context
     * @param supplementaryData
     * @throws HomeException
     */
    public void addOrUpdateSupplementaryData(Context context, SupplementaryData supplementaryData) throws HomeException;


    /**
     * Remove  a supplementary data object
     * @param context
     * @param supplementaryData
     * @throws HomeException
     */
    public void removeSupplementaryData(Context context, SupplementaryData supplementaryData) throws HomeException;


    /**
     * Remove  all supplementary data object from a given entity identified by a given identifier
     * @param context
     * @param entity
     * @param identifier
     * @throws HomeException
     */
    public void removeAllSupplementaryData(Context context, SupplementaryDataEntityEnum entity, String identifier)
            throws HomeException;

}
