<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <!--如果想在工具栏加入更多按钮，可以使用插槽方式， slot = 'left' or 'right'-->
      <el-input v-model="query.seriesName" clearable size="small" placeholder="请输入系列名称" style="width: 200px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
      <el-button class="filter-item" size="mini" type="success" icon="el-icon-search" @click="crud.toQuery">搜索</el-button>
      <crudOperation :permission="permission" />
      <!--表单组件-->
      <el-dialog :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="540px">
        <el-form ref="form" :model="form" :rules="rules" size="small" label-width="110px">
          <el-form-item label="系列名称" prop="seriesName">
            <el-input v-model="form.seriesName" style="width: 370px;" maxlength="15"/>
          </el-form-item>
          <el-form-item label="船只类别" prop="shipCategory">
           <el-select v-model="form.shipCategory" placeholder="请选择" style="width: 370px;">
                <el-option
                  v-for="item in dict.ship_category"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
          </el-form-item>
          <el-form-item label="限乘人数" prop="rideLimit">
            <el-input-number v-model="form.rideLimit" style="width: 370px;" :max="1000" :min="0"
            @change="()=>{$refs.form.validateField('rideLimit')}"/>
          </el-form-item>
          <el-form-item label="尺寸" prop="shipSize">
            <el-input v-model="form.shipSize" style="width: 370px;" maxlength="15"/>
          </el-form-item>
          <el-form-item label="乘船省市区" prop="shipProvince">
<!--            <el-input v-model="form.shipProvince" style="width: 370px;" />-->
            <el-cascader
              :options="options"
              v-model="selectedOptions"
              @change='selectedProvince' style="width: 370px;" >
            </el-cascader>
          </el-form-item>
          <el-form-item label="乘船地址" prop="shipAddress">
            <el-input v-model="form.shipAddress" style="width: 370px;" maxlength="50"
                      @change="codeAddress"/>
          </el-form-item>
          <el-form-item label=" ">
            <div id="mapContainer" style="width:370px;height:300px;"></div>
          </el-form-item>
          <el-form-item label="帆船状态" prop="status">
            <el-radio-group v-model="form.status">
              <el-radio :label="0">启用</el-radio>
              <el-radio :label="1">禁用</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="地图坐标经度" v-show="false">
            <el-input v-model.number="form.coordinateX"/>
          </el-form-item>
          <el-form-item label="地图坐标纬度" v-show="false">
            <el-input v-model.number="form.coordinateY"/>
          </el-form-item>
          <el-form-item label=" " v-show="false">
            <el-input v-model="form.merId"/>
          </el-form-item>
          <el-form-item label=" " v-show="false">
            <el-input v-model="form.storeId"/>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="text" @click="crud.cancelCU">取消</el-button>
          <el-button :loading="crud.cu === 2" type="primary" @click="crud.submitCU">确认</el-button>
        </div>
      </el-dialog>
      <!--表格渲染-->
      <el-table ref="table" v-loading="crud.loading" :data="crud.data" size="small" style="width: 100%;"
                @selection-change="crud.selectionChangeHandler">
        <el-table-column type="selection" width="55" />
        <el-table-column v-if="columns.visible('seriesName')" prop="seriesName" label="系列名称" />
        <el-table-column v-if="columns.visible('status')" prop="status" label="状态" >
          <template slot-scope="scope">
            <span>{{ scope.row.status?'禁用':'启用' }}</span>
          </template>
        </el-table-column>
        <el-table-column v-permission="permission.edit" label="操作" width="200px" align="center">
          <template slot-scope="scope">
            <!-- <el-button size="mini" type="text" icon="el-icon-edit"
                       @click="crud.toEdit(scope.row)" >修改</el-button>
            <el-divider direction="vertical"></el-divider>
            <el-button size="mini" type="text" icon="el-icon-edit"
                       @click="editStatus(scope.row)" >{{ scope.row.status?'启用':'禁用' }}</el-button>
            <el-divider direction="vertical"></el-divider> -->
            <div class="flexs">
              <app-link :to="resolvePath('/ship/shipInfo?seriesId='+scope.row.id)">
                <el-button size="small" type="primary"  style='margin-right:5px;padding:9px 15px;' plain  >船只管理</el-button>
              </app-link>

              <el-dropdown trigger="click" style="margin-top:10px padding:9px 15px;" placement="bottom">
              <el-button type="primary" plain size="small" style="padding-left:10px;padding-right:10px;">
              更多操作<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
                  <el-dropdown-menu slot="dropdown">          
                      <el-dropdown-item >
                          <el-button  size="mini" type="primary" icon="el-icon-edit"  @click="crud.toEdit(scope.row)" plain>修改</el-button>
                      </el-dropdown-item >
                      <el-dropdown-item  >
                          <el-button  style="marginTop:5px;" size="mini" type="primary" icon="el-icon-edit" @click="editStatus(scope.row)"  plain>{{ scope.row.status?'启用':'禁用' }}</el-button>
                      </el-dropdown-item>
                  
                  </el-dropdown-menu>
             </el-dropdown>          
            </div>
          </template>
        </el-table-column>
      </el-table>
      <!--分页组件-->
      <pagination />
    </div>
  </div>
