package com.redknee.app.crm.home;

import com.redknee.app.crm.support.IdentifierSequenceSupportHelper;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.xdb.sequence.Sequence;
import com.redknee.framework.xlog.log.DebugLogMsg;
import com.redknee.framework.xlog.log.LogSupport;

public class CrmIdentifierSequence
implements Sequence
{
    public long     increment_;
    public String   name;

    public CrmIdentifierSequence(Context ctx, String name, long increment)
    {
        setName(name);
       
        
        try
        {
            synchronized (name)
            {
                IdentifierSequenceSupportHelper.get(ctx).ensureSequenceExists(ctx, name, 0, Long.MAX_VALUE,0, increment); 
                increment = IdentifierSequenceSupportHelper.get(ctx).getIdentifierSequence(ctx, name).getIncrement();        
                setIncrement(increment);
            }
        }    catch (HomeException e)
        {
            if(LogSupport.isDebugEnabled(ctx))
            {
                new DebugLogMsg(this,e.getMessage(),e).log(ctx);
            }
        }
          
    }

    
    @Override
    public void dropSequence(Context ctx)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public long nextValue(Context ctx)
    {
        
        try
        {
            synchronized (name)
            {
                return IdentifierSequenceSupportHelper.get(ctx).getNextIdentifier(ctx, name,  null);
            }
        }    catch (HomeException e)
        {
            if(LogSupport.isDebugEnabled(ctx))
            {
                new DebugLogMsg(this,e.getMessage(),e).log(ctx);
            }
        }

        return -1L;
    }

    @Override
    public void resetSequence(Context ctx)
    {
        // TODO Auto-generated method stub
        
    }



    public long getIncrement()
    {
        return increment_;
    }

    public void setIncrement(long increment)
    {
        this.increment_ = increment;
    }


    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    
    
}
