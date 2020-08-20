package co.yixiang.modules.manage.service.impl;

import co.yixiang.modules.manage.entity.Dict;
import co.yixiang.modules.manage.mapper.DictMapper;
import co.yixiang.modules.manage.service.DictService;
import co.yixiang.modules.manage.web.param.DictQueryParam;
import co.yixiang.modules.manage.web.vo.DictQueryVo;
import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.web.vo.Paging;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.io.Serializable;


/**
 * <p>
 * 数据字典 服务实现类
 * </p>
 *
 * @author nxl
 * @since 2020-08-20
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DictServiceImpl extends BaseServiceImpl<DictMapper, Dict> implements DictService {

    @Autowired
    private DictMapper dictMapper;

    @Override
    public DictQueryVo getDictById(Serializable id) throws Exception {
        return dictMapper.getDictById(id);
    }

    @Override
    public Paging<DictQueryVo> getDictPageList(DictQueryParam dictQueryParam) throws Exception {
        Page page = setPageParam(dictQueryParam, OrderItem.desc("create_time"));
        IPage<DictQueryVo> iPage = dictMapper.getDictPageList(page, dictQueryParam);
        return new Paging(iPage);
    }

}
