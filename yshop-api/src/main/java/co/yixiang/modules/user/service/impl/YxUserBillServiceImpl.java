package co.yixiang.modules.user.service.impl;

import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.enums.BillDetailEnum;
import co.yixiang.enums.BillEnum;
import co.yixiang.enums.BillInfoEnum;
import co.yixiang.modules.couponUse.dto.UserBillVo;
import co.yixiang.modules.couponUse.param.UserAccountQueryParam;
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
import co.yixiang.utils.CommonsUtils;
import co.yixiang.utils.DateUtils;
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
     *
     * @param uid
     * @return
     */
    @Override
    public int cumulativeAttendance(int uid) {
        QueryWrapper<YxUserBill> wrapper = new QueryWrapper<>();
        wrapper.eq("uid", uid).eq("category", "integral")
                .eq("type", "sign").eq("pm", 1);
        return yxUserBillMapper.selectCount(wrapper);
    }

    @Override
    public Map<String, Object> spreadOrder(int uid, int page, int limit) {
        QueryWrapper<YxUserBill> wrapper = new QueryWrapper<>();
        wrapper.in("uid", uid).eq("type", "brokerage")
                .eq("category", "now_money").orderByDesc("time")
                .groupBy("time");
        Page<YxUserBill> pageModel = new Page<>(page, limit);
        List<String> list = yxUserBillMapper.getBillOrderList(wrapper, pageModel);


//        QueryWrapper<YxUserBill> wrapperT = new QueryWrapper<>();
//        wrapperT.in("uid",uid).eq("type","brokerage")
//                .eq("category","now_money");

        int count = yxUserBillMapper.selectCount(Wrappers.<YxUserBill>lambdaQuery()
                .eq(YxUserBill::getUid, uid)
                .eq(YxUserBill::getType, BillDetailEnum.TYPE_2.getValue())
                .eq(YxUserBill::getCategory, BillDetailEnum.CATEGORY_1.getValue()));
        List<BillOrderDTO> listT = new ArrayList<>();
        for (String str : list) {
            BillOrderDTO billOrderDTO = new BillOrderDTO();
            List<BillOrderRecordDTO> orderRecordDTOS = yxUserBillMapper
                    .getBillOrderRList(str, uid);
            billOrderDTO.setChild(orderRecordDTOS);
            billOrderDTO.setCount(orderRecordDTOS.size());
            billOrderDTO.setTime(str);

            listT.add(billOrderDTO);
        }

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("list", listT);
        map.put("count", count);

        return map;
    }

    @Override
    public List<BillDTO> getUserBillList(int page, int limit, int uid, int type) {
        QueryWrapper<YxUserBill> wrapper = new QueryWrapper<>();
        wrapper.eq("uid", uid).orderByDesc("add_time").groupBy("time");
        switch (BillInfoEnum.toType(type)) {
            case PAY_PRODUCT:
                wrapper.eq("category", "now_money");
                wrapper.eq("type", "pay_product");
                break;
            case RECHAREGE:
                wrapper.eq("category", "now_money");
                wrapper.eq("type", "recharge");
                break;
            case BROKERAGE:
                wrapper.eq("category", "now_money");
                wrapper.eq("type", "brokerage");
                break;
            case EXTRACT:
                wrapper.eq("category", "now_money");
                wrapper.eq("type", "extract");
                break;
            case SIGN_INTEGRAL:
                wrapper.eq("category", "integral");
                wrapper.eq("type", "sign");
                break;
            default:
                wrapper.eq("category", "now_money");
                String str = "recharge,brokerage,pay_product,system_add,pay_product_refund,system_sub";
                wrapper.in("type", Arrays.asList(str.split(",")));

        }
        wrapper.eq("user_type", 3);
        Page<YxUserBill> pageModel = new Page<>(page, limit);
        List<BillDTO> billDTOList = yxUserBillMapper.getBillList(wrapper, pageModel);
        for (BillDTO billDTO : billDTOList) {
            QueryWrapper<YxUserBill> wrapperT = new QueryWrapper<>();
            wrapperT.in("id", Arrays.asList(billDTO.getIds().split(","))).orderByDesc("add_time");
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
    public BigDecimal todayCommissionSum(int uid) {
        return yxUserBillMapper.todayCommissionSum(uid);
    }

    @Override
    public BigDecimal totalCommissionSum(int uid) {
        return yxUserBillMapper.totalCommissionSum(uid);
    }

    @Override
    public List<YxUserBillQueryVo> userBillList(int uid, int page,
                                                int limit, String category) {
        QueryWrapper<YxUserBill> wrapper = new QueryWrapper<>();
        wrapper.eq("status", 1).eq("uid", uid)
                .eq("category", category).orderByDesc("add_time");
        Page<YxUserBill> pageModel = new Page<>(page, limit);

        IPage<YxUserBill> pageList = yxUserBillMapper.selectPage(pageModel, wrapper);
        return biillMap.toDto(pageList.getRecords());
    }

    @Override
    public YxUserBillQueryVo getYxUserBillById(Serializable id) throws Exception {
        return yxUserBillMapper.getYxUserBillById(id);
    }

    @Override
    public Paging<YxUserBillQueryVo> getYxUserBillPageList(YxUserBillQueryParam yxUserBillQueryParam) throws Exception {
        Page page = setPageParam(yxUserBillQueryParam, OrderItem.desc("add_time"));
        IPage<YxUserBillQueryVo> iPage = yxUserBillMapper.getYxUserBillPageList(page, yxUserBillQueryParam);
        return new Paging(iPage);
    }

    /**
     * 确认收货保存商户资金和可用资金
     *
     * @param order
     */
    @Override
    @Transactional
    public void saveMerchantsBill(YxStoreOrderQueryVo order) {
        BigDecimal bigAmount = BigDecimal.ZERO;
        BigDecimal bigTotle = BigDecimal.ZERO;
        BigDecimal bigMerPrice = BigDecimal.ZERO;

        //获取订单佣金
        List<String> cartIds = Arrays.asList(order.getCartId().split(","));
        BigDecimal bigDecimalComm = yxUserBillMapper.getSumCommission(cartIds);
        // 商户收入=支付金额-佣金 小于0 的情况给个0
        if(order.getPayPrice().compareTo(bigDecimalComm) > 0) {
            bigMerPrice = order.getPayPrice().subtract(bigDecimalComm);
        }

        //更新user表的可提现金额
        //订单支付金额
        SystemUserQueryVo systemUserQueryVo = systemUserService.getUserById(order.getMerId());
        bigAmount = bigMerPrice.add(systemUserQueryVo.getWithdrawalAmount());
        bigTotle = bigMerPrice.add(systemUserQueryVo.getTotalAmount());
        SystemUser systemUser = systemUserService.getById(order.getMerId());
        systemUser.setWithdrawalAmount(bigAmount);
        systemUser.setTotalAmount(bigTotle);
        systemUserService.updateById(systemUser);
        //插入资金明细
        YxUserBill userBill = new YxUserBill();
        userBill.setUid(order.getMerId());
        userBill.setLinkId(order.getOrderId());
        userBill.setPm(BillEnum.PM_1.getValue());
        userBill.setTitle("小程序商品购买");
        userBill.setCategory(BillDetailEnum.CATEGORY_1.getValue());
        userBill.setType(BillDetailEnum.TYPE_3.getValue());
        userBill.setNumber(bigMerPrice);
        userBill.setUsername(systemUserQueryVo.getNickName());
        userBill.setBalance(order.getPayPrice());
        userBill.setMerId(order.getMerId());
        userBill.setMark("小程序购买商品，订单确认收货，商户收入");
        userBill.setAddTime(OrderUtil.getSecondTimestampTwo());
        userBill.setStatus(BillEnum.STATUS_1.getValue());
        //商户
        userBill.setUserType(1);
        yxUserBillMapper.insert(userBill);
    }

    /**
     * 查询商户的线下交易流水列表
     *
     * @param param
     * @return
     */
    @Override
    public Paging<UserBillVo> getYxUserAccountPageList(UserAccountQueryParam param, Long userId) {

        QueryWrapper<YxUserBill> wrapper = new QueryWrapper<>();
        wrapper.eq("status", 1).eq("uid", userId)
                .eq("type", BillDetailEnum.TYPE_10.getValue())
                .eq("user_type", 1)
                .eq("category", BillDetailEnum.CATEGORY_1.getValue())
                .orderByDesc("id");

        Page<YxUserBill> pageModel = new Page<>(param.getPage(), param.getLimit());

        IPage<YxUserBill> pageList = yxUserBillMapper.selectPage(pageModel, wrapper);
        return getResultList(pageList);
    }

    @Override
    public Paging<UserBillVo> getUserProductAccountList(UserAccountQueryParam param, Long id) {
        QueryWrapper<YxUserBill> wrapper = new QueryWrapper<>();
        wrapper.eq("status", 1).eq("uid", id)
                .in("type", BillDetailEnum.TYPE_3.getValue(), BillDetailEnum.TYPE_8.getValue())
                .eq("user_type", 1).eq("category", BillDetailEnum.CATEGORY_1.getValue())
                .orderByDesc("id");

        Page<YxUserBill> pageModel = new Page<>(param.getPage(), param.getLimit());

        IPage<YxUserBill> pageList = yxUserBillMapper.selectPage(pageModel, wrapper);


        return getResultList(pageList);
    }

    /**
     * 查询线下支付的数据统计
     *
     * @return
     */
    @Override
    public Map<String, Object> getTodayOffPayData() {
        return yxUserBillMapper.getTodayOffPayData();
    }

    /**
     * 线上支付信息
     *
     * @return
     */
    @Override
    public Map<String, Object> getOnlinePayData() {
        return yxUserBillMapper.getOnlinePayData();
    }

    private Paging<UserBillVo> getResultList(IPage<YxUserBill> result) {
        Paging<UserBillVo> resultStr = new Paging<UserBillVo>();
        resultStr.setSum(result.getTotal() + "");
        resultStr.setTotal(result.getTotal());
        if (result.getRecords() != null) {
            List<UserBillVo> list = new ArrayList<>();
            for (YxUserBill item : result.getRecords()) {
                UserBillVo userBillVo = CommonsUtils.convertBean(item, UserBillVo.class);
                userBillVo.setAddTimeStr(DateUtils.timestampToStr10(item.getAddTime(), DateUtils.YYYY_MM_DD_HH_MM_SS));
                list.add(userBillVo);
            }
            resultStr.setRecords(list);
        }
        return resultStr;
    }

}
