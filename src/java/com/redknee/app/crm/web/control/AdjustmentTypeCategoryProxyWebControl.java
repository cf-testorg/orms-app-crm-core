/*
 * This code is a protected work and subject to domestic and international
 * copyright law(s). A complete listing of authors of this work is readily
 * available. Additionally, source code is, by its very nature, confidential
 * information and inextricably contains trade secrets and other information
 * proprietary, valuable and sensitive to Redknee. No unauthorized use,
 * disclosure, manipulation or otherwise is permitted, and may only be used in
 * accordance with the terms of the license agreement entered into with Redknee
 * Inc. and/or its subsidiaries.
 * 
 * Copyright (c) Redknee Inc. and its subsidiaries. All Rights Reserved.
 */

package com.redknee.app.crm.web.control;

import java.util.ArrayList;
import java.util.List;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.visitor.Visitor;
import com.redknee.framework.xhome.webcontrol.ProxyWebControl;
import com.redknee.framework.xhome.webcontrol.WebControl;
import com.redknee.framework.xlog.log.DebugLogMsg;

import com.redknee.app.crm.bean.AdjustmentTypeEnum;
import com.redknee.app.crm.bean.AdjustmentTypeHome;
import com.redknee.app.crm.bean.AdjustmentTypeTransientHome;
import com.redknee.app.crm.bean.core.AdjustmentType;
import com.redknee.app.crm.home.core.CoreAdjustmentTypeHomePipelineFactory;
import com.redknee.app.crm.support.AdjustmentTypeSupportHelper;

/**
 * This web control only display adjustment types belonging to a certain
 * category.
 * 
 * @author cindy.wong@redknee.com
 */
public class AdjustmentTypeCategoryProxyWebControl extends ProxyWebControl
{
    /**
     * Category to display.
     */
    private final AdjustmentTypeEnum category_;

    /**
     * Visitor to Adjustment Type Home for filtering adjustment types based on
     * categories. This is necessary because Home.where() does not (yet) support
     * selecting objects within a category recursively.
     * 
     * @author cindy.wong@redknee.com
     */
    private static class AdjustmentTypeCategoryVisitor implements Visitor
    {
        /**
         * Serial Version UID.
         */
        private static final long serialVersionUID = 7361218797230033894L;


        /**
        * Category to display.
        */
        private final AdjustmentTypeEnum category_;
        
        /**
         * Adjustment type category.
         */
        private final AdjustmentType categoryType_;

        /**
         * The list of all valid adjustment types.
         */
        private final List<AdjustmentType> adjustmentTypes_;

        /**
         * Creates a new visitor which selects all Adjustment Types with a
         * specific ancestor in the hierarchy.
         * 
         * @param category
         *            The ancestor to search for.
         * @throws HomeException
         * Thrown if cannont find the adjustmenttype
         */
        public AdjustmentTypeCategoryVisitor(final Context context, final AdjustmentTypeEnum category)
                throws HomeException
        {
            category_ = category;
            adjustmentTypes_ = new ArrayList<AdjustmentType>();
            categoryType_ = AdjustmentTypeSupportHelper.get(context).getAdjustmentType(context, category);
            if (categoryType_ == null)
            {
                throw new HomeException("Cannot find adjustment type category mapped to AdjustmentTypeEnum "
                        + category_);
            }
        }

        /**
         * {@inheritDoc}
         */
        public void visit(final Context context, final Object object)
        {
            final AdjustmentType adjustmentType = (AdjustmentType) object;
            if (adjustmentType.isInCategory(context, category_)
                    && adjustmentType.getCode() != categoryType_.getCode()
                    && !adjustmentType.isCategory())
            {
                adjustmentTypes_.add(adjustmentType);
            }
        }

        /**
         * Returns a list of all adjustment types in the category.
         * 
         * @return A list of all adjustment types in the category hierarchy.
         */
        public List<AdjustmentType> getAdjustmentTypes()
        {
            return adjustmentTypes_;
        }
    }

    /**
     * Creates a new proxy web control which displays adjustment types belonging
     * to a specific category within the Adjustment Type hierarchy.
     * 
     * @param delegate
     *            The delegating web control.
     * @param category
     *            The adjustment type category to filter.
     */
    public AdjustmentTypeCategoryProxyWebControl(final WebControl delegate,
            final AdjustmentTypeEnum category)
    {
        super(delegate);
        category_ = category;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Context wrapContext(final Context context)
    {
        final Context subContext = context.createSubContext();

        try
        {
            AdjustmentTypeCategoryVisitor  visitor = new  AdjustmentTypeCategoryVisitor(
                    context, category_);
            visitor = (AdjustmentTypeCategoryVisitor) ((Home) context
                    .get(CoreAdjustmentTypeHomePipelineFactory.ADJUSTMENT_TYPE_READ_ONLY_HOME))
                    .forEach(context, visitor);
            final Home home = new AdjustmentTypeTransientHome(subContext);
            for (AdjustmentType adjustmentType : visitor.getAdjustmentTypes())
            {
                home.create(subContext, adjustmentType);
            }
            subContext.put(AdjustmentTypeHome.class, home);
        }
        catch (HomeException exception)
        {
            new DebugLogMsg(this, "Home exception caught", exception);
        }
        return subContext;
    }
}
