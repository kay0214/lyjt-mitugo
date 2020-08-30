package co.yixiang.modules.coupons.web.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName OrderCountDTO
 * @Author hupeng <610796224@qq.com>
 * @Date 2019/10/30
 **/
@Data
public class OrderCountVO implements Serializable {
    private Integer waitPayCount; //待付款数量
    private Integer waitUseCount;  //待使用数量
    private Integer usedCount;  //已使用数量
    private Integer refundCount;  //退款数量
}
