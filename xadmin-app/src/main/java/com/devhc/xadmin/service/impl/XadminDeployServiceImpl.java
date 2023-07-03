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
package com.devhc.xadmin.service.impl;

import com.devhc.xadmin.domain.XadminDeploy;
import com.devhc.xadmin.utils.ValidationUtil;
import com.devhc.xadmin.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import com.devhc.xadmin.repository.XadminDeployRepository;
import com.devhc.xadmin.service.XadminDeployService;
import com.devhc.xadmin.service.dto.XadminDeployDto;
import com.devhc.xadmin.service.dto.XadminDeployQueryCriteria;
import com.devhc.xadmin.service.mapstruct.XadminDeployMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.devhc.xadmin.utils.PageUtil;
import com.devhc.xadmin.utils.QueryHelp;
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
* @date 2023-05-10
**/
@Service
@RequiredArgsConstructor
public class XadminDeployServiceImpl implements XadminDeployService {

    private final XadminDeployRepository xadminDeployRepository;
    private final XadminDeployMapper xadminDeployMapper;

    @Override
    public Map<String,Object> queryAll(XadminDeployQueryCriteria criteria, Pageable pageable){
        Page<XadminDeploy> page = xadminDeployRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(xadminDeployMapper::toDto));
    }

    @Override
    public List<XadminDeployDto> queryAll(XadminDeployQueryCriteria criteria){
        return xadminDeployMapper.toDto(xadminDeployRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public XadminDeployDto findById(Long deployId) {
        XadminDeploy xadminDeploy = xadminDeployRepository.findById(deployId).orElseGet(XadminDeploy::new);
        ValidationUtil.isNull(xadminDeploy.getDeployId(),"XadminDeploy","deployId",deployId);
        return xadminDeployMapper.toDto(xadminDeploy);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public XadminDeployDto create(XadminDeploy resources) {
        return xadminDeployMapper.toDto(xadminDeployRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(XadminDeploy resources) {
        XadminDeploy xadminDeploy = xadminDeployRepository.findById(resources.getDeployId()).orElseGet(XadminDeploy::new);
        ValidationUtil.isNull( xadminDeploy.getDeployId(),"XadminDeploy","id",resources.getDeployId());
        xadminDeploy.copy(resources);
        xadminDeployRepository.save(xadminDeploy);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long deployId : ids) {
            xadminDeployRepository.deleteById(deployId);
        }
    }

    @Override
    public void download(List<XadminDeployDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (XadminDeployDto xadminDeploy : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("app id", xadminDeploy.getAppId());
            map.put("cfg id", xadminDeploy.getCfgId());
            map.put("env id", xadminDeploy.getEnvId());
            map.put("revision", xadminDeploy.getRevision());
            map.put("status", xadminDeploy.getStatus());
            map.put("创建者", xadminDeploy.getCreateBy());
            map.put("更新者", xadminDeploy.getUpdateBy());
            map.put("创建日期", xadminDeploy.getCreateTime());
            map.put("更新时间", xadminDeploy.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
