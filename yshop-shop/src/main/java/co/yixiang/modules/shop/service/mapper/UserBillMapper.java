package co.yixiang.modules.shop.service.mapper;

import co.yixiang.common.mapper.CoreMapper;
import co.yixiang.modules.shop.domain.YxUserBill;
import co.yixiang.modules.shop.service.dto.YxUserBillDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
* @author hupeng
* @date 2020-05-12
*/
@Repository
@Mapper
public interface UserBillMapper extends CoreMapper<YxUserBill> {

    @Select("<script> select b.title,b.pm,b.category,b.type,b.number,b.add_time ,b.username " +
            "from yx_user_bill b where 1=1  "  +
            "<if test =\"pm !='' and pm != null\">and b.pm=#{pm}</if> " +
            "<if test =\"username !='' and username != null\">and b.username LIKE CONCAT('%',#{username},'%')</if>" +
            "<if test =\"title != '' and title != null\">and b.title LIKE CONCAT('%',#{title},'%')</if> " +
            "<if test = \"startTime != '' and startTime != null\"> and b.add_time BETWEEN #{startTime} AND #{endTime}</if> </script>")
    List<YxUserBillDto> findAllByQueryCriteria(@Param("username") String username, @Param("title") String title, @Param("pm") Integer pm, @Param("startTime") Integer startTime, @Param("endTime") Integer endTime);

    /**
     * 根据 关联ID, 查询指定类型数据
     * @param linkId
     * @return
     */
    @Select("<script> select b.title,b.pm,b.category,b.type,b.number,b.add_time ,b.username as username " +
            "from yx_user_bill b where 1=1  "  +
            "<if test = \"linkId != '' and linkId != null\"> and b.link_id=#{linkId}</if> </script>")
    List<YxUserBillDto> withdrawReviewLog(@Param("linkId") Integer linkId);


    @Select("<script>SELECT IFNULL(SUM(number),0) as subSum from yx_user_bill where pm=#{pm} AND uid = #{uid} AND user_type = #{usetType} </script>")
    BigDecimal getSumPrice(@Param("uid") Integer uid, @Param("pm") Integer pm, @Param("usetType") Integer usetType);

}
