package com.redknee.app.crm.support;

import java.util.Iterator;
import java.util.List;

import com.redknee.app.crm.bean.MsisdnPrefix;
import com.redknee.app.crm.bean.MsisdnZonePrefix;
import com.redknee.app.crm.bean.MsisdnZonePrefixHome;
import com.redknee.app.crm.collection.PrefixService;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.support.StringUtil;
import com.redknee.framework.xlog.log.DebugLogMsg;
import com.redknee.framework.xlog.log.LogSupport;


public class DefaultDestinationZoneSupport implements DestinationZoneSupport
{
    
    protected static DestinationZoneSupport instance_ = null;
    public static DestinationZoneSupport instance()
    {
        if (instance_ == null)
        {
            instance_ = new DefaultDestinationZoneSupport();
        }
        return instance_;
    }

    protected DefaultDestinationZoneSupport()
    {
    }
    /** Finds the zone id of a msisdn */
    public long getDestinationZoneId(Context ctx, String destMSISDN)
    {
        PrefixService service=(PrefixService) ctx.get(PrefixService.class);
        if(service!=null)
        {
            return service.getZoneId(ctx,destMSISDN);
        }
        
        if(LogSupport.isDebugEnabled(ctx))
        {
            new DebugLogMsg(DestinationZoneSupport.class.getName(),
                "System error: this call should be executed on the BAS side. Using homes to find Zone Description",null).log(ctx);
        }
        
        Home home=(Home) ctx.get(MsisdnZonePrefixHome.class);

        long savedId=-1;
        long savedLength=-1;
        
        try
        {
            for(Iterator i=home.selectAll(ctx).iterator();i.hasNext();)
            {
                MsisdnZonePrefix zone=(MsisdnZonePrefix) i.next();
                List prefixes=zone.getPrefixes(ctx);

                for(Iterator j=prefixes.iterator();j.hasNext();)
                {
                    MsisdnPrefix prefix=(MsisdnPrefix) j.next();

                    if(destMSISDN.startsWith(prefix.getPrefix()))
                    {
                        String strPrefix=prefix.getPrefix();
                        if(strPrefix==null)
                        {
                            strPrefix="";
                        }
                        
                        // save it if it is more specific
                        if(strPrefix.length()>savedLength)
                        {
                            savedId=zone.getIdentifier();
                            savedLength=prefix.getPrefix().length();
                        }
                    }
                }
            }
        }
        catch (HomeException e)
        {
            if(LogSupport.isDebugEnabled(ctx))
            {
                new DebugLogMsg(DestinationZoneSupport.class.getName(),e.getMessage(),e).log(ctx);
            }
        }

        return savedId;
    }

    /** Finds the zone id of a msisdn */
    public long getShortCodeDestinationZoneId(Context ctx, String destMSISDN, boolean isShortCode)
    {
        PrefixService service=(PrefixService) ctx.get(PrefixService.class);
        if(service!=null)
        {
            return service.getShortCodeZoneId(ctx, destMSISDN, isShortCode);
        }
        
        if(LogSupport.isDebugEnabled(ctx))
        {
            new DebugLogMsg(DestinationZoneSupport.class.getName(),
                "System error: this call should be executed on the BAS side. Using homes to find Zone Description",null).log(ctx);
        }
        
        Home home=(Home) ctx.get(MsisdnZonePrefixHome.class);

        long savedId=-1;
        long savedLength=-1;
        
        try
        {
            for(Iterator i=home.selectAll(ctx).iterator();i.hasNext();)
            {
                MsisdnZonePrefix zone=(MsisdnZonePrefix) i.next();
                List prefixes=zone.getPrefixes(ctx);

                for(Iterator j=prefixes.iterator();j.hasNext();)
                {
                    MsisdnPrefix prefix=(MsisdnPrefix) j.next();

                    if(destMSISDN.startsWith(prefix.getPrefix()) && prefix.getIsShortCode()== isShortCode)
                    {
                        String strPrefix=prefix.getPrefix();
                        if(strPrefix==null)
                        {
                            strPrefix="";
                        }
                        
                        // save it if it is more specific
                        if(strPrefix.length()>savedLength)
                        {
                            savedId=zone.getIdentifier();
                            savedLength=prefix.getPrefix().length();
                        }
                    }
                }
            }
        }
        catch (HomeException e)
        {
            if(LogSupport.isDebugEnabled(ctx))
            {
                new DebugLogMsg(DestinationZoneSupport.class.getName(),e.getMessage(),e).log(ctx);
            }
        }

        return savedId;
    }

