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

import com.devhc.xadmin.domain.XadminLog;
import com.devhc.xadmin.utils.ValidationUtil;
import com.devhc.xadmin.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import com.devhc.xadmin.repository.XadminLogRepository;
import com.devhc.xadmin.service.XadminLogService;
import com.devhc.xadmin.service.dto.XadminLogDto;
import com.devhc.xadmin.service.dto.XadminLogQueryCriteria;
import com.devhc.xadmin.service.mapstruct.XadminLogMapper;
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
public class XadminLogServiceImpl implements XadminLogService {

    private final XadminLogRepository xadminLogRepository;
    private final XadminLogMapper xadminLogMapper;

    @Override
    public Map<String,Object> queryAll(XadminLogQueryCriteria criteria, Pageable pageable){
        Page<XadminLog> page = xadminLogRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(xadminLogMapper::toDto));
    }

    @Override
    public List<XadminLogDto> queryAll(XadminLogQueryCriteria criteria){
        return xadminLogMapper.toDto(xadminLogRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public XadminLogDto findById(Long logId) {
        XadminLog xadminLog = xadminLogRepository.findById(logId).orElseGet(XadminLog::new);
        ValidationUtil.isNull(xadminLog.getLogId(),"XadminLog","logId",logId);
        return xadminLogMapper.toDto(xadminLog);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public XadminLogDto create(XadminLog resources) {
        return xadminLogMapper.toDto(xadminLogRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(XadminLog resources) {
        XadminLog xadminLog = xadminLogRepository.findById(resources.getLogId()).orElseGet(XadminLog::new);
        ValidationUtil.isNull( xadminLog.getLogId(),"XadminLog","id",resources.getLogId());
        xadminLog.copy(resources);
        xadminLogRepository.save(xadminLog);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long logId : ids) {
            xadminLogRepository.deleteById(logId);
        }
    }

    @Override
    public void download(List<XadminLogDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (XadminLogDto xadminLog : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("deploy id", xadminLog.getDeployId());
            map.put("deploy logs", xadminLog.getContent());
            map.put("创建者", xadminLog.getCreateBy());
            map.put("更新者", xadminLog.getUpdateBy());
            map.put("创建日期", xadminLog.getCreateTime());
            map.put("更新时间", xadminLog.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
