<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <!--如果想在工具栏加入更多按钮，可以使用插槽方式， slot = 'left' or 'right'-->
      <crudOperation :permission="permission" />
      <!--表单组件-->
      <el-dialog :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="500px">
        <el-form ref="form" :model="form" :rules="rules" size="small" label-width="80px">
          <el-form-item label="id">
            <el-input v-model="form.id" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="卡券订单id" prop="couponOrderId">
            <el-input v-model="form.couponOrderId" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="船只出港批次号" prop="batchNo">
            <el-input v-model="form.batchNo" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="船只id" prop="shipId">
            <el-input v-model="form.shipId" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="乘客姓名" prop="passengerName">
            <el-input v-model="form.passengerName" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="乘客身份证" prop="idCard">
            <el-input v-model="form.idCard" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="乘客电话" prop="phone">
            <el-input v-model="form.phone" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="0:未成年 1:成年人 2：老年人">
            <el-input v-model="form.isAdult" style="width: 370px;" />
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
      <el-table ref="table" v-loading="crud.loading" :data="crud.data" size="small" style="width: 100%;" @selection-change="crud.selectionChangeHandler">
        <el-table-column type="selection" width="55" />
        <el-table-column v-if="columns.visible('id')" prop="id" label="id" />
        <el-table-column v-if="columns.visible('couponOrderId')" prop="couponOrderId" label="卡券订单id" />
        <el-table-column v-if="columns.visible('batchNo')" prop="batchNo" label="船只出港批次号" />
        <el-table-column v-if="columns.visible('shipId')" prop="shipId" label="船只id" />
        <el-table-column v-if="columns.visible('passengerName')" prop="passengerName" label="乘客姓名" />
        <el-table-column v-if="columns.visible('idCard')" prop="idCard" label="乘客身份证" />
        <el-table-column v-if="columns.visible('phone')" prop="phone" label="乘客电话" />
        <el-table-column v-if="columns.visible('isAdult')" prop="isAdult" label="0:未成年 1:成年人 2：老年人" />
        <el-table-column v-if="columns.visible('delFlag')" prop="delFlag" label="是否删除（0：未删除，1：已删除）" />
        <el-table-column v-if="columns.visible('createUserId')" prop="createUserId" label="创建人" />
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
        <el-table-column v-permission="['admin','yxShipPassenger:edit','yxShipPassenger:del']" label="操作" width="150px" align="center">
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
import crudYxShipPassenger from '@/api/yxShipPassenger'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import udOperation from '@crud/UD.operation'
import pagination from '@crud/Pagination'
import MaterialList from "@/components/material";

// crud交由presenter持有
const defaultCrud = CRUD({ title: '船只乘客', url: 'api/yxShipPassenger', sort: 'id,desc', crudMethod: { ...crudYxShipPassenger }})
const defaultForm = { id: null, couponOrderId: null, batchNo: null, shipId: null, passengerName: null, idCard: null, phone: null, isAdult: null, delFlag: null, createUserId: null, updateUserId: null, createTime: null, updateTime: null }
export default {
  name: 'YxShipPassenger',
  components: { pagination, crudOperation, rrOperation, udOperation ,MaterialList},
  mixins: [presenter(defaultCrud), header(), form(defaultForm), crud()],
  data() {
    return {
      
      permission: {
        add: ['admin', 'yxShipPassenger:add'],
        edit: ['admin', 'yxShipPassenger:edit'],
        del: ['admin', 'yxShipPassenger:del']
      },
      rules: {
        couponOrderId: [
          { required: true, message: '卡券订单id不能为空', trigger: 'blur' }
        ],
        batchNo: [
          { required: true, message: '船只出港批次号不能为空', trigger: 'blur' }
        ],
        shipId: [
          { required: true, message: '船只id不能为空', trigger: 'blur' }
        ],
        passengerName: [
          { required: true, message: '乘客姓名不能为空', trigger: 'blur' }
        ],
        idCard: [
          { required: true, message: '乘客身份证不能为空', trigger: 'blur' }
        ],
        phone: [
          { required: true, message: '乘客电话不能为空', trigger: 'blur' }
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
