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
package com.devhc.xadmin.modules.system.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.devhc.xadmin.modules.system.service.mapstruct.DictMapper;
import com.devhc.xadmin.config.CacheKey;
import com.devhc.xadmin.utils.FileUtil;
import com.devhc.xadmin.utils.PageUtil;
import com.devhc.xadmin.utils.QueryHelp;
import com.devhc.xadmin.redis.RedisUtils;
import com.devhc.xadmin.utils.ValidationUtil;
import com.devhc.xadmin.modules.system.service.dto.DictDetailDto;
import lombok.RequiredArgsConstructor;
import com.devhc.xadmin.modules.system.domain.Dict;
import com.devhc.xadmin.modules.system.service.dto.DictQueryCriteria;
import com.devhc.xadmin.modules.system.repository.DictRepository;
import com.devhc.xadmin.modules.system.service.DictService;
import com.devhc.xadmin.modules.system.service.dto.DictDto;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
* 
*
*/
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "dict")
public class DictServiceImpl implements DictService {

    private final DictRepository dictRepository;
    private final DictMapper dictMapper;
    private final RedisUtils redisUtils;

    @Override
    public Map<String, Object> queryAll(DictQueryCriteria dict, Pageable pageable){
        Page<Dict> page = dictRepository.findAll((root, query, cb) -> QueryHelp.getPredicate(root, dict, cb), pageable);
        return PageUtil.toPage(page.map(dictMapper::toDto));
    }

    @Override
    public List<DictDto> queryAll(DictQueryCriteria dict) {
        List<Dict> list = dictRepository.findAll((root, query, cb) -> QueryHelp.getPredicate(root, dict, cb));
        return dictMapper.toDto(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(Dict resources) {
        dictRepository.save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Dict resources) {
        // 清理缓存
        delCaches(resources);
        Dict dict = dictRepository.findById(resources.getId()).orElseGet(Dict::new);
        ValidationUtil.isNull( dict.getId(),"Dict","id",resources.getId());
        dict.setName(resources.getName());
        dict.setDescription(resources.getDescription());
        dictRepository.save(dict);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        // 清理缓存
        List<Dict> dicts = dictRepository.findByIdIn(ids);
        for (Dict dict : dicts) {
            delCaches(dict);
        }
        dictRepository.deleteByIdIn(ids);
    }

    @Override
    public void download(List<DictDto> dictDtos, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (DictDto dictDTO : dictDtos) {
            if(CollectionUtil.isNotEmpty(dictDTO.getDictDetails())){
                for (DictDetailDto dictDetail : dictDTO.getDictDetails()) {
                    Map<String,Object> map = new LinkedHashMap<>();
                    map.put("字典名称", dictDTO.getName());
                    map.put("字典描述", dictDTO.getDescription());
                    map.put("字典标签", dictDetail.getLabel());
                    map.put("字典值", dictDetail.getValue());
                    map.put("创建日期", dictDetail.getCreateTime());
                    list.add(map);
                }
            } else {
                Map<String,Object> map = new LinkedHashMap<>();
                map.put("字典名称", dictDTO.getName());
                map.put("字典描述", dictDTO.getDescription());
                map.put("字典标签", null);
                map.put("字典值", null);
                map.put("创建日期", dictDTO.getCreateTime());
                list.add(map);
            }
        }
        FileUtil.downloadExcel(list, response);
    }

    public void delCaches(Dict dict){
        redisUtils.del(CacheKey.DICT_NAME + dict.getName());
    }
}
