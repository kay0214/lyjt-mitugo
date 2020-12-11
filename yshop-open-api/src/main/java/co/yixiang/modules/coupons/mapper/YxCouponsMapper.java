package co.yixiang.modules.coupons.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import co.yixiang.modules.coupons.entity.YxCoupons;
import co.yixiang.modules.coupons.web.param.YxCouponsQueryParam;
import co.yixiang.modules.coupons.web.vo.YxCouponsQueryVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * <p>
 * 本地生活, 卡券表 Mapper 接口
 * </p>
 *
 * @author zqq
 * @since 2020-12-11
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
    IPage<YxCouponsQueryVo> getYxCouponsPageList(@Param("page") Page page, @Param("param") YxCouponsQueryParam yxCouponsQueryParam);

}
