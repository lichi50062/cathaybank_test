package org.example.cathaybank.security;

import org.example.cathaybank.filter.XssFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

/**
 * @Author: Lichi
 * @Date: 2024/02/03/上午 05:49
 * @Description:
 */

@Configuration
public class WebSecurityConfig {

    @Bean
    public FilterRegistrationBean<XssFilter> xssPreventFilter() {
        FilterRegistrationBean<XssFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new XssFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setName("xssPreventFilter");
        registrationBean.setOrder(Ordered.LOWEST_PRECEDENCE);
        return registrationBean;
    }
}
