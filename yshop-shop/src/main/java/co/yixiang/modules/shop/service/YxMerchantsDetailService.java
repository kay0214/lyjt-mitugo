/**
* Copyright (C) 2018-2020
*/
package co.yixiang.modules.shop.service;
import co.yixiang.common.service.BaseService;
import co.yixiang.modules.shop.domain.YxMerchantsDetail;
import co.yixiang.modules.shop.service.dto.YxMerchantsDetailDto;
import co.yixiang.modules.shop.service.dto.YxMerchantsDetailQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author liusy
* @date 2020-08-19
*/
public interface YxMerchantsDetailService  extends BaseService<YxMerchantsDetail>{

/**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(YxMerchantsDetailQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<YxMerchantsDetailDto>
    */
    List<YxMerchantsDetailDto> queryAll(YxMerchantsDetailQueryCriteria criteria);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<YxMerchantsDetailDto> all, HttpServletResponse response) throws IOException;

    /**
     * 查询商家信息详情
     *
     * @param id
     * @return
     */
    YxMerchantsDetailDto queryById(Integer id);

    /**
     * 提交认证数据
     *
     * @param resources
     * @return
     */
    boolean createOrUpdate(YxMerchantsDetailDto resources);

    /**
     * 更新审批状态
     *
     * @param resources
     * @return
     */
    boolean updateExamineStatus(YxMerchantsDetailDto resources);
}
