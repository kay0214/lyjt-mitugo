<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <!--如果想在工具栏加入更多按钮，可以使用插槽方式， slot = 'left' or 'right'-->
      <crudOperation :permission="permission" />
      <!--表单组件-->
      <el-dialog :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="500px">
        <el-form ref="form" :model="form" :rules="rules" size="small" label-width="80px">
          <el-form-item label="店铺编号" prop="storeNid">
            <el-input v-model="form.storeNid" style="width: 370px;" disabled="true" />
          </el-form-item>
          <el-form-item label="店铺名称" prop="storeName">
            <el-input v-model="form.storeName" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="管理人用户名" prop="manageUserName">
            <el-input v-model="form.manageUserName" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="管理人电话" prop="manageMobile">
            <el-input v-model="form.manageMobile" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="店铺电话" prop="storeMobile">
            <el-input v-model="form.storeMobile" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="人均消费">
            <el-input v-model="form.perCapita" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="行业类别">
<!--            <el-select v-model="form.industryCategory" style="width: 370px;" />-->
            <el-select v-model="form.industryCategory" placeholder="行业类别" style="width: 370px;" >
              <el-option v-for="item in dict.dict.industry_category" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>
          <el-form-item label="店铺服务">
            <!--            <el-select v-model="form.industryCategory" style="width: 370px;" />-->
            <el-select v-model="form.industryCategory" placeholder="店铺服务" style="width: 370px;" >
              <!-- <el-option v-for="item in dict.dict.industry_category" :key="item.value" :label="item.label" :value="item.value" />-->
            </el-select>
          </el-form-item>

          <el-form-item label="店铺省市区" prop="storeProvince">
            <el-input v-model="form.storeProvince" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="店铺详细地址" prop="storeAddress">
            <el-input v-model="form.storeAddress" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="店铺介绍">
            <el-input v-model="form.introduction" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="地图坐标经度">
            <el-input v-model="form.coordinateX" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="地图坐标纬度">
            <el-input v-model="form.coordinateY" style="width: 370px;" />
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
        <el-table-column v-if="columns.visible('storeNid')" prop="storeNid" label="店铺编号" />
        <el-table-column v-if="columns.visible('storeName')" prop="storeName" label="店铺名称" />
        <el-table-column v-if="columns.visible('manageUserName')" prop="manageUserName" label="管理人用户名" />
        <el-table-column v-if="columns.visible('merId')" prop="merId" label="商户id" />
        <el-table-column v-if="columns.visible('partnerId')" prop="partnerId" label="合伙人id" />
        <el-table-column v-if="columns.visible('manageMobile')" prop="manageMobile" label="管理人电话" />
        <el-table-column v-if="columns.visible('storeMobile')" prop="storeMobile" label="店铺电话" />
        <el-table-column v-if="columns.visible('status')" prop="status" label="状态：0：上架，1：下架" />
        <el-table-column v-if="columns.visible('perCapita')" prop="perCapita" label="人均消费" />
        <el-table-column v-if="columns.visible('industryCategory')" prop="industryCategory" label="行业类别" />
        <el-table-column v-if="columns.visible('storeProvince')" prop="storeProvince" label="店铺省市区" />
        <el-table-column v-if="columns.visible('storeAddress')" prop="storeAddress" label="店铺详细地址" />
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
        <el-table-column v-if="columns.visible('introduction')" prop="introduction" label="店铺介绍" />
        <el-table-column v-if="columns.visible('coordinateX')" prop="coordinateX" label="地图坐标经度" />
        <el-table-column v-if="columns.visible('coordinateY')" prop="coordinateY" label="地图坐标纬度" />
        <el-table-column v-permission="['admin','yxStoreInfo:edit','yxStoreInfo:del']" label="操作" width="150px" align="center">
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
      <eForm :industry_category="dict.industry_category" />
    </div>
  </div>
</template>

<script>
import crudYxStoreInfo from '@/api/yxStoreInfo'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import udOperation from '@crud/UD.operation'
import pagination from '@crud/Pagination'
import MaterialList from "@/components/material";

// crud交由presenter持有
const defaultCrud = CRUD({ title: '店铺表', url: 'api/yxStoreInfo', sort: 'id,desc', crudMethod: { ...crudYxStoreInfo }})
const defaultForm = { id: null, storeNid: null, storeName: null, manageUserName: null, merId: null, partnerId: null, manageMobile: null, storeMobile: null, status: null, perCapita: null, industryCategory: null, storeProvince: null, storeAddress: null, delFlag: null, createUserId: null, updateUserId: null, createTime: null, updateTime: null, introduction: null, coordinateX: null, coordinateY: null }
export default {
  name: 'YxStoreInfo',
  components: { pagination, crudOperation, rrOperation, udOperation ,MaterialList},
  mixins: [presenter(defaultCrud), header(), form(defaultForm), crud()],
  data() {
    return {

      permission: {
        add: ['admin', 'yxStoreInfo:add'],
        edit: ['admin', 'yxStoreInfo:edit'],
        del: ['admin', 'yxStoreInfo:del']
      },
      rules: {
        storeNid: [
          { required: true, message: '店铺编号不能为空', trigger: 'blur' }
        ],
        storeName: [
          { required: true, message: '店铺名称不能为空', trigger: 'blur' }
        ],
        manageUserName: [
          { required: true, message: '管理人用户名不能为空', trigger: 'blur' }
        ],
        merId: [
          { required: true, message: '商户id不能为空', trigger: 'blur' }
        ],
        partnerId: [
          { required: true, message: '合伙人id不能为空', trigger: 'blur' }
        ],
        manageMobile: [
          { required: true, message: '管理人电话不能为空', trigger: 'blur' }
        ],
        storeMobile: [
          { required: true, message: '店铺电话不能为空', trigger: 'blur' }
        ],
        status: [
          { required: true, message: '状态：0：上架，1：下架不能为空', trigger: 'blur' }
        ],
        storeProvince: [
          { required: true, message: '店铺省市区不能为空', trigger: 'blur' }
        ],
        storeAddress: [
          { required: true, message: '店铺详细地址不能为空', trigger: 'blur' }
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
