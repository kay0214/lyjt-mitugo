<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <!--如果想在工具栏加入更多按钮，可以使用插槽方式， slot = 'left' or 'right'-->
      <crudOperation :permission="permission" />
      <!--表单组件-->
      <el-dialog append-to-body :close-on-click-modal="false" :before-close="dialogBeforeCancel" :visible.sync="crud.status.cu>0 || dialogVisible"
        :title="crud.status.title" width="570px">
        <el-form ref="form" :inline="true" :model="form" :rules="rules" size="small" label-width="120px" :disabled='formDisabled'>
            <!-- 以下是新增展示字段 -->
            <div v-if="crud.status.add">
              <el-form-item label="商户名称" prop="nickName">
                <el-input v-model="form.nickName" :maxlength='20' style="width: 350px;"/>
              </el-form-item>
              <el-form-item label="商户联系人" prop="merchantsContact">
                <el-input v-model="form.merchantsContact" :maxlength='20' style="width: 350px;"/>
              </el-form-item>
              <el-form-item label="商户联系人手机" prop="phone">
                <el-input v-model.number="form.phone"  style="width: 350px;"/>
              </el-form-item>
              <el-form-item label="用户名" prop="username">
                <el-input v-model="form.username" :maxlength='20' style="width: 350px;"/>
              </el-form-item>
            </div>

            <!-- 以下是编辑页面展示字段 -->
            <div v-if="crud.status.edit || examineEdit">
              <el-form-item label="商户名称" prop="merchantsName">
                <el-input v-model="form.merchantsName" :maxlength='20' style="width: 350px;"/>
              </el-form-item>
              <el-form-item label="商户地址" prop="address">
                <el-input v-model="form.address" style="width: 370px;"  :maxlength='200' />
              </el-form-item>
              <el-form-item label="联系人" prop="contacts">
                <el-input v-model="form.contacts" :maxlength='20' style="width: 370px;" />
              </el-form-item>
              <el-form-item label="联系电话" prop="contactMobile">
                <el-input v-model="form.contactMobile" style="width: 370px;" />
              </el-form-item>
              <el-form-item label="邮箱" prop="mailbox">
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
            <div v-if="!crud.status.add">
              <el-form-item label="银行账号" prop="bankNo">
                <el-input v-model="form.bankNo" :maxlength='20' style="width: 370px;" />
              </el-form-item>
              <el-form-item label="开户省市" prop="openAccountProvince">
                <el-input v-model="form.openAccountProvince" style="width: 370px;" />
              </el-form-item>
              <el-form-item label="银行卡信息" prop="bankType">
                <el-radio v-model="form.bankType" :label="0" >对私账号</el-radio>
                <el-radio v-model="form.bankType" :label="1" style="width: 200px;">对公账号</el-radio>
              </el-form-item>
              <el-form-item label="收款户名" prop="openAccountName">
                <el-input v-model="form.openAccountName" :maxlength='40' style="width: 370px;" />
              </el-form-item>
              <el-form-item label="开户行" prop="openAccountBank">
                <el-input v-model="form.openAccountBank" :maxlength='20' style="width: 370px;" />
              </el-form-item>
              <el-form-item label="开户支行" prop="openAccountSubbranch">
                <el-input v-model="form.openAccountSubbranch" :maxlength='20' style="width: 370px;" />
              </el-form-item>
              <el-form-item  label="联行号" prop="bankCode"  :rules="bankCodeRuls()">
                <el-input v-model="form.bankCode" style="width: 370px;">
                  <el-link  slot="append" href="https://www.icvio.cn" target="_blank">联行号查询</el-link>
                </el-input>
              </el-form-item>
              <el-form-item label="认证类型" prop="merchantsType">
                <el-radio v-model="form.merchantsType" :label="0">个人</el-radio>
                <el-radio v-model="form.merchantsType" :label="1">企业</el-radio>
                <el-radio v-model="form.merchantsType" :label="2" style="width: 200px;">个体商户</el-radio>
              </el-form-item>
            </div>

            <!-- 以下是个人认证 -->
            <div v-if="!crud.status.add && form.merchantsType == 0">
              <el-form-item label="手持证件照" prop="personIdCard">
                <MaterialList v-model="perIdCard" type="image" :num="1" :width="150" :height="150" :readonly='formDisabled' />
              </el-form-item>
              <el-form-item label="证件照人像面" prop="personIdCardFace">
                <MaterialList v-model="perIdCardFace" type="image" :num="1" :width="150" :height="150" :readonly='formDisabled' />
              </el-form-item>
              <el-form-item label="证件照国徽面" prop="personIdCardBack">
                <MaterialList v-model="perIdCardBack" type="image" :num="1" :width="150" :height="150" :readonly='formDisabled'/>
              </el-form-item>
            </div>
            <!-- 以下是企业 -->
            <div v-if="!crud.status.add &&form.merchantsType==1">
              <el-form-item label="企业所在省市区" prop="companyProvince">
                <el-input v-model="form.companyProvince" style="width: 370px;" />
              </el-form-item>
              <el-form-item label="企业所在详细地址" prop="companyAddress">
                <el-input v-model="form.companyAddress" :maxlength='200' style="width: 370px;" />
              </el-form-item>
              <el-form-item label="公司名称" prop="companyName">
                <el-input v-model="form.companyName" :maxlength='40' style="width: 370px;" />
              </el-form-item>
              <el-form-item label="法定代表人" prop="companyLegalPerson">
                <el-input v-model="form.companyLegalPerson" :maxlength='20' style="width: 370px;" />
              </el-form-item>
              <el-form-item label="公司电话" prop="companyPhone">
                <el-input v-model="form.companyPhone" :maxlength='20' style="width: 370px;" />
              </el-form-item>
            </div>

            <!-- 以下企业与个体户 -->
            <div v-if="!crud.status.add && (form.merchantsType==1 || form.merchantsType==2)">
            <!-- 下拉框 取值从dict的business_category和qualifications_type、两个下拉框联动 -->
              <el-form-item label="经营类目" prop="businessCategory">
                <!-- <el-input v-model="form.businessCategory" style="width: 370px;" /> -->
                <el-select v-model="form.businessCategory" placeholder="请选择" @change="businessCategoryChange">
                  <el-option
                    v-for="item in dict.business_category"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="主体资质类型" prop="qualificationsType">
                <!-- <el-input v-model="form.qualificationsType" style="width: 370px;" /> -->
                <el-select v-model="form.qualificationsType" placeholder="请选择">
                  <el-option
                    v-for="item in qualificationsType"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="营业执照" prop="businessLicenseImg">
                <MaterialList v-model="businessLicenseImg" type="image" :num="1" :width="150" :height="150" :readonly='Boolean(formDisabled)'/>
              </el-form-item>
              <el-form-item label="银行开户证明" prop="bankOpenProveImg">
                <MaterialList v-model="bankOpenProveImg" type="image" :num="1" :width="150" :height="150" :readonly='Boolean(formDisabled)'/>
              </el-form-item>
              <el-form-item label="法人身份证头像面" prop="legalIdCardFace">
                <MaterialList v-model="legalIdCardFace" type="image" :num="1" :width="150" :height="150" :readonly='Boolean(formDisabled)'/>
              </el-form-item>
              <el-form-item label="法人身份证国徽面" prop="legalIdCardBack">
                <MaterialList v-model="legalIdCardBack" type="image" :num="1" :width="150" :height="150" :readonly='Boolean(formDisabled)'/>
              </el-form-item>
              <el-form-item label="门店照及经营场所" prop="storeImg">
                <MaterialList v-model="storeImg" type="image" :num="1" :width="150" :height="150" :readonly='Boolean(formDisabled)'/>
              </el-form-item>
              <el-form-item label="医疗机构许可证" prop="licenceImg">
                <MaterialList v-model="licenceImg" type="image" :num="1" :width="150" :height="150" :readonly='Boolean(formDisabled)'/>
              </el-form-item>
            </div>            
            <el-form-item v-if='crud.status.edit' label=" " prop="checkbox">
                <el-row type='flex'>
                  <el-checkbox v-model="form.checkbox" name='checkbox' ></el-checkbox>
                  <span style='margin:0 10px'>我已阅读并同意</span>
                  <span style='margin:0 10px'><router-link to="/member/yxMerchantsDetail/pdf1" target="_blank">协议一 </router-link></span>
                  <span style='margin:0 10px'><router-link to="/member/yxMerchantsDetail/pdf2" target="_blank">协议二 </router-link></span>
                </el-row>
            </el-form-item>
            <el-form-item v-else-if='!crud.status.add' label=" ">
              <el-row type='flex'>
                    <el-checkbox v-model="form.checkbox" name='checkbox' checked readonly></el-checkbox>
                    <span style='margin:0 10px'>我已阅读并同意</span>
                    <span style='margin:0 10px'><router-link to="/member/yxMerchantsDetail/pdf1" target="_blank">协议一 </router-link></span>
                    <span style='margin:0 10px'><router-link to="/member/yxMerchantsDetail/pdf2" target="_blank">协议二 </router-link></span>
                </el-row>
            </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">          
          <div v-if="crud.status.add || crud.status.edit">
            <el-button type="text" @click="crud.cancelCU">取消</el-button>
            <el-button :loading="crud.cu === 2" type="primary" @click="crud.submitCU">确认</el-button>
          </div>
          <div v-if="readStatus">
            <el-button type="primary" @click="closeRead()">关闭</el-button>
          </div>
          <div v-if="examineEdit">
            <el-row style='marginBottom:20px'>
              <el-col :span='5' style='marginRight:12px'>
                <label>审核意见</label>
              </el-col>
              <el-col :span="17">
              <el-input
                  type="textarea"
                  :rows="5"
                  placeholder="请输入内容"
                  maxlength="50"
                  v-model="form.examineRemark">
                </el-input>
              </el-col>
            </el-row>
            <el-button type="text" @click="examineEditCancelCU">驳回</el-button>
            <el-button :loading="examineEdit === 2" type="primary" @click="examineEditSubmitCU">通过</el-button>
          </div>
        </div>

      </el-dialog>
      <!--表格渲染-->
      <el-table ref="table" v-loading="crud.loading" :data="crud.data" size="small" style="width: 100%;" @selection-change="crud.selectionChangeHandler">
        <el-table-column type="selection" width="55" />
        <el-table-column v-if="columns.visible('id')" prop="id" label="商户id" />
        <el-table-column v-if="columns.visible('username')" prop="username" label="商户用户名" />
        <el-table-column v-if="columns.visible('merchantsName')" prop="merchantsName" label="商户名称" />
        <el-table-column v-if="columns.visible('contacts')" prop="contacts" label="商户联系人" />
        <el-table-column v-if="columns.visible('contactMobile')" prop="contactMobile" label="联系人电话" />
        <el-table-column v-if="columns.visible('withdrawalAmount')" prop="withdrawalAmount" label="可提现金额" />
        <el-table-column label="商户状态" align="center">
          <template slot-scope="scope">
            <div @click="updateStatus(scope.row.uid,scope.row.status)">
              <el-tag v-if="scope.row.status == 1">已禁用</el-tag>
              <el-tag v-else-if="scope.row.status == 0">启用中</el-tag>
              <el-tag v-else></el-tag>
            </div>
          </template>
        </el-table-column>
        <el-table-column v-if="columns.visible('examineStatus')" prop="examineStatus" label="审批状态">
          <template slot-scope="scope">
            {{ dict.label.merchants_status[scope.row.examineStatus] }}
          </template>
        </el-table-column>
        <el-table-column v-permission="['admin','yxMerchantsDetail:edit','yxMerchantsDetail:examine','yxMerchantsDetail:del']" label="操作" width="250px" align="center">
          <template slot-scope="scope">
            <el-button v-if="scope.row.examineStatus===3" v-permission="permission.examine" size="mini" type="primary" icon="el-icon-s-check" @click="examineOpt(scope.row)" plain></el-button>
            <el-button v-if="scope.row.examineStatus===2 || scope.row.examineStatus===0" v-permission="permission.edit" size="mini" type="primary" icon="el-icon-edit" @click="crud.toEdit(scope.row)" ></el-button>

            <el-button size="mini" type="success" icon="el-icon-reading" @click="toRead(scope.row)" plain ></el-button>

            <!-- <el-button v-if="scope.row.examineStatus===3" v-permission="permission.examine" size="mini" type="primary" icon="el-icon-s-check" @click="examineOpt(scope.row)" plain></el-button> -->
            <br/>
