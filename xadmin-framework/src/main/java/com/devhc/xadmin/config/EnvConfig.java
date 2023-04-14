package com.devhc.xadmin.config;

import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class EnvConfig {
    @Autowired
    private ApplicationContext ctx;

    public boolean isProd() {
        return Sets.newHashSet(ctx.getEnvironment().getActiveProfiles()).contains("prod");
    }
}
