/**
* Copyright (C) 2018-2020
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.shop.service.impl;

import co.yixiang.modules.shop.domain.YxLeaveMessage;
import co.yixiang.common.service.impl.BaseServiceImpl;
import lombok.AllArgsConstructor;
import co.yixiang.dozer.service.IGenerator;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import co.yixiang.common.utils.QueryHelpPlus;
import co.yixiang.utils.ValidationUtil;
import co.yixiang.utils.FileUtil;
import co.yixiang.modules.shop.service.YxLeaveMessageService;
import co.yixiang.modules.shop.service.dto.YxLeaveMessageDto;
import co.yixiang.modules.shop.service.dto.YxLeaveMessageQueryCriteria;
import co.yixiang.modules.shop.service.mapper.YxLeaveMessageMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
// 默认不使用缓存
//import org.springframework.cache.annotation.CacheConfig;
//import org.springframework.cache.annotation.CacheEvict;
//import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
* @author nxl
* @date 2020-11-04
*/
@Service
@AllArgsConstructor
//@CacheConfig(cacheNames = "yxLeaveMessage")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YxLeaveMessageServiceImpl extends BaseServiceImpl<YxLeaveMessageMapper, YxLeaveMessage> implements YxLeaveMessageService {

    private final IGenerator generator;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(YxLeaveMessageQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<YxLeaveMessage> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), YxLeaveMessageDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<YxLeaveMessage> queryAll(YxLeaveMessageQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(YxLeaveMessage.class, criteria));
    }


    @Override
    public void download(List<YxLeaveMessageDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YxLeaveMessageDto yxLeaveMessage : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("订单号", yxLeaveMessage.getOrderId());
            map.put("商户id", yxLeaveMessage.getMerId());
            map.put("联系人", yxLeaveMessage.getUserName());
            map.put("电话", yxLeaveMessage.getUserPhone());
            map.put("留言信息", yxLeaveMessage.getMessage());
            map.put("状态：0 -> 待处理，1 -> 已处理，2 -> 不予处理", yxLeaveMessage.getStatus());
            map.put("留言类型：0 -> 商品，1 -> 商城订单，2 -> 本地生活订单，3 ->商户，4 -> 平台", yxLeaveMessage.getMessageType());
            map.put("备注", yxLeaveMessage.getRemark());
            map.put("是否删除（0：未删除，1：已删除）", yxLeaveMessage.getDelFlag());
            map.put("创建人", yxLeaveMessage.getCreateUserId());
            map.put("修改人", yxLeaveMessage.getUpdateUserId());
            map.put("创建时间", yxLeaveMessage.getCreateTime());
            map.put("更新时间", yxLeaveMessage.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
