/**
* Copyright (C) 2018-2020
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.coupon.service.dto;

import lombok.Data;
import java.util.List;
import co.yixiang.annotation.Query;

/**
* @author huiy
* @date 2020-08-14
*/
@Data
public class YxCouponsQueryCriteria{

    /** 卡券名模糊查询 */
    @Query(type = Query.Type.INNER_LIKE)
    private String couponName;

    /** 查找指定卡券类型 */
    @Query(type = Query.Type.EQUAL)
    private Integer couponType;

    /** 查找是否上架 */
    @Query(type = Query.Type.EQUAL)
    private Integer isShow;

    /** 查找是否热销 */
    @Query(type = Query.Type.EQUAL)
    private Integer isHot;
}
