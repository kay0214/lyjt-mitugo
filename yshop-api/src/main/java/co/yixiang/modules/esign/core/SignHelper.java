package co.yixiang.modules.esign.core;

import co.yixiang.modules.esign.exception.DefineException;
import com.timevale.esign.sdk.tech.bean.*;
import com.timevale.esign.sdk.tech.bean.result.CodeMultiSignResult;
import com.timevale.esign.sdk.tech.bean.result.FileDigestSignBatchResult;
import com.timevale.esign.sdk.tech.bean.result.FileDigestSignBatchResult.SignInfo;
import com.timevale.esign.sdk.tech.bean.result.FileDigestSignResult;
import com.timevale.esign.sdk.tech.bean.result.Result;
import com.timevale.esign.sdk.tech.impl.constants.SignType;
import com.timevale.esign.sdk.tech.service.EventSignService;
import com.timevale.esign.sdk.tech.service.SelfSignService;
import com.timevale.esign.sdk.tech.service.UserSignService;
import com.timevale.esign.sdk.tech.v3.client.ServiceClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;

import java.text.MessageFormat;
import java.util.List;

/**
 * description 签署辅助类
 *
 * @author lsy
 * date 2019年7月1日下午7:13:07
 */
@Slf4j
public class SignHelper {

    private EventSignService eventSignService;
    private SelfSignService selfSignService;
    private UserSignService userSignService;
    private ServiceClient serviceClient;

    public SignHelper(ServiceClient serviceClient) {
        this.serviceClient = serviceClient;
        this.eventSignService = serviceClient.eventSignService();
        this.selfSignService = serviceClient.selfSignService();
        this.userSignService = serviceClient.userSignService();
    }

    // -----------------------------------共有方法 start-------------------------------------------

    /**
     * description 平台自身PDF摘要签署（印章标识）
     * <p>
     * 使用项目编号绑定的账户完成接入系统本地指定文档的签署
     * <p>
     * {@link SelfSignService#localSignPdf(com.timevale.esign.sdk.tech.bean.AbstractSignPdfBean, PosBean, int, SignType)}
     *
     * @param fileBean {@link SignPDFFileBean}签署PDF文档信息
     * @param signPos  {@link PosBean}签章位置信息
     * @param sealId   签署印章的标识，为0表示用默认印章签署
     * @param signType {@link SignType}签章类型
     * @throws DefineException
     * @author lsy
     * @since JDK1.7
     */
    public void localSignPDF(SignPDFFileBean fileBean, PosBean signPos, int sealId, SignType signType)
            throws DefineException {

        FileDigestSignResult signRst = selfSignService.localSignPdf(fileBean, signPos, sealId, signType);
        castSignRst(signRst, "自身", false);
    }


    /**
     * description 平台自身PDF摘要签署（印章图片）
     * <p>
     * 使用项目编号绑定的账户完成接入系统本地指定文档的签署
     * <p>
     * {@link SelfSignService#localSignPdf(com.timevale.esign.sdk.tech.bean.AbstractSignPdfBean, PosBean, String, SignType)}
     *
     * @param fileBean {@link SignPDFFileBean} 签署PDF文档信息
     * @param signPos  {@link PosBean}签署位置信息
     * @param sealData {@link String}印章图片Base64
     * @param signType {@link SignType}签章类型
     * @throws DefineException
     * @author lsy
     * @since JDK1.7
     */
    public void localSignPDF(SignPDFFileBean fileBean, PosBean signPos, String sealData, SignType signType)
            throws DefineException {

        FileDigestSignResult signRst = selfSignService.localSignPdf(fileBean, signPos, sealData, signType);
        castSignRst(signRst, "自身", false);
    }


