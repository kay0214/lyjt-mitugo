/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package co.yixiang.modules.esign.util;

import co.yixiang.modules.esign.constant.IdentificationConstants;
import co.yixiang.modules.esign.core.*;
import co.yixiang.modules.esign.exception.DefineException;
import co.yixiang.modules.esign.vo.SignPDFCommonVO;
import com.hyjf.framework.common.util.SpringUtils;
import com.timevale.esign.sdk.tech.bean.*;
import com.timevale.esign.sdk.tech.bean.result.FileCreateFromTemplateResult;
import com.timevale.esign.sdk.tech.bean.result.FileDigestSignResult;
import com.timevale.esign.sdk.tech.bean.seal.OrganizeTemplateType;
import com.timevale.esign.sdk.tech.bean.seal.PersonTemplateType;
import com.timevale.esign.sdk.tech.bean.seal.SealColor;
import com.timevale.esign.sdk.tech.impl.constants.*;
import com.timevale.esign.sdk.tech.v3.client.ServiceClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author PC-LIUSHOUYI
 * @version WukongUtil, v0.1 2019/12/24 14:23
 */
@Slf4j
public class WukongUtil {

    private static ServiceClient serviceClient;

    private static ServiceClient getServiceClient() {
        if (serviceClient == null) {
            serviceClient = SpringUtils.getBean(ServiceClient.class);
        }
        return serviceClient;
    }

    private static AccountHelper accountHelper;
    private static SealHelper sealHelper;
    private static SignHelper signHelper;
    private static VerifyPDFHelper verifyPDFHelper;
    private static PdfTemplateHelper pdfTemplateHelper;
    private static MobileCodeHelper mobileCodeHelper;

    static {
        //实例化辅助类
        accountHelper = new AccountHelper(getServiceClient());
        sealHelper = new SealHelper(getServiceClient());
        signHelper = new SignHelper(getServiceClient());
        verifyPDFHelper = new VerifyPDFHelper(getServiceClient());
        pdfTemplateHelper = new PdfTemplateHelper(getServiceClient());
        mobileCodeHelper = new MobileCodeHelper(getServiceClient());
    }


    /**
     * 创建个人客户账户
     *
     * @param personBean 必传字段 Name IdNo PersonArea默认大陆LegalAreaType.MAINLAND
     * @return
     */
    public static String addPersonalAcct(PersonBean personBean) {
        // 参数校验
        if (StringUtils.isBlank(personBean.getIdNo()) || StringUtils.isBlank(personBean.getName())) {
            log.error("创建个人客户账户参数不全！");
            return null;
        }
        // 身份证类型默认为大陆身份证
        personBean.setPersonArea(LegalAreaType.MAINLAND);
        try {
            // 个人客户账户AccountId
            return accountHelper.addAccount(personBean);
        } catch (DefineException e) {
            log.error("用户：" + personBean.getName() + "，身份证号：" + personBean.getIdNo() + "注册账户失败！" + e.getE());
            return null;
        }
    }

    /**
     * 更新用户信息
     *
     * @param accountId
     * @param updatePersonBean
     * @return
     */
    public static boolean updatePersonalAcct(String accountId, UpdatePersonBean updatePersonBean) {
        // 参数校验
        if (StringUtils.isBlank(accountId) || null == updatePersonBean) {
            log.error("更新个人客户账户参数不全！");
            return false;
        }
        List<DeleteParamType> list = new ArrayList<>();
        try {
            accountHelper.updateAcct(accountId, updatePersonBean, list);
            return true;
        } catch (DefineException e) {
            log.error("个人账户id" + accountId + "账户信息更新失败！" + e.getE());
            return false;
        }
    }

