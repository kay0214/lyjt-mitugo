<template>
  <div class="evaluate-list" ref="container">
    <div class="header">
      <div class="generalComment acea-row row-between-wrapper">
        <div class="acea-row row-middle font-color-red">
          <div class="evaluate">评分</div>
          <div class="start" :class="'star' + replyData.replyStar"></div>
        </div>
        <div>
          <span class="font-color-red">{{ replyData.replyChance || 0 }}%</span
          >好评率
        </div>
      </div>
      <div class="nav acea-row row-middle">
        <div
          class="acea-row row-center-wrapper"
          v-for="(item, index) in navList"
          :key="index"
          @click="changeType(index)"
        >
          <div
            class="item"
            :class="currentActive === index ? 'bg-color-red' : ''"
            v-if="item.num"
          >
            {{ item.evaluate }}({{ item.num }})
          </div>
        </div>
      </div>
    </div>
    <UserEvaluation :reply="reply"></UserEvaluation>
    <Loading :loaded="loadend" :loading="loading"></Loading>
  </div>
</template>
<script>
import UserEvaluation from "@components/UserEvaluation";
import { getReplyConfig, getReplyList } from "@api/store";
import Loading from "@components/Loading";
let NAME = "EvaluateList";

export default {
  name: "EvaluateList",
  components: {
    UserEvaluation,
    Loading
  },
  props: {},
  data: function() {
    return {
      product_id: 0,
      replyData: {},
      navList: [
        { evaluate: "全部", num: 0 },
        { evaluate: "好评", num: 0 },
        { evaluate: "中评", num: 0 },
        { evaluate: "差评", num: 0 }
      ],
      currentActive: 0,
      page: 1,
      limit: 8,
      reply: [],
      loadTitle: "",
      loading: false,
      loadend: false
    };
  },
  watch: {
    $route(n) {
      if (n.name === NAME) {
        this.product_id = this.$route.params.id;
        this.loadend = false;
        this.page = 1;
        this.$set(this, "reply", []);
        this.getProductReplyCount();
        this.getProductReplyList();
      }
    }
  },
  mounted: function() {
    this.product_id = this.$route.params.id;
    console.log('aa:'+this.product_id)
    this.getProductReplyCount();
    this.getProductReplyList();
    this.$scroll(this.$refs.container, () => {
      !this.loading && this.getProductReplyList();
    });
  },
  methods: {
    getProductReplyCount: function() {
      let that = this;
      getReplyConfig(that.product_id).then(res => {
        that.$set(that, "replyData", res.data);
        that.navList[0].num = res.data.sumCount;
        that.navList[1].num = res.data.goodCount;
        that.navList[2].num = res.data.inCount;
        that.navList[3].num = res.data.poorCount;
      });
    },
    getProductReplyList: function() {
      let that = this;
      if (that.loading) return;
      if (that.loadend) return;
      that.loading = true;
      let q = { page: that.page, limit: that.limit, type: that.currentActive };
      getReplyList(that.product_id, q).then(res => {
        that.loading = false;
        that.reply.push.apply(that.reply, res.data);
        that.loadend = res.data.length < that.limit; //判断所有数据是否加载完成；
        that.page = that.page + 1;
      });
    },
    changeType: function(index) {
      let that = this;
      that.currentActive = index;
      that.page = 1;
      that.loadend = false;
      that.$set(that, "reply", []);
      that.getProductReplyList();
    }
  }
};
</script>
<style scoped>
.noCommodity {
  height: 8rem;
  background-color: #fff;
}
</style>
