<template>
  <div class="my-order" ref="container">
    <div class="header bg-color-red">
      <div class="picTxt acea-row row-between-wrapper">
        <div class="text">
          <div class="name">订单信息</div>
          <div>
            累计订单：{{ orderData.orderCount || 0 }} 总消费：￥{{
              orderData.sumPrice || 0
            }}
          </div>
        </div>
      </div>
    </div>
    <div class="nav acea-row row-around">
      <div
        class="item"
        :class="{ on: type === 0 }"
        @click="$router.replace({ path: '/order/list/0' })"
      >
        <div>待付款</div>
        <div class="num">{{ orderData.unpaidCount || 0 }}</div>
      </div>
      <div
        class="item"
        :class="{ on: type === 1 }"
        @click="$router.replace({ path: '/order/list/1' })"
      >
        <div>待发货</div>
        <div class="num">{{ orderData.unshippedCount || 0 }}</div>
      </div>
      <div
        class="item"
        :class="{ on: type === 2 }"
        @click="$router.replace({ path: '/order/list/2' })"
      >
        <div>待收货</div>
        <div class="num">{{ orderData.receivedCount || 0 }}</div>
      </div>
      <div
        class="item"
        :class="{ on: type === 3 }"
        @click="$router.replace({ path: '/order/list/3' })"
      >
        <div>待评价</div>
        <div class="num">{{ orderData.evaluatedCount || 0 }}</div>
      </div>
      <div
        class="item"
        :class="{ on: type === 4 }"
        @click="$router.replace({ path: '/order/list/4' })"
      >
        <div>已完成</div>
        <div class="num">{{ orderData.completeCount || 0 }}</div>
      </div>
    </div>
    <div class="list">
      <div class="item" v-for="order in orderList" :key="order.id">
        <div class="title acea-row row-between-wrapper">
          <div class="acea-row row-middle">
            <span
              class="sign cart-color acea-row row-center-wrapper"
              v-if="order.combinationId > 0"
              >拼团</span
            ><span
              class="sign cart-color acea-row row-center-wrapper"
              v-if="order.seckillId > 0"
              >秒杀</span
            ><span
              class="sign cart-color acea-row row-center-wrapper"
              v-if="order.bargainId > 0"
              >砍价</span
            >
            <span
              class="sign cart-color acea-row row-center-wrapper"
              v-if="order.storeId > 0"
              >门店</span
            >

            {{ dataFormat(order.addTime) }}
          </div>
          <div class="font-color-red">{{ getStatus(order) }}</div>
        </div>
        <div @click="$router.push({ path: '/order/detail/' + order.orderId })">
          <div
            class="item-info acea-row row-between row-top"
            v-for="cart in order.cartInfo"
            :key="cart.id"
          >
            <div class="pictrue">
              <img
                :src="cart.productInfo.image"
                @click.stop="
                  $router.push({ path: '/detail/' + cart.productInfo.id })
                "
                v-if="
                  cart.combinationId === 0 &&
                    cart.bargainId === 0 &&
                    cart.seckillId === 0
                "
              />
              <img
                :src="cart.productInfo.image"
                @click.stop="
                  $router.push({
                    path: '/activity/group_detail/' + cart.combinationId
                  })
                "
                v-else-if="cart.combinationId > 0"
              />
              <img
                :src="cart.productInfo.image"
                @click.stop="
                  $router.push({
                    path: '/activity/dargain_detail/' + cart.bargainId
                  })
                "
                v-else-if="cart.bargainId > 0"
              />
              <img
                :src="cart.productInfo.image"
                @click.stop="
                  $router.push({
                    path: '/activity/seckill_detail/' + cart.seckillId
                  })
                "
                v-else-if="cart.seckillId > 0"
              />
            </div>
            <div class="text acea-row row-between">
              <div class="name line2">
                {{ cart.productInfo.storeName }}
              </div>
              <div class="money">
                <div>
                  ￥{{
                    cart.productInfo.attrInfo
                      ? cart.productInfo.attrInfo.price
                      : cart.productInfo.price
                  }}
                </div>
                <div>x{{ cart.cartNum }}</div>
              </div>
            </div>
          </div>
        </div>
        <div class="totalPrice">
          共{{ order.cartInfo.length || 0 }}件商品，总金额
          <span class="money font-color-red">￥{{ order.payPrice }}</span>
        </div>
        <div class="bottom acea-row row-right row-middle">
          <template v-if="order._status._type == 0">
            <div class="bnt cancelBnt" @click="cancelOrder(order)">
              取消订单
            </div>
            <div
              class="bnt bg-color-red"
              @click="$router.push({ path: '/order/detail/' + order.orderId })"
            >
              立即付款
            </div>
          </template>
          <template v-if="order._status._type == 1 || order._status._type == 9">
            <div
              class="bnt bg-color-red"
              @click="$router.push({ path: '/order/detail/' + order.orderId })"
            >
              查看详情
            </div>
          </template>
          <template v-if="order._status._type == 2">
            <div
              class="bnt default"
              @click="
                $router.push({ path: '/order/logistics/' + order.orderId })
              "
            >
              查看物流
            </div>
            <div class="bnt bg-color-red" @click="takeOrder(order)">
              确认收货
            </div>
          </template>
          <template v-if="order._status._type == 3">
            <div
              class="bnt bg-color-red"
              @click="$router.push({ path: '/order/detail/' + order.orderId })"
            >
              去评价
            </div>
          </template>
          <template v-if="order._status._type === 4">
            <div
              class="bnt bg-color-red"
              @click="$router.push({ path: '/order/detail/' + order.orderId })"
            >
              查看订单
            </div>
          </template>
        </div>
      </div>
    </div>
    <div class="noCart" v-if="orderList.length === 0 && page > 1">
      <div class="pictrue"><img src="@assets/images/noOrder.png" /></div>
    </div>
    <Loading :loaded="loaded" :loading="loading"></Loading>
    <Payment
      v-model="pay"
      :types="payType"
      @checked="toPay"
      :balance="userInfo.nowMoney"
    ></Payment>
  </div>
