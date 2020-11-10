<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <!--如果想在工具栏加入更多按钮，可以使用插槽方式， slot = 'left' or 'right'-->
      <!--      <crudOperation :permission="permission" />-->
      <!--表单组件-->
      <el-dialog :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="500px">
        <el-form ref="form" :model="form" :rules="rules" disabled size="small" label-width="80px">
          <el-form-item label="船只名称">
            <el-input v-model="form.shipName" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="船长姓名">
            <el-input v-model="form.captainName" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="承载人数">
            <el-input v-model="form.totalPassenger" style="width: 370px;" >
              <template slot="append">人</template>
            </el-input>
          </el-form-item>
          <el-form-item label="老年人人数">
            <el-input v-model="form.oldPassenger" style="width: 370px;" >
              <template slot="append">人</template>
            </el-input>
          </el-form-item>
          <el-form-item label="未成年人数">
            <el-input v-model="form.underagePassenger" style="width: 370px;" >
              <template slot="append">人</template>
            </el-input>
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
        <el-table-column v-if="columns.visible('shipId')" prop="shipId" label="船只id" />
        <el-table-column v-if="columns.visible('batchNo')" prop="batchNo" label="船只出港批次号" />
        <el-table-column v-if="columns.visible('shipName')" prop="shipName" label="船只名称" />
        <el-table-column v-if="columns.visible('captainId')" prop="captainId" label="船长id" />
        <el-table-column v-if="columns.visible('captainName')" prop="captainName" label="船长姓名" />
        <el-table-column v-if="columns.visible('useId')" prop="useId" label="核销人id" />
        <el-table-column v-if="columns.visible('useName')" prop="useName" label="核销人姓名" />
        <el-table-column v-if="columns.visible('healthStatus')" prop="healthStatus" label="乘客身体状况" />
        <el-table-column v-if="columns.visible('totalPassenger')" prop="totalPassenger" label="承载人数" />
        <el-table-column v-if="columns.visible('oldPassenger')" prop="oldPassenger" label="老年人人数" />
        <el-table-column v-if="columns.visible('underagePassenger')" prop="underagePassenger" label="未成年人数" />
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
        <el-table-column v-permission="['admin','yxShipOperationDetail:edit','yxShipOperationDetail:del']" label="操作" width="150px" align="center">
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
import crudYxShipOperationDetail from '@/api/yxShipOperationDetail'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import udOperation from '@crud/UD.operation'
import pagination from '@crud/Pagination'
import MaterialList from "@/components/material";

// crud交由presenter持有
const defaultCrud = CRUD({ title: '运营记录详情', url: 'api/yxShipOperationDetail', sort: 'id,desc', crudMethod: { ...crudYxShipOperationDetail }})
const defaultForm = { id: null, couponOrderId: null, shipId: null, batchNo: null, shipName: null, captainId: null, captainName: null, useId: null, useName: null, healthStatus: null, totalPassenger: null, oldPassenger: null, underagePassenger: null, delFlag: null, createUserId: null, updateUserId: null, createTime: null, updateTime: null }
export default {
  name: 'YxShipOperationDetail',
  components: { pagination, crudOperation, rrOperation, udOperation ,MaterialList},
  mixins: [presenter(defaultCrud), header(), form(defaultForm), crud()],
  data() {
    return {

      permission: {
        add: ['admin', 'yxShipOperationDetail:add'],
        edit: ['admin', 'yxShipOperationDetail:edit'],
        del: ['admin', 'yxShipOperationDetail:del']
      },
      rules: {
        couponOrderId: [
          { required: true, message: '卡券订单id不能为空', trigger: 'blur' }
        ],
        shipId: [
          { required: true, message: '船只id不能为空', trigger: 'blur' }
        ],
        batchNo: [
          { required: true, message: '船只出港批次号不能为空', trigger: 'blur' }
        ],
        captainId: [
          { required: true, message: '船长id不能为空', trigger: 'blur' }
        ],
        useId: [
          { required: true, message: '核销人id不能为空', trigger: 'blur' }
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
      this.crud.url='api/yxShipOperation/getOperationDetailInfo/'+this.$route.param.id
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
