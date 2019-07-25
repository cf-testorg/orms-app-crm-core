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
package com.redknee.app.crm.web.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.filter.Predicate;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.SortingHome;
import com.redknee.framework.xhome.support.IdentitySupport;
import com.redknee.framework.xhome.webcontrol.OutputWebControl;
import com.redknee.framework.xhome.webcontrol.RequestServicer;
import com.redknee.framework.xlog.log.DebugLogMsg;
import com.redknee.framework.xlog.log.LogSupport;
import com.redknee.framework.xlog.log.MajorLogMsg;
import com.redknee.framework.xlog.log.MinorLogMsg;


/**
 * A servicer to return the data from the system in JSON format. It will select data from
 * the "homeKey" Home filtered by the "predicate" Predicate. With the string parameters
 * "parameters". These parameters should be in semicolon separated values. The results will be
 * sorted using the "comparator" comparator (if it is defined) and the bean description
 * will be output based on the "outputWC" OutputWebControl.
 * 
 * i.e.
 * homeKey = "com.redknee.framework.xhome.auth.bean.UserHome"
 * predicate = "com.redknee.framework.xhome.auth.bean.UserGroupPredicate"
 * comparator = "com.redknee.framework.xhome.auth.bean.UserComparator"
 * outputWC = "com.redknee.framework.xhome.auth.bean.UserOutputWebControl"
 * parameters = "10;15"
 * 
 *    - Home UserHome will be instantiate. 
 *    
 *    - Predicate UserGroupPredicate will be instantiated with constructor UserGroupPredicate("10","15")
 *    
 *    - UserHome will be filtered by UserGroupPredicate and results will be compared using UserComparator.
 *
 *    - Result will be formatted using UserOutputWebControl, so it will be in the form:
 *        userId1:UserOutputWebControl.toWeb(userId1),userId2:UserOutputWebControl.toWeb(userId2),...
 **/
public class RetrieveDataServicer implements RequestServicer
{

    public void service(Context ctx, HttpServletRequest req, HttpServletResponse res) throws ServletException,
            IOException
    {
        try
        {
            res.reset();
            res.setContentType("application/json");
            res.addHeader ("Pragma", "no-cache");
            PrintWriter out = res.getWriter();
            String homeClassName = req.getParameter("homeKey");
            String predicateClassName = req.getParameter("predicate");
            String comparatorClassName = req.getParameter("comparator");
            String outputWCClassName = req.getParameter("outputWC");
            String predicateParameters[] = req.getParameter("parameters").split(";");
            if (LogSupport.isDebugEnabled(ctx))
            {
                new DebugLogMsg(this, "RetrieveDataServicer::service() \nHomeKey:"
                        + req.getParameter("homeKey") 
                        + "\nPredicate Class:" + req.getParameter("predicate")
                        + "\nComparator Class:" + req.getParameter("comparator")
                        + "\nOutputWebControl Class:" + req.getParameter("outputWC")
                        + "\nParameters :" + req.getParameter("parameters"), null).log(ctx);
            }
            Class homeCls = null;
            Class identitySupportCls = null;
            Class predicateCls = null;
            Class outputWCCls = null;
            Class comparatorCls = null;
            Home home = null;
            IdentitySupport identitySupport = null;
            Predicate predicate = null;
            OutputWebControl outputWC = null;
            Comparator comparator = new Comparator()
            {
                public int compare(Object obj1, Object obj2)
                {
                    return obj1.toString().compareTo(obj2.toString());
                }
            };

            /********************************************************************************
             * Step 1 Create the bean object, as specified in the http request
             **********************************************************************************/
            try
            {
                homeCls = Class.forName(homeClassName);
                identitySupportCls = Class.forName(homeClassName.substring(0,homeClassName.length()-4) + "IdentitySupport");
                Method identitySupportInstanceMethod = identitySupportCls.getMethod("instance", new Class[] {});
                identitySupport = (IdentitySupport) identitySupportInstanceMethod.invoke(null, new Object[] {});
                home = (Home) ctx.get(homeCls);
            }
            catch (Exception e)
            {
                new MinorLogMsg(this, "Exception occurs when instantiating home object for class: " + homeClassName, e).log(ctx);
            }

            if (predicateClassName!=null && !predicateClassName.isEmpty())
            {
                try
                {
                    predicateCls = Class.forName(predicateClassName);
                    if (predicateParameters.length>0)
                    {
                        Class[] parameterTypes = new Class[predicateParameters.length];
                        Object[] parameters = new Object[predicateParameters.length];
                        for (int i=0;i<predicateParameters.length;i++)
                        {
                            parameterTypes[i] = String.class;
                            parameters[i] = parameterTypes[i].cast(predicateParameters[i]);
                        }
                        
                        predicate = (Predicate) predicateCls.getConstructor(parameterTypes).newInstance(parameters);
                    }
                    else
                    {
                        predicate = (Predicate) predicateCls.newInstance();
                    }
                    if (predicate!=null)
                    {
                        home = home.where(ctx, predicate);
                    }
                }
                catch (Exception e)
                {
                    new MinorLogMsg(this, "Exception occurs when instantiating predicate object for class: " + predicateClassName, e).log(ctx);
                }
            }
            
            if (outputWCClassName!=null && !outputWCClassName.isEmpty())
            {
                try
                {
                    outputWCCls = (Class.forName(outputWCClassName));
                    outputWC = (OutputWebControl) outputWCCls.newInstance();
                }
                catch (Exception e)
                {
                    new MinorLogMsg(this, "Exception occurs when instantiating outputWebControl object for class: " + outputWCClassName, e).log(ctx);
                }
            }

            if (comparatorClassName!=null && !comparatorClassName.isEmpty())
            {
                try
                {
                    comparatorCls = (Class.forName(comparatorClassName));
                    comparator = (Comparator) comparatorCls.newInstance();
                }
                catch (Exception e)
                {
                    new MinorLogMsg(this, "Exception occurs when instantiating comparator object for class: " + comparatorClassName, e).log(ctx);
                }
            }

            home = new SortingHome(home, comparator);
            
            Collection col = home.selectAll(ctx);
            Iterator iter = col.iterator();
            
            while (iter.hasNext())
            {
                Object obj = iter.next();
                out.print(identitySupport.ID(obj));
                out.print(":");
                if (outputWC != null)
                {
                    outputWC.toWeb(ctx, out, "", obj);
                }
                else
                {
                    out.print(identitySupport.ID(obj));
                }
                
                if (iter.hasNext())
                {
                    out.print(",");
                }
            }

            out.flush();
        } // Try block for the whole webcontrol process
        catch (Exception e)
        {
            new MajorLogMsg(this, "Critical exception encountered", e).log(ctx);
        }
    }
}
