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

    private  Integer userRole;

    private List<Long> childUser;
}
