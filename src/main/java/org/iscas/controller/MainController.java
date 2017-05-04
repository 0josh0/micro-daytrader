package org.iscas.controller;

//import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by andyren on 2016/6/28.
 */
@RestController
@RequestMapping(value = "/control")
public class MainController {
    @RequestMapping(value = "/")
    @ResponseBody
    public String index(){
        return "Welcome to NASDAQ DayTrader System !";
    }
}
