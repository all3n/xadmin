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
package com.devhc.xadmin.modules.quartz.rest;

import com.devhc.xadmin.modules.quartz.domain.QuartzJob;
import com.devhc.xadmin.modules.quartz.service.QuartzJobService;
import com.devhc.xadmin.modules.quartz.service.dto.JobQueryCriteria;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.devhc.xadmin.annotation.Log;
import com.devhc.xadmin.exception.BadRequestException;
import com.devhc.xadmin.utils.SpringContextHolder;
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
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/jobs")
@Tag(name = "系统:定时任务管理")
public class QuartzJobController {

    private static final String ENTITY_NAME = "quartzJob";
    private final QuartzJobService quartzJobService;

    @Operation(summary="查询定时任务")
    @GetMapping
    @PreAuthorize("@xps.check('timing:list')")
    public ResponseEntity<Object> queryQuartzJob(JobQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(quartzJobService.queryAll(criteria,pageable), HttpStatus.OK);
    }

    @Operation(summary="导出任务数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@xps.check('timing:list')")
    public void exportQuartzJob(HttpServletResponse response, JobQueryCriteria criteria) throws IOException {
        quartzJobService.download(quartzJobService.queryAll(criteria), response);
    }

    @Operation(summary="导出日志数据")
    @GetMapping(value = "/logs/download")
    @PreAuthorize("@xps.check('timing:list')")
    public void exportQuartzJobLog(HttpServletResponse response, JobQueryCriteria criteria) throws IOException {
        quartzJobService.downloadLog(quartzJobService.queryAllLog(criteria), response);
    }

    @Operation(summary="查询任务执行日志")
    @GetMapping(value = "/logs")
    @PreAuthorize("@xps.check('timing:list')")
    public ResponseEntity<Object> queryQuartzJobLog(JobQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(quartzJobService.queryAllLog(criteria,pageable), HttpStatus.OK);
    }

    @Log("新增定时任务")
    @Operation(summary="新增定时任务")
    @PostMapping
    @PreAuthorize("@xps.check('timing:add')")
    public ResponseEntity<Object> createQuartzJob(@Validated @RequestBody QuartzJob resources){
        if (resources.getId() != null) {
            throw new BadRequestException("A new "+ ENTITY_NAME +" cannot already have an ID");
        }
        // 验证Bean是不是合法的，合法的定时任务 Bean 需要用 @Service 定义
        checkBean(resources.getBeanName());
        quartzJobService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Log("修改定时任务")
    @Operation(summary="修改定时任务")
    @PutMapping
    @PreAuthorize("@xps.check('timing:edit')")
    public ResponseEntity<Object> updateQuartzJob(@Validated(QuartzJob.Update.class) @RequestBody QuartzJob resources){
        // 验证Bean是不是合法的，合法的定时任务 Bean 需要用 @Service 定义
        checkBean(resources.getBeanName());
        quartzJobService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("更改定时任务状态")
    @Operation(summary="更改定时任务状态")
    @PutMapping(value = "/{id}")
    @PreAuthorize("@xps.check('timing:edit')")
    public ResponseEntity<Object> updateQuartzJobStatus(@PathVariable Long id){
        quartzJobService.updateIsPause(quartzJobService.findById(id));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("执行定时任务")
    @Operation(summary="执行定时任务")
    @PutMapping(value = "/exec/{id}")
    @PreAuthorize("@xps.check('timing:edit')")
    public ResponseEntity<Object> executionQuartzJob(@PathVariable Long id){
        quartzJobService.execution(quartzJobService.findById(id));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除定时任务")
    @Operation(summary="删除定时任务")
    @DeleteMapping
    @PreAuthorize("@xps.check('timing:del')")
    public ResponseEntity<Object> deleteQuartzJob(@RequestBody Set<Long> ids){
        quartzJobService.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void checkBean(String beanName){
        // 避免调用攻击者可以从SpringContextHolder获得控制jdbcTemplate类
        // 并使用getDeclaredMethod调用jdbcTemplate的queryForMap函数，执行任意sql命令。
        if(!SpringContextHolder.getAllServiceBeanName().contains(beanName)){
            throw new BadRequestException("非法的 Bean，请重新输入！");
        }
    }
}
