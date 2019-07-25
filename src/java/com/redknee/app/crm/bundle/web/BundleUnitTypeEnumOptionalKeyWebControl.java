package com.redknee.app.crm.bundle.web;

import java.io.PrintWriter;

import javax.servlet.ServletRequest;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.webcontrol.EnumOptionalWebControl;
import com.redknee.framework.xhome.xenum.EnumCollection;

import com.redknee.app.crm.bundle.UnitTypeEnum;

/**
 * Allows setting an optional value at the beginning of the Enumeration drop-down list.
 * Sample optional values are:
 *   "NONE"
 *   "ANY"
 *   
 * @author ali
 *
 */
public class BundleUnitTypeEnumOptionalKeyWebControl extends EnumOptionalWebControl 
{

    public BundleUnitTypeEnumOptionalKeyWebControl(
            EnumCollection enumCollection, 
            Object optionalValue, 
            boolean autoPreview)
    {
        super(enumCollection, optionalValue, autoPreview);
    }

    /**
     * Adapt the Object, obj, to the UnitTypeEnum.  The Object is a Integer index to that Enumeration. 
     * The super class expects an Enum Object (obj).
     * If the "optionalValue" is selected don't look up the UnitTypeEnum
     */
    @Override
    public void toWeb(Context ctx, PrintWriter out, String name, Object obj)
    {
        Integer index = (Integer) obj;
        if (index.equals(Integer.valueOf(OPTIONAL_INDEX)))
        {
            super.toWeb(ctx, out, name, null);
        }
        else
        {
            UnitTypeEnum unitType = (UnitTypeEnum) UnitTypeEnum.COLLECTION.get(index.shortValue());
            super.toWeb(ctx, out, name, unitType);
        }
    }

    /**
     * Translate the UnitTypeEnum object received to the Index.
     * If no UnitTypeEnum is selected, return -1;
     */
    @Override
    public Object fromWeb(Context ctx, ServletRequest req, String name)
    throws NullPointerException
    {
        if (Integer.toString(OPTIONAL_INDEX).equals(req.getParameter(name)))
        {
            return Integer.valueOf(OPTIONAL_VALUE);
        }
        else if (req.getParameter(name)!= null)
        {
            return Integer.valueOf(req.getParameter(name));
        } 
        else
        {
            return super.fromWeb(ctx, req, name);   
        }
    }

    public static int OPTIONAL_VALUE = -1;
}
