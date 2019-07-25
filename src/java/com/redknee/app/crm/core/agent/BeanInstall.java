package com.redknee.app.crm.core.agent;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Iterator;

import com.redknee.framework.core.platform.CoreSupport;
import com.redknee.framework.xhome.beans.AbstractPropertySupport;
import com.redknee.framework.xhome.beans.SafetyUtil;
import com.redknee.framework.xhome.beans.XCloneable;
import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.context.ContextAgent;
import com.redknee.framework.xhome.context.ContextFactory;
import com.redknee.framework.xlog.log.CritLogMsg;

import com.redknee.app.crm.bean.Adjustment;
import com.redknee.app.crm.bean.AdjustmentType;
import com.redknee.app.crm.bean.CT23RuleDefaultValueConfig;
import com.redknee.app.crm.bean.CurrencyPrecision;
import com.redknee.app.crm.bean.GSMPackage;
import com.redknee.app.crm.bean.SmppConfig;
import com.redknee.app.crm.bean.StartupScripts;
import com.redknee.app.crm.bean.TDMAPackage;
import com.redknee.app.crm.bean.Transaction;
import com.redknee.app.crm.bean.template.AdjustmentTemplate;
import com.redknee.app.crm.bean.template.AdjustmentTypeTemplate;
import com.redknee.app.crm.bean.template.GSMPackageTemplate;
import com.redknee.app.crm.bean.template.TDMAPackageTemplate;
import com.redknee.app.crm.bean.template.TransactionTemplate;
import com.redknee.app.crm.bean.template.VSATPackageTemplate;
import com.redknee.app.crm.delivery.email.KeywordConfiguration;
import com.redknee.app.crm.factory.ContextRedirectingContextFactory;
import com.redknee.app.crm.listener.CurrencyPrecisionChangeListener;
import com.redknee.app.crm.notification.NotificationTypeEnum;
import com.redknee.app.crm.notification.delivery.BinaryDeliveryService;
import com.redknee.app.crm.notification.delivery.EmailDeliveryService;
import com.redknee.app.crm.notification.delivery.MessageDeliveryService;
import com.redknee.app.crm.notification.delivery.MessageDeliveryServiceProxy;
import com.redknee.app.crm.notification.delivery.SmsDeliveryService;
import com.redknee.app.crm.notification.generator.MessageGeneratorProxy;
import com.redknee.app.crm.notification.generator.SimpleEmailGenerator;
import com.redknee.app.crm.notification.generator.SimpleJasperMessageGenerator;
import com.redknee.app.crm.notification.generator.SimpleSmsGenerator;
import com.redknee.app.crm.notification.generator.SimpleUjacMessageGenerator;
import com.redknee.app.crm.notification.liaison.NotificationLiaison;
import com.redknee.app.crm.notification.liaison.NotificationLiaisonProxy;
import com.redknee.app.crm.notification.liaison.RealTimeNotificationLiaison;
import com.redknee.app.crm.notification.liaison.ScheduledTaskNotificationLiaison;
import com.redknee.app.crm.support.NotificationSupport;
import com.redknee.app.crm.support.NotificationSupportHelper;


public class BeanInstall extends CoreSupport implements ContextAgent
{
    public final static String ADJUSTMENT_TEMPLATE = AdjustmentTemplate.class.getName();
    public final static String ADJUSTMENTTYPE_TEMPLATE = AdjustmentTypeTemplate.class.getName();
    public final static String TRANSACTION_TEMPLATE = TransactionTemplate.class.getName();
    public final static String GSM_PACKAGE_TEMPLATE = GSMPackageTemplate.class.getName();
    public final static String TDMA_PACKAGE_TEMPLATE = TDMAPackageTemplate.class.getName();
    public final static String VSAT_PACKAGE_TEMPLATE = VSATPackageTemplate.class.getName();