    /**
     * 创建个人印章
     *
     * @param accountId
     * @param identification 用户身份标识
     * @return
     * @throws DefineException
     */
    public static String addPersonTemplateSeal(String accountId, String identification) {
        // 参数校验
        if (StringUtils.isBlank(accountId)) {
            log.error("创建个人印章参数不全！");
            return null;
        }
        // 印章模板类型,可选SQUARE-正方形印章 | RECTANGLE-矩形印章 | BORDERLESS-无框矩形印章
        PersonTemplateType personTemplateType = null;
        if (identification.equals(IdentificationConstants.LEGALPERSON)) {
            personTemplateType = PersonTemplateType.SQUARE;
        } else {
            personTemplateType = PersonTemplateType.BORDERLESS;
        }
        // 印章颜色：RED-红色 | BLUE-蓝色 | BLACK-黑色
        SealColor sealColor = SealColor.RED;
        try {
            // 个人模板印章SealData
            return sealHelper.addTemplateSeal(accountId, personTemplateType, sealColor);
        } catch (DefineException e) {
            log.error("用户编号：" + accountId + "创建印章失败！" + e.getE());
            return null;
        }
    }

    /**
     * 创建企业客户账户
     *
     * @return
     * @throws DefineException name机构名称 organCode社会信用编码 regType默认MERGE，多证合一，传递社会信用代码号
     */
    public static String addOrganizeAcct(OrganizeBean organizeBean) {
        // 参数校验
        if (StringUtils.isBlank(organizeBean.getOrganCode()) || StringUtils.isBlank(organizeBean.getName())) {
            log.error("创建企业客户账户参数不全！");
            return null;
        }
        // 类型：社会信用代码号
        organizeBean.setRegType(OrganRegType.MERGE);
        try {
            // 企业客户账户AccountId
            return accountHelper.addAccount(organizeBean);
        } catch (DefineException e) {
            log.error("机构名称：" + organizeBean.getName() + "，社会信用编码：" + organizeBean.getOrganCode() + "注册账户失败！" + e.getE());
            return null;
        }
    }

    /**
     * 更新企业客户账户信息
     *
     * @param accountId
     * @param organize
     * @return
     */
    public static boolean updateOrganizeAcct(String accountId, UpdateOrganizeBean organize) {
        // 参数校验
        if (StringUtils.isBlank(accountId) || null == organize) {
            log.error("【悟空接口调用】更新企业客户账户信息参数不全！");
            return false;
        }
        List<DeleteParamType> list = new ArrayList<>();
        try {
            accountHelper.updateAcct(accountId, organize, list);
            return true;
        } catch (DefineException e) {
            log.error("【悟空接口调用】企业账户id" + accountId + "账户信息更新失败！" + e.getE());
            return false;
        }
    }

    /**
     * 查询客户账户信息
     *
     * @param idNo
     * @param type
     * @return
     */
    public static AccountProfile getAccountInfoByIdNo(String idNo, LicenseQueryType type) {
        if (StringUtils.isBlank(idNo) || null == type) {
            log.error("【悟空接口调用】查询客户账户信息参数不全！");
            return null;
        }
        try {
            AccountProfile result = accountHelper.showAccountInfoByIdNo(idNo, type);
            return result;
        } catch (DefineException e) {
            log.error("【悟空接口调用】根据证件号：" + idNo + "查询客户账户信息失败！");
            return null;
        }
    }

    /**
     * 创建企业用章
     *
     * @param accountId
     * @param chapterCode 企业章编码
     * @return
     * @throws DefineException
     */
    public static String addOrganizeTemplateSeal(String accountId, String chapterCode) {

        // 印章模板类型,可选STAR-标准公章 | DEDICATED-圆形无五角星章 | OVAL-椭圆形印章
        OrganizeTemplateType organizeTemplateType = OrganizeTemplateType.STAR;

        // 印章颜色：RED-红色 | BLUE-蓝色 | BLACK-黑色
        SealColor sealColor = SealColor.RED;
//        // hText 生成印章中的横向文内容 如“合同专用章、财务专用章”
//        String hText = "合同专用章";
        // qText 生成印章中的下弦文内容 如公章防伪码（一般为13位数字）
        String qText = chapterCode;
        try {
            // 企业模板印章SealData
            return sealHelper.addTemplateSeal(accountId, organizeTemplateType, sealColor,
                    "", qText);
        } catch (DefineException e) {
            log.error("【悟空接口调用】企业编号：" + accountId + "创建印章失败！" + e.getE());
            return null;
        }
    }

