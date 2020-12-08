<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <!--如果想在工具栏加入更多按钮，可以使用插槽方式， slot = 'left' or 'right'-->
      <el-input v-model.trim="query.batchNo" clearable size="small"
                placeholder="请输入批次号" style="width: 200px;" class="filter-item"
                @keyup.enter.native="crud.toQuery" />
      <el-cascader
        class="filter-item"
        v-model="query.shipInfoId"
        :options="shipSeriesTree"
        :props="{ expandTrigger: 'hover' }"
        clearable
        @change="(val)=>{
          if(val&&val.length){
            query.shipId=val[1]
          }else{
            query.shipId=''
            $route.query.id=''
          }
        }"></el-cascader>
      <el-input v-model.trim="query.captainName" clearable size="small"
                placeholder="请输入船长姓名" style="width: 200px;" class="filter-item"
                @keyup.enter.native="crud.toQuery" />
      <el-date-picker
        class="filter-item"
        v-model="query.daterange"
        type="daterange"
        range-separator="至"
        start-placeholder="出港时间"
        end-placeholder="出港时间"
        value-format="yyyy-MM-dd"
        @change="(val)=>{
        if(val) {
          query.startDate =val[0];query.endDate =val[1]
        }else{
          query.startDate='';query.endDate=''
        }
      }"
      >
      </el-date-picker>
      <el-button class="filter-item" size="mini" type="success" icon="el-icon-search" @click="crud.toQuery">搜索</el-button>
      <crudOperation :permission="permission" />
      <!--表单组件-->
      <detail ref="detail" />
      <el-dialog :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="500px">
        <el-form ref="form" :model="form" :rules="rules" size="small" label-width="80px">
          <el-form-item label="id">
            <el-input v-model="form.id" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="船只出港批次号" prop="batchNo">
            <el-input v-model="form.batchNo" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="船只id" prop="shipId">
            <el-input v-model="form.shipId" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="船只名称">
            <el-input v-model="form.shipName" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="船长id" prop="captainId">
            <el-input v-model="form.captainId" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="船长姓名">
            <el-input v-model="form.captainName" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="承载人数">
            <el-input v-model="form.totalPassenger" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="老年人人数">
            <el-input v-model="form.oldPassenger" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="未成年人数">
            <el-input v-model="form.underagePassenger" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="出港时间">
            <el-input v-model="form.leaveTime" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="回港时间">
            <el-input v-model="form.returnTime" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="船只状态 0:待出港 1：出港 2：回港" prop="status">
            <el-input v-model="form.status" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="是否删除（0：未删除，1：已删除）" prop="delFlag">
            <el-input v-model="form.delFlag" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="创建人">
            <el-input v-model="form.createUserId" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="修改人">
            <el-input v-model="form.updateUserId" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="创建时间" prop="createTime">
            <el-input v-model="form.createTime" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="更新时间" prop="updateTime">
            <el-input v-model="form.updateTime" style="width: 370px;" />
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
        <el-table-column v-if="columns.visible('batchNo')" prop="batchNo" label="批次号" />
        <el-table-column v-if="columns.visible('shipName')" prop="shipName" label="船只名称" />
        <el-table-column v-if="columns.visible('captainName')" prop="captainName" label="船长姓名" />
        <el-table-column v-if="columns.visible('totalPassenger')" prop="totalPassenger" label="承载人数" >
          <template slot-scope="scope">
            <span>{{ scope.row.totalPassenger>0 ? scope.row.totalPassenger+'人':'无' }}</span>
          </template>
        </el-table-column>
        <el-table-column v-if="columns.visible('oldPassenger')" prop="oldPassenger" label="老年人人数" >
          <template slot-scope="scope">
            <span>{{ scope.row.oldPassenger>0 ? scope.row.oldPassenger+'人':'无' }}</span>
          </template>
        </el-table-column>
        <el-table-column v-if="columns.visible('underagePassenger')" prop="underagePassenger" label="未成年人数" >
          <template slot-scope="scope">
            <span>{{ scope.row.underagePassenger>0 ? scope.row.underagePassenger+'人':'无' }}</span>
          </template>
        </el-table-column>
        <el-table-column v-if="columns.visible('leaveFortmatTime')" prop="leaveFortmatTime" label="出港时间" >
          <template slot-scope="scope">
            <span>{{ scope.row.leaveFortmatTime }}</span>
          </template>
        </el-table-column>
        <el-table-column v-if="columns.visible('returnFormatTime')" prop="returnFormatTime" label="回港时间" >
          <template slot-scope="scope">
            <span>{{ scope.row.returnFormatTime }}</span>
          </template>
        </el-table-column>
        <el-table-column v-permission="['admin','yxShipOperation:edit','yxShipOperation:del']" label="操作" width="160px" align="center">
          <template slot-scope="scope">
            <div class="flexs">
                 <el-button v-permission="permission.edit" type="primary" size="mini" plain @click="detail(scope.row)">详情</el-button>
                  <el-button size="mini" type="primary" plain style="marginLeft:5px;">合同下载</el-button>
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
import crudYxShipOperation from '@/api/yxShipOperation'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import udOperation from '@crud/UD.operation'
import pagination from '@crud/Pagination'
import MaterialList from "@/components/material"
import { initData } from '@/api/data'
import detail from '../yxShipOperationDetail/index'

