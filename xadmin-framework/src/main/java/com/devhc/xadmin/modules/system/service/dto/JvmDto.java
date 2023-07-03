package com.devhc.xadmin.modules.system.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class JvmDto implements Serializable {
    private JvmClassLoaderDto classLoader;
    private JvmMemoryDto mem;
    private List<JvmGcDto> gc;
    private List<JvmMemPoolDto> pool;


}
