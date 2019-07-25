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
 * Copyright (c) Redknee Inc. and its subsidiaries. All Rights Reserved.
 */
package com.redknee.app.crm.web.control;


import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import com.redknee.framework.xhome.beans.ExceptionListener;
import com.redknee.framework.xhome.beans.XBeans;
import com.redknee.framework.xhome.beans.xi.PropertyInfo;
import com.redknee.framework.xhome.beans.xi.XInfo;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.entity.EntityInfo;
import com.redknee.framework.xhome.entity.EntityInfoHome;
import com.redknee.framework.xhome.filter.Predicate;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.SortingHome;
import com.redknee.framework.xhome.language.MessageMgr;
import com.redknee.framework.xhome.web.agent.WebAgents;
import com.redknee.framework.xhome.web.renderer.DefaultDetailRenderer;
import com.redknee.framework.xhome.web.renderer.DetailRenderer;
import com.redknee.framework.xhome.web.renderer.DetailRendererProxy;
import com.redknee.framework.xhome.webcontrol.AbstractWebControl;
import com.redknee.framework.xhome.webcontrol.GenericTableWebControl;
import com.redknee.framework.xhome.webcontrol.HTMLExceptionListener;
import com.redknee.framework.xhome.webcontrol.WebControl;
import com.redknee.framework.xhome.webcontrol.WebController;
import com.redknee.framework.xlog.log.MajorLogMsg;
import com.redknee.framework.xlog.log.MinorLogMsg;

import com.redknee.app.crm.extension.CategoryExtension;
import com.redknee.app.crm.extension.Extension;
import com.redknee.app.crm.extension.ExtensionHolder;
import com.redknee.app.crm.extension.FinalExtension;
import com.redknee.app.crm.extension.MandatoryExtension;
import com.redknee.app.crm.extension.SubCategoryExtension;
import com.redknee.app.crm.support.ExtensionSupportHelper;


/**
 * This web control presents a collection of objects in a tabbed layout.
 * The tabs to be shown are selected through a checkbox menu which is shown
 * over the tabbed layout.
 * It supports the addition and removal of elements, custom tab description
 * functions. (e.g. based on licence or permission perhaps).
 *
 * @author Marcio Marques
 * @since 8.5
 */
public class CheckboxTabbedExtensionCollectionWebControl extends GenericTableWebControl
{
    private class ExtensionInfo
    {
        private Class class_;
        
        private EntityInfo entityInfo_;
        private boolean checked_;
        private int maxChildren_;
        private boolean readOnly_;
        
        public ExtensionInfo(Class extensionClass, EntityInfo entityInfo, boolean checked)
        {
            class_ = extensionClass;
            entityInfo_ = entityInfo;
            checked_ = checked;
            maxChildren_ = 0;
            readOnly_ = false;
        }
        
        public ExtensionInfo(Class extensionClass, EntityInfo entityInfo, int maxChildren)
        {
            class_ = extensionClass;
            entityInfo_ = entityInfo;
            checked_ = false;
            maxChildren_ = maxChildren;
            readOnly_ = false;
        }

        public ExtensionInfo(Class extensionClass)
        {
            class_ = extensionClass;
            entityInfo_ = null;
            checked_ = false;
            maxChildren_ = 0;
            readOnly_ = false;

        }

        public int getMaxChildren()
        {
            return maxChildren_;
        }

        
        public void setMaxChildren(int maxChildren)
        {
            maxChildren_ = maxChildren;
        }

        public Class getExtensionClass()
        {
            return class_;
        }

        
        public EntityInfo getEntityInfo()
        {
            return entityInfo_;
        }

        public void setEntityInfo(EntityInfo entityInfo)
        {
            entityInfo_ = entityInfo;
        }
        
        public boolean isChecked()
        {
            return checked_;
        }

        
        public void setChecked(boolean checked)
        {
            checked_ = checked;
        }

        public boolean isReadOnly()
        {
            return readOnly_;
        }

        
        public void setReadOnly(boolean readOnly)
        {
            readOnly_ = readOnly;
        }
    }
    
    /**
     * The # of tabs presented in each row. 
     */
    protected int tabsPerRow_ = 5;
   

    protected short assumedNestedTabRows_ = 3;
    
    private Predicate predicate_;
    
    public static final String DEFAULT_TABS_KEY = "TabbedCololectionWebControl.DEFAULT_TABS";
    
