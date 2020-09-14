/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package co.yixiang.modules.shop.service.dto;

import lombok.Data;

import java.util.List;

/**
 * @author PC-LIUSHOUYI
 * @version BaseCriteria, v0.1 2020/8/30 10:16
 */
@Data
public class BaseCriteria {

    // 用户角色：0->平台运营,1->合伙人,2->商户
    private Integer userRole;

    private Integer uid;

    private List<Long> childUser;

    private List<Long> childStoreId;
}
