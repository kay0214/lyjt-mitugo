<template>
  <div class="app-container">
    <div class="container">
      <el-tabs v-model="orderStatus" type="card" @tab-click="handleOrder">
        
          <el-tab-pane 
          v-for="(item,index) in orderStatusList"
          :key='item.value'
          :label="item.value"
          :name='index.toString()'>
            <span slot="label"><i class="el-icon-bank-card"></i> {{item.label}}</span>
          </el-tab-pane>
          <!-- <el-tab-pane name="" :key="-1">
          <span slot="label"><i class="el-icon-s-order"></i> 全部订单</span>
        </el-tab-pane> -->
        </el-tabs>
      <!--工具栏-->
      <div class="head-container">
        <div>
          <!-- 搜索 -->
          <el-input v-model="query.value" clearable placeholder="输入搜索内容" style="width: 200px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
          <el-select v-model="query.orderType" clearable placeholder="类型" class="filter-item" style="width: 130px">
            <el-option v-for="item in queryTypeOptions" :key="item.key" :label="item.display_name" :value="item.key" />
          </el-select>
          <el-date-picker
            v-model="createTime"
            :default-time="['00:00:00','23:59:59']"
            type="daterange"
            range-separator=":"
            size="small"
            class="date-item"
            value-format="yyyy-MM-dd HH:mm:ss"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
          />
          <el-button class="filter-item" size="mini" type="success" icon="el-icon-search" @click="crud.toQuery">搜索</el-button>
          <!-- 新增 -->
          <el-button
            type="danger"
            class="filter-item"
            size="mini"
            icon="el-icon-refresh"
            @click="crud.toQuery"
          >刷新</el-button>
        </div>
        <!--表单组件-->
        <!-- <el-dialog :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="500px">
          <el-form ref="form" :model="form" :rules="rules" size="small" label-width="80px">
            <div>{{form}}</div>            
            <el-form-item label="订单号" prop="orderId">
              <el-input v-model="form.orderId" style="width: 370px;" disabled/>
            </el-form-item>
            <el-form-item label="备注" prop="mark">
              <el-input v-model="form.mark" style="width: 370px;" />
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button type="text" @click="crud.cancelCU">取消</el-button>
            <el-button :loading="crud.cu === 2" type="primary" @click="crud.submitCU">确认</el-button>
          </div>
        </el-dialog> -->
        <eDetail ref="form1" :is-add="false" />
        <eRefund ref="form2" :is-add="false" />

        <!--表格渲染-->
        <el-table ref="table" v-loading="crud.loading" :data="crud.data" size="small" style="width: 100%;" @selection-change="crud.selectionChangeHandler">
          <el-table-column type="selection" width="55" />
          <el-table-column v-if="columns.visible('orderId')" prop="orderId" label="订单号" />
          <el-table-column v-if="columns.visible('realName')" prop="realName" label="用户姓名" />
          <el-table-column v-if="columns.visible('couponPrice')" prop="couponPrice" label="商品信息" >
             <template slot-scope="scope">
               <img :src='scope.row.image' :width='30'/>
              <span>{{scope.row.yxCouponsDto.couponName}}</span>
            </template>
          </el-table-column>
          <el-table-column v-if="columns.visible('couponPrice')" prop="couponPrice" label="实际支付" />
          <el-table-column v-if="columns.visible('commission')" prop="commission" label="佣金" />
          <el-table-column v-if="columns.visible('payStaus')" prop="payStaus" label="支付状态" ><!-- 0未支付 1已支付-->
            <template slot-scope="scope">
              <span>{{Boolean(scope.row.payStaus)?"已支付":"未支付"}}</span>
            </template>
          </el-table-column>
          <el-table-column v-if="columns.visible('status')" prop="status" label="订单状态">
            <!--（0:待支付 1:已过期 2:待发放3:支付失败4:待使用5:已使用6:已核销7:退款中8:已退款9:退款驳回10:已取消-->
            <template slot-scope="scope">
              <!-- <span>{{ scope.row.status < 11 ?JSON.parse(JSON.stringify(orderStatusList[scope.row.status*1+1])).label:""}}</span> -->
              <span>{{ scope.row.status < 11 ? orderStatusList[orderStatusList.findIndex(item=>{
                 return parseInt(item.value)===scope.row.status
                })].label : ""}}</span>
              <br/>
              <div v-if="parseInt(scope.row.status)==7||parseInt(scope.row.status)==8">
                  退款原因：{{scope.row.refundReasonWapExplain}}<br/>
                  <!-- 备注说明：<br/> -->
                  <span v-if='parseInt(scope.row.status)==8'>退款时间：{{parseTime(scope.row.refundReasonTime)}}<br/></span>
              </div>
            </template>            
          </el-table-column>
          <el-table-column v-if="columns.visible('createTime')" prop="createTime" label="创建时间">
            <template slot-scope="scope">
              <span>{{ formatTime(scope.row.createTime) }}</span>
            </template>
          </el-table-column>         
          <el-table-column v-permission="['admin','yxCouponOrder:edit','yxCouponOrder:refund']" label="操作" width="150px" align="center">
            <template slot-scope="scope">
              <el-button
              v-permission="permission.edit"
              size="mini"
              type="primary"
              @click="detail(scope.row)"
            >
              订单详情</el-button>
              <el-button 
              v-permission="permission.refund"
              v-if='scope.row.refundStatus===1'
              size="mini"
              type="danger"
              @click="refund(scope.row)"
            >
              退款</el-button><!--0 未退款 1 申请中 2 已退款-->             
            </template>
          </el-table-column>
        </el-table>
        <!--分页组件-->
        <pagination />
      </div>
    </div>
  </div>
