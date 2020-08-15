<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <!--如果想在工具栏加入更多按钮，可以使用插槽方式， slot = 'left' or 'right'-->
      <crudOperation :permission="permission" />
      <!--表单组件-->
      <el-dialog :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="500px">
        <el-form ref="form" :model="form" :rules="rules" size="small" label-width="80px">
          <el-form-item label="卡券主键">
            <el-input v-model="form.id" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="卡券编号" prop="couponNum">
            <el-input v-model="form.couponNum" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="卡券名称" prop="couponName">
            <el-input v-model="form.couponName" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="卡券类型;1:代金券, 2:折扣券, 3:满减券" prop="couponType">
            <el-input v-model="form.couponType" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="卡券所属分类" prop="couponCategory">
            <el-input v-model="form.couponCategory" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="代金券面额, coupon_type为1时使用">
            <el-input v-model="form.denomination" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="折扣券折扣率, coupon_type为2时使用">
            <el-input v-model="form.discount" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="使用门槛, coupon_type为3时使用">
            <el-input v-model="form.threshold" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="优惠金额, coupon_type为3时使用">
            <el-input v-model="form.discountAmount" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="销售价格" prop="sellingPrice">
            <el-input v-model="form.sellingPrice" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="原价" prop="originalPrice">
            <el-input v-model="form.originalPrice" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="平台结算价" prop="settlementPrice">
            <el-input v-model="form.settlementPrice" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="佣金" prop="commission">
            <el-input v-model="form.commission" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="每人限购数量" prop="quantityLimit">
            <el-input v-model="form.quantityLimit" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="库存" prop="inventory">
            <el-input v-model="form.inventory" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="销量">
            <el-input v-model="form.sales" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="虚拟销量">
            <el-input v-model="form.ficti" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="核销次数" prop="writeOff">
            <el-input v-model="form.writeOff" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="有效期始" prop="expireDateStart">
            <el-input v-model="form.expireDateStart" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="有效期止" prop="expireDateEnd">
            <el-input v-model="form.expireDateEnd" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="热门优惠; 1:是, 0否">
            <el-input v-model="form.isHot" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="状态（0：未上架，1：上架）">
            <el-input v-model="form.isShow" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="过期退 0:不支持 1支持">
            <el-input v-model="form.outtimeRefund" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="免预约 0:不支持 1支持">
            <el-input v-model="form.needOrder" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="随时退 0:不支持 1支持">
            <el-input v-model="form.awaysRefund" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="使用条件 描述" prop="useCondition">
            <el-input v-model="form.useCondition" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="可用时间始" prop="availableTimeStart">
            <el-input v-model="form.availableTimeStart" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="可用时间止" prop="availableTimeEnd">
            <el-input v-model="form.availableTimeEnd" style="width: 370px;" />
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
          <el-form-item label="卡券详情" prop="content">
            <el-input v-model="form.content" style="width: 370px;" />
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
        <el-table-column v-if="columns.visible('id')" prop="id" label="卡券主键" />
        <el-table-column v-if="columns.visible('couponNum')" prop="couponNum" label="卡券编号" />
        <el-table-column v-if="columns.visible('couponName')" prop="couponName" label="卡券名称" />
        <el-table-column v-if="columns.visible('couponType')" prop="couponType" label="卡券类型;1:代金券, 2:折扣券, 3:满减券" />
        <el-table-column v-if="columns.visible('couponCategory')" prop="couponCategory" label="卡券所属分类" />
        <el-table-column v-if="columns.visible('denomination')" prop="denomination" label="代金券面额, coupon_type为1时使用" />
        <el-table-column v-if="columns.visible('discount')" prop="discount" label="折扣券折扣率, coupon_type为2时使用" />
        <el-table-column v-if="columns.visible('threshold')" prop="threshold" label="使用门槛, coupon_type为3时使用" />
        <el-table-column v-if="columns.visible('discountAmount')" prop="discountAmount" label="优惠金额, coupon_type为3时使用" />
        <el-table-column v-if="columns.visible('sellingPrice')" prop="sellingPrice" label="销售价格" />
        <el-table-column v-if="columns.visible('originalPrice')" prop="originalPrice" label="原价" />
        <el-table-column v-if="columns.visible('settlementPrice')" prop="settlementPrice" label="平台结算价" />
        <el-table-column v-if="columns.visible('commission')" prop="commission" label="佣金" />
        <el-table-column v-if="columns.visible('quantityLimit')" prop="quantityLimit" label="每人限购数量" />
        <el-table-column v-if="columns.visible('inventory')" prop="inventory" label="库存" />
        <el-table-column v-if="columns.visible('sales')" prop="sales" label="销量" />
        <el-table-column v-if="columns.visible('ficti')" prop="ficti" label="虚拟销量" />
        <el-table-column v-if="columns.visible('writeOff')" prop="writeOff" label="核销次数" />
        <el-table-column v-if="columns.visible('expireDateStart')" prop="expireDateStart" label="有效期始">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.expireDateStart) }}</span>
          </template>
        </el-table-column>
        <el-table-column v-if="columns.visible('expireDateEnd')" prop="expireDateEnd" label="有效期止">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.expireDateEnd) }}</span>
          </template>
        </el-table-column>
        <el-table-column v-if="columns.visible('isHot')" prop="isHot" label="热门优惠; 1:是, 0否" />
        <el-table-column v-if="columns.visible('isShow')" prop="isShow" label="状态（0：未上架，1：上架）" />
        <el-table-column v-if="columns.visible('outtimeRefund')" prop="outtimeRefund" label="过期退 0:不支持 1支持" />
        <el-table-column v-if="columns.visible('needOrder')" prop="needOrder" label="免预约 0:不支持 1支持" />
        <el-table-column v-if="columns.visible('awaysRefund')" prop="awaysRefund" label="随时退 0:不支持 1支持" />
        <el-table-column v-if="columns.visible('useCondition')" prop="useCondition" label="使用条件 描述" />
        <el-table-column v-if="columns.visible('availableTimeStart')" prop="availableTimeStart" label="可用时间始" />
        <el-table-column v-if="columns.visible('availableTimeEnd')" prop="availableTimeEnd" label="可用时间止" />
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
        <el-table-column v-if="columns.visible('content')" prop="content" label="卡券详情" />
        <el-table-column v-permission="['admin','yxCoupons:edit','yxCoupons:del']" label="操作" width="150px" align="center">
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
import crudYxCoupons from '@/api/yxCoupons'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import udOperation from '@crud/UD.operation'
import pagination from '@crud/Pagination'
import MaterialList from "@/components/material";

