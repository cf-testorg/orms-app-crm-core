package com.redknee.app.crm.configshare;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.redknee.framework.xhome.beans.XBeans;
import com.redknee.framework.xhome.beans.xi.PropertyInfo;
import com.redknee.framework.xhome.beans.xi.XInfo;
import com.redknee.framework.xhome.beans.xi.XInfoSupport;
import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.context.ContextLocator;
import com.redknee.framework.xhome.home.HomeChangeEvent;
import com.redknee.framework.xhome.home.HomeChangeListener;
import com.redknee.framework.xhome.home.NotifyingHomeItem;
import com.redknee.framework.xhome.visitor.AbortVisitException;
import com.redknee.framework.xlog.log.DebugLogMsg;
import com.redknee.framework.xlog.log.LogSupport;
import com.redknee.framework.xlog.log.MinorLogMsg;

import com.redknee.app.crm.bean.CurrencyPrecision;

/**
 * When used as a HomeChangeListener, this must be used along with ConfigSharingHome instead of FW's default NotifyingHome.
 * 
 *
 * @author aaron.gourley@redknee.com
 * @since
 */
public class SharedConfigChangeListener implements PropertyChangeListener, HomeChangeListener
{    
    private static SharedConfigChangeListener instance_ = null;
    public static SharedConfigChangeListener instance()
    {
        if (instance_ == null)
        {
            instance_ = new SharedConfigChangeListener();
        }
        return instance_;
    }
    
    protected SharedConfigChangeListener()
    {
    }

    @Override
    public void propertyChange(PropertyChangeEvent event)
    {
        Context ctx = ContextLocator.locate();
        
        if (event.getClass().isAssignableFrom(com.redknee.framework.xhome.beans.NestedPropertyChangeEvent.class))
        {
            new DebugLogMsg(this, "Skipping event as it is nested event", null).log(ctx);
			return;
        }
        if (LogSupport.isDebugEnabled(ctx))
        {
            new DebugLogMsg(this, "Processing shared config change event (bean update)...", null).log(ctx);
        }

        Object obj = event.getSource();
        if (obj != null)
        {
            XInfo xinfo = (XInfo) XBeans.getInstanceOf(ctx, obj.getClass(), XInfo.class);
            if (xinfo != null)
            {
                PropertyInfo property = XInfoSupport.getPropertyInfo(ctx, xinfo, event.getPropertyName());
                if (property != null)
                {
                    final Object oldValue = event.getOldValue();

                    ConfigSharingVisitor propertyChangeVisitor = new ConfigSharingVisitor(property, oldValue);
                    try
                    {
                        propertyChangeVisitor.visit(ctx, obj);
                    }
                    catch (AbortVisitException e)
                    {
                        // NOP
                    }
                    catch (AgentException e)
                    {
                        new MinorLogMsg(this, "Error sharing configuration change for property change event on " + obj.getClass().getName() + "." + event.getPropertyName(), e).log(ctx);
                    }
                }
            }
            else
            {
                new MinorLogMsg(this, "No XInfo found for class " + obj.getClass().getName() + ".  No change request sent.", null).log(ctx);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public final void homeChange(HomeChangeEvent evt)
    {
        Context ctx = evt.getContext();

        if (LogSupport.isDebugEnabled(ctx))
        {
            new DebugLogMsg(this, "Processing shared config change event (Home operation)...", null).log(ctx);
        }
        
        Object oldObj = null;
        Object newObj = null;
        
        final Object source = evt.getSource();
        if (source instanceof NotifyingHomeItem)
        {
            final NotifyingHomeItem notifyingHomeItem = (NotifyingHomeItem) source;
            oldObj = notifyingHomeItem.getOldObject();
            newObj = notifyingHomeItem.getNewObject();
        }
        else
        {
            newObj = source;
        }

        ConfigSharingVisitor homeChangeVisitor = new ConfigSharingVisitor(evt.getOperation(), oldObj);
        try
        {
            homeChangeVisitor.visit(ctx, newObj);
        }
        catch (AbortVisitException e)
        {
            // NOP
        }
        catch (AgentException e)
        {
            new MinorLogMsg(this, "Error sharing configuration change for home change event [" + evt + "]", e).log(ctx);
        }
    }
}
