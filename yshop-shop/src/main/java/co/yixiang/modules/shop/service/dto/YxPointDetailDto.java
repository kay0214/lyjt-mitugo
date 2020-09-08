package co.yixiang.modules.shop.service.dto;

import lombok.Data;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.io.Serializable;

/**
* @author huiy
* @date 2020-08-19
*/
@Data
public class YxPointDetailDto implements Serializable {

    /** 主键id */

    private Integer id;


    /** 用户ID */

    private Integer uid;


    /** 用户名 */

    private String username;


    /** 积分类别 0:拉新 1:分红 */

    private Integer type;


    /** 订单编号 */

    private String orderId;


    /** 订单类型 0:商品购买 1:本地生活 */

    private Integer orderType;


    /** 订单金额 */

    private BigDecimal orderPrice;


    /** 订单佣金 */

    private BigDecimal commission;


    /** 商户id */

    private Integer merchantsId;

    /** 商户username */
    private String merUsername;

    /** 商户获取积分数 */

    private BigDecimal merchantsPoint;


    /** 合伙人id */

    private Integer partnerId;

    /** 合伙人username */
    private String parUsername;

    /** 合伙人获取积分数 */

    private BigDecimal partnerPoint;


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