    public void execute(Context ctx) throws AgentException
    {
        try
        {
            CoreSupport.bindBean(ctx, StartupScripts.class);
            
            CoreSupport.bindBeanToXML(ctx, AdjustmentTemplate.class, Adjustment.class, "AdjustmentTemplate.xml");

            CoreSupport.bindBeanToXML(ctx, AdjustmentTypeTemplate.class, AdjustmentType.class, "AdjustmentTypeTemplate.xml");

            CoreSupport.bindBeanToXML(ctx, TransactionTemplate.class, Transaction.class, "TransactionTemplate.xml");

            CoreSupport.bindBeanToXML(ctx, GSMPackageTemplate.class, GSMPackage.class, "GSMPackageTemplate.xml");
            CoreSupport.bindBeanToXML(ctx, TDMAPackageTemplate.class, TDMAPackage.class, "TDMAPackageTemplate.xml");
            CoreSupport.bindBeanToXML(ctx, VSATPackageTemplate.class, TDMAPackage.class, "VSATPackageTemplate.xml");

            // Email keyword substitution configuration
            // (deprecated, auto-migrates to KeyConfiguration in KeyConfigurationHomePipelineFactory
            CoreSupport.bindBean(ctx, KeywordConfiguration.class);
            CurrencyPrecision currentPrecision = (CurrencyPrecision) CoreSupport.bindBean(ctx, CurrencyPrecision.class);
            
            CoreSupport.bindBean(ctx, SmppConfig.class);

			CoreSupport.bindBean(ctx, CT23RuleDefaultValueConfig.class);
            
            currentPrecision.removePropertyChangeListener(CurrencyPrecisionChangeListener.instance());
            currentPrecision.addPropertyChangeListener(CurrencyPrecisionChangeListener.instance());

            installNotificationBeans(ctx);
        }
        catch (Throwable t)
        {
            new CritLogMsg(this, "fail to install", t).log(ctx);
            throw new AgentException("Fail to complete AppCrmCore BeanInstall", t);
        }
    }

    protected void installNotificationBeans(Context ctx)
    {
        // Install basic configurable liaisons
        CoreSupport.bindBean(ctx, RealTimeNotificationLiaison.class, NotificationLiaisonProxy.class);
        CoreSupport.bindBean(ctx, ScheduledTaskNotificationLiaison.class);
        
        // Default liaison is the configurable real-time one
        ctx.put(NotificationLiaison.class, new ContextRedirectingContextFactory(RealTimeNotificationLiaison.class));
        
        // Install configurable message generators
        CoreSupport.bindBean(ctx, SimpleSmsGenerator.class, MessageGeneratorProxy.class);
        CoreSupport.bindBean(ctx, SimpleEmailGenerator.class, MessageGeneratorProxy.class);
        CoreSupport.bindBean(ctx, SimpleJasperMessageGenerator.class, MessageGeneratorProxy.class);
        CoreSupport.bindBean(ctx, SimpleUjacMessageGenerator.class, MessageGeneratorProxy.class);
        
        // Install configurable delivery services
        CoreSupport.bindBean(ctx, SmsDeliveryService.class, MessageDeliveryServiceProxy.class);
        CoreSupport.bindBean(ctx, EmailDeliveryService.class, MessageDeliveryServiceProxy.class);
        CoreSupport.bindBean(ctx, BinaryDeliveryService.class, MessageDeliveryServiceProxy.class);
        
        ctx.put("Email Delivery Service Config", new SimplifiedDeliveryServiceContextFactory(EmailDeliveryService.class));
        ctx.put("SMS Delivery Service Config", new SimplifiedDeliveryServiceContextFactory(SmsDeliveryService.class));
        ctx.put("Binary Delivery Service Config", new SimplifiedDeliveryServiceContextFactory(BinaryDeliveryService.class));

        installTypeSpecificNotificationLiaisons(ctx);
    }

