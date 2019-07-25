package com.redknee.app.crm.smsc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.redknee.app.crm.bean.*;
import com.redknee.app.crm.support.SystemStatusSupportHelper;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.context.ContextAwareSupport;
import com.redknee.framework.xhome.home.*;
import com.redknee.framework.xlog.log.DebugLogMsg;
import com.redknee.framework.xlog.log.InfoLogMsg;
import com.redknee.framework.xlog.log.LogSupport;
import com.redknee.service.oam.LogServiceFactory;
import com.redknee.service.smsc.SmppBindType;
import com.redknee.service.smsc.SmscConnection;
import com.redknee.service.smsc.SmscProperty;
import com.redknee.service.smsc.helper.SmscWorkerAction;


public class SmscConnectionMgr
   extends ContextAwareSupport
{
	public static final int  THREAD_POOL_SIZE=10;
	public static final int  WORKER_POOL_SIZE=10;

   SmscTxMgr  tx_mgr             = null;
   ArrayList  tx_connectionList  = new ArrayList();
   int        takePtr            = 0;
   volatile  boolean    stopped            = false;



   public SmscConnectionMgr(Context ctx)
   {
      setContext(ctx);
      init();
      //ctx.put(SmscConnectionMgr.class, this);
   }


   public SmscConnection getConnection()
   {
      if (tx_connectionList.isEmpty())
      {
         return null;
      }
      SmscConnection connection  = null;
      synchronized (this)
      {
         for (int i =0; i< tx_connectionList.size(); i++)
         {
             if (takePtr >= tx_connectionList.size())
             {
                takePtr = 0;
             }

             connection = (SmscConnection) tx_connectionList.get(takePtr);
             ++takePtr;
             
             if (connection.isBound())
             {
                 return connection;
             }
         }
      }
      
      return null;
   }
   
   public boolean hasActiveConnection()
   {
      if (tx_connectionList.isEmpty())
      {
         return false;
      }
     
      
      synchronized (this)
      {
         for (int i =0; i< tx_connectionList.size(); i++)
         {
             SmscConnection connection = (SmscConnection) tx_connectionList.get(i);
             
             if (connection.isBound())
             {
                 return true;
             }
         }
      }
      
      return false;
   }

   public void init()
   {
      tx_mgr = new SmscTxMgr();
   }


   public void stop()
   {
      tx_mgr.stop();
      stopped = true;
   }


   void createTxConnection(SmscTxConnectionConfig cfg)
   {
      SmscProperty  property  = new SmscProperty();
     
      setRebindDelay(property, cfg.getRebindSchedule());
      property.setWorkpoolSize(WORKER_POOL_SIZE);
      property.setThreadpoolSize(THREAD_POOL_SIZE);
      property.setId(String.valueOf(cfg.getID()));
      property.setBindType(SmppBindType.TX);
      property.setHost(cfg.getSmscHost());
      property.setPort(cfg.getSmscPort());
      property.setSystemId(cfg.getSystemId());
      property.setPassword(cfg.getPassword());
      property.setSystemType(cfg.getSystemType());
      property.setInterfaceVersion(cfg.getInterfaceVersion());
      property.setEnquireLink(cfg.getEnquireLinkEnabled());
      property.setEnquireLinkInterval(cfg.getEnquireLinkInterval());
      property.setConnectionTimeout(cfg.getConnectionTimeout());
	  if (LogSupport.isDebugEnabled(getContext()))
	  {
      	new DebugLogMsg(this, "create new SMSC Tx connection with " + cfg.getSmscHost() + ":" + cfg.getSmscPort(), null).log(getContext());
	  }
      
	  SmscConnectionExternalService smscConnectionExternalService = new SmscConnectionExternalService(getContext(), property);
	  String externalServiceKey = SmscConnectionExternalService.class.getName()+"_"+property.getId();
	  getContext().put(externalServiceKey, smscConnectionExternalService);
	  SystemStatusSupportHelper.get(getContext()).registerExternalService(getContext(), externalServiceKey);
	  
	  synchronized (this)
	  {
    	  tx_connectionList.add(smscConnectionExternalService);
	  }
   }

    
   public void setRebindDelay(SmscProperty property, List schedule)
   {
      if ( schedule.size() == 0 )
      {
         // 1 minute, 1 minute, 1 minute, 10 minutes
         property.setRebindDelay(new long[] { 60000l, 60000l, 60000l, 600000l });
         
         return;
      }
      
      long[] rebindDelay = new long[schedule.size()];
      
      for ( int i = 0 ; i < rebindDelay.length ; i++ )
      {
         RebindInterval interval = (RebindInterval) schedule.get(i);
         
         switch ( interval.getUnits().getIndex() )
         {
            case IntervalTypeEnum.MILLISECONDS_INDEX:
               rebindDelay[i] = interval.getDelay();
            break;
            
            case IntervalTypeEnum.SECONDS_INDEX:
               rebindDelay[i] = 1000l * interval.getDelay();
            break;
            
            case IntervalTypeEnum.MINUTES_INDEX:
               rebindDelay[i] = 60000l * interval.getDelay();
         }
      }

      property.setRebindDelay(rebindDelay);      
   }


   void dropTxConnection(SmscTxConnectionConfig cfg)
   {
      // not use (PS)
      //ArrayList  connections  = tx_connectionList;
      int        ptr          = findConnection("" + cfg.getID(), tx_connectionList);
      if (ptr != -1)
      {
         synchronized (this)
         {
            new InfoLogMsg(this, "Remove SMSC Tx connection with " + cfg.getSmscHost() + ":" + cfg.getSmscPort(), null).log(getContext());
            ((SmscConnection) tx_connectionList.remove(ptr)).disconnect();
         }

      }
   }


 
   void updateTxConnection(SmscTxConnectionConfig cfg)
   {
      dropTxConnection(cfg);
      createTxConnection(cfg);
   }



   private int findConnection(String Id, ArrayList connections)
   {
      for (int i = 0; i < connections.size(); ++i)
      {
         if (((SmscConnection) connections.get(i)).getProperty().getId().equals(Id))
         {
            return i;
         }
      }
      return -1;
   }


   private class SmscTxMgr implements HomeChangeListener
   {

      SmscTxMgr()
      {
         Home      tx_home  = (Home) getContext().get(SmscTxConnectionConfigHome.class);
         Iterator  iter     = null;
         try
         {
            iter = tx_home.selectAll(getContext()).iterator();
            while (iter.hasNext())
            {
               createTxConnection(
                     (SmscTxConnectionConfig) iter.next());
            }
            tx_home.cmd(getContext(),new NotifyingHomeCmd(this, true));
         }
         catch (HomeException e)
         {
            return;
         }
      }


      public void homeChange(final HomeChangeEvent evt)
      {
         if (stopped)
         {
            return;
         }

         // Perform asynchronously so as to avoid hanging the UI         
         new Thread() {
            public void run()
            {
               if(evt.getOperation()==HomeOperationEnum.CREATE)
               {
                     createTxConnection((SmscTxConnectionConfig) evt.getSource());
               }
               else if(evt.getOperation()==HomeOperationEnum.STORE)
               {
                     updateTxConnection((SmscTxConnectionConfig) evt.getSource());
               }
               else if(evt.getOperation()==HomeOperationEnum.REMOVE)
               {
                     dropTxConnection((SmscTxConnectionConfig) evt.getSource());
               }
               else
               {
               	   if(LogSupport.isDebugEnabled(getContext()))
               	   {
               	   	new DebugLogMsg(this,"Unknown and unhandled home operation",null).log(getContext());
               	   }
               }
            } // run
         }.start();
      }


      public void stop()
      {
         for (int i = 0; i < tx_connectionList.size(); ++i)
         {
            ((SmscConnection) tx_connectionList.get(i)).disconnect();
         }
         
         synchronized (this)
         {
             tx_connectionList.clear();
         }

      }

   }


  }
