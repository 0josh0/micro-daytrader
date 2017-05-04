package org.iscas.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by andyren on 2016/6/27.
 * 完成数据库服务的处理，支持各类型的数据库操作；为DayTrader的业务处理做支撑
 */
public class DBService {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @RequestMapping("/getuserinfo")
    public String getUserAccount(String userId){

         return "用户信息： ";
    }

}
