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
package com.devhc.xadmin.modules.system.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;


/**
* 
* @description /
* 
* @date 2022-12-22
**/
@Entity
@Data
@Table(name="server_group")
public class ServerGroup implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`group_id`")
    @Schema(title ="Group ID")
    private Long groupId;

    @Column(name = "`name`",nullable = false)
    @NotBlank
    @Schema(title = "group name")
    private String name;

    @Column(name = "`status`",nullable = false)
    @NotNull
    @Schema(title = "status 0 ok 1 disable")
    private Integer status;

    @ToString.Exclude
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "server_group_config",
        joinColumns = {@JoinColumn(name = "group_id",referencedColumnName = "group_id")},
        inverseJoinColumns = {@JoinColumn(name = "server_id",referencedColumnName = "server_id")})
    private Set<Server> servers;

    public void copy(ServerGroup source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
