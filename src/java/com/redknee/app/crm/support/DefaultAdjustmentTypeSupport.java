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

import java.util.HashSet;
import java.util.Set;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.csv.Constants;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xlog.log.LogSupport;

import com.redknee.app.crm.bean.AdjustmentInfo;
import com.redknee.app.crm.bean.AdjustmentTypeCSVSupport;
import com.redknee.app.crm.bean.AdjustmentTypeEnum;
import com.redknee.app.crm.bean.Service;
import com.redknee.app.crm.bean.ServiceHome;
import com.redknee.app.crm.bean.ServiceXInfo;
import com.redknee.app.crm.bean.SystemAdjustTypeMapping;
import com.redknee.app.crm.bean.SystemAdjustTypeMappingHome;
import com.redknee.app.crm.bean.SystemAdjustTypeMappingXInfo;
import com.redknee.app.crm.bean.TaxAuthority;
import com.redknee.app.crm.bean.Transaction;
import com.redknee.app.crm.bean.core.AdjustmentType;
import com.redknee.app.crm.home.core.CoreAdjustmentTypeHomePipelineFactory;


/**
 * Provides utility functions for working with AdjustmentTypes.
 *
 * @author gary.anderson@redknee.com
 */
public class DefaultAdjustmentTypeSupport implements AdjustmentTypeSupport
{
    /**
     * We'll use this flag to turn off debug messages for this class.
     * If you need to do local debugging, set to true, but don't commit a true value.
     */
    private static final boolean LOCAL_DEBUG = false;
    
    protected static AdjustmentTypeSupport instance_ = null;
    public static AdjustmentTypeSupport instance()
    {
        if (instance_ == null)
        {
            instance_ = new DefaultAdjustmentTypeSupport();
        }
        return instance_;
    }

