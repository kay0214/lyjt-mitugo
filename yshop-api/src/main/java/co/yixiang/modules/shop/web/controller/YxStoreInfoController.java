package co.yixiang.modules.shop.web.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import co.yixiang.annotation.AnonymousAccess;
import co.yixiang.common.api.ApiResult;
import co.yixiang.common.web.controller.BaseController;
import co.yixiang.common.web.param.IdParam;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.constant.SystemConfigConstants;
import co.yixiang.enums.AppFromEnum;
import co.yixiang.modules.coupons.web.param.YxCouponsQueryParam;
import co.yixiang.modules.coupons.web.vo.YxCouponsQueryVo;
import co.yixiang.modules.shop.entity.YxStoreInfo;
import co.yixiang.modules.shop.service.CreatShareStoreService;
import co.yixiang.modules.shop.service.YxStoreInfoService;
import co.yixiang.modules.shop.service.YxSystemConfigService;
import co.yixiang.modules.shop.web.param.YxStoreInfoQueryParam;
import co.yixiang.modules.shop.web.vo.YxStoreInfoDetailQueryVo;
import co.yixiang.modules.shop.web.vo.YxStoreInfoQueryVo;
import co.yixiang.modules.user.entity.YxSystemAttachment;
import co.yixiang.modules.user.service.YxSystemAttachmentService;
import co.yixiang.modules.user.service.YxUserService;
import co.yixiang.modules.user.web.vo.YxUserQueryVo;
import co.yixiang.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 店铺表 前端控制器
 * </p>
 *
 * @author hupeng
 * @since 2020-08-14
 */
@Slf4j
@RestController
@RequestMapping("/yxStoreInfo")
@Api(value = "商铺模块", tags = "商城：商铺信息", description = "商铺模块")
public class YxStoreInfoController extends BaseController {

    @Autowired
    private YxStoreInfoService yxStoreInfoService;
    @Autowired
    private YxSystemConfigService systemConfigService;
    @Autowired
    private YxSystemAttachmentService systemAttachmentService;
    @Autowired
    private CreatShareStoreService creatShareStoreService;
    @Autowired
    private YxUserService yxUserService;
    @Value("${file.path}")
    private String path;

    /**
     * 添加店铺表
     */
//    @PostMapping("/add")
//    @ApiOperation(value = "添加YxStoreInfo对象",notes = "添加店铺表",response = ApiResult.class)
    public ApiResult<Boolean> addYxStoreInfo(@Valid @RequestBody YxStoreInfo yxStoreInfo) throws Exception {
        boolean flag = yxStoreInfoService.save(yxStoreInfo);
        return ApiResult.result(flag);
    }

    /**
     * 修改店铺表
     */
    //    @PostMapping("/update")
//    @ApiOperation(value = "修改YxStoreInfo对象",notes = "修改店铺表",response = ApiResult.class)
    public ApiResult<Boolean> updateYxStoreInfo(@Valid @RequestBody YxStoreInfo yxStoreInfo) throws Exception {
        boolean flag = yxStoreInfoService.updateById(yxStoreInfo);
        return ApiResult.result(flag);
    }

    /**
     * 删除店铺表
     */
    //  @PostMapping("/delete")
//    @ApiOperation(value = "删除YxStoreInfo对象",notes = "删除店铺表",response = ApiResult.class)
    public ApiResult<Boolean> deleteYxStoreInfo(@Valid @RequestBody IdParam idParam) throws Exception {
        boolean flag = yxStoreInfoService.removeById(idParam.getId());
        return ApiResult.result(flag);
    }

    /**
     * 获取店铺表
     */
    //    @PostMapping("/info")
//    @ApiOperation(value = "获取YxStoreInfo对象详情",notes = "查看店铺表",response = YxStoreInfoQueryVo.class)
    public ApiResult<YxStoreInfoQueryVo> getYxStoreInfo(@Valid @RequestBody IdParam idParam) {
        YxStoreInfoQueryVo yxStoreInfoQueryVo = yxStoreInfoService.getYxStoreInfoById(idParam.getId());
        return ApiResult.ok(yxStoreInfoQueryVo);
    }

    /**
     * 店铺表分页列表
     */
//     @PostMapping("/getPageList")
//    @ApiOperation(value = "获取YxStoreInfo分页列表",notes = "店铺表分页列表",response = YxStoreInfoQueryVo.class)
    public ApiResult<Paging<YxStoreInfoQueryVo>> getYxStoreInfoPageList(@RequestBody YxStoreInfoQueryParam yxStoreInfoQueryParam) throws Exception {
        Paging<YxStoreInfoQueryVo> paging = yxStoreInfoService.getYxStoreInfoPageList(yxStoreInfoQueryParam);
        return ApiResult.ok(paging);
    }

