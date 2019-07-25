package com.redknee.app.crm.filter;

import java.sql.SQLException;

import com.redknee.framework.xhome.filter.Predicate;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.visitor.AbortVisitException;
import com.redknee.framework.xhome.xdb.XPreparedStatement;
import com.redknee.framework.xhome.xdb.XStatement;
import com.redknee.app.crm.bean.DealerCodeAware;

/**
 * @author alekurwale
 */
public class BySecondaryBalanceCharged implements XStatement
{
    protected String fieldName="secondaryBalanceChargedAmount";
    protected long secondaryBalanceChargedAmount;

    public BySecondaryBalanceCharged(long code)
    {
        setSecondaryBalanceChargedAmount(code);
    }

    public BySecondaryBalanceCharged(String fieldName,long code)
    {
        setFieldName(fieldName);
        setSecondaryBalanceChargedAmount(code);
        
    }

   

    /**
     * Set a PreparedStatement with the supplied Object.
     */
    public void set(Context ctx, XPreparedStatement ps) throws SQLException
    {
    }


    public String createStatement(Context ctx)

    {
        return fieldName + " >= " + getSecondaryBalanceChargedAmount() + "";
    }

    public String getFieldName()
    {
        return fieldName;
    }

    public void setFieldName(String fieldName)
    {
        this.fieldName = fieldName;
    }

	/**
	 * @return 
	 */
	public long getSecondaryBalanceChargedAmount() {
		return secondaryBalanceChargedAmount;
	}

	/**
	 * @param secondaryBalanceChargedAmount 
	 */
	public void setSecondaryBalanceChargedAmount(
			long secondaryBalanceChargedAmount) {
		this.secondaryBalanceChargedAmount = secondaryBalanceChargedAmount;
	}

   
}