// crud交由presenter持有
const defaultCrud = CRUD({ title: '船只运营记录', url: 'api/yxShipOperation', sort: 'id,desc',
  optShow:{
    add: false,
    edit: false,
    del: false,
    download: true
  },
  crudMethod: { ...crudYxShipOperation },
  query:{
    shipInfoId:[]
  }
})
const defaultForm = { id: null, batchNo: null, shipId: null, shipName: null, captainId: null,
  captainName: null, totalPassenger: null, oldPassenger: null, underagePassenger: null,
  leaveTime: null, returnTime: null, status: null, delFlag: null, createUserId: null,
  updateUserId: null, createTime: null, updateTime: null }
export default {
  name: 'YxShipOperation',
  components: { pagination, crudOperation, rrOperation, udOperation ,MaterialList,detail},
  mixins: [presenter(defaultCrud), header(), form(defaultForm), crud()],
  data() {
    return {
      shipSeriesTree:[],
      permission: {
        add: ['admin', 'yxShipOperation:add'],
        edit: ['admin', 'yxShipOperation:edit'],
        del: ['admin', 'yxShipOperation:del']
      },
      rules: {
        batchNo: [
          { required: true, message: '船只出港批次号不能为空', trigger: 'blur' }
        ],
        shipId: [
          { required: true, message: '船只id不能为空', trigger: 'blur' }
        ],
        captainId: [
          { required: true, message: '船长id不能为空', trigger: 'blur' }
        ],
        status: [
          { required: true, message: '船只状态 0:待出港 1：出港 2：回港不能为空', trigger: 'blur' }
        ],
        delFlag: [
          { required: true, message: '是否删除（0：未删除，1：已删除）不能为空', trigger: 'blur' }
        ],
        createTime: [
          { required: true, message: '创建时间不能为空', trigger: 'blur' }
        ],
        updateTime: [
          { required: true, message: '更新时间不能为空', trigger: 'blur' }
        ]
      }    ,
      basePath:''
    }
  },
  watch: {
  },
  created() {
    this.$nextTick(()=>{
      let that=this
      initData('api/yxShipInfo/getShipSeriseTree').then(res=>{
        if(res && res.options){
          this.shipSeriesTree=res.options
          if(Boolean(that.$route.query.id) && !isNaN(that.$route.query.id)){
            let i= this.shipSeriesTree.filter(function(series){
              let j=series.children.filter(function(item){
                return new RegExp(item.value, 'i').test(that.$route.query.id)
              })
              if(j.length){
                that.query.shipInfoId=[series.value,parseInt(that.$route.query.id)]
              }
            })
          }
        }else{
          this.shipSeriesTree=[]
        }
      })
    })
  },
  methods: {
    // 获取数据前设置好接口地址
    [CRUD.HOOK.beforeRefresh]() {
        if(Boolean(this.$route.query.id) && !isNaN(this.$route.query.id)){
          this.query.shipId=parseInt(this.$route.query.id)
        }else if(!this.query.shipInfoId.length>0){
          this.query.shipId=''
        }
      return true
    }, // 新增与编辑前做的操作
    [CRUD.HOOK.afterToCU](crud, form) {
    },
    detail(data) {
      const _this = this.$refs.detail
      _this.form = {
        id: data.id,
      }
      _this.dialog = true
      _this.loading = true
      this.$refs.detail.init(data.id)
    },
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
