package com.redknee.app.crm.configshare;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;

import com.redknee.framework.xhome.beans.XBeans;
import com.redknee.framework.xhome.beans.xi.PropertyInfo;
import com.redknee.framework.xhome.beans.xi.XInfo;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.context.ContextSupport;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.msp.SpidAware;
import com.redknee.framework.xhome.visitor.Visitor;
import com.redknee.framework.xlog.log.DebugLogMsg;
import com.redknee.framework.xlog.log.LogSupport;
import com.redknee.framework.xlog.log.MinorLogMsg;

import com.redknee.app.crm.support.HomeSupportHelper;


/**
 * When used as a HomeChangeListener, this must be used along with ConfigSharingHome
 * instead of FW's default NotifyingHome.
 * 
 * 
 * @author aaron.gourley@redknee.com
 * @since
 */
public class SharedConfigSyncProcessor
{
    protected SharedConfigSyncProcessor()
    {
    }


    /**
     * {@inheritDoc}
     * @throws SharedConfigSyncException 
     */
    public final void process(Context ctx, SharedConfigSyncForm form) throws SharedConfigSyncException
    {
        // Remove the principal so that SpidAwareHome doesn't restrict the sync operation to same-spid stuff
        ctx = ctx.createSubContext();
        ctx.put(Principal.class, null);

        if (LogSupport.isDebugEnabled(ctx))
        {
            new DebugLogMsg(this, "Processing Force sync shared config change ...", null).log(ctx);
        }

        final Collection<SharedConfigSyncCriteria> syncCriteria;
        if (!form.getFilteredSync())
        {
            syncCriteria = new ArrayList<SharedConfigSyncCriteria>();
            try
            {
                Collection<SharedBean> beans = HomeSupportHelper.get(ctx).getBeans(ctx, SharedBean.class);
                for (SharedBean bean : beans)
                {
                    SharedConfigSyncCriteria criteria = new SharedConfigSyncCriteria();
                    criteria.setSharedBeanClass(bean.getBeanClassName());
                    criteria.setApplyIdFilter(SyncModeEnum.SYNC_ALL);
                    syncCriteria.add(criteria);
                }
            }
            catch (HomeException e)
            {
                new MinorLogMsg(this, "Error retrieving registered shared beans.", e).log(ctx);
            }
        }
        else
        {
            syncCriteria = form.getSyncCriteria();
        }

        SharedConfigSyncException lastError = null;
        if (syncCriteria != null)
        {
            for (SharedConfigSyncCriteria criteria : syncCriteria)
            {
                try
                {
                    applyFilteredSync(ctx, criteria);
                }
                catch (Throwable t)
                {
                    if (t instanceof SharedConfigSyncException)
                    {
                        lastError = (SharedConfigSyncException) t;
                    }
                    else
                    {
                        lastError = new SharedConfigSyncException("Unexpected exception executing sync.", t);
                    }
                    new MinorLogMsg(this, "Error synchronizing bean " + criteria.getSharedBeanClass(), null).log(ctx);
                    new DebugLogMsg(this, "Error synchronizing bean " + criteria.getSharedBeanClass() + " given criteria [" + criteria + "]", lastError).log(ctx);
                }
            }
        }

        if (lastError != null)
        {
            throw lastError;
        }
    }


    protected void applyFilteredSync(Context ctx, SharedConfigSyncCriteria criteria) throws Exception
    {
        String beanClassName = criteria.getSharedBeanClass();
        Class beanClass = null;
        try
        {
            beanClass = Class.forName(beanClassName);
        }
        catch (ClassNotFoundException ex)
        {
            throw new SharedConfigSyncException("Bean class " + beanClassName + " could not be loaded.", ex);
        }

        SharedBean sharedBean = HomeSupportHelper.get(ctx).findBean(ctx, SharedBean.class, beanClassName);
        if (sharedBean != null)
        {
            Visitor syncVisitor = new ConfigSharingVisitor();
            if (sharedBean.isShareHome())
            {
            	SyncModeEnum mode = criteria.getApplyIdFilter();
            	
            	if(mode == SyncModeEnum.SYNC_SINGLE_RECORD)
            	{
            		Object beanID = criteria.getBeanIdentifier();
                    ctx = HomeSupportHelper.get(ctx).getWhereContext(ctx, beanClass, beanID);
            	}
            	else if(mode == SyncModeEnum.SYNC_ALL_FOR_SPID)
            	{
            		if(SpidAware.class.isAssignableFrom(beanClass))
            		{
	            		Object beanID = criteria.getBeanIdentifier();
	            		Object whereClause = prepareWhereClauseForSpid(ctx,beanClass,beanID);
	            		if(whereClause !=null)
	            			ctx = HomeSupportHelper.get(ctx).getWhereContext(ctx, beanClass, whereClause);
	            		else
	            			throw new SharedConfigSyncException("Sync All For Spid cannot be performed for bean "+beanClassName+ " as bean is not spid Aware");
            		}
            		else
            		{
            			throw new SharedConfigSyncException("Sync All For Spid cannot be performed for bean "+beanClassName+ " as bean is not spid Aware");
            		}
            	}
            	
                Home home = HomeSupportHelper.get(ctx).getHome(ctx, beanClass);
                if (home != null)
                {
                    home.forEach(ctx, syncVisitor);
                }
                else
                {
                    throw new SharedConfigSyncException("Home for bean " + beanClassName + " is not installed in the context.");
                }
            }
            else
            {
                Object bean = ctx.get(beanClass);
                if (bean != null)
                {
                    syncVisitor.visit(ctx, bean);
                }
                else
                {
                    throw new SharedConfigSyncException("Bean " + beanClassName + " is not installed in the context.");
                }
            }
        }
        else
        {
            throw new SharedConfigSyncException("Bean class " + beanClassName + " is not registered for sharing.");
        }
    }
    
    private Object prepareWhereClauseForSpid(Context ctx,Class beanClass,Object spid)
    {
    	XInfo xinfoObj = XBeans.getInstanceOf(ctx, beanClass, XInfo.class);
    	PropertyInfo spidPropertyInfo = xinfoObj.getPropertyInfo(ctx, "spid");
    	Object where = null;
    	if(spidPropertyInfo != null)
    	{
    		where = new EQ(spidPropertyInfo, spid);
    	}
    	return where;
    }
}
