<template>
  <div class="quality-recommend">
    <div class="title acea-row row-center-wrapper">
      <div class="line"></div>
      <div class="name">
        <span class="iconfont icon-cuxiaoguanli"></span>促销单品
      </div>
      <div class="line"></div>
    </div>
    <Promotion-good :benefit="goodsList"></Promotion-good>
  </div>
</template>
<script>
import "@assets/css/swiper.min.css";
import PromotionGood from "@components/PromotionGood";
import { getGroomList } from "@api/store";
export default {
  name: "GoodsPromotion",
  components: {
    PromotionGood
  },
  props: {},
  data: function() {
    return {
      imgUrls: [],
      goodsList: []
    };
  },
  mounted: function() {
    this.getIndexGroomList();
  },
  methods: {
    getIndexGroomList: function() {
      let that = this;
      getGroomList(4)
        .then(res => {
          that.imgUrls = res.data.banner;
          that.goodsList = res.data.list;
        })
        .catch(function(res) {
          this.$dialog.toast({ mes: res.msg });
        });
    }
  }
};
</script>
