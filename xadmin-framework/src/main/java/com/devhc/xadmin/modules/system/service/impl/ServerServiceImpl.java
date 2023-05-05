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


import java.util.Optional;

import com.devhc.xadmin.exception.EntityExistException;
import com.devhc.xadmin.modules.system.domain.Server;
import com.devhc.xadmin.modules.system.repository.ServerRepository;
import com.devhc.xadmin.modules.system.service.ServerService;
import com.devhc.xadmin.modules.system.service.dto.ServerDto;
import com.devhc.xadmin.modules.system.service.dto.ServerQueryCriteria;
import com.devhc.xadmin.modules.system.service.mapstruct.ServerMapper;
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
public class ServerServiceImpl implements ServerService {

    private final ServerRepository serverRepository;
    private final ServerMapper serverMapper;

    @Override
    public Map<String,Object> queryAll(ServerQueryCriteria criteria, Pageable pageable){
        Page<Server> page = serverRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(serverMapper::toDto));
    }

    @Override
    public List<ServerDto> queryAll(ServerQueryCriteria criteria){
        return serverMapper.toDto(serverRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public ServerDto findById(Long serverId) {
        Server server = serverRepository.findById(serverId).orElseGet(Server::new);
        ValidationUtil.isNull(server.getServerId(),"Server","serverId",serverId);
        return serverMapper.toDto(server);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServerDto create(Server resources) {
        Optional<Server> s = serverRepository.findByHost(resources.getHost());
        if(!s.isPresent()) {
            return serverMapper.toDto(serverRepository.save(resources));
        }else{
            return null;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Server resources) {
        Optional<Server> s = serverRepository.findByHost(resources.getHost());
        if(s.isPresent() && s.get().getServerId() != resources.getServerId()){
            throw new EntityExistException(Server.class, "host", resources.getHost());
        }else {
            Server server = serverRepository.findById(resources.getServerId())
                .orElseGet(Server::new);
            ValidationUtil.isNull(server.getServerId(), "Server", "id", resources.getServerId());
            server.copy(resources);
            serverRepository.save(server);
        }
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long serverId : ids) {
            serverRepository.deleteById(serverId);
        }
    }

    @Override
    public void download(List<ServerDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ServerDto server : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("host or ip", server.getHost());
            map.put("status 0 ok 1 disable", server.getStatus());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