</template>

<script>
import crudYxCouponOrder from '@/api/yxCouponOrder'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import udOperation from '@crud/UD.operation'
import pagination from '@crud/Pagination'
import { formatTime } from '@/utils/index'
import eDetail from './detail'
import eRefund from './refund'

// crud交由presenter持有
const defaultCrud = CRUD({ title: '卡券订单表', url: 'api/yxCouponOrder', 
sort: 'id,desc', crudMethod: { ...crudYxCouponOrder }, query:{orderStatus: '',orderType: '', }})
const defaultForm = {  orderId: null,  mark: null }
export default {
  name: 'YxCouponOrder',
  components: { pagination, crudOperation, rrOperation, udOperation ,eRefund, eDetail},
  mixins: [presenter(defaultCrud), header(), form(defaultForm), crud()],
  data() {
    return {
      permission: {
        add: ['admin', 'yxCouponOrder:add'],
        edit: ['admin', 'yxCouponOrder:edit'],
        del: ['admin', 'yxCouponOrder:del'],
        refund: ['admin', 'yxCouponOrder:refund']
      },
      rules: {
      },  
      orderStatus:'',
      orderType: '',
      orderStatusList:[ 
        { value: ' ', label: '全部订单' },
        { value: '0', label: '待支付' },
        { value: '1', label: '已过期' },
        // { value: '2', label: '待发放' },
        { value: '3', label: '支付失败' },
        { value: '4', label: '待使用' },
        { value: '5', label: '已使用' },
        { value: '6', label: '已核销' },
        { value: '7', label: '退款中' },
        { value: '8', label: '已退款' },
        // { value: '9', label: '退款驳回'},
        { value: '10', label: '已取消'},
      ],  
      queryTypeOptions: [
        { key: 'orderId', display_name: '订单号' },
        { key: 'realName', display_name: '用户姓名' },
        { key: 'userPhone', display_name: '用户电话' }
      ],
      createTime: '',
    }
  },
  computed:{
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
    formatTime,
    handleOrder(tab, event) {
      this.crud.query.orderStatus = tab.label
      this.crud.page.page = 1
      this.crud.toQuery()
    },    
    beforeInit() {
      this.url = 'api/yxCouponOrder'
      const sort = 'id,desc'
      this.params = { page: this.page, size: this.size, sort: sort, orderType: this.orderType, orderStatus: this.orderStatus, addTime: this.createTime,}
      const query = this.query
      const type = query.type
      const value = query.value
      if (type && value) { this.params[type] = value }
      return true
    },
    detail(data) {
      // this.isAdd = false
      const _this = this.$refs.form1
      _this.form = {        
        orderId: data.orderId,
        totalNum: data.totalNum,
        commission: data.commission,
        totalPrice: data.totalPrice,
        payType: data.payType,
        status: data.status,
        addTime: data.addTime,
        payTime: data.payTime,
        couponOrderUseList: data.couponOrderUseList,
      }
      _this.dialog = true
    },
    refund(data) {
        this.isAdd = false
        const _this = this.$refs.form2
        _this.form = {
          id: data.id,
          orderId: data.orderId,
          refundPrice: '',
          refundStatus: '',
          refundReason: '',  
        }
        _this.dialog = true
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
