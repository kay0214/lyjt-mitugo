 <!--提现审批记录-->
<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <!--如果想在工具栏加入更多按钮，可以使用插槽方式， slot = 'left' or 'right'-->
      <div v-if="crud.props.searchToggle">
        <!-- 搜索 -->
        <el-input v-model="query.username" clearable size="small" placeholder="输入姓名搜索" style="width: 200px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
        <el-button class="filter-item" size="mini" type="success" icon="el-icon-search" @click="crud.toQuery">搜索</el-button>
      </div>
      <!-- <crudOperation :permission="permission" /> -->
      <!--表单组件-->
      <el-dialog :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="500px">
        <el-form ref="form" :model="form" :rules="rules" size="small" label-width="80px">
          <el-form-item label="姓名">
            <el-input v-model="form.merchantsName" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="提现金额">
            <el-input v-model="form.contacts" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="微信号">
            <el-input v-model="form.contactMobile" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="提现方式">
            <el-input v-model="form.contacts" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="添加时间">
            <el-input v-model="form.contactMobile" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="审核状态：0->待审核,1->通过,2->驳回">
            <el-input v-model="form.status" style="width: 370px;" />
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
        <el-table-column v-if="columns.visible('username')" prop="username" label="姓名" />
        <el-table-column v-if="columns.visible('wechat')" prop="wechat" label="用户名" />
        <el-table-column v-if="columns.visible('typeId')" prop="typeId" label="提现申请ID" />
        <el-table-column v-if="columns.visible('extractPrice')" prop="extractPrice" label="提现金额" />
        <el-table-column v-if="columns.visible('extractType')" prop="extractType" label="提现方式" />
        <el-table-column prop="createTime" label="添加时间">
          <template slot-scope="scope">
            <span>{{ formatTime(scope.row.createTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="审批状态" align="center">
          <template slot-scope="scope">
            <div v-if="scope.row.status==1">
            通过
          </div>
          <div v-else>
            驳回<br>
            驳回原因：{{ scope.row.failMsg }}
            <br>
            未通过时间：{{ scope.row.failTime?formatTimeTwo(scope.row.failTime):'' }}
          </div>
          </template>
        </el-table-column>
        <!-- <el-table-column v-if="columns.visible('mark')" prop="mark" label="审核说明" /> -->
      </el-table>
      <!--分页组件-->
      <pagination />
    </div>
  </div>
</template>

<script>
import yxWithdrawExamineLog from '@/api/yxWithdrawExamineLog'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import udOperation from '@crud/UD.operation'
import pagination from '@crud/Pagination'
import MaterialList from "@/components/material";
import { formatTime,formatTimeTwo } from '@/utils/index'

// crud交由presenter持有
const defaultCrud = CRUD({ title: '提现审批记录', query: { type: 2 }, url: 'api/yxExamineLog/extract', sort: 'id,desc', crudMethod: { ...yxWithdrawExamineLog },optShow: {
  add: false,
    edit: false,
    del: false,
    download: false
}})
const defaultForm = { id: null, type: null, typeId: null, status: null, remark: null, delFlag: null, createUserId: null, updateUserId: null, createTime: null, updateTime: null, uid: null, username: null }
export default {
  name: 'YxExamineLog',
  components: { pagination, crudOperation, rrOperation, udOperation ,MaterialList},
  mixins: [presenter(defaultCrud), header(), form(defaultForm), crud()],
  data() {
    return {

      permission: {
        add: ['admin', 'yxExamineLog:add'],
        edit: ['admin', 'yxExamineLog:edit'],
        del: ['admin', 'yxExamineLog:del']
      },
      rules: {
      }    }
  },
  watch: {
  },
  methods: {
    formatTime,
    formatTimeTwo,
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
