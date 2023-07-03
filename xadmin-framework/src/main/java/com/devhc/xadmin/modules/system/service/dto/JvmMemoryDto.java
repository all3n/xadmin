package com.devhc.xadmin.modules.system.service.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class JvmMemoryDto implements Serializable {
    // heap size
    private Long committed;
    // init heap size
    private Long init;
    // max heap size
    private Long max;
    // used
    private Long used;

    // non heap committed
    private Long nonCommitted;
    // non heap init
    private Long nonInit;
    // non heap max
    private Long nonMax;
    // non heap used
    private Long nonUsed;

}
