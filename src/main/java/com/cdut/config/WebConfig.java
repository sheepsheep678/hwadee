package com.cdut.config;

import com.cdut.interceptor.LoginInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC 配置：跨域、拦截器注册、上传文件静态映射。
 */
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final LoginInterceptor loginInterceptor;

    @Value("${file.upload-dir:./uploads}")
    private String uploadDir;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/doctor/auth/login", "/api/doctor/auth/register");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("*")
                .allowedHeaders("*")
                .maxAge(168000);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String path = uploadDir;
        if (!path.endsWith("/") && !path.endsWith("\\")) {
            path = path + "/";
        }
        registry.addResourceHandler("/files/**")
                .addResourceLocations("file:" + path);
    }

}
