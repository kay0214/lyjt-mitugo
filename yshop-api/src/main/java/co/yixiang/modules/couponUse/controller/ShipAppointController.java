package co.yixiang.modules.couponUse.controller;

import co.yixiang.annotation.AnonymousAccess;
import co.yixiang.common.web.controller.BaseController;
import co.yixiang.modules.couponUse.dto.YxShipAppointResultVo;
import co.yixiang.modules.couponUse.param.ShipInAppotionDaysParam;
import co.yixiang.modules.couponUse.param.ShipInAppotionParam;
import co.yixiang.modules.manage.entity.SystemUser;
import co.yixiang.modules.ship.service.YxShipAppointService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 船只预约表 前端控制器
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
@Slf4j
@RestController
@RequestMapping("/shipAppoint")
@Api(value = "核销端：预约管理", tags = "核销端：预约管理")
public class ShipAppointController extends BaseController {

    @Autowired
    private YxShipAppointService yxShipAppointService;

    @AnonymousAccess
    @PostMapping("/getMonthAllDays")
    @ApiOperation(value = "预约管理-获取日历",notes = "预约管理-获取日历")
    public ResponseEntity<Object> getYxShipInfoPageList(@Valid @RequestBody ShipInAppotionDaysParam param, @RequestHeader(value = "token") String token) {
        SystemUser user = getRedisUser(token);
        Map<String, Object> map = new HashMap<>();
        String strDate = param.getDate();
        List<String> dateList = yxShipAppointService.getMonthAllDays(strDate);
        List<YxShipAppointResultVo> resultVoList = yxShipAppointService.getAppointByDate(dateList,user.getStoreId(),param.getShipId());
        map.put("status", "1");
        map.put("statusDesc", "成功");
        map.put("data", resultVoList);
        return ResponseEntity.ok(map);
    }
    @AnonymousAccess
    @PostMapping("/addAppotionInfo")
    @ApiOperation(value = "添加申请",notes = "添加申请")
    public ResponseEntity<Object> addAppotionInfo(@Valid @RequestBody ShipInAppotionParam param, @RequestHeader(value = "token") String token) {
        SystemUser user = getRedisUser(token);
        Map<String, Object> map = yxShipAppointService.saveAppotionInfo(param,user);
        return ResponseEntity.ok(map);
    }

    @AnonymousAccess
    @PostMapping("/getAppotionByDate")
    @ApiOperation(value = "获取对应日期预约详情",notes = "获取对应日期预约详情")
    public ResponseEntity<Object> getAppotionByDate(@Valid @RequestBody ShipInAppotionDaysParam param, @RequestHeader(value = "token") String token) {
        SystemUser user = getRedisUser(token);
      /*  Map<String, Object> map = yxShipAppointService.saveAppotionInfo(param,user);
        return ResponseEntity.ok(map);*/
        user.setStoreId(63);
       Map<String, Object> map = yxShipAppointService.getAppotionByDate(param,user);

        return ResponseEntity.ok(map);
    }

}

