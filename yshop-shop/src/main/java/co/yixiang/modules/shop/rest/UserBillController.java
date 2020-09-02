package co.yixiang.modules.shop.rest;

import co.yixiang.exception.BadRequestException;
import co.yixiang.logging.aop.log.Log;
import co.yixiang.modules.activity.domain.YxUserExtract;
import co.yixiang.modules.shop.domain.User;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

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

    @Log("查询")
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

    /**
     *
     */
    @Log("发起提现")
    @ApiOperation(value = "发起提现")
    @GetMapping(value = "/withdraw")
    @PreAuthorize("hasAnyRole('admin','YXUSERBILL_ALL','YXUSERBILL_WITHDRAW')")
    public ResponseEntity<Object> withdraw(@RequestBody YxUserExtract request) {
        int uid = SecurityUtils.getUserId().intValue();
        boolean result = this.userService.updateUserWithdraw(uid, request.getExtractPrice());
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
