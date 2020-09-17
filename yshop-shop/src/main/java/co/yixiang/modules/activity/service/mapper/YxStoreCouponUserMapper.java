/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co

 */
package co.yixiang.modules.activity.service.mapper;

import co.yixiang.common.mapper.CoreMapper;
import co.yixiang.modules.activity.domain.YxStoreCouponUser;
import co.yixiang.modules.activity.service.dto.YxStoreCouponQueryParam;
import co.yixiang.modules.activity.service.dto.YxStoreCouponUserDto;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* @author hupeng
* @date 2020-05-13
*/
@Repository
@Mapper
public interface YxStoreCouponUserMapper extends CoreMapper<YxStoreCouponUser> {

    @Select("<script> SELECT scu.id, scu.cid,scu.uid,scu.coupon_title," +
            "scu.coupon_price,scu.use_min_price,scu.add_time," +
            "scu.end_time,scu.use_time,scu.type,scu.`status`," +
            "scu.is_fail,scu.store_id,scu.issue_coupon_id,yu.nickname " +
            " FROM yx_store_coupon_user scu LEFT JOIN yx_user yu ON scu.uid = yu.uid" +
            " where 1=1 " +
            " <if test=\"storeIds != null and storeIds.length > 0\">"+
            " and scu.store_id IN " +
            "  <foreach item='storeId' index='index' collection='storeIds' open='(' separator=',' close=')'>" +
            "  #{storeId} " +
            " </foreach>"+
            "</if>" +
            "<if test=\"param.userName!=null and param.userName != '' \"> and yu.nickname LIKE  CONCAT('%',#{param.userName},'%')</if>" +
            "<if test=\"param.couponTitle!=null and param.couponTitle != '' \"> and scu.coupon_title LIKE CONCAT('%',#{param.couponTitle},'%')</if>" +
            " order by  scu.add_time  desc "+
            "</script>")
    List<YxStoreCouponUserDto> selectCouponUserPage(Page<YxStoreCouponUserDto> page,@Param("param") YxStoreCouponQueryParam param, @Param("storeIds") List<Long> childStoreId);

    @Select("<script> SELECT count(scu.id) FROM yx_store_coupon_user scu LEFT JOIN yx_user yu ON scu.uid = yu.uid" +
            " where 1=1 " +
            " <if test=\"storeIds != null and storeIds.length > 0\">"+
            " and scu.store_id IN " +
            "  <foreach item='storeId' index='index' collection='storeIds' open='(' separator=',' close=')'>" +
            "  #{storeId} " +
            " </foreach>"+
            "</if>" +
            "<if test=\"param.userName!=null and param.userName != '' \"> and yu.nickname LIKE  CONCAT('%',#{param.userName},'%')</if>" +
            "<if test=\"param.couponTitle!=null and param.couponTitle != '' \"> and scu.coupon_title LIKE CONCAT('%',#{param.couponTitle},'%')</if>" +
            "</script>")
    int countCouponUserPage(@Param("param") YxStoreCouponQueryParam param, @Param("storeIds") List<Long> childStoreId);

}
