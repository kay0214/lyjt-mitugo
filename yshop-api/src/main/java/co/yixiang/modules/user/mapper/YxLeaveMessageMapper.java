package co.yixiang.modules.user.mapper;

import co.yixiang.modules.couponUse.dto.YxLeaveMessageVo;
import co.yixiang.modules.user.entity.YxLeaveMessage;
import co.yixiang.modules.user.web.param.YxLeaveMessageQueryParam;
import co.yixiang.modules.user.web.vo.YxLeaveMessageQueryVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * <p>
 * 常用联系人表 Mapper 接口
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
@Repository
public interface YxLeaveMessageMapper extends BaseMapper<YxLeaveMessage> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxLeaveMessageQueryVo getYxLeaveMessageById(Serializable id);

    /**
     * 获取分页对象
     * @param page
     * @param yxLeaveMessageQueryParam
     * @return
     */
    IPage<YxLeaveMessageQueryVo> getYxLeaveMessagePageList(@Param("page") Page page, @Param("param") YxLeaveMessageQueryParam yxLeaveMessageQueryParam);

    IPage<YxLeaveMessageVo> getYxLeaveMessagePageListByParam(@Param("page") Page page, @Param("param") YxLeaveMessageQueryParam yxLeaveMessageQueryParam);

}
