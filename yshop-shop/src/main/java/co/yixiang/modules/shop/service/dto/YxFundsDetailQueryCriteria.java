package co.yixiang.modules.shop.service.dto;

import lombok.Data;

import java.sql.Timestamp;
import java.util.List;
import co.yixiang.annotation.Query;

/**
* @author huiy
* @date 2020-08-19
*/
@Data
public class YxFundsDetailQueryCriteria {

    /** 用户名模糊查询 */
    @Query(type = Query.Type.EQUAL)
    private String username;

    /** 账单明细种类 */
    @Query(type = Query.Type.EQUAL)
    private Integer pm;

    @Query(type = Query.Type.BETWEEN)
    private List<Timestamp> addTime;
//
//    private String startTime;
//    private String endTime;
}
