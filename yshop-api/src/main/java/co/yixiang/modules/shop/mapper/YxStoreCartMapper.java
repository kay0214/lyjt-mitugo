package co.yixiang.modules.shop.mapper;

import co.yixiang.modules.shop.entity.YxStoreCart;
import co.yixiang.modules.shop.web.param.YxStoreCartQueryParam;
import co.yixiang.modules.shop.web.vo.YxStoreCartQueryVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 购物车表 Mapper 接口
 * </p>
 *
 * @author hupeng
 * @since 2019-10-25
 */
@Repository
public interface YxStoreCartMapper extends BaseMapper<YxStoreCart> {

    @Select("select IFNULL(sum(cart_num),0) from yx_store_cart " +
            "where is_pay=0 and is_del=0 and is_new=0 and uid=#{uid} and type=#{type}")
    int cartSum(@Param("uid") int uid,@Param("type") String type);

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxStoreCartQueryVo getYxStoreCartById(Serializable id);

    /**
     * 获取分页对象
     * @param page
     * @param yxStoreCartQueryParam
     * @return
     */
    IPage<YxStoreCartQueryVo> getYxStoreCartPageList(@Param("page") Page page, @Param("param") YxStoreCartQueryParam yxStoreCartQueryParam);

    @Select(" <script> SELECT DISTINCT store_id " +
            "from yx_store_cart " +
            "WHERE id in " +
            "   <foreach item='item' index='index' collection='cartIds' open='(' separator=',' close=')'>" +
            "       #{item}" +
            "   </foreach>" +
            "AND uid =#{uid} </script> ")
    List<Integer> getStoreIds(@Param("uid") int uid, @Param("cartIds") List<String> type);

    @Select("SELECT sum(cart_num) as count FROM yx_store_cart where uid = #{uid} AND is_del =0 and is_new=0 AND is_pay=0")
    int countCartByUserId(@Param("uid") int uid);
}
