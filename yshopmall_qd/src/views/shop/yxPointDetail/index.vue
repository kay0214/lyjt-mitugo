<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <el-row>
        <el-input v-model.trim="query.username" clearable placeholder="用户昵称" style="width: 200px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
        <el-input v-model.trim="query.phone" clearable placeholder="输入用户手机号" style="width: 200px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
        <el-select v-model="query.userType" clearable placeholder="用户类型" class="filter-item" style="width: 130px">
          <template>
            <el-option
              v-for="item in userTypes"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </template>
        </el-select>
        <el-select v-model="query.type" clearable placeholder="明细类型" class="filter-item" style="width: 130px">
          <template v-for="item in pointTypeOptions">
            <el-option
              v-for="(val,key) in item"
              :key="key"
              :label="val"
              :value="key"
            />
          </template>
        </el-select>
        <!--<el-select v-model="pm" clearable placeholder="收支类型" class="filter-item" style="width: 130px">-->
          <!--<el-option-->
            <!--v-for="item in pmOptions"-->
            <!--:key="item.value"-->
            <!--:label="item.label"-->
            <!--:value="item.value"-->
          <!--/>-->
        <!--</el-select>-->
      <el-date-picker
          type="daterange"
          v-model="searchTime"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          placeholder="选择时间范围"
          value-format='yyyy-MM-dd'
          style="verticalAlign:top;marginRight:20px;"
          @change="(val)=>{if(val){query.addTimeStart=val[0],query.addTimeEnd=val[1]}else{query.addTimeStart=null,query.addTimeEnd=null}}"
          >
        </el-date-picker>
        <el-button class="filter-item" size="mini" type="success" icon="el-icon-search" @click="crud.toQuery">搜索</el-button>
      </el-row>
      <crudOperation :permission="permission" />
      <!--表单组件-->
      <el-dialog :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="500px">
        <el-form ref="form" :model="form" :rules="rules" size="small" label-width="80px">
          <el-form-item label="用户名">
            <el-input v-model="form.username" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="积分类别 0:拉新 1:分红" prop="type">
            <el-input v-model="form.type" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="订单编号">
            <el-input v-model="form.linkId" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="订单类型 0:商品购买 1:本地生活">
            <el-input v-model="form.brokerageType" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="订单金额">
            <el-input v-model="form.orderPrice" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="订单佣金">
            <el-input v-model="form.commission" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="商户用户名" prop="merUsername">
            <el-input v-model="form.merUsername" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="商户获取积分数">
            <el-input v-model="form.merchantsPoint" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="合伙人用户名" prop="parUsername">
            <el-input v-model="form.parUsername" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="合伙人获取积分数">
            <el-input v-model="form.partnerPoint" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="创建时间" prop="createTime">
            <el-input v-model="form.createTime" style="width: 370px;" />
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="text" @click="crud.cancelCU">取消</el-button>
          <el-button :loading="crud.cu === 2" type="primary" @click="crud.submitCU">确认</el-button>
        </div>
      </el-dialog>
      <!--表格渲染-->
      <el-table ref="table" v-loading="crud.loading" :data="crud.data" size="small" style="width: 100%;" @selection-change="crud.selectionChangeHandler">
        <el-table-column type="selection" width="55" />
        <el-table-column v-if="columns.visible('username')" prop="username" label="用户昵称" />
        <el-table-column v-if="columns.visible('phone')" prop="phone" label="用户手机号" />
        <el-table-column v-if="columns.visible('userType')" prop="userType" label="用户类型" >
          <template slot-scope="scope">
            {{ transferLabel(scope.row.userType,userTypes) }}
          </template>
        </el-table-column>
        <el-table-column prop="title" label="账单标题" />
        <!-- <el-table-column label="积分类别" align="center">
          <template slot-scope="scope">
            <div>
              <el-tag v-if="scope.row.type == 1">分红</el-tag>
              <el-tag v-else-if="scope.row.type == 0">拉新</el-tag>
              <el-tag v-else></el-tag>
            </div>
          </template>
        </el-table-column> -->
        <el-table-column v-if="columns.visible('linkId')" prop="linkId" label="订单编号" />
         <el-table-column label="订单类型" align="center">
          <template slot-scope="scope">
            <div>
              <el-tag v-if="scope.row.brokerageType == 1">本地生活</el-tag>
              <el-tag v-else-if="scope.row.brokerageType == 0">商品购买</el-tag>
              <el-tag v-else></el-tag>
            </div>
          </template>
        </el-table-column>
      <el-table-column prop="category" label="明细种类">
        <template slot-scope="scope">
          <span v-if="scope.row.category == 'now_money'">余额</span>
          <span v-else-if="scope.row.category == 'integral'">积分</span>
          <span v-else>未知</span>
        </template>
      </el-table-column>
        <el-table-column prop="type" label="明细类型">
          <template slot-scope="scope">
            {{
            typeLabel(scope.row.type)
            }}
          </template>
        </el-table-column>
        <el-table-column v-if="columns.visible('number')" prop="number" label="积分" />
        <!-- <el-table-column prop="userType" label="用户类型">
        <template slot-scope="scope">
          <span v-if="scope.row.userType == 0">预留</span>
          <span v-else-if="scope.row.userType == 1">商户</span>
          <span v-else-if="scope.row.userType == 2">合伙人</span>
          <span v-else>用户</span>
        </template>
      </el-table-column> -->
        <!-- <el-table-column v-if="columns.visible('commission')" prop="commission" label="订单佣金" /> -->
        <!-- <el-table-column v-if="columns.visible('merUsername')" prop="merUsername" label="商户用户名" /> -->
        <!-- <el-table-column v-if="columns.visible('merchantsPoint')" prop="merchantsPoint" label="商户获取积分数" /> -->
        <!-- <el-table-column v-if="columns.visible('parUsername')" prop="parUsername" label="合伙人用户名" /> -->
        <!-- <el-table-column v-if="columns.visible('partnerPoint')" prop="partnerPoint" label="合伙人获取积分数" /> -->
        <el-table-column v-if="columns.visible('addTime')" prop="addTime" label="创建时间">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.addTime) }}</span>
          </template>
        </el-table-column>
      </el-table>
      <!--分页组件-->
      <pagination />
    </div>
  </div>
