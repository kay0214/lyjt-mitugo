<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <!--如果想在工具栏加入更多按钮，可以使用插槽方式， slot = 'left' or 'right'-->
      <crudOperation :permission="permission" />
      <!--表单组件-->
      <el-dialog :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="500px">
        <el-form ref="form" :model="form" :rules="rules" size="small" label-width="80px">
          <el-form-item label="问题" prop="question">
            <el-input v-model="form.question" style="width: 370px;" maxlength="20"/>
          </el-form-item>
          <el-form-item label="回答" prop="answer">
            <el-input type="textarea"
                      :rows="8" v-model="form.answer" style="width: 370px;" maxlength="1000"/>
          </el-form-item>
          <el-form-item label="排序">
            <el-input-number v-model="form.sort" style="width: 370px;" :precision="0"/>
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-radio-group v-model="form.status">
              <el-radio v-for="item in statusList" :label="item.value">{{item.label}}</el-radio>
            </el-radio-group>
          </el-form-item>
<!--          <el-form-item label="用户角色" prop="userRole">-->
<!--            <el-input v-model="form.userRole" style="width: 370px;" />-->
<!--          </el-form-item>-->
<!--          <el-form-item label="创建人">-->
<!--            <el-input v-model="form.createUserId" style="width: 370px;" />-->
<!--          </el-form-item>-->
<!--          <el-form-item label="修改人">-->
<!--            <el-input v-model="form.updateUserId" style="width: 370px;" />-->
<!--          </el-form-item>-->
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="text" @click="crud.cancelCU">取消</el-button>
          <el-button :loading="crud.cu === 2" type="primary" @click="crud.submitCU">确认</el-button>
        </div>
      </el-dialog>
      <!--表格渲染-->
      <el-table ref="table" v-loading="crud.loading" :data="crud.data" size="small" style="width: 100%;"
                @selection-change="crud.selectionChangeHandler">
        <el-table-column type="selection" width="55" />
        <el-table-column v-if="columns.visible('id')" prop="id" label="id" />
        <el-table-column v-if="columns.visible('question')" prop="question" label="问题" />
<!--        <el-table-column v-if="columns.visible('sort')" prop="sort" label="排序" />        -->
        <el-table-column v-if="columns.visible('answer')" prop="answer" label="回答" />
        <el-table-column v-if="columns.visible('status')" prop="status" label="状态" >
          <template slot-scope="scope">
            <span @click="update(scope.row)">{{ transferLabel(scope.row.status,statusList) }}</span>
          </template>
        </el-table-column>
        <el-table-column v-permission="['admin','yxCustomerService:edit','yxCustomerService:del']"
                         label="操作" width="150px" align="center">
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
import crudYxCustomerService,{updateStatus} from '@/api/yxCustomerService'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import udOperation from '@crud/UD.operation'
import pagination from '@crud/Pagination'
import MaterialList from "@/components/material";
import checkPermission from '@/utils/permission'

// crud交由presenter持有
const defaultCrud = CRUD({ title: '机器人客服', url: 'api/yxCustomerService', sort: 'id,desc',
  crudMethod: { ...crudYxCustomerService },
  optShow:{
    add: true,
    edit: false,
    del: false,
    download: false
  }
})
const defaultForm = { id: null, question: null, sort: null, status: null, userRole: null,
  delFlag: null, createUserId: null, updateUserId: null, createTime: null, updateTime: null,
  answer: null }
export default {
  name: 'YxCustomerService',
  components: { pagination, crudOperation, rrOperation, udOperation ,MaterialList},
  mixins: [presenter(defaultCrud), header(), form(defaultForm), crud()],
  data() {
    return {
      permission: {
        add: ['admin', 'yxCustomerService:add'],
        edit: ['admin', 'yxCustomerService:edit'],
        del: ['admin', 'yxCustomerService:del'],
        switch: ['admin', 'yxCustomerService:switch'],
      },
      rules: {
        question: [
          { required: true, message: '问题不能为空', trigger: 'blur' }
        ],
        status: [
          { required: true, message: '状态不能为空', trigger: 'blur' }
        ],
        userRole: [
          { required: true, message: '用户角色不能为空', trigger: 'blur' }
        ],
        answer: [
          { required: true, message: '回答不能为空', trigger: 'blur' }
        ]
      },
      roleList:[
        {value:0,label:'平台运营'},
        {value:1,label:'合伙人'},
        {value:2,label:'商户'}
      ],
      statusList:[
        {value:0,label:'启用'},
        {value:1,label:'禁用'}
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
    update(data){
      console.log(data)
      let ret=checkPermission(this.permission.switch)
      console.log(ret)
      if(!ret){
        return ret
      }
      this.$confirm(`确定进行[${data.status ? '启用' : '禁用'}]操作?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          updateStatus( {id:data.id, status: data.status?0:1 }).then(() => {
            this.crud.refresh()
            this.$message({
              message: '操作成功',
              type: 'success',
              duration: 1000
            })
          })
        })
        .catch(() => { })

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
