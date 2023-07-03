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
import com.devhc.xadmin.domain.XadminEnv;
import com.devhc.xadmin.service.XadminEnvService;
import com.devhc.xadmin.service.dto.XadminEnvQueryCriteria;
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
@Tag(name = "XadminEnv管理")
@RequestMapping("/api/xadminEnv")
public class XadminEnvController {

    private final XadminEnvService xadminEnvService;

    @Log("导出数据")
    @Operation(summary="导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@xps.check('xadminEnv:list')")
    public void exportXadminEnv(HttpServletResponse response, XadminEnvQueryCriteria criteria) throws IOException {
        xadminEnvService.download(xadminEnvService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询XadminEnv")
    @Operation(summary="查询XadminEnv")
    @PreAuthorize("@xps.check('xadminEnv:list')")
    public ResponseEntity<Object> queryXadminEnv(XadminEnvQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(xadminEnvService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增XadminEnv")
    @Operation(summary="新增XadminEnv")
    @PreAuthorize("@xps.check('xadminEnv:add')")
    public ResponseEntity<Object> createXadminEnv(@Validated @RequestBody XadminEnv resources){
        return new ResponseEntity<>(xadminEnvService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改XadminEnv")
    @Operation(summary="修改XadminEnv")
    @PreAuthorize("@xps.check('xadminEnv:edit')")
    public ResponseEntity<Object> updateXadminEnv(@Validated @RequestBody XadminEnv resources){
        xadminEnvService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除XadminEnv")
    @Operation(summary="删除XadminEnv")
    @PreAuthorize("@xps.check('xadminEnv:del')")
    public ResponseEntity<Object> deleteXadminEnv(@RequestBody Integer[] ids) {
        xadminEnvService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
