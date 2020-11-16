<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <!--如果想在工具栏加入更多按钮，可以使用插槽方式， slot = 'left' or 'right'-->
<!--      <crudOperation :permission="permission" />-->
      <!--表单组件-->
      <el-dialog :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="500px">
        <el-form ref="form" :model="form" :rules="rules" size="small" label-width="80px">
          <el-form-item label="图片" prop="noticeImage">
            <MaterialList v-model="form.sliderImage" style="width: 370px" type="image"
                          :num="1" :width="150" :height="150"
                          @setValue='(val)=>{
                            form.sliderImage=val;
                            form.noticeImage=val.join(",");
                          $refs.form.validateField("noticeImage")
                          }'/>
          </el-form-item>
          <el-form-item label="内容">
            <el-input v-model="form.noticeContent" type="textarea"
                      :row='5' style="width: 370px;" maxlength="100"/>
          </el-form-item>
          <el-form-item label="跳转链接" prop="linkUrl">
            <el-input v-model="form.linkUrl" type="textarea"
                      :row='5' style="width: 370px;" maxlength="500"/>
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-radio-group v-model="form.status">
              <el-radio v-for="item in statusList" :label="item.value">
                {{item.label}}</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="text" @click="crud.cancelCU">取消</el-button>
          <el-button :loading="crud.cu === 2" type="primary" @click="crud.submitCU">保存</el-button>
        </div>
      </el-dialog>
      <!--表格渲染-->
      <el-table ref="table" v-loading="crud.loading" :data="crud.data" size="small" style="width: 100%;" @selection-change="crud.selectionChangeHandler">
        <el-table-column type="selection" width="55" />
        <el-table-column v-if="columns.visible('id')" prop="id" label="id" />
        <el-table-column v-if="columns.visible('noticeImage')" prop="noticeImage" label="图片" >
          <template slot-scope="scope">
            <a :href="scope.row.noticeImage" style="color: #42b983" target="_blank">
              <img :src="scope.row.noticeImage" alt="点击打开" class="el-avatar"></a>
          </template>
        </el-table-column>
        <el-table-column v-if="columns.visible('noticeContent')" prop="noticeContent" label="内容" />
        <el-table-column v-if="columns.visible('linkUrl')" prop="linkUrl" label="跳转链接" />
        <el-table-column v-if="columns.visible('status')" prop="status" label="状态" >
          <template slot-scope="scope">
            <span @click="update(scope.row)">
              {{ transferLabel(scope.row.status,statusList) }}</span>
          </template>
        </el-table-column>
        <el-table-column v-permission="['admin','yxNotice:edit','yxNotice:del']" label="操作" width="150px" align="center">
          <template slot-scope="scope">
            <udOperation
              :data="scope.row"
              :permission="permission"
              :disabledDle="true"
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
import crudYxNotice from '@/api/yxNotice'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import udOperation from '@crud/UD.operation'
import pagination from '@crud/Pagination'
import MaterialList from "@/components/material";
import checkPermission from '@/utils/permission'
import { validURL } from '@/utils/validate'

// crud交由presenter持有
const defaultCrud = CRUD({ title: '公告', url: 'api/yxNotice', sort: 'id,desc',
  crudMethod: { ...crudYxNotice }})
const defaultForm = { id: null, noticeImage: null, noticeContent: null, linkUrl: null,
  status: null, delFlag: null, createUserId: null, updateUserId: null, createTime: null,
  updateTime: null,sliderImage:[] }
export default {
  name: 'YxNotice',
  components: { pagination, crudOperation, rrOperation, udOperation ,MaterialList},
  mixins: [presenter(defaultCrud), header(), form(defaultForm), crud()],
  data() {
    return {
      statusList:[
        {value:0,label:'启用'},
        {value:1,label:'禁用'}
      ],
      permission: {
        add: ['admin', 'yxNotice:add'],
        edit: ['admin', 'yxNotice:edit'],
        del: ['']
      },
      rules: {
        status: [
          { required: true, message: '状态不能为空', trigger: 'blur' }
        ],
        noticeImage: [
          { required: true, message: '必选项', trigger: 'change' }
        ],
        linkUrl: [
          { validator:(r,v,c)=>{
            if(!validURL(v)){
              c(new Error('请检查地址格式'))
            }
          }, trigger: 'blur' }
        ]
      }
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
      if(form.noticeImage && form.noticeImage.length){
        form.sliderImage=form.noticeImage.split(',')
      }
    },
    update(data){
      let ret=checkPermission(this.permission.edit)
      if(!ret){
        return ret
      }
      let that=this
      this.$confirm(`确定进行[${data.status ? '启用' : '禁用'}]操作?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          console.log(data)
          that.crud.crudMethod.edit(Object.assign(data,{status:Number(!data.status)})).then(() => {
            this.crud.refresh()
            this.$message({
              message: '操作成功',
              type: 'success',
              duration: 1000
            })
          })
        })
        .catch(() => { })
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
