package com.redknee.app.crm.web.border;

import com.redknee.app.crm.bean.externalapp.ExternalAppErrorCodeMsg;
import com.redknee.app.crm.bean.externalapp.ExternalAppErrorCodeMsgXInfo;
import com.redknee.app.crm.bean.search.ExternalAppErrorCodeMsgSearchWebControl;
import com.redknee.app.crm.bean.search.ExternalAppErrorCodeMsgSearchXInfo;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.web.search.SearchBorder;
import com.redknee.framework.xhome.web.search.SelectSearchAgent;

/**
 * Search border for SupplementaryData objects.
 * 
 * @author Marcio Marques
 * @since 9.1.3
 */
public class ExternalAppErrorCodeMsgSearchBorder  extends SearchBorder
{
    public ExternalAppErrorCodeMsgSearchBorder(Context ctx)
    {
        super(ctx, ExternalAppErrorCodeMsg.class, new ExternalAppErrorCodeMsgSearchWebControl());
        
        addAgent(new SelectSearchAgent(ExternalAppErrorCodeMsgXInfo.EXTERNAL_APP, ExternalAppErrorCodeMsgSearchXInfo.EXTERNAL_APP).addIgnore(Integer.valueOf(-1)));

        addAgent(new SelectSearchAgent(ExternalAppErrorCodeMsgXInfo.ERROR_CODE, ExternalAppErrorCodeMsgSearchXInfo.ERROR_CODE).addIgnore(Integer.valueOf(-9999)));

        addAgent(new SelectSearchAgent(ExternalAppErrorCodeMsgXInfo.LANGUAGE, ExternalAppErrorCodeMsgSearchXInfo.LANGUAGE).addIgnore(Integer.valueOf(-1)));
    }

}
