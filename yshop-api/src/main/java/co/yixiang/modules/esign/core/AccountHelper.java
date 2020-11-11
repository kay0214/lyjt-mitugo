package co.yixiang.modules.esign.core;

import co.yixiang.modules.esign.exception.DefineException;
import com.timevale.esign.sdk.tech.bean.*;
import com.timevale.esign.sdk.tech.bean.result.AddAccountResult;
import com.timevale.esign.sdk.tech.bean.result.GetAccountProfileResult;
import com.timevale.esign.sdk.tech.bean.result.Result;
import com.timevale.esign.sdk.tech.impl.constants.DeleteParamType;
import com.timevale.esign.sdk.tech.impl.constants.LicenseQueryType;
import com.timevale.esign.sdk.tech.service.AccountService;
import com.timevale.esign.sdk.tech.v3.client.ServiceClient;
import lombok.extern.slf4j.Slf4j;

import java.text.MessageFormat;
import java.util.List;

/**
 * description 证书服务辅助类
 *
 * @author lsy
 * date 2019年7月1日上午10:46:44
 */
@Slf4j
public class AccountHelper {

    private ServiceClient serviceClient;
    private AccountService acctService;

    public AccountHelper(ServiceClient serviceClient) {
        this.serviceClient = serviceClient;
        this.acctService = serviceClient.accountService();
    }
    // -----------------------------------公有方法 start-------------------------------------------


    /**
     * description 创建个人账户
     * <p>
     * 创建个人账户，所创建账户是半实名的，即在快捷签对接项目中可以正常签署，
     * 在e签宝平台中无法使用，必须重新通过实名认证后才可以正常使用
     * <p>
     * {@link AccountService#addAccount(PersonBean)}
     *
     * @param personBean {@link PersonBean}个人信息详情
     * @throws DefineException
     * @author lsy
     * @since JDK1.7
     */
    public String addAccount(PersonBean personBean)
            throws DefineException {
        AddAccountResult acctRst = acctService.addAccount(personBean);
        return castAddAccount(acctRst, "个人");
    }


    /**
     * description 创建企业账户
     * <p>
     * 创建企业账户，所创建账户是半实名的，即在快捷签对接项目中可以正常签署，
     * 在e签宝平台中无法使用，必须重新通过实名认证后才可以正常使用
     * <p>
     * {@link AccountService#addAccount(OrganizeBean)}
     *
     * @param organizeBean {@link OrganizeBean}企业信息详情
     * @throws DefineException
     * @author lsy
     * @since JDK1.7
     */
    public String addAccount(OrganizeBean organizeBean)
            throws DefineException {
        AddAccountResult acctRst = acctService.addAccount(organizeBean);
        return castAddAccount(acctRst, "企业");
    }


    /**
     * description 更新个人账户信息
     * <p>
     * 更新个人账户信息。只有此账户的创建者才有权限更改账户信息，用户归属地（personArea）和身份证号（idNo）
     * 不允许修改。若修改了姓名，将自动为用户重发数字证书
     * <p>
     * {@link AccountService#updateAccount(String, UpdatePersonBean, List)}
     *
     * @param accountId       {@link String}待更新账号的标识
     * @param person          {@link UpdatePersonBean} 更新的个人信息详情
     * @param deleteParamType {@link List} 待置空的属性集合
     * @throws DefineException
     * @author lsy
     * @since JDK1.7
     */
    public void updateAcct(String accountId, UpdatePersonBean person, List<DeleteParamType> deleteParamType)
            throws DefineException {
        Result rst = acctService.updateAccount(accountId, person, deleteParamType);
        castUpdateAcct(rst, accountId, "个人");
    }


    /**
     * description 更新企业账户信息
     * <p>
     * 更新企业账户信息。只有此账户的创建者才有权限更改账户信息，企业注册类型（regType）
     * 和企业证件号（organCode）不允许修改。若更改企业名称，将自动为用户重发数字证书
     * <p>
     * {@link AccountService#updateAccount(String, UpdateOrganizeBean, List)}
     *
     * @param accountId       {@link String}待更新账号的标识
     * @param organize        {@link UpdateOrganizeBean}更新的企业信息详情
     * @param deleteParamType {@link List}待置空的属性集合
     * @throws DefineException
     * @author lsy
     * @since JDK1.7
     */
    public void updateAcct(String accountId, UpdateOrganizeBean organize, List<DeleteParamType> deleteParamType)
            throws DefineException {
        Result rst = acctService.updateAccount(accountId, organize, deleteParamType);
        castUpdateAcct(rst, accountId, "企业");
    }


