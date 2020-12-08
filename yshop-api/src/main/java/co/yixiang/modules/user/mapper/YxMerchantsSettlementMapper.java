package co.yixiang.modules.user.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import co.yixiang.modules.user.entity.YxMerchantsSettlement;
import co.yixiang.modules.user.web.param.YxMerchantsSettlementQueryParam;
import co.yixiang.modules.user.web.vo.YxMerchantsSettlementQueryVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * <p>
 * 商家入驻表 Mapper 接口
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
@Repository
public interface YxMerchantsSettlementMapper extends BaseMapper<YxMerchantsSettlement> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxMerchantsSettlementQueryVo getYxMerchantsSettlementById(Serializable id);

    /**
     * 获取分页对象
     * @param page
     * @param yxMerchantsSettlementQueryParam
     * @return
     */
    IPage<YxMerchantsSettlementQueryVo> getYxMerchantsSettlementPageList(@Param("page") Page page, @Param("param") YxMerchantsSettlementQueryParam yxMerchantsSettlementQueryParam);

}