</template>

<script>
import crudYxPointDetail from '@/api/yxPointDetail'
import {getType} from '@/api/yxUserBill'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import udOperation from '@crud/UD.operation'
import pagination from '@crud/Pagination'
import MaterialList from "@/components/material";
import { Message } from 'element-ui'

// crud交由presenter持有
const defaultCrud = CRUD({ title: '积分获取明细', url: 'api/pointDetail', sort: 'id,desc',optShow: {
      add: false,
      edit: false,
      del: false,
      download: false
    },query: {
      category: '', type: '',pm:'',addTimeStart:'',addTimeEnd:'',phone:'',userType:''
    },
    crudMethod: { ...crudYxPointDetail }})
const defaultForm = { id: null, uid: null, username: null, type: null, orderId: null, orderType: null, orderPrice: null, commission: null, merchantsId: null, merchantsPoint: null, partnerId: null, partnerPoint: null, delFlag: null, createUserId: null, updateUserId: null, createTime: null, updateTime: null }
export default {
  name: 'YxPointDetail',
  components: { pagination, crudOperation, rrOperation, udOperation ,MaterialList},
  mixins: [presenter(defaultCrud), header(), form(defaultForm), crud()],
  data() {
    return {
       category: '', type: '',pm:'',addTimeStart:'',addTimeEnd:'',searchTime:'',
      permission: {
        add: ['admin', 'yxPointDetail:add'],
        edit: ['admin', 'yxPointDetail:edit'],
        del: ['admin', 'yxPointDetail:del']
      },
      rules: {
        uid: [
          { required: true, message: '用户ID不能为空', trigger: 'blur' }
        ],
        type: [
          { required: true, message: '积分类别 0:拉新 1:分红不能为空', trigger: 'blur' }
        ],
        merchantsId: [
          { required: true, message: '商户id不能为空', trigger: 'blur' }
        ],
        partnerId: [
          { required: true, message: '合伙人id不能为空', trigger: 'blur' }
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
      },
      categoryOptions: [
        { value: 'now_money', label: '余额' },
        { value: 'integral', label: '积分' }
      ],
      typeOptions: [],
      pointTypeOptions:[
        {"share_dividend":"分红"},
        {"pull_new":"拉新"}],
      pmOptions: [
        { value: '0', label: '支出 ' },
        { value: '1', label: '获得' }
      ],
      userTypes: [
        { value: 4, label: '平台 ' },
        { value: 1, label: '商户' },
        { value: 2, label: '合伙人' },
        { value: 3, label: '用户' }
      ],
    }
  },
  watch: {
  },
  mounted() {
    this.$nextTick(() => {
      getType().then(res=>{
        if(res){
          this.typeOptions=res
        }
      }).catch(err=>{
        Message({ message: err, type: 'error' })
      })
    })
  },
  computed:{
    typeLabel:function(){
      return function(type){
        if(this.typeOptions.length){
          let i= this.typeOptions.filter(function(item){
            for(let key in item){
              if(key===type){
                return JSON.parse(JSON.stringify(item))
              }
            }
          })
          if(i.length){
            return i[0][type]
          }
        }else{
          return ""
        }
      }
    },
    transferLabel:function(){
      return function(value,list){
        if(list.length){
          let i= list.filter(function(item){
            return new RegExp(item.value, 'i').test(value)
          })
          if(i.length){
            return i[0].label
          }else{
            return ""
          }
        }else{
          return ""
        }
      }
    }
  },
  methods: {
    // 获取数据前设置好接口地址
    [CRUD.HOOK.beforeRefresh]() {
      if(this.crud.query.phone.length>0 && this.crud.query.userType===''){
        this.crud.notify('请选择用户类型','error')
        return false
      }
      return true
    }, // 新增与编辑前做的操作
    [CRUD.HOOK.afterToCU](crud, form) {
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
</style>
