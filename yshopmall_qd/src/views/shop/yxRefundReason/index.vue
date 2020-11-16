<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <!--如果想在工具栏加入更多按钮，可以使用插槽方式， slot = 'left' or 'right'-->
      <crudOperation :permission="permission" />
      <!--表单组件-->
      <el-dialog :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="500px">
        <el-form ref="form" :model="form" :rules="rules" size="small" label-width="80px">
          <el-form-item label="类型" prop="reasonType">
            <el-radio-group v-model="form.reasonType">
              <el-radio v-for="item in statusList" :label="item.value">{{item.label}}</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="理由" prop="reason">
            <el-input v-model="form.reason" style="width: 370px;" />
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
        <el-table-column v-if="columns.visible('reasonType')" prop="reasonType" label="类型" >
          <template slot-scope="scope">
            <span @click="update(scope.row)">{{ transferLabel(scope.row.reasonType,statusList) }}</span>
          </template>
        </el-table-column>
        <el-table-column v-if="columns.visible('reason')" prop="reason" label="理由" />
        <el-table-column v-permission="['admin','yxRefundReason:edit','yxRefundReason:del']" label="操作" width="150px" align="center">
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
import crudYxRefundReason from '@/api/yxRefundReason'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import udOperation from '@crud/UD.operation'
import pagination from '@crud/Pagination'
import MaterialList from "@/components/material";

// crud交由presenter持有
const defaultCrud = CRUD({ title: '退款理由', url: 'api/yxRefundReason', sort: 'id,desc', crudMethod: { ...crudYxRefundReason },
  optShow:{
    add: true,
    edit: false,
    del: false,
    download: false
  }})
const defaultForm = { id: null, reason: null, reasonType: 0, delFlag: null, createUserId: null,
  updateUserId: null, createTime: null, updateTime: null }
export default {
  name: 'YxRefundReason',
  components: { pagination, crudOperation, rrOperation, udOperation ,MaterialList},
  mixins: [presenter(defaultCrud), header(), form(defaultForm), crud()],
  data() {
    return {

      permission: {
        add: ['admin', 'yxRefundReason:add'],
        edit: ['admin', 'yxRefundReason:edit'],
        del: ['admin', 'yxRefundReason:del']
      },
      rules: {
        reason: [
          { required: true, message: '理由不能为空', trigger: 'blur' }
        ],
        reasonType: [
          { required: true, message: '类型不能为空', trigger: 'change' }
        ]
      } ,
      statusList:[
        {value:0,label:'本地生活'},
        {value:1,label:'商城'}
      ]
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
