package co.yixiang.modules.shop.service.dto;

import co.yixiang.annotation.Query;
import lombok.Data;

/**
 * 查询资金表中指定关联ID的特定场景数据使用
 * @Author : huanghui
 */
@Data
public class WithdrawReviewQueryCriteria {

    /** 查询单条提现记录的审核记录使用 */
    @Query(type = Query.Type.EQUAL)
    private Integer linkId;
}
