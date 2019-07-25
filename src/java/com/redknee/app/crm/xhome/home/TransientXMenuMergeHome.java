package com.redknee.app.crm.xhome.home;

import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.MergeHome;
import com.redknee.framework.xhome.menu.XMenu;
import com.redknee.framework.xhome.menu.XMenuHome;
import com.redknee.framework.xhome.menu.XMenuIdentitySupport;
import com.redknee.framework.xhome.menu.XMenuXInfo;
import com.redknee.framework.xhome.relationship.NoRelationshipRemoveHome;

/**
 * 
 *
 * @author aaron.gourley@redknee.com
 * @since 
 */
public class TransientXMenuMergeHome extends MergeHome
{
    public TransientXMenuMergeHome(Context ctx, Home persistentXMenuHome, Home transientXMenuHome)
    {
        super(
                ctx, 
                persistentXMenuHome, 
                new Home[] {
                        new NoRelationshipRemoveHome(ctx, 
                                XMenuXInfo.KEY, XMenuXInfo.KEY, 
                                XMenuHome.class, "Not allowed to delete transient XMenu.", 
                                transientXMenuHome)}, 
                XMenuIdentitySupport.instance());
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(Context ctx, Object obj) throws HomeException
    {
        if (obj instanceof XMenu)
        {
            if (getDelegate(ctx).find(ctx, ((XMenu) obj).ID()) != null)
            {
                // Only remove from the persistent home if the menu exists in the persistent home.
                // Journal will output a remove bean line otherwise
                getDelegate(ctx).remove(ctx, obj);
            }
            
            for (int i = 0; i < cluster_.length; i++)
            {
                cluster_[i].remove(ctx, obj);
            }
        }
        else
        {
            super.remove(ctx, obj);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object store(Context ctx, Object obj) throws HomeException
    {
        Object result = null;
        
        if (getDelegate(ctx).find(ctx, ((XMenu) obj).ID()) != null)
        {
            // Only update the persistent home if the menu exists in the persistent home.
            // Journal will do 'homeCreateOrStore' otherwise, effectively overriding the transient ones
            result = getDelegate(ctx).store(ctx, obj);
        }

        for (int i = 0; i < cluster_.length; i++)
        {
            try
            {
                result = cluster_[i].store(ctx, obj);
            }
            catch(HomeException ex)
            {
            }
        }
        
        return result;
    }
}