<el-popover
  placement="left"
  width="260"
trigger="click">
  <el-form ref='formWithdraw' :model="formWithdraw" :rules="rules" style='padding:10px 20px;'>
  <p>提现金额调整</p>
    <el-form-item label="类型" prop='ptype'>
      <el-radio v-model="formWithdraw.ptype" :label="1" style='margin-left:20px;'>增</el-radio>
      <el-radio v-model="formWithdraw.ptype" :label="0">减</el-radio>
    </el-form-item>
    <el-form-item label="金额" prop='money'>
      <el-input v-model="formWithdraw.money " placeholder="金额" :maxlength='12'/>
    </el-form-item>
  <div style="text-align: right; margin: 0">
    <el-button type="primary" size="mini" @click="withdrawEdit($event,formWithdraw,scope.row.uid)">提交</el-button>
  </div>
  </el-form>
  <el-button v-permission="permission.modify" size="small" type="primary" icon="el-icon-edit" slot="reference" style='marginTop:10px' plain>修改金额</el-button>
</el-popover>

          </template>
        </el-table-column>
      </el-table>
      <!--分页组件-->
      <pagination />
    </div>
  </div>
</template>

<script>
import crudYxMerchantsDetail,{examine as examineSubmit,update,modiyfMerWithdrawal as withdrawEdit } from '@/api/yxMerchantsDetail'
import { isvalidPhone ,amountValid} from '@/utils/validate'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import udOperation from '@crud/UD.operation'
import pagination from '@crud/Pagination'
import MaterialList from "@/components/material";
import { get as getDictDetail } from '@/api/system/dictDetail'
import { Notification } from 'element-ui'
import checkPermission from '@/utils/permission'