    /**
     * description 平台自身PDF单文档批量摘要签署（印章标识）
     * <p>
     * 使用项目编号绑定的账户完成接入系统本地指定文档的多个签名域批量签署。
     * <p>
     * {@link SelfSignService#localBatchSignPdf(com.timevale.esign.sdk.tech.bean.AbstractSignPdfBean, List)}
     *
     * @param fileBean       {@link SignPDFFileBean}签署PDF文档信息
     * @param signatureInfos {@link List} 签章信息集合
     * @throws DefineException
     * @author lsy
     * @since JDK1.7
     */
    public void localBatchSignPDF(SignPDFFileBean fileBean, List<SignSignatureInfo> signatureInfos)
            throws DefineException {

        FileDigestSignBatchResult signRst = selfSignService.localBatchSignPdf(fileBean, signatureInfos);
        castSignRst(signRst, "自身", false);
    }


    /**
     * description 平台自身PDF摘要签署（文件流&印章标识）
     * <p>
     * 使用项目编号绑定的账户完成接入系统本地指定文档的签署
     * <p>
     * {@link SelfSignService#localSignPdf(com.timevale.esign.sdk.tech.bean.AbstractSignPdfBean,
     * PosBean, int, SignType)}
     *
     * @param stream   {@link SignPDFStreamBean} 签署PDF文档信息
     * @param signPos  {@link PosBean} 签章位置信息
     * @param sealId   签署印章的标识，为0表示用默认印章签署
     * @param signType {@link SignType} 签章类型
     * @return {@link FileDigestSignResult} 签署返回结果
     * @throws DefineException
     * @author lsy
     * @since JDK1.7
     */
    public FileDigestSignResult localSignPDF(SignPDFStreamBean stream, PosBean signPos, int sealId, SignType signType)
            throws DefineException {

        FileDigestSignResult signRst = selfSignService.localSignPdf(stream, signPos, sealId, signType);
        castSignRst(signRst, "自身", true);
        return signRst;
    }


    /**
     * description  平台自身PDF摘要签署（文件流&印章图片）
     * <p>
     * 使用项目编号绑定的账户完成接入系统本地指定文档的签署
     * <p>
     * {@link SelfSignService#localSignPdf(com.timevale.esign.sdk.tech.bean.AbstractSignPdfBean,
     * PosBean, String, SignType)}
     *
     * @param stream   {@link SignPDFStreamBean} 签署PDF文档信息
     * @param signPos  {@link PosBean} 签章位置信息
     * @param sealDate {@link String} 印张图片Base64
     * @param signType {@link SignType} 签章类型
     * @throws DefineException
     * @author lsy
     * @since JDK1.7
     */
    public void localSignPDF(SignPDFStreamBean stream, PosBean signPos, String sealDate, SignType signType)
            throws DefineException {

        FileDigestSignResult signRst = selfSignService.localSignPdf(stream, signPos, sealDate, signType);
        castSignRst(signRst, "自身", true);
    }


    /**
     * description 平台自身PDF单文档批量摘要签署（文件流&印章标识）
     * <p>
     * 使用项目编号绑定的账户完成接入系统本地指定文档的多个签名域批量签署
     * <p>
     * {@link SelfSignService#localBatchSignPdf(com.timevale.esign.sdk.tech.bean.AbstractSignPdfBean, List)}
     *
     * @param stream         {@link SignPDFStreamBean} 签署PDF文档信息
     * @param signatureInfos {@link List} 签章信息集合
     * @throws DefineException
     * @author lsy
     * @since JDK1.7
     */
    public void localSignPDF(SignPDFStreamBean stream, List<SignSignatureInfo> signatureInfos)
            throws DefineException {

        FileDigestSignBatchResult signBatchRst = selfSignService.localBatchSignPdf(stream, signatureInfos);
        castSignRst(signBatchRst, "自身", true);
    }


    /**
     * description 平台用户PDF摘要签署
     * <p>
     * 用指定账户的证书对文档进行签署，此签署过程不将文档上传至e签宝平台，只传递文档摘要信息
     * <p>
     * {@link UserSignService#localSignPDF(String, String,
     * com.timevale.esign.sdk.tech.bean.AbstractSignPdfBean, PosBean, SignType)}
     *
     * @param accountId {@link String} 签署者账号标识，以此获取账户的证书进行签署
     * @param sealData  {@link String} 印章图片Base64，若为空最终签署后将没有直观图片展现
     * @param fileBean  {@link SignPDFFileBean} 签署PDF文档信息
     * @param signPos   {@link PosBean} 签章位置信息
     * @param signType  {@link SignType} 签章类型
     * @throws DefineException
     * @author lsy
     * @since JDK1.7
     */
    public FileDigestSignResult localSignPDF(String accountId, String sealData, SignPDFFileBean fileBean,
                                             PosBean signPos, SignType signType) throws DefineException {

        FileDigestSignResult signRst = userSignService
                .localSignPDF(accountId, sealData, fileBean, signPos, signType);
        castSignRst(signRst, "客户", false);
        return signRst;
    }


