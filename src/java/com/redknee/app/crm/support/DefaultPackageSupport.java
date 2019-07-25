package com.redknee.app.crm.support;

import com.redknee.app.crm.bean.GSMPackageHome;
import com.redknee.app.crm.bean.GSMPackageXInfo;
import com.redknee.app.crm.bean.PackageStateEnum;
import com.redknee.app.crm.bean.TDMAPackageHome;
import com.redknee.app.crm.bean.TDMAPackageID;
import com.redknee.app.crm.bean.TDMAPackageXInfo;
import com.redknee.app.crm.bean.VSATPackageHome;
import com.redknee.app.crm.bean.core.GSMPackage;
import com.redknee.app.crm.bean.core.TDMAPackage;
import com.redknee.app.crm.bean.core.VSATPackage;
import com.redknee.app.crm.numbermgn.GenericPackage;
import com.redknee.app.crm.technology.TechnologyEnum;
import com.redknee.framework.xhome.context.Context;
import com.redknee.framework.xhome.elang.And;
import com.redknee.framework.xhome.elang.EQ;
import com.redknee.framework.xhome.home.Home;
import com.redknee.framework.xhome.home.HomeException;
import com.redknee.framework.xhome.home.HomeInternalException;
import com.redknee.framework.xlog.log.InfoLogMsg;


/**
 * @author jchen This support class is not a good choice for the model. New Package Types
 *         (GSM, VSAT etc) get added and the support for them may be missed. To reduce the
 *         chances of missing support of new Package Types, making this support class
 *         implement Package-Processor.
 */
public class DefaultPackageSupport implements PackageSupport
{

    protected static PackageSupport instance_ = null;


    public static PackageSupport instance()
    {
        if (instance_ == null)
        {
            instance_ = new DefaultPackageSupport();
        }
        return instance_;
    }


    protected DefaultPackageSupport()
    {
    }

    /**
     * {@inheritDoc}
     */
    public String retrieveIMSIorMIN(Context ctx, String packageId, TechnologyEnum technology, int spid) throws HomeException
    {
        if (TechnologyEnum.GSM == technology)
        {
            final GSMPackage pack = this.getGSMPackage(ctx, packageId);
            return pack.getIMSI();
        }
        else if (TechnologyEnum.TDMA == technology || TechnologyEnum.CDMA == technology)
        {
            final TDMAPackage pack = this.getTDMAPackage(ctx, packageId, spid);
            return pack.getMin();
        }
        else
        {
            return null;
        }
    }    

    
    /**
     * Gets the package of the given technology type and identifer.
     * 
     * @param context
     *            The operating context.
     * @param technology
     *            The type of card technology.
     * @param identifier
     *            The identifier of the package.
     */
    public GenericPackage getPackage(final Context context, final TechnologyEnum technology, final String identifier, int spid)
            throws HomeException
    {
        final GenericPackage card;
        if (technology == TechnologyEnum.GSM)
        {
            card = getGSMPackage(context, identifier);
        }
        else if (technology == TechnologyEnum.TDMA || technology == TechnologyEnum.CDMA)
        {
            card = getTDMAPackage(context, identifier, spid);
        }
        else if (technology == TechnologyEnum.VSAT_PSTN)
        {
            card = getVSATPackage(context, identifier);
        }
        else
        {
            throw new IllegalStateException("Technology type " + technology + " not supported");
        }
        return card;
    }


    public TDMAPackage getTDMAPackage(Context ctx, final String number, int spid) throws HomeException
    {

        TDMAPackageID tdmaPackageId = new TDMAPackageID(number, spid);

        return HomeSupportHelper.get(ctx).findBean(ctx, TDMAPackage.class, tdmaPackageId);
    }


    public VSATPackage getVSATPackage(Context ctx, final String number) throws HomeException
    {
        final Home home = (Home) ctx.get(VSATPackageHome.class);
        return (VSATPackage) home.find(ctx, number);
    }


    public GSMPackage getGSMPackage(Context ctx, final String number) throws HomeException
    {
        final Home home = (Home) ctx.get(GSMPackageHome.class);
        return (GSMPackage) home.find(ctx, number);
    }


