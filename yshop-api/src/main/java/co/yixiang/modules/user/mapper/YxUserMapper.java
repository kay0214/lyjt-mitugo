package co.yixiang.modules.user.mapper;

import co.yixiang.modules.user.entity.YxUser;
import co.yixiang.modules.user.web.dto.PromUserDTO;
import co.yixiang.modules.user.web.param.YxUserQueryParam;
import co.yixiang.modules.user.web.vo.YxUserQueryVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author hupeng
 * @since 2020-08-13
 */
@Repository
public interface YxUserMapper extends BaseMapper<YxUser> {



    @Select("<script>SELECT u.uid,u.nickname,u.avatar,from_unixtime(u.add_time,'%Y/%m/%d') as time," +
            "u.spread_count as childCount,COUNT(o.id) as orderCount," +
            "IFNULL(SUM(o.pay_price),0) as numberCount FROM yx_user u " +
            "LEFT JOIN yx_store_order o ON u.uid=o.uid " +
            "WHERE u.uid in <foreach item='id' index='index' collection='uids' " +
            " open='(' separator=',' close=')'>" +
            "   #{id}" +
            " </foreach> <if test='keyword != null'>" +
            " AND ( u.nickname LIKE CONCAT(CONCAT('%',#{keyword}),'%') OR u.phone LIKE CONCAT(CONCAT('%',#{keyword}),'%'))</if>" +
            " GROUP BY u.uid ORDER BY #{orderBy} " +
            "</script>")
    List<PromUserDTO> getUserSpreadCountList(Page page,
                                             @Param("uids") List uids,
                                             @Param("keyword") String keyword,
                                             @Param("orderBy") String orderBy);

    @Update("update yx_user set now_money=now_money-#{payPrice}" +
            " where uid=#{uid}")
    int decPrice(@Param("payPrice") double payPrice,@Param("uid") int uid);

    @Update("update yx_user set brokerage_price=brokerage_price+#{brokeragePrice}" +
            " where uid=#{uid}")
    int incBrokeragePrice(@Param("brokeragePrice") double brokeragePrice,@Param("uid") int uid);

    @Update("update yx_user set pay_count=pay_count+1" +
            " where uid=#{uid}")
    int incPayCount(@Param("uid") int uid);

    @Update("update yx_user set now_money=now_money+#{price}" +
            " where uid=#{uid}")
    int incMoney(@Param("uid") int uid,double price);

    @Update("update yx_user set integral=integral-#{integral}" +
            " where uid=#{uid}")
    int decIntegral(@Param("integral") double integral,@Param("uid") int uid);

    @Update("update yx_user set integral=integral+#{integral}" +
            " where uid=#{uid}")
    int incIntegral(@Param("integral") double integral,@Param("uid") int uid);


    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxUserQueryVo getYxUserById(Serializable id);

    /**
     * 获取分页对象
     * @param page
     * @param yxUserQueryParam
     * @return
     */
    IPage<YxUserQueryVo> getYxUserPageList(@Param("page") Page page, @Param("param") YxUserQueryParam yxUserQueryParam);

    @Update("update `yx_user` set `push_count` = `push_count` + 1,`spread_count` = `spread_count` + 1 where `uid` = #{spread}")
    int updateUserPusCount(@Param("spread") int spread);

    /**
     * 更新用户金额
     * @param money
     * @param oldMoney
     * @param uid
     * @return
     */
    @Update("update yx_user set now_money=now_money+#{money} , brokerage_price=brokerage_price+#{money}" +
            " where uid=#{uid} and now_money= #{oldMoney}")
    int updateUserMoney(@Param("money") BigDecimal money, @Param("oldMoney")BigDecimal oldMoney,@Param("uid") int uid);

    @Update( "update yx_user set now_money = now_money - ${money} where uid = #{uid}")
    void updateExtractMoney(@Param("uid")int uid, BigDecimal money);

    /**
     * 增加前台用户金额
     * @param uid
     */
    @Update( "update yx_user set now_money = now_money + ${money} where uid = #{uid}")
    void updateAddUserMoney(@Param("uid")Integer uid, @Param("money")BigDecimal money);

    /**
     * 增加admin用户金额
     * @param uid
     */
    @Update( "update user set withdrawal_amount = withdrawal_amount + ${money} where id = #{uid}")
    void updateAddMerMoney(@Param("uid")Integer uid, @Param("money")BigDecimal money);

    /**
     * 查询所有商户
     * @return
     */
    @Select("select count(0) as allMer from yx_merchants_detail where status=0  and del_flag=0")
    Integer getAllMer();

    /**
     * 查询所有认证通过商户数量
     * @return
     */
    @Select("select count(0) as allMer from yx_merchants_detail where status=0 and examine_status=1 and del_flag=0")
    Integer getAllOkMer();

    /**
     * 平台的用户数量
     * @return
     */
    @Select("select count(0) as allMer from yx_user where status=1")
    Integer getAllUser();

    /**
     * 分销客用户数量
     * @return
     */
    @Select("select count(0) as allMer from yx_user where status=1 and user_role=1")
    Integer getAllFxUser();
}
