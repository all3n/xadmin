package com.devhc.xadmin.config;

import com.devhc.xadmin.utils.SpringContextHolder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class XAdminConfigs {
    @Bean
    public SpringContextHolder springContextHolder() {
        return new SpringContextHolder();
    }
}
