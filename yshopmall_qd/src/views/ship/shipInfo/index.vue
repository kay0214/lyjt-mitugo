<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <!--如果想在工具栏加入更多按钮，可以使用插槽方式， slot = 'left' or 'right'-->
      <el-input v-model="query.seriesName" clearable size="small" placeholder="请输入系列名称" style="width: 200px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
      <el-select v-model="query.seriesId" clearable placeholder="请选择船只系列" style="width: 200px;" class="filter-item">
        <el-option
          v-for="item in shipSeries"
          :key="item.id"
          :label="item.seriesName"
          :value="item.id">
        </el-option>
      </el-select>
      <el-select v-model="query.currentStatus" clearable placeholder="请选择船只当前状态" style="width: 200px;" class="filter-item">
        <el-option
          v-for="item in curStatus"
          :key="item.value"
          :label="item.label"
          :value="item.value">
        </el-option>
      </el-select>
      <el-button class="filter-item" size="mini" type="success" icon="el-icon-search" @click="crud.toQuery">搜索</el-button>
      <crudOperation :permission="permission" />
      <!--表单组件-->
      <el-dialog :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="560px">
        <el-form ref="form" :model="form" :rules="rules" size="small" label-width="120px">
         <!-- <el-form-item label="船只id">
            <el-input v-model="form.id" style="width: 370px;" />
          </el-form-item>-->
          <el-form-item label="船只系列" prop="seriesId">
            <!--            <el-input v-model="form.seriesId" style="width: 370px;" />-->
            <el-select v-model="form.seriesId" placeholder="请选择" style="width: 370px;">
              <el-option
                v-for="item in shipSeries"
                :key="item.id"
                :label="item.seriesName"
                :value="item.id">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="船只名称" prop="shipName">
            <el-input v-model="form.shipName" style="width: 370px;" maxlength="20"/>
          </el-form-item>
          <!--<el-form-item label="商户id" prop="merId">
            <el-input v-model="form.merId" style="width: 370px;" />
          </el-form-item>-->
<!--          <el-form-item label="所属商铺" prop="storeId">-->
<!--            <el-input v-model="form.storeId" style="width: 370px;" />-->
<!--          </el-form-item>-->
          <el-form-item label="船只所属商户名" prop="merName">
            <el-input v-model="form.merName" style="width: 370px;" maxlength="20" clearable/>
          </el-form-item>
          <el-form-item label="船只负责人" prop="managerName">
            <el-input v-model="form.managerName" style="width: 370px;" maxlength="20" clearable/>
          </el-form-item>
          <el-form-item label="负责人电话" prop="managerPhone">
            <el-input v-model="form.managerPhone" style="width: 370px;" maxlength="20" clearable/>
          </el-form-item>
          <el-form-item label="船只状态" prop="shipStatus">
            <el-radio-group v-model="form.shipStatus">
              <el-radio :label="0">启用</el-radio>
              <el-radio :label="1">禁用</el-radio>
            </el-radio-group>
          </el-form-item>
          <!--<el-form-item label="船只当前状态：0：在港，1：离港。2：维修中" prop="currentStatus">
            <el-input v-model="form.currentStatus" style="width: 370px;" />
          </el-form-item>-->
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="text" @click="crud.cancelCU">取消</el-button>
          <el-button :loading="crud.cu === 2" type="primary" @click="crud.submitCU">确认</el-button>
        </div>
      </el-dialog>
      <!--表格渲染-->
      <el-table ref="table" v-loading="crud.loading" :data="crud.data" size="small" style="width: 100%;" @selection-change="crud.selectionChangeHandler">
        <el-table-column type="selection" width="55" />
        <el-table-column v-if="columns.visible('seriesId')" prop="seriesId" label="船只系列" >
          <template slot-scope="scope">
            {{
            seriesLabel(scope.row.seriesId)
            }}
          </template>
        </el-table-column>
        <el-table-column v-if="columns.visible('shipName')" prop="shipName" label="船只名称" />
        <el-table-column v-if="columns.visible('merName')" prop="merName" label="船只所属商户" />
        <el-table-column v-if="columns.visible('managerName')" prop="managerName" label="船只负责人" />
        <el-table-column v-if="columns.visible('managerPhone')" prop="managerPhone" label="负责人电话" />
        <el-table-column v-if="columns.visible('shipStatus')" prop="shipStatus" label="船只状态" >
          <template slot-scope="scope">
            <span>{{ scope.row.shipStatus?'禁用':'启用' }}</span>
          </template>
        </el-table-column>
        <el-table-column v-if="columns.visible('currentStatus')" prop="currentStatus" label="船只当前状态" >
          <template slot-scope="scope">
            {{
            curStatusLabel(scope.row.currentStatus)
            }}
          </template>
        </el-table-column>

        <el-table-column v-permission="['admin','yxShipInfo:edit','yxShipInfo:del']" label="操作" width="150px" align="center">
          <template slot-scope="scope">
            <el-button size="mini" type="text" icon="el-icon-edit"
                       @click="crud.toEdit(scope.row)" >修改</el-button>
            <el-divider direction="vertical"></el-divider>
            <el-button size="mini" type="text" icon="el-icon-edit"
                       @click="editStatus(scope.row)" >{{ scope.row.shipStatus?'启用':'禁用' }}</el-button>
            <el-divider direction="vertical"></el-divider>
            <el-button size="mini" type="text" icon="el-icon-edit"
                       @click="crud.toEdit(scope.row)" >出行记录</el-button>
          </template>
        </el-table-column>
      </el-table>
      <!--分页组件-->
      <pagination />
    </div>
  </div>