</template>
<script>
import { getOrderData, getOrderList } from "@api/order";
import {
  cancelOrderHandle,
  payOrderHandle,
  takeOrderHandle
} from "@libs/order";
import Loading from "@components/Loading";
import Payment from "@components/Payment";
import { mapGetters } from "vuex";
import { isWeixin, dataFormat } from "@utils";

const STATUS = [
  "待付款",
  "待发货",
  "待收货",
  "待评价",
  "已完成",
  "",
  "",
  "",
  "",
  "待付款"
];

const NAME = "MyOrder";

export default {
  name: NAME,
  data() {
    return {
      offlinePayStatus: 2,
      orderData: {},
      type: parseInt(this.$route.params.type) || 0,
      page: 1,
      limit: 20,
      loaded: false,
      loading: false,
      orderList: [],
      pay: false,
      payType: ["yue", "weixin"],
      from: isWeixin() ? "weixin" : "weixinh5"
    };
  },
  components: {
    Loading,
    Payment
  },
  computed: mapGetters(["userInfo"]),
  watch: {
    $route(n) {
      if (n.name === NAME) {
        const type = parseInt(this.$route.params.type) || 0;
        if (this.type !== type) {
          this.changeType(type);
        }
        this.getOrderData();
      }
    }
  },
  methods: {
    dataFormat,
    setOfflinePayStatus: function(status) {
      var that = this;
      that.offlinePayStatus = status;
      if (status === 1) {
        if (that.payType.indexOf("offline") < 0) {
          that.payType.push("offline");
        }
      }
    },
    getOrderData() {
      getOrderData().then(res => {
        this.orderData = res.data;
      });
    },
    takeOrder(order) {
      takeOrderHandle(order.orderId).finally(() => {
        this.reload();
        this.getOrderData();
      });
    },
    reload() {
      this.changeType(this.type);
    },
    changeType(type) {
      this.type = type;
      this.orderList = [];
      this.page = 1;
      this.loaded = false;
      this.loading = false;
      this.getOrderList();
    },
    getOrderList() {
      if (this.loading || this.loaded) return;
      this.loading = true;
      const { page, limit, type } = this;
      getOrderList({
        page,
        limit,
        type
      }).then(res => {
        this.orderList = this.orderList.concat(res.data);
        this.page++;
        this.loaded = res.data.length < this.limit;
        this.loading = false;
      });
    },
    getStatus(order) {
      return STATUS[order._status._type];
    },
    cancelOrder(order) {
      cancelOrderHandle(order.orderId)
        .then(() => {
          this.orderList.splice(this.orderList.indexOf(order), 1);
        })
        .catch(() => {
          this.reload();
        });
    },
    paymentTap: function(order) {
      var that = this;
      if (
        !(order.combinationId > 0 || order.bargainId > 0 || order.seckillId > 0)
      ) {
        that.setOfflinePayStatus(order.offlinePayStatus);
      }
      this.pay = true;
      this.toPay = type => {
        payOrderHandle(order.orderId, type, that.from)
          .then(() => {
            const type = parseInt(this.$route.params.type) || 0;
            that.changeType(type);
            that.getOrderData();
          })
          .catch(() => {
            const type = parseInt(that.$route.params.type) || 0;
            that.changeType(type);
            that.getOrderData();
          });
      };
    },
    toPay() {}
  },
  mounted() {
    this.getOrderData();
    this.getOrderList();
    this.$scroll(this.$refs.container, () => {
      !this.loading && this.getOrderList();
    });
  }
};
</script>

<style scoped>
.noCart {
  margin-top: 0.17rem;
  padding-top: 0.1rem;
}

.noCart .pictrue {
  width: 4rem;
  height: 3rem;
  margin: 0.7rem auto 0.5rem auto;
}

.noCart .pictrue img {
  width: 100%;
  height: 100%;
}
</style>
