package org.iscas.service;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by andyren on 2016/9/14.
 */
@RestController
public class QuoteRest {



    @RequestMapping("/")
    public String welcome(){
        return "欢迎访问DayTrader Quote Service！";
    }



}
