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
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import com.redknee.app.crm.filter.FilteredMultiSelectPredicate;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.support.IdentitySupport;
import com.redknee.framework.xhome.webcontrol.AbstractKeyWebControl;
import com.redknee.framework.xhome.webcontrol.JavascriptFactory;
import com.redknee.framework.xhome.webcontrol.KeyWebControl;
import com.redknee.framework.xhome.webcontrol.MultiSelectWebControl;
import com.redknee.framework.xhome.webcontrol.OutputWebControl;
import com.redknee.framework.xhome.webcontrol.WebControl;
import com.redknee.framework.xhome.webcontrol.tree.KeyTreeWebControl;
import com.redknee.framework.xlog.log.MajorLogMsg;

/**
 * A MultiSelect web control in which the left list box values are filtered
 * based on the selected value in the drop down menu over it and a given predicate.
 * @author Marcio Marques
 *
 */
public class FilteredMultiSelectWebControl extends MultiSelectWebControl
{
    private class FilteredMultiSelectDropDownOnChangeJavascriptFactory implements JavascriptFactory
    {
        private String name_;
        private Class homeKey_;
        private Class predicate_;
        private Class formatter_;
        
        public FilteredMultiSelectDropDownOnChangeJavascriptFactory(String name, Class homeKey, Class predicate, Class formatter)
        {
            this.name_ = name;
            this.homeKey_ = homeKey;
            this.predicate_ = predicate;
            this.formatter_ = formatter;
        }
        
        public String generate (Context ctx, String name, Object obj)
        {
            String functionName = name_.replace(SEPERATOR,"_");
            return "filterMultiSelect" + functionName + "(this.form, '" + name_ + "', '" + homeKey_.getName() + "', '" + predicate_.getName() + "', '" + formatter_.getName()  +"');";
        }
    }
    
    private WebControl filterWebControl_ = null;
    private FilteredMultiSelectPredicate predicate_ = null;
    
    /**
     * 
     * @param homeKey Home key class.
     * @param idSupport Identify support for the key class.
     * @param filterWebControl Key web control whose selected value is used to filter the values in the multi select web control.
     * @param formatter  Output web control used to format the bean data for display in the listbox.
     * @param predicate Predicate that will filter the values in the listbox based on the already selected values and on the filterWebControl selected value.
     */
    public FilteredMultiSelectWebControl(Class homeKey, IdentitySupport idSupport, KeyWebControl filterWebControl, OutputWebControl formatter, FilteredMultiSelectPredicate predicate)
    {
        super(homeKey, idSupport, formatter);
        filterWebControl_ = filterWebControl;
        predicate_ = predicate;
    }

    /**
     * 
     * @param homeKey Home key class.
     * @param idSupport Identify support for the key class.
     * @param filterWebControl Key tree web control whose selected value is used to filter the values in the multi select web control.
     * @param formatter  Output web control used to format the bean data for display in the listbox.
     * @param predicate Predicate that will filter the values in the listbox based on the already selected values and on the filterWebControl selected value.
     */    
    public FilteredMultiSelectWebControl(Class homeKey, IdentitySupport idSupport, KeyTreeWebControl filterWebControl, OutputWebControl formatter, FilteredMultiSelectPredicate predicate)
    {
        super(homeKey, idSupport, formatter);
        filterWebControl_ = filterWebControl;
        predicate_ = predicate;
    }

