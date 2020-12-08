package co.yixiang.modules.system.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import co.yixiang.modules.system.entity.YxRefundReason;
import co.yixiang.modules.system.web.param.YxRefundReasonQueryParam;
import co.yixiang.modules.system.web.vo.YxRefundReasonQueryVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * <p>
 * 退款理由配置表 Mapper 接口
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
@Repository
public interface YxRefundReasonMapper extends BaseMapper<YxRefundReason> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxRefundReasonQueryVo getYxRefundReasonById(Serializable id);

    /**
     * 获取分页对象
     * @param page
     * @param yxRefundReasonQueryParam
     * @return
     */
    IPage<YxRefundReasonQueryVo> getYxRefundReasonPageList(@Param("page") Page page, @Param("param") YxRefundReasonQueryParam yxRefundReasonQueryParam);

}
