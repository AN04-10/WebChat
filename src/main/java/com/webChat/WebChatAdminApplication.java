package com.webChat;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan(basePackages = {"com.webChat.mapper"})
@SpringBootApplication
public class WebChatAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebChatAdminApplication.class, args);
        System.out.println("        webChat    启动成功        ");
    }

}
