package org.iscas.spring;

import org.iscas.dao.UserDAO;
import org.iscas.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by andyren on 2016/6/27.
 */
@AutoConfigurationPackage
@Controller
public class SampleController {

    @Autowired
    UserDAO dao;

    @RequestMapping("/")
    @ResponseBody
    String home(){
        return "hello world! 任仲山您好！";
    }

    @RequestMapping("/account")
    @ResponseBody
    String getAccount(){
        return "username: andy ren, password: rzs123456";
    }

    @RequestMapping("/save")
    @ResponseBody
    String saveUser(){

        System.out.println("save users");
        User user = new User("renzhongshan13@otcaix.iscas.ac.cn","andyren");

        if(null == user || null == dao){
            System.out.println("user or dao is null...");
        }

        try {
            dao.create(user);
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
        System.out.println("save users end");
        return "success";
    }
}
