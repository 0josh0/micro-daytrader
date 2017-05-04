package org.iscas.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.codehaus.jackson.map.ObjectMapper;
import org.iscas.databean.AccountDataBean;
import org.iscas.databean.MarketSummaryDataBean;
import org.iscas.entity.*;
import org.iscas.repository.AccountRepository;
import org.iscas.repository.HoldingRepository;
import org.iscas.repository.OrdersRepository;
import org.iscas.repository.QuoteRepository;
import org.iscas.util.FinancialUtils;
import org.iscas.util.Log;
import org.iscas.util.ServiceInvoke;
import org.iscas.util.TradeConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@RestController
@RequestMapping("/trade")
@EnableAutoConfiguration
public class TradeService {

    @Autowired
    private QuoteRepository quoteRepository;
    @Autowired
    private HoldingRepository holdingRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    public Environment env;//读取配置文件中的信息，application.properties

    //buy
    @RequestMapping(value = "/buy/{symbol}",method = RequestMethod.POST)
    public Orders buy(@PathVariable String symbol,
                     @RequestParam(value="quantity") Double quantity,
                     @RequestParam(value = "userid") String userid,
                      @RequestParam(value = "uuid") String uuid){
        Quote quote = quoteRepository.findBySymbol(symbol);
        Orders orders = null;
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
            orders = new Orders();
            orders.setOrderType("buy");
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
        Log.log("Buy  "+uuid);
        return orders;
    }


    //sell
    @RequestMapping(value = "/sell/{symbol}",method = RequestMethod.POST)
    public Orders sell(@PathVariable String symbol,@RequestParam(value = "userid") String userid,@RequestParam(value = "uuid") String uuid){
        Account account = accountRepository.findByProfileUserID(userid);
        List<Holding> holdingList = holdingRepository.findByAccountID(account.getAccountID());
        Iterator<Holding> it = holdingList.iterator();
        Holding holding = null;
        Orders orders =null;
        while(it.hasNext()){
            holding = it.next();
            if(holding.getQuoteSymbol().equals(symbol)){
                //更新订单信息，order
                orders = new Orders();
                orders.setOrderType("sell");
                orders.setOrderStatus("closed");
                orders.setOpenDate(new Date());
                orders.setCompletionDate(new Date());
                orders.setQuantity(holding.getQuantity());
                orders.setPrice(holding.getPurchasePrice());
                orders.setOrderFee(new BigDecimal(20));
                orders.setQuoteSymbol(symbol);
                orders.setAccountID(account.getAccountID());
                orders.setHoldingID(holding.getHoldingID());
                ordersRepository.save(orders);
                //删除持股信息
                holdingRepository.delete(holding);
                //return holding;
                break;
            }
        }
        Log.log("Sell  "+uuid);
        return  orders;
    }

    //get marketSummaryDatabean
    @RequestMapping(value = "/marketSummary",method = RequestMethod.GET)
    public MarketSummaryDataBean getMarketSummary(@RequestParam(value = "uuid") String uuid) throws Exception, RemoteException {
        Log.log("getMarketSummary  "+uuid);
        return MarketSummaryDataBean.getRandomInstance();
    }

