<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <!--如果想在工具栏加入更多按钮，可以使用插槽方式， slot = 'left' or 'right'-->
      <crudOperation :permission="permission" />
      <!--表单组件-->
      <el-dialog append-to-body :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="570px">
        <el-form ref="form" :inline="true" :model="form" :rules="rules" size="small" label-width="120px">
            <!-- 以下是新增展示字段 -->
            <div v-if="crud.status.add">
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
            </div>

            <!-- 以下是编辑页面展示字段 -->
            <div v-if="crud.status.edit">
              <el-form-item label="商户名称" prop="merchantsName">
                <el-input v-model="form.merchantsName"  style="width: 350px;"/>
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
            </div>
            <!--
            <el-form-item label="认证类型" prop="merchantsType">
              <el-radio-group v-model="form.type" size="mini" style="width: 370px">
                <el-radio-button label="0">个人</el-radio-button>
                <el-radio-button label="1">企业</el-radio-button>
                <el-radio-button label="2">个体商户</el-radio-button>
              </el-radio-group>
            </el-form-item>
            -->
            <el-form-item label="认证类型">
              <el-radio v-model="form.merchantsType" :label="0">个人</el-radio>
              <el-radio v-model="form.merchantsType" :label="1">企业</el-radio>
              <el-radio v-model="form.merchantsType" :label="2" style="width: 200px;">个体商户</el-radio>
            </el-form-item>

            <!-- 以下是个人认证 -->
            <!--  手持证件照 personIdCard、 证件照人像面 personIdCardFace、证件照国徽面personIdCardBack  -->
            <div v-if="form.merchantsType==0">
              <el-form-item label="银行账号">
                <el-input v-model="form.bankNo" style="width: 370px;" />
              </el-form-item>
              <el-form-item label="开户省市">
                <el-input v-model="form.openAccountProvince" style="width: 370px;" />
              </el-form-item>
              <el-form-item label="银行卡信息">
                <el-radio v-model="form.bankType" :label="0">对私账号</el-radio>
                <el-radio v-model="form.bankType" :label="1" style="width: 200px;">对公账号</el-radio>
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
            </div>
            <!-- 以下是企业 -->
            <div v-if="form.merchantsType==1">
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
            </div>

            <!-- 以下企业与个体户 -->
            <div v-if="form.merchantsType==1 || form.merchantsType==2">
            <!-- 下拉框 取值从dict的business_category和qualifications_type、两个下拉框联动 -->
              <el-form-item label="经营类目">
                <el-input v-model="form.businessCategory" style="width: 370px;" />
              </el-form-item>
              <el-form-item label="主体资质类型">
                <el-input v-model="form.qualificationsType" style="width: 370px;" />
              </el-form-item>            
            <!-- 营业执照 businessLicenseImg、银行开户证明 bankOpenProveImg、法人身份证头像面 legalIdCardFace、法人身份证国徽面 legalIdCardBack、门店照及经营场所 storeImg、医疗机构许可证 licenceImg -->
            </div>

            <!-- 审核意见、大点的文本框只用于展示 -->
            <el-form-item label="主体资质类型">
              <el-input v-model="form.examineRemark" style="width: 370px;" />
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
        <!-- 需要加一个审核按钮还有审核用的弹出框 -->
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
const defaultCrud = CRUD({ title: '商户详情表', url: 'api/yxMerchantsDetail/getYxMerchantsDetailsList', sort: 'id,desc', crudMethod: { ...crudYxMerchantsDetail },optShow: {
      add: true,
      edit: false,
      del: false,
      download: false
    }})
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
        examine: ['admin', 'yxMerchantsDetail:examine'],
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
