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
package com.redknee.app.crm.notification.delivery;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.util.time.Time;
import com.redknee.framework.xlog.log.DebugLogMsg;
import com.redknee.framework.xlog.log.ERLogMsg;
import com.redknee.framework.xlog.log.LogSupport;
import com.redknee.framework.xlog.log.MinorLogMsg;

import com.redknee.app.crm.bean.SmppConfig;
import com.redknee.app.crm.bean.SystemNoteSubTypeEnum;
import com.redknee.app.crm.bean.SystemNoteTypeEnum;

import com.redknee.app.crm.notification.LoggingNotificationResultCallback;
import com.redknee.app.crm.notification.NotificationResultCallback;
import com.redknee.app.crm.notification.NullNotificationResultCallback;
import com.redknee.app.crm.notification.RecipientInfo;
import com.redknee.app.crm.notification.ScheduledNotification;
import com.redknee.app.crm.notification.ScheduledNotificationHome;
import com.redknee.app.crm.notification.message.NotificationMessage;
import com.redknee.app.crm.notification.message.SmsNotificationMessage;
import com.redknee.app.crm.smsc.SmscConnectionMgr;
import com.redknee.app.crm.support.CalendarSupportHelper;
import com.redknee.app.crm.support.NoteSupportHelper;
import com.redknee.protocol.smpp.SmppErrors;
import com.redknee.protocol.smpp.SmppSubmitSm;
import com.redknee.service.smsc.SmscConnection;
import com.redknee.service.smsc.SmscException;


/**
 * This service delivers SMS notification messages to an SMSC.
 *
 * @author aaron.gourley@redknee.com
 * @since 8.8/9.0
 */
public class SmsDeliveryService extends AbstractSmsDeliveryService
{
    protected static final Pattern WHITESPACE_REGEX = Pattern.compile("\\s");
	private static final int IDENTIFIER = 1165;
	private static final int CLASS = 1100;
	private static final String TITLE = "Notification Event";
    public static int PER_MESSAGE_LIMIT = 160;
    public static int MIN_SEGMENTATION_LENGTH = PER_MESSAGE_LIMIT - (PER_MESSAGE_LIMIT * 2 / 3);
    public static String ENCODING_CHARSET = "SmsDeliveryService.EncodingCharset";
    