    /**
     * description 注销账户
     * <p>
     * 注销账户，注销后账户将不可再使用，请谨慎调用
     * <p>
     * {@link AccountService#deleteAccount(String)}
     *
     * @param accountId {@link String}待注销账号的标识
     * @throws DefineException
     * @author lsy
     * @since JDK1.7
     */
    public void deleteAccount(String accountId)
            throws DefineException {
        Result rst = acctService.deleteAccount(accountId);
        if (rst.getErrCode() != 0) {
            throw new DefineException(
                    MessageFormat.format("注销账户失败：errCode={0},msg={1}",
                            rst.getErrCode(), rst.getMsg()));
        }
        log.debug("注销账户成功，accountId={}", accountId);
    }


    /**
     * description  根据证件号获取账户信息
     * <p>
     * 根据证件号获取账户信息。只能获取自己项目下的帐号信息
     * <p>
     * {@link AccountService#getAccountInfoByIdNo(String, int)}
     *
     * @param idNo     {@link String}待查询的证件号码
     * @param idNoType {@link LicenseQueryType} 账号对应的类型
     * @throws DefineException
     * @author lsy
     * @since JDK1.7
     */
    public AccountProfile showAccountInfoByIdNo(String idNo, LicenseQueryType idNoType) throws DefineException {
        GetAccountProfileResult acctRst = acctService.getAccountInfoByIdNo(idNo, idNoType);

        if (acctRst.getErrCode() != 0) {
            throw new DefineException(
                    MessageFormat.format("查询账号信息失败：errCode={0},msg={1}", acctRst.getErrCode(), acctRst.getMsg()));
        }

        AccountProfile acctProfile = acctRst.getAccountInfo();
        log.debug("查询账号成功：账号标识accountId = {},名称name={},证件号idNo={},"
                        + "证件类型idNoType={},绑定手机号mobile={}",
                acctProfile.getAccountUid(), acctProfile.getName(), acctProfile.getIdNo(),
                acctProfile.getIdNoType(), acctProfile.getMobile());
        return acctProfile;
    }


    // -----------------------------------公有方法  end---------------------------------------------

    // -----------------------------------私有方法  start-------------------------------------------


    /**
     * description 创建账户返回结果处理
     *
     * @param acctRst {@link AddAccountResult}返回结果
     * @param typeMsg {@link String}个人/企业
     * @throws DefineException
     * @author lsy
     * @since JDK1.7
     */
    private String castAddAccount(AddAccountResult acctRst, String typeMsg) throws DefineException {
        if (acctRst.getErrCode() != 0) {
            throw new DefineException(MessageFormat.format("创建账号失败：errCode = {1},msg = {2}", typeMsg,
                    acctRst.getErrCode(), acctRst.getMsg()));
        }
        log.debug("创建{}账号成功:accountId={},请妥善保管AccountId以便后续场景存证使用", typeMsg, acctRst.getAccountId());
        return acctRst.getAccountId();
    }

    /**
     * description 更新账号返回结果处理
     *
     * @param rst       {@link Result} 返回结果
     * @param accountId {@link String}待更新账号的标识
     * @param typeMsg   {@link String} 个人/企业
     * @throws DefineException
     * @author lsy
     * @since JDK1.7
     */
    private void castUpdateAcct(Result rst, String accountId, String typeMsg) throws DefineException {
        if (rst.getErrCode() != 0) {
            throw new DefineException(
                    MessageFormat.format(
                            "更新{0}账号失败：errCode={1},msg={2}", typeMsg, rst.getErrCode(), rst.getMsg()));
        }
        log.debug("更新{}账号成功,accountId = {},请妥善保管AccountId以便后续场景存证使用", typeMsg, accountId);
    }


    // -----------------------------------共有方法  end---------------------------------------------

    // -----------------------------------getter setter start---------------------------------------
    public ServiceClient getServiceClient() {
        return serviceClient;
    }

    public void setServiceClient(ServiceClient serviceClient) {
        this.serviceClient = serviceClient;
    }

    public AccountService getAcctService() {
        return acctService;
    }

    public void setAcctService(AccountService acctService) {
        this.acctService = acctService;
    }
    // -----------------------------------getter setter end----------------------------------------

}