</template>

<script>
import crudYxShipInfo , { changeStatus } from '@/api/yxShipInfo'
// import yxShipSeries from '@/api/yxShipSeries'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import udOperation from '@crud/UD.operation'
import pagination from '@crud/Pagination'
import MaterialList from "@/components/material";
import { initData } from '@/api/data'
import { validatePhoneTwo } from '@/utils/validate'
import { Notification } from 'element-ui'

// crud交由presenter持有
const defaultCrud = CRUD({ title: '船只管理', url: 'api/yxShipInfo', sort: 'id,desc',
  crudMethod: { ...crudYxShipInfo },optShow:{
    add: true,
    edit: false,
    del: false,
    download: false
  }
  , query: {
    seriesName:'',
    seriesId:'',
    currentStatus:''
  },
})
const defaultForm = { id: null, shipName: null, seriesId: null, merId: null,
  storeId: null, merName: null, managerName: null, managerPhone: null, shipStatus: 1,
  currentStatus: null, lastLeaveTime: null, lastReturnTime: null, delFlag: null,
  createUserId: null, updateUserId: null, createTime: null, updateTime: null }
export default {
  name: 'YxShipInfo',
  components: { pagination, crudOperation, rrOperation, udOperation ,MaterialList},
  mixins: [presenter(defaultCrud), header(), form(defaultForm), crud()],
  data() {
    return {
      permission: {
        add: ['admin', 'yxShipInfo:add'],
        edit: ['admin', 'yxShipInfo:edit'],
        del: ['admin', 'yxShipInfo:del']
      },
      rules: {
        shipName: [
          { required: true, message: '船只名称不能为空', trigger: 'blur' }
        ],
        seriesId: [
          { required: true, message: '船只系列不能为空', trigger: 'blur' }
        ],
        managerPhone: [
          // { required: true, message: '负责人电话不能为空', trigger: 'blur' },
          {validator:validatePhoneTwo,trigger:'blur'}
        ],
        shipStatus: [
          { required: true, message: '船只状态不能为空', trigger: 'blur' }
        ],
        currentStatus: [
          // { required: true, message: '船只当前状态：0：在港，1：离港。2：维修中不能为空', trigger: 'blur' }
        ],
      },
      shipSeries: [],
      curStatus:[
        {value:'0',label:'在港'},
        {value:'1',label:'离港'},
        {value:'2',label:'维修中'}
      ]
    }
  },
  watch: {
  },
  computed:{
    // transferLabel:function(){
    //   return function(options,key,val){
    //     if(this.options.length){
    //       let i= this.options.filter(function(item){
    //         return new RegExp(item.key, 'i').test(val)
    //       })
    //       if(i.length){
    //         return i
    //       }else{
    //         return i
    //       }
    //     }else{
    //       return ""
    //     }
    //   }
    // },
    seriesLabel:function(){
      return function(id){
        if(this.shipSeries.length){
          let i= this.shipSeries.filter(function(item){
            return new RegExp(item.id, 'i').test(id)
          })
          if(i.length){
            return i[0].seriesName
          }
        }else{
          return ""
        }
      }
    },
    curStatusLabel:function(){
      return function(value){
        if(this.curStatus.length){
          let i= this.curStatus.filter(function(item){
            return new RegExp(item.value, 'i').test(value)
          })
          if(i.length){
            return i[0].label
          }
        }else{
          return ""
        }
      }
    }
  },
  mounted() {
    this.$nextTick(()=>{
      initData('api/yxShipSeries').then(res=>{
        this.shipSeries=res.content
      })
    })
  },
  methods: {
    // 获取数据前设置好接口地址
    [CRUD.HOOK.beforeRefresh]() {
      return true
    }, // 新增与编辑前做的操作
    [CRUD.HOOK.afterToCU](crud, form) {
    },
    editStatus(row){
      let that=this
      changeStatus(row.id).then(res=>{
        let t='已'+ (row.currentStatus?'启用':'禁用')
        Notification.success({
          title: t
        })
        that.crud.refresh()
      })
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
</style>
