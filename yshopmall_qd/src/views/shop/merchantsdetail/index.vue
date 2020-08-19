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
          <el-form-item label="用户id">
            <el-input v-model="form.uid" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="审批状态：0->待审核,1->通过,2->驳回">
            <el-input v-model="form.examineStatus" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="商户地址">
            <el-input v-model="form.address" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="联系人">
            <el-input v-model="form.contacts" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="联系电话">
            <el-input v-model="form.contactMobile" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="邮箱">
            <el-input v-model="form.mailbox" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="认证类型：0->个人,1->企业,2->个体商户">
            <el-input v-model="form.merchantsType" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="银行账号">
            <el-input v-model="form.bankNo" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="开户省市">
            <el-input v-model="form.openAccountProvince" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="银行卡信息：0->对私账号,1->对公账号">
            <el-input v-model="form.bankType" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="开户名称">
            <el-input v-model="form.openAccountName" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="开户行">
            <el-input v-model="form.openAccountBank" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="开户支行">
            <el-input v-model="form.openAccountSubbranch" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="企业所在省市区">
            <el-input v-model="form.companyProvince" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="企业所在详细地址">
            <el-input v-model="form.companyAddress" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="公司名称">
            <el-input v-model="form.companyName" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="法定代表人">
            <el-input v-model="form.companyLegalPerson" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="公司电话">
            <el-input v-model="form.companyPhone" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="经营类目">
            <el-input v-model="form.businessCategory" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="主体资质类型">
            <el-input v-model="form.qualificationsType" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="是否删除（0：未删除，1：已删除）">
            <el-input v-model="form.delFlag" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="创建人">
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
          <el-form-item label="商户名称" prop="merchantsName">
            <el-input v-model="form.merchantsName" style="width: 370px;" />
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
        <el-table-column v-if="columns.visible('uid')" prop="uid" label="用户id" />
        <el-table-column v-if="columns.visible('examineStatus')" prop="examineStatus" label="审批状态：0->待审核,1->通过,2->驳回">
          <template slot-scope="scope">
            {{ dict.label.merchants_status[scope.row.examineStatus] }}
          </template>
        </el-table-column>
        <el-table-column v-if="columns.visible('address')" prop="address" label="商户地址" />
        <el-table-column v-if="columns.visible('contacts')" prop="contacts" label="联系人" />
        <el-table-column v-if="columns.visible('contactMobile')" prop="contactMobile" label="联系电话" />
        <el-table-column v-if="columns.visible('mailbox')" prop="mailbox" label="邮箱" />
        <el-table-column v-if="columns.visible('merchantsType')" prop="merchantsType" label="认证类型：0->个人,1->企业,2->个体商户" />
        <el-table-column v-if="columns.visible('bankNo')" prop="bankNo" label="银行账号" />
        <el-table-column v-if="columns.visible('openAccountProvince')" prop="openAccountProvince" label="开户省市" />
        <el-table-column v-if="columns.visible('bankType')" prop="bankType" label="银行卡信息：0->对私账号,1->对公账号" />
        <el-table-column v-if="columns.visible('openAccountName')" prop="openAccountName" label="开户名称" />
        <el-table-column v-if="columns.visible('openAccountBank')" prop="openAccountBank" label="开户行" />
        <el-table-column v-if="columns.visible('openAccountSubbranch')" prop="openAccountSubbranch" label="开户支行" />
        <el-table-column v-if="columns.visible('companyProvince')" prop="companyProvince" label="企业所在省市区" />
        <el-table-column v-if="columns.visible('companyAddress')" prop="companyAddress" label="企业所在详细地址" />
        <el-table-column v-if="columns.visible('companyName')" prop="companyName" label="公司名称" />
        <el-table-column v-if="columns.visible('companyLegalPerson')" prop="companyLegalPerson" label="法定代表人" />
        <el-table-column v-if="columns.visible('companyPhone')" prop="companyPhone" label="公司电话" />
        <el-table-column v-if="columns.visible('businessCategory')" prop="businessCategory" label="经营类目" />
        <el-table-column v-if="columns.visible('qualificationsType')" prop="qualificationsType" label="主体资质类型" />
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
        <el-table-column v-if="columns.visible('merchantsName')" prop="merchantsName" label="商户名称" />
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
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import udOperation from '@crud/UD.operation'
import pagination from '@crud/Pagination'
import MaterialList from "@/components/material";

// crud交由presenter持有
const defaultCrud = CRUD({ title: '商户详情表', url: 'api/yxMerchantsDetail', sort: 'id,desc', crudMethod: { ...crudYxMerchantsDetail }})
const defaultForm = { id: null, uid: null, examineStatus: null, address: null, contacts: null, contactMobile: null, mailbox: null, merchantsType: null, bankNo: null, openAccountProvince: null, bankType: null, openAccountName: null, openAccountBank: null, openAccountSubbranch: null, companyProvince: null, companyAddress: null, companyName: null, companyLegalPerson: null, companyPhone: null, businessCategory: null, qualificationsType: null, delFlag: null, createUserId: null, updateUserId: null, createTime: null, updateTime: null, merchantsName: null }
export default {
  name: 'YxMerchantsDetail',
  components: { pagination, crudOperation, rrOperation, udOperation ,MaterialList},
  mixins: [presenter(defaultCrud), header(), form(defaultForm), crud()],
  dicts: ['merchants_status'],
  data() {
    return {
      
      permission: {
        add: ['admin', 'yxMerchantsDetail:add'],
        edit: ['admin', 'yxMerchantsDetail:edit'],
        del: ['admin', 'yxMerchantsDetail:del']
      },
      rules: {
        merchantsName: [
          { required: true, message: '商户名称不能为空', trigger: 'blur' }
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
