package com.cdut;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.cdut.mapper")
public class YiyangProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(YiyangProjectApplication.class, args);
    }

}