    /**
     * {@inheritDoc}
     */
    public void sendMessage(Context ctx, RecipientInfo recipient, NotificationMessage msg,
            NotificationResultCallback callback)
    {
        if (callback == null)
        {
            callback = new LoggingNotificationResultCallback(NullNotificationResultCallback.instance());
        }
        
        callback.reportAttempt(ctx);
        try
        {
            if (msg instanceof SmsNotificationMessage)
            {
                SmsNotificationMessage smsMsg = (SmsNotificationMessage) msg;

                String from = getFrom(ctx, smsMsg);
                String to = recipient.getSmsTo();
                String message = smsMsg.getMessage();
                Time timeToSend = smsMsg.getTimeToSend();
                
                if (from != null && from.length() > 0 
                        && to != null && to.length() > 0
                        && message != null && message.length() > 0)
                {
                    sendSmsMessage(ctx, from, to, message, timeToSend);
                    if (LogSupport.isDebugEnabled(ctx)){
        				LogSupport.debug(ctx, this, "SMS sent successfully from " + getFrom(ctx, smsMsg));
        			}
                    callback.reportSuccess(ctx);
                }
                else if (from == null || from.length() == 0)
                {
                    callback.reportFailure(ctx, false, "No 'from' number could be found in message or delivery service.  No message will be delivered.");
                }
                else if (to == null || to.length() == 0)
                {
                    callback.reportFailure(ctx, false, "No 'to' number could be found in message.  No message will be delivered.");
                }
                else if (message == null || message.length() == 0)
                {
                    callback.reportFailure(ctx, false, "Message content is empty.  No message will be delivered.");
                }
            }
            else
            {
                String msgType = (msg != null ? msg.getClass().getName() : null);
                callback.reportFailure(ctx, false, "Message of type " + msgType + " not supported by " + this.getClass().getName());
            }
        }
        catch (MessageDeliveryException e)
        {
        	/**
        	 *   Retry for maxRetry times [configurable in smpp config] otherwise mark recoverable flag as false.
        	 */
        	int maxNumberOfRetries = getSmppConfig().getMaxRetry(); //default is 0 if not configured
        	ScheduledNotification entry = (ScheduledNotification) ctx.get(ScheduledNotification.class);
        	

        	if (entry!=null && entry.getNumAttempts()!= maxNumberOfRetries) {

        		ScheduledNotification clonedEntry = null;
        		try {
        			clonedEntry = (ScheduledNotification) entry.deepClone(); //cloning entity because its a frozen bean 
        		} catch (CloneNotSupportedException cloneEx) {
        			 new DebugLogMsg(this, "Fail to clone object ", cloneEx).log(ctx);
        		}
        		clonedEntry.setNumAttempts(clonedEntry.getNumAttempts() + 1);
        		try {
        			Home home = (Home) ctx.get(ScheduledNotificationHome.class);
        			home.store(clonedEntry);
        		}catch (HomeException exc){
        			new DebugLogMsg(this, "Fail to store entity ScheduledNotification ", exc).log(ctx);
        		}
        		new DebugLogMsg(this, "Number of attempts made to deliver SMS with Id " +clonedEntry.getId() + " is "+ clonedEntry.getNumAttempts(),null).log(ctx);
        		callback.reportFailure(ctx, true, e); //make recoverable flag as true so that it will be tried again
        	}else {
        		//default behavior
        		callback.reportFailure(ctx, e.isRecoverable(), e);  
        	}
        	
        	String ban = entry!=null ? entry.getBan() : null;
        	
        	try {
        		new ERLogMsg(
            				IDENTIFIER,
            				CLASS,
            				TITLE,
            				1,
            				new String[]
            						{
            					ban,
            					e.getMessage(),
            					msg.toString(),
            						}).log(ctx);
        		
        		if (ban !=null && !ban.trim().equals("")){
        			NoteSupportHelper.get(ctx).addAccountNote(ctx, ban, " Message Delivery failed for ID "+ 
        					entry.getId() +"Message : " + msg.toString() + "Please check ER ID 1165 for details ", SystemNoteTypeEnum.EVENTS, SystemNoteSubTypeEnum.NOTIFICATION);
        		}
        	}catch (HomeException homeExc){
        			 new DebugLogMsg(this, "Exception caught ", homeExc).log(ctx);        		
        	}catch (Exception ex){
        			 new DebugLogMsg(this, "Exception caught ", ex).log(ctx);
        	}
        }
        catch (Exception e)
        {
            callback.reportFailure(ctx, false, "Unexpected exception occurred while delivering message: " + e.getMessage(), e);
            new DebugLogMsg(this, "Unexpected error ", e).log(ctx);
        }
    }

    public void sendSmsMessage(Context ctx, String src, String dest, String message, Time timeToSend) throws MessageDeliveryException
    {
        Collection<SmppSubmitSm> smsMessage = createMsg(ctx, src, dest, message, timeToSend);
        
        SmscConnection connection = ((SmscConnectionMgr) ctx.get(SmscConnectionMgr.class)).getConnection();
        if (connection == null)
        {
            throw new MessageDeliveryException("Can not get active SMSC TX connection", true);
        }
        
        try
        {
            Iterator<SmppSubmitSm> smsIterator = smsMessage.iterator();
            while (smsIterator.hasNext())
            {
                int resultCode = connection.sendSmppMsg(smsIterator.next());
                if (resultCode != SmppErrors.SMPP_ROK)
                {
                    new DebugLogMsg(this, "Fail to deliver SMS msg, response code =" + resultCode, null).log(ctx);
                    throw new MessageDeliveryException("Fail to deliver SMS msg, response code = " + resultCode, false);
                }
            }
        }
        catch (SmscException e)
        {
            throw new MessageDeliveryException(false, e);
        }
        
    }

    protected Collection<String> segmentMessage(String message)
    {
        Collection<String> result = new LinkedList<String>();

        int minMessageLengthCutoff = MIN_SEGMENTATION_LENGTH;
        if (message.length() > PER_MESSAGE_LIMIT)
        {
            String endOfCutOffMessage = message.substring(minMessageLengthCutoff, PER_MESSAGE_LIMIT);
            
            int end = endOfCutOffMessage.lastIndexOf("\n");
            if (end < 0)
            {
                end = endOfCutOffMessage.lastIndexOf(" ");
            }
            if (end < 0)
            {
                Matcher matcher = WHITESPACE_REGEX.matcher(endOfCutOffMessage);
                while(matcher.find())
                {
                    end = matcher.start();
                }
            }

            end += minMessageLengthCutoff;
            if (end < minMessageLengthCutoff)
            {
                end = minMessageLengthCutoff + endOfCutOffMessage.length();
            }
            
            result.add(message.substring(0, end));
            result.addAll(segmentMessage(message.substring(end, message.length())));
        }
        else
        {
            result.add(message);
        }
        
        return result;
    }

