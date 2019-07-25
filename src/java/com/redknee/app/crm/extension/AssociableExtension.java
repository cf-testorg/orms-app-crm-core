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
 * It provides a means to associate, updateAssociation, and dissociate the extension to the bean.
 *
 * @author Marcio Marques
 * @since 9.1.2
 */
public interface AssociableExtension<T extends AbstractBean> extends Extension
{
    /**
     * Associate must be called on an Associable Extension whenever it is associated to an external bean. The extension is responsible
     * for provisioning the association, setting up any other dependent data, etc.
     * 
     * @param ctx The operating Context
     * @param associatedBean The external bean
     * @throws ExtensionAssociationException On error, this exception is thrown.  If the exception's wasExtensionUpdated() flag is set, then the extension itself was modified during installation and should be stored accordingly.
     */
    public void associate(Context ctx, T associatedBean) throws ExtensionAssociationException;

    /**
     * UpdatedAssociation must be called on an Associable Extension whenever the external bean associated to this extension is updated. The extension is responsible
     * for updating external dependencies.  Multiple update calls made on the same extension instance containing the same data
     * should be handled gracefully.
     * 
     * @param ctx The operating Context
     * @param associatedBean The external bean
     * @throws ExtensionInstallationException On error, this exception is thrown.  If the exception's wasExtensionUpdated() flag is set, then the extension itself was modified during installation and should be stored accordingly.
     */
    public void updateAssociation(Context ctx, T associatedBean) throws ExtensionAssociationException;
    
    /**
     * Dissociate must be called on an Associable Extension when it is being dissociate from an external bean.  
     *  
     * @param ctx The operating Context
     * @param associatedBean The external bean
     * @throws ExtensionInstallationException On error, this exception is thrown.  If the exception's wasExtensionUpdated() flag is set, then the extension itself was modified during installation and should be stored accordingly.
     */
    public void dissociate(Context ctx, T associatedBean) throws ExtensionAssociationException;

}