// crud交由presenter持有
const defaultCrud = CRUD({ title: '卡券表', url: 'api/yxCoupons', sort: 'id,desc', crudMethod: { ...crudYxCoupons }})
const defaultForm = { id: null, couponNum: null, couponName: null, couponType: null, couponCategory: null, denomination: null, discount: null, threshold: null, discountAmount: null, sellingPrice: null, originalPrice: null, settlementPrice: null, commission: null, quantityLimit: null, inventory: null, sales: null, ficti: null, writeOff: null, expireDateStart: null, expireDateEnd: null, isHot: null, isShow: null, outtimeRefund: null, needOrder: null, awaysRefund: null, useCondition: null, availableTimeStart: null, availableTimeEnd: null, delFlag: null, createUserId: null, updateUserId: null, createTime: null, updateTime: null, content: null }
export default {
  name: 'YxCoupons',
  components: { pagination, crudOperation, rrOperation, udOperation ,MaterialList},
  mixins: [presenter(defaultCrud), header(), form(defaultForm), crud()],
  data() {
    return {
      
      permission: {
        add: ['admin', 'yxCoupons:add'],
        edit: ['admin', 'yxCoupons:edit'],
        del: ['admin', 'yxCoupons:del']
      },
      rules: {
        couponNum: [
          { required: true, message: '卡券编号不能为空', trigger: 'blur' }
        ],
        couponName: [
          { required: true, message: '卡券名称不能为空', trigger: 'blur' }
        ],
        couponType: [
          { required: true, message: '卡券类型;1:代金券, 2:折扣券, 3:满减券不能为空', trigger: 'blur' }
        ],
        couponCategory: [
          { required: true, message: '卡券所属分类不能为空', trigger: 'blur' }
        ],
        sellingPrice: [
          { required: true, message: '销售价格不能为空', trigger: 'blur' }
        ],
        originalPrice: [
          { required: true, message: '原价不能为空', trigger: 'blur' }
        ],
        settlementPrice: [
          { required: true, message: '平台结算价不能为空', trigger: 'blur' }
        ],
        commission: [
          { required: true, message: '佣金不能为空', trigger: 'blur' }
        ],
        quantityLimit: [
          { required: true, message: '每人限购数量不能为空', trigger: 'blur' }
        ],
        inventory: [
          { required: true, message: '库存不能为空', trigger: 'blur' }
        ],
        writeOff: [
          { required: true, message: '核销次数不能为空', trigger: 'blur' }
        ],
        expireDateStart: [
          { required: true, message: '有效期始不能为空', trigger: 'blur' }
        ],
        expireDateEnd: [
          { required: true, message: '有效期止不能为空', trigger: 'blur' }
        ],
        useCondition: [
          { required: true, message: '使用条件 描述不能为空', trigger: 'blur' }
        ],
        availableTimeStart: [
          { required: true, message: '可用时间始不能为空', trigger: 'blur' }
        ],
        availableTimeEnd: [
          { required: true, message: '可用时间止不能为空', trigger: 'blur' }
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
        content: [
          { required: true, message: '卡券详情不能为空', trigger: 'blur' }
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
