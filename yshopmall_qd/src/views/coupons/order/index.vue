<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <!--如果想在工具栏加入更多按钮，可以使用插槽方式， slot = 'left' or 'right'-->
      <crudOperation :permission="permission" />
      <!--表单组件-->
      <el-dialog :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="500px">
        <el-form ref="form" :model="form" :rules="rules" size="small" label-width="80px">
          <el-form-item label="订单ID">
            <el-input v-model="form.id" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="订单号" prop="orderId">
            <el-input v-model="form.orderId" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="用户id" prop="uid">
            <el-input v-model="form.uid" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="用户姓名" prop="realName">
            <el-input v-model="form.realName" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="用户电话" prop="userPhone">
            <el-input v-model="form.userPhone" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="订单商品总数" prop="totalNum">
            <el-input v-model="form.totalNum" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="订单总价" prop="totalPrice">
            <el-input v-model="form.totalPrice" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="卡券id" prop="couponId">
            <el-input v-model="form.couponId" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="卡券金额" prop="couponPrice">
            <el-input v-model="form.couponPrice" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="支付状态 0未支付 1已支付" prop="payStaus">
            <el-input v-model="form.payStaus" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="支付时间">
            <el-input v-model="form.payTime" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="可被核销次数">
            <el-input v-model="form.useCount" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="已核销次数">
            <el-input v-model="form.usedCount" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="订单状态（0:待支付 1:已过期 2:待发放3:支付失败4:待使用5:已使用6:已核销7:退款中8:已退款9:退款驳回" prop="status">
            <el-input v-model="form.status" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="0 未退款 1 申请中 2 已退款" prop="refundStatus">
            <el-input v-model="form.refundStatus" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="退款用户说明">
            <el-input v-model="form.refundReasonWapExplain" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="退款时间">
            <el-input v-model="form.refundReasonTime" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="不退款的理由">
            <el-input v-model="form.refundReason" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="退款金额" prop="refundPrice">
            <el-input v-model="form.refundPrice" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="备注" prop="mark">
            <el-input v-model="form.mark" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="商户ID" prop="merId">
            <el-input v-model="form.merId" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="推荐人用户ID">
            <el-input v-model="form.parentId" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="推荐人类型:1商户;2合伙人;3用户">
            <el-input v-model="form.parentType" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="分享人Id">
            <el-input v-model="form.shareId" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="分享人的推荐人id">
            <el-input v-model="form.shareParentId" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="分享人的推荐人类型">
            <el-input v-model="form.shareParentType" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="核销码" prop="verifyCode">
            <el-input v-model="form.verifyCode" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="是否删除（0：未删除，1：已删除）" prop="delFlag">
            <el-input v-model="form.delFlag" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="创建人 根据创建人关联店铺">
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
          <el-form-item label="唯一id(md5加密)类似id" prop="unique">
            <el-input v-model="form.unique" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="支付方式" prop="payType">
            <el-input v-model="form.payType" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="支付渠道(0微信公众号1微信小程序)">
            <el-input v-model="form.isChannel" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="佣金">
            <el-input v-model="form.commission" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="分佣状态 0:未分佣 1:已分佣">
            <el-input v-model="form.rebateStatus" style="width: 370px;" />
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
        <el-table-column v-if="columns.visible('id')" prop="id" label="订单ID" />
        <el-table-column v-if="columns.visible('orderId')" prop="orderId" label="订单号" />
        <el-table-column v-if="columns.visible('uid')" prop="uid" label="用户id" />
        <el-table-column v-if="columns.visible('realName')" prop="realName" label="用户姓名" />
        <el-table-column v-if="columns.visible('userPhone')" prop="userPhone" label="用户电话" />
        <el-table-column v-if="columns.visible('totalNum')" prop="totalNum" label="订单商品总数" />
        <el-table-column v-if="columns.visible('totalPrice')" prop="totalPrice" label="订单总价" />
        <el-table-column v-if="columns.visible('couponId')" prop="couponId" label="卡券id" />
        <el-table-column v-if="columns.visible('couponPrice')" prop="couponPrice" label="卡券金额" />
        <el-table-column v-if="columns.visible('payStaus')" prop="payStaus" label="支付状态 0未支付 1已支付" />
        <el-table-column v-if="columns.visible('payTime')" prop="payTime" label="支付时间" />
        <el-table-column v-if="columns.visible('useCount')" prop="useCount" label="可被核销次数" />
        <el-table-column v-if="columns.visible('usedCount')" prop="usedCount" label="已核销次数" />
        <el-table-column v-if="columns.visible('status')" prop="status" label="订单状态（0:待支付 1:已过期 2:待发放3:支付失败4:待使用5:已使用6:已核销7:退款中8:已退款9:退款驳回" />
        <el-table-column v-if="columns.visible('refundStatus')" prop="refundStatus" label="0 未退款 1 申请中 2 已退款" />
        <el-table-column v-if="columns.visible('refundReasonWapExplain')" prop="refundReasonWapExplain" label="退款用户说明" />
        <el-table-column v-if="columns.visible('refundReasonTime')" prop="refundReasonTime" label="退款时间" />
        <el-table-column v-if="columns.visible('refundReason')" prop="refundReason" label="不退款的理由" />
        <el-table-column v-if="columns.visible('refundPrice')" prop="refundPrice" label="退款金额" />
        <el-table-column v-if="columns.visible('mark')" prop="mark" label="备注" />
        <el-table-column v-if="columns.visible('merId')" prop="merId" label="商户ID" />
        <el-table-column v-if="columns.visible('parentId')" prop="parentId" label="推荐人用户ID" />
        <el-table-column v-if="columns.visible('parentType')" prop="parentType" label="推荐人类型:1商户;2合伙人;3用户" />
        <el-table-column v-if="columns.visible('shareId')" prop="shareId" label="分享人Id" />
        <el-table-column v-if="columns.visible('shareParentId')" prop="shareParentId" label="分享人的推荐人id" />
        <el-table-column v-if="columns.visible('shareParentType')" prop="shareParentType" label="分享人的推荐人类型" />
        <el-table-column v-if="columns.visible('verifyCode')" prop="verifyCode" label="核销码" />
        <el-table-column v-if="columns.visible('delFlag')" prop="delFlag" label="是否删除（0：未删除，1：已删除）" />
        <el-table-column v-if="columns.visible('createUserId')" prop="createUserId" label="创建人 根据创建人关联店铺" />
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
        <el-table-column v-if="columns.visible('unique')" prop="unique" label="唯一id(md5加密)类似id" />
        <el-table-column v-if="columns.visible('payType')" prop="payType" label="支付方式" />
        <el-table-column v-if="columns.visible('isChannel')" prop="isChannel" label="支付渠道(0微信公众号1微信小程序)" />
        <el-table-column v-if="columns.visible('commission')" prop="commission" label="佣金" />
        <el-table-column v-if="columns.visible('rebateStatus')" prop="rebateStatus" label="分佣状态 0:未分佣 1:已分佣" />
        <el-table-column v-permission="['admin','yxCouponOrder:edit','yxCouponOrder:del']" label="操作" width="150px" align="center">
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
import crudYxCouponOrder from '@/api/yxCouponOrder'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import udOperation from '@crud/UD.operation'
import pagination from '@crud/Pagination'
import MaterialList from "@/components/material";

