/*
 * This code is a protected work and subject to domestic and international
 * copyright law(s).  A complete listing of authors of this work is readily
 * available.  Additionally, source code is, by its very nature, confidential
 * information and inextricably contains trade secrets and other information
 * proprietary, valuable and sensitive to Redknee.  No unauthorized use,
 * disclosure, manipulation or otherwise is permitted, and may only be used
 * in accordance with the terms of the license agreement entered into with
 * Redknee Inc. and/or its subsidiaries.
 *
 * Copyright (c) Redknee Inc. and its subsidiaries. All Rights Reserved.
 */
package com.redknee.app.crm.web.control;

import java.io.PrintWriter;
import java.util.Collection;

import javax.servlet.ServletRequest;

import com.redknee.framework.xhome.beans.FacetFactory;
import com.redknee.framework.xhome.beans.FacetMgr;
import com.redknee.framework.xhome.beans.XBeans;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.entity.EntityInfo;
import com.redknee.framework.xhome.entity.EntityInfoHome;
import com.redknee.framework.xhome.entity.EntityInfoKeyWebControl;
import com.redknee.framework.xhome.entity.EntitySupport;
import com.redknee.framework.xhome.entity.EntityWebControl;
import com.redknee.framework.xhome.filter.Predicate;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.SortingHome;
import com.redknee.framework.xhome.web.renderer.DefaultDetailRenderer;
import com.redknee.framework.xhome.web.renderer.DetailRenderer;
import com.redknee.framework.xhome.web.support.WebSupport;
import com.redknee.framework.xhome.webcontrol.AbstractWebControl;
import com.redknee.framework.xhome.webcontrol.WebControl;
import com.redknee.framework.xlog.log.DebugLogMsg;
import com.redknee.framework.xlog.log.LogSupport;
import com.redknee.framework.xlog.log.MajorLogMsg;

/**
 * @author victor.stratan@redknee.com
 */
public class EntityOneRowSelectionWebControl extends EntityWebControl
{   
    public EntityOneRowSelectionWebControl(final Predicate predicate)
    {
        super(predicate);
    }

    public EntityOneRowSelectionWebControl(final Class cls, final String role)
    {
        super(cls, role);
    }

    public EntityOneRowSelectionWebControl(final Class cls)
    {
        super(cls);
    }

    public EntityOneRowSelectionWebControl(final Class[] clss)
    {
        super(clss);
    }

    public EntityOneRowSelectionWebControl(final Class cls, final boolean sameLine)
    {
        super(cls, sameLine);
    }

    public EntityOneRowSelectionWebControl(final Class[] clss, final boolean sameLine)
    {
        super(clss, sameLine);
    }

    public EntityOneRowSelectionWebControl()
    {
        super();
    }
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected EntityInfoKeyWebControl getKeyWebControl(Object obj)
    {
        return isOptional_ ? nullKey_ : super.getKeyWebControl(obj);
    }

    /**
     * By default disabled, the web-control will use bean factory to instantiate.
     * If enabled, it will use the proto-type from EntityInfo entry for the type
     * @param enable
     * @return
     */
    public EntityOneRowSelectionWebControl usingPrototypeInstantiation(boolean enable)
    {
        useProtoTypeInstantiation_ = enable;
        return this;
    }

    /**
     * By default disabled, the web-control will use bean factory to instantiate.
     * If enabled, it will use the proto-type from EntityInfo entry for the type
     * @param enable
     * @return
     */
    public EntityOneRowSelectionWebControl setOptional(boolean isOptional)
    {
        isOptional_ = isOptional;
        return this;
    }

