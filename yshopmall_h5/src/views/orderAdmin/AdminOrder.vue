<template>
  <div class="order-details pos-order-details">
    <div class="header acea-row row-middle">
      <div class="state">{{ title }}</div>
      <div class="data">
        <div class="order-num">订单：{{ orderInfo.orderId }}</div>
        <div>
          <span class="time">{{ dataFormat(orderInfo.addTime) }}</span>
        </div>
      </div>
    </div>
    <div class="orderingUser acea-row row-middle">
      {{ orderInfo.nickname }}
    </div>
    <div class="address">
      <div class="name">
        {{ orderInfo.realName
        }}<span class="phone">{{ orderInfo.userPhone }}</span>
      </div>
      <div>{{ orderInfo.userAddress }}</div>
    </div>
    <div class="line"><img src="@assets/images/line.jpg" /></div>
    <div class="pos-order-goods">
      <div
        class="goods acea-row row-between-wrapper"
        v-for="(item, index) in orderInfo.cartInfo"
        :key="index"
      >
        <div class="picTxt acea-row row-between-wrapper">
          <div class="pictrue">
            <img :src="item.productInfo.image" />
          </div>
          <div class="text acea-row row-between row-column">
            <div class="info line2">
              {{ item.productInfo.storeName }}
            </div>
            <div class="attr">{{ item.productInfo.suk }}</div>
          </div>
        </div>
        <div class="money">
          <div class="x-money">￥{{ item.productInfo.price }}</div>
          <div class="num">x{{ item.cartNum }}</div>
          <div class="y-money">
            ￥{{ item.productInfo.otPrice ? item.productInfo.otPrice : 0 }}
          </div>
        </div>
      </div>
    </div>
    <div class="public-total">
      共{{ orderInfo.totalNum }}件商品，应支付
      <span class="money">￥{{ orderInfo.payPrice }}</span> ( 邮费 ¥{{
        orderInfo.payPostage
      }}
      )
    </div>
    <div class="wrapper">
      <div class="item acea-row row-between">
        <div>订单编号：</div>
        <div class="conter acea-row row-middle row-right">
          {{ orderInfo.orderId
          }}<span
            class="copy copy-data"
            :data-clipboard-text="orderInfo.orderId"
            >复制</span
          >
        </div>
      </div>
      <div class="item acea-row row-between">
        <div>下单时间：</div>
        <div class="conter">{{ dataFormat(orderInfo.addTime) }}</div>
      </div>
      <div class="item acea-row row-between">
        <div>支付状态：</div>
        <div class="conter">
          {{ orderInfo.paid == 1 ? "已支付" : "未支付" }}
        </div>
      </div>
      <div class="item acea-row row-between">
        <div>支付方式：</div>
        <div class="conter">{{ payType }}</div>
      </div>
      <div class="item acea-row row-between">
        <div>买家留言：</div>
        <div class="conter">{{ orderInfo.mark }}</div>
      </div>
    </div>
    <div class="wrapper">
      <div class="item acea-row row-between">
        <div>支付金额：</div>
        <div class="conter">￥{{ orderInfo.totalPrice }}</div>
      </div>
      <div class="item acea-row row-between">
        <div>优惠券抵扣：</div>
        <div class="conter">-￥{{ orderInfo.couponPrice }}</div>
      </div>
      <div class="actualPay acea-row row-right">
        实付款：<span class="money font-color-red"
          >￥{{ orderInfo.payPrice }}</span
        >
      </div>
    </div>
    <div
      class="wrapper"
      v-if="
        orderInfo.delivery_type != 'fictitious' && orderInfo._status._type === 2
      "
    >
      <div class="item acea-row row-between">
        <div>配送方式：</div>
        <div class="conter" v-if="orderInfo.delivery_type === 'express'">
          快递
        </div>
      </div>
      <div class="item acea-row row-between">
        <div v-if="orderInfo.delivery_type === 'express'">快递公司：</div>
        <div class="conter">{{ orderInfo.delivery_name }}</div>
      </div>
      <div class="item acea-row row-between">
        <div v-if="orderInfo.delivery_type === 'express'">快递单号：</div>
        <div class="conter">
          {{ orderInfo.delivery_id
          }}<span
            class="copy copy-data"
            :data-clipboard-text="orderInfo.delivery_id"
            >复制</span
          >
        </div>
      </div>
    </div>
    <div style="height:1.2rem;"></div>
    <div class="footer acea-row row-right row-middle">
      <div class="more"></div>
      <div class="bnt cancel" @click="modify(0)" v-if="types == 0">
        一键改价
      </div>
      <div class="bnt cancel" @click="modify(0)" v-if="types == -1">
        立即退款
      </div>
      <div
        class="bnt cancel"
        v-if="orderInfo.pay_type === 'offline' && orderInfo.paid === 0"
        @click="offlinePay"
      >
        确认付款
      </div>
      <router-link
        class="bnt delivery"
        v-if="types == 1"
        :to="'/customer/delivery/' + orderInfo.orderId"
        >去发货</router-link
      >
    </div>
    <PriceChange
      :change="change"
      :orderInfo="orderInfo"
      v-on:closechange="changeclose($event)"
      v-on:savePrice="savePrice"
      :status="status"
    ></PriceChange>
  </div>
