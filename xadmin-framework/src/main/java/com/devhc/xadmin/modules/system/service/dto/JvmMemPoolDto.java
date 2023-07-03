package com.devhc.xadmin.modules.system.service.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class JvmMemPoolDto implements Serializable {

    /**
     * 内存区名称
     */
    private String name;

    /**
     * 所属内存管理者
     */
    private String manageNames;

    /**
     * 已申请内存
     */
    private Long committed;

    /**
     * 最大内存量
     */
    private Long max;

    /**
     * 已使用内存（字节）
     */
    private Long used;
}
