package co.yixiang.modules.activity.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 优惠券添加实体
 * @Author : huanghui
 */
@Data
public class SroreCouponAddRequest {

    /** 所属商户 */
    private Integer belong;

    /** 优惠券名称 */
    @NotBlank(message = "请填写优惠券名称")
    private String title;

    /** 兑换的优惠券面值 */
    @DecimalMin(value="0.00", message = "优惠券面值不在合法范围内" )
    @DecimalMax(value="99999999.99", message = "优惠券面值不在合法范围内")
    private BigDecimal couponPrice;


    /** 最低消费多少金额可用优惠券 */
    @DecimalMin(value="0.00", message = "最低消费不在合法范围内" )
    @DecimalMax(value="99999999.99", message = "最低消费不在合法范围内")
    private BigDecimal useMinPrice;


    /** 优惠券有效期限（单位：天） */
    @NotNull(message = "请输入有效期限")
    private Integer couponTime;

    /** 排序 */
    private Integer sort;


    /** 状态（0：关闭，1：开启） */
    @TableField(value = "`status`")
    private Integer status;
}
