<template>
  <div class="flash-sale " ref="container">
    <div class="saleBox">
      <div class="header">
        秒杀专区
      </div>
    </div>
    <div class="logoPic">
      <img src="../../assets/images/baokuan.png" />
    </div>
    <Tabs
      class="time-tabs"
      line-height="0"
      v-model="active"
      animated
      title-inactive-color="2"
      sticky
      ref="timeList"
    >
      <Tab v-for="(item, index) in timeList" :key="index">
        <div
          slot="title"
          class="timeItem acea-row row-column-around"
          @click="setTime(index)"
        >
          <span class="time">{{ item.time }}</span>
          <span class="state">{{ item.state }}</span>
        </div>
        <div class="list">
          <div
            class="item acea-row row-between-wrapper"
            v-for="(itemSeckill, indexSeckill) in seckillList"
            :key="indexSeckill"
          >
            <div class="pictrue">
              <img :src="itemSeckill.image" />
            </div>
            <div class="text acea-row row-column-around">
              <div class="line1" v-text="itemSeckill.title"></div>
              <div class="money">
                <span
                  class="num font-color-red"
                  v-text="'￥' + itemSeckill.price"
                ></span>
                <span
                  v-text="'￥' + itemSeckill.otPrice"
                  class="ot_price"
                ></span>
              </div>
              <div class="stock">
                限量<span v-text="itemSeckill.stock + '件'"></span>
              </div>
              <div class="progress cart-color">
                <div
                  class="bg-red"
                  :style="{ width: loading ? itemSeckill.percent + '%' : '' }"
                ></div>
                <div
                  class="piece font-color-red"
                  v-text="itemSeckill.percent + '%'"
                ></div>
              </div>
            </div>
            <div
              class="grab bg-color-red"
              v-if="item.status === 1 && itemSeckill.stock > 0"
              @click="goDetail(itemSeckill.id, item.status)"
            >
              马上抢
            </div>
            <div
              class="grab"
              v-if="item.status === 1 && itemSeckill.stock <= 0"
              @click="goDetail(itemSeckill.id, item.status)"
            >
              已售罄
            </div>
            <div
              class="grab bg-color-red"
              v-if="item.status === 2"
              @click="goDetail(itemSeckill.id, item.status)"
            >
              即将开始
            </div>
            <div
              class="grab bg-color-red"
              v-if="item.status === 0"
              @click="goDetail(itemSeckill.id, item.status)"
            >
              已结束
            </div>
          </div>
        </div>
        <div
          class="noCommodity"
          v-cloak
          v-if="seckillList.length === 0 && page > 1"
        >
          <div class="noPictrue">
            <img src="@assets/images/noGood.png" class="image" />
          </div>
        </div>
        <Loading
          :loaded="status"
          :loading="loadingList"
          v-if="seckillList.length > 0"
        ></Loading>
      </Tab>
    </Tabs>
  </div>
</template>
<script>
import { getSeckillConfig, getSeckillList } from "@api/activity";
import { Tab, Tabs } from "vant";
import Loading from "@components/Loading";

export default {
  name: "GoodsSeckill",
  components: {
    Tab,
    Tabs,
    Loading
  },
  props: {},
  data: function() {
    return {
      timeList: [],
      sticky: false,
      loading: false,
      datatime: 0,
      active: 0,
      seckillList: [],
      status: false,
      loadingList: false,
      page: 1, //页码
      limit: 5 //数量
    };
  },
  mounted: function() {
    this.mountedStart();
  },
  methods: {
    mountedStart: function() {
      var that = this;
      getSeckillConfig().then(res => {
        // that.$set(that, "headerImg", res.data.lovely);
        that.$set(that, "timeList", res.data.seckillTime);
        that.$set(that, "active", res.data.seckillTimeIndex);
        that.datatime = that.timeList[that.active].stop;
        that.getSeckillList();
        that.$nextTick(function() {
          that.sticky = true;
          that.$refs.timeList.scrollIntoView();
        });
      });
      this.$scroll(this.$refs.container, () => {
        !this.loadingList && this.getSeckillList();
      });
      setTimeout(function() {
        that.loading = true;
      }, 500);
    },
    setTime: function(index) {
      var that = this;
      that.active = index;
      that.datatime = that.timeList[that.active].stop;
      that.seckillList = [];
      that.page = 1;
      that.status = false;
      that.getSeckillList();
    },
    getSeckillList: function() {
      var that = this;
      if (that.loadingList) return;
      if (that.status) return;
      that.loadingList = true;
      var time = that.timeList[that.active].id;
      getSeckillList(time, { page: that.page, limit: that.limit })
        .then(res => {
          that.status = res.data.length < that.limit;
          that.seckillList.push.apply(that.seckillList, res.data);
          that.page++;
          that.loadingList = false;
        })
        .catch(() => {
          that.loadingList = false;
        });
    },
    goDetail: function(id, status) {
      var that = this;
      var time = that.timeList[that.active].stop;
      this.$router.push({
        path: "/activity/seckill_detail/" + id + "/" + time + "/" + status
      });
    }
  }
};
</script>
<style scoped>
.cart-color {
  border: none !important;
}
.saleBox {
  width: 100%;
  height: 1.04rem;
  background: #00c17b;
  opacity: 1;
  -webkit-box-sizing: border-box;
  box-sizing: border-box;
  text-align: center;
  line-height: 1.04rem;
}
.header {
  width: 100%;
  box-sizing: border-box;
  border-radius: 0.2rem 0.2rem 0 0;
  overflow: hidden;
  color: #fff;
  font-size: 0.5rem;
}

.timeItem {
  font-size: 0.22rem;
  color: #333;
  width: 100%;
  text-align: center;
  background-color: #f5f5f5;
  padding: 0.21rem 0 0.28rem 0;
}
.flash-sale {
  height: 100%;
  background: #f5f5f5 !important;
}
.time-tabs {
  background: #f5f5f5;
  top: 0.1rem;
}
.time-tabs.van-tabs--line {
  padding: 0;
  background: #f5f5f5;
}
.timeItem .time {
  font-size: 0.36rem;
  font-weight: bold;
  height: 0.5rem;
  line-height: 0.5rem;
}
.timeItem .state {
  height: 0.3rem;
  line-height: 0.3rem;
  font-size: 0.2rem;
  width: auto;
  margin: auto;
}
.activity {
  color: #333;
}
.flash-sale .list .item .grab {
  background-color: #999;
}
.logoPic {
  width: 0.75rem;
  height: 0.7rem;
  position: absolute;
  top: 1.39rem;
  z-index: 9999;
  left: 3.5%;
}
.logoPic img {
  width: 100%;
  height: 100%;
}
.list {
  padding: 0 0.2rem;
}
.stock {
  color: #999999;
  font-size: 0.22rem;
  margin-bottom: 0.08rem;
}
.ot_price {
  text-decoration: line-through;
  color: #999999;
  font-size: 0.24rem;
}
</style>
