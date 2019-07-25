package com.redknee.app.crm.home;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Comparator;

import com.redknee.app.crm.bean.AlternateInvoice;
import com.redknee.app.crm.home.PipelineFactory;
import com.redknee.app.crm.support.StorageSupportHelper;
import com.redknee.framework.xhome.beans.SafetyUtil;
import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.NoSelectAllHome;
import com.redknee.framework.xhome.home.SortingHome;


/**
 * Creates pipeline for Alternate Invoice Table.
 * @author sgaidhani
 * @since 9.1
 *
 */
public class AlternateInvoiceHomePipelineFactory implements PipelineFactory
{
    /**
     * {@inheritDoc}
     */
    @Override
    public Home createPipeline(Context ctx, Context serverCtx)
            throws RemoteException, HomeException, IOException, AgentException
    {
        Home alternateInvoiceHome = StorageSupportHelper.get(ctx).createHome(ctx, AlternateInvoice.class, "ALTERNATEINVOICE");
        alternateInvoiceHome = new SortingHome(ctx, alternateInvoiceHome, new Comparator<AlternateInvoice>()
        {
            @Override
            public int compare(AlternateInvoice inv1, AlternateInvoice inv2)
            {
                int result = SafetyUtil.safeCompare(inv2.getInvoiceDate(), inv1.getInvoiceDate());
                if (result == 0)
                {
                    result = SafetyUtil.safeCompare(inv1.getBAN(), inv2.getBAN());
                }
                return result;
            }
        });
        alternateInvoiceHome = new NoSelectAllHome(alternateInvoiceHome);
        
		return alternateInvoiceHome;
    }


}
