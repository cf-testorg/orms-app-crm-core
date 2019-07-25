package com.redknee.app.crm.tcbsrp;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Collections;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.HomeInternalException;
import com.redknee.framework.xhome.home.HomeProxy;
import com.redknee.framework.xhome.visitor.Visitor;
import com.redknee.framework.xlog.log.LogSupport;
import com.redknee.app.crm.bean.tcbsrp.*;

/**
 * 
 * 
 * This is a buffered home which converts instances of type {@linkcom.redknee.app.crm.bean.TcbSrp} into a csv file. The number of instances
 * persisted in a single csv file is configurable. The instances are buffered into a List and once a threshold is reached, the entire contents 
 * of that list are flushed into a CSV file.
 * 
 * 
 * @seeModelAppCrm/src/idl/TcbSrp.xml
 * @seeModelAppCrm/src/idl/TcbSrpFileConfig.xml
 * 
 * @author ameya.bhurke@redknee.com
 * 
 */
public class TcbSrpFileBufferedHome extends HomeProxy {

	private List<TcbSrp> buffer = null;
	private int bufferSize = 0;
	private TcbSrpFileConfig config = null;
	private Date previousFileGenDate = null;
	private DecimalFormat dFormat = new DecimalFormat("00000");
	private static char delimiter = ',';
	
	public TcbSrpFileBufferedHome() {
		// TODO Auto-generated constructor stub
	}

	public TcbSrpFileBufferedHome(Context ctx) {
		super(ctx);
		
		config = (TcbSrpFileConfig)ctx.get(TcbSrpFileConfig.class);
		bufferSize = config.getBuffer();
		buffer = Collections.synchronizedList(new ArrayList<TcbSrp>(bufferSize));
		
		new TimedBufferFlushThread(ctx).start();
	}
	
	public Object create(Context ctx, Object obj)
		throws HomeException, HomeInternalException	{
		
		
		TcbSrp srp = (TcbSrp)obj;
		
		if ( "".equals(srp.getServiceID()) || "".equals(srp.getServiceDescription()) || "".equals(srp.getChargingIDData()) ) {
			
			throw new HomeInternalException("All the Mandatory properties to generate an SRP file are not set.");
		}
		
		buffer.add( srp );
		
		if( buffer.size() == bufferSize ) {
								
			resetBuffer(ctx);
		}
		
		return obj;
	}
	
	/**
	 * Resets the buffer. Flushes the content and clears the buffer. 
	 * 
	 * @param ctx
	 */
	private synchronized void resetBuffer(Context ctx) {

		if ( buffer.isEmpty() )
			return;
		
		new Thread(new BufferFlushRunner(ctx, generateNewFilename(ctx), buffer)).start();
		buffer.clear();			
	}
	