// crud交由presenter持有
const defaultCrud = CRUD({ title: '商户详情表', url: 'api/yxMerchantsDetail/getYxMerchantsDetailsList', sort: 'id,desc', crudMethod: { ...crudYxMerchantsDetail },optShow: {
      add: true,
      edit: false,
      del: false,
      download: false
    }})
const defaultForm = { id: null, uid: null, nickName:null, merchantsContact:null, phone:null, username:null, examineStatus: null, address: null, contacts: null, contactMobile: null, mailbox: null, merchantsType: null, bankNo: null, openAccountProvince: null, bankType: null, openAccountName: null, openAccountBank: null, openAccountSubbranch: null, companyProvince: null, companyAddress: null, companyName: null, companyLegalPerson: null, companyPhone: null, businessCategory: null, qualificationsType: null, delFlag: null, createUserId: null, updateUserId: null, createTime: null, updateTime: null, merchantsName: null,bankCode: null,withdrawalAmount: null }
export default {
  name: 'YxMerchantsDetail',
  components: { pagination, crudOperation, rrOperation, udOperation ,MaterialList},
  mixins: [presenter(defaultCrud), header(), form(defaultForm), crud()],
  dicts: ['merchants_status','business_category'],
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
      bankType: defaultForm.bankType,
      formWithdraw:{},
      permission: {
        add: ['admin', 'yxMerchantsDetail:insert'],
        edit: ['admin', 'yxMerchantsDetail:edit'],
        examine: ['admin', 'yxMerchantsDetail:examine'],
        del: ['admin', 'yxMerchantsDetail:del'],
        modify:['admin','yxMerchantsDetail:modify']
      },
      rules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
        ],
        nickName: [
          { required: true, message: '请输入商户名称', trigger: 'blur' },
          { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
        ],
        merchantsContact: [
          { required: true, message: '请输入商户联系人', trigger: 'blur' },
          { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
        ],
        phone: [
          { required: true, trigger: 'blur', validator: validPhone }
        ],
        merchantsName: [
          { required: true, message: '请输入商户名称', trigger: 'blur' },
          { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
        ],
        address: [
          { required: true, message: '请输入商户地址', trigger: 'blur' },
        ],
        contacts: [
          { required: true, message: '请输入商户联系人', trigger: 'blur' },
          { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
        ],
        contactMobile: [
          { required: true, trigger: 'blur', validator: validPhone }
        ],
        mailbox: [
          { required: true, message: '必填项', trigger: 'blur' },
        ],
        merchantsType: [
          { required: true, message: '必填项', trigger: 'blur' },
        ],
        //以下是个人认证
        personIdCard: [
          { required: true, message: '必填项', trigger: 'blur' },
        ],
        personIdCardFace: [
          { required: true, message: '必填项', trigger: 'blur' },
        ],
        personIdCardBack: [
          { required: true, message: '必填项', trigger: 'blur' },
        ],
        bankNo: [
          { required: true, message: '必填项', trigger: 'blur' },
        ],
        openAccountProvince: [
          { required: true, message: '必填项', trigger: 'blur' },
        ],
        bankType: [
          { required: true, message: '必填项', trigger: 'blur' },
        ],
        openAccountName: [
          { required: true, message: '必填项', trigger: 'blur' },
        ],
        openAccountBank: [
          { required: true, message: '必填项', trigger: 'blur' },
        ],
        
        openAccountSubbranch: [
          { required: true, message: '必填项', trigger: 'blur' },
        ],
        //以下是企业
        companyProvince: [
          { required: true, message: '必填项', trigger: 'blur' },
        ],
        companyAddress: [
          { required: true, message: '必填项', trigger: 'blur' },
        ],
        companyName: [
          { required: true, message: '必填项', trigger: 'blur' },
        ],
        companyLegalPerson: [
          { required: true, message: '必填项', trigger: 'blur' },
        ],
        companyPhone: [
          { required: true, message: '必填项', trigger: 'blur' },
        ],
        //以下是企业与个体户
        businessCategory: [
          { required: true, message: '必填项', trigger: 'blur' },
        ],
        qualificationsType: [
          { required: true, message: '必填项', trigger: 'blur' },
        ],
        businessLicenseImg: [
          { required: true, message: '必填项', trigger: 'blur' },
        ],
        bankOpenProveImg: [
          { required: true, message: '必填项', trigger: 'blur' },
        ],
        legalIdCardFace: [
          { required: true, message: '必填项', trigger: 'blur' },
        ],
        legalIdCardBack: [
          { required: true, message: '必填项', trigger: 'blur' },
        ],
        storeImg: [
          { required: true, message: '必填项', trigger: 'blur' },
        ],
        licenceImg: [
          { required: true, message: '必填项', trigger: 'blur' },
        ],
        money: [
          { required: true, message: '必填项', trigger: 'blur' },
          { validator: amountValid, trigger: 'blur'},
        ],
        ptype: [
          { required: true, message: '必填项', trigger: 'blur' },
        ],
        checkbox: [
          { required: true, message: '必选项', trigger: 'change' },
          { validator:(r,v,c)=>{if(!v){ return c(new Error('必选项') )}else{c()}}, trigger: 'change' },
        ],
      },
      qualificationsType:[], //主体资质类型
      examineEdit:0,  //审核状态
      readStatus:0, //查看状态
      dialogVisible:this.crud.status.cu>0,
      formDisabled:false,
      // 个人认证
      perIdCard:[],//手持证件照
      perIdCardFace:[],//证件照人像面
      perIdCardBack:[],//证件照国徽面
      // 企业和个体户
      businessLicenseImg:[],//营业执照
      bankOpenProveImg:[],//银行开户证明
      legalIdCardFace:[],//法人身份证头像面
      legalIdCardBack:[],//法人身份证国徽面
      storeImg:[],//门店照及经营场所
      licenceImg:[],//医疗机构许可证
   }
  },
  watch: {
    // 个人认证
    perIdCard: function(val) {
      this.form.personIdCard = val.join(',')
      if (this.form.personIdCard != '') this.$refs.form.validateField('personIdCard')
    },
    perIdCardFace: function(val) {
      this.form.personIdCardFace = val.join(',')
      if (this.form.personIdCardFace != '') this.$refs.form.validateField('personIdCardFace')
    },
    perIdCardBack: function(val) {
      this.form.personIdCardBack = val.join(',')
      if (this.form.personIdCardBack != '') this.$refs.form.validateField('personIdCardBack')
    },
    // 企业和个体户
    businessLicenseImg: function(val) {
      this.form.businessLicenseImg = val.join(',')
      if (this.form.businessLicenseImg != '') this.$refs.form.validateField('businessLicenseImg')
    },
    bankOpenProveImg: function(val) {
      this.form.bankOpenProveImg = val.join(',')
      if (this.form.bankOpenProveImg != '') this.$refs.form.validateField('bankOpenProveImg')
    },
    legalIdCardFace: function(val) {
      this.form.legalIdCardFace = val.join(',')
      if (this.form.legalIdCardFace != '') this.$refs.form.validateField('legalIdCardFace')
    },
    legalIdCardBack: function(val) {
      this.form.legalIdCardBack = val.join(',')
      if (this.form.legalIdCardBack != '') this.$refs.form.validateField('legalIdCardBack')
    },
    storeImg: function(val) {
      this.form.storeImg = val.join(',')
      if (this.form.storeImg != '') this.$refs.form.validateField('storeImg')
    },
    licenceImg: function(val) {
      this.form.licenceImg = val.join(',')
      if (this.form.licenceImg != '') this.$refs.form.validateField('licenceImg')
    },
    "form.bankType": function(newValue, old) {
      this.bankType = newValue == 1
    }
  },
  methods: {
    // 联行号， 对公需要
    bankCodeRuls(){
      return [
        { required: this.bankType, message: "联行号必填", trigger: 'blur' },
      ]
    },
    
    // 获取数据前设置好接口地址
    [CRUD.HOOK.beforeRefresh]() {
      return true
    }, // 新增与编辑前做的操作
    [CRUD.HOOK.afterToCU](crud, form) {
      // 个人认证
      if (form.personIdCard) {
        this.perIdCard = form.personIdCard.split(',')
      }else{
        this.perIdCard = []
      }
      if (form.personIdCardFace) {
        this.perIdCardFace = form.personIdCardFace.split(',')
      }else{
        this.perIdCardFace = []
      }
      if (form.personIdCardBack) {
        this.perIdCardBack = form.personIdCardBack.split(',')
      }else{
        this.perIdCardBack = []
      }
      // 企业和个体户
      if (form.businessLicenseImg) {
        this.businessLicenseImg = form.businessLicenseImg.split(',')
      }else{
        this.businessLicenseImg = []
      }
      if (form.bankOpenProveImg) {
        this.bankOpenProveImg = form.bankOpenProveImg.split(',')
      }else{
        this.bankOpenProveImg = []
      }
      if (form.legalIdCardFace) {
        this.legalIdCardFace = form.legalIdCardFace.split(',')
      }else{
        this.legalIdCardFace = []
      }
      if (form.legalIdCardBack) {
        this.legalIdCardBack = form.legalIdCardBack.split(',')
      }else{
        this.legalIdCardBack = []
      }
      if (form.storeImg) {
        this.storeImg = form.storeImg.split(',')
      }else{
        this.storeImg = []
      }
      if (form.licenceImg) {
        this.licenceImg = form.licenceImg.split(',')
      }else{
        this.licenceImg = []
      }
    },
    //获取主体资质类型列表
    businessCategoryChange(v){
      this.form.qualificationsType=""
      getDictDetail(v).then(res=>{
          this.qualificationsType=res.content;
      })
    },

    //显示审核弹出框
    examineOpt(data){
      this.examineEdit=1;
      this.dialogVisible=Boolean(this.examineEdit)

      this.crud.resetForm(JSON.parse(JSON.stringify(data)))
      /*图片默认值赋值*/
      // 个人认证
      if (this.form.personIdCard) {
        this.perIdCard = this.form.personIdCard.split(',')
      }else{
        this.perIdCard = []
      }
      if (this.form.personIdCardFace) {
        this.perIdCardFace = this.form.personIdCardFace.split(',')
      }else{
        this.perIdCardFace = []
      }
      if (this.form.personIdCardBack) {
        this.perIdCardBack = this.form.personIdCardBack.split(',')
      }else{
        this.perIdCardBack = []
      }
      // 企业和个体户
      if (this.form.businessLicenseImg) {
        this.businessLicenseImg = this.form.businessLicenseImg.split(',')
      }else{
        this.businessLicenseImg = []
      }
      if (this.form.bankOpenProveImg) {
        this.bankOpenProveImg = this.form.bankOpenProveImg.split(',')
      }else{
        this.bankOpenProveImg = []
      }
      if (this.form.legalIdCardFace) {
        this.legalIdCardFace = this.form.legalIdCardFace.split(',')
      }else{
        this.legalIdCardFace = []
      }
      if (this.form.legalIdCardBack) {
        this.legalIdCardBack = this.form.legalIdCardBack.split(',')
      }else{
        this.legalIdCardBack = []
      }
      if (this.form.storeImg) {
        this.storeImg = this.form.storeImg.split(',')
      }else{
        this.storeImg = []
      }
      if (this.form.licenceImg) {
        this.licenceImg = this.form.licenceImg.split(',')
      }else{
        this.licenceImg = []
      }
      this.formDisabled=true
    },
    //审核通过 /** 审批状态examineStatus：0->待审核,1->通过,2->驳回 */
    examineEditSubmitCU(){
      this.examineEdit=2
      examineSubmit({
        examineStatus:1,
        examineRemark:this.form.examineRemark,
        id:this.form.id
      }).then(res=>{
        if(res){
          Notification.success({
            title: '审核成功'
          })
          this.examineEdit=0;
          this.crud.toQuery()
        }else{
          this.examineEdit=1;
          Notification.error({
            title: "审核失败，请重新尝试"
          })
        }
        this.formDisabled=false
        this.dialogVisible=Boolean(this.crud.status.cu);
      })
    },
    //审核驳回 /** 审批状态examineStatus：0->待审核,1->通过,2->驳回 */
    examineEditCancelCU(){
      this.examineEdit=2;
      examineSubmit({
        examineStatus:2,
        examineRemark:this.form.examineRemark,
        id:this.form.id
      }).then(res=>{
        if(res){
          Notification.success({
            title: '驳回已完成'
          })
          this.examineEdit=0;
          this.crud.toQuery()
        }else{
          this.examineEdit=1;
          Notification.error({
            title: "驳回失败，请重新尝试"
          })
        }
        this.formDisabled=false
        this.dialogVisible=Boolean(this.crud.status.cu);
      })
    },
    //新增、编辑、审核弹出框关闭
    dialogBeforeCancel(done){
        if(this.crud.status.cu>0){
          this.crud.cancelCU()
        }
        if(this.examineEdit){
          this.examineEdit=0;
          this.dialogVisible=Boolean(this.crud.status.cu);
          this.formDisabled=false
        }
        if(this.readStatus){
          this.readStatus=0
          this.formDisabled=false
          this.dialogVisible=Boolean(this.crud.status.cu);
        }
    },
    //启禁用商户状态
    updateStatus(uid,status) {
      let per=checkPermission(['admin','yxMerchantsDetail:switch'])
      if(!per){
        return;
      }
      this.$confirm(`确定进行[${status ? '启用' : '禁用'}]操作?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          update({uid}).then(({ data }) => {
            this.$message({
              message: '操作成功',
              type: 'success',
              duration: 1000,
              onClose: () => {
                this.crud.toQuery()
              }
            })
          })
        })
        .catch(() => { })
    },
    toRead(data){
      this.examineOpt(data);
      this.examineEdit=0;
      this.readStatus=1;
    },
    closeRead(){
      this.readStatus=0
      this.formDisabled=false
      this.dialogVisible=Boolean(this.crud.status.cu);
    },
    withdrawEdit(btn,formData,uid){
      this.$refs.formWithdraw.validate(function(ret,obj){
        if(ret){          
          withdrawEdit(Object.assign(formData,{uid})).then(res=>{
            console.log('xxxxxx')
            console.log(res)
            if(res){
              Notification.success({
              title: '提交成功'
              })
            }else{
              Notification.error({
              title: '提交失败'
              })
            }
          })
        }else{          
          Notification.error({
            title: '请查看必填项是否输入正确'
            })
        }
      })

    }
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
