<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <!--如果想在工具栏加入更多按钮，可以使用插槽方式， slot = 'left' or 'right'-->
      <div class="fles">
        <el-input v-model="query.cateName" clearable placeholder="分类名称" style="width: 130px;marginRight:6px;" class="filter-item" />
        <rrOperation :crud="crud" />
        <crudOperation :permission="permission" class="tips" />
      </div>

      <!--表单组件-->
      <el-dialog :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="500px">
        <el-form ref="form" :model="form" :rules="rules" size="small" label-width="140px">
          <el-form-item label="分类名称" prop="cateName">
            <el-input v-model="form.cateName" style="width: 100%;" />
          </el-form-item>
          <el-form-item label="分类图片(128*128)" prop="path">
            <MaterialList v-model="pathArr" style="width: 100%" type="image" :num="1" :width="150" :height="150" />
          </el-form-item>
          <el-form-item label="是否显示" prop="isShow">
            <el-radio-group v-model="form.isShow">
              <el-radio :label="0">隐藏</el-radio>
              <el-radio :label="1">显示</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="排序" prop="sort">
            <el-input v-model="form.sort" style="width: 100%;" maxlength="3" />
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
        <el-table-column v-if="columns.visible('id')" prop="id" label="分类id" />
        <el-table-column v-if="columns.visible('cateName')" prop="cateName" label="分类名称" />
        <el-table-column v-if="columns.visible('isShow')" prop="isShow" label="显示状态">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.isShow === 1">显示</el-tag>
            <el-tag v-else type="info">隐藏</el-tag>
          </template>
        </el-table-column>
        <el-table-column v-if="columns.visible('sort')" prop="sort" label="排序" sortable/>
        <el-table-column v-permission="['admin','yxCouponsCategory:edit','yxCouponsCategory:del']" label="操作" width="150px" align="center">
          <template slot-scope="scope">
            <udOperation
              :data="scope.row"
              :permission="permission"
            />
          </template>
        </el-table-column>
      </el-table>
      <!--分页组件-->
      <!-- <pagination /> -->
    </div>
  </div>
</template>

<script>
import crudYxCouponsCategory from '@/api/yxCouponsCategory'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import udOperation from '@crud/UD.operation'
import pagination from '@crud/Pagination'
import MaterialList from "@/components/material";

// crud交由presenter持有
const defaultCrud = CRUD({ title: '分类', url: 'api/yxCouponsCategory', sort: 'id,desc', crudMethod: { ...crudYxCouponsCategory },optShow: {
      add: true,
      edit: true,
      del: true,
      download: false
    }})
const defaultForm = { id: null, pid: null, cateName: null, sort: null, isShow: 0, delFlag: null, createUserId: null, updateUserId: null, createTime: null, updateTime: null,path: null }
const pathArr = []
if (defaultForm.path) { pathArr[0] = defaultForm.path }
export default {
  name: 'YxCouponsCategory',
  components: { pagination, crudOperation, rrOperation, udOperation ,MaterialList},
  mixins: [presenter(defaultCrud), header(), form(defaultForm), crud()],
  data() {
    return {
      pathArr: pathArr,
      permission: {
        add: ['admin', 'yxCouponsCategory:add'],
        edit: ['admin', 'yxCouponsCategory:edit'],
        del: ['admin', 'yxCouponsCategory:del']
      },
      rules: {
        cateName: [
          { required: true, message: '分类名称不能为空', trigger: 'blur' },
          { max: 20, message: '20个字符以内', trigger: 'blur' }
        ],
        sort: [
          { required: true, message: '排序不能为空', trigger: 'blur' }
        ],
        isShow: [
          { required: true, message: '是否推荐. 0:不推荐, 1:推荐不能为空', trigger: 'blur' }
        ],
        path:[
          {required: true, message: '分类图片不能为空', trigger: 'blur' }
        ]
      }
    }
  },
  watch: {
    pathArr(value) {
      this.form.path = value.join(',')
      if (this.$refs.form) {
        this.$refs.form.validateField('path')
      }
    }
  },
  methods: {
    // 获取数据前设置好接口地址
    [CRUD.HOOK.beforeRefresh]() {
      return true
    }, // 新增与编辑前做的操作
    [CRUD.HOOK.afterToCU](crud, form) {
      let pathArr = [];
      if (form.path) {
        pathArr.push(form.path)
      }
      this.pathArr = pathArr;
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
  .fles{
    display: flex;
    align-items: center;
  }
  .tips >>> .crud-opts-right {
    margin: 0 0px 10px 2px;
    position: absolute;
    right: 20px;
  }
    .tips >>>.crud-opts-left{
    margin-left: 2px;
  }
</style>
