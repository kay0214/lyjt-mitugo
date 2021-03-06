package co.yixiang.modules.user.mapper;

import co.yixiang.modules.user.entity.YxUserBill;
import co.yixiang.modules.user.web.dto.BillDTO;
import co.yixiang.modules.user.web.dto.BillOrderRecordDTO;
import co.yixiang.modules.user.web.dto.SignDTO;
import co.yixiang.modules.user.web.dto.UserBillDTO;
import co.yixiang.modules.user.web.param.YxUserBillQueryParam;
import co.yixiang.modules.user.web.vo.YxUserBillQueryVo;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户账单表 Mapper 接口
 * </p>
 *
 * @author hupeng
 * @since 2020-08-13
 */
@Repository
public interface YxUserBillMapper extends BaseMapper<YxUserBill> {

    @Select("select IFNULL(sum(number),0) from yx_user_bill " +
            "where status=1 and type='sign' and pm=1 and category='integral' " +
            "and uid=#{uid}")
    double sumIntegral(@Param("uid") int uid);

    @Select("SELECT FROM_UNIXTIME(a.add_time,'%Y-%m-%d') as addTime,a.title,a.number " +
            "FROM yx_user_bill a INNER JOIN yx_user u ON u.uid=a.uid WHERE a.category = 'integral'" +
            " AND a.type = 'sign' AND a.status = 1 AND a.uid = #{uid} " +
            "ORDER BY a.add_time DESC")
    List<SignDTO> getSignList(@Param("uid") int uid, Page page);

    //    @Select("SELECT if(b.brokerage_type = 0,o.order_id,co.order_id) AS orderId,FROM_UNIXTIME(b.add_time, '%Y-%m-%d %H:%i') as time, " +
//            "b.number,u.avatar,u.nickname FROM yx_user_bill b " +
//            "INNER JOIN yx_store_order o ON o.id=b.link_id and b.brokerage_type = 0 " +
//            "RIGHT JOIN yx_user u ON u.uid=o.uid and b.brokerage_type = 0 " +
//            "INNER JOIN yx_coupon_order co ON co.id=b.link_id and b.brokerage_type = 1 " +
//            "RIGHT JOIN yx_user cu ON cu.uid=co.uid and b.brokerage_type = 1 " +
//            " WHERE b.uid = #{uid} AND ( FROM_UNIXTIME(b.add_time, '%Y-%m')= #{time} ) AND " +
//            "b.category = 'now_money' AND b.type = 'brokerage' ORDER BY time DESC")
//    List<BillOrderRecordDTO> getBillOrderRList(@Param("time") String time, @Param("uid") int uid);
    @Select("(SELECT o.order_id as orderId,FROM_UNIXTIME(b.add_time, '%Y-%m-%d %H:%i') as time," +
            "b.number,u.avatar,u.nickname FROM yx_user_bill b " +
            "INNER JOIN yx_store_order o ON o.order_id=b.link_id " +
            "RIGHT JOIN yx_user u ON u.uid=o.uid" +
            " WHERE b.uid = #{uid} AND ( FROM_UNIXTIME(b.add_time, '%Y-%m')= #{time} ) AND " +
            "b.category = 'now_money' AND b.type = 'brokerage')" +
            "UNION ALL " +
            "(SELECT o.order_id as orderId,FROM_UNIXTIME(b.add_time, '%Y-%m-%d %H:%i') as time," +
            "b.number,u.avatar,u.nickname FROM yx_user_bill b " +
            "INNER JOIN yx_coupon_order o ON o.order_id=b.link_id " +
            "RIGHT JOIN yx_user u ON u.uid=o.uid" +
            " WHERE b.uid = #{uid} AND ( FROM_UNIXTIME(b.add_time, '%Y-%m')= #{time} ) AND " +
            "b.category = 'now_money' AND b.type = 'brokerage')" +
            "ORDER BY time DESC")
    List<BillOrderRecordDTO> getBillOrderRList(@Param("time") String time, @Param("uid") int uid);

