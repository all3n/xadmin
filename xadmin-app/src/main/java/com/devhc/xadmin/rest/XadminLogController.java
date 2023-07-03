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
import com.devhc.xadmin.domain.XadminLog;
import com.devhc.xadmin.service.XadminLogService;
import com.devhc.xadmin.service.dto.XadminLogQueryCriteria;
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
@Tag(name = "XadminLog管理")
@RequestMapping("/api/xadminLog")
public class XadminLogController {

    private final XadminLogService xadminLogService;

    @Log("导出数据")
    @Operation(summary="导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@xps.check('xadminLog:list')")
    public void exportXadminLog(HttpServletResponse response, XadminLogQueryCriteria criteria) throws IOException {
        xadminLogService.download(xadminLogService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询XadminLog")
    @Operation(summary="查询XadminLog")
    @PreAuthorize("@xps.check('xadminLog:list')")
    public ResponseEntity<Object> queryXadminLog(XadminLogQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(xadminLogService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增XadminLog")
    @Operation(summary="新增XadminLog")
    @PreAuthorize("@xps.check('xadminLog:add')")
    public ResponseEntity<Object> createXadminLog(@Validated @RequestBody XadminLog resources){
        return new ResponseEntity<>(xadminLogService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改XadminLog")
    @Operation(summary="修改XadminLog")
    @PreAuthorize("@xps.check('xadminLog:edit')")
    public ResponseEntity<Object> updateXadminLog(@Validated @RequestBody XadminLog resources){
        xadminLogService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除XadminLog")
    @Operation(summary="删除XadminLog")
    @PreAuthorize("@xps.check('xadminLog:del')")
    public ResponseEntity<Object> deleteXadminLog(@RequestBody Long[] ids) {
        xadminLogService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
