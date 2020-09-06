<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <!--如果想在工具栏加入更多按钮，可以使用插槽方式， slot = 'left' or 'right'-->
      <crudOperation :permission="permission" />
      <!--表单组件-->
      <el-dialog :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="500px">
        <el-form ref="form" :model="form" :rules="rules" size="small" label-width="80px">
          <el-form-item label="主键">
            <el-input v-model="form.id" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="1:微商城下单,2:本地生活下单,3:微商城退款,4:本地生活退款" prop="type">
            <el-input v-model="form.type" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="用户uid">
            <el-input v-model="form.uid" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="用户名">
            <el-input v-model="form.username" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="订单号" prop="orderId">
            <el-input v-model="form.orderId" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="明细种类; 0:支出;1:收入" prop="pm">
            <el-input v-model="form.pm" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="订单金额" prop="orderAmount">
            <el-input v-model="form.orderAmount" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="订单日期" prop="addTime">
            <el-input v-model="form.addTime" style="width: 370px;" />
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
        <el-table-column v-if="columns.visible('id')" prop="id" label="主键" />
        <el-table-column v-if="columns.visible('type')" prop="type" label="1:微商城下单,2:本地生活下单,3:微商城退款,4:本地生活退款" />
        <el-table-column v-if="columns.visible('uid')" prop="uid" label="用户uid" />
        <el-table-column v-if="columns.visible('username')" prop="username" label="用户名" />
        <el-table-column v-if="columns.visible('orderId')" prop="orderId" label="订单号" />
        <el-table-column v-if="columns.visible('pm')" prop="pm" label="明细种类; 0:支出;1:收入" />
        <el-table-column v-if="columns.visible('orderAmount')" prop="orderAmount" label="订单金额" />
        <el-table-column v-if="columns.visible('addTime')" prop="addTime" label="订单日期">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.addTime) }}</span>
          </template>
        </el-table-column>
        <!-- <el-table-column v-permission="['admin','fundsDetail:edit','fundsDetail:del']" label="操作" width="150px" align="center">
          <template slot-scope="scope">
            <udOperation
              :data="scope.row"
              :permission="permission"
            />
          </template>
        </el-table-column> -->
      </el-table>
      <!--分页组件-->
      <pagination />
    </div>
  </div>
</template>

<script>
import crudFundsDetail from '@/api/fundsDetail'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import udOperation from '@crud/UD.operation'
import pagination from '@crud/Pagination'
import MaterialList from "@/components/material";

// crud交由presenter持有
const defaultCrud = CRUD({ title: '平台资金明细', url: 'api/fundsDetail', sort: 'id,desc', crudMethod: { ...crudFundsDetail }})
const defaultForm = { id: null, type: null, uid: null, username: null, orderId: null, pm: null, orderAmount: null, addTime: null }
export default {
  name: 'FundsDetail',
  components: { pagination, crudOperation, rrOperation, udOperation ,MaterialList},
  mixins: [presenter(defaultCrud), header(), form(defaultForm), crud()],
  data() {
    return {
      
      permission: {
        add: ['admin', 'fundsDetail:add'],
        edit: ['admin', 'fundsDetail:edit'],
        del: ['admin', 'fundsDetail:del']
      },
      rules: {
        type: [
          { required: true, message: '1:微商城下单,2:本地生活下单,3:微商城退款,4:本地生活退款不能为空', trigger: 'blur' }
        ],
        orderId: [
          { required: true, message: '订单号不能为空', trigger: 'blur' }
        ],
        pm: [
          { required: true, message: '明细种类; 0:支出;1:收入不能为空', trigger: 'blur' }
        ],
        orderAmount: [
          { required: true, message: '订单金额不能为空', trigger: 'blur' }
        ],
        addTime: [
          { required: true, message: '订单日期不能为空', trigger: 'blur' }
        ]
      }    }
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
