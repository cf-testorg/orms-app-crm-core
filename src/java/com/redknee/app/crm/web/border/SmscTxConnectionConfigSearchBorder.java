/*
 * Created on Jul 7, 2005
 * 
 * 
 * Copyright (c) 1999-2005 REDKNEE.com. All Rights Reserved.
 * 
 * This software is the confidential and proprietary information of REDKNEE.com.
 * ("Confidential Information"). You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement you entered
 * into with REDKNEE.com.
 * 
 * REDKNEE.COM MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHCDR EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED
 * WARRANTIES OF MCDRCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, OR NON-INFRINGEMENT.
 * REDKNEE.COM SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFCDRED BY LICENSEE AS A RESULT OF
 * USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS DCDRIVATIVES.
 */
package com.redknee.app.crm.web.border;

import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.context.ContextAgentProxy;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.web.search.SearchBorder;
import com.redknee.framework.xhome.web.search.WildcardSelectSearchAgent;
import com.redknee.framework.xlog.log.DebugLogMsg;

import com.redknee.app.crm.bean.SmscTxConnectionConfig;
import com.redknee.app.crm.bean.SmscTxConnectionConfigXInfo;
import com.redknee.app.crm.bean.search.SmscTxConnectionConfigSearch;
import com.redknee.app.crm.bean.search.SmscTxConnectionConfigSearchWebControl;
import com.redknee.app.crm.bean.search.SmscTxConnectionConfigSearchXInfo;


/**
 * @author jke
 * 
 *  
 */
public class SmscTxConnectionConfigSearchBorder extends SearchBorder
{

    public SmscTxConnectionConfigSearchBorder(Context ctx)
    {
        super(ctx, SmscTxConnectionConfig.class, new SmscTxConnectionConfigSearchWebControl());
        // ID
        addAgent(new ContextAgentProxy()
        {

            @Override
            public void execute(Context ctx) throws AgentException
            {
                SmscTxConnectionConfigSearch criteria = (SmscTxConnectionConfigSearch) getCriteria(ctx);
                if (criteria.getID() > -1)
                {
                    doSelect(ctx, new EQ(SmscTxConnectionConfigXInfo.ID, Long.valueOf(criteria.getID())));
                }
                delegate(ctx);
            }
        });
        // HOST
        addAgent(new WildcardSelectSearchAgent(SmscTxConnectionConfigXInfo.SMSC_HOST,SmscTxConnectionConfigSearchXInfo.SMSC_HOST, true));
        
    }
}
