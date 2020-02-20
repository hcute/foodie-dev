package com.imooc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    public CorsConfig() {
    }

    @Bean
    public CorsFilter corsFilter(){
        // 1. 配置Cors配置
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("http://localhost:8080"); //添加准许跨域的url
        corsConfiguration.setAllowCredentials(true); // 请求准许携带凭证信息 比如cookie
        corsConfiguration.addAllowedMethod("*"); // 准许请求的方式
        corsConfiguration.addAllowedHeader("*"); // 设置准许的header

        // 2. 添加url映射路径
        UrlBasedCorsConfigurationSource corsSource = new UrlBasedCorsConfigurationSource();
        corsSource.registerCorsConfiguration("/**",corsConfiguration); // 前端所有的请求

        // 3. 返回CorsFilter
        return  new CorsFilter(corsSource);
    }
}
