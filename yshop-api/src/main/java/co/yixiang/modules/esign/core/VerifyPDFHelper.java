package co.yixiang.modules.esign.core;

import co.yixiang.modules.esign.exception.DefineException;
import com.timevale.esign.sdk.tech.bean.SignBean;
import com.timevale.esign.sdk.tech.bean.SignatureBean;
import com.timevale.esign.sdk.tech.bean.result.VerifyPdfResult;
import com.timevale.esign.sdk.tech.service.SignService;
import com.timevale.esign.sdk.tech.v3.client.ServiceClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;

import java.text.MessageFormat;
import java.util.List;

/**
 * description  PDF验签服务辅助类
 *
 * @author lsy
 * date 2019/7/3 15:44
 */
@Slf4j
public class VerifyPDFHelper {

    private ServiceClient serviceClient;
    private SignService signService;

    public VerifyPDFHelper(ServiceClient serviceClient) {
        this.serviceClient = serviceClient;
        this.signService = serviceClient.signService();
    }
    //--------------------------------共有方法 start-------------------------------------

    /**
     * description PDF文档验签
     * <p>
     * 验证本地PDF文档签名的有效性
     * </p>
     * {@link SignService#localVerifyPdf(String)}
     *
     * @param filePath {@link String} 已签的pdf文件完整路径
     * @return void
     * @author lsy
     * date 16:02 2019/7/3
     **/
    public void localVerifyPdf(String filePath)
            throws DefineException {
        VerifyPdfResult verifyRst = signService.localVerifyPdf(filePath);
        castVerifyRst(verifyRst);
    }

    /**
     * description PDF文档验签（文件流）
     * <p>
     * 验证签名的有效性
     * </p>
     * {@link SignService#localVerifyPdf(byte[])}
     *
     * @param stream {@link java.lang.reflect.Array}
     * @return void
     * @author lsy
     * date 2019/7/3 16:34
     **/
    public void localVerifyPdf(byte[] stream)
            throws DefineException {
        VerifyPdfResult verifyRst = signService.localVerifyPdf(stream);
        castVerifyRst(verifyRst);
    }

    //--------------------------------共有方法 end---------------------------------------
    //--------------------------------私有方法 start-------------------------------------

    /**
     * description 格式化验签结果
     *
     * @param verifyRst {@link VerifyPdfResult} 验签结果
     * @return void
     * @author lsy
     * date 2019/7/3 16:22
     **/
    private void castVerifyRst(VerifyPdfResult verifyRst)
            throws DefineException {

        if (verifyRst.getErrCode() != 0) {
            throw new DefineException(MessageFormat.format("PDF文档验签失败：errCode={0},errMsg={1}",
                    verifyRst.getErrCode(), verifyRst.getMsg()));
        }

        List<SignBean> signBeans = verifyRst.getSignatures();
        //获取PDF文件中的所有签名信息
        for (int i = 0; CollectionUtils.isNotEmpty(signBeans)
                && i < signBeans.size(); i++) {
            SignBean signBean = signBeans.get(i);
            String sealName = signBean.getSealName();
            String cn = signBean.getCert().getCn();
            String certSN = signBean.getCert().getSn();
            String issuerCn = signBean.getCert().getIssuerCN();
            String startDate = signBean.getCert().getStartDate();
            String endDate = signBean.getCert().getEndDate();

            SignatureBean signatureBean = signBean.getSignature();
            boolean isValid = signatureBean.isValidate();
            String signDate = signatureBean.getSignDate();
            String timeFrom = signatureBean.getTimeFrom();

            log.debug("签名使用的印章名称:{},签署人证书名称:{},签署人证书序列号:{},证书发布者名称:{}," +
                    "签署人证书有效期开始时间:{},签署人证书有效期结束时间:{}，该PDF中签名的验证结果:{}，文档签署时间:{}," +
                    "签名数据来源:{}", sealName, cn, certSN, issuerCn, startDate, endDate, isValid, signDate, timeFrom);
        }
    }
    //--------------------------------私有方法 end---------------------------------------
    //--------------------------------getter setter start--------------------------------

    public ServiceClient getServiceClient() {
        return serviceClient;
    }

    public void setServiceClient(ServiceClient serviceClient) {
        this.serviceClient = serviceClient;
    }

    public SignService getSignService() {
        return signService;
    }

    public void setSignService(SignService signService) {
        this.signService = signService;
    }

    //--------------------------------getter setter end----------------------------------
}
