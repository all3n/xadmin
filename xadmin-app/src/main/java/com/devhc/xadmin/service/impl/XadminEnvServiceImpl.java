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

import com.devhc.xadmin.domain.XadminEnv;
import com.devhc.xadmin.utils.ValidationUtil;
import com.devhc.xadmin.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import com.devhc.xadmin.repository.XadminEnvRepository;
import com.devhc.xadmin.service.XadminEnvService;
import com.devhc.xadmin.service.dto.XadminEnvDto;
import com.devhc.xadmin.service.dto.XadminEnvQueryCriteria;
import com.devhc.xadmin.service.mapstruct.XadminEnvMapper;
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
public class XadminEnvServiceImpl implements XadminEnvService {

    private final XadminEnvRepository xadminEnvRepository;
    private final XadminEnvMapper xadminEnvMapper;

    @Override
    public Map<String,Object> queryAll(XadminEnvQueryCriteria criteria, Pageable pageable){
        Page<XadminEnv> page = xadminEnvRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(xadminEnvMapper::toDto));
    }

    @Override
    public List<XadminEnvDto> queryAll(XadminEnvQueryCriteria criteria){
        return xadminEnvMapper.toDto(xadminEnvRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public XadminEnvDto findById(Integer envId) {
        XadminEnv xadminEnv = xadminEnvRepository.findById(envId).orElseGet(XadminEnv::new);
        ValidationUtil.isNull(xadminEnv.getEnvId(),"XadminEnv","envId",envId);
        return xadminEnvMapper.toDto(xadminEnv);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public XadminEnvDto create(XadminEnv resources) {
        return xadminEnvMapper.toDto(xadminEnvRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(XadminEnv resources) {
        XadminEnv xadminEnv = xadminEnvRepository.findById(resources.getEnvId()).orElseGet(XadminEnv::new);
        ValidationUtil.isNull( xadminEnv.getEnvId(),"XadminEnv","id",resources.getEnvId());
        xadminEnv.copy(resources);
        xadminEnvRepository.save(xadminEnv);
    }

    @Override
    public void deleteAll(Integer[] ids) {
        for (Integer envId : ids) {
            xadminEnvRepository.deleteById(envId);
        }
    }

    @Override
    public void download(List<XadminEnvDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (XadminEnvDto xadminEnv : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("name", xadminEnv.getName());
            map.put("创建者", xadminEnv.getCreateBy());
            map.put("更新者", xadminEnv.getUpdateBy());
            map.put("创建日期", xadminEnv.getCreateTime());
            map.put("更新时间", xadminEnv.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
