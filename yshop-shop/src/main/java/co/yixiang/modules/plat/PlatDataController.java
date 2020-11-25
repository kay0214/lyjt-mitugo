/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package co.yixiang.modules.plat;

import co.yixiang.constant.SystemConfigConstants;
import co.yixiang.modules.plat.domain.TodayDataDto;
import co.yixiang.modules.shop.domain.User;
import co.yixiang.modules.shop.service.UserService;
import co.yixiang.modules.shop.service.YxStoreOrderService;
import co.yixiang.modules.shop.service.param.ExpressParam;
import co.yixiang.utils.CurrUser;
import co.yixiang.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhangqingqing
 * @version PlatDataController, v0.1 2020/11/24 9:28
 */
@Api(tags = "平台数据统计")
@RestController
@RequestMapping("api/platData")
@Slf4j
public class PlatDataController {

    @Autowired
    private YxStoreOrderService yxStoreOrderService;

    @Autowired
    private UserService userService;

    /**@Valid
     * 根据商品分类统计订单占比
     */
    @GetMapping("/getData")
    @ApiOperation(value = "查询平台数据",notes = "查询平台数据",response = ExpressParam.class)
    public ResponseEntity getData(){
        CurrUser currUser = SecurityUtils.getCurrUser();
        int storeId = 0;
        if(!currUser.getUserRole().equals(SystemConfigConstants.ROLE_ADMIN)
                && !currUser.getUserRole().equals(SystemConfigConstants.ROLE_MANAGE)){
            // 平台运营  和 admin 查询所有的
            User user = userService.getById(currUser.getId());

            storeId = user.getStoreId();
        }

        TodayDataDto orderCountDto  = yxStoreOrderService.getPlatformData(storeId);
        return new ResponseEntity(orderCountDto, HttpStatus.OK);
    }


}
