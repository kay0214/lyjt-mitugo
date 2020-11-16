package co.yixiang.modules.couponUse.controller;

import co.yixiang.annotation.AnonymousAccess;
import co.yixiang.common.web.controller.BaseController;
import co.yixiang.modules.couponUse.dto.YxShipAppointResultVo;
import co.yixiang.modules.manage.entity.SystemUser;
import co.yixiang.modules.ship.service.YxShipAppointService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
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
    @ApiOperation(value = "获取日期",notes = "获取日期")
    public ResponseEntity<Object> getYxShipInfoPageList(@Valid @RequestBody Map<String,String> mapParam, @RequestHeader(value = "token") String token) {
        SystemUser user = getRedisUser(token);
        Map<String, Object> map = new HashMap<>();
        String strDate = mapParam.get("monthDay");
        List<String> dateList = yxShipAppointService.getMonthAllDays(strDate);
        List<YxShipAppointResultVo> resultVoList = yxShipAppointService.getAppointByDate(dateList,user.getStoreId());
        if(CollectionUtils.isEmpty(resultVoList)){
            map.put("status", "99");
            map.put("statusDesc", "暂无船只系列信息！");
        }else{
            map.put("status", "1");
            map.put("statusDesc", "成功");
            map.put("data", resultVoList);
        }
        return ResponseEntity.ok(map);
    }

}

