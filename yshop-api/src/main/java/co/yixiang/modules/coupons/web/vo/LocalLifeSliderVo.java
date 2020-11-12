package co.yixiang.modules.coupons.web.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 本地生活幻灯片返回实体
 * @Author : huanghui
 */
@Data
public class LocalLifeSliderVo implements Serializable {

    private String name;
    private String pic;
    private String sort;
    private String url;
    private String wxappUrl;
    private String status;

    private String price;
    private String crossPrice;
    // 展示方式  0纵向 1横向
    private String showType;
}
