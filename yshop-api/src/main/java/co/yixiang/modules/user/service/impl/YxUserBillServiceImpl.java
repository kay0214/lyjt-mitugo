package co.yixiang.modules.user.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import co.yixiang.common.api.ApiResult;
import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.util.WxUtils;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.constant.SystemConfigConstants;
import co.yixiang.enums.AppFromEnum;
import co.yixiang.enums.BillDetailEnum;
import co.yixiang.enums.BillEnum;
import co.yixiang.enums.BillInfoEnum;
import co.yixiang.modules.couponUse.dto.UserBillVo;
import co.yixiang.modules.couponUse.param.UserAccountQueryParam;
import co.yixiang.modules.manage.entity.SystemUser;
import co.yixiang.modules.manage.service.SystemUserService;
import co.yixiang.modules.manage.web.vo.SystemUserQueryVo;
import co.yixiang.modules.order.web.vo.YxStoreOrderQueryVo;
import co.yixiang.modules.shop.service.YxSystemConfigService;
import co.yixiang.modules.user.entity.YxSystemAttachment;
import co.yixiang.modules.user.entity.YxUserBill;
import co.yixiang.modules.user.mapper.YxUserBillMapper;
import co.yixiang.modules.user.mapping.BiillMap;
import co.yixiang.modules.user.service.YxSystemAttachmentService;
import co.yixiang.modules.user.service.YxUserBillService;
import co.yixiang.modules.user.service.YxUserService;
import co.yixiang.modules.user.web.dto.BillDTO;
import co.yixiang.modules.user.web.dto.BillOrderDTO;
import co.yixiang.modules.user.web.dto.BillOrderRecordDTO;
import co.yixiang.modules.user.web.param.YxUserBillQueryParam;
import co.yixiang.modules.user.web.vo.YxUserBillQueryVo;
import co.yixiang.modules.user.web.vo.YxUserQueryVo;
import co.yixiang.tools.domain.QiniuContent;
import co.yixiang.tools.service.QiNiuService;
import co.yixiang.utils.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.*;


