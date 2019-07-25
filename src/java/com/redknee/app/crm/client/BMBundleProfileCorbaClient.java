/*
 * This code is a protected work and subject to domestic and international copyright
 * law(s). A complete listing of authors of this work is readily available. Additionally,
 * source code is, by its very nature, confidential information and inextricably contains
 * trade secrets and other information proprietary, valuable and sensitive to Redknee. No
 * unauthorized use, disclosure, manipulation or otherwise is permitted, and may only be
 * used in accordance with the terms of the license agreement entered into with Redknee
 * Inc. and/or its subsidiaries.
 * 
 * Copyright (c) Redknee Inc. and its subsidiaries. All Rights Reserved.
 */
package com.redknee.app.crm.client;

import static com.redknee.app.crm.client.CorbaClientTrapIdDef.BUNDLE_PROFILE_PROV_SVC_DOWN;
import static com.redknee.app.crm.client.CorbaClientTrapIdDef.BUNDLE_PROFILE_PROV_SVC_UP;

import java.util.ArrayList;
import java.util.List;

import com.redknee.app.crm.bundle.BMReturnCodeMsgMapping;
import com.redknee.app.crm.bundle.BundleProfileAdapter;
import com.redknee.app.crm.bundle.DurationTypeEnum;
import com.redknee.app.crm.bundle.FlexTypeEnum;
import com.redknee.app.crm.bundle.RecurrenceTypeEnum;
import com.redknee.app.crm.bundle.exception.BundleManagerException;
import com.redknee.app.crm.bundle.support.ParameterIDSupportHelper;
import com.redknee.framework.xhome.beans.SafetyUtil;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xlog.log.LogMsg;
import com.redknee.framework.xlog.log.LogSupport;
import com.redknee.framework.xlog.log.PMLogMsg;
import com.redknee.product.bundle.manager.provision.v5_0.bundle.BundleProfile;
import com.redknee.product.bundle.manager.provision.v5_0.bundle.BundleProfileProvision;
import com.redknee.product.bundle.manager.provision.v5_0.bundle.BundleReturnParam;
import com.redknee.product.bundle.manager.provision.v5_0.bundle.FlexType;
import com.redknee.product.bundle.manager.provision.common.param.Parameter;
import com.redknee.product.bundle.manager.provision.common.param.ParameterID;
import com.redknee.product.bundle.manager.provision.common.param.ParameterValue;
import com.redknee.util.snippet.log.Logger;

public class BMBundleProfileCorbaClient extends AbstractCrmClient<BundleProfileProvision> implements BundleProfileProvisionClient
{
    /**
     * Name of the CORBA client.
     */
    private static final String CLIENT_NAME = "BundleProfileProvisionClient";

    private static final Class<BundleProfileProvision> SERVICE_TYPE = BundleProfileProvision.class;

    /**
     * Service description.
     */
    private static final String SERVICE_DESCRIPTION = "CORBA client for Bundle Profile Provisioning services";

