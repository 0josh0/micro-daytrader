package org.iscas.spring;

import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by andyren on 2016/6/27.
 */
public class Trade {

    @RequestMapping("/trade/buy")
    public String stockbuy(String stockId, double buybill, double price){
          //deal with the buying stock service
          return "success";
    }

    @RequestMapping("/trade/profile")
    public String stockprofio(){
        return "order id ";
    }


    @RequestMapping("/trade/sold")
    public String stocksold(String stockId, double soldbill, double price, double amount){
        //deal with the sold stock practice
        return "failed";
    }
}