    //get AccountDataBean
    @RequestMapping(value = "/accountDataBean/{userid}",method = RequestMethod.GET)
    public AccountDataBean getAccountDataBean(@PathVariable String userid,@RequestParam(value = "uuid") String uuid) throws IOException {

        //调用Account服务获取相关信息
        String accountServiceIP = env.getProperty("account.services.ip");
        //String accountServiceIP = getServiceAddress("account-tomcat9");
        //System.out.println("accountService-Address: "+accountServiceIP);
        String accountServicePort = env.getProperty("account.services.port");
        ServiceInvoke accountServiceInvoke = new ServiceInvoke();
        //调用account服务的getAccount
        String accountResponse = accountServiceInvoke.doGetCall("http://"+accountServiceIP+":"+accountServicePort+"/account/query/"+userid,"uuid=" + uuid);
        //将返回的json封装成account对象
        ObjectMapper accountObjectMapper = new ObjectMapper();
        Account account = accountObjectMapper.readValue(accountResponse,Account.class);

        if(account.equals(null)||account==null){//用户userid不存在
            System.out.println("用户不存在");
            return new AccountDataBean();
        }else{
           // System.out.println("用户存在");

           // System.out.println("用户存在,userid为："+account.getProfileUserID());

            //调用Holding服务获取相关信息
            String holdingServiceIP = env.getProperty("holding.services.ip");
           // String holdingServiceIP = getServiceAddress("holding-tomcat9");
            //System.out.println("holdingService-Address: "+holdingServiceIP);
            String holdingServicePort = env.getProperty("holding.services.port");
            ServiceInvoke holdingServiceInvoke = new ServiceInvoke();
            //调用holding服务的getholding,获取userid的所有持股信息
            String holdingResponse = holdingServiceInvoke.doGetCall("http://"+holdingServiceIP+":"+holdingServicePort+"/holding/query/"+userid,"uuid=" + uuid);
            //将返回的json封装成Holding对象数组
            ObjectMapper holdingObjectMapper = new ObjectMapper();
            Holding[] holdings = holdingObjectMapper.readValue(holdingResponse,Holding[].class);

            if(holdings.equals(null)||holdings.length==0){//该用户userid暂无持股信息
                System.out.println("该用户userid暂无持股信息");
                AccountDataBean accountDataBean = new AccountDataBean();
                accountDataBean.setSessionCreationDate(new Date());
                accountDataBean.setCurrentTime(account.getLastLogin());
                accountDataBean.setProfileID(account.getProfileUserID());
                accountDataBean.setAccountID(account.getAccountID());
                accountDataBean.setCreationDate(account.getCreationDate());
                accountDataBean.setLoginCount(account.getLoginCount());
                accountDataBean.setLastLogin(account.getLastLogin());
                accountDataBean.setLogoutCount(account.getLogoutCount());
                accountDataBean.setBalance(account.getBalance());
                accountDataBean.setOpenBalance(account.getOpenBalance());
                accountDataBean.setNumberHoldings(0);//持股数
                accountDataBean.setHoldingsTotal(new BigDecimal(0));//持股价值
                accountDataBean.setSumOfCashHoldings(new BigDecimal(account.getOpenBalance().doubleValue()-0));
                accountDataBean.setGain(new BigDecimal(0));
                accountDataBean.setGainPercent(new BigDecimal(0));

                Log.log("getAccountDataBean  "+uuid);
                return accountDataBean;

            }else{
                //System.out.println("该用户useri有持股信息");


                //System.out.println(list.size());

                //调用quote股票报价服务获取相关信息
                String quoteServiceIP = env.getProperty("quote.services.ip");
                //String quoteServiceIP = getServiceAddress("quote-tomcat9");
                //System.out.println("quoteService-Address: "+quoteServiceIP);
                String quoteServicePort = env.getProperty("quote.services.port");
                ServiceInvoke quoteServiceInvoke = new ServiceInvoke();


//                double sum = 0.0;//购买当前用户持有股票所花费的价值
//                double curValue = 0.0;//当前持有的股票价值
//                for(Holding holding:holdings){
//                    String symbol = holding.getQuoteSymbol();
//                    String quoteResponse = quoteServiceInvoke.doGetCall("http://"+quoteServiceIP+":"+quoteServicePort+"/quote/query/"+symbol,"");
//                    ObjectMapper quoteObjectMapper = new ObjectMapper();
//                    Quote quote = quoteObjectMapper.readValue(quoteResponse,Quote.class);
//                    curValue += (holding.getQuantity())*(quote.getPrice().doubleValue());
//                    sum +=(holding.getQuantity())*(holding.getPurchasePrice().doubleValue());
//                }

                AccountDataBean accountDataBean = new AccountDataBean();
                accountDataBean.setSessionCreationDate(new Date());
                accountDataBean.setCurrentTime(account.getLastLogin());
                accountDataBean.setProfileID(account.getProfileUserID());
                accountDataBean.setAccountID(account.getAccountID());
                accountDataBean.setCreationDate(account.getCreationDate());
                accountDataBean.setLoginCount(account.getLoginCount());
                accountDataBean.setLastLogin(account.getLastLogin());
                accountDataBean.setLogoutCount(account.getLogoutCount());
                accountDataBean.setBalance(account.getBalance());
                accountDataBean.setOpenBalance(account.getOpenBalance());
                accountDataBean.setNumberHoldings(holdings.length);//持股数
                //accountDataBean.setHoldingsTotal(new BigDecimal(curValue));//持股价值
                accountDataBean.setHoldingsTotal(TradeConfig.rndBigDecimal(1000.0f));
                //accountDataBean.setSumOfCashHoldings(new BigDecimal(account.getOpenBalance().doubleValue()-curValue));
                accountDataBean.setSumOfCashHoldings(TradeConfig.rndBigDecimal(10.0f));
                //accountDataBean.setGain(new BigDecimal(curValue-sum));
                accountDataBean.setGain(TradeConfig.rndBigDecimal(1000.0f));
                //accountDataBean.setGainPercent(new BigDecimal((curValue-sum)/sum));
                accountDataBean.setGainPercent(FinancialUtils.computeGainPercent(holdings[0].getPurchasePrice(), holdings[0].getPurchasePrice()));

                Log.log("getAccountDataBean  "+uuid);
                return accountDataBean;
            }
        }
        //return new AccountDataBean();
    }

