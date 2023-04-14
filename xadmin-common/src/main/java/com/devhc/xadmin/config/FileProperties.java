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

import com.devhc.xadmin.utils.XAdminConstant;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "file")
public class FileProperties {
    private Long maxSize;
    private Long avatarMaxSize;
    private XPath mac;
    private XPath linux;
    private XPath windows;

    public XPath getPath() {
        String os = System.getProperty("os.name");
        if (os.toLowerCase().startsWith(XAdminConstant.WIN)) {
            return windows;
        } else if (os.toLowerCase().startsWith(XAdminConstant.MAC)) {
            return mac;
        }
        return linux;
    }

    @Data
    public static class XPath {
        private String path;
        private String avatar;
    }
}