    public BMBundleProfileCorbaClient(final Context ctx)
    {
        // TODO change the trap IDs
        super(ctx, CLIENT_NAME, SERVICE_DESCRIPTION, SERVICE_TYPE,
        BUNDLE_PROFILE_PROV_SVC_DOWN, BUNDLE_PROFILE_PROV_SVC_UP);
    }
    
    
    private static Parameter[] getInParamsetForBundleCreateAndUpdate(com.redknee.app.crm.bean.core.BundleProfile bundle)
    {
        List<Parameter> inParamList = new ArrayList<Parameter>();
        final ParameterValue rollOverFirstParam = new ParameterValue();
        rollOverFirstParam.booleanValue(bundle.getUseRolloverFirst());
        inParamList.add(new Parameter(ParameterID.IN_CHARGE_ROLLOVER_FIRST, rollOverFirstParam));
        if (bundle.isFlex())
        {
            // flex indicator param
            ParameterValue flexParam = new ParameterValue();
            flexParam.booleanValue(bundle.isFlex());
            inParamList.add(new Parameter(ParameterID.IN_FLEX_INDICATOR, flexParam));
            // flex type indicator param
            ParameterValue flexTypeParam = new ParameterValue();
            // cps is going to retrieve this detail from shortvalue
            flexTypeParam.shortValue((short) FlexType.from_int(bundle.getFlexType().getIndex()).value());
            inParamList.add(new Parameter(ParameterID.IN_FLEX_TYPE_INDICATOR, flexTypeParam));
            // next bundle reference
            ParameterValue nextBundleRef = new ParameterValue();
            nextBundleRef.longValue(bundle.getNextBundleRef());
            inParamList.add(new Parameter(ParameterID.IN_NEXT_BUNDLE_REFERENCE, nextBundleRef));
            // adjustment type
            ParameterValue adjustmentType = new ParameterValue();
            adjustmentType.intValue(bundle.getAdjustmentType());
            inParamList.add(new Parameter(ParameterID.IN_ADJUSTMENT_TYPE, adjustmentType));
            if (bundle.getFlexType() == FlexTypeEnum.SECONDARY)
            {
                // root flex
                ParameterValue rootFlex = new ParameterValue();
                rootFlex.longValue(bundle.getRoot());
                inParamList.add(new Parameter(ParameterID.IN_ROOT_FLEX, rootFlex));                
            }
        }
        // expire on bcd
        boolean oneOffFixedInterval = SafetyUtil.safeEquals(bundle.getRecurrenceScheme(),
                RecurrenceTypeEnum.ONE_OFF_FIXED_INTERVAL);
        if (oneOffFixedInterval && bundle.getInterval() == DurationTypeEnum.BCD_INDEX)
        {
            ParameterValue expireOnBCD = new ParameterValue();
            expireOnBCD.booleanValue(true);
            inParamList.add(new Parameter(ParameterID.IN_EXPIRE_ON_BCD, expireOnBCD));
        }
        return inParamList.toArray(new Parameter[inParamList.size()]);
    }

    
    public BundleProfile createBundleProfile(com.redknee.app.crm.bean.core.BundleProfile bundle)
            throws BundleManagerException
    {
        
        return createBundleProfile(getContext(), bundle, getInParamsetForBundleCreateAndUpdate(bundle));
    }

    private BundleProfile createBundleProfile(Context ctx, com.redknee.app.crm.bean.core.BundleProfile bundle, Parameter[] inParamSet)
            throws BundleManagerException
    {
        final LogMsg pm = new PMLogMsg(getModule(), CLIENT_NAME, "createBundleProfile");
        BundleProfile result = null;
        BundleProfileProvision service = getService();
        
        if (service == null)
        {
            final BundleManagerException exception = new BundleManagerException(
                    "Cannot retrieve " + CLIENT_NAME + " CORBA service. Unable to create bundle profile.");
            Logger.minor(ctx, this, exception.getMessage(), exception);
            throw exception;
        }
        
        if (Logger.isInfoEnabled())
        {
            final StringBuilder msg = new StringBuilder();
            msg.append("Sending CORBA request BundleProfileProvision.createBundleProfile() to URCS: ");
            msg.append("bundle = ");
            msg.append(bundle.toString());
            LogSupport.info(ctx, this, msg.toString());
        }

        try
        {
            BundleProfile adaptedBundleProfile = BundleProfileAdapter.adaptBundleProfile(bundle);

            BundleReturnParam returnParam = service.createBundleProfile(adaptedBundleProfile, inParamSet);
            
            if (LogSupport.isDebugEnabled(ctx))
            {
                final StringBuilder msg = new StringBuilder();
                msg.append("CORBA request BundleProfileProvision.createBundleProfile() to URCS: ");
                msg.append("ResultCode = ");
                msg.append(returnParam.resultCode);
                LogSupport.debug(ctx, this, msg.toString());
            }

            if (returnParam.resultCode == 0)
            {
                result = returnParam.outBundleProfile;
                ParameterValue bundleId = ParameterIDSupportHelper.get(getContext()).getParam(returnParam.outParamSet,
                        ParameterID.OUT_BUNDLE_ID);
                if (bundleId != null)
                {
                    result.bundleId = bundleId.longValue();
                }
            }
            else
            {
                throw new BundleManagerException(BMReturnCodeMsgMapping.getMessage(returnParam.resultCode) + " (Code="
                        + returnParam.resultCode + ")");
            }
        }
        catch (BundleManagerException e)
        {
            throw e;
        }
        catch (final org.omg.CORBA.SystemException e)
        {
            final StringBuilder msgBldr = new StringBuilder();
            msgBldr.append("CORBA communication with URCS Bundle Profile Provision Service server failed.");
            final String msg = msgBldr.toString();
            Logger.minor(ctx, this, msg, e);
            final BundleManagerException exception = new BundleManagerException(msg, e);
            throw exception;
        }
        catch (final Throwable t)
        {
            final StringBuilder msgBldr = new StringBuilder();
            msgBldr.append("Unexpected failure in CORBA communication with URCS Bundle Profile Provision Service.");
            final String msg = msgBldr.toString();
            Logger.minor(ctx, this, msg, t);
            final BundleManagerException exception = new BundleManagerException(msg, t);
            throw exception;
        }
        finally
        {
            pm.log(ctx);
        }
        return result;
    }

