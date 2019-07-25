/*
 *  PrePostWildcardSelectSearchAgent
 *
 *  Author : Kevin Greer
 *  Date   : Oct 16, 2003
 *  
 *  Copyright (c) 2003, Redknee
 *  All rights reserved.
 */
 
package com.redknee.app.crm.web.search;

import com.redknee.framework.xhome.beans.xi.PropertyInfo;
import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.Contains;
import com.redknee.framework.xhome.elang.ContainsIC;
import com.redknee.framework.xhome.web.search.SearchBorder;
import com.redknee.framework.xhome.web.search.WildcardSelectSearchAgent;


/**
 * A Search Agent for performing pre- and post- matching on String fields.
 *
 * @author     Candy Wong
 **/
public class PrePostWildcardSelectSearchAgent
   extends WildcardSelectSearchAgent
{
   public PrePostWildcardSelectSearchAgent(PropertyInfo pInfo, PropertyInfo searchInfo)
   {
      super(pInfo, searchInfo);
   }

   public PrePostWildcardSelectSearchAgent(PropertyInfo pInfo, PropertyInfo searchInfo, boolean ignoreCase)
   {
      super(pInfo, searchInfo, ignoreCase);
   }
   
   
   @Override
public void execute(Context ctx)
      throws AgentException
   {
      final Object criteria = getSearchCriteria(ctx);

      String value = cleanupString(String.valueOf(criteria));
      
      if ( ! "".equals(value) )
      {
          if (getIgnoreCase())
          {
              SearchBorder.doSelect(ctx, new ContainsIC(getPropertyInfo(), value));
          }
          else
          {
              SearchBorder.doSelect(ctx, new Contains(getPropertyInfo(), value));
          }
      }
      
      delegate(ctx);
   }
   
   private String cleanupString(String value)
   {
      value = value.trim();
      
      // Trim trailing *'s
      while ( value.endsWith("*") ) value = value.substring(0, value.length()-1);
      
      int i = 0;
      
      // Remove %'s
      while ( ( i = value.indexOf('%') ) != -1 ) value = value.substring(0, i) + value.substring(i+1);
      
      // Remove "'"'s
      while ( ( i = value.indexOf('\'') ) != -1 ) value = value.substring(0, i) + value.substring(i+1);

      return value;
   }

}