    public CheckboxTabbedExtensionCollectionWebControl(Class beanType, Predicate predicate)
    {
        super(beanType);
        predicate_ = predicate;
        
    }

    public CheckboxTabbedExtensionCollectionWebControl(Context ctx, Class beanType,  Predicate predicate)
    {
        super(ctx, beanType);
        predicate_ = predicate;
    }

    public CheckboxTabbedExtensionCollectionWebControl(WebControl delegate, Class beanType, Predicate predicate)
    {
        super(delegate, beanType);
        predicate_ = predicate;
    }

    public CheckboxTabbedExtensionCollectionWebControl(WebControl delegate, Predicate predicate)
    {
        super(delegate);
        predicate_ = predicate;
    }


    /**
     * @return # of tabs to be shown in each row.  The new element tab goes on the last row, even if it is already full.
     */
    public int getTabsPerRow()
    {
        return tabsPerRow_;
    }

    /**
     * Sets the number of tabs to be presented in each row.  Can be used to control the width of the set of tabs.
     * 
     * The new element tab goes on the last row, even if it is already full.
     * 
     * @param tabsPerRow New # of tabs per row
     * @return This web control (for use in model files)
     */
    public CheckboxTabbedExtensionCollectionWebControl setTabsPerRow(int tabsPerRow)
    {
        tabsPerRow_ = tabsPerRow;
        return this;
    }
    
    private Collection<Map> getExtensionsCollection(Context ctx)
    {
        final Context subCtx = ctx.createSubContext();
        Home entityHome = (Home) subCtx.get(EntityInfoHome.class);
        
        try
        {
            entityHome = new SortingHome(entityHome.where(ctx, predicate_));
            Collection col = entityHome.selectAll(ctx);
         
            Collection<Map> result = new ArrayList<Map>();
            Map<Class,Map> mapping = new HashMap<Class,Map>();
         
            Iterator<EntityInfo> iter = col.iterator();
            
            while (iter.hasNext())
            {
                EntityInfo entityInfo = iter.next();
                Class extensionClass = Class.forName(entityInfo.getClassName());
                Extension extension = (Extension) extensionClass.newInstance();
                
                int maxChildren = (extension instanceof CategoryExtension)?((CategoryExtension)extension).getMaxChildren():0;
                if (!(extension instanceof SubCategoryExtension))
                {
                    Map<ExtensionInfo,Collection> extensionMap = mapping.get(extensionClass);

                    if (extensionMap==null)
                    {
                        extensionMap = new HashMap<ExtensionInfo,Collection>();
                        extensionMap.put(new ExtensionInfo(extensionClass, entityInfo, maxChildren), new ArrayList());
                        mapping.put(extensionClass, extensionMap);
                    }
                    else
                    {
                        ExtensionInfo extensionInfo = extensionMap.keySet().iterator().next();
                        if (extensionInfo.getEntityInfo()==null)
                        {
                            extensionInfo.setEntityInfo(entityInfo);
                        }
                        extensionInfo.setMaxChildren(maxChildren);
                    }
                    result.add(extensionMap);
                }
                else
                {
                    SubCategoryExtension subCategoryExtension = (SubCategoryExtension) extension;
                    Map<ExtensionInfo,Collection> parentExtensionMap = mapping.get(subCategoryExtension.getExtensionCategoryClass());
                    if (parentExtensionMap==null)
                    {
                        parentExtensionMap = new HashMap<ExtensionInfo,Collection>();
                        parentExtensionMap.put(new ExtensionInfo(subCategoryExtension.getExtensionCategoryClass()), new ArrayList());
                        mapping.put(subCategoryExtension.getExtensionCategoryClass(), parentExtensionMap);
                    }

                    Map<ExtensionInfo,Collection> extensionMap = mapping.get(extensionClass);
                    if (extensionMap==null)
                    {
                        extensionMap = new HashMap<ExtensionInfo,Collection>();
                        extensionMap.put(new ExtensionInfo(extensionClass, entityInfo, maxChildren), new ArrayList());
                        mapping.put(extensionClass, extensionMap);
                    }
                    
                    parentExtensionMap.get(parentExtensionMap.keySet().iterator().next()).add(extensionMap);
                }
            }
            return result;
        }
        catch (Exception e)
        {
            new MajorLogMsg(this, e.toString(), e).log(ctx);
        }
        return new ArrayList();
                    
    }
    
