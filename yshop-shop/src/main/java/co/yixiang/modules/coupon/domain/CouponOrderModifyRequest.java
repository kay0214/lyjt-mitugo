package co.yixiang.modules.coupon.domain;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Author : huanghui
 */
@Data
public class CouponOrderModifyRequest {

    @NotNull(message = "主键信息不可为空")
    private Integer id;

    @NotBlank(message = "备注信息不可为空")
    private String mark;
}
