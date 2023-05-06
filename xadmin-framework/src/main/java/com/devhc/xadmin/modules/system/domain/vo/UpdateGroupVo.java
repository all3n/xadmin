package com.devhc.xadmin.modules.system.domain.vo;

import java.util.List;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UpdateGroupVo {
  private List<Long> ids;
  private Long groupId;
  private String hosts;

}
