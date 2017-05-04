package org.iscas.databean;

import org.iscas.entity.Quote;
import org.iscas.util.FinancialUtils;
import org.iscas.util.Log;
import org.iscas.util.TradeConfig;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

//大盘面板信息的封装bean，Home

public class MarketSummaryDataBean implements Serializable {

    private BigDecimal TSIA;            /* Trade Stock Index Average */
    private BigDecimal openTSIA;        /* Trade Stock Index Average at the open */
    private double volume;         /* volume of shares traded */
    private Collection topGainers;        /* Collection of top gaining stocks */
    private Collection topLosers;        /* Collection of top losing stocks */
    //FUTURE private Collection     topVolume;        /* Collection of top stocks by volume */        
    private Date summaryDate;   /* Date this summary was taken */

    //cache the gainPercent once computed for this bean
    private BigDecimal gainPercent = null;

    public MarketSummaryDataBean() {
    }

    public MarketSummaryDataBean(BigDecimal TSIA,
                                 BigDecimal openTSIA,
                                 double volume,
                                 Collection topGainers,
                                 Collection topLosers//, Collection topVolume
    ) {
        setTSIA(TSIA);
        setOpenTSIA(openTSIA);
        setVolume(volume);
        setTopGainers(topGainers);
        setTopLosers(topLosers);
        setSummaryDate(new java.sql.Date(System.currentTimeMillis()));
        gainPercent = FinancialUtils.computeGainPercent(getTSIA(), getOpenTSIA());

    }

    public static MarketSummaryDataBean getRandomInstance() {
        Collection gain = new ArrayList();
        Collection lose = new ArrayList();

        for (int ii = 0; ii < 5; ii++) {
            QuoteDataBean quote1 = QuoteDataBean.getRandomInstance();
            QuoteDataBean quote2 = QuoteDataBean.getRandomInstance();

            gain.add(quote1);
            lose.add(quote2);
        }

        return new MarketSummaryDataBean(
                TradeConfig.rndBigDecimal(1000000.0f),
                TradeConfig.rndBigDecimal(1000000.0f),
                TradeConfig.rndQuantity(),
                gain,
                lose
        );
    }

    public String toString() {
        String ret = "\n\tMarket Summary at: " + getSummaryDate()
                + "\n\t\t        TSIA:" + getTSIA()
                + "\n\t\t    openTSIA:" + getOpenTSIA()
                + "\n\t\t        gain:" + getGainPercent()
                + "\n\t\t      volume:" + getVolume();

        if ((getTopGainers() == null) || (getTopLosers() == null))
            return ret;
        ret += "\n\t\t   Current Top Gainers:";
        Iterator it = getTopGainers().iterator();
        while (it.hasNext()) {
            Quote quoteData = (Quote) it.next();
            ret += ("\n\t\t\t" + quoteData.toString());
        }
        ret += "\n\t\t   Current Top Losers:";
        it = getTopLosers().iterator();
        while (it.hasNext()) {
            Quote quoteData = (Quote) it.next();
            ret += ("\n\t\t\t" + quoteData.toString());
        }
        return ret;
    }

    public String toHTML() {
        String ret = "<BR>Market Summary at: " + getSummaryDate()
                + "<LI>        TSIA:" + getTSIA() + "</LI>"
                + "<LI>    openTSIA:" + getOpenTSIA() + "</LI>"
                + "<LI>      volume:" + getVolume() + "</LI>";
        if ((getTopGainers() == null) || (getTopLosers() == null))
            return ret;
        ret += "<BR> Current Top Gainers:";
        Iterator it = getTopGainers().iterator();
        while (it.hasNext()) {
            Quote quoteData = (Quote) it.next();
            ret += ("<LI>" + quoteData.toString() + "</LI>");
        }
        ret += "<BR>   Current Top Losers:";
        it = getTopLosers().iterator();
        while (it.hasNext()) {
            Quote quoteData = (Quote) it.next();
            ret += ("<LI>" + quoteData.toString() + "</LI>");
        }
        return ret;
    }

    public void print() {
        Log.log(this.toString());
    }

    public BigDecimal getGainPercent() {
        if (gainPercent == null)
            gainPercent = FinancialUtils.computeGainPercent(getTSIA(), getOpenTSIA());
        return gainPercent;
    }

    public void setGainPercent(BigDecimal gainPercent) {
        this.gainPercent = gainPercent;
    }

    public BigDecimal getTSIA() {
        return TSIA;
    }

    public void setTSIA(BigDecimal tSIA) {
        TSIA = tSIA;
    }


    public BigDecimal getOpenTSIA() {
        return openTSIA;
    }

    public void setOpenTSIA(BigDecimal openTSIA) {
        this.openTSIA = openTSIA;
    }


    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }


    public Collection getTopGainers() {
        return topGainers;
    }

    public void setTopGainers(Collection topGainers) {
        this.topGainers = topGainers;
    }


    public Collection getTopLosers() {
        return topLosers;
    }

    public void setTopLosers(Collection topLosers) {
        this.topLosers = topLosers;
    }


    public Date getSummaryDate() {
        return summaryDate;
    }

    public void setSummaryDate(Date summaryDate) {
        this.summaryDate = summaryDate;
    }

}