    protected String getFrom(Context ctx, SmsNotificationMessage smsMsg)
    {
        String from = smsMsg.getFrom();
        if (from == null || from.length() == 0)
        {
            final SmppConfig cfg = getSmppConfig();
            if (cfg != null)
            {
                from = cfg.getSourceAddr();
            }
        }
        return from;
    }
    
    private Collection<SmppSubmitSm> createMsg(final Context ctx,
            final String source, final String dest, 
            final String smsTxt, final Time timeToSend)
    {
        Collection<SmppSubmitSm> messages = new ArrayList<SmppSubmitSm>();
        
        final SmppConfig cfg = getSmppConfig();
        String prefix = cfg.getMSISDNPrefix();
        if (prefix == null)
        {
            prefix = "";
        }
        
        Collection<String> msgParts = segmentMessage(smsTxt);
        
        SmppSubmitSm msg = null;
        for (String msgPart : msgParts)
        {
            if (msgPart.trim().length() == 0)
            {
                // Don't send empty SMS messages
                continue;
            }
            
            msg = new SmppSubmitSm();
            
            msg.setServiceType(cfg.getServiceType());
            msg.setSourceAddrTon(cfg.getSourceAddrTon());
            msg.setSourceAddrNpi(cfg.getSourceAddrNpi());
            msg.setSourceAddr(source);
            msg.setDestAddrTon(cfg.getDestAddrTon());
            msg.setDestAddrNpi(cfg.getDestAddrNpi());
            msg.setDestAddr(prefix.trim() + dest);
            msg.setEsmClass(cfg.getEsmClass());
            msg.setProtocolId(cfg.getProtocolId());
            msg.setPriorityFlag(cfg.getPriorityFlag());
            msg.setValidityPeriod(cfg.getValidityPeriod());
            msg.setRegisteredDeliveryFlag(cfg.getRegisteredDeliveryFlag());
            msg.setReplaceIfPresentFlag(cfg.getReplaceIfPresentFlag());
            msg.setDataCoding(cfg.getDataCoding());
            msg.setSmDefaultMsgId(0);
            msg.setSequenceNumber(messages.size() + 1);
            
            try {
				msg.setShortMessage(msgPart.trim().getBytes((String) ctx.get(ENCODING_CHARSET, "ISO-8859-1")));
			} catch (UnsupportedEncodingException ex) {
				new MinorLogMsg(this, "Exception while encoding the message part "+messages.size()+1 , ex).log(ctx);
			}
            
            String scheduledDateString = "";
            if (timeToSend != null && cfg.isSmscScheduledDelivery())
            {
                try
                {
                    Date scheduledDate = getSendingTime(ctx, timeToSend);
                    Calendar scheduledDateCalendar = Calendar.getInstance();
                    scheduledDateCalendar.setTime(scheduledDate);
                    int quartersOffset = (scheduledDateCalendar.get(Calendar.ZONE_OFFSET)+scheduledDateCalendar.get(Calendar.DST_OFFSET)) / (15*60*1000);
                    scheduledDateString = new SimpleDateFormat("yyMMddHHmmssS").format(scheduledDate).substring(0,13) + Math.abs(quartersOffset) + (quartersOffset<0?"-":"+");
                }
                catch (Exception e)
                {
                    new MinorLogMsg(this, "Failed to queue message for delivery time " + timeToSend + ". Delivering immediately." + dest, e).log(ctx);
                }
            }
            msg.setScheduleDeliveryTime(scheduledDateString);

            messages.add(msg);
        }
        
        return messages;
    }

    private Date getSendingTime(Context ctx, Time smstime)
    {
        // Set the current Year, Month and Date to smsDate.
        // Then it needs to be compared with current time and set to next day if
        // the time is before current time.
        // If time is not set, set one min. after current time.
        Date now = new Date();
        
        if (smstime == null)
        {
            // If time is null or empty, set to send right away.
            return CalendarSupportHelper.get(ctx).findDateMinAfter(1, now);
        }

        Date smsDate = smstime.toDate(Calendar.getInstance());
        if (smsDate.before(now))
        {
            smsDate = CalendarSupportHelper.get(ctx).getDayAfter(smsDate);
        }
        
        return smsDate;
    }
}
