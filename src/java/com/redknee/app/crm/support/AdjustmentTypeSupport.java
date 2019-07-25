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

import java.util.Set;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;

import com.redknee.app.crm.bean.AdjustmentTypeEnum;
import com.redknee.app.crm.bean.Service;
import com.redknee.app.crm.bean.TaxAuthority;
import com.redknee.app.crm.bean.Transaction;
import com.redknee.app.crm.bean.core.AdjustmentType;


/**
 * Provides utility functions for working with AdjustmentTypes.
 *
 * @author gary.anderson@redknee.com
 */
public interface AdjustmentTypeSupport extends Support
{
    public Set<AdjustmentTypeEnum> getHiddenAdjustmentTypes(Context ctx);
    
    /**
     * Gets the AdjustmentType for the given AdjustmentTypeEnum. Equivalent to
     * <code>getAdjustmentType(context, getAdjustmentTypeCodeByAdjustmentTypeEnum(context, type))</code>.
     *
     * @param context
     *            The operating context.
     * @param type
     *            The type of the AdjustmentType.
     * @return The AdjsutmentType corresponding to the given enumerated type value.
     * @exception HomeException
     *                Thrown if there are any problems accessing data in the context.
     * @see #getAdjustmentType(Context, int)
     * @see #getAdjustmentTypeCodeByAdjustmentTypeEnum(Context, AdjustmentTypeEnum)
     * @see #getAdjustmentTypeForRead(Context, AdjustmentTypeEnum)
     */
    public AdjustmentType getAdjustmentType(final Context context, final AdjustmentTypeEnum type)
        throws HomeException;


    /**
     * Gets the AdjustmentType for the given AdjustmentTypeEnum. Equivalent to
     * <code>getAdjustmentType(context, (Home) context.get(AdjustmentTypeHome.class), typeIdentifier)</code>.
     *
     * @param context
     *            The operating context.
     * @param typeIdentifier
     *            The identifier of the AdjustmentType.
     * @return The AdjsutmentType corresponding to the given enumerated type value.
     * @exception HomeException
     *                Thrown if there are any problems accessing data in the context.
     * @see #getAdjustmentType(Context, Home, int)
     * @see #getAdjustmentType(Context, AdjustmentTypeEnum)
     * @see #getAdjustmentTypeForRead(Context, int)
     */
    public AdjustmentType getAdjustmentType(final Context context, final int typeIdentifier)
        throws HomeException;


    /**
     * Returns the adjustment type code of the provided system adjustment type enum. If no
     * mapping is found, assumes the adjustment type with the same ID as the system
     * adjustment type enum index.
     *
     * @param context
     *            The operating context.
     * @param systemType
     *            System adjustment type enum.
     * @return The adjustment type code corresponding to the provided system adjustment
     *         type, or the original index of the adjustment type enum if no mapping is
     *         found.
     */
    public int getAdjustmentTypeCodeByAdjustmentTypeEnum(final Context context,
        final AdjustmentTypeEnum systemType);


    /**
     * Returns an adjustment type from the provided home.
     *
     * @param context
     *            The operating context.
     * @param home
     *            Adjustment type home.
     * @param adjustmentType
     *            Adjustment type code to look up.
     * @return The adjustment type with the provided code, or <code>null</code> if none
     *         is found.
     * @throws HomeException
     *             Thrown if there are problems looking up the adjustment type.
     */
    public AdjustmentType getAdjustmentType(final Context context, final Home home, final int adjustmentType)
        throws HomeException;


