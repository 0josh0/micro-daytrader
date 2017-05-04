package org.iscas.spring;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by andyren on 2016/6/27.
 */
@Controller
public class AccountController {
    @RequestMapping("/get-by-accountid")
    @ResponseBody
    public String getByAccountId(int accountId){

        return "success";

    }
}
