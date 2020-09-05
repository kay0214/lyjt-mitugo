package co.yixiang.modules.offpay.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import co.yixiang.modules.offpay.entity.YxOffPayOrder;
import co.yixiang.modules.offpay.web.param.YxOffPayOrderQueryParam;
import co.yixiang.modules.offpay.web.vo.YxOffPayOrderQueryVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * <p>
 * 线下支付订单表 Mapper 接口
 * </p>
 *
 * @author sss
 * @since 2020-09-05
 */
@Repository
public interface YxOffPayOrderMapper extends BaseMapper<YxOffPayOrder> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxOffPayOrderQueryVo getYxOffPayOrderById(Serializable id);

    /**
     * 获取分页对象
     * @param page
     * @param yxOffPayOrderQueryParam
     * @return
     */
    IPage<YxOffPayOrderQueryVo> getYxOffPayOrderPageList(@Param("page") Page page, @Param("param") YxOffPayOrderQueryParam yxOffPayOrderQueryParam);

}
