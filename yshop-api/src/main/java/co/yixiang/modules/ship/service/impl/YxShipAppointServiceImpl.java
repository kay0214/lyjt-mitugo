package co.yixiang.modules.ship.service.impl;

import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.modules.couponUse.dto.YxShipAppointResultVo;
import co.yixiang.modules.couponUse.dto.YxShipAppointVo;
import co.yixiang.modules.couponUse.param.ShipInAppotionDaysParam;
import co.yixiang.modules.couponUse.param.ShipInAppotionParam;
import co.yixiang.modules.manage.entity.SystemUser;
import co.yixiang.modules.ship.entity.YxShipAppoint;
import co.yixiang.modules.ship.entity.YxShipAppointDetail;
import co.yixiang.modules.ship.entity.YxShipInfo;
import co.yixiang.modules.ship.mapper.YxShipAppointMapper;
import co.yixiang.modules.ship.service.YxShipAppointDetailService;
import co.yixiang.modules.ship.service.YxShipAppointService;
import co.yixiang.modules.ship.service.YxShipInfoService;
import co.yixiang.modules.ship.web.param.YxShipAppointQueryParam;
import co.yixiang.modules.ship.web.vo.YxShipAppointQueryVo;
import co.yixiang.modules.user.entity.YxLeaveMessage;
import co.yixiang.modules.user.entity.YxUser;
import co.yixiang.modules.user.service.YxLeaveMessageService;
import co.yixiang.modules.user.service.YxUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * <p>
 * 船只预约表 服务实现类
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class YxShipAppointServiceImpl extends BaseServiceImpl<YxShipAppointMapper, YxShipAppoint> implements YxShipAppointService {

    @Autowired
    private YxShipAppointMapper yxShipAppointMapper;

    @Autowired
    private YxShipInfoService yxShipInfoService;
    @Autowired
    private YxShipAppointDetailService yxShipAppointDetailService;
    @Autowired
    private YxLeaveMessageService yxLeaveMessageService;
    @Autowired
    private YxUserService yxUserService;

    @Override
    public YxShipAppointQueryVo getYxShipAppointById(Serializable id) throws Exception{
        return yxShipAppointMapper.getYxShipAppointById(id);
    }

    @Override
    public Paging<YxShipAppointQueryVo> getYxShipAppointPageList(YxShipAppointQueryParam yxShipAppointQueryParam) throws Exception{
        Page page = setPageParam(yxShipAppointQueryParam,OrderItem.desc("create_time"));
        IPage<YxShipAppointQueryVo> iPage = yxShipAppointMapper.getYxShipAppointPageList(page,yxShipAppointQueryParam);
        return new Paging(iPage);
    }

    @Override
    public List<String> getMonthAllDays(String strDate){
        List<String> listDate = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
        try {
            Date dateParam = simpleDateFormat.parse(strDate);

            int month=Integer.parseInt(new SimpleDateFormat("MM").format(dateParam));//取到月份
            int year = Integer.parseInt(new SimpleDateFormat("yyyy").format(dateParam));//年份

            Integer num = getDaysByYearMonth(year, month);
            //开始日期
            String strDateParamStart  = strDate+"-01";
            String strDateParamEnd  = strDate+"-"+num;
            List<String> monthDays = getDayList(strDateParamStart,strDateParamEnd);
            //上个月最后七天数据
            String lastDays = getCalculationDate(7,strDateParamStart,0);
            //下个月前七天数据
            String firstDays = getCalculationDate(7,strDateParamEnd,1);
            List<String> listLast = getDayList(lastDays,strDateParamStart);
            List<String> listFirst = getDayList(strDateParamEnd,firstDays);
            listDate.addAll(listLast);
            listDate.addAll(monthDays);
            listDate.addAll(listFirst);
            LinkedHashSet<String> hashSet = new LinkedHashSet<>(listDate);
            listDate = new ArrayList<>(hashSet);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return listDate;
    }
    /**
     * 根据年 月 获取对应的月份 天数
     */
    public int getDaysByYearMonth(int year, int month) {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month -1);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    public static String getCalculationDate(int past,String strDate,int flg) throws ParseException{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(strDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if(0==flg){
            calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - past);
        }else{
            calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + past);
        }
        Date dateCalcu = calendar.getTime();
        String result = sdf.format(dateCalcu);
        return result;
    }

    /**
     * 获取指定日期之间的所有日期
     * @param strDateParamStart
     * @param strDateParamEnd
     * @return
     * @throws ParseException
     */
    private List<String> getDayList(String strDateParamStart,String strDateParamEnd) throws ParseException{
        List<String> listDate = new ArrayList<>();
        //开始日期
        SimpleDateFormat smpFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar tempStart = Calendar.getInstance();
        Date dateParamStart = smpFormat.parse(strDateParamStart);
        tempStart.setTime(dateParamStart);
        //结束日期
        Calendar tempEnd = Calendar.getInstance();
        Date dateParamEnd = smpFormat.parse(strDateParamEnd);
        tempEnd.setTime(dateParamEnd);
        while (tempStart.before(tempEnd)) {
            listDate.add(smpFormat.format(tempStart.getTime()));
            tempStart.add(Calendar.DAY_OF_YEAR, 1);
        }
        listDate.add(strDateParamEnd);

        return listDate;
    }


    @Override
    public List<YxShipAppointResultVo> getAppointByDate(List<String> dateList,Integer storeId,Integer shipId) {
        List<YxShipAppointResultVo> resultVoList = new ArrayList<>();
        YxShipAppointQueryParam param = new YxShipAppointQueryParam();
        param.setDateList(dateList);
        List<Integer> shipIds = new ArrayList<>();
        if(null == shipId){
            shipIds = yxShipInfoService.shipIdList(null, storeId);
        }else{
            shipIds.add(shipId);
        }

        param.setShipIdList(shipIds);

        List<String> appointmentDateList = yxShipAppointMapper.getAppointmentDateByParam(param);
        for (String strDate : dateList) {
            YxShipAppointResultVo appointResultVo = new YxShipAppointResultVo();
            appointResultVo.setDateShow(strDate);
            int orderFlg = 0;
            if (!CollectionUtils.isEmpty(appointmentDateList)) {
                if(appointmentDateList.contains(strDate)){
                    orderFlg = 1;
                }
            }
            appointResultVo.setOrderFlg(orderFlg);
            resultVoList.add(appointResultVo);
        }
        return resultVoList;

    }

    /**
     * 新增船只预约
     * @param param
     * @param user
     * @return
     */
    @Transactional
    @Override
    public Map<String, Object> saveAppotionInfo(ShipInAppotionParam param, SystemUser user){
        Map<String, Object> map = new HashMap<>();
        YxShipAppoint shipAppoint = new YxShipAppoint();
        if(null!=param.getLeaveId()){
            //留言id
            YxLeaveMessage yxLeaveMessage = yxLeaveMessageService.getById(param.getLeaveId());
            if(null==yxLeaveMessage){
                map.put("status", "99");
                map.put("statusDesc", "根据留言id："+param.getLeaveId()+" 获取信息失败！");
                return map;
            }
            YxUser yxUser = yxUserService.getById(yxLeaveMessage.getCreateUserId());
            if(null==yxUser){
                map.put("status", "99");
                map.put("statusDesc", "根据留言用户id："+yxLeaveMessage.getCreateUserId()+" 获取信息失败！");
                return map;
            }
            param.setName(yxUser.getRealName());
            param.setPhone(yxUser.getPhone());

        }
        BeanUtils.copyProperties(param,shipAppoint);
        shipAppoint.setCreateTime(new Date());
        shipAppoint.setCreateUserId(user.getId().intValue());
        shipAppoint.setUpdateTime(new Date());
        shipAppoint.setUpdateUserId(user.getId().intValue());
        this.save(shipAppoint);

        if(!StringUtils.isEmpty(param.getShipIds())){
            String[] shipIds = param.getShipIds().split(",");
            List<YxShipAppointDetail> detailList = new ArrayList<>();
            for(int i=0;i<shipIds.length;i++){
                int intShipId = Integer.parseInt(shipIds[i]);
                YxShipInfo yxShipInfo = yxShipInfoService.getById(intShipId);
                YxShipAppointDetail yxShipAppointDetail = new YxShipAppointDetail();
                yxShipAppointDetail.setAppointId(shipAppoint.getId());
                yxShipAppointDetail.setShipId(yxShipInfo.getId());
                yxShipAppointDetail.setSeriesId(yxShipInfo.getSeriesId());
                yxShipAppointDetail.setCreateTime(new Date());
                yxShipAppointDetail.setCreateUserId(user.getId().intValue());
                yxShipAppointDetail.setUpdateTime(new Date());
                yxShipAppointDetail.setUpdateUserId(user.getId().intValue());
                detailList.add(yxShipAppointDetail);
            }
            yxShipAppointDetailService.saveBatch(detailList);
        }
        map.put("status", "1");
        map.put("statusDesc", "成功！");
        return map;
    }


    /**
     * 显示预约详情
     * @param param
     * @param user
     * @return
     */
    @Override
    public Map<String,Object> getAppotionByDate(ShipInAppotionDaysParam param,SystemUser user){
        Map<String,Object> map = new HashMap<>();
        YxShipAppointQueryParam yxShipAppointQueryParam = new YxShipAppointQueryParam();
        List<Integer> shipIds = new ArrayList<>();
        if(null == param.getShipId()){
            shipIds = yxShipInfoService.shipIdList(null, user.getStoreId());
        }else{
            shipIds.add(param.getShipId());
        }
        yxShipAppointQueryParam.setShipIdList(shipIds);
        List<String> listDates = new ArrayList<>();
        listDates.add(param.getDate());
        yxShipAppointQueryParam.setDateList(listDates);
        Page page = setPageParam(yxShipAppointQueryParam, OrderItem.desc("create_time"));
        IPage<YxShipAppointVo> shipOpertionPage = yxShipAppointMapper.getYxShipAppointPageByParam(page, yxShipAppointQueryParam);
        Paging<YxShipAppointVo> shipAppointQueryVoPaging = new Paging(shipOpertionPage);
        if (shipAppointQueryVoPaging.getTotal() > 0) {
            List<YxShipAppointVo> appointQueryVoList = shipAppointQueryVoPaging.getRecords();
            for(YxShipAppointVo appointVo:appointQueryVoList){
                appointVo.setShipNameList(getShipNameByAppotionId(appointVo.getId()));
            }
        }
        map.put("status", "1");
        map.put("statusDesc", "成功！");
        map.put("data", shipAppointQueryVoPaging);
        return map;
    }

    private List<String>  getShipNameByAppotionId(int appId){
        QueryWrapper<YxShipAppointDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(YxShipAppointDetail::getAppointId,appId).eq(YxShipAppointDetail::getDelFlag,0);
        List<YxShipAppointDetail> appointDetailList = yxShipAppointDetailService.list(queryWrapper);
        List<String> shipName = new ArrayList<>();
        if(CollectionUtils.isEmpty(appointDetailList)){
            return shipName;
        }
        for(YxShipAppointDetail appointDetail :appointDetailList){
            YxShipInfo shipInfo = yxShipInfoService.getById(appointDetail.getId());
            if(null!=shipInfo){
                shipName.add(shipInfo.getShipName());
            }
        }
        return shipName;
    }
}
