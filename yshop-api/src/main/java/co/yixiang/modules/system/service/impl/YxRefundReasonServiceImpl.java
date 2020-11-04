package co.yixiang.modules.system.service.impl;

import co.yixiang.modules.system.entity.YxRefundReason;
import co.yixiang.modules.system.mapper.YxRefundReasonMapper;
import co.yixiang.modules.system.service.YxRefundReasonService;
import co.yixiang.modules.system.web.param.YxRefundReasonQueryParam;
import co.yixiang.modules.system.web.vo.YxRefundReasonQueryVo;
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
 * 退款理由配置表 服务实现类
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class YxRefundReasonServiceImpl extends BaseServiceImpl<YxRefundReasonMapper, YxRefundReason> implements YxRefundReasonService {

    @Autowired
    private YxRefundReasonMapper yxRefundReasonMapper;

    @Override
    public YxRefundReasonQueryVo getYxRefundReasonById(Serializable id) throws Exception{
        return yxRefundReasonMapper.getYxRefundReasonById(id);
    }

    @Override
    public Paging<YxRefundReasonQueryVo> getYxRefundReasonPageList(YxRefundReasonQueryParam yxRefundReasonQueryParam) throws Exception{
        Page page = setPageParam(yxRefundReasonQueryParam,OrderItem.desc("create_time"));
        IPage<YxRefundReasonQueryVo> iPage = yxRefundReasonMapper.getYxRefundReasonPageList(page,yxRefundReasonQueryParam);
        return new Paging(iPage);
    }

}
