package com.redknee.app.crm.web.border;

import java.util.HashSet;
import java.util.Set;

import com.redknee.framework.xhome.context.AgentException;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.context.ContextAgentProxy;
import com.redknee.framework.xhome.elang.In;
import com.redknee.framework.xhome.web.search.SearchBorder;
import com.redknee.framework.xhome.web.search.WildcardSelectSearchAgent;
import com.redknee.framework.xlog.log.LogSupport;

import com.redknee.app.crm.bean.KeyConfiguration;
import com.redknee.app.crm.bean.KeyConfigurationXInfo;
import com.redknee.app.crm.bean.KeyValueFeatureEnum;
import com.redknee.app.crm.bean.search.KeyConfigurationSearch;
import com.redknee.app.crm.bean.search.KeyConfigurationSearchWebControl;
import com.redknee.app.crm.bean.search.KeyConfigurationSearchXInfo;

/**
 * Search border for KeyConfiguration objects.
 * 
 * @author aaron.gourley@redknee.com
 * @since 8.2
 */
public class KeyConfigurationSearchBorder  extends SearchBorder
{
    public KeyConfigurationSearchBorder(Context ctx)
    {
        super(ctx, KeyConfiguration.class, new KeyConfigurationSearchWebControl());

        addAgent(new ContextAgentProxy()
        {
            private static final long serialVersionUID = 1L;

            @Override
            public void execute(Context agentCtx)
               throws AgentException
            {
                KeyConfigurationSearch criteria = (KeyConfigurationSearch)getCriteria(agentCtx);

              
                doSelect(
                		agentCtx,
						new com.redknee.framework.xhome.elang.EQ(KeyConfigurationXInfo.SYSTEM_DEFINED, criteria.isSystemDefined()));
               delegate(agentCtx);
            }
         });
        
        
        

        addAgent(new ContextAgentProxy()
        {
            private static final long serialVersionUID = 1L;

            @Override
            public void execute(Context agentCtx)
               throws AgentException
            {
                KeyConfigurationSearch criteria = (KeyConfigurationSearch)getCriteria(agentCtx);

               if (criteria!= null)
               {
                   Set<KeyValueFeatureEnum> features = new HashSet<KeyValueFeatureEnum>();
                   features.add(criteria.getFeature());

                   if (!KeyValueFeatureEnum.GENERIC.equals(criteria.getFeature()))
                   {
                       // Generic keys apply to all features, so show them in all search results.
                       features.add(KeyValueFeatureEnum.GENERIC);
                   }
                   
                   /**
                    * 
                    * Feature property can be null when the search criteria is loaded for
                    * the first time. By default the criteria property is set to 
                    * --Select All--
                    * 
                    * The enum value for --Select All-- does not exist as it's passed as 
                    * a property to EnumWebControl constructor for default drop down value
                    * and is for display purpose only.
                    * 
                    */
                   switch (criteria.getFeature() == null ? -1 : criteria.getFeature().getIndex())
                   {
                   // Alcatel SSC feature is a hierarchy: Subscription -> Service -> SPID
                   case KeyValueFeatureEnum.ALCATEL_SSC_SUBSCRIPTION_INDEX:
                       features.add(KeyValueFeatureEnum.ALCATEL_SSC_SERVICE);
                   case KeyValueFeatureEnum.ALCATEL_SSC_SERVICE_INDEX:
                       features.add(KeyValueFeatureEnum.ALCATEL_SSC_SPID);
                   }
                   
                   doSelect(
                           agentCtx,
                           new In(KeyConfigurationXInfo.FEATURE, features));
               }
               delegate(agentCtx);
            }
         });

        addAgent(new WildcardSelectSearchAgent(KeyConfigurationXInfo.KEY, KeyConfigurationSearchXInfo.KEY, true));

        addAgent(new WildcardSelectSearchAgent(KeyConfigurationXInfo.USER_FRIENDLY_NAME, KeyConfigurationSearchXInfo.USER_FRIENDLY_NAME, true));
    }

}
