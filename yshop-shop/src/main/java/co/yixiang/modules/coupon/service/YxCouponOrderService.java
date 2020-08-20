package co.yixiang.modules.coupon.service;

import co.yixiang.common.service.BaseService;
import co.yixiang.modules.coupon.domain.YxCouponOrder;
import co.yixiang.modules.coupon.service.dto.YxCouponOrderDto;
import co.yixiang.modules.coupon.service.dto.YxCouponOrderQueryCriteria;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
* @author huiy
* @date 2020-08-14
*/
public interface YxCouponOrderService  extends BaseService<YxCouponOrder>{

/**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(YxCouponOrderQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<YxCouponOrderDto>
    */
    List<YxCouponOrder> queryAll(YxCouponOrderQueryCriteria criteria);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<YxCouponOrderDto> all, HttpServletResponse response) throws IOException;
}
