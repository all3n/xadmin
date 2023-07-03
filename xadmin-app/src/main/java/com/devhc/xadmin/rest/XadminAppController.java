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
import com.devhc.xadmin.domain.XadminApp;
import com.devhc.xadmin.service.XadminAppService;
import com.devhc.xadmin.service.dto.XadminAppQueryCriteria;
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
@Tag(name = "XadminApp管理")
@RequestMapping("/api/xadminApp")
public class XadminAppController {

    private final XadminAppService xadminAppService;

    @Log("导出数据")
    @Operation(summary="导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@xps.check('xadminApp:list')")
    public void exportXadminApp(HttpServletResponse response, XadminAppQueryCriteria criteria) throws IOException {
        xadminAppService.download(xadminAppService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询XadminApp")
    @Operation(summary="查询XadminApp")
    @PreAuthorize("@xps.check('xadminApp:list')")
    public ResponseEntity<Object> queryXadminApp(XadminAppQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(xadminAppService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增XadminApp")
    @Operation(summary="新增XadminApp")
    @PreAuthorize("@xps.check('xadminApp:add')")
    public ResponseEntity<Object> createXadminApp(@Validated @RequestBody XadminApp resources){
        return new ResponseEntity<>(xadminAppService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改XadminApp")
    @Operation(summary="修改XadminApp")
    @PreAuthorize("@xps.check('xadminApp:edit')")
    public ResponseEntity<Object> updateXadminApp(@Validated @RequestBody XadminApp resources){
        xadminAppService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除XadminApp")
    @Operation(summary="删除XadminApp")
    @PreAuthorize("@xps.check('xadminApp:del')")
    public ResponseEntity<Object> deleteXadminApp(@RequestBody Long[] ids) {
        xadminAppService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
