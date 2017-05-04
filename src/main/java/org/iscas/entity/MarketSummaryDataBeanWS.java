package org.iscas.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

import org.iscas.util.Log;
import org.iscas.util.FinancialUtils;

/**
 * This is a duplicate MarketSummaryDataBean to handle web service handling
 * of collections.  Instead this class uses typed arrays.
 *
 * Created by andyren on 2016/6/28.
 */
public class MarketSummaryDataBeanWS implements Serializable
{

    private BigDecimal     TSIA;            /* Trade Stock Index Average */
    private BigDecimal     openTSIA;        /* Trade Stock Index Average at the open */    
    private double      volume;         /* volume of shares traded */
    private Quote     topGainers[];        /* Collection of top gaining stocks */
    private Quote    topLosers[];        /* Collection of top losing stocks */
    private Date            summaryDate;   /* Date this summary was taken */
    
    //cache the gainPercent once computed for this bean
    private BigDecimal  gainPercent=null;

    public MarketSummaryDataBeanWS(){ }
    public MarketSummaryDataBeanWS(BigDecimal TSIA,
                            BigDecimal  openTSIA,
                            double        volume,
                            Quote[]     topGainers,
                            Quote[]     topLosers//, Collection topVolume
                            )
    {
        setTSIA(TSIA);
        setOpenTSIA(openTSIA);
        setVolume(volume);
        setTopGainers(topGainers);
        setTopLosers(topLosers);
        setSummaryDate(new java.sql.Date(System.currentTimeMillis()));
        gainPercent = FinancialUtils.computeGainPercent(getTSIA(), getOpenTSIA());
        
    }
    

    public String toString()
    {
        String ret = "\n\tMarket Summary at: " + getSummaryDate()
            + "\n\t\t        TSIA:" + getTSIA()
            + "\n\t\t    openTSIA:" + getOpenTSIA()
            //+ "\n\t\t        gain:" + getGainPercent()
            + "\n\t\t      volume:" + getVolume()
            ;

        if ( (getTopGainers()==null) || (getTopLosers()==null) )
            return ret;
        ret += "\n\t\t   Current Top Gainers:";
        for (int ii = 0; ii < topGainers.length; ii++) {
            Quote quoteData = topGainers[ii];
            ret += ( "\n\t\t\t"  + quoteData.toString() );
        }
        ret += "\n\t\t   Current Top Losers:";
        for (int ii = 0; ii < topLosers.length; ii++) {
            Quote quoteData = topLosers[ii];
            ret += ( "\n\t\t\t"  + quoteData.toString() );
        }
        return ret;        
    }
    public String toHTML()
    {
        String ret = "<BR>Market Summary at: " + getSummaryDate()
            + "<LI>        TSIA:" + getTSIA() + "</LI>"
            + "<LI>    openTSIA:" + getOpenTSIA() + "</LI>"
            + "<LI>      volume:" + getVolume() + "</LI>"
            ;
        if ( (getTopGainers()==null) || (getTopLosers()==null) )
            return ret;
        ret += "<BR> Current Top Gainers:";
        for (int ii = 0; ii < topGainers.length; ii++) {
            Quote quoteData = topGainers[ii];
            ret += ( "<LI>"  + quoteData.toString()  + "</LI>" );
        }
        ret += "<BR>   Current Top Losers:";
        for (int ii = 0; ii < topLosers.length; ii++) {
            Quote quoteData = topLosers[ii];
            ret += ( "<LI>"  + quoteData.toString()  + "</LI>" );
        }
        return ret;
    }
    public void print()
    {
        Log.log( this.toString() );
    }    
    
    
    /* Disabled for D185273
    public BigDecimal getGainPercent()
    {
        if ( gainPercent == null )
            gainPercent = FinancialUtils.computeGainPercent(getTSIA(), getOpenTSIA());
        return gainPercent;
    }
    */


    /**
     * Gets the tSIA
     * @return Returns a BigDecimal
     */
    public BigDecimal getTSIA() {
        return TSIA;
    }
    /**
     * Sets the tSIA
     * @param tSIA The tSIA to set
     */
    public void setTSIA(BigDecimal tSIA) {
        TSIA = tSIA;
    }

    /**
     * Gets the openTSIA
     * @return Returns a BigDecimal
     */
    public BigDecimal getOpenTSIA() {
        return openTSIA;
    }
    /**
     * Sets the openTSIA
     * @param openTSIA The openTSIA to set
     */
    public void setOpenTSIA(BigDecimal openTSIA) {
        this.openTSIA = openTSIA;
    }

    /**
     * Gets the volume
     * @return Returns a BigDecimal
     */
    public double getVolume() {
        return volume;
    }
    /**
     * Sets the volume
     * @param volume The volume to set
     */
    public void setVolume(double volume) {
        this.volume = volume;
    }

    /**
     * Gets the topGainers
     * @return Returns a Collection
     */
    public Quote[] getTopGainers() {
        return topGainers;
    }
    /**
     * Sets the topGainers
     * @param topGainers The topGainers to set
     */
    public void setTopGainers(Quote[] topGainers) {
        this.topGainers = topGainers;
    }

    /**
     * Gets the topLosers
     * @return Returns a Collection
     */
    public Quote[] getTopLosers() {
        return topLosers;
    }
    /**
     * Sets the topLosers
     * @param topLosers The topLosers to set
     */
    public void setTopLosers(Quote[] topLosers) {
        this.topLosers = topLosers;
    }

    /**
     * Gets the summaryDate
     * @return Returns a Date
     */
    public Date getSummaryDate() {
        return summaryDate;
    }
    /**
     * Sets the summaryDate
     * @param summaryDate The summaryDate to set
     */
    public void setSummaryDate(Date summaryDate) {
        this.summaryDate = summaryDate;
    }
    
    public static MarketSummaryDataBeanWS convertBean(MarketSummaryDataBean origBean) {
        Collection gainCol = origBean.getTopGainers();
        Quote gain[] = (Quote[])gainCol.toArray(new Quote[0]);
        Collection loseCol = origBean.getTopLosers();
        Quote lose[] = (Quote[])loseCol.toArray(new Quote[0]);
        return new MarketSummaryDataBeanWS(origBean.getTSIA(), origBean.getOpenTSIA(), origBean.getVolume(), gain, lose);
    }

}
