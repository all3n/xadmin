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
import com.devhc.xadmin.domain.XadminDeploy;
import com.devhc.xadmin.service.XadminDeployService;
import com.devhc.xadmin.service.dto.XadminDeployQueryCriteria;
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
@Tag(name = "XadminDeploy管理")
@RequestMapping("/api/xadminDeploy")
public class XadminDeployController {

    private final XadminDeployService xadminDeployService;

    @Log("导出数据")
    @Operation(summary="导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@xps.check('xadminDeploy:list')")
    public void exportXadminDeploy(HttpServletResponse response, XadminDeployQueryCriteria criteria) throws IOException {
        xadminDeployService.download(xadminDeployService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询XadminDeploy")
    @Operation(summary="查询XadminDeploy")
    @PreAuthorize("@xps.check('xadminDeploy:list')")
    public ResponseEntity<Object> queryXadminDeploy(XadminDeployQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(xadminDeployService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增XadminDeploy")
    @Operation(summary="新增XadminDeploy")
    @PreAuthorize("@xps.check('xadminDeploy:add')")
    public ResponseEntity<Object> createXadminDeploy(@Validated @RequestBody XadminDeploy resources){
        return new ResponseEntity<>(xadminDeployService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改XadminDeploy")
    @Operation(summary="修改XadminDeploy")
    @PreAuthorize("@xps.check('xadminDeploy:edit')")
    public ResponseEntity<Object> updateXadminDeploy(@Validated @RequestBody XadminDeploy resources){
        xadminDeployService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除XadminDeploy")
    @Operation(summary="删除XadminDeploy")
    @PreAuthorize("@xps.check('xadminDeploy:del')")
    public ResponseEntity<Object> deleteXadminDeploy(@RequestBody Long[] ids) {
        xadminDeployService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