    protected DefaultAdjustmentTypeSupport()
    {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AdjustmentType getAdjustmentType(final Context context, final AdjustmentTypeEnum type)
        throws HomeException
    {
        return getAdjustmentType(context, getAdjustmentTypeCodeByAdjustmentTypeEnum(context, type));
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public AdjustmentType getAdjustmentType(final Context context, final int typeIdentifier)
        throws HomeException
    {
        return getAdjustmentTypeForRead(context, typeIdentifier);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int getAdjustmentTypeCodeByAdjustmentTypeEnum(final Context context,
        final AdjustmentTypeEnum systemType)
    {
        int result = -1;
        boolean found = false;
        try
        {
            final SystemAdjustTypeMapping mapping = HomeSupportHelper.get(context).findBean(context, SystemAdjustTypeMapping.class,
                    Integer.valueOf(systemType.getIndex()));

            if (mapping != null)
            {
                if (LOCAL_DEBUG && LogSupport.isDebugEnabled(context))
                {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("System adjustment type ");
                    sb.append(systemType.getIndex());
                    sb.append(" mapped to adjustment type ");
                    sb.append(mapping.getAdjType());
                    LogSupport.debug(context, this, sb.toString());
                }
                result = mapping.getAdjType();
                found = true;
            }
        }
        catch (final HomeException exception)
        {
            final StringBuilder sb = new StringBuilder();
            sb.append(exception.getClass().getSimpleName());
            sb.append(" caught in  AdjustmentTypeSupport.getAdjustmentTypeCodeByAdjustmentTypeEnum(): ");
            if (exception.getMessage() != null)
            {
                sb.append(exception.getMessage());
            }
            LogSupport.minor(context, this, sb.toString(), exception);
        }

        // return the original system type ID if none found.
        if (!found)
        {
            final StringBuilder sb = new StringBuilder();
            sb.append("Adjustment type mapping not found for system type ");
            sb.append(systemType);
            sb.append(", returning enum index ");
            sb.append(systemType.getIndex());
			LogSupport.info(context, this, sb.toString());
            result = systemType.getIndex();
        }
        return result;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public AdjustmentType getAdjustmentType(final Context context, final Home home, final int adjustmentType)
        throws HomeException
    {
        if (home == null)
        {
            throw new HomeException("System Error: Adjustment Type Home not found");
        }
        return (AdjustmentType) home.find(context, Integer.valueOf(adjustmentType));
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public AdjustmentType getAdjustmentTypeForRead(final Context context, final AdjustmentTypeEnum type)
        throws HomeException
    {
        return getAdjustmentTypeForRead(context, getAdjustmentTypeCodeByAdjustmentTypeEnum(context, type));
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public AdjustmentType getAdjustmentTypeForRead(final Context ctx, final int adj) throws HomeException
    {
        return getAdjustmentType(ctx, getAdjustmentTypeReadOnlyHome(ctx), adj);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public AdjustmentTypeEnum getAdjustmentTypeEnum(final Context context, final short adjustmentTypeEnumIndex)
    {
        AdjustmentTypeEnum systemAdjustmentType = null;
        try
        {
            systemAdjustmentType = AdjustmentTypeEnum.get(adjustmentTypeEnumIndex);
        }
        catch (final ArrayIndexOutOfBoundsException exception)
        {
            final StringBuilder sb = new StringBuilder();
            sb.append(exception.getClass().getSimpleName());
            sb.append(" caught in AdjustmentTypeSupport.getAdjustmentTypeEnum(): ");
            if (exception.getMessage() != null)
            {
                sb.append(exception.getMessage());
            }
            LogSupport.minor(context, this, sb.toString(), exception);
        }

        if (systemAdjustmentType == null)
        {
			LogSupport.info(context, this, "System adjustment type index "
                    + adjustmentTypeEnumIndex + " is invalid");
        }
        return systemAdjustmentType;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getAdjustmentTypeNameDescription(final Context context, final int typeIdentifier)
        throws HomeException
    {
        final AdjustmentType value = getAdjustmentType(context, typeIdentifier);

        if (value == null)
        {
            return "";
        }

        return value.getName() + " - " + value.getDesc();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @Deprecated
    public Set<Integer> getPaymentsButDepositCodes(final Context ctx)
    {
        /*
         * Find all AdjustmentType Codes that belong to Payments category but not Deposit
         * category.
         */
        final Set<Integer> paymentsCodes = getSelfAndDescendantCodes(ctx,
                getAdjustmentTypeCodeByAdjustmentTypeEnum(ctx, AdjustmentTypeEnum.Payments));
        final Set<Integer> depositCodes = getSelfAndDescendantCodes(ctx,
                getAdjustmentTypeCodeByAdjustmentTypeEnum(ctx,
                        AdjustmentTypeEnum.DepositPayments));
        paymentsCodes.removeAll(depositCodes);
        return paymentsCodes;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Integer> getPaymentsCodes(final Context ctx)
    {
        // Find all AdjustmentType Codes that belong to Payments category
        final Set<Integer> paymentsCodes = getSelfAndDescendantCodes(ctx,
            AdjustmentTypeEnum.Payments);
        return paymentsCodes;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Integer> getSelfAndDescendantCodes(final Context ctx, final AdjustmentTypeEnum typeEnum)
    {
        return getSelfAndDescendantCodes(ctx, getAdjustmentTypeCodeByAdjustmentTypeEnum(ctx, typeEnum));
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Integer> getSelfAndDescendantCodes(final Context ctx, final int typeIdentifier)
    {
        AdjustmentType adjustmentType = null;
        try
        {
            adjustmentType = getAdjustmentType(ctx, typeIdentifier);
        }
        catch (final HomeException e)
        {
            LogSupport.minor(ctx, this, "Exception caught when looking up adjustment type "
                    + typeIdentifier, e);
        }

        if (adjustmentType == null)
        {
			LogSupport.info(ctx, this, "Adjustment type " + typeIdentifier
                    + " does not exist");
            return new HashSet<Integer>();
        }
        return adjustmentType.getSelfAndDescendantCodes(ctx);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @Deprecated
    public Service getServiceForThisAdjustmentType(final Context ctx, final int adjustmentType)
    {
        final Home h = (Home) ctx.get(ServiceHome.class);
        Service service = null;
        try
        {
            service = (Service) h.find(ctx, new EQ(ServiceXInfo.ADJUSTMENT_TYPE, Integer.valueOf(adjustmentType)));
        }
        catch (final HomeException exception)
        {
            final StringBuilder sb = new StringBuilder();
            sb.append(exception.getClass().getSimpleName());
            sb.append(" caught in AdjustmentTypeSupport.getServiceForThisAdjustmentType(): ");
            if (exception.getMessage() != null)
            {
                sb.append(exception.getMessage());
            }
            LogSupport.minor(ctx, this, sb.toString(), exception);
        }
        return service;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public AdjustmentTypeEnum getSystemAdjustmentTypeByAdjustmentTypeCode(final Context context,
        final int adjustmentType)
    {
        AdjustmentTypeEnum systemType = null;
        final Home mapHome = (Home) context.get(SystemAdjustTypeMappingHome.class);
        try
        {
            final SystemAdjustTypeMapping mapping = (SystemAdjustTypeMapping) mapHome.find(context, new EQ(
                SystemAdjustTypeMappingXInfo.ADJ_TYPE, Integer.valueOf(adjustmentType)));

            if (mapping != null)
            {
                final StringBuilder sb = new StringBuilder();
                sb.append("Adjustment type ");
                sb.append(adjustmentType);
                sb.append(" mapped to system adjustment type ");
                sb.append(mapping.getSysAdjustmeType());
				LogSupport.info(context, this, sb.toString());
                systemType = getAdjustmentTypeEnum(context, (short) mapping.getSysAdjustmeType());
            }
        }
        catch (final HomeException exception)
        {
            final StringBuilder sb = new StringBuilder();
            sb.append(exception.getClass().getSimpleName());
            sb.append(" caught in AdjustmentTypeSupport.getSystemAdjustmentTypeByAdjustmentTypeCode(): ");
            if (exception.getMessage() != null)
            {
                sb.append(exception.getMessage());
            }
            LogSupport.minor(context, this, sb.toString(), exception);
        }

        if (systemType == null)
        {
            final StringBuilder sb = new StringBuilder();
            sb.append("Mapping from adjustment type ");
            sb.append(adjustmentType);
            sb.append(" to system adjustment type not found.  Assuming ");
            sb.append(adjustmentType);
            sb.append(" is a valid system adjustment type");
			LogSupport.info(context, this, sb.toString());
            systemType = getAdjustmentTypeEnum(context, (short) adjustmentType);
        }
        return systemType;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isInCategory(final Context ctx, final int typeIdentifier, final AdjustmentTypeEnum category)
    {
        AdjustmentType adjustmentType = null;
        boolean result = false;
        try
        {
            adjustmentType = getAdjustmentType(ctx, typeIdentifier);
        }
        catch (final HomeException e)
        {
			LogSupport.minor(ctx, this,
			    "Exception caught when looking up adjustment type "
			        + typeIdentifier, e);
            return false;
        }

        if (adjustmentType != null)
        {
            result = adjustmentType.isInCategory(ctx, category);
        }
        else
        {
            final StringBuilder sb = new StringBuilder();
            sb.append("Adjustment type ");
            sb.append(typeIdentifier);
            sb.append(" cannot be found.");
			LogSupport.info(ctx, this, sb.toString());
        }
        return result;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isInOneOfCategories(final Context ctx, final int typeIdentifier, final AdjustmentTypeEnum... categories)
    {
        AdjustmentType adjustmentType = null;
        boolean result = false;
        try
        {
            adjustmentType = getAdjustmentType(ctx, typeIdentifier);
        }
        catch (final HomeException e)
        {
            LogSupport.minor(ctx, this, "Exception caught when looking up adjustment type "
                    + typeIdentifier, e);
            return false;
        }

        if (adjustmentType != null)
        {
            result = adjustmentType.isInOneOfCategories(ctx, categories);
        }
        else
        {
            final StringBuilder sb = new StringBuilder();
            sb.append("Adjustment type ");
            sb.append(typeIdentifier);
            sb.append(" cannot be found.");
			LogSupport.info(ctx, this, sb.toString());
        }
        return result;
    }

    
    /**
     * {@inheritDoc}
     */
    @Override
    @Deprecated
    public String getAdjustmentTypeCSVString(final Context ctx, final int adjustType) throws HomeException
    {
        // TODO 2007-01-24: this method is not called anywhere
       final StringBuffer sb = new StringBuffer();
        final AdjustmentType type = getAdjustmentType(ctx, adjustType);
        if (type != null)
        {
            final AdjustmentTypeCSVSupport csv = new AdjustmentTypeCSVSupport();
            csv.append(sb, Constants.DEFAULT_SEPERATOR, type);
        }
        return sb.toString();
    }

    
    /**
     * {@inheritDoc}
     */
    private Home getAdjustmentTypeReadOnlyHome(final Context context) throws HomeException
    {
        final Home home = (Home) context.get(CoreAdjustmentTypeHomePipelineFactory.ADJUSTMENT_TYPE_READ_ONLY_HOME);

        if (home == null)
        {
            throw new HomeException("System error: could not find AdjustmentTypeHome in context.");
        }
        return home;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getAdjustmentTypeName(final Context context, final int code)
    {
        AdjustmentType adjustmentType = null;
        try
        {
            adjustmentType = getAdjustmentTypeForRead(context, code);
        }
        catch (final HomeException he)
        {
			LogSupport.minor(context, this,
			    "Error when getting adjustment type " + code, he);
            // ignore
        }

        String result = null;
        if (adjustmentType != null)
        {
            result = adjustmentType.getName();
        }
        return result;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getAdjustmentTypeName(final Context context, final AdjustmentTypeEnum systemType)
    {
        return getAdjustmentTypeName(context, getAdjustmentTypeCodeByAdjustmentTypeEnum(context, systemType));
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getAdjustmentTypeName(final Context ctx, final Transaction trans)
    {
        String adjustmentType = getAdjustmentTypeName(ctx, trans.getAdjustmentType());
        if (adjustmentType == null)
        {
            adjustmentType = String.valueOf(trans.getAdjustmentType());
        }
        return adjustmentType;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getGLCodeForAdjustmentType(final Context ctx, int adjID, int spid)
    {
        String glCode = "";
        try
        {
            AdjustmentType adjustmentType = getAdjustmentType(ctx, adjID);
            AdjustmentInfo info = (AdjustmentInfo) adjustmentType.getAdjustmentSpidInfo().get(Integer.valueOf(spid));
            if (info != null)
            {
                glCode = info.getGLCode();
            }
            else
            {
				LogSupport.info(ctx,
                        "AdjustmentTypeSupport.getGLCodeForAdjustmentType", 
                        "Adjustment Spid Info not found.  [AdjustmentTypeId=" + adjID + "] [Spid=" + spid + "]", 
                        null);
            }
        }
        catch (HomeException e)
        {
            LogSupport.minor(ctx, 
                    "AdjustmentTypeSupport.getGLCodeForAdjustmentType", 
			    "Exception caught while looking up the Adjustment Type for [AdjustmentTypeId="
			        + adjID + "] [Spid=" + spid + "]",
                    e);
        }
        return glCode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<AdjustmentTypeEnum> getHiddenAdjustmentTypes(Context ctx)
    {
        return new HashSet<AdjustmentTypeEnum>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TaxAuthority getTaxAuthorityForSpid(final Context ctx, int adjID, int spid)
    {
        TaxAuthority taxAuth = null;
        try
        {
            AdjustmentType adjustmentType = getAdjustmentType(ctx, adjID);
            AdjustmentInfo info = (AdjustmentInfo) adjustmentType.getAdjustmentSpidInfo().get(Integer.valueOf(spid));
            if (info != null)
            {
                taxAuth = HomeSupportHelper.get(ctx).findBean(ctx, TaxAuthority.class, info.getTaxAuthority());
            }
            else
            {
                LogSupport.info(ctx,
                        "AdjustmentTypeSupport.getTaxAuthorityForSpid", 
                        "Adjustment Spid Info not found.  [AdjustmentTypeId=" + adjID + "] [Spid=" + spid + "]", 
                        null);
            }
        }
        catch (HomeException e)
        {
            LogSupport.minor(ctx, 
                    "AdjustmentTypeSupport.getTaxAuthorityForSpid", 
                    "Exception caught while looking up the Adjustment Type for [AdjustmentTypeId="
                    + adjID + "] [Spid=" + spid + "]",
                    e);
        }
        return taxAuth;
    }
    
} // class
