package co.yixiang.modules.user.service.impl;

import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.enums.BillDetailEnum;
import co.yixiang.enums.BillEnum;
import co.yixiang.enums.BillInfoEnum;
import co.yixiang.modules.manage.entity.SystemUser;
import co.yixiang.modules.manage.service.SystemUserService;
import co.yixiang.modules.manage.web.vo.SystemUserQueryVo;
import co.yixiang.modules.order.web.vo.YxStoreOrderQueryVo;
import co.yixiang.modules.user.entity.YxUserBill;
import co.yixiang.modules.user.mapper.YxUserBillMapper;
import co.yixiang.modules.user.mapping.BiillMap;
import co.yixiang.modules.user.service.YxUserBillService;
import co.yixiang.modules.user.web.dto.BillDTO;
import co.yixiang.modules.user.web.dto.BillOrderDTO;
import co.yixiang.modules.user.web.dto.BillOrderRecordDTO;
import co.yixiang.modules.user.web.param.YxUserBillQueryParam;
import co.yixiang.modules.user.web.vo.YxUserBillQueryVo;
import co.yixiang.utils.OrderUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;


/**
 * <p>
 * 用户账单表 服务实现类
 * </p>
 *
 * @author hupeng
 * @since 2020-08-13
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class YxUserBillServiceImpl extends BaseServiceImpl<YxUserBillMapper, YxUserBill> implements YxUserBillService {

    @Autowired
    private YxUserBillMapper yxUserBillMapper;
    @Autowired
    private BiillMap biillMap;
    @Autowired
    private SystemUserService systemUserService;

    /**
     * 签到了多少次
     * @param uid
     * @return
     */
    @Override
    public int cumulativeAttendance(int uid) {
        QueryWrapper<YxUserBill> wrapper = new QueryWrapper<>();
        wrapper.eq("uid",uid).eq("category","integral")
                .eq("type","sign").eq("pm",1);
        return yxUserBillMapper.selectCount(wrapper);
    }

    @Override
    public Map<String, Object> spreadOrder(int uid, int page, int limit) {
        QueryWrapper<YxUserBill> wrapper = new QueryWrapper<>();
        wrapper.in("uid",uid).eq("type","brokerage")
                .eq("category","now_money").orderByDesc("time")
                .groupBy("time");
        Page<YxUserBill> pageModel = new Page<>(page, limit);
        List<String> list = yxUserBillMapper.getBillOrderList(wrapper,pageModel);


//        QueryWrapper<YxUserBill> wrapperT = new QueryWrapper<>();
//        wrapperT.in("uid",uid).eq("type","brokerage")
//                .eq("category","now_money");

        int count = yxUserBillMapper.selectCount(Wrappers.<YxUserBill>lambdaQuery()
                .eq(YxUserBill::getUid,uid)
                .eq(YxUserBill::getType, BillDetailEnum.TYPE_2.getValue())
                .eq(YxUserBill::getCategory,BillDetailEnum.CATEGORY_1.getValue()));
        List<BillOrderDTO> listT = new ArrayList<>();
        for (String str : list) {
            BillOrderDTO billOrderDTO = new BillOrderDTO();
            List<BillOrderRecordDTO> orderRecordDTOS = yxUserBillMapper
                    .getBillOrderRList(str,uid);
            billOrderDTO.setChild(orderRecordDTOS);
            billOrderDTO.setCount(orderRecordDTOS.size());
            billOrderDTO.setTime(str);

            listT.add(billOrderDTO);
        }

        Map<String,Object> map = new LinkedHashMap<>();
        map.put("list",listT);
        map.put("count",count);

        return map;
    }

    @Override
    public List<BillDTO> getUserBillList(int page, int limit, int uid, int type) {
        QueryWrapper<YxUserBill> wrapper = new QueryWrapper<>();
        wrapper.eq("uid",uid).orderByDesc("add_time").groupBy("time");
        switch (BillInfoEnum.toType(type)){
            case PAY_PRODUCT:
                wrapper.eq("category","now_money");
                wrapper.eq("type","pay_product");
                break;
            case RECHAREGE:
                wrapper.eq("category","now_money");
                wrapper.eq("type","recharge");
                break;
            case BROKERAGE:
                wrapper.eq("category","now_money");
                wrapper.eq("type","brokerage");
                break;
            case EXTRACT:
                wrapper.eq("category","now_money");
                wrapper.eq("type","extract");
                break;
            case SIGN_INTEGRAL:
                wrapper.eq("category","integral");
                wrapper.eq("type","sign");
                break;
            default:
                wrapper.eq("category","now_money");
                String str = "recharge,brokerage,pay_product,system_add,pay_product_refund,system_sub";
                wrapper.in("type", Arrays.asList(str.split(",")));

        }
        Page<YxUserBill> pageModel = new Page<>(page, limit);
        List<BillDTO> billDTOList = yxUserBillMapper.getBillList(wrapper,pageModel);
        for (BillDTO billDTO : billDTOList) {
            QueryWrapper<YxUserBill> wrapperT = new QueryWrapper<>();
            wrapperT.in("id",Arrays.asList(billDTO.getIds().split(",")));
            billDTO.setList(yxUserBillMapper.getUserBillList(wrapperT));

        }

        return billDTOList;
    }

    @Override
    public double getBrokerage(int uid) {
        return yxUserBillMapper.sumPrice(uid);
    }

    @Override
    public BigDecimal yesterdayCommissionSum(int uid) {
        return yxUserBillMapper.sumYesterdayPrice(uid);
    }

    @Override
    public List<YxUserBillQueryVo> userBillList(int uid, int page,
                                                int limit, String category) {
        QueryWrapper<YxUserBill> wrapper = new QueryWrapper<>();
        wrapper.eq("status",1).eq("uid",uid)
                .eq("category",category).orderByDesc("add_time");
        Page<YxUserBill> pageModel = new Page<>(page, limit);

        IPage<YxUserBill> pageList = yxUserBillMapper.selectPage(pageModel,wrapper);
        return biillMap.toDto(pageList.getRecords());
    }
    @Override
    public YxUserBillQueryVo getYxUserBillById(Serializable id) throws Exception{
        return yxUserBillMapper.getYxUserBillById(id);
    }

    @Override
    public Paging<YxUserBillQueryVo> getYxUserBillPageList(YxUserBillQueryParam yxUserBillQueryParam) throws Exception{
        Page page = setPageParam(yxUserBillQueryParam,OrderItem.desc("add_time"));
        IPage<YxUserBillQueryVo> iPage = yxUserBillMapper.getYxUserBillPageList(page,yxUserBillQueryParam);
        return new Paging(iPage);
    }

    /**
     * 确认收货保存商户资金和可用资金
     * @param order
     */
    @Override
    @Transactional
    public void saveMerchantsBill(YxStoreOrderQueryVo order){
        //更新user表的可提现金额
        //订单支付金额
        BigDecimal bigAmount = order.getPayPrice();
        SystemUserQueryVo systemUserQueryVo = systemUserService.getUserById(order.getMerId());
        bigAmount = bigAmount.add(systemUserQueryVo.getWithdrawalAmount());
        SystemUser systemUser = systemUserService.getById(order.getMerId());
//        systemUser.setWithdrawalAmount(bigAmount);
        systemUserService.updateById(systemUser);
        //插入资金明细
        YxUserBill userBill = new YxUserBill();
        userBill.setUid(order.getMerId());
        userBill.setLinkId(order.getOrderId());
        userBill.setTitle("商户返现");
        userBill.setCategory(BillDetailEnum.CATEGORY_2.getValue());
        userBill.setType(BillDetailEnum.TYPE_9.getValue());
        userBill.setNumber(order.getPayPrice());
        userBill.setUsername(systemUserQueryVo.getUsername());
        userBill.setBalance(bigAmount);
        userBill.setMerId(order.getMerId());
        userBill.setMark("订单确认收货，商户返现");
        userBill.setAddTime(OrderUtil.getSecondTimestampTwo());
        userBill.setStatus(BillEnum.STATUS_1.getValue());
        yxUserBillMapper.insert(userBill);
    }
}