    private void outputCheckBox(final Context ctx, final PrintWriter out, final String name, final Class bean, final String entityInfoName, final boolean checked, final boolean readOnly, final int ident)
    {
        int mode = ctx.getInt("MODE", DISPLAY_MODE);
        StringBuilder checkboxStr = new StringBuilder();
        if (readOnly && checked)
        {
            StringBuilder hiddenCheckboxStr = new StringBuilder();
            hiddenCheckboxStr.append("<input type=\"hidden\" name=\"");
            hiddenCheckboxStr.append(name).append(SEPERATOR).append(bean.getName()).append(SEPERATOR);
            hiddenCheckboxStr.append("_enabled");
            hiddenCheckboxStr.append("\" value=\"X\" ");
            hiddenCheckboxStr.append("checked=\"checked\" ");  
            hiddenCheckboxStr.append("/>");
            out.print(hiddenCheckboxStr.toString());
        }
        for (int i=0; i<ident; i++)
        {
            checkboxStr.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
        }
        checkboxStr.append("<input type=\"checkbox\" name=\"");
        checkboxStr.append(name).append(SEPERATOR).append(bean.getName()).append(SEPERATOR);
        if (readOnly)
        {
            checkboxStr.append("_disabled");
        }
        else
        {
            checkboxStr.append("_enabled");
        }
        checkboxStr.append("\" value=\"\" ");
        checkboxStr.append(" onClick=\"autoPreview('" + 
                WebAgents.getDomain(ctx) + "', event);\"");
        if (checked)
        {
                checkboxStr.append("checked=\"checked\" ");  
        }
        if ((mode != EDIT_MODE && mode != CREATE_MODE) || readOnly)
        {
            checkboxStr.append("disabled");
        }
        checkboxStr.append("/>");
        out.print(checkboxStr.toString());
        out.print(entityInfoName);
        out.print("<BR/>");
    }

    private void outputRadioBox(final Context ctx, final PrintWriter out, final String name, final Class bean, final String entityClassName, final String entityInfoName, final boolean checked, final boolean readOnly, final int ident)
    {
        int mode = ctx.getInt("MODE", DISPLAY_MODE);
        StringBuilder checkboxStr = new StringBuilder();
        if (readOnly && checked)
        {
            StringBuilder hiddenCheckboxStr = new StringBuilder();
            hiddenCheckboxStr.append("<input type=\"hidden\" name=\"");
            hiddenCheckboxStr.append(name).append(SEPERATOR).append(bean.getName()).append(SEPERATOR);
            hiddenCheckboxStr.append("radioGroup_enabled");
            hiddenCheckboxStr.append("\" value=\"X\" ");
            hiddenCheckboxStr.append("checked=\"checked\" ");  
            hiddenCheckboxStr.append("/>");
            out.print(hiddenCheckboxStr.toString());
        }
        for (int i=0; i<ident; i++)
        {
            checkboxStr.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
        }
        checkboxStr.append("<input type=\"radio\" name=\"");
        checkboxStr.append(name).append(SEPERATOR).append(bean.getName()).append(SEPERATOR);
        if (readOnly)
        {
            checkboxStr.append("radioGroup_disabled");
        }
        else
        {
            checkboxStr.append("radioGroup_enabled");
        }
        checkboxStr.append("\" value=\"");
        checkboxStr.append(entityClassName);
        checkboxStr.append("\" ");
        checkboxStr.append(" onClick=\"autoPreview('" + 
                WebAgents.getDomain(ctx) + "', event);\"");
        if (checked)
        {
                checkboxStr.append("checked=\"checked\" ");  
        }
        if ((mode != EDIT_MODE && mode != CREATE_MODE) || readOnly)
        {
            checkboxStr.append("disabled");
        }
        checkboxStr.append("/>");
        out.print(checkboxStr.toString());
        out.print(entityInfoName);
        out.print("<BR/>");
    }
    
