package org.iscas.web.web.broker;

import org.iscas.entity.*;
import org.iscas.web.core.api.TradeServices;

public interface TradeSLSBLocal extends TradeServices {
    public double investmentReturn(double investment, double NetValue) throws Exception;
    
    public Quote pingTwoPhase(String symbol) throws Exception;
}