<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <!--如果想在工具栏加入更多按钮，可以使用插槽方式， slot = 'left' or 'right'-->
      <crudOperation :permission="permission" />
      <!--表单组件-->
      <el-dialog :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="500px">
        <el-form ref="form" :model="form" :rules="rules" size="small" label-width="80px">
          <el-form-item label="评论ID">
            <el-input v-model="form.id" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="用户ID" prop="uid">
            <el-input v-model="form.uid" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="订单ID" prop="oid">
            <el-input v-model="form.oid" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="卡券id" prop="couponId">
            <el-input v-model="form.couponId" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="总体感觉" prop="generalScore">
            <el-input v-model="form.generalScore" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="评论内容" prop="comment">
            <el-input v-model="form.comment" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="评论时间" prop="addTime">
            <el-input v-model="form.addTime" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="管理员回复时间">
            <el-input v-model="form.merchantReplyTime" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="0：未回复，1：已回复" prop="isReply">
            <el-input v-model="form.isReply" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="商户id" prop="merId">
            <el-input v-model="form.merId" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="管理员回复内容">
            <el-input v-model="form.merchantReplyContent" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="是否删除（0：未删除，1：已删除）" prop="delFlag">
            <el-input v-model="form.delFlag" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="创建人">
            <el-input v-model="form.createUserId" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="修改人">
            <el-input v-model="form.updateUserId" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="创建时间" prop="createTime">
            <el-input v-model="form.createTime" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="更新时间" prop="updateTime">
            <el-input v-model="form.updateTime" style="width: 370px;" />
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
        <el-table-column v-if="columns.visible('id')" prop="id" label="评论ID" />
        <el-table-column v-if="columns.visible('uid')" prop="uid" label="用户ID" />
        <el-table-column v-if="columns.visible('oid')" prop="oid" label="订单ID" />
        <el-table-column v-if="columns.visible('couponId')" prop="couponId" label="卡券id" />
        <el-table-column v-if="columns.visible('generalScore')" prop="generalScore" label="总体感觉" />
        <el-table-column v-if="columns.visible('comment')" prop="comment" label="评论内容" />
        <el-table-column v-if="columns.visible('addTime')" prop="addTime" label="评论时间" />
        <el-table-column v-if="columns.visible('merchantReplyTime')" prop="merchantReplyTime" label="管理员回复时间" />
        <el-table-column v-if="columns.visible('isReply')" prop="isReply" label="0：未回复，1：已回复" />
        <el-table-column v-if="columns.visible('merId')" prop="merId" label="商户id" />
        <el-table-column v-if="columns.visible('merchantReplyContent')" prop="merchantReplyContent" label="管理员回复内容" />
        <el-table-column v-if="columns.visible('delFlag')" prop="delFlag" label="是否删除（0：未删除，1：已删除）" />
        <el-table-column v-if="columns.visible('createUserId')" prop="createUserId" label="创建人" />
        <el-table-column v-if="columns.visible('updateUserId')" prop="updateUserId" label="修改人" />
        <el-table-column v-if="columns.visible('createTime')" prop="createTime" label="创建时间">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.createTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column v-if="columns.visible('updateTime')" prop="updateTime" label="更新时间">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.updateTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column v-permission="['admin','yxCouponsReply:edit','yxCouponsReply:del']" label="操作" width="150px" align="center">
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
import crudYxCouponsReply from '@/api/yxCouponsReply'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import udOperation from '@crud/UD.operation'
import pagination from '@crud/Pagination'
import MaterialList from "@/components/material";

// crud交由presenter持有
const defaultCrud = CRUD({ title: '本地生活评论', url: 'api/yxCouponsReply', sort: 'id,desc', crudMethod: { ...crudYxCouponsReply }})
const defaultForm = { id: null, uid: null, oid: null, couponId: null, generalScore: null, comment: null, addTime: null, merchantReplyTime: null, isReply: null, merId: null, merchantReplyContent: null, delFlag: null, createUserId: null, updateUserId: null, createTime: null, updateTime: null }
export default {
  name: 'YxCouponsReply',
  components: { pagination, crudOperation, rrOperation, udOperation ,MaterialList},
  mixins: [presenter(defaultCrud), header(), form(defaultForm), crud()],
  data() {
    return {
      
      permission: {
        add: ['admin', 'yxCouponsReply:add'],
        edit: ['admin', 'yxCouponsReply:edit'],
        del: ['admin', 'yxCouponsReply:del']
      },
      rules: {
        uid: [
          { required: true, message: '用户ID不能为空', trigger: 'blur' }
        ],
        oid: [
          { required: true, message: '订单ID不能为空', trigger: 'blur' }
        ],
        couponId: [
          { required: true, message: '卡券id不能为空', trigger: 'blur' }
        ],
        generalScore: [
          { required: true, message: '总体感觉不能为空', trigger: 'blur' }
        ],
        comment: [
          { required: true, message: '评论内容不能为空', trigger: 'blur' }
        ],
        addTime: [
          { required: true, message: '评论时间不能为空', trigger: 'blur' }
        ],
        isReply: [
          { required: true, message: '0：未回复，1：已回复不能为空', trigger: 'blur' }
        ],
        merId: [
          { required: true, message: '商户id不能为空', trigger: 'blur' }
        ],
        delFlag: [
          { required: true, message: '是否删除（0：未删除，1：已删除）不能为空', trigger: 'blur' }
        ],
        createTime: [
          { required: true, message: '创建时间不能为空', trigger: 'blur' }
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
