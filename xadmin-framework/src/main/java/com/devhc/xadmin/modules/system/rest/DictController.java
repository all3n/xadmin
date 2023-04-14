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

import com.devhc.xadmin.modules.system.domain.Dict;
import com.devhc.xadmin.modules.system.service.DictService;
import com.devhc.xadmin.modules.system.service.dto.DictQueryCriteria;
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
@Tag(name = "系统：字典管理")
@RequestMapping("/api/dict")
public class DictController {

    private final DictService dictService;
    private static final String ENTITY_NAME = "dict";

    @Operation(summary="导出字典数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@xps.check('dict:list')")
    public void exportDict(HttpServletResponse response, DictQueryCriteria criteria) throws IOException {
        dictService.download(dictService.queryAll(criteria), response);
    }

    @Operation(summary="查询字典")
    @GetMapping(value = "/all")
    @PreAuthorize("@xps.check('dict:list')")
    public ResponseEntity<Object> queryAllDict(){
        return new ResponseEntity<>(dictService.queryAll(new DictQueryCriteria()),HttpStatus.OK);
    }

    @Operation(summary="查询字典")
    @GetMapping
    @PreAuthorize("@xps.check('dict:list')")
    public ResponseEntity<Object> queryDict(DictQueryCriteria resources, Pageable pageable){
        return new ResponseEntity<>(dictService.queryAll(resources,pageable),HttpStatus.OK);
    }

    @Log("新增字典")
    @Operation(summary="新增字典")
    @PostMapping
    @PreAuthorize("@xps.check('dict:add')")
    public ResponseEntity<Object> createDict(@Validated @RequestBody Dict resources){
        if (resources.getId() != null) {
            throw new BadRequestException("A new "+ ENTITY_NAME +" cannot already have an ID");
        }
        dictService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Log("修改字典")
    @Operation(summary="修改字典")
    @PutMapping
    @PreAuthorize("@xps.check('dict:edit')")
    public ResponseEntity<Object> updateDict(@Validated(Dict.Update.class) @RequestBody Dict resources){
        dictService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除字典")
    @Operation(summary="删除字典")
    @DeleteMapping
    @PreAuthorize("@xps.check('dict:del')")
    public ResponseEntity<Object> deleteDict(@RequestBody Set<Long> ids){
        dictService.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
