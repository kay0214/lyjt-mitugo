<template>
  <div>
    <div class="searchGood">
      <div class="search acea-row row-between-wrapper">
        <div class="input acea-row row-between-wrapper">
          <span class="iconfont icon-sousuo2"></span>
          <form @submit.prevent="submit"></form>
          <input type="text" placeholder="点击搜索商品" v-model="search" />
        </div>
        <div class="bnt" @click="submit">搜索</div>
      </div>
      <div v-if="keywords.length">
        <div class="title">热门搜索</div>
        <div class="list acea-row">
          <div
            class="item"
            v-for="key of keywords"
            :key="key"
            @click="toSearch(key)"
          >
            {{ key }}
          </div>
        </div>
      </div>
      <div class="line"></div>
    </div>
  </div>
</template>
<script>
import { getSearchKeyword } from "@api/store";
import { trim } from "@utils";

export default {
  name: "GoodSearch",
  props: {},
  data: function() {
    return {
      keywords: [],
      search: ""
    };
  },
  mounted: function() {
    this.getData();
  },
  methods: {
    submit() {
      const search = trim(this.search) || "";
      if (!search) return;
      this.toSearch(search);
    },
    toSearch(s) {
      this.$router.push({ path: "/goods_list", query: { s } });
    },
    getData() {
      getSearchKeyword().then(res => {
        this.keywords = res.data;
      });
    }
  }
};
</script>
<style scoped>
.noCommodity {
  border-top: 0.05rem solid #f5f5f5;
}
</style>
