<!--分红池页面-->
<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <!--如果想在工具栏加入更多按钮，可以使用插槽方式， slot = 'left' or 'right'-->
      <el-row>
        <el-input v-model="query.username" clearable placeholder="用户昵称" style="width: 200px;marginRight:20px;" class="filter-item" @keyup.enter.native="crud.toQuery" />        
        <el-date-picker          
          type="daterange"
          v-model="query.createTime"
          range-separator="至"
          start-placeholder="订单日期"
          end-placeholder="订单日期"
          placeholder="选择时间范围"
          style="verticalAlign:top;marginRight:20px;">
        </el-date-picker>
        <el-button class="filter-item" size="mini" type="success" icon="el-icon-search" @click="crud.toQuery">搜索</el-button>
      </el-row>
      <el-row :gutter='6' style="margin:20px;"><el-col :span='4'>累计总金额: <span>{{totalAmount}}</span></el-col></el-row>
      <!--表格渲染-->
      <el-table ref="table" v-loading="crud.loading" :data="crud.data" size="small" style="width: 100%;" @selection-change="crud.selectionChangeHandler">
        <el-table-column type="index" width="55" />
        <el-table-column v-if="columns.visible('username')" prop="username" label="用户昵称" />
        <el-table-column v-if="columns.visible('orderId')" prop="orderId" label="订单编号" />
        <el-table-column v-if="columns.visible('orderPrice')" prop="orderPrice" label="金额" />
        <el-table-column v-if="columns.visible('createTime')" prop="createTime" label="订单日期">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.createTime) }}</span>
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
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import udOperation from '@crud/UD.operation'
import pagination from '@crud/Pagination'
import MaterialList from "@/components/material";

// crud交由presenter持有
const defaultCrud = CRUD({ title: '分红池', url: 'api/yxPointDetail/pullNew', sort: 'id,desc', crudMethod: { ...crudYxPointDetail }})
const defaultForm = { id: null, uid: null, username: null, type: null, orderId: null, orderType: null, orderPrice: null, commission: null, merchantsId: null, merchantsPoint: null, partnerId: null, partnerPoint: null, delFlag: null, createUserId: null, updateUserId: null, createTime: null, updateTime: null }
export default {
  name: 'YxPointDetail',
  components: { pagination, crudOperation, rrOperation, udOperation ,MaterialList},
  mixins: [presenter(defaultCrud), header(), form(defaultForm), crud()],
  data() {
    return {
      totalAmount:0,//累计金额
      permission: {
        add: ['admin', 'yxPointDetail:add'],
        edit: ['admin', 'yxPointDetail:edit'],
        del: ['admin', 'yxPointDetail:del']
      },
      rules: {
      }    
    }
  },
  mounted(){
    //获取累计金额
    this.crud.refresh().then(res=>{
      this.totalAmount=res.totalAmount
    })
  },
  watch: {
  },
  methods: {
    // 获取数据前设置好接口地址
    [CRUD.HOOK.beforeRefresh]() {
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