    /**
     * 平台用户PDF摘要签署
     *
     * @param signPDFCommonVO
     * @return
     */
    public static FileDigestSignResult localSignPDF(SignPDFCommonVO signPDFCommonVO) {
        // 参数check
        if (StringUtils.isBlank(signPDFCommonVO.getAccountId()) || StringUtils.isBlank(signPDFCommonVO.getSealData()) || StringUtils.isBlank(signPDFCommonVO.getSrcPdfFile())
                || StringUtils.isBlank(signPDFCommonVO.getDstPdfFile()) || StringUtils.isBlank(signPDFCommonVO.getKey())) {
            log.error("【悟空接口调用】平台用户PDF摘要签署参数校验失败！");
            return null;
        }

        // 平台用户PDF摘要签署
        // 签署文档信息
        SignPDFFileBean file = new SignPDFFileBean();
        // 待签署PDF文档本地路径，含文档名（与bytes至少有一个不为空）
        file.setSrcPdfFile(signPDFCommonVO.getSrcPdfFile());
        // 签署后PDF文档本地路径，含文档名（为空时返回签署后的文件流）
        file.setDstPdfFile(signPDFCommonVO.getDstPdfFile());

        // 是否增加e签宝logo图标
        file.setShowImage(false);

        // 签章位置信息
        PosBean signPos = new PosBean();
        // 定位类型，0-坐标定位，1-关键字定位，默认0，SignType为关键字签署的时候，为1，否则为0。用户可以不作处理。此处只是为了兼容旧版本而保留
        signPos.setPosType(1);
        // 签署位置X坐标，若为关键字定位，相对于关键字的X坐标偏移量，默认0
        signPos.setPosX(signPDFCommonVO.getPosX() != null ? signPDFCommonVO.getPosX() : 0);
        // 签署位置Y坐标，若为关键字定位，相对于关键字的Y坐标偏移量，默认0
        signPos.setPosY(signPDFCommonVO.getPosY() != null ? signPDFCommonVO.getPosY() : 0);
        // 关键字，仅限关键字签章时有效，若为关键字定位时，不可空
        signPos.setKey(signPDFCommonVO.getKey());
//        signPos.setCacellingSign(true);//是否是作废签签署，默认为false;如果签署作废章的话，建议线下也签署一份作废协议，这样法律效力较高
//        signPos.setAddSignTime(true);// 是否显示本地签署时间，需要width设置92以上才可以看到时间
        if (1 == signPDFCommonVO.getIsPerson()) {
            // 个人签章太大、此处固定写死个人签章大小
            signPos.setWidth(80);
        }

        try {
            return signHelper.localSignPDF(signPDFCommonVO.getAccountId(), signPDFCommonVO.getSealData(), file, signPos, SignType.Key);
        } catch (DefineException e) {
            log.error("【悟空接口调用】平台用户PDF摘要签署失败！accountId:" + signPDFCommonVO.getAccountId() + "错误信息：" + e.getE());
            return null;
        }
    }

    /**
     * pdf模板填充
     *
     * @param srcPdfFile
     * @param dstPdfFile
     * @param txtFields
     * @return
     */
    public static FileCreateFromTemplateResult createFileFromTemplate(String srcPdfFile, String dstPdfFile, Map<String, Object> txtFields) {
        try {
            FileCreateFromTemplateResult fileCreateFromTemplateResult = pdfTemplateHelper.createFileFromTemplate(srcPdfFile, dstPdfFile, true, txtFields);
            return fileCreateFromTemplateResult;
        } catch (DefineException e) {
            log.error("【悟空接口调用】PDF摘要填充失败！原filePath:" + srcPdfFile + "错误信息：" + e.getE());
            return null;
        }
    }
}
