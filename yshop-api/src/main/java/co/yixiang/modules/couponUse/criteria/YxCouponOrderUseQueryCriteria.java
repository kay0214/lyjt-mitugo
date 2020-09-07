/**
* Copyright (C) 2018-2020
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.couponUse.criteria;

import co.yixiang.common.web.param.QueryParam;
import lombok.Data;

/**
* @author huiy
* @date 2020-08-14
*/
@Data
public class YxCouponOrderUseQueryCriteria extends QueryParam {

    /** 创建人 根据创建人关联店铺 */
    private Integer createUserId;
}
