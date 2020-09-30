package co.yixiang.modules.shop.rest;

import co.yixiang.common.rocketmq.MqProducer;
import co.yixiang.constant.MQConstant;
import co.yixiang.constant.ShopConstants;
import co.yixiang.constant.SystemConfigConstants;
import co.yixiang.enums.BillDetailEnum;
import co.yixiang.exception.BadRequestException;
import co.yixiang.logging.aop.log.Log;
import co.yixiang.modules.activity.domain.YxUserExtract;
import co.yixiang.modules.shop.domain.YxSystemConfig;
import co.yixiang.modules.shop.service.UserService;
import co.yixiang.modules.shop.service.YxSystemConfigService;
import co.yixiang.modules.shop.service.YxUserBillService;
import co.yixiang.modules.shop.service.dto.WithdrawReviewQueryCriteria;
import co.yixiang.modules.shop.service.dto.YxUserBillQueryCriteria;
import co.yixiang.utils.CurrUser;
import co.yixiang.utils.RedisUtil;
import co.yixiang.utils.SecurityUtils;
import co.yixiang.utils.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hyjf.framework.starter.recketmq.MQException;
import com.hyjf.framework.starter.recketmq.MessageContent;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @author hupeng
 * @date 2019-11-06
 */
@Api(tags = "商城:用户账单表(资金明细)")
@Slf4j
@RestController
@RequestMapping("api")
public class UserBillController {

    @Autowired
    private YxUserBillService yxUserBillService;
    @Autowired
    private UserService userService;
    @Autowired
    private MqProducer mqProducer;
    @Autowired
    private YxSystemConfigService yxSystemConfigService;

