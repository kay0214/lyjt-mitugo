package co.yixiang.modules.shop.web.dto;

import co.yixiang.common.web.param.QueryParam;
import co.yixiang.modules.shop.entity.YxStoreProductAttrValue;
import co.yixiang.modules.shop.web.vo.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品dto
 * </p>
 *
 * @author hupeng
 * @date 2019-10-23
 */
@Data
@EqualsAndHashCode(callSuper = true)

public class ProductDTO extends QueryParam {
    private static final long serialVersionUID = 1L;

    //todo
//    private List<YxStoreProductQueryVo> goodList = new ArrayList();


    //todo
//    private Integer merId = 0;
//
//    private String priceName = "";

    private List<YxStoreProductAttrQueryVo> productAttr = new ArrayList();

    private Map<String, YxStoreProductAttrValue>  productValue = new LinkedHashMap<>();

    private YxStoreProductReplyQueryVo reply;

    private String replyChance;

    private String replyStar;

    private Integer replyCount = 0;

    //todo
//    private List similarity = new ArrayList();

    private YxStoreProductQueryVo storeInfo;

    private String mapKey;

    //门店
//    private YxSystemStoreQueryVo systemStore;

    private Integer uid = 0;

    private YxStoreInfoQueryVo systemStore;

    // 关闭店铺进入按钮 0:未关闭 1：关闭
    private Integer storeClose;
}