    /**
     * Returns a system adjustment type from the read-only home. Equivalent to
     * <code>getAdjustmentTypeForRead(context, getAdjustmentTypeCodeByAdjustmentTypeEnum(context, type))</code>.
     *
     * @param context
     *            The operating context.
     * @param type
     *            Adjustment type enum to look up.
     * @return The adjustment type corresponding to the provided system type enum.
     * @throws HomeException
     *             Thrown if there are problems retrieving the adjustment type.
     * @see #getAdjustmentType(Context, AdjustmentTypeEnum)
     * @see #getAdjustmentTypeForRead(Context, int)
     * @see #getAdjustmentTypeCodeByAdjustmentTypeEnum(Context, AdjustmentTypeEnum)
     */
    public AdjustmentType getAdjustmentTypeForRead(final Context context, final AdjustmentTypeEnum type)
        throws HomeException;


    /**
     * Returns a adjustment type from the read-only home.
     *
     * @param ctx
     *            The operating context.
     * @param adj
     *            Adjustment type code to look up.
     * @return The adjustment type with the provided adjustment type code.
     * @throws HomeException
     *             Thrown if there are problems retrieving the adjustment type.
     * @see #getAdjustmentType(Context, Home, int)
     * @see #getAdjustmentType(Context, int)
     * @see #getAdjustmentTypeForRead(Context, AdjustmentTypeEnum)
     */
    public AdjustmentType getAdjustmentTypeForRead(final Context ctx, final int adj) throws HomeException;


    /**
     * Returns the adjustment type enum with the provided index. This method is guarded
     * against {@link ArrayIndexOutOfBoundsException}, which may be thrown by
     * {@link AdjustmentTypeEnum#get(short)} if the provided index is out of bounds.
     *
     * @param context
     *            The operating context.
     * @param adjustmentTypeEnumIndex
     *            Enum index.
     * @return The adjustment type enum with the provided index, or <code>null</code> if
     *         none exists.
     */
    public AdjustmentTypeEnum getAdjustmentTypeEnum(final Context context, final short adjustmentTypeEnumIndex);


    /**
     * Returns the 'Name-Description' of the AdjustmentType for the given
     * AdjustmentTypeEnum or blank if it's not found.
     *
     * @param context
     *            The operating context.
     * @param typeIdentifier
     *            The identifier of the AdjustmentType.
     * @return Name and description of the adjustment type in "name - description" form.
     * @exception HomeException
     *                Thrown if there are any problems accessing data in the context.
     */
    public String getAdjustmentTypeNameDescription(final Context context, final int typeIdentifier)
        throws HomeException;


    /**
     * Return payment codes, excluding deposit payments.
     *
     * @param ctx
     *            The operating context.
     * @return Set of payment adjustment type codes.
     * @deprecated Deposit Payments does not belong to Payments category any more. Use
     *             {@link #getPaymentsCodes(Context)} instead.
     */
    @Deprecated
    public Set<Integer> getPaymentsButDepositCodes(final Context ctx);


    /**
     * Return payment codes, excluding deposit payments.
     *
     * @param ctx
     *            The operating context.
     * @return Set of payment adjustment type codes.
     */
    public Set<Integer> getPaymentsCodes(final Context ctx);


    /**
     * Return the code of the given AdjustmentType as well as those of all of its
     * descendants in the given context.
     *
     * @param ctx
     *            The operating context.
     * @param typeEnum
     *            The system adjustment type enum.
     * @return Vector Codes of the AdjusmtmentType and all of its descendants.
     */
    public Set<Integer> getSelfAndDescendantCodes(final Context ctx, final AdjustmentTypeEnum typeEnum);


    /**
     * Return the code of the given AdjustmentType as well as those of all of its
     * descendants in the given context.
     *
     * @param ctx
     *            The operating context.
     * @param typeIdentifier
     *            The identifier of the AdjustmentType.
     * @return Vector Codes of the AdjusmtmentType and all of its descendants.
     */
    public Set<Integer> getSelfAndDescendantCodes(final Context ctx, final int typeIdentifier);


    /**
     * Returns the service associated with the provided adjustment type.
     *
     * @param ctx
     *            The operating context.
     * @param adjustmentType
     *            Adjustment type to search for.
     * @return The service associated with the provided adjustment type.
     * @deprecated Use {@link ServiceSupport#getServiceByAdjustment(Context, int)}
     *             instead.
     */
    @Deprecated
    public Service getServiceForThisAdjustmentType(final Context ctx, final int adjustmentType);


