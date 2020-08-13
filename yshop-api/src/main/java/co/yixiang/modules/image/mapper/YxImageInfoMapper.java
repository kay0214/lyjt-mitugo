package co.yixiang.modules.image.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import co.yixiang.modules.image.entity.YxImageInfo;
import co.yixiang.modules.image.web.param.YxImageInfoQueryParam;
import co.yixiang.modules.image.web.vo.YxImageInfoQueryVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * <p>
 * 图片表 Mapper 接口
 * </p>
 *
 * @author hupeng
 * @since 2020-08-13
 */
@Repository
public interface YxImageInfoMapper extends BaseMapper<YxImageInfo> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxImageInfoQueryVo getYxImageInfoById(Serializable id);

    /**
     * 获取分页对象
     * @param page
     * @param yxImageInfoQueryParam
     * @return
     */
    IPage<YxImageInfoQueryVo> getYxImageInfoPageList(@Param("page") Page page, @Param("param") YxImageInfoQueryParam yxImageInfoQueryParam);

}
