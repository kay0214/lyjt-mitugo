<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <!--如果想在工具栏加入更多按钮，可以使用插槽方式， slot = 'left' or 'right'-->
      <el-input v-model="query.title" clearable size="small" placeholder="请输入标题"
                style="width: 250px;" class="filter-item"
                @keyup.enter.native="crud.toQuery" />
      <el-button class="filter-item" size="mini" type="success" icon="el-icon-search"
                 @click="crud.toQuery">搜索</el-button>
      <crudOperation :permission="permission" />
      <!--表单组件-->
      <el-dialog :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="850px">
        <el-form ref="form" v-if="crud.status.cu > 0" :model="form" :rules="rules" size="small" label-width="80px">
          <el-form-item label="标题" prop="title">
            <el-input v-model="form.title" style="width: 650px;" maxlength="15"/>
          </el-form-item>
          <el-form-item label="封面图" prop="coverImg">
            <MaterialList v-model="form.sliderImage" style="width: 650px" type="image" :num="1" :width="150" :height="150"
                          @setValue='(val)=>{
                            form.sliderImage=val;
                            form.coverImg=val.join(",");
                          $refs.form.validateField("coverImg")
                          }'/>
          </el-form-item>
          <el-form-item label="链接">
            <el-input v-model="form.linkUrl" style="width: 650px;" maxlength="200"/>
          </el-form-item>
          <el-form-item label="排序">
            <el-input-number v-model="form.sort" style="width: 650px;" :precision="0"/>
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-radio-group v-model="form.status">
              <el-radio v-for="item in statusList" :label="item.value">
                {{item.label}}</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="内容">
            <editor v-model="form.content"/>
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
        <el-table-column v-if="columns.visible('title')" prop="title" label="标题" />
        <el-table-column v-if="columns.visible('coverImg')" prop="coverImg" label="封面图" >
          <template slot-scope="scope">
            <a :href="scope.row.coverImg" style="color: #42b983" target="_blank">
              <img :src="scope.row.coverImg" alt="点击打开" class="el-avatar"></a>
          </template>
        </el-table-column>
        <el-table-column v-if="columns.visible('sort')" prop="sort" label="排序" />
        <el-table-column v-if="columns.visible('updateUsername')" prop="updateUsername"
                         label="操作人" />
        <el-table-column v-if="columns.visible('createTime')" prop="createTime"
                         label="创建时间">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.createTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column v-if="columns.visible('status')" prop="status" label="状态" >
          <template slot-scope="scope">
            <span @click="update(scope.row)">
              {{ transferLabel(scope.row.status,statusList) }}</span>
          </template>
        </el-table-column>
        <el-table-column v-permission="['admin','yxHotConfig:edit','yxHotConfig:del']" label="操作" width="150px" align="center">
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
import crudYxHotConfig,{updateStatus} from '@/api/yxHotConfig'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import udOperation from '@crud/UD.operation'
import pagination from '@crud/Pagination'
import MaterialList from "@/components/material";
import editor from '@/views/components/Editor'
import checkPermission from '@/utils/permission'

// crud交由presenter持有
const defaultCrud = CRUD({ title: 'hot配置', url: 'api/yxHotConfig', sort: 'id,desc',
  crudMethod: { ...crudYxHotConfig },
  optShow:{
    add: true,
    edit: false,
    del: false,
    download: false
  }
})
const defaultForm = { id: null, title: null, coverImg: null, linkUrl: null, sort: null,
  status: 1, delFlag: null, createUserId: null, updateUserId: null,
  createTime: null, updateTime: null, content: '',
  sliderImage:[] }
export default {
  name: 'YxHotConfig',
  components: { pagination, crudOperation, rrOperation, udOperation ,MaterialList,editor},
  mixins: [presenter(defaultCrud), header(), form(defaultForm), crud()],
  data() {
    return {

      permission: {
        add: ['admin', 'yxHotConfig:add'],
        edit: ['admin', 'yxHotConfig:edit'],
        del: ['admin', 'yxHotConfig:del'],
        udpateStatus: ['admin', 'yxHotConfig:udpateStatus']
      },
      rules: {
        title: [
          { required: true, message: '公司名不能为空', trigger: 'blur' }
        ],
        coverImg:[
          { required: true,message: '必选项', trigger: 'change'}
        ],
        status: [
          { required: true, message: '状态不能为空', trigger: 'blur' }
        ]
      } ,
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
      if(form.coverImg && form.coverImg.length){
        form.sliderImage=form.coverImg.split(',')
      }
    },
    update(data){
      console.log(data)
      let ret=checkPermission(this.permission.udpateStatus)
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
