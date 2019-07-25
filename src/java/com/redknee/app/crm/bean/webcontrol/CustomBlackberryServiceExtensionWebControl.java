package com.redknee.app.crm.bean.webcontrol;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletRequest;

import com.redknee.app.crm.extension.service.BlackberryServiceExtensionWebControl;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.holder.LongHolder;
import com.redknee.framework.xhome.holder.LongHolderXMLSupport;
import com.redknee.framework.xhome.webcontrol.AbstractTableWebControl;
import com.redknee.framework.xhome.webcontrol.FinalWebControl;
import com.redknee.framework.xhome.webcontrol.WebControl;
import com.redknee.service.blackberry.model.ServiceMappingRow;
import com.redknee.service.blackberry.model.ServiceMappingRowTableWebControl;

public class CustomBlackberryServiceExtensionWebControl extends BlackberryServiceExtensionWebControl
{
    @Override
    public WebControl getBlackberryServicesWebControl()
    {
        return CUSTOM_BLACKBERRY_SERVICE_WC;
    }

    public static final WebControl CUSTOM_BLACKBERRY_SERVICE_WC = new AbstractTableWebControl()
    {
        private final WebControl serviceMappingRowTableWebControl_ = new FinalWebControl(new ServiceMappingRowTableWebControl());
        
        @Override
        public Object fromWeb(Context ctx, ServletRequest req, String name)
        {
          Object obj = new ArrayList();

          fromWeb(ctx, obj, req, name);

          return obj;
        }


        @Override
        public void fromWeb(Context ctx, Object obj, ServletRequest req, String name)
        {
            Collection result = (ArrayList) obj;
            ctx = ctx.createSubContext();
            int mode = ctx.getInt("MODE", DISPLAY_MODE);
            Collection tempObj = new ArrayList();
            if ( mode == CREATE_MODE )
            {
                serviceMappingRowTableWebControl_.fromWeb(ctx, tempObj, req, name);
                Iterator i     = tempObj.iterator();
                while (i.hasNext())
                {
                    ServiceMappingRow mapping  = (ServiceMappingRow) i.next();
                    result.add(new LongHolder(mapping.getRimServiceId()));
                }
            }
            else
            {
                String hidden = req.getParameter(name);
                if (hidden!=null && !hidden.isEmpty())
                {
                    List<LongHolder> mappings = LongHolderXMLSupport.instance().toList(hidden);
                    Iterator<LongHolder> i = mappings.iterator();
                    while (i.hasNext())
                    {
                        result.add(i.next());
                    }
                }
            }
            
        }

        @Override
        public void toWeb(Context ctx, PrintWriter out, String name, Object obj)
        {
            Collection<ServiceMappingRow> serviceMappingRowBeans = new ArrayList<ServiceMappingRow>();
            Collection         beans    = (Collection) obj;
            Iterator i     = beans.iterator();
            while (i.hasNext())
            {
                long rimServiceId = ((LongHolder) i.next()).getValue();
                ServiceMappingRow mapping = new ServiceMappingRow();
                mapping.setRimServiceId(rimServiceId);
                serviceMappingRowBeans.add(mapping);
            }
            
            if (ctx.getInt("MODE") != DISPLAY_MODE)
            {
                ctx.put(NUM_OF_BLANKS, Integer.valueOf(2));
            }
            
            serviceMappingRowTableWebControl_.toWeb(ctx, out, name, serviceMappingRowBeans);
        }
        
    };
}