    @Override
    protected String printKey(final Context ctx, final PrintWriter out, final String name, final Object obj,
            final Home home)
    {
        EntityInfo info = EntitySupport.findEntity(ctx, obj);
        String infoName = (info == null) ? null : info.getName();

        EntityInfoKeyWebControl keyWebControl = getKeyWebControl(obj);
        Home entityHome = new SortingHome(home.where(ctx, getPredicate()));
        Context keyCtx = ctx.createSubContext().put(EntityInfoHome.class, entityHome);

        // Put a stupid facet manager in the selectAll context because we don't want to see entries
        // that "may have implementations available" for the given type.  If they are not directly
        // assignable to the target type then don't allow them to be selected.
        keyCtx.put(FacetMgr.class, NULL_FACET_MGR);

        try
        {
            String webName = name + SEPERATOR + "class";

            if (!getAllowToChoose())
            {
                Collection col = entityHome.selectAll(keyCtx);
                Object[] objs = col.toArray();

                if (getAllowToChoose() || info == null)
                {
                    info = (EntityInfo) objs[0];
                }

                out.print("<input type=\"hidden\" id=\"");
                out.print(WebSupport.fieldToId(ctx, webName));
                out.print("\" name=\"");
                out.print(webName);
                out.print("\" value=\"");
                out.print(keyWebControl.getDesc(ctx, info));
                out.print("\">");
            }
            else
            {
                keyWebControl.toWeb(keyCtx, out, webName, infoName);
            }
        }
        catch (Exception e)
        {
            new MajorLogMsg(this, e.toString(), e).log(ctx);
        }

        if (info != null)
        {
            return info.getName();
        }

        return null;
    }
    
    
    @Override
    public Object fromWeb(Context ctx, ServletRequest req, String name) throws NullPointerException
    {
        try
        {
            String key = (String) key_.fromWeb(ctx, req, name + SEPERATOR + "class");
            Object proto = EntitySupport.instantiateEntity(ctx, key);
            if (proto != null)
            {
                if (!useProtoTypeInstantiation_)
                {
                    proto = XBeans.instantiate(proto.getClass(), ctx);
                }
                WebControl ctrl = webControl(ctx, proto);
                if (ctrl != null)
                {
                    try
                    {
                        try
                        {
                            ctrl.fromWeb(ctx, proto, req, name + SEPERATOR + "obj");
                            // ctrl.fromWeb(ctx, proto, req, name + SEPERATOR + key +
                            // SEPERATOR + "obj");
                        }
                        catch (UnsupportedOperationException e)
                        {
                            return ctrl.fromWeb(ctx, req, name + SEPERATOR + "obj");
                        }
                    }
                    catch (NullPointerException e)
                    {
                        return proto;
                    }
                }
            }
            return proto;
        }
        catch (NullPointerException e)
        {
            throw e;
        }
        catch (Throwable t)
        {
            new MajorLogMsg(this, t.toString(), t).log(ctx);
        }

        return null;
    }
    
