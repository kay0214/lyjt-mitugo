package co.yixiang.modules.coupons.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import co.yixiang.modules.coupons.entity.YxCouponsReply;
import co.yixiang.modules.coupons.web.param.YxCouponsReplyQueryParam;
import co.yixiang.modules.coupons.web.vo.YxCouponsReplyQueryVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * <p>
 * 本地生活评论表 Mapper 接口
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
@Repository
public interface YxCouponsReplyMapper extends BaseMapper<YxCouponsReply> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxCouponsReplyQueryVo getYxCouponsReplyById(Serializable id);

    /**
     * 获取分页对象
     * @param page
     * @param yxCouponsReplyQueryParam
     * @return
     */
    IPage<YxCouponsReplyQueryVo> getYxCouponsReplyPageList(@Param("page") Page page, @Param("param") YxCouponsReplyQueryParam yxCouponsReplyQueryParam);

}
