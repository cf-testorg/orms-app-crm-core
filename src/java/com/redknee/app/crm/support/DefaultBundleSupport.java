package com.redknee.app.crm.support;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.redknee.framework.xhome.beans.XBeans;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.xenum.Enum;
import com.redknee.framework.xhome.xenum.EnumCollection;
import com.redknee.framework.xlog.log.LogSupport;
import com.redknee.framework.xlog.log.PMLogMsg;

import com.redknee.app.crm.bean.core.BundleFee;
import com.redknee.app.crm.bean.core.BundleProfile;
import com.redknee.app.crm.bundle.Balance;
import com.redknee.app.crm.bundle.BalanceXInfo;
import com.redknee.app.crm.bundle.InvalidBundleApiException;
import com.redknee.app.crm.bundle.SubcriberBucketModelBundleManager;
import com.redknee.app.crm.bundle.SubcriberBucketModelBundleManagerV21;
import com.redknee.app.crm.bundle.exception.BundleDoesNotExistsException;
import com.redknee.app.crm.bundle.exception.BundleManagerException;
import com.redknee.app.crm.bundle.service.CRMBundleProfile;
import com.redknee.product.bundle.manager.api.v21.Balances;
import com.redknee.product.bundle.manager.api.v21.BalancesXInfo;

/**
 * Support class for shared methods
 *
 */
public class DefaultBundleSupport implements BundleSupport
{
    protected static BundleSupport instance_ = null;
    public static BundleSupport instance()
    {
        if (instance_ == null)
        {
            instance_ = new DefaultBundleSupport();
        }
        return instance_;
    }

    protected DefaultBundleSupport()
    {
    }
    
    /**
     * {@inheritDoc}
     */
    public Collection<BundleFee> getSortedBundles(Collection<BundleFee> bundleFees)
    {
        // charge Package bundles first, then PricePlan bundles. Auxiliary bundles last
        // TODO 2006-03-14 change the code to not rely on the Source
        List<BundleFee> bundleFeeList = new ArrayList<BundleFee>(bundleFees);
        Collections.sort(bundleFeeList, new Comparator<BundleFee>()
        {
            public int compare(BundleFee f1, BundleFee f2)
            {
                if (f1.getSource().equals(f2.getSource()))
                {
                    return 0;
                }

                if (f1.getSource().startsWith("Package"))
                {
                    return -1;
                }
                else if (f2.getSource().startsWith("Package"))
                {
                    return 1;
                }

                if ("Auxiliary".equals(f1.getSource()))
                {
                    return 1;
                }
                else if ("Auxiliary".equals(f2.getSource()))
                {
                    return -1;
                }

                return f1.getSource().compareTo(f2.getSource());
            }
        });
        return bundleFeeList;
    }

    public Collection<BundleProfile> getBundleByCategoryId(Context ctx, int id) throws BundleManagerException, HomeException
    {

        CRMBundleProfile service = (CRMBundleProfile) ctx.get(CRMBundleProfile.class);
        Collection<BundleProfile>  collection = null;

        if (service != null)
        {
            collection = service.getBundleByCategory(ctx,id);
        }
        else
        {
            if (LogSupport.isDebugEnabled(ctx))
            {
                LogSupport.debug(ctx, DefaultBundleSupport.class.getName(), "No Bundle Service in the context");
            }
            throw new HomeException("System error: CRMBundleProfile not found in context");
        }

            return collection;
    }

    public BundleProfile getBundleByAdjustmentType(Context ctx, int adjType) throws BundleManagerException, HomeException
    {

        CRMBundleProfile service = (CRMBundleProfile) ctx.get(CRMBundleProfile.class);

        if (service != null)
        {
            return service.getBundleByAdjustmentType(ctx, adjType);
        }
        else
        {
            if (LogSupport.isDebugEnabled(ctx))
            {
                LogSupport.debug(ctx, DefaultBundleSupport.class.getName(), "No Bundle Service in the context");
            }
            throw new HomeException("System error: CRMBundleProfile not found in context");
        }
    }

	/**
	 * get bundle profile API.
	 * @param ctx
	 * @param id
	 * @return
	 * @throws InvalidBundleApiException
	 * @throws HomeException
	 */

