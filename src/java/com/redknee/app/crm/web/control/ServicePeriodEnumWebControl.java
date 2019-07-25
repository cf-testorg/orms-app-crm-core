package com.redknee.app.crm.web.control;

import javax.servlet.ServletRequest;

import com.redknee.app.crm.CoreCrmLicenseConstants;
import com.redknee.app.crm.bean.AuxiliaryServiceTypeEnum;
import com.redknee.app.crm.bean.ServicePeriodEnum;
import com.redknee.app.crm.bean.SubscriberTypeEnum;
import com.redknee.app.crm.bean.core.AuxiliaryService;
import com.redknee.app.crm.support.LicensingSupportHelper;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.webcontrol.AbstractWebControl;
import com.redknee.framework.xhome.webcontrol.EnumWebControl;
import com.redknee.framework.xhome.xenum.Enum;
import com.redknee.framework.xhome.xenum.EnumCollection;

public class ServicePeriodEnumWebControl    extends EnumWebControl
{
    public ServicePeriodEnumWebControl()
    {
        this(true);
    }
    
    public ServicePeriodEnumWebControl(boolean autoPreview)
    {
        super(SubscriberTypeEnum.COLLECTION, autoPreview);
    }
    
    // Hardcoded cached collections.  These must be sorted by index in ascending order in order
    // for EnumCollection.getByIndex() to work properly because it uses binary search.
    private static EnumCollection NO_ANNUAL_AND_DAILY_COLLECTION = new EnumCollection(new Enum[]
    {
            ServicePeriodEnum.MONTHLY,
            ServicePeriodEnum.WEEKLY,
            ServicePeriodEnum.ONE_TIME,
            ServicePeriodEnum.MULTIMONTHLY,
            ServicePeriodEnum.MULTIDAY
    });
    
    private static EnumCollection NO_ONETIME_AND_DAILY_COLLECTION = new EnumCollection(new Enum[]
    {
            ServicePeriodEnum.MONTHLY,
            ServicePeriodEnum.WEEKLY,
            ServicePeriodEnum.MULTIMONTHLY,
            ServicePeriodEnum.MULTIDAY
     });
    
    // Hardcoded cached collections.  These must be sorted by index in ascending order in order
    // for EnumCollection.getByIndex() to work properly because it uses binary search.
    private static EnumCollection NO_ANNUAL_COLLECTION = new EnumCollection(new Enum[]
    {
            ServicePeriodEnum.MONTHLY,
            ServicePeriodEnum.WEEKLY,
            ServicePeriodEnum.ONE_TIME,
            ServicePeriodEnum.MULTIMONTHLY,
            ServicePeriodEnum.DAILY,
            ServicePeriodEnum.MULTIDAY
    });
    
    private static EnumCollection NO_ONETIME_COLLECTION = new EnumCollection(new Enum[]
    {
            ServicePeriodEnum.MONTHLY,
            ServicePeriodEnum.WEEKLY,
            ServicePeriodEnum.MULTIMONTHLY,
            ServicePeriodEnum.DAILY,
            ServicePeriodEnum.MULTIDAY
     });
    
    @Override
    public EnumCollection getEnumCollection(Context ctx)
    {
        if (ctx.get(AbstractWebControl.BEAN) instanceof AuxiliaryService)
        {
        	AuxiliaryService service = (AuxiliaryService)ctx.get(AbstractWebControl.BEAN); 
        	
        	if (service.isCUG(ctx) || service.isPrivateCUG(ctx) || (service.getType().equals(AuxiliaryServiceTypeEnum.Vpn)))
        	{
        	    if (LicensingSupportHelper.get(ctx).isLicensed(ctx, CoreCrmLicenseConstants.DAILY_RECURRING_RECHARGES))
        	    {
        	        return NO_ONETIME_COLLECTION;
        	    }
        	    else
        	    {
        	        return NO_ONETIME_AND_DAILY_COLLECTION;
        	    }
        	}
        }
        
        if (LicensingSupportHelper.get(ctx).isLicensed(ctx, CoreCrmLicenseConstants.DAILY_RECURRING_RECHARGES))
        {
            return NO_ANNUAL_COLLECTION; 
        }
        else
        {
            return NO_ANNUAL_AND_DAILY_COLLECTION;
        }
    }

    /**
     * if the selected value is no longer available in the list of
     * possible values, force the selected value value to be the 
     * first item in the possible values.
     */
    @Override
    public Object fromWeb(Context ctx, ServletRequest req, String name)
    {
        try
        {
            req.getParameter(name); 
            return super.fromWeb(ctx, req, name);
        }
        catch (NullPointerException npEx)
        {
            throw npEx;
        }
        catch (Throwable t)
        {
            return getEnumCollection(ctx).get((short)0);
        }
    }
}
