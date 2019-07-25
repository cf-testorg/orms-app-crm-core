/*
 * Created on Mar 14, 2005
 *
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
package com.redknee.app.crm.bean.core;

import com.redknee.app.crm.bean.UsageReport;
import com.redknee.app.crm.bean.calldetail.BillingCategoryEnum;
import com.redknee.app.crm.bean.calldetail.CallTypeEnum;

public class CallDetailSummary extends com.redknee.app.crm.bean.calldetail.CallDetailSummary implements  com.redknee.app.crm.bas.promotion.summary.Summary
{
	public void extractTo(Object obj)
	{
		if (!(obj instanceof UsageReport))
		{
			return;
		}
		long usage;
		UsageReport report = (UsageReport)obj;
		switch (getCallType().getIndex())
		{
			case (CallTypeEnum.ORIG_INDEX):
			usage = getDuration().getTime()/1000;     //in sec
			switch (getBillingCategory())
			{
				case (BillingCategoryEnum.DOMESTIC_INDEX):
					report.setDomVoiceMO(report.getDomVoiceMO()+ usage);
					report.setDomVoiceCharge(report.getDomVoiceCharge()+getCharge());
					report.setTotalCharge(report.getTotalCharge()+getCharge());
					break;
				case (BillingCategoryEnum.INTERNATIONAL_INDEX):
					report.setInterVoice(report.getInterVoice()+usage);
					report.setInterVoiceCharge(report.getInterVoiceCharge()+getCharge());
					report.setTotalCharge(report.getTotalCharge()+getCharge());
					break;
				default:
				
			}
			break;
			case (CallTypeEnum.TERM_INDEX):  
			usage = getDuration().getTime()/1000; 		//in sec
			switch (getBillingCategory())
			{
				case (BillingCategoryEnum.DOMESTIC_INDEX):
					report.setDomVoiceMT(report.getDomVoiceMT()+usage);
					report.setDomVoiceCharge(report.getDomVoiceCharge()+getCharge());
					report.setTotalCharge(report.getTotalCharge()+getCharge());
					break;
				case (BillingCategoryEnum.INTERNATIONAL_INDEX):
					report.setInterVoice(report.getInterVoice()+usage);
					report.setInterVoiceCharge(report.getInterVoiceCharge()+getCharge());
					report.setTotalCharge(report.getTotalCharge()+getCharge());
					break;
				default:
				
			}
			break;

			case (CallTypeEnum.ROAMING_MO_INDEX):
			case (CallTypeEnum.ROAMING_MT_INDEX):
				usage = getDuration().getTime()/1000;		//in sec
				report.setRoamVoice(report.getRoamVoice()+usage);
				report.setRoamVoiceCharge(report.getRoamVoiceCharge()+getCharge());
				report.setTotalCharge(report.getTotalCharge()+getCharge());
				break;
			case (CallTypeEnum.SMS_INDEX):
				report.setSms(report.getSms()+getCount());
				report.setSmsCharge(report.getSmsCharge()+getCharge());
				report.setTotalCharge(report.getTotalCharge()+getCharge());
				break;
			case (CallTypeEnum.ROAMING_SMS_INDEX):
				report.setRoamSms(report.getRoamSms()+getCount());
				report.setSmsCharge(report.getSmsCharge()+getCharge());
				report.setTotalCharge(report.getTotalCharge()+getCharge());
				break;
			case (CallTypeEnum.WEB_INDEX):
				report.setWeb(report.getWeb()+getDataUsage());
				report.setGprsCharge(report.getGprsCharge()+getCharge());
				report.setTotalCharge(report.getTotalCharge()+getCharge());
				break;
			case (CallTypeEnum.WAP_INDEX):
				report.setWap(report.getWap()+getDataUsage());
				report.setGprsCharge(report.getGprsCharge()+getCharge());
				report.setTotalCharge(report.getTotalCharge()+getCharge());
				break;
			case (CallTypeEnum.MMS_INDEX):
				report.setMms(report.getMms()+getDataUsage());
				report.setGprsCharge(report.getGprsCharge()+getCharge());
				report.setTotalCharge(report.getTotalCharge()+getCharge());
				break;
			case (CallTypeEnum.DOWNLOAD_INDEX):
				report.setDownloadCount(report.getDownloadCount()+getCount());
				report.setGprsCharge(report.getGprsCharge()+getCharge());
				report.setTotalCharge(report.getTotalCharge()+getCharge());
				break;
			default :
		}
	}
}
