package com.redknee.app.crm.web.border;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.web.search.SearchBorder;
import com.redknee.framework.xhome.web.search.SelectSearchAgent;
import com.redknee.framework.xhome.web.search.WildcardSelectSearchAgent;

import com.redknee.app.crm.bean.search.BundleCategorySearchWebControl;
import com.redknee.app.crm.bean.search.BundleCategorySearchXInfo;
import com.redknee.app.crm.bundle.BundleCategory;
import com.redknee.app.crm.bundle.BundleCategoryXInfo;

public class BundleCategorySearchBorder
   extends SearchBorder
{
   public BundleCategorySearchBorder(Context ctx)
   {
      super(ctx, BundleCategory.class, new BundleCategorySearchWebControl());

      // ID
      addAgent(new SelectSearchAgent(BundleCategoryXInfo.CATEGORY_ID, BundleCategorySearchXInfo.ID).addIgnore(Integer.valueOf(-1)));
      
      // category name
      addAgent(new WildcardSelectSearchAgent(BundleCategoryXInfo.NAME, BundleCategorySearchXInfo.NAME, true));

      // SPID
      addAgent(new SelectSearchAgent(BundleCategoryXInfo.SPID, BundleCategorySearchXInfo.SPID).addIgnore(Integer.valueOf(9999)));
   }
}
