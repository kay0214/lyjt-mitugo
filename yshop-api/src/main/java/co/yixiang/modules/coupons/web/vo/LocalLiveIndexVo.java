package co.yixiang.modules.coupons.web.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 本地生活 导航实体
 * @Author : huanghui
 */
@Data
public class LocalLiveIndexVo implements Serializable {

    /** 本地生活Banner下导航 */
    List<LocalLifeSliderVo> localLiveMenu;

    /** 本地生活Banner上导航 */
    List<LocalLifeSliderVo> localLiveLink;

    /** 幻灯片 */
    List<LocalLifeSliderVo> sliderList;

    /**首页通知公告**/
    NoticeVO notice ;

    // 首页的文字
    IndexTitleVO title_1;
    IndexTitleVO title_2;
    IndexTitleVO title_3_1;
    IndexTitleVO title_3_2;
    IndexTitleVO title_3_3;

    // 模块的内容
    List<LocalLifeSliderVo> module1;
    List<LocalLifeSliderVo> module2;

}