    protected void installTypeSpecificNotificationLiaisons(Context ctx)
    {
        Iterator<NotificationTypeEnum> iter = NotificationSupportHelper.get(ctx).getSupportedNotificationTypes(ctx).iterator();
        while (iter.hasNext())
        {
            NotificationTypeEnum type = iter.next();
            String key = NotificationSupport.NOTIFICATION_TYPE_LIAISON_CTX_KEY_PREFIX + type;

            boolean isNew = !ctx.has(key);
            NotificationLiaisonProxy liaison = (NotificationLiaisonProxy) CoreSupport.bindBean(ctx, 
                    key, 
                    NotificationLiaisonProxy.class);
            if (isNew)
            {
                NotificationLiaison delegate = (NotificationLiaison) ctx.get(NotificationLiaison.class, new RealTimeNotificationLiaison());
                if (NotificationLiaisonProxy.class.getName().equals(delegate.getClass().getName()))
                {
                    delegate = ((NotificationLiaisonProxy) delegate).getDelegate();
                }
                liaison.setDelegate(delegate);
            }
        }
    }

    private static final class SimplifiedDeliveryServiceContextFactory implements ContextFactory
    {
        private Class<? extends MessageDeliveryService> type_;
        private MessageDeliveryService cachedInstance_ = null;
        
        public SimplifiedDeliveryServiceContextFactory(Class<? extends MessageDeliveryService> type)
        {
            type_ = type;
        }

        public Object create(Context ctx)
        {
            if (cachedInstance_ != null)
            {
                return cachedInstance_;
            }
            
            MessageDeliveryService svc = (MessageDeliveryService) ctx.get(type_);
            if (type_.isInstance(svc))
            {
                cachedInstance_ = svc;
            }
            else if (svc instanceof MessageDeliveryServiceProxy)
            {
                final MessageDeliveryServiceProxy proxy = (MessageDeliveryServiceProxy) svc;
                
                MessageDeliveryService baseService = proxy.findDecorator(type_);
                if (type_.isInstance(baseService)
                        && baseService instanceof AbstractPropertySupport)
                {
                    AbstractPropertySupport simpleService = (AbstractPropertySupport) baseService;
                    if (simpleService instanceof XCloneable)
                    {
                        try
                        {
                            simpleService = (AbstractPropertySupport) ((XCloneable) simpleService).clone();
                        }
                        catch (CloneNotSupportedException e)
                        {
                            // NOP
                        }
                    }
                    
                    simpleService.addPropertyChangeListener(new SimplifiedDeliveryServicePropertyChangeListener(proxy));
                    
                    cachedInstance_ = (MessageDeliveryService) simpleService;
                }
            }
            
            return cachedInstance_;
        }
    }

    private static final class SimplifiedDeliveryServicePropertyChangeListener implements PropertyChangeListener
    {
        private final MessageDeliveryServiceProxy proxy_;

        private SimplifiedDeliveryServicePropertyChangeListener(MessageDeliveryServiceProxy proxy)
        {
            this.proxy_ = proxy;
        }

        public void propertyChange(PropertyChangeEvent evt)
        {
            Object src = evt.getSource();
            if (src instanceof XCloneable)
            {
                src = safeClone(src);
            }
            
            if (src instanceof MessageDeliveryService)
            {
                MessageDeliveryService delegate = proxy_.getDelegate();
                
                if (delegate instanceof MessageDeliveryServiceProxy)
                {
                    delegate = safeClone(delegate);
                    
                    MessageDeliveryServiceProxy lastDelegate = (MessageDeliveryServiceProxy) delegate;
                    while (lastDelegate.getDelegate() instanceof MessageDeliveryServiceProxy)
                    {
                        lastDelegate = (MessageDeliveryServiceProxy) lastDelegate.getDelegate();
                    }
                    if (lastDelegate != null)
                    {
                        lastDelegate.setDelegate((MessageDeliveryService) src);
                    }
                }
                else
                {
                    delegate = (MessageDeliveryService) src;
                }
                
                proxy_.setDelegate(delegate);
            }
        }

        protected <T> T safeClone(T obj)
        {
            if (obj instanceof XCloneable)
            {
                try
                {
                    return (T) SafetyUtil.safeClone((XCloneable)obj);
                }
                catch (CloneNotSupportedException e)
                {
                }
            }
            return obj;
        }
    }
}
