package org.iscas.web.core.api;

/**
  * TradeDBServices interface specifies the DB specific methods provided by SOME TradeServices instances.
  *
  * @see TradeJDBCDirect
  * @see TradeJEEDirect
  *
  */ 
public interface TradeDBServices {
    
    /**
     * TradeBuildDB needs this abstracted method
     */
    public String checkDBProductName() throws Exception;
    
    /**
     * TradeBuildDB needs this abstracted method
     */
    public boolean recreateDBTables(Object[] sqlBuffer, java.io.PrintWriter out) throws Exception;
}   
