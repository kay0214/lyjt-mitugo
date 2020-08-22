package co.yixiang.modules.coupons.web.vo;

import lombok.Data;

import java.util.List;

/**
 * 本地生活 导航实体
 * @Author : huanghui
 */
@Data
public class LocalLiveMenusVo {

    /** 本地生活Banner下导航 */
    List<LocalLiveNavVo> localLiveMenu;

    /** 本地生活Banner上导航 */
    List<LocalLiveNavVo> localLiveLink;
}
