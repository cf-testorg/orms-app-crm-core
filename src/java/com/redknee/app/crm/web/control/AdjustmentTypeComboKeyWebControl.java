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

import java.io.PrintWriter;

import javax.servlet.ServletRequest;

import com.redknee.framework.xhome.beans.xi.PropertyInfo;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.And;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.webcontrol.AbstractKeyWebControl;
import com.redknee.framework.xhome.webcontrol.AbstractWebControl;
import com.redknee.framework.xhome.webcontrol.KeyWebControlOptionalValue;
import com.redknee.framework.xhome.webcontrol.WebControl;
import com.redknee.framework.xhome.webcontrol.tree.KeyTreeWebControl;
import com.redknee.framework.xlog.log.DebugLogMsg;
import com.redknee.framework.xlog.log.LogSupport;

import com.redknee.app.crm.bean.AdjustmentType;
import com.redknee.app.crm.bean.AdjustmentTypeKeyWebControl;
import com.redknee.app.crm.bean.AdjustmentTypeXInfo;
import com.redknee.app.crm.support.AdjustmentTypeSupportHelper;

/**
 * Web control for selecting adjustment type by categories. There are two select
 * box, one for selecting the adjustment type category (using
 * AdjustmentTypeKeyTreeWebControl) and another for selecting the adjustment
 * type (AdjustmentTypeKeyWebControl).
 * 
 * @author cindy.wong@redknee.com
 * @since 8.3
 */
public class AdjustmentTypeComboKeyWebControl extends KeyTreeWebControl
{

    /**
     * Creates a new web control.
     * 
     * @param id
     *            Identity support.
     * @param homeKey
     *            Key for adjustment type home.
     * @param showAll
     *            Whether to show all adjustement types. Used by
     *            AdjustmentTypeKeyTreeWebControl.
     * @param adjustmentTypeCategoryProperty
     *            The field for storing category. This field should be transient
     *            and hidden.
     */
    public AdjustmentTypeComboKeyWebControl(Object homeKey,
            boolean showAll, PropertyInfo adjustmentTypeCategoryProperty)
    {
        super(com.redknee.app.crm.bean.AdjustmentTypeIdentitySupport.instance(), homeKey, true);
        categoryProperty_ = adjustmentTypeCategoryProperty;
        categoryWebControl_ = new AdjustmentTypeKeyTreeWebControl(homeKey,
                showAll, true, true);
        delegate_ = new AdjustmentTypeKeyWebControl(1, true,
                new KeyWebControlOptionalValue("--", "-1"));
    }

    /**
     * 
     * @see com.redknee.framework.xhome.webcontrol.tree.KeyTreeWebControl#toWeb(com.redknee.framework.xhome.context.Context,
     *      java.io.PrintWriter, java.lang.String, java.lang.Object)
     */
    @Override
    public void toWeb(final Context ctx, final PrintWriter out,
            final String name, final Object obj)
    {
        final Object bean = ctx.get(AbstractWebControl.BEAN);
        Number adjustmentType = (Number) obj;
        final int category = ((Number) categoryProperty_.get(bean)).intValue();
        final int mode = ctx.getInt("MODE", DISPLAY_MODE);

        displayEditableWebControl(ctx, category, adjustmentType, out, name);
    }

