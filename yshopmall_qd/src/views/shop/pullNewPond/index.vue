<!--拉新池页面-->
<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <!--如果想在工具栏加入更多按钮，可以使用插槽方式， slot = 'left' or 'right'-->
      <el-row>
        <el-input v-model="query.username" clearable placeholder="用户昵称" style="width: 200px;marginRight:20px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
        <el-date-picker
          type="daterange"
          v-model="query.searchTime"
          range-separator="-"
          start-placeholder="订单日期"
          end-placeholder="订单日期"
          placeholder="选择时间范围"
          value-format='yyyy-MM-dd'
          style="verticalAlign:top;marginRight:20px;">
        </el-date-picker>
        <el-button class="filter-item" size="mini" type="success" icon="el-icon-search" @click="crud.toQuery">搜索</el-button>
      </el-row>
      <el-row :gutter='6' style="margin:20px;"><el-col :span='4'>累计总积分: <span>{{totalAmount}}</span></el-col></el-row>
      <!--表格渲染-->
      <el-table ref="table" v-loading="crud.loading" :data="crud.data" size="small" style="" @selection-change="crud.selectionChangeHandler">
        <el-table-column type="index" width="55" />
        <el-table-column v-if="columns.visible('username')" prop="username" label="用户昵称" />
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
        <el-table-column prop="title" label="账单标题" />
        <el-table-column prop="category" label="明细种类">
        <template slot-scope="scope">
          <span v-if="scope.row.category == 'now_money'">余额</span>
          <span v-else-if="scope.row.category == 'integral'">积分</span>
          <span v-else>未知</span>
        </template>
      </el-table-column>
        <el-table-column v-if="columns.visible('number')" prop="number" label="积分" />
        <el-table-column prop="userType" label="用户类型">
        <template slot-scope="scope">
          <span v-if="scope.row.userType == 0">预留</span>
          <span v-else-if="scope.row.userType == 1">商户</span>
          <span v-else-if="scope.row.userType == 2">合伙人</span>
          <span v-else>用户</span>
        </template>
      </el-table-column>
        <el-table-column v-if="columns.visible('addTime')" prop="addTime" label="订单日期">
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
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import udOperation from '@crud/UD.operation'
import pagination from '@crud/Pagination'
import MaterialList from "@/components/material";

// crud交由presenter持有
const defaultCrud = CRUD({ title: '拉新池', url: 'api/getPullNewPoint', sort: 'id,desc', crudMethod: { ...crudYxPointDetail }})
const defaultForm = { id: null, uid: null, username: null, type: null, linkId: null, orderType: null, number: null, commission: null, merchantsId: null, merchantsPoint: null, partnerId: null, partnerPoint: null, delFlag: null, createUserId: null, updateUserId: null, addTime: null, updateTime: null }
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
      rules: {}
    }
  },
  mounted(){
    //获取累计金额
    this.crud.refresh().then(res=>{
      this.totalAmount=res.totalPoint
    })
  },
  watch: {
    'query.searchTime'(value) {
      this.crud.query.addTimeStart = value ? value[0] : ''
      this.crud.query.addTimeEnd = value ? value[1] : ''
    }
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