    @AnonymousAccess
    @PostMapping("/getStoreInfoList")
    @ApiOperation(value = "查询店铺列表信息", notes = "查询店铺列表信息")
    public ApiResult<List<YxStoreInfoQueryVo>> productsAndStore(@RequestBody YxStoreInfoQueryParam yxStoreInfoQueryParam) {
        List<YxStoreInfoQueryVo> yxStoreInfoQueryVoList = yxStoreInfoService.getStoreInfoList(yxStoreInfoQueryParam);
        return ApiResult.ok(yxStoreInfoQueryVoList);
    }

    /**
     * 获取店铺表
     */
//    @AnonymousAccess
    @GetMapping("/getStoreDetail/{storeId}")
    @ApiOperation(value = "获取商品详情", notes = "获取商品详情")
    public ApiResult<YxStoreInfoDetailQueryVo> getStoreDetail(@PathVariable Integer storeId) {
        YxStoreInfoDetailQueryVo yxStoreInfoQueryVo = yxStoreInfoService.getStoreDetailInfoById(storeId);
        return ApiResult.ok(yxStoreInfoQueryVo);
    }

    /**
     * 商品详情海报
     */
    @GetMapping("/store/poster/{id}")
    @ApiOperation(value = "商铺详情海报", notes = "商铺详情海报")
    public ApiResult<String> prodoctPoster(@PathVariable Integer id) throws IOException, FontFormatException {
        int uid = SecurityUtils.getUserId().intValue();
        // 海报
        String siteUrl = systemConfigService.getData(SystemConfigConstants.SITE_URL);
        if (StrUtil.isEmpty(siteUrl)) {
            return ApiResult.fail("未配置h5地址");
        }
        String apiUrl = systemConfigService.getData(SystemConfigConstants.API_URL);
        if (StrUtil.isEmpty(apiUrl)) {
            return ApiResult.fail("未配置api地址");
        }
        YxUserQueryVo userInfo = yxUserService.getYxUserById(uid);
        String userType = userInfo.getUserType();
        if (!userType.equals(AppFromEnum.ROUNTINE.getValue())) {
            userType = AppFromEnum.H5.getValue();
        }
        String name = uid + "_" + id + "_store_" + userType + "_product_detail_wap.jpg";
        YxSystemAttachment attachment = systemAttachmentService.getInfo(name);
        String fileDir = path + "qrcode" + File.separator;
        String qrcodeUrl = "";
        if (ObjectUtil.isNull(attachment)) {
            File file = FileUtil.mkdir(new File(fileDir));
            //如果类型是小程序
            if (userType.equals(AppFromEnum.ROUNTINE.getValue())) {
                //小程序地址
                siteUrl = siteUrl + "/shop/";
                //生成二维码
                QrCodeUtil.generate(siteUrl + "?productId=" + id + "&spread=" + uid + "&codeType=" + AppFromEnum.ROUNTINE.getValue(), 122, 122,
                        FileUtil.file(fileDir + name));
            } else if (userType.equals(AppFromEnum.APP.getValue())) {
                //h5地址
                siteUrl = siteUrl + "/shop/";
                //生成二维码
                QrCodeUtil.generate(siteUrl + "?productId=" + id + "&spread=" + uid + "&codeType=" + AppFromEnum.APP.getValue(), 122, 122,
                        FileUtil.file(fileDir + name));
            } else {//如果类型是h5
                //生成二维码
                QrCodeUtil.generate(siteUrl + "/detail/" + id + "?spread=" + uid, 122, 122,
                        FileUtil.file(fileDir + name));
            }
            systemAttachmentService.attachmentAdd(name, String.valueOf(FileUtil.size(file)),
                    fileDir + name, "qrcode/" + name);

            qrcodeUrl = apiUrl + "/file/qrcode/" + name;
        } else {
            qrcodeUrl = apiUrl + "/file/" + attachment.getSattDir();
        }
        String spreadPicName = uid + "_" + id + "_store_" + userType + "_product_user_spread.jpg";
        String spreadPicPath = fileDir + spreadPicName;
        String rr = creatShareStoreService.creatProductPic(id, qrcodeUrl,
                spreadPicName, spreadPicPath, apiUrl);
        return ApiResult.ok(rr);
    }

    @AnonymousAccess
    @PostMapping("/getCouponsList")
    @ApiOperation(value = "根据店铺id获取卡券列表", notes = "获取卡券列表", response = YxCouponsQueryVo.class)
    public ApiResult<Paging<YxCouponsQueryVo>> getYxCouponsPageList(@Valid @RequestBody(required = false) YxCouponsQueryParam yxCouponsQueryParam) {
        return yxStoreInfoService.getYxCouponsPageListByStoreId(yxCouponsQueryParam);
    }
}

