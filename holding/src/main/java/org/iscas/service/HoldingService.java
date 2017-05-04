package org.iscas.service;

import org.iscas.entity.Holding;
import org.iscas.repository.AccountRepository;
import org.iscas.repository.HoldingRepository;
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
@RequestMapping("/holding")
@EnableAutoConfiguration
public class HoldingService {

    @Autowired
    HoldingRepository holdingRepository;
    @Autowired
    AccountRepository accountRepository;

    //query holding
    @RequestMapping(value = "/query/{userid}",method = RequestMethod.GET)
    public List<Holding> getHoldingByUserID(@PathVariable String userid,@RequestParam(value = "uuid") String uuid){
      //  Account account = ;
        List<Holding> holdingList = holdingRepository.findByAccountID(accountRepository.findByProfileUserID(userid).getAccountID());
        Log.log("getHoldingByUserID  "+uuid);
        return holdingList;
    }
    @RequestMapping(value = "/queryByHoldingID/{holdingID}",method = RequestMethod.GET)
    public Holding getHoldingByHoldingID(@PathVariable String holdingID,@RequestParam(value = "uuid") String uuid){
        //  Account account = ;
        Holding holding = holdingRepository.findByHoldingID(new Integer(holdingID));
        Log.log("getHoldingByHoldingID  "+uuid);
        return holding;
    }
    //sell holding
    @RequestMapping(value = "/sell/{symbol}",method = RequestMethod.POST)
    public Holding sell(@PathVariable String symbol,@RequestParam(value = "userid") String userid){
        List<Holding> holdingList = holdingRepository.findByAccountID(accountRepository.findByProfileUserID(userid).getAccountID());
        Iterator<Holding> it = holdingList.iterator();
        Holding holding = null;
        while(it.hasNext()){
            holding = it.next();
            if(holding.getQuoteSymbol().equals(symbol)){
                holdingRepository.delete(holding);
                //return holding;
                break;
            }
        }
        //holdingRepository.delete(holding);
        Log.log("HoldingService ———————— Sell");
        return  holding;
    }
    //update holding
    @RequestMapping(value = "/update/{symbol}",method = RequestMethod.POST)
    public Holding update(@PathVariable String symbol, @RequestParam(value = "userid") String userid, @RequestBody Holding holding){
        List<Holding> holdingList = holdingRepository.findByAccountID(accountRepository.findByProfileUserID(userid).getAccountID());
        Iterator<Holding> it = holdingList.iterator();
        Holding h = null;
        while(it.hasNext()){
            h = it.next();
            if(h.getQuoteSymbol().equals(symbol)){
                //holdingRepository.delete(holding);
                //return holding;
                h.setQuantity(holding.getQuantity());
                h.setPurchasePrice(holding.getPurchasePrice());
                h.setPurchaseDate(holding.getPurchaseDate());
                holdingRepository.save(h);
                break;
            }
        }
        //holdingRepository.delete(holding);
        Log.log("HoldingService ———————— Update");
        return  h;
    }

    @RequestMapping("/getAllHoldings")
    public List<Holding> getAllHoldings() throws Exception {

        List<Holding> holdings = null;
        try {
            holdings = holdingRepository.findAll();
        } catch (Exception e) {
            //system.out.println("TradeDirect:getAllQuotes", e);
        }
        Log.log("HoldingService ———————— GetAllHoldings");
        return holdings;
    }

}
