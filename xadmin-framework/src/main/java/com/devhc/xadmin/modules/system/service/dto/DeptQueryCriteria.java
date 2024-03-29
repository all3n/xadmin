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
package com.devhc.xadmin.modules.system.service.dto;

import com.devhc.xadmin.annotation.XQuery;
import lombok.Data;
import com.devhc.xadmin.annotation.DataPermission;

import java.sql.Timestamp;
import java.util.List;

/**
* 
*
*/
@Data
@DataPermission(fieldName = "id")
public class DeptQueryCriteria{

    @XQuery(type = XQuery.Type.INNER_LIKE)
    private String name;

    @XQuery
    private Boolean enabled;

    @XQuery
    private Long pid;

    @XQuery(type = XQuery.Type.IS_NULL, propName = "pid")
    private Boolean pidIsNull;

    @XQuery(type = XQuery.Type.BETWEEN)
    private List<Timestamp> createTime;
}
