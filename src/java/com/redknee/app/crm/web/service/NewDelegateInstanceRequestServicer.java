package com.redknee.app.crm.web.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.webcontrol.RequestServicer;
import com.redknee.framework.xhome.webcontrol.RequestServicerProxy;
import com.redknee.framework.xlog.log.MajorLogMsg;

/**
 * This proxy creates a new intance of the delegate. This is used with certain RequestServiers that
 * are not FlyWeight (use object local variables) and cannot handle more than one request
 * at the same time.
 * 
 * @author asim.mahmood@redknee.com
 * @since 8.5 
 *
 */
public class NewDelegateInstanceRequestServicer extends RequestServicerProxy
{


    public NewDelegateInstanceRequestServicer(RequestServicer delegate)
    {
        super(delegate);
    }
    
    
    @Override
    public void service(Context ctx, HttpServletRequest req, HttpServletResponse res) throws ServletException,
            IOException
    {
        Class<? extends RequestServicer> class_ = getDelegate().getClass();
        
        RequestServicer newDelegate = null;
        try
        {
            newDelegate = class_.newInstance();
            
            //Copy the pipeline
            if (newDelegate instanceof RequestServicerProxy)
            {
                ((RequestServicerProxy)newDelegate).setDelegate(((RequestServicerProxy)getDelegate()).getDelegate());
            }   
        }
        catch (Exception e)
        {
            new MajorLogMsg(this, "DEV Error: Unable to instantiate " + class_.getName() + ". Error: " + e.getMessage(), e).log(ctx);
            throw new ServletException("Invalid menu configuration, please contact your administrator.",  e);
        }

        newDelegate.service(ctx,req,res);
    }
}
