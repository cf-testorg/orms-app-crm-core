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
package com.redknee.app.crm.bundle;

import com.redknee.framework.xhome.beans.xi.XInfo;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.beans.xi.PropertyInfo;



/**
 * Manages the compatibility between Bundle Manager 2_5 and 2_7
 * For the ModelProductBundleManager project for the Subscriber bucket bean 
 * @author arturo.medina@redknee.com
 *
 */
public interface SubcriberBucketModelBundleManager
{
    
    /**
     * Returns the home class for use in the subscriber home key
     * on the context
     * @param ctx
     * @return
     */
    public Class getSubscriberBucketHome();

    /**
     * Decides if the application should install the bucket V26
     * @return true if the application is using 2_6 BM model
     */
    public boolean installSubscriberBucketV6();
    
    /**
     * Returns the XInfo supported by this CRM configuration
     * @return
     */
    public XInfo getSubscriberBucketXInfo();
    
    /**
     * Returns a new XDB Home for the subscriber bucket
     * @param ctx
     * @return
     */
    public Home getSubscriberBucketXDBHome(Context ctx);
    
    /**
     * returns a SubscriberBucket XML home depending on the BM model version 
     * @param ctx
     * @param fileName
     * @return
     */
    public Home getSubscriberBucketXMLHome(Context ctx, String fileName);
    
    /**
     * Adapts the subscriber bucket to the natural source (so far SubscriberBucket V21 and V26) 
     * @param ctx
     * @param source
     * @param destination
     * @return
     */
    public SubscriberBucket adaptSubscriberBucket(Context ctx, Object source, SubscriberBucket destination);
    
    
    /**
     * unAdapts the subscriber bucket into the required RMI bucket (v21 or v26)
     * @param ctx
     * @param source
     * @param destination
     * @return
     */
    public Object unAdaptSubscriberBucket(Context ctx, SubscriberBucket source, Object destination);

    /**
     * Returns the XInformation for MSISDN on ths subscriber bucket
     * @return
     */
    public PropertyInfo getMSISDNXInfo();
    
    
    /**
     * Returns the XInformation for Bundle Id on ths subscriber bucket
     * @return
     */
    public PropertyInfo getBundleIdXInfo();
    
    
    /**
     * Returns the XInformation for Bucket Id on ths subscriber bucket
     * @return
     */
    public PropertyInfo getBucketIdXInfo();
    

}
