package co.yixiang.modules.shop.service.impl;

import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.utils.QueryHelpPlus;
import co.yixiang.dozer.service.IGenerator;
import co.yixiang.modules.shop.domain.User;
import co.yixiang.modules.shop.domain.YxPointDetail;
import co.yixiang.modules.shop.service.UserService;
import co.yixiang.modules.shop.service.YxPointDetailService;
import co.yixiang.modules.shop.service.dto.YxPointDetailDto;
import co.yixiang.modules.shop.service.dto.YxPointDetailQueryCriteria;
import co.yixiang.modules.shop.service.mapper.UserMapper;
import co.yixiang.modules.shop.service.mapper.YxPointDetailMapper;
import co.yixiang.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author huiy
 * @date 2020-08-19
 */
@Service
@AllArgsConstructor
//@CacheConfig(cacheNames = "yxPointDetail")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YxPointDetailServiceImpl extends BaseServiceImpl<YxPointDetailMapper, YxPointDetail> implements YxPointDetailService {

    private final IGenerator generator;
    @Autowired
    private UserService userService;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(YxPointDetailQueryCriteria criteria, Pageable pageable) {
//        getPage(pageable);
//        PageInfo<YxPointDetail> page = new PageInfo<>(queryAll(criteria));

        QueryWrapper<YxPointDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().orderByDesc(YxPointDetail::getCreateTime);
        if (0 != criteria.getUserRole()) {
            if (null == criteria.getChildUser() || criteria.getChildUser().size() <= 0) {
                Map<String, Object> map = new LinkedHashMap<>(2);
                map.put("content", new ArrayList<>());
                map.put("totalElements", 0);
                return map;
            }
            queryWrapper.lambda().in(YxPointDetail::getMerchantsId, criteria.getChildUser()).eq(YxPointDetail::getDelFlag, 0);
        }
        if (null != criteria.getType()) {
            queryWrapper.lambda().eq(YxPointDetail::getType, criteria.getType());
        }
        if (StringUtils.isNotBlank(criteria.getUsername())) {
            queryWrapper.lambda().eq(YxPointDetail::getUsername, criteria.getUsername());
        }
        if (criteria.getCreateTime() != null && criteria.getCreateTime().size() > 0){
            queryWrapper.lambda().between(YxPointDetail::getCreateTime, criteria.getCreateTime().get(0), criteria.getCreateTime().get(1));
        }
        IPage<YxPointDetail> ipage = this.page(new Page<>(pageable.getPageNumber() + 1, pageable.getPageSize()), queryWrapper);

        Map<String, Object> map = new LinkedHashMap<>(3);

        // 统计总金额
        BigDecimal totalAmount = new BigDecimal(BigInteger.ZERO);
        // 商户合伙人处理username
        List<YxPointDetailDto> list = new ArrayList<>();
        if (ipage.getTotal() > 0) {
            List<YxPointDetail> pointDetailList = ipage.getRecords();
            for (YxPointDetail pointDetail : pointDetailList) {
                totalAmount = totalAmount.add(pointDetail.getOrderPrice());
                YxPointDetailDto yxPointDetailDto = generator.convert(pointDetail,YxPointDetailDto.class);
                User mer = this.userService.getById(yxPointDetailDto.getMerchantsId());
                yxPointDetailDto.setMerUsername(mer.getUsername());
                User par = this.userService.getById(yxPointDetailDto.getPartnerId());
                yxPointDetailDto.setParUsername(par.getUsername());
                list.add(yxPointDetailDto);
            }
        }

        map.put("content", list);
        map.put("totalAmount", totalAmount);
        map.put("totalElements", ipage.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<YxPointDetail> queryAll(YxPointDetailQueryCriteria criteria) {
        return baseMapper.selectList(QueryHelpPlus.getPredicate(YxPointDetail.class, criteria));
    }
}
