package co.yixiang.modules.coupon.rest;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import co.yixiang.exception.BadRequestException;
import co.yixiang.logging.aop.log.Log;
import co.yixiang.modules.coupon.domain.CouponOrderModifyRequest;
import co.yixiang.modules.coupon.domain.YxCouponOrder;
import co.yixiang.modules.coupon.domain.YxCouponOrderUse;
import co.yixiang.modules.coupon.service.YxCouponOrderService;
import co.yixiang.modules.coupon.service.YxCouponOrderUseService;
import co.yixiang.modules.coupon.service.dto.YxCouponOrderDto;
import co.yixiang.modules.coupon.service.dto.YxCouponOrderQueryCriteria;
import co.yixiang.utils.Base64Utils;
import co.yixiang.utils.CurrUser;
import co.yixiang.utils.SecurityUtils;
import co.yixiang.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author huiy
 * @date 2020-08-14
 */
@AllArgsConstructor
@Api(tags = "卡券订单表管理")
@RestController
@RequestMapping("/api/yxCouponOrder")
public class YxCouponOrderController {

    private final YxCouponOrderService yxCouponOrderService;

    private final YxCouponOrderUseService yxCouponOrderUseService;


    @GetMapping
    @Log("查询卡券订单表")
    @ApiOperation("查询卡券订单表")
    @PreAuthorize("@el.check('admin','yxCouponOrder:list')")
    public ResponseEntity<Object> getYxCouponOrders(YxCouponOrderQueryCriteria criteria, Pageable pageable,
                                                    @RequestParam(name = "orderStatus") Integer orderStatus,
                                                    @RequestParam(name = "orderType") String orderType,
                                                    @RequestParam(name = "orderType") String value) {
        CurrUser currUser = SecurityUtils.getCurrUser();
        criteria.setUserRole(currUser.getUserRole());
        if (null != currUser.getChildUser()) {
            criteria.setChildUser(currUser.getChildUser());
        }
        criteria.setOrderStatus(orderStatus);
        criteria.setOrderType(orderType);
        criteria.setValue(value);
        return new ResponseEntity<>(yxCouponOrderService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @GetMapping(value = "/getCouponOrderInfo/{id}")
    @Log("查询卡券订单详情")
    @ApiOperation("查询卡券订单详情")
    public ResponseEntity<Object> getYxCouponOrderInfo(@PathVariable String id) {
        if (StringUtils.isBlank(id)) {
            throw new BadRequestException("请传入正确的ID!");
        }
        QueryWrapper<YxCouponOrder> yxCouponOrderQueryWrapper = new QueryWrapper<>();
        yxCouponOrderQueryWrapper.lambda()
                .and(idStr -> idStr.eq(YxCouponOrder::getId, Integer.valueOf(id)))
                .and(del -> del.eq(YxCouponOrder::getDelFlag, 0));
        YxCouponOrder yxCouponOrder = yxCouponOrderService.getOne(yxCouponOrderQueryWrapper);

        // 查询卡券的核销记录
        QueryWrapper<YxCouponOrderUse> yxCouponOrderUseQueryWrapper = new QueryWrapper<>();
        yxCouponOrderUseQueryWrapper.lambda()
                .and(orderIdStr -> orderIdStr.eq(YxCouponOrderUse::getOrderId, yxCouponOrder.getOrderId()));
        List<YxCouponOrderUse> couponOrderUseList = yxCouponOrderUseService.list(yxCouponOrderUseQueryWrapper);
        YxCouponOrderDto yxCouponOrderDto = new YxCouponOrderDto();
        BeanUtil.copyProperties(yxCouponOrder, yxCouponOrderDto);
        yxCouponOrderDto.setCouponOrderUseList(couponOrderUseList);
        return new ResponseEntity<>(yxCouponOrderDto, HttpStatus.OK);
    }

    @PostMapping
    @Log("新增卡券订单表")
    @ApiOperation("新增卡券订单表")
    @PreAuthorize("@el.check('admin','yxCouponOrder:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody YxCouponOrder resources) {
        return new ResponseEntity<>(yxCouponOrderService.save(resources), HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改卡券订单表")
    @ApiOperation("修改卡券订单表")
    @PreAuthorize("@el.check('admin','yxCouponOrder:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody CouponOrderModifyRequest request) {
        YxCouponOrder yxCouponOrder = new YxCouponOrder();
        BeanUtil.copyProperties(request, yxCouponOrder);
        return new ResponseEntity<>(yxCouponOrderService.updateById(yxCouponOrder), HttpStatus.NO_CONTENT);
    }

    @Log("删除卡券订单表")
    @ApiOperation("删除卡券订单表")
    @PreAuthorize("@el.check('admin','yxCouponOrder:del')")
    @DeleteMapping(value = "/delCouponOrder/{ids}")
    public ResponseEntity<Object> deleteAll(@PathVariable String ids) {
        String[] idsArr = ids.split(",");
        boolean delStatus = false;
        for (String id : idsArr) {
            YxCouponOrder yxCouponOrder = new YxCouponOrder();
            yxCouponOrder.setId(Integer.valueOf(id));
            yxCouponOrder.setDelFlag(1);
            yxCouponOrder.setUpdateUserId(SecurityUtils.getUserId().intValue());
            yxCouponOrder.setUpdateTime(DateTime.now().toTimestamp());
            delStatus = yxCouponOrderService.removeById(id);
        }
        return new ResponseEntity<>(delStatus, HttpStatus.OK);

//        Arrays.asList(ids).forEach(id->{
//            yxCouponOrderService.removeById(id);
//        });
//        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "退款")
    @PostMapping(value = "/yxStoreOrder/refund")
    @PreAuthorize("@el.check('admin','yxCouponOrder:refund')")
    public ResponseEntity refund(@Validated @RequestBody YxCouponOrderDto resources) {
        yxCouponOrderService.refund(resources);

        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("核销卡券")
    @ApiOperation("B端：核销卡券")
    @GetMapping(value = "/useCoupon/{verifyCode}")
    public ResponseEntity<Object> updateCouponOrder(@PathVariable String verifyCode) {
        int uid = SecurityUtils.getUserId().intValue();
        boolean result = this.yxCouponOrderService.updateCouponOrder(Base64Utils.decode(verifyCode), uid);
        Map<String, String> map = new HashMap<>();
        if (result) {
            map.put("status", "0");
            map.put("statusDesc", "核销成功");
        } else {
            map.put("status", "1");
            map.put("statusDesc", "核销失败");
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
