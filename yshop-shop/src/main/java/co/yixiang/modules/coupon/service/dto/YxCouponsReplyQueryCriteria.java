/**
* Copyright (C) 2018-2020
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.coupon.service.dto;

import co.yixiang.modules.shop.service.dto.BaseCriteria;
import lombok.Data;
import java.util.List;
import co.yixiang.annotation.Query;

import javax.validation.constraints.NotNull;

/**
* @author nxl
* @date 2020-11-04
*/
@Data
public class YxCouponsReplyQueryCriteria extends BaseCriteria {

    /** 商户id */
    @Query(type = Query.Type.EQUAL)
    private Integer merId;

    /** 0：未回复，1：已回复 */
    @Query(type = Query.Type.EQUAL)
    private Integer isReply;

    /** 卡券id */
    @Query(type = Query.Type.EQUAL)
    private Integer couponId;

    /** 商户昵称 */
    private String nickName;
    /** 商户用户名 */
    private String username;
    /** 商品名称 */
    private String couponName;

}
