<template>
  <div class="apply-return">
    <div
      class="goodsStyle acea-row row-between"
      v-for="cart in orderInfo.cartInfo"
      :key="cart.id"
    >
      <div class="pictrue">
        <img :src="cart.productInfo.image" class="image" />
      </div>
      <div class="text acea-row row-between">
        <div class="name line2">{{ cart.productInfo.storeName }}</div>
        <div class="money">
          <div>
            ￥{{
              cart.productInfo.attrInfo
                ? cart.productInfo.attrInfo.price
                : cart.productInfo.price
            }}
          </div>
          <div class="num">x{{ cart.cartNum }}</div>
        </div>
      </div>
    </div>
    <div class="list">
      <div class="item acea-row row-between-wrapper">
        <div>退货件数</div>
        <div class="num">{{ orderInfo.totalNum }}</div>
      </div>
      <div class="item acea-row row-between-wrapper">
        <div>退款金额</div>
        <div class="num">￥{{ orderInfo.payPrice }}</div>
      </div>
      <div class="item acea-row row-between-wrapper">
        <div>退款原因</div>
        <div class="num acea-row row-left">
          <select v-model="reason">
            <option value="">选择退款原因</option>
            <option
              v-for="(text, index) in reasonList"
              :value="text"
              :key="index"
            >
              {{ text }}
            </option>
          </select>
          <span class="iconfont icon-jiantou"></span>
        </div>
      </div>
      <div class="item textarea acea-row row-between">
        <div>备注说明</div>
        <textarea
          placeholder="填写备注信息，100字以内"
          class="num"
          v-model="refund_reason_wap_explain"
        ></textarea>
      </div>
    </div>
    <div class="returnBnt bg-color-red" @click="submit">申请退款</div>
  </div>
</template>

<script>
import { orderDetail, getRefundReason, postOrderRefund } from "@api/order";
import { trim, VUE_APP_API_URL } from "@utils";
import VueCoreImageUpload from "vue-core-image-upload";

export default {
  name: "goodsReturn",
  components: {
    VueCoreImageUpload
  },
  data() {
    return {
      url: `${VUE_APP_API_URL}/upload/image`,
      headers: {
        Authorization: "Bearer " + this.$store.state.app.token
      },
      id: this.$route.params.id || 0,
      orderInfo: {},
      reasonList: [],
      reason: "",
      refund_reason_wap_explain: "",
      refund_reason_wap_img: []
    };
  },
  methods: {
    imageuploaded(res) {
      if (res.status !== 200)
        return this.$dialog.error(res.msg || "上传图片失败");
      this.refund_reason_wap_img.push(res.data.url);
    },
    getOrderDetail() {
      orderDetail(this.id)
        .then(res => {
          this.orderInfo = res.data;
        })
        .catch(err => {
          this.$dialog.error(err.msg || "获取订单失败");
        });
    },
    getRefundReason() {
      getRefundReason().then(res => {
        this.reasonList = res.data;
      });
    },
    submit() {
      const refund_reason_wap_explain = trim(this.refund_reason_wap_explain),
        text = this.reason;
      if (!text) return this.$dialog.toast({ mes: "请选择退款原因" });
      postOrderRefund({
        text,
        uni: this.orderInfo.orderId,
        refund_reason_wap_img: this.refund_reason_wap_img.join(","),
        refund_reason_wap_explain
      })
        .then(res => {
          this.$dialog.success(res.msg);
          this.$router.go(-1);
        })
        .catch(res => {
          this.$dialog.error(res.msg);
        });
    }
  },
  mounted() {
    this.getOrderDetail();
    this.getRefundReason();
  }
};
</script>
