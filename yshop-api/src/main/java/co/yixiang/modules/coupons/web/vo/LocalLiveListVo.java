package co.yixiang.modules.coupons.web.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 本地生活分类列表
 * @Author : huanghui
 */
@Data
public class LocalLiveListVo {

    @ApiModelProperty(value = "店铺主键")
    private Integer id;

    @ApiModelProperty(value = "店铺编号")
    private String storeNid;

    @ApiModelProperty(value = "店铺名称")
    private String storeName;

    @ApiModelProperty(value = "店铺缩略图")
    private String img;

    @ApiModelProperty(value = "地图坐标经度")
    private String coordinateX;

    @ApiModelProperty(value = "地图坐标纬度")
    private String coordinateY;

    @ApiModelProperty(value = "距离")
    private String distance;

    @ApiModelProperty(value = "店铺卡券")
    private List<LocalLiveCouponsVo> localLiveCouponsVoList;
}
