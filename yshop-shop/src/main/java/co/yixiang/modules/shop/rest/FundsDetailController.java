package co.yixiang.modules.shop.rest;

import co.yixiang.logging.aop.log.Log;
import co.yixiang.modules.shop.service.YxFundsDetailService;
import co.yixiang.modules.shop.service.dto.YxFundsDetailQueryCriteria;
import co.yixiang.utils.DateUtils;
import co.yixiang.utils.StringUtils;
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

/**
* @author huiy
* @date 2020-08-19
*/
@AllArgsConstructor
@Api(tags = "平台资金明细管理")
@RestController
@RequestMapping("/api/fundsDetail")
public class FundsDetailController {

    private final YxFundsDetailService fundsDetailService;

    @GetMapping
    @Log("查询平台资金明细")
    @ApiOperation("查询平台资金明细")
    @PreAuthorize("@el.check('admin','fundsDetail:list')")
    public ResponseEntity<Object> getFundsDetails(YxFundsDetailQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(fundsDetailService.queryAll(criteria,pageable),HttpStatus.OK);
    }

}
