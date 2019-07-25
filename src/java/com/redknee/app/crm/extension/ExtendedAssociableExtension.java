/*
 * This code is a protected work and subject to domestic and international
 * copyright law(s). A complete listing of authors of this work is readily 
 * available. Additionally, source code is, by its very nature, confidential 
 * information and inextricably contains trade secrets and other information 
 * proprietary, valuable and sensitive to Redknee, no unauthorised use, 
 * disclosure, manipulation or otherwise is permitted, and may only be used 
 * in accordance with the terms of the licence agreement entered into with 
 * Redknee Inc. and/or its subsidiaries.
 *
 * Copyright (c) Redknee Inc. and its subsidiaries. All Rights Reserved.
 */
package com.redknee.app.crm.extension;

import com.redknee.framework.xhome.beans.AbstractBean;
import com.redknee.framework.xhome.context.Context;


/**
 * This interface is to be used by extensions that can be associated to other beans.
 * It extends the AssociableExtension in a way that it provides methods to be executed
 * after the external bean is created/updated/removed or an exception occurred.
 *
 * @author Marcio Marques
 * @since 9.1.2
 */
public interface ExtendedAssociableExtension<T extends AbstractBean> extends AssociableExtension<T>
{
    /**
     * postExternalBeanCreation is executed after the external bean is created. 
     * 
     * @param ctx The operating Context
     * @param associatedBean The external bean
     * @param success Whether or not creation was successful
     * @throws ExtensionAssociationException On error, this exception is thrown.  If the exception's wasExtensionUpdated() flag is set, then the extension itself was modified during installation and should be stored accordingly.
     */
    public void postExternalBeanCreation(Context ctx, T associatedBean, boolean success) throws ExtensionAssociationException;

    /**
     * postExternalBeanCreation is executed after the external bean is updated. 
     * 
     * @param ctx The operating Context
     * @param associatedBean The external bean
     * @param success Whether or not update was successful
     * @throws ExtensionAssociationException On error, this exception is thrown.  If the exception's wasExtensionUpdated() flag is set, then the extension itself was modified during installation and should be stored accordingly.
     */
    public void postExternalBeanUpdate(Context ctx, T associatedBean, boolean success) throws ExtensionAssociationException;
    
    /**
     * postExternalBeanCreation is executed after the external bean is removed. 
     * 
     * @param ctx The operating Context
     * @param associatedBean The external bean
     * @param success Whether or not removal was successful
     * @throws ExtensionAssociationException On error, this exception is thrown.  If the exception's wasExtensionUpdated() flag is set, then the extension itself was modified during installation and should be stored accordingly.
     */
    public void postExternalBeanRemoval(Context ctx, T associatedBean, boolean success) throws ExtensionAssociationException;

}
