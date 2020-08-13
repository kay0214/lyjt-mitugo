<template>
  <div
    :class="[posterImageStatus ? 'noscroll product-con' : 'product-con']"
    v-show="domStatus"
  >
    <ProductConSwiper :imgUrls="imgUrls"></ProductConSwiper>
    <div class="nav acea-row row-between-wrapper">
      <div class="money">
        ￥<span class="num" v-text="storeInfo.price"></span
        ><span class="y-money" v-text="'￥' + storeInfo.otPrice"></span>
      </div>
      <div class="acea-row row-middle">
        <div class="times">
          <div>距秒杀结束仅剩</div>
          <CountDown
            :is-day="false"
            :tip-text="''"
            :day-text="''"
            :hour-text="' : '"
            :minute-text="' : '"
            :second-text="''"
            :datatime="datatime"
          ></CountDown>
        </div>
        <div class="iconfont icon-jiantou"></div>
      </div>
    </div>
    <div class="wrapperRush">
      <div class="introduce acea-row row-between">
        <div class="infor" v-text="storeInfo.title"></div>
      </div>
      <div class="label acea-row row-middle">
        <div class="stock" v-text="'库存:' + storeInfo.stock + '件'"></div>
        <div v-text="'销量:' + storeInfo.sales + '件'"></div>
      </div>
    </div>
    <div class="product-intro">
      <div class="title">产品介绍</div>
      <div class="conter" v-html="storeInfo.description"></div>
    </div>
    <div style="height:1.2rem;"></div>
    <div class="footerRush acea-row row-between-wrapper">
      <div
        class="item acea-row row-center-wrapper row-column"
        @click="setCollect"
      >
        <div
          class="iconfont"
          :class="userCollect ? 'icon-shoucang1' : 'icon-shoucang'"
        ></div>
        <div>收藏</div>
      </div>
      <div
        class="bnt acea-row"
        v-if="
          $route.params.status === '1' &&
            storeInfo.num > 0 &&
            storeInfo.stock > 0
        "
      >
        <div class="joinCart" @click="openAlone">原价购买</div>
        <div class="buy" @click="tapBuy">立即抢购</div>
      </div>
      <div
        class="bnt acea-row"
        v-if="
          $route.params.status === '1' &&
            storeInfo.num <= 0 &&
            storeInfo.stock <= 0
        "
      >
        <div class="joinCart" @click="openAlone">原价购买</div>
        <div class="buy bg-color-hui">已售罄</div>
      </div>
      <div class="bnt acea-row" v-if="$route.params.status === '2'">
        <div class="joinCart" @click="openAlone">原价购买</div>
        <div class="buy bg-color-hui">即将开始</div>
      </div>
      <div class="bnt acea-row" v-if="$route.params.status === '0'">
        <div class="joinCart" @click="openAlone">原价购买</div>
        <div class="buy bg-color-hui">已结束</div>
      </div>
    </div>
    <ProductWindow v-on:changeFun="changeFun" :attr="attr"></ProductWindow>
  </div>
</template>
<style scoped>
.noscroll {
  height: 100%;
  overflow: hidden;
}
</style>
<script>
import ProductConSwiper from "@components/ProductConSwiper";
import CountDown from "@components/CountDown";
import ProductWindow from "@components/ProductWindow";
import StorePoster from "@components/StorePoster";
import { getSeckillDetail } from "@api/activity";
import { postCartAdd } from "@api/store";
import { getCollectAdd, getCollectDel } from "@api/user";
const NAME = "SeckillDetails";

