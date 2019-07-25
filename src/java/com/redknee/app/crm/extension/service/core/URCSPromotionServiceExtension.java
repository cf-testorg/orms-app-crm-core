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
package com.redknee.app.crm.extension.service.core;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.redknee.framework.xhome.beans.CompoundIllegalStateException;
import com.redknee.framework.xhome.beans.IllegalPropertyArgumentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.And;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.elang.In;
import com.redknee.framework.xhome.elang.NEQ;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.HomeOperationEnum;
import com.redknee.framework.xhome.xenum.AbstractEnum;
import com.redknee.framework.xlog.log.LogSupport;

import com.redknee.app.crm.CoreCrmLicenseConstants;
import com.redknee.app.crm.bean.AuxiliaryServiceStateEnum;
import com.redknee.app.crm.bean.AuxiliaryServiceTypeEnum;
import com.redknee.app.crm.bean.AuxiliaryServiceXInfo;
import com.redknee.app.crm.bean.ServiceTypeEnum;
import com.redknee.app.crm.bean.ServiceXInfo;
import com.redknee.app.crm.bean.core.AuxiliaryService;
import com.redknee.app.crm.bean.core.Service;
import com.redknee.app.crm.extension.DependencyValidatableExtension;
import com.redknee.app.crm.extension.ExtensionAware;
import com.redknee.app.crm.extension.FinalExtension;
import com.redknee.app.crm.extension.MandatoryExtension;
import com.redknee.app.crm.extension.TypeDependentExtension;
import com.redknee.app.crm.extension.auxiliaryservice.URCSPromotionAuxSvcExtensionXInfo;
import com.redknee.app.crm.extension.auxiliaryservice.core.URCSPromotionAuxSvcExtension;
import com.redknee.app.crm.extension.service.URCSPromotionServiceExtensionHome;
import com.redknee.app.crm.extension.service.URCSPromotionServiceExtensionXInfo;
import com.redknee.app.crm.license.LicenseAware;
import com.redknee.app.crm.support.ExtensionSupportHelper;
import com.redknee.app.crm.support.HomeSupportHelper;
import com.redknee.app.crm.support.LicensingSupportHelper;

/**
 * 
 * 
 * @author ksivasubramaniam
 * @since
 */
