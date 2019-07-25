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
package com.redknee.app.crm.bean.core;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.redknee.app.crm.bean.AdjustmentInfo;
import com.redknee.app.crm.bean.AdjustmentTypeEnum;
import com.redknee.app.crm.bean.AdjustmentTypeXInfo;
import com.redknee.app.crm.bean.GLCodeMapping;
import com.redknee.app.crm.home.core.CoreAdjustmentTypeHomePipelineFactory;
import com.redknee.app.crm.support.AdjustmentTypeSupportHelper;
import com.redknee.app.crm.support.GLCodeSupportHelper;
import com.redknee.app.crm.xhome.auth.PrincipalAware;
import com.redknee.framework.xhome.beans.Validatable;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.util.snippet.log.Logger;


/**
 * Provides detailed JavaBean implementation for the AdjustmentType model.
 *
 * @author jimmy.ng@redknee.com
 */
public class AdjustmentType extends com.redknee.app.crm.bean.AdjustmentType implements PrincipalAware, Validatable
{
    /**
     * Serial version UID.
     */
    public static final long serialVersionUID = -2821420371730180598L;

    /**
     * Create a new AdjustmentType.
     */
    public AdjustmentType()
    {
        super();
    }


    /**
     * Determine if this AdjustmentType belongs to the given category in the given
     * context.
     *
     * @param ctx
     *            The operating context.
     * @param category
     *            The given category.
     * @return boolean True if this AdjustmentType belongs to the given category; False
     *         otherwise.
     */
    public boolean isInCategory(final Context ctx, final AdjustmentTypeEnum category)
    {
        int oldAdjustmentTypeCode = 0;
        com.redknee.app.crm.bean.AdjustmentType adjustmentType = this;
        final int categoryAdjustmentTypeCode = AdjustmentTypeSupportHelper.get(ctx).getAdjustmentTypeCodeByAdjustmentTypeEnum(ctx,
            category);
        do
        {
            if (categoryAdjustmentTypeCode == adjustmentType.getCode())
            {
                return true;
            }

            oldAdjustmentTypeCode = adjustmentType.getCode();
            adjustmentType = adjustmentType.getParent(ctx);
        }
        while (adjustmentType != null && oldAdjustmentTypeCode != adjustmentType.getCode());

        return false;
    }

    /**
     * Determine if this AdjustmentType belongs to the given category in the given
     * context.
     *
     * @param ctx
     *            The operating context.
     * @param category
     *            The given category.
     * @return boolean True if this AdjustmentType belongs to the given category; False
     *         otherwise.
     */
    public boolean isInOneOfCategories(final Context ctx, AdjustmentTypeEnum... categories)
    {
        int oldAdjustmentTypeCode = 0;
        com.redknee.app.crm.bean.AdjustmentType adjustmentType = this;
        Set<Integer> categoryAdjustmentTypeCodes = new HashSet<Integer>();
        for (int i=0; i<categories.length; i++)
        {
            categoryAdjustmentTypeCodes.add(Integer.valueOf(AdjustmentTypeSupportHelper.get(ctx).getAdjustmentTypeCodeByAdjustmentTypeEnum(ctx,
                    categories[i])));
        }
        do
        {
            if (categoryAdjustmentTypeCodes.contains(Integer.valueOf(adjustmentType.getCode())))
            {
                return true;
            }

            oldAdjustmentTypeCode = adjustmentType.getCode();
            adjustmentType = adjustmentType.getParent(ctx);
        }
        while (adjustmentType != null && oldAdjustmentTypeCode != adjustmentType.getCode());

        return false;
    }

    /**
     * Return the codes of itself and all of its descendants in the given context.
     *
     * @param ctx
     *            The operating context.
     * @return Vector Codes of the AdjusmtmentType itself and all its descendants.
     */
    public Set<Integer> getSelfAndDescendantCodes(final Context ctx)
    {
        final Set<Integer> codes = new HashSet<Integer>();

        final Collection<AdjustmentType> descendants = getDescendants(ctx, true, true);
        descendants.add(this);

        for (final AdjustmentType adjustmentType : descendants)
        {
            // Get descendant codes for this child.
            codes.add(Integer.valueOf(adjustmentType.getCode()));
        }

        return codes;
    }

    public String getGLCodeForSPID(final Context ctx, final int spid)
    {
        String result;
        final Map<Integer, AdjustmentInfo> infoMap = this.getAdjustmentSpidInfo();
        if (infoMap != null)
        {
            final Integer key = Integer.valueOf(spid);
            final AdjustmentInfo info = infoMap.get(key);
            if (info != null)
            {
                result = info.getGLCode();
            }
            else
            {
                result = "MISSING SPID INFO";
                Logger.minor(ctx, this, "Error: Missing information for SPID " + spid + " in Adjustment Type "
                        + this.getCode(), null);
            }
        }
        else
        {
            result = "MISSING INFO MAP";
            Logger.minor(ctx, this, "Error: Missing information Map in Adjustment Type "
                    + this.getCode(), null);
        }
        return result;
    }

