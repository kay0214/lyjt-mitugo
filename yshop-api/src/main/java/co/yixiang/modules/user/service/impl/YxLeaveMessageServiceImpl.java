package co.yixiang.modules.user.service.impl;

import cn.hutool.core.date.DateTime;
import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.constant.ShopConstants;
import co.yixiang.dozer.service.IGenerator;
import co.yixiang.exception.BadRequestException;
import co.yixiang.modules.coupons.entity.YxCoupons;
import co.yixiang.modules.coupons.service.YxCouponOrderService;
import co.yixiang.modules.coupons.service.YxCouponsService;
import co.yixiang.modules.image.entity.YxImageInfo;
import co.yixiang.modules.image.service.YxImageInfoService;
import co.yixiang.modules.order.service.YxStoreOrderService;
import co.yixiang.modules.shop.entity.YxStoreProduct;
import co.yixiang.modules.shop.service.YxStoreInfoService;
import co.yixiang.modules.shop.service.YxStoreProductService;
import co.yixiang.modules.user.entity.YxLeaveMessage;
import co.yixiang.modules.user.mapper.YxLeaveMessageMapper;
import co.yixiang.modules.user.service.YxLeaveMessageService;
import co.yixiang.modules.user.web.dto.YxLeaveMessageGetDataDto;
import co.yixiang.modules.user.web.dto.YxLeaveMessageSaveDto;
import co.yixiang.modules.user.web.param.YxLeaveMessageQueryParam;
import co.yixiang.modules.user.web.vo.YxLeaveMessageQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;


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
    private YxStoreProductService yxStoreProductService;
    @Autowired
    private YxStoreOrderService yxStoreOrderService;
    @Autowired
    private YxCouponsService yxCouponsService;
    @Autowired
    private YxCouponOrderService yxCouponOrderService;
    @Autowired
    private YxStoreInfoService yxStoreInfoService;
    @Autowired
    private YxImageInfoService yxImageInfoService;
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
        // 留言类型：0 -> 商品，1-> 卡券 2 -> 商城订单，3 -> 本地生活订单，4 ->商户，5 -> 平台
        if (4 != request.getMessageType() && 5 != request.getMessageType()) {
            List<YxLeaveMessage> findList = this.list(new QueryWrapper<YxLeaveMessage>().lambda()
                    .eq(YxLeaveMessage::getLinkId, request.getLinkId())
                    .eq(YxLeaveMessage::getMessageType, request.getMessageType())
                    .eq(YxLeaveMessage::getStatus, 0));
            if (null != findList && findList.size() > 0) {
                throw new BadRequestException("留言已提交，请勿重复操作");
            }
        }
        YxLeaveMessage saveData = generator.convert(request, YxLeaveMessage.class);
        saveData.setStatus(0);
        saveData.setDelFlag(0);
        saveData.setCreateTime(DateTime.now().toTimestamp());
        boolean flag = this.save(saveData);
        return flag;
    }

    /**
     * 根据留言类型获取相关数据
     *
     * @param request
     * @return
     */
    @Override
    public YxLeaveMessageGetDataDto getData(YxLeaveMessageSaveDto request) {
        YxLeaveMessageGetDataDto result = new YxLeaveMessageGetDataDto();
        // 留言类型：0 -> 商品，1-> 卡券 2 -> 商城订单，3 -> 本地生活订单，4 ->商户，5 -> 平台
        switch (request.getMessageType()) {
            case 0:
                // 商品
                YxStoreProduct yxStoreProduct = this.yxStoreProductService.getById(request.getLinkId());
                if (null == yxStoreProduct) {
                    throw new BadRequestException("留言失败,商品不存在");
                }
                result.setImageUrl(yxStoreProduct.getImage());
                result.setTitle(yxStoreProduct.getStoreName());
                break;
            case 1:
                // 卡券
                YxCoupons yxCoupons = this.yxCouponsService.getById(request.getLinkId());
                if (null == yxCoupons) {
                    throw new BadRequestException("留言失败,卡券不存在");
                }
                // 获取卡券缩略图
                YxImageInfo yxImageInfo = this.yxImageInfoService.getOne(new QueryWrapper<YxImageInfo>().lambda()
                        .eq(YxImageInfo::getDelFlag, 0)
                        .eq(YxImageInfo::getImgCategory, ShopConstants.IMG_CATEGORY_PIC)
                        .eq(YxImageInfo::getImgType, ShopConstants.IMG_TYPE_CARD)
                        .eq(YxImageInfo::getTypeId, request.getLinkId()));
                if (null != yxImageInfo) {
                    result.setImageUrl(yxImageInfo.getImgUrl());
                }
                result.setTitle(yxCoupons.getCouponName());
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                // 商户
                break;
            case 5:
                // 平台
                break;
            default:
                throw new BadRequestException("留言失败");
        }
        return result;
    }

}
