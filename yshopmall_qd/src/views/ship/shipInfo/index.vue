<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <!--如果想在工具栏加入更多按钮，可以使用插槽方式， slot = 'left' or 'right'-->
      <crudOperation :permission="permission" />
      <!--表单组件-->
      <el-dialog :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="500px">
        <el-form ref="form" :model="form" :rules="rules" size="small" label-width="80px">
         <!-- <el-form-item label="船只id">
            <el-input v-model="form.id" style="width: 370px;" />
          </el-form-item>-->
          <el-form-item label="船只名称" prop="shipName">
            <el-input v-model="form.shipName" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="船只系列id" prop="seriesId">
            <el-input v-model="form.seriesId" style="width: 370px;" />
          </el-form-item>
          <!--<el-form-item label="商户id" prop="merId">
            <el-input v-model="form.merId" style="width: 370px;" />
          </el-form-item>-->
          <el-form-item label="所属商铺" prop="storeId">
            <el-input v-model="form.storeId" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="帆船所属商户名" prop="merName">
            <el-input v-model="form.merName" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="帆船负责人" prop="managerName">
            <el-input v-model="form.managerName" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="负责人电话" prop="managerPhone">
            <el-input v-model="form.managerPhone" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="船只状态：0：启用，1：禁用" prop="shipStatus">
            <el-input v-model="form.shipStatus" style="width: 370px;" />
          </el-form-item>
          <!--<el-form-item label="船只当前状态：0：在港，1：离港。2：维修中" prop="currentStatus">
            <el-input v-model="form.currentStatus" style="width: 370px;" />
          </el-form-item>
          <!--<el-form-item label="最近一次出港时间" prop="lastLeaveTime">
            <el-input v-model="form.lastLeaveTime" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="最近一次返港时间" prop="lastReturnTime">
            <el-input v-model="form.lastReturnTime" style="width: 370px;" />
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
        <el-table-column v-if="columns.visible('id')" prop="id" label="船只id" />
        <el-table-column v-if="columns.visible('shipName')" prop="shipName" label="船只名称" />
        <el-table-column v-if="columns.visible('seriesId')" prop="seriesId" label="船只系列id" />
        <el-table-column v-if="columns.visible('merId')" prop="merId" label="商户id" />
        <el-table-column v-if="columns.visible('storeId')" prop="storeId" label="所属商铺" />
        <el-table-column v-if="columns.visible('merName')" prop="merName" label="帆船所属商户名" />
        <el-table-column v-if="columns.visible('managerName')" prop="managerName" label="帆船负责人" />
        <el-table-column v-if="columns.visible('managerPhone')" prop="managerPhone" label="负责人电话" />
        <el-table-column v-if="columns.visible('shipStatus')" prop="shipStatus" label="船只状态：0：启用，1：禁用" />
        <el-table-column v-if="columns.visible('currentStatus')" prop="currentStatus" label="船只当前状态：0：在港，1：离港。2：维修中" />
        <el-table-column v-if="columns.visible('lastLeaveTime')" prop="lastLeaveTime" label="最近一次出港时间" />
        <el-table-column v-if="columns.visible('lastReturnTime')" prop="lastReturnTime" label="最近一次返港时间" />
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
        <el-table-column v-permission="['admin','yxShipInfo:edit','yxShipInfo:del']" label="操作" width="150px" align="center">
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
import crudYxShipInfo from '@/api/yxShipInfo'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import udOperation from '@crud/UD.operation'
import pagination from '@crud/Pagination'
import MaterialList from "@/components/material";

// crud交由presenter持有
const defaultCrud = CRUD({ title: '船只管理', url: 'api/yxShipInfo', sort: 'id,desc', crudMethod: { ...crudYxShipInfo }})
const defaultForm = { id: null, shipName: null, seriesId: null, merId: null, storeId: null, merName: null, managerName: null, managerPhone: null, shipStatus: null, currentStatus: null, lastLeaveTime: null, lastReturnTime: null, delFlag: null, createUserId: null, updateUserId: null, createTime: null, updateTime: null }
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
          { required: true, message: '船只系列id不能为空', trigger: 'blur' }
        ],
        merId: [
          { required: true, message: '商户id不能为空', trigger: 'blur' }
        ],
        storeId: [
          { required: true, message: '所属商铺不能为空', trigger: 'blur' }
        ],
        merName: [
          { required: true, message: '帆船所属商户名不能为空', trigger: 'blur' }
        ],
        managerName: [
          { required: true, message: '帆船负责人不能为空', trigger: 'blur' }
        ],
        managerPhone: [
          { required: true, message: '负责人电话不能为空', trigger: 'blur' }
        ],
        shipStatus: [
          { required: true, message: '船只状态：0：启用，1：禁用不能为空', trigger: 'blur' }
        ],
        currentStatus: [
          { required: true, message: '船只当前状态：0：在港，1：离港。2：维修中不能为空', trigger: 'blur' }
        ],
        lastLeaveTime: [
          { required: true, message: '最近一次出港时间不能为空', trigger: 'blur' }
        ],
        lastReturnTime: [
          { required: true, message: '最近一次返港时间不能为空', trigger: 'blur' }
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
