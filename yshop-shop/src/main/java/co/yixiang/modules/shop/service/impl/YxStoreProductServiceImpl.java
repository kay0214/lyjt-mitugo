/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 */
package co.yixiang.modules.shop.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.utils.QueryHelpPlus;
import co.yixiang.dozer.service.IGenerator;
import co.yixiang.exception.BadRequestException;
import co.yixiang.modules.shop.domain.*;
import co.yixiang.modules.shop.service.*;
import co.yixiang.modules.shop.service.dto.*;
import co.yixiang.modules.shop.service.mapper.StoreProductAttrMapper;
import co.yixiang.modules.shop.service.mapper.StoreProductMapper;
import co.yixiang.utils.BeanUtils;
import co.yixiang.utils.FileUtil;
import co.yixiang.utils.OrderUtil;
import co.yixiang.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

// 默认不使用缓存
//import org.springframework.cache.annotation.CacheConfig;
//import org.springframework.cache.annotation.CacheEvict;
//import org.springframework.cache.annotation.Cacheable;

/**
 * @author hupeng
 * @date 2020-05-12
 */
@Service
@AllArgsConstructor
//@CacheConfig(cacheNames = "yxStoreProduct")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YxStoreProductServiceImpl extends BaseServiceImpl<StoreProductMapper, YxStoreProduct> implements YxStoreProductService {

    private final IGenerator generator;

    @Autowired
    private StoreProductMapper storeProductMapper;
    @Autowired
    private YxStoreCategoryService yxStoreCategoryService;
    @Autowired
    private YxStoreProductAttrService yxStoreProductAttrService;
    @Autowired
    private YxStoreProductAttrValueService yxStoreProductAttrValueService;
    @Autowired
    private YxStoreProductAttrResultService yxStoreProductAttrResultService;
    @Autowired
    private YxStoreInfoService yxStoreInfoService;
    @Autowired
    private UserService userService;

    @Autowired
    private StoreProductAttrMapper storeProductAttrMapper;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(YxStoreProductQueryCriteria criteria, Pageable pageable) {
//        getPage(pageable);
//        PageInfo<YxStoreProduct> page = new PageInfo<>(queryAll(criteria));
        QueryWrapper<YxStoreProduct> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().orderByDesc(YxStoreProduct::getAddTime);
        if (0 != criteria.getUserRole()) {
            if (null == criteria.getChildUser() || criteria.getChildUser().size() <= 0) {
                Map<String, Object> map = new LinkedHashMap<>(2);
                map.put("content", new ArrayList<>());
                map.put("totalElements", 0);
                return map;
            }
            queryWrapper.lambda().in(YxStoreProduct::getMerId, criteria.getChildUser());
        }
        if (null != criteria.getIsDel()) {
            queryWrapper.lambda().eq(YxStoreProduct::getIsDel, criteria.getIsDel());
        }
        if (null != criteria.getIsShow()) {
            queryWrapper.lambda().eq(YxStoreProduct::getIsShow, criteria.getIsShow());
        }
        if (StringUtils.isNotBlank(criteria.getStoreName())) {
            queryWrapper.lambda().like(YxStoreProduct::getStoreName, criteria.getStoreName());
        }
        IPage<YxStoreProduct> ipage = this.page(new Page<>(pageable.getPageNumber() + 1, pageable.getPageSize()), queryWrapper);
        List<YxStoreProduct> productList = getStoreName(ipage.getRecords());
        Map<String, Object> map = new LinkedHashMap<>(2);

        map.put("content", generator.convert(productList, YxStoreProductDto.class));
        map.put("totalElements", ipage.getTotal());
//        int sysUserId = SecurityUtils.getUserId().intValue();
//        User userSys = userService.getById(sysUserId);
//        int showFlg =0;
//        if(userSys.getUserRole().equals(1)){
//            //平台用户
//            showFlg = 1;
//        }
        map.put("showFlg", 0);

        return map;
    }


    @Override
    //@Cacheable
    public List<YxStoreProduct> queryAll(YxStoreProductQueryCriteria criteria) {
        List<YxStoreProduct> yxStoreProductList = baseMapper.selectList(QueryHelpPlus.getPredicate(YxStoreProduct.class, criteria));
        yxStoreProductList.forEach(yxStoreProduct -> {
            yxStoreProduct.setStoreCategory(yxStoreCategoryService.getById(yxStoreProduct.getCateId()));
            YxStoreInfoDto yxStoreInfoDto = new YxStoreInfoDto();
            YxStoreInfo yxStoreInfo = yxStoreInfoService.getById(yxStoreProduct.getStoreId());
            if (ObjectUtil.isNotNull(yxStoreInfo)) {
                BeanUtils.copyProperties(yxStoreInfo, yxStoreInfoDto);
            }
            yxStoreProduct.setStore(yxStoreInfoDto);
        });
        return yxStoreProductList;
    }

    public List<YxStoreProduct> getStoreName(List<YxStoreProduct> storeProductList) {
        storeProductList.forEach(yxStoreProduct -> {
            yxStoreProduct.setStoreCategory(yxStoreCategoryService.getById(yxStoreProduct.getCateId()));
            YxStoreInfoDto yxStoreInfoDto = new YxStoreInfoDto();
            YxStoreInfo yxStoreInfo = yxStoreInfoService.getById(yxStoreProduct.getStoreId());
            if (ObjectUtil.isNotNull(yxStoreInfo)) {
                BeanUtils.copyProperties(yxStoreInfo, yxStoreInfoDto);
            }
            yxStoreProduct.setStore(yxStoreInfoDto);
        });
        return storeProductList;
    }

    @Override
    public void download(List<YxStoreProductDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YxStoreProductDto yxStoreProduct : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("商户Id(0为总后台管理员创建,不为0的时候是商户后台创建)", yxStoreProduct.getMerId());
            map.put("商品图片", yxStoreProduct.getImage());
            map.put("轮播图", yxStoreProduct.getSliderImage());
            map.put("商品名称", yxStoreProduct.getStoreName());
            map.put("商品简介", yxStoreProduct.getStoreInfo());
            map.put("关键字", yxStoreProduct.getKeyword());
            map.put("产品条码（一维码）", yxStoreProduct.getBarCode());
            map.put("分类id", yxStoreProduct.getCateId());
            map.put("商品价格", yxStoreProduct.getPrice());
            map.put("会员价格", yxStoreProduct.getVipPrice());
            map.put("市场价", yxStoreProduct.getOtPrice());
            map.put("邮费", yxStoreProduct.getPostage());
            map.put("单位名", yxStoreProduct.getUnitName());
            map.put("排序", yxStoreProduct.getSort());
            map.put("销量", yxStoreProduct.getSales());
            map.put("库存", yxStoreProduct.getStock());
            map.put("状态（0：未上架，1：上架）", yxStoreProduct.getIsShow());
            map.put("是否热卖", yxStoreProduct.getIsHot());
            map.put("是否优惠", yxStoreProduct.getIsBenefit());
            map.put("是否精品", yxStoreProduct.getIsBest());
            map.put("是否新品", yxStoreProduct.getIsNew());
            map.put("产品描述", yxStoreProduct.getDescription());
            map.put("添加时间", yxStoreProduct.getAddTime());
            map.put("是否包邮", yxStoreProduct.getIsPostage());
            map.put("是否删除", yxStoreProduct.getIsDel());
            map.put("商户是否代理 0不可代理1可代理", yxStoreProduct.getMerUse());
            map.put("获得积分", yxStoreProduct.getGiveIntegral());
            map.put("成本价", yxStoreProduct.getCost());
            map.put("秒杀状态 0 未开启 1已开启", yxStoreProduct.getIsSeckill());
            map.put("砍价状态 0未开启 1开启", yxStoreProduct.getIsBargain());
            map.put("是否优品推荐", yxStoreProduct.getIsGood());
            map.put("虚拟销量", yxStoreProduct.getFicti());
            map.put("浏览量", yxStoreProduct.getBrowse());
            map.put("产品二维码地址(用户小程序海报)", yxStoreProduct.getCodePath());
            map.put("淘宝京东1688类型", yxStoreProduct.getSoureLink());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public YxStoreProduct saveProduct(YxStoreProduct storeProduct) {
        if (storeProduct.getStoreCategory().getId() == null) {
            throw new BadRequestException("分类名称不能为空");
        }
        boolean check = yxStoreCategoryService
                .checkProductCategory(storeProduct.getStoreCategory().getId());
        if (!check) throw new BadRequestException("商品分类必选选择二级");
        storeProduct.setCateId(storeProduct.getStoreCategory().getId().toString());
        this.save(storeProduct);
        return storeProduct;
    }

    @Override
    public void recovery(Integer id) {
        storeProductMapper.updateDel(0, id);
        storeProductMapper.updateOnsale(0, id);
    }

    @Override
    public void onSale(Integer id, int status) {
        if (status == 1) {
            status = 0;
        } else {
            status = 1;
        }
        storeProductMapper.updateOnsale(status, id);
    }

    @Override
    public List<ProductFormatDto> isFormatAttr(Integer id, String jsonStr) {
        if (ObjectUtil.isNull(id)) throw new BadRequestException("产品不存在");

        YxStoreProductDto yxStoreProductDTO = generator.convert(this.getById(id), YxStoreProductDto.class);
        DetailDto detailDTO = attrFormat(jsonStr);
        List<ProductFormatDto> newList = new ArrayList<>();
        for (LinkedHashMap<String, Map<String, String>> map : detailDTO.getRes()) {
            ProductFormatDto productFormatDTO = new ProductFormatDto();
            productFormatDTO.setDetail(map.get("detail"));
            List<String> stringList = productFormatDTO.getDetail().values()
                    .stream().collect(Collectors.toList());
            Collections.sort(stringList);
            String sku = "";
            sku = StrUtil.join(",", stringList);
            if (!"".equals(sku)) {
                YxStoreProductAttrValue getProductAttrValue = yxStoreProductAttrValueService.getOne(new LambdaQueryWrapper<YxStoreProductAttrValue>().eq(YxStoreProductAttrValue::getSuk, sku));
                if (getProductAttrValue != null) {
                    productFormatDTO.setCost(getProductAttrValue.getCost().doubleValue());
                    productFormatDTO.setPrice(getProductAttrValue.getPrice().doubleValue());
                    productFormatDTO.setSales(getProductAttrValue.getStock());
                    productFormatDTO.setPic(getProductAttrValue.getImage());
                    productFormatDTO.setCommission(getProductAttrValue.getCommission().doubleValue());
                    productFormatDTO.setCheck(false);
                } else {
                    sku = "";
                }
            }
            if ("".equals(sku)) {
                productFormatDTO.setCost(yxStoreProductDTO.getCost().doubleValue());
                productFormatDTO.setPrice(yxStoreProductDTO.getPrice().doubleValue());
                productFormatDTO.setPic(yxStoreProductDTO.getImage());
//                productFormatDTO.setSales(yxStoreProductDTO.getStock());
                productFormatDTO.setCommission(yxStoreProductDTO.getCommission().doubleValue());

                productFormatDTO.setCheck(false);
            }
            newList.add(productFormatDTO);
        }
        return newList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createProductAttr(Integer id, String jsonStr) {
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        //System.out.println(jsonObject);
        List<FromatDetailDto> attrList = JSON.parseArray(
                jsonObject.get("items").toString(),
                FromatDetailDto.class);
        List<ProductFormatDto> valueList = JSON.parseArray(
                jsonObject.get("attrs").toString(),
                ProductFormatDto.class);
        YxStoreProduct yxStoreProductParam = this.getById(id);
        List<YxStoreProductAttr> attrGroup = new ArrayList<>();
        for (FromatDetailDto fromatDetailDTO : attrList) {
            YxStoreProductAttr yxStoreProductAttr = new YxStoreProductAttr();
            yxStoreProductAttr.setProductId(id);
            yxStoreProductAttr.setAttrName(fromatDetailDTO.getValue());
            yxStoreProductAttr.setAttrValues(StrUtil.
                    join(",", fromatDetailDTO.getDetail()));
            attrGroup.add(yxStoreProductAttr);
        }


        List<YxStoreProductAttrValue> valueGroup = new ArrayList<>();
        for (ProductFormatDto productFormatDTO : valueList) {
            YxStoreProductAttrValue yxStoreProductAttrValue = new YxStoreProductAttrValue();
            yxStoreProductAttrValue.setProductId(id);
            //productFormatDTO.getDetail().values().stream().collect(Collectors.toList());
            List<String> stringList = productFormatDTO.getDetail().values()
                    .stream().collect(Collectors.toList());
            Collections.sort(stringList);
            yxStoreProductAttrValue.setSuk(StrUtil.
                    join(",", stringList));
            if(null==productFormatDTO.getPrice()||null==productFormatDTO.getCost()||null==productFormatDTO.getCommission()){
                throw new BadRequestException("价格不能为空！!");
            }
            yxStoreProductAttrValue.setPrice(BigDecimal.valueOf(productFormatDTO.getPrice()));
            yxStoreProductAttrValue.setCost(BigDecimal.valueOf(productFormatDTO.getCost()));
            yxStoreProductAttrValue.setStock(productFormatDTO.getSales());
            yxStoreProductAttrValue.setUnique(IdUtil.simpleUUID());
            yxStoreProductAttrValue.setImage(productFormatDTO.getPic());
            //佣金
            yxStoreProductAttrValue.setCommission(BigDecimal.valueOf(productFormatDTO.getCommission()));

            valueGroup.add(yxStoreProductAttrValue);
        }

        if (attrGroup.isEmpty() || valueGroup.isEmpty()) {
            throw new BadRequestException("请设置至少一个属性!");
        }

        //如果设置sku 处理价格与库存

        ////取最小价格
        BigDecimal minPrice = valueGroup
                .stream()
                .map(YxStoreProductAttrValue::getPrice)
                .min(Comparator.naturalOrder())
                .orElse(BigDecimal.ZERO);

        //计算库存
        Integer stock = valueGroup
                .stream()
                .map(YxStoreProductAttrValue::getStock)
                .reduce(Integer::sum)
                .orElse(0);

        //
        YxStoreProduct yxStoreProduct = this.getById(id);
        if (stock > yxStoreProduct.getStock()) {
            throw new BadRequestException("库存不能大于产品库存数" + yxStoreProduct.getStock() + "！");
        }
        /*YxStoreProduct yxStoreProduct = YxStoreProduct.builder()
                .stock(stock)
                .price(minPrice)
                .id(id)
                .build();*/
        this.updateById(yxStoreProduct);

        //插入之前清空
        clearProductAttr(id, false);

        //保存属性
        yxStoreProductAttrService.saveOrUpdateBatch(attrGroup);

        //保存值
        yxStoreProductAttrValueService.saveOrUpdateBatch(valueGroup);

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("attr", jsonObject.get("items"));
        map.put("value", jsonObject.get("attrs"));

        //保存结果
        setResult(map, id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setResult(Map<String, Object> map, Integer id) {
        YxStoreProductAttrResult yxStoreProductAttrResult = new YxStoreProductAttrResult();
        yxStoreProductAttrResult.setProductId(id);
        yxStoreProductAttrResult.setResult(JSON.toJSONString(map));
        yxStoreProductAttrResult.setChangeTime(OrderUtil.getSecondTimestampTwo());

        yxStoreProductAttrResultService.remove(new QueryWrapper<YxStoreProductAttrResult>().eq("product_id", id));

        yxStoreProductAttrResultService.saveOrUpdate(yxStoreProductAttrResult);
    }

    @Override
    public String getStoreProductAttrResult(Integer id) {
        YxStoreProductAttrResult yxStoreProductAttrResult = yxStoreProductAttrResultService
                .getOne(new QueryWrapper<YxStoreProductAttrResult>().eq("product_id", id));
        if (ObjectUtil.isNull(yxStoreProductAttrResult)) return "";
        return yxStoreProductAttrResult.getResult();
    }

    @Override
    public void updateProduct(YxStoreProduct resources) {
        int sumStock = storeProductAttrMapper.getStocketByProductId(resources.getId());
        if (resources.getStock() < sumStock) {
            throw new BadRequestException("库存数不能小于规格库存总数！");
        }
        if (resources.getStoreCategory() == null || resources.getStoreCategory().getId() == null)
            throw new BadRequestException("请选择分类");
        boolean check = yxStoreCategoryService
                .checkProductCategory(resources.getStoreCategory().getId());
        if (!check) throw new BadRequestException("商品分类必选选择二级");
        resources.setCateId(resources.getStoreCategory().getId().toString());
        //
        resources.setCommission(resources.getPrice().subtract(resources.getSettlement()));
        //删除
        this.saveOrUpdate(resources);
    }

    @Override
    public void delete(Integer id) {
        storeProductMapper.updateDel(1, id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void clearProductAttr(Integer id, boolean isActice) {
        if (ObjectUtil.isNull(id)) throw new BadRequestException("产品不存在");

        yxStoreProductAttrService.remove(new QueryWrapper<YxStoreProductAttr>().eq("product_id", id));
        yxStoreProductAttrValueService.remove(new QueryWrapper<YxStoreProductAttrValue>().eq("product_id", id));

        if (isActice) {
            yxStoreProductAttrResultService.remove(new QueryWrapper<YxStoreProductAttrResult>().eq("product_id", id));
        }
    }

    /**
     * 组合规则属性算法
     * @param jsonStr
     * @return
     */
    public DetailDto attrFormat(String jsonStr) {
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        List<FromatDetailDto> fromatDetailDTOList = JSON.parseArray(jsonObject.get("items").toString(),
                FromatDetailDto.class);
        List<String> data = new ArrayList<>();
        List<LinkedHashMap<String, Map<String, String>>> res = new ArrayList<>();
        if (fromatDetailDTOList.size() > 1) {
            for (int i = 0; i < fromatDetailDTOList.size() - 1; i++) {
                if (i == 0) data = fromatDetailDTOList.get(i).getDetail();
                List<String> tmp = new LinkedList<>();
                for (String v : data) {
                    for (String g : fromatDetailDTOList.get(i + 1).getDetail()) {
                        String rep2 = "";
                        if (i == 0) {
                            rep2 = fromatDetailDTOList.get(i).getValue() + "_" + v + "-"
                                    + fromatDetailDTOList.get(i + 1).getValue() + "_" + g;
                        } else {
                            rep2 = v + "-"
                                    + fromatDetailDTOList.get(i + 1).getValue() + "_" + g;
                        }
                        tmp.add(rep2);
                        if (i == fromatDetailDTOList.size() - 2) {
                            LinkedHashMap<String, Map<String, String>> rep4 = new LinkedHashMap<>();
                            Map<String, String> reptemp = new LinkedHashMap<>();
                            for (String h : Arrays.asList(rep2.split("-"))) {
                                List<String> rep3 = Arrays.asList(h.split("_"));

                                if (rep3.size() > 1) {
                                    reptemp.put(rep3.get(0), rep3.get(1));
                                } else {
                                    reptemp.put(rep3.get(0), "");
                                }
                            }
                            rep4.put("detail", reptemp);
                            res.add(rep4);
                        }
                    }
                }
                //System.out.println("tmp:"+tmp);
                if (!tmp.isEmpty()) {
                    data = tmp;
                }
            }
        } else {
            List<String> dataArr = new ArrayList<>();

            for (FromatDetailDto fromatDetailDTO : fromatDetailDTOList) {

                for (String str : fromatDetailDTO.getDetail()) {
                    LinkedHashMap<String, Map<String, String>> map2 = new LinkedHashMap<>();
                    //List<Map<String,String>> list1 = new ArrayList<>();
                    dataArr.add(fromatDetailDTO.getValue() + "_" + str);
                    Map<String, String> map1 = new LinkedHashMap<>();
                    map1.put(fromatDetailDTO.getValue(), str);
                    //list1.add(map1);
                    map2.put("detail", map1);
                    res.add(map2);
                }
            }
            String s = StrUtil.join("-", dataArr);
            data.add(s);
        }
        DetailDto detailDTO = new DetailDto();
        detailDTO.setData(data);
        detailDTO.setRes(res);
        return detailDTO;
    }

    /**
     *
     * @param request
     */
    @Override
    public void changeStatus(YxStoreProductChange request) {
        YxStoreProduct yxStoreProduct = this.getById(request.getId());
        // 0:是，1：否
        switch (request.getChangeType()) {
            case "benefit":
                yxStoreProduct.setIsBenefit(request.getChangeStatus());
                break;
            case "best":
                yxStoreProduct.setIsBest(request.getChangeStatus());
                break;
            case "hot":
                yxStoreProduct.setIsHot(request.getChangeStatus());
                break;
            case "new":
                yxStoreProduct.setIsNew(request.getChangeStatus());
                break;
        }
        this.updateById(yxStoreProduct);
    }

}
