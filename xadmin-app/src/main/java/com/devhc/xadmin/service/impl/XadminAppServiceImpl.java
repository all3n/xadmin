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

import com.devhc.xadmin.domain.XadminApp;
import com.devhc.xadmin.utils.ValidationUtil;
import com.devhc.xadmin.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import com.devhc.xadmin.repository.XadminAppRepository;
import com.devhc.xadmin.service.XadminAppService;
import com.devhc.xadmin.service.dto.XadminAppDto;
import com.devhc.xadmin.service.dto.XadminAppQueryCriteria;
import com.devhc.xadmin.service.mapstruct.XadminAppMapper;
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
public class XadminAppServiceImpl implements XadminAppService {

    private final XadminAppRepository xadminAppRepository;
    private final XadminAppMapper xadminAppMapper;

    @Override
    public Map<String,Object> queryAll(XadminAppQueryCriteria criteria, Pageable pageable){
        Page<XadminApp> page = xadminAppRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(xadminAppMapper::toDto));
    }

    @Override
    public List<XadminAppDto> queryAll(XadminAppQueryCriteria criteria){
        return xadminAppMapper.toDto(xadminAppRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public XadminAppDto findById(Long appId) {
        XadminApp xadminApp = xadminAppRepository.findById(appId).orElseGet(XadminApp::new);
        ValidationUtil.isNull(xadminApp.getAppId(),"XadminApp","appId",appId);
        return xadminAppMapper.toDto(xadminApp);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public XadminAppDto create(XadminApp resources) {
        return xadminAppMapper.toDto(xadminAppRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(XadminApp resources) {
        XadminApp xadminApp = xadminAppRepository.findById(resources.getAppId()).orElseGet(XadminApp::new);
        ValidationUtil.isNull( xadminApp.getAppId(),"XadminApp","id",resources.getAppId());
        xadminApp.copy(resources);
        xadminAppRepository.save(xadminApp);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long appId : ids) {
            xadminAppRepository.deleteById(appId);
        }
    }

    @Override
    public void download(List<XadminAppDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (XadminAppDto xadminApp : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("name", xadminApp.getName());
            map.put("创建者", xadminApp.getCreateBy());
            map.put("更新者", xadminApp.getUpdateBy());
            map.put("创建日期", xadminApp.getCreateTime());
            map.put("更新时间", xadminApp.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