    /**
     * Returns the system adjustment type mapped to the provided adjustment type code.
     * Basically, it is the reverse of
     * {@link #getAdjustmentTypeCodeByAdjustmentTypeEnum(Context, AdjustmentTypeEnum)}.
     * Note: If the mapping is not one-to-one, the result is indeterministic.
     *
     * @param context
     *            The operating context.
     * @param adjustmentType
     *            Adjustment type code.
     * @return System adjustment type mapped to the provided adjustment type code, or
     *         <code>null</code> if none exists.
     */
    public AdjustmentTypeEnum getSystemAdjustmentTypeByAdjustmentTypeCode(final Context context,
        final int adjustmentType);


    /**
     * Determine if the given AdjustmentType belongs to the given category in the given
     * context.
     *
     * @param ctx
     *            The operating context.
     * @param typeIdentifier
     *            The identifier of the AdjustmentType.
     * @param category
     *            The given category.
     * @return boolean True if the AdjustmentType belongs to the given category; False
     *         otherwise.
     */
    public boolean isInCategory(final Context ctx, final int typeIdentifier, final AdjustmentTypeEnum category);


    /**
     * Determine if the given AdjustmentType belongs to the one of the given categories in the given
     * context.
     *
     * @param ctx
     *            The operating context.
     * @param typeIdentifier
     *            The identifier of the AdjustmentType.
     * @param category
     *            The given category.
     * @return boolean True if the AdjustmentType belongs to the given category; False
     *         otherwise.
     */
    public boolean isInOneOfCategories(final Context ctx, final int typeIdentifier, final AdjustmentTypeEnum... categories);

    /**
     * Formats the adjustment type into a CSV string.
     *
     * @param ctx
     *            THe operating context.
     * @param adjustType
     *            Adjustment type code.
     * @return CSV string representation of the adjustment type.
     * @throws HomeException
     *             Thrown if there are problems finding the adjustment type.
     * @deprecated No reference found in current code base.
     */
    @Deprecated
    public String getAdjustmentTypeCSVString(final Context ctx, final int adjustType) throws HomeException;


    /**
     * Returns the name of the adjustment type.
     *
     * @param context
     *            The operating context.
     * @param code
     *            Adjustment type code.
     * @return Name of the adjustment type; if the adjustment type is not found, returns
     *         <code>null</code>.
     */
    public String getAdjustmentTypeName(final Context context, final int code);

    /**
     * Returns the name of the adjustment type.
     *
     * @param context
     *            The operating context.
     * @param systemType
     *            System adjustment type enum.
     * @return Name of the adjustment type; if the adjustment type is not found, returns
     *         <code>null</code>.
     */
    public String getAdjustmentTypeName(final Context context, final AdjustmentTypeEnum systemType);


    /**
     * Returns the name of the adjustment type used in the transaction.
     *
     * @param ctx
     *            The operating context.
     * @param trans
     *            Transaction to be looked up.
     * @return Name of the adjustment type. If the adjustment type does not exist, return
     *         the adjustment type code specified in the transaction.
     */
    public String getAdjustmentTypeName(final Context ctx, final Transaction trans);

    /**
     * Returns the GL code for the given Adjustment Type id
     * @param ctx
     * @param adjID Adjustment Type ID
     * @param spid The service provider id
     * @return
     */
    public String getGLCodeForAdjustmentType(final Context ctx, int adjID, int spid);
    
    /**
     * Returns the Tax Authority for the given Adjustment Type id
      * @param ctx
     * @param adjID Adjustment Type ID
     * @param spid The service provider id
     * @return
     * */
    public TaxAuthority getTaxAuthorityForSpid(final Context ctx, int adjID, int spid);
    
} // class