    public BundleProfile getBundleProfile(int spId, int bundleId) throws BundleManagerException
    {
        Parameter[] inParamSet = new Parameter[0];
        return getBundleProfile(getContext(), spId, bundleId, inParamSet);
    }

    private BundleProfile getBundleProfile(Context ctx, int spId, int bundleId, Parameter[] inParamSet)
            throws BundleManagerException
    {
        final LogMsg pm = new PMLogMsg(getModule(), CLIENT_NAME, "getBundleProfile");
        BundleProfile result = null;
        BundleProfileProvision service = getService();
        
        if (service == null)
        {
            final BundleManagerException exception = new BundleManagerException(
                    "Cannot retrieve " + CLIENT_NAME + " CORBA service. Unable to get bundle profile.");
            Logger.minor(ctx, this, exception.getMessage(), exception);
            throw exception;
        }
        
        if (Logger.isInfoEnabled())
        {
            final StringBuilder msg = new StringBuilder();
            msg.append("Sending CORBA request BundleProfileProvision.getBundleProfile() to URCS: ");
            msg.append("bundleId = ");
            msg.append(bundleId);
            LogSupport.info(ctx, this, msg.toString());
        }

        try
        {
            BundleReturnParam returnParam = service.getBundleProfile(spId, bundleId, inParamSet);
            
            if (LogSupport.isDebugEnabled(ctx))
            {
                final StringBuilder msg = new StringBuilder();
                msg.append("CORBA request BundleProfileProvision.getBundleProfile() to URCS: ");
                msg.append("ResultCode = ");
                msg.append(returnParam.resultCode);
                LogSupport.debug(ctx, this, msg.toString());
            }

            if (returnParam.resultCode == 0)
            {
                result = returnParam.outBundleProfile;
            }
        }
        catch (final org.omg.CORBA.SystemException e)
        {
            final StringBuilder msgBldr = new StringBuilder();
            msgBldr.append("CORBA communication with URCS Bundle Profile Provision Service server failed");
            final String msg = msgBldr.toString();
            Logger.minor(ctx, this, msg, e);
            final BundleManagerException exception = new BundleManagerException(msg, e);
            throw exception;
        }
        catch (final Throwable t)
        {
            final StringBuilder msgBldr = new StringBuilder();
            msgBldr.append("Unexpected failure in CORBA communication with URCS Bundle Profile Provision Service");
            final String msg = msgBldr.toString();
            Logger.minor(ctx, this, msg, t);
            final BundleManagerException exception = new BundleManagerException(msg, t);
            throw exception;
        }
        finally
        {
            pm.log(ctx);
        }
        
        return result;
    }

    public short removeBundleProfile(int spId, long bundleId) throws BundleManagerException
    {
        Parameter[] inParamSet = new Parameter[0];
        return removeBundleProfile(getContext(), spId, bundleId, inParamSet);
    }

