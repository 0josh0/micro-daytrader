package org.iscas.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by andyren on 2016/6/27.
 */

@Entity(name = "orders")
@Table(name = "orders")
public class Orders implements Serializable {

    @TableGenerator(
            name = "orderIdGen",
            table = "keygen",
            pkColumnName = "keyname",
            valueColumnName = "keyval",
            pkColumnValue = "order",
            allocationSize = 1000)
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "orderIdGen")
    @Column(name = "orderid", nullable = false)
    @NotNull
    private Integer orderID;

    @Column(name = "ordertype")
    private String orderType;

    @Column(name = "orderstatus")
    private String orderStatus;         /* orderStatus (open, processing, completed, closed, cancelled) */

    @Column(name = "opendate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date openDate;              /* openDate (when the order was entered) */

    @Column(name = "completiondate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date completionDate;

    @Column(name = "quantity", nullable = false)
    @NotNull
    private Double quantity;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "orderfee")
    private BigDecimal orderFee;

    @Column(name = "quote_symbol")
    private String quoteSymbol;

    @Column(name = "account_accountid")
    private Integer accountID;


    @Column(name = "holding_holdingid")
    private Integer holdingID;

    public Orders() {
    }

    public Orders(Integer orderID,
                  String orderType,
                  String orderStatus,
                  Date openDate,
                  Date completionDate,
                  Double quantity,
                  BigDecimal price,
                  BigDecimal orderFee,
                  String quoteSymbol
    ) {
        setOrderID(orderID);
        setOrderType(orderType);
        setOrderStatus(orderStatus);
        setOpenDate(openDate);
        setCompletionDate(completionDate);
        setQuantity(quantity);
        setPrice(price);
        setOrderFee(orderFee);
        setQuoteSymbol(quoteSymbol);
    }

    public Orders(String orderType,
                  String orderStatus,
                  Date openDate,
                  Date completionDate,
                  Double quantity,
                  BigDecimal price,
                  BigDecimal orderFee,
                  Integer accountID,
                  String quoteSymbol, Integer holdingID) {
        setOrderType(orderType);
        setOrderStatus(orderStatus);
        setOpenDate(openDate);
        setCompletionDate(completionDate);
        setQuantity(quantity);
        setPrice(price);
        setOrderFee(orderFee);
        setAccountID(accountID);
        setQuoteSymbol(quoteSymbol);
        setHoldingID(holdingID);
    }

    public String toString() {
        return "Orders " + getOrderID()
                + "\n\t      orderType: " + getOrderType()
                + "\n\t    orderStatus: " + getOrderStatus()
                + "\n\t       openDate: " + getOpenDate()
                + "\n\t completionDate: " + getCompletionDate()
                + "\n\t       quantity: " + getQuantity()
                + "\n\t          price: " + getPrice()
                + "\n\t       orderFee: " + getOrderFee()
                + "\n\t         symbol: " + getQuoteSymbol()
                ;
    }

    public String toHTML() {
        return "<BR>Orders <B>" + getOrderID() + "</B>"
                + "<LI>      orderType: " + getOrderType() + "</LI>"
                + "<LI>    orderStatus: " + getOrderStatus() + "</LI>"
                + "<LI>       openDate: " + getOpenDate() + "</LI>"
                + "<LI> completionDate: " + getCompletionDate() + "</LI>"
                + "<LI>       quantity: " + getQuantity() + "</LI>"
                + "<LI>          price: " + getPrice() + "</LI>"
                + "<LI>       orderFee: " + getOrderFee() + "</LI>"
                + "<LI>         symbol: " + getQuoteSymbol() + "</LI>"
                ;
    }

    public Integer getOrderID() {
        return orderID;
    }

    public void setOrderID(Integer orderID) {
        this.orderID = orderID;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Date getOpenDate() {
        return openDate;
    }

    public void setOpenDate(Date openDate) {
        this.openDate = openDate;
    }

    public Date getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(Date completionDate) {
        this.completionDate = completionDate;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }


    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getOrderFee() {
        return orderFee;
    }

    public void setOrderFee(BigDecimal orderFee) {
        this.orderFee = orderFee;
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

    public Integer getHoldingID() {
        return holdingID;
    }

    public void setHoldingID(Integer holdingID) {
        this.holdingID = holdingID;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (this.orderID != null ? this.orderID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Orders)) {
            return false;
        }
        Orders other = (Orders) object;
        if (this.orderID != other.orderID && (this.orderID == null || !this.orderID.equals(other.orderID)))
            return false;
        return true;
    }
}

