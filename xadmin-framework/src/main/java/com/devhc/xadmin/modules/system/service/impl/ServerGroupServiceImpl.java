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
package com.devhc.xadmin.modules.system.service.impl;

import com.devhc.xadmin.modules.system.domain.Server;
import com.devhc.xadmin.modules.system.domain.ServerGroup;
import com.devhc.xadmin.modules.system.domain.vo.UpdateGroupVo;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.devhc.xadmin.modules.system.repository.ServerGroupRepository;
import com.devhc.xadmin.modules.system.repository.ServerRepository;
import com.devhc.xadmin.modules.system.service.ServerGroupService;
import com.devhc.xadmin.modules.system.service.dto.ServerGroupDto;
import com.devhc.xadmin.modules.system.service.dto.ServerGroupQueryCriteria;
import com.devhc.xadmin.modules.system.service.mapstruct.ServerGroupMapper;
import com.devhc.xadmin.utils.FileUtil;
import com.devhc.xadmin.utils.PageUtil;
import com.devhc.xadmin.utils.QueryHelp;
import com.devhc.xadmin.utils.ValidationUtil;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
* 
* @description 服务实现
* 
* @date 2022-12-22
**/
@Service
@Slf4j
@RequiredArgsConstructor
public class ServerGroupServiceImpl implements ServerGroupService {
    private final ServerRepository serverRepository;
    private final ServerGroupRepository serverGroupRepository;
    private final ServerGroupMapper serverGroupMapper;

    @Override
    public Map<String,Object> queryAll(ServerGroupQueryCriteria criteria, Pageable pageable){
        Page<ServerGroup> page = serverGroupRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        List<Long> gIds = page.map(ServerGroup::getGroupId).stream().collect(Collectors.toList());
        Map<Long, Long> cnts = serverGroupRepository.countByIdsAsMap(gIds);
        log.info("{}", cnts);
        return PageUtil.toPage(page.map(serverGroupMapper::toDto).map(dto->{
            dto.setCount(cnts.getOrDefault(dto.getGroupId(), 0L));
            return dto;
        }));
    }

    @Override
    public List<ServerGroupDto> queryAll(ServerGroupQueryCriteria criteria){
        return serverGroupMapper.toDto(serverGroupRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public ServerGroupDto findById(Long groupId) {
        ServerGroup serverGroup = serverGroupRepository.findById(groupId).orElseGet(ServerGroup::new);
        ValidationUtil.isNull(serverGroup.getGroupId(),"ServerGroup","groupId",groupId);
        return serverGroupMapper.toDto(serverGroup);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServerGroupDto create(ServerGroup resources) {
        return serverGroupMapper.toDto(serverGroupRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ServerGroup resources) {
        ServerGroup serverGroup = serverGroupRepository.findById(resources.getGroupId()).orElseGet(ServerGroup::new);
        ValidationUtil.isNull( serverGroup.getGroupId(),"ServerGroup","id",resources.getGroupId());
        serverGroup.copy(resources);
        serverGroupRepository.save(serverGroup);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long groupId : ids) {
            serverGroupRepository.deleteById(groupId);
        }
    }

    @Override
    public void download(List<ServerGroupDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ServerGroupDto serverGroup : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("ID", serverGroup.getGroupId());
            map.put("name", serverGroup.getName());
            map.put("status", serverGroup.getStatus());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public void updateGroup(UpdateGroupVo ug) {
        ServerGroup sg = serverGroupRepository.getById(ug.getGroupId());
        Set<Server> servers;
        if (ug.getIds() != null) {
            servers = ug.getIds().stream().map(id -> {
                Server s = new Server();
                s.setServerId(id);
                return s;
            }).collect(Collectors.toSet());
        } else {
            servers = Arrays.stream(ug.getHosts().split("\n")).map(String::trim).map(s -> {
                Optional<Server> svr = serverRepository.findByHost(s);
                return svr;
            }).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toSet());
        }
        sg.setServers(servers);
        serverGroupRepository.save(sg);
    }
}