// crud交由presenter持有
const defaultCrud = CRUD({ title: '卡券订单表', url: 'api/yxCouponOrder', sort: 'id,desc', crudMethod: { ...crudYxCouponOrder }})
const defaultForm = { id: null, orderId: null, uid: null, realName: null, userPhone: null, totalNum: null, totalPrice: null, couponId: null, couponPrice: null, payStaus: null, payTime: null, useCount: null, usedCount: null, status: null, refundStatus: null, refundReasonWapExplain: null, refundReasonTime: null, refundReason: null, refundPrice: null, mark: null, merId: null, parentId: null, parentType: null, shareId: null, shareParentId: null, shareParentType: null, verifyCode: null, delFlag: null, createUserId: null, updateUserId: null, createTime: null, updateTime: null, unique: null, payType: null, isChannel: null, commission: null, rebateStatus: null }
export default {
  name: 'YxCouponOrder',
  components: { pagination, crudOperation, rrOperation, udOperation ,MaterialList},
  mixins: [presenter(defaultCrud), header(), form(defaultForm), crud()],
  data() {
    return {
      
      permission: {
        add: ['admin', 'yxCouponOrder:add'],
        edit: ['admin', 'yxCouponOrder:edit'],
        del: ['admin', 'yxCouponOrder:del']
      },
      rules: {
        orderId: [
          { required: true, message: '订单号不能为空', trigger: 'blur' }
        ],
        uid: [
          { required: true, message: '用户id不能为空', trigger: 'blur' }
        ],
        realName: [
          { required: true, message: '用户姓名不能为空', trigger: 'blur' }
        ],
        userPhone: [
          { required: true, message: '用户电话不能为空', trigger: 'blur' }
        ],
        totalNum: [
          { required: true, message: '订单商品总数不能为空', trigger: 'blur' }
        ],
        totalPrice: [
          { required: true, message: '订单总价不能为空', trigger: 'blur' }
        ],
        couponId: [
          { required: true, message: '卡券id不能为空', trigger: 'blur' }
        ],
        couponPrice: [
          { required: true, message: '卡券金额不能为空', trigger: 'blur' }
        ],
        payStaus: [
          { required: true, message: '支付状态 0未支付 1已支付不能为空', trigger: 'blur' }
        ],
        status: [
          { required: true, message: '订单状态（0:待支付 1:已过期 2:待发放3:支付失败4:待使用5:已使用6:已核销7:退款中8:已退款9:退款驳回不能为空', trigger: 'blur' }
        ],
        refundStatus: [
          { required: true, message: '0 未退款 1 申请中 2 已退款不能为空', trigger: 'blur' }
        ],
        refundPrice: [
          { required: true, message: '退款金额不能为空', trigger: 'blur' }
        ],
        mark: [
          { required: true, message: '备注不能为空', trigger: 'blur' }
        ],
        merId: [
          { required: true, message: '商户ID不能为空', trigger: 'blur' }
        ],
        verifyCode: [
          { required: true, message: '核销码不能为空', trigger: 'blur' }
        ],
        delFlag: [
          { required: true, message: '是否删除（0：未删除，1：已删除）不能为空', trigger: 'blur' }
        ],
        createTime: [
          { required: true, message: '创建时间不能为空', trigger: 'blur' }
        ],
        updateTime: [
          { required: true, message: '更新时间不能为空', trigger: 'blur' }
        ],
        unique: [
          { required: true, message: '唯一id(md5加密)类似id不能为空', trigger: 'blur' }
        ],
        payType: [
          { required: true, message: '支付方式不能为空', trigger: 'blur' }
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
