<template>
  <div class="group-list" ref="container">
    <div class="header"></div>
    <div class="list">
      <div
        class="item acea-row row-between-wrapper"
        v-for="(item, index) in combinationList"
        :key="index"
        @click="link(item.id)"
      >
        <div class="pictrue"><img :src="item.image" /></div>
        <div class="text">
          <div class="line1" v-text="item.title"></div>
          <div class="acea-row">
            <div class="team acea-row row-middle cart-color">
              <div class="iconfont icon-pintuan"></div>
              <div class="num" v-text="item.people + '人团'"></div>
            </div>
          </div>
          <div class="bottom acea-row row-between-wrapper">
            <div class="money">
              ￥<span class="num" v-text="item.price"></span
              ><span class="y-money" v-text="'￥' + item.productPrice"></span>
            </div>
            <div class="groupBnt bg-color-red">
              去拼团<span class="iconfont icon-jiantou"></span>
            </div>
          </div>
        </div>
      </div>
      <Loading :loaded="status" :loading="loadingList"></Loading>
    </div>
  </div>
</template>
<script>
import { getCombinationList } from "@api/activity";
import Loading from "@components/Loading";

export default {
  name: "GoodsGroup",
  components: {
    Loading
  },
  props: {},
  data: function() {
    return {
      combinationList: [],
      status: false,
      loading: false,
      page: 1, //页码
      limit: 20, //数量
      loadingList: false
    };
  },
  mounted: function() {
    document
      .querySelector("body")
      .setAttribute("style", "background-color:#00c17b");
    this.getCombinationList();
    this.$scroll(this.$refs.container, () => {
      !this.loadingList && this.getCombinationList();
    });
  },
  methods: {
    getCombinationList: function() {
      var that = this;
      if (that.loading) return;
      if (that.status) return;
      getCombinationList({ page: that.page, limit: that.limit }).then(res => {
        that.status = res.data.length < that.limit;
        that.combinationList.push.apply(that.combinationList, res.data);
        that.page++;
        that.loading = false;
      });
    },
    link: function(id) {
      this.$router.push({
        path: "/activity/group_detail/" + id
      });
    }
  }
};
</script>
