package co.yixiang.modules.esign.core;

import co.yixiang.modules.esign.exception.DefineException;
import com.timevale.esign.sdk.tech.bean.result.Result;
import com.timevale.esign.sdk.tech.service.MobileService;
import com.timevale.esign.sdk.tech.v3.client.ServiceClient;
import lombok.extern.slf4j.Slf4j;

import java.text.MessageFormat;

/**
 * @author lsy
 * date 2019/7/3 16:39
 * @Desciption 短信验证码发送辅助类
 * @Since 1.7
 */
@Slf4j
public class MobileCodeHelper {

    private ServiceClient serviceClient;
    private MobileService mobileService;

    public MobileCodeHelper(ServiceClient serviceClient) {
        this.serviceClient = serviceClient;
        this.mobileService = serviceClient.mobileService();
    }

    //--------------------------------共有方法 start-------------------------------------

    /**
     * description  发送签署短信验证码
     * <p>
     * 发送签署验证短信，调用此接口后，签署时需要调用相应的短信验证签署接口
     * </p>
     * {@link MobileService#sendSignMobileCode(String)}
     *
     * @param accountId {@link String} 签署者账号标识，以此获取账户的手机号发送短信
     * @return void
     * @author lsy
     * date 2019/7/3 16:42
     **/
    public void sendSignMobileCode(String accountId)
            throws DefineException {
        Result rst = mobileService.sendSignMobileCode(accountId);
        castSendRst(rst, 0, "");
    }


    /**
     * description  指定手机发送签署短信验证码
     * <p>
     * 发送签署验证短信，调用此接口后，签署时需要调用相应的短信验证签署接口
     * </p>
     * {@link MobileService#sendSignMobileCode3rd(String, String)}
     *
     * @param accountId {@link String} 签署者账号标识
     * @param mobile    {@link String} 待接收短信验证码的手机
     * @return void
     * @author lsy
     * date 2019/7/3 16:57
     **/
    public void sendSignMobileCode(String accountId, String mobile)
            throws DefineException {
        //如果是国外手机号，可使用mobileService.sendSignMobileCode3rd(accountId, mobile,false);
        Result rst = mobileService.sendSignMobileCode3rd(accountId, mobile);
        castSendRst(rst, 1, mobile);
    }
    //--------------------------------共有方法 end---------------------------------------
    //--------------------------------私有方法 start-------------------------------------

    /**
     * description 格式化发送短信验证码返回的结果
     *
     * @param rst      {@link Result} 发送验证码返回的结果
     * @param sendType {@link String} 0：非指定手机号 /1: 指定手机号
     * @param mobile   {@link String} 指定手机号
     * @return void
     * @author lsy
     * date 2019/7/3 16:53
     **/
    private void castSendRst(Result rst, int sendType, String mobile)
            throws DefineException {

        if (rst.getErrCode() != 0) {
            throw new DefineException(MessageFormat.format("{0}签署短信验证码发送失败：errCode={1},errMsg={2}",
                    sendType, rst.getErrCode(), rst.getMsg()));
        }
        if (sendType == 1) {
            log.debug("指定手机号签署短信验证码发送成功, 请查找手机号[{}]接收的验证码且在30分钟内完成签署", mobile);
        } else {
            log.debug("签署短信验证码发送成功, 请使用创建账户时所填写的手机号中查找验证码且在30分钟内完成签署");
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

    public MobileService getMobileService() {
        return mobileService;
    }

    public void setMobileService(MobileService mobileService) {
        this.mobileService = mobileService;
    }

    //--------------------------------getter setter end----------------------------------

}


