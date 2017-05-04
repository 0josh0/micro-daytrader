package org.iscas.service;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by andyren on 2016/9/14.
 */
@ComponentScan("org.iscas.*")
@RestController
public class OrdersRest {

    @RequestMapping("/")
    public String index(){
        return "欢迎访问DayTrader Orders Service！";
    }

}
