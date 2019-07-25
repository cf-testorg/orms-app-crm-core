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
package com.redknee.app.crm.web.border;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.web.search.FindSearchAgent;
import com.redknee.framework.xhome.web.search.SearchBorder;
import com.redknee.framework.xhome.web.search.SelectSearchAgent;

import com.redknee.app.crm.bean.AuxiliaryService;
import com.redknee.app.crm.bean.search.AuxiliaryServiceSearch;
import com.redknee.app.crm.bean.search.AuxiliaryServiceSearchWebControl;
import com.redknee.app.crm.bean.search.AuxiliaryServiceSearchXInfo;
import com.redknee.app.crm.bean.AuxiliaryServiceXInfo;

/**
 * Provides search functionality for the auxiliary services.
 *
 * @author candy.wong@redknee.com
 */
public class AuxiliaryServiceSearchBorder extends SearchBorder
{
    /**
     * Creates a new AuxiliaryServiceSearchBorder.
     *
     * @param context The operating context.
     */
    public AuxiliaryServiceSearchBorder(final Context context)
    {
        super(context, AuxiliaryService.class, new AuxiliaryServiceSearchWebControl());

        // IDENTIFIER
        final FindSearchAgent idAgent;
        idAgent = new FindSearchAgent(AuxiliaryServiceXInfo.IDENTIFIER, AuxiliaryServiceSearchXInfo.IDENTIFIER);
        addAgent(idAgent.addIgnore(Long.valueOf(AuxiliaryServiceSearch.DEFAULT_IDENTIFIER)));

        // TYPE
        final SelectSearchAgent typeAgent;
        typeAgent = new SelectSearchAgent(AuxiliaryServiceXInfo.TYPE, AuxiliaryServiceSearchXInfo.TYPE);
        addAgent(typeAgent.addIgnore(null));

        // SPID
        final SelectSearchAgent spidAgent;
        spidAgent = new SelectSearchAgent(AuxiliaryServiceXInfo.SPID, AuxiliaryServiceSearchXInfo.SPID);
        addAgent(spidAgent.addIgnore(Integer.valueOf(AuxiliaryServiceSearch.DEFAULT_SPID)));
    }

} // class
