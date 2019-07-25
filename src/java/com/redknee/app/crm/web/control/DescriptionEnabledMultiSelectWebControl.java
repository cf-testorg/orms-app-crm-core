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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import com.redknee.framework.xhome.beans.xi.XInfo;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.In;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.SortingHome;
import com.redknee.framework.xhome.support.IdentitySupport;
import com.redknee.framework.xhome.webcontrol.MultiSelectWebControl;
import com.redknee.framework.xhome.webcontrol.OutputWebControl;
import com.redknee.framework.xlog.log.MajorLogMsg;


/**
 * A MultiSelect web control in which a description is shown for the selected value.
 * 
 * @author Marcio Marques
 *
 */
public class DescriptionEnabledMultiSelectWebControl extends MultiSelectWebControl
{
    protected OutputWebControl descriptionFormatter_;
    public static String DESCRIPTION = "<b>Description:</b> ";
    private XInfo xInfo_;

    public DescriptionEnabledMultiSelectWebControl(Object homeKey, IdentitySupport idSupport, XInfo xInfo, OutputWebControl formatter, OutputWebControl descriptionFormatter)
    {
         super( homeKey, idSupport, formatter);
         this.descriptionFormatter_ = descriptionFormatter;
         this.xInfo_ = xInfo;
    }

    public DescriptionEnabledMultiSelectWebControl(Object homeKey, IdentitySupport idSupport, XInfo xInfo, OutputWebControl formatter, OutputWebControl descriptionFormatter, 
                                            String leftListTitle, String rightListTitle )
    {
        super( homeKey, idSupport, formatter, leftListTitle, rightListTitle);
        this.descriptionFormatter_ = descriptionFormatter;
        this.xInfo_ = xInfo;
    }
    
    private Object getBeanFromCollection(Collection beans, Object identifier)
    {
        for (Object bean : beans)
        {
            if (idSupport_.ID(bean).equals(identifier))
            {
                return bean;
            }
        }
        return null;
    }

    public void toWeb(Context ctx, PrintWriter out, String name, Object obj)
    {
        int mode     = ctx.getInt("MODE", DISPLAY_MODE);
        Home home    = getHome(ctx);
        home = home.where(ctx, getSelectFilter());
     
        Set set     = (Set) obj;
        Set selectedSet = new TreeSet(getComparator());

        String hiddenField = "";
        
        try
        {
            Set selectedIds = new HashSet();
            for ( Iterator i = set.iterator() ; i.hasNext() ; )
            {
                String  string_key  = i.next().toString();
                selectedIds.add(fromStringId(ctx, string_key));
            }

            Collection allBeans = home.select(ctx, new In(xInfo_.getID(), selectedIds));
            
            selectedSet.addAll(allBeans);
            
        }
        catch(Exception e)
        {
            new MajorLogMsg(this, e.getMessage(), e).log(ctx);
        }   
        
        for ( Iterator i = selectedSet.iterator(); i.hasNext();)
        {
            String value = toStringId(ctx, i.next()).replace('[', ' ').replace(']',' ').trim();

            hiddenField = hiddenField + value + ",";
        }

        out.print("<input type=\"hidden\" name=\"" + name + "_valueofsub" + "\" value=\"");
        out.print(hiddenField);
        out.print("\"/>");
    
        switch (mode)
        {
            case EDIT_MODE:
            case CREATE_MODE:
                out.print("<table border=\"0\">");
                out.print("<tr>");

                /** first part of the table, 
                         select field to display all elements in home 
                            but without those entries those are selected into
                            sublist. **/
                outPutWholeList(ctx, out, home, name, selectedSet);
            
                /** second part of the table **/
                outPutButtons(ctx, out,name);
            
                /** third part of the table, 
                    will accept selected result from first part **/ 
                outputSubList(ctx, out, home, name, selectedSet);
            
                /** end of the table */
                out.println("<tr>");
                out.println("</table>");    
        break;

        case DISPLAY_MODE:
        default:
            String delim = "";
            for (Iterator i = selectedSet.iterator(); i.hasNext(); )
            {
                out.print(delim);

                outputBean(ctx, out, i.next());

                delim = ",";
            }
        } // switch
        out.print("<table border=\"0\">");
        out.print("<tr><td>");
        out.print("<div id=\"");
        out.print(name + SEPERATOR + "descriptionDiv");
        out.print("\">");
        out.print(DescriptionEnabledMultiSelectWebControl.DESCRIPTION + " -- </div>");
        out.println("</td><tr>");
        out.println("</table>");  
        outputDescriptions(ctx, out, name);
    }
    
    protected void outputDescription(Context ctx, PrintWriter out, Object bean)
    {
        descriptionFormatter_.toWeb(
                                ctx,
                                out,
                                "",
                                bean);
    }
    
