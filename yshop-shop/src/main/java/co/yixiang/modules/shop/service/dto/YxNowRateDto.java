/**
* Copyright (C) 2018-2020
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.shop.service.dto;

import lombok.Data;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.io.Serializable;

/**
* @author nxl
* @date 2020-11-04
*/
@Data
public class YxNowRateDto implements Serializable {

    /** 主键 */

    private Integer id;


    /** 类型：0:商品购买 1:本地生活 */

    private Integer rateType;


    /** 关联订单id */

    private String orderId;


    /** 购物车id */

    private Integer cartId;


    /** 商品id */

    private Integer productId;


    /** 平台抽成 */

    private BigDecimal fundsRate;


    /** 分享人 */

    private BigDecimal shareRate;


    /** 分享人上级 */

    private BigDecimal shareParentRate;


    /** 推荐人 */

    private BigDecimal parentRate;


    /** 商户 */

    private BigDecimal merRate;


    /** 合伙人 */

    private BigDecimal partnerRate;


    /** 拉新池 */

    private BigDecimal referenceRate;


    /** 是否删除（0：未删除，1：已删除） */

    private Integer delFlag;


    /** 创建人 */

    private Integer createUserId;


    /** 修改人 */

    private Integer updateUserId;


    /** 创建时间 */

    private Timestamp createTime;


    /** 更新时间 */

    private Timestamp updateTime;

}
