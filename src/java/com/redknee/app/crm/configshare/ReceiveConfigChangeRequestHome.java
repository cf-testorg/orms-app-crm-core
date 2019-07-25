package com.redknee.app.crm.configshare;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.redknee.framework.xhome.beans.AbstractBean;
import com.redknee.framework.xhome.beans.Identifiable;
import com.redknee.framework.xhome.beans.XBeans;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.HomeOperationEnum;
import com.redknee.framework.xhome.home.HomeProxy;
import com.redknee.framework.xhome.home.NullHome;
import com.redknee.framework.xhome.home.ReadOnlyHome;
import com.redknee.framework.xhome.xdb.XDBHome;
import com.redknee.framework.xlog.log.DebugLogMsg;
import com.redknee.framework.xlog.log.InfoLogMsg;
import com.redknee.framework.xlog.log.LogSupport;
import com.redknee.framework.xlog.log.MinorLogMsg;
import com.redknee.framework.xlog.log.SeverityEnum;

import com.redknee.app.crm.bean.AdjustmentType;
import com.redknee.app.crm.bean.core.BeanClassMapping;
import com.redknee.app.crm.extension.ExtensionHandlingHome;
import com.redknee.app.crm.home.core.CoreAdjustmentTypeHomePipelineFactory;
import com.redknee.app.crm.support.ConfigChangeRequestSupport;
import com.redknee.app.crm.support.ConfigChangeRequestSupportHelper;
import com.redknee.app.crm.support.HomeSupport;
import com.redknee.app.crm.support.HomeSupportHelper;
import com.redknee.app.crm.xhome.home.TotalCachingHome;


public class ReceiveConfigChangeRequestHome extends NullHome
{
    public ReceiveConfigChangeRequestHome(Context ctx)
    {
        super(ctx);
    }


    /**
     * write to data store
     */
    @Override
    public Object create(Context ctx, Object obj) throws HomeException
    {
        ConfigChangeRequest request = (ConfigChangeRequest) obj;
        
        if (LogSupport.isDebugEnabled(ctx))
        {
            new DebugLogMsg(this, "Processing " + request.getTypeOfUpdate()
                    + " update request from " + request.getModifiedAppName()
                    + " on source bean class " + request.getBeanClass(), null).log(ctx);
        }
        
        BeanClassMapping classMapping = getLocalBeanMapping(ctx, request);
        ConfigChangeRequestTranslator configAdapter = ConfigChangeRequestTranslatorFactory.instance(ctx, classMapping);
        
        if (configAdapter != null)
        {
            final ConfigChangeRequestSupport changeSupport = ConfigChangeRequestSupportHelper.get(ctx);
            
            Context shareCtx = changeSupport.getConfigSharingContext(ctx);
            
            Collection<? extends AbstractBean> list = Collections.emptyList();
            try
            {
                list = configAdapter.translate(shareCtx, request);
            }
            catch (ConfigChangeRequestTranslationException e)
            {
                String msg = "Error occurred translating from " + request.getModifiedAppName()
                + "'s source bean class " + request.getBeanClass();
                new MinorLogMsg(this, msg, e).log(shareCtx);
                throw new HomeException(msg, e);
            }
            catch (Throwable t)
            {
                String msg = "Unexpected exception occurred translating from " + request.getModifiedAppName()
                        + "'s source bean class " + request.getBeanClass();
                new MinorLogMsg(this, msg, t).log(shareCtx);
                throw new HomeException(msg, t);
            }

            final short operation = request.getTypeOfUpdate();
            Map<Class, Boolean> homeInstalledCache = new HashMap<Class, Boolean>();
            for (AbstractBean updateObj : list)
            {
                if (updateObj instanceof Identifiable)
                {
                    Class<? extends AbstractBean> beanClass = updateObj.getClass();
                    shareCtx = changeSupport.getConfigSharingContext(shareCtx, updateObj);
                    
                    // Ensure that the home that we are updating is configured properly to receive the update
                    initializeHomeSharingContext(shareCtx, updateObj);
                    
                    if (!homeInstalledCache.containsKey(beanClass))
                    {
                        try
                        {
                            homeInstalledCache.put(beanClass, HomeSupportHelper.get(ctx).getHome(shareCtx, updateObj.getClass()) != null);
                        }
                        catch (Throwable t)
                        {
                            homeInstalledCache.put(beanClass, Boolean.FALSE);
                        }
                    }
                    if (homeInstalledCache.get(beanClass))
                    {
                        // This is a Home update
                        if (LogSupport.isDebugEnabled(ctx))
                        {
                            new DebugLogMsg(this, " Applying the action to the bean " + updateObj, null).log(ctx);
                        }
                        executeHomeAction(shareCtx, operation, updateObj);
                    }
                    else
                    {
                        // This was a bean update (not a home update).  Translater would have completed
                        // processing by calling the bean's setters.
                    }
                }
                else
                {
                    // This was a bean update (not a home update).  Translater would have completed
                    // processing by calling the bean's setters.
                }
            }

            if (LogSupport.isDebugEnabled(ctx))
            {
                new DebugLogMsg(this, "Successfully processed " + request.getTypeOfUpdate()
                        + " update request from " + request.getModifiedAppName()
                        + " on source bean class " + request.getBeanClass(), null).log(ctx);
            }
        }
        else
        {
            String msg = "Unable to process " + request.getTypeOfUpdate()
                    + " update request from " + request.getModifiedAppName()
                    + " on source bean class " + request.getBeanClass()
                    + ".  See logs for details.";
            new MinorLogMsg(this, msg, null).log(ctx);
            throw new HomeException(msg);
        }
        
        return obj;
    }


