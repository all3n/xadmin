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
package com.devhc.xadmin.service.mapstruct;

import com.devhc.xadmin.base.BaseMapper;
import com.devhc.xadmin.domain.XadminApp;
import com.devhc.xadmin.service.dto.XadminAppDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* 
* 
* @date 2023-05-10
**/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface XadminAppMapper extends BaseMapper<XadminAppDto, XadminApp> {

}