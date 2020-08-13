<template>
  <div class="newsList" ref="container">
    <div class="list" v-for="(item, index) in articleList" :key="index">
      <router-link
        :to="{ path: '/news_detail/' + item.id }"
        class="item acea-row row-between-wrapper"
      >
        <div class="text acea-row row-column-between">
          <div class="name line2">{{ item.title }}</div>
          <div>{{ item.addTime }}</div>
        </div>
        <div class="pictrue"><img :src="item.imageInput" /></div>
      </router-link>
    </div>

    <!--暂无新闻-->
    <div class="noCommodity" v-if="articleList.length === 0 && page > 1">
      <div class="noPictrue">
        <img src="@assets/images/noNews.png" class="image" />
      </div>
    </div>
  </div>
</template>
<script>
import "@assets/css/swiper.min.css";
import { getArticleList } from "@api/public";

export default {
  name: "NewsList",
  components: {},
  props: {},
  data: function() {
    return {
      page: 1,
      limit: 20,
      loadTitle: "",
      loading: false,
      loadend: false,
      imgUrls: [],
      navLsit: [],
      articleList: [],
      active: 0,
      cid: 0,
      swiperNew: {
        pagination: {
          el: ".swiper-pagination",
          clickable: true
        },
        autoplay: {
          disableOnInteraction: false,
          delay: 2000
        },
        loop: true,
        speed: 1000,
        observer: true,
        observeParents: true
      }
    };
  },
  mounted: function() {
    this.getArticleLists();
  },
  methods: {
    getArticleLists: function() {
      let that = this;
      if (that.loading) return;
      if (that.loadend) return;
      that.loading = true;
      let q = {
        page: that.page,
        limit: that.limit
      };
      getArticleList(q).then(res => {
        that.loading = false;
        console.log(res.data);
        that.articleList.push.apply(that.articleList, res.data);
        that.loadend = res.data.length < that.limit;
        that.page = that.page + 1;
      });
    }
  }
};
</script>
