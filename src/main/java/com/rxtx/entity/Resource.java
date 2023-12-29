package com.rxtx.entity;

import lombok.Data;


@Data
public class Resource {
    /**
     * 匹配访问路径
     * 多个用','隔离
     */
    private String pathPatterns;

    /**
     * 资源路径
     * 多个用','隔离
     */
    private String resourceLocations;
}

