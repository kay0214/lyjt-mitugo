package co.yixiang.modules.esign.core;

import co.yixiang.modules.esign.exception.DefineException;
import com.timevale.esign.sdk.tech.bean.result.AddSealResult;
import com.timevale.esign.sdk.tech.bean.seal.OrganizeTemplateType;
import com.timevale.esign.sdk.tech.bean.seal.PersonTemplateType;
import com.timevale.esign.sdk.tech.bean.seal.SealColor;
import com.timevale.esign.sdk.tech.service.SealService;
import com.timevale.esign.sdk.tech.v3.client.ServiceClient;
import com.timevale.esign.sdk.tech.v3.service.TemplateSealService;
import lombok.extern.slf4j.Slf4j;

import java.text.MessageFormat;

/**
 * description 印章辅助类
 *
 * @author lsy
 * date 2019年7月1日下午5:49:31
 */
@Slf4j
public class SealHelper {

    private SealService sealService;
    private TemplateSealService templateSealService;
    private ServiceClient serviceClient;

    public SealHelper(ServiceClient serviceClient) {
        this.serviceClient = serviceClient;
        sealService = serviceClient.sealService();
        templateSealService = serviceClient.templateSealService();
    }

    //-----------------------------------共有方法 start-------------------------------------------

    /**
     * description Str创建个人模板印章
     * <p>
     * 为指定个人账户创建模板印章，返回创建后的电子印章图片数据，
     * 请妥善保管，作为用户签署印章使用
     * <p>
     * {@link SealService#addTemplateSeal(String, PersonTemplateType, SealColor)}
     *
     * @param accountId    {@link String}待创建印章的账户标识
     * @param templateType {@link PersonTemplateType}模板类型
     * @param color        {@link SealColor}生成印章的颜色
     * @return sealData
     * {@link String}最终生成的电子印章图片Base64数据
     * @throws DefineException
     * @author lsy
     * @since JDK1.7
     */
    public String addTemplateSeal(String accountId, PersonTemplateType templateType,
                                  SealColor color) throws DefineException {
        AddSealResult sealRst = sealService.addTemplateSeal(accountId, templateType, color);
        return castAddSealRst(sealRst, "个人模板印章");
    }


    /**
     * description 创建企业模板印章
     * <p>
     * 为指定企业账户创建模板印章，返回创建后的电子印章图片数据，
     * 请妥善保管，作为用户签署印章使用
     * <p>
     * {@link SealService#addTemplateSeal(String, String,
     * OrganizeTemplateType, SealColor, String, String)}
     *
     * @param accountId    {@link String}待创建印章的账户标识
     * @param templateType {@link OrganizeTemplateType}模板类型
     * @param color        {@link SealColor}生成印章的颜色
     * @param hText        {@link String}生成印章中横向文内容
     * @param qText        {@link String}生成印章中的下弦文内容
     * @return sealData
     * {@link String}最终生成的电子印章图片Base64数据
     * @throws DefineException
     * @author lsy
     * @since JDK1.7
     */
    public String addTemplateSeal(String accountId, OrganizeTemplateType templateType,
                                  SealColor color, String hText, String qText) throws DefineException {
        AddSealResult sealRst = sealService.addTemplateSeal(accountId, templateType, color, hText, qText);
        return castAddSealRst(sealRst, "企业模板印章");
    }


    /**
     * description 创建个人模板印章（本地）
     * <p>
     * 输入个人模板印章相关信息创建的电子印章图片数据，作为个人用户签署印章使用。
     * 注意：使用本地模板印章接口时需要依赖ext-sdk-fonts.jar
     * <p>
     * {@link TemplateSealService#createPersonalTemplateSeal(PersonTemplateType,
     * String, SealColor)}
     *
     * @param type  {@link String}模板类型
     * @param text  {@link String}待创建印章的内容文本，文本长度2~17字符（双边包含），
     *              当选择SQUARE或者HWXKBORDER时，仅支持2/3/4/7/8/9/14/15/16
     *              字数的印章
     * @param color {@link SealColor}生成的印章颜色
     * @return sealData
     * {@link String}最终生成的电子印章图片Base64数据
     * @throws DefineException
     * @author lsy
     * @since JDK1.7
     */
    public String addTemplateSeal(PersonTemplateType type, String text,
                                  SealColor color) throws DefineException {
        AddSealResult sealRst = templateSealService.createPersonalTemplateSeal(type, text, color);
        return castAddSealRst(sealRst, "个人模板印章（本地）");
    }


    /**
     * description 创建企业模板印章（本地）
     * <p>
     * 输入企业模板印章类型与印章文字创建的电子印章图片数据，作为企业用户签署印章使用。
     * 注意：使用本地模板印章接口时需要依赖ext-sdk-fonts.jar
     * <p>
     * {@link TemplateSealService#createOfficialTemplateSeal(OrganizeTemplateType,
     * String, String, String, SealColor)}
     *
     * @param type      {@link OrganizeTemplateType}模板类型
     * @param roundText {@link String}生成印章中的上弦文
     * @param hText     {@link String}生成印章中的横向文内容
     * @param qText     {@link String}生成印章中的下弦文内容
     * @param color     {@link SealColor}生成印章的颜色
     * @return sealData
     * {@link String}最终生成的电子印章图片Base64数据
     * @throws DefineException
     * @author lsy
     * @since JDK1.7
     */
    public String addTemplateSeal(OrganizeTemplateType type, String roundText,
                                  String hText, String qText, SealColor color) throws DefineException {
        AddSealResult sealRst = templateSealService.createOfficialTemplateSeal(type, roundText,
                hText, qText, color);
        return castAddSealRst(sealRst, "企业模板印章（本地）");
    }


    //-----------------------------------共有方法 end---------------------------------------------

    //-----------------------------------私有方法 start-------------------------------------------

    /**
     * description 封装创建模板印章方法
     *
     * @param sealRst {@link AddSealResult}创建个人/企业模板印章返回的对象
     * @param typeMsg {@link String}个人模板印章/企业模板印章/个人模板印章（本地）/企业模板印章（本地）
     * @return sealData
     * {@link String}最终生成的电子印章图片Base64数据
     * @author lsy
     * @since JDK1.7
     */
    private String castAddSealRst(AddSealResult sealRst, String typeMsg)
            throws DefineException {
        if (sealRst.getErrCode() != 0) {
            throw new DefineException(MessageFormat.format("创建{0}失败：errCode={1},msg={2}",
                    typeMsg, sealRst.getErrCode(), sealRst.getMsg()));
        }
        log.debug("创建{}模板印章成功：sealData={}，可将该sealData保存到贵司数据库以便日后直接使用", typeMsg, sealRst.getSealData());
        return sealRst.getSealData();
    }

    //-----------------------------------私有方法 end---------------------------------------------

    //-----------------------------------getter 、setter 方法 start-------------------------------
    public SealService getSealService() {
        return sealService;
    }

    public void setSealService(SealService sealService) {
        this.sealService = sealService;
    }

    public ServiceClient getServiceClient() {
        return serviceClient;
    }

    public void setServiceClient(ServiceClient serviceClient) {
        this.serviceClient = serviceClient;
    }

    public TemplateSealService getTemplateSealService() {
        return templateSealService;
    }

    public void setTemplateSealService(TemplateSealService templateSealService) {
        this.templateSealService = templateSealService;
    }
    //-----------------------------------getter 、setter 方法 end---------------------------------

}
