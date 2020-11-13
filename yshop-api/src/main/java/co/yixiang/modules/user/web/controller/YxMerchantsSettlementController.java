package co.yixiang.modules.user.web.controller;

import cn.hutool.core.date.DateTime;
import co.yixiang.common.api.ApiResult;
import co.yixiang.common.web.controller.BaseController;
import co.yixiang.exception.BadRequestException;
import co.yixiang.modules.user.entity.YxMerchantsSettlement;
import co.yixiang.modules.user.service.YxMerchantsSettlementService;
import co.yixiang.utils.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 商家入驻表 前端控制器
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
@Slf4j
@RestController
@RequestMapping("/yxMerchantsSettlement")
@Api("商家入驻表 API")
public class YxMerchantsSettlementController extends BaseController {

    @Autowired
    private YxMerchantsSettlementService yxMerchantsSettlementService;

    /**
     * 添加商家入驻表
     */
    @PostMapping("/add")
    @ApiOperation(value = "添加YxMerchantsSettlement对象", notes = "添加商家入驻表", response = ApiResult.class)
    public ApiResult<Boolean> addYxMerchantsSettlement(@Valid @RequestBody YxMerchantsSettlement yxMerchantsSettlement) throws Exception {
        // 判断当前公司是否在确认中
        List<YxMerchantsSettlement> findList = this.yxMerchantsSettlementService.list(new QueryWrapper<YxMerchantsSettlement>().lambda()
                .eq(YxMerchantsSettlement::getCompanyName, yxMerchantsSettlement.getCompanyName())
                .eq(YxMerchantsSettlement::getContactsName, yxMerchantsSettlement.getCompanyName())
                .eq(YxMerchantsSettlement::getStatus, 0));
        if (null != findList && findList.size() > 0) {
            throw new BadRequestException("当前公司已在确认中");
        }

        int uid = SecurityUtils.getUserId().intValue();
        yxMerchantsSettlement.setCreateUserId(uid);
        yxMerchantsSettlement.setCreateTime(DateTime.now().toTimestamp());
        boolean flag = yxMerchantsSettlementService.save(yxMerchantsSettlement);
        return ApiResult.result(flag);
    }
}

