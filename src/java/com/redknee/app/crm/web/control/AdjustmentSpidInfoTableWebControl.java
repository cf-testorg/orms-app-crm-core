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

package com.redknee.app.crm.web.control;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletRequest;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.msp.Spid;
import com.redknee.framework.xhome.msp.SpidHome;

import com.redknee.app.crm.bean.AdjustmentInfo;
import com.redknee.app.crm.bean.AdjustmentTypeEnum;
import com.redknee.app.crm.bean.core.AdjustmentType;
import com.redknee.app.crm.bean.webcontrol.CustomAdjustmentInfoTableWebControl;



/**
 * This web control only display adjustment types belonging to a certain category.
 * 
 * @author kumaran.sivasubramaniam@redknee.com
 */
public class AdjustmentSpidInfoTableWebControl extends CustomAdjustmentInfoTableWebControl
{





    /**
     * Creates a new proxy web control which displays adjustment types belonging to a specific category within the
     * Adjustment Type hierarchy.
     * 
     * @param delegate
     *            The delegating web control.
     * @param category
     *            The adjustment type category to filter.
     */
    public AdjustmentSpidInfoTableWebControl()
    {
    }
    
    @Override
    public void outputCheckBox(Context ctx, PrintWriter out, String name, Object bean, boolean checked)
    {
        AdjustmentInfo adjInfo = (AdjustmentInfo) bean;
        
        out.print(" <td><input type=\"checkbox\" name=\"");
        out.print(name);
        out.print(SEPERATOR);
        out.print("_enabled\" value=\"X\"");
        
        AdjustmentType adjustType = (AdjustmentType) ctx.get(BEAN);
        
        String checkedOrNotChecked = "";
        
        if ( ( ctx.getInt("MODE", CREATE_MODE) == CREATE_MODE )  &&
             ( ! adjustType.getSystem() ) )
        {   
            if ( checked )
            {
                checkedOrNotChecked = "";
            }
        }
        else
        {
            if ( checked )
            {
                checkedOrNotChecked = "checked=\"checked\"";
            }
        }
        out.print(checkedOrNotChecked);
        out.println("></td>");
    }
    
    @Override
    public void fromWeb(Context ctx, Object obj, ServletRequest req, String name)
    {
       ctx = ctx.createSubContext();

       ctx.put("MODE", CREATE_MODE);

       super.fromWeb(ctx, obj, req, name);
    }

    /*
     * Override
     * 
     * @see com.redknee.framework.xhome.webcontrol.TableWebControl#toWeb(com.redknee.framework.xhome.context.Context,
     *      java.io.PrintWriter, java.lang.String, java.lang.Object)
     */
    @Override
    public void toWeb(Context ctx, PrintWriter out, String name, Object obj)
    {
       // Disable creation of new AdjustmentInfo's
       ctx = ctx.createSubContext();
       ctx.put(NUM_OF_BLANKS, -1);

       Map  map  = new TreeMap();
       Home home = (Home) ctx.get(SpidHome.class);
       boolean listAllServiceProviders = false;
       int count = 0;

       AdjustmentType adjustType = (AdjustmentType) ctx.get(BEAN);
       if ( ( ctx.getInt("MODE", DISPLAY_MODE) != DISPLAY_MODE) &&
            ( ! adjustType.isInCategory(ctx, AdjustmentTypeEnum.RecurringCharges ) ||
              adjustType.getCode() == AdjustmentTypeEnum.RecurringCharges.getIndex()  ||
              adjustType.isInCategory(ctx, AdjustmentTypeEnum.PostpaidSupportRedirectCharges ) ||
              adjustType.isInCategory(ctx, AdjustmentTypeEnum.CUGPriceplanServiceCharges)) )
       {

            // Pre-load defaults for each Spid
            try
            {
                listAllServiceProviders = true;
                for ( Iterator i = home.selectAll(ctx).iterator() ; i.hasNext() ; )
                {
                     Spid     spid = (Spid) i.next();

                     // output hidden field
                    out.println("<input type=hidden name=\".adjustmentSpidInfo." + count + ".spid\" value=\"" + spid.getId() + "\"/>");

                    AdjustmentInfo info = new AdjustmentInfo();
                    info.setSpid(spid.getId());

                    map.put(Integer.valueOf(info.getSpid()), info);
                    count++;
                }
            }
            catch (HomeException e)
            {
            // nop
            }
       }

       // Override with explicit values from the Bean
       for ( Iterator i = ((List) obj).iterator() ; i.hasNext() ; )
       {
           AdjustmentInfo info = (AdjustmentInfo) i.next();

           try
           {
               Spid spid = (Spid) home.find(ctx, Integer.valueOf(info.getSpid()));
               if (spid != null)
               {
                   if (!listAllServiceProviders)
                   {
                       // output hidden field
                       out.println("<input type=hidden name=\".adjustmentSpidInfo." + count + ".spid\" value=\"" + info.getSpid() + "\"/>");
                       count++;
                   }

                  map.put(Integer.valueOf(info.getSpid()), info);
               }

           }
           catch (HomeException he)
           {
               // ignore
           }
       }

       ctx.put("ACTIONS",false);

       super.toWeb(ctx, out, name, new ArrayList(map.values()));
    }

}
