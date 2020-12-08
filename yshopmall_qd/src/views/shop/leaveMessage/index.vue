<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <!--如果想在工具栏加入更多按钮，可以使用插槽方式， slot = 'left' or 'right'-->
      <el-input v-model="query.userName" clearable size="small" placeholder="请输入联系人"
                style="width: 200px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
      <el-input v-model="query.userPhone" clearable size="small" placeholder="请输入联系电话"
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
<!--      <crudOperation :permission="permission" />-->
      <!--表单组件-->
      <el-dialog :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="500px">
        <el-form ref="form" :model="form" :rules="rules" size="small" label-width="80px">
          <el-form-item label="" v-show="false">
            <el-input v-model="form.id" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="留言类型">
            {{ transferLabel(form.messageType,messageTypeList) }}
          </el-form-item>
          <el-form-item label="商品名称">
            {{form.goodsName}}
<!--            <el-input v-model="form.merId" style="width: 370px;" />-->
          </el-form-item>
          <el-form-item label="订单号">
            {{form.orderId}}
          </el-form-item>
          <el-form-item label="联系人">
            {{form.userName}}
          </el-form-item>
          <el-form-item label="联系电话">
            {{form.userPhone}}
          </el-form-item>
          <el-form-item label="留言信息">
            {{form.message}}
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-radio-group v-if="editStatus" v-model="form.status">
              <el-radio v-for="item in statusList" :label="item.value">{{item.label}}</el-radio>
            </el-radio-group>
            <span  v-else>{{ transferLabel(form.status,statusList) }}</span>
          </el-form-item>
          <el-form-item label="备注" prop="remark">
            <el-input v-if="editStatus" type="textarea"
                      :rows="4" v-model="form.remark" style="width: 370px;" maxlength="200"/>
            <span v-else>{{form.remark}}</span>
          </el-form-item>
          <el-form-item label="处理人">
            {{form.updateNickname}}
          </el-form-item>
          <el-form-item label="处理时间" >
            {{form.takeTimeStr}}
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="text" @click="crud.cancelCU">关闭</el-button>
          <el-button v-if="editStatus" :loading="crud.cu === 2" type="primary" @click="crud.submitCU">确认</el-button>
        </div>
      </el-dialog>
      <!--表格渲染-->
      <el-table ref="table" v-loading="crud.loading" :data="crud.data" size="small" style="width: 100%;" @selection-change="crud.selectionChangeHandler">
<!--        <el-table-column type="selection" width="55" />-->
        <el-table-column v-if="columns.visible('id')" prop="id" label="id" />
        <el-table-column v-if="columns.visible('userName')" prop="userName" label="联系人" />
        <el-table-column v-if="columns.visible('userPhone')" prop="userPhone" label="电话" />
        <el-table-column v-if="columns.visible('status')" prop="status" label="状态" >
          <template slot-scope="scope">
            <span>
              <el-tag v-if="scope.row.status === 0" :type="'success'">{{ transferLabel(scope.row.status,statusList) }}</el-tag>
              <el-tag v-else-if="scope.row.status === 1" :type=" '' ">{{ transferLabel(scope.row.status,statusList) }}</el-tag>
              <el-tag v-else :type=" 'info' ">{{ transferLabel(scope.row.status,statusList) }}</el-tag>
            </span>
          </template>
        </el-table-column>

        <el-table-column v-if="columns.visible('updateNickname')" prop="updateNickname" label="处理人" />
        <el-table-column v-if="columns.visible('takeTimeStr')" prop="takeTimeStr" label="处理时间">
        </el-table-column>
        <el-table-column v-permission="['admin','yxLeaveMessage:edit','yxLeaveMessage:del']" label="操作" width="150px" align="center">
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
import crudYxLeaveMessage from '@/api/yxLeaveMessage'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import udOperation from '@crud/UD.operation'
import pagination from '@crud/Pagination'
import MaterialList from "@/components/material";

// crud交由presenter持有
const defaultCrud = CRUD({ title: '留言管理', url: 'api/yxLeaveMessage', sort: 'id,desc',
  crudMethod: { ...crudYxLeaveMessage },
  query:{
    userName:'',
    userPhone:'',
    status:''
  }
})
const defaultForm = { id: null, orderId: null, merId: null, userName: null, userPhone: null,
  message: null, status: 0, messageType: null, remark: null, delFlag: null, createUserId: null,
  updateUserId: null, createTime: null, updateTime: null }
export default {
  name: 'YxLeaveMessage',
  components: { pagination, crudOperation, rrOperation, udOperation ,MaterialList},
  mixins: [presenter(defaultCrud), header(), form(defaultForm), crud()],
  data() {
    return {

      permission: {
        add: ['admin', 'yxLeaveMessage:add'],
        edit: ['admin', 'yxLeaveMessage:edit'],
        del: ['admin', 'yxLeaveMessage:del']
      },
      rules: {
        orderId: [
          { required: true, message: '订单号不能为空', trigger: 'blur' }
        ],
        userName: [
          { required: true, message: '联系人不能为空', trigger: 'blur' }
        ],
        userPhone: [
          { required: true, message: '电话不能为空', trigger: 'blur' }
        ],
        message: [
          { required: true, message: '留言信息不能为空', trigger: 'blur' }
        ],
        status: [
          { required: true, message: '必选项', trigger: 'blur' }
        ],
        messageType: [
          { required: true, message: '留言类型', trigger: 'blur' }
        ],
        delFlag: [
          { required: true, message: '是否删除（0：未删除，1：已删除）不能为空', trigger: 'blur' }
        ],
        remark: [
          { required: true, message: '必填项', trigger: 'blur' }
        ],
        updateTime: [
          { required: true, message: '更新时间不能为空', trigger: 'blur' }
        ]
      },
      messageTypeList:[
        {value:0,label:'商品'},
        {value:1,label:'商城订单'},
        {value:2,label:'本地生活订单'},
        {value:3,label:'商户'},
        {value:4,label:'平台'},
        {value:5,label:'平台留言'}
      ],
      statusList:[
        {value:0,label:'待处理'},
        {value:1,label:'已处理'},
        {value:2,label:'不予处理'}
      ],
      editStatus:false
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
    }, // 新增与编辑前做的操作
    [CRUD.HOOK.afterToCU](crud, form) {
      this.editStatus=!Boolean(form.status)
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