    private void outputExtension(final Context ctx, final PrintWriter out, final String name, final Map<ExtensionInfo,Collection> extensionMap, final Class parentClass, boolean checkBox, final int ident)
    {
        ExtensionInfo extensionInfo = extensionMap.keySet().iterator().next();
        Collection<Map<ExtensionInfo,Collection>> extensionChildren = extensionMap.get(extensionInfo);
        boolean checked = extensionInfo.isChecked();
        boolean readOnly = extensionInfo.isReadOnly();
        
        if (checkBox)
        {
            outputCheckBox(ctx, out, name, extensionInfo.getExtensionClass(), extensionInfo.getEntityInfo().getName(), checked, readOnly, ident);
        }
        else
        {
            outputRadioBox(ctx, out, name, parentClass, extensionInfo.getExtensionClass().getName(), extensionInfo.getEntityInfo().getName(), checked, readOnly, ident);
        }
        
        if (extensionChildren!=null && extensionChildren.size()>0 && checked)
        {
            for (Map<ExtensionInfo,Collection> childMap : extensionChildren)
            {
                boolean childCheckbox = false;
                if (extensionInfo.getMaxChildren()>1)
                {
                    childCheckbox = true;
                }
                outputExtension(ctx, out, name, childMap, extensionInfo.getExtensionClass(), childCheckbox, ident + 1);
            }
            
        }
        
    }
    
    private void removeExtensions(Context ctx, String name, Collection beans, final Map<ExtensionInfo,Collection> extensionMap)
    {
        if (beans!=null)
        {

            ExtensionInfo extensionInfo = extensionMap.keySet().iterator().next();
            Collection<Map<ExtensionInfo,Collection>> extensionChildren = extensionMap.get(extensionInfo);
            
            int j=0;
            Iterator<ExtensionHolder> iter = beans.iterator();
            while (iter.hasNext())
            {
                j++;
                ExtensionHolder holder = iter.next();
                if (holder.getExtension().getClass().getName().equals(extensionInfo.getEntityInfo().getClassName()))
                {
                    iter.remove();
                    
                    // Shift all create mode values now that we've removed an entry
                    int oldSize = (beans.size()+1);
                    for (int k=j; k<oldSize; k++)
                    {
                        String createModeKey = name + SEPERATOR + k + SEPERATOR + "_createMode";
                        String nextCreateModeKey = name + SEPERATOR + (k+1) + SEPERATOR + "_createMode";
                        Object createModeValue = ctx.get(nextCreateModeKey);
                        if (createModeValue != null)
                        {
                            ctx.put(createModeKey, createModeValue);
                        }
                    }

                    String createModeKey = name + SEPERATOR + oldSize + SEPERATOR + "_createMode";
                    ctx.remove(createModeKey);
                }
            }
            if (extensionChildren!=null && extensionChildren.size()>0)
            {
                for (Map<ExtensionInfo,Collection> childMap : extensionChildren)
                {
                    removeExtensions(ctx, name, beans,childMap);
                }
            }
        }
    }