    /** Finds the zone description of a msisdn */
    public String getDestinationZoneDescription(Context ctx, String destMSISDN)
    {
        PrefixService service=(PrefixService) ctx.get(PrefixService.class);
        if(service!=null)
        {
            return service.getZoneDescription(ctx,destMSISDN);
        }
        
        if(LogSupport.isDebugEnabled(ctx))
        {
            new DebugLogMsg(DestinationZoneSupport.class.getName(),
                "System error: this call should be executed on the BAS side. Using homes to find Zone Description",null).log(ctx);
        }
        
        Home home=(Home) ctx.get(MsisdnZonePrefixHome.class);

        String savedDescription=null;
        long savedLength=-1;
        
        try
        {
            for(Iterator i=home.selectAll(ctx).iterator();i.hasNext();)
            {
                MsisdnZonePrefix zone=(MsisdnZonePrefix) i.next();
                List prefixes=zone.getPrefixes(ctx);

                for(Iterator j=prefixes.iterator();j.hasNext();)
                {
                    MsisdnPrefix prefix=(MsisdnPrefix) j.next();

                    if(destMSISDN.startsWith(prefix.getPrefix()))
                    {
                        String desc=prefix.getDescription();
                        
                        if(StringUtil.invalidString(prefix.getDescription()))
                        {
                            desc=zone.getDescription();
                        }
                        
                        String strPrefix=prefix.getPrefix();
                        if(strPrefix==null)
                        {
                            strPrefix="";
                        }

                        // save it if it is more specific
                        if(strPrefix.length()>savedLength)
                        {
                            savedDescription=desc;
                            savedLength=prefix.getPrefix().length();
                        }
                    }
                }
            }
        }
        catch (HomeException e)
        {
            if(LogSupport.isDebugEnabled(ctx))
            {
                new DebugLogMsg(DestinationZoneSupport.class.getName(),e.getMessage(),e).log(ctx);
            }
        }

        return savedDescription;
    }

    /** Finds the zone description of a msisdn */
    public String getShortCodeDestinationZoneDescription(Context ctx, String destMSISDN, boolean isShortCode)
    {
        PrefixService service=(PrefixService) ctx.get(PrefixService.class);
        if(service!=null)
        {
            return service.getShortCodeZoneDescription(ctx, destMSISDN, isShortCode);
        }
        
        if(LogSupport.isDebugEnabled(ctx))
        {
            new DebugLogMsg(DestinationZoneSupport.class.getName(),
                "System Error: this call should be executed on the BAS side. Using homes to find Zone Description",null).log(ctx);
        }
        
        Home home=(Home) ctx.get(MsisdnZonePrefixHome.class);

        String savedDescription=null;
        long savedLength=-1;
        
        try
        {
            for(Iterator i=home.selectAll(ctx).iterator();i.hasNext();)
            {
                MsisdnZonePrefix zone=(MsisdnZonePrefix) i.next();
                List prefixes=zone.getPrefixes(ctx);

                for(Iterator j=prefixes.iterator();j.hasNext();)
                {
                    MsisdnPrefix prefix=(MsisdnPrefix) j.next();

                    if(destMSISDN.startsWith(prefix.getPrefix()) && prefix.getIsShortCode()== isShortCode)
                    {
                        String desc=prefix.getDescription();
                        
                        if(StringUtil.invalidString(prefix.getDescription()))
                        {
                            desc=zone.getDescription();
                        }
                        
                        String strPrefix=prefix.getPrefix();
                        if(strPrefix==null)
                        {
                            strPrefix="";
                        }

                        // save it if it is more specific
                        if(strPrefix.length()>savedLength)
                        {
                            savedDescription=desc;
                            savedLength=prefix.getPrefix().length();
                        }
                    }
                }
            }
        }
        catch (HomeException e)
        {
            if(LogSupport.isDebugEnabled(ctx))
            {
                new DebugLogMsg(DestinationZoneSupport.class.getName(),e.getMessage(),e).log(ctx);
            }
        }

        return savedDescription;
    }

}
