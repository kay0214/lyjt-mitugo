<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <!--如果想在工具栏加入更多按钮，可以使用插槽方式， slot = 'left' or 'right'-->
      <crudOperation :permission="permission" />
      <!--表单组件-->
      <el-dialog :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="900px">
        <!--        <el-form ref="form" :model="form" :rules="rules" size="small" label-width="80px">-->
        <el-form ref="form" :model="form" :inline="true" :rules="rules" size="small" label-width="80px">
          <el-form-item label="店铺编号" prop="storeNid">
            <el-input v-model="form.storeNid" style="width: 370px;" disabled="disabled" />
          </el-form-item>
          <el-form-item label="店铺名称" prop="storeName">
            <el-input v-model="form.storeName" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="管理人用户名" prop="manageUserName">
            <el-input v-model="form.manageUserName" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="管理人电话" prop="manageMobile">
            <el-input v-model="form.manageMobile" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="店铺电话" prop="storeMobile">
            <el-input v-model="form.storeMobile" style="width: 370px;" />
          </el-form-item>

          <el-form-item label="人均消费" prop="perCapita">
            <el-input v-model="form.perCapita" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="行业类别" prop="industryCategory">
            <el-select v-model="form.industryCategory" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="店铺服务"prop="stroeService">
            <!--<el-checkbox></el-checkbox>-->
            <!--<el-input v-model="form.stroeService" style="width: 370px;" />-->
          </el-form-item>
          <el-form-item label="店铺图片" prop="imageArr">
            <MaterialList v-model="form.imageArr" style="width: 500px" type="image" :num="1" :width="150" :height="150" />
          </el-form-item>
          <el-form-item label="轮播图" prop="sliderImageArr">
            <MaterialList v-model="form.sliderImageArr" style="width: 500px" type="image" :num="4" :width="150" :height="150" />
          </el-form-item>
          <el-form-item label="店铺省市区" prop="storeProvince">
            <el-input v-model="form.storeProvince" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="店铺详细地址" prop="storeAddress">
            <el-input v-model="form.storeAddress" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="店铺介绍" prop="introduction">
            <editor v-model="form.introduction" />
          </el-form-item>
          <el-form-item label="地图坐标经度">
            <el-input v-model="form.coordinateX" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="地图坐标纬度">
            <el-input v-model="form.coordinateY" style="width: 370px;" />
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
        <!--<el-table-column v-if="columns.visible('id')" prop="id" label="id" />-->
        <el-table-column v-if="columns.visible('storeNid')" prop="storeNid" label="店铺编号" />
        <el-table-column v-if="columns.visible('storeName')" prop="storeName" label="店铺名称" />
        <el-table-column v-if="columns.visible('manageUserName')" prop="manageUserName" label="管理人用户名" />
        <el-table-column v-if="columns.visible('industryCategory')" prop="industryCategory" label="行业类别" />
        <el-table-column v-if="columns.visible('manageMobile')" prop="manageMobile" label="管理人电话" />
        <el-table-column v-if="columns.visible('storeMobile')" prop="storeMobile" label="店铺电话" />
        <!--        <el-table-column v-if="columns.visible('status')" prop="status" label="状态：0：上架，1：下架" />-->
        <el-table-column label="状态" align="center">
          <template slot-scope="scope">
            <div @click="onSale(scope.row.id,scope.row.status)">
              <el-tag v-if="scope.row.status === 1" style="cursor: pointer" :type="''">已上架</el-tag>
              <el-tag v-else style="cursor: pointer" :type=" 'info' ">已下架</el-tag>
            </div>
          </template>
        </el-table-column>
        <el-table-column v-permission="['admin','yxStoreInfo:edit','yxStoreInfo:del']" label="操作" width="150px" align="center">
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
  import crudYxStoreInfo from '@/api/yxStoreInfo'
  import editor from '../../components/Editor'
  import picUpload from '@/components/pic-upload'
  import mulpicUpload from '@/components/mul-pic-upload'
  import CRUD, { presenter, header, form, crud } from '@crud/crud'
  import rrOperation from '@crud/RR.operation'
  import crudOperation from '@crud/CRUD.operation'
  import udOperation from '@crud/UD.operation'
  import pagination from '@crud/Pagination'
  import MaterialList from "@/components/material";
  import {onsale} from '@/api/yxStoreInfo'

  // crud交由presenter持有
  const defaultCrud = CRUD({ title: '店铺表', url: 'api/yxStoreInfo', sort: 'id,desc',optShow: {
      add: false,
      edit: false,
      del: false,
      download: false
    }, crudMethod: { ...crudYxStoreInfo }})
  const defaultForm = { id: null, storeNid: null, storeName: null, manageUserName: null, merId: null, partnerId: null, manageMobile: null, storeMobile: null, status: null, perCapita: null, industryCategory: null, storeProvince: null, storeAddress: null, delFlag: null, createUserId: null, updateUserId: null, createTime: null, updateTime: null, introduction: null, coordinateX: null, coordinateY: null }
  export default {
    name: 'YxStoreInfo',
    // components: { pagination, crudOperation, rrOperation, udOperation ,MaterialList},
    components: {editor, picUpload, mulpicUpload,pagination, crudOperation, rrOperation, udOperation ,MaterialList},
    mixins: [presenter(defaultCrud), header(), form(defaultForm), crud()],
    data() {
      return {

        permission: {
          add: ['admin', 'yxStoreInfo:add'],
          edit: ['admin', 'yxStoreInfo:edit'],
          del: ['admin', 'yxStoreInfo:del']
        },
        rules: {
          storeNid: [
            { required: true, message: '店铺编号不能为空', trigger: 'blur' }
          ],
          storeName: [
            { required: true, message: '店铺名称不能为空', trigger: 'blur' }
          ],
          manageUserName: [
            { required: true, message: '管理人用户名不能为空', trigger: 'blur' }
          ],
          perCapita: [
            { required: true, message: '人均消费不能为空', trigger: 'blur' }
          ],
          industryCategory: [
            { required: true/*, message: '管理人用户名不能为空', trigger: 'blur'*/ }
          ],
          stroeService: [
            { required: true /*,message: '不能为空', trigger: 'blur'*/ }
          ],
          imageArr: [
            { required: true}
          ],
          sliderImageArr: [
            { required: true }
          ],
          /*merId: [
            { required: true, message: '商户id不能为空', trigger: 'blur' }
          ],
          partnerId: [
            { required: true, message: '合伙人id不能为空', trigger: 'blur' }
          ],*/
          manageMobile: [
            { required: true, message: '管理人电话不能为空', trigger: 'blur' }
          ],
          storeMobile: [
            { required: true, message: '店铺电话不能为空', trigger: 'blur' }
          ],
          introduction: [
            { required: true, message: '店铺介绍不能为空', trigger: 'blur' }
          ],
          storeProvince: [
            { required: true, message: '店铺省市区不能为空', trigger: 'blur' }
          ],
          storeAddress: [
            { required: true, message: '店铺详细地址不能为空', trigger: 'blur' }
          ]/*,
          updateTime: [
            { required: true, message: '更新时间不能为空', trigger: 'blur' }
          ]*/
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
      onSale(id, status) {
        this.$confirm(`确定进行[${status ? '下架' : '上架'}]操作?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
          .then(() => {
            onsale(id, { status: status }).then(({ data }) => {
              this.$message({
                message: '操作成功',
                type: 'success',
                duration: 1000,
                onClose: () => {
                  this.init()
                }
              })
            })
          })
          .catch(() => { })
      }
    },

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
