/*
 *  Copyright 2019-2020
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.devhc.xadmin.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * WebMvcConfigurer
 */
@Slf4j
@Configuration
@EnableWebMvc
@RequiredArgsConstructor
public class ConfigurerAdapter implements WebMvcConfigurer {
  private final FileProperties properties;
  private final EnvConfig envConfig;

  @Value("${app.web.path}")
  private String webPath;



  @Bean
  public CorsFilter corsFilter() {
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowCredentials(true);
    config.addAllowedOriginPattern("*");
    config.addAllowedHeader("*");
    config.addAllowedMethod("*");
    source.registerCorsConfiguration("/**", config);
    return new CorsFilter(source);
  }

  @Override
  public void addViewControllers(ViewControllerRegistry registry) {
    if(envConfig.isProd()){
      registry.addViewController("/").setViewName("forward:/index.html");
    }else{
      registry.addViewController("/").setViewName("forward:/doc.html");
    }
  }

  @Bean
  public WebServerFactoryCustomizer<ConfigurableWebServerFactory> webServerFactoryCustomizer() {
    return factory -> {
      String defIndex = "/index.html";
      if(!envConfig.isProd()) {
        defIndex = "/doc.html";
      }
      ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, defIndex);
      ErrorPage unauthPage = new ErrorPage(HttpStatus.UNAUTHORIZED, defIndex);
      factory.addErrorPages(error404Page, unauthPage);
    };
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    FileProperties.XPath path = properties.getPath();
    String avatarUtl = "file:" + path.getAvatar().replace("\\", "/");
    String pathUtl = "file:" + path.getPath().replace("\\", "/");
    log.info("www  prod:{}:{}", envConfig.isProd(), webPath);
    registry.addResourceHandler("/avatar/**").addResourceLocations(avatarUtl).setCachePeriod(0);
    registry.addResourceHandler("/file/**").addResourceLocations(pathUtl).setCachePeriod(0);
    //registry.addResourceHandler("/**").addResourceLocations("classpath:/META-INF/resources/").setCachePeriod(0);
    registry.addResourceHandler("/**")
        .addResourceLocations(webPath, "classpath:/BOOT-INF/classes/public/", "classpath:/dist/",
            "classpath:/META-INF/resources/")
        .setCachePeriod(0);
  }


  @Override
  public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
    // avoid base64 error
    converters.add(new ByteArrayHttpMessageConverter());
    // 使用 fastjson 序列化，会导致 @JsonIgnore 失效，可以使用 @JSONField(serialize = false) 替换
    FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
    List<MediaType> supportMediaTypeList = new ArrayList<>();
    supportMediaTypeList.add(MediaType.APPLICATION_JSON);
    FastJsonConfig config = new FastJsonConfig();
    config.setDateFormat("yyyy-MM-dd HH:mm:ss");
    config.setSerializerFeatures(SerializerFeature.DisableCircularReferenceDetect);
    converter.setFastJsonConfig(config);
    converter.setSupportedMediaTypes(supportMediaTypeList);
    converter.setDefaultCharset(StandardCharsets.UTF_8);
    converters.add(converter);

  }
}
