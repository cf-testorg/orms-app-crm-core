package com.redknee.app.crm.extension.validator;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.redknee.framework.xhome.beans.CompoundIllegalStateException;
import com.redknee.framework.xhome.beans.IllegalPropertyArgumentException;
import com.redknee.framework.xhome.beans.Validator;
import com.redknee.framework.xhome.context.Context;

import com.redknee.app.crm.extension.Extension;
import com.redknee.app.crm.extension.ExtensionAware;


public class SingleInstanceExtensionsValidator implements Validator
{   
    /**
     * {@inheritDoc}
     */
    public void validate(Context ctx, Object obj) throws IllegalStateException
    {
        CompoundIllegalStateException exception = new CompoundIllegalStateException();
        
        if (obj instanceof ExtensionAware)
        {
            ExtensionAware parentBean = (ExtensionAware)obj; 
            
            Set<Class> extensionTypeSet = new HashSet<Class>();

            Collection<Extension> extensions = parentBean.getExtensions();
            if (extensions != null)
            {
                for (Extension extension : extensions)
                {
                    if (extension == null)
                    {
                        exception.thrown(new IllegalPropertyArgumentException(parentBean.getExtensionHolderProperty(), "No extension is selected."));
                    }
                    else
                    {
                        try
                        {
                            if (extensionTypeSet.contains(extension.getClass()))
                            {
                                exception.thrown(new IllegalPropertyArgumentException(parentBean.getExtensionHolderProperty(), extension.getName(ctx) + " extension is defined more than once."));
                            } 
                            else
                            {
                                extensionTypeSet.add(extension.getClass());
                            }
                        }
                        catch( IllegalArgumentException e )
                        {
                            // Merge all of the validation exceptions
                            exception.thrown(e);
                        }
                        catch( IllegalStateException e )
                        {
                            // Merge all of the validation exceptions
                            exception.thrown(e);
                        }
                    }
                }    
            }
        }
        
        exception.throwAll();
    }
}
