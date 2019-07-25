/*
    WebControllerWebControl

    Author : Kevin Greer
    Date   : Sept 4, 2003

    Copyright (c) Redknee, 2003
        - all rights reserved
*/

package com.redknee.app.crm.web.control;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.*;

import com.redknee.framework.xhome.beans.Child;
import com.redknee.framework.xhome.beans.SafetyUtil;
import com.redknee.framework.xhome.beans.XBeans;
import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.context.ContextAgent;
import com.redknee.framework.xhome.context.ContextAgentProxy;
import com.redknee.framework.xhome.context.ContextFactory;
import com.redknee.framework.xhome.elang.Or;
import com.redknee.framework.xhome.filter.Predicate;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.menu.XMenu;
import com.redknee.framework.xhome.menu.XMenuHome;
import com.redknee.framework.xhome.menu.XMenuWebControllerConfig;
import com.redknee.framework.xhome.support.IdentitySupport;
import com.redknee.framework.xhome.web.agent.ParameterContext;
import com.redknee.framework.xhome.web.agent.WebAgent;
import com.redknee.framework.xhome.web.agent.WebAgents;
import com.redknee.framework.xhome.web.border.Border;
import com.redknee.framework.xhome.web.border.BorderRequestServicer;
import com.redknee.framework.xhome.web.renderer.FormRenderer;
import com.redknee.framework.xhome.web.renderer.NullFormRenderer;
import com.redknee.framework.xhome.web.util.Link;
import com.redknee.framework.xhome.web.xmenu.border.XMenuActionDefBorder;
import com.redknee.framework.xhome.web.xmenu.factory.XMenuWebControllerFactory;
import com.redknee.framework.xhome.webcontrol.AbstractWebControl;
import com.redknee.framework.xhome.webcontrol.RequestServicer;
import com.redknee.framework.xhome.webcontrol.WebControl;
import com.redknee.framework.xhome.webcontrol.WebController;
import com.redknee.framework.xlog.log.MajorLogMsg;
import com.redknee.framework.xlog.log.MinorLogMsg;


class ByParentPredicate
   implements Predicate
{

   protected final Object parentValue_;

   public ByParentPredicate(Object parentValue)
   {
      parentValue_ = parentValue;
   }

   public boolean f(Context ctx, Object obj)
   {
      Child child = (Child) obj;

      return SafetyUtil.safeEquals(child.getParent(), parentValue_);
   }

}



/**
 *  Parent/Child Composite WebController.
 *
 *  Makes use of WebAgent Domains to prevent the embedded WebController from
 *  conflicting with the parent WebController's HTTP parameter space.
 **/