    /**
     * 
     * @see com.redknee.framework.xhome.webcontrol.tree.KeyTreeWebControl#fromWeb(Context,
     *      ServletRequest, String)
     */
    @Override
    public Object fromWeb(Context ctx, ServletRequest req, String name)
            throws NullPointerException
    {
        Number adjustmentTypeCode = new Integer(-1);
        try
        {
            adjustmentTypeCode = (Number) super.fromWeb(ctx, req, name);
        }
        catch (NullPointerException exception)
        {
            new DebugLogMsg(this, "Cannot select adjustment type", exception)
                    .log(ctx);
        }

        int selectedCategory = -1;
        Number selectedCategoryObj = (Number) this.categoryWebControl_.fromWeb(
                ctx, req, getWebControlName(categoryProperty_, name));
        if (selectedCategoryObj != null)
        {
            selectedCategory = selectedCategoryObj.intValue();
        }

        if (selectedCategory == -1)
        {
            AdjustmentType adjustmentType = getAdjustmentType(ctx,
                    adjustmentTypeCode);
            if (adjustmentType != null)
            {
                selectedCategory = adjustmentType.getParentCode();
            }
        }

        if (LogSupport.isDebugEnabled(ctx))
        {
            StringBuilder sb = new StringBuilder();
            sb.append("Selected adjustment type category: ");
            sb.append(selectedCategory);
            sb.append(", selected adjustment type: ");
            sb.append(adjustmentTypeCode);
            new DebugLogMsg(this, sb.toString(), null).log(ctx);
        }
        Object bean = ctx.get(AbstractWebControl.BEAN);
        categoryProperty_.set(bean, Integer.valueOf(selectedCategory));

        return adjustmentTypeCode;
    }

    /**
     * Returns the category of the current adjustment type.
     * 
     * @param ctx
     *            Operating context.
     * @param currentAdjType
     *            Adjustment type.
     * @return Category of the current adjustment type.
     */
    protected AdjustmentType getAdjustmentType(Context ctx,
            Number currentAdjType)
    {
        Home home = (Home) ctx.get(delegate_.getHomeKey());
        AdjustmentType adjustmentType = null;
        try
        {
            if (currentAdjType != null)
            {
                adjustmentType = AdjustmentTypeSupportHelper.get(ctx).getAdjustmentType(ctx,
                        home, currentAdjType.intValue());
            }
        }
        catch (HomeException exception)
        {
            new DebugLogMsg(this,
                    "HomeException caught when looking up adjustment type "
                            + currentAdjType.intValue(), exception).log(ctx);
        }

        return adjustmentType;
    }

    /**
     * Returns the name of the web control.
     * 
     * @param property
     *            Field to display.
     * @param name
     *            Name of the bena.
     * @return
     */
    protected String getWebControlName(PropertyInfo property, final String name)
    {
        StringBuilder newName = new StringBuilder(name);
        if (newName.lastIndexOf(".") + 1 < newName.length())
        {
            newName.delete(newName.lastIndexOf(".") + 1, newName.length());
        }
        newName.append(property.getName());
        return newName.toString();
    }

    /**
     * Display web control.
     * 
     * @param context
     *            Operating context.
     * @param currentCategory
     *            Current category.
     * @param currentAdjustmentType
     *            Current adjustment type.
     * @param out
     *            Print writer.
     * @param name
     *            Name of field.
     */
    protected void displayEditableWebControl(final Context context,
            final int currentCategory, final Number currentAdjustmentType,
            final PrintWriter out, final String name)
    {
        int category = currentCategory;
        AdjustmentType adjustmentType = getAdjustmentType(context,
                currentAdjustmentType);
        if (category < 0 && adjustmentType != null)
        {
            category = adjustmentType.getParentCode();
        }

        // Don't need to see the category if in a read-only mode
        final int mode = context.getInt("MODE", DISPLAY_MODE);
        if (mode != DISPLAY_MODE)
        {
            Context subCtx = context.createSubContext();
            subCtx.setName(categoryWebControl_.getClass().getName());
            subCtx.put(AbstractWebControl.PROPERTY, null);

            this.categoryWebControl_.toWeb(subCtx, out, getWebControlName(
                    categoryProperty_, name), Integer.valueOf(category));
        }

        // only show  adjustment types, not categories.
        And predicate = new And();
        predicate.add(new EQ(AdjustmentTypeXInfo.PARENT_CODE, category));
        predicate.add(new EQ(AdjustmentTypeXInfo.CATEGORY, false));
        
        delegate_.setSelectFilter(predicate);

        if (adjustmentType == null
                || adjustmentType.getParentCode() != category)
        {
            delegate_.toWeb(context, out, name, new Integer(-1));
        }
        else
        {
            delegate_.toWeb(context, out, name, currentAdjustmentType);
        }
    }

    private PropertyInfo categoryProperty_;
    private WebControl categoryWebControl_;
    private AbstractKeyWebControl delegate_;

}
