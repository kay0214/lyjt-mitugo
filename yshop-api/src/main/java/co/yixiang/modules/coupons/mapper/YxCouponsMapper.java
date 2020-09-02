package co.yixiang.modules.coupons.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import co.yixiang.modules.coupons.entity.YxCoupons;
import co.yixiang.modules.coupons.web.param.YxCouponsQueryParam;
import co.yixiang.modules.coupons.web.vo.YxCouponsQueryVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 本地生活, 卡券表 Mapper 接口
 * </p>
 *
 * @author hupeng
 * @since 2020-08-13
 */
@Repository
public interface YxCouponsMapper extends BaseMapper<YxCoupons> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxCouponsQueryVo getYxCouponsById(Serializable id);

    /**
     * 获取分页对象
     * @param page
     * @param yxCouponsQueryParam
     * @return
     */
    @Select("select * from yx_coupons where is_show = 1")
    IPage<YxCouponsQueryVo> getYxCouponsPageList(@Param("page") Page page, @Param("param") YxCouponsQueryParam yxCouponsQueryParam);

    @Select("select * from (select *, (sales+ficti) as totalSales from yx_coupons) tmp where is_hot = 1 order by totalSales desc limit 20")
    List<YxCouponsQueryVo> getCouponsHotList(@Param("param") YxCouponsQueryParam yxCouponsQueryParam);

    @Select("select count(1) from yx_coupons")
    int getCount(@Param("param") YxCouponsQueryParam yxCouponsQueryParam);

}
