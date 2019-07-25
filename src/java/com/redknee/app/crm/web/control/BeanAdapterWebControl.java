/*
 * This code is a protected work and subject to domestic and international
 * copyright law(s). A complete listing of authors of this work is readily
 * available. Additionally, source code is, by its very nature, confidential
 * information and inextricably contains trade secrets and other information
 * proprietary, valuable and sensitive to Redknee, no unauthorised use,
 * disclosure, manipulation or otherwise is permitted, and may only be used
 * in accordance with the terms of the licence agreement entered into with
 * Redknee Inc. and/or its subsidiaries.
 *
 * Copyright &copy; Redknee Inc. and its subsidiaries. All Rights Reserved.
 *
 */
package com.redknee.app.crm.web.control;

import java.io.PrintWriter;

import javax.servlet.ServletRequest;

import com.redknee.app.crm.xhome.adapter.BeanAdapter;
import com.redknee.app.crm.exception.XBeansCopyLoggingExceptionListener;
import com.redknee.framework.xhome.beans.AbstractBean;
import com.redknee.framework.xhome.beans.XBeans;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.webcontrol.AbstractWebControl;
import com.redknee.framework.xhome.webcontrol.ProxyWebControl;
import com.redknee.framework.xhome.webcontrol.WebControl;
import com.redknee.framework.xlog.log.LogSupport;

/**
 * <p>
 * A proxy web control for field-level web control which converting the bean in
 * context from one bean class to another.
 * </p>
 * <p>
 * This is needed if the field-level web control expects the parent bean of the
 * field to be of a different class (named <code>WC_BEAN_TYPE</code> in the
 * template) than the one actually in the context.
 * </p>
 * <p>
 * The two bean classes must be adaptable using a BeanAdapter.
 * <code>HOME_BEAN_TYPE</code> is the type of the bean existing in the context
 * under the key <code>AbstractWebControl.BEAN</code>. <code>WC_BEAN_TYPE</code>
 * is the type of the bean expected to be in context under the same key by the
 * delegate web control.
 * </p>
 * <p>
 * A typical use case for this web control:
 * </p>
 * <ol>
 * <li>A bean type C exists in <code>ModelAppCrm</code>, and its corresponding
 * view-level bean type U exists in <code>AppCrm</code>.</li>
 * <li>In <code>AppCrm</code>, bean type U is configured in the menu. The
 * <code>Home</code> pipeline of U is configured to adapt to and from C's Home
 * seamlessly.</li>
 * <li>One of the fields, F, needs a special web control WC to be displayed
 * properly. WC requires information on the bean's other field(s), and looks up
 * the bean in <code>ctx.get(AbstractWebControl.BEAN)</code>.</li>
 * <li>WC exists in <code>AppCrmCore</code>. It cannot be moved to
 * <code>AppCrm</code> because other bean type(s) in <code>ModelAppCrm</code>/
 * <code>AppCrmCore</code> still needs it. As a result, WC expects the bean to
 * be of type C.</li>
 * <li>If we specify the web control of U.F to be WC, there would be
 * <code>ClassCastException</code> because the
 * <code>ctx.get(AbstractWebControl.BEAN)</code> is of type U, but WC expects
 * the bean to be of type C.</li>
 * <li>Instead, we specify the web control of U.F to be
 * <code>BeanAdapterWebControl<U, C>(WC)</code>. For <code>toWeb()</code> and
 * <code>fromWeb()</code> calls, <code>BeanAdapterWebControl</code> converts
 * <code>ctx.get(AbstractWebControl.BEAN)</code> from U to C and store it in a
 * sub-context first, before calling <code>WC.toWeb()</code>/
 * <code>WC.fromWeb()</code> respectively. After WC has finished its processing,
 * <code>BeanAdapterWebControl</code> extracts the C-type bean from the
 * sub-context and convert it back to U-type bean and place it back in the
 * original.</li>
 * </ol>
 * 
 * @author cindy.wong@redknee.com
 * @since 2010-09-22
 */
public class BeanAdapterWebControl<HOME_BEAN_TYPE extends AbstractBean, WC_BEAN_TYPE extends AbstractBean>
    extends ProxyWebControl
{

	/**
	 * Constructor for BeanAdapterWebControl.
	 * 
	 * @param delegate
	 */
	public BeanAdapterWebControl(WebControl delegate,
	    BeanAdapter<HOME_BEAN_TYPE, WC_BEAN_TYPE> adapter)
	{
		super(delegate);
		adapter_ = adapter;
	}

	@Override
	public void toWeb(Context ctx, PrintWriter out, String name, Object obj)
	{
		Context subCtx = adaptContext(ctx);
		super.toWeb(subCtx, out, name, obj);
		ctx.put(
		    AbstractWebControl.BEAN,
		    unAdaptBean(ctx, (WC_BEAN_TYPE) subCtx.get(AbstractWebControl.BEAN)));
	}

	@Override
	public void
	    fromWeb(Context ctx, Object obj, ServletRequest req, String name)
	{
		Context subCtx = adaptContext(ctx);
        
		super.fromWeb(subCtx, obj, req, name);

		if (subCtx.get(AbstractWebControl.BEAN) != ctx.get(AbstractWebControl.BEAN))
        {
	        XBeans.copy(subCtx, subCtx.get(AbstractWebControl.BEAN), ctx.get(AbstractWebControl.BEAN),
	                new XBeansCopyLoggingExceptionListener(subCtx, subCtx.get(AbstractWebControl.BEAN), ctx.get(AbstractWebControl.BEAN)));
        }
	}

	@Override
	public Object fromWeb(Context ctx, ServletRequest req, String name)
	{
	    Context subCtx = adaptContext(ctx);
		Object obj = super.fromWeb(subCtx, req, name);
        if (subCtx.get(AbstractWebControl.BEAN) != ctx.get(AbstractWebControl.BEAN))
        {
            
            XBeans.copy(subCtx, subCtx.get(AbstractWebControl.BEAN), ctx.get(AbstractWebControl.BEAN),
                new XBeansCopyLoggingExceptionListener(subCtx, subCtx.get(AbstractWebControl.BEAN), ctx.get(AbstractWebControl.BEAN)));
        }
        return obj;
	}

	private Context adaptContext(Context ctx)
	{
		Context subContext = ctx.createSubContext();
		Object bean = ctx.get(AbstractWebControl.BEAN);
		Object result = null;

		if (bean != null)
		{
			try
			{
				result = adapter_.adapt(ctx, bean);
			}
			catch (HomeException exception)
			{
				LogSupport.info(ctx, this,
				    "Fail to adapt bean from " + adapter_.getUnAdaptClass()
				        + " to " + adapter_.getAdaptClass());
			}

		}

		if (result == null)
		{
			LogSupport.info(ctx, this, "Fail to adapt bean " + bean);
		}
		else
		{
			subContext.put(AbstractWebControl.BEAN, result);
		}
		return subContext;
	}

	public Object unAdaptBean(Context ctx, WC_BEAN_TYPE bean)
	{
		Object result = null;

		if (bean != null)
		{
			try
			{
				result = adapter_.unAdapt(ctx, bean);
			}
			catch (HomeException exception)
			{
				LogSupport.info(ctx, this,
				    "Fail to unadapt bean from " + adapter_.getAdaptClass()
				        + " to " + adapter_.getUnAdaptClass());
			}
		}

		if (result == null)
		{
			LogSupport.info(ctx, this, "Fail to unAdapt bean " + bean);
		}

		return result;
	}

	private final BeanAdapter<HOME_BEAN_TYPE, WC_BEAN_TYPE> adapter_;
}
