package co.yixiang.modules.coupon.domain;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Author : huanghui
 */
@Data
public class CouponsCategoryAddRequest implements Serializable {

    /** 分类名称 */
    @NotBlank(message = "分类名称不可为空")
    private String cateName;

    @NotBlank(message = "分类图片不可为空")
    private String path;

    /** 排序 */
    @NotNull(message = "分类排序不可为空")
    private Integer sort;


    /** 是否推荐. 0:不推荐, 1:推荐 */
    @NotNull(message = "是否显示不可为空")
    private Integer isShow;

}
