package co.yixiang.modules.manage.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import co.yixiang.modules.manage.entity.YxMerchantsDetail;
import co.yixiang.modules.manage.web.param.YxMerchantsDetailQueryParam;
import co.yixiang.modules.manage.web.vo.YxMerchantsDetailQueryVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * <p>
 * 商户详情 Mapper 接口
 * </p>
 *
 * @author hupeng
 * @since 2020-08-13
 */
@Repository
public interface YxMerchantsDetailMapper extends BaseMapper<YxMerchantsDetail> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxMerchantsDetailQueryVo getYxMerchantsDetailById(Serializable id);

    /**
     * 获取分页对象
     * @param page
     * @param yxMerchantsDetailQueryParam
     * @return
     */
    IPage<YxMerchantsDetailQueryVo> getYxMerchantsDetailPageList(@Param("page") Page page, @Param("param") YxMerchantsDetailQueryParam yxMerchantsDetailQueryParam);

}
