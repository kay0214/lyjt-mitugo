package co.yixiang.modules.bill.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import co.yixiang.modules.bill.entity.YxWxUserBill;
import co.yixiang.modules.bill.web.param.YxWxUserBillQueryParam;
import co.yixiang.modules.bill.web.vo.YxWxUserBillQueryVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * <p>
 * 用户账单明细表 Mapper 接口
 * </p>
 *
 * @author zqq
 * @since 2020-12-10
 */
@Repository
public interface YxWxUserBillMapper extends BaseMapper<YxWxUserBill> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxWxUserBillQueryVo getYxWxUserBillById(Serializable id);

    /**
     * 获取分页对象
     * @param page
     * @param yxWxUserBillQueryParam
     * @return
     */
    IPage<YxWxUserBillQueryVo> getYxWxUserBillPageList(@Param("page") Page page, @Param("param") YxWxUserBillQueryParam yxWxUserBillQueryParam);

}