    /**
     * 根据微服务名字获取服务地址
     */
    @RequestMapping(value = "/query",method = RequestMethod.GET)
    public String getServiceAddress(@RequestParam(value = "serviceName") String serviceName) throws IOException {
        // 读url
        //ClassLoader cl = Docker.class.getClassLoader();
        String api = "/v1/catalog/service/" + serviceName;
        String consulAddress = env.getProperty("application.consul.address");
        URL url = new URL(consulAddress + api);
        // 打开restful链接
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        // 提交模式
        conn.setRequestMethod("GET");// POST GET PUT DELETE
        // 设置访问提交模式，表单提交
        conn.setRequestProperty("Content-Type", "application/form-data");
        conn.setConnectTimeout(10000);// 连接超时 单位毫秒
        conn.setReadTimeout(2000);// 读取超时 单位毫秒
        conn.setDoOutput(true);// 是否输入参数
        String s = "";
        byte[] body = s.getBytes();
        conn.getOutputStream().write(body);
        // 读取请求返回值
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String lines;
        StringBuffer sb = new StringBuffer("");
        while ((lines = reader.readLine()) != null) {
            lines = new String(lines.getBytes(), "utf-8");
            sb.append(lines);
        }
        if (sb == null || sb.equals("")) {
            return "no such service!";
        } else {
            JSONArray jsonArray = JSONArray.parseArray(sb.toString());
            //System.out.println(sb);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            String serviceAddress = jsonObject.get("ServiceAddress").toString();
            reader.close();
            conn.disconnect();
            return serviceAddress;
        }
    }

    public void queueOrder(Integer orderID, boolean twoPhase) throws Exception, RemoteException {

    }

    public Orders completeOrder(Integer orderID, boolean twoPhase) throws Exception, RemoteException {
        return null;
    }


    public void cancelOrder(Integer orderID, boolean twoPhase) throws Exception, RemoteException {

    }


    public void orderCompleted(String userID, Integer orderID) throws Exception, RemoteException {

    }


    public Collection getOrders(String userID) throws Exception, RemoteException {
        return null;
    }


    public Collection getClosedOrders(String userID) throws Exception, RemoteException {
        return null;
    }


    public Quote createQuote(String symbol, String companyName, BigDecimal price) throws Exception, RemoteException {
        return null;
    }


    public Quote getQuote(String symbol) throws Exception, RemoteException {
        return null;
    }


    public Collection getAllQuotes() throws Exception, RemoteException {
        return null;
    }


    public Quote updateQuotePriceVolume(String symbol, BigDecimal newPrice, double sharesTraded) throws Exception, RemoteException {
        return null;
    }


    public Collection getHoldings(String userID) throws Exception, RemoteException {
        return null;
    }


    public Holding getHolding(Integer holdingID) throws Exception, RemoteException {
        return null;
    }


    public Account getAccountData(String userID) throws Exception, RemoteException {
        return null;
    }


    public AccountProfile getAccountProfileData(String userID) throws Exception, RemoteException {
        return null;
    }


    public AccountProfile updateAccountProfile(AccountProfile profileData) throws Exception, RemoteException {
        return null;
    }


    public Account login(String userID, String password) throws Exception, RemoteException {
        return null;
    }


    public void logout(String userID) throws Exception, RemoteException {

    }


    public Account register(String userID, String password, String fullname, String address, String email, String creditcard, BigDecimal openBalance) throws Exception, RemoteException {
        return null;
    }
}
