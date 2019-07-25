package com.redknee.app.crm.bean;

import java.util.Date;

public interface SubscriberProvisionableEntity
{

    public String getSubscriberId(); 
    public long getEntityID(); 
    public long getSecondaryIdentifier();      
    
    public Date getCreated(); 
    public Date getLastModified();
    public void setLastModified(Date date);
    public Date getStartDate();
    public void setStartDate(Date date); 
    public Date getEndDate();
    public void setEndDate(Date date); 
    
    public short getServPeriod();
    public void setServPeriod(short period); 
    
    public short getReason(); 
    public void setReason(short reason);
    
    public short getState();
    public void setState(short state);
    
}