public class URCSPromotionServiceExtension extends
		com.redknee.app.crm.extension.service.URCSPromotionServiceExtension
		implements FinalExtension, 
		DependencyValidatableExtension,
		MandatoryExtension, 
		TypeDependentExtension,
        LicenseAware
{

	public static URCSPromotionServiceExtension getURCSPromotionServiceExtension(
			Context ctx, long serviceID) throws HomeException

	{
		Home home = (Home) ctx.get(URCSPromotionServiceExtensionHome.class);
		return (URCSPromotionServiceExtension) home.find(ctx, new EQ(
				URCSPromotionServiceExtensionXInfo.SERVICE_ID, serviceID));
	}

	public void validateDependency(Context ctx) throws IllegalStateException {
		CompoundIllegalStateException cise = new CompoundIllegalStateException();

		// Validate whether or not this extension is allowed to be contained
		// within the parent bean.
		ExtensionAware parentBean = this.getParentBean(ctx);
		if (parentBean instanceof Service) {
			Service service = (Service) parentBean;
			if (!ServiceTypeEnum.URCS_PROMOTION.equals(service.getType())) {
				cise.thrown(new IllegalArgumentException(this.getName(ctx)
						+ " extension only allowed for "
						+ ServiceTypeEnum.URCS_PROMOTION + " services."));
			}
			validatePricePlanService(ctx, service);
		}
		cise.throwAll();
	}

	@Override
	public boolean isValidForType(AbstractEnum serviceType) {
		return ServiceTypeEnum.URCS_PROMOTION.equals(serviceType);
	}

    @Override
    public boolean isLicensed(Context ctx)
    {
        return true;        
    }
    
	public static void validate(final Context ctx, final Object obj)
			throws IllegalStateException 
	{
		if (obj.getClass().isAssignableFrom(AuxiliaryService.class))
		{
			final AuxiliaryService auxSrv = (AuxiliaryService) obj;
			validateAuxiliaryService(ctx, auxSrv);	
		}
	}

	private static void validateAuxiliaryService(final Context ctx,
			final AuxiliaryService auxSrv) throws IllegalStateException {
		if (AuxiliaryServiceTypeEnum.URCS_Promotion.equals(auxSrv.getType()))

		{
			long newServiceOption = URCSPromotionAuxSvcExtension.DEFAULT_SERVICEOPTION;
			URCSPromotionAuxSvcExtension urcsPromotionAuxSvcExtension = ExtensionSupportHelper
					.get(ctx).getExtension(ctx, auxSrv,
							URCSPromotionAuxSvcExtension.class);
			if (urcsPromotionAuxSvcExtension != null) 
			{
				newServiceOption = urcsPromotionAuxSvcExtension
						.getServiceOption();
			} else {
				LogSupport.minor(
						ctx,
						URCSPromotionServiceExtension.class,
						"Unable to find required extension of type '"
								+ URCSPromotionAuxSvcExtension.class
										.getSimpleName()
								+ "' for auxiliary service "
								+ auxSrv.getIdentifier());
			}

			if (!HomeOperationEnum.CREATE.equals(ctx
					.get(HomeOperationEnum.class))) 
			{
				try 
				{
					final AuxiliaryService auxSrvOld = HomeSupportHelper.get(
							ctx).findBean(ctx, AuxiliaryService.class,
							auxSrv.getIdentifier());

					long oldServiceOption = URCSPromotionAuxSvcExtension.DEFAULT_SERVICEOPTION;
					URCSPromotionAuxSvcExtension oldUrcsPromotionAuxSvcExtension = ExtensionSupportHelper
							.get(ctx).getExtension(ctx, auxSrvOld,
									URCSPromotionAuxSvcExtension.class);
					if (oldUrcsPromotionAuxSvcExtension != null) 
					{
						oldServiceOption = oldUrcsPromotionAuxSvcExtension
								.getServiceOption();
					} 
					else 
					{
						LogSupport.minor(
								ctx,
								URCSPromotionServiceExtension.class,
								"Unable to find required extension of type '"
										+ URCSPromotionAuxSvcExtension.class
												.getSimpleName()
										+ "' for auxiliary service "
										+ auxSrvOld.getIdentifier());
					}

					if (oldServiceOption == newServiceOption) {
						// nothing to do
						return;
					}
				} catch (HomeException e) {
					final CompoundIllegalStateException exceptions = new CompoundIllegalStateException();
					final IllegalPropertyArgumentException propException;
					propException = new IllegalPropertyArgumentException(
							AuxiliaryServiceXInfo.IDENTIFIER,
							"Can't validate Auxiliary Service with ID "
									+ auxSrv.getIdentifier() + ". Reason: "
									+ e.getMessage());
					propException.initCause(e);
					exceptions.thrown(propException);
					exceptions.throwAll();
				}
			}

			findDuplicate(ctx, auxSrv.getSpid(), newServiceOption);
		}
	}

	private static void validatePricePlanService(final Context ctx,
			final Service service) throws IllegalStateException 
	{
		long newServiceOption = URCSPromotionAuxSvcExtension.DEFAULT_SERVICEOPTION;
		URCSPromotionServiceExtension urcsPromotionExt = ExtensionSupportHelper
				.get(ctx).getExtension(ctx, service,
						URCSPromotionServiceExtension.class);

		if (urcsPromotionExt != null) 
		{
			newServiceOption = urcsPromotionExt.getServiceOption();
		} 
		else 
		{
			LogSupport.minor(
					ctx,
					URCSPromotionServiceExtension.class,
					"Unable to find required extension of type '"
							+ URCSPromotionServiceExtension.class
									.getSimpleName() + "' for  service "
							+ service.getIdentifier());
		}

		if (!HomeOperationEnum.CREATE.equals(ctx.get(HomeOperationEnum.class))) 
		{
			try {
				final Service oldService = HomeSupportHelper.get(ctx).findBean(
						ctx, Service.class, service.getIdentifier());

				long oldServiceOption = URCSPromotionServiceExtension.DEFAULT_SERVICEOPTION;
				URCSPromotionServiceExtension oldUrcsPromotionSvcExt = ExtensionSupportHelper
						.get(ctx).getExtension(ctx, oldService,
								URCSPromotionServiceExtension.class);
				if (oldUrcsPromotionSvcExt != null) {
					oldServiceOption = oldUrcsPromotionSvcExt
							.getServiceOption();
				} else {
					LogSupport.minor(
							ctx,
							URCSPromotionServiceExtension.class,
							"Unable to find required extension of type '"
									+ URCSPromotionServiceExtension.class
											.getSimpleName()
									+ "' for  service "
									+ oldService.getIdentifier());
				}

				if (oldServiceOption == newServiceOption) {
					// nothing to do
					return;
				}
			}

			catch (HomeException e) {
				final CompoundIllegalStateException exceptions = new CompoundIllegalStateException();
				final IllegalPropertyArgumentException propException;
				propException = new IllegalPropertyArgumentException(
						ServiceXInfo.ID, "Can't validate Service with ID "
								+ service.getIdentifier() + ". Reason: "
								+ e.getMessage());
				propException.initCause(e);
				exceptions.thrown(propException);
				exceptions.throwAll();
			}
			findDuplicate(ctx, service.getSpid(), newServiceOption);
		}

	}

	private static void findDuplicate(final Context ctx, int spid,
			final long newServiceOption) 
	{
		try 
		{
			Collection<URCSPromotionAuxSvcExtension> extensions = HomeSupportHelper
					.get(ctx)
					.getBeans(
							ctx,
							URCSPromotionAuxSvcExtension.class,
							new EQ(
									URCSPromotionAuxSvcExtensionXInfo.SERVICE_OPTION,
									newServiceOption));

			if (extensions != null && extensions.size() > 0) 
			{
				Set<Long> identifiers = new HashSet<Long>();
				for (URCSPromotionAuxSvcExtension extension : extensions) 
				{
					identifiers.add(extension.getAuxiliaryServiceId());
				}

				And condition = new And();
				condition.add(new EQ(AuxiliaryServiceXInfo.TYPE,
						AuxiliaryServiceTypeEnum.URCS_Promotion));
				condition.add(new EQ(AuxiliaryServiceXInfo.SPID, spid));
				condition.add(new In(AuxiliaryServiceXInfo.IDENTIFIER,
						identifiers));
				condition.add(new NEQ(AuxiliaryServiceXInfo.STATE,
						AuxiliaryServiceStateEnum.CLOSED));
				final AuxiliaryService duplicateAuxService = HomeSupportHelper
						.get(ctx).findBean(ctx, AuxiliaryService.class,
								condition);

				if (duplicateAuxService != null) 
				{
					final CompoundIllegalStateException exceptions = new CompoundIllegalStateException();
					StringBuffer buf = new StringBuffer("Service option ["
							+ newServiceOption + "] already in used by ");
						buf.append(" non-CLOSED Auxiliary Service [" + duplicateAuxService.getIdentifier() + "] .");
					

					exceptions.thrown(new IllegalPropertyArgumentException(
							URCSPromotionAuxSvcExtensionXInfo.SERVICE_OPTION,
							buf.toString()));
					exceptions.throwAll();
				}
			}

			Collection<URCSPromotionServiceExtension> serExts = HomeSupportHelper
					.get(ctx)
					.getBeans(
							ctx,
							URCSPromotionServiceExtension.class,
							new EQ(
									URCSPromotionServiceExtensionXInfo.SERVICE_OPTION,
									newServiceOption));
			if (serExts != null & serExts.size() > 0)
			{
				Set<Long> identifiers = new HashSet<Long>();
				for (URCSPromotionServiceExtension extension : serExts) 
				{
					identifiers.add(extension.getServiceId());
				}
				And condition2 = new And();
				condition2.add(new EQ(ServiceXInfo.TYPE,
						ServiceTypeEnum.URCS_PROMOTION));
				condition2.add(new In(ServiceXInfo.ID, identifiers));

				final Service duplicateService = HomeSupportHelper.get(ctx)
						.findBean(ctx, Service.class, condition2);

				if (duplicateService != null) {
					final CompoundIllegalStateException exceptions = new CompoundIllegalStateException();
					StringBuffer buf = new StringBuffer("Service option ["
							+ newServiceOption + "] already in used by ");

	
						buf.append(" Price Plan service ["
								+ duplicateService.getID() + "] .");
	
					exceptions.thrown(new IllegalPropertyArgumentException(
							URCSPromotionAuxSvcExtensionXInfo.SERVICE_OPTION,
							buf.toString()));
					exceptions.throwAll();
				}
			}
		} 
		catch (HomeException e)
		{
			final CompoundIllegalStateException exceptions = new CompoundIllegalStateException();
			final IllegalPropertyArgumentException propException;
			propException = new IllegalPropertyArgumentException(
					URCSPromotionAuxSvcExtensionXInfo.SERVICE_OPTION,
					"Can't validate Service with service option "
							+ newServiceOption + ". Reason: " + e.getMessage());
			propException.initCause(e);
			exceptions.thrown(propException);
			exceptions.throwAll();
		}
	}
}
