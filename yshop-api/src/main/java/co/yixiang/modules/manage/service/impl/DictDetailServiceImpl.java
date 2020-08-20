package co.yixiang.modules.manage.service.impl;

import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.modules.manage.entity.Dict;
import co.yixiang.modules.manage.entity.DictDetail;
import co.yixiang.modules.manage.mapper.DictDetailMapper;
import co.yixiang.modules.manage.service.DictDetailService;
import co.yixiang.modules.manage.service.DictService;
import co.yixiang.modules.manage.web.param.DictDetailQueryParam;
import co.yixiang.modules.manage.web.vo.DictDetailQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;


/**
 * <p>
 * 数据字典详情 服务实现类
 * </p>
 *
 * @author nxl
 * @since 2020-08-20
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DictDetailServiceImpl extends BaseServiceImpl<DictDetailMapper, DictDetail> implements DictDetailService {

    @Autowired
    private DictDetailMapper dictDetailMapper;
    @Autowired
    private DictService dictService;

    @Override
    public DictDetailQueryVo getDictDetailById(Serializable id) throws Exception {
        return dictDetailMapper.getDictDetailById(id);
    }

    @Override
    public Paging<DictDetailQueryVo> getDictDetailPageList(DictDetailQueryParam dictDetailQueryParam) throws Exception {
        Page page = setPageParam(dictDetailQueryParam, OrderItem.desc("create_time"));
        IPage<DictDetailQueryVo> iPage = dictDetailMapper.getDictDetailPageList(page, dictDetailQueryParam);
        return new Paging(iPage);
    }

    /**
     * 根据字段类型以及数值查找dictDetail
     *
     * @param type
     * @param valueId
     * @return
     */
    @Override
    public DictDetail getDictDetailValueByType(String type, Integer valueId) {
        QueryWrapper<Dict> wrapperDitc = new QueryWrapper<Dict>();
        wrapperDitc.eq("name", type);
        Dict dictParam = dictService.getOne(wrapperDitc);
        if (null != dictParam) {
            QueryWrapper<DictDetail> wrapperDitcDetail = new QueryWrapper<DictDetail>();
            wrapperDitcDetail.eq("dict_id", dictParam.getId());
            wrapperDitcDetail.eq("value", valueId);
            DictDetail dictDetail = dictDetailMapper.selectOne(wrapperDitcDetail);
            return dictDetail;
        }
        return null;
    }
}
