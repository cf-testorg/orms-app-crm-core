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
package com.redknee.app.crm.notification.template;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xlog.log.MinorLogMsg;


/**
 * This notification template is backed by a file.  The getTemplateMessage() method
 * returns the string content of the referenced file.
 * 
 * Sub-classes can override this behaviour to add support for more complicated file-based
 * templates.
 *
 * @author aaron.gourley@redknee.com
 * @since 8.8/9.0
 */
public class FileBasedNotificationTemplate extends AbstractFileBasedNotificationTemplate
{

    /**
     * {@inheritDoc}
     */
    public String getTemplateMessage(Context ctx)
    {
        StringBuilder result = new StringBuilder("");

        try
        {
            String primaryTemplateFilename = null;
            
            List<FilenameHolder> templateFilenames = getTemplateFilenames();
            if (templateFilenames != null && templateFilenames.size() > 0)
            {
                primaryTemplateFilename = (String) templateFilenames.iterator().next().getFilename();
            }
            
            if (primaryTemplateFilename != null)
            {
                BufferedReader br = new BufferedReader(new FileReader(primaryTemplateFilename));
                try
                {
                    for (String line = br.readLine(); line != null; line = br.readLine())
                    {
                        result.append(line);
                    }
                }
                catch (IOException e)
                {
                    new MinorLogMsg(this, "Error reading template file '" + "'", e).log(ctx);
                }
                finally
                {
                    try
                    {
                        br.close();
                    }
                    catch (IOException e)
                    {
                        // NOP
                    }  
                }
            }
            else
            {
                new MinorLogMsg(this, "No template file is configured.", null).log(ctx);
            }
        }
        catch (FileNotFoundException e)
        {
            new MinorLogMsg(this, "Template file '" + "' not found.", null).log(ctx);
        }

        return result.toString();
    }

}