    /**
     * This method will check the extensions checkbox that need to be checked, mark the read-oly ones, and remove the children extensions
     * of unchecked extensions.
     * @param ctx
     * @param beans
     * @param extensionMap
     * @param readOnly
     */
    private void markCheckedExtensions(Context ctx, String name, Collection beans, final Map<ExtensionInfo,Collection> extensionMap, final Set<ExtensionHolder> validBeans, final boolean readOnly)
    {
        if (beans!=null)
        {
            ExtensionInfo extensionInfo = extensionMap.keySet().iterator().next();
            Collection<Map<ExtensionInfo,Collection>> extensionChildren = extensionMap.get(extensionInfo);
            boolean extensionIsReadOnly = readOnly;
            
            // Marking extension if it's checked
            int j = 0;
            for (Object obj : beans)
            {
                j++;
                String createModeKey = name + SEPERATOR + j + SEPERATOR + "_createMode";
                
                ExtensionHolder holder = (ExtensionHolder) obj;
                if (holder.getExtension().getClass().getName().equals(extensionInfo.getEntityInfo().getClassName()))
                {
                    validBeans.add(holder);
                    extensionInfo.setChecked(true);
                    // Setting read-only extensions
                    if (readOnly || (!ctx.getBoolean(createModeKey, Boolean.FALSE) && holder.getExtension() instanceof FinalExtension))
                    {
                        extensionIsReadOnly = true;
                        extensionInfo.setReadOnly(true);
                    }
                    else if ((holder.getExtension() instanceof MandatoryExtension))
                    {
                        extensionInfo.setReadOnly(true);
                    }
                    break;
                }
            }
            
            if (!extensionInfo.isChecked() && MandatoryExtension.class.isAssignableFrom(extensionInfo.getExtensionClass()))
            {
                try
                {
                    ExtensionHolder holder = (ExtensionHolder) XBeans.instantiate(beanType_, ctx);
                    holder.setExtension( (Extension) XBeans.instantiate(extensionInfo.getExtensionClass(), ctx));
                    beans.add(holder);
                    validBeans.add(holder);
                    
                    String createModeKey = name + SEPERATOR + beans.size() + SEPERATOR + "_createMode";
                    ctx.put(createModeKey, Boolean.TRUE);
                }
                catch (Throwable t)
                {
                    
                }

                extensionInfo.setChecked(true);
                extensionInfo.setReadOnly(true);

            }
    
            if (extensionChildren!=null && extensionChildren.size()>0)
            {
                boolean childrenChecked = false;
                // Marking the checked children extensions and setting this as read only
                // if one of the child is read only. 
                for (Map<ExtensionInfo,Collection> childMap : extensionChildren)
                {
                    markCheckedExtensions(ctx, name, beans, childMap, validBeans, extensionIsReadOnly);
                    ExtensionInfo childExtensionInfo = childMap.keySet().iterator().next();
                    if (childExtensionInfo.isChecked())
                    {
                        childrenChecked = true;
                        extensionInfo.setChecked(true);
                        if (childExtensionInfo.isReadOnly())
                        {
                            extensionInfo.setReadOnly(true);
                        }
                    }
                }

                // Removed children extensions is this is not checked.
                if (!extensionInfo.isChecked())
                {
                    for (Map<ExtensionInfo,Collection> childMap : extensionChildren)
                    {
                        removeExtensions(ctx, name, beans, childMap);
                    }
                }

                if (extensionInfo.getMaxChildren()==1 && extensionInfo.isChecked())
                {
                    // If no children is checked and only one can be selected (radio button) then mark the
                    // first child.
                    if (!childrenChecked)
                    {
                        try
                        {
                            Map<ExtensionInfo,Collection> childMap = extensionChildren.iterator().next();
                            ExtensionInfo childExtensionInfo = childMap.keySet().iterator().next();
        
                            ExtensionHolder holder = (ExtensionHolder) XBeans.instantiate(beanType_, ctx);
                            holder.setExtension( (Extension) XBeans.instantiate(childExtensionInfo.getExtensionClass(), ctx));
                            beans.add(holder);
                            
                            markCheckedExtensions(ctx, name, beans, childMap, validBeans, false);
                            
                            String createModeKey = name + SEPERATOR + beans.size() + SEPERATOR + "_createMode";
                            ctx.put(createModeKey, Boolean.TRUE);
                        }
                        catch (Throwable t)
                        {
                        }
                    // If any children is checked and it's read only, then mark the all the children as read only.
                    } else if (extensionInfo.isReadOnly())
                    {
                        for (Map<ExtensionInfo,Collection> childMap : extensionChildren)
                        {
                            ExtensionInfo childExtensionInfo = childMap.keySet().iterator().next();
                            childExtensionInfo.setReadOnly(true);
                        }                        
                    }
                }
            }
        }
    }

    /**
     * Output extensions checkboxes and radio buttons.
     */
    private void outputCheckBoxes(final Context ctx, final PrintWriter out, final String name, final Collection beans, MessageMgr mmgr)
    {
        int mode = ctx.getInt("MODE", DISPLAY_MODE);
        final Context subCtx = ctx.createSubContext();
        final Collection<Map> extensions = getExtensionsCollection(subCtx);
        Set<ExtensionHolder> validBeans = new HashSet<ExtensionHolder>();
        
        if (extensions.size()>0)
        {
            try
            {
                for (Map extensionMap : extensions)
                {
                    markCheckedExtensions(ctx, name, beans, extensionMap, validBeans, false);
                    outputExtension(ctx, out, name, extensionMap, null, true, 0);
                }
            }
            catch (Exception e)
            {
                new MajorLogMsg(this, e.toString(), e).log(ctx);
            }
            
            Iterator iter = beans.iterator();
            while (iter.hasNext())
            {
                if (!validBeans.contains(iter.next()))
                {
                    iter.remove();
                }
            }
        }
        else
        {
            beans.clear();
            out.print(mmgr.get("Extensions.NoExtensionAvailable_" + beanType_.getSimpleName(), "No extensions available"));
        }
    }
    
