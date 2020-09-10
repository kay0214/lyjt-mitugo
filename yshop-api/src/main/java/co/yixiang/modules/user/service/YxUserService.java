package co.yixiang.modules.user.service;

import co.yixiang.common.service.BaseService;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.modules.manage.entity.SystemUser;
import co.yixiang.modules.order.web.vo.YxStoreOrderQueryVo;
import co.yixiang.modules.security.rest.param.LoginParam;
import co.yixiang.modules.user.entity.YxUser;
import co.yixiang.modules.user.web.dto.PromUserDTO;
import co.yixiang.modules.user.web.param.PromParam;
import co.yixiang.modules.user.web.param.YxUserQueryParam;
import co.yixiang.modules.user.web.vo.YxUserQueryVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

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

    boolean setSpread(int spread,int uid);

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
}
