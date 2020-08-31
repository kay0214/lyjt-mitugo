<template>
  <el-dialog :append-to-body="true" :close-on-click-modal="false" :before-close="cancel" :visible.sync="dialog" :title="isAdd ? '新增' : '订单详情'" width="700px">
    <!-- <el-card>
      <div slot="header">
        <span>收货信息</span>
      </div>
      <div class="text item">用户昵称:{{ form.nickname }}</div>
      <div class="text item">收货人: {{ form.realName }}</div>
      <div class="text item">联系电话: {{ form.userPhone }}</div>
      <div class="text item">收货地址: {{ form.userAddress }}</div>
    </el-card> -->
    <el-card>
      <div slot="header">
        <span>订单信息</span>
      </div>
      <el-row :gutter="24">
        <el-col :span="12">
          <div class="text item">订单编号: {{ form.orderId }}</div>
          <div class="text item">商品总数: {{ form.totalNum }}</div>
          <div class="text item">佣金: {{ form.commission }}</div>
          <div class="text item">实际支付: {{ form.totalPrice }}</div>
          <div class="text item">支付方式: {{ form.payType }}</div>
        </el-col>
        <el-col :span="12">
          <div class="text item">订单状态: {{ form.status }}</div>
          <div class="text item">商品总价: {{ form.totalPrice }}</div>
          <div class="text item"></div>
          <div class="text item">创建时间: {{ parseTime(form.addTime) }}</div>
          <div class="text item">支付时间: {{ parseTime(form.payTime) }}</div>
        </el-col>
      </el-row>
    </el-card>
    <el-card v-if="form.storeId == 0">
      <div slot="header">
        <span>核销信息</span>
      </div>
      <el-row>
        <div class="text item">核销门店:{{ form.couponOrderUseList.storeName }}</div>
        <div class="text item">核销门店id:{{ form.couponOrderUseList.storeId  }}</div>
        <div class="text item">核销次数:{{ form.couponOrderUseList.usedCount  }}</div>
        <!-- <div class="text item">核销门店电话:{{ form.deliveryId }}</div> -->
      </el-row>
    </el-card>
    <el-card>
      <div slot="header">
        <span>备注信息</span>
      </div>
      <div class="text item">{{ form.remark }}</div>
    </el-card>
  </el-dialog>
</template>

<script>
import { parseTime } from '@/utils/index'
export default {
  props: {
    isAdd: {
      type: Boolean,
      required: true
    }
  },
  data() {
    return {
      loading: false, dialog: false, expressInfo: [],
      orderStatusList:[ //顺序不能变，value和index需要对应关系
        { value: '0', label: '待支付' },
        { value: '1', label: '已过期' },
        { value: '2', label: '待发放' },
        { value: '3', label: '支付失败' },
        { value: '4', label: '待使用' },
        { value: '5', label: '已使用' },
        { value: '6', label: '已核销' },
        { value: '7', label: '退款中' },
        { value: '8', label: '已退款' },
        { value: '9', label: '退款驳回'},
      ],  
      form: {
        orderId: '',
        totalNum: '',
        commission: '',
        totalPrice: '',
        payType: '',
        status: '',
        addTime: '',
        payTime: '',
        couponOrderUseList: '',
      },
      rules: {
      }
    }
  },
  methods: {
    parseTime,
    cancel() {
      this.dialog = false
    },
    getStatus(){

    }
  }
}
</script>

<style scoped>
  .text {
    font-size: 12px;
  }

  .item {
    padding: 6px 0;
  }

</style>
