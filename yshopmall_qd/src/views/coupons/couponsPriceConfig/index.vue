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
          <el-form-item label="卡券id" prop="couponId">
            <el-input v-model="form.couponId" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="开始日期(YYYYMMDD)">
            <el-input v-model="form.startDate" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="结束日期(YYYYMMDD)">
            <el-input v-model="form.endDate" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="销售价格" prop="sellingPrice">
            <el-input v-model="form.sellingPrice" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="佣金" prop="commission">
            <el-input v-model="form.commission" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="景区推广价格" prop="scenicPrice">
            <el-input v-model="form.scenicPrice" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="旅行社价格" prop="travelPrice">
            <el-input v-model="form.travelPrice" style="width: 370px;" />
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
        <el-table-column v-if="columns.visible('couponId')" prop="couponId" label="卡券id" />
        <el-table-column v-if="columns.visible('startDate')" prop="startDate" label="开始日期(YYYYMMDD)" />
        <el-table-column v-if="columns.visible('endDate')" prop="endDate" label="结束日期(YYYYMMDD)" />
        <el-table-column v-if="columns.visible('sellingPrice')" prop="sellingPrice" label="销售价格" />
        <el-table-column v-if="columns.visible('commission')" prop="commission" label="佣金" />
        <el-table-column v-if="columns.visible('scenicPrice')" prop="scenicPrice" label="景区推广价格" />
        <el-table-column v-if="columns.visible('travelPrice')" prop="travelPrice" label="旅行社价格" />
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
        <el-table-column v-permission="['admin','yxCouponsPriceConfig:edit','yxCouponsPriceConfig:del']" label="操作" width="150px" align="center">
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
import crudYxCouponsPriceConfig from '@/api/yxCouponsPriceConfig'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import udOperation from '@crud/UD.operation'
import pagination from '@crud/Pagination'
import MaterialList from "@/components/material";

// crud交由presenter持有
const defaultCrud = CRUD({ title: '本地生活价格配置', url: 'api/yxCouponsPriceConfig', sort: 'id,desc', crudMethod: { ...crudYxCouponsPriceConfig }})
const defaultForm = { id: null, couponId: null, startDate: null, endDate: null, sellingPrice: null, commission: null, scenicPrice: null, travelPrice: null, delFlag: null, createUserId: null, updateUserId: null, createTime: null, updateTime: null }
export default {
  name: 'YxCouponsPriceConfig',
  components: { pagination, crudOperation, rrOperation, udOperation ,MaterialList},
  mixins: [presenter(defaultCrud), header(), form(defaultForm), crud()],
  data() {
    return {
      
      permission: {
        add: ['admin', 'yxCouponsPriceConfig:add'],
        edit: ['admin', 'yxCouponsPriceConfig:edit'],
        del: ['admin', 'yxCouponsPriceConfig:del']
      },
      rules: {
        couponId: [
          { required: true, message: '卡券id不能为空', trigger: 'blur' }
        ],
        sellingPrice: [
          { required: true, message: '销售价格不能为空', trigger: 'blur' }
        ],
        commission: [
          { required: true, message: '佣金不能为空', trigger: 'blur' }
        ],
        scenicPrice: [
          { required: true, message: '景区推广价格不能为空', trigger: 'blur' }
        ],
        travelPrice: [
          { required: true, message: '旅行社价格不能为空', trigger: 'blur' }
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
