<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <!--如果想在工具栏加入更多按钮，可以使用插槽方式， slot = 'left' or 'right'-->
      <el-input v-model.trim="query.merchantsName" clearable size="small" placeholder="输入商户名称" style="width: 200px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
      <el-input v-model.trim="query.contactMobile" clearable size="small" placeholder="输入商户联系人电话" style="width: 200px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
      <el-select v-model="query.status" clearable placeholder="审批状态"
                 style="width: 200px;" class="filter-item">
        <el-option
          v-for="item in statusList"
          :key="item.value"
          :label="item.label"
          :value="item.value">
        </el-option>
      </el-select>
      <el-button class="filter-item" size="mini" type="success" icon="el-icon-search" @click="crud.toQuery">搜索</el-button>
      <el-button
        type="danger"
        class="filter-item"
        size="mini"
        icon="el-icon-refresh"
        @click="crud.toQuery"
      >刷新</el-button>
      <!-- <crudOperation :permission="permission" /> -->
      <!--表单组件-->
      <el-dialog :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="500px">
        <el-form ref="form" :model="form" :rules="rules" size="small" label-width="80px">
          <el-form-item label="商户id">
            <el-input v-model="form.uid" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="商户名称">
            <el-input v-model="form.merchantsName" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="商户联系人">
            <el-input v-model="form.contacts" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="联系人电话">
            <el-input v-model="form.contactMobile" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="审批状态：0->待审核,1->通过,2->驳回">
            <el-input v-model="form.status" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="审核说明">
            <el-input v-model="form.remark" style="width: 370px;" />
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
        <el-table-column v-if="columns.visible('uid')" prop="uid" label="商户id" />
        <el-table-column v-if="columns.visible('merchantsName')" prop="merchantsName" label="商户名称" />
        <el-table-column v-if="columns.visible('username')" prop="username" label="商户联系人" />
        <el-table-column v-if="columns.visible('contactMobile')" prop="contactMobile" label="联系人电话" />
        <el-table-column label="审批状态" align="center">
          <template slot-scope="scope">
            <div>
              <el-tag v-if="scope.row.status == 1" type="success">通过</el-tag>
              <el-tag v-else-if="scope.row.status == 2" type="danger">驳回</el-tag>
              <el-tag v-else type="info">待审核</el-tag>
            </div>
          </template>
        </el-table-column>
        <el-table-column v-if="columns.visible('remark')" prop="remark" label="审核说明" />
      </el-table>
      <!--分页组件-->
      <pagination />
    </div>
  </div>
</template>

<script>
import crudYxExamineLog from '@/api/yxExamineLog'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import udOperation from '@crud/UD.operation'
import pagination from '@crud/Pagination'
import MaterialList from "@/components/material";

// crud交由presenter持有
const defaultCrud = CRUD({ title: '审核记录', query: { type: 2 }, url: 'api/yxExamineLog', sort: 'id,desc', crudMethod: { ...crudYxExamineLog },optShow: {
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
      },
      statusList:[
        {value:0,label:'待审核'},
        {value:1,label:'通过'},
        {value:2,label:'驳回'}
      ]
    }
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
