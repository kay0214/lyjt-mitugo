package co.yixiang.modules.image.service.impl;

import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.modules.image.entity.YxImageInfo;
import co.yixiang.modules.image.mapper.YxImageInfoMapper;
import co.yixiang.modules.image.service.YxImageInfoService;
import co.yixiang.modules.image.web.param.YxImageInfoQueryParam;
import co.yixiang.modules.image.web.vo.YxImageInfoQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>
 * 图片表 服务实现类
 * </p>
 *
 * @author hupeng
 * @since 2020-08-13
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class YxImageInfoServiceImpl extends BaseServiceImpl<YxImageInfoMapper, YxImageInfo> implements YxImageInfoService {

    @Autowired
    private YxImageInfoMapper yxImageInfoMapper;

    @Override
    public YxImageInfoQueryVo getYxImageInfoById(Serializable id) throws Exception{
        return yxImageInfoMapper.getYxImageInfoById(id);
    }

    @Override
    public Paging<YxImageInfoQueryVo> getYxImageInfoPageList(YxImageInfoQueryParam yxImageInfoQueryParam) throws Exception{
        Page page = setPageParam(yxImageInfoQueryParam,OrderItem.desc("create_time"));
        IPage<YxImageInfoQueryVo> iPage = yxImageInfoMapper.getYxImageInfoPageList(page,yxImageInfoQueryParam);
        return new Paging(iPage);
    }

    /**
     * 获取图片信息返回list
     * @param typeId
     * @param imgType
     * @param cateTypeId
     * @return
     */
    @Override
    public List<String> selectImgByParamList(int typeId, Integer imgType, Integer cateTypeId) {
        YxImageInfo imageInfoParam = new YxImageInfo();
        imageInfoParam.setTypeId(typeId);
        imageInfoParam.setImgType(imgType);
        imageInfoParam.setImgCategory(cateTypeId);
        List<String> images = new ArrayList<>();
        List<YxImageInfo> imageInfoList = list(Wrappers.query(imageInfoParam));
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(imageInfoList)) {
            for (YxImageInfo imageInfo : imageInfoList) {
                images.add(imageInfo.getImgUrl());
            }
        }
        return images;
    }
    /**
     * 获取图片信息,返回逗号拼接的字符
     * @param typeId
     * @param imgType
     * @param cateTypeId
     * @return
     */
    @Override
    public String selectImgByParam(int typeId, Integer imgType, Integer cateTypeId) {
        YxImageInfo imageInfoParam = new YxImageInfo();
        imageInfoParam.setTypeId(typeId);
        imageInfoParam.setImgType(imgType);
        imageInfoParam.setImgCategory(cateTypeId);
        imageInfoParam.setDelFlag(0);
        String strImg = "";
        List<YxImageInfo> imageInfoList = list(Wrappers.query(imageInfoParam));
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(imageInfoList)) {
            for (YxImageInfo imageInfo : imageInfoList) {
                strImg = strImg.concat(imageInfo.getImgUrl()).concat(",");
            }
            strImg = strImg.substring(0, strImg.length() - 1);
        }
        return strImg;
    }


    /**
     * @return
     */
    @Override
    public YxImageInfo selectOneImg(int typeId, Integer imgType, Integer cateTypeId) {

        List<YxImageInfo> yxImageInfoList = yxImageInfoMapper.selectList(new QueryWrapper<YxImageInfo>().lambda().eq(YxImageInfo::getTypeId, typeId).eq(YxImageInfo::getImgType, imgType).eq(YxImageInfo::getImgCategory, cateTypeId).eq(YxImageInfo::getDelFlag,0));
        if(null == yxImageInfoList||yxImageInfoList.size()<=0){
            return null;
        }
        return yxImageInfoList.get(0);
    }
}
