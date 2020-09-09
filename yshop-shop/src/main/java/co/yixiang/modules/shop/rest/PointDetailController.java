package co.yixiang.modules.shop.rest;

import co.yixiang.logging.aop.log.Log;
import co.yixiang.modules.shop.service.YxPointDetailService;
import co.yixiang.modules.shop.service.dto.YxPointDetailQueryCriteria;
import co.yixiang.utils.CurrUser;
import co.yixiang.utils.DateUtils;
import co.yixiang.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author huiy
 * @date 2020-08-19
 */
@AllArgsConstructor
@Api(tags = "积分获取明细管理")
@RestController
@RequestMapping("/api/yxPointDetail")
public class PointDetailController {

    private final YxPointDetailService yxPointDetailService;


    /**
     * 积分明细
     *
     * @param criteria
     * @param pageable
     * @return
     */
    @GetMapping
    @ApiOperation("查询积分获取明细")
    @PreAuthorize("@el.check('admin','yxPointDetail:list')")
    public ResponseEntity<Object> getYxPointDetails(YxPointDetailQueryCriteria criteria, Pageable pageable) {
        CurrUser currUser = SecurityUtils.getCurrUser();
        criteria.setUserRole(currUser.getUserRole());
        if (null != currUser.getChildUser()) {
            criteria.setChildUser(currUser.getChildUser());
        }
        int uid = SecurityUtils.getUserId().intValue();
        criteria.setUid(uid);
        return new ResponseEntity<>(yxPointDetailService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @GetMapping(value = "/pullNew")
    @ApiOperation("查询积分明细, 拉新")
    @PreAuthorize("@el.check('admin','yxPointDetail:list')")
    public ResponseEntity<Object> getYxPointDetailsPullNew(YxPointDetailQueryCriteria criteria, Pageable pageable) {
        CurrUser currUser = SecurityUtils.getCurrUser();
        criteria.setUserRole(currUser.getUserRole());
        if (null != currUser.getChildUser()) {
            criteria.setChildUser(currUser.getChildUser());
        }
        if (criteria.getSearchTime() != null && criteria.getSearchTime().size() > 0) {
            List<Date> timeList = new ArrayList<>();
            timeList.add(DateUtils.parseDate(criteria.getSearchTime().get(0) + " 00:00:00"));
            timeList.add(DateUtils.parseDate(criteria.getSearchTime().get(1) + " 23:59:59"));
            criteria.setCreateTime(timeList);
        }
        // 只取拉新用户
        criteria.setType(0);
        return new ResponseEntity<>(yxPointDetailService.queryAllPullNew(criteria, pageable), HttpStatus.OK);
    }

    @GetMapping(value = "/dividends")
    @ApiOperation("查询积分明细, 分红池")
    @PreAuthorize("@el.check('admin','yxPointDetail:list')")
    public ResponseEntity<Object> getYxPointDetailsDividends(YxPointDetailQueryCriteria criteria, Pageable pageable) {
        CurrUser currUser = SecurityUtils.getCurrUser();
        criteria.setUserRole(currUser.getUserRole());
        if (null != currUser.getChildUser()) {
            criteria.setChildUser(currUser.getChildUser());
        }
        if (criteria.getSearchTime() != null && criteria.getSearchTime().size() > 0) {
            List<Date> timeList = new ArrayList<>();
            timeList.add(DateUtils.parseDate(criteria.getSearchTime().get(0) + " 00:00:00"));
            timeList.add(DateUtils.parseDate(criteria.getSearchTime().get(1) + " 23:59:59"));
            criteria.setCreateTime(timeList);
        }
        // 只取拉新用户
        criteria.setType(1);
        return new ResponseEntity<>(yxPointDetailService.queryAllDividends(criteria, pageable), HttpStatus.OK);
    }
}
