package co.yixiang.modules.user.service.impl;

import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.dozer.service.IGenerator;
import co.yixiang.modules.user.entity.YxLeaveMessage;
import co.yixiang.modules.user.mapper.YxLeaveMessageMapper;
import co.yixiang.modules.user.service.YxLeaveMessageService;
import co.yixiang.modules.user.web.dto.YxLeaveMessageSaveDto;
import co.yixiang.modules.user.web.param.YxLeaveMessageQueryParam;
import co.yixiang.modules.user.web.vo.YxLeaveMessageQueryVo;
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
 * 用户留言表 服务实现类
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class YxLeaveMessageServiceImpl extends BaseServiceImpl<YxLeaveMessageMapper, YxLeaveMessage> implements YxLeaveMessageService {

    @Autowired
    private YxLeaveMessageMapper yxLeaveMessageMapper;
    @Autowired
    private IGenerator generator;

    @Override
    public YxLeaveMessageQueryVo getYxLeaveMessageById(Serializable id) throws Exception {
        return yxLeaveMessageMapper.getYxLeaveMessageById(id);
    }

    @Override
    public Paging<YxLeaveMessageQueryVo> getYxLeaveMessagePageList(YxLeaveMessageQueryParam yxLeaveMessageQueryParam) throws Exception {
        Page page = setPageParam(yxLeaveMessageQueryParam, OrderItem.desc("create_time"));
        IPage<YxLeaveMessageQueryVo> iPage = yxLeaveMessageMapper.getYxLeaveMessagePageList(page, yxLeaveMessageQueryParam);
        return new Paging(iPage);
    }

    /**
     * 保存用户留言(暂时不用、联调时如果有数据取不到再考虑做数据加工处理)
     *
     * @param request
     * @return
     */
    @Override
    public boolean saveLeaveMessage(YxLeaveMessageSaveDto request) {
        YxLeaveMessage saveDate = generator.convert(request, YxLeaveMessage.class);
//        // 留言类型：0 -> 商品，1-> 卡券 2 -> 商城订单，3 -> 本地生活订单，4 ->商户，5 -> 平台
//        switch (request.getMessageType()) {
//            case 0:
//                // 商品
//                break;
//            case 1:
//                // 卡券
//                break;
//            case 2:
//                // 商城订单
//                break;
//            case 3:
//                // 本地生活订单
//                break;
//            case 4:
//                // 商户
//
//                break;
//            case 5:
//                // 平台
//
//                break;
//            default:
//                break;
//
//        }


        return false;
    }

}
