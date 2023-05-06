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
package com.devhc.xadmin.modules.system.service;


import com.devhc.xadmin.modules.system.domain.ServerGroup;
import com.devhc.xadmin.modules.system.domain.vo.UpdateGroupVo;
import com.devhc.xadmin.modules.system.service.dto.ServerGroupDto;
import com.devhc.xadmin.modules.system.service.dto.ServerGroupQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
 * @description 服务接口
 * @date 2022-12-22
 **/
public interface ServerGroupService {

  /**
   * 查询数据分页
   *
   * @param criteria 条件
   * @param pageable 分页参数
   * @return Map<String, Object>
   */
  Map<String, Object> queryAll(ServerGroupQueryCriteria criteria, Pageable pageable);

  /**
   * 查询所有数据不分页
   *
   * @param criteria 条件参数
   * @return List<ServerGroupDto>
   */
  List<ServerGroupDto> queryAll(ServerGroupQueryCriteria criteria);

  /**
   * 根据ID查询
   *
   * @param groupId ID
   * @return ServerGroupDto
   */
  ServerGroupDto findById(Long groupId);

  /**
   * 创建
   *
   * @param resources /
   * @return ServerGroupDto
   */
  ServerGroupDto create(ServerGroup resources);

  /**
   * 编辑
   *
   * @param resources /
   */
  void update(ServerGroup resources);

  /**
   * 多选删除
   *
   * @param ids /
   */
  void deleteAll(Long[] ids);

  /**
   * 导出数据
   *
   * @param all      待导出的数据
   * @param response /
   * @throws IOException /
   */
  void download(List<ServerGroupDto> all, HttpServletResponse response) throws IOException;

  void updateGroup(UpdateGroupVo ug);
}
