package com.redknee.app.crm.web.border;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.web.search.SearchBorder;
import com.redknee.framework.xhome.web.search.SelectSearchAgent;
import com.redknee.framework.xhome.web.search.WildcardSelectSearchAgent;

import com.redknee.app.crm.bean.MsisdnZonePrefix;
import com.redknee.app.crm.bean.MsisdnZonePrefixHome;
import com.redknee.app.crm.bean.MsisdnZonePrefixXInfo;
import com.redknee.app.crm.bean.search.MsisdnZonePrefixSearchWebControl;
import com.redknee.app.crm.bean.search.MsisdnZonePrefixSearchXInfo;

/**
 * @author psperneac
 * @since May 4, 2005 3:30:46 PM
 */
public class MsisdnZonePrefixSearchBoder extends SearchBorder
{
    public MsisdnZonePrefixSearchBoder(Context ctx)
    {
        super(ctx, MsisdnZonePrefixHome.class, MsisdnZonePrefix.class, new MsisdnZonePrefixSearchWebControl());
        
        // Call Type gl Code
        addAgent(new SelectSearchAgent(MsisdnZonePrefixXInfo.IDENTIFIER, MsisdnZonePrefixSearchXInfo.IDENTIFIER).addIgnore(new Long(-1)));
            
        // Call Type invoice description
        addAgent(new WildcardSelectSearchAgent(MsisdnZonePrefixXInfo.DESCRIPTION, MsisdnZonePrefixSearchXInfo.DESCRIPTION, true));
    }
}
