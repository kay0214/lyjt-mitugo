package co.yixiang.modules.coupon.domain;

import co.yixiang.modules.shop.domain.YxCustomizeRate;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 卡券修改
 * @Author : liushouyi
 */
@Data
public class CouponModifyRateRequest {

    @NotNull(message = "请传入修改数据的ID")
    private Integer id;

    /** 分佣模式（0：按平台，1：不分佣，2：自定义分佣） */
    private Integer customizeType;

    // 自定义分佣比例
    private YxCustomizeRate yxCustomizeRate;

    private Integer createUser;
}
