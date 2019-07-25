/*
 * This code is a protected work and subject to domestic and international copyright
 * law(s). A complete listing of authors of this work is readily available. Additionally,
 * source code is, by its very nature, confidential information and inextricably contains
 * trade secrets and other information proprietary, valuable and sensitive to Redknee. No
 * unauthorized use, disclosure, manipulation or otherwise is permitted, and may only be
 * used in accordance with the terms of the license agreement entered into with Redknee
 * Inc. and/or its subsidiaries.
 * 
 * Copyright (c) Redknee Inc. and its subsidiaries. All Rights Reserved.
 */
package com.redknee.app.crm.xhome.adapter;

import com.redknee.app.crm.bean.Note;
import com.redknee.app.crm.bean.SystemNoteSubTypeEnum;
import com.redknee.app.crm.bean.SystemNoteTypeEnum;
import com.redknee.app.crm.contract.SubscriptionContractTerm;
import com.redknee.app.crm.contract.SubscriptionContractTermXInfo;
import com.redknee.app.crm.support.HomeSupportHelper;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.home.Adapter;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.language.MessageMgr;
import com.redknee.framework.xlog.log.MinorLogMsg;


/**
 * 
 * For multi langauge support, translating the notes to apppropriate language
 * Currently,this only supported for invoiceable notes Dates and currency are still
 * limitation
 */
public class SubscriberNoteTranslationAdapter implements Adapter
{

    public Object adapt(Context ctx, Object obj) throws HomeException
    {
        Note note = (Note) obj;
        // Only support invoicealbe notes
        if (note.isShowOnInvoice())
        {
            String noteMsg = getSubscriptionContractUpdateNotes(ctx,note);
            note.setNote(noteMsg);
        }
        return note;
    }


    @Override
    public Object unAdapt(Context ctx, Object obj) throws HomeException
    {
        return obj;
    }


    private String getSubscriptionContractUpdateNotes(final Context ctx, final Note note)
    {
        String result = note.getNote();
        // Current only support contract updates
        if (note.getType().equals(SystemNoteTypeEnum.CONTRACT_UPDATES.getDescription()))
        {
            MessageMgr mmgr = new MessageMgr(ctx, this);
            if (note.getSubType().equals(SystemNoteSubTypeEnum.CONTRACT_EXPIRE_WARN.getDescription()))
            {
                String params[] = parseCsv(note.getNote());
                result = mmgr.get(SUBSCRIPTION_CONTRACT_EXPIRY_NOTE_KEY, "{0} contract will expire on {1} (within a month).",
                        params);
            }
            else if (note.getSubType().equals(SystemNoteSubTypeEnum.CONTRACT_ADD.getDescription()))
            {
                String params[] = parseCsv(note.getNote());
                if (params != null && params.length > 1)
                {
                    try
                    {
                        SubscriptionContractTerm term = HomeSupportHelper.get(ctx).findBean(ctx,
                                SubscriptionContractTerm.class, new EQ(SubscriptionContractTermXInfo.ID, Long.valueOf(params[1])));
                        if ( term != null)
                        {
                            params[1] = term.getContractPolicySummary();
                        }
                    }
                    catch (HomeException homeEx)
                    {
                        new MinorLogMsg(SubscriberNoteTranslationAdapter.class,
                                "Unable to find subscription contract term => " + params[1], homeEx).log(ctx);
                    }
                }
                result = mmgr.get(SUBSCRIPTION_CONTRACT_ENROLL_NOTE_KEY,
                        "The Subscription has enrolled in the {0}. {1}", params);
            }
            else if (note.getSubType().equals(SystemNoteSubTypeEnum.CONTRACT_REMOVE.getDescription()))
            {
                result = mmgr.get(SUBSCRIPTION_CONTRACT_REMOVE_NOTE_KEY, "Terminated from {0} contract.", new String[]
                    {note.getNote()});
            }
            else if (note.getSubType().equals(SystemNoteSubTypeEnum.CONTRACT_BONUS.getDescription()))
            {
                result = mmgr.get(SUBSCRIPTION_CONTRACT_BONUS_NOTE_KEY, "{1} contract's bonus applied", new String[]
                    {note.getNote()});
            }
        }
        return result;
    }


    private String[] parseCsv(String note)
    {
        if (note != null)
        {
            return note.split(",");
        }
        return null;
    }

    public static final String SUBSCRIPTION_CONTRACT_EXPIRY_NOTE_KEY = "SubscriptionContract.ExpiryNoteMsg";
    public static final String SUBSCRIPTION_CONTRACT_ENROLL_NOTE_KEY = "SubscriptionContract.EnrollmentNoteMsg";
    public static final String SUBSCRIPTION_CONTRACT_REMOVE_NOTE_KEY  = "SubscriptionContract.TerminationNoteMsg";
    public static final String SUBSCRIPTION_CONTRACT_BONUS_NOTE_KEY = "SubscriptionContract.BonusNoteMsg";
}