package com.redknee.app.crm.xhome.validator;

import com.redknee.app.crm.CoreCrmLicenseConstants;
import com.redknee.app.crm.bean.KeyConfiguration;
import com.redknee.app.crm.bean.KeyValueFeatureEnum;
import com.redknee.framework.license.LicenseMgr;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.HomeProxy;

public class GRRKeyConfigurationCheckHome extends HomeProxy {

    public GRRKeyConfigurationCheckHome(Context ctx, Home delegate)
    {
        super(ctx, delegate);
    }

    public void remove(Context ctx, Object obj) throws HomeException
    {      
    	KeyConfiguration bean = (KeyConfiguration) obj;
       
        
        if (isUnAuthorizedForSystemKeys(ctx, bean))
        {
            throw new HomeException("Cannot Delete : You are not authorized to delete System Defined Keys.");
        }
        super.remove(ctx, obj);
    }
    
    public Object store(Context ctx, Object obj) throws HomeException
    {      
    	KeyConfiguration bean = (KeyConfiguration) obj;
        
        if (isUnAuthorizedForSystemKeys(ctx, bean))
        {
            throw new HomeException("Cannot Update : You are not authorized to update System Defined Key. \n Please copy this Key and perform necessary modifications.");
        }
        return super.store(ctx, obj);
    }
    
    public Object create(Context ctx, Object obj) throws HomeException
    {      
    	KeyConfiguration bean = (KeyConfiguration) obj;
        
        if (isUnAuthorizedForSystemKeys(ctx, bean))
        {
            throw new HomeException("Cannot Create : You are not authorized to create System Defined Key. \n Please unselect System flag.");
        }
        return super.create(ctx, obj);
    }
    
    
    private boolean isUnAuthorizedForSystemKeys(Context ctx, KeyConfiguration bean) throws HomeException
    {
    	if(bean.getFeature().getIndex() == KeyValueFeatureEnum.GRR_XML_GENERATOR_INDEX && bean.getSystemDefined())
    	{
    		LicenseMgr lMgr = (LicenseMgr) ctx.get(LicenseMgr.class);
    		if (lMgr == null)
    		{
    			throw new HomeException("LicenseManager not found in Context");
    		}
    		if(lMgr.isLicensed(ctx, CoreCrmLicenseConstants.GRR_DEVELOPER_LICENSE))
    		{
    			return false;
    		}
    		return true;
    	}
    	else
    	{
    		return false;
    	}
    }


}
