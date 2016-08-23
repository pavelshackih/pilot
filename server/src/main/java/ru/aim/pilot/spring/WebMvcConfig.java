package ru.aim.pilot.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    private UserSessionInterceptor userSessionInterceptor;

    @Autowired
    public void setUserSessionInterceptor(UserSessionInterceptor userSessionInterceptor) {
        this.userSessionInterceptor = userSessionInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        super.addInterceptors(registry);
        registry.addInterceptor(userSessionInterceptor).addPathPatterns("/**");
    }
}
