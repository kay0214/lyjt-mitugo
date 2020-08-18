package co.yixiang.modules.coupon.service;
import co.yixiang.common.service.BaseService;
import co.yixiang.modules.coupon.domain.CouponsCategoryRequest;
import co.yixiang.modules.coupon.domain.YxCouponsCategory;
import co.yixiang.modules.coupon.service.dto.YxCouponsCategoryDto;
import co.yixiang.modules.coupon.service.dto.YxCouponsCategoryQueryCriteria;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author huiy
* @date 2020-08-14
*/
public interface YxCouponsCategoryService  extends BaseService<YxCouponsCategory>{

    /**
     * 写入
     * @param yxCouponsCategory
     * @return
     */
    int insCouponCate(YxCouponsCategory yxCouponsCategory);

    IPage<YxCouponsCategory> getAllList(CouponsCategoryRequest request);

    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(YxCouponsCategoryQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<YxCouponsCategoryDto>
    */
    List<YxCouponsCategory> queryAll(YxCouponsCategoryQueryCriteria criteria);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<YxCouponsCategoryDto> all, HttpServletResponse response) throws IOException;
}