	public BundleProfile getBundleProfile(Context ctx, long id) throws InvalidBundleApiException, HomeException
	{
        CRMBundleProfile service = (CRMBundleProfile) ctx.get(CRMBundleProfile.class);
        BundleProfile bundle = null;

        StringBuilder details = new StringBuilder();
        details.append("BundleId=");
        details.append(id);
        final PMLogMsg pmLogMsg = new PMLogMsg("BundleProfile", "Retrieve bundle profile", details.toString());

        if (service != null)
        {
            try
            {
            	int spid = fetchSpidFromBundleProfileOrUser(ctx, id);
                bundle = service.getBundleProfile(ctx, spid, id);
            }
            catch (BundleDoesNotExistsException e)
            {
                LogSupport.info(ctx, DefaultBundleSupport.class.getName(), "getBundleProfile: Bundle does not exist:" + e.getMessage());
            }
            catch (Exception e)
            {
                LogSupport.major(ctx, DefaultBundleSupport.class.getName(), "getBundleProfile: BundleManagerException: " + e.getMessage(), e);
            }

   	        if (bundle == null)
   	        {
     	        throw new InvalidBundleApiException("Cannot find BundleProfile " + id );
   	        }
        }
        else
        {
            LogSupport.major(ctx, DefaultBundleSupport.class.getName(), "No Bundle Service is installed in the context");
        }
        pmLogMsg.log(ctx);

   	    return bundle;
	}

    /**
	 * Maps from the original BM Model to the new CRM-BM enum
	 * @param source the BM model enum
	 * @param dest the CRM destination
	 * @return
	 */
	public Enum mapBundleEnums(Enum source, Enum dest)
	{
	    EnumCollection destColection = dest.getCollection();
        Enum result = destColection.get(source.getIndex());

	    return result;
	}

	 /**
     * Maps from the CORBA enum to the new CRM-BM enum
     * @param sourceIndex the CORBA enum index
     * @param dest the CRM destination
     * @return
     */
    public Enum mapBundleEnums(short sourceIndex, Enum dest)
    {
        EnumCollection destColection = dest.getCollection();
        Enum result = destColection.get(sourceIndex);

        return result;
    }


    /**
     * Reflects the SubcriberBucketModelBundleManager Handler to return the BM model 2.5 or 2.7
     * @param ctx
     * @return
     */
    public SubcriberBucketModelBundleManager getSubscriberBucketModel(Context ctx)
    {
        SubcriberBucketModelBundleManager handler = (SubcriberBucketModelBundleManager) ctx.get(SubcriberBucketModelBundleManager.class, new SubcriberBucketModelBundleManagerV21());

        return handler;
    }


    /**
     * Converts the regular balance from CRM balance to BM balances
     * @param ctx
     * @param regularBal
     * @return
     */
    public Balances unAdaptRegularBalance(Context ctx, Balance regularBal)
    {
        Balances balance = null;
        try
        {
            balance = (Balances) XBeans.copy(ctx, BalanceXInfo.instance(), regularBal, BalancesXInfo.instance());
        }
        catch (Exception e)
        {
            LogSupport.major(ctx, DefaultBundleSupport.class.getName(), "Error on adaptRegularBalance", e);
        }

        return balance;
    }

    /**
     * Adapts the regular balance bean from the BM bucket to another
     * @param ctx
     * @param regularBal
     * @return
     */
    public Balance adaptRegularBalance(Context ctx, Balances regularBal)
    {
        Balance balance = null;
        try
        {
            balance = (Balance) XBeans.copy(ctx, BalancesXInfo.instance(), regularBal, BalanceXInfo.instance());
        }
//catch(IOException e)
        catch (Exception e)
        {
            LogSupport.major(ctx, DefaultBundleSupport.class.getName(), "Error on adaptRegularBalance", e);
        }
/*        catch (InstantiationException e)
        {
            LogSupport.major(ctx, DefaultBundleSupport.class.getName(), "InstantiationException when trying to get all the attributes on the Balances", e);
        }
*/
        return balance;
    }
    
    /**
     * TT#13020107017 fixed. This is a hack solution. As one user can only be linked to single spid, UserSpid is little risky.
     * Adding an extra XDB call to fetch the spid from BSS DB.
     * @param context
     * @return
     * @throws Exception
     */
    private static int fetchSpidFromBundleProfileOrUser(Context context, long bundleId) throws Exception {
		final int DEFAULT_SPID = -1;
		int spid = DEFAULT_SPID;
		Home xdbHome = (Home) context.get(com.redknee.app.crm.bundle.BundleProfileXDBHome.class);
		
		if(xdbHome != null)
		{
			try
			{
				com.redknee.app.crm.bundle.BundleProfile profile = (com.redknee.app.crm.bundle.BundleProfile) xdbHome.find(context, bundleId);
				if(profile != null)
				{
					spid = profile.getSpid();
				}
			}catch(HomeException he)
			{
				LogSupport.minor(context, BundleProfileSupport.class, "Unable to fetch spid from DB for BundleID :" + bundleId);
			}
		}
		
		if(spid == DEFAULT_SPID)
		{
			spid = UserSupport.getSpid(context);
		}
		return spid;
	}

}
