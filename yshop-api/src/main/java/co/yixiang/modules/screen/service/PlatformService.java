package co.yixiang.modules.screen.service;

import co.yixiang.modules.couponUse.dto.TodayDataDto;
import co.yixiang.modules.screen.dto.PlatformDto;


public interface PlatformService {

    /**
     * 查询今天的数据
     * @return
     */
    PlatformDto getTodayData();

    /**
     * 核销端今日数据统计
     * @param storeId
     * @return
     */
    TodayDataDto getWorkDataBySid(Integer storeId);
}
