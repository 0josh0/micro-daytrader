//package org.iscas.trade;
//import org.iscas.Startup;
//import org.iscas.entity.Account;
//import org.iscas.entity.AccountProfile;
//import org.iscas.entity.Quote;
//import org.iscas.repository.*;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.SpringApplicationConfiguration;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//import java.math.BigDecimal;
//import java.util.Date;
//import java.util.List;
//
///**
// * Created by andyren on 2016/10/25.
// */
//@ComponentScan("org.iscas.*")
//@SpringApplicationConfiguration(Startup.class)
//@RunWith(SpringJUnit4ClassRunner.class)
//public class TradeMicroserviceTests {
//
//    @Autowired
//    private AccountProfileRepository accountProfileRepository;
//
//    @Autowired
//    private AccountRepository accountRepository;
//
//    @Autowired
//    private HoldingRepository holdingRepository;
//
//    @Autowired
//    private OrdersRepository ordersRepository;
//
//    @Autowired
//    private QuoteRepository quoteRepository;
//
//    @Test
//    public void test() throws Exception{
//        //创建10条股票报价信息
//        quoteRepository.save(new Quote("s:01","中国集美集团", 1200000, new BigDecimal(3.21), new BigDecimal(3200), new BigDecimal(2996), new BigDecimal(3300), new BigDecimal(204)));
//        quoteRepository.save(new Quote("s:02","中国移动集团", 1200000, new BigDecimal(3.21), new BigDecimal(3200), new BigDecimal(2996), new BigDecimal(3300), new BigDecimal(204)));
//        quoteRepository.save(new Quote("s:03","中国联通集团", 1200000, new BigDecimal(3.21), new BigDecimal(3200), new BigDecimal(2996), new BigDecimal(3300), new BigDecimal(204)));
//        quoteRepository.save(new Quote("s:04","中国大唐集团", 1200000, new BigDecimal(3.21), new BigDecimal(3200), new BigDecimal(2996), new BigDecimal(3300), new BigDecimal(204)));
//        quoteRepository.save(new Quote("s:05","中国伊利集团", 1200000, new BigDecimal(3.21), new BigDecimal(3200), new BigDecimal(2996), new BigDecimal(3300), new BigDecimal(204)));
//        quoteRepository.save(new Quote("s:06","中国蒙牛集团", 1200000, new BigDecimal(3.21), new BigDecimal(3200), new BigDecimal(2996), new BigDecimal(3300), new BigDecimal(204)));
//        quoteRepository.save(new Quote("s:07","中国铁通集团", 1200000, new BigDecimal(3.21), new BigDecimal(3200), new BigDecimal(2996), new BigDecimal(3300), new BigDecimal(204)));
//        quoteRepository.save(new Quote("s:08","中国电信集团", 1200000, new BigDecimal(3.21), new BigDecimal(3200), new BigDecimal(2996), new BigDecimal(3300), new BigDecimal(204)));
//        quoteRepository.save(new Quote("s:09","中国东方集团", 1200000, new BigDecimal(3.21), new BigDecimal(3200), new BigDecimal(2996), new BigDecimal(3300), new BigDecimal(204)));
//        quoteRepository.save(new Quote("s:10","中国海运集团", 1200000, new BigDecimal(3.21), new BigDecimal(3200), new BigDecimal(2996), new BigDecimal(3300), new BigDecimal(204)));
//        quoteRepository.save(new Quote("s:11","中国航运集团", 1200000, new BigDecimal(3.21), new BigDecimal(3200), new BigDecimal(2996), new BigDecimal(3300), new BigDecimal(204)));
//
//    }
//
//
//
//    @Test
//    public void testQuery() throws Exception{
//        List<Quote> result =         quoteRepository.withSymbolAndCompanyNameQuery("s:02","中国移动集团");
//        System.out.println("查询s:02 中国移动集团 的信息： " + result.size());
//    }
//
//
//    @Test
//    public void testAccount(){
//
//        accountRepository.save(new Account(10001, "10001",new BigDecimal(109200),new BigDecimal(109200),new Date(),new Date(),1,1 ));
//        accountRepository.save(new Account(10002, "10001",new BigDecimal(109200),new BigDecimal(109200),new Date(),new Date(),1,1 ));
//        accountRepository.save(new Account(10003, "10001",new BigDecimal(109200),new BigDecimal(109200),new Date(),new Date(),1,1 ));
//        accountRepository.save(new Account(10004, "10001",new BigDecimal(109200),new BigDecimal(109200),new Date(),new Date(),1,1 ));
//        accountRepository.save(new Account(10005, "10001",new BigDecimal(109200),new BigDecimal(109200),new Date(),new Date(),1,1 ));
//        accountRepository.save(new Account(10006, "10001",new BigDecimal(109200),new BigDecimal(109200),new Date(),new Date(),1,1 ));
//        accountRepository.save(new Account(10007, "10001",new BigDecimal(109200),new BigDecimal(109200),new Date(),new Date(),1,1 ));
//        accountRepository.save(new Account(10008, "10001",new BigDecimal(109200),new BigDecimal(109200),new Date(),new Date(),1,1 ));
//        accountRepository.save(new Account(10009, "10001",new BigDecimal(109200),new BigDecimal(109200),new Date(),new Date(),1,1 ));
//        accountRepository.save(new Account(10010, "10001",new BigDecimal(109200),new BigDecimal(109200),new Date(),new Date(),1,1 ));
//
//        List<Account> result = accountRepository.findAll();
//
//        for(Account temp : result) {
//            System.out.println(temp.toString());
//        }
//    }
//
//    @Test
//    public void testAccountProfile(){
//        accountProfileRepository.save(new AccountProfile("10001","10001", "edward john", "9238200990011", "jose@facebook.com","Jose Angoles, Washington DC"));
//        accountProfileRepository.save(new AccountProfile("10002","10002", "howard john", "9238200990012", "jose@facebook.com","Jose Angoles, Washington DC"));
//        accountProfileRepository.save(new AccountProfile("10003","10003", "lucy john", "9238200990013", "jose@facebook.com","Jose Angoles, Washington DC"));
//        accountProfileRepository.save(new AccountProfile("10004","10004", "cocuci john", "9238200990014", "jose@facebook.com","Jose Angoles, Washington DC"));
//        accountProfileRepository.save(new AccountProfile("10005","10005", "judy john", "9238200990015", "jose@facebook.com","Jose Angoles, Washington DC"));
//        accountProfileRepository.save(new AccountProfile("10006","10006", "spoke john", "9238200990016", "jose@facebook.com","Jose Angoles, Washington DC"));
//        accountProfileRepository.save(new AccountProfile("10007","10007", "tincy john", "9238200990017", "jose@facebook.com","Jose Angoles, Washington DC"));
//        accountProfileRepository.save(new AccountProfile("10008","10008", "student john", "9238200990018", "jose@facebook.com","Jose Angoles, Washington DC"));
//        accountProfileRepository.save(new AccountProfile("10009","10009", "hulu john", "9238200990019", "jose@facebook.com","Jose Angoles, Washington DC"));
//        accountProfileRepository.save(new AccountProfile("10010","10010", "tools john", "9238200990020", "jose@facebook.com","Jose Angoles, Washington DC"));
//    }
//
//
//}
