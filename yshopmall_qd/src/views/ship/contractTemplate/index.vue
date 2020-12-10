<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <!--如果想在工具栏加入更多按钮，可以使用插槽方式， slot = 'left' or 'right'-->
      <el-input v-model.trim="query.tempName" clearable size="small" placeholder="请输入模板名称（完全匹配）" style="width: 250px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
      <el-button class="filter-item" size="mini" type="success" icon="el-icon-search" @click="crud.toQuery">搜索</el-button>
      <crudOperation :permission="permission" />
      <!--表单组件-->
      <el-dialog :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="500px">
        <el-form ref="form" :model="form" :rules="rules" size="small" label-width="80px">
          <el-form-item label="模板名称" prop="tempName">
            <el-input v-model.trim="form.tempName" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="模板文件" prop="file">
            <el-upload
              drag
              action="/api/upload"
              :headers="headers"
              :file-list="fileList"
              :before-upload="beforeUpload"
              :on-success="handleSuccess"
              :on-change="handleChange"
              :on-preview="handlePreview"
              accept="application/pdf"
              >
              <i class="el-icon-upload"></i>
              <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
              <div class="el-upload__tip" slot="tip">不超过5Mb</div>
            </el-upload>
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
        <el-table-column v-if="columns.visible('tempName')" prop="tempName" label="模板名称" />
        <el-table-column v-if="columns.visible('createNickname')" prop="createNickname" label="添加人" />
        <el-table-column v-if="columns.visible('createTime')" prop="createTime" label="添加时间">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.createTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column v-if="columns.visible('updateTime')" prop="updateTime" label="修改时间">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.updateTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column v-permission="['admin','yxContractTemplate:edit','yxContractTemplate:del']" label="操作" width="150px" align="center">
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
import crudYxContractTemplate from '@/api/yxContractTemplate'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import udOperation from '@crud/UD.operation'
import pagination from '@crud/Pagination'
import MaterialList from "@/components/material";
import { getToken } from '@/utils/auth'
// crud交由presenter持有
const defaultCrud = CRUD({ title: '合同模板', url: 'api/yxContractTemplate', sort: 'id,desc',
  crudMethod: { ...crudYxContractTemplate },optShow:{
    add: true,
    edit: false,
    del: false,
    download: false
  }
})
const defaultForm = { id: null, tempName: null,
  filePath: null, delFlag: null, createUserId: null,
  updateUserId: null, createTime: null, updateTime: null }
export default {
  name: 'YxContractTemplate',
  components: { pagination, crudOperation, rrOperation, udOperation ,MaterialList},
  mixins: [presenter(defaultCrud), header(), form(defaultForm), crud()],
  data() {
    return {
      dialogImageUrl: '',
      dialogVisible: false,
      disabled: false,
      headers: {
        Authorization: getToken()
      },
      permission: {
        add: ['admin', 'yxContractTemplate:add'],
        edit: ['admin', 'yxContractTemplate:edit'],
        del: ['admin', 'yxContractTemplate:del']
      },
      rules: {
        tempName: [
          { required: true, message: '模板名称不能为空', trigger: 'blur' }
        ],
        file: [
          { required: true, message: '至少传一个文件', trigger: 'change' }
        ]
      },
      fileList:[],
      fileUrl:''
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
      if(crud.status.edit){
        this.fileList=[
          {name:this.form.tempName,
            url:this.form.filePath}
        ]
      }else{
        this.fileList=[]
      }
    },
    beforeUpload(file) {
      const isPdf =
        file.type === 'application/pdf'
      if (!isPdf) {
        this.$message.error('上传模板只能是 pdf 格式!')
        return false
      }
      const isLt5M = file.size / 1024 / 1024 < 5
      if (!isLt5M) {
        this.$message.error('上传大小不能超过 5MB!')
      }
      return isLt5M
    },
    handleSuccess(response, file, fileList) {
      this.fileList=fileList
      this.fileUrl=response.link
      this.$set(this.form,'file',file)
      this.$set(this.form,'filePath',response.link)
    },
    handleChange(files, fileList){
      if(fileList.length>1){
        fileList.splice(0,(fileList.length-1))
      }
    },
    handlePreview(file){
      if(file.url){
        window.open(file.url, '_blank')
      }else{
        window.open(file.response.link, '_blank')
      }
    }
  }
}



</script>

<style scoped>
</style>