/**
 * <p>
 * 用户账单表 服务实现类
 * </p>
 *
 * @author hupeng
 * @since 2020-08-13
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class YxUserBillServiceImpl extends BaseServiceImpl<YxUserBillMapper, YxUserBill> implements YxUserBillService {

    @Autowired
    private YxUserBillMapper yxUserBillMapper;
    @Autowired
    private BiillMap biillMap;
    @Autowired
    private SystemUserService systemUserService;

    @Autowired
    YxSystemAttachmentService systemAttachmentService;

    @Autowired
    YxSystemConfigService systemConfigService;

    @Autowired
    YxUserService yxUserService;

    @Autowired
    QiNiuService qiNiuService;

    @Value("${file.path}")
    private String path;


    @Value("${file.localUrl}")
    private String localUrl;

    /**
     * 签到了多少次
     *
     * @param uid
     * @return
     */
    @Override
    public int cumulativeAttendance(int uid) {
        QueryWrapper<YxUserBill> wrapper = new QueryWrapper<>();
        wrapper.eq("uid", uid).eq("category", "integral")
                .eq("type", "sign").eq("pm", 1);
        return yxUserBillMapper.selectCount(wrapper);
    }

    @Override
    public Map<String, Object> spreadOrder(int uid, int page, int limit) {
        QueryWrapper<YxUserBill> wrapper = new QueryWrapper<>();
        wrapper.in("uid", uid).eq("type", "brokerage")
                .eq("category", "now_money").orderByDesc("time")
                .groupBy("time");
        Page<YxUserBill> pageModel = new Page<>(page, limit);
        List<String> list = yxUserBillMapper.getBillOrderList(wrapper, pageModel);


//        QueryWrapper<YxUserBill> wrapperT = new QueryWrapper<>();
//        wrapperT.in("uid",uid).eq("type","brokerage")
//                .eq("category","now_money");

        int count = yxUserBillMapper.selectCount(Wrappers.<YxUserBill>lambdaQuery()
                .eq(YxUserBill::getUid, uid)
                .eq(YxUserBill::getType, BillDetailEnum.TYPE_2.getValue())
                .eq(YxUserBill::getCategory, BillDetailEnum.CATEGORY_1.getValue()));
        List<BillOrderDTO> listT = new ArrayList<>();
        for (String str : list) {
            BillOrderDTO billOrderDTO = new BillOrderDTO();
            List<BillOrderRecordDTO> orderRecordDTOS = yxUserBillMapper
                    .getBillOrderRList(str, uid);
            billOrderDTO.setChild(orderRecordDTOS);
            billOrderDTO.setCount(orderRecordDTOS.size());
            billOrderDTO.setTime(str);

            listT.add(billOrderDTO);
        }

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("list", listT);
        map.put("count", count);

        return map;
    }

    @Override
    public List<BillDTO> getUserBillList(int page, int limit, int uid, int type) {
        QueryWrapper<YxUserBill> wrapper = new QueryWrapper<>();
        wrapper.eq("uid", uid).orderByDesc("add_time").groupBy("time");
        switch (BillInfoEnum.toType(type)) {
            case PAY_PRODUCT:
                wrapper.eq("category", "now_money");
                wrapper.eq("type", "pay_product");
                break;
            case RECHAREGE:
                wrapper.eq("category", "now_money");
                wrapper.eq("type", "recharge");
                break;
            case BROKERAGE:
                wrapper.eq("category", "now_money");
                wrapper.eq("type", "brokerage");
                break;
            case EXTRACT:
                wrapper.eq("category", "now_money");
                wrapper.eq("type", "extract");
                break;
            case SIGN_INTEGRAL:
                wrapper.eq("category", "integral");
                wrapper.eq("type", "sign");
                break;
            default:
                wrapper.eq("category", "now_money");
                String str = "recharge,brokerage,pay_product,system_add,pay_product_refund,system_sub";
                wrapper.in("type", Arrays.asList(str.split(",")));

        }
        wrapper.eq("user_type", 3);
        Page<YxUserBill> pageModel = new Page<>(page, limit);
        List<BillDTO> billDTOList = yxUserBillMapper.getBillList(wrapper, pageModel);
        for (BillDTO billDTO : billDTOList) {
            QueryWrapper<YxUserBill> wrapperT = new QueryWrapper<>();
            wrapperT.in("id", Arrays.asList(billDTO.getIds().split(","))).orderByDesc("add_time");
            billDTO.setList(yxUserBillMapper.getUserBillList(wrapperT));

        }

        return billDTOList;
    }

    @Override
    public double getBrokerage(int uid) {
        return yxUserBillMapper.sumPrice(uid);
    }

    @Override
    public BigDecimal yesterdayCommissionSum(int uid) {
        return yxUserBillMapper.sumYesterdayPrice(uid);
    }

    @Override
    public BigDecimal todayCommissionSum(int uid) {
        return yxUserBillMapper.todayCommissionSum(uid);
    }

    @Override
    public BigDecimal totalCommissionSum(int uid) {
        return yxUserBillMapper.totalCommissionSum(uid);
    }

    @Override
    public List<YxUserBillQueryVo> userBillList(int uid, int page,
                                                int limit, String category) {
        QueryWrapper<YxUserBill> wrapper = new QueryWrapper<>();
        wrapper.eq("status", 1).eq("uid", uid)
                .eq("category", category).orderByDesc("add_time");
        Page<YxUserBill> pageModel = new Page<>(page, limit);

        IPage<YxUserBill> pageList = yxUserBillMapper.selectPage(pageModel, wrapper);
        return biillMap.toDto(pageList.getRecords());
    }

    @Override
    public YxUserBillQueryVo getYxUserBillById(Serializable id) throws Exception {
        return yxUserBillMapper.getYxUserBillById(id);
    }

    @Override
    public Paging<YxUserBillQueryVo> getYxUserBillPageList(YxUserBillQueryParam yxUserBillQueryParam) throws Exception {
        Page page = setPageParam(yxUserBillQueryParam, OrderItem.desc("add_time"));
        IPage<YxUserBillQueryVo> iPage = yxUserBillMapper.getYxUserBillPageList(page, yxUserBillQueryParam);
        return new Paging(iPage);
    }

    /**
     * 确认收货保存商户资金和可用资金
     *
     * @param order
     */
    @Override
    @Transactional
    public void saveMerchantsBill(YxStoreOrderQueryVo order) {
        BigDecimal bigAmount = BigDecimal.ZERO;
        BigDecimal bigTotle = BigDecimal.ZERO;
        BigDecimal bigMerPrice = BigDecimal.ZERO;

        //获取订单佣金
        List<String> cartIds = Arrays.asList(order.getCartId().split(","));
        BigDecimal bigDecimalComm = yxUserBillMapper.getSumCommission(cartIds);
        // 商户收入=支付金额-佣金 小于0 的情况给个0
        if (order.getPayPrice().compareTo(bigDecimalComm) > 0) {
            bigMerPrice = order.getPayPrice().subtract(bigDecimalComm);
        }

        //更新user表的可提现金额
        //订单支付金额
        SystemUserQueryVo systemUserQueryVo = systemUserService.getUserById(order.getMerId());
        bigAmount = bigMerPrice.add(systemUserQueryVo.getWithdrawalAmount());
        bigTotle = bigMerPrice.add(systemUserQueryVo.getTotalAmount());
        SystemUser systemUser = systemUserService.getById(order.getMerId());
        systemUser.setWithdrawalAmount(bigAmount);
        systemUser.setTotalAmount(bigTotle);
        systemUserService.updateById(systemUser);
        //插入资金明细
        YxUserBill userBill = new YxUserBill();
        userBill.setUid(order.getMerId());
        userBill.setLinkId(order.getOrderId());
        userBill.setPm(BillEnum.PM_1.getValue());
        userBill.setTitle("小程序商品购买");
        userBill.setCategory(BillDetailEnum.CATEGORY_1.getValue());
        userBill.setType(BillDetailEnum.TYPE_3.getValue());
        userBill.setNumber(bigMerPrice);
        userBill.setUsername(systemUserQueryVo.getNickName());
        userBill.setBalance(order.getPayPrice());
        userBill.setMerId(order.getMerId());
        userBill.setMark("小程序购买商品，订单确认收货，商户收入");
        userBill.setAddTime(OrderUtil.getSecondTimestampTwo());
        userBill.setStatus(BillEnum.STATUS_1.getValue());
        //商户
        userBill.setUserType(1);
        yxUserBillMapper.insert(userBill);
    }

    /**
     * 查询商户的线下交易流水列表
     *
     * @param param
     * @return
     */
    @Override
    public Paging<UserBillVo> getYxUserAccountPageList(UserAccountQueryParam param, Long userId) {

        QueryWrapper<YxUserBill> wrapper = new QueryWrapper<>();
        wrapper.eq("status", 1).eq("uid", userId)
                .eq("type", BillDetailEnum.TYPE_10.getValue())
                .eq("user_type", 1)
                .eq("category", BillDetailEnum.CATEGORY_1.getValue())
                .orderByDesc("id");

        Page<YxUserBill> pageModel = new Page<>(param.getPage(), param.getLimit());

        IPage<YxUserBill> pageList = yxUserBillMapper.selectPage(pageModel, wrapper);
        return getResultList(pageList);
    }

    @Override
    public Paging<UserBillVo> getUserProductAccountList(UserAccountQueryParam param, Long id) {
        QueryWrapper<YxUserBill> wrapper = new QueryWrapper<>();
        wrapper.eq("status", 1).eq("uid", id)
                .in("type", BillDetailEnum.TYPE_3.getValue(), BillDetailEnum.TYPE_8.getValue())
                .eq("user_type", 1).eq("category", BillDetailEnum.CATEGORY_1.getValue())
                .orderByDesc("id");

        Page<YxUserBill> pageModel = new Page<>(param.getPage(), param.getLimit());

        IPage<YxUserBill> pageList = yxUserBillMapper.selectPage(pageModel, wrapper);


        return getResultList(pageList);
    }

    /**
     * 查询线下支付的数据统计
     *
     * @return
     */
    @Override
    public Map<String, Object> getTodayOffPayData() {
        return yxUserBillMapper.getTodayOffPayData();
    }

    /**
     * 线上支付信息
     *
     * @return
     */
    @Override
    public Map<String, Object> getOnlinePayData() {
        return yxUserBillMapper.getOnlinePayData();
    }

    @Override
    public ApiResult spreadBanner() throws IOException{

        int uid = SecurityUtils.getUserId().intValue();
        YxUserQueryVo userInfo = yxUserService.getYxUserById(uid);

        String apiUrl = systemConfigService.getData(SystemConfigConstants.API_URL);
        if (StrUtil.isEmpty(apiUrl)) {
            return ApiResult.fail("未配置api地址");
        }
        String siteUrl = systemConfigService.getData(SystemConfigConstants.SITE_URL);
        if (StrUtil.isEmpty(siteUrl)) {
            return ApiResult.fail("未配置h5地址");
        }

        String userType = userInfo.getUserType();
        if (!userType.equals(AppFromEnum.ROUNTINE.getValue())) {
            userType = AppFromEnum.H5.getValue();
        }
        String spreadPicName = uid + "_" + userType + "_user_spread.png";
        YxSystemAttachment attachmentT = systemAttachmentService.getInfo(spreadPicName);

        //不再重复生成
        String spreadUrl;
        if (ObjectUtil.isNotNull(attachmentT)) {
            spreadUrl = attachmentT.getImageType().equals(2) ? attachmentT.getSattDir() : apiUrl + "/file/" + attachmentT.getSattDir();
            ApiResult.ok(getResult(spreadUrl));
        }
        String fileDir = path + "qrcode" + File.separator;
        //获取小程序码连接
        String qrCodeUrl = getQrCode(fileDir, userType, siteUrl, apiUrl, uid);
        String spreadPicPath = fileDir + spreadPicName;
        //读取二维码图片
        BufferedImage qrCode = null;
        try {
            qrCode = ImageIO.read(new URL(qrCodeUrl));
        } catch (IOException e) {
            log.error("二维码图片读取失败", e);
            e.printStackTrace();
        }

        //创建图片
        BufferedImage img = new BufferedImage(672, 1354, BufferedImage.TYPE_INT_ARGB);
        //开启画图
        Graphics2D g = img.createGraphics();
        //背景 -- 读取互联网图片
        InputStream stream = getClass().getClassLoader().getResourceAsStream("af-background2.png");
        ImageInputStream background = ImageIO.createImageInputStream(stream);
        BufferedImage back = ImageIO.read(background);

        g.drawImage(back.getScaledInstance(672, 1354, Image.SCALE_DEFAULT), 0, 0, null); // 绘制缩小后的图

        //banner图
        InputStream fxStream = getClass().getClassLoader().getResourceAsStream("af-fx.png");
        ImageInputStream fxInput = ImageIO.createImageInputStream(fxStream);
        BufferedImage fx = ImageIO.read(fxInput);
        g.drawImage(fx.getScaledInstance(672, 1059, Image.SCALE_DEFAULT), 0, 0, null);

        InputStream streamT = getClass().getClassLoader()
                .getResourceAsStream("Alibaba-PuHuiTi-Regular.otf");
        File newFileT = new File("Alibaba-PuHuiTi-Regular.otf");
        FileUtils.copyInputStreamToFile(streamT, newFileT);

        // 小程序码
        g.drawImage(qrCode.getScaledInstance(228, 228, Image.SCALE_DEFAULT), 65, 1098, null);

        //读取用户头像(圆形)
        BufferedImage avaCode = transferImgForRoundImage(userInfo.getAvatar());
        g.drawImage(avaCode.getScaledInstance(59, 59, Image.SCALE_DEFAULT), 325, 1120, null);

        //二维码字体
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_DEFAULT);
        g.setFont(new Font("ArialMT",Font.PLAIN, 30));
        g.setColor(new Color(29, 29, 29));
        g.drawString(userInfo.getNickname() , 402, 1160);

        //二维码字体
        g.setFont(new Font("ArialMT",Font.PLAIN, 30));
        g.setColor(new Color(89, 89, 89));
        g.drawString("向您推荐奥帆LIFE", 326, 1235);

        g.setFont(new Font("ArialMT",Font.PLAIN, 30));
        g.setColor(new Color(89, 89, 89));
        g.drawString("扫描或长按小程序码", 326, 1279);
        g.dispose();
        //先将画好的海报写到本地
        File file = new File(spreadPicPath);
        try {
            ImageIO.write(img, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (StrUtil.isEmpty(localUrl)) {
            QiniuContent qiniuContent = qiNiuService.uploadPic(file, qiNiuService.find());
            systemAttachmentService.attachmentAdd(spreadPicName, String.valueOf(qiniuContent.getSize()),
                    qiniuContent.getUrl(), qiniuContent.getUrl(), 2);
            spreadUrl = qiniuContent.getUrl();
        } else {
            systemAttachmentService.attachmentAdd(spreadPicName, String.valueOf(FileUtil.size(new File(spreadPicPath))),
                    spreadPicPath, "qrcode/" + spreadPicName);
            spreadUrl = apiUrl + "/file/qrcode/" + spreadPicName;
        }
        return ApiResult.ok(getResult(spreadUrl));
    }


    /**
     * 将图片处理为圆形图片
     * 传入的图片必须是正方形的才会生成圆形 如果是长方形的比例则会变成椭圆的
     *
     * @param url
     * @return
     */
    public BufferedImage transferImgForRoundImage(String url){
        BufferedImage resultImg = null;
        try {
            if (StringUtils.isBlank(url)) {
                return null;
            }
            BufferedImage buffImg1 = ImageIO.read(new URL(url));
            resultImg = new BufferedImage(buffImg1.getWidth(), buffImg1.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D g = resultImg.createGraphics();
            Ellipse2D.Double shape = new Ellipse2D.Double(0, 0, buffImg1.getWidth(), buffImg1.getHeight());
            // 使用 setRenderingHint 设置抗锯齿
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            resultImg = g.getDeviceConfiguration().createCompatibleImage(buffImg1.getWidth(), buffImg1.getHeight(),
                    Transparency.TRANSLUCENT);
            g = resultImg.createGraphics();
            // 使用 setRenderingHint 设置抗锯齿
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setClip(shape);
            g.drawImage(buffImg1, 0, 0, null);
            g.dispose();
        } catch (MalformedURLException e) {
            log.error("URL格式异常" + e.getMessage(), e);
        } catch (IOException e) {
            log.error("读取图片异常" + e.getMessage(), e);
        }
        return resultImg;
    }

    /**
     * 返回结果处理
     * @param spreadUrl
     * @return
     */
    private List getResult(String spreadUrl) {
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", 1);
        map.put("pic", "");
        map.put("title", "分享海报");
        map.put("wap_poster", spreadUrl);
        list.add(map);
        return list;
    }

    private Paging<UserBillVo> getResultList(IPage<YxUserBill> result) {
        Paging<UserBillVo> resultStr = new Paging<UserBillVo>();
        resultStr.setSum(result.getTotal() + "");
        resultStr.setTotal(result.getTotal());
        if (result.getRecords() != null) {
            List<UserBillVo> list = new ArrayList<>();
            for (YxUserBill item : result.getRecords()) {
                UserBillVo userBillVo = CommonsUtils.convertBean(item, UserBillVo.class);
                userBillVo.setAddTimeStr(DateUtils.timestampToStr10(item.getAddTime(), DateUtils.YYYY_MM_DD_HH_MM_SS));
                list.add(userBillVo);
            }
            resultStr.setRecords(list);
        }
        return resultStr;
    }


    /**
     * 获取小程序码连接
     *
     * @param fileDir
     * @param userType
     * @param siteUrl
     * @param apiUrl
     * @param uid
     * @return
     */
    public String getQrCode(String fileDir, String userType, String siteUrl, String apiUrl, int uid) {
        String name = uid + "_" + userType + "_user_wap.jpg";
        YxSystemAttachment attachment = systemAttachmentService.getInfo(name);

        String qrCodeUrl;
        if (ObjectUtil.isNull(attachment)) {
            File file = new File(fileDir + name);
            String appId = WxUtils.getAppId();
            String secret = WxUtils.getSecret();
            String accessToken = WxUtils.getAccessToken(appId,secret);
            WxUtils.getQrCode(accessToken,fileDir + name,"uid=" + uid+"&type=spread");
            if (StrUtil.isEmpty(localUrl)) {
                QiniuContent qiniuContent = qiNiuService.uploadPic(file, qiNiuService.find());
                systemAttachmentService.attachmentAdd(name, String.valueOf(qiniuContent.getSize()),
                        qiniuContent.getUrl(), qiniuContent.getUrl(), 2);
                qrCodeUrl = qiniuContent.getUrl();
            } else {
                systemAttachmentService.attachmentAdd(name, String.valueOf(FileUtil.size(file)),
                        fileDir + name, "qrcode/" + name);
                qrCodeUrl = apiUrl + "/file/qrcode/" + name;
            }

        } else {
            qrCodeUrl = attachment.getImageType().equals(2) ? attachment.getSattDir() : apiUrl + "/file/" + attachment.getSattDir();
        }

        return qrCodeUrl;
    }

}
