package com.devhc.xadmin.modules.system.service.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class JvmGcDto implements Serializable {
    private String name;
    private String objName;
    private String poolName;
    private Long count;
    private Long time;
    private Boolean isValid;
}
