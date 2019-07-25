package com.redknee.app.crm.log;

import static org.assertj.core.api.Assertions.assertThat;

import com.redknee.app.crm.bean.SubscriberTypeEnum;
import com.redknee.app.crm.bean.Transaction;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.context.ContextSupport;
import com.redknee.framework.xlog.log.LogMsg;
import com.redknee.framework.xlog.logger.ERLogger;
import com.redknee.framework.xlog.logger.Logger;
import java.util.Date;
import org.junit.Test;

public class CoreERLoggerTest {

    private Context ctx = new ContextSupport();

    @Test
    public void createTransactionEr() {
        // given
        LoggerStub logger = new LoggerStub(ctx);
        Date transactionDate = new Date();
        Transaction transaction = mockTransaction(transactionDate);

        ctx.put(ERLogger.class, logger);
        ctx.put(CoreERLogger.CHANNEL_TYPE_ENUM_INDEX, 1);

        // when
        CoreERLogger.createTransactionEr(ctx, transaction, 100, 200L, 0);

        // then
        assertThat(logger.getMessage()).contains("1104,1100,Subscriber Transaction Event,410,4101223129,3054068245,1,"
                + CoreERLogger.formatERDateDayOnly(transactionDate) +
                ",Exito GLC,100,50174,3815,0,200,,,,,,,,agent,,,,,,1,");
    }

    private Transaction mockTransaction(Date transactionDate) {
        Transaction transaction = new Transaction();
        transaction.setSpid(410);
        transaction.setBAN("4101223129");
        transaction.setMSISDN("3054068245");
        transaction.setSubscriberType(SubscriberTypeEnum.PREPAID);
        transaction.setTransDate(transactionDate);
        transaction.setGLCode("Exito GLC");
        transaction.setAdjustmentType(50174);
        transaction.setReceiptNum(3815L);
        transaction.setAgent("agent");

        return transaction;
    }

    private class LoggerStub implements Logger {

        private Context ctx;

        private String message;

        LoggerStub(Context ctx) {
            this.ctx = ctx;
        }

        @Override
        public void log(LogMsg logMsg) {
            this.message = logMsg.toString(ctx);
        }

        @Override
        public Object cmd(Object o){
            return null;
        }

        String getMessage() {
            return message;
        }
    }
}
