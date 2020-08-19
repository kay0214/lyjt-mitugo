package co.yixiang.modules.shop.service.dto;

import co.yixiang.annotation.Query;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

/**
* @author huiy
* @date 2020-08-19
*/
@Data
public class YxPointDetailQueryCriteria{

    /** 用户名模糊查询 */
    @Query(type = Query.Type.EQUAL)
    private String username;

    /** 账单明细种类 */
    @Query(type = Query.Type.EQUAL)
    private Integer type;

    @Query(type = Query.Type.BETWEEN)
    private List<Timestamp> createTime;
}
