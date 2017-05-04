package org.iscas.web.core.bean;

/*
import org.apache.geronimo.daytrader.javaee6.entities.OrderDataBean;
import org.apache.geronimo.daytrader.javaee6.entities.Quote;
import org.apache.geronimo.daytrader.javaee6.utils.Log;
import org.apache.geronimo.daytrader.javaee6.utils.TradeConfig;
import org.apache.geronimo.daytrader.javaee6.web.TradeAction;
import javax.faces.bean.ManagedBean;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.context.FacesContext;
*/

import javax.servlet.http.HttpSession;
import java.util.ArrayList;

/* @ManagedBean(name="quotedata") */

public class QuoteDataJSF {
    private QuoteData[] quotes;
    private String symbols = "s:0, s:1, s:2, s:3, s:4";
    private HtmlDataTable dataTable;
    private Integer quantity = 100;
    
    public QuoteDataJSF(){
        getAllQuotes();
    }

    public void setQuotes(QuoteData[] quotes) {
        this.quotes = quotes;
    }

    public QuoteData[] getQuotes() {
        return quotes;
    }

    public void getAllQuotes() {        
        String symbols = "s:0,s:1,s:2,s:3,s:4";        
        ArrayList quotes = new ArrayList();
        java.util.StringTokenizer st = new java.util.StringTokenizer(symbols, " ,");
        QuoteData[] quoteDatas = new QuoteData[6];
        int count = 0;
        while (st.hasMoreElements()) {
            String symbol = st.nextToken();
            TradeAction tAction = new TradeAction();            
            try {
                Quote quoteData = tAction.getQuote(symbol);
                quoteDatas[count] =  new QuoteData(quoteData.getOpen(), quoteData.getPrice(), quoteData.getSymbol(), quoteData.getHigh(), quoteData.getLow(), quoteData.getCompanyName(),quoteData.getVolume(), quoteData.getChange());
                count ++;
            } catch (Exception e) {
                Log.error(e.toString());
            }
        }
        setQuotes(quoteDatas);
    }
    
    public String getQuotesBySymbols() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
        session.setAttribute("symbols", symbols);
        
        ArrayList quotes = new ArrayList();
        java.util.StringTokenizer st = new java.util.StringTokenizer(symbols, " ,");
        QuoteData[] quoteDatas = new QuoteData[6];
        int count = 0;
        while (st.hasMoreElements()) {
            String symbol = st.nextToken();
            TradeAction tAction = new TradeAction();            
            try {
                Quote quoteData = tAction.getQuote(symbol);
                quoteDatas[count] =  new QuoteData(quoteData.getOpen(), quoteData.getPrice(), quoteData.getSymbol(), quoteData.getHigh(), quoteData.getLow(), quoteData.getCompanyName(),quoteData.getVolume(), quoteData.getChange());
                count ++;
            } catch (Exception e) {
                Log.error(e.toString());
            }
        }
        setQuotes(quoteDatas);
        return "quotes";
    }    
    
    public String buy(){
    	FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
        String userID = (String)session.getAttribute("uidBean");    
        QuoteData quoteData = (QuoteData)dataTable.getRowData();
    	TradeAction tAction=null;
        tAction = new TradeAction();
    	OrderDataBean orderDataBean;
		try {
			orderDataBean = tAction.buy(userID, quoteData.getSymbol(), new Double(this.quantity).doubleValue(), TradeConfig.orderProcessingMode);
			OrderData orderData = new OrderData(orderDataBean.getOrderID(),
					orderDataBean.getOrderStatus(), orderDataBean.getOpenDate(),
					orderDataBean.getCompletionDate(), orderDataBean.getOrderFee(),
					orderDataBean.getOrderType(), orderDataBean.getQuantity(),
					orderDataBean.getSymbol());
			session.setAttribute("orderData", orderData);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.error(e.toString());
			e.printStackTrace();
		}
    	return "buy";
    }

    public void setSymbols(String symbols) {
        this.symbols = symbols;
    }

    public String getSymbols() {
        return symbols;
    }
    public void setDataTable(HtmlDataTable dataTable) {
		this.dataTable = dataTable;
	}

	public HtmlDataTable getDataTable() {
		return dataTable;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getQuantity() {
		return quantity;
	}
}
