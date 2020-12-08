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
          <el-form-item label="关联订单号" prop="orderId">
            <el-input v-model="form.orderId" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="模板id" prop="tempId">
            <el-input v-model="form.tempId" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="模板名称" prop="tempName">
            <el-input v-model="form.tempName" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="签署文件地址">
            <el-input v-model="form.filePath" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="签署状态 0:签署中 1：签署完成" prop="status">
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
      <el-table ref="table" v-loading="crud.loading" :data="crud.data" size="small" style="width: 100%;" @selection-change="crud.selectionChangeHandler">
        <el-table-column type="selection" width="55" />
        <el-table-column v-if="columns.visible('id')" prop="id" label="id" />
        <el-table-column v-if="columns.visible('orderId')" prop="orderId" label="关联订单号" />
        <el-table-column v-if="columns.visible('tempId')" prop="tempId" label="模板id" />
        <el-table-column v-if="columns.visible('tempName')" prop="tempName" label="模板名称" />
        <el-table-column v-if="columns.visible('filePath')" prop="filePath" label="签署文件地址" />
        <el-table-column v-if="columns.visible('status')" prop="status" label="签署状态 0:签署中 1：签署完成" />
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
        <el-table-column v-permission="['admin','yxContractSign:edit','yxContractSign:del']" label="操作" width="150px" align="center">
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
import crudYxContractSign from '@/api/yxContractSign'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import udOperation from '@crud/UD.operation'
import pagination from '@crud/Pagination'
import MaterialList from "@/components/material";

// crud交由presenter持有
const defaultCrud = CRUD({ title: '合同签署', url: 'api/yxContractSign', sort: 'id,desc', crudMethod: { ...crudYxContractSign }})
const defaultForm = { id: null, orderId: null, tempId: null, tempName: null, filePath: null, status: null, delFlag: null, createUserId: null, updateUserId: null, createTime: null, updateTime: null }
export default {
  name: 'YxContractSign',
  components: { pagination, crudOperation, rrOperation, udOperation ,MaterialList},
  mixins: [presenter(defaultCrud), header(), form(defaultForm), crud()],
  data() {
    return {
      
      permission: {
        add: ['admin', 'yxContractSign:add'],
        edit: ['admin', 'yxContractSign:edit'],
        del: ['admin', 'yxContractSign:del']
      },
      rules: {
        orderId: [
          { required: true, message: '关联订单号不能为空', trigger: 'blur' }
        ],
        tempId: [
          { required: true, message: '模板id不能为空', trigger: 'blur' }
        ],
        tempName: [
          { required: true, message: '模板名称不能为空', trigger: 'blur' }
        ],
        status: [
          { required: true, message: '签署状态 0:签署中 1：签署完成不能为空', trigger: 'blur' }
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
