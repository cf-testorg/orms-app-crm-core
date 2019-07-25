package com.redknee.app.crm.support;

import java.util.Collection;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.xenum.Enum;

import com.redknee.app.crm.bean.core.BundleFee;
import com.redknee.app.crm.bean.core.BundleProfile;
import com.redknee.app.crm.bundle.Balance;
import com.redknee.app.crm.bundle.InvalidBundleApiException;
import com.redknee.app.crm.bundle.SubcriberBucketModelBundleManager;
import com.redknee.app.crm.bundle.exception.BundleManagerException;
import com.redknee.product.bundle.manager.api.v21.Balances;

public interface BundleSupport extends Support
{
    /**
     * Sorts the bundles
     * @param bundleFees
     * @return
     */
    public Collection<BundleFee> getSortedBundles(Collection<BundleFee> bundleFees);
    
    public Collection<BundleProfile> getBundleByCategoryId(Context ctx, int id) throws BundleManagerException, HomeException;

	/**
	 * get bundle profile API.
	 * @param ctx
	 * @param id
	 * @return
	 * @throws InvalidBundleApiException
	 * @throws HomeException
	 */

	public BundleProfile getBundleProfile(Context ctx, long id) throws InvalidBundleApiException, HomeException;

    /**
     * get bundle profile by adjustment type.
     * @param ctx
     * @param adjType
     * @return
     * @throws InvalidBundleApiException
     * @throws HomeException
     */
    public BundleProfile getBundleByAdjustmentType(Context ctx, int adjType) throws BundleManagerException, HomeException;

    /**
	 * Maps from the original BM Model to the new CRM-BM enum
	 * @param source the BM model enum
	 * @param dest the CRM destination
	 * @return
	 */
	public Enum mapBundleEnums(Enum source, Enum dest);

	 /**
     * Maps from the CORBA enum to the new CRM-BM enum
     * @param sourceIndex the CORBA enum index
     * @param dest the CRM destination
     * @return
     */
    public Enum mapBundleEnums(short sourceIndex, Enum dest);


    /**
     * Reflects the SubcriberBucketModelBundleManager Handler to return the BM model 2.5 or 2.7
     * @param ctx
     * @return
     */
    public SubcriberBucketModelBundleManager getSubscriberBucketModel(Context ctx);


    /**
     * Converts the regular balance from CRM balance to BM balances
     * @param ctx
     * @param regularBal
     * @return
     */
    public Balances unAdaptRegularBalance(Context ctx, Balance regularBal);

    /**
     * Adapts the regular balance bean from the BM bucket to another
     * @param ctx
     * @param regularBal
     * @return
     */
    public Balance adaptRegularBalance(Context ctx, Balances regularBal);

}
