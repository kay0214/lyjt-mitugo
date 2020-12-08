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
          <el-form-item label="类型：0:商品购买 1:本地生活" prop="rateType">
            <el-input v-model="form.rateType" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="卡券/商品关联id" prop="linkId">
            <el-input v-model="form.linkId" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="平台抽成">
            <el-input v-model="form.fundsRate" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="分享人">
            <el-input v-model="form.shareRate" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="分享人上级">
            <el-input v-model="form.shareParentRate" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="推荐人">
            <el-input v-model="form.parentRate" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="商户">
            <el-input v-model="form.merRate" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="合伙人">
            <el-input v-model="form.partnerRate" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="拉新池">
            <el-input v-model="form.referenceRate" style="width: 370px;" />
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
        <el-table-column v-if="columns.visible('id')" prop="id" label="主键" />
        <el-table-column v-if="columns.visible('rateType')" prop="rateType" label="类型：0:商品购买 1:本地生活" />
        <el-table-column v-if="columns.visible('linkId')" prop="linkId" label="卡券/商品关联id" />
        <el-table-column v-if="columns.visible('fundsRate')" prop="fundsRate" label="平台抽成" />
        <el-table-column v-if="columns.visible('shareRate')" prop="shareRate" label="分享人" />
        <el-table-column v-if="columns.visible('shareParentRate')" prop="shareParentRate" label="分享人上级" />
        <el-table-column v-if="columns.visible('parentRate')" prop="parentRate" label="推荐人" />
        <el-table-column v-if="columns.visible('merRate')" prop="merRate" label="商户" />
        <el-table-column v-if="columns.visible('partnerRate')" prop="partnerRate" label="合伙人" />
        <el-table-column v-if="columns.visible('referenceRate')" prop="referenceRate" label="拉新池" />
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
        <el-table-column v-permission="['admin','yxCustomizeRate:edit','yxCustomizeRate:del']" label="操作" width="150px" align="center">
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
import crudYxCustomizeRate from '@/api/yxCustomizeRate'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import udOperation from '@crud/UD.operation'
import pagination from '@crud/Pagination'
import MaterialList from "@/components/material";

// crud交由presenter持有
const defaultCrud = CRUD({ title: '商品&卡券费率配置', url: 'api/yxCustomizeRate', sort: 'id,desc', crudMethod: { ...crudYxCustomizeRate }})
const defaultForm = { id: null, rateType: null, linkId: null, fundsRate: null, shareRate: null, shareParentRate: null, parentRate: null, merRate: null, partnerRate: null, referenceRate: null, delFlag: null, createUserId: null, updateUserId: null, createTime: null, updateTime: null }
export default {
  name: 'YxCustomizeRate',
  components: { pagination, crudOperation, rrOperation, udOperation ,MaterialList},
  mixins: [presenter(defaultCrud), header(), form(defaultForm), crud()],
  data() {
    return {

      permission: {
        add: ['admin', 'yxCustomizeRate:add'],
        edit: ['admin', 'yxCustomizeRate:edit'],
        del: ['admin', 'yxCustomizeRate:del']
      },
      rules: {
        rateType: [
          { required: true, message: '类型：0:商品购买 1:本地生活不能为空', trigger: 'blur' }
        ],
        linkId: [
          { required: true, message: '卡券/商品关联id不能为空', trigger: 'blur' }
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
