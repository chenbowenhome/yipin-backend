package com.yipin.basic;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import utils.JwtUtil;

@SpringBootApplication
@EnableSwagger2Doc
public class BasicApplication {
    public static void main(String[] args){
        SpringApplication.run(BasicApplication.class,args);
    }

    /**添加jwtUtil**/
    @Bean
    public JwtUtil jwtUtil(){
        return new JwtUtil();
    }
}
