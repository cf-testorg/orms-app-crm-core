package com.redknee.app.crm.sequenceId;

import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.visitor.AbortVisitException;
import com.redknee.framework.xhome.visitor.Visitor;

public class MaxIdentifierVisitor
   implements Visitor
{
   protected long maxId_ = 0;

   public long getMaxId()
   {
      return maxId_;
   }

   public void setMaxId(long maxId)
   {
      maxId_ = maxId;
   }

   public void visit(Context ctx, Object bean)
      throws AgentException, AbortVisitException
   {
      SequenceIdentified identified = (SequenceIdentified)bean;

      if (identified.getIdentifier() > maxId_)
      {
         setMaxId(identified.getIdentifier());
      }
   }
}
