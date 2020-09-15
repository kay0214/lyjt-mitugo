package co.yixiang.modules.screen.service;

import co.yixiang.modules.screen.dto.PlatformDto;


public interface PlatformService {

    /**
     * 查询今天的数据
     * @return
     */
    PlatformDto getTodayData();
}
