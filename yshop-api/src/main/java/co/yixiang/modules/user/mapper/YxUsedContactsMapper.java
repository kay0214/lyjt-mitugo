package co.yixiang.modules.user.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import co.yixiang.modules.user.entity.YxUsedContacts;
import co.yixiang.modules.user.web.param.YxUsedContactsQueryParam;
import co.yixiang.modules.user.web.vo.YxUsedContactsQueryVo;
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
public interface YxUsedContactsMapper extends BaseMapper<YxUsedContacts> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxUsedContactsQueryVo getYxUsedContactsById(Serializable id);

    /**
     * 获取分页对象
     * @param page
     * @param yxUsedContactsQueryParam
     * @return
     */
    IPage<YxUsedContactsQueryVo> getYxUsedContactsPageList(@Param("page") Page page, @Param("param") YxUsedContactsQueryParam yxUsedContactsQueryParam);

}