    @Select("SELECT FROM_UNIXTIME(add_time,'%Y-%m') as time " +
            " FROM yx_user_bill ${ew.customSqlSegment}")
    List<String> getBillOrderList(@Param(Constants.WRAPPER) Wrapper<YxUserBill> userWrapper, Page page);

    @Select("SELECT FROM_UNIXTIME(add_time,'%Y-%m') as time,group_concat(id SEPARATOR ',') ids " +
            " FROM yx_user_bill ${ew.customSqlSegment}")
    List<BillDTO> getBillList(@Param(Constants.WRAPPER) Wrapper<YxUserBill> userWrapper, Page page);

    @Select("SELECT FROM_UNIXTIME(add_time,'%Y-%m-%d %H:%i') as add_time,title,number,pm " +
            " FROM yx_user_bill ${ew.customSqlSegment}")
    List<UserBillDTO> getUserBillList(@Param(Constants.WRAPPER) Wrapper<YxUserBill> userWrapper);

    @Select("select IFNULL(sum(number),0) from yx_user_bill " +
            "where status=1 and type='brokerage' and pm=1 and category='now_money' " +
            "and uid=#{uid}")
    double sumPrice(@Param("uid") int uid);


    @Select("select IFNULL(sum(number),0) from yx_user_bill " +
            "where status=1 and type='brokerage' and pm=1 and category='now_money' " +
            "and uid=#{uid} and TO_DAYS(NOW()) - TO_DAYS(add_time) <= 1")
    BigDecimal sumYesterdayPrice(@Param("uid") int uid);

    @Select("select IFNULL(sum(number),0) from yx_user_bill " +
            "where status=1 and type='brokerage' and pm=1 and category='now_money' " +
            "and uid=#{uid} and add_time >= UNIX_TIMESTAMP(CAST(SYSDATE() AS DATE))")
    BigDecimal todayCommissionSum(@Param("uid") int uid);

    @Select("select IFNULL(sum(number),0) from yx_user_bill " +
            "where status=1 and type='brokerage' and pm=1 and category='now_money' " +
            "and uid=#{uid}")
    BigDecimal totalCommissionSum(@Param("uid") int uid);

    /**
     * 根据ID获取查询对象
     *
     * @param id
     * @return
     */
    YxUserBillQueryVo getYxUserBillById(Serializable id);

    /**
     * 获取分页对象
     *
     * @param page
     * @param yxUserBillQueryParam
     * @return
     */
    IPage<YxUserBillQueryVo> getYxUserBillPageList(@Param("page") Page page, @Param("param") YxUserBillQueryParam yxUserBillQueryParam);

    @Select("<script> SELECT IFNULL(SUM((commission)),0) from yx_store_cart" +
            " WHERE 1=1" +
            " and id in " +
            "  <foreach item='cartid' index='index' collection='cartIds' open='(' separator=',' close=')'>" +
            "  #{cartid} " +
            " </foreach> </script>" )
    BigDecimal getSumCommission(@Param("cartIds") List<String> cartIds);

    @Select("select count(0) as todayOffPayCount,IFNULL(sum(number),0) as todayOffPay from yx_user_bill where pm=1 and type='mer_off_pay' " +
            "and add_time BETWEEN UNIX_TIMESTAMP(CAST(SYSDATE()AS DATE)) and (UNIX_TIMESTAMP(CAST(SYSDATE()AS DATE) + INTERVAL 1 DAY))")
    Map<String, Object> getTodayOffPayData();

    @Select("select count(0) as todayOnlinePayCount,IFNULL(sum(number),0) as todayOnlinePay from yx_user_bill where pm=0 and user_type=3 and (type='pay_product' or type='pay_coupon')" +
            "and add_time BETWEEN UNIX_TIMESTAMP(CAST(SYSDATE()AS DATE)) and (UNIX_TIMESTAMP(CAST(SYSDATE()AS DATE) + INTERVAL 1 DAY))")
    Map<String, Object> getOnlinePayData();
}
