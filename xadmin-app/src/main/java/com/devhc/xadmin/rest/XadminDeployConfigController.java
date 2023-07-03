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
package com.devhc.xadmin.rest;

import com.devhc.xadmin.annotation.Log;
import com.devhc.xadmin.domain.XadminDeployConfig;
import com.devhc.xadmin.service.XadminDeployConfigService;
import com.devhc.xadmin.service.dto.XadminDeployConfigQueryCriteria;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* 
* 
* @date 2023-05-10
**/
@RestController
@RequiredArgsConstructor
@Tag(name = "XadminDeployConfig管理")
@RequestMapping("/api/xadminDeployConfig")
public class XadminDeployConfigController {

    private final XadminDeployConfigService xadminDeployConfigService;

    @Log("导出数据")
    @Operation(summary="导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@xps.check('xadminDeployConfig:list')")
    public void exportXadminDeployConfig(HttpServletResponse response, XadminDeployConfigQueryCriteria criteria) throws IOException {
        xadminDeployConfigService.download(xadminDeployConfigService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询XadminDeployConfig")
    @Operation(summary="查询XadminDeployConfig")
    @PreAuthorize("@xps.check('xadminDeployConfig:list')")
    public ResponseEntity<Object> queryXadminDeployConfig(XadminDeployConfigQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(xadminDeployConfigService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增XadminDeployConfig")
    @Operation(summary="新增XadminDeployConfig")
    @PreAuthorize("@xps.check('xadminDeployConfig:add')")
    public ResponseEntity<Object> createXadminDeployConfig(@Validated @RequestBody XadminDeployConfig resources){
        return new ResponseEntity<>(xadminDeployConfigService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改XadminDeployConfig")
    @Operation(summary="修改XadminDeployConfig")
    @PreAuthorize("@xps.check('xadminDeployConfig:edit')")
    public ResponseEntity<Object> updateXadminDeployConfig(@Validated @RequestBody XadminDeployConfig resources){
        xadminDeployConfigService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除XadminDeployConfig")
    @Operation(summary="删除XadminDeployConfig")
    @PreAuthorize("@xps.check('xadminDeployConfig:del')")
    public ResponseEntity<Object> deleteXadminDeployConfig(@RequestBody Long[] ids) {
        xadminDeployConfigService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
