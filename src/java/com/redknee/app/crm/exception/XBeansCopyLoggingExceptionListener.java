package com.redknee.app.crm.exception;

import com.redknee.framework.xhome.beans.CompoundIllegalStateException;
import com.redknee.framework.xhome.beans.DefaultExceptionListener;
import com.redknee.framework.xhome.beans.Identifiable;
import com.redknee.framework.xhome.beans.IllegalPropertyArgumentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xlog.log.DebugLogMsg;
import com.redknee.framework.xlog.log.LogSupport;
import com.redknee.framework.xlog.log.MinorLogMsg;


/**
 * Exception listener that logs the errors but does not propagate them to any existing exception listeners.
 */
public class XBeansCopyLoggingExceptionListener extends DefaultExceptionListener
{

    private final Context ctx_;
    private final Object sourceBean_;
    private final Object destBean_;
    private final boolean isDebugEnabled_;

    public XBeansCopyLoggingExceptionListener(Context ctx, Object sourceBean, Object destBean)
    {
        this.ctx_ = ctx;
        isDebugEnabled_ = LogSupport.isDebugEnabled(ctx);
        this.sourceBean_ = sourceBean;
        this.destBean_ = destBean;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void thrown(Throwable t)
    {
        if (t instanceof CompoundIllegalStateException)
        {
            super.thrown(t);
        }
        else
        {
            boolean isUnexpectedException = !(t instanceof IllegalPropertyArgumentException) && !(t.getCause() instanceof IllegalPropertyArgumentException);
            if (isDebugEnabled_ || isUnexpectedException)
            {
                // the resulting string will be very long. Preallocate space.
                final StringBuilder msg = new StringBuilder(512);
                msg.append("Error copying properties");
                if (sourceBean_ != null)
                {
                    msg.append(" from ").append(sourceBean_.getClass().getName());
                    if (sourceBean_ instanceof Identifiable)
                    {
                        msg.append(" with ID=[").append(((Identifiable)sourceBean_).ID() + "]");
                    }
                }
                if (destBean_ != null)
                {
                    msg.append(" to ").append(destBean_.getClass().getName());
                    if (destBean_ instanceof Identifiable)
                    {
                        msg.append(" with ID=[").append(((Identifiable)destBean_).ID() + "]");
                    }
                }

                msg.append(" (message=");
                msg.append(t.getMessage());

                if (isUnexpectedException)
                {
                    msg.append(").");
                    if (isDebugEnabled_)
                    {
                        new MinorLogMsg(this, msg.toString(), t).log(ctx_);
                    }
                    else
                    {
                        new MinorLogMsg(this, msg.toString() + " Enable DEBUG logs for stacktrace.", null).log(ctx_);
                    }
                }
                else if (isDebugEnabled_)
                {
                    // There will probably be a lot of IllegalPropertyArgumentExceptions, so keep them as debug-only
                    msg.append("). Verify that existing data is valid, otherwise ignore this exception.");
                    new DebugLogMsg(this, msg.toString(), null).log(ctx_);
                }
            }
        }
    }
}