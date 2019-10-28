package com.redknee.app.crm.notification;

import com.google.common.collect.ForwardingConcurrentMap;
import com.redknee.app.crm.notification.liaison.NotificationLiaison;
import com.redknee.app.crm.notification.liaison.NotificationLiaisonProxy;
import com.redknee.app.crm.notification.liaison.RealTimeNotificationLiaison;
import com.redknee.app.crm.notification.liaison.ScheduledTaskNotificationLiaison;
import com.redknee.app.crm.support.NotificationSupportHelper;
import com.redknee.framework.xhome.beans.SafetyUtil;
import com.redknee.framework.xhome.beans.XBeans;
import com.redknee.framework.xhome.beans.XCloneable;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.xenum.EnumCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * A special map that is actually backed by a notification type specific liaisons stored in the context.
 */
public class DynamicNotificationTypeScheduleMap extends ForwardingConcurrentMap<Object, NotificationTypeSchedule>
        implements ConcurrentMap<Object, NotificationTypeSchedule> {

    private final Context context;
    private final ConcurrentMap<Object, NotificationTypeSchedule> delegate;

    DynamicNotificationTypeScheduleMap(Context context) {
        this.context = context;
        delegate = new ConcurrentHashMap<>();
    }

    @Override
    protected ConcurrentMap<Object, NotificationTypeSchedule> delegate() {
        return delegate;
    }

    public Context getContext() {
        return context;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int size() {
        return keySet().size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NotificationTypeSchedule remove(Object key) {
        Context ctx = getContext();
        NotificationTypeSchedule result = null;

        EnumCollection supportedNotificationTypes =
                NotificationSupportHelper.get(ctx).getSupportedNotificationTypes(ctx);
        if (supportedNotificationTypes != null) {
            NotificationTypeEnum type =
                    (NotificationTypeEnum) supportedNotificationTypes.getByIndex(((Number) key).shortValue());
            if (type != null) {
                NotificationLiaison originalLiaison =
                        NotificationSupportHelper.get(ctx).getLiaisonForNotificationType(ctx, type);
                NotificationLiaison liaison = originalLiaison;
                if (liaison instanceof NotificationLiaisonProxy) {
                    liaison = ((NotificationLiaisonProxy) liaison).getDelegate();
                    if (liaison instanceof XCloneable) {
                        try {
                            liaison = (NotificationLiaison) ((XCloneable) liaison).clone();
                        } catch (CloneNotSupportedException e) {
                        }
                    }
                }

                NotificationLiaison scheduledLiaison = liaison;
                if (liaison instanceof NotificationLiaisonProxy) {
                    NotificationLiaisonProxy proxy = (NotificationLiaisonProxy) liaison;
                    scheduledLiaison = proxy.findDecorator(ScheduledTaskNotificationLiaison.class);
                    if (scheduledLiaison != null) {
                        while (proxy.getDelegate() instanceof NotificationLiaisonProxy) {
                            proxy = (NotificationLiaisonProxy) proxy.getDelegate();
                        }

                        RealTimeNotificationLiaison realTimeLiaison;
                        try {
                            realTimeLiaison = XBeans.instantiate(RealTimeNotificationLiaison.class, ctx);
                        } catch (Exception e) {
                            realTimeLiaison = new RealTimeNotificationLiaison();
                        }

                        proxy.setDelegate(realTimeLiaison);
                    }
                } else if (scheduledLiaison != originalLiaison) {
                    try {
                        liaison = (RealTimeNotificationLiaison) XBeans.instantiate(RealTimeNotificationLiaison.class, ctx);
                    } catch (Exception e) {
                        liaison = new RealTimeNotificationLiaison();
                    }
                }

                if (scheduledLiaison instanceof ScheduledTaskNotificationLiaison) {
                    try {
                        result = XBeans.instantiate(NotificationTypeSchedule.class, ctx);
                    } catch (Exception e) {
                        result = new NotificationTypeSchedule();
                    }
                    result.setNotificationTypeIndex(type.getIndex());
                    result.setSchedule(((ScheduledTaskNotificationLiaison) scheduledLiaison).getLiaisonSchedule());

                    if (scheduledLiaison == originalLiaison) {
                        ((ScheduledTaskNotificationLiaison) scheduledLiaison).setLiaisonSchedule("");
                    }
                }

                if (originalLiaison instanceof NotificationLiaisonProxy) {
                    ((NotificationLiaisonProxy) originalLiaison).setDelegate(liaison);
                }
            }
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        Context ctx = getContext();
        EnumCollection supportedNotificationTypes =
                NotificationSupportHelper.get(ctx).getSupportedNotificationTypes(ctx);
        if (supportedNotificationTypes != null) {
            Iterator<NotificationTypeEnum> iter = supportedNotificationTypes.iterator();
            while (iter.hasNext()) {
                NotificationTypeSchedule schedule = new NotificationTypeSchedule();
                schedule.setNotificationTypeIndex(iter.next().getIndex());
                remove(schedule.ID());
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean containsValue(Object obj) {
        return values().contains(obj);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NotificationTypeSchedule get(Object key) {
        Context ctx = getContext();
        EnumCollection supportedNotificationTypes =
                NotificationSupportHelper.get(ctx).getSupportedNotificationTypes(ctx);
        if (supportedNotificationTypes != null) {
            Iterator<NotificationTypeEnum> iter = supportedNotificationTypes.iterator();
            while (iter.hasNext()) {
                NotificationTypeEnum type = iter.next();

                if (((Number) key).shortValue() == type.getIndex()) {
                    NotificationLiaison liaison =
                            NotificationSupportHelper.get(ctx).getLiaisonForNotificationType(ctx, type);
                    if (liaison instanceof NotificationLiaisonProxy) {
                        liaison = ((NotificationLiaisonProxy) liaison).findDecorator(
                                ScheduledTaskNotificationLiaison.class);
                    }
                    if (liaison instanceof ScheduledTaskNotificationLiaison) {
                        return createScheduleEntry(type,
                                ((ScheduledTaskNotificationLiaison) liaison).getLiaisonSchedule());
                    }
                }
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NotificationTypeSchedule put(Object key, NotificationTypeSchedule schedule) {
        NotificationTypeSchedule result = null;

        Context ctx = getContext();
        EnumCollection supportedNotificationTypes =
                NotificationSupportHelper.get(ctx).getSupportedNotificationTypes(ctx);
        if (supportedNotificationTypes != null) {
            NotificationTypeEnum type =
                    (NotificationTypeEnum) supportedNotificationTypes.getByIndex(((Number) key).shortValue());
            if (type != null) {
                boolean isNew = false;

                NotificationLiaison originalLiaison =
                        NotificationSupportHelper.get(ctx).getLiaisonForNotificationType(ctx, type);
                NotificationLiaison liaison = originalLiaison;
                if (liaison instanceof NotificationLiaisonProxy) {
                    liaison = ((NotificationLiaisonProxy) liaison).getDelegate();
                    if (liaison instanceof XCloneable) {
                        try {
                            liaison = (NotificationLiaison) ((XCloneable) liaison).clone();
                        } catch (CloneNotSupportedException e) {
                        }
                    }
                }

                NotificationLiaison scheduledLiaison = liaison;
                if (liaison instanceof NotificationLiaisonProxy) {
                    NotificationLiaisonProxy proxy = (NotificationLiaisonProxy) liaison;
                    scheduledLiaison = proxy.findDecorator(ScheduledTaskNotificationLiaison.class);
                    if (scheduledLiaison == null) {
                        isNew = true;
                        try {
                            scheduledLiaison = (ScheduledTaskNotificationLiaison) XBeans.instantiate(
                                    ScheduledTaskNotificationLiaison.class, ctx);
                        } catch (Exception e) {
                            scheduledLiaison = new ScheduledTaskNotificationLiaison();
                        }
                        while (proxy.getDelegate() instanceof NotificationLiaisonProxy) {
                            proxy = (NotificationLiaisonProxy) proxy.getDelegate();
                        }
                        proxy.setDelegate(scheduledLiaison);
                    }
                } else if (!(scheduledLiaison instanceof ScheduledTaskNotificationLiaison)
                        && scheduledLiaison != originalLiaison) {
                    isNew = true;
                    try {
                        scheduledLiaison = (ScheduledTaskNotificationLiaison) XBeans.instantiate(
                                ScheduledTaskNotificationLiaison.class, ctx);
                    } catch (Exception e) {
                        scheduledLiaison = new ScheduledTaskNotificationLiaison();
                    }
                    liaison = scheduledLiaison;
                }

                if (scheduledLiaison instanceof ScheduledTaskNotificationLiaison) {
                    ScheduledTaskNotificationLiaison scheduledTaskLiaison =
                            (ScheduledTaskNotificationLiaison) scheduledLiaison;

                    if (!isNew) {
                        try {
                            result = XBeans.instantiate(NotificationTypeSchedule.class, ctx);
                        } catch (Exception e) {
                            result = new NotificationTypeSchedule();
                        }
                        result.setNotificationTypeIndex(type.getIndex());
                        result.setSchedule(scheduledTaskLiaison.getLiaisonSchedule());
                    }

                    scheduledTaskLiaison.setLiaisonSchedule(schedule.getSchedule());
                }

                if (originalLiaison instanceof NotificationLiaisonProxy) {
                    ((NotificationLiaisonProxy) originalLiaison).setDelegate(liaison);
                }
            }
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void putAll(Map<? extends Object, ? extends NotificationTypeSchedule> source) {
        for (Map.Entry entry : source.entrySet()) {
            put(entry.getKey(), (NotificationTypeSchedule) entry.getValue());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Object> keySet() {
        Context ctx = getContext();
        Set values = new HashSet();

        EnumCollection supportedNotificationTypes =
                NotificationSupportHelper.get(ctx).getSupportedNotificationTypes(ctx);
        if (supportedNotificationTypes != null) {
            Iterator<NotificationTypeEnum> iter = supportedNotificationTypes.iterator();
            while (iter.hasNext()) {
                NotificationTypeEnum type = iter.next();
                NotificationTypeSchedule schedule = get(type.getIndex());
                if (schedule != null) {
                    values.add(schedule.ID());
                }
            }
        }
        return values;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<NotificationTypeSchedule> values() {
        Context ctx = getContext();
        Collection<NotificationTypeSchedule> values = new ArrayList<>();

        EnumCollection supportedNotificationTypes =
                NotificationSupportHelper.get(ctx).getSupportedNotificationTypes(ctx);
        if (supportedNotificationTypes != null) {
            Iterator<NotificationTypeEnum> iter = supportedNotificationTypes.iterator();
            while (iter.hasNext()) {
                NotificationTypeEnum type = iter.next();
                NotificationTypeSchedule schedule = get(type.getIndex());
                if (schedule != null) {
                    values.add(schedule);
                }
            }
        }
        return values;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Map.Entry<Object, NotificationTypeSchedule>> entrySet() {
        Set<Map.Entry<Object, NotificationTypeSchedule>> result = new HashSet<>();
        for (final NotificationTypeSchedule value : values()) {
            result.add(new NotificationTypeScheduleEntry(value));
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Map)) {
            return false;
        }

        Map otherMap = (Map) other;

        return entrySet().equals(otherMap.entrySet());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int result = 0;

        for (Map.Entry<Object, NotificationTypeSchedule> entry : entrySet()) {
            result += entry.hashCode();
        }

        return result;
    }

    protected NotificationTypeSchedule createScheduleEntry(NotificationTypeEnum type, String scheduleId) {
        Context ctx = getContext();
        NotificationTypeSchedule schedule = null;
        try {
            schedule = XBeans.instantiate(NotificationTypeSchedule.class, ctx);
        } catch (Exception e) {
            schedule = new NotificationTypeSchedule();
        }

        schedule.setNotificationTypeIndex(type.getIndex());
        schedule.setSchedule(scheduleId);
        return schedule;
    }

    private final class NotificationTypeScheduleEntry implements Map.Entry<Object, NotificationTypeSchedule> {

        private final NotificationTypeSchedule value;

        private NotificationTypeScheduleEntry(NotificationTypeSchedule value) {
            this.value = value;
        }

        @Override
        public Object getKey() {
            return value.ID();
        }

        @Override
        public NotificationTypeSchedule getValue() {
            return value;
        }

        @Override
        public NotificationTypeSchedule setValue(NotificationTypeSchedule value) {
            return put(value.ID(), value);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int hashCode() {
            return SafetyUtil.hashCode(getKey()) ^
                    SafetyUtil.hashCode(getValue());
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean equals(Object other) {
            if (!(other instanceof Map.Entry)) {
                return false;
            }

            Map.Entry e2 = (Map.Entry) other;

            return SafetyUtil.safeEquals(getKey(), e2.getKey())
                    && SafetyUtil.safeEquals(getValue(), e2.getValue());
        }
    }
}