    /**
     * @see com.redknee.framework.xhome.webcontrol.OutputWebControl#toWeb(com.redknee.framework.xhome.context.Context,
     *      java.io.PrintWriter, java.lang.String, java.lang.Object)
     */
    @Override
    public void toWeb(Context ctx, PrintWriter out, String name, Object obj)
    {
        Home home = (Home) ctx.get(EntityInfoHome.class);

        Object parentBean = ctx.get(AbstractWebControl.BEAN);

        if (isSameLine())
        {
            out.println("<table border=\"0\"><tr><td>");
        }

        out.println("<table border=\"0\"><tr><td>");

        String entityName = printKey(ctx, out, name, obj, home);

        // Replace default bean with prototype if available.
        if (entityName != null)
        {
          EntityInfo info = EntitySupport.findEntity(ctx, obj); 
          // Use default bean to lookup corresponding EntityInfo
          // with which to create prototype
          try
          {
            if(useProtoTypeInstantiation_)
            {
                Object o = XBeans.instantiate(info.getClassName(), ctx);
                if (o != null &&
                        o.equals(obj))
                    {
                      Object proto = EntitySupport.instantiatePrototype(ctx, info);
                      if (proto != null)
                      {
                        obj = proto;
                      }
                    }
            }
            
          }
          catch(Exception e) // IOException, InstantiationException
          {
            // ignore
            if (LogSupport.isDebugEnabled(ctx))
            {
              new DebugLogMsg(this, e.getMessage(), e).log(ctx);
            }
          }
        }

        String webName = name;

        if (webName.startsWith("."))
        {
            webName = webName.substring(1);
        }

        webName = webName.replace('.', 'X');
        webName = webName.replace('-', '_');

        out.println("</td><td>");

        // prints the open/close button

        /*
        out.println("<div id=\"" + webName + "Closed\" class=\"divHidden\"><p>[<span onclick=\"show(\"" + webName
                + "Open\");show(\"" + webName + "Obj\");hide(\"" + webName + "Closed\")\">+</span>]&nbsp;</p></div>");
        out.println("<div id=\"" + webName + "Open\"><p>[<span onclick=\"hide(\"" + webName + "Open\");hide(\"" + webName
                + "Obj\");show(\"" + webName + "Closed\")\">-</span>]&nbsp;</p></div>");
*/
        out.println("</td></tr></table>");

        WebControl ctrl = getWebControl(ctx, obj);

        if (ctrl != null)
        {
            if (isSameLine())
            {
                out.println("</td><td>");
            }
            else
            {
                ctx = ctx.createSubContext();

                ctx.put(AbstractWebControl.NUM_OF_BLANKS, getNumOfBlanks());

                /*
                 * I can only do this if I'm already in a DetailRenderer. If I'm in a TableRenderer then it gets all
                 * messed up.
                 */
                if (ctx.getBoolean("TABLE_MODE"))
                {
                    ctx.put(DetailRenderer.class, new DefaultDetailRenderer()
                    {
                        @Override
                        public void Table(Context ctx1, PrintWriter _out, String title)
                        {
                            _out.print("<table>");
                            if (!"".equals(title))
                            {
                                _out.print("<tr><th colspan=2 bgcolor=\"");
                                _out.print(colours().getDetailTitleBG());
                                _out.print("\">");
                                _out.print("<font color=\"");
                                _out.print(colours().getDetailTitleText());
                                _out.print("\">");
                                _out.print(title);
                                _out.print("</font></td></tr>");
                            }
                        }

                        @Override
                        public void TableEnd(Context ctx1, PrintWriter _out, String footer)
                        {
                            _out.println("</table>");
                        }
                    });
                }
                else
                {
                    ctx.put(DetailRenderer.class, new DefaultDetailRenderer(ctx)
                    {
                        @Override
                        public void Table(Context ctx1, PrintWriter _out, String title)
                        {
                            TREnd(ctx1, _out);
                            if (!"".equals(title))
                            {
                                _out.print("<tr><th colspan=2 bgcolor=\"");
                                _out.print(colours().getDetailTitleBG());
                                _out.print("\">");
                                _out.print("<font color=\"");
                                _out.print(colours().getDetailTitleText());
                                _out.print("\">");
                                _out.print(title);
                                _out.print("</font></td></tr>");
                            }
                        }

                        @Override
                        public void TableEnd(Context ctx1, PrintWriter _out, String footer)
                        {
                            _out.println("</table>");
                        }
                    });
                }

                // out.println("<br/>");
            }

            ctx.put(PARENT_BEAN, parentBean);

            // This "div" is broken by the above Renderers
            out.println("<div id=\"" + webName + "Obj\">");

//            if (entityName != null)
//           {
//              ctrl.toWeb(ctx, out, name + SEPERATOR + entityName + SEPERATOR + "obj", obj);
//            }
//            else
            {
              ctrl.toWeb(ctx, out, name + SEPERATOR + "obj", obj);
            }

            out.println("</div>");
        }

        if (isSameLine())
        {
            out.println("</td></tr></table>");
        }
    }
    
    private volatile boolean useProtoTypeInstantiation_ = false;
    private volatile boolean isOptional_ = false;

    private static final FacetMgr NULL_FACET_MGR = new FacetMgr()
    {
        public Class getClass(Context ctx, Class beanType, Class targetType)
        {
            return null;
        }

        public Object getInstanceOf(Context ctx, Class beanType, Class targetType)
        {
            return null;
        }

        public void register(Context ctx, Class beanType, Class targetType, Object facet)
        {
            return;
        }

        public void register(Context ctx, Class beanType, Class targetType, FacetFactory factory)
        {
            return;
        }

        public void register(Context ctx, Class beanType, Class targetType, Class facet)
        {
            return;
        }

        public void register(Context ctx, Class targetType, FacetFactory factory)
        {
            return;
        }
    };
}
