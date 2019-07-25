/*
 *  PrefixService
 *
 *  Author : Kevin Greer
 *  Date   : Nov 25, 2003
 *
 *  Copyright (c) Redknee, 2003
 *    - all rights reserved
 */
 
package com.redknee.app.crm.collection;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.context.ContextAwareSupport;
import com.redknee.framework.xhome.filter.Predicate;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeChangeEvent;
import com.redknee.framework.xhome.home.HomeChangeListener;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.HomeOperationEnum;
import com.redknee.framework.xhome.home.NotifyingHomeCmd;
import com.redknee.framework.xhome.util.collection.trie.PrefixMap;
import com.redknee.framework.xhome.util.collection.trie.AsciiPrefixMap;
import com.redknee.framework.xlog.log.DebugLogMsg;
import com.redknee.framework.xlog.log.LogSupport;
import com.redknee.framework.xlog.log.MajorLogMsg;
import com.redknee.framework.xlog.log.MinorLogMsg;

import com.redknee.app.crm.bean.BillingOptionMapping;
import com.redknee.app.crm.bean.BillingOptionMappingHome;
import com.redknee.app.crm.bean.MsisdnPrefix;
import com.redknee.app.crm.bean.MsisdnPrefixHome;
import com.redknee.app.crm.bean.MsisdnZonePrefix;
import com.redknee.app.crm.bean.MsisdnZonePrefixHome;


/** 
 * Given an MSISDN and a SPID, return a BillingOptionMapping based on prefix 
 * mappings defined in MsisdnZonePrefixHome. 
 */
