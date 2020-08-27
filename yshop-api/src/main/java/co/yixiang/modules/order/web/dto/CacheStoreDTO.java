package co.yixiang.modules.order.web.dto;

import co.yixiang.modules.shop.web.vo.YxStoreStoreCartQueryVo;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName CacheDTO
 * @Author hupeng <610796224@qq.com>
 * @Date 2019/10/27
 **/
@Data
public class CacheStoreDTO implements Serializable {
    private List<YxStoreStoreCartQueryVo> cartInfo;
}
