package co.yixiang.modules.coupons.service.impl;

import co.yixiang.modules.coupons.entity.YxCouponsReply;
import co.yixiang.modules.coupons.mapper.YxCouponsReplyMapper;
import co.yixiang.modules.coupons.service.YxCouponsReplyService;
import co.yixiang.modules.coupons.web.param.YxCouponsReplyQueryParam;
import co.yixiang.modules.coupons.web.vo.YxCouponsReplyQueryVo;
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
 * 本地生活评论表 服务实现类
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class YxCouponsReplyServiceImpl extends BaseServiceImpl<YxCouponsReplyMapper, YxCouponsReply> implements YxCouponsReplyService {

    @Autowired
    private YxCouponsReplyMapper yxCouponsReplyMapper;

    @Override
    public YxCouponsReplyQueryVo getYxCouponsReplyById(Serializable id) throws Exception{
        return yxCouponsReplyMapper.getYxCouponsReplyById(id);
    }

    @Override
    public Paging<YxCouponsReplyQueryVo> getYxCouponsReplyPageList(YxCouponsReplyQueryParam yxCouponsReplyQueryParam) throws Exception{
        Page page = setPageParam(yxCouponsReplyQueryParam,OrderItem.desc("create_time"));
        IPage<YxCouponsReplyQueryVo> iPage = yxCouponsReplyMapper.getYxCouponsReplyPageList(page,yxCouponsReplyQueryParam);
        return new Paging(iPage);
    }

}
