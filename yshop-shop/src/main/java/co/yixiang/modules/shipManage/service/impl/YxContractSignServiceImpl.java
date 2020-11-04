/**
* Copyright (C) 2018-2020
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.shipManage.service.impl;

import co.yixiang.modules.shipManage.domain.YxContractSign;
import co.yixiang.common.service.impl.BaseServiceImpl;
import lombok.AllArgsConstructor;
import co.yixiang.dozer.service.IGenerator;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import co.yixiang.common.utils.QueryHelpPlus;
import co.yixiang.utils.ValidationUtil;
import co.yixiang.utils.FileUtil;
import co.yixiang.modules.shipManage.service.YxContractSignService;
import co.yixiang.modules.shipManage.service.dto.YxContractSignDto;
import co.yixiang.modules.shipManage.service.dto.YxContractSignQueryCriteria;
import co.yixiang.modules.shipManage.service.mapper.YxContractSignMapper;
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
//@CacheConfig(cacheNames = "yxContractSign")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YxContractSignServiceImpl extends BaseServiceImpl<YxContractSignMapper, YxContractSign> implements YxContractSignService {

    private final IGenerator generator;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(YxContractSignQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<YxContractSign> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), YxContractSignDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<YxContractSign> queryAll(YxContractSignQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(YxContractSign.class, criteria));
    }


    @Override
    public void download(List<YxContractSignDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YxContractSignDto yxContractSign : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("关联订单号", yxContractSign.getOrderId());
            map.put("模板id", yxContractSign.getTempId());
            map.put("模板名称", yxContractSign.getTempName());
            map.put("签署文件地址", yxContractSign.getFilePath());
            map.put("签署状态 0:签署中 1：签署完成", yxContractSign.getStatus());
            map.put("是否删除（0：未删除，1：已删除）", yxContractSign.getDelFlag());
            map.put("创建人", yxContractSign.getCreateUserId());
            map.put("修改人", yxContractSign.getUpdateUserId());
            map.put("创建时间", yxContractSign.getCreateTime());
            map.put("更新时间", yxContractSign.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
