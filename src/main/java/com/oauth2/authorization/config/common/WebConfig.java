package com.oauth2.authorization.config.common;

import com.oauth2.authorization.config.filter.RestApiLoggingFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public FilterRegistrationBean<RestApiLoggingFilter> loggingFilter() {
        FilterRegistrationBean<RestApiLoggingFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RestApiLoggingFilter());
        registrationBean.addUrlPatterns("/api/*"); // 필터를 적용할 URL 패턴
        registrationBean.setOrder(1);
        return registrationBean;
    }

}