    private short removeBundleProfile(Context ctx, int spId, long bundleId, Parameter[] inParamSet)
            throws BundleManagerException
    {
        final LogMsg pm = new PMLogMsg(getModule(), CLIENT_NAME, "removeBundleProfile");
        final short result;
        BundleProfileProvision service = getService();
        
        if (service == null)
        {
            final BundleManagerException exception = new BundleManagerException(
                    "Cannot retrieve " + CLIENT_NAME + " CORBA service. Unable to remove bundle profile.");
            Logger.minor(ctx, this, exception.getMessage(), exception);
            throw exception;
        }
        
        if (Logger.isInfoEnabled())
        {
            final StringBuilder msg = new StringBuilder();
            msg.append("Sending CORBA request BundleProfileProvision.removeBundleProfile() to URCS: ");
            msg.append("bundleId = ");
            msg.append(bundleId);
            LogSupport.info(ctx, this, msg.toString());
        }

        try
        {
            BundleReturnParam returnParam = service.removeBundleProfile(spId, bundleId, inParamSet);
            
            if (LogSupport.isDebugEnabled(ctx))
            {
                final StringBuilder msg = new StringBuilder();
                msg.append("CORBA request BundleProfileProvision.removeBundleProfile() to URCS: ");
                msg.append("ResultCode = ");
                msg.append(returnParam.resultCode);
                LogSupport.debug(ctx, this, msg.toString());
            }
            
            result = returnParam.resultCode;

        }
        catch (final org.omg.CORBA.SystemException e)
        {
            final StringBuilder msgBldr = new StringBuilder();
            msgBldr.append("CORBA communication with URCS Bundle Profile Provision Service server failed");
            final String msg = msgBldr.toString();
            Logger.minor(ctx, this, msg, e);
            final BundleManagerException exception = new BundleManagerException(msg, e);
            throw exception;
        }
        catch (final Throwable t)
        {
            final StringBuilder msgBldr = new StringBuilder();
            msgBldr.append("Unexpected failure in CORBA communication with URCS Bundle Profile Provision Service");
            final String msg = msgBldr.toString();
            Logger.minor(ctx, this, msg, t);
            final BundleManagerException exception = new BundleManagerException(msg, t);
            throw exception;
        }
        finally
        {
            pm.log(ctx);
        }
        
        return result;
    }

    public short updateBundleProfile(long bundleId, com.redknee.app.crm.bean.core.BundleProfile bundle) throws BundleManagerException
    {        
        return updateBundleProfile(getContext(), bundleId, bundle, getInParamsetForBundleCreateAndUpdate(bundle));
    }

    private short updateBundleProfile(Context ctx, long bundleId, com.redknee.app.crm.bean.core.BundleProfile bundle, Parameter[] inParamSet)
            throws BundleManagerException
    {
        final LogMsg pm = new PMLogMsg(getModule(), CLIENT_NAME, "updateBundleProfile");
        final short result;
        BundleProfileProvision service = getService();
        
        if (service == null)
        {
            final BundleManagerException exception = new BundleManagerException(
                    "Cannot retrieve " + CLIENT_NAME + " CORBA service. Unable to update bundle profile.");
            Logger.minor(ctx, this, exception.getMessage(), exception);
            throw exception;
        }
        
        if (Logger.isInfoEnabled())
        {
            final StringBuilder msg = new StringBuilder();
            msg.append("Sending CORBA request BundleProfileProvision.updateBundleProfile() to URCS: ");
            msg.append("bundleId = ");
            msg.append(bundleId);
            msg.append(", bundle = ");
            msg.append(bundle.toString());
            LogSupport.info(ctx, this, msg.toString());
        }

        try
        {
            BundleProfile adaptedBundleProfile = BundleProfileAdapter.adaptBundleProfile(bundle);
            
            BundleReturnParam returnParam = service.updateBundleProfile(bundleId, adaptedBundleProfile, inParamSet);
            
            if (LogSupport.isDebugEnabled(ctx))
            {
                final StringBuilder msg = new StringBuilder();
                msg.append("CORBA request BundleProfileProvision.updateBundleProfile() to URCS: ");
                msg.append("ResultCode = ");
                msg.append(returnParam.resultCode);
                LogSupport.debug(ctx, this, msg.toString());
            }
            
            result = returnParam.resultCode;

        }
        catch (final org.omg.CORBA.SystemException e)
        {
            final StringBuilder msgBldr = new StringBuilder();
            msgBldr.append("CORBA communication with URCS Bundle Profile Provision Service server failed");
            final String msg = msgBldr.toString();
            Logger.minor(ctx, this, msg, e);
            final BundleManagerException exception = new BundleManagerException(msg, e);
            throw exception;
        }
        catch (final Throwable t)
        {
            final StringBuilder msgBldr = new StringBuilder();
            msgBldr.append("Unexpected failure in CORBA communication with URCS Bundle Profile Provision Service");
            final String msg = msgBldr.toString();
            Logger.minor(ctx, this, msg, t);
            final BundleManagerException exception = new BundleManagerException(msg, t);
            throw exception;
        }
        finally
        {
            pm.log(ctx);
        }
        
        return result;
    }


    /**
     * Used for Logging
     * @return
     */
    private String getModule()
    {
        return this.getClass().getName();
    }

}
