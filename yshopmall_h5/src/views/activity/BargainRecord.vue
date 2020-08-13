<template>
  <div class="bargain-record" ref="container">
    <div class="item" v-for="(item, index) in bargain" :key="index">
      <div class="picTxt acea-row row-between-wrapper">
        <div class="pictrue"><img :src="item.image" /></div>
        <div class="text acea-row row-column-around">
          <div class="line1">{{ item.title }}</div>
          <count-down
            :is-day="true"
            :tip-text="'倒计时 '"
            :day-text="' 天 '"
            :hour-text="' 时 '"
            :minute-text="' 分 '"
            :second-text="' 秒'"
            :datatime="item.datatime"
          ></count-down>
          <div class="money font-color-red">
            已砍至<span class="symbol">￥</span
            ><span class="num">{{ item.residuePrice }}</span>
          </div>
        </div>
      </div>
      <div class="bottom acea-row row-between-wrapper">
        <div class="purple" v-if="item.status === 1">活动进行中</div>
        <div class="success" v-else-if="item.status === 3">砍价成功</div>
        <div class="end" v-else>活动已结束</div>
        <div class="acea-row row-middle row-right">
          <div
            class="bnt cancel"
            v-if="item.status === 1"
            @click="getBargainUserCancel(item.bargainId)"
          >
            取消活动
          </div>
          <div
            class="bnt bg-color-red"
            v-if="item.status === 1"
            @click="goDetail(item.bargainId)"
          >
            继续砍价
          </div>
          <div class="bnt bg-color-red" v-else @click="goList">重开一个</div>
        </div>
      </div>
    </div>
    <Loading :loaded="status" :loading="loadingList"></Loading>
  </div>
</template>
<script>
import CountDown from "@components/CountDown";
import { getBargainUserList, getBargainUserCancel } from "@api/activity";
import Loading from "@components/Loading";

export default {
  name: "BargainRecord",
  components: {
    CountDown,
    Loading
  },
  props: {},
  data: function() {
    return {
      bargain: [],
      status: false, //砍价列表是否获取完成 false 未完成 true 完成
      loadingList: false, //当前接口是否请求完成 false 完成 true 未完成
      page: 1, //页码
      limit: 20 //数量
    };
  },
  mounted: function() {
    this.getBargainUserList();
    this.$scroll(this.$refs.container, () => {
      !this.loadingList && this.getBargainUserList();
    });
  },
  methods: {
    goDetail: function(id) {
      this.$router.push({
        path: "/activity/dargain_detail/" + id + "/0"
      });
    },
    goList: function() {
      this.$router.push({
        path: "/activity/bargain"
      });
    },
    getBargainUserList: function() {
      var that = this;
      if (that.loadingList) return;
      if (that.status) return;
      getBargainUserList({ page: that.page, limit: that.limit })
        .then(res => {
          that.status = res.data.length < that.limit;
          that.bargain.push.apply(that.bargain, res.data);
          that.page++;
          that.loadingList = false;
        })
        .catch(res => {
          that.$dialog.error(res.msg);
        });
    },
    getBargainUserCancel: function(bargainId) {
      var that = this;
      getBargainUserCancel({ bargainId: bargainId })
        .then(res => {
          that.$dialog.success(res.msg).then(() => {
            that.status = false;
            that.loadingList = false;
            that.page = 1;
            that.bargain = [];
            that.getBargainUserList();
          });
        })
        .catch(res => {
          that.$dialog.error(res.msg);
        });
    }
  }
};
</script>
