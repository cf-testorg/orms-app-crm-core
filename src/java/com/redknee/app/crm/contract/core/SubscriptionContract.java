package com.redknee.app.crm.contract.core;

import java.util.Calendar;
import java.util.Date;

import com.redknee.app.crm.contract.SubscriptionContractTerm;
import com.redknee.app.crm.contract.SubscriptionContractTermHome;
import com.redknee.app.crm.contract.SubscriptionContractTermXInfo;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xlog.log.MinorLogMsg;


public class SubscriptionContract extends com.redknee.app.crm.contract.SubscriptionContract
{

    public SubscriptionContract()
    {
        super();
    }
    public SubscriptionContract(String sub, SubscriptionContractTerm term, Date startDate)
    {
        super();
        this.setSubscriptionId(sub);
        this.setContractStartDate(startDate);
        Date endDate = getMonthsAfter(startDate, term.getContractLength());
        Date bonusEndDate = getMonthsAfter(endDate,term.getBonusPeriodLength());
        Date prePaymentEndDate = getMonthsAfter(startDate, term.getPrePaymentLength());
        this.setContractEndDate(endDate);
        this.setBonusEndDate(bonusEndDate);
        this.setPrePaymentEndDate(prePaymentEndDate);
        this.setContractId(term.getId());
        this.setBalancePaymentAmount(term.getPrepaymentAmount());
    }
    
    public SubscriptionContractTerm getSubscriptionContractTerm(Context ctx)
    {
        if (term_ != null)
        {
            return term_;
        }
        else
        {
            Home home = (Home) ctx.get(SubscriptionContractTermHome.class);
            try
            {
                term_ = (SubscriptionContractTerm) home.find(ctx, new EQ(SubscriptionContractTermXInfo.ID,this.getContractId()));
            }
            catch (HomeException homeEx)
            {
                new MinorLogMsg(this, "Unable to load subcription contract term " + this.getContractId(), homeEx).log(ctx);
            }
        }
        return term_;
    }

    
    private Date getMonthsAfter(final Date day, final int monthsAfter)
    {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(day);
        calendar.add(Calendar.MONDAY, monthsAfter);        
        //calendar.set(Calendar.MILLISECOND, 0);
        //calendar.set(Calendar.SECOND, 0);
        //calendar.set(Calendar.MINUTE, 0);
        //calendar.set(Calendar.HOUR_OF_DAY, 0);

        return calendar.getTime();
    }
    private transient SubscriptionContractTerm term_ = null;
}
