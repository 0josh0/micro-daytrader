package org.iscas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by andyren on 2016/6/28.
 */
@Configuration
@EnableTransactionManagement // 启注解事务管理，等同于xml配置方式的 <tx:annotation-driven />
@ComponentScan("org.iscas.*")
@EnableAutoConfiguration
@SpringBootApplication
public class Startup {
    public static void main(String[] args){
        SpringApplication.run(Startup.class, args);
    }
//    @Primary
//    @Bean
//    Sampler sampler() {
//        return new AlwaysSampler();
//    }
//
//    @Bean
//    public AlwaysSampler defaultSampler(){
//        return new AlwaysSampler();
//    }
}
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