    /**
     * description 平台用户PDF摘要签署（短信验证）
     * <p>
     * 用指定账户的证书对文档进行签署，此签署过程不将文档上传至e签宝平台，
     * 只传递文档摘要信息。签署过程需要校验用户短信验证码，验证码必须通过e签宝接口发送
     * <p>
     * {@link UserSignService#localSafeSignPDF(String, String,
     * com.timevale.esign.sdk.tech.bean.AbstractSignPdfBean, PosBean, SignType, String)}
     *
     * @param accountId {@link String} 签署者账号标识，以此获取账户的证书进行签署
     * @param sealData  {@link String} 印章图片Base64，若为空最终签署后将没有直观图片展现
     * @param fileBean  {@link SignPDFFileBean} 签署PDF文档信息
     * @param signPos   {@link PosBean} 签章位置信息
     * @param signType  {@link SignType} 签章类型
     * @param code      {@link String} 短信验证码，必须通过e签宝接口发送
     * @throws DefineException
     * @author lsy
     * @since JDK1.7
     */
    public void localSignPDF(String accountId, String sealData, SignPDFFileBean fileBean, PosBean signPos,
                             SignType signType, String code) throws DefineException {

        FileDigestSignResult signRst = userSignService.
                localSafeSignPDF(accountId, sealData, fileBean, signPos, signType, code);
        castSignRst(signRst, "客户", false);
    }


    /**
     * description  平台用户PDF摘要签署（指定手机短信验证）
     * <p>
     * 用指定账户的证书对文档进行签署，此签署过程不将文档上传至e签宝平台，
     * 只传递文档摘要信息。签署过程需要校验用户短信验证码，验证码必须通过e签宝接口发送
     * <p>
     * {@link UserSignService#localSafeSignPDF3rd(String, String,
     * com.timevale.esign.sdk.tech.bean.AbstractSignPdfBean, PosBean, SignType, String, String)}
     *
     * @param accountId {@link String} 签署者账号标识，以此获取账户的证书进行签署
     * @param sealData  {@link String} 印章图片的Base64，若为空最终签署后将没有直观图片展现
     * @param fileBean  {@link SignPDFFileBean} 签署PDF文档信息
     * @param signPos   {@link PosBean} 签章位置信息
     * @param signType  {@link SignType} 签章类型
     * @param mobile    {@link String} 接收短信验证码的手机
     * @param code      {@link String} 短信验证码，必须通过e签宝接口发送
     * @throws DefineException
     * @author lsy
     * @since JDK1.7
     */
    public void localSignPDF(String accountId, String sealData, SignPDFFileBean fileBean, PosBean signPos,
                             SignType signType, String mobile, String code) throws DefineException {

        FileDigestSignResult signRst = userSignService
                .localSafeSignPDF3rd(accountId, sealData, fileBean, signPos, signType, mobile, code);
        castSignRst(signRst, "客户", false);

    }


    /**
     * description 平台用户PDF摘要签署（文件流）
     * <p>
     * 用指定账户的证书对文档进行签署，此签署过程不将文档上传至e签宝平台，只传递文档摘要信息
     * <p>
     * {@link UserSignService#localSignPDF(String, String,
     * com.timevale.esign.sdk.tech.bean.AbstractSignPdfBean, PosBean, SignType)}
     *
     * @param accountId {@link String} 签署者账号标识，以此获取账户的证书进行签署
     * @param sealData  {@link String} 印章图片Base64，若为空最终签署后将没有直观图片展示
     * @param stream    {@link SignPDFStreamBean} 签署PDF文档信息
     * @param sigPos    {@link PosBean} 签章位置信息
     * @param signType  {@link SignType} 签章类型
     * @return {@link FileDigestSignResult} 签署返回结果
     * @throws DefineException
     * @author lsy
     * @since JDK1.7
     */
    public FileDigestSignResult localSignPDF(String accountId, String sealData, SignPDFStreamBean stream, PosBean sigPos,
                                             SignType signType) throws DefineException {

        FileDigestSignResult signRst = userSignService
                .localSignPDF(accountId, sealData, stream, sigPos, signType);
        castSignRst(signRst, "客户", true);
        return signRst;
    }