    public void toWeb(Context ctx, PrintWriter out, String name, Object obj)
    {
        String functionName = name.replace(SEPERATOR,"_");
        int mode     = ctx.getInt("MODE", DISPLAY_MODE);
        Home home    = getHome(ctx);
        home = home.where(ctx, getSelectFilter());
     
        Set set     = (Set) obj;
        Set selectedSet = new TreeSet(getComparator());

        String hiddenField = "";
        
        for ( Iterator i = set.iterator() ; i.hasNext() ; )
        {
            String  string_key  = i.next().toString();
            try
            {
                Object bean = home.find(ctx, fromStringId(ctx, string_key));
                if ( bean != null)
                {
                    selectedSet.add(bean);
                }
            }
            catch(Exception e)
            {
                new MajorLogMsg(this, e.getMessage(), e).log(ctx);
            }   
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
                FilteredMultiSelectDropDownOnChangeJavascriptFactory javascriptFactory = new FilteredMultiSelectDropDownOnChangeJavascriptFactory(name, (Class) getHomeKey(), predicate_.getClass(), getFormatter().getClass());
                
                if (filterWebControl_ instanceof AbstractKeyWebControl)
                {
                    ((AbstractKeyWebControl) filterWebControl_).setJavascriptForOnChange(javascriptFactory);
                }
                else 
                {
                    ((KeyTreeWebControl) filterWebControl_).setJavascriptForOnChange(javascriptFactory);
                }
                
                outputJavascript(ctx, out, name, javascriptFactory);
                out.print("<table border=\"0\">");
                out.print("<tr>");
                out.print("<td colspan=3>");
                
                // Output filter web control and button to filter after selection.
                filterWebControl_.toWeb(ctx, out, name + SEPERATOR + "filter", obj);
                
                if (javascriptFactory==null)
                {
                    out.println("<INPUT type=button style=\"font-size: 9pt; color: #003565; width:150px\" name=\"FilterMultiSelectRedknee" + name + "\" value=\"Filter\" onclick=\"filterMultiSelect" + functionName + "(this.form, '" + name + "', '" + ((Class) getHomeKey()).getName() + "', '" + predicate_.getClass().getName() + "', '" + getFormatter().getClass().getName()  +"')\">");
                }
                
                out.print("</td>");
                out.print("</tr>");
                out.print("<tr>");

                /** first part of the table, 
                         select field to display all elements in home 
                            but without those entries those are selected into
                            sublist. **/
                outputWholeList(ctx, out, home, name, selectedSet);
            
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
    }
    
    private void outputJavascript(Context ctx, PrintWriter out, String name, JavascriptFactory javascriptFactory)
    {

        String functionName = name.replace(SEPERATOR,"_");
        out.println("<script type=\"text/javascript\" language=\"JavaScript\"><!--");
        out.println("var wholeListMultiSelect" + functionName + ";");

        out.println("function filterMultiSelect" + functionName + "(form , eleName, classKey, predicate, outputWC)");
        out.println("{");
        out.println("    var dropDownMenu;");
        out.println("    var subList;");
        out.println("    var subList_value ;");

        out.println("    var counter = 0;");

        out.println("    var form_num = 0;");
        out.println("    for ( var j = 0; j < document.forms.length; j++)");
        out.println("    {");
        out.println("        if ( document.forms[j] == form )");
        out.println("        {");
        out.println("            form_num = j;");
        out.println("            break;");
        out.println("        }");
        out.println("    }");

        out.println("    for ( var i = 0 ; i < document.forms[form_num].elements.length; i++)");
        out.println("    {");
        out.println("        if ( document.forms[form_num].elements[i].name == (\"AddAllRedknee\" + eleName ))");
        out.println("        {");
        out.println("            wholeListMultiSelect" + functionName + "      = document.forms[form_num].elements[i-1];");
        out.println("            dropDownMenu   = document.forms[form_num].elements[i-2];");
        out.println("            subList_value  = document.forms[form_num].elements[i-3];");
        out.println("            break;");
        out.println("        }");
        out.println("    }");

        out.println("    var url = 'home';");

        out.println("    var pars = 'cmd=retrieveData&homeKey=' + classKey + '&predicate=' + predicate + '"  +
        "&parameters='+dropDownMenu.value + ';' + subList_value.value + '&comparator=&outputWC='+ outputWC + '&border=hide&menu=hide&header=hide';");
        //pars=encodeURIComponent(pars);
        out.println("    var myAjax = new Ajax.Request( url, { method: 'get', parameters: pars, onComplete: reloadList" + functionName +", onFailure: showResponseError });");
        out.println("};");

        out.println("function reloadList" + functionName + "(originalRequest,object)");
        out.println("{");
        out.println("    for (var i=(wholeListMultiSelect" + functionName + ".options.length-1); i>=0; i--)");
        out.println("    {");
        out.println("        wholeListMultiSelect" + functionName + ".options[i] = null;");
        out.println("    }");
        out.println("    var response = originalRequest.responseText;");
        out.println("    if (response != '')");
        out.println("    {");
        out.println("        var values=response.split(','); ");
        out.println("        for (i=0;i<values.length;i++)");
        out.println("        {");
        out.println("          var newOption=document.createElement('OPTION');");
        out.println("          var value = values[i].split(':');");
        out.println("          newOption.value=value[0];");
        out.println("          newOption.text=value[1];");
        out.println("          wholeListMultiSelect" + functionName + ".options.add(newOption);");
        out.println("       }");
        out.println("    }");
        out.println("};");
        
        out.println("function addAllEntryFiltered" + functionName + "( form , eleName)");
        out.println("{");
        out.println("    var wholeList;");
        out.println("    var subList;");
        out.println("    var subList_value ;");

        out.println("    var subList_valueValue='' ;");
        out.println("    var counter = 0;");

        out.println("    var isNav = (navigator.appName.indexOf('Netscape') != -1) ? true : false;");
        out.println("    var isIE  = (navigator.appName.indexOf('Microsoft') != -1) ? true : false;");

        out.println("    var form_num = 0;");
        out.println("    for ( var j = 0; j < document.forms.length; j++)");
        out.println("    {");
        out.println("        if ( document.forms[j] == form )");
        out.println("        {");
        out.println("            form_num = j;");
        out.println("            break;");
        out.println("        }");
        out.println("     }");

        out.println("    for ( var i = 0 ; i < document.forms[form_num].elements.length; i++)");
        out.println("    {");
        out.println("        if ( document.forms[form_num].elements[i].name == ('AddAllRedknee' + eleName ))");
        out.println("        {");
        out.println("            wholeList      = document.forms[form_num].elements[i-1];");
        out.println("            subList        = document.forms[form_num].elements[i+4];");
        out.println("            subList_value  = document.forms[form_num].elements[i-3];");

        out.println("            break;");
        out.println("        }");
        out.println("    }");

        out.println("    var sub_len = subList.length;");

        out.println("    for ( var k = 0; k < wholeList.length; k++)");
        out.println("    {");
        out.println("        subList.options[sub_len + k ] = new Option(wholeList.options[k].text, wholeList.options[k].value );");
        out.println("    }");

        out.println("    for (var i=(wholeList.options.length-1); i>=0; i--)");
        out.println("    {");
        out.println("        wholeList.options[i] = null;");
        out.println("    }");

        out.println("    for ( var j =0; j < subList.length; j++)");
        out.println("    {");
        out.println("        subList_valueValue += subList.options[j].value + ',';");
        out.println("    }");

        out.println("    subList_value.value = subList_valueValue;");
        out.println("}");
        
        out.println("function addEntryFiltered" + functionName + "( form , eleName)");
        out.println("{");
        out.println("    var wholeList;");
        out.println("    var subList;");
        out.println("    var subList_value ;");

        out.println("    var subList_valueValue='' ;");
        out.println("    var counter = 0;");

        out.println("    var isNav = (navigator.appName.indexOf('Netscape') != -1) ? true : false;");
        out.println("    var isIE  = (navigator.appName.indexOf('Microsoft') != -1) ? true : false;");

        out.println("    var form_num = 0;");
        out.println("    for ( var j = 0; j < document.forms.length; j++)");
        out.println("    {");
        out.println("        if ( document.forms[j] == form )");
        out.println("        {");
        out.println("            form_num = j;");
        out.println("            break;");
        out.println("        }");
        out.println("     }");

        out.println("    for ( var i = 0 ; i < document.forms[form_num].elements.length; i++)");
        out.println("    {");
        out.println("        if ( document.forms[form_num].elements[i].name == ('AddAllRedknee' + eleName ))");
        out.println("        {");
        out.println("            wholeList      = document.forms[form_num].elements[i-1];");
        out.println("            subList        = document.forms[form_num].elements[i+4];");
        out.println("            subList_value  = document.forms[form_num].elements[i-3];");

        out.println("            break;");
        out.println("        }");
        out.println("    }");

        out.println("    if ( wholeList.selectedIndex != -1)");
        out.println("    {");
        out.println("      for ( i = 0; i < wholeList.options.length; i++ )");
        out.println("      {");
        out.println("          if ( wholeList.options[i].selected )  // check each option on whole list");
        out.println("          {");
        out.println("             if ( subList.length == 0)");
        out.println("             {");
        out.println("                  subList.options[0]");
        out.println("                            = new Option(wholeList.options[i].text,");
        out.println("                                         wholeList.options[i].value );");
        out.println("             }");
        out.println("             else");
        out.println("             {");
        out.println("                    var existing = false;");

        out.println("                    for ( j=0; j < subList.length; j++)   // check if on sub list already");
        out.println("                    {");
        out.println("                        if ( subList.options[j].text == wholeList.options[i].text )");
        out.println("                        {");
        out.println("                            existing = true;");
        out.println("                        }");
        out.println("                     }  // for");

        out.println("                     if ( !existing)");
        out.println("                     {");
        out.println("                          subList.options[subList.length]");
        out.println("                                = new Option(wholeList.options[i].text, wholeList.options[i].value );");

        out.println("                     }");
        out.println("              }  // else");
        out.println("          } // if");
        out.println("      } // for");

        out.println("      for ( i =  wholeList.options.length-1;i>=0; i-- )");
        out.println("      {");
        out.println("          if ( wholeList.options[i].selected )  // check each option on whole list");
        out.println("          {");
        out.println("            wholeList.remove(wholeList.selectedIndex);");
        out.println("          }");
        out.println("      }// for");
        out.println("    } // if");

        out.println("    for ( var j =0; j < subList.length; j++)");
        out.println("    {");
        out.println("        subList_valueValue += subList.options[j].value + ',';");
        out.println("    }");

        out.println("  subList_value.value = subList_valueValue;");
        out.println("}");
        
        out.println("function removeEntryFiltered" + functionName + "( form , eleName)");
        out.println("{");
        out.println("    var wholeList;");
        out.println("    var subList;");
        out.println("    var subList_value ;");
//        out.println("    var filterButton;");

        out.println("    var subList_valueValue='' ;");
        out.println("    var counter = 0;");

        out.println("    var isNav = (navigator.appName.indexOf('Netscape') != -1) ? true : false;");
        out.println("    var isIE  = (navigator.appName.indexOf('Microsoft') != -1) ? true : false;");

        out.println("    var form_num = 0;");
        out.println("    for ( var j = 0; j < document.forms.length; j++)");
        out.println("    {");
        out.println("        if ( document.forms[j] == form )");
        out.println("        {");
        out.println("            form_num = j;");
        out.println("            break;");
        out.println("        }");
        out.println("     }");

        out.println("    for ( var i = 0 ; i < document.forms[form_num].elements.length; i++)");
        out.println("    {");
        out.println("        if ( document.forms[form_num].elements[i].name == ('AddAllRedknee' + eleName ))");
        out.println("        {");
        out.println("            wholeList      = document.forms[form_num].elements[i-1];");
        out.println("            subList        = document.forms[form_num].elements[i+4];");
        out.println("            subList_value  = document.forms[form_num].elements[i-3];");
//        out.println("            filterButton   = document.forms[form_num].elements[i-2];");

        out.println("            break;");
        out.println("        }");
        out.println("    }");

            // reset hidden value
        out.println("    subList_valueValue = '';");

        out.println("    if (subList.selectedIndex != -1)");
        out.println("    {");
        out.println("        for ( var l = 0; l < subList.length; l++ )");
        out.println("        {");
        out.println("            if ( subList.options[l].value == subList.options[subList.selectedIndex].value )");
        out.println("                continue;");

        out.println("            subList_valueValue += subList.options[l].value + ',';");
        out.println("        }");

        out.println("        for ( var j = 0; j < subList.length; j++ )");
        out.println("        {");
        out.println("            if ( subList.options[j].value == subList.options[subList.selectedIndex].value )");
        out.println("            {");
        out.println("                subList.options[subList.selectedIndex] = null;");
        out.println("                break;");
        out.println("            }");
        out.println("        }");
        out.println("        subList_value.value = subList_valueValue;");
        out.println("        " + javascriptFactory.generate(ctx, name, this));
        out.println("    }");

        out.println("}");
        
        out.println("function removeAllEntryFiltered" + functionName + "( form , eleName)");
        out.println("{");
        out.println("    var wholeList;");
        out.println("    var subList;");
        out.println("    var subList_value ;");

        out.println("    var subList_valueValue='' ;");
        out.println("    var counter = 0;");

        out.println("    var isNav = (navigator.appName.indexOf('Netscape') != -1) ? true : false;");
        out.println("    var isIE  = (navigator.appName.indexOf('Microsoft') != -1) ? true : false;");

        out.println("    var form_num = 0;");
        out.println("    for ( var j = 0; j < document.forms.length; j++)");
        out.println("    {");
        out.println("        if ( document.forms[j] == form )");
        out.println("        {");
        out.println("            form_num = j;");
        out.println("            break;");
        out.println("        }");
        out.println("     }");

        out.println("    for ( var i = 0 ; i < document.forms[form_num].elements.length; i++)");
        out.println("    {");
        out.println("        if ( document.forms[form_num].elements[i].name == ('AddAllRedknee' + eleName ))");
        out.println("        {");
        out.println("            wholeList      = document.forms[form_num].elements[i-1];");
        out.println("            subList        = document.forms[form_num].elements[i+4];");
        out.println("            subList_value  = document.forms[form_num].elements[i-3];");

        out.println("            break;");
        out.println("        }");
        out.println("    }");

            // reset hidden value

        out.println("    for ( var j = subList.length; j > -1  ; j-- )");
        out.println("    {");
        out.println("        subList.options[j] = null;");
        out.println("    }");

        out.println("    subList_valueValue = '';");

        out.println("    subList_value.value = subList_valueValue;");

        out.println("    " + javascriptFactory.generate(ctx, name, this));

        out.println("}");
        
        out.print("//--></script>");        
    }
    
    protected void outPutButtons(Context ctx, PrintWriter out, String name )
    {
        String functionName = name.replace(SEPERATOR,"_");
        out.println("<td>");
        out.println("<TABLE border=0>");
        out.println("<TR>");
        out.println("<TD align=center><INPUT type=button style=\"font-size: 9pt; color: #003565; width:150px\" name=\"AddAllRedknee" + name + "\" value=\"Add All ->\" onclick=\"addAllEntryFiltered" + functionName + "(this.form, '" + name + "')\"></TD>");
        out.println("</TR>");
                        
        out.println("<TR>");
        out.println("<TD align=center><INPUT type=button style=\"font-size: 9pt; color: #003565 ;width:150px \" name=\"AddRedknee" + name + "\" value=\"Add     ->\" onclick=\"addEntryFiltered" + functionName + "(this.form, '" + name + "')\"></TD>");
        out.println("</TR>");
                        
        out.println("<TR>");
        out.println("<TD align=center><INPUT type=button style=\"font-size: 9pt; color: #003565 ;width:150px \" name=\"RemoveRedknee" + name + "\" value=\"<-     Remove\" onclick=\"removeEntryFiltered" + functionName + "(this.form, '" + name + "')\"></TD>");
        out.println("</TR>");

        out.println("<TR>");
        out.println("<TD align=center><INPUT type=button style=\"font-size: 9pt; color: #003565 ;width:150px\" name=\"RemoveAllRedknee" + name + "\" value=\"<- Remove All\" onclick=\"removeAllEntryFiltered" + functionName + "(this.form, '" + name + "')\"></TD>");
        out.println("</TR>");
        out.println("</TABLE>");
        out.println("</td>");
    }
    
    @Override
    public Home getHome(Context ctx)
    {
        Home home = (Home) super.getHome(ctx);
        return home.where(ctx, predicate_);
    }
    
    
}