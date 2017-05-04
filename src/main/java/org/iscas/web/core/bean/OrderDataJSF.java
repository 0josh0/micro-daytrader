package org.iscas.web.core.bean;

/*
import org.apache.geronimo.daytrader.javaee6.entities.Account;
import org.apache.geronimo.daytrader.javaee6.entities.Order;
import org.apache.geronimo.daytrader.javaee6.utils.TradeConfig;
import org.apache.geronimo.daytrader.javaee6.web.TradeAction;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
*/

import org.iscas.entity.Account;
import org.iscas.entity.Order;
import org.iscas.util.TradeConfig;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;

//@ManagedBean(name="orderdata")
public class OrderDataJSF {    
    
    private OrderData[] allOrders;
    private int size;
    private OrderData orderData;

    public OrderDataJSF(){
        /*getAllOrder();
        setSize(allOrders.length);*/
        getOrder();
    }

    public void getAllOrder(){
        TradeAction tAction = new TradeAction();
        try {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
        String userID = (String)session.getAttribute("uidBean");
        Account accountData = tAction.getAccountData(userID);
        //AccountProfileDataBean accountProfileData = tAction .getAccountProfileData(userID);
        ArrayList Orders = (TradeConfig.getLongRun() ? new ArrayList() : (ArrayList) tAction.getOrders(userID));
        OrderData[] orders = new OrderData[Orders.size()];
        int count = 0;
        for (Object order : Orders){
            OrderData r = new OrderData(((Order)order).getOrderID(),((Order)order).getOrderStatus(), ((Order)order).getOpenDate(), ((Order)order).getCompletionDate(), ((Order)order).getOrderFee(), ((Order)order).getOrderType(),((Order)order).getQuantity(), ((Order)order).getSymbol());
            r.setPrice(((Order)order).getPrice());
            r.setTotal(r.getPrice().multiply(new BigDecimal(r.getQuantity())));
            orders[count] = r;
            count ++;
        }
        setAllOrders(orders);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void getOrder(){

        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
        OrderData order = (OrderData)session.getAttribute("orderData");
        if(order != null){
        	setOrderData(order);
        }
    }

    public void setAllOrders(OrderData[] allOrders) {
        this.allOrders = allOrders;
    }

    public OrderData[] getAllOrders() {
        return allOrders;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public void setOrderData(OrderData orderData) {
        this.orderData = orderData;
    }

    public OrderData getOrderData() {
        return orderData;
    }
}
