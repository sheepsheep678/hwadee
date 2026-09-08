package com.cdut.config;

import com.github.pagehelper.PageInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class MybatisConfig {

    // 手动注册 PageHelper 拦截器（适配 Spring Boot 4，不依赖 starter 自动装配） /
    @Bean
    public Interceptor pageInterceptor() {
        PageInterceptor pageInterceptor = new PageInterceptor();
        Properties properties = new Properties();
        // 数据库方言 /
        properties.setProperty("helperDialect", "mysql");
        // 分页合理化：pageNum<1 查第一页，pageNum>总页数 查最后一页 /
        properties.setProperty("reasonable", "true");
        // 支持通过 Mapper 方法参数传递分页参数 /
        properties.setProperty("supportMethodsArguments", "true");
        properties.setProperty("params", "count=countSql");
        pageInterceptor.setProperties(properties);
        return pageInterceptor;
    }
}