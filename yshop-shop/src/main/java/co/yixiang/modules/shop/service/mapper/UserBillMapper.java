package co.yixiang.modules.shop.service.mapper;

import co.yixiang.common.mapper.CoreMapper;
import co.yixiang.modules.shop.domain.YxUserBill;
import co.yixiang.modules.shop.service.dto.YxUserBillDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* @author hupeng
* @date 2020-05-12
*/
@Repository
@Mapper
public interface UserBillMapper extends CoreMapper<YxUserBill> {

    @Select("<script> select b.title,b.pm,b.category,b.type,b.number,b.add_time ,u.nickname " +
            "from yx_user_bill b left join yx_user u on u.uid=b.uid  where 1=1  "  +
            "<if test =\"pm !='' and pm != null\">and b.pm=#{pm}</if> " +
            "<if test =\"username !='' and username != null\">and b.username LIKE CONCAT('%',#{username},'%')</if>" +
            "<if test =\"title != '' and title != null\">and b.title LIKE CONCAT('%',#{title},'%')</if> " +
            "<if test = \"startTime != '' and startTime != null\"> and b.add_time BETWEEN #{startTime} AND #{endTime}</if> </script>")
    List<YxUserBillDto> findAllByQueryCriteria(@Param("username") String username, @Param("title") String title, @Param("pm") Integer pm, @Param("startTime") Integer startTime, @Param("endTime") Integer endTime);
}
