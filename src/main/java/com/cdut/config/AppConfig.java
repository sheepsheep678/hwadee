package com.cdut.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 通用 Bean 配置
 */
@Configuration
public class AppConfig {

    /**
     * 密码加密器：注册/改密用 encode()，登录用 matches() 比对。
     * BCrypt 每次加密结果不同但可比对，无需自己实现 salt。
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