    private Object executeHomeAction(Context ctx, short operation, AbstractBean bean) throws HomeException
    {
        Object result = null;
     
        if (operation == -1)
        {
            operation = HomeOperationEnum.CREATE_INDEX;
        }
        
        if (LogSupport.isDebugEnabled(ctx))
        {
            String msg = "Executing " + operation + " operation on bean class " + bean.getClass().getName();
            if (bean instanceof Identifiable)
            {
                msg += " with ID [" + ((Identifiable)bean).ID() + "]...";
            } 
            new DebugLogMsg(this, msg, null).log(ctx);
        }
        
        HomeSupport homeSupport = HomeSupportHelper.get(ctx);
        if (HomeOperationEnum.STORE_INDEX ==  operation)
        {
            // Can't do 'createOrStore' here because there is no gaurantee that the delta update contains
            // enough information for create.
            result = homeSupport.storeBean(ctx, bean);
        }
        else if (HomeOperationEnum.CREATE_INDEX == operation)
        {
            // Do 'createOrStore' here because on create we are passed the entire bean contents, which would
            // be sufficient for both create and store.
            boolean beanExists = false;
            if (bean instanceof Identifiable
                    && homeSupport.findBean(ctx, bean.getClass(), ((Identifiable)bean).ID()) != null)
            {
                beanExists = true;
            }

            if (!beanExists)
            {
                result = homeSupport.createBean(ctx, bean);
            }
            else
            {
                result = homeSupport.storeBean(ctx, bean);
            }
        }
        else if (HomeOperationEnum.REMOVE_INDEX ==  operation )
        {
            homeSupport.removeBean(ctx, bean);
        }
        else
        {
            if (LogSupport.isEnabled(ctx, SeverityEnum.INFO))
            {
                new InfoLogMsg(this, "ConfigChangeRequest for operation " + operation + " is not supported ", null).log(ctx);
            }
        }
        
        if (LogSupport.isDebugEnabled(ctx))
        {
            String msg = "Finished executing " + operation + " operation on bean class " + bean.getClass().getName();
            if (bean instanceof Identifiable)
            {
                msg += " with ID [" + ((Identifiable)bean).ID() + "]";
            } 
            new DebugLogMsg(this, msg, null).log(ctx);
        }

        return result;
    }
                        


    private BeanClassMapping getLocalBeanMapping(Context ctx, ConfigChangeRequest request) throws HomeException
    {
        Home home = (Home) ctx.get(BeanClassMappingHome.class);
        BeanClassMapping beanClass = (BeanClassMapping) home.find(ctx, request.getBeanClass());
        return beanClass;
    }

    /**
     * This method either removes or renders inoperable homes that are known to cause problems
     * with the processing of incoming config sharing requests.
     */
    private void initializeHomeSharingContext(Context ctx, Object obj)
    {
        Class homeClass = XBeans.getClass(ctx, obj.getClass(), Home.class);
        if (homeClass != null)
        {
            Home home = null;
            if (obj instanceof AdjustmentType)
            {
                // Adjustment types are stored in database, but config sharing is used
                // to keep each application's cache sync'd with each other.  This special
                // logic is used to config-share the caches.
                home = (Home) ctx.get(CoreAdjustmentTypeHomePipelineFactory.ADJUSTMENT_TYPE_READ_ONLY_HOME);
            }
            else
            {
                try
                {
                    home = HomeSupportHelper.get(ctx).getHome(ctx, obj.getClass());
                }
                catch (HomeException e)
                {
                    // NOP
                    // This is not a fatal step and anyone trying to use the home later will see
                    // the same error, so they should log it.
                }
            }

            // If this is an XDB home pipeline with a TotalCachingHome, we only want to propogate changes to the cache
            // The source application would have already updated the database so this is an optimization to avoid race
            // conditions and duplicate database updates.
            if (home instanceof HomeProxy)
            {
                TotalCachingHome cachingHome = (TotalCachingHome) ((HomeProxy)home).findDecorator(TotalCachingHome.class);
                if (cachingHome != null
                        && cachingHome.hasDecorator(XDBHome.class))
                {
                    home = cachingHome.getCacheHome();
                }
            }
            
            // Must remove ReadOnlyHome instances from pipeline in order to do config sharing
            while (home instanceof HomeProxy
                    && ((HomeProxy)home).hasDecorator(ReadOnlyHome.class))
            {
                home = ((ReadOnlyHome) ((HomeProxy)home).findDecorator(ReadOnlyHome.class)).getDelegate(ctx);
            }

            if (home != null)
            {
                ctx.put(homeClass, home);
            }
        }

        // Extensions must be config-shared separately if necessary.  We don't
        // want to let the ExtensionHandlingHome process shared config changes
        // because the sending application may have already done everything
        // that is necessary.
        ctx.put(ExtensionHandlingHome.BYPASS_UPDATE_TO_EXTENSION, Boolean.TRUE);
    }
}
