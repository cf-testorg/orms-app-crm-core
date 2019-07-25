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
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import javax.servlet.ServletRequest;

import com.redknee.framework.xhome.beans.XBeans;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.And;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.filter.Predicate;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.webcontrol.AbstractWebControl;
import com.redknee.framework.xhome.webcontrol.PrimitiveWebControl;
import com.redknee.framework.xlog.log.DebugLogMsg;
import com.redknee.framework.xlog.log.LogSupport;

import com.redknee.app.crm.bean.ProvisionCommand;
import com.redknee.app.crm.bean.ProvisionCommandHome;
import com.redknee.app.crm.bean.ProvisionCommandID;
import com.redknee.app.crm.bean.ProvisionCommandXInfo;
import com.redknee.app.crm.bean.SubscriberTypeEnum;
import com.redknee.app.crm.technology.TechnologyEnum;

/**
 * This web control assumes that there is a getSpid() defined for the bean
 * where the field belongs to.
 *
 * @author rattapattu
 */
public class CustomProvisioningCommandsWebControl extends PrimitiveWebControl
{
    public void fromWeb(Context ctx, Object obj, ServletRequest req, String name)
    {
        if (req.getParameter(name) == null)
        {
            throw new NullPointerException(
                    "Value not selected yet");
        }
        else
        {
            //return getProvisioningCommand(ctx, req.getParameter(name));
            ProvisionCommand selectedComand = (ProvisionCommand)getProvisioningCommand(ctx, req.getParameter(name));
            ProvisionCommand originalCommand =  (ProvisionCommand)obj; 
            originalCommand.setName(selectedComand.getName());
            originalCommand.setTechnology(selectedComand.getTechnology());
            originalCommand.setHlrCmd(selectedComand.getHlrCmd());
            originalCommand.setSpid(selectedComand.getSpid());
            originalCommand.setType(selectedComand.getType());
            originalCommand.setId(selectedComand.getId());
        }
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.redknee.framework.xhome.webcontrol.InputWebControl#fromWeb(com.redknee.framework.xhome.context.Context,
     *      javax.servlet.ServletRequest, java.lang.String)
     */
    public Object fromWeb(Context ctx, ServletRequest req, String name)
            throws NullPointerException
    {
        Object obj;
        
        try
        {
          obj = XBeans.instantiate(ProvisionCommand.class, ctx);
        }
        catch (Exception e)
        {
          if(LogSupport.isDebugEnabled(ctx))
    	  {
    		new DebugLogMsg(this,e.getMessage(),e).log(ctx);
    	  }
          
          obj = new ProvisionCommand();
        }
     
        fromWeb(ctx, obj, req, name);
     
        return obj;
    }

    /**
     * @param ctx
     * @param parameter
     * @return
     */
    private Object getProvisioningCommand(Context ctx, String parameter)
    {
        int spid = getSpid(ctx);
        ProvisionCommandID id = new ProvisionCommandID(parameter, spid, SubscriberTypeEnum.POSTPAID, TechnologyEnum.GSM);
        try
        {
            Home h = (Home) ctx.get(ProvisionCommandHome.class);
            return h.find(ctx, id);
        }

        catch (HomeException e)
        {
            return null;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.redknee.framework.xhome.webcontrol.OutputWebControl#toWeb(com.redknee.framework.xhome.context.Context,
     *      java.io.PrintWriter, java.lang.String, java.lang.Object)
     */
    public void toWeb(Context ctx, PrintWriter out, String name, Object obj)
    {
        int mode = ctx.getInt("MODE", DISPLAY_MODE);
        String selectedCommand = ((ProvisionCommand) obj).getName();

        switch (mode) {
        case EDIT_MODE:
        case CREATE_MODE:
            
            Collection col = getCollection(ctx);
            
            out.print("<select name=\"");
            out.print(name);
            //out.print("\" size=\"");
            //out.print(String.valueOf(col.size()));
            out.print("\"");

            out.println(">");

            for (Iterator i = col.iterator(); i.hasNext();)
            {
                ProvisionCommand command = (ProvisionCommand) i.next();

                out.print("<option value=\"");
                out.print(command.getName());
                out.print("\"");
                if (command.getName().equals(selectedCommand))
                {
                    out.print(" selected=\"selected\"");
                }
                out.print(">");
                out.print(command.getName());
                out.println("</option>");
            }
            out.println("</select>");
            break;

        case DISPLAY_MODE:
        default:
            out.print(selectedCommand);
        }

    }

    /**
     * @param ctx
     * @return
     */
    private Collection getCollection(Context ctx)
    {
        try
        {
            Home h = (Home) ctx.get(ProvisionCommandHome.class);
            if (h == null)
            {
                return Collections.EMPTY_LIST;
            }            
            And filter = new And().add(new EQ(ProvisionCommandXInfo.SPID, getSpid(ctx))).add(
                    new EQ(ProvisionCommandXInfo.TYPE, SubscriberTypeEnum.POSTPAID));
            return h.where(ctx, filter).selectAll();
        }
        catch (HomeException e)
        {
            return Collections.EMPTY_LIST;
        }
    }
    
        
    private int getSpid(Context ctx)
    {
        Object obj =  ctx.get(AbstractWebControl.BEAN);
        try
        {
            // TODO 2006-10-24 reimplement using SPID Aware
            Method method = obj.getClass().getMethod("getSpid",new Class[0]);
            Integer result = (Integer)method.invoke(obj,new Object[0]);
            return result.intValue();
        }
        catch (Exception e)
        {
            LogSupport.minor(ctx,this,"there is no getSpid() for " + obj.getClass(),e);
            return -1;
        }
    }
}