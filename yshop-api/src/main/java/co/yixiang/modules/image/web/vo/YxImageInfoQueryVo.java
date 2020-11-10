package co.yixiang.modules.image.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;

import java.util.Date;

/**
 * <p>
 * 图片表 查询结果对象
 * </p>
 *
 * @author hupeng
 * @date 2020-08-13
 */
@Data
@ApiModel(value="YxImageInfoQueryVo对象", description="图片表查询参数")
public class YxImageInfoQueryVo implements Serializable{
    private static final long serialVersionUID = 1L;

@ApiModelProperty(value = "id")
private Integer id;

@ApiModelProperty(value = "类型id（关联id）")
private Integer typeId;

@ApiModelProperty(value = "图片类型（1：卡券，2：店铺，3：商品，4：商户相关）")
private Integer imgType;

@ApiModelProperty(value = "图片类别 1：缩略图/图片，2：轮播图，3:视频，4:评论图片。以下选项针对商户 1：个人手持身份证,2：个人证件照人像面，3：个人证件照国徽面，4：营业执照，5：银行开户证明，6：法人身份证头像面，7：法人身份证头像面国徽面，8：门店照及经营场所，9： 医疗机构许可证")
private Integer imgCategory;

@ApiModelProperty(value = "图片地址")
private String imgUrl;

@ApiModelProperty(value = "是否删除（0：未删除，1：已删除）")
private Integer delFlag;

@ApiModelProperty(value = "创建人")
private Integer createUserId;

@ApiModelProperty(value = "修改人")
private Integer updateUserId;

@ApiModelProperty(value = "创建时间")
private Date createTime;

@ApiModelProperty(value = "更新时间")
private Date updateTime;

}