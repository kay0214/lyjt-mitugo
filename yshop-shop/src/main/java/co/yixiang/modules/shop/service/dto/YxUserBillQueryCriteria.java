package co.yixiang.modules.shop.service.dto;

import co.yixiang.annotation.Query;
import lombok.Data;

/**
* @author hupeng
* @date 2020-05-12
*/
@Data
public class YxUserBillQueryCriteria extends BaseCriteria{

    /** 用户名模糊查询 */
    @Query(type = Query.Type.EQUAL)
    private String username;

    /** 账单明细种类 */
    @Query(type = Query.Type.EQUAL)
    private Integer pm;

    /** 账单标题 */
    @Query(type = Query.Type.INNER_LIKE)
    private String title;


    private String addTimeStart;
    private String addTimeEnd;
}
