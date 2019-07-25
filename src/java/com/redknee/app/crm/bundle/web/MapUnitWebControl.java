package com.redknee.app.crm.bundle.web;


import com.redknee.app.crm.bean.core.BundleProfile;
import com.redknee.app.crm.bundle.BundleCategoryAssociation;
import com.redknee.app.crm.bundle.UnitTypeEnum;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.webcontrol.AbstractWebControl;
import com.redknee.framework.xhome.webcontrol.LongWebControl;
import com.redknee.framework.xhome.webcontrol.ProxyWebControl;
import com.redknee.framework.xlog.log.MinorLogMsg;

/**
 * This webcontrol converts the input based on the UnitType associated with the selected
 * bundle category of the bundle profile.
 * @author Candy Wong
 */
public class MapUnitWebControl
   extends ProxyWebControl
{
   // If this key is set to true in the context, we disable the unit conversion
   public static final String DISABLE_UNIT_CONVERSION = "disable unit conversion";
   

   public MapUnitWebControl(int width)
   {
      this(width, "", false);
   }

   public MapUnitWebControl(int width, String textAfter, boolean disableUnitConversion)
   {
      super(new LongWebControl(width));
      textAfter_ = textAfter;
      disableUnitConversion_ = disableUnitConversion;
   }

   public void toWeb(Context ctx, java.io.PrintWriter out, String name, Object obj)
   {
      Object abstractBean = ctx.get(AbstractWebControl.BEAN);
      if (abstractBean != null && abstractBean instanceof BundleProfile &&  ((BundleProfile) abstractBean).isCurrency())
      {
          com.redknee.framework.core.web.XCurrencyWebControl.instance().toWeb(ctx, out, name, obj);
          return;
      }
      
      UnitTypeEnum unit = getUnitType(ctx);
      long value = ((Number) obj).longValue();
      String unitsDescription = "";
      
      if (unit != null)
      {
          if (! ctx.getBoolean(DISABLE_UNIT_CONVERSION, false) && !disableUnitConversion_)
          {
                value = ((Number) obj).longValue() / unit.getMultiplier(ctx);
    
                unitsDescription = unit.getUnits();
          }
          else
          {
                unitsDescription = unit.getBasicUnit();
          }
      }

      super.toWeb(ctx, out, name, Long.valueOf(value));

      if (unit != null)
      {
          if (textAfter_ != null && textAfter_.length()>0)
          {
              out.print(textAfter_ + " ");
          }
          
          out.print(unitsDescription);
      }
   }

   public Object fromWeb(Context ctx, javax.servlet.ServletRequest req, String name)
   {
       Long input = null;
       Object abstractBean = ctx.get(AbstractWebControl.BEAN);
       if (abstractBean != null && abstractBean instanceof BundleProfile &&  ((BundleProfile) abstractBean).isCurrency())
       {
           input = (Long) com.redknee.framework.core.web.XCurrencyWebControl.instance().fromWeb(ctx, req, name);
       }
       else
       {    	   
           input = (Long)super.fromWeb(ctx, req, name);
           if (!ctx.getBoolean(DISABLE_UNIT_CONVERSION, false) && !disableUnitConversion_)
           {
              // convert the input using the multiplier
              UnitTypeEnum unit = getUnitType(ctx);
              if (unit != null)
              {
                 long value = input.longValue();
                 
                 if (value > Long.MAX_VALUE/(10*unit.getMultiplier(ctx)))
                 {
                     throw new IllegalArgumentException("Value cannot be greater than " + Long.MAX_VALUE/(10*unit.getMultiplier(ctx)));
                 }

                 return Long.valueOf(value * unit.getMultiplier(ctx));
              }
           }
       }
     
      return input;
   }
   

   public UnitTypeEnum getUnitType(Context ctx)
   {
       Object obj = ctx.get(AbstractWebControl.BEAN);
       if (obj instanceof BundleProfile)
       {
           return getUnitType(ctx, (BundleProfile) obj);
       }
       else if (obj instanceof BundleCategoryAssociation)
       {
           return getUnitType(ctx, (BundleCategoryAssociation) obj);
       }
       else
       {
           return null;
       }
       
   }
   

   public UnitTypeEnum getUnitType(Context ctx, BundleProfile bundle)
   {
        if (bundle.getType() == -1)
        {
            return null;
        }

        try
        {
            UnitTypeEnum unitType = UnitTypeEnum.get((short) bundle.getType());
            return unitType;
        }
        catch (Throwable t)
        {
            /*
             * If bundle is being created (id=0), it may not have a bundle type
             * defined yet. No need to log an exception in this case, unless a
             * category is already associated to it.
             */
            if (bundle.getBundleId() > 0 || bundle.getBundleCategoryIds().size() > 0)
            {
                new MinorLogMsg(this, "unable to discover unit type", t).log(ctx);
            }
            return null;
        }
   }

   public UnitTypeEnum getUnitType(Context ctx, BundleCategoryAssociation categoryAssociation)
   {
        if (categoryAssociation.getType() == -1)
        {
            return null;
        }

        try
        {
            UnitTypeEnum unitType = UnitTypeEnum.get((short) categoryAssociation.getType());
            return unitType;
        }
        catch (Throwable t)
        {
            return null;
        }
   }

   private String textAfter_;

   private boolean disableUnitConversion_;
}
