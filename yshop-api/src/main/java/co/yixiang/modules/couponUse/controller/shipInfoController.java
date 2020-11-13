package co.yixiang.modules.couponUse.controller;

import co.yixiang.annotation.AnonymousAccess;
import co.yixiang.common.web.controller.BaseController;
import co.yixiang.modules.couponUse.param.ShipInfoChangeParam;
import co.yixiang.modules.couponUse.param.ShipInfoQueryParam;
import co.yixiang.modules.manage.entity.SystemUser;
import co.yixiang.modules.ship.entity.YxShipInfo;
import co.yixiang.modules.ship.service.YxShipInfoService;
import co.yixiang.modules.ship.web.vo.YxShipInfoQueryVo;
import co.yixiang.modules.user.service.YxUserService;
import co.yixiang.utils.DateUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 船只表 前端控制器
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
@Slf4j
@RestController
@RequestMapping("/shipInfo")
@Api(value = "核销端：船只信息", tags = "核销端：船只信息")
public class shipInfoController extends BaseController {

    @Autowired
    private YxShipInfoService yxShipInfoService;
    @Autowired
    private YxUserService yxUserService;

    @AnonymousAccess
    @PostMapping("/getShipInfoList")
    @ApiOperation(value = "船只列表",notes = "船只列表",response = YxShipInfoQueryVo.class)
    public ResponseEntity<Object> getYxShipInfoPageList(@Valid @RequestBody(required = false) ShipInfoQueryParam shipInfoQueryParam,@RequestHeader(value = "token") String token) {
//        SystemUser user = getRedisUser(token);
        SystemUser user = yxUserService.getSystemUserByParam(148);
        Map<String, Object> map = yxShipInfoService.getShipInfoList(shipInfoQueryParam, user);
        return ResponseEntity.ok(map);

    }

    /**
     * 修改船只状态
     * @param shipInfoChangeParam
     * @param token
     * @return
     */
    @AnonymousAccess
    @PostMapping("/changeShipStatus")
    @ApiOperation(value = "船只（回港、维修中）",notes = "船只（回港、维修中）",response = YxShipInfoQueryVo.class)
    public ResponseEntity<Object> changeShipStatus(@Valid @RequestBody ShipInfoChangeParam shipInfoChangeParam, @RequestHeader(value = "token") String token) {
//        SystemUser user = getRedisUser(token);
        SystemUser user = yxUserService.getSystemUserByParam(148);
        Map<String, Object> map = new HashMap<>();
        YxShipInfo shipInfo = yxShipInfoService.getById(shipInfoChangeParam.getShipId());
        shipInfo.setCurrentStatus(shipInfoChangeParam.getCurrentStatus());
        if(0==shipInfoChangeParam.getCurrentStatus()){
            shipInfo.setLastReturnTime(DateUtils.getNowTime());
        }
        shipInfo.setUpdateTime(new Date());
        shipInfo.setUpdateUserId(user.getId().intValue());
        yxShipInfoService.updateById(shipInfo);
        map.put("status", "1");
        map.put("statusDesc", "成功！");
        return ResponseEntity.ok(map);
    }
    @AnonymousAccess
    @PostMapping("/captainOutPort")
    @ApiOperation(value = "船长出港",notes = "船长出港",response = YxShipInfoQueryVo.class)
    public ResponseEntity<Object> captainOutPort(@RequestHeader(value = "token") String token) {
//        SystemUser user = getRedisUser(token);
        SystemUser user = yxUserService.getSystemUserByParam(148);
        Map<String, Object> map = new HashMap<>();

        map.put("status", "1");
        map.put("statusDesc", "成功！");
        return ResponseEntity.ok(map);
    }
}