    private Map<Class, Boolean> retrieveCheckBoxes(Context ctx, ServletRequest req, String name)
    {
        int mode = ctx.getInt("MODE", DISPLAY_MODE);
        Map<Class, Boolean> result = new HashMap<Class, Boolean>();
        final Context subCtx = ctx.createSubContext();
        ExtensionSupportHelper.get(subCtx).setParentBean(subCtx, subCtx.get(AbstractWebControl.BEAN));

        Home entityHome = (Home) subCtx.get(EntityInfoHome.class);
        
        try
        {
            entityHome = new SortingHome(entityHome.where(subCtx, predicate_));
            Collection col = entityHome.selectAll(subCtx);
            String webName = name + SEPERATOR + "class";
            for (Object entityInfo : col)
            {
                String parameterName = name + SEPERATOR + ((EntityInfo) entityInfo).getClassName() + SEPERATOR + "_enabled";
                if ( req.getParameter(parameterName) != null )
                {
                    result.put(Class.forName(((EntityInfo) entityInfo).getClassName()), Boolean.TRUE);
                }
                
                parameterName = name + SEPERATOR + ((EntityInfo) entityInfo).getClassName() + SEPERATOR + "radioGroup_enabled";
                if ( req.getParameter(parameterName) != null )
                {
                    result.put(Class.forName(req.getParameter(parameterName)), Boolean.TRUE);
                }
            }
        }
        catch (Exception e)
        {
            new MajorLogMsg(this, e.toString(), e).log(ctx);
        }   
        return result;
    }
    
    @Override
    public void fromWeb(Context ctx, Object obj, ServletRequest req, String name)
    {
        Context currentCtx = ctx;
        Context controllerCtx = ctx;
        while (currentCtx != null
                && currentCtx.has(HttpServletRequest.class))
        {
            controllerCtx = currentCtx;
            currentCtx = (Context) currentCtx.get("..");
        }
        
        //WebControl delegate_;

        Collection beans = (Collection) obj;
        
        String str = req.getParameter(name + SEPERATOR + "_count");

        if ( str == null )
        {
            throw new NullPointerException("Null CheckBox Value");
        }
        
        Map<Class, Boolean> expectedExtensions = retrieveCheckBoxes(ctx, req, name);
        
        // Removing expected extensions if its category extension is not checked.
        Iterator<Class> expectedExtensionsIter = expectedExtensions.keySet().iterator();
        while (expectedExtensionsIter.hasNext())
        {
            Class extensionClass = expectedExtensionsIter.next();
            try
            {
                Extension extension = (Extension) XBeans.instantiate(extensionClass, ctx);
                if (extension instanceof SubCategoryExtension)
                {
                    if (expectedExtensions.get(((SubCategoryExtension) extension).getExtensionCategoryClass()) == null)
                    {
                        // Remove extension is category is not present, and restart the iterator.
                        expectedExtensionsIter.remove();
                        expectedExtensionsIter = expectedExtensions.keySet().iterator();
                    }
                }
            }
            catch (Exception e)
            {
                
            }
        }
        
        
        super.fromWeb(ctx, beans, req, name);
        
        int i=0;
        int j=0;
        Iterator<ExtensionHolder> iter = beans.iterator();
        Collection<Class> beansToBeRemoved = new ArrayList<Class>();

        // Remove unchecked extensions.
        while (iter.hasNext())
        {
            ExtensionHolder holder = iter.next();
            i++;
            String createModeKey = name + SEPERATOR + i + SEPERATOR + "_createMode";
            String newCreateModeKey = name + SEPERATOR + (i - j) + SEPERATOR + "_createMode";
            if (holder.getExtension() == null)
            {
                iter.remove();
                j++;
            }
            else if (expectedExtensions.get(holder.getExtension().getClass())==null)
            {
                iter.remove();
                j++;
                beansToBeRemoved.add(holder.getExtension().getClass());
            }
            else
            {
                expectedExtensions.remove((holder.getExtension().getClass()));
            }
            
            String createMode = req.getParameter(createModeKey);
            controllerCtx.put(newCreateModeKey, "X".equals(createMode));
        }
        
        // Add new checked extensions.
        for (Class extensionClass : expectedExtensions.keySet())
        {
            i++;
            String newCreateModeKey = name + SEPERATOR + (i - j) + SEPERATOR + "_createMode";
            try
            {
                
                ExtensionHolder holder = (ExtensionHolder) XBeans.instantiate(beanType_, ctx);
                holder.setExtension( (Extension) XBeans.instantiate(extensionClass, ctx));
                beans.add(holder);
            }
            catch (Throwable t)
            {
                
            }
            controllerCtx.put(newCreateModeKey, Boolean.TRUE);
        }
    }
   
