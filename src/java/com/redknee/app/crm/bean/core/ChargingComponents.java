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
package com.redknee.app.crm.bean.core;

import java.util.StringTokenizer;

import com.redknee.app.crm.bean.ChargingComponentPlaceEnum;
import com.redknee.app.crm.bean.ComponentCharge;

/**
 * Concrete class for model's custom java code
 * 
 * @author simar.singh@redknee.com
 * @since 8.4
 */
public class ChargingComponents extends com.redknee.app.crm.bean.ChargingComponents
{

    private static final long serialVersionUID = 1L;


    public ChargingComponents setComponentCharge(ComponentCharge componentCharge, ChargingComponentPlaceEnum place)
    {
        return place.setComponent(this, componentCharge);
    }


    public ComponentCharge getComponentCharge(ComponentCharge componentCharge, ChargingComponentPlaceEnum place)
    {
        return place.getComponent(this);
    }


    /**
     * Sets Charging Components from String of Pattern { <component id>1|<rate>1|<GL
     * Code>1|<amount charged>1;<component id>2|<rate>2|<GL Code>2|<amount
     * charged>2;<component id>3|<rate>3|<GL Code>3|<amount charged>3 }
     * 
     * @param ctx
     * @param allComponentString
     * @param configuration
     * @return
     * @throws IllegalStateException
     */
    public ChargingComponents setAllComponentsFromString(String allComponentString,
            ChargingComponentsConfig configuration) throws IllegalStateException
    {
        final StringTokenizer tokenizer = new StringTokenizer(allComponentString, ";");
        int counter = 0;
        while (tokenizer.hasMoreTokens() || counter < MAX_NUMBER_OF_COMPONENTS)
        {
            setSingleComponentFromString(tokenizer.nextToken(), configuration);
            ++counter;
        }
        return this;
    }


    /**
     * Set Charging Component from String of Pattern { <component id>|<rate>|<GL
     * Code>|<amount charged> }
     * 
     * @param ctx
     * @param singleCmponentString
     * @param configuration
     * @return
     * @throws IllegalStateException
     */
    public ChargingComponents setSingleComponentFromString(String singleCmponentString,
            ChargingComponentsConfig configuration) throws IllegalStateException
    {
        final String idToken;
        final String chargeToken;
        final String rateToken;
        final String glCodeToken;
        try
        {
            final StringTokenizer componentTokenizer = new StringTokenizer(singleCmponentString, "|");
            int numberOfTokents = componentTokenizer.countTokens();
            if (numberOfTokents < 4)
            {
                throw new IllegalStateException("Error. Missing fields in component string. Got only [" + numberOfTokents
                        + "] of  required 4 tokens {<component id>|<rate>|<GLCode>|<amount charged>}.");
            }
            idToken = componentTokenizer.nextToken().trim();
            rateToken = componentTokenizer.nextToken().trim();
            glCodeToken = componentTokenizer.nextToken().trim();
            chargeToken = componentTokenizer.nextToken().trim();
            final ComponentCharge componentCharge;
            final ChargingComponentPlaceEnum place;
            {
                componentCharge = new ComponentCharge();
                componentCharge.setRate(Long.valueOf(rateToken));
                componentCharge.setGlCode(glCodeToken);
                componentCharge.setCharge(Long.valueOf(chargeToken));
                place = configuration.getComponentPlace(Long.valueOf(idToken));
            }
            setComponentCharge(componentCharge, place);
        }
        catch (NumberFormatException ne)
        {
            throw new IllegalStateException(ne.getMessage()
                    + " [ Check ID, Rate and Charge values; they should be numberic]", ne);
        }
        catch (IndexOutOfBoundsException inde)
        {
            throw new IllegalStateException(inde.getMessage() + " [ Check if Component ID exists and is Valid ]", inde);
        }
        catch (IllegalStateException e)
        {
            throw e;
        }
        catch (Throwable t)
        {
            throw new IllegalStateException(t.getMessage(), t);
        }
        return this;
    }

    public final static int MAX_NUMBER_OF_COMPONENTS = 3;
}
