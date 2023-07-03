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

import com.devhc.xadmin.domain.XadminDeployConfig;
import com.devhc.xadmin.utils.ValidationUtil;
import com.devhc.xadmin.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import com.devhc.xadmin.repository.XadminDeployConfigRepository;
import com.devhc.xadmin.service.XadminDeployConfigService;
import com.devhc.xadmin.service.dto.XadminDeployConfigDto;
import com.devhc.xadmin.service.dto.XadminDeployConfigQueryCriteria;
import com.devhc.xadmin.service.mapstruct.XadminDeployConfigMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.devhc.xadmin.utils.PageUtil;
import com.devhc.xadmin.utils.QueryHelp;

import java.util.List;
import java.util.Map;
import java.io.IOException;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * @description 服务实现
 * @date 2023-05-10
 **/
@Service
@RequiredArgsConstructor
public class XadminDeployConfigServiceImpl implements XadminDeployConfigService {

    private final XadminDeployConfigRepository xadminDeployConfigRepository;
    private final XadminDeployConfigMapper xadminDeployConfigMapper;

    @Override
    public Map<String, Object> queryAll(XadminDeployConfigQueryCriteria criteria, Pageable pageable) {
        Page<XadminDeployConfig> page = xadminDeployConfigRepository.findAll((root, criteriaQuery, criteriaBuilder) ->
                {
//                    List<Predicate> preds = new ArrayList<>();
//                    Join<XadminDeployConfig, XadminDeployConfig> j = root.join("xadmin_deploy_config", JoinType.LEFT);
//                    preds.add(criteriaBuilder.equal(j.get("parent_id"), root.get("cfg_id")));
//                    preds.add(criteriaQuery.multiselect(j.get("parent_id")));
                    //Predicate x = QueryHelp.getPredicate(root, criteria, criteriaBuilder, preds);
                    Predicate x = QueryHelp.getPredicate(root, criteria, criteriaBuilder);
                    return x;
                }
                , pageable);
        return PageUtil.toPage(page.map(xadminDeployConfigMapper::toDto));
    }

    @Override
    public List<XadminDeployConfigDto> queryAll(XadminDeployConfigQueryCriteria criteria) {
        return xadminDeployConfigMapper.toDto(xadminDeployConfigRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Override
    @Transactional
    public XadminDeployConfigDto findById(Long cfgId) {
        XadminDeployConfig xadminDeployConfig = xadminDeployConfigRepository.findById(cfgId).orElseGet(XadminDeployConfig::new);
        ValidationUtil.isNull(xadminDeployConfig.getCfgId(), "XadminDeployConfig", "cfgId", cfgId);
        return xadminDeployConfigMapper.toDto(xadminDeployConfig);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public XadminDeployConfigDto create(XadminDeployConfig resources) {
        return xadminDeployConfigMapper.toDto(xadminDeployConfigRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(XadminDeployConfig resources) {
        XadminDeployConfig xadminDeployConfig = xadminDeployConfigRepository.findById(resources.getCfgId()).orElseGet(XadminDeployConfig::new);
        ValidationUtil.isNull(xadminDeployConfig.getCfgId(), "XadminDeployConfig", "id", resources.getCfgId());
        xadminDeployConfig.copy(resources);
        xadminDeployConfigRepository.save(xadminDeployConfig);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long cfgId : ids) {
            xadminDeployConfigRepository.deleteById(cfgId);
        }
    }

    @Override
    public void download(List<XadminDeployConfigDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (XadminDeployConfigDto xadminDeployConfig : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("ID", xadminDeployConfig.getCfgId());
            map.put("name", xadminDeployConfig.getName());
            map.put("app id", xadminDeployConfig.getAppId());
            map.put("parent id", xadminDeployConfig.getParentId());
            map.put("env id", xadminDeployConfig.getEnvId());
            map.put("config", xadminDeployConfig.getContent());
            map.put("创建者", xadminDeployConfig.getCreateBy());
            map.put("更新者", xadminDeployConfig.getUpdateBy());
            map.put("创建日期", xadminDeployConfig.getCreateTime());
            map.put("更新时间", xadminDeployConfig.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
