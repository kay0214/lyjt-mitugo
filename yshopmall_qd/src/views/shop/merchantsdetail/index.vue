<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <!--如果想在工具栏加入更多按钮，可以使用插槽方式， slot = 'left' or 'right'-->
      <crudOperation :permission="permission" />
      <!--表单组件-->
      <el-dialog append-to-body :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="570px">
        <el-form ref="form" :inline="true" :model="form" :rules="rules" size="small" label-width="120px">
            <el-form-item label="商户名称" prop="nickName">
              <el-input v-model="form.nickName"  style="width: 350px;"/>
            </el-form-item>
            <el-form-item label="商户联系人" prop="merchantsContact">
              <el-input v-model="form.merchantsContact"  style="width: 350px;"/>
            </el-form-item>
            <el-form-item label="商户联系人手机" prop="phone">
              <el-input v-model.number="form.phone"  style="width: 350px;"/>
            </el-form-item>
            <el-form-item label="用户名" prop="username">
              <el-input v-model="form.username"  style="width: 350px;"/>
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
        <el-table-column v-if="columns.visible('id')" prop="id" label="商户id" />
        <el-table-column v-if="columns.visible('merchantsName')" prop="merchantsName" label="商户名称" />
        <el-table-column v-if="columns.visible('contacts')" prop="contacts" label="商户联系人" />
        <el-table-column v-if="columns.visible('contactMobile')" prop="contactMobile" label="联系人电话" />
        <el-table-column v-if="columns.visible('delFlag')" prop="delFlag" label="商户状态" />
        <el-table-column label="商户状态" align="center">
          <template slot-scope="scope">
            <div>
              <el-tag v-if="scope.row.delFlag == 1">已禁用</el-tag>
              <el-tag v-else-if="scope.row.delFlag == 0">启用中</el-tag>
              <el-tag v-else></el-tag>
            </div>
          </template>
        </el-table-column>
        <el-table-column v-if="columns.visible('examineStatus')" prop="examineStatus" label="审批状态">
          <template slot-scope="scope">
            {{ dict.label.merchants_status[scope.row.examineStatus] }}
          </template>
        </el-table-column>
        <el-table-column v-permission="['admin','yxMerchantsDetail:edit','yxMerchantsDetail:del']" label="操作" width="150px" align="center">
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
import crudYxMerchantsDetail from '@/api/yxMerchantsDetail'
import { isvalidPhone } from '@/utils/validate'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import udOperation from '@crud/UD.operation'
import pagination from '@crud/Pagination'
import MaterialList from "@/components/material";

// crud交由presenter持有
const defaultCrud = CRUD({ title: '商户详情表', url: 'api/yxMerchantsDetail/getYxMerchantsDetailsList', sort: 'id,desc', crudMethod: { ...crudYxMerchantsDetail }})
const defaultForm = { id: null, uid: null, examineStatus: null, address: null, contacts: null, contactMobile: null, mailbox: null, merchantsType: null, bankNo: null, openAccountProvince: null, bankType: null, openAccountName: null, openAccountBank: null, openAccountSubbranch: null, companyProvince: null, companyAddress: null, companyName: null, companyLegalPerson: null, companyPhone: null, businessCategory: null, qualificationsType: null, delFlag: null, createUserId: null, updateUserId: null, createTime: null, updateTime: null, merchantsName: null }
export default {
  name: 'YxMerchantsDetail',
  components: { pagination, crudOperation, rrOperation, udOperation ,MaterialList},
  mixins: [presenter(defaultCrud), header(), form(defaultForm), crud()],
  dicts: ['merchants_status'],
  data() {
    // 自定义验证
    const validPhone = (rule, value, callback) => {
      if (!value) {
        callback(new Error('请输入电话号码'))
      } else if (!isvalidPhone(value)) {
        callback(new Error('请输入正确的11位手机号码'))
      } else {
        callback()
      }
    }
    return {
      permission: {
        add: ['admin', 'yxMerchantsDetail:add'],
        edit: ['admin', 'yxMerchantsDetail:edit'],
        del: ['admin', 'yxMerchantsDetail:del']
      },
      rules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
        ],
        nickName: [
          { required: true, message: '请输入用户昵称', trigger: 'blur' },
          { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
        ],
        merchantsContact: [
          { required: true, message: '请输入商户联系人', trigger: 'blur' },
          { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
        ],
        phone: [
          { required: true, trigger: 'blur', validator: validPhone }
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
