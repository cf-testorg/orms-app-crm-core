/*
 * This code is a protected work and subject to domestic and international
 * copyright law(s).  A complete listing of authors of this work is readily
 * available.  Additionally, source code is, by its very nature, confidential
 * information and inextricably contains trade secrets and other information
 * proprietary, valuable and sensitive to Redknee.  No unauthorized use,
 * disclosure, manipulation or otherwise is permitted, and may only be used
 * in accordance with the terms of the license agreement entered into with
 * Redknee Inc. and/or its subsidiaries.
 *
 * Copyright (c) Redknee Inc. and its subsidiaries. All Rights Reserved.
 */
package com.redknee.app.crm.notification.message;


/**
 * A post notification message is a printable document that can be sent via regular letter
 * mail. Since there is always a file backing a post notification, the InputStream is a
 * FileInputStream.
 * 
 * @author aaron.gourley@redknee.com
 * @since 8.8/9.0
 */
public interface PostNotificationMessage extends BinaryNotificationMessage
{
    public String getFilename();
    public void setFilename(String filename);
}
