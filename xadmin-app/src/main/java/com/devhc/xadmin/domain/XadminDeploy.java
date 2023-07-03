/*
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
package com.devhc.xadmin.domain;

import com.devhc.xadmin.base.BaseEntity;
import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.sql.Timestamp;
import java.io.Serializable;

/**
* 
* @description /
* 
* @date 2023-05-10
**/
@Entity
@Data
@Table(name="xadmin_deploy")
public class XadminDeploy extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`deploy_id`")
    @Schema(title = "ID")
    private Long deployId;

    @Column(name = "`app_id`",nullable = false)
    @NotNull
    @Schema(title = "app id")
    private Long appId;

    @Column(name = "`cfg_id`",nullable = false)
    @NotNull
    @Schema(title = "cfg id")
    private Long cfgId;

    @Column(name = "`env_id`",nullable = false)
    @NotNull
    @Schema(title = "env id")
    private Integer envId;

    @Column(name = "`revision`",nullable = false)
    @NotBlank
    @Schema(title = "revision")
    private String revision;

    @Column(name = "`status`",nullable = false)
    @NotNull
    @Schema(title = "status")
    private Integer status;


    public void copy(XadminDeploy source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
