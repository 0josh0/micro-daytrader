package org.iscas.web.core.bean;

/*
import org.apache.geronimo.daytrader.javaee6.core.direct.FinancialUtils;
import org.apache.geronimo.daytrader.javaee6.entities.Holding;
import org.apache.geronimo.daytrader.javaee6.entities.Order;
import org.apache.geronimo.daytrader.javaee6.entities.Quote;
import org.apache.geronimo.daytrader.javaee6.utils.TradeConfig;
import org.apache.geronimo.daytrader.javaee6.web.TradeAction;
import javax.faces.bean.ManagedBean;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.context.FacesContext;
*/

import org.iscas.entity.Holding;
import org.iscas.entity.Order;
import org.iscas.entity.Quote;
import org.iscas.util.FinancialUtils;
import org.iscas.util.TradeConfig;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

//@ManagedBean(name="portfolio")
public class PortfolioJSF {        
    private BigDecimal balance;
    private BigDecimal openBalance;    
    private Integer numberHoldings;
    private BigDecimal holdingsTotal;
    private BigDecimal sumOfCashHoldings;
    private BigDecimal totalGain = new BigDecimal(0.0);
    private BigDecimal totalValue = new BigDecimal(0.0);
    private BigDecimal totalBasis = new BigDecimal(0.0);
    private BigDecimal totalGainPercent = new BigDecimal(0.0);    
    private ArrayList<HoldingData> holdingDatas;
    private HtmlDataTable dataTable;
   
    
    
    
    public PortfolioJSF(){
        getPortfolio();
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

    
    public void setNumberHoldings(Integer numberHoldings) {
        this.numberHoldings = numberHoldings;
    }

    public Integer getNumberHoldings() {
        return numberHoldings;
    }
    
    public void getPortfolio(){
        TradeAction tAction = new TradeAction();
        try {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);    
        String userID = (String)session.getAttribute("uidBean");        
        Collection Quotes = new ArrayList();
        Collection Holdings = tAction.getHoldings(userID);
        
        numberHoldings = Holdings.size();
        // Walk through the collection of user holdings and creating a list of quotes
        if (Holdings.size() > 0) {
            Iterator it = Holdings.iterator();  
            holdingDatas = new ArrayList<HoldingData>(Holdings.size());
            //int count = 0;
            while (it.hasNext()) {
                Holding holdingData = (Holding) it.next();
                Quote quoteData = tAction.getQuote(holdingData.getQuoteID());               
               
                BigDecimal basis = holdingData.getPurchasePrice().multiply(new BigDecimal(holdingData.getQuantity()));
                BigDecimal marketValue = quoteData.getPrice().multiply(new BigDecimal(holdingData.getQuantity()));
                totalBasis = totalBasis.add(basis);    
                totalValue = totalValue.add(marketValue);    
                BigDecimal gain = marketValue.subtract(basis);
                totalGain = totalGain.add(gain);
                BigDecimal gainPercent = null;
                if (basis.doubleValue() == 0.0)
                {
                    gainPercent = new BigDecimal(0.0);
                    //Log.error("portfolio.jsp: Holding with zero basis. holdingID="+holdingData.getHoldingID() + " symbol=" + holdingData.getQuoteID() + " purchasePrice=" + holdingData.getPurchasePrice());
                }
                else {
                    gainPercent = marketValue.divide(basis, BigDecimal.ROUND_HALF_UP).subtract(new BigDecimal(1.0)).multiply(new BigDecimal(100.0)); 
                }
                HoldingData h = new HoldingData();
                h.setHoldingID(holdingData.getHoldingID());
                h.setPurchaseDate(holdingData.getPurchaseDate());
                h.setQuoteID(holdingData.getQuoteID());
                h.setQuantity(holdingData.getQuantity());
                h.setPurchasePrice(holdingData.getPurchasePrice());
                h.setBasis(basis);
                h.setGain(gain);
                h.setMarketValue(marketValue);
                h.setPrice(quoteData.getPrice());               
                holdingDatas.add(h);
                //count++;
            }
                //dataTable
                setTotalGainPercent(FinancialUtils.computeGainPercent(totalValue, totalBasis));
            
        } else {
            //results = results + ".  Your portfolio is empty.";
        }
            } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            }
        }    

    public void setTotalGain(BigDecimal totalGain) {
        this.totalGain = totalGain;
    }

    public BigDecimal getTotalGain() {
        return totalGain;
    }

    public void setTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
    }

    public BigDecimal getTotalValue() {
        return totalValue;
    }

    public void setTotalBasis(BigDecimal totalBasis) {
        this.totalBasis = totalBasis;
    }

    public BigDecimal getTotalBasis() {
        return totalBasis;
    }

    public void setHoldingDatas(ArrayList<HoldingData> holdingDatas) {
        this.holdingDatas = holdingDatas;
    }

    public ArrayList<HoldingData> getHoldingDatas() {
        return holdingDatas;
    }    

    public void setTotalGainPercent(BigDecimal totalGainPercent) {
        this.totalGainPercent = totalGainPercent;
    }

    public BigDecimal getTotalGainPercent() {
        return totalGainPercent;
    } 
    public String sell(){
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);    
        String userID = (String)session.getAttribute("uidBean");    
        TradeAction tAction = new TradeAction();
        Order Order = null;
        HoldingData holdingData = (HoldingData)dataTable.getRowData();
        try {
        	Order = tAction.sell(userID, holdingData.getHoldingID(), TradeConfig.orderProcessingMode);
            holdingDatas.remove(holdingData);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
           
        OrderData orderData = new OrderData(Order.getOrderID(),
				Order.getOrderStatus(), Order.getOpenDate(),
				Order.getCompletionDate(), Order.getOrderFee(),
				Order.getOrderType(), Order.getQuantity(),
				Order.getSymbol());
        session.setAttribute("orderData", orderData);
        return "sell";
    }

	public void setDataTable(HtmlDataTable dataTable) {
		this.dataTable = dataTable;
	}

	public HtmlDataTable getDataTable() {
		return dataTable;
	}
}
