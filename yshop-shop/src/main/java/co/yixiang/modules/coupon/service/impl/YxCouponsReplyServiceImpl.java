/**
* Copyright (C) 2018-2020
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.coupon.service.impl;

import co.yixiang.modules.coupon.domain.YxCouponsReply;
import co.yixiang.common.service.impl.BaseServiceImpl;
import lombok.AllArgsConstructor;
import co.yixiang.dozer.service.IGenerator;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import co.yixiang.common.utils.QueryHelpPlus;
import co.yixiang.utils.ValidationUtil;
import co.yixiang.utils.FileUtil;
import co.yixiang.modules.coupon.service.YxCouponsReplyService;
import co.yixiang.modules.coupon.service.dto.YxCouponsReplyDto;
import co.yixiang.modules.coupon.service.dto.YxCouponsReplyQueryCriteria;
import co.yixiang.modules.coupon.service.mapper.YxCouponsReplyMapper;
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
//@CacheConfig(cacheNames = "yxCouponsReply")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YxCouponsReplyServiceImpl extends BaseServiceImpl<YxCouponsReplyMapper, YxCouponsReply> implements YxCouponsReplyService {

    private final IGenerator generator;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(YxCouponsReplyQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<YxCouponsReply> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), YxCouponsReplyDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<YxCouponsReply> queryAll(YxCouponsReplyQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(YxCouponsReply.class, criteria));
    }


    @Override
    public void download(List<YxCouponsReplyDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YxCouponsReplyDto yxCouponsReply : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("用户ID", yxCouponsReply.getUid());
            map.put("订单ID", yxCouponsReply.getOid());
            map.put("卡券id", yxCouponsReply.getCouponId());
            map.put("总体感觉", yxCouponsReply.getGeneralScore());
            map.put("评论内容", yxCouponsReply.getComment());
            map.put("评论时间", yxCouponsReply.getAddTime());
            map.put("管理员回复时间", yxCouponsReply.getMerchantReplyTime());
            map.put("0：未回复，1：已回复", yxCouponsReply.getIsReply());
            map.put("商户id", yxCouponsReply.getMerId());
            map.put("管理员回复内容", yxCouponsReply.getMerchantReplyContent());
            map.put("是否删除（0：未删除，1：已删除）", yxCouponsReply.getDelFlag());
            map.put("创建人", yxCouponsReply.getCreateUserId());
            map.put("修改人", yxCouponsReply.getUpdateUserId());
            map.put("创建时间", yxCouponsReply.getCreateTime());
            map.put("更新时间", yxCouponsReply.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
