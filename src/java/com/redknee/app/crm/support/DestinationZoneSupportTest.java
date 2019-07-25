/*
 * Created on Jun 6, 2005
 *
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
package com.redknee.app.crm.support;

import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.redknee.app.crm.bean.BillingOptionMappingHome;
import com.redknee.app.crm.bean.BillingOptionMappingTransientHome;
import com.redknee.app.crm.bean.MsisdnPrefix;
import com.redknee.app.crm.bean.MsisdnPrefixHome;
import com.redknee.app.crm.bean.MsisdnPrefixTransientHome;
import com.redknee.app.crm.bean.MsisdnZonePrefix;
import com.redknee.app.crm.bean.MsisdnZonePrefixHome;
import com.redknee.app.crm.bean.MsisdnZonePrefixTransientHome;
import com.redknee.app.crm.collection.PrefixService;
import com.redknee.app.crm.collection.PrefixServiceImpl;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.context.ContextAware;
import com.redknee.framework.xhome.context.ContextSupport;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.NotifyingHome;
import com.redknee.framework.xlog.log.DebugLogMsg;
import com.redknee.framework.xlog.log.LogSupport;

/**
 * Tests getting the correct destination zone id and description
 * 
 * @author psperneac
 *
 */
public class DestinationZoneSupportTest extends TestCase implements ContextAware
{
	protected transient Context context;

	public DestinationZoneSupportTest()
    {
        super();
    }

    public DestinationZoneSupportTest(String arg0)
    {
        super(arg0);
    }

    /**
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception
	{
		super.setUp();
		
		setContext(new ContextSupport());
		Context ctx=getContext();

        ctx.put(MsisdnZonePrefixHome.class,new NotifyingHome(new MsisdnZonePrefixTransientHome(ctx)));
        ctx.put(MsisdnPrefixHome.class,new NotifyingHome(new MsisdnPrefixTransientHome(ctx)));
        ctx.put(BillingOptionMappingHome.class,new NotifyingHome(new BillingOptionMappingTransientHome(ctx)));
        
        ctx.put(PrefixService.class, new PrefixServiceImpl(ctx));
        
	}
	
	public Context getContext()
	{
		return context;
	}
	
	public void setContext(Context context)
	{
		this.context=context;
	}

    public static Test suite()
    {
        TestSuite suite=new TestSuite(DestinationZoneSupportTest.class);
        
        return suite;
    }
    
    public static Test suite(Context ctx)
    {
        TestSuite suite=new TestSuite(DestinationZoneSupportTest.class);
        
        return suite;
    }
    
	public void testGetDestinationZoneDescription()
	{
        Context ctx=getContext();
        
        createEntries0();
        assertEquals(DestinationZoneSupportHelper.get(ctx).getDestinationZoneDescription(ctx, "29"),"B");
        assertEquals(DestinationZoneSupportHelper.get(ctx).getDestinationZoneDescription(ctx, "290"),"A");

        removeEntries();
        
        createEntries1();
        assertEquals(DestinationZoneSupportHelper.get(ctx).getDestinationZoneDescription(ctx, "29"),"B");
        assertEquals(DestinationZoneSupportHelper.get(ctx).getDestinationZoneDescription(ctx, "290"),"A");
    }

    private void removeEntries()
    {
        Context ctx=getContext();
        Home zhome=(Home) ctx.get(MsisdnZonePrefixHome.class);
        Home home=(Home) ctx.get(MsisdnPrefixHome.class);
        try
        {
            zhome.removeAll();
            home.removeAll();
        } 
        catch (UnsupportedOperationException e)
        {
            fail(e.getMessage());
            if(LogSupport.isDebugEnabled(ctx))
            {
                new DebugLogMsg(this,e.getMessage(),e).log(ctx);
            }
        } 
        catch (HomeException e)
        {
            fail(e.getMessage());
            if(LogSupport.isDebugEnabled(ctx))
            {
                new DebugLogMsg(this,e.getMessage(),e).log(ctx);
            }
        }
    }

    private void createEntries0()
    {
        Context ctx=getContext();
        Home zhome=(Home) ctx.get(MsisdnZonePrefixHome.class);
        Home home=(Home) ctx.get(MsisdnPrefixHome.class);
        
        try
        {
            MsisdnPrefix prefix;
            MsisdnZonePrefix zone;
            List prefixes;

            // A/A/290
            
            zone=new MsisdnZonePrefix();
            zone.setIdentifier(0);
            zone.setDescription("A");
            zhome.create(ctx,zone);
            
            prefix=new MsisdnPrefix();
            prefix.setId(0);
            prefix.setIdentifier(0);
            prefix.setDescription("A");
            prefix.setPrefix("290");
            home.create(ctx, prefix);

            // B/B/29
            
            zone=new MsisdnZonePrefix();
            zone.setIdentifier(1);
            zone.setDescription("B");
            zhome.create(ctx,zone);
            
            prefix=new MsisdnPrefix();
            prefix.setId(0);
            prefix.setIdentifier(1);
            prefix.setDescription("B");
            prefix.setPrefix("29");
            home.create(ctx, prefix);
        } 
        catch (IllegalArgumentException e)
        {
            fail(e.getMessage());
            if(LogSupport.isDebugEnabled(ctx))
            {
                new DebugLogMsg(this,e.getMessage(),e).log(ctx);
            }
        } 
        catch (HomeException e)
        {
            fail(e.getMessage());
            if(LogSupport.isDebugEnabled(ctx))
            {
                new DebugLogMsg(this,e.getMessage(),e).log(ctx);
            }
        }
    }

    private void createEntries1()
    {
        Context ctx=getContext();
        Home zhome=(Home) ctx.get(MsisdnZonePrefixHome.class);
        Home home=(Home) ctx.get(MsisdnPrefixHome.class);
        
        try
        {
            MsisdnPrefix prefix;
            MsisdnZonePrefix zone;
            List prefixes;

            // A/A/290
            
            zone=new MsisdnZonePrefix();
            zone.setIdentifier(0);
            zone.setDescription("AB");
            zhome.create(ctx,zone);
            
            prefix=new MsisdnPrefix();
            prefix.setId(0);
            prefix.setIdentifier(0);
            prefix.setDescription("A");
            prefix.setPrefix("290");
            home.create(ctx,prefix);
            
            prefix=new MsisdnPrefix();
            prefix.setId(1);
            prefix.setIdentifier(0);
            prefix.setDescription("B");
            prefix.setPrefix("29");
            home.create(ctx,prefix);
        } 
        catch (IllegalArgumentException e)
        {
            fail(e.getMessage());
            if(LogSupport.isDebugEnabled(ctx))
            {
                new DebugLogMsg(this,e.getMessage(),e).log(ctx);
            }
        } 
        catch (HomeException e)
        {
            fail(e.getMessage());
            if(LogSupport.isDebugEnabled(ctx))
            {
                new DebugLogMsg(this,e.getMessage(),e).log(ctx);
            }
        }
    }
    
}
