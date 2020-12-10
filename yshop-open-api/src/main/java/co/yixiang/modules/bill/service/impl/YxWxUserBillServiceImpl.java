package co.yixiang.modules.bill.service.impl;

import co.yixiang.modules.bill.entity.YxWxUserBill;
import co.yixiang.modules.bill.mapper.YxWxUserBillMapper;
import co.yixiang.modules.bill.service.YxWxUserBillService;
import co.yixiang.modules.bill.web.param.YxWxUserBillQueryParam;
import co.yixiang.modules.bill.web.vo.YxWxUserBillQueryVo;
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
 * 用户账单明细表 服务实现类
 * </p>
 *
 * @author zqq
 * @since 2020-12-10
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class YxWxUserBillServiceImpl extends BaseServiceImpl<YxWxUserBillMapper, YxWxUserBill> implements YxWxUserBillService {

    @Autowired
    private YxWxUserBillMapper yxWxUserBillMapper;

    @Override
    public YxWxUserBillQueryVo getYxWxUserBillById(Serializable id) throws Exception{
        return yxWxUserBillMapper.getYxWxUserBillById(id);
    }

    @Override
    public Paging<YxWxUserBillQueryVo> getYxWxUserBillPageList(YxWxUserBillQueryParam yxWxUserBillQueryParam) throws Exception{
        Page page = setPageParam(yxWxUserBillQueryParam,OrderItem.desc("create_time"));
        IPage<YxWxUserBillQueryVo> iPage = yxWxUserBillMapper.getYxWxUserBillPageList(page,yxWxUserBillQueryParam);
        return new Paging(iPage);
    }

}
