package co.yixiang.modules.funds.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import co.yixiang.modules.funds.entity.YxFundsAccount;
import co.yixiang.modules.funds.web.param.YxFundsAccountQueryParam;
import co.yixiang.modules.funds.web.vo.YxFundsAccountQueryVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * <p>
 * 平台账户表 Mapper 接口
 * </p>
 *
 * @author zqq
 * @since 2020-08-13
 */
@Repository
public interface YxFundsAccountMapper extends BaseMapper<YxFundsAccount> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxFundsAccountQueryVo getYxFundsAccountById(Serializable id);

    /**
     * 获取分页对象
     * @param page
     * @param yxFundsAccountQueryParam
     * @return
     */
    IPage<YxFundsAccountQueryVo> getYxFundsAccountPageList(@Param("page") Page page, @Param("param") YxFundsAccountQueryParam yxFundsAccountQueryParam);

}
