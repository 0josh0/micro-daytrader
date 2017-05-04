package org.iscas.service;

import org.iscas.entity.Account;
import org.iscas.entity.Holding;
import org.iscas.entity.Orders;
import org.iscas.entity.Quote;
import org.iscas.repository.AccountRepository;
import org.iscas.repository.HoldingRepository;
import org.iscas.repository.OrdersRepository;
import org.iscas.repository.QuoteRepository;
import org.iscas.util.FinancialUtils;
import org.iscas.util.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by andyren on 2016/6/28.
 */
@RestController
@RequestMapping("/quote")
@EnableAutoConfiguration
public class QuoteService {
    @Autowired
    private QuoteRepository quoteRepository;
    @Autowired
    private HoldingRepository holdingRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private OrdersRepository ordersRepository;


    /**
     * 创建一个报价，使用方法http://uri:port/createQuote?symbol=s:1002&companyName=xxxxx&price=10209.990
     * @param symbol
     * @param companyName
     * @param price
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/createQuote",method = RequestMethod.POST)
    public Quote createQuote(@RequestParam(name = "symbol") String symbol, @RequestParam(name = "companyName") String companyName, @RequestParam(name = "price") BigDecimal price) throws Exception {
        Quote quoteData = null;
        try {
            if (Log.doTrace()) {
                Log.traceEnter("TradeDirect:createQuote: symbol = " + symbol + "  companyName = " + companyName + " price=" + price);
            }

            price = price.setScale(FinancialUtils.SCALE, FinancialUtils.ROUND);
            double volume = 0.0, change = 0.0;
            quoteData = new Quote();
            quoteData.setSymbol(symbol);
            quoteData.setCompanyName(companyName);
            quoteData.setVolume(volume);
            quoteData.setPrice(price);
            quoteData.setOpen1(price);
            quoteData.setLow(price);
            quoteData.setHigh(price);
            quoteData.setChange1(change);

            quoteRepository.save(quoteData);

            if (Log.doTrace()) {
                Log.traceExit("TradeDirect:createQuote");
            }
        } catch (Exception e) {
            Log.error("TradeDirect:createQuote -- error creating quote", e);
        }
        Log.log("QuoteService —————— CreateQuote");
        return quoteData;
    }

    /**
     * 获得报价信息
     * @param symbol
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/query/{symbol}",method = RequestMethod.GET)
    public Quote getQuote(@PathVariable String symbol,@RequestParam(value = "uuid") String uuid) throws Exception {
        Quote quoteData = null;
        try {
            if (Log.doTrace()) {
                Log.trace("TradeDirect:getQuote - symbol = " + symbol);
            }
            quoteData = quoteRepository.findBySymbol(symbol);
        } catch (Exception e) {
            Log.error("TradeDirect:getQuote -- error getting quote", e);
        }
        Log.log("getQuote  "+uuid);
        return quoteData;
    }

    /**
     * 获得所有的股票报价信息
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getAllQuotes",method = RequestMethod.GET)
    public List<Quote> getAllQuotes() throws Exception {

        List<Quote> quotes = null;
        try {
            quotes = quoteRepository.findAll();
        } catch (Exception e) {
            Log.error("TradeDirect:getAllQuotes", e);
        }
        Log.log("QuoteService —————— GetAllQuotes");
        return quotes;
    }

    /**
     * 更新股票报价信息，如果需要则发布股价变更消息
     * @param symbol
     * @param price
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/updatPrice/{symbol}",method = RequestMethod.POST)
    public Quote updatePrice(@PathVariable String symbol,@RequestParam(value = "price") String price){
        Quote quoteData = quoteRepository.findBySymbol(symbol);
        quoteData.setPrice(new BigDecimal(price));
        quoteRepository.save(quoteData);
        Log.log("QuoteService —————— UpdatePrice");
        return quoteData;
    }
    @RequestMapping(value = "/updateVolume/{symbol}",method = RequestMethod.POST)
    public Quote updateVolume(@PathVariable String symbol, @RequestParam(value = "volume") Double volume){
        Quote quoteData = quoteRepository.findBySymbol(symbol);
        quoteData.setVolume(volume);
        quoteRepository.save(quoteData);
        Log.log("QuoteService —————— UpdateVolume");
        return quoteData;
    }
    //buy
    @RequestMapping(value = "/buy/{symbol}",method = RequestMethod.POST)
    public Quote buy(@PathVariable String symbol,
                     @RequestParam(value="quantity") Double quantity,
                     @RequestParam(value = "userid") String userid){
        Quote quote = quoteRepository.findBySymbol(symbol);
        if(quantity<=quote.getVolume()){//更新quote
            quote.setVolume(quote.getVolume()-quantity);
            quoteRepository.save(quote);

            Account account = accountRepository.findByProfileUserID(userid);

            //更新持股信息，holding
            Holding holding = new Holding();
            holding.setQuantity(quantity);
            holding.setPurchaseDate(new Date());
            holding.setPurchasePrice(quote.getPrice());
            holding.setQuoteSymbol(symbol);
            holding.setAccountID(account.getAccountID());//外键关联Account表。
            //accountRepository.save(account);
            holdingRepository.save(holding);

            //更新订单信息，order
            Orders orders = new Orders();
            orders.setOrderType("1");
            orders.setOrderStatus("closed");
            orders.setOpenDate(new Date());
            orders.setCompletionDate(new Date());
            orders.setQuantity(quantity);
            orders.setPrice(quote.getPrice());
            orders.setOrderFee(new BigDecimal(20));
            orders.setQuoteSymbol(symbol);
            orders.setAccountID(account.getAccountID());

            //获取刚才持久化的holdingID
            Integer holdingID = null;
            List<Holding> hList = holdingRepository.findByQuoteSymbol(symbol);
            Iterator<Holding> it = hList.iterator();
            while(it.hasNext()){
                Holding tmp = it.next();
                if(tmp.getAccountID().equals(account.getAccountID())){
                    holdingID = tmp.getHoldingID();
                    break;
                }
            }

            orders.setHoldingID(holdingID);
            ordersRepository.save(orders);

        }
        Log.log("QuoteService —————— Buy");
        return quote;
    }


}