    @ApiOperation(value = "资金明细")
    @GetMapping(value = "/yxUserBill")
    @PreAuthorize("hasAnyRole('admin','YXUSERBILL_ALL','YXUSERBILL_SELECT')")
    public ResponseEntity getYxUserBills(YxUserBillQueryCriteria criteria, Pageable pageable) {
        int uid = SecurityUtils.getUserId().intValue();
        CurrUser currUser = SecurityUtils.getCurrUser();
        criteria.setUserRole(currUser.getUserRole());
        criteria.setUid(uid);
        if (null != currUser.getChildUser()) {
            criteria.setChildUser(currUser.getChildUser());
        }
        return new ResponseEntity(yxUserBillService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @ApiOperation(value = "平台资金明细")
    @GetMapping(value = "/yxUserBillAll")
    @PreAuthorize("hasAnyRole('admin','YXUSERBILL_ALL','YXUSERBILL_SELECT')")
    public ResponseEntity getYxUserBillAll(YxUserBillQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity(yxUserBillService.queryAllNew(criteria, pageable), HttpStatus.OK);
    }

    @ApiOperation(value = "积分明细 废弃")
    @GetMapping(value = "/yxUserPointBill")
    @PreAuthorize("hasAnyRole('admin','YXUSERBILL_ALL','YXUSERBILL_SELECT')")
    public ResponseEntity yxUserPointBill(YxUserBillQueryCriteria criteria, Pageable pageable) {
        criteria.setCategory("integral");
        return new ResponseEntity(yxUserBillService.queryAllNew(criteria, pageable), HttpStatus.OK);
    }


    @GetMapping(value = "/yxUserBillType")
    public ResponseEntity yxUserBillType() {
        List<Map<String, String>> listType = new ArrayList<Map<String, String>>();
        for (int i = 1; i <= 12; i++) {
            Map<String, String> mapType = new HashMap<String, String>();
            switch (i) {
                case 1:
                    mapType.put(BillDetailEnum.TYPE_1.getValue(), BillDetailEnum.TYPE_1.getDesc());
                    break;
                case 2:
                    mapType.put(BillDetailEnum.TYPE_2.getValue(), BillDetailEnum.TYPE_2.getDesc());
                    break;
                case 3:
                    mapType.put(BillDetailEnum.TYPE_3.getValue(), BillDetailEnum.TYPE_3.getDesc());
                    break;
                case 4:
                    mapType.put(BillDetailEnum.TYPE_4.getValue(), BillDetailEnum.TYPE_4.getDesc());
                    break;
                case 5:
                    mapType.put(BillDetailEnum.TYPE_5.getValue(), BillDetailEnum.TYPE_5.getDesc());
                    break;
                case 6:
                    mapType.put(BillDetailEnum.TYPE_6.getValue(), BillDetailEnum.TYPE_6.getDesc());
                    break;
                case 7:
                    mapType.put(BillDetailEnum.TYPE_7.getValue(), BillDetailEnum.TYPE_7.getDesc());
                    break;
                case 8:
                    mapType.put(BillDetailEnum.TYPE_8.getValue(), BillDetailEnum.TYPE_8.getDesc());
                    break;
                case 9:
                    mapType.put(BillDetailEnum.TYPE_9.getValue(), BillDetailEnum.TYPE_9.getDesc());
                    break;
                case 10:
                    mapType.put(BillDetailEnum.TYPE_10.getValue(), BillDetailEnum.TYPE_10.getDesc());
                    break;
                case 11:
                    mapType.put(BillDetailEnum.TYPE_11.getValue(), BillDetailEnum.TYPE_11.getDesc());
                    break;
                case 12:
                    mapType.put(BillDetailEnum.TYPE_12.getValue(), BillDetailEnum.TYPE_12.getDesc());
                    break;
            }
            listType.add(mapType);
        }
        return new ResponseEntity(listType, HttpStatus.OK);

    }

    /**
     *
     */
    @Log("发起提现")
    @ApiOperation(value = "发起提现")
    @PostMapping(value = "/withdraw")
    @PreAuthorize("hasAnyRole('admin','YXUSERBILL_ALL','YXUSERBILL_WITHDRAW')")
    public ResponseEntity<Object> withdraw(@RequestBody YxUserExtract request) throws MQException {
        int uid = SecurityUtils.getUserId().intValue();

        // 是否可提现校验
        YxSystemConfig yxSystemConfig = this.yxSystemConfigService.getOne(new QueryWrapper<YxSystemConfig>().lambda().eq(YxSystemConfig::getMenuName, SystemConfigConstants.STORE_EXTRACT_SWITCH));
        if (null == yxSystemConfig || "1".equals(yxSystemConfig.getValue())) {
            throw new BadRequestException("未到提现开放时间");
        }

        // 同一时间 用户只能提现一次
        String value = RedisUtil.get(ShopConstants.WITHDRAW_USER_SUBMIT_ADMIN + uid);
        if (StringUtils.isNotBlank(value)) {
            log.info("提现 操作过快，请稍候，id：{}", uid);
            return new ResponseEntity<>(false, HttpStatus.OK);
        }
        RedisUtil.set(ShopConstants.WITHDRAW_USER_SUBMIT_ADMIN + uid, 1, 5);
        Integer id = 0;
        try {
            // 0->平台运营,1->合伙人,2->商户 userType 1商户;2合伙人;3用户
            int userType = 1;
            if (1 == SecurityUtils.getCurrUser().getUserRole()) {
                userType = 2;
            }
            id = this.userService.updateUserWithdraw(uid, userType, request.getExtractPrice());
        } catch (Exception e) {
            log.error("提现 出错，id：{}", uid, e);
            return new ResponseEntity<>(false, HttpStatus.OK);
        } finally {
            RedisUtil.del(ShopConstants.WITHDRAW_USER_SUBMIT_ADMIN + uid);
        }


        if (id != null && id > 0) {
            // 插入一条提现是申请记录后发送mq
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", id + "");
            mqProducer.messageSendDelay(new MessageContent(MQConstant.MITU_TOPIC, MQConstant.MITU_WITHDRAW_TAG, UUID.randomUUID().toString(), jsonObject), 2);
        }
        return new ResponseEntity<>(true, HttpStatus.OK);
    }


    /**
     * 查询提现记录的审批记录
     *
     * @param criteria
     * @param pageable
     * @return
     */
    @Log("查询提现审批记录")
    @ApiOperation(value = "查询提现审批记录")
    @GetMapping(value = "/withdrawReviewLog")
    public ResponseEntity<Object> getUserWithdrawList(WithdrawReviewQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity(yxUserBillService.queryAll(criteria, pageable), HttpStatus.OK);
    }


    /**
     * 积分明细
     *
     * @param criteria
     * @param pageable
     * @return
     */
    @Log("查询积分明细")
    @ApiOperation(value = "查询积分明细")
    @GetMapping(value = "/pointDetail")
    public ResponseEntity<Object> getPointDetail(YxUserBillQueryCriteria criteria, Pageable pageable) {
        criteria.setUid(SecurityUtils.getUserId().intValue());
        criteria.setUserRole(SecurityUtils.getCurrUser().getUserRole());
        return new ResponseEntity(yxUserBillService.getPointDetail(criteria, pageable), HttpStatus.OK);
    }

    /**
     * 分红池
     *
     * @param criteria
     * @param pageable
     * @return
     */
    @Log("分红池")
    @ApiOperation(value = "分红池")
    @GetMapping(value = "/getShareDividendPoint")
    public ResponseEntity<Object> getShareDividendPoint(YxUserBillQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity(yxUserBillService.getShareDividendPoint(criteria, pageable), HttpStatus.OK);
    }

    /**
     * 拉新池
     *
     * @param criteria
     * @param pageable
     * @return
     */
    @Log("拉新池")
    @ApiOperation(value = "拉新池")
    @GetMapping(value = "/getPullNewPoint")
    public ResponseEntity<Object> getPullNewPoint(YxUserBillQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity(yxUserBillService.getPullNewPoint(criteria, pageable), HttpStatus.OK);
    }

}
