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


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;
import com.devhc.xadmin.annotation.Log;
import com.devhc.xadmin.modules.system.domain.Server;
import com.devhc.xadmin.modules.system.repository.ServerRepository;
import com.devhc.xadmin.modules.system.service.ServerService;
import com.devhc.xadmin.modules.system.service.dto.ServerQueryCriteria;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
 * @date 2022-12-22
 **/
@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Server管理")
@RequestMapping("/api/server")
public class ServerController {


    private final ServerService serverService;
    private final ServerRepository serverRepository;

    @Log("导出数据")
    @Operation(summary = "导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@xps.check('server:list')")
    public void exportServer(HttpServletResponse response, ServerQueryCriteria criteria)
            throws IOException {
        serverService.download(serverService.queryAll(criteria), response);
    }

    @GetMapping("/all")
    @Log("查询ServerAll")
    @Operation(summary = "查询ServerAll")
    @PreAuthorize("@xps.check('server:list')")
    public ResponseEntity<Object> queryAllServer(ServerQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(serverRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping
    @Log("查询Server")
    @Operation(summary = "查询Server")
    @PreAuthorize("@xps.check('server:list')")
    public ResponseEntity<Object> queryServer(ServerQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(serverService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @PostMapping
    @Log("新增Server")
    @Operation(summary = "新增Server")
    @PreAuthorize("@xps.check('server:add')")
    public ResponseEntity<Object> createServer(@Validated @RequestBody Server resources) {
        return new ResponseEntity<>(serverService.create(resources), HttpStatus.CREATED);
    }

    @PostMapping("/batchAdd")
    @Log("批量新增Server")
    @Operation(summary = "批量新增Server")
    @PreAuthorize("@xps.check('server:add')")
    public ResponseEntity<Object> batchAddServer(@RequestBody Map<String, String> args) {
        Arrays.stream(args.get("servers").split("\n")).forEach(server -> {
            List<String> info = Arrays.stream(StringUtils.split(server)).map(String::trim)
                    .collect(Collectors.toList());
            Server s = new Server();
            s.setHost(info.get(0));
            if (info.size() > 1) {
                if (info.get(1).startsWith("{")) {
                    String json = server.substring(server.indexOf("{"));
                    s.setMeta(JSON.toJSONString(JSON.parse(json)));
                } else {
                    Map<String, String> metaMap = new HashMap<>();
                    for (int i = 1; i < info.size(); ++i) {
                        String isplit[] = info.get(i).split("=");
                        metaMap.put(isplit[0], isplit[1]);
                    }
                    s.setMeta(JSON.toJSONString(metaMap));
                }
            }
            s.setStatus(0);
            log.info("{}", s);
            serverService.create(s);
        });
        return new ResponseEntity<>(0, HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改Server")
    @Operation(summary = "修改Server")
    @PreAuthorize("@xps.check('server:edit')")
    public ResponseEntity<Object> updateServer(@Validated @RequestBody Server resources) {
        serverService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除Server")
    @Operation(summary = "删除Server")
    @PreAuthorize("@xps.check('server:del')")
    public ResponseEntity<Object> deleteServer(@RequestBody Long[] ids) {
        serverService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
