/*
 * Created on Sep 25, 2003
 *
 * Copyright (c) 1999-2003 REDKNEE.com. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * REDKNEE.com. ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with REDKNEE.com.
 *
 * REDKNEE.COM MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE
 * SUITABILITY OF THE SOFTWARE, EITHCDR EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE IMPLIED WARRANTIES OF MCDRCHANTABILITY, FITNESS FOR
 * A PARTICULAR PURPOSE, OR NON-INFRINGEMENT. REDKNEE.COM SHALL NOT BE
 * LIABLE FOR ANY DAMAGES SUFFCDRED BY LICENSEE AS A RESULT OF USING,
 * MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS DCDRIVATIVES.
 */
package com.redknee.app.crm.client;

import com.redknee.framework.xhome.home.HomeException;

import com.redknee.app.crm.CoreCrmConstants;

/**
 * @author psperneac
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ProvisioningHomeException extends HomeException
{
	protected int resultCode;
	protected String errModule = CoreCrmConstants.OM_CRM_PROV_ERROR;

	public ProvisioningHomeException(String _message,int _resultCode)
	{
		super(_message);
		setResultCode(_resultCode);
	}
	public ProvisioningHomeException(String _message,int _resultCode, String module)
	{
		super(_message);
		setResultCode(_resultCode);
		setErrModule(module);
	}
	
	public ProvisioningHomeException(String _message,int _resultCode, String module, Exception e)
	{
		super(_message, e);
		setResultCode(_resultCode);
		setErrModule(module);
	}
	
	public ProvisioningHomeException(String _message, Exception e)
	{
		super(_message, e);
		setResultCode(-1);
	}
	/**
	 * @return
	 */
	public int getResultCode()
	{
		return resultCode;
	}

	/**
	 * @param i
	 */
	public void setResultCode(int i)
	{
		resultCode= i;
	}
	


	/**
	 * @return Returns the errModule.
	 */
	public String getErrModule() {
		return errModule;
	}
	/**
	 * @param errModule The errModule to set.
	 */
	public void setErrModule(String errModule) {
		this.errModule = errModule;
	}
}
