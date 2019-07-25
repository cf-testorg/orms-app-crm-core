/*
    NotifyingHomeCmd
 
    @author: Joel Hughes
    Date   : Jan 20, 2003
 
    Copyright (c) Redknee, 2002
        - all rights reserved
*/

package com.redknee.app.crm.xhome.home;

import com.redknee.framework.xhome.home.HomeOperationEnum;

/**
 * Home command object to add/remove home change listeners
 */
public class TotalCachingHomeCmd
{
    protected Object            bean_      = null;
    protected HomeOperationEnum operation_ = null;


    public TotalCachingHomeCmd(HomeOperationEnum operation, Object bean)
    {
        operation_ = operation;
        bean_ = bean;
    }


    public Object getBean()
    {
        return bean_;
    }


    public HomeOperationEnum getOperation()
    {
        return operation_;
    }

}

