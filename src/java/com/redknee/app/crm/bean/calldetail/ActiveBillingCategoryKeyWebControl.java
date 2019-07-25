/**
 * Copyright (c) 2010 Redknee, Inc. and its subsidiaries. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of REDKNEE.
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with REDKNEE.
 *
 * REDKNEE MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, OR
 * NON-INFRINGEMENT. REDKNEE SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY
 * LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR
 * ITS DERIVATIVES.
 */
 
package com.redknee.app.crm.bean.calldetail;

import java.util.*;

import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.beans.XBeans;
import com.redknee.framework.xhome.beans.ExceptionListener;
import com.redknee.framework.xhome.context.*;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.msp.SpidAware;
import com.redknee.framework.xhome.visitor.MapVisitor;
import com.redknee.framework.xhome.webcontrol.*;
import com.redknee.framework.xhome.web.agent.WebAgents;
import com.redknee.framework.xhome.web.support.*;
import com.redknee.framework.xhome.support.*;
import com.redknee.framework.xlog.log.MinorLogMsg;

import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

/**
 * Custom web control that filters collection of (Active) Billing Categories
 **/

public class ActiveBillingCategoryKeyWebControl extends BillingCategoryKeyWebControl
{

  public ActiveBillingCategoryKeyWebControl()
  {
  }

  public ActiveBillingCategoryKeyWebControl(boolean autoPreview)
  {
      super(autoPreview);
  }

  public ActiveBillingCategoryKeyWebControl(int listSize)
  {
      super(listSize);
  }

  public ActiveBillingCategoryKeyWebControl(int listSize, boolean autoPreview)
  {
      super(listSize, autoPreview);
  }

  public ActiveBillingCategoryKeyWebControl(int listSize, boolean autoPreview, Class baseClass,String sourceField,String targetField)
  {
      super(listSize, autoPreview, baseClass,sourceField,targetField);
  }

  public ActiveBillingCategoryKeyWebControl(int listSize, boolean autoPreview, boolean isOptional)
  {
      super(listSize, autoPreview, isOptional);
  }

  public ActiveBillingCategoryKeyWebControl(int listSize, boolean autoPreview, boolean isOptional, boolean allowCustom)
  {
      super(listSize, autoPreview, isOptional, allowCustom);
  }

  public ActiveBillingCategoryKeyWebControl(int listSize, boolean autoPreview, Object optionalValue)
  {
      super(listSize, autoPreview, optionalValue);
  }

  public ActiveBillingCategoryKeyWebControl(int listSize, boolean autoPreview, Object optionalValue, Class baseClass,String sourceField,String targetField)
  {
      super(listSize, autoPreview, optionalValue,baseClass,sourceField,targetField);
  }

  public ActiveBillingCategoryKeyWebControl(int listSize, boolean autoPreview, Object optionalValue, boolean allowCustom)
  {
      super(listSize, autoPreview, optionalValue, allowCustom);
  }

  /**
   * Overrides to filter out the Active billing categories 
   * @param ctx
   * @return
   */
  @Override
  public Home getHome(Context ctx)
  {
      Home home = (Home) ctx.get(getHomeKey());      
      return home.where(ctx, new EQ(BillingCategoryXInfo.STATUS, BillingCategoryStateEnum.ACTIVE));
  }

}


