package org.iscas.databean;

import org.iscas.entity.Orders;

import java.math.BigDecimal;
import java.util.Date;

//Account和Home面板上信息的封装bean，账户信息

public class AccountDataBean {
    private Date sessionCreationDate;//session creaded Time
    private Date currentTime;//login time
    private String profileID;    //userid
    private Integer accountID;//Account ID
    private Date creationDate;//Account created time
    private int loginCount;//totals logins
    private Date lastLogin;//last Login
    private int logoutCount;//totals logout
    private BigDecimal balance;//cash balance
    private BigDecimal openBalance;    //opening balance

    private Integer numberHoldings;//number of holdings
    private BigDecimal holdingsTotal;//total of holdings
    private BigDecimal sumOfCashHoldings;//sum of cash/holdingss
    private BigDecimal gain;//crrent gain
    private BigDecimal gainPercent;    
    
    private Orders[] closedOrders;
    private Orders[] allOrders;

//    public AccountDataWeb(){
//        home();
//    }

    public Date getSessionCreationDate() {
        return sessionCreationDate;
    }

    public void setSessionCreationDate(Date sessionCreationDate) {
        this.sessionCreationDate = sessionCreationDate;
    }

    public Date getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(Date currentTime) {
        this.currentTime = currentTime;
    }

    public String getProfileID() {
        return profileID;
    }

    public void setProfileID(String profileID) {
        this.profileID = profileID;
    }


    public void setAccountID(Integer accountID) {
        this.accountID = accountID;
    }

    public Integer getAccountID() {
        return accountID;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setLoginCount(int loginCount) {
        this.loginCount = loginCount;
    }

    public int getLoginCount() {
        return loginCount;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setOpenBalance(BigDecimal openBalance) {
        this.openBalance = openBalance;
    }

    public BigDecimal getOpenBalance() {
        return openBalance;
    }

    public void setHoldingsTotal(BigDecimal holdingsTotal) {
        this.holdingsTotal = holdingsTotal;
    }

    public BigDecimal getHoldingsTotal() {
        return holdingsTotal;
    }

    public void setSumOfCashHoldings(BigDecimal sumOfCashHoldings) {
        this.sumOfCashHoldings = sumOfCashHoldings;
    }

    public BigDecimal getSumOfCashHoldings() {
        return sumOfCashHoldings;
    }

    public void setGain(BigDecimal gain) {
        this.gain = gain;
    }

    public BigDecimal getGain() {
        return gain;
    }

    public void setGainPercent(BigDecimal gainPercent) {
        this.gainPercent = gainPercent;
    }

    public BigDecimal getGainPercent() {
        return gainPercent;
    }

    public void setNumberHoldings(Integer numberHoldings) {
        this.numberHoldings = numberHoldings;
    }

    public Integer getNumberHoldings() {
        return numberHoldings;
    }
/*
    public void home(){
        TradeAction tAction = new TradeAction();
        try {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
        String userID = (String)session.getAttribute("uidBean");
        Account accountData = tAction.getAccountData(userID);
        Collection<Holding> Holdings = tAction.getHoldings(userID);
        Collection closedOrders = tAction.getClosedOrders(userID);

        if ( (closedOrders!=null) && (closedOrders.size() > 0) ) {
            session.setAttribute("closedOrders", closedOrders);
            Orders[] orderjsfs = new Orders[closedOrders.size()];
            Iterator it = closedOrders.iterator();
            int i = 0;
            while (it.hasNext() )
            {
                Orders order = (Orders)it.next();
                Orders r = new Orders(order.getOrderID(),order.getOrderStatus(), order.getOpenDate(), order.getCompletionDate(), order.getOrderFee(), order.getOrderType(), order.getQuantity(),order.getSymbol());
                orderjsfs[i] = r;
                i++;
            }
            setClosedOrders(orderjsfs);
        }

        ArrayList Orders = (TradeConfig.getLongRun() ? new ArrayList() : (ArrayList) tAction.getOrders(userID));
        if ( (Orders!=null) && (Orders.size() > 0) ) {
            session.setAttribute("Orders", Orders);
            Orders[] orderjsfs = new Orders[Orders.size()];
            Iterator it = Orders.iterator();
            int i = 0;
            while (it.hasNext() )
            {
                Orders order = (Orders)it.next();
                Orders r = new Orders(order.getOrderID(),order.getOrderStatus(), order.getOpenDate(), order.getCompletionDate(), order.getOrderFee(), order.getOrderType(), order.getQuantity(),order.getSymbol());
                orderjsfs[i] = r;
                i++;
            }
            setAllOrders(orderjsfs);
        }

        setSessionCreationDate((Date)session.getAttribute("sessionCreationDate"));
        setCurrentTime(new Date());
        doAccountData(accountData, Holdings);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
*/
    /*public String quotes(){
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
        session.setAttribute("symbols", symbols);
        return "Quotes";
    }*/
/*
    private void doAccountData(Account accountData, Collection<Holding> Holdings){
        setProfileID(accountData.getProfileID());
        setAccountID(accountData.getAccountID());
        setCreationDate(accountData.getCreationDate());
        setLoginCount(accountData.getLoginCount());
        setLogoutCount(accountData.getLogoutCount());
        setLastLogin(accountData.getLastLogin());
        setOpenBalance(accountData.getOpenBalance());
        setBalance(accountData.getBalance());
        setNumberHoldings(Holdings.size());
        setHoldingsTotal(FinancialUtils.computeHoldingsTotal(Holdings));
        setSumOfCashHoldings(balance.add(holdingsTotal));
        setGain(FinancialUtils.computeGain(sumOfCashHoldings, openBalance));
        setGainPercent(FinancialUtils.computeGainPercent(sumOfCashHoldings, openBalance));

    }
*/

    public Orders[] getClosedOrders(){
        return closedOrders;
    }

    public void setClosedOrders(Orders[] closedOrders) {
        this.closedOrders = closedOrders;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLogoutCount(int logoutCount) {
        this.logoutCount = logoutCount;
    }

    public int getLogoutCount() {
        return logoutCount;
    }

    public void setAllOrders(Orders[] allOrders) {
        this.allOrders = allOrders;
    }

    public Orders[] getAllOrders() {
        return allOrders;
    }
}
