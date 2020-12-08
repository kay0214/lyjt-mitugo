package co.yixiang.modules.shop.service.dto;

import co.yixiang.annotation.Query;
import lombok.Data;

/**
 * @author hupeng
 * @date 2020-05-12
 */
@Data
public class YxUserBillQueryCriteria extends BaseCriteria {

    /**
     * 用户名模糊查询
     */
    @Query(type = Query.Type.EQUAL)
    private String username;

    /**
     * 账单明细种类
     */
    @Query(type = Query.Type.EQUAL)
    private Integer pm;

    /**
     * 账单标题
     */
    @Query(type = Query.Type.INNER_LIKE)
    private String title;

    @Query(type = Query.Type.EQUAL)
    private String linkId;

    //开始时间
    private String addTimeStart;
    //结束时间
    private String addTimeEnd;

    //明细种类
    private String category;
    // 明细类型
    private String type;
    // 用户类型
    private Integer userType;
    // 手机号
    private String phone;

    private Integer brokerageType;

}
