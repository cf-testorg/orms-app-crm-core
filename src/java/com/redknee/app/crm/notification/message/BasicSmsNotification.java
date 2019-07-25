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
 * This message simply contains a textual string that can be sent via SMS. The textual
 * string in this implementation should contain no strings that require dynamic value
 * substitution. If the template used to generate this message contained such strings, it
 * is the message generator's responsibility to perform the substitution during message
 * generation.
 * 
 * 
 * @author aaron.gourley@redknee.com
 * @since 8.8/9.0
 */
public class BasicSmsNotification extends AbstractBasicSmsNotification
{

}
