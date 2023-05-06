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
package com.devhc.xadmin.modules.system.rest;
import com.devhc.xadmin.annotation.Log;

import java.util.List;

import com.devhc.xadmin.modules.system.domain.ServerGroup;
import com.devhc.xadmin.modules.system.domain.vo.UpdateGroupVo;
import com.devhc.xadmin.modules.system.repository.ServerGroupRepository;
import com.devhc.xadmin.modules.system.service.ServerGroupService;
import com.devhc.xadmin.modules.system.service.dto.ServerGroupQueryCriteria;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* 
* 
* @date 2022-12-22
**/
@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "ServerGroup管理")
@RequestMapping("/api/serverGroup")
public class ServerGroupController {

    private final ServerGroupService serverGroupService;
    private final ServerGroupRepository repo;

    @Log("导出数据")
    @Operation(summary="导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@xps.check('serverGroup:list')")
    public void exportServerGroup(HttpServletResponse response, ServerGroupQueryCriteria criteria) throws IOException {
        serverGroupService.download(serverGroupService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询ServerGroup")
    @Operation(summary="查询ServerGroup")
    @PreAuthorize("@xps.check('serverGroup:list')")
    public ResponseEntity<Object> queryServerGroup(ServerGroupQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(serverGroupService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增ServerGroup")
    @Operation(summary="新增ServerGroup")
    @PreAuthorize("@xps.check('serverGroup:add')")
    public ResponseEntity<Object> createServerGroup(@Validated @RequestBody ServerGroup resources){
        return new ResponseEntity<>(serverGroupService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改ServerGroup")
    @Operation(summary="修改ServerGroup")
    @PreAuthorize("@xps.check('serverGroup:edit')")
    public ResponseEntity<Object> updateServerGroup(@Validated @RequestBody ServerGroup resources){
        serverGroupService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除ServerGroup")
    @Operation(summary="删除ServerGroup")
    @PreAuthorize("@xps.check('serverGroup:del')")
    public ResponseEntity<Object> deleteServerGroup(@RequestBody Long[] ids) {
        serverGroupService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }



    @PostMapping("updateGroup")
    @Log("修改ServerGroup servers")
    @Operation(summary="修改ServerGroup servers")
    @PreAuthorize("@xps.check('serverGroup:edit')")
    public ResponseEntity<Object> createServerGroup(@RequestBody UpdateGroupVo ug){
        serverGroupService.updateGroup(ug);
        return new ResponseEntity<>("",HttpStatus.CREATED);
    }
    @PostMapping("updateGroupText")
    @Log("修改ServerGroup servers text")
    @Operation(summary="修改ServerGroup servers Text")
    @PreAuthorize("@xps.check('serverGroup:edit')")
    public ResponseEntity<Object> createServerGroupText(@RequestBody UpdateGroupVo ug){
        serverGroupService.updateGroup(ug);
        return new ResponseEntity<>("",HttpStatus.CREATED);
    }


    @GetMapping("hosts")
    @Log("获取group hosts")
    @Operation(summary="获取group Hosts")
    @PreAuthorize("@xps.check('serverGroup:list')")
    public ResponseEntity<Object> getHosts(@RequestParam("groupId") Long groupId){
        ServerGroup r = repo.getById(groupId);
        return new ResponseEntity<>(r.getServers(),HttpStatus.OK);
    }
    @GetMapping("search")
    @Log("search serverGroup by name")
    @Operation(summary="search serverGroup by name")
    @PreAuthorize("@xps.check('serverGroup:list')")
    public ResponseEntity<Object> getHosts(@RequestParam("query") String query){
        List<ServerGroup> r = repo.findTop10ByNameContainingAllIgnoreCase(query);
        return new ResponseEntity<>(r,HttpStatus.OK);
    }
}