    /**
     * description 平台用户PDF摘要签署（文件流&短信验证）
     * <p>
     * 用指定账户的证书对文档进行签署，此签署过程不将文档上传至e签宝平台，
     * 只传递文档摘要信息。签署过程需要校验用户短信验证码，验证码必须通过e签宝接口发送
     * <p>
     * {@link UserSignService#localSafeSignPDF(String, String,
     * com.timevale.esign.sdk.tech.bean.AbstractSignPdfBean, PosBean, SignType, String)}
     *
     * @param accountId {@link String} 签署者账号标识，以此获取账户的证书进行签署
     * @param sealData  {@link String} 印章图片Base64，若为空最终签署后无直观图片展现
     * @param stream    {@link SignPDFFileBean} 签署PDF文档信息
     * @param signPos   {@link PosBean} 签章位置信息
     * @param signType  {@link SignType} 签章类型
     * @param code      {@link String} 短信验证码，必须通过e签宝接口发送
     * @throws DefineException
     * @author lsy
     * @since JDK1.7
     */
    public void localSignPDF(String accountId, String sealData, SignPDFStreamBean stream, PosBean signPos,
                             SignType signType, String code) throws DefineException {

        FileDigestSignResult signRst = userSignService
                .localSafeSignPDF(accountId, sealData, stream, signPos, signType, code);
        castSignRst(signRst, "客户", true);
    }


    /**
     * description 平台用户PDF摘要签署（文件流&指定手机短信验证）
     * <p>
     * 用指定账户的证书对文档进行签署，此签署过程不将文档上传至e签宝平台，
     * 只传递文档摘要信息。签署过程需要校验用户短信验证码，验证码必须通过e签宝接口发送。
     * <p>
     * {@link UserSignService#localSafeSignPDF3rd(String, String, com.timevale.esign.sdk.tech.bean.AbstractSignPdfBean,
     * PosBean, SignType, String, String)}
     *
     * @param accountId {@link String} 签署者账号标识，以此获取账户的证书进行签署
     * @param sealData  {@link String} 印章图片Base64，若为空最终签署后将没有直观图片展现
     * @param stream    {@link SignPDFStreamBean} 签署PDF文档信息
     * @param signPos   {@link PosBean} 签章位置信息
     * @param signType  {@link SignType} 签章类型
     * @param mobile    {@link String} 接收短信验证码的手机
     * @param code      {@link String} 短信验证码，必须通过e签宝接口发送
     * @throws DefineException
     * @author lsy
     * @since JDK1.7
     */
    public void localSignPDF(String accountId, String sealData, SignPDFStreamBean stream, PosBean signPos,
                             SignType signType, String mobile, String code) throws DefineException {

        FileDigestSignResult signRst = userSignService
                .localSafeSignPDF3rd(accountId, sealData, stream, signPos, signType, mobile, code);
        castSignRst(signRst, "客户", true);
    }


    /**
     * description 平台用户PDF摘要签署（短信验证批量签署）
     * <p>
     * 用指定账户的证书批量签署文档（最大为50份文档），此签署过程不将文档上传至e签宝平台，
     * 只传递文档摘要信息。签署过程需要校验用户短信验证码，验证码必须通过e签宝接口发送
     * <p>
     * {@link UserSignService#localSafeMultiPureSignPDF(String, List, String, String)}
     *
     * @param accountId  {@link String} 签署者账号标识，以此获取账户的证书进行签署
     * @param signParams {@link List} 签署文档和签署位置信息集合（上限位50个）
     * @param sealData   {@link String} 印章图片Base64，若为空最终签署后将没有直观图片展现
     * @param code       {@link String} 短信验证码，必须通过e签宝接口发送
     * @throws DefineException
     * @author lsy
     * @since JDK1.7
     */
    public void localBatchSignPDF(String accountId, List<SignParamBean> signParams, String sealData,
                                  String code) throws DefineException {

        CodeMultiSignResult signRst = userSignService
                .localSafeMultiPureSignPDF(accountId, signParams, sealData, code);
        castBathSignRst(signRst);
    }


