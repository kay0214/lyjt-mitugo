package co.yixiang.modules.user.service;

import co.yixiang.common.service.BaseService;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.modules.couponUse.dto.ShipUserVO;
import co.yixiang.modules.manage.entity.SystemUser;
import co.yixiang.modules.manage.entity.UsersRoles;
import co.yixiang.modules.order.web.vo.YxStoreOrderQueryVo;
import co.yixiang.modules.security.rest.param.LoginParam;
import co.yixiang.modules.user.entity.YxUser;
import co.yixiang.modules.user.web.dto.PromUserDTO;
import co.yixiang.modules.user.web.param.PromParam;
import co.yixiang.modules.user.web.param.YxUserQueryParam;
import co.yixiang.modules.user.web.vo.YxUserQueryVo;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author hupeng
 * @since 2020-08-13
 */
public interface YxUserService extends BaseService<YxUser> {

    double setLevelPrice(double price, int uid);

    void incMoney(int uid,double price);

    void incIntegral(int uid,double integral);

    YxUserQueryVo getNewYxUserById(Serializable id);
    boolean backOrderBrokerage(YxStoreOrderQueryVo order);

    boolean backOrderBrokerageTwo(YxStoreOrderQueryVo order);

    void setUserSpreadCount(int uid);

    int getSpreadCount(int uid,int type);

    List<PromUserDTO> getUserSpreadGrade(PromParam promParam,int uid);

    boolean setSpread(int spread,int uid,String spreadType);

    void decIntegral(int uid,double integral);

    void incPayCount(int uid);

    void decPrice(int uid,double payPrice);

    YxUser findByName(String name);

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxUserQueryVo getYxUserById(Serializable id);

    /**
     * 获取分页对象
     * @param yxUserQueryParam
     * @return
     */
    Paging<YxUserQueryVo> getYxUserPageList(YxUserQueryParam yxUserQueryParam) throws Exception;

    Object authLogin(String code, String spread, HttpServletRequest request);

    Object wxappAuth(LoginParam loginParam, HttpServletRequest request);

    /**
     * 根据登录用户名查询系统用户
     * @param username
     * @return
     */
    SystemUser getSystemUserByUserName(String username);

    /**
     * 提现申请扣减用户可提现金额
     *
     * @param uid
     * @param money
     */
    void updateExtractMoney(int uid, BigDecimal money);

    /**
     * 查询admin的用户
     * @param uid
     * @return
     */
    SystemUser getSystemUserById(Integer uid);

    /**
     * 增加用户提现金额
     * @param updateYxUser
     */
    void updateUserMoney(YxUser updateYxUser);

    /**
     * 增加商户提现金额
     * @param updateYxUser
     */
    void updateMerMoney(YxUser updateYxUser);

    /**
     * 查询所有商户数量
     * @return
     */
    Integer getAllMer();

    /**
     * 查询所有认证通过的商户数量
     * @return
     */
    Integer getAllOkMer();

    /**
     * 平台的用户数量
     * @return
     */
    Integer getAllUser();

    /**
     * 分销客数量
     * @return
     */
    Integer getAllFxUser();

    /**
     * 获取本商户所有船长
     * @param storeId
     * @return
     */
    List<ShipUserVO> getAllShipUserByStoreId(int storeId);

    /**
     * 根据用户id获取角色
     * @param uid
     * @return
     */
    UsersRoles getUserRolesByUserId(Integer uid);

    SystemUser getSystemUserByParam(Integer uid);

    SystemUser getSystemUserByUserNameNew(String username);
}
