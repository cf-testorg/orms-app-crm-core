package com.redknee.app.crm.web.border;

import com.redknee.app.crm.bean.SupplementaryData;
import com.redknee.app.crm.bean.SupplementaryDataXInfo;
import com.redknee.app.crm.bean.search.SupplementaryDataSearchWebControl;
import com.redknee.app.crm.bean.search.SupplementaryDataSearchXInfo;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.web.search.SearchBorder;
import com.redknee.framework.xhome.web.search.SelectSearchAgent;
import com.redknee.framework.xhome.web.search.WildcardSelectSearchAgent;

/**
 * Search border for SupplementaryData objects.
 * 
 * @author Marcio Marques
 * @since 9.1.3
 */
public class SupplementaryDataSearchBorder  extends SearchBorder
{
    public SupplementaryDataSearchBorder(Context ctx)
    {
        super(ctx, SupplementaryData.class, new SupplementaryDataSearchWebControl());
        
        addAgent(new SelectSearchAgent(SupplementaryDataXInfo.ENTITY, SupplementaryDataSearchXInfo.ENTITY).addIgnore(Integer.valueOf(-1)));

        addAgent(new SelectSearchAgent(SupplementaryDataXInfo.IDENTIFIER, SupplementaryDataSearchXInfo.IDENTIFIER));

        addAgent(new WildcardSelectSearchAgent(SupplementaryDataXInfo.KEY, SupplementaryDataSearchXInfo.KEY, true));

    }

}
