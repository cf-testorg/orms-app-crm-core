package com.redknee.app.crm.configshare;

import com.redknee.framework.xhome.home.HomeOperationEnum;


public class ConfigShareAPIUpdateEntityInfo
{

    public ConfigShareAPIUpdateEntityInfo(final Class<? extends Object> clazz, final HomeOperationEnum action)
    {
        setUpdateEntityClass(clazz);
        setAction(action);
    }


    public ConfigShareAPIUpdateEntityInfo(final Class<? extends Object> clazz, final HomeOperationEnum action,
            final String identifier)
    {
        this(clazz, action);
        setIdentifier(identifier);
    }


    public Class<? extends Object> getUpdateEntityClass()
    {
        return updateEntityClass_;
    }


    public Object getIdentifier()
    {
        return identifier_;
    }


    public void setUpdateEntityClass(Class<? extends Object> updateEntityClass)
    {
        this.updateEntityClass_ = updateEntityClass;
    }


    public HomeOperationEnum getAction()
    {
        return action_;
    }


    public void setAction(HomeOperationEnum action)
    {
        this.action_ = action;
    }


    public void setIdentifier(String identifier)
    {
        this.identifier_ = identifier;
    }

    private Class<? extends Object> updateEntityClass_;
    private String identifier_;
    private HomeOperationEnum action_;
}