public abstract class WebControllerWebControl57
   extends    ContextAgentProxy
   implements WebAgent, WebControl
{

   /** Parent Bean of Parent/Child relationship. **/
   public final static String PARENT_CPROPERTY = "WebControllerWebControl.Parent";

    /**
     * Flag that prevents us from installing the borders every time we execute(ctx)
     */
   protected boolean firstUse=true;

   public static void setParent(Context ctx, Object value)
   {
      ctx.put(PARENT_CPROPERTY, value);
   }

   public static Object getParent(Context ctx)
   {
      return (Object) ctx.get(PARENT_CPROPERTY);
   }


   ////////////////////////////////////////////////////////////////////////

   /** The Class of Beans to be Controlled. **/
   protected Class        beanType_;

   /** Optional Key for finding Homein Context. **/
   protected String       homeIdentifier_;

   /**
    * Xmenu WebController config key
    */
   protected String       xmenuKey_ = null;

   public WebControllerWebControl57(Class beanType)
   {
      this(beanType, null);
   }

   public WebControllerWebControl57(Class beanType, String homeIdentifier)
   {
      beanType_       = beanType;
      homeIdentifier_ = homeIdentifier;
      setXmenuKey(beanType);

      setDelegate(new ContextAgent()
      {
         public void execute(Context ctx)
            throws AgentException
         {
            PrintWriter   out  = WebAgents.getWriter(ctx);
            WebController ctrl = (WebController) ctx.get(WebController.class);

            out.println("\n<!-- BEGIN WEBCONTROLLERWEBCONTROL -->");

            if ( homeIdentifier_ != null )
            {
               ctrl.setHome((Home)ctx.get(homeIdentifier_));
            }

            ctrl.execute(ctx);

            out.println("\n<!-- END WEBCONTROLLERWEBCONTROL -->");
         }
      });

   }

   public WebControllerWebControl57 setXmenuKey(String xmenuKey)
   {
     xmenuKey_ = xmenuKey;

     return this;
   }

   public String getXmenuKey()
   {
     return xmenuKey_;
   }

   /**
    * Default for xmenu key. Defaults to the bean class name stripped of package prefix.
    * @see setXmenuKey(String)
    */
   protected void setXmenuKey(Class beanType)
   {
      String key = beanType.getName();
      key = key.substring((key.lastIndexOf(".") +1));
      setXmenuKey(key);
   }

   public WebControllerWebControl57 addBorder(final Border border)
   {
      // Add a Border to a WebAgent
      setDelegate(new ContextAgentProxy(getDelegate())
      {
         public void execute(Context ctx)
            throws AgentException
         {
            HttpServletRequest  req = (HttpServletRequest)  ctx.get(HttpServletRequest.class);
            HttpServletResponse res = (HttpServletResponse) ctx.get(HttpServletResponse.class);

            try
            {
               border.service(ctx, req, res, new RequestServicer()
               {
                  public void service(Context ctx, HttpServletRequest req, HttpServletResponse res)
                     throws ServletException, IOException
                  {
                     ctx = ctx.createSubContext();

                     ctx.put(HttpServletRequest.class,  req);
                     ctx.put(HttpServletResponse.class, res);

                     WebAgents.setParameters(ctx, new ParameterContext(ctx));

                     try
                     {
                        delegate(ctx);
                     }
                     catch (AgentException e)
                     {
                     }
                  }
               });
            }
            catch (IOException e)
            {
            }
            catch (ServletException e)
            {
            }
         }
      });

      return this;
   }


   ///////////////////////////////////////////////////////////////////// Impl WebAgent

   public void execute(final Context ctx)
     throws AgentException
   {
      int mode = ctx.getInt("MODE", DISPLAY_MODE);

      // Cannot create children until after the parent is created
      if ( mode == CREATE_MODE )
      {
         // WebAgents.getWriter(ctx).println("&lt;save first&gt;");
         WebAgents.getWriter(ctx).println("&nbsp;");
         return;
      }

      Object bean = ctx.get(AbstractWebControl.BEAN);

      if ( bean == null )
      {
         return;
      }

      HttpServletRequest req  = (HttpServletRequest) ctx.get(HttpServletRequest.class);
      WebController      ctrl = getWebController(ctx);

      // add borders
      addXmenuBorders(ctx);

      if ( homeIdentifier_ != null )
      {
         // If I ever remember why I did this then I should document it, KGR
         ctrl.setHome(((Context)ctx.get("..") ).get(homeIdentifier_));
      }

      // ctrl.setBackEnabled(false);

      // This will disable the embedded search border
      // ctrl.setSummaryBorder(NullBorder.instance());

      setUpWebControlFunctionButton(ctx,  ctrl, beanType_);

      ctx.put(WebController.class, ctrl);

      ctx.put(WebControllerWebControl57.class, this);

      // store parent in Ctx
      setParent(ctx, bean);

      XBeans.putBeanFactory(ctx, beanType_, new ContextFactory()
      {
         public Object create(Context ctx)
         {
            try
            {
               Object          parent    = getParent(ctx);
               // Use the Parent Context ("..") so that we don't call ourself and cause an infinite recursion
               Child           child     = (Child)           XBeans.instantiate(beanType_, (Context) ctx.get(".."));
               IdentitySupport idSupport = (IdentitySupport) XBeans.getInstanceOf(ctx, parent.getClass(), IdentitySupport.class);

               child.setParent(idSupport.ID(parent));

               return modifyChild(ctx, child);
            }
            catch (Exception e)
            {
                return null;
            }
            
         }
      });
      // do something so that primarykey is already set

            // each nested controller will prepend it's domain component.
            String fullDomain = WebAgents.getDomain(ctx);
            if (fullDomain.indexOf(".") > -1)
            {
                fullDomain = fullDomain.substring(fullDomain.lastIndexOf("."));
            }
            final String domain = fullDomain;

      // adapt servlet request
      req = new HttpServletRequestWrapper(req)
      {
         public String getParameter(String key)
         {
                      return super.getParameter(domain + key);
         }
                         /**
                    * @deprecated  leave this for backward compatability
                    */
                    public String getRealPath(String param) {
                        return super.getRealPath(param);
                    }

                    /**
                    * @deprecated leave this for backward compatability
                    */
                    public boolean isRequestedSessionIdFromUrl(){
                        return super.isRequestedSessionIdFromUrl();
                    }

      };

      ctx.put(HttpServletRequest.class, req);
      WebAgents.setParameters(ctx, new ParameterContext(ctx));
      ctx.put(FormRenderer.class,       NullFormRenderer.instance());

      // decorate child home, usually to filter
      if ( homeIdentifier_ == null )
      {
            Class homeClass = XBeans.getClass(ctx, beanType_, Home.class);
            ctx.put(homeClass, decorateHome(ctx, (Home) ctx.get(homeClass)));
      }
      else
      {
          ctx.put(homeIdentifier_, decorateHome(ctx, (Home)ctx.get(homeIdentifier_)));
      }

      delegate(ctx);
   }


   /** Template method to customize child bean's if required. **/
   public Object modifyChild(Context ctx, Object child)
   {
      return child;
   }


   /**
    *  Decorate the Home with a HomePredicateFilterHome so that only Children
    *  from the Parent bean are available.
    **/
   public Home decorateHome(Context ctx, Home home)
   {
      Object parent = getParent(ctx);

      IdentitySupport idSupport = (IdentitySupport) XBeans.getInstanceOf(ctx, parent.getClass(), IdentitySupport.class);

      final Object key = idSupport.ID(parent);

      return home.where(ctx, new Or(new ByParentPredicate(key), createSQLClause(key)));
   }


   public abstract String createSQLClause(Object key);


   ///////////////////////////////////////////////////////////////////// Impl WebControl


   /** Only toWeb is implemented or required. **/
   public void toWeb(Context ctx, PrintWriter out, String name, Object obj)
   {
      ctx = ctx.createSubContext();

      // adapt Link
      HttpServletRequest req = (HttpServletRequest) ctx.get(HttpServletRequest.class);
      Link link = new Link(ctx);
      // link.add("CMD", "Preview");
      link.copy(req, "key");
      link.copy(req, "query");
      link.copy(req, "mode");
      // link.add("mode", "display");
      ctx.put(Link.class, link);

      WebAgents.setDomain(ctx, name);
      WebAgents.setWriter(ctx, out);

      try
      {
         execute(ctx);
      }
      catch (AgentException e)
      {
         e.printStackTrace();
      }
   }


   /** No-op'ed because the Beans field is only a dummy and doesn't actually contain a value. **/
   public Object fromWeb(Context ctx, ServletRequest req, String name)
      throws NullPointerException
   {
      return null;
   }


   /** No-op'ed because the Beans field is only a dummy and doesn't actually contain a value. **/
   public void fromWeb(Context ctx, Object obj, ServletRequest req, String name)
   {
      // nop
   }

   /**
    *  Template method to enable/disable delete/new/preview/copy/help/update buttons
    *  for this WebController.
    *
    *  It probably would have made more sense to have had a WebController factory instead.
    **/
   public void setUpWebControlFunctionButton(Context ctx, WebController ctrl, Class beanType_)
   {
   }

   /**
    *
    */
   public WebController getWebController(Context ctx)
   {
     WebController ctlr = null;
     if (xmenuKey_ != null)
     {
        try
        {
           XMenu service = (XMenu) ((Home) ctx.get(XMenuHome.class)).find(ctx, xmenuKey_);
           if (service != null)
           {
              BorderRequestServicer brs = (BorderRequestServicer) new XMenuWebControllerFactory().create(ctx.createSubContext().put("XMenuObj", service));
              // pull off the borders
              Object delegate = brs.getDelegate();
              while (delegate != null &&
                  ! (delegate instanceof WebController) )
              {
                 delegate = ((BorderRequestServicer) delegate).getDelegate();
              }

              ctlr = (WebController) delegate;
           }
           else
           {
             new MinorLogMsg(this, "XMenu service key ["+xmenuKey_+"] not found", null).log(ctx);
           }
        }
        catch(HomeException e)
        {
          new MajorLogMsg(this, e.getMessage(), e).log(ctx);
        }
     }

     if (ctlr == null)
     {
        ctlr = new WebController(ctx, beanType_);
     }

     return ctlr;
   }

   public void addXmenuBorders(Context ctx)
   {
       if(!firstUse)
       {
           return;
       }

       firstUse=false;

     if (xmenuKey_ != null)
     {
        try
        {
           XMenu service = (XMenu) ((Home) ctx.get(XMenuHome.class)).find(ctx, xmenuKey_);
           if (service != null)
           {
             XMenuWebControllerConfig config =
               (XMenuWebControllerConfig) service.getConfig();

             if (config.getActions().size() > 0)
             {
               addBorder(new XMenuActionDefBorder(ctx, config.getActions()));
             }
           }
       }
       catch(HomeException e)
       {
         new MajorLogMsg(this, e.getMessage(), e).log(ctx);
       }
     }
   }
}
