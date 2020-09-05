/**
 * Copyright (C) 2018-2020
 */
package co.yixiang.modules.shop.service;

import co.yixiang.common.service.BaseService;
import co.yixiang.modules.shop.domain.YxImageInfo;
import co.yixiang.modules.shop.service.dto.YxImageInfoDto;
import co.yixiang.modules.shop.service.dto.YxImageInfoQueryCriteria;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author nxl
 * @date 2020-08-14
 */
public interface YxImageInfoService extends BaseService<YxImageInfo> {

    /**
     * 查询数据分页
     * @param criteria 条件
     * @param pageable 分页参数
     * @return Map<String , Object>
     */
    Map<String, Object> queryAll(YxImageInfoQueryCriteria criteria, Pageable pageable);

    /**
     * 查询所有数据不分页
     * @param criteria 条件参数
     * @return List<YxImageInfoDto>
     */
    List<YxImageInfo> queryAll(YxImageInfoQueryCriteria criteria);

    /**
     * 导出数据
     * @param all 待导出的数据
     * @param response /
     * @throws IOException /
     */
    void download(List<YxImageInfoDto> all, HttpServletResponse response) throws IOException;

    /**
     * 查询图片
     *
     * @param typeId
     * @param imgType
     * @param cateTypeId
     * @return
     */
    String selectImgByParam(int typeId, Integer imgType, Integer cateTypeId);

    /**
     * 查询图片返回list
     *
     * @param typeId
     * @param imgType
     * @param cateTypeId
     * @return
     */
    List<String> selectImgByParamList(int typeId, Integer imgType, Integer cateTypeId);
}