export default {
  name: "SeckillDetails",
  components: {
    ProductConSwiper,
    CountDown,
    ProductWindow,
    StorePoster
  },
  props: {},
  data: function() {
    return {
      domStatus: false,
      posterImageStatus: false,
      action: "",
      imgUrls: [],
      storeInfo: [],
      replyCount: 0,
      reply: [],
      cartNum: 1,
      attr: {
        cartAttr: false,
        productSelect: {
          image: "",
          store_name: "",
          price: "",
          stock: "",
          unique: "",
          cart_num: 1
        }
      },
      datatime: 0,
      userCollect: false
    };
  },
  watch: {
    $route: function(n) {
      var that = this;
      console.log(n);
      if (n.name === NAME) {
        that.mountedStart();
      }
    }
  },
  mounted: function() {
    this.mountedStart();
  },
  methods: {
    //收藏商品
    setCollect: function() {
      let that = this,
        id = that.storeInfo.productId,
        category = "product";
      if (that.userCollect) {
        getCollectDel(id, category).then(function() {
          that.userCollect = !that.userCollect;
        });
      } else {
        getCollectAdd(id, category).then(function() {
          that.userCollect = !that.userCollect;
        });
      }
    },
    openAlone: function() {
      this.$router.push({ path: "/detail/" + this.storeInfo.productId });
    },
    mountedStart: function() {
      var that = this;
      let id = that.$route.params.id;
      that.datatime = parseInt(that.$route.params.time);
      getSeckillDetail(id).then(res => {
        that.$set(that, "storeInfo", res.data.storeInfo);
        that.$set(that, "imgUrls", res.data.storeInfo.sliderImageArr);
        that.$set(that, "replyCount", res.data.replyCount);
        that.$set(that, "reply", res.data.reply);
        that.$set(that, "userCollect", res.data.userCollect);
        that.updateTitle();
        that.setProductSelect();
        that.domStatus = true;
      });
    },
    updateTitle() {
      document.title = this.storeInfo.title || this.$route.meta.title;
    },
    setPosterImageStatus: function() {
      var sTop = document.body || document.documentElement;
      sTop.scrollTop = 0;
      this.posterImageStatus = !this.posterImageStatus;
    },
    //将父级向子集多次传送的函数合二为一；
    changeFun: function(opt) {
      if (typeof opt !== "object") opt = {};
      let action = opt.action || "";
      let value = opt.value === undefined ? "" : opt.value;
      this[action] && this[action](value);
    },
    changeattr: function(res) {
      var that = this;
      that.attr.cartAttr = res;
    },
    ChangeCartNum: function(res) {
      var that = this;
      if (res) {
        if (that.attr.productSelect.cart_num < that.storeInfo.stock) {
          that.attr.productSelect.cart_num++;
        }
      } else {
        if (that.attr.productSelect.cart_num > 1) {
          that.attr.productSelect.cart_num--;
        }
      }
    },
    setProductSelect: function() {
      var that = this;
      var attr = that.attr;
      attr.productSelect.image = that.storeInfo.image;
      attr.productSelect.store_name = that.storeInfo.title;
      attr.productSelect.price = that.storeInfo.price;
      attr.productSelect.stock = that.storeInfo.stock;
      attr.cartAttr = false;
      that.$set(that, "attr", attr);
    },
    selecAttrTap: function() {
      this.cartAttr = true;
    },
    tapBuy: function() {
      var that = this;
      if (that.attr.cartAttr == false) {
        that.attr.cartAttr = !this.attr.attrcartAttr;
      } else {
        var data = {};
        data.productId = that.storeInfo.productId;
        data.cartNum = that.attr.productSelect.cart_num;
        data.uniqueId = that.attr.productSelect.unique;
        data.secKillId = that.storeInfo.id;
        data.new = 1;
        postCartAdd(data)
          .then(res => {
            that.$router.push({
              path: "/order/submit/" + res.data.cartId
            });
          })
          .catch(res => {
            console.log(res);
            this.$dialog.error(res.msg);
          });
      }
    }
  }
};
</script>
<style scoped>
.product-con .nav {
  padding: 0 0.2rem;
}
.bg-color-hui {
  background: #bbbbbb !important;
  line-height: 0.76rem;
  text-align: center;
}
.product-con .bnt {
  width: 90%;
  height: 0.76rem;
  color: #fff;
  font-size: 0.28rem;
}
.product-con .bnt > div {
  width: 50%;
  text-align: center;
  line-height: 0.76rem;
}
.product-con .bnt .joinCart {
  border-radius: 0.5rem 0 0 0.5rem;
  background-image: linear-gradient(to right, #fea10f 0%, #fa8013 100%);
  background-image: -webkit-linear-gradient(to right, #fea10f 0%, #fa8013 100%);
  background-image: -moz-linear-gradient(to right, #fea10f 0%, #fa8013 100%);
}
.product-con .bnt .buy {
  border-radius: 0 0.5rem 0.5rem 0;
  background-image: linear-gradient(to right, #00c17b 0%, #00c17b 100%);
  background-image: -webkit-linear-gradient(to right, #00c17b 0%, #00c17b 100%);
  background-image: -moz-linear-gradient(to right, #00c17b 0%, #00c17b 100%);
}

.icon-shoucang1 {
  color: #e93323;
}
</style>
