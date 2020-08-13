<template>
  <div class="pos-order-list" ref="container">
    <div class="nav acea-row row-around row-middle">
      <div
        class="item"
        :class="where.status == 0 ? 'on' : ''"
        @click="changeStatus(0)"
      >
        待付款
      </div>
      <div
        class="item"
        :class="where.status == 1 ? 'on' : ''"
        @click="changeStatus(1)"
      >
        待发货
      </div>
      <div
        class="item"
        :class="where.status == 2 ? 'on' : ''"
        @click="changeStatus(2)"
      >
        待收货
      </div>
      <div
        class="item"
        :class="where.status == 3 ? 'on' : ''"
        @click="changeStatus(3)"
      >
        待评价
      </div>
      <div
        class="item"
        :class="where.status == 4 ? 'on' : ''"
        @click="changeStatus(4)"
      >
        已完成
      </div>
      <div
        class="item"
        :class="where.status == -3 ? 'on' : ''"
        @click="changeStatus(-3)"
      >
        退款
      </div>
    </div>
    <div class="list">
      <div class="item" v-for="(item, index) in list" :key="index">
        <div class="order-num acea-row row-middle" @click="toDetail(item)">
          订单号：{{ item.orderId }}
          <span class="time">下单时间：{{ dataFormatF(item.addTime) }}</span>
        </div>
        <div
          class="pos-order-goods"
          v-for="(val, key) in item.cartInfo"
          :key="key"
        >
          <div
            class="goods acea-row row-between-wrapper"
            @click="toDetail(item)"
          >
            <div class="picTxt acea-row row-between-wrapper">
              <div class="pictrue">
                <img :src="val.productInfo.image" />
              </div>
              <div class="text acea-row row-between row-column">
                <div class="info line2">
                  {{ val.productInfo.storeName }}
                </div>
                <div class="attr" v-if="val.productInfo.suk">
                  {{ val.productInfo.suk }}
                </div>
              </div>
            </div>
            <div class="money">
              <div class="x-money">￥{{ val.productInfo.price }}</div>
              <div class="num">x{{ val.cartNum }}</div>
              <div class="y-money">￥{{ val.productInfo.otPrice }}</div>
            </div>
          </div>
        </div>
        <div class="public-total">
          共{{ item.totalNum }}件商品，应支付
          <span class="money">￥{{ item.payPrice }}</span> ( 邮费 ¥{{
            item.totalPostage
          }}
          )
        </div>
        <div class="operation acea-row row-between-wrapper">
          <div class="more"></div>
          <div class="acea-row row-middle">
            <div class="bnt" @click="modify(item, 0)" v-if="where.status == 0">
              一键改价
            </div>
            <div
              class="bnt"
              @click="modify(item, 0)"
              v-if="where.status == -3 && item.refundStatus === 1"
            >
              立即退款
            </div>
            <div
              class="bnt cancel"
              v-if="item.pay_type === 'offline' && item.paid === 0"
              @click="offlinePay(item)"
            >
              确认付款
            </div>
            <router-link
              class="bnt"
              v-if="where.status == 1"
              :to="'/customer/delivery/' + item.orderId"
              >去发货
            </router-link>
          </div>
        </div>
      </div>
    </div>
    <Loading :loaded="loaded" :loading="loading"></Loading>
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
import Loading from "@components/Loading";
import {
  getAdminOrderList,
  setAdminOrderPrice,
  setAdminOrderRemark,
  setOfflinePay,
  setOrderRefund
} from "../../api/admin";
import { required, num } from "@utils/validate";
import { validatorDefaultCatch } from "@utils/dialog";
import { dataFormatF } from "@utils";
export default {
  name: "AdminOrderList",
  components: {
    PriceChange,
    Loading
  },
  props: {},
  data: function() {
    return {
      current: "",
      change: false,
      types: 0,
      where: {
        page: 1,
        limit: 5,
        status: 0
      },
      list: [],
      loaded: false,
      loading: false,
      orderInfo: {},
      status: ""
    };
  },
  watch: {
    "$route.params.types": function(newVal) {
      let that = this;
      if (newVal != undefined) {
        that.where.status = newVal;
        that.init();
      }
    },
    types: function() {
      this.getIndex();
    }
  },
  mounted: function() {
    let that = this;
    that.where.status = that.$route.params.types;
    that.current = "";
    that.getIndex();
    that.$scroll(that.$refs.container, () => {
      !that.loading && that.getIndex();
    });
  },
  methods: {
    dataFormatF,
    more: function(index) {
      if (this.current === index) this.current = "";
      else this.current = index;
    },
    modify: function(item, status) {
      this.change = true;
      this.orderInfo = item;
      this.status = status;
    },
    changeclose: function(msg) {
      this.change = msg;
    },
    async savePrice(opt) {
      // console.log('opt:'+opt)
      let that = this,
        data = {},
        price = opt.price.toString(),
        refund_price = opt.refund_price.toString(),
        refund_status = that.orderInfo.refundStatus,
        remark = opt.remark;
      data.orderId = that.orderInfo.orderId;
      if (that.status == 0 && refund_status === 0) {
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
            that.init();
          },
          function() {
            that.change = false;
            that.$dialog.error("改价失败");
          }
        );
      } else if (that.status == 0 && refund_status === 1) {
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
            that.init();
          },
          err => {
            that.change = false;
            that.$dialog.error(err.msg);
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
            that.init();
          },
          err => {
            that.change = false;
            that.$dialog.error(err.msg);
          }
        );
      }
    },
    init: function() {
      this.list = [];
      this.where.page = 1;
      this.loaded = false;
      this.loading = false;
      this.getIndex();
      this.current = "";
    },
    getIndex: function() {
      let that = this;
      if (that.loading || that.loaded) return;
      that.loading = true;
      getAdminOrderList(that.where).then(
        res => {
          that.loading = false;
          that.loaded = res.data.length < that.where.limit;
          that.list.push.apply(that.list, res.data);
          that.where.page = that.where.page + 1;
        },
        err => {
          that.$dialog.error(err.msg);
        }
      );
    },
    changeStatus: function(val) {
      if (this.where.status != val) {
        this.where.status = val;
        this.init();
      }
    },
    toDetail: function(item) {
      this.$router.push({ path: "/customer/orderdetail/" + item.orderId });
    },
    offlinePay: function(item) {
      console.log(item);
      setOfflinePay({ order_id: item.order_id }).then(
        res => {
          this.$dialog.success(res.msg);
          this.init();
        },
        error => {
          this.$dialog.error(error.msg);
        }
      );
    }
  }
};
</script>
