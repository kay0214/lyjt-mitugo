/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co

 */
package co.yixiang.gen.service;

import co.yixiang.gen.domain.GenConfig;

/**
 * @author Zheng Jie
 * @date 2019-01-14
 */
public interface GenConfigService {

    /**
     * 查询表配置
     * @param tableName 表名
     * @return 表配置
     */
    GenConfig find(String tableName);

    /**
     * 更新表配置
     * @param tableName 表名
     * @param genConfig 表配置
     * @return 表配置
     */
    GenConfig update(String tableName, GenConfig genConfig);
}
