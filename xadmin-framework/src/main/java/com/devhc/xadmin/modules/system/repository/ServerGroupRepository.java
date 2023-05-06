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
package com.devhc.xadmin.modules.system.repository;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.devhc.xadmin.modules.system.domain.ServerGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @date 2022-12-22
 **/
public interface ServerGroupRepository extends JpaRepository<ServerGroup, Long>, JpaSpecificationExecutor<ServerGroup> {
    List<ServerGroup> findTop10ByNameContainingAllIgnoreCase(String name);

    @Query(value = "SELECT g.`group_id`, COUNT(g.`server_id`) FROM `server_group_config` g WHERE g.`group_id` IN (:ids) GROUP BY g.`group_id`", nativeQuery = true)
    List<Object[]> countByGroupIdIn(@Param("ids") List<Long> ids);

    default Map<Long, Long> countByIdsAsMap(List<Long> ids) {
        List<Object[]> counts = countByGroupIdIn(ids);
        Map<Long, Long> result = new HashMap<>();
        for (Object[] row : counts) {
            BigInteger id = (BigInteger) row[0];
            BigInteger count = (BigInteger) row[1];
            result.put(id.longValue(), count.longValue());
        }
        return result;
    }
}
