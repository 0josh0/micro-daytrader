package org.iscas.service;

import org.iscas.entity.Orders;
import org.iscas.repository.AccountRepository;
import org.iscas.repository.OrdersRepository;
import org.iscas.util.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;
import java.util.List;

/**
 * Created by andyren on 2016/6/28.
 */
@RestController
@RequestMapping("/orders")
@EnableAutoConfiguration
public class OrdersService {

    @Autowired
    OrdersRepository ordersRepository;
    @Autowired
    AccountRepository accountRepository;

    //query orders
    @RequestMapping(value = "/query/{userid}",method = RequestMethod.GET)
    public List<Orders> getOrdersByUserID(@PathVariable String userid,@RequestParam(value = "uuid") String uuid){
      //  Account account = ;
        List<Orders> ordersList = ordersRepository.findByAccountID(accountRepository.findByProfileUserID(userid).getAccountID());

        Log.log("getOrdersByUserID  "+uuid);

        return ordersList;
    }

    //delete orders
    @RequestMapping(value = "/delete/{symbol}",method = RequestMethod.POST)
    public Orders update(@PathVariable String symbol,@RequestParam(value = "userid") String userid){
        List<Orders> ordersList = ordersRepository.findByAccountID(accountRepository.findByProfileUserID(userid).getAccountID());
        Iterator<Orders> it = ordersList.iterator();
        Orders orders = null;
        while(it.hasNext()){
            orders = it.next();
            if(orders.getQuoteSymbol().equals(symbol)){
                ordersRepository.delete(orders);
                break;
            }
        }
        //holdingRepository.delete(holding);
        Log.log("OrderService —————— Delete");
        return  orders;
    }
    @RequestMapping("/getAllOrders")
    public List<Orders> getAllOrders() throws Exception {

        List<Orders> orders = null;
        try {
            orders = ordersRepository.findAll();
        } catch (Exception e) {
            //system.out.println("TradeDirect:getAllQuotes", e);
        }
        Log.log("OrderService —————— GetAllOrders");
        return orders;
    }

}