    protected void outPutButtons(Context ctx, PrintWriter out, String name )
    {
        out.println("<td>");
        out.println("<TABLE border=0>");
        out.println("<TR>");
        out.println("<TD align=center><INPUT type=button style=\"font-size: 9pt; color: #003565; width:150px\" name=\"AddAllRedknee" + name + "\" value=\"Add All ->\" onclick=\"document.getElementById('" + name + SEPERATOR + "descriptionDiv').innerHTML='" + DESCRIPTION + "--';addAllEntry(this.form, '" + name + "')\"></TD>");
        out.println("</TR>");
                        
        out.println("<TR>");
        out.println("<TD align=center><INPUT type=button style=\"font-size: 9pt; color: #003565 ;width:150px \" name=\"AddRedknee" + name + "\" value=\"Add     ->\" onclick=\"document.getElementById('" + name + SEPERATOR + "descriptionDiv').innerHTML='" + DESCRIPTION + "--';addEntry(this.form, '" + name + "')\"></TD>");
        out.println("</TR>");
                        
        out.println("<TR>");
        out.println("<TD align=center><INPUT type=button style=\"font-size: 9pt; color: #003565 ;width:150px \" name=\"RemoveRedknee" + name + "\" value=\"<-     Remove\" onclick=\"removeEntry(this.form, '" + name + "')\"></TD>");
        out.println("</TR>");

        out.println("<TR>");
        out.println("<TD align=center><INPUT type=button style=\"font-size: 9pt; color: #003565 ;width:150px\" name=\"RemoveAllRedknee" + name + "\" value=\"<- Remove All\" onclick=\"removeAllEntry(this.form, '" + name + "')\"></TD>");
        out.println("</TR>");
        out.println("</TABLE>");
        out.println("</td>");
    }
    
    protected void outPutWholeList(Context ctx, PrintWriter out, Home home, String name, Set selected)
    {
        String functionName = name.replace(SEPERATOR,"_");
        out.println("<script type=\"text/javascript\" language=\"JavaScript\"><!--");
        out.println("function fillInDescriptionFor" + functionName + "(name) { ");
        out.println("  var value = '" + DESCRIPTION + "--';");
        out.println("  if (document.getElementById(name).selectedIndex!=null && document.getElementById(name).selectedIndex>-1) {");
        out.println("    if (document.getElementById(name + '" + SEPERATOR + "' + document.getElementById(name).options[document.getElementById(name).selectedIndex].value)!=null) {");
        out.println("      value = '" + DESCRIPTION + "' + document.getElementById(name + '" + SEPERATOR + "' + document.getElementById(name).options[document.getElementById(name).selectedIndex].value).value;");
        out.println("    }");
        out.println("  }");
        out.println("  document.getElementById(name + '" + SEPERATOR + "descriptionDiv').innerHTML=value;");
        out.println("};");
        out.print("//--></script>");
        out.print("<td>");
        out.print("<table><tr><th>" + leftListTitle_ + "</th></tr><tr><td>");

        Home sortingHome = new SortingHome(home, getComparator());
        
        out.println("<select id=\"" + name + "\" onchange=\"fillInDescriptionFor" + functionName + "('" + name + "');\" name=\"" + name + "\" multiple size=" + getSize() + " width=\"250\">");
        try
        {
            for (Iterator i = sortingHome.selectAll(ctx).iterator(); i.hasNext();)
            {
                Object bean = i.next();
                String string_key = toStringId(ctx, bean);
                if (selected.contains(bean))
                {
                    continue;
                }
                out.print("<option");
                out.print(" value=\"");
                out.print(string_key.trim());
                out.print("\">");
                outputBean(ctx, out, bean);
                out.print("</option>");
            }
        }
        catch (HomeException e)
        {
            new MajorLogMsg(this, e.getMessage(), e).log(ctx);
        }
        out.print("</td></tr></table>");
        out.println("</select>");
        out.println("</td>");
    }  
    
    protected void outputDescriptions(Context ctx, PrintWriter out, String name)
    {
        Home home    = getHome(ctx);
        home = home.where(ctx, getSelectFilter());
        Home sortingHome = new SortingHome(home, getComparator());
        try
        {
            for (Iterator i = sortingHome.selectAll(ctx).iterator(); i.hasNext();)
            {
                Object bean = i.next();
                String string_key = toStringId(ctx, bean);
                out.print("<input type=\"hidden\" name=\"" + name + SEPERATOR + string_key.trim() + "\" id=\"" + name + SEPERATOR + string_key.trim() + "\" value=\"");
                outputDescription(ctx, out, bean);
                out.print("\" />");
            }
        }
        catch (HomeException e)
        {
            new MajorLogMsg(this, e.getMessage(), e).log(ctx);
        }
    }
        

}