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
public class ByChargingSource implements XStatement
{
    protected String fieldName="secondaryBalanceIndicator";
    protected boolean secondaryBalanceIndicator;

    public ByChargingSource(boolean secondaryBalanceIndicator)
    {
    	setSecondaryBalanceIndicator(secondaryBalanceIndicator);
    }

    public ByChargingSource(String fieldName,boolean secondaryBalanceIndicator)
    {
        setFieldName(fieldName);
        setSecondaryBalanceIndicator(secondaryBalanceIndicator);
        
    }

   

    /**
     * Set a PreparedStatement with the supplied Object.
     */
    public void set(Context ctx, XPreparedStatement ps) throws SQLException
    {
    }


    public String createStatement(Context ctx)

    {
    	if(this.secondaryBalanceIndicator)
    		return "(" + fieldName + " = 1 or " + fieldName + " = 2 " + ")";
    	else
    		return fieldName + " = " + fieldName;
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
	public boolean getSecondaryBalanceIndicator() {
		return secondaryBalanceIndicator;
	}

	/**
	 * @param secondaryBalanceChargedAmount 
	 */
	public void setSecondaryBalanceIndicator(
			boolean secondaryBalanceIndicator) {
		this.secondaryBalanceIndicator = secondaryBalanceIndicator;
	}

   
}
