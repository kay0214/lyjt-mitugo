package co.yixiang.modules.shop.rest;

import co.yixiang.enums.BillDetailEnum;
import co.yixiang.logging.aop.log.Log;
import co.yixiang.modules.activity.domain.YxUserExtract;
import co.yixiang.modules.shop.service.UserService;
import co.yixiang.modules.shop.service.YxUserBillService;
import co.yixiang.modules.shop.service.dto.WithdrawReviewQueryCriteria;
import co.yixiang.modules.shop.service.dto.YxUserBillQueryCriteria;
import co.yixiang.utils.CurrUser;
import co.yixiang.utils.DateUtils;
import co.yixiang.utils.SecurityUtils;
import co.yixiang.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hupeng
 * @date 2019-11-06
 */
@Api(tags = "商城:用户账单表(资金明细)")
@RestController
@RequestMapping("api")
public class UserBillController {

    @Autowired
    private YxUserBillService yxUserBillService;
    @Autowired
    private UserService userService;

    @ApiOperation(value = "查询")
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
        if (StringUtils.isNotBlank(criteria.getAddTimeStart()) && StringUtils.isBlank(criteria.getAddTimeEnd())) {
            criteria.setAddTimeEnd(DateUtils.getDate());
        }
        if (StringUtils.isBlank(criteria.getAddTimeStart()) && StringUtils.isNotBlank(criteria.getAddTimeEnd())) {
            criteria.setAddTimeStart(DateUtils.getDate());
        }
        return new ResponseEntity(yxUserBillService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @GetMapping(value = "/yxUserBillType")
    public ResponseEntity yxUserBillType() {
        List<Map<String,String>> listType = new ArrayList<Map<String,String>>();
        for(int i=1;i<=10;i++){
            Map<String,String> mapType = new HashMap<String,String>();
            switch (i){
                case 1:
                    mapType.put(BillDetailEnum.TYPE_1.getValue(),BillDetailEnum.TYPE_1.getDesc());
                    break;
                case 2:
                    mapType.put(BillDetailEnum.TYPE_2.getValue(),BillDetailEnum.TYPE_2.getDesc());
                    break;
                case 3:
                    mapType.put(BillDetailEnum.TYPE_3.getValue(),BillDetailEnum.TYPE_3.getDesc());
                    break;
                case 4:
                    mapType.put(BillDetailEnum.TYPE_4.getValue(),BillDetailEnum.TYPE_4.getDesc());
                    break;
                case 5:
                    mapType.put(BillDetailEnum.TYPE_5.getValue(),BillDetailEnum.TYPE_5.getDesc());
                    break;
                case 6:
                    mapType.put(BillDetailEnum.TYPE_6.getValue(),BillDetailEnum.TYPE_6.getDesc());
                    break;
                case 7:
                    mapType.put(BillDetailEnum.TYPE_7.getValue(),BillDetailEnum.TYPE_7.getDesc());
                    break;
                case 8:
                    mapType.put(BillDetailEnum.TYPE_8.getValue(),BillDetailEnum.TYPE_8.getDesc());
                    break;
                case 9:
                    mapType.put(BillDetailEnum.TYPE_9.getValue(),BillDetailEnum.TYPE_9.getDesc());
                    break;
                case 10:
                    mapType.put(BillDetailEnum.TYPE_10.getValue(),BillDetailEnum.TYPE_10.getDesc());
                    break;
            }
            listType.add(mapType);
        }
        return new ResponseEntity(listType,HttpStatus.OK);

    }
    /**
     *
     */
    @Log("发起提现")
    @ApiOperation(value = "发起提现")
    @PostMapping(value = "/withdraw")
    @PreAuthorize("hasAnyRole('admin','YXUSERBILL_ALL','YXUSERBILL_WITHDRAW')")
    public ResponseEntity<Object> withdraw(@RequestBody YxUserExtract request) {
        int uid = SecurityUtils.getUserId().intValue();
        // 0->平台运营,1->合伙人,2->商户
        int userType = 2;
        if (1 == SecurityUtils.getCurrUser().getUserRole()) {
            userType = 3;
        }
        boolean result = this.userService.updateUserWithdraw(uid, userType, request.getExtractPrice());
        return new ResponseEntity<>(result, HttpStatus.OK);
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

}
