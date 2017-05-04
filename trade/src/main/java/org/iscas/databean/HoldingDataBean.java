package org.iscas.databean;

import java.math.BigDecimal;
import java.util.Date;

//Portfolio面板上的信息封装bean，持股信息

public class HoldingDataBean {
    private Integer holdingID;
    private Date purchaseDate;
    private String symbol;
    private double quantity;
    private BigDecimal purchasePrice;
    private BigDecimal currentPrice;
    private BigDecimal purchasePasis;
    private BigDecimal marketValue;
    private BigDecimal gain;

    public Integer getHoldingID() {
        return holdingID;
    }

    public void setHoldingID(Integer holdingID) {
        this.holdingID = holdingID;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
    }

    public BigDecimal getPurchasePasis() {
        return purchasePasis;
    }

    public void setPurchasePasis(BigDecimal purchasePasis) {
        this.purchasePasis = purchasePasis;
    }

    public BigDecimal getMarketValue() {
        return marketValue;
    }

    public void setMarketValue(BigDecimal marketValue) {
        this.marketValue = marketValue;
    }

    public BigDecimal getGain() {
        return gain;
    }

    public void setGain(BigDecimal gain) {
        this.gain = gain;
    }
}
