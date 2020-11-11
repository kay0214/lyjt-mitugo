package co.yixiang.modules.esign.core;

import co.yixiang.modules.esign.exception.DefineException;
import com.timevale.esign.sdk.tech.bean.SignPDFFileBean;
import com.timevale.esign.sdk.tech.bean.SignPDFStreamBean;
import com.timevale.esign.sdk.tech.bean.result.FileCreateFromTemplateResult;
import com.timevale.esign.sdk.tech.v3.client.ServiceClient;
import com.timevale.esign.sdk.tech.v3.service.PdfDocumentService;
import lombok.extern.slf4j.Slf4j;

import java.text.MessageFormat;
import java.util.Map;

/**
 * @author lsy
 * date 2019/7/3 17:17
 * @Desciption PDF模板生成PDF辅助类
 * @Since 1.7
 */
@Slf4j
public class PdfTemplateHelper {

    private ServiceClient serviceClient;
    private PdfDocumentService pdfDocumentService;

    public PdfTemplateHelper(ServiceClient serviceClient) {
        this.serviceClient = serviceClient;
        this.pdfDocumentService = serviceClient.pdfDocumentService();
    }
    //--------------------------------共有方法 start-------------------------------------

    /**
     * description 本地pdf模板生成
     * <p>
     * 以绝对文件路径的方式传入文档模板,设置填充的模板文本域Key-Value。
     * 调用方法生成PDF文档。存在字体包时，默认选用 黑体 字体进行填充
     * </p>
     * {@link PdfDocumentService#createFileFromTemplate(SignPDFFileBean, boolean, Map)}
     *
     * @param srcPdfFile {@link String}待签署PDF文档本地路径，含文档名
     * @param dstPdfPath {@link String}签署后PDF文档本地路径，含文档名（为空时返回签署后的文件流）
     * @param isFlag     {@link Boolean}填充后是否禁用现有文档中的对象域
     * @param txtFields  {@link Map}待填充的文本域key-value值
     * @return void
     * @author lsy
     * date 2019/7/3 18:38
     **/
    public FileCreateFromTemplateResult createFileFromTemplate(String srcPdfFile, String dstPdfPath, boolean isFlag,
                                                               Map<String, Object> txtFields) throws DefineException {
        SignPDFFileBean bean = new SignPDFFileBean();
        bean.setSrcPdfFile(srcPdfFile);
        bean.setDstPdfFile(dstPdfPath);
        // 是否增加e签宝logo图标
        bean.setShowImage(false);

        FileCreateFromTemplateResult rst = pdfDocumentService
                .createFileFromTemplate(bean, isFlag, txtFields);
        castRst(rst, "");
        return rst;
    }


    /**
     * description 本地pdf模版生成（文件流）
     * <p>
     * 以文件流的方式传入文档模板,设置填充的模板文本域Key-Value。调用方法生成PDF文档。存在字体包时可使用
     * </p>
     * {@link PdfDocumentService#createFileFromTemplate(SignPDFFileBean, boolean, Map)}
     *
     * @param pdfBytes   {@link java.lang.reflect.Array} 待签署文档本地二进制数据
     * @param dstPdfPath {@link String} 签署后PDF文档本地路径，含文档名（为空时返回签署后的文件流）
     * @param pdfEditPwd {@link String} 文档文档编辑密码，当目标PDF设置权限保护时必填
     * @param isFlag     {@link Boolean}填充后是否禁用现有文档中的对象域
     * @param txtFields  {@link Map} 待填充的文本域key-value值
     * @author lsy
     * date 2019/7/3 18:58
     **/
    public void createFileFromTemplate(byte[] pdfBytes, String dstPdfPath, String pdfEditPwd, boolean isFlag,
                                       Map<String, Object> txtFields) throws DefineException {
        SignPDFStreamBean stream = new SignPDFStreamBean();
        stream.setStream(pdfBytes);
        stream.setDstPdfFile(dstPdfPath);
        stream.setOwnerPassword(pdfEditPwd);

        FileCreateFromTemplateResult rst = pdfDocumentService.createFileFromTemplate(stream, isFlag, txtFields);
        castRst(rst, "文件流");
    }

    //--------------------------------共有方法 end---------------------------------------
    //--------------------------------私有方法 start-------------------------------------

    /**
     * description 处理返回结果
     *
     * @param rst     {@link FileCreateFromTemplateResult} 返回结果
     * @param typeMsg {@link String} 生成PDF模板的方式，仅用于日志打印使用
     * @return void
     * @author lsy
     * date 2019/7/3 18:32
     **/
    private void castRst(FileCreateFromTemplateResult rst, String typeMsg)
            throws DefineException {
        if (rst.getErrCode() != 0) {
            throw new DefineException(MessageFormat.format("本地PDF模板({0})生成失败：errCode={1},errMsg={2}",
                    typeMsg, rst.getErrCode(), rst.getMsg()));
        }
        log.debug("本地PDF模板({})生成成功，填充后PDF文件保存路径：{}", typeMsg, rst.getDstPdfFile());
    }

    //--------------------------------私有方法 end---------------------------------------
    //--------------------------------getter setter start--------------------------------

    public ServiceClient getServiceClient() {
        return serviceClient;
    }

    public void setServiceClient(ServiceClient serviceClient) {
        this.serviceClient = serviceClient;
    }

    public PdfDocumentService getPdfDocumentService() {
        return pdfDocumentService;
    }

    public void setPdfDocumentService(PdfDocumentService pdfDocumentService) {
        this.pdfDocumentService = pdfDocumentService;
    }

    //--------------------------------getter setter end----------------------------------

}


