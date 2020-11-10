<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <!--如果想在工具栏加入更多按钮，可以使用插槽方式， slot = 'left' or 'right'-->
      <el-input v-model="query.nickName" clearable size="small" placeholder="请输入姓名" style="width: 200px;"
                class="filter-item" @keyup.enter.native="crud.toQuery" />
      <el-input v-model="query.username" clearable size="small" placeholder="请输入用户名" style="width: 200px;"
                class="filter-item" @keyup.enter.native="crud.toQuery" />
      <el-date-picker
        class="filter-item"
        v-model="query.daterange"
        type="daterange"
        range-separator="至"
        start-placeholder="开始日期"
        end-placeholder="结束日期"
        format="yyyy 年 MM 月 dd 日"
        value-format="yyyy-MM-dd"
      @change="(val)=>{
        if(val) {
          query.startDate =val[0];query.endDate =val[1]
        }else{
          query.startDate='';query.endDate=''
        }
      }"
      >
      </el-date-picker>
      <el-button class="filter-item" size="mini" type="success" icon="el-icon-search" @click="crud.toQuery">搜索</el-button>
      <crudOperation :permission="permission" />
      <!--表单组件-->
      <el-dialog :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="500px">
        <el-form ref="form" :model="form" :rules="rules" size="small" label-width="80px">
          <el-form-item label="姓名" prop="nickName">
            <el-input v-model="form.nickName" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="用户名" prop="username">
            <el-input v-model="form.username" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="联系电话" prop="userPhone">
            <el-input v-model="form.userPhone" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="体温">
            <el-input v-model="form.temperature" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="签到时间" prop="createTime">
            <el-input v-model="form.createTime" style="width: 370px;" />
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
<!--        <el-table-column v-if="columns.visible('uid')" prop="uid" label="用户ID" />-->
        <el-table-column v-if="columns.visible('nickName')" prop="nickName" label="姓名" />
        <el-table-column v-if="columns.visible('username')" prop="username" label="用户名" />
        <el-table-column v-if="columns.visible('userPhone')" prop="userPhone" label="联系电话" />
        <el-table-column v-if="columns.visible('temperature')" prop="temperature" label="体温" />
        <el-table-column v-if="columns.visible('createTime')" prop="createTime" label="签到时间">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.createTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column v-permission="['admin','yxCrewSign:edit','yxCrewSign:del']" label="操作" width="150px" align="center">
          <template slot-scope="scope">
            <el-popover v-model="pop" v-permission="permission.del" placement="top" width="180" trigger="manual" @show="onPopoverShow" @hide="onPopoverHide">
              <p>确定删除本条数据吗？</p>
              <div style="text-align: right; margin: 0">
                <el-button size="mini" type="text" @click="doCancel(scope.row)">取消</el-button>
                <el-button :loading="crud.dataStatus[scope.row.id].delete === 2" type="primary" size="mini" @click="crud.doDelete(scope.row)">确定</el-button>
              </div>
              <el-button slot="reference" type="danger" icon="el-icon-delete" size="mini" @click="toDelete" />
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
import crudYxCrewSign from '@/api/yxCrewSign'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import udOperation from '@crud/UD.operation'
import pagination from '@crud/Pagination'
import MaterialList from "@/components/material";

// crud交由presenter持有
const defaultCrud = CRUD({ title: '船员签到', url: 'api/yxCrewSign', sort: 'id,desc',
  optShow:{
    add: false,
    edit: false,
    del: false,
    download: true
  },
  crudMethod: { ...crudYxCrewSign }
  , query: {
    nickName:'',
    username:'',
    startDate:'',
    endDate:''
  }
})
const defaultForm = { id: null, uid: null, username: null, userPhone: null, temperature: null, delFlag: null, createUserId: null, updateUserId: null, createTime: null, updateTime: null }
export default {
  name: 'YxCrewSign',
  components: { pagination, crudOperation, rrOperation, udOperation ,MaterialList},
  mixins: [presenter(defaultCrud), header(), form(defaultForm), crud()],
  data() {
    return {
      pop: false,
      permission: {
        add: ['admin', 'yxCrewSign:add'],
        edit: ['admin', 'yxCrewSign:edit'],
        del: ['admin', 'yxCrewSign:del']
      },
      rules: {
        uid: [
          { required: true, message: '用户ID不能为空', trigger: 'blur' }
        ],
        username: [
          { required: true, message: '用户名不能为空', trigger: 'blur' }
        ],
        userPhone: [
          { required: true, message: '联系电话不能为空', trigger: 'blur' }
        ],
        delFlag: [
          { required: true, message: '是否删除（0：未删除，1：已删除）不能为空', trigger: 'blur' }
        ],
        createTime: [
          { required: true, message: '创建时间（签到时间）不能为空', trigger: 'blur' }
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
    doCancel(data) {
      this.pop = false
      this.crud.cancelDelete(data)
    },
    toDelete() {
      this.pop = true
    },
    [CRUD.HOOK.afterDelete](crud, data) {
      if (data === this.data) {
        this.pop = false
      }
    },
    onPopoverShow() {
      setTimeout(() => {
        document.addEventListener('click', this.handleDocumentClick)
      }, 0)
    },
    onPopoverHide() {
      document.removeEventListener('click', this.handleDocumentClick)
    },
    handleDocumentClick(event) {
      this.pop = false
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
