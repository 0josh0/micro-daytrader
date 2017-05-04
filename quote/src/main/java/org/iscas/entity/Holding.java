package org.iscas.entity;

import org.iscas.util.TradeConfig;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by andyren on 2016/6/28.
 */

@Entity(name = "holding")
@Table(name = "holding")
public class Holding implements Serializable {

    @TableGenerator(
            name = "holdingIdGen",
            table = "keygen",
            pkColumnName = "keyname",
            valueColumnName = "keyval",
            pkColumnValue = "holding",
            allocationSize = 1000)
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "holdingIdGen")
    @Column(name = "holdingid", nullable = false)
    @NotNull
    private Integer holdingID;

    @NotNull
    @Column(name = "quantity", nullable = false)
    private Double quantity;

    @Column(name = "purchaseprice")
    private BigDecimal purchasePrice;

    @Column(name = "purchasedate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date purchaseDate;

    @Column(name = "quote_symbol")
    private String quoteSymbol;

    @Column(name = "account_accountid")
    private Integer accountID;


    public Holding() {
    }

    public Holding(Integer holdingID,
                   Double quantity,
                   BigDecimal purchasePrice,
                   Date purchaseDate,
                   String quoteSymbol) {
        setHoldingID(holdingID);
        setQuantity(quantity);
        setPurchasePrice(purchasePrice);
        setPurchaseDate(purchaseDate);
        setQuoteSymbol(quoteSymbol);
    }

    public Holding(Double quantity,
                   BigDecimal purchasePrice,
                   Date purchaseDate,
                   Integer accountID,
                   String quoteSymbol) {
        setQuantity(quantity);
        setPurchasePrice(purchasePrice);
        setPurchaseDate(purchaseDate);
        setAccountID(accountID);
        setQuoteSymbol(quoteSymbol);
    }

    /* random generateed test cases */
    public static Holding getRandomInstance() {
        return new Holding(
                new Integer(TradeConfig.rndInt(100000)),         //holdingID
                (double)TradeConfig.rndQuantity(),               //quantity
                TradeConfig.rndBigDecimal(1000.0f),              //purchasePrice
                new Date(TradeConfig.rndInt(Integer.MAX_VALUE)), //purchaseDate
                TradeConfig.rndSymbol()                          // symbol
        );
    }

    public String toString() {
        return "\n\tHolding Data for holding: " + getHoldingID()
                + "\n\t\t      quantity:" + getQuantity()
                + "\n\t\t purchasePrice:" + getPurchasePrice()
                + "\n\t\t  purchaseDate:" + getPurchaseDate()
                + "\n\t\t       quoteID:" + getQuoteSymbol()
                ;
    }

    public String toHTML() {
        return "<BR>Holding Data for holding: " + getHoldingID() + "</B>"
                + "<LI>      quantity:" + getQuantity() + "</LI>"
                + "<LI> purchasePrice:" + getPurchasePrice() + "</LI>"
                + "<LI>  purchaseDate:" + getPurchaseDate() + "</LI>"
                + "<LI>       quoteID:" + getQuoteSymbol() + "</LI>"
                ;
    }

    public Integer getHoldingID() {
        return holdingID;
    }

    public void setHoldingID(Integer holdingID) {
        this.holdingID = holdingID;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getQuoteSymbol() {
        return quoteSymbol;
    }

    public void setQuoteSymbol(String quoteSymbol) {
        this.quoteSymbol = quoteSymbol;
    }

    public Integer getAccountID() {
        return accountID;
    }

    public void setAccountID(Integer accountID) {
        this.accountID = accountID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (this.holdingID != null ? this.holdingID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Holding)) {
            return false;
        }
        Holding other = (Holding) object;
        if (this.holdingID != other.holdingID && (this.holdingID == null || !this.holdingID.equals(other.holdingID)))
            return false;
        return true;
    }
}
