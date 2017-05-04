package org.iscas.service;

import org.iscas.entity.Account;
import org.iscas.entity.AccountDataBean;
import org.iscas.entity.AccountProfile;
import org.iscas.entity.Holding;
import org.iscas.repository.AccountProfileRepository;
import org.iscas.repository.AccountRepository;
import org.iscas.repository.HoldingRepository;
import org.iscas.util.Log;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
//import com.github.kristofa.brave.Brave;

/**
 * Created by andyren on 2016/6/28.
 */
@RestController
@RequestMapping("/account")
@EnableAutoConfiguration
public class AccountService {

    //protected static Logger logger = LoggerFactory.getLogger(AccountService.class);

    @Autowired
    AccountProfileRepository accountProfileRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    HoldingRepository holdingRepository;

    //login account
    @RequestMapping(value="/login",method = RequestMethod.POST)
    public Account login(@RequestParam(value = "userid") String userid, @RequestParam(value = "password") String password,@RequestParam(value = "uuid") String uuid){
        //AccountProfile accountProfile = accountProfileRepository.findByUserID(userid);
        //验证用户名和密码
        AccountProfile accountProfile = accountProfileRepository.findByUserIDAndPasswd(userid,password);
        if(accountProfile==null ){
            return null;
        }
        //更新最近登录时间和登录次数
        Account account = accountRepository.findByProfileUserID(userid);
        account.setLastLogin(new Date());
        account.setLoginCount(account.getLoginCount()+1);
        accountRepository.save(account);
        //两条关键日志信息：API，Identifiers
        Log.log("Login  "+uuid);

        return account;
    }
    //register account
    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public Account register(@RequestParam(value = "fullname") String fullname,@RequestParam(value = "address") String address,
                                    @RequestParam(value = "email") String email,@RequestParam(value = "userID") String userID,
                                    @RequestParam(value = "password") String password,@RequestParam(value = "cpassword") String cpassword,
                                    @RequestParam(value = "creditcard") String creditcard,@RequestParam(value = "openBalance") String openBalance,@RequestParam(value = "uuid") String uuid){
        //System.out.println(accountDataBean.getFullname());
        //持久化account
        Account account = new Account();
        account.setProfileUserID(userID);
        account.setBalance(new BigDecimal(openBalance));
        account.setOpenBalance(new BigDecimal(openBalance));
        account.setCreationDate(new Date());
        account.setLastLogin(new Date());
        account.setLoginCount(0);
        account.setLogoutCount(0);
        //accountRepository.save(account);

        //持久化accountProfile
        AccountProfile accountProfile = new AccountProfile();
        accountProfile.setUserID(userID);
        accountProfile.setPassword(password);
        accountProfile.setFullName(fullname);
        accountProfile.setCreditCard(creditcard);
        accountProfile.setEmail(email);
        accountProfile.setAddress(address);
        accountProfileRepository.save(accountProfile);
        accountRepository.save(account);

        Log.log("Register  "+uuid);

        return account;
    }

    //query Account
    @RequestMapping(value = "/query/{userid}",method = RequestMethod.GET)
    public Account getAccountByUserID(@PathVariable String userid,@RequestParam(value = "uuid") String uuid){


//        Brave.Builder builder = new Brave.Builder("AccountService");
//        builder.spanCollector(HttpSpanCollector.create(
//                "http://133.133.10.28:9411",
//                new EmptySpanCollectorMetricsHandler()));
//        Brave brave = builder.build();

       // logger.info("AccountService");
        Log.log("getAccountByUserID  "+uuid);
        //System.out.println("AcountService");

        return accountRepository.findByProfileUserID( userid);
    }
    @RequestMapping(value = "/queryAccountProfile/{userid}",method = RequestMethod.GET)
    public AccountProfile getAccountProfileByUserID(@PathVariable String userid,@RequestParam(value = "uuid") String uuid){

        Log.log("getAccountProfileByUserID  "+uuid);

        return accountProfileRepository.findByUserID(userid);
    }

    //Logout account
    @RequestMapping(value = "/logout/{userid}",method = RequestMethod.POST)
    public Account logout(@PathVariable String userid,@RequestParam(value = "uuid") String uuid){
        //
        //更新注销次数
        Account account = accountRepository.findByProfileUserID(userid);
        //account.setLastLogin(new Date());
        account.setLogoutCount(account.getLogoutCount()+1);
        accountRepository.save(account);

        Log.log("Logout  "+uuid);

        return account;
    }

    //update Account
    @RequestMapping(value = "/update/{userid}",method = RequestMethod.POST)
    public AccountDataBean update(@PathVariable String userid,@RequestBody AccountDataBean accountDataBean){
        Account account = accountRepository.findByProfileUserID(userid);
        AccountProfile accountProfile = accountProfileRepository.findByUserID(userid);
        //持久化account
        account.setProfileUserID(accountDataBean.getUserID());
        account.setBalance(new BigDecimal(accountDataBean.getOpenBalance()));
        account.setOpenBalance(new BigDecimal(accountDataBean.getOpenBalance()));
        account.setCreationDate(new Date());
        account.setLastLogin(new Date());
        account.setLoginCount(account.getLoginCount());
        account.setLogoutCount(account.getLogoutCount());

        //持久化accountProfile
        accountProfile.setUserID(accountDataBean.getUserID());
        accountProfile.setPassword(accountDataBean.getPasswd());
        accountProfile.setFullName(accountDataBean.getFullname());
        accountProfile.setCreditCard(accountDataBean.getCreditcard());
        accountProfile.setEmail(accountDataBean.getEmail());
        accountProfile.setAddress(accountDataBean.getAddress());
        accountProfileRepository.save(accountProfile);
        accountRepository.save(account);

       Log.log("Update  "+0001);

        return accountDataBean;
    }

    //delete Account
    @RequestMapping("/delete/{userid}")
    public Account delete(@PathVariable String userid){
        Account account = accountRepository.findByProfileUserID(userid);
        AccountProfile accountProfile = accountProfileRepository.findByUserID(userid);

        //找到删除账户所有持股信息，删除
        List<Holding> list = holdingRepository.findByAccountID(account.getAccountID());

        accountRepository.delete(account);
        accountProfileRepository.delete(accountProfile);
        holdingRepository.delete(list);

        Log.log("Delete  "+0001);

        return account;
    }
    //Account managed services

}
