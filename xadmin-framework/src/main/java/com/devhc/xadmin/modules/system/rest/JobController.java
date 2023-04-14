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
package com.devhc.xadmin.modules.system.rest;

import com.devhc.xadmin.modules.system.domain.Job;
import com.devhc.xadmin.modules.system.service.JobService;
import com.devhc.xadmin.modules.system.service.dto.JobQueryCriteria;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import com.devhc.xadmin.annotation.Log;
import com.devhc.xadmin.exception.BadRequestException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
* 
*
*/
@RestController
@RequiredArgsConstructor
@Tag(name = "系统：岗位管理")
@RequestMapping("/api/job")
public class JobController {

    private final JobService jobService;
    private static final String ENTITY_NAME = "job";

    @Operation(summary="导出岗位数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@xps.check('job:list')")
    public void exportJob(HttpServletResponse response, JobQueryCriteria criteria) throws IOException {
        jobService.download(jobService.queryAll(criteria), response);
    }

    @Operation(summary="查询岗位")
    @GetMapping
    @PreAuthorize("@xps.check('job:list','user:list')")
    public ResponseEntity<Object> queryJob(JobQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(jobService.queryAll(criteria, pageable),HttpStatus.OK);
    }

    @Log("新增岗位")
    @Operation(summary="新增岗位")
    @PostMapping
    @PreAuthorize("@xps.check('job:add')")
    public ResponseEntity<Object> createJob(@Validated @RequestBody Job resources){
        if (resources.getId() != null) {
            throw new BadRequestException("A new "+ ENTITY_NAME +" cannot already have an ID");
        }
        jobService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Log("修改岗位")
    @Operation(summary="修改岗位")
    @PutMapping
    @PreAuthorize("@xps.check('job:edit')")
    public ResponseEntity<Object> updateJob(@Validated(Job.Update.class) @RequestBody Job resources){
        jobService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除岗位")
    @Operation(summary="删除岗位")
    @DeleteMapping
    @PreAuthorize("@xps.check('job:del')")
    public ResponseEntity<Object> deleteJob(@RequestBody Set<Long> ids){
        // 验证是否被用户关联
        jobService.verification(ids);
        jobService.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
