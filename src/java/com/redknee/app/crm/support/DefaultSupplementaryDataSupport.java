package com.redknee.app.crm.support;

import java.util.Collection;

import com.redknee.app.crm.bean.SupplementaryData;
import com.redknee.app.crm.bean.SupplementaryDataEntityEnum;
import com.redknee.app.crm.bean.SupplementaryDataXInfo;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.And;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.home.BeanNotFoundHomeException;
import com.redknee.framework.xhome.home.HomeException;

public class DefaultSupplementaryDataSupport implements SupplementaryDataSupport
{
    protected static SupplementaryDataSupport instance_ = null;
    public static SupplementaryDataSupport instance()
    {
        if (instance_ == null)
        {
            instance_ = new DefaultSupplementaryDataSupport();
        }
        return instance_;
    }

    protected DefaultSupplementaryDataSupport()
    {
    }

    @Override
    public Collection<SupplementaryData> getSupplementaryData(Context context, SupplementaryDataEntityEnum entity,
            String identifier) throws HomeException
    {
        And filter = new And();
        filter.add(new EQ(SupplementaryDataXInfo.ENTITY, Integer.valueOf(entity.getIndex())));
        filter.add(new EQ(SupplementaryDataXInfo.IDENTIFIER, identifier));
        return HomeSupportHelper.get(context).getBeans(context, SupplementaryData.class, filter);
    }

    @Override
    public SupplementaryData getSupplementaryData(Context context, SupplementaryDataEntityEnum entity,
            String identifier, String key) throws HomeException
    {
        And filter = new And();
        filter.add(new EQ(SupplementaryDataXInfo.ENTITY, Integer.valueOf(entity.getIndex())));
        filter.add(new EQ(SupplementaryDataXInfo.IDENTIFIER, identifier));
        filter.add(new EQ(SupplementaryDataXInfo.KEY, key));
        return HomeSupportHelper.get(context).findBean(context, SupplementaryData.class, filter);
    }

    @Override
    public void addOrUpdateSupplementaryData(Context context, SupplementaryDataEntityEnum entity, String identifier,
            String key, String value) throws HomeException
    {
        SupplementaryData bean = new SupplementaryData();
        bean.setEntity(entity.getIndex());
        bean.setIdentifier(identifier);
        bean.setKey(key);
        bean.setValue(value);
        addOrUpdateSupplementaryData(context, bean);
    }

    @Override
    public void removeSupplementaryData(Context context, SupplementaryDataEntityEnum entity, String identifier,
            String key) throws HomeException
    {
        SupplementaryData bean = new SupplementaryData();
        bean.setEntity(entity.getIndex());
        bean.setIdentifier(identifier);
        bean.setKey(key);
        removeSupplementaryData(context, bean);
    }

    @Override
    public void addOrUpdateSupplementaryData(Context context, SupplementaryData supplementaryData) throws HomeException
    {
        HomeSupportHelper.get(context).createBean(context, supplementaryData);
    }

    @Override
    public void removeSupplementaryData(Context context, SupplementaryData supplementaryData) throws HomeException
    {
        HomeSupportHelper.get(context).removeBean(context, supplementaryData);
    }

    @Override
    public void removeAllSupplementaryData(Context context, SupplementaryDataEntityEnum entity, String identifier)
            throws HomeException
    {
        And filter = new And();
        filter.add(new EQ(SupplementaryDataXInfo.ENTITY, Integer.valueOf(entity.getIndex())));
        filter.add(new EQ(SupplementaryDataXInfo.IDENTIFIER, identifier));
        HomeSupportHelper.get(context).getHome(context, SupplementaryData.class).where(context, filter).removeAll(context);
    }

}
