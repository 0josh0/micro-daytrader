package org.iscas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/*
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@AutoConfigurationPackage
@SpringBootApplication
*/

/**
 * Created by andyren on 2016/9/14.
 */
@Configuration
@EnableTransactionManagement // 启注解事务管理，等同于xml配置方式的 <tx:annotation-driven />
@ComponentScan("org.iscas.*")
@EnableAutoConfiguration
@SpringBootApplication
//public class Startup extends SpringBootServletInitializer {
//    @Override
//    protected SpringApplicationBuilder configure(
//            SpringApplicationBuilder application) {
//        return application.sources(Startup.class);
//    }
//    public static void main(String[] args){
//        SpringApplication.run(Startup.class, args);
//    }
//}
public class Startup {
    public static void main(String[] args){
        SpringApplication.run(Startup.class, args);
    }
}
