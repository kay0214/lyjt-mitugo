/**
* Copyright (C) 2018-2020
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.shipManage.domain;
import co.yixiang.modules.mybatis.GeoPoint;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.*;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.validation.constraints.*;
import java.sql.Timestamp;
import java.io.Serializable;

/**
* @author nxl
* @date 2020-11-04
*/
@Data
@TableName("yx_ship_series")
public class YxShipSeries implements Serializable {

    /** 系列id */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    /** 系列名称 */
    @NotBlank
    private String seriesName;


    /** 船只类别 */
    @NotNull
    private Integer shipCategory;


    /** 限乘人数 */
    @NotNull
    private Integer rideLimit;


    /** 尺寸 */
    @NotBlank
    private String shipSize;


    /** 状态：0：启用，1：禁用 */
    @NotNull
    private Integer status;


    /** 乘船省市区 */
    @NotBlank
    private String shipProvince;


    /** 乘船地址 */
    @NotBlank
    private String shipAddress;


    /** 地图坐标 */
    private GeoPoint coordinate;

    /** 地图坐标经度 */
    private String coordinateX;


    /** 地图坐标纬度 */
    private String coordinateY;


    /** 是否删除（0：未删除，1：已删除） */
    @NotNull
    @TableLogic
    @TableField(fill=FieldFill.INSERT_UPDATE)
    private Integer delFlag;


    /** 创建人 */
    private Integer createUserId;


    /** 修改人 */
    private Integer updateUserId;


    /** 创建时间 */
    @NotNull
    @TableField(fill= FieldFill.INSERT)
    private Timestamp createTime;


    /** 更新时间 */
    @NotNull
    @TableField(fill= FieldFill.INSERT_UPDATE)
    private Timestamp updateTime;

    /** 商户id */
    @NotNull
    private Integer merId;

    /** 所属商铺 */
    @NotNull
    private Integer storeId;

    public void copy(YxShipSeries source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