    /** Returns the DetailRenderer if specified in the Context or the DefaultDetailRenderer otherwise. **/
    public DetailRenderer detailRenderer(Context ctx)
    {
       DetailRenderer renderer = (DetailRenderer) ctx.get(DetailRenderer.class);
       
       return ( renderer == null ) ?
          DefaultDetailRenderer.instance() :
          renderer;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void toWeb(final Context context, final PrintWriter out, final String name, final Object obj)
    {
        Context ctx = context.createSubContext();
        DetailRenderer        renderer  = detailRenderer(ctx);
        MessageMgr            mmgr      = new MessageMgr(ctx, this);

        int mode = ctx.getInt("MODE", DISPLAY_MODE);
        Collection beans = (Collection) obj; 
        
        ExtensionSupportHelper.get(ctx).setParentBean(ctx, ctx.get(AbstractWebControl.BEAN));

        outputCheckBoxes(ctx, out, name, beans, mmgr);

        if (beans!=null)
        {
            Iterator<ExtensionHolder> iter = beans.iterator();
    
            // Remove category extensions so that they won't be shown in the extensions tabs.
            while (iter.hasNext())
            {
                ExtensionHolder holder = iter.next();
                if (holder.getExtension() instanceof CategoryExtension)
                {
                    iter.remove();
                }
            }
        }
        

        final Object[] objects = beans.toArray();

        final XInfo id;
        if (objects != null && objects.length > 0)
        {
            id =  XBeans.getInstanceOf(ctx, objects[0].getClass(), XInfo.class);
        }
        else
        {
            id = XBeans.getInstanceOf(ctx, beanType_, XInfo.class);
        }

        if (mode == EDIT_MODE || mode == CREATE_MODE)
        {
            out.print("<input type=\"hidden\" name=\"" + name + SEPERATOR + "_count\" value=\"" + (beans.size())
                    + "\" />");
        }
        
        String[][] tabsInSec = null;
        HashMap defaultTabs = (ctx.has(DEFAULT_TABS_KEY) ? (HashMap)ctx.get(DEFAULT_TABS_KEY) : new HashMap());
        
        if(objects.length>0) 
        {
            int numRows = getTabsPerRow() > 0 ? new Double(Math.ceil((double)(objects.length)/(double)getTabsPerRow())).intValue() : new Double(Math.ceil((double)(objects.length)/(double)5)).intValue();
            int tabsPerRow = getTabsPerRow() > 0 ? getTabsPerRow() : 5;
            
            tabsInSec = new String[numRows][tabsPerRow];
            
            out.print("<table width=\"100%\">");
            for ( int i = 0; i < numRows; ++i )
            {
                int offset = i*tabsPerRow;
                for (int j = 0; j < tabsPerRow && (j + offset) < objects.length; ++j)
                {
                    tabsInSec[i][j]="";
                    if(getDescriptionFunction()!=null)
                    {
                        tabsInSec[i][j]=getDescriptionFunction().f(ctx, objects[j+offset]).toString();
                    }
                    else 
                    {
                        PropertyInfo oid=id.getID();
                        if(oid==null)
                        {
                            new MajorLogMsg(this,"DEvELOPER ERROR: no ID property for "
                                    +objects[0].getClass().getName(),null).log(ctx);
                            tabsInSec[i][j]=Integer.toString(j+offset+1);
                        }
                        else
                        {
                            tabsInSec[i][j]=oid.toString(objects[j+offset]);
                        }
                    }
                }
                
                
                final String[] tabsInRow;
                if( objects.length-offset < tabsInSec[i].length )
                {
                    int size = objects.length-offset;
                    tabsInRow = new String[size];
                    for( int k = 0; k < objects.length-offset; ++k )
                    {
                        tabsInRow[k] = tabsInSec[i][k];
                    }
                }
                else
                {
                    tabsInRow = tabsInSec[i];
                }

                int    tabSecId    = ctx.getInt(AbstractWebControl.TAB_SECTION_ID, 0)+1;
                int subTabSecId = tabSecId;
                AbstractWebControl.prepareTabMemoryHolder(ctx, tabSecId, out, tabsInRow, WebAgents.getDomain(ctx), defaultTabs);
                AbstractWebControl.startTabRow(out);
                AbstractWebControl.writeTabs(tabSecId, tabsInRow, out, WebAgents.getDomain(ctx), defaultTabs);
                
                AbstractWebControl.endTabRow(out);

                int tabId = 0;
                boolean isFirstTabInSec = true;
                for (int j = 0; j < tabsInRow.length; ++j)
                {
                    final int jj = (j+offset+1);
                    
                    AbstractWebControl.startTabSection(out, tabSecId, tabId, isFirstTabInSec, tabsInRow.length, WebAgents.getDomain(ctx));
                    isFirstTabInSec = false;
                    tabId++ ;

                    out.print("<tr><td>");
                    
                    Context ctx1=ctx.createSubContext();
                    if (mode == EDIT_MODE || mode == CREATE_MODE)
                    {
                        DetailRenderer oldDetailRenderer = (DetailRenderer)ctx.get(DetailRenderer.class, DefaultDetailRenderer.instance());
                        ctx1.put(DetailRenderer.class, new DetailRendererProxy(oldDetailRenderer){
                            boolean isFirstTime=true;
                            @Override
                            public void Table(Context ctx1, PrintWriter out, String title, String attrs)
                            {
                                StringBuilder checkboxStr = new StringBuilder();
                                checkboxStr.append("<input type=\"hidden\" name=\"");
                                checkboxStr.append(name).append(SEPERATOR).append(jj).append(SEPERATOR);
                                checkboxStr.append("_enabled\" value=\"X\" ");
                                checkboxStr.append("/>");
                                out.print(checkboxStr.toString());

                                super.Table(ctx1, out, title, attrs);
                            }
                            
                            @Override
                            public void TableEnd(Context ctx1, PrintWriter out)
                            {
                                super.TableEnd(ctx1, out);
                            }
                            
                        });
                    }
                    
                    
                    // Put the current tab section in the sub-context for the delegate to use (it may contain tabs itself)
                    ctx1.put(AbstractWebControl.TAB_SECTION_ID, subTabSecId);

                    String createModeKey = name + SEPERATOR + jj + SEPERATOR + "_createMode";
                    if( (jj-1) < objects.length )
                    {
                        Context subCtx = ctx1.createSubContext(); 
                        Object bean = objects[jj-1];

                        final boolean isNew;
                        HttpServletRequest req = (HttpServletRequest) ctx.get(HttpServletRequest.class);
                        if (req != null)
                        {
                            boolean isSuccessfulWriteOperation = WebController.isCmd("Update",  req) || WebController.isCmd("Save",  req);
                            if (isSuccessfulWriteOperation)
                            {
                                ExceptionListener el = (ExceptionListener) ctx.get(ExceptionListener.class);
                                if (el instanceof HTMLExceptionListener)
                                {
                                    isSuccessfulWriteOperation &= !((HTMLExceptionListener) el).hasErrors();
                                }
                            }
                            if (!isSuccessfulWriteOperation)
                            {
                                isNew = ctx.getBoolean(createModeKey, Boolean.FALSE);
                            }
                            else
                            {
                                // Bean was just saved
                                isNew = false;
                            }
                        }
                        else
                        {
                            isNew = ctx.getBoolean(createModeKey, Boolean.FALSE);
                        }
                        
                        if (isNew)
                        {
                            out.print("<input type=\"hidden\" name=\"" + createModeKey + "\" value=\"X\" />");
                            if (mode == EDIT_MODE)
                            {
                                // if the bean was newly created then we need to set the mode accordingly
                                subCtx.put("MODE", CREATE_MODE);
                            }
                        }
                        
                        getDelegate().toWeb(subCtx, out, name + SEPERATOR + jj, bean);
                    }
                    else if( beanType_ != null )
                    {
                        try
                        {
                            // Support for adding "New" Beans
                            Context subCtx = ctx1.createSubContext(); 
                            subCtx.put("MODE", CREATE_MODE);
                            
                            // Mark the bean to indicate that it needs to be in the CREATE mode
                            Object bean = XBeans.instantiate(beanType_, subCtx);

                            out.print("<input type=\"hidden\" name=\"" + createModeKey + "\" value=\"X\" />");
                            getDelegate().toWeb(subCtx, out, name + SEPERATOR + jj, bean);
                        }
                        catch (Exception e)
                        {
                            new MinorLogMsg(this,"Error!!!",e).log(ctx1);
                        }
                    }
                    
                    // Update the tab section after the delegate is finished
                    subTabSecId = ctx1.getInt(AbstractWebControl.TAB_SECTION_ID, subTabSecId);
                    
                    out.print("</td></tr>");
                }

                AbstractWebControl.endTabSection(out, true);
                ctx.put(AbstractWebControl.TAB_SECTION_ID, subTabSecId);
            }
            
            out.print("</tr></table>");
        }
    }
}

