package com.redknee.app.crm.web.border;

import java.io.IOException;
import java.util.Comparator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.redknee.framework.xhome.beans.XBeans;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.SortingHome;
import com.redknee.framework.xhome.web.border.Border;
import com.redknee.framework.xhome.webcontrol.RequestServicer;


public class SortingBorder implements Border
{
    public SortingBorder(Context ctx, Class<Comparable> beanClass)
    {
        homeClass_ = XBeans.getClass(ctx, beanClass, Home.class);
    }

    public void service(Context ctx, HttpServletRequest req, HttpServletResponse res, RequestServicer delegate)
            throws ServletException, IOException
    {
        Context subCtx = ctx.createSubContext();
        if (homeClass_!=null)
        {
            Home home = (Home) ctx.get(homeClass_);
            home = new SortingHome(ctx, home);
            subCtx.put(homeClass_, home);
        }
        
        delegate.service(subCtx, req, res);
    }
    
    private Class homeClass_ = null;
}