    /**
     * description 平台用户PDF摘要签署（短信验证批量签署）
     * <p>
     * 用指定账户的证书批量签署文档（最大为50份文档），此签署过程不将文档上传至e签宝平台，
     * 只传递文档摘要信息。签署过程需要校验用户短信验证码，验证码必须通过e签宝接口发送
     * <p>
     * {@link UserSignService#localSafeMultiPureSignPDF3rd(String, List, String, String, String)}
     *
     * @param accountId  {@link String} 签署者账号标识，以此获取账户的证书进行签署
     * @param signParams {@link List} 签署文档和签署位置信息集合（上限位50个）
     * @param sealData   {@link String} 印章图片Base64，若为空最终签署后将没有直观图片展现
     * @param mobile     {@link String} 接收短信验证码的手机
     * @param code       {@link String} 短信验证码，必须通过e签宝接口发送
     * @throws DefineException
     * @author lsy
     * @since JDK1.7
     */
    public void localBatchSignPDF(String accountId, List<SignParamBean> signParams, String sealData, String mobile,
                                  String code) throws DefineException {

        CodeMultiSignResult signRst = userSignService
                .localSafeMultiPureSignPDF3rd(accountId, signParams, sealData, mobile, code);
        castBathSignRst(signRst);
    }


    /**
     * description 事件证书PDF摘要签署
     * <p>
     * 使用事件证书对文档进行签署，此签署过程不将文档上传至e签宝平台，只传递文档摘要信息。
     * 注：事件证书具有单次有效性，即使用证书完成签署后，此证书立即失效，不可重复使用
     * <p>
     * {@link EventSignService#localSignPDFByEvent(String, String,
     * com.timevale.esign.sdk.tech.bean.AbstractSignPdfBean, PosBean, SignType)}
     *
     * @param certId   {@link String} 事件证书标识
     * @param sealData {@link String} 印章图片Base64，若为空最终签署后将没有直观图片展现
     * @param fileBean {@link SignPDFFileBean} 签署PDF文档信息
     * @param signPos  {@link PosBean} 签章位置信息
     * @param signType {@link SignType} 签章类型
     * @throws DefineException
     * @author lsy
     * @since JDK1.7
     */
    public void localSignPDFByEvent(String certId, String sealData, SignPDFFileBean fileBean, PosBean signPos,
                                    SignType signType) throws DefineException {

        FileDigestSignResult signRst = eventSignService
                .localSignPDFByEvent(certId, sealData, fileBean, signPos, signType);
        castSignRst(signRst, "客户", false);
    }


    // -----------------------------------共有方法  end---------------------------------------------

    // -----------------------------------私有方法  start-------------------------------------------

    /**
     * description 签署结果处理
     *
     * @param rst        {@link Result} 签署后返回的结果超类
     * @param typeMsg    {@link String} 平台自身 / 平台客户
     * @param streamSign {@link Boolean} 是否文件流签署方式
     * @throws DefineException
     * @author lsy
     * @since JDK1.7
     */
    private void castSignRst(Result rst, String typeMsg, boolean streamSign) throws DefineException {

        if (rst.getErrCode() != 0) {
            throw new DefineException(
                    MessageFormat.format("接口调用方(平台方){0}签署失败: errCode = {1},msg = {2}",
                            typeMsg, rst.getErrCode(), rst.getMsg()));
        }

        // 判断该Result具体属于哪一种子类类型
        if (rst instanceof FileDigestSignResult) {

            FileDigestSignResult signRst = (FileDigestSignResult) rst;
            signLog(signRst.getSignServiceId(), signRst.getDstFilePath(), typeMsg, streamSign);

        } else if (rst instanceof FileDigestSignBatchResult) {
            FileDigestSignBatchResult signRst = (FileDigestSignBatchResult) rst;

            StringBuilder sb = new StringBuilder();
            for (SignInfo signInfo : signRst.getSignDetails()) {
                sb.append(signInfo.getSignServiceId()).append(",");
            }
            sb.replace(sb.lastIndexOf(","), sb.length(), "");

            signLog(sb.toString(), signRst.getDstFilePath(), typeMsg, streamSign);
        }

    }


