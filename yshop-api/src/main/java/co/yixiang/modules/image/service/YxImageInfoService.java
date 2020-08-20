package co.yixiang.modules.image.service;

import co.yixiang.modules.image.entity.YxImageInfo;
import co.yixiang.common.service.BaseService;
import co.yixiang.modules.image.web.param.YxImageInfoQueryParam;
import co.yixiang.modules.image.web.vo.YxImageInfoQueryVo;
import co.yixiang.common.web.vo.Paging;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 图片表 服务类
 * </p>
 *
 * @author hupeng
 * @since 2020-08-13
 */
public interface YxImageInfoService extends BaseService<YxImageInfo> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxImageInfoQueryVo getYxImageInfoById(Serializable id) throws Exception;

    /**
     * 获取分页对象
     * @param yxImageInfoQueryParam
     * @return
     */
    Paging<YxImageInfoQueryVo> getYxImageInfoPageList(YxImageInfoQueryParam yxImageInfoQueryParam) throws Exception;
    /**
     * 获取图片信息返回list
     * @param typeId
     * @param imgType
     * @param cateTypeId
     * @return
     */
    List<String> selectImgByParamList(int typeId, Integer imgType, Integer cateTypeId);
    /**
     * 获取图片信息,返回逗号拼接的字符
     * @param typeId
     * @param imgType
     * @param cateTypeId
     * @return
     */
    String selectImgByParam(int typeId, Integer imgType, Integer cateTypeId);
}
