package com.devhc.xadmin.modules.system.service.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class JvmClassLoaderDto implements Serializable {

    /**
     * JVM加载类的数量
     */
    private Integer count;

    /**
     * JVM已加载类数量
     */
    private Long loaded;

    /**
     * JVM未加载类数量
     */
    private Long unLoaded;

    /**
     * 是否启用类加载详细信息的输出
     */
    private Boolean isVerbose;
}
