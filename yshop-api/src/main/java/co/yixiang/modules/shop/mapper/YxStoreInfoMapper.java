package co.yixiang.modules.shop.mapper;

import co.yixiang.modules.coupons.web.param.LocalLiveQueryParam;
import co.yixiang.modules.coupons.web.vo.LocalLiveListVo;
import co.yixiang.modules.shop.entity.YxStoreInfo;
import co.yixiang.modules.shop.web.param.YxStoreInfoQueryParam;
import co.yixiang.modules.shop.web.vo.YxStoreInfoQueryVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 店铺表 Mapper 接口
 * </p>
 *
 * @author nxl
 * @since 2020-08-20
 */
@Repository
public interface YxStoreInfoMapper extends BaseMapper<YxStoreInfo> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxStoreInfoQueryVo getYxStoreInfoById(Serializable id);

    /**
     * 获取分页对象
     * @param page
     * @param yxStoreInfoQueryParam
     * @return
     */
    IPage<YxStoreInfoQueryVo> getYxStoreInfoPageList(@Param("page") Page page, @Param("param") YxStoreInfoQueryParam yxStoreInfoQueryParam);


    /**
     * 根据经纬度查询
     * @param lnt
     * @param lat
     * @return
     */
    YxStoreInfoQueryVo getYxStoreInfoByPoint(BigDecimal lnt,BigDecimal lat);

    /**
     * 本地生活
     * @param page
     * @param localLiveQueryParam
     * @return
     */
    IPage<LocalLiveListVo> getLocalLiveList(@Param("page") Page page, @Param("param") LocalLiveQueryParam localLiveQueryParam);
}
