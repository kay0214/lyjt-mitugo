package co.yixiang.modules.shop.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import co.yixiang.modules.shop.entity.YxStoreAttribute;
import co.yixiang.modules.shop.web.param.YxStoreAttributeQueryParam;
import co.yixiang.modules.shop.web.vo.YxStoreAttributeQueryVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * <p>
 * 店铺属性表 Mapper 接口
 * </p>
 *
 * @author hupeng
 * @since 2020-08-13
 */
@Repository
public interface YxStoreAttributeMapper extends BaseMapper<YxStoreAttribute> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxStoreAttributeQueryVo getYxStoreAttributeById(Serializable id);

    /**
     * 获取分页对象
     * @param page
     * @param yxStoreAttributeQueryParam
     * @return
     */
    IPage<YxStoreAttributeQueryVo> getYxStoreAttributePageList(@Param("page") Page page, @Param("param") YxStoreAttributeQueryParam yxStoreAttributeQueryParam);

}
