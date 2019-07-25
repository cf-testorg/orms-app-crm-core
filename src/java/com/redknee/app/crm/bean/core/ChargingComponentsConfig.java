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

import com.redknee.framework.xhome.context.Context;

import com.redknee.app.crm.bean.ChargingComponentMetaData;
import com.redknee.app.crm.bean.ChargingComponentPlaceEnum;


/**
 * Concrete class for ComponentChargingConfiguration
 *
 * @author simar.singh@redknee.com
 * @since 8.4
 */
public class ChargingComponentsConfig extends com.redknee.app.crm.bean.ChargingComponentsConfig implements com.redknee.framework.xhome.beans.Validatable
{
    private static final long serialVersionUID = 1L;

   
    /**
     *   @param componmentID - is the component-ID whose place value will component find
     */
     public ChargingComponentPlaceEnum getComponentPlace(long componentID) throws IndexOutOfBoundsException
     {
         if(componentFirst_.isEnabled() && componentFirst_.getID() == componentID)
         {
             return ChargingComponentPlaceEnum.FIRST;
         }
         if(componentSecond_.isEnabled() && componentSecond_.getID() == componentID)
         {
             return ChargingComponentPlaceEnum.SECOND;
         }
         if(componentThird_.isEnabled() && componentThird_.getID() == componentID)
         {
             return ChargingComponentPlaceEnum.THIRD;
         }
         throw new IndexOutOfBoundsException("Charging Component with ID [" + componentID + "] not Found");
         
     }
     
     /**
     *   @param componmentID - the component-ID corresponding to which the function returns the meta-bean
     */
    public ChargingComponentMetaData getComponentMetaData(long componentID)
     {
         if(componentFirst_.isEnabled() && componentFirst_.getID() == componentID)
         {
             return componentFirst_;
         }
         if(componentSecond_.isEnabled() && componentSecond_.getID() == componentID)
         {
             return componentSecond_;
         }
         if(componentThird_.isEnabled() && componentThird_.getID() == componentID)
         {
             return componentThird_;
         }
         return null;        
     }
    
     public void validate(Context ctx) throws IllegalStateException
     {
         validate(); 
     }
    
    public void validate() throws IllegalStateException
     {
         if((componentFirst_.getID() == componentSecond_.getID()) && componentFirst_.isEnabled() && componentSecond_.isEnabled())
         {
             throw new IllegalStateException("Charging Component IDs should be unique. Please check first and second component.");      
         }
         
         if((componentSecond_.getID() == componentThird_.getID()) && componentSecond_.isEnabled() && componentThird_.isEnabled())
         {
             throw new IllegalStateException("Charging Component IDs should be unique. Please check second and thrid component.");      
         }
         
         if((componentFirst_.getID() == componentThird_.getID()) && componentFirst_.isEnabled() && componentThird_.isEnabled())
         {
             throw new IllegalStateException("Charging Component IDs should be unique. Please check first and third component.");      
         }
     }          
    
}