    /**
     * Return the all of its descendants in the given context.
     *
     * @param ctx
     *            The operating context.
     * @param recurse
     *            Will there by an additional query for each descendant adjustment
     *            category
     * @param includeCategories
     *            Will the category adjustments be included also
     * @return Vector Codes of the AdjusmtmentType itself and all its descendants.
     */
    public Collection<AdjustmentType> getDescendants(final Context ctx, final boolean recurse,
        final boolean includeCategories)
    {
        final Home home = (Home) ctx.get(CoreAdjustmentTypeHomePipelineFactory.ADJUSTMENT_TYPE_READ_ONLY_HOME);
        Collection<AdjustmentType> adjustmentTypes = null;
        try
        {
            final EQ condition = new EQ(AdjustmentTypeXInfo.PARENT_CODE, Integer.valueOf(getCode()));
            adjustmentTypes = home.select(ctx, condition);
        }
        catch (final HomeException e)
        {
            Logger.minor(ctx, this, "Cannot load Adjustment types that have parent code " + this.getCode(), e);
        }

        if (adjustmentTypes == null)
        {
            adjustmentTypes = new ArrayList<AdjustmentType>();
        }

        if (recurse)
        {
            final Collection<AdjustmentType> resultTypes = new ArrayList<AdjustmentType>(adjustmentTypes.size());

            for (final AdjustmentType adjustmentType : adjustmentTypes)
            {
                if (adjustmentType.isCategory())
                {
                    // Get descendant codes for this child, if it's a category
                    resultTypes.addAll(adjustmentType.getDescendants(ctx, recurse, includeCategories));

                    if (includeCategories)
                    {
                        resultTypes.add(adjustmentType);
                    }
                }
                else
                {
                    resultTypes.add(adjustmentType);
                }
            }
            adjustmentTypes = resultTypes;
        }

        return adjustmentTypes;
    }


    /**
     * {@inheritDoc}
     */
    public void validate(final Context ctx) throws IllegalStateException
    {
        validateGLCodeSelected(ctx);

        validateParentNoCycles(ctx);
    }


    /**
     * Validates that SPID info has a GL code selected.
     *
     * @param ctx
     *            the operating context
     */
    private void validateGLCodeSelected(final Context ctx)
    {
        for (final Integer key : (Set<Integer>) this.adjustmentSpidInfo_.keySet())
        {
            final AdjustmentInfo info = (AdjustmentInfo) this.adjustmentSpidInfo_.get(key);
            if (info.getGLCode() == null || info.getGLCode().length() == 0)
            {
                try
                {
                    final Collection<GLCodeMapping> glCodes = GLCodeSupportHelper.get(ctx).getGLCodes(ctx, info.getSpid());
                    if (glCodes == null || glCodes.size() == 0)
                    {
                        throw new IllegalStateException("No GL Code for Service Provider ID " + info.getSpid()
                            + ". Please configure a GL code for such Spid");
                    }
                    final GLCodeMapping glCode = glCodes.iterator().next();
                    info.setGLCode(glCode.getGlCode());
                }
                catch (final UnsupportedOperationException e)
                {
                    throw new IllegalStateException(e.getMessage(), e);
                }
                catch (final HomeException e)
                {
                    throw new IllegalStateException(e.getMessage(), e);
                }
            }
        }
    }


    /**
     * Validates parent code selection does not create to avoid cycles
     *
     * @param ctx
     *            the operating context
     */
    private void validateParentNoCycles(final Context ctx)
    {
        final List<Integer> tree = new ArrayList<Integer>();
        tree.add(Integer.valueOf(this.getCode()));
        int parentCode = this.getParentCode();
        com.redknee.app.crm.bean.AdjustmentType adjustment = this;
        while (parentCode > 0)
        {
            final Integer codeObject = Integer.valueOf(parentCode);
            if (tree.contains(codeObject))
            {
                adjustment = this.getParent(ctx);
                throw new IllegalStateException("Cannot select parent code \"" + adjustment.getName() + "\" ID="
                    + this.getParentCode() + " because this will create the cycle " + tree);
            }
            adjustment = adjustment.getParent(ctx);
            if (adjustment == null)
            {
                throw new IllegalStateException("Unable to load Adjustment Type " + parentCode);
            }

            tree.add(codeObject);
            parentCode = adjustment.getParentCode();
        }
    }


    /**
     * {@inheritDoc}
     */
    public Principal getPrincipal()
    {
        return this.principal_;
    }


    /**
     * {@inheritDoc}
     */
    public void setPrincipal(final Principal usr)
    {
        this.principal_ = usr;
    }

    /**
     * Return variable for com.redknee.app.crm.bean.PrincipalAware interface
     */
    private Principal principal_;
}