public class PrefixServiceImpl extends ContextAwareSupport
    implements HomeChangeListener, PrefixService
{
    private PrefixCache cache_;    
    private boolean needUpdatingCache = true;
    private final ReentrantReadWriteLock lock_ = new ReentrantReadWriteLock();
    
    public PrefixServiceImpl(Context ctx)
    {
        setContext(ctx);
      
        validate();
      
        try
		{
            Home home = (Home) ctx.get(MsisdnZonePrefixHome.class);
			home.cmd(ctx,new NotifyingHomeCmd(this));

            home = (Home) ctx.get(MsisdnPrefixHome.class);
			home.cmd(ctx,new NotifyingHomeCmd(this));

			home = (Home) ctx.get(BillingOptionMappingHome.class);
			home.cmd(ctx,new NotifyingHomeCmd(this));            
		}
		catch (HomeException e)
		{
			if(LogSupport.isDebugEnabled(ctx))
			{
				new DebugLogMsg(this,e.getMessage(),e).log(ctx);
			}
		}
    }
   
    /** 
     * Deletes the PrefixMap because it is no longer valid. 
     */
    public void invalidate()
    {
       lock_.writeLock().lock();
    	{
       		needUpdatingCache = true; 	
    	}
       lock_.writeLock().unlock(); 
    }
   
   
    /** 
     * Copy the Home data to the PrefixMap. 
     */
    protected PrefixCache validate()
    {
    	
    	lock_.readLock().lock(); 
  
    	try
    	{
      		if ( !needUpdatingCache )
    		{
    			return cache_; 
    		}
    	} finally
    	{
    		lock_.readLock().unlock(); 
    	}
    	
    	
    	lock_.writeLock().lock(); 
    	
    	
    	try 
    	{
    	
      		
    		if ( !needUpdatingCache )
    		{
    			return cache_; 
    		}
    		
      		cache_ = new PrefixCache(); 

    		PrefixMap allZones = new AsciiPrefixMap();
    		PrefixMap allShortCodeTrueZones = new AsciiPrefixMap();
    		PrefixMap allShortCodeFalseZones = new AsciiPrefixMap();
    		Collection allRules = null;
        
    		// Gets set to true if an empty prefix ("") is found
    		boolean   foundDefault = false;

    		try
    		{   			
                Home home = (Home) getContext().get(MsisdnZonePrefixHome.class);

                for ( Iterator i = home.selectAll(getContext()).iterator(); i.hasNext(); )
                {
                    MsisdnZonePrefix  zone = (MsisdnZonePrefix) i.next();

                    for ( Iterator j = zone.getPrefixes(getContext()).iterator() ; j.hasNext() ; )
                    {
                        MsisdnPrefix prefix = (MsisdnPrefix) j.next();

                        try
                        {
                            MsisdnZonePrefix zone2 = (MsisdnZonePrefix) zone.clone();
                  
                            // this moves the prefix description into this copy of the zone if the prefix has any description
                            if ( ! "".equals(prefix.getDescription().trim()) )
                            {
                                zone2.setPrefixDescription(prefix.getDescription());  
                            }
                  
                            String realPrefix = prefix.getPrefix().trim();
                  
                            if ( realPrefix.equals("") )
                            {
                                foundDefault = true;   
                            }

                            //Fill up the allShortCodeTrueZones and allShortCodeFalseZones first
                            //as filling up allZones may thorw IllegalArgumentException becoz of
                            //unique constraint
                            if( prefix.getIsShortCode())
                            {
                                allShortCodeTrueZones.add(realPrefix, zone2);
                            }
                            else
                            {
                                allShortCodeFalseZones.add(realPrefix, zone2);
                            }

                            allZones.add(realPrefix, zone2);
                            
                        }
                        catch (CloneNotSupportedException e)
                        {
                        	if(LogSupport.isDebugEnabled(getContext()))
                        	{
                        		new DebugLogMsg(this,e.getMessage(),e).log(getContext());
                        	}
                        }
                        catch (IllegalArgumentException e)
                        {
                            // This will happen if the prefix is invalid: ie it conflicts with a prefix already added
                            new MinorLogMsg(this, "PREFIX ERROR: " + zone, e).log(getContext());
                        }
                    }
                }
            
            
            
             home = (Home)getContext().get(BillingOptionMappingHome.class);
             allRules = home.selectAll(getContext());
            
        }
        catch (HomeException e)
        {
            new MinorLogMsg(this, "INTERNAL ERROR", e).log(getContext());
        }
      
        if ( ! foundDefault )
        {
            new MajorLogMsg(this, "CONFIGURATION ERROR: No default MsisdnZonePrefix defined (ie. prefix=\"\").", null).log(getContext());
        }

        cache_.shortCodeTrueMap = allShortCodeTrueZones;
        cache_.shortCodeFalseMap = allShortCodeFalseZones;
        cache_.rules = allRules;       
        cache_.allZones_ = allZones;
        needUpdatingCache = false;

        return cache_; 
        
    	} finally
    	{
    		lock_.writeLock().unlock(); 
    	}
    	
    	
    }
   
 

    
    ////////////////////////////////////////////////////////// impl NotifyingHome2Listener

    /**
     * 
     */
    public synchronized void homeChange(HomeChangeEvent evt)
    {
        HomeOperationEnum op = evt.getOperation();
       
        if ( op == HomeOperationEnum.CREATE   ||
             op == HomeOperationEnum.STORE    ||
             op == HomeOperationEnum.REMOVE   )
        {
            invalidate();
        }
    }

    /**
     * @see com.redknee.app.crm.collection.PrefixService#getZoneDescription(com.redknee.framework.xhome.context.Context, java.lang.String)
     */
    public String getZoneDescription(Context ctx, String msisdn)
    {
       	MsisdnZonePrefix zone=(MsisdnZonePrefix) getCache().allZones_.lookup(msisdn);
  	
    	if(zone!=null)
    	{
    		return zone.getPrefixDescription();
    	}
    	
    	return "";
    }


    public String getShortCodeZoneDescription(Context ctx, String msisdn, boolean isShortCode)
    {
    	
    	PrefixCache cache = getCache();
        PrefixMap checkMap = null;
        if (isShortCode)
        {
            checkMap = cache.shortCodeTrueMap;
        }
        else
        {
            checkMap = cache.shortCodeFalseMap;
        }
        
        if (checkMap != null)
        {
            MsisdnZonePrefix zone = (MsisdnZonePrefix) checkMap.lookup(msisdn);
            if (zone != null)
            {
                return zone.getPrefixDescription();
            }
        }
        return "";
    }

    
    /**
     * @see com.redknee.app.crm.collection.PrefixService#getZoneId(com.redknee.framework.xhome.context.Context, java.lang.String)
     */
    public long getZoneId(Context ctx, String msisdn)
    {
       

        MsisdnZonePrefix zone=(MsisdnZonePrefix) getCache().allZones_.lookup(msisdn);
    	
    	if(zone!=null)
    	{
    		return zone.getIdentifier();
    	}
    	
    	return -1;
    }

    public long getShortCodeZoneId(Context ctx, String msisdn, boolean isShortCode)
    {
    	PrefixCache cache = getCache(); 
    	
    	PrefixMap checkMap = null;
    	
    	if(isShortCode)
        {
            checkMap = cache.shortCodeTrueMap;
        }
        else
        {
            checkMap = cache.shortCodeFalseMap;
        }
    	
        MsisdnZonePrefix zone=(MsisdnZonePrefix) checkMap.lookup(msisdn);
    	
    	if(zone!=null)
    	{
    		return zone.getIdentifier();
    	}
    	
    	return -1;
    }

    
    
	/**
	 * @return Returns the map.
	 */
	public PrefixCache getCache()
	{
		return validate();
	}


	
    private static final Comparator priorityComparator_ =
        new Comparator()
        {
            public int compare(final Object object1, final Object object2)
            {
                final BillingOptionMapping rule1 = (BillingOptionMapping)object1;
                final BillingOptionMapping rule2 = (BillingOptionMapping)object2;
                
                return rule2.getPriority().getIndex() - rule1.getPriority().getIndex();
            }
        };

    private final static
    class SPIDPredicate
        implements Predicate
    {
        public SPIDPredicate(final int spid)
        {
            spid_ = spid;
        }
        
        public boolean f(Context ctx,final Object object)
        {
            final BillingOptionMapping mapping = (BillingOptionMapping)object;
            return mapping.getSpid() == spid_;
        }

        private final int spid_;
        
    } // inner-class

    private final static
    class BillingOptionPredicate
        implements Predicate
    {
        public BillingOptionPredicate(final String option)
        {
            option_ = option;
        }

        public boolean f(Context ctx,final Object object)
        {
            final BillingOptionMapping rule = (BillingOptionMapping)object;

            final String ruleOption = rule.getBillingOption();
            
            return
                (option_.length() == 0 && (ruleOption == null || ruleOption.trim().length() == 0))
                || (option_.length() > 0 && ruleOption != null && option_.equals(ruleOption.trim()));
        }

        private final String option_;
        
    } // inner-class
    
    private class PrefixCache
    {
        PrefixMap allZones_ = null;
        PrefixMap shortCodeTrueMap = null;
        PrefixMap shortCodeFalseMap = null;
        Collection rules = null;

     }
}
