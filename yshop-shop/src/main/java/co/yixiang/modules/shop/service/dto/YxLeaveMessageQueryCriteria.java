/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.shop.service.dto;

import co.yixiang.annotation.Query;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author nxl
 * @date 2020-11-04
 */
@Data
public class YxLeaveMessageQueryCriteria extends BaseCriteria {
    /** 联系人 */
    @Query(type = Query.Type.EQUAL)
    private String userName;

    /** 电话 */
    @Query(type = Query.Type.EQUAL)
    private String userPhone;

    /** 状态：0 -> 待处理，1 -> 已处理，2 -> 不予处理 */
    @Query(type = Query.Type.EQUAL)
    private Integer status;
}