</template>

<script>
import crudYxShipSeries, { changeStatus } from '@/api/yxShipSeries'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import udOperation from '@crud/UD.operation'
import pagination from '@crud/Pagination'
import MaterialList from "@/components/material";
import storeMark from "@/assets/images/store_mark.png"
import {  regionData,   CodeToText } from 'element-china-area-data'
import { Notification,Loading } from 'element-ui'
import path from 'path'
import { isExternal } from '@/utils/validate'
import AppLink from '@/layout/components/Sidebar/Link'

// crud交由presenter持有
const defaultCrud = CRUD({ title: '船只系列', url: 'api/yxShipSeries', sort: 'id,desc',
  crudMethod: { ...crudYxShipSeries },optShow:{
    add: true,
    edit: false,
    del: false,
    download: false
  }, query: {
    seriesName:''
  }
})
const defaultForm = { id: null, seriesName: null, shipCategory: null, rideLimit: null, shipSize: null,
  status: 1, shipProvince: null, shipAddress: null,  coordinateX: null, coordinateY: null, delFlag: null,
  createUserId: null, updateUserId: null, createTime: null, updateTime: null, merId:null, storeId:null }
export default {
  name: 'YxShipSeries',
  components: { pagination, crudOperation, rrOperation, udOperation ,MaterialList, AppLink},
  mixins: [presenter(defaultCrud), header(), form(defaultForm), crud()],
  dicts:['ship_category'],
  data() {
    return {
      map:null,
      geocoder: null,
      permission: {
        add: ['admin', 'yxShipSeries:add'],
        edit: ['admin', 'yxShipSeries:edit'],
        del: ['admin', 'yxShipSeries:del']
      },
      rules: {
        seriesName: [
          { required: true, message: '系列名称不能为空', trigger: 'blur' }
        ],
        shipCategory: [
          { required: true, message: '船只类别不能为空', trigger: 'blur' }
        ],
        rideLimit: [
          { required: true, message: '限乘人数不能为空', trigger: 'blur' }
        ],
        shipSize: [
          { required: true, message: '尺寸不能为空', trigger: 'blur' }
        ],
        status: [
          { required: true, message: '状态不能为空', trigger: 'blur' }
        ],
        shipProvince: [
          { required: true, message: '乘船省市区不能为空', trigger: 'change' }
        ],
        shipAddress: [
          { required: true, message: '乘船地址不能为空', trigger: 'blur' }
        ],
      },
      options: regionData,
      selectedOptions: [],
      shipProvinceTest:'',
      basePath:'',
      mapLoading:'',
    }
  },
  watch: {
  },
  methods: {
    // 初始化地图
    initMap() {
      const that = this;
      // 设置一个基础中心点
      const lat = that.form.coordinateY ||116.397128;
      const lng = that.form.coordinateX|| 39.916527;
      const center = new qq.maps.LatLng(lng,lat);
      const map = new qq.maps.Map(document.getElementById('mapContainer'),{
        center: center,
        zoom: 15,
        // 关闭控制选项和功能
        draggable: true,
        scrollwheel: false,
        disableDoubleClickZoom: false,
        keyboardShortcuts:false,
        mapTypeControl: false,
        panControl: false,
        zoomControl: false,
      });
      // 保存地图
      that.map = map;
      var middleControl = document.createElement("div");
      middleControl.className = "store-mark";
      middleControl.innerHTML ='<img src="'+storeMark+'" />';
      document.getElementById("mapContainer").appendChild(middleControl);
      let timer = null;
      qq.maps.event.addListener(that.map, 'center_changed', function() {
        const center = map.getCenter();
        if(timer){
          clearTimeout(timer);
        }
        timer = setTimeout(()=>{
          that.form.coordinateY = center.lat;
          that.form.coordinateX = center.lng;
        },200)

      });
      //调用地址解析类
      that.geocoder = new qq.maps.Geocoder({
        complete : result=>{
          console.log(result)
          const { location } = result.detail;
          that.map.setCenter(location);
          // 设置经纬度
          that.form.coordinateY = location.lat;
          that.form.coordinateX = location.lng;
          that.mapLoading.close()
        }
      });
      // 有经纬度跳到地址
      if(that.form.coordinateY){
        // this.mapLoading=Loading.service({
        //   target:'#mapContainer'
        // });
        map.panTo(new qq.maps.LatLng(that.form.coordinateY, that.form.coordinateX))
      }else{
        // 无经纬度跳到地址的经纬度
        that.codeAddress();
      }

    },
    codeAddress() {
      const that = this;
      //通过getLocation();方法获取位置信息值
      if(that.geocoder){
        if(this.shipProvinceTest!==''){
          this.mapLoading=Loading.service({
            target:'#mapContainer'
          });
          that.geocoder.getLocation(this.shipProvinceTest + this.form.shipAddress);
        }
      }

    },
    // 获取数据前设置好接口地址
    [CRUD.HOOK.beforeRefresh]() {
      return true
    }, // 新增与编辑前做的操作
    [CRUD.HOOK.afterToCU](crud, form) {
      this.map = null;
      this.geocoder = null;
      if(form.shipProvince){
        this.selectedOptions=form.shipProvince.split(',')
        let pcrt=[]
        this.selectedOptions.forEach(item=>{
          pcrt.push(CodeToText[item])
        })
        this.shipProvinceTest=pcrt.join('')
      }else{
        this.selectedOptions=[]
        this.shipProvinceTest=''
      }
      if(form.shipCategory){
        form.shipCategory=form.shipCategory.toString()
      }
      this.$nextTick(()=>{
        document.getElementById('mapContainer').innerHTML = "";
        this.initMap()
      })
    },
    selectedProvince(val){
      let pcr=[],pcrt=[]
      val.forEach(item=>{
        pcr.push(item)
        pcrt.push(CodeToText[item])
      })
      this.form.shipProvince=pcr.join(',')
      this.shipProvinceTest=pcrt.join('')
    },
    editStatus(row){
      let that=this
      changeStatus(row.id).then(res=>{
        let t='已'+ (row.status?'启用':'禁用')
        Notification.success({
          title: t
        })
        that.crud.refresh()
      })
    },
    resolvePath(routePath) {
      if (isExternal(routePath)) {
        return routePath
      }
      if (isExternal(this.basePath)) {
        return this.basePath
      }
      return path.resolve(this.basePath, routePath)
    }
  }
}



</script>

<style scoped>
  .table-img {
    display: inline-block;
    text-align: center;
    background: #ccc;
    color: #fff;
    white-space: nowrap;
    position: relative;
    overflow: hidden;
    vertical-align: middle;
    width: 32px;
    height: 32px;
    line-height: 32px;
  }
  .flexs{
    display: flex;
    flex-direction: row;
    align-items: center;
  }
</style>
