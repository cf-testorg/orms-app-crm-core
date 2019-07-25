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
package com.redknee.app.crm.bean.core;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.elang.True;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xlog.log.LogSupport;

import com.redknee.app.crm.bean.account.SubscriptionTypeEnum;
import com.redknee.app.crm.bean.account.SubscriptionTypeXInfo;
import com.redknee.app.crm.move.MoveRequest;
import com.redknee.app.crm.support.HomeSupportHelper;


/**
 * 
 *
 * @author aaron.gourley@redknee.com
 * @since 
 */
public class SubscriptionType extends com.redknee.app.crm.bean.account.SubscriptionType
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    protected final static Set<SubscriptionTypeEnum> VOICE_SUBSCRIPTION_TYPES = 
        Collections.unmodifiableSet(new HashSet<SubscriptionTypeEnum>(Arrays.asList(
                SubscriptionTypeEnum.AIRTIME,
                SubscriptionTypeEnum.WIRE_LINE)));
    
    protected final static Set<SubscriptionTypeEnum> MESSAGING_SUBSCRIPTION_TYPES = 
        Collections.unmodifiableSet(new HashSet<SubscriptionTypeEnum>(Arrays.asList(
                SubscriptionTypeEnum.AIRTIME)));

    protected final static Set<SubscriptionTypeEnum> WIRELESS_SUBSCRIPTION_TYPES =
        Collections.unmodifiableSet(new HashSet<SubscriptionTypeEnum>(Arrays.asList(
                SubscriptionTypeEnum.AIRTIME)));

    protected final static Set<SubscriptionTypeEnum> DATA_SUBSCRIPTION_TYPES = 
        Collections.unmodifiableSet(
                new HashSet<SubscriptionTypeEnum>(
                        Arrays.asList(
                                SubscriptionTypeEnum.AIRTIME,
                                SubscriptionTypeEnum.BROADBAND)));

    protected final static Set<SubscriptionTypeEnum> SERVICE_SUBSCRIPTION_TYPES = 
        Collections.unmodifiableSet(
                new HashSet<SubscriptionTypeEnum>(
                        Arrays.asList(
                                SubscriptionTypeEnum.AIRTIME,
                                SubscriptionTypeEnum.WIRE_LINE,
                                SubscriptionTypeEnum.BROADBAND)));

    protected final static Set<SubscriptionTypeEnum> WALLET_SUBSCRIPTION_TYPES = 
        Collections.unmodifiableSet(
                new HashSet<SubscriptionTypeEnum>(
                        Arrays.asList(
                                SubscriptionTypeEnum.MOBILE_WALLET,
                                SubscriptionTypeEnum.NETWORK_WALLET)));



    protected final static Set<SubscriptionTypeEnum> MSISDN_AWARE_SUBSCRIPTION_TYPES = 
        Collections.unmodifiableSet(
                new HashSet<SubscriptionTypeEnum>(
                        Arrays.asList(
                                SubscriptionTypeEnum.AIRTIME,
                                SubscriptionTypeEnum.WIRE_LINE,
                                SubscriptionTypeEnum.MOBILE_WALLET,
                                SubscriptionTypeEnum.NETWORK_WALLET)));

    protected final static Set<SubscriptionTypeEnum> CONVERSION_SUPPORTED_TYPES = 
        Collections.unmodifiableSet(
                new HashSet<SubscriptionTypeEnum>(
                        Arrays.asList(
                                SubscriptionTypeEnum.AIRTIME)));


    /**
     * Gets the enumeration value of the subscription type.
     * 
     * @throws ArrayIndexOutOfBoundsException if enum index does not exist.
     * @return Subscription type enumeration for this subscription
     */
    public SubscriptionTypeEnum getTypeEnum()
    {
        SubscriptionTypeEnum type = SubscriptionTypeEnum.get((short)this.getType());
        return type;
    }

    public boolean isWallet()
    {
        return isOfType(WALLET_SUBSCRIPTION_TYPES);
    }

    public boolean isVoice()
    {
        return isOfType(VOICE_SUBSCRIPTION_TYPES);
    }

    public boolean isWireless()
    {
        return isOfType(WIRELESS_SUBSCRIPTION_TYPES);
    }

    public boolean isData()
    {
        return isOfType(DATA_SUBSCRIPTION_TYPES);
    }

    public boolean isMessaging()
    {
        return isOfType(MESSAGING_SUBSCRIPTION_TYPES);
    }

    public boolean isService()
    {
        return isOfType(SERVICE_SUBSCRIPTION_TYPES);
    }
    
    public boolean isMsisdnAware()
    {
        return isOfType(MSISDN_AWARE_SUBSCRIPTION_TYPES);
    }

    public boolean isMoveSupported(final MoveRequest request)
    {
        return true;
    }


    public boolean isOfType(final SubscriptionTypeEnum... types)
    {
        if (types != null && types.length > 0)
        {
            return isOfType(Arrays.asList(types));
        }
        else
        {
            return false;
        }
    }


    /**
     * @param type List of possible enumerated subscription type values.
     * @return True if this subscription's type matches the input type
     */
    public boolean isOfType(final Collection<SubscriptionTypeEnum> types)
    {
        if (types != null)
        {
            return types.contains(getTypeEnum());
        }
        else
        {
            return false;
        }
    }

    public static SubscriptionType getSubscriptionType(final Context ctx, final long subscriptionType)
    {
        SubscriptionType typeObj;
        try
        {
            typeObj = getSubscriptionTypeWithException(ctx, subscriptionType);
        }
        catch (HomeException e)
        {
            LogSupport.minor(ctx, SubscriptionType.class.getName(),
                    "Unable to retreive SubscriptionType ID=" + subscriptionType, e);
            typeObj = null;
        }

        return typeObj;
    }

    public static SubscriptionType getSubscriptionTypeWithException(final Context ctx, final long subscriptionType)
        throws HomeException
    {
        final SubscriptionType typeObj = HomeSupportHelper.get(ctx).findBean(ctx, SubscriptionType.class, 
                Long.valueOf(subscriptionType));
        return typeObj;
    }

    public static SubscriptionType getFirstSubscriptionType(final Context ctx)
    {
        SubscriptionType typeObj = null;
        try
        {
            typeObj = HomeSupportHelper.get(ctx).findBean(ctx, SubscriptionType.class, True.instance());
        }
        catch (HomeException e)
        {
            LogSupport.minor(ctx, SubscriptionType.class.getName(),
                    "Unable to retreive first SubscriptionType record", e);
        }
        return typeObj;
    }

    public static boolean isSubscriptionTypeExisting(final Context ctx, final long subscriptionType)
    {
        boolean result = false;
        try
        {
            result = HomeSupportHelper.get(ctx).hasBeans(ctx, SubscriptionType.class, subscriptionType);
        }
        catch (HomeException e)
        {
            LogSupport.minor(ctx, SubscriptionType.class.getName(),
                    "Unable to retreive SubscriptionType ID=" + subscriptionType, e);
        }
        return result;
    }

    public static boolean isSubscriptionTypeExisting(final Context ctx, final SubscriptionTypeEnum type)
    {
        boolean result = false;
        try
        {
            result = HomeSupportHelper.get(ctx).hasBeans(ctx, SubscriptionType.class,
                    new EQ(SubscriptionTypeXInfo.TYPE, Integer.valueOf(type.getIndex())));
        }
        catch (HomeException e)
        {
            LogSupport.minor(ctx, SubscriptionType.class.getName(),
                    "Unable to retreive SubscriptionType of type=" + type, e);
        }
        return result;
    }

    /**
     * 
     * @param ctx
     * @return Returns the first Subscription-Type object of type airitme in the SubscriptionType Store
     * @throws HomeException
     */
    public static SubscriptionType getINSubscriptionType(final Context ctx)
        throws HomeException
    {
        return getSubscriptionType(ctx, SubscriptionTypeEnum.AIRTIME);
    }

    /**
     * 
     * @param ctx
     * @return Returns the first Subscription-Type object of specified type in the SubscriptionType Store
     * @throws HomeException
     */
    public static SubscriptionType getSubscriptionType(final Context ctx, SubscriptionTypeEnum type)
        throws HomeException
    {
        SubscriptionType result = HomeSupportHelper.get(ctx).findBean(
                ctx,
                SubscriptionType.class,
                new EQ(SubscriptionTypeXInfo.TYPE, Integer.valueOf(type.getIndex())));

        return result;
    }

    /**
     * This method returns the enumerated SubscriptionType corresponding to the long
     * subscription identifier passed as an argument.
     * 
     * @param ctx Operating context
     * @param subscriptionTypeId Subscription Type bean identifier
     * @return The corresponding SubscriptionTypeEnum as found or null if not found
     * @throws HomeException
     */
    public static SubscriptionTypeEnum getSubscriptionTypeEnum(final Context ctx, final long subscriptionTypeId)
        throws HomeException
    {
        final SubscriptionType subType = HomeSupportHelper.get(ctx).findBean(ctx, SubscriptionType.class, subscriptionTypeId);
        if (subType == null)
        {
            return null;
        }
        return subType.getTypeEnum();
    }
}
