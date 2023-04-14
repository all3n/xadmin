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

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * api页面 /doc.html
 */
@Configuration
public class SwaggerConfig {

    @Value("${jwt.header}")
    private String tokenHeader;

    @Bean
    public OpenAPI openApi() {
        return new OpenAPI()
                .info(new Info().title("xadmin api")
                        .description("xadmin")
                        .version("v0.0.1"));
    }


    @Schema
    @Data
    private static class Page {
        @Schema(title = "页码 (0..N)")
        private Integer page;

        @Schema(title = "每页显示的数目")
        private Integer size;

        @Schema(title = "以下列格式排序标准：property[,asc | desc]。 默认排序顺序为升序。 支持多种排序条件：如：id,asc")
        private List<String> sort;
    }
}