	/**
	 * Generates a new tcb srp file's name.
	 * @param ctx
	 * @return
	 */
	private final String generateNewFilename(Context ctx) {
		
		if (previousFileGenDate == null) {
			previousFileGenDate = getCurrentDateWithoutTimeInfo();
		} else if (previousFileGenDate.before(getCurrentDateWithoutTimeInfo())) {
			((SRPFileExtensionCounter)ctx.get(SRPFileExtensionCounter.class)).resetCounter(ctx);
			previousFileGenDate = getCurrentDateWithoutTimeInfo();
		} 
		
		long counter = ((SRPFileExtensionCounter)ctx.get(SRPFileExtensionCounter.class)).incrementCounter(ctx);
		
		StringBuffer filenameBuffer = new StringBuffer(config.getOutputDirectory());
		filenameBuffer.append(File.separator);
		filenameBuffer.append(config.getPrefix());
		filenameBuffer.append(new SimpleDateFormat(config.getDatePattern()).format(new Date()));
		filenameBuffer.append(".");
		filenameBuffer.append(dFormat.format(counter));
		
		return filenameBuffer.toString();
	}
	

	
	/**
	 * create a Date instance with hh:mm:ss = 00:00:00
	 * @return
	 */
	private final Date getCurrentDateWithoutTimeInfo() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0); 
        return c.getTime();
	}
	
	/**
	 * 
	 * A timer thread which is started when this home is initialized.
	 * It flushes the buffer at regular interval irrespective of Buffer size.
	 * This is need as some applications may generate SRP files in batches and these bathces
	 * may not be an exact multiple of the  buffer size. This will cause the buffer to be partially full 
	 * and will be flushed only when it is full during the next batch run.
	 * 
	 * example:Invoice Server.
	 * 
	 * Suppose a Invoice GEN run on a bill cycle begins execution with 375 invoices and the 
	 * buffer size is 50. This will cause 350 srp files to be generated ( 1 SRP per invoice ) and
	 * the remaining 25 to be in the buffer till the next Invoice GEN run is triggered.
	 * 
	 * @author ameya.bhurke@redknee.com
	 *
	 */
	private class TimedBufferFlushThread extends Thread {
		
		Context ctx = null;
		
		public TimedBufferFlushThread(Context ctx_) {
			
			ctx = ctx_;
		}
		
		public void run() {
			
			while(true){
			
				try{					
					Thread.sleep(60000 * ( (TcbSrpFileConfig)ctx.get(TcbSrpFileConfig.class) ).getBufferFlushInterval() );
				} catch ( Exception e ) {
					
					LogSupport.info(ctx, this.getClass(), "TimedBufferFlushThread:SRP_file_gen interrupted.");
				}
				resetBuffer(ctx);
				LogSupport.info(ctx, this.getClass(), "TimedBufferFlushThread:buffer flushed.");
			}
		}
	}
	
	
	/**
	 * 
	 * A runnable implementation to flush the buffer into a csv file.
	 * 
	 * @author abhurke
	 *
	 */
	private class BufferFlushRunner implements Runnable {
		
		List<TcbSrp> buffer = new ArrayList<TcbSrp>();
		Context ctx = null;
		String filename = null;
		
		public BufferFlushRunner(Context ctx , String filename , List<TcbSrp> buffer) {
			
			this.ctx = ctx.createSubContext();
			this.filename = filename;
			this.buffer.addAll(buffer);
		}
		
		public void run() {

			flushBuffer();
		
		}
		
		/**
		 * flushes the TcbSrp buffer into a csv file.

		 */
		private final void flushBuffer() {
			


			try
			{
				BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(filename));
				final PrintWriter out = new PrintWriter(os);

				Iterator<TcbSrp> bufferIterator = buffer.iterator();
				
				while ( bufferIterator.hasNext()){
					
					//StringBuffer csvBuffer = TcbSrpCSVSupport.instance().append(new StringBuffer() , ',' , bufferIterator.next());
					
					TcbSrp bean = bufferIterator.next();
					
					StringBuffer buf = new StringBuffer();
					
				      buf.append(String.valueOf(bean.getOriginalRecordKey()));
				      buf.append(delimiter);
				      buf.append(String.valueOf(bean.getRequestType()));
				      buf.append(delimiter);
				      buf.append(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(bean.getDttm()));
				      buf.append(delimiter);
				      buf.append(String.valueOf(bean.getChargingIDType().getIndex()));
				      buf.append(delimiter);
				      buf.append(bean.getChargingIDData());
				      //appendString(buf, bean.getChargingIDData());
				      buf.append(delimiter);
				      buf.append(String.valueOf(bean.getRequestedAction().getIndex()));
				      buf.append(delimiter);
				      buf.append(String.valueOf(bean.getServiceType().getIndex()));
				      buf.append(delimiter);
				      buf.append(bean.getServiceID());			      
				      buf.append(delimiter);
				      //appendString(buf, bean.getServiceDescription());
				      buf.append(bean.getServiceDescription());
				      buf.append(delimiter);
				      buf.append(String.valueOf(bean.getUsageType().getIndex()));
				      buf.append(delimiter);
				      buf.append(String.valueOf(bean.getUsageData()));
				      buf.append(delimiter);
				      
				      // In case of Loyalty Points, make sure the amount field in srp is empty (null).
				      if( !(bean.getRequestedAction().equals(RequestedActionEnum.ACCRUE) || bean.getRequestedAction().equals(RequestedActionEnum.ADJUST)) ) {
				      	buf.append(String.valueOf(bean.getAmount()));
				      } 

				      buf.append(delimiter);
				      //appendString(buf, bean.getCurrencyCode());
				      buf.append(bean.getCurrencyCode());
				      buf.append(delimiter);
				      buf.append(String.valueOf(bean.getCurrencyExponent()));
				      buf.append(delimiter);
				      buf.append(String.valueOf(bean.getOriginatingSubscriptionIDType().getIndex()));
				      buf.append(delimiter);
				      //appendString(buf, bean.getOriginatingSubscriptionIDData());
				      buf.append(bean.getOriginatingSubscriptionIDData());
				      buf.append(delimiter);
				      buf.append(String.valueOf(bean.getOriginatingNetworkType().getIndex()));
				      buf.append(delimiter);
				      //appendString(buf, bean.getOriginatingNetworkData());
				      buf.append(bean.getOriginatingNetworkData());
				      buf.append(delimiter);
				      buf.append(String.valueOf(bean.getOriginatingLocationType().getIndex()));
				      buf.append(delimiter);
				      //appendString(buf, bean.getOriginatingLocationData());
				      buf.append(bean.getOriginatingLocationData());
				      buf.append(delimiter);
				      buf.append(String.valueOf(bean.getTerminatingSubscriptionIDType().getIndex()));
				      buf.append(delimiter);
				      //appendString(buf, bean.getTerminatingSubscriptionIDData());
				      buf.append(bean.getTerminatingSubscriptionIDData());
				      buf.append(delimiter);
				      buf.append(String.valueOf(bean.getTerminatingNetworkType().getIndex()));
				      buf.append(delimiter);
				      //appendString(buf, bean.getTerminatingNetworkData());
				      buf.append(bean.getTerminatingNetworkData());
				      buf.append(delimiter);
				      buf.append(String.valueOf(bean.getTerminatingLocationType().getIndex()));
				      buf.append(delimiter);
				      //appendString(buf, bean.getTerminatingLocationData());
				      buf.append(bean.getTerminatingLocationData());
				      buf.append(delimiter);
				      buf.append(String.valueOf(bean.getSubscriptionType()));
				      buf.append(delimiter);
				      buf.append(String.valueOf(bean.getCallType().getIndex()));
				      buf.append(delimiter);
				      //appendString(buf, bean.getExternalTransactionID());
				      buf.append(bean.getExternalTransactionID());
				      buf.append(delimiter);
				      
				      if (bean.getStartDttm() != null )
				    	  buf.append(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(bean.getStartDttm()));
				      
				      buf.append(delimiter);
				      
				      if(bean.getEndDttm() != null)
				    	  buf.append(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(bean.getEndDttm()));
				      
				      buf.append(delimiter);
				      buf.append(String.valueOf(bean.getTerminationCause()));
				      buf.append(delimiter);
				      //appendString(buf, bean.getCustom00());
				      buf.append(bean.getCustom00());
				      buf.append(delimiter);
				      //appendString(buf, bean.getCustom01());
				      buf.append(bean.getCustom01());
				      buf.append(delimiter);
				      //appendString(buf, bean.getCustom02());
				      buf.append(bean.getCustom02());
				      buf.append(delimiter);
				      //appendString(buf, bean.getCustom03());
				      buf.append(bean.getCustom03());
				      buf.append(delimiter);
				      //appendString(buf, bean.getCustom04());
				      buf.append(bean.getCustom04());
				      buf.append(delimiter);
				      //appendString(buf, bean.getCustom05());
				      buf.append(bean.getCustom05());
				      buf.append(delimiter);
				      //appendString(buf, bean.getCustom06());
				      buf.append(bean.getCustom06());
				      buf.append(delimiter);
				      //appendString(buf, bean.getCustom07());
				      buf.append(bean.getCustom07());
				      buf.append(delimiter);
				      //appendString(buf, bean.getCustom08());
				      buf.append(bean.getCustom08());
				      buf.append(delimiter);
				      //appendString(buf, bean.getCustom09());				
				      buf.append(bean.getCustom09());
					
				      out.println(buf.toString());
				}

				out.close();
				os.close();
				//buffer.clear();
			}
			catch (IOException e)
			{
				LogSupport.minor(ctx, this.getClass(), (buffer.toArray()).toString());
				//throw new HomeInternalException("File error during save of [" + filenameBuffer.toString() + "]: " + e.getMessage(), e);
			}		
			
		}		
	}
}
