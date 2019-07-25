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
package com.redknee.app.crm.notification.generator;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.redknee.framework.core.bean.Script;
import com.redknee.framework.core.bean.ScriptLanguageEnum;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xlog.log.MinorLogMsg;

import com.redknee.app.crm.io.FileInputStreamFactory;
import com.redknee.app.crm.io.InputStreamFactory;
import com.redknee.app.crm.io.ScriptInputStreamFactory;


/**
 * Abstract class providing helper functions to all binary message generators.
 *
 * @author aaron.gourley@redknee.com
 * @since 8.8/9.0
 */
public abstract class AbstractBinaryMessageGenerator extends AbstractMessageGenerator
{   
    protected OutputStream getIntermediateOutputStream(Context ctx, String filenamePrefix)
    {
        OutputStream out = null;
        
        File tempFile = null;
        try
        {
            tempFile = File.createTempFile(filenamePrefix, null);
            try
            {
                out = new FilenameAwareFileOutputStream(tempFile);
            }
            catch (FileNotFoundException e)
            {
                new MinorLogMsg(this, "Temp file " + tempFile.getPath() + " not found.", e).log(ctx);
            }
        }
        catch (IOException e)
        {
            new MinorLogMsg(this, "Error creating temp file for Jasper output.", e).log(ctx);
        }
        
        if (out == null)
        {
            out = new ByteArrayOutputStream();
        }
        
        return out;
    }

    protected InputStreamFactory getInputStreamFactoryForIntermediateOutputStream(Context ctx, OutputStream tmpOut)
    {
        InputStreamFactory factory = null;
        if (tmpOut instanceof FilenameAwareFileOutputStream)
        {
            FileInputStreamFactory fileFactory = new FileInputStreamFactory();
            fileFactory.setFilename(((FilenameAwareFileOutputStream) tmpOut).getFilename());
            fileFactory.setTempFile(true);
            factory = fileFactory;
        }
        else if (tmpOut instanceof ByteArrayOutputStream)
        {
            ByteArrayOutputStream boStream = (ByteArrayOutputStream) tmpOut;
            byte[] bytes = boStream.toByteArray();
            
            if (bytes != null && bytes.length > 0)
            {
                StringBuilder content = new StringBuilder();
                content.append("byte[] bytes = new byte[] {");
                for (byte current : bytes)
                {
                    content.append(current).append(',');
                }
                content.append("};return new java.io.ByteArrayInputStream(bytes);");
                
                ScriptInputStreamFactory scriptFactory = new ScriptInputStreamFactory();
                Script script = new Script();
                script.setLang(ScriptLanguageEnum.BSH);
                script.setScript(content.toString());
                scriptFactory.setScript(script);
                factory = scriptFactory;
            }
        }
        return factory;
    }
    
    private static class FilenameAwareFileOutputStream extends FileOutputStream
    {
        public FilenameAwareFileOutputStream(File file) throws FileNotFoundException
        {
            super(file);
            filename_ = file.getPath();
        }
        
        public String getFilename()
        {
            return filename_;
        }
        
        private final String filename_;
    }
}
