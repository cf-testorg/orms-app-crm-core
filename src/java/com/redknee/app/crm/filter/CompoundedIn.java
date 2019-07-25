package com.redknee.app.crm.filter;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.redknee.framework.xhome.beans.xi.PropertyInfo;
import com.redknee.framework.xhome.elang.In;


public class CompoundedIn extends com.redknee.framework.xhome.elang.Or
{
    public CompoundedIn(
            PropertyInfo         arg,
            Set                  set
         )
    {
        super();
        if (set.size()<=1000)
        {
            this.add(new In(arg, set));
        }
        else
        {
            Iterator iter = set.iterator();
            Set newSet = new HashSet();
            while (iter.hasNext())
            {
                Object o = iter.next();
                newSet.add(o);
                if (newSet.size()==1000)
                {
                    this.add(new In(arg, newSet));
                    newSet = new HashSet();
                }
            }

        }
    }
    
}
