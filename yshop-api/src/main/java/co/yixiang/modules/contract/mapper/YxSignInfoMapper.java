package co.yixiang.modules.contract.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import co.yixiang.modules.contract.entity.YxSignInfo;
import co.yixiang.modules.contract.web.param.YxSignInfoQueryParam;
import co.yixiang.modules.contract.web.vo.YxSignInfoQueryVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * <p>
 * 签章信息表 Mapper 接口
 * </p>
 *
 * @author lsy
 * @since 2020-11-11
 */
@Repository
public interface YxSignInfoMapper extends BaseMapper<YxSignInfo> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxSignInfoQueryVo getYxSignInfoById(Serializable id);

    /**
     * 获取分页对象
     * @param page
     * @param yxSignInfoQueryParam
     * @return
     */
    IPage<YxSignInfoQueryVo> getYxSignInfoPageList(@Param("page") Page page, @Param("param") YxSignInfoQueryParam yxSignInfoQueryParam);

}
