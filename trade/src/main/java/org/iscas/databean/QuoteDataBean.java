package org.iscas.databean;

import org.iscas.util.FinancialUtils;
import org.iscas.util.TradeConfig;

import java.math.BigDecimal;

//quotes&Trade面板信息封装bean

public class QuoteDataBean {
    private BigDecimal currentPrice;
    private BigDecimal openPrice;
    private String symbol;
    private BigDecimal high;
    private BigDecimal low;
    private String companyName;
    private double volume;
    private double change;
    private String range;
    private BigDecimal gainPercent;
    private BigDecimal gain;    


    public QuoteDataBean(BigDecimal price, BigDecimal open, String symbol, BigDecimal high, BigDecimal low, String companyName, Double volume, Double change){
        this.currentPrice = price;
        this.openPrice = open;
        this.symbol = symbol;
        this.high = high;
        this.low = low;
        this.companyName = companyName;
        this.volume = volume;
        this.change = change;
        this.range = high.toString() + "-" + low.toString();
        this.gainPercent = FinancialUtils.computeGainPercent(price, open);
        this.gain = TradeConfig.rndBigDecimal(1000.0f);
    }

    public static QuoteDataBean getRandomInstance() {
        return new QuoteDataBean(
                TradeConfig.rndBigDecimal(1000.0f),     //price
                TradeConfig.rndBigDecimal(1000.0f),     //open1
                TradeConfig.rndSymbol(),                 //symbol
                TradeConfig.rndBigDecimal(1000.0f),     //low
                TradeConfig.rndBigDecimal(1000.0f),     //high
                TradeConfig.rndSymbol() + " Incorporated",         //Company Name

                Double.parseDouble(String.valueOf(TradeConfig.rndFloat(100000))) ,            //volume
                Double.parseDouble(String.valueOf(TradeConfig.rndFloat(100000)))           //change
        );
    }

    @Override
    public String toString() {
        return "QuoteDataBean{" +
                "currentPrice=" + currentPrice +
                ", openPrice=" + openPrice +
                ", symbol='" + symbol + '\'' +
                ", high=" + high +
                ", low=" + low +
                ", companyName='" + companyName + '\'' +
                ", volume=" + volume +
                ", change=" + change +
                ", range='" + range + '\'' +
                ", gainPercent=" + gainPercent +
                ", gain=" + gain +
                '}';
    }

    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
    }

    public BigDecimal getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(BigDecimal openPrice) {
        this.openPrice = openPrice;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
    
    public String getSymbol() {
        return symbol;
    }

    public void setHigh(BigDecimal high) {
        this.high = high;
    }

    public BigDecimal getHigh() {
        return high;
    }

    public void setLow(BigDecimal low) {
        this.low = low;
    }

    public BigDecimal getLow() {
        return low;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public double getVolume() {
        return volume;
    }

    public void setChange(double change) {
        this.change = change;
    }

    public double getChange() {
        return change;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public String getRange() {
        return range;
    }

    public void setGainPercent(BigDecimal gainPercent) {
        this.gainPercent = gainPercent;
    }

    public BigDecimal getGainPercent() {
        return gainPercent;
    }
   

	public void setGain(BigDecimal gain) {
		this.gain = gain;
	}

	public BigDecimal getGain() {
		return gain;
	}
}