    /**
     * description 记录签署后打印日志
     *
     * @param signServiceId {@link String} 签署后返回的签署记录ID
     * @param dstFilePath   {@link String} 签署后的目标地址
     * @param typeMsg       {@link String} 平台自身/ 平台客户
     * @param streamSign    {@link Boolean} 是否是文件流签署方式
     * @author lsy
     * @since JDK1.7
     */
    private void signLog(String signServiceId, String dstFilePath, String typeMsg, boolean streamSign) {
        log.debug("接口调用方(平台方){}签署成功:SignServiceId集合为 [{}],"
                + "请妥善保管签署记录ID(SignServiceId)以便日后场景式存证使用", typeMsg, signServiceId);

        if (streamSign) {
            log.debug("接口调用方(平台方){}签署成功, 请妥善保管签署后的文件字节流", typeMsg);
        } else {
            log.debug("接口调用方(平台方){}签署成功后的PDF文件存放路径：{}，请妥善保管签署后的文件", typeMsg, dstFilePath);
        }
    }

    /**
     * description 批量签署后结果处理
     *
     * @param signRst {@link CodeMultiSignResult} 签署后返回的对象
     * @throws DefineException
     * @author lsy
     * @since JDK1.7
     */
    private void castBathSignRst(CodeMultiSignResult signRst)
            throws DefineException {

        if (signRst.getErrCode() != 0) {
            StringBuilder sb = new StringBuilder();

            List<FileDigestSignResult> failList = signRst.getFailList();
            for (int i = 0; CollectionUtils.isNotEmpty(failList) && i < failList.size(); i++) {
                FileDigestSignResult rst = failList.get(i);
                sb.append(rst.getFilePath());
                if (i < failList.size() - 1) {
                    sb.append(",\n");
                }
            }

            log.debug("以下待签署文件路径列表签署失败：[{}]", sb.toString());
            throw new DefineException(MessageFormat.format("接口调用方(平台方)的客户批量签署失败: errCode = {0},msg = {1}",
                    signRst.getErrCode(), signRst.getMsg()));
        }

        List<FileDigestSignResult> sucList = signRst.getSuccessList();
        for (int i = 0; CollectionUtils.isNotEmpty(sucList) && i < sucList.size(); i++) {
            FileDigestSignResult rst = sucList.get(i);
            log.debug("签署成功后的PDF文件存放路径: {}, SignServiceId = {}, 请妥善保管签署记录ID(SignServiceId)以便日后场景式存证使用",
                    rst.getDstFilePath(), rst.getSignServiceId());
        }
    }

    // -----------------------------------私有方法  end---------------------------------------------

    // -----------------------------------getter 、setter 方法  start-------------------------------
    public ServiceClient getServiceClient() {
        return serviceClient;
    }

    public void setServiceClient(ServiceClient serviceClient) {
        this.serviceClient = serviceClient;
    }

    public EventSignService getEventSignService() {
        return eventSignService;
    }

    public void setEventSignService(EventSignService eventSignService) {
        this.eventSignService = eventSignService;
    }

    public SelfSignService getSelfSignService() {
        return selfSignService;
    }

    public void setSelfSignService(SelfSignService selfSignService) {
        this.selfSignService = selfSignService;
    }

    public UserSignService getUserSignService() {
        return userSignService;
    }

    public void setUserSignService(UserSignService userSignService) {
        this.userSignService = userSignService;
    }

    // -----------------------------------getter 、setter 方法  end---------------------------------

}