</template>
<script>
import PriceChange from "@components/PriceChange";
import ClipboardJS from "clipboard";
import {
  getAdminOrderDetail,
  setAdminOrderPrice,
  setAdminOrderRemark,
  setOfflinePay,
  setOrderRefund
} from "../../api/admin";
import { required, num } from "@utils/validate";
import { validatorDefaultCatch } from "@utils/dialog";
import { dataFormat } from "@utils";
export default {
  name: "AdminOrder",
  components: {
    PriceChange
  },
  props: {},
  data: function() {
    return {
      order: false,
      change: false,
      order_id: "",
      orderInfo: {
        _status: {}
      },
      status: "",
      title: "",
      payType: "",
      types: ""
    };
  },
  watch: {
    "$route.params.oid": function(newVal) {
      let that = this;
      if (newVal != undefined) {
        that.order_id = newVal;
        that.getIndex();
      }
    }
  },
  mounted: function() {
    this.order_id = this.$route.params.oid;
    this.getIndex();
    this.$nextTick(function() {
      var copybtn = document.getElementsByClassName("copy-data");
      const clipboard = new ClipboardJS(copybtn);
      clipboard.on("success", () => {
        this.$dialog.success("复制成功");
      });
    });
  },
  methods: {
    dataFormat,
    more: function() {
      this.order = !this.order;
    },
    modify: function(status) {
      this.change = true;
      this.status = status;
    },
    changeclose: function(msg) {
      this.change = msg;
    },
    getIndex: function() {
      let that = this;
      getAdminOrderDetail(that.order_id).then(
        res => {
          that.orderInfo = res.data;
          that.types = res.data._status._type;
          that.title = res.data._status._title;
          that.payType = res.data._status._payType;
        },
        err => {
          that.$dialog.error(err.msg);
        }
      );
    },
    async savePrice(opt) {
      let that = this,
        data = {},
        price = opt.price,
        remark = opt.remark,
        refund_price = opt.refund_price.toString();
      data.orderId = that.orderInfo.orderId;
      if (that.status == 0 && that.orderInfo.refundStatus === 0) {
        try {
          await this.$validator({
            price: [
              required(required.message("金额")),
              num(num.message("金额"))
            ]
          }).validate({ price });
        } catch (e) {
          return validatorDefaultCatch(e);
        }
        data.price = price;
        setAdminOrderPrice(data).then(
          function() {
            that.change = false;
            that.$dialog.success("改价成功");
            that.getIndex();
          },
          function() {
            that.change = false;
            that.$dialog.error("改价失败");
          }
        );
      } else if (that.status == 0 && that.orderInfo.refundStatus === 1) {
        try {
          await this.$validator({
            refund_price: [
              required(required.message("金额")),
              num(num.message("金额"))
            ]
          }).validate({ refund_price });
        } catch (e) {
          return validatorDefaultCatch(e);
        }
        data.price = refund_price;
        data.type = opt.type;
        setOrderRefund(data).then(
          res => {
            that.change = false;
            that.$dialog.success(res.msg);
            that.getIndex();
          },
          err => {
            that.change = false;
            that.$dialog.error(err.msg);
            that.getIndex();
          }
        );
      } else {
        try {
          await this.$validator({
            remark: [required(required.message("备注"))]
          }).validate({ remark });
        } catch (e) {
          return validatorDefaultCatch(e);
        }
        data.remark = remark;
        setAdminOrderRemark(data).then(
          res => {
            that.change = false;
            that.$dialog.success(res.msg);
            that.getIndex();
          },
          err => {
            that.change = false;
            that.$dialog.error(err.msg);
          }
        );
      }
    },
    offlinePay: function() {
      setOfflinePay({ order_id: this.orderInfo.order_id }).then(
        res => {
          this.$dialog.success(res.msg);
          this.getIndex();
        },
        err => {
          this.$dialog.error(err.msg);
        }
      );
    }
  }
};
</script>
