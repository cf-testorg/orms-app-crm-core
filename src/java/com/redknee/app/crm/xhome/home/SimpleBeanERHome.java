/*
 * This code is a protected work and subject to domestic and international copyright
 * law(s). A complete listing of authors of this work is readily available. Additionally,
 * source code is, by its very nature, confidential information and inextricably contains
 * trade secrets and other information proprietary, valuable and sensitive to Redknee. No
 * unauthorized use, disclosure, manipulation or otherwise is permitted, and may only be
 * used in accordance with the terms of the license agreement entered into with Redknee
 * Inc. and/or its subsidiaries.
 * 
 * Copyright (c) Redknee Inc. and its subsidiaries. All Rights Reserved.
 */
package com.redknee.app.crm.xhome.home;

import java.util.ArrayList;
import java.util.List;

import com.redknee.framework.xhome.beans.xi.PropertyInfo;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.HomeProxy;
import com.redknee.framework.xhome.msp.Spid;
import com.redknee.framework.xhome.msp.SpidAware;
import com.redknee.framework.xlog.log.ERLogMsg;

import com.redknee.app.crm.bean.BeanOperationEnum;
import com.redknee.app.crm.support.FrameworkSupportHelper;
import com.redknee.app.crm.resource.*;


/**
 * Use this as a super class for the ERLogHomes.
 * 
 * @author angie.li@redknee.com
 */
public abstract class SimpleBeanERHome extends HomeProxy
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;


    public SimpleBeanERHome(final Home delegate, final int identifier, final int classID, final String title,
            final PropertyInfo[] fields, final PropertyInfo idField)
    {
        super(delegate);
        identifier_ = identifier;
        classID_ = classID;
        title_ = title;
        fields_ = fields;
        idField_ = idField;
    }


    public SimpleBeanERHome(final Home delegate, final int identifier, final int classID, final String title,
            final PropertyInfo[] fields)
    {
        this(delegate, identifier, classID, title, fields, null);
    }


    public Object create(final Context context, final Object bean) throws HomeException
    {
        final Object result = super.create(context, bean);
        createLog(context, BeanOperationEnum.ADD, getSPID(context, bean), null, result);
        return result;
    }


    public Object store(final Context context, final Object bean) throws HomeException
    {
        Object original = null;
        try
        {
            original = getOriginal(context, bean);
        }
        catch (HomeException e)
        {
            throw new HomeException("Failed to look up original bean", e);
        }
        final Object result = super.store(context, bean);
        createLog(context, BeanOperationEnum.UPDATE, getSPID(context, bean), original, result);
        return result;
    }


    public void remove(final Context context, final Object bean) throws HomeException
    {
        super.remove(context, bean);
        createLog(context, BeanOperationEnum.DELETE, getSPID(context, bean), bean, null);
    }


    // Creates the actual log.
    private void createLog(final Context context, final BeanOperationEnum action, final int spid, final Object oldBean,
            final Object newBean)
    {
        new ERLogMsg(identifier_, classID_, title_, spid, getFieldValues(context, action, oldBean, newBean))
                .log(context);
    }


    protected String[] getFieldValues(final Context context, final BeanOperationEnum action, final Object oldBean,
            final Object newBean)
    {
        final List<String> values = new ArrayList<String>(fields_.length * 2 + 3);
        // Agent
        values.add(FrameworkSupportHelper.get(context).getCurrentUserID(context));
        // Action
        values.add(String.valueOf(action.getIndex()));
        // Bean ID
        if (idField_ != null)
        {
            addValue(values, newBean == null ? oldBean : newBean, idField_);
        }
        for (final PropertyInfo info : fields_)
        {
            addValue(values, oldBean, info);
            addValue(values, newBean, info);
        }
        return values.toArray(new String[values.size()]);
    }


    protected void addValue(final List<String> values, final Object bean, final PropertyInfo info)
    {
        String property = "";
        if (bean != null)
        {
            if (info.get(bean) instanceof Boolean)
            {
                // Change all Booleans to print out 1/0 instead of y/n
                property = ((Boolean) info.get(bean)) ? "1" : "0";
            }
            else
            {
                property = info.toString(bean);
            }

            if (property == null)
            {
                property = "";
            }

            if (property.indexOf(",") >= 0)
            {
                // Safely deal with extra commas
                // FIXME: this does not treat the
                // \" properly. \" should be doubled when enclosing in "".
                property = new StringBuilder().append("\"").append(property).append("\"").toString();
            }
        }

        values.add(property);
    }


    // Template method allows subsclasses to determine SPID.
    protected int getSPID(final Context context, final Object bean)
    {
        final int spid;
        if (bean instanceof SpidAware)
        {
            spid = ((SpidAware) bean).getSpid();
        }
        else if (context.has(Spid.class))
        {
            spid = ((Spid) context.get(Spid.class)).getSpid();
        }
        else
        {
            spid = -1;
        }
        return spid;
    }


    // Template method (with default implementation) that allows subclasses to return
    // original bean.
    protected Object getOriginal(final Context context, final Object bean) throws HomeException
    {
        try
        {
            final EQ criteria = new EQ(idField_, idField_.f(context, bean));
            return super.find(context, criteria);
        }
        catch (NullPointerException e)
        {
            if (null == idField_)
            {
                throw new HomeException("BEAN [" + ((null == bean) ? "null" : bean.getClass().getSimpleName())
                        + "]'s ID-PROPERTY-XINFO is not intialized. Original bean cannot be fetched", e);
            }
            else
            {
                throw e;
            }
        }
    }

    final public int identifier_;
    final public int classID_;
    final public String title_;
    final public PropertyInfo[] fields_;
    final public PropertyInfo idField_;
}