/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 */
package co.yixiang.modules.activity.service.dto;

import co.yixiang.annotation.Query;
import lombok.Data;

import java.util.List;

/**
 * @author hupeng
 * @date 2020-05-13
 */
@Data
public class YxUserExtractQueryCriteria {

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String realName;
    @Query(type = Query.Type.EQUAL)
    private Integer status;
    @Query(type = Query.Type.EQUAL)
    private Integer userType;
    private String seqNo;
    private String userTrueName;
    private String bankCode;
    private String phone;
    private List<String> addTime;
}
