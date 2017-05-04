package org.iscas.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.iscas.util.TradeConfig;
import org.iscas.util.Log;

/**
 * Created by andyren on 2016/6/28.
 */

@Entity(name = "holding")
@Table(name = "holding")
@NamedQueries( {
        @NamedQuery(name = "holding.findByPurchaseprice", query = "SELECT h FROM holding h WHERE h.purchasePrice = :purchaseprice"),
        @NamedQuery(name = "holding.findByHoldingid", query = "SELECT h FROM holding h WHERE h.holdingID = :holdingid"),
        @NamedQuery(name = "holding.findByQuantity", query = "SELECT h FROM holding h WHERE h.quantity = :quantity"),
        @NamedQuery(name = "holding.findByPurchasedate", query = "SELECT h FROM holding h WHERE h.purchaseDate = :purchasedate"),
        @NamedQuery(name = "holding.holdingsByUserID", query = "SELECT h FROM holding h where h.account.profile.userID = :userID")
    })
public class Holding implements Serializable {

    /* persistent/relationship fields */

    @TableGenerator(
            name="holdingIdGen",
            table="keygenejb",
            pkColumnName="KEYNAME",
            valueColumnName="KEYVAL",
            pkColumnValue="holding",
            allocationSize=1000)
    @Id
    @GeneratedValue(strategy=GenerationType.TABLE, generator="holdingIdGen")
    @Column(name = "HOLDINGID", nullable = false)
    @NotNull
    private Integer holdingID;              /* holdingID */
    
    @NotNull
    @Column(name = "QUANTITY", nullable = false)
    private double quantity;                /* quantity */
    
    @Column(name = "PURCHASEPRICE")
    private BigDecimal purchasePrice;       /* purchasePrice */
    
    @Column(name = "PURCHASEDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date purchaseDate;              /* purchaseDate */
    
    @Transient
    private String quoteID;                 /* Holding(*)  ---> Quote(1) */
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="ACCOUNT_ACCOUNTID")
    private Account account;
    
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "QUOTE_SYMBOL")
    private Quote quote;

//    @Version
//    private Integer optLock;

    public Holding() {
    }

    public Holding(Integer holdingID,
                   double quantity,
                   BigDecimal purchasePrice,
                   Date purchaseDate,
                   String quoteID) {
        setHoldingID(holdingID);
        setQuantity(quantity);
        setPurchasePrice(purchasePrice);
        setPurchaseDate(purchaseDate);
        setQuoteID(quoteID);
    }

    public Holding(double quantity,
                   BigDecimal purchasePrice,
                   Date purchaseDate,
                   Account account,
                   Quote quote) {
        setQuantity(quantity);
        setPurchasePrice(purchasePrice);
        setPurchaseDate(purchaseDate);
        setAccount(account);
        setQuote(quote);
    }

    public static Holding getRandomInstance() {
        return new Holding(
                new Integer(TradeConfig.rndInt(100000)),     //holdingID
                TradeConfig.rndQuantity(),                     //quantity
                TradeConfig.rndBigDecimal(1000.0f),             //purchasePrice
                new Date(TradeConfig.rndInt(Integer.MAX_VALUE)), //purchaseDate
                TradeConfig.rndSymbol()                        // symbol
        );
    }

    public String toString() {
        return "\n\tHolding Data for holding: " + getHoldingID()
                + "\n\t\t      quantity:" + getQuantity()
                + "\n\t\t purchasePrice:" + getPurchasePrice()
                + "\n\t\t  purchaseDate:" + getPurchaseDate()
                + "\n\t\t       quoteID:" + getQuoteID()
                ;
    }

    public String toHTML() {
        return "<BR>Holding Data for holding: " + getHoldingID() + "</B>"
                + "<LI>      quantity:" + getQuantity() + "</LI>"
                + "<LI> purchasePrice:" + getPurchasePrice() + "</LI>"
                + "<LI>  purchaseDate:" + getPurchaseDate() + "</LI>"
                + "<LI>       quoteID:" + getQuoteID() + "</LI>"
                ;
    }

    public void print() {
        Log.log(this.toString());
    }

    public Integer getHoldingID() {
        return holdingID;
    }

    public void setHoldingID(Integer holdingID) {
        this.holdingID = holdingID;
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

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getQuoteID() {
        if (quote != null) {
            return quote.getSymbol();
        }
        return quoteID;
    }

    public void setQuoteID(String quoteID) {
        this.quoteID = quoteID;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    /* Disabled for D185273
     public String getSymbol() {
         return getQuoteID();
     }
     */
    
    public Quote getQuote() {
        return quote;
    }

    public void setQuote(Quote quote) {
        this.quote = quote;
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
        if (this.holdingID != other.holdingID && (this.holdingID == null || !this.holdingID.equals(other.holdingID))) return false;
        return true;
    }
}
