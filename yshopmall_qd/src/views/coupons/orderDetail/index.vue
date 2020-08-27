<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <!--如果想在工具栏加入更多按钮，可以使用插槽方式， slot = 'left' or 'right'-->
      <crudOperation :permission="permission" />
      <!--表单组件-->
      <el-dialog :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="500px">
        <el-form ref="form" :model="form" :rules="rules" size="small" label-width="80px">
          <el-form-item label="订单ID">
            <el-input v-model="form.id" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="订单号">
            <el-input v-model="form.orderId" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="用户id">
            <el-input v-model="form.uid" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="卡券id">
            <el-input v-model="form.couponId" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="可被核销次数">
            <el-input v-model="form.useCount" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="已核销次数">
            <el-input v-model="form.usedCount" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="卡券状态（0:待支付 1:已过期 2:待发放3:支付失败4:待使用5:已使用6:已核销7:退款中8:已退款9:退款驳回">
            <el-input v-model="form.status" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="备注">
            <el-input v-model="form.remark" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="核销码">
            <el-input v-model="form.verifyCode" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="是否删除（0：未删除，1：已删除）">
            <el-input v-model="form.delFlag" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="创建人 根据创建人关联店铺">
            <el-input v-model="form.createUserId" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="修改人">
            <el-input v-model="form.updateUserId" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="创建时间">
            <el-input v-model="form.createTime" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="更新时间">
            <el-input v-model="form.updateTime" style="width: 370px;" />
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
        <el-table-column v-if="columns.visible('id')" prop="id" label="订单ID" />
        <el-table-column v-if="columns.visible('orderId')" prop="orderId" label="订单号" />
        <el-table-column v-if="columns.visible('uid')" prop="uid" label="用户id" />
        <el-table-column v-if="columns.visible('couponId')" prop="couponId" label="卡券id" />
        <el-table-column v-if="columns.visible('useCount')" prop="useCount" label="可被核销次数" />
        <el-table-column v-if="columns.visible('usedCount')" prop="usedCount" label="已核销次数" />
        <el-table-column v-if="columns.visible('status')" prop="status" label="卡券状态（0:待支付 1:已过期 2:待发放3:支付失败4:待使用5:已使用6:已核销7:退款中8:已退款9:退款驳回" />
        <el-table-column v-if="columns.visible('remark')" prop="remark" label="备注" />
        <el-table-column v-if="columns.visible('verifyCode')" prop="verifyCode" label="核销码" />
        <el-table-column v-if="columns.visible('delFlag')" prop="delFlag" label="是否删除（0：未删除，1：已删除）" />
        <el-table-column v-if="columns.visible('createUserId')" prop="createUserId" label="创建人 根据创建人关联店铺" />
        <el-table-column v-if="columns.visible('updateUserId')" prop="updateUserId" label="修改人" />
        <el-table-column v-if="columns.visible('createTime')" prop="createTime" label="创建时间">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.createTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column v-if="columns.visible('updateTime')" prop="updateTime" label="更新时间">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.updateTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column v-permission="['admin','yxCouponOrderDetail:edit','yxCouponOrderDetail:del']" label="操作" width="150px" align="center">
          <template slot-scope="scope">
            <udOperation
              :data="scope.row"
              :permission="permission"
            />
          </template>
        </el-table-column>
      </el-table>
      <!--分页组件-->
      <pagination />
    </div>
  </div>
</template>

<script>
import crudYxCouponOrderDetail from '@/api/yxCouponOrderDetail'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import udOperation from '@crud/UD.operation'
import pagination from '@crud/Pagination'
import MaterialList from "@/components/material";

// crud交由presenter持有
const defaultCrud = CRUD({ title: '卡券订单详情表', url: 'api/yxCouponOrderDetail', sort: 'id,desc', crudMethod: { ...crudYxCouponOrderDetail }})
const defaultForm = { id: null, orderId: null, uid: null, couponId: null, useCount: null, usedCount: null, status: null, remark: null, verifyCode: null, delFlag: null, createUserId: null, updateUserId: null, createTime: null, updateTime: null }
export default {
  name: 'YxCouponOrderDetail',
  components: { pagination, crudOperation, rrOperation, udOperation ,MaterialList},
  mixins: [presenter(defaultCrud), header(), form(defaultForm), crud()],
  data() {
    return {
      
      permission: {
        add: ['admin', 'yxCouponOrderDetail:add'],
        edit: ['admin', 'yxCouponOrderDetail:edit'],
        del: ['admin', 'yxCouponOrderDetail:del']
      },
      rules: {
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