    public void setPackageState(final Context ctx, final String number, TechnologyEnum tech, final int newState, int spid)
            throws HomeException, HomeInternalException
    {
        if (TechnologyEnum.GSM == tech)
        {
            GSMPackage pk = getGSMPackage(ctx, number);
            if (pk != null)
            {
                pk.setState(PackageStateEnum.get((short) newState));
                final Home packageHome = (Home) ctx.get(GSMPackageHome.class);
                packageHome.store(ctx, pk);
            }
            else
            {
                new InfoLogMsg(PackageSupport.class.getName(), "GSM Package for number [" + number
                        + "] does not exist. No use to set it's state to [" + newState + " ]", null).log(ctx);
            }
        }
        else if (TechnologyEnum.TDMA == tech || TechnologyEnum.CDMA == tech)
        {
            TDMAPackage pk = getTDMAPackage(ctx, number, spid);
            if (pk != null)
            {
                pk.setState(PackageStateEnum.get((short) newState));
                final Home packageHome = (Home) ctx.get(TDMAPackageHome.class);
                packageHome.store(ctx, pk);
            }
            else
            {
                new InfoLogMsg(PackageSupport.class.getName(), "TDMA-CDMA Package for number [" + number
                        + "] does not exist. No use to set it's state to [" + newState + " ]", null).log(ctx);
            }
        }
        else if (TechnologyEnum.VSAT_PSTN == tech)
        {
            VSATPackage pk = getVSATPackage(ctx, number);
            if (pk != null)
            {
                pk.setState(PackageStateEnum.get((short) newState));
                final Home packageHome = (Home) ctx.get(VSATPackageHome.class);
                packageHome.store(ctx, pk);
            }
            else
            {
                new InfoLogMsg(PackageSupport.class.getName(), "VSAT Package for number [" + number
                        + "] does not exist. No use to set it's state to [" + newState + " ]", null).log(ctx);
            }
        }
    }


    /**
     * Some parts of the code, like the Subscriber.IMSI field, treat the GSM IMSI as
     * interchangable with the TDMA/CDMA MIN field. This method abstracts the specifics of
     * searching the card package tables for an entry that matches IMSI or MIN.
     * 
     * @param context
     *            The operating context.
     * @param technology
     *            The car package technology.
     * @param imsiOrMin
     *            The IMSI or MIN on which to find.
     * @return The package that matches the IMSI or MIN; or null if no such card package
     *         is found.
     * 
     * @exception HomeException
     *                Thrown if there are problems accessing the Home data in the context.
     */
    public GenericPackage lookupPackageForIMSIOrMIN(final Context context, final TechnologyEnum technology,
            final String imsiOrMin, final int spid) throws HomeException
    {
        final Home home;
        And filter = new And();
        if (TechnologyEnum.GSM == technology)
        {
            home = (Home) context.get(GSMPackageHome.class);
            filter = filter.add(new EQ(GSMPackageXInfo.IMSI, imsiOrMin));
            filter = filter.add(new EQ(GSMPackageXInfo.SPID, spid));
        }
        else if (TechnologyEnum.TDMA == technology || TechnologyEnum.CDMA == technology)
        {
            home = (Home) context.get(TDMAPackageHome.class);
            filter = filter.add(new EQ(TDMAPackageXInfo.MIN, imsiOrMin));
            filter = filter.add(new EQ(TDMAPackageXInfo.SPID, spid));
        }
        else
        {
            throw new IllegalStateException("Unexpected technology type: " + technology);
        }
        final GenericPackage card = (GenericPackage) home.find(context, filter);
        return card;
    }


    /**
     * @param ctx
     * @param technologyType
     * @return
     */
    public Home returnPackageHomeBasedOnTechnology(Context ctx, TechnologyEnum technologyType)
    {
        Home packageHome = null;
        if (technologyType.equals(TechnologyEnum.GSM))
        {
            packageHome = (Home) ctx.get(GSMPackageHome.class);
        }
        else if (technologyType.equals(TechnologyEnum.TDMA) || technologyType.equals(TechnologyEnum.CDMA))
        {
            packageHome = (Home) ctx.get(TDMAPackageHome.class);
        }
        else if (technologyType.equals(TechnologyEnum.VSAT_PSTN))
        {
            packageHome = (Home) ctx.get(VSATPackageHome.class);
        }
        return packageHome;
    }
}
