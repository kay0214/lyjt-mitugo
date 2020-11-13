package co.yixiang.modules.coupons.web.vo;

import co.yixiang.common.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 首页文字
 * </p>
 *
 * @author
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="首页文字", description="首页文字")
public class IndexTitleVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

@ApiModelProperty(value = "文字")
private String title;

@ApiModelProperty(value = "跳转链接")
private String linkUrl;

@ApiModelProperty(value = "图片地址")
private String imgUrl;


}
