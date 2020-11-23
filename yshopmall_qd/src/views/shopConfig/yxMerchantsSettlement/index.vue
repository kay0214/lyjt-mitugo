<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <!--如果想在工具栏加入更多按钮，可以使用插槽方式， slot = 'left' or 'right'-->
      <el-input v-model="query.companyName" clearable size="small" placeholder="请输入公司名"
                style="width: 200px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
      <el-input v-model="query.contactsName" clearable size="small" placeholder="请输入联系人"
                style="width: 200px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
      <el-input v-model="query.phone" clearable size="small" placeholder="请输入联系电话"
                style="width: 200px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
      <el-select v-model="query.status" clearable placeholder="请选择状态"
                 style="width: 200px;" class="filter-item">
        <el-option
          v-for="item in statusList"
          :key="item.value"
          :label="item.label"
          :value="item.value">
        </el-option>
      </el-select>
      <el-button class="filter-item" size="mini" type="success" icon="el-icon-search" @click="crud.toQuery">搜索</el-button>
      <crudOperation :permission="permission" />
      <!--表单组件-->
      <el-dialog :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="500px">
        <el-form ref="form" :model="form" :rules="rules" size="small" label-width="80px">
          <el-form-item label="公司名">
            {{form.companyName}}
          </el-form-item>
          <el-form-item label="联系人">
            {{form.contactsName}}
          </el-form-item>
          <el-form-item label="联系电话">
            {{form.phone}}
          </el-form-item>
          <el-form-item label="联系地址">
            {{form.address}}
          </el-form-item>
          <el-form-item label="说明">
            {{form.explain}}
          </el-form-item>
          <el-form-item label="备注" prop="remark">
            <div v-if="form.status">
              {{form.remark}}
            </div>
            <el-input v-else v-model.trim="form.remark" type="textarea"
                      :row='10' style="width: 370px;"
                      show-word-limit  maxlength="200"/>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer" v-if="!form.status">
          <el-button :loading="crud.cu === 2" type="primary" @click="()=>{this.setStatus=1;crud.submitCU()}">有意向</el-button>
          <el-button :loading="crud.cu === 2" type="primary" @click="()=>{this.setStatus=2;crud.submitCU()}">已拒绝</el-button>
        </div>
        <div slot="footer" class="dialog-footer" v-else>
          <el-button type="primary" @click="crud.cancelCU">关闭</el-button>
        </div>
      </el-dialog>
      <!--表格渲染-->
      <el-table ref="table" v-loading="crud.loading" :data="crud.data" size="small" style="width: 100%;" @selection-change="crud.selectionChangeHandler">
        <el-table-column type="selection" width="55" />
        <el-table-column v-if="columns.visible('companyName')" prop="companyName" label="公司名" />
        <el-table-column v-if="columns.visible('contactsName')" prop="contactsName" label="联系人" />
        <el-table-column v-if="columns.visible('phone')" prop="phone" label="联系电话" />
        <el-table-column v-if="columns.visible('status')" prop="status" label="状态" >
          <template slot-scope="scope">
            <span @click="update(scope.row)">
              {{ transferLabel(scope.row.status,statusList) }}</span>
          </template>
        </el-table-column>
        <el-table-column v-permission="['admin','yxMerchantsSettlement:edit','yxMerchantsSettlement:del']" label="操作" width="150px" align="center">
          <template slot-scope="scope">
            <el-button v-if="!scope.row.status" v-permission="permission.edit" size="mini" type="primary"
                       icon="el-icon-edit" @click="crud.toEdit(scope.row)" />
            <el-button v-else v-permission="permission.edit" size="mini" type="primary"
                       icon="el-icon-view" @click="crud.toEdit(scope.row)" plain/>
            <udOperation
              :data="scope.row"
              :permission="permissionDel"
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
import crudYxMerchantsSettlement from '@/api/yxMerchantsSettlement'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import udOperation from '@crud/UD.operation'
import pagination from '@crud/Pagination'
import MaterialList from "@/components/material";

// crud交由presenter持有
const defaultCrud = CRUD({ title: '商家入驻', url: 'api/yxMerchantsSettlement', sort: 'id,desc',
  optShow: {
    add: false,
    edit: false,
    del: false,
    download: true
  },crudMethod: { ...crudYxMerchantsSettlement }})
const defaultForm = { id: null, companyName: null, contactsName: null, phone: null, address: null, explain: null,
  remark: null, delFlag: null, createUserId: null, updateUserId: null, createTime: null, updateTime: null }
export default {
  name: 'YxMerchantsSettlement',
  components: { pagination, crudOperation, rrOperation, udOperation ,MaterialList},
  mixins: [presenter(defaultCrud), header(), form(defaultForm), crud()],
  data() {
    return {
      statusList:[
        {value:0,label:'待联系'},
        {value:1,label:'有意向'},
        {value:2,label:'已拒绝'}
      ],
      permission: {
        add: ['admin', 'yxMerchantsSettlement:add'],
        edit: ['admin', 'yxMerchantsSettlement:edit'],
        del: ['admin', 'yxMerchantsSettlement:del']
      },
      permissionDel:{
        edit: [''],
        del: ['admin', 'yxMerchantsSettlement:del']
      },
      rules: {
        companyName: [
          { required: true, message: '公司名不能为空', trigger: 'blur' }
        ],
        contactsName: [
          { required: true, message: '联系人不能为空', trigger: 'blur' }
        ],
        phone: [
          { required: true, message: '联系电话不能为空', trigger: 'blur' }
        ],
        address: [
          { required: true, message: '联系地址不能为空', trigger: 'blur' }
        ],
        explain: [
          { required: true, message: '说明不能为空', trigger: 'blur' }
        ],
        remark: [
          { required: true, message: '备注不能为空', trigger: 'blur' }
        ],
        status: [
          { required: true, message: '状态：0：待联系，1：有意向，2：已拒绝不能为空', trigger: 'blur' }
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
      },
      setStatus:0
    }
  },
  watch: {
  },
  computed:{
    transferLabel:function(){
      return function(value,list){
        if(list.length){
          let i= list.filter(function(item){
            return new RegExp(item.value, 'i').test(value)
          })
          if(i.length){
            return i[0].label
          }else{
            return ""
          }
        }else{
          return ""
        }
      }
    }
  },
  methods: {
    // 获取数据前设置好接口地址
    [CRUD.HOOK.beforeRefresh]() {
      return true
    }, // "新建/编辑" 验证 - 之后
    [CRUD.HOOK.afterValidateCU](crud) {
      crud.form.status=this.setStatus
      console.log(crud.form)
